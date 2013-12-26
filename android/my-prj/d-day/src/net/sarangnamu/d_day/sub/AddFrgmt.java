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

import java.text.DateFormat;
import java.util.Date;

import net.sarangnamu.common.BkCfg;
import net.sarangnamu.common.DLog;
import net.sarangnamu.common.fonts.FontLoader;
import net.sarangnamu.common.ui.dlg.DlgCalendar;
import net.sarangnamu.common.ui.dlg.DlgCalendar.DlgCalendarListener;
import net.sarangnamu.d_day.Navigator;
import net.sarangnamu.d_day.R;
import net.sarangnamu.d_day.db.DbHelper;
import net.sarangnamu.d_day.db.ScheduleData;
import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class AddFrgmt extends SubBaseFrgmt {
    private static final String TAG = "AddFrgmt";

    private int id;
    private Date dateTime;
    private TextView eventDate;
    private EditText eventTitle, eventDescription;
    private RadioGroup eventType, eventAlarm;
    private RadioButton rdoTypeNone, rdoYear, rdoDay, rdoAlarmNone, rdoAlarm;
    private LinearLayout addLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.page_add;
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        eventDate        = (TextView) base.findViewById(R.id.eventDate);
        eventTitle       = (EditText) base.findViewById(R.id.eventTitle);
        eventDescription = (EditText) base.findViewById(R.id.eventDescription);
        eventType        = (RadioGroup) base.findViewById(R.id.eventType);
        eventAlarm       = (RadioGroup) base.findViewById(R.id.eventAlarm);
        rdoTypeNone      = (RadioButton) base.findViewById(R.id.rdoTypeNone);
        rdoYear          = (RadioButton) base.findViewById(R.id.rdoYear);
        rdoDay           = (RadioButton) base.findViewById(R.id.rdoDay);
        rdoAlarmNone     = (RadioButton) base.findViewById(R.id.rdoAlarmNone);
        rdoAlarm         = (RadioButton) base.findViewById(R.id.rdoAlarm);
        addLayout        = (LinearLayout) base.findViewById(R.id.addLayout);

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DlgCalendar dlg = new DlgCalendar();
                dlg.setBaseColor(getResources().getColor(R.color.dBg));
                dlg.setOnDlgCalendarListener(new DlgCalendarListener() {
                    @Override
                    public void ok(int year, int month, int day) {
                        DateFormat df = DateFormat.getDateInstance();

                        dateTime = new Date(year-1900, month-1, day);
                        eventDate.setText(df.format(dateTime));
                        eventDate.setTextColor(0xff000000);
                    }
                });

                if (dateTime == null) {
                    dlg.show(getActivity());
                } else {
                    dlg.show(getActivity(), dateTime.getYear()+1900, dateTime.getMonth(), dateTime.getDate());
                }
            }
        });

        Bundle bd = getArguments();
        if (bd != null) {
            id = bd.getInt("id");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FontLoader.getInstance(getActivity()).applyChild("Roboto-Light", addLayout);

        title.setText(R.string.addSchedule);
        add.setText(R.string.done);
        add.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        if (id != 0) {
            int pos = 0;
            Cursor cr = DbHelper.select(id);
            if (cr == null) {
                DLog.e(TAG, "===================================================================");
                DLog.e(TAG, "onActivityCreated cr == null");
                DLog.e(TAG, "===================================================================");
                return ;
            }

            pos++; // id

            eventTitle.setText(cr.getString(pos++));
            dateTime = new Date(Long.parseLong(cr.getString(pos++)));
            eventDate.setText(DateFormat.getDateInstance().format(dateTime));
            eventDate.setTextColor(0xff000000);
            eventDescription.setText(cr.getString(pos++));

            int remainder = cr.getInt(pos++);
            int alarm = cr.getInt(pos++);

            switch (remainder) {
            case 1:  rdoYear.performClick();     break;
            case 2:  rdoDay.performClick();      break;
            default: rdoTypeNone.performClick(); break;
            }

            switch (alarm) {
            case 1: rdoAlarm.performClick(); break;
            default: rdoAlarmNone.performClick(); break;
            }
        }
    }

    @Override
    protected void onAddButton() {
        ScheduleData data = new ScheduleData();

        if (dateTime == null) {
            showPopup(getString(R.string.plsInsertDate));
            return;
        }

        data.title       = eventTitle.getText().toString();
        data.date        = dateTime.getTime();
        data.description = eventDescription.getText().toString();
        data.reminder    = getCheckedEventType();
        data.alarm       = getCheckedAlarmType();

        if (data.title == null || data.title.length() == 0) {
            showPopup(getString(R.string.plsInsertTitle));
            return ;
        }

        if (id != 0) {
            if (DbHelper.update(id, data)) {
                Navigator.getInstance(getActivity()).popBack();
            } else {
                // error
            }
        } else {
            if (DbHelper.insert(data)) {
                Navigator.getInstance(getActivity()).popBack();
            } else {
                // error
            }
        }

        BkCfg.hideKeyboard(eventTitle);
    }

    private int getCheckedEventType() {
        int id = eventType.getCheckedRadioButtonId();

        if (id == rdoTypeNone.getId()) {
            return 0;
        } else if (id == rdoYear.getId()) {
            return 1;
        } else if (id == rdoDay.getId()) {
            return 2;
        }

        return -1;
    }

    private int getCheckedAlarmType() {
        int id = eventAlarm.getCheckedRadioButtonId();

        if (id == rdoAlarmNone.getId()) {
            return 0;
        } else if (id == rdoAlarm.getId()) {
            return 1;
        }

        return -1;
    }
}
