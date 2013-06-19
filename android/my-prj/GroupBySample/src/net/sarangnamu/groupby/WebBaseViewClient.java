package net.sarangnamu.groupby;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebBaseViewClient extends WebViewClient{
    public static final String TAG = "WebBaseViewClient";

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url){
        view.loadUrl(url);
        return false;
    }
}
