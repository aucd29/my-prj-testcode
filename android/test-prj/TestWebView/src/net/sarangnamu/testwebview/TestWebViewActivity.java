package net.sarangnamu.testwebview;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TestWebViewActivity extends Activity {
    private static final String TAG = "TestWebViewActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView v = new WebView(this);
        v.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return false;
            }
        });

        v.getSettings().setJavaScriptEnabled(true);
        v.getSettings().setPluginState(WebSettings.PluginState.ON);
        //v.getSettings().setBuiltInZoomControls(true);
        v.getSettings().setBuiltInZoomControls(false);
        v.getSettings().setSupportZoom(false);
        v.getSettings().setSaveFormData(true);
        v.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        v.getSettings().setLoadWithOverviewMode(false);
        //v.getSettings().setDatabaseEnabled(true);
        v.getSettings().setDomStorageEnabled(true);



        setContentView(v);

        v.loadUrl("http://m.news.naver.com/");
    }

}