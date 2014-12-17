/*
 * ColorSelector.java
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
package net.sarangnamu.common.ui;

import java.util.HashMap;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;

public class ColorSelector {
    private static final String TAG = "ColorSelector";

    private HashMap<Integer, ColorDrawable> mapColors;
    private int normal, pressed, disabled;

    public StateListDrawable getSelector(int[] colors) {
        StateListDrawable states = new StateListDrawable();

        if (colors == null) {
            Log.e(TAG, "loadColor <colors == null>");
            return null;
        }

        setColorIndex(colors.length);

        for (int i=0; i<colors.length; ++i) {
            if (i == normal) {
                states.addState(new int[] { }, getDrawable(colors[i]));
            } else if (i == pressed) {
                states.addState(new int[] { android.R.attr.state_pressed }, getDrawable(colors[i]));
            } else if (i == disabled) {
                states.addState(new int[] { -android.R.attr.state_enabled }, getDrawable(colors[i]));
            }
        }

        return states;
    }

    private void setColorIndex(int normalValue) {
        normal   = normalValue - 1;
        pressed  = normal - 1;
        disabled = pressed - 1;
    }

    public Drawable getDrawable(int color) {
        if (mapColors == null) {
            mapColors = new HashMap<Integer, ColorDrawable>();
        }

        if (mapColors.get(color) == null) {
            mapColors.put(color, new ColorDrawable(color));
        }

        return mapColors.get(color);
    }
}
