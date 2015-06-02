/*
 * SwipeListenerBase.java
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
package net.sarangnamu.common.ui.swipe;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public abstract class SwipeListenerBase implements GestureDetector.OnGestureListener, View.OnTouchListener {
    public static int SWIPE_THRESHOLD = 300;
    public static int SWIPE_VELOCITY_THRESHOLD = 300;

    protected GestureDetector mDetector;

    public SwipeListenerBase(Context context) {
        this.mDetector = new GestureDetector(context, this);
    }

    protected void setThreshold(int val) {
        SWIPE_THRESHOLD = val;
    }

    protected void setVelocity(int val) {
        SWIPE_VELOCITY_THRESHOLD = val;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // GestureDetector.OnGestureListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            //int pos = list.pointToPosition((int) e1.getX(), (int) e1.getY()) - list.getFirstVisiblePosition();
            int pos = getPosition((int) e1.getX(), (int) e1.getY());

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight(pos);
                    } else {
                        onSwipeLeft(pos);
                    }
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom(pos);
                    } else {
                        onSwipeTop(pos);
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return result;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // View.OnTouchListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mDetector != null && mDetector.onTouchEvent(event)) {
            return true;
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    ////////////////////////////////////////////////////////////////////////////////////

    protected abstract int getPosition(int x, int y);
    public abstract void onSwipeTop(final int position);
    public abstract void onSwipeRight(final int position);
    public abstract void onSwipeLeft(final int position);
    public abstract void onSwipeBottom(final int position);
}
