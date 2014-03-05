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
 * {@code
 * <pre>

 * </pre>}
 * 
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class DlgTimer extends DlgBtnBase {
    private int time = 1000, layoutId;
    private String value;
    private Spanned html;
    protected TextView msg;

    public DlgTimer(Context context, int layoutId) {
        super(context);

        this.layoutId = layoutId;
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        hideTitle();
        hideButtons();

        View view = inflate(layoutId);
        content.addView(view);

        int msgId = view.getResources().getIdentifier("msg", "id", getContext().getPackageName());
        msg = (TextView) view.findViewById(msgId);
        msg.setText(html == null ? value : html);
        msg.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, time);
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setMessage(String msg) {
        this.value = msg;
    }

    public void setMessage(int resid) {
        this.value = getString(resid);
    }

    public void setMessage(Spanned msg) {
        html = msg;
    }
}
