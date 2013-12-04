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

import java.util.ArrayDeque;
import java.util.HashMap;

import net.sarangnamu.common.DLog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * {@code
 * <pre>
    public class Navigator extends FrgmtManager {
        private static Navigator inst;

        public static final String HOME         = "home";
        public static final String HOME_WRITE   = "homeWrite";
        public static final String STUDY        = "study";
        public static final String STUDY_DETAIL = "studyDetail";
        public static final String QNA          = "qna";

        public static Navigator getInstance(FragmentActivity act) {
            if (inst == null) {
                inst = new Navigator();
            }

            inst.setFragmentManager(act);

            return inst;
        }

        private Navigator() {

        }
    }

    - example
    Navigator nv = Navigator.getInstance(this);
    nv.setBaseLayoutId(R.id.content);
    nv.add(Navigator.HOME, HomeFrgmt.class);
    nv.add(Navigator.STUDY, StudyFrgmt.class);
    nv.add(Navigator.STUDY_DETAIL, StudyDetailFrgmt.class);
    nv.setBase(Navigator.HOME);

    - change page
    nv.show(Navigator.STUDY_DETAIL);
 * </pre>}
 * 
 * @author <a href="mailto:aucd29@gmail.com.com">Burke Choi</a>
 */
public abstract class FrgmtManager {
    private static final String TAG = "FrgmtManager";

    protected int baseLayoutId;
    protected String currentName, baseName;
    protected FragmentManager fm;
    protected HashMap<String, Class<?>> classes;
    protected HashMap<String, Fragment> frgmts;
    protected ArrayDeque<String> history;

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

        if (history == null) {
            history = new ArrayDeque<String>();
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

        showBase();
        if (baseName != null) {
            trans.remove(frgmts.get(baseName));
        }

        currentName = baseName = name;
        history.add(baseName);
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
        setTransition(trans);
        history.add(name);

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

    public void setCurrentName(String name) {
        currentName = name;
    }

    public Fragment getCurrent() {
        if (frgmts != null) {
            return frgmts.get(currentName);
        }

        return null;
    }

    public Fragment getFragmentByName(String name) {
        if (frgmts == null) {
            return null;
        }

        return frgmts.get(name);
    }

    public void back() {
        if (fm != null) {
            fm.popBackStack();
        }
    }

    protected void setTransition(FragmentTransaction trans) {
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    }
}
