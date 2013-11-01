/*
 * DlgLogin.java
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
package net.sarangnamu.home.page.dlg;

import net.sarangnamu.common.ui.dlg.DlgBtnBase;
import net.sarangnamu.home.R;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DlgLogin extends DlgBtnBase {
    protected int layoutId;
    protected EditText id, pw;
    protected DlgLoginListener l;

    public DlgLogin(Context context, int layoutId) {
        super(context);

        this.layoutId = layoutId;
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        View view = inflate(layoutId);
        content.addView(view);

        title.setText(R.string.login);
        left.setText(R.string.login);
        right.setText(R.string.cancel);

        id = (EditText) view.findViewById(R.id.id);
        pw = (EditText) view.findViewById(R.id.pw);
    }

    public void setOnLoginListener(DlgLoginListener l) {
        this.l = l;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == left.getId()) {
            String id = this.id.getText().toString();
            String pw = this.pw.getText().toString();

            if (id == null || id.length() == 0) {
                Toast.makeText(getContext(), "Please insert id", Toast.LENGTH_SHORT).show();
                return ;
            }

            if (pw == null || pw.length() == 0) {
                Toast.makeText(getContext(), "Please insert password", Toast.LENGTH_SHORT).show();
                return ;
            }

            if (l != null) {
                l.ok(id, pw);
            }

            dismiss();
        } else {
            dismiss();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    //
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public interface DlgLoginListener {
        public void ok(String id, String pw);
    }
}
