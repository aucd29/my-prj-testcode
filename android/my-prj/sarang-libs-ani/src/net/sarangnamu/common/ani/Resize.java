/*
 * Expand.java
 * Copyright 2014 Burke Choi All right reserverd.
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
package net.sarangnamu.common.ani;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Resize {
    private static final String TAG = "Resize";
    private static final int TYPE_HEIGHT = 1;

    public static void height(final View view, int changeValue, int duration, final ResizeAnimationListener l) {
        if (view == null) {
            return ;
        }

/*        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                case TYPE_HEIGHT: {
                    View view = (View) msg.obj;

                    if (view.getParent() instanceof LinearLayout) {
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();

                        params.height = msg.arg1;
                        view.setLayoutParams(params);
                    } else if (view.getParent() instanceof RelativeLayout) {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();

                        params.height = msg.arg1;
                        view.setLayoutParams(params);
                    } else if (view.getParent() instanceof FrameLayout) {
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();

                        params.height = msg.arg1;
                        view.setLayoutParams(params);
                    }
                } break;
                }
            }
        };*/

        final ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredHeight(), changeValue);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                /*Message msg = handler.obtainMessage();
                msg.what = TYPE_HEIGHT;
                msg.obj  = view;
                msg.arg1 = val;
                handler.sendMessage(msg);*/

                if (view.getParent() instanceof LinearLayout) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();

                    params.height = val;
                    view.setLayoutParams(params);
                } else if (view.getParent() instanceof RelativeLayout) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();

                    params.height = val;
                    view.setLayoutParams(params);
                } else if (view.getParent() instanceof FrameLayout) {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();

                    params.height = val;
                    view.setLayoutParams(params);
                }
            }
        });
        anim.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator arg0) {
                if (l != null) {
                    l.onAnimationStart();
                }
            }
            @Override
            public void onAnimationRepeat(Animator arg0) { }
            @Override
            public void onAnimationEnd(Animator arg0) {
                anim.removeAllUpdateListeners();
                anim.removeAllListeners();

                if (l != null) {
                    l.onAnimationEnd();
                }
            }
            @Override
            public void onAnimationCancel(Animator arg0) { }
        });
        anim.setDuration(duration);
        anim.start();
    }

    public static void height(View view, int changeValue, ResizeAnimationListener l) {
        height(view, changeValue, 300, l);
    }

    /*public static void width(View view, float value, ResizeAnimationListener listener) {
        if (view == null) {
            return ;
        }

        ObjectAnimator obj = ObjectAnimator.ofFloat(view, "x", value);

        if (listener != null) {
            listener.setObjectAnimator(obj);
            obj.addListener(listener);
        }

        obj.start();
    }*/

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ResizeAnimatorListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public interface ResizeAnimationListener {
        public void onAnimationStart();
        public void onAnimationEnd();
    }
}
