/*
 * PageBaseFrgmt.java
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
package net.sarangnamu.home.page;

import net.sarangnamu.common.AutoInflateFrgmtBase;
import net.sarangnamu.common.DLog;
import net.sarangnamu.home.MainActivity;
import net.sarangnamu.home.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class PageBaseFrgmt extends AutoInflateFrgmtBase {
    private static final String TAG = "PageBaseFrgmt";

    protected Button pageWrite;
    protected TextView pageTitle;
    protected ImageView pageRefresh;
    protected ProgressBar pageProgress;

    @Override
    protected int getLayoutId() {
        return R.layout.page_base;
    }

    @Override
    protected int getPageContentId() {
        return R.id.pageContent;
    }

    @Override
    protected void initLayout() {
        pageWrite    = (Button) base.findViewById(R.id.pageWrite);
        pageTitle    = (TextView) base.findViewById(R.id.pageTitle);
        pageRefresh  = (ImageView) base.findViewById(R.id.pageRefresh);
        pageProgress = (ProgressBar) base.findViewById(R.id.pageProgress);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pageTitle.setText(getStringTitleIdentifier());
        pageRefresh.setAlpha(0.4f);
    }

    public boolean onBackPressed() {
        return false;
    }

    protected void showDlgProgress() {
        if (getActivity() == null) {
            return ;
        }

        ((MainActivity) getActivity()).showDlgProgress();
    }

    protected void hideDlgProgress() {
        if (getActivity() == null) {
            return ;
        }

        ((MainActivity) getActivity()).hideDlgProgress();
    }

    protected void showIconProgress() {
        pageProgress.setVisibility(View.VISIBLE);
        pageRefresh.setClickable(false);
    }

    protected void hideIconProgress() {
        pageProgress.setVisibility(View.GONE);
        pageRefresh.setClickable(true);
    }

    protected void showFrgmt(Class<?> cls) {
        if (getActivity() == null) {
            return ;
        }

        Navigator.getInstance(getActivity()).replace(R.id.content, cls);
    }

    public void backFrgmt() {
        if (getActivity() == null) {
            return ;
        }

        Navigator.getInstance(getActivity()).popBack();
    }

    public Fragment getFrgmtByName(Class<?> cls) {
        if (getActivity() == null) {
            return null;
        }

        return Navigator.getInstance(getActivity()).getFragment(cls);
    }

    public void showWriteButton() {
        DLog.e(TAG, "show write button");

        pageWrite.setVisibility(View.VISIBLE);
        pageWrite.invalidate();
    }
}
