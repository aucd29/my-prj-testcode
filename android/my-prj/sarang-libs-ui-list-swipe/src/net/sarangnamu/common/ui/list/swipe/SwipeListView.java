/*
 * SwipeListView.java
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
package net.sarangnamu.common.ui.list.swipe;

import net.sarangnamu.common.ui.gesture.Gesture;
import net.sarangnamu.common.ui.gesture.Gesture.GestureListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class SwipeListView extends ListView {
    private Gesture gesture;

    public SwipeListView(Context context) {
        super(context);
        initLayout();
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout();
    }

    protected void initLayout() {
        gesture = Gesture.newInstance(getContext());
        gesture.setOnGestureListener(new GestureListener() {
            @Override
            public void toUp() {
            }

            @Override
            public void toRight() {
            }

            @Override
            public void toLeft() {
            }

            @Override
            public void toDown() {
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (gesture != null) {
            gesture.onTouchEvent(ev);
        }

        return true;
        //return super.onInterceptTouchEvent(ev);
    }

}
