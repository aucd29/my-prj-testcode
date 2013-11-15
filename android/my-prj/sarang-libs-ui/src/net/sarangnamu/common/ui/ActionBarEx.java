/*
 * ActionBarEx.java
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
package net.sarangnamu.common.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.view.View;

public class ActionBarEx {
    protected int displayOpt = ActionBar.DISPLAY_SHOW_CUSTOM;
    protected Activity activity;
    protected ActionBar actionBar;
    protected ActionBar.LayoutParams params;
    protected View view;

    public ActionBarEx(Activity activity) {
        this.activity = activity;
        params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
    }

    public void init(final int resId) {
        view = activity.getLayoutInflater().inflate(resId, null);

        actionBar = activity.getActionBar();

        if (actionBar == null) {
            return;
        }

        actionBar.setCustomView(view, params);
        actionBar.setDisplayOptions(displayOpt);
    }

    public void setDisplayOption(final int opt) {
        displayOpt = opt;
    }

    public void setLayoutParams(ActionBar.LayoutParams params) {
        this.params = params;
    }

    public View getView() {
        return view;
    }

    public View getChildView(int id) {
        return view.findViewById(id);
    }
}
