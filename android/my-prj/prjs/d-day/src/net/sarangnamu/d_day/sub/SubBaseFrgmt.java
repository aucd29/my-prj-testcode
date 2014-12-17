/*
 * SubBaseFrgmt.java
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
package net.sarangnamu.d_day.sub;

import net.sarangnamu.common.FrgmtBase;
import net.sarangnamu.common.fonts.FontLoader;
import net.sarangnamu.d_day.MainActivity;
import net.sarangnamu.d_day.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class SubBaseFrgmt extends FrgmtBase {
    protected Button add;
    protected TextView title;
    protected RelativeLayout titleBar;

    @Override
    protected void initLayout() {
        base.setClickable(true);

        add      = (Button) base.findViewById(R.id.add);
        title    = (TextView) base.findViewById(R.id.title);
        titleBar = (RelativeLayout) base.findViewById(R.id.titleBar);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FontLoader.getInstance(getActivity()).applyChild("Roboto-Light", titleBar);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButton();
            }
        });
    }

    protected void showPopup(String msg) {
        if (getActivity() == null) {
            return ;
        }

        ((MainActivity) getActivity()).showPopup(msg);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    ////////////////////////////////////////////////////////////////////////////////////

    protected abstract void onAddButton();
}
