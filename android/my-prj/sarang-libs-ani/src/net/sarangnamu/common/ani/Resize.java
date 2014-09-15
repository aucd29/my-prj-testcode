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
import android.animation.ObjectAnimator;
import android.view.View;

public class Resize {
    public static void height(View view, float value, ResizeAnimatorListener listener) {
        if (view == null) {
            return ;
        }

        ObjectAnimator obj = ObjectAnimator.ofFloat(view, "y", value);

        if (listener != null) {
            listener.setObjectAnimator(obj);
            obj.addListener(listener);
        }

        obj.start();
    }

    public static void width(View view, float value, ResizeAnimatorListener listener) {
        if (view == null) {
            return ;
        }

        ObjectAnimator obj = ObjectAnimator.ofFloat(view, "x", value);

        if (listener != null) {
            listener.setObjectAnimator(obj);
            obj.addListener(listener);
        }

        obj.start();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ResizeAnimatorListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public static abstract class ResizeAnimatorListener implements AnimatorListener {
        private ObjectAnimator obj;

        public ObjectAnimator getObjectAnimator() {
            return obj;
        }

        public void setObjectAnimator(ObjectAnimator obj) {
            this.obj = obj;
        }

        @Override
        public void onAnimationRepeat(Animator arg0) {
        }

        @Override
        public void onAnimationCancel(Animator arg0) {
        }
    }
}
