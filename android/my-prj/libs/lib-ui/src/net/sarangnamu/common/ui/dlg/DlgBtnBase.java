/*
 * DlgBtnBase.java
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
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public abstract class DlgBtnBase extends DlgBase implements View.OnClickListener {
    protected int mTitleColor;
    protected Button mLeftBtn, mRightBtn;
    protected TextView mTitle;
    protected FrameLayout mLayout;
    protected LinearLayout mContent, mBottom;
    private DlgBtnListener mListener;

    public DlgBtnBase(Context context) {
        super(context);
    }

    @Override
    protected int getBaseLayoutId() {
        return R.layout.dlg_base;
    }

    @Override
    protected void initLayout() {
        mLeftBtn    = (Button) findViewById(R.id.left);
        mRightBtn   = (Button) findViewById(R.id.right);
        mTitle   = (TextView) findViewById(R.id.title);
        mLayout  = (FrameLayout) findViewById(R.id.layout);
        mContent = (LinearLayout) findViewById(R.id.content);
        mBottom  = (LinearLayout) findViewById(R.id.bottom);

        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);

        if (mTitleColor != 0) {
            mTitle.setBackgroundColor(mTitleColor);
        }
    }

    public void setOnButtonClickListener(boolean left, View.OnClickListener l) {
        if (left) {
            this.mLeftBtn.setOnClickListener(l);
        } else {
            this.mRightBtn.setOnClickListener(l);
        }
    }

    public void setOneButton() {
        mRightBtn.setVisibility(View.GONE);
    }

    public void setTitleText(int id) {
        mTitle.setText(id);
    }

    public void setBtnLeftText(int id) {
        mLeftBtn.setText(id);
    }

    public void setBtnRightText(int id) {
        mRightBtn.setText(id);
    }

    public void setOnBtnListener(DlgBtnListener l) {
        mListener = l;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mRightBtn.getId()) {
            if (mListener != null) {
                mListener.ok();
            }

            dismiss();
        } else {
            dismiss();
        }
    }

    public void hideTitle() {
        mTitle.setVisibility(View.GONE);

        FrameLayout.LayoutParams lp = (LayoutParams) mContent.getLayoutParams();
        lp.topMargin = dpToPixelInt(10);
    }

    public void hideButtons() {
        mBottom.setVisibility(View.GONE);

        FrameLayout.LayoutParams lp = (LayoutParams) mContent.getLayoutParams();
        lp.bottomMargin = dpToPixelInt(10);
    }

    public void setTransparentBaseLayout() {
        mLayout.setBackgroundColor(0x00000000);
    }

    public void setDialogSize(int width, int height) {
        ViewGroup.LayoutParams lp = mLayout.getLayoutParams();
        lp.width = width;
        lp.height = height;

        mLayout.setLayoutParams(lp);
    }

    public void setTitleBackgroundColor(int color) {
        mTitleColor = color;
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
