/*
 * FragmentController.java
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package com.sarangnamu.test_fragments.common;

import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class FragmentController {
    protected FragmentManager manager;
    protected HashMap<String, Fragment> fragments = new HashMap<String, Fragment>();
    protected int targetResId;

    public FragmentController(Activity activity, final int targetResId) {
        this.manager = activity.getFragmentManager();
        this.targetResId = targetResId;
    }

    public void add(final String name, final int resId) {
        fragments.put(name, manager.findFragmentById(resId));
    }

    public void add(final String name, final Fragment frgmt) {
        fragments.put(name, frgmt);
    }

    public void showDefault() {
        int count = manager.getBackStackEntryCount();
        for (int i=0; i<count; ++i) {
            manager.popBackStack(i, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public Fragment show(final String name) {
        Fragment current = fragments.get(name);
        if (current == null) {
            return null;
        }

        FragmentTransaction trans = manager.beginTransaction();
        if (current.isVisible()) {
            return current;
        }

        showProcess(name);

        trans.replace(targetResId, current);
        trans.addToBackStack(null);
        trans.commit();

        return current;
    }

    public void showProcess(final String name) {

    }

    public boolean sendMessage(final String name, final String msg) {
        Fragment current = fragments.get(name);
        if (current == null) {
            return false;
        }

        if (!(current instanceof FragmentEventListener)) {
            return false;
        }

        ((FragmentEventListener)current).sendMessage(msg);

        return true;
    }
}
