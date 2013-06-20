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
package org.apache.http2.impl.pool;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.http2.HttpClientConnection;
import org.apache.http2.HttpHost;
import org.apache.http2.annotation.ThreadSafe;
import org.apache.http2.params.HttpParams;
import org.apache.http2.pool.AbstractConnPool;
import org.apache.http2.pool.ConnFactory;
import org.apache.http2.pool.ConnPool;

/**
 * A very basic {@link ConnPool} implementation that represents a pool
 * of blocking {@link HttpClientConnection} connections identified by
 * an {@link HttpHost} instance. Please note this pool implementation
 * does not support complex routes via a proxy cannot differentiate between
 * direct and proxied connections.
 * <p/>
 * The following parameters can be used to customize the behavior of this
 * class:
 * <ul>
 *  <li>{@link org.apache.http2.params.CoreProtocolPNames#HTTP_ELEMENT_CHARSET}</li>
 *  <li>{@link org.apache.http2.params.CoreConnectionPNames#TCP_NODELAY}</li>
 *  <li>{@link org.apache.http2.params.CoreConnectionPNames#SO_TIMEOUT}</li>
 *  <li>{@link org.apache.http2.params.CoreConnectionPNames#SO_LINGER}</li>
 *  <li>{@link org.apache.http2.params.CoreConnectionPNames#SOCKET_BUFFER_SIZE}</li>
 *  <li>{@link org.apache.http2.params.CoreConnectionPNames#MAX_LINE_LENGTH}</li>
 * </ul>
 *
 * @see HttpHost
 * @since 4.2
 */
@ThreadSafe
public class BasicConnPool extends AbstractConnPool<HttpHost, HttpClientConnection, BasicPoolEntry> {

    private static AtomicLong COUNTER = new AtomicLong();

    public BasicConnPool(final ConnFactory<HttpHost, HttpClientConnection> connFactory) {
        super(connFactory, 2, 20);
    }

    public BasicConnPool(final HttpParams params) {
        super(new BasicConnFactory(params), 2, 20);
    }

    @Override
    protected BasicPoolEntry createEntry(
            final HttpHost host,
            final HttpClientConnection conn) {
        return new BasicPoolEntry(Long.toString(COUNTER.getAndIncrement()), host, conn);
    }

}
