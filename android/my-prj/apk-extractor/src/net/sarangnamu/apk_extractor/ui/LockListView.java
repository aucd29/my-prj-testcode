/*
 * LockListView.java
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
package net.sarangnamu.apk_extractor.ui;

import net.sarangnamu.common.DLog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class LockListView extends ListView {
    private static final String TAG = "LockListView";
    private boolean lockScroll;
    private TouchUpListener listener;

    public LockListView(Context context) {
        super(context);
        initLayout();
    }

    public LockListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public LockListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout();
    }

    protected void initLayout() {
        RobotoLightTextView empty = new RobotoLightTextView(getContext());
        empty.setText("Not found a app");
        setEmptyView(empty);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (lockScroll) {
            switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                DLog.d(TAG, "up");
                if (listener != null) {
                    listener.up();
                }
                break;
            }

            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (lockScroll) {
            return false;
        } else {
            return super.onTouchEvent(ev);
        }
    }

    public synchronized void  setLock() {
        lockScroll = lockScroll ? false : true;

        //DLog.d(TAG, "lock stat " + lockScroll);
    }

    public boolean getLockStatus() {
        return lockScroll;
    }

    public void setOnTouchListener(TouchUpListener l) {
        listener = l;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // TouchUpListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public interface TouchUpListener {
        public void up();
    }
}
