/*
 * BitmapTool.java
 * Copyright 2014 Burke.Choi All rights reserved.
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
package net.sarangnamu.common.ui.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

public class BitmapTool {
    public static Bitmap getBitmapFromView(View view) {
        return getBitmapFromView(view, Bitmap.Config.ARGB_8888);
    }

    public static Bitmap getBitmapFromView(View view, Bitmap.Config bmpCfg) {
        // @see http://stackoverflow.com/questions/2801116/converting-a-view-to-bitmap-without-displaying-it-in-android
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), bmpCfg);

        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }

        view.draw(canvas);

        return returnedBitmap;
    }
}
