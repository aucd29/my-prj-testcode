/*
 *	Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package net.sarangnamu.android;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import android.content.Context;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public final class Util {


	public static String openURL(String url, String method, Bundle params)
		throws MalformedURLException, IOException {

		final String boundary = "sarangnamu.net_boundry-------------------";
		final String endLine = "\r\n";

		OutputStream os;

		if (method.equals("GET")) {
			url = url + "?" + percentEncode(params);
		}

		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestProperty("User-Agent",
								System.getProperties().getProperty("http.agent") + " sarangnamu.net");

		if (!method.equals("GET")) {
			Bundle dataParams = new Bundle();

			for (String key : params.keySet()) {
				if (params.getByteArray(key) != null) {
					dataParams.putByteArray(key, params.getByteArray(key));
				}
			}

			if (!params.containsKey("method")) {
				params.putString("method", method);
			}

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.connect();

			os = new BufferedOutputStream(conn.getOutputStream());
			os.write(("---" + boundary + endLine).getBytes());
			os.write(percentEncodeBody(params, boundary).getBytes());
			os.write((endLine + "---" + boundary + endLine).getBytes());

			if (!dataParams.isEmpty()) {
				for (String key : dataParams.keySet()) {
					os.write(("Content-Disposition: form-data; filename=\"" + key + "\"" + endLine).getBytes());
                    os.write(("Content-Type: content/unknown" + endLine + endLine).getBytes());
                    os.write(dataParams.getByteArray(key));
                    os.write((endLine + "--" + boundary + endLine).getBytes());
				}
			}

			os.flush();
		}

		String response = "";
        try {
            response = read(conn.getInputStream());
        } catch (FileNotFoundException e) {
            // Error Stream contains JSON that we can parse to a FB error
            response = read(conn.getErrorStream());
        }
        return response;
	}

	private static String read(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

	/*
	 * percent encode
	 */

	public static String percentEncodeBody(Bundle parameters, String boundary) {
		if (parameters == null)
			return "";

			StringBuilder sb = new StringBuilder();

	        for (String key : parameters.keySet()) {
	            if (parameters.getByteArray(key) != null) {
	                continue;
	            }

	            sb.append("Content-Disposition: form-data; name=\"" + key +
	            			"\"\r\n\r\n" + parameters.getString(key));
	            sb.append("\r\n" + "--" + boundary + "\r\n");
	        }

	        return sb.toString();
	}

	public static String percentEncode(Bundle parameters) {
		if (parameters == null) {
	    	return "";
		}

	    StringBuilder sb = new StringBuilder();
	    boolean first = true;
	    for (String key : parameters.keySet()) {
	    	if (first) first = false; else sb.append("&");
	        	sb.append(URLEncoder.encode(key) + "=" +
	                      URLEncoder.encode(parameters.getString(key)));
	        }
	        return sb.toString();
	}

	public static Bundle percentDecode(String s) {
		Bundle params = new Bundle();
		if (s != null) {
	    	String array[] = s.split("&");
	    	for (String parameter : array) {
	    		String v[] = parameter.split("=");
	            params.putString(URLDecoder.decode(v[0]),
	            				URLDecoder.decode(v[1]));
	    	}
	    }
		return params;
	}


    /*
     * cookie
     */

    public static void clearCookies(Context context) {
        // Edge case: an illegal state exception is thrown if an instance of
        // CookieSyncManager has not be created.  CookieSyncManager is normally
        // created by a WebKit view, but this might happen if you start the
        // app, restore saved state, and click logout before running a UI
        // dialog in a WebView -- in which case the app crashes
        @SuppressWarnings("unused")
        CookieSyncManager cookieSyncMngr =
            CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

}
