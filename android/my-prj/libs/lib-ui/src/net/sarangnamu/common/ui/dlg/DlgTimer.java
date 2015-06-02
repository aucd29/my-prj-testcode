/*
 * DlgTimer.java
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

import android.content.Context;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

/**
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class DlgTimer extends DlgBtnBase {
    private int mTime = 1000, mLayoutId;
    private String mValue;
    private Spanned mHtml;
    protected TextView mMsg;

    public DlgTimer(Context context, int layoutId) {
        super(context);

        this.mLayoutId = layoutId;
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        hideTitle();
        hideButtons();

        View view = inflate(mLayoutId);
        mContent.addView(view);

        int msgId = view.getResources().getIdentifier("msg", "id", getContext().getPackageName());
        mMsg = (TextView) view.findViewById(msgId);
        mMsg.setText(mHtml == null ? mValue : mHtml);
        mMsg.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, mTime);
    }

    public void setTime(int time) {
        this.mTime = time;
    }

    public void setMessage(String msg) {
        this.mValue = msg;
    }

    public void setMessage(int resid) {
        this.mValue = getString(resid);
    }

    public void setMessage(Spanned msg) {
        mHtml = msg;
    }
}
