/*
 * FrgmtManager.java
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
package net.sarangnamu.common.frgmt;

import java.util.HashMap;

import net.sarangnamu.common.DLog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public abstract class FrgmtManager {
    private static final String TAG = "FrgmtManager";

    protected int baseLayoutId;
    protected String currentName, baseName;
    protected FragmentManager fm;
    protected HashMap<String, Class<?>> classes;
    protected HashMap<String, Fragment> frgmts;

    public FrgmtManager() {
        setMap();
    }

    public void setFragmentManager(FragmentActivity act) {
        if (act == null) {
            DLog.e(TAG, "setFragmentManager act is null");
            return ;
        }

        fm = act.getSupportFragmentManager();
    }

    protected void setMap() {
        if (classes == null) {
            classes = new HashMap<String, Class<?>>();
        }

        if (frgmts == null) {
            frgmts = new HashMap<String, Fragment>();
        }
    }

    public void add(String name, Class<?> cls) {
        setMap();

        classes.put(name, cls);
    }

    public void setBaseLayoutId(int id) {
        baseLayoutId = id;
    }

    public void setBase(String name) {
        if (baseLayoutId == 0) {
            DLog.e(TAG, "setBase");
            return ;
        }

        Fragment frgmt = frgmts.get(name);
        if (frgmt == null) {
            frgmt = instFragment(name);
        }

        FragmentTransaction trans = fm.beginTransaction();
        if (frgmt.isVisible()) {
            return ;
        }

        baseName = name;
        trans.add(baseLayoutId, frgmt);
        trans.commit();
    }

    protected Fragment instFragment(String name) {
        try {
            Class<?> cls = classes.get(name);
            Fragment frgmt = (Fragment) cls.newInstance();
            frgmts.put(name, frgmt);

            return frgmt;
        } catch (Exception e) {
            DLog.e(TAG, "instFragment", e);
        }

        return null;
    }

    public Fragment show(String name) {
        Fragment frgmt = frgmts.get(name);
        if (frgmt == null) {
            frgmt = instFragment(name);
        }

        if (frgmt == null) {
            return frgmt;
        }

        FragmentTransaction trans = fm.beginTransaction();
        if (frgmt.isVisible()) {
            return frgmt;
        }

        currentName = name;
        trans.replace(baseLayoutId, frgmt);
        trans.addToBackStack(null);
        trans.commit();

        return frgmt;
    }

    public void showBase() {
        if (fm == null) {
            DLog.e(TAG, "setFragmentManager fm is null");
            return ;
        }

        int count = fm.getBackStackEntryCount();
        for (int i=0; i<count; ++i) {
            fm.popBackStack(i, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        currentName = baseName;
    }

    public Fragment getCurrent() {
        if (frgmts != null) {
            return frgmts.get(currentName);
        }

        return null;
    }
}
