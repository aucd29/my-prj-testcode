/*
 * DlgCalendar.java
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
package net.sarangnamu.common.ui.dlg;

import java.util.Calendar;

import android.support.v4.app.FragmentActivity;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;

/**
 * <pre>
 * {@code
    DlgCalendar dlg = new DlgCalendar();
    dlg.setBaseColor(getResources().getColor(R.color.dBg));
    dlg.setOnDlgCalendarListener(new DlgCalendarListener() {
        @Override
        public void ok(int year, int month, int day) {
            DLog.d(TAG, year + "-" + month + "-" + day);
        }
    });
    dlg.show(getActivity());
 * }
 * </pre>
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class DlgCalendar implements CalendarDatePickerDialog.OnDateSetListener {
    protected int mBaseColor;

    protected CalendarDatePickerDialog mDlg;
    protected DlgCalendarListener mListener;


    public DlgCalendar() {

    }

    public void show(FragmentActivity act) {
        Calendar c = Calendar.getInstance();

        int year  = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day   = c.get(Calendar.DATE);

        mDlg = CalendarDatePickerDialog.newInstance(this, year, month, day);
        if (mBaseColor != 0) {
            mDlg.setBaseColor(mBaseColor);
        }
        mDlg.show(act.getSupportFragmentManager(), "fragment_date_picker_name");
    }

    public void show(FragmentActivity act, int year, int month, int day) {
        mDlg = CalendarDatePickerDialog.newInstance(this, year, month, day);
        if (mBaseColor != 0) {
            mDlg.setBaseColor(mBaseColor);
        }
        mDlg.show(act.getSupportFragmentManager(), "fragment_date_picker_name");
    }

    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        if (mListener != null) {
            mListener.ok(year, monthOfYear+1, dayOfMonth);
        }

        dialog.dismiss();
    }

    public void setOnDlgCalendarListener(DlgCalendarListener l) {
        mListener = l;
    }

    public void setBaseColor(int color) {
        mBaseColor = color;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // DlgCalendarListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public interface DlgCalendarListener {
        public void ok(int year, int month, int day);
    }
}
