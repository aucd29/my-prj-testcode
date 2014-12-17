/*
 * Gesture.java
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
package net.sarangnamu.common.ui.gesture;

import net.sarangnamu.common.DLog;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * <pre>
 * {@code
    private Gesture gesture;

    gesture = Gesture.newInstance(this);
    gesture.setOnGestureRightListener(new GestureRightListener() {
        @Override
        public void toRight() {
            onBackPressed();
            Navigator.getInstance(MainActivity.this).setCurrentName(Navigator.STUDY);
        }
    });

    otherView.setOnTouchListener(new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gesture.onTouchEvent(event);

            return true;
        }
    });
 * }
 * </pre>
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class Gesture extends GestureDetector {
    private static final String TAG = "Gesture";

    public static int SWIPE_THRESHOLD = 300;
    public static int SWIPE_VELOCITY_THRESHOLD = 300;

    protected GestureDetectorListener gdListener;

    public static Gesture newInstance(Context context) {
        return new Gesture(context, new GestureDetectorListener());
    }

    public Gesture(Context context, GestureDetectorListener gdl) {
        super(context, gdl);

        gdListener = gdl;
    }

    public void setThreshold(int threshold, int velocity) {
        SWIPE_THRESHOLD = threshold;
        SWIPE_VELOCITY_THRESHOLD = velocity;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // GestureDetectorListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public static class GestureDetectorListener implements GestureDetector.OnGestureListener {
        public GestureListener listener;

        public GestureUpListener listenerUp;
        public GestureLeftListener listenerLeft;
        public GestureDownListener listenerDown;
        public GestureRightListener listenerRight;

        public GestureDetectorListener() {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            DLog.d(TAG, "onFling");

            if (e1 == null || e2 == null) {
                return false;
            }

            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        if (listener != null) {
                            listener.toRight();
                        }

                        if (listenerRight != null) {
                            listenerRight.toRight();
                        }
                    } else {
                        if (listener != null) {
                            listener.toLeft();
                        }

                        if (listenerLeft != null) {
                            listenerLeft.toLeft();
                        }
                    }
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        if (listener != null) {
                            listener.toDown();
                        }

                        if (listenerDown != null) {
                            listenerDown.toDown();
                        }
                    } else {
                        if (listener != null) {
                            listener.toUp();
                        }

                        if (listenerUp != null) {
                            listenerUp.toUp();
                        }
                    }
                }
            }

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) { }

        @Override
        public void onShowPress(MotionEvent e) { }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // interfaces
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public void setOnGestureListener(GestureListener l) {
        if (gdListener == null) {
            return ;
        }

        gdListener.listener = l;
    }

    public void setOnGestureLeftListener(GestureLeftListener l) {
        if (gdListener == null) {
            return ;
        }

        gdListener.listenerLeft = l;
    }

    public void setOnGestureUpListener(GestureUpListener l) {
        if (gdListener == null) {
            return ;
        }

        gdListener.listenerUp = l;
    }

    public void setOnGestureRightListener(GestureRightListener l) {
        if (gdListener == null) {
            return ;
        }

        gdListener.listenerRight = l;
    }

    public void setOnGestureDownListener(GestureDownListener l) {
        if (gdListener == null) {
            return ;
        }

        gdListener.listenerDown = l;
    }

    public interface GestureListener {
        public void toLeft();
        public void toUp();
        public void toRight();
        public void toDown();
    }

    public interface GestureLeftListener {
        public void toLeft();
    }

    public interface GestureUpListener {
        public void toUp();
    }

    public interface GestureRightListener {
        public void toRight();
    }

    public interface GestureDownListener {
        public void toDown();
    }
}
