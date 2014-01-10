/*
 * FrgmtManager.java
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
package net.sarangnamu.common.frgmt;

import net.sarangnamu.common.DLog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
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
    nv.setBase(HomeFrgmt.class);

    - change page
    nv.replace(OtherFrgmt.class);
 * </pre>}
 * 
 * @author <a href="mailto:aucd29@gmail.com.com">Burke Choi</a>
 */
public abstract class FrgmtManager {
    private static final String TAG = "FrgmtManager";

    protected FragmentManager fm;

    public FrgmtManager() {
    }

    public void setFragmentManager(FragmentActivity act) {
        if (act == null) {
            DLog.e(TAG, "setFragmentManager act is null");
            return ;
        }

        fm = act.getSupportFragmentManager();
    }

    public void add(int id, Class<?> cls) {
        try {
            Fragment frgmt = (Fragment) cls.newInstance();
            if (frgmt == null) {
                DLog.e(TAG, "setBase frgmt == null");
                return ;
            }

            FragmentTransaction trans = fm.beginTransaction();
            if (frgmt.isVisible()) {
                return ;
            }

            trans.add(id, frgmt, frgmt.getClass().getName());
            trans.commit();
        } catch (Exception e) {
            DLog.e(TAG, "setBase", e);
        }
    }

    public void resetAdd(int id, Class<?> cls) {
        replace(id, cls, false, null);
    }

    public Fragment replace(int id, Class<?> cls) {
        return replace(id, cls, true, null);
    }

    public Fragment replace(int id, Class<?> cls, Bundle bundle) {
        return replace(id, cls, true, bundle);
    }

    private Fragment replace(int id, Class<?> cls, boolean stack, Bundle bundle) {
        try {
            Fragment frgmt = fm.findFragmentByTag(cls.getName());
            FragmentTransaction trans = fm.beginTransaction();

            if (frgmt != null && frgmt.isVisible()) {
                return frgmt;
            }

            frgmt = (Fragment) cls.newInstance();
            if (frgmt == null) {
                DLog.e(TAG, "replace frgmt == null");
                return null;
            }

            if (bundle != null) {
                frgmt.setArguments(bundle);
            }

            if (stack) {
                setTransition(trans);
            }

            trans.replace(id, frgmt, frgmt.getClass().getName());

            if (stack) {
                trans.addToBackStack(frgmt.getClass().getName());
            }

            trans.commit();

            return frgmt;
        } catch (Exception e) {
            DLog.e(TAG, "replace", e);
        }

        return null;
    }

    public void popBack() {
        if (fm != null) {
            fm.popBackStack();
        }
    }

    public void popBackAll() {
        if (fm == null) {
            DLog.e(TAG, "setFragmentManager fm is null");
            return ;
        }

        int count = fm.getBackStackEntryCount();
        for (int i=0; i<count; ++i) {
            fm.popBackStack(i, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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

    public Fragment getCurrentFragment() {
        if (fm == null) {
            return null;
        }

        int count = fm.getBackStackEntryCount();
        if (count > 0) {
            BackStackEntry frgmt = fm.getBackStackEntryAt(count - 1);
            return fm.findFragmentByTag(frgmt.getName());
        }

        return null;
    }

    public Fragment getFragment(Class<?> cls) {
        if (fm == null) {
            return null;
        }

        return fm.findFragmentByTag(cls.getName());
    }
}
