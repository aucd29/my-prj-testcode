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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.GeolocationPermissions.Callback;

public class CustomWebView extends WebView {
	public CustomWebView(Context context) {
		super(context);
		try {
			getSettings().setJavaScriptEnabled(true);
			clearCache(true);
			setWebViewClient(new MyWebViewClient());
			setWebChromeClient(new MyWebChromeClient());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	final class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)  { 
			view.loadUrl(url);
		    return true;
		}
	}
	
	final class MyWebChromeClient extends WebChromeClient {  
        @Override  
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {  
        	
        	final JsResult finalJsResult = result;
            new AlertDialog.Builder(view.getContext())
            	.setIcon(android.R.drawable.ic_dialog_alert)
            	.setMessage(message)
            	.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finalJsResult.confirm();
                }
            }).setCancelable(false).create().show();        	
        	
            return true;
        }

		@Override
		public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
			callback.invoke(origin, true, false);
		}
    }
}
