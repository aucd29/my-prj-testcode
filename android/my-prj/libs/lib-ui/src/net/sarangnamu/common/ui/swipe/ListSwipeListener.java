/*
 * ListSwipeListener.java
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
package net.sarangnamu.common.ui.swipe;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ListView;

/**
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public abstract class ListSwipeListener extends SwipeListenerBase {
    private ListView mList;
    protected float mScaleFactor = 1.f;
    protected ScaleGestureDetector mScaleDetector;

    public ListSwipeListener(Context context, ListView list) {
        super(context);

        this.mList = list;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleDetector());
    }

    @Override
    protected int getPosition(int x, int y) {
        return mList.pointToPosition(x, y) - mList.getFirstVisiblePosition();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mScaleDetector != null && event.getPointerCount() > 1) {
            mScaleDetector.onTouchEvent(event);
            return true;
        }

        return super.onTouch(v, event);
    }

    class ScaleDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

            if (mScaleFactor > 1.5f) {
                mScaleFactor = 1.f;

                int x = (int) detector.getFocusX();
                int y = (int) detector.getFocusY();
                int pos = getPosition(x, y);

                onPinchZoom(pos);
            }

            return true;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public abstract void onPinchZoom(int position);
}
