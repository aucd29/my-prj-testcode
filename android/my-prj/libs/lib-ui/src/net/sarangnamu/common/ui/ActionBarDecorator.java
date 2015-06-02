/*
 * ActionBarEx.java
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
package net.sarangnamu.common.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.view.View;
/**
 * <pre>
 * {@code
    ActionBarDecorator actionBar;
    actionBar = new ActionBarDecorator(this);
    actionBar.init(R.layout.actionbar);
 * }
 * </pre>
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class ActionBarDecorator {
    protected int mDisplayOpt = ActionBar.DISPLAY_SHOW_CUSTOM;
    protected Activity mActivity;
    protected ActionBar mActionBar;
    protected ActionBar.LayoutParams mParams;
    protected View mView;

    public ActionBarDecorator(Activity activity) {
        this.mActivity = activity;
        mParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
    }

    public void init(final int resId) {
        mView = mActivity.getLayoutInflater().inflate(resId, null);

        mActionBar = mActivity.getActionBar();

        if (mActionBar == null) {
            return;
        }

        mActionBar.setCustomView(mView, mParams);
        mActionBar.setDisplayOptions(mDisplayOpt);
    }

    public void setDisplayOption(final int opt) {
        mDisplayOpt = opt;
    }

    public void setLayoutParams(ActionBar.LayoutParams params) {
        this.mParams = params;
    }

    public View getView() {
        return mView;
    }

    public View getChildView(int id) {
        return mView.findViewById(id);
    }
}
