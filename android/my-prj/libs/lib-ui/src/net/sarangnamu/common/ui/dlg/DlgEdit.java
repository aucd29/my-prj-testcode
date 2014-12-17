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
    protected int layoutId;
    protected String value, hint;
    protected EditText edit;
    private DlgEditListenr listener;

    public DlgEdit(Context context, int layoutId) {
        super(context);

        this.layoutId = layoutId;
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        initEdit();
    }

    private void initEdit() {
        View view = inflate(layoutId);
        content.addView(view);

        int editId = view.getResources().getIdentifier("edit", "id", getContext().getPackageName());
        edit = (EditText) view.findViewById(editId);
        edit.setText(value);

        if (hint != null && hint.length() > 0) {
            edit.setHint(hint);
        }

        if (value != null && value.length() > 0) {
            edit.setText(value);
        }
    }

    public void setHint(String value) {
        hint = value;
    }

    public void setHint(int resid) {
        hint = getString(resid);
    }

    public void setMessage(String value) {
        this.value = value;
    }

    public void setMessage(int resid) {
        this.value = getString(resid);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == left.getId()) {
            String res = edit.getText().toString();
            if (res.length() == 0) {
                if (listener != null) {
                    listener.error();
                }

                return ;
            }

            if (listener != null) {
                listener.ok(res);
            }

            dismiss();
        } else {
            dismiss();
        }
    }

    public void setOnEditListener(DlgEditListenr l) {
        listener = l;
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
