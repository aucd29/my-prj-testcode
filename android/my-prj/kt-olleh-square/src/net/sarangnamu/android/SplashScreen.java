/*
 *	Copyright 2011 cheol-dong, choi, twitter @aucd29
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


import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import net.sarangnamu.android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

public class SplashScreen extends Activity {


	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		// set no title
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// attach
        setContentView(R.layout.splash);

		// thread for displaying the SplashScreen
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {

                	long startTime = System.currentTimeMillis ();
                	String response = ResponseData.getInstance().listData(true);

                	if (response.length() != 0) {

                		// download thumbnail.
                    	int ps = response.indexOf("<ul class=\"list_data\">");
            			if (ps == -1) throw new Exception();

            			int pe = response.indexOf("</ul>", ps);
            			if (pe == -1) throw new Exception();

            			response = response.substring(ps, pe);
            			ps = pe = 0;
            			//Log.d("@response", Integer.toString(response.length()));

                    	DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            			DocumentBuilder builder = builderFactory.newDocumentBuilder();
            			XPath xpath = XPathFactory.newInstance().newXPath();

            			String concert, expression, image;
            			while(true) {
            				ps = response.indexOf("<li>", pe);
            				if (ps == -1) break;

            				pe = response.indexOf("</li>", ps);
            				if (pe == -1) break;
            				pe += 5; // </li> length

            				concert = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + response.substring(ps, pe).trim();

            				// html parsing
            				Document document = builder.parse(new ByteArrayInputStream(concert.getBytes()));

            				expression = "//div [@class='data_img']/a/img/@src";
            				image = ResponseData.getInstance().getBaseURL() + ((String) xpath.evaluate(expression, document, XPathConstants.STRING)).trim();
            				//Log.d("imgURL", image);

            				DrawableManager.getInstance().fetchDrawable(image);
            			}

                    	long endTime = System.currentTimeMillis ();
                    	long gap = (endTime - startTime) / 1000;

                    	//Log.d("splash", "start time ############ (" + Long.toString(startTime) + ") #############");
                    	//Log.d("splash", "end time ############ (" + Long.toString(endTime) + ") #############");
                    	Log.d("splash", "gap time ############ (" + Long.toString(gap) + ") #############");

                    	if (gap > _splashTime) {
	                    	_splashTime -= gap; //

	                    	int waited = 0;
	                        while(_active && (waited < _splashTime)) {
	                            sleep(100);
	                            if(_active) {
	                                waited += 100;
	                            }
	                        }
                    	}

                        startActivity(new Intent("net.sarangnamu.android.ScheduleList"));

                    } else {
                    	Toast.makeText(SplashScreen.this, "Network error", Toast.LENGTH_SHORT).show();
                    }

                } catch(Exception e) {
                	e.printStackTrace();
                } finally {
                    finish();
                    stop();
                }
            }
        };
        splashTread.start();
    }


	/*
	 * Attributes
	 */
	protected boolean _active = true;

	protected int _splashTime = 5000; // time to display the splash screen in ms


}