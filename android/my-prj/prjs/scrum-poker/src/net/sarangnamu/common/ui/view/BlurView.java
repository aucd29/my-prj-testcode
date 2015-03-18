/*
 * BlurTextView.java
 * Copyright 2015 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.common.ui.view;

import net.sarangnamu.common.ui.LinearBase;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.View;

public class BlurView extends LinearBase implements View.OnClickListener {
    private static final String TAG = "BlurView";

    private boolean parentDrawn = false;

    protected Bitmap parentBm;
    protected Bitmap blurBm;
    protected Canvas blurCanvas;
    protected Allocation in;
    protected Allocation out;
    protected RenderScript rs;
    protected ScriptIntrinsicBlur scriptBlur;

    public BlurView(Context context) {
        super(context);
        initLayout();
    }

    public BlurView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public BlurView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout();
    }

    @Override
    protected void initLayout() {
        setLayerType(View.LAYER_TYPE_NONE, null);
        setBackgroundColor(0x00ffffff);

        rs = RenderScript.create(getContext());
        scriptBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        setWillNotDraw(false);
    }

    @Override
    protected void callGlobalLayout() {
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        createBitmaps();
    }

    private void createBitmaps() {
        parentBm = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        blurBm = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        in = Allocation.createFromBitmap(rs, parentBm);
        out = Allocation.createFromBitmap(rs, blurBm);
        blurCanvas = new Canvas(parentBm);
    }

    private void blur() {
        scriptBlur.setRadius(25);
        scriptBlur.setInput(in);
        scriptBlur.forEach(out);

        out.copyTo(blurBm);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        View v = (View) getParent();

        if (parentDrawn) {
            return;
        }

        parentDrawn = true;
        drawParentInBitmap(v);
        blur();
        canvas.drawBitmap(blurBm, 0, 0, null);

        super.draw(canvas);
        parentDrawn = false;
    }

    private void drawParentInBitmap(View v) {
        blurCanvas.save();
        blurCanvas.translate(-getLeft(), -getTop());
        v.draw(blurCanvas);
        blurCanvas.restore();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // View.OnClickListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onClick(View v) {
        Animator ani = ObjectAnimator.ofFloat(this, "alpha", 0);
        ani.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) { }
            @Override
            public void onAnimationRepeat(Animator animation) { }
            @Override
            public void onAnimationCancel(Animator animation) { }
            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(View.GONE);
            }
        });
        ani.start();
    }
}
