/*
 * FontLoader.java
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
package net.sarangnamu.common.fonts;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * <pre>
 * {@code
 * FontLoader.getInstance(this).applyChild("Roboto-Light", layout, Button.class);
 * }
 * </pre>
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class FontLoader {
    private Context mContext;
    private HashMap<String, Typeface> mFontList = new HashMap<String, Typeface>();
    private static FontLoader sInst = null;

    public static FontLoader getInstance(Context context) {
        if (sInst == null) {
            sInst = new FontLoader();
        }

        sInst.setContext(context);

        return sInst;
    }

    private FontLoader() {

    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public Typeface getFont(String name) {
        Typeface typeface = mFontList.get(name);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + name + ".ttf");
            if (typeface != null) {
                mFontList.put(name, typeface);
            }
        }

        return typeface;
    }

    public Typeface getRobotoLight() {
        return getFont("Roboto-Light");
    }

    public void applyChild(String fontName, ViewGroup target, Class<?> type) {
        int count = target.getChildCount();
        Typeface tf = getFont(fontName);

        for (int i=0; i<count; ++i) {
            View child = target.getChildAt(i);

            if (child.getClass().getSimpleName().equals(type.getSimpleName())) {
                ((TextView) child).setTypeface(tf);
            }
        }
    }

    public void applyChild(String fontName, ViewGroup target) {
        int count = target.getChildCount();
        Typeface tf = getFont(fontName);

        for (int i=0; i<count; ++i) {
            View child = target.getChildAt(i);

            if (child instanceof TextView) {
                ((TextView) child).setTypeface(tf);
            } else if (child instanceof ViewGroup) {
                applyChild(fontName, ((ViewGroup) child));
            }
        }
    }
}
