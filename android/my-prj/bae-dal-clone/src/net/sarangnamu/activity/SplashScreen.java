/* 
 * Copyright (c) 2003-2011, cheol-dong choi, twitter @aucd29
 * 
 * Permission is hereby granted, free of charge, to any person 
 * obtaining a copy of this software and associated documentation 
 * files (the "Software"), to deal in the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following 
 * conditions: 
 * 
 * The above copyright notice and this permission notice shall be 
 * included in all copies or substantial portions of the Software. 
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR 
 * OTHER DEALINGS IN THE SOFTWARE. 
 *  
 */

package net.sarangnamu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;
import net.sarangnamu.activity.SplashScreen;

public abstract class SplashScreen extends Activity {
	protected int splashMinTime = 2000;
	protected String intentName = "";
	protected Thread splashThread = null;
	protected Intent targetIntent = null;
	protected long startTime, endTime;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        splashThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					long startTime = System.currentTimeMillis ();					
										
					work();
					
					long endTime = System.currentTimeMillis ();
					startTime += splashMinTime;
                	                	
                	if (startTime > endTime) {
                		Thread.sleep(startTime - endTime);
                	}
                	
                	Thread.sleep(1);
                	if (targetIntent == null) {
                		throw new Exception("target Intent == null");
                	}
                	
                	startActivity(targetIntent);
                	finish();
				} catch (Exception e) {
					e.printStackTrace();
					finish();
				}
			}
        });
        
        splashThread.start();
    }
    
    public void setSplashMinTime(int time) {
    	splashMinTime = time;
    }
        
    public void setTargetIntent(Intent intent) {
    	targetIntent = intent;
    }
    
    @Override
	public void onBackPressed() {
    	splashThread.interrupt();
		super.onBackPressed();
	}

	abstract public void work() throws Exception;
}