/*
 * FadeColor.java
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
package net.sarangnamu.common.ani;

import android.animation.Animator.AnimatorListener;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.view.View;

/**
 * <pre>
 * {@code
   FadeColor.startResource(view, R.color.red, R.color.blue, null);
   FadeColor.start(view, 0xffff0000, 0xff0000ff, null);
 * }
 * </pre>
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class FadeColor {
    private static final int DURATION = 500;

    public static void startResource(View view, int fRes, int sRes, AnimatorListener l) {
        startResource(view, fRes, sRes, DURATION, l);
    }

    public static void startResource(View view, int fRes, int sRes, int duration, AnimatorListener l) {
        Resources res = view.getContext().getResources();
        start(view, res.getColor(fRes), res.getColor(sRes), duration, l);
    }

    public static void start(View view, int fColor, int sColor, AnimatorListener l) {
        start(view, fColor, sColor, DURATION, l);
    }

    public static void start(View view, int fColor, int sColor, int duration, AnimatorListener l) {
        if (view == null) {
            return ;
        }

        ObjectAnimator colorFade = ObjectAnimator.ofObject(
                view, "backgroundColor", new ArgbEvaluator(), fColor, sColor);
        colorFade.setDuration(duration);
        colorFade.start();
    }
}
