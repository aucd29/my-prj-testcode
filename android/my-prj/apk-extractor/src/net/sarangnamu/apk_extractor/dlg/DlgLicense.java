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
package net.sarangnamu.apk_extractor.dlg;

import net.sarangnamu.apk_extractor.R;
import net.sarangnamu.common.ui.dlg.DlgBase;
import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DlgLicense extends DlgBase {
    private WebView web;

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
        
        web = (WebView) findViewById(R.id.web);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        web.loadUrl("file:///android_asset/license.html");
    }
}
