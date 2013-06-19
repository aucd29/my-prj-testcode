package net.sarangnamu.groupby;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebBaseView extends WebView {
    static final String LOG_TAG = "WebBaseView";

    private Context mContext = null;
    private Bitmap buffer;

    public WebBaseView(Context context) {
        super(context);
        initWebBaseView(context);
    }

    public WebBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebBaseView(context);
    }

    public WebBaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWebBaseView(context);
    }

    private void initWebBaseView(Context context) {
        mContext = context;

        getSettings().setJavaScriptEnabled(true);
        getSettings().setPluginState(WebSettings.PluginState.ON);
        getSettings().setBuiltInZoomControls(false);
        getSettings().setSupportZoom(false);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        this.setWebViewClient(new WebBaseViewClient());
        this.setWebChromeClient(new WebBaseChromeClient(mContext));
    }
}
