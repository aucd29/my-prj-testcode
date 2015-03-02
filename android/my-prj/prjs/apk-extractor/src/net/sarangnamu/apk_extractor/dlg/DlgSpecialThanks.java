/*
 * DlgSpecialThanks.java
 * Copyright 2015 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.apk_extractor.dlg;

import net.sarangnamu.common.ui.R;
import net.sarangnamu.common.ui.dlg.DlgBase;
import android.content.Context;
import android.graphics.Typeface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class DlgSpecialThanks extends DlgBase {
    private WebView web;
    private TextView title;
    private Typeface tf;
    private String thanksUrl = "file:///android_asset/speical_thanks.html";
    private String titleMsg;

    public DlgSpecialThanks(Context context) {
        super(context);
    }

    @Override
    protected int getBaseLayoutId() {
        return R.layout.dlg_license;
    }

    @Override
    protected void initLayout() {
        setFullscreen();

        web   = (WebView) findViewById(R.id.web);
        title = (TextView) findViewById(R.id.title);

        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        web.loadUrl(thanksUrl);

        if (tf != null && title != null) {
            title.setTypeface(tf);
        }

        title.setText(titleMsg);
    }

    @Override
    public void setTitle(CharSequence title) {
        titleMsg = title.toString();
    }

    public void setTitleTypeface(Typeface tf) {
        this.tf = tf;
    }

    public void setLicenseFilePath(final String path) {
        thanksUrl = path;
    }
}
