/*
 * DlgBtnBase.java
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * {@code
 * <pre>

 * </pre>}
 * 
 * @author <a href="mailto:aucd29@gmail.com.com">Burke Choi</a>
 */
public abstract class DlgBtnBase extends DlgBase implements View.OnClickListener {
    protected int titleColor;
    protected Button left, right;
    protected TextView title;
    protected FrameLayout layout;
    protected LinearLayout content, bottom;
    private DlgBtnListener listener;

    public DlgBtnBase(Context context) {
        super(context);
    }

    @Override
    protected int getBaseLayoutId() {
        return R.layout.dlg_base;
    }

    @Override
    protected void initLayout() {
        left    = (Button) findViewById(R.id.left);
        right   = (Button) findViewById(R.id.right);
        title   = (TextView) findViewById(R.id.title);
        layout  = (FrameLayout) findViewById(R.id.layout);
        content = (LinearLayout) findViewById(R.id.content);
        bottom  = (LinearLayout) findViewById(R.id.bottom);

        left.setOnClickListener(this);
        right.setOnClickListener(this);

        if (titleColor != 0) {
            title.setBackgroundColor(titleColor);
        }
    }

    public void setOnButtonClickListener(boolean left, View.OnClickListener l) {
        if (left) {
            this.left.setOnClickListener(l);
        } else {
            this.right.setOnClickListener(l);
        }
    }

    public void setOneButton() {
        right.setVisibility(View.GONE);
    }

    public void setTitleText(int id) {
        title.setText(id);
    }

    public void setBtnLeftText(int id) {
        left.setText(id);
    }

    public void setBtnRightText(int id) {
        right.setText(id);
    }

    public void setOnBtnListener(DlgBtnListener l) {
        listener = l;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == right.getId()) {
            if (listener != null) {
                listener.ok();
            }

            dismiss();
        } else {
            dismiss();
        }
    }

    public void hideTitle() {
        title.setVisibility(View.GONE);

        FrameLayout.LayoutParams lp = (LayoutParams) content.getLayoutParams();
        lp.topMargin = dpToPixelInt(10);
    }

    public void hideButtons() {
        bottom.setVisibility(View.GONE);

        FrameLayout.LayoutParams lp = (LayoutParams) content.getLayoutParams();
        lp.bottomMargin = dpToPixelInt(10);
    }

    public void setTransparentBaseLayout() {
        layout.setBackgroundColor(0x00000000);
    }

    public void setDialogSize(int width, int height) {
        ViewGroup.LayoutParams lp = layout.getLayoutParams();
        lp.width = width;
        lp.height = height;

        layout.setLayoutParams(lp);
    }

    public void setTitleBackgroundColor(int color) {
        titleColor = color;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // DlgBtnListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public interface DlgBtnListener {
        public void ok();
    }
}
