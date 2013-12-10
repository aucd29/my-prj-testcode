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
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;

/**
 * {@code
 * <pre>
    public class Navigator extends FrgmtManager {
        private static Navigator inst;

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
    nv.add(HomeFrgmt.class);
    nv.add(StudyFrgmt.class);
    nv.add(StudyDetailFrgmt.class);
    nv.setBase(HomeFrgmt.class);

    - change page
    nv.show(HomeFrgmt.class);
 * </pre>}
 * 
 * @author <a href="mailto:aucd29@gmail.com.com">Burke Choi</a>
 */
public abstract class FrgmtManager implements OnBackStackChangedListener {
    private static final String TAG = "FrgmtManager";

    protected int baseLayoutId;
    protected String currentName;
    protected Fragment baseFrgmt;
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

        if (fm == null) {
            fm = act.getSupportFragmentManager();
            fm.addOnBackStackChangedListener(this);
        }
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

    public void add(Class<?> cls) {
        setMap();

        classes.put(cls.getName(), cls);
    }

    public void setBaseLayoutId(int id) {
        baseLayoutId = id;
    }

    public void setBase(Class<?> cls) {
        setBase(cls.getName());
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
        if (baseFrgmt != null) {
            trans.remove(frgmts.get(baseFrgmt.getClass().getName()));
        }

        baseFrgmt   = frgmt;
        currentName = name;

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

    public Fragment show(Class<?> cls) {
        return show(cls.getName());
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

        setTransition(trans);

        trans.replace(baseLayoutId, frgmt);
        trans.addToBackStack(name);
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

        if (baseFrgmt != null) {
            currentName = baseFrgmt.getClass().getName();
        }
    }

    public void setCurrentName(String name) {
        currentName = name;
    }

    public void setCurrentName(Class<?> cls) {
        currentName = cls.getName();
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

    public Fragment getFragmentByName(Class<?> cls) {
        if (frgmts == null) {
            return null;
        }

        return frgmts.get(cls.getName());
    }

    public void back() {
        if (fm != null) {
            fm.popBackStack();
        }
    }

    protected void setTransition(FragmentTransaction trans) {
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    }

    protected void setSlideTransition(FragmentTransaction trans) {
        trans.setCustomAnimations(R.anim.slide_in_current, R.anim.slide_in_next, R.anim.slide_out_current, R.anim.slide_out_prev);
    }

    protected void setUpTransition(FragmentTransaction trans) {
        trans.setCustomAnimations(R.anim.slide_up_current, R.anim.slide_up_next, R.anim.slide_down_current, R.anim.slide_down_prev);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // OnBackStackChangedListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onBackStackChanged() {
        int count = fm.getBackStackEntryCount();
        if (count > 0) {
            BackStackEntry frgmt = fm.getBackStackEntryAt(count - 1);
            currentName = frgmt.getName();
        } else {
            currentName = baseFrgmt.getClass().getName();
        }

        DLog.d(TAG, "backStack : currentName:" + currentName);
    }
}
