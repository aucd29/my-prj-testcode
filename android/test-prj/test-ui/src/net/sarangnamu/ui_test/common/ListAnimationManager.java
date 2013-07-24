/*
 * ListAnimationManager.java
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.ui_test.common;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * 
 * @author @aucd29
 *
 */
public class ListAnimationManager {
    private static final int DURATION = 500;

    public static void start(Context context, View target, final ListAnimationListener listener) {
        if (target == null) {
            return ;
        }

        Animation anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        anim.setDuration(DURATION);

        Animation anim2 = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
        anim2.setDuration(DURATION);

        target.startAnimation(anim);
        target.startAnimation(anim2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onAnimationEnd();
                }
            }
        }, anim.getDuration());
    }


    public interface ListAnimationListener {
        public void onAnimationEnd();
    }
}
