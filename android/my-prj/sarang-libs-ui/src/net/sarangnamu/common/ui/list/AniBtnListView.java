/*
 * AniBtnListView.java
 * Copyright 2013 OBIGO All rights reserved.
 *             http://www.obigo.com
 */
package net.sarangnamu.common.ui.list;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.DimTool;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class AniBtnListView extends LockListView {
    private static final String TAG = "AniBtnListView";

    protected int slidingMargin = 0, rowId, btnLayoutId;
    protected View currView;
    protected boolean checkedList;

    public AniBtnListView(Context context) {
        super(context);
    }

    public AniBtnListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AniBtnListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void initLayout() {
        setOnTouchListener(new TouchUpListener() {
            @Override
            public void up() {
                if (currView != null) {
                    showAnimation(currView);
                }
            }
        });
    }

    protected int dpToPixelInt(int dp) {
        return DimTool.dpToPixelInt(getContext(), dp);
    }

    public void setSlidingMargin(int dp) {
        slidingMargin = dpToPixelInt(dp);
    }

    public void setBtnLayoutId(int id) {
        rowId = id;
    }

    public void setRowId(int id) {
        btnLayoutId = id;
    }

    public void showAnimation(final View view) {
        if (rowId == 0 || btnLayoutId == 0) {
            DLog.e(TAG, "showAnimation Please init id " );
            return ;
        }

        final int endX;
        final int moveX = slidingMargin;
        View tempView;

        if (checkedList) {
            endX = 0;
            tempView = (View) currView.getParent();
            currView = null;
        } else {
            endX = moveX * -1;
            tempView = (View) view.getParent();
            currView = view;
        }

        checkedList = !checkedList;
        setLock();

        final View btnLayout = tempView.findViewById(btnLayoutId);
        final View row       = tempView.findViewById(rowId);

        ObjectAnimator.ofFloat(btnLayout, "translationX", endX).start();
        final ObjectAnimator objAni = ObjectAnimator.ofFloat(row, "translationX", endX);
        objAni.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                objAni.removeAllListeners();
                view.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animator animation) { }
            @Override
            public void onAnimationCancel(Animator animation) { }
        });
        objAni.start();
    }
}
