/*
 * AddFrgmt.java
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

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.FrgmtBase;
import net.sarangnamu.common.ui.dlg.DlgCalendar;
import net.sarangnamu.common.ui.dlg.DlgCalendar.DlgCalendarListener;
import net.sarangnamu.d_day.R;
import android.view.View;
import android.widget.TextView;

public class AddFrgmt extends FrgmtBase {
    private static final String TAG = "AddFrgmt";

    private TextView eventDate;

    @Override
    protected int getLayoutId() {
        return R.layout.page_add;
    }

    @Override
    protected void initLayout() {
        eventDate = (TextView) base.findViewById(R.id.eventDate);
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DlgCalendar dlg = new DlgCalendar();
                dlg.setBaseColor(getResources().getColor(R.color.dBg));
                dlg.setOnDlgCalendarListener(new DlgCalendarListener() {
                    @Override
                    public void ok(int year, int month, int day) {
                        eventDate.setText(year + "-" + month + "-" + day);

                        DLog.d(TAG, year + "-" + month + "-" + day);
                    }
                });
                dlg.show(getActivity());
            }
        });
    }
}
