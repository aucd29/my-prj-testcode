package net.sarangnamu.ui_test.common;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ListView;

/**
 * 
 * @author @aucd29
 *
 */
public abstract class ListSwipeListener extends SwipeListenerBase {
    private static final String TAG = "ListSwipeListener";

    private ListView list;
    protected float scaleFactor = 1.f;
    protected ScaleGestureDetector scaleDetector;

    public ListSwipeListener(Context context, ListView list) {
        super(context);

        this.list = list;
        scaleDetector = new ScaleGestureDetector(context, new ScaleDetector());
    }

    @Override
    protected int getPosition(int x, int y) {
        return list.pointToPosition(x, y) - list.getFirstVisiblePosition();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (scaleDetector != null && event.getPointerCount() > 1) {
            scaleDetector.onTouchEvent(event);
            return true;
        }

        return super.onTouch(v, event);
    }

    class ScaleDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f));

            if (scaleFactor > 1.5f) {
                scaleFactor = 1.f;

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
