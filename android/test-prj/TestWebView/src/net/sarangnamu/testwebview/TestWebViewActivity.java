package net.sarangnamu.testwebview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TestWebViewActivity extends Activity {
    private static final String TAG = "TestWebViewActivity";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView v = new WebView(this);
        v.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                int pos = url.indexOf('?');
                String domain = url.substring(0, pos);
                String params = url.substring(pos + 1, url.length());

                String params.

                Log.d(TAG, "===================================================================");
                Log.d(TAG, "params " + params);
                Log.d(TAG, "===================================================================");



                view.loadUrl(url);
                return false;
            }
        });

        v.getSettings().setJavaScriptEnabled(true);
        v.getSettings().setPluginState(WebSettings.PluginState.ON);
        v.getSettings().setBuiltInZoomControls(false);
        v.getSettings().setSupportZoom(false);
        v.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        int screenDensity = metrics.densityDpi;

        if (screenDensity >= 240) {
            v.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (screenDensity == 160) {
            v.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (screenDensity == 120) {
            v.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }

        v.getSettings().setLoadWithOverviewMode(false);
        v.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        Display dis = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        float disWidth = dis.getWidth();
        float scale = 100 * (1 / (480 / disWidth));

        v.setInitialScale((int)scale);


        setContentView(v);

        v.loadUrl("http://daum.net?value=dfdf.net");
    }

}