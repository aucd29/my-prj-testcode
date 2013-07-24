/*
 * PinchZoomListenerBase.java
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.ui_test.common;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * 
 * @author @aucd29
 *
 */
public abstract class PinchZoomListenerBase extends ScaleGestureDetector.SimpleOnScaleGestureListener implements View.OnTouchListener {
    private static final String TAG = "PinchZoomListenerBase";
    private static final float BASE_FACTOR = 1.5f;

    protected float scaleFactor = 1.f;
    protected Context context;
    protected ScaleGestureDetector scaleDetector;

    public PinchZoomListenerBase(Context context) {
        this.context = context;

        scaleDetector = new ScaleGestureDetector(context, this);
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scaleFactor *= detector.getScaleFactor();

        // Don't let the object get too small or too large.
        scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f));

        if (scaleFactor > BASE_FACTOR) {
            scaleFactor = 1.0f;

            onChangeScale();
        }

        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // View.OnTouchListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (scaleDetector != null) {
            scaleDetector.onTouchEvent(event);
        }

        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public abstract void onChangeScale();
}
