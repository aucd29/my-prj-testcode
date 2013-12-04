/*
 * FadeColor.java
 * Copyright 2013 OBIGO All rights reserved.
 *             http://www.obigo.com
 */
package net.sarangnamu.libs_common.ani;

import android.animation.Animator.AnimatorListener;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.view.View;

public class FadeColor {
    public static void startResource(View view, int fRes, int sRes, AnimatorListener l) {
        Resources res = view.getContext().getResources();
        start(view, res.getColor(fRes), res.getColor(sRes), l);
    }

    public static void startResource(View view, int fRes, int sRes, int duration, AnimatorListener l) {
        Resources res = view.getContext().getResources();
        start(view, res.getColor(fRes), res.getColor(sRes), duration, l);
    }

    public static void start(View view, int fColor, int sColor, AnimatorListener l) {
        start(view, fColor, sColor, 500, l);
    }

    public static void start(View view, int fColor, int sColor, int duration, AnimatorListener l) {
        if (view == null) {
            return ;
        }

        ObjectAnimator colorFade = ObjectAnimator.ofObject(
                view, "backgroundColor", new ArgbEvaluator(),  fColor, sColor);
        colorFade.setDuration(duration);
        colorFade.start();
    }
}
