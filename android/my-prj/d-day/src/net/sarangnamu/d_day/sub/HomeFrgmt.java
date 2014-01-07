/*
 * HomeFrgmt.java
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

import net.sarangnamu.common.sqlite.DbManager;
import net.sarangnamu.common.ui.list.AniBtnListView;
import net.sarangnamu.d_day.Navigator;
import net.sarangnamu.d_day.R;
import net.sarangnamu.d_day.db.DbHelper;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeFrgmt extends SubBaseFrgmt implements View.OnClickListener {
    private static final String TAG = "HomeFrgmt";

    private static final int SLIDING_MARGIN = 120;
    private ScheduleAdapter adapter;

    private TextView empty;
    private AniBtnListView list;


    @Override
    protected int getLayoutId() {
        return R.layout.page_home;
    }

    @Override
    public void onResume() {
        DbManager.getInstance().open(getActivity(), new DbHelper(getActivity()));

        super.onResume();
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        empty = (TextView) base.findViewById(android.R.id.empty);
        list  = (AniBtnListView) base.findViewById(android.R.id.list);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DbManager.getInstance().open(getActivity(), new DbHelper(getActivity()));
        initListView();
    }

    @Override
    protected void onAddButton() {
        Navigator.getInstance(getActivity()).replace(R.id.content, AddFrgmt.class);
    }

    private void initListView() {
        Cursor cr = DbHelper.selectDesc();

        if (cr != null) {
            adapter = new ScheduleAdapter(getActivity(), cr);
        }

        list.setEmptyView(empty);
        list.setAdapter(adapter);
        list.setSlidingMargin(SLIDING_MARGIN);
        list.setBtnLayoutId(R.id.btnLayout);
        list.setRowId(R.id.row);
    }

    private void deleteItem(final int id) {
        boolean res = DbHelper.delete(id);

        if (res) {
            Cursor cr = DbHelper.selectDesc();
            adapter.changeCursor(cr);

            showPopup(getString(R.string.deleted));
        }
    }

    private void modifyItem(final int id) {
        Bundle bd = new Bundle();
        bd.putInt("id", id);

        Navigator.getInstance(getActivity()).replace(R.id.content, AddFrgmt.class, bd);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ADAPTER
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class ViewHolder {
        TextView count, date, title, reminder, alarm, modify, delete, description;
        LinearLayout btnLayout;
        RelativeLayout row;
    }

    //    class DetailType {
    //        String emsNum;
    //        View row;
    //
    //        DetailType(String emsNum, View row) {
    //            this.emsNum = emsNum;
    //            this.row = row;
    //        }
    //    }

    class ScheduleAdapter extends CursorAdapter {
        private Date currDate;

        public ScheduleAdapter(Context context, Cursor c) {
            super(context, c);

            Date tmpDate = new Date();
            currDate = new Date(tmpDate.getYear(), tmpDate.getMonth(), tmpDate.getDate());
        }

        @Override
        public void bindView(View view, Context context, Cursor cr) {
            ViewHolder vh = new ViewHolder();

            vh.count       = (TextView) view.findViewById(R.id.count);
            vh.title       = (TextView) view.findViewById(R.id.title);
            vh.date        = (TextView) view.findViewById(R.id.date);
            vh.reminder    = (TextView) view.findViewById(R.id.reminder);
            vh.alarm       = (TextView) view.findViewById(R.id.alarm);
            vh.delete      = (TextView) view.findViewById(R.id.delete);
            vh.modify      = (TextView) view.findViewById(R.id.modify);
            vh.description = (TextView) view.findViewById(R.id.description);
            vh.btnLayout   = (LinearLayout) view.findViewById(R.id.btnLayout);
            vh.row         = (RelativeLayout) view.findViewById(R.id.row);

            int pos = 0;
            int id = cr.getInt(pos++);

            vh.delete.setTag(id);
            vh.modify.setTag(id);
            vh.delete.setOnClickListener(HomeFrgmt.this);
            vh.modify.setOnClickListener(HomeFrgmt.this);
            vh.title.setText(cr.getString(pos++));

            long date = Long.parseLong(cr.getString(pos++));
            long gap = (currDate.getTime() - date) / 1000 / 86400;

            vh.description.setText(cr.getString(pos++));

            int remainder = cr.getInt(pos++);
            int alarm = cr.getInt(pos++);

            ////////////////////////////////////////////////////////////////////////////////////
            //
            // REMAINDER
            //
            ////////////////////////////////////////////////////////////////////////////////////

            switch (remainder) {
            case 0:
                if (gap == 0) {
                    vh.count.setText(R.string.today);
                } else if (gap < 0) {
                    vh.count.setText("" + gap);
                } else {
                    vh.count.setText("+" + gap);
                }

                vh.reminder.setText("");
                break;

            case 1: {
                int cal = 365 - (int) (gap % 365);
                if (cal != 365) {
                    vh.count.setText("-" + cal);
                } else {
                    vh.count.setText(R.string.today);
                }

                vh.reminder.setVisibility(View.VISIBLE);
                vh.reminder.setText(R.string.year);
            }
            break;

            case 2:
                vh.count.setText((remainder != 1 ? "+" : "")  + (gap + 1)  + "");
                vh.reminder.setVisibility(View.VISIBLE);
                vh.reminder.setText(R.string.day100);
                break;
            }

            vh.date.setText(DateFormat.getDateInstance().format(new Date(date)));

            ////////////////////////////////////////////////////////////////////////////////////
            //
            // ALARM
            //
            ////////////////////////////////////////////////////////////////////////////////////

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) vh.alarm.getLayoutParams();
            switch (alarm) {
            case 1:
                vh.alarm.setVisibility(View.VISIBLE);
                vh.alarm.setText(R.string.alarm);
                lp.leftMargin = dpToPixelInt(5);
                break;

            default:
                vh.alarm.setVisibility(View.INVISIBLE);
                lp.leftMargin = 0;
                break;
            }

            vh.alarm.setLayoutParams(lp);
            vh.modify.setOnClickListener(HomeFrgmt.this);
            vh.row.setOnClickListener(HomeFrgmt.this);

            view.setTag(vh);
        }

        @Override
        public View newView(Context context, Cursor arg1, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.home_row, parent, false);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // View.OnClickListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onClick(View v) {
        if (v instanceof RelativeLayout) {
            list.showAnimation(v);
        } else {
            Object tag = v.getTag();

            if (v instanceof TextView) {
                TextView tv = (TextView) v;

                if (tag instanceof Integer) {
                    String msg = tv.getText().toString();
                    if (msg.equals(getString(R.string.modify))) {
                        modifyItem((Integer) tag);
                    } else if (msg.equals(getString(R.string.delete))) {
                        deleteItem((Integer) tag);
                    }
                }
            }
        }
    }
}
