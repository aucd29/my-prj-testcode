/*
 * AnimatorEndListener.java
 * Copyright 2015 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.common.ani;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;

public abstract class AnimatorEndListener implements AnimatorListener {
    private static boolean mIsRunning;

    @Override
    public void onAnimationStart(Animator animation) {
        mIsRunning = true;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        animation.removeListener(this);
        onAnimationEnd();

        mIsRunning = false;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }

    public static boolean isRunning() {
        return mIsRunning;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public abstract void onAnimationEnd();
}
