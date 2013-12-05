/*
 * AniBtnListView.java
 * Copyright 2013 Burke.Choi All rights reserved.
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
package net.sarangnamu.common.ui.list;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.DimTool;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * {@code
 * <pre>
    - xml
    <net.sarangnamu.common.ui.list.AniBtnListView
        android:id="@android:id/list"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@android:color/white"
        android:drawSelectorOnTop="false" />

    - row xml
    <RelativeLayout
        android:id="@+id/row"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:padding="5dp" >

        <TextView
            android:id="@+id/emsNum"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentTop="true"
            android:textColor="@android:color/black"
            android:textSize="17sp"
            android:textStyle="bold" />
    </RelativeLayout>

    <LinearLayout
        android:id="@+id/btnLayout"
        android:layout_width="124dp"
        android:layout_height="62dp"
        android:layout_alignParentRight="true"
        android:layout_marginRight="-124dp"
        android:orientation="horizontal" >

        <TextView
            android:id="@+id/detail"
            style="@style/btnLayout"
            android:layout_width="62dp"
            android:layout_height="match_parent"
            android:background="#acacac"
            android:text="@string/detail"
            android:textColor="#ffffff" />

        <TextView
            android:id="@+id/delete"
            style="@style/btnLayout"
            android:layout_width="62dp"
            android:layout_height="match_parent"
            android:background="#ed594e"
            android:text="@string/delete"
            android:textColor="#ffffff" />
    </LinearLayout>

    - code
    private static final int SLIDING_MARGIN = 130;

    AniBtnListView list = (AniBtnListView) getListView();
    list.setSlidingMargin(SLIDING_MARGIN);
    list.setBtnLayoutId(R.id.btnLayout);
    list.setRowId(R.id.row);

    - row
    holder.row.setOnClickListener(MainActivity.this);

    - click
    ((AniBtnListView) getListView()).showAnimation(v);
 * </pre>}
 * 
 * @author <a href="mailto:aucd29@gmail.com.com">Burke Choi</a>
 */
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
