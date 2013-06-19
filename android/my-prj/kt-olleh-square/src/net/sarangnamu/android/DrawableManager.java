
package net.sarangnamu.android;

/*
 Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

<a href="http://www.apache.org/licenses/LICENSE-2.0" target="_blank" class="HyperLink">http://www.apache.org/licenses/LICENSE-2.0</a>

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class DrawableManager {

    static {
    	_instance = new DrawableManager();
    }


    /**
     * 내가 만든게 아니라서 주석을 적긴 머하지만 대충 클래스에 목적을 적자면은
	 * 사용자가 원하는 image uri 를 요청하면 이를 다운한뒤 uri 를 map 에 key
	 * 값으로 하여 실 image 데이터를 보관하게 된다. 이 후 데이터를 요청하면
	 * map 의 key에 해당하는 데이터가 이미 존재하면 memory 에 있는 데이터를
	 * 전달하게 된다. 원래는 이 클래스는 싱글톤은 아니였으나 싱글톤으로 변경
	 * 하여 사용하고 있다.
     */
    public static DrawableManager getInstance() {
    	return _instance;
    }

    private DrawableManager() {
        drawableMap = new HashMap<String, Drawable>();
    }

    public Drawable fetchDrawable(String urlString) {
        if (drawableMap.containsKey(urlString)) {
            return drawableMap.get(urlString);
        }

        Log.d(this.getClass().getSimpleName(), "image url:" + urlString);
        try {
            InputStream is = fetch(urlString);
            Drawable drawable = null;

            drawable = Drawable.createFromStream(is, "src");

            if (drawable != null) {
            	drawableMap.put(urlString, drawable);
            	Log.d(this.getClass().getSimpleName(), "got a thumbnail drawable: " + drawable.getBounds() + ", "
                    + drawable.getIntrinsicHeight() + "," + drawable.getIntrinsicWidth() + ", "
                    + drawable.getMinimumHeight() + "," + drawable.getMinimumWidth());
            }
            return drawable;
        } catch (MalformedURLException e) {
            Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
            return null;
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
            return null;
        }
    }

    public void fetchDrawableOnThread(final String urlString, final ImageView imageView) {
        if (drawableMap.containsKey(urlString)) {
            imageView.setImageDrawable(drawableMap.get(urlString));
        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                imageView.setImageDrawable((Drawable) message.obj);
            }
        };

        Thread thread = new Thread() {
            @Override
            public void run() {
                //TODO : set imageView to a "pending" image
                Drawable drawable = fetchDrawable(urlString);
                Message message = handler.obtainMessage(1, drawable);
                handler.sendMessage(message);
            }
        };
        thread.start();
    }

    private InputStream fetch(String urlString) throws MalformedURLException, IOException {
    	URL url = new URL(urlString);
		return (InputStream)url.getContent();
    }


	private static DrawableManager _instance;

    private Map<String, Drawable> drawableMap;		///< cache
}
