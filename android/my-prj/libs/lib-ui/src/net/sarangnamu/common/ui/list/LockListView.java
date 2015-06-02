/*
 * LockListView.java
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
package net.sarangnamu.common.ui.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * <pre>
 * {@code
    - xml
    <net.sarangnamu.common.ui.list.LockListView
        android:id="@android:id/list"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@android:color/white"
        android:drawSelectorOnTop="false" />

    - code
    LockListView list = (LockListView) findViewById(R.id.list);
    list.setOnTouchListener(new TouchUpListener() {
        public void up() {

        }
    });

    list.setLock();
 * }
 * </pre>
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class LockListView extends ListView {
    //    private static final String TAG = "LockListView";
    protected boolean mIsScrollLock;
    private TouchUpListener mListener;

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
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsScrollLock) {
            switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mListener != null) {
                    mListener.up();
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
        if (mIsScrollLock) {
            return false;
        } else {
            return super.onTouchEvent(ev);
        }
    }

    public synchronized void setLock() {
        mIsScrollLock = mIsScrollLock ? false : true;
    }

    public boolean getLockStatus() {
        return mIsScrollLock;
    }

    public void setOnTouchListener(TouchUpListener l) {
        mListener = l;
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
