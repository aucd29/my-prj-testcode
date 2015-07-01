/*
 * AlphaHelper.java
 * Copyright 2015 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.common.ani;

import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.view.View;

public class AlphaHelper {
    private static ObjectAnimator mAnimator;

    public static ObjectAnimator show(View view, AnimatorListener l) {
        return alpha(view, 1f, 0, l);
    }

    public static ObjectAnimator show(View view, int duration, AnimatorListener l) {
        return alpha(view, 1f, duration, l);
    }

    public static ObjectAnimator hide(View view, AnimatorListener l) {
        return alpha(view, 0f, 0, l);
    }

    public static ObjectAnimator hide(View view, int duration, AnimatorListener l) {
        return alpha(view, 0f, duration, l);
    }

    public static ObjectAnimator hideAndShow(final View view, final AnimatorListener l) {
        mAnimator = hide(view, 500, new AnimatorEndListener() {
            @Override
            public void onAnimationEnd() {
                mAnimator = show(view, 500, l);
            }
        });

        return null;
    }

    public static ObjectAnimator hideAndShow(final View view, final AnimatorEndListener fl, final AnimatorEndListener sl) {
        mAnimator = hide(view, 500, new AnimatorEndListener() {
            @Override
            public void onAnimationEnd() {
                if (fl != null) {
                    fl.onAnimationEnd();
                }

                mAnimator = show(view, 500, sl);
            }
        });

        return null;
    }

    public static ObjectAnimator hideAndShow(final View view, final int duration, final AnimatorListener l) {
        mAnimator = hide(view, duration, new AnimatorEndListener() {
            @Override
            public void onAnimationEnd() {
                mAnimator = show(view, duration, l);
            }
        });

        return null;
    }

    public static ObjectAnimator hideAndShow(final View view, final int duration, final AnimatorEndListener fl, final AnimatorEndListener sl) {
        mAnimator = hide(view, duration, new AnimatorEndListener() {
            @Override
            public void onAnimationEnd() {
                if (fl != null) {
                    fl.onAnimationEnd();
                }

                mAnimator = show(view, duration, sl);
            }
        });

        return null;
    }

    public static void stopAnimation() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    private static ObjectAnimator alpha(View view, float value, int duration, AnimatorListener l) {
        ObjectAnimator ani = ObjectAnimator.ofFloat(view, "alpha", value);
        if (l != null) {
            ani.addListener(l);
        }

        if (duration > 0) {
            ani.setDuration(duration);
        }

        ani.start();
        return ani;
    }
}
