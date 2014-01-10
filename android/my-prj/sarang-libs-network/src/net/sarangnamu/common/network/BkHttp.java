/*
 * BkHttp.java
 * Copyright 2013 Burke Choi All rights reserved.
 *             http://www.sarangnamu.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sarangnamu.common.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * {@code
 * <pre>

 * </pre>}
 * 
 * @author <a href="mailto:aucd29@gmail.com.com">Burke Choi</a>
 */
public class BkHttp {
    private String method = "POST";
    protected DefaultHttpClient http;

    public BkHttp() {
        initHttp();
    }

    protected void initHttp() {
        if (http == null) {
            http = new DefaultHttpClient();
        }
    }

    private void timeout() {
        HttpParams httpParameters = new BasicHttpParams();

        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    public String submit(final String getUrl, final Map<String, String> parameters) throws Exception {
        HttpEntity entity = null;

        if (method.equals("GET")) {
            entity = methodGet(getUrl, parameters);
        } else {
            entity = methodPost(getUrl, parameters);
        }

        return EntityUtils.toString(entity);
    }

    public JSONObject jsonSubmit(final String getUrl, final Map<String, String> parameters) throws Exception {
        return new JSONObject(submit(getUrl, parameters));
    }

    public byte[] bytesSubmit(String getUrl, final Map<String, String> parameters) throws Exception {
        HttpEntity entity = null;

        if (method.equals("GET")) {
            entity = methodGet(getUrl, parameters);
        } else {
            entity = methodPost(getUrl, parameters);
        }

        return EntityUtils.toByteArray(entity);
    }

    protected HttpEntity methodGet(final String getUrl, final Map<String, String> parameters) throws Exception {
        initHttp();

        String params = "";
        if (parameters != null) {
            ParameterEncoder pe = new ParameterEncoder();

            for (String key : parameters.keySet()) {
                pe.add(key, parameters.get(key));
            }

            params = "?" + pe.getParameter();
        }

        HttpGet httpGet = new HttpGet(getUrl + params);
        timeout();
        HttpResponse response = http.execute(httpGet);

        return response.getEntity();
    }

    protected HttpEntity methodPost(final String getUrl, final Map<String, String> parameters) throws Exception {
        initHttp();

        HttpPost httpPost = new HttpPost(getUrl);
        timeout();
        List<NameValuePair> pair = new ArrayList<NameValuePair>();

        for (String key : parameters.keySet()) {
            pair.add(new BasicNameValuePair(key, parameters.get(key)));
        }

        httpPost.setEntity(new UrlEncodedFormEntity(pair));
        HttpResponse response = http.execute(httpPost);

        return response.getEntity();
    }

    public List<Cookie> getCookie() {
        return http.getCookieStore().getCookies();
    }
}
