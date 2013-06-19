package net.sarangnamu.groupby;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class WebBaseChromeClient extends WebChromeClient {
    Context mContext = null;

    public WebBaseChromeClient(Context ctx) {
        super();
        mContext = ctx;
    }

    @Override
    public boolean onJsAlert( WebView view, String url, String message, final JsResult result ) {
        Context context = mContext;

        if( context == null ) {
            return false;
        }

        new AlertDialog.Builder( context )
        .setTitle("Alert")
        .setMessage(message)
        .setPositiveButton("OK", new AlertDialog.OnClickListener() {
            @Override
            public void onClick( DialogInterface dialog, int which) {
                result.confirm();
            }
        })
        .setCancelable(false)
        .show();

        return true;
    }
}
