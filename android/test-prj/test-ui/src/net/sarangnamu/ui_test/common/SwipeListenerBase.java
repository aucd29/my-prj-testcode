/*
 * SwipeListenerBase.java
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.ui_test.common;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * @author @aucd29
 *
 */
public abstract class SwipeListenerBase implements GestureDetector.OnGestureListener, View.OnTouchListener {
    public static final int SWIPE_THRESHOLD = 300;
    public static final int SWIPE_VELOCITY_THRESHOLD = 300;

    protected GestureDetector detector;

    public SwipeListenerBase(Context context) {
        this.detector = new GestureDetector(context, this);
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
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
            float distanceY) {
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
        if (detector != null && detector.onTouchEvent(event)) {
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
