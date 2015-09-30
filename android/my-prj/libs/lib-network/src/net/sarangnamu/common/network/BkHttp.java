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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

/**
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class BkHttp {
	private static final String TAG = "BkHttp";

    private String method = "POST";
//    protected DefaultHttpClient mHttp;
    private int mConnTimeout = 3000, mReadTimeout = 5000;

    public BkHttp() {
//        initHttp();
    }

    protected void initHttp() {
//        if (mHttp == null) {
//            mHttp = new DefaultHttpClient();
//            timeout();
//        }
//    	if (mConn == null) {
//
//    	}
    }

    private void timeout() {
//        HttpParams httpParameters = new BasicHttpParams();
//
//        int timeoutConnection = 3000;
//        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
//
//        int timeoutSocket = 5000;
//        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
//
//        mHttp.setParams(httpParameters);
    }

    public void setTimeout(int connTimeout, int socketTimeout) {
        mConnTimeout = connTimeout;
        mReadTimeout = socketTimeout;
//        if (mHttp == null) {
//            return ;
//        }
//
//        HttpParams httpParameters = new BasicHttpParams();
//        HttpConnectionParams.setConnectionTimeout(httpParameters, connTimeout);
//        HttpConnectionParams.setSoTimeout(httpParameters, socketTimeout);
//
//        mHttp.setParams(httpParameters);
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    public String submit(final String getUrl, final Map<String, String> parameters) throws Exception {
        URL url = new URL(getUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setConnectTimeout(mConnTimeout);
        conn.setReadTimeout(mReadTimeout);
        conn.setRequestMethod(method);

        String params = "";
        if (parameters != null) {
            ParameterEncoder pe = new ParameterEncoder();

            for (String key : parameters.keySet()) {
                pe.add(key, parameters.get(key));
            }

            params = "?" + pe.encode();
        }

        OutputStream os = conn.getOutputStream();
        os.write(params.getBytes("UTF-8"));
        os.flush();
        os.close();

        int responseCode = conn.getResponseCode();
        Log.i(TAG, "POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                 response.append(inputLine);
            }
            in.close();

            return response.toString();
        }

        return null;
    }

    public JSONObject jsonSubmit(final String getUrl, final Map<String, String> parameters) throws Exception {
        return new JSONObject(submit(getUrl, parameters));
    }

//    public byte[] bytesSubmit(String getUrl, final Map<String, String> parameters) throws Exception {
//        return EntityUtils.toByteArray(entity);
//    }


//    protected HttpEntity methodGet(final String getUrl, final Map<String, String> parameters) throws Exception {
//        initHttp();
//
//        String params = "";
//        if (parameters != null) {
//            ParameterEncoder pe = new ParameterEncoder();
//
//            for (String key : parameters.keySet()) {
//                pe.add(key, parameters.get(key));
//            }
//
//            params = "?" + pe.encode();
//        }
//
//        HttpGet httpGet = new HttpGet(getUrl + params);
//        HttpResponse response = mHttp.execute(httpGet);
//
//        return response.getEntity();
//    }
//
//    protected HttpEntity methodPost(final String getUrl, final Map<String, String> parameters) throws Exception {
//        initHttp();
//
//        HttpPost httpPost = new HttpPost(getUrl);
//        List<NameValuePair> pair = new ArrayList<NameValuePair>();
//
//        for (String key : parameters.keySet()) {
//            pair.add(new BasicNameValuePair(key, parameters.get(key)));
//        }
//
//        httpPost.setEntity(new UrlEncodedFormEntity(pair));
//        HttpResponse response = mHttp.execute(httpPost);
//
//        return response.getEntity();
//    }

//    public List<Cookie> getCookie() {
//        return mHttp.getCookieStore().getCookies();
//    }
}
