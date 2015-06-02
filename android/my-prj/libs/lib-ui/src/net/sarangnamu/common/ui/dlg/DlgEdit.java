/*
 * DlgEdit.java
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
import android.view.View;
import android.widget.EditText;

/**
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class DlgEdit extends DlgBtnBase {
    protected int mLayoutId;
    protected String mValue, mHint;
    protected EditText mEdit;
    private DlgEditListenr mListener;

    public DlgEdit(Context context, int layoutId) {
        super(context);

        this.mLayoutId = layoutId;
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        initEdit();
    }

    private void initEdit() {
        View view = inflate(mLayoutId);
        mContent.addView(view);

        int editId = view.getResources().getIdentifier("edit", "id", getContext().getPackageName());
        mEdit = (EditText) view.findViewById(editId);
        mEdit.setText(mValue);

        if (mHint != null && mHint.length() > 0) {
            mEdit.setHint(mHint);
        }

        if (mValue != null && mValue.length() > 0) {
            mEdit.setText(mValue);
        }
    }

    public void setHint(String value) {
        mHint = value;
    }

    public void setHint(int resid) {
        mHint = getString(resid);
    }

    public void setMessage(String value) {
        this.mValue = value;
    }

    public void setMessage(int resid) {
        this.mValue = getString(resid);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mLeftBtn.getId()) {
            String res = mEdit.getText().toString();
            if (res.length() == 0) {
                if (mListener != null) {
                    mListener.error();
                }

                return ;
            }

            if (mListener != null) {
                mListener.ok(res);
            }

            dismiss();
        } else {
            dismiss();
        }
    }

    public void setOnEditListener(DlgEditListenr l) {
        mListener = l;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // DlgEditListenr
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public interface DlgEditListenr {
        public void ok(String value);
        public void error();
    }

}
