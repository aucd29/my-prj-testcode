/*
 * DlgNormal.java
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
package net.sarangnamu.common.ui.dlg;

import net.sarangnamu.common.ui.R;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class DlgNormal extends DlgBtnBase {
    protected int layoutId;
    protected String value;
    protected TextView msg;

    public DlgNormal(Context context, int layoutId) {
        super(context);

        this.layoutId = layoutId;
    }

    public DlgNormal(Context context) {
        super(context);

        this.layoutId = R.layout.dlg_normal;
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        initMsg();
    }

    private void initMsg() {
        View view = inflate(layoutId);
        content.addView(view);

        int msgId = view.getResources().getIdentifier("msg", "id", getContext().getPackageName());
        msg = (TextView) view.findViewById(msgId);
        msg.setText(value);
    }

    public void setMessage(String msg) {
        this.value = msg;
    }

    public void setMessage(int resid) {
        this.value = getString(resid);
    }
}
