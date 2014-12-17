/*
 * DlgBase.java
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

import net.sarangnamu.common.DimTool;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public abstract class DlgBase extends Dialog {
    public DlgBase(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setCanceledOnTouchOutside(false);
        setContentView(getBaseLayoutId());
        initLayout();
    }

    public void setSize(int width, int height) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(this.getWindow().getAttributes());
        lp.width = dpToPixelInt(width);
        lp.height = dpToPixelInt(height);

        this.getWindow().setAttributes(lp);
    }

    public void setWidth(int width) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(this.getWindow().getAttributes());
        lp.width = dpToPixelInt(width);

        this.getWindow().setAttributes(lp);
    }

    public void setHeight(int height) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(this.getWindow().getAttributes());
        lp.height = dpToPixelInt(height);

        this.getWindow().setAttributes(lp);
    }

    public void setVerticalMargin(int margin) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(this.getWindow().getAttributes());
        lp.verticalMargin = dpToPixelInt(margin);

        this.getWindow().setAttributes(lp);
    }

    public void setSoftInputMode(int mode) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(this.getWindow().getAttributes());
        lp.softInputMode = mode;

        getWindow().setAttributes(lp);
    }

    protected int dpToPixelInt(int val) {
        return DimTool.dpToPixelInt(getContext(), val);
    }

    protected float dpToPixel(float val) {
        return DimTool.dpToPixel(getContext(), val);
    }

    protected View inflate(int id) {
        return LayoutInflater.from(getContext()).inflate(id, null);
    }

    protected LinearLayout.LayoutParams insLayoutParams(int w, int h) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w, h);
        return lp;
    }

    protected String getString(int resid) {
        return getContext().getResources().getString(resid);
    }

    public void setWindowPosition(View v) {
        int bottom = v.getBottom();
        int left   = v.getLeft();

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.TOP | Gravity.LEFT;
        lp.x = left;
        lp.y = v.getHeight() + v.getHeight() + bottom;
    }

    protected void setFullscreen() {
        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    ////////////////////////////////////////////////////////////////////////////////////

    protected abstract int getBaseLayoutId();
    protected abstract void initLayout();
}
