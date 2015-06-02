/*
 * ContentSlidingDrawerListener.java
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
package net.sarangnamu.common.ui.widget.drawerlayout;

import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.View;
import android.view.animation.TranslateAnimation;

public abstract class ContentSlidingDrawerListener implements DrawerListener {
    protected float mLastTranslate = 0.0f;
    protected TranslateAnimation mAnim;

    @Override
    public void onDrawerClosed(View arg0) {
    }

    @Override
    public void onDrawerOpened(View arg0) {
    }

    @Override
    public void onDrawerStateChanged(int arg0) {
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        // @see http://stackoverflow.com/questions/20057084/how-to-move-main-content-with-drawer-layout-left-side
        float moveFactor = (getListView().getWidth() * slideOffset);
        TranslateAnimation anim = new TranslateAnimation(mLastTranslate, moveFactor, 0.0f, 0.0f);
        anim.setDuration(0);
        anim.setFillAfter(true);
        getContentFrame().startAnimation(anim);

        mLastTranslate = moveFactor;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public abstract View getListView();
    public abstract View getContentFrame();
}
