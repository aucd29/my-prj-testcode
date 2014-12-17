/*
 * LinearBase.java
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
package net.sarangnamu.common.ui;

import net.sarangnamu.common.DimTool;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;

public abstract class LinearBase extends LinearLayout {
    public LinearBase(Context context) {
        super(context);
        initLayout();
    }

    public LinearBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public LinearBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout();
    }

    protected void setLayoutListener() {
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    // This method was deprecated in API level 16. Use #removeOnGlobalLayoutListener instead
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                callGlobalLayout();
            }
        });
    }

    protected void callGlobalLayout() {
    }

    protected int dpToPixelInt(int dp) {
        return DimTool.dpToPixelInt(getContext(), dp);
    }

    protected float dpToPixel(float dp) {
        return DimTool.dpToPixel(getContext(), dp);
    }

    protected String getString(int resid) {
        return getContext().getResources().getString(resid);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    ////////////////////////////////////////////////////////////////////////////////////

    protected abstract void initLayout();
}
