/*
 * CustomActionBar.java
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package com.sarangnamu.test_fragments.common;

import android.app.ActionBar;
import android.app.Activity;
import android.view.View;

/**
 * 
 * @author @aucd29
 *
 */
public class CustomActionBar {
    protected Activity activity;
    protected ActionBar actionBar;
    protected int displayOpt = ActionBar.DISPLAY_SHOW_CUSTOM;
    protected ActionBar.LayoutParams params;

    public CustomActionBar(Activity activity) {
        this.activity = activity;
        params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
    }

    public void init(final int resId) {
        View view = activity.getLayoutInflater().inflate(resId, null);

        actionBar = activity.getActionBar();
        actionBar.setCustomView(view, params);
        actionBar.setDisplayOptions(displayOpt);
    }

    public void setDisplayOption(final int opt) {
        displayOpt = opt;
    }

    public void setLayoutParams(ActionBar.LayoutParams params) {
        this.params = params;
    }
}
