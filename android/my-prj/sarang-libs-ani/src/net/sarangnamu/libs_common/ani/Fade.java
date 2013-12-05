/*
 * ChangeFade.java
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
package net.sarangnamu.libs_common.ani;

import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * {@code
 * <pre>
    Fade.start(hideView, showView, null);
 * </pre>}
 * 
 * @author <a href="mailto:aucd29@gmail.com.com">Burke Choi</a>
 */
public class Fade {
    private static final String TAG = "Fade";

    public static void start(View hideView, View showView, AnimatorListener l) {
        if (hideView == null || showView == null) {
            return ;
        }

        ObjectAnimator.ofFloat(hideView, "alpha", 0.25f, 1, 1).start();
        ObjectAnimator obj = ObjectAnimator.ofFloat(showView, "alpha", 1f, 1, 1);

        if (l != null) {
            obj.addListener(l);
        }

        obj.start();
    }
}

