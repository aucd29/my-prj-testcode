/*
 * DlgEmail.java
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
package net.sarangnamu.apk_extractor.dlg;

import net.sarangnamu.apk_extractor.R;
import net.sarangnamu.apk_extractor.cfg.Cfg;
import net.sarangnamu.common.fonts.FontLoader;
import net.sarangnamu.common.fonts.RobotoLightTextView;
import net.sarangnamu.common.ui.dlg.DlgBase;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class DlgEmail extends DlgBase {
    private Button btn;
    private EditText edit;
    private LinearLayout layout;

    public DlgEmail(Context context) {
        super(context);
    }

    @Override
    protected void initLayout() {
        btn     = (Button) findViewById(R.id.btn);
        edit    = (EditText) findViewById(R.id.edit);
        layout  = (LinearLayout) findViewById(R.id.list);

        edit.setTypeface(FontLoader.getInstance(getContext()).getFont("Roboto-Light"));
        btn.setTypeface(FontLoader.getInstance(getContext()).getFont("Roboto-Light"));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit.getText().toString();
                if (email == null || email.length() == 0) {
                    return ;
                }

                Cfg.setEmail(getContext(), email);
                putEmail(email);
                edit.setText("");
            }
        });

        final String email = Cfg.getEmail(getContext());
        if (email != null) {
            layout.post(new Runnable() {
                @Override
                public void run() {
                    putEmail(email);
                }
            });
        }
    }

    private void putEmail(String email) {
        layout.removeAllViews();

        RobotoLightTextView tv = new RobotoLightTextView(getContext());
        tv.setText(email);
        tv.setGravity(Gravity.RIGHT);
        tv.setTextColor(0xff929292);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

        layout.addView(tv);
    }

    @Override
    protected int getBaseLayoutId() {
        return R.layout.dlg_email;
    }
}
