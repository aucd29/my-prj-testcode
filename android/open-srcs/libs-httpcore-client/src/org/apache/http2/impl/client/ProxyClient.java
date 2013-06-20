/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http2.impl.client;

import java.io.IOException;
import java.net.Socket;

import javax.net.ssl.SSLSession;

import org.apache.http2.ConnectionReuseStrategy;
import org.apache.http2.HttpEntity;
import org.apache.http2.HttpException;
import org.apache.http2.HttpHost;
import org.apache.http2.HttpRequest;
import org.apache.http2.HttpRequestInterceptor;
import org.apache.http2.HttpResponse;
import org.apache.http2.ProtocolVersion;
import org.apache.http2.auth.AuthSchemeRegistry;
import org.apache.http2.auth.AuthScope;
import org.apache.http2.auth.AuthState;
import org.apache.http2.auth.Credentials;
import org.apache.http2.client.params.AuthPolicy;
import org.apache.http2.client.params.HttpClientParams;
import org.apache.http2.client.protocol.ClientContext;
import org.apache.http2.client.protocol.RequestClientConnControl;
import org.apache.http2.client.protocol.RequestProxyAuthentication;
import org.apache.http2.conn.HttpRoutedConnection;
import org.apache.http2.conn.routing.HttpRoute;
import org.apache.http2.entity.BufferedHttpEntity;
import org.apache.http2.impl.DefaultConnectionReuseStrategy;
import org.apache.http2.impl.DefaultHttpClientConnection;
import org.apache.http2.impl.auth.BasicSchemeFactory;
import org.apache.http2.impl.auth.DigestSchemeFactory;
import org.apache.http2.impl.auth.NTLMSchemeFactory;
import org.apache.http2.message.BasicHttpRequest;
import org.apache.http2.params.BasicHttpParams;
import org.apache.http2.params.HttpParams;
import org.apache.http2.params.HttpProtocolParams;
import org.apache.http2.protocol.BasicHttpContext;
import org.apache.http2.protocol.ExecutionContext;
import org.apache.http2.protocol.HttpContext;
import org.apache.http2.protocol.HttpProcessor;
import org.apache.http2.protocol.HttpRequestExecutor;
import org.apache.http2.protocol.ImmutableHttpProcessor;
import org.apache.http2.protocol.RequestContent;
import org.apache.http2.protocol.RequestTargetHost;
import org.apache.http2.protocol.RequestUserAgent;
import org.apache.http2.util.EntityUtils;

public class ProxyClient {

    private final HttpProcessor httpProcessor;
    private final HttpRequestExecutor requestExec;
    private final ProxyAuthenticationStrategy proxyAuthStrategy;
    private final HttpAuthenticator authenticator;
    private final AuthState proxyAuthState;
    private final AuthSchemeRegistry authSchemeRegistry;
    private final ConnectionReuseStrategy reuseStrategy;
    private final HttpParams params;

    public ProxyClient(final HttpParams params) {
        super();
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        this.httpProcessor = new ImmutableHttpProcessor(new HttpRequestInterceptor[] {
                new RequestContent(),
                new RequestTargetHost(),
                new RequestClientConnControl(),
                new RequestUserAgent(),
                new RequestProxyAuthentication()
        } );
        this.requestExec = new HttpRequestExecutor();
        this.proxyAuthStrategy = new ProxyAuthenticationStrategy();
        this.authenticator = new HttpAuthenticator();
        this.proxyAuthState = new AuthState();
        this.authSchemeRegistry = new AuthSchemeRegistry();
        this.authSchemeRegistry.register(AuthPolicy.BASIC, new BasicSchemeFactory());
        this.authSchemeRegistry.register(AuthPolicy.DIGEST, new DigestSchemeFactory());
        this.authSchemeRegistry.register(AuthPolicy.NTLM, new NTLMSchemeFactory());
        //        this.authSchemeRegistry.register(AuthPolicy.SPNEGO, new SPNegoSchemeFactory());
        //        this.authSchemeRegistry.register(AuthPolicy.KERBEROS, new KerberosSchemeFactory());
        this.reuseStrategy = new DefaultConnectionReuseStrategy();
        this.params = params;
    }

    public ProxyClient() {
        this(new BasicHttpParams());
    }

    public HttpParams getParams() {
        return this.params;
    }

    public AuthSchemeRegistry getAuthSchemeRegistry() {
        return this.authSchemeRegistry;
    }

    public Socket tunnel(
            final HttpHost proxy,
            final HttpHost target,
            final Credentials credentials) throws IOException, HttpException {
        ProxyConnection conn = new ProxyConnection(new HttpRoute(proxy));
        HttpContext context = new BasicHttpContext();
        HttpResponse response = null;

        for (;;) {
            if (!conn.isOpen()) {
                Socket socket = new Socket(proxy.getHostName(), proxy.getPort());
                conn.bind(socket, this.params);
            }
            String host = target.getHostName();
            int port = target.getPort();
            if (port < 0) {
                port = 80;
            }

            StringBuilder buffer = new StringBuilder(host.length() + 6);
            buffer.append(host);
            buffer.append(':');
            buffer.append(Integer.toString(port));

            String authority = buffer.toString();
            ProtocolVersion ver = HttpProtocolParams.getVersion(this.params);
            HttpRequest connect = new BasicHttpRequest("CONNECT", authority, ver);
            connect.setParams(this.params);

            BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(new AuthScope(proxy), credentials);

            // Populate the execution context
            context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, target);
            context.setAttribute(ExecutionContext.HTTP_PROXY_HOST, proxy);
            context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
            context.setAttribute(ExecutionContext.HTTP_REQUEST, connect);
            context.setAttribute(ClientContext.PROXY_AUTH_STATE, this.proxyAuthState);
            context.setAttribute(ClientContext.CREDS_PROVIDER, credsProvider);
            context.setAttribute(ClientContext.AUTHSCHEME_REGISTRY, this.authSchemeRegistry);

            this.requestExec.preProcess(connect, this.httpProcessor, context);

            response = this.requestExec.execute(connect, conn, context);

            response.setParams(this.params);
            this.requestExec.postProcess(response, this.httpProcessor, context);

            int status = response.getStatusLine().getStatusCode();
            if (status < 200) {
                throw new HttpException("Unexpected response to CONNECT request: " +
                        response.getStatusLine());
            }

            if (HttpClientParams.isAuthenticating(this.params)) {
                if (this.authenticator.isAuthenticationRequested(proxy, response,
                        this.proxyAuthStrategy, this.proxyAuthState, context)) {
                    if (this.authenticator.authenticate(proxy, response,
                            this.proxyAuthStrategy, this.proxyAuthState, context)) {
                        // Retry request
                        if (this.reuseStrategy.keepAlive(response, context)) {
                            // Consume response content
                            HttpEntity entity = response.getEntity();
                            EntityUtils.consume(entity);
                        } else {
                            conn.close();
                        }
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        int status = response.getStatusLine().getStatusCode();

        if (status > 299) {

            // Buffer response content
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                response.setEntity(new BufferedHttpEntity(entity));
            }

            conn.close();
            throw new TunnelRefusedException("CONNECT refused by proxy: " +
                    response.getStatusLine(), response);
        }
        return conn.getSocket();
    }

    static class ProxyConnection extends DefaultHttpClientConnection implements HttpRoutedConnection {

        private final HttpRoute route;

        ProxyConnection(final HttpRoute route) {
            super();
            this.route = route;
        }

        @Override
        public HttpRoute getRoute() {
            return this.route;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public SSLSession getSSLSession() {
            return null;
        }

        @Override
        public Socket getSocket() {
            return super.getSocket();
        }

    }

}
