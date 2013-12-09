/*
 * DlgCalendar.java
 * Copyright 2013 OBIGO All rights reserved.
 *             http://www.obigo.com
 */
package net.sarangnamu.common.ui.dlg;

import java.util.Calendar;

import android.support.v4.app.FragmentActivity;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;

public class DlgCalendar implements CalendarDatePickerDialog.OnDateSetListener {
    protected int baseColor;

    protected CalendarDatePickerDialog dlg;
    protected DlgCalendarListener listener;


    public DlgCalendar() {

    }

    public void show(FragmentActivity act) {
        Calendar c = Calendar.getInstance();

        int year  = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day   = c.get(Calendar.DATE);

        dlg = CalendarDatePickerDialog.newInstance(this, year, month, day);
        if (baseColor != 0) {
            dlg.setBaseColor(baseColor);
        }
        dlg.show(act.getSupportFragmentManager(), "fragment_date_picker_name");
    }

    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        if (listener != null) {
            listener.ok(year, monthOfYear, dayOfMonth);
        }

        dialog.dismiss();
    }

    public void setOnDlgCalendarListener(DlgCalendarListener l) {
        listener = l;
    }

    public void setBaseColor(int color) {
        baseColor = color;
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
