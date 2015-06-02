/*
 * DlgLicense.java
 * Copyright 2013 Burke Choi All rights reserved.
 *             http://www.sarangnamu.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sarangnamu.common.ui.dlg;

import net.sarangnamu.common.ui.R;
import android.content.Context;
import android.graphics.Typeface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class DlgLicense extends DlgBase {
    private WebView mWeb;
    private TextView mTitle;
    private Typeface mFont;
    private String mLicenseFilePath = "file:///android_asset/license.html";

    public DlgLicense(Context context) {
        super(context);
    }

    @Override
    protected int getBaseLayoutId() {
        return R.layout.dlg_license;
    }

    @Override
    protected void initLayout() {
        setFullscreen();

        mWeb   = (WebView) findViewById(R.id.web);
        mTitle = (TextView) findViewById(R.id.title);

        mWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWeb.loadUrl(mLicenseFilePath);

        if (mFont != null && mTitle != null) {
            mTitle.setTypeface(mFont);
        }
    }

    public void setTitleTypeface(Typeface tf) {
        this.mFont = tf;
    }

    public void setLicenseFilePath(final String path) {
        mLicenseFilePath = path;
    }
}
