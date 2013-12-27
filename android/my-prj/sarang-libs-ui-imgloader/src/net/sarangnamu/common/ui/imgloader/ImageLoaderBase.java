/*
 * ImageLoaderBase.java
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
package net.sarangnamu.common.ui.imgloader;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;

public abstract class ImageLoaderBase {
    private static final String TAG = "ImageLoaderBase";

    protected static final String DRAWABLE = "drawable";

    public static final int DISABLED = 0X1;
    public static final int PRESSED  = 0X2;
    public static final int NORMAL   = 0X4;
    public static final int ALL      = (0X1|0X2|0X4);

    public static final int MASK     = 0X1;

    public StateListDrawable loadImage(Context context, String name, int type) {
        int pressed;
        int disabled;
        int normal;

        StateListDrawable states = new StateListDrawable();

        for (int i=0; i<3; ++i) {
            switch (type & (MASK << i)) {
            case NORMAL:
                Log.d(TAG, "NORMAL");
                normal = getDrawableIdByName(context, name + getNormalSuffix());
                states.addState(new int[] { }, getDrawable(context, normal));
                break;

            case PRESSED:
                Log.d(TAG, "PRESSED");
                pressed = getDrawableIdByName(context, name + getPressedSuffix());
                states.addState(new int[] {android.R.attr.state_pressed}, getDrawable(context, pressed));
                break;

            case DISABLED:
                Log.d(TAG, "DISABLED");
                disabled = getDrawableIdByName(context, name + getDisableSuffix());
                states.addState(new int[] {-android.R.attr.state_enabled}, getDrawable(context, disabled));
                break;
            }
        }

        return states;
    }

    protected int getDrawableIdByName(Context context, String name) {
        return context.getResources().getIdentifier(name, DRAWABLE, context.getPackageName());
    }

    protected Drawable getDrawable(Context context, int id) {
        return context.getResources().getDrawable(id);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    ////////////////////////////////////////////////////////////////////////////////////

    protected abstract String getDisableSuffix();
    protected abstract String getPressedSuffix();
    protected abstract String getNormalSuffix();
}
