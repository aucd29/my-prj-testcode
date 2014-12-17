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
    private WebView web;
    private TextView title;
    private Typeface tf;
    private String licenseFilePath = "file:///android_asset/license.html";

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

        web   = (WebView) findViewById(R.id.web);
        title = (TextView) findViewById(R.id.title);

        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        web.loadUrl(licenseFilePath);

        if (tf != null && title != null) {
            title.setTypeface(tf);
        }
    }

    public void setTitleTypeface(Typeface tf) {
        this.tf = tf;
    }

    public void setLicenseFilePath(final String path) {
        licenseFilePath = path;
    }
}
