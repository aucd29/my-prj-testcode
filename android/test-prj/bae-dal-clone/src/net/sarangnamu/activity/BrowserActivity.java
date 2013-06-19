package net.sarangnamu.activity;

import net.sarangnamu.baedal.R;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowserActivity extends Activity {
	public static final String URI = "uri";
	private WebView web;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.webview);
		
		try {
			Bundle bundle = getIntent().getExtras();			
			if (bundle == null) {
				throw new Exception();
			}
			
			String uri = bundle.getString(URI);
			web = (WebView) findViewById(R.id.webView);
			web.getSettings().setJavaScriptEnabled(true);
			web.clearCache(true);
//			web.setWebViewClient(new MyWebViewClient());			
			web.loadUrl(uri);
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
}
