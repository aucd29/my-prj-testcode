/*
 * DlgAnotherName.java
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
package net.sarangnamu.ems_tracking.dlg;

import net.sarangnamu.common.BkCfg;
import net.sarangnamu.common.fonts.FontLoader;
import net.sarangnamu.common.ui.dlg.DlgBase;
import net.sarangnamu.ems_tracking.R;
import net.sarangnamu.ems_tracking.cfg.Cfg;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DlgAnotherName extends DlgBase {
    private Button mBtn;
    private String mEmsNum;
    private EditText mEdit;
    private String mAnotherName;

    public DlgAnotherName(Context context, String emsNum) {
        super(context);

        this.mEmsNum = emsNum;
    }

    public DlgAnotherName(Context context, String emsNum, String anotherName) {
        this(context, emsNum);

        this.mAnotherName = anotherName;
    }

    @Override
    protected void initLayout() {
        mBtn     = (Button) findViewById(R.id.btn);
        mEdit    = (EditText) findViewById(R.id.edit);

        mEdit.setTypeface(FontLoader.getInstance(getContext()).getFont("Roboto-Light"));
        mBtn.setTypeface(FontLoader.getInstance(getContext()).getFont("Roboto-Light"));
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEdit.getText().toString();
                Cfg.setAnotherName(getContext(), mEmsNum, text);
                BkCfg.hideKeyboard(mEdit);
                dismiss();
            }
        });

        if (mAnotherName != null) {
            mEdit.setText(mAnotherName);
        }

        BkCfg.showKeyboard(mEdit);
    }

    @Override
    protected int getBaseLayoutId() {
        return R.layout.dlg_another_name;
    }
}
