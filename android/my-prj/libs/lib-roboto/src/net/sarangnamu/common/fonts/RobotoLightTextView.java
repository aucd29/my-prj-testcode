/*
 * RobotoLightTextView.java
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

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * <pre>
 * {@code
    <net.sarangnamu.common.fonts.RobotoLightTextView
        android:id="@+id/id"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/sarangnamu"
        />
 * }
 * </pre>
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class RobotoLightTextView extends TextView {
    public RobotoLightTextView(Context context) {
        super(context);
        initLayout();
    }

    public RobotoLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public RobotoLightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout();
    }

    protected void initLayout() {
        setTypeface(FontLoader.getInstance(getContext()).getFont("Roboto-Light"));
    }
}
