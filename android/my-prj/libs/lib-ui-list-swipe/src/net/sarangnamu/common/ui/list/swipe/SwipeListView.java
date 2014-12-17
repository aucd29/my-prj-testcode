/*
 * SwipeListView.java
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
package net.sarangnamu.common.ui.list.swipe;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.ui.gesture.Gesture;
import net.sarangnamu.common.ui.gesture.Gesture.GestureLeftListener;
import net.sarangnamu.common.ui.list.AniBtnListView;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SwipeListView extends AniBtnListView {
    private static final String TAG = "SwipeListView";
    private Gesture gesture;

    public SwipeListView(Context context) {
        super(context);
        initLayout();
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout();
    }

    @Override
    protected void initLayout() {
        gesture = Gesture.newInstance(getContext());
        //gesture.setThreshold(1000, 1000);
        gesture.setOnGestureLeftListener(new GestureLeftListener() {
            @Override
            public void toLeft() {
                DLog.d(TAG, "===================================================================");
                DLog.d(TAG, "TO LEFT");
                DLog.d(TAG, "===================================================================");

                setLock(); // lock;
            }
        });

        setOnTouchListener(new TouchUpListener() {
            @Override
            public void up() {
                setLock(); // unlock
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
      if (gesture != null) {
          DLog.d(TAG, "gesture.onTouchEvent");
          gesture.onTouchEvent(ev);
      }

//      return false;
        return super.onTouchEvent(ev);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        DLog.d(TAG, "onInterceptTouchEvent");
//
//        if (gesture != null) {
//            DLog.d(TAG, "gesture.onTouchEvent");
//            gesture.onTouchEvent(ev);
//        }
//
//        return true;
//    }
}
