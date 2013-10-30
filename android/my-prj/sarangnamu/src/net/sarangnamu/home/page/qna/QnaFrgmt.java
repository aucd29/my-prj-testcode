/*
 * QnaFrgmt.java
 * Copyright 2013 Burke.Choi All rights reserved.
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
package net.sarangnamu.home.page.qna;

import net.sarangnamu.home.R;
import net.sarangnamu.home.page.PageBaseFrgmt;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class QnaFrgmt extends PageBaseFrgmt {
    private WebView web;

    @Override
    protected void initLayout() {
        super.initLayout();

        web = (WebView) view.findViewById(R.id.web);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        web.loadUrl("https://groups.google.com/forum/m/?fromgroups#!forum/aucd29");
        //        web.loadUrl("http://m.daum.net");
    }

    @Override
    public boolean onBackPressed() {
        if (web.canGoBack()) {
            web.goBack();

            return true;
        }

        return false;
    }
}

