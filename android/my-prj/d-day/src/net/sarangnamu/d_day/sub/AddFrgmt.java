/*
 * AddFrgmt.java
 * Copyright 2013 OBIGO All rights reserved.
 *             http://www.obigo.com
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
                        DLog.d(TAG, year + "-" + month + "-" + day);
                    }
                });
                dlg.show(getActivity());
            }
        });
    }
}
