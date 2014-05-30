/*
 * ProgressFlat.java
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
package net.sarangnamu.common.ui.progressbar.flat;

import net.sarangnamu.common.ui.progress.flat.R;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class FlatProgressBar extends ProgressBar {
    public FlatProgressBar(Context context) {
        this(context, null, 0);
        initLayout();
    }

    public FlatProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initLayout();
    }

    public FlatProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, android.R.attr.progressBarStyleHorizontal);
        initLayout();
    }

    protected void initLayout() {
        setDefaultType();
    }

    public void setDefaultType() {
        setProgressDrawableResId(R.drawable.libs_progressbar_flat_cyan);
    }

    public void setProgressDrawableResId(int resid) {
        Resources res = getResources();
        setProgressDrawable(res.getDrawable(resid));
    }
}
