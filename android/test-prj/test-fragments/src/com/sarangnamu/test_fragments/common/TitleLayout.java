/*
 * TitleLayout.java
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package com.sarangnamu.test_fragments.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

public class TitleLayout extends LinearLayout {
    protected Button title;

    public TitleLayout(Context context) {
        super(context);
        initLayout();
    }

    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public TitleLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout();
    }

    protected void initLayout() {
        title = new Button(getContext());
        title.setPadding(0, 0, 0, 0);
        setOrientation(LinearLayout.VERTICAL);

        addView(title);
    }

    public void setTitleText(final int resId, final OnClickListener l) {
        title.setText(resId);
        title.setOnClickListener(l);
    }

    public void setTitleText(final String msg, final OnClickListener l) {
        title.setText(msg);
        title.setOnClickListener(l);
    }

    public void setTitleBackgroundColor(final int color) {
        title.setBackgroundColor(color);
    }

    public void setTitleBackgroundDrawable(final Drawable drawable) {
        title.setBackgroundDrawable(drawable);
    }

    public void setTitleBackgroundResource(final int resId) {
        title.setBackgroundResource(resId);
    }
}
