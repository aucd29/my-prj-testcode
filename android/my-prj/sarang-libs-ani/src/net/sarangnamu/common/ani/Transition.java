/*
 * Transition.java
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
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * <pre>
 * {@code
 * Transition.startX(view, 100, null);
 * }
 * </pre>
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class Transition {
    public static void startX(View view, float dpMoveX, AnimatorListener l) {
        start(view, 0, dpMoveX, l);
    }

    public static void startY(View view, float dpMoveY, AnimatorListener l) {
        start(view, 0, dpMoveY, l);
    }

    private static void start(final View view, int type, float move, AnimatorListener l) {
        if (view == null) {
            return ;
        }

        String cmd;
        switch (type) {
        case 0: cmd = "translationX"; break;
        case 1: cmd = "translationY"; break;
        default: cmd = "translationX"; break;
        }

        move = move * view.getContext().getResources().getDisplayMetrics().density;
        ObjectAnimator obj = ObjectAnimator.ofFloat(view, cmd, move);

        if (l != null) {
            obj.addListener(l);
        }

        obj.start();
    }
}
