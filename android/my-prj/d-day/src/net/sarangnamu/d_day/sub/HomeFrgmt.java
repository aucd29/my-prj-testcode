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
    private static final int SLIDING_MARGIN = 124;
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
        adapter = new ScheduleAdapter(getActivity(), DbHelper.selectDesc());

        list.setEmptyView(empty);
        list.setAdapter(adapter);
        list.setSlidingMargin(SLIDING_MARGIN);
        list.setBtnLayoutId(R.id.btnLayout);
        list.setRowId(R.id.row);
    }

    //    private void deleteItem(final int id) {
    //        boolean res = EmsDbHelper.delete(id);
    //
    //        if (res) {
    //            Cursor cr = EmsDbHelper.selectDesc();
    //            adapter.changeCursor(cr);
    //
    //            showPopup(getString(R.string.deleted));
    //        }
    //    }


    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ADAPTER
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class ViewHolder {
        TextView count, date, title, reminder, alarm, detail, delete;
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

            currDate = new Date();
        }

        @Override
        public void bindView(View view, Context context, Cursor cr) {
            ViewHolder vh = new ViewHolder();

            vh.count     = (TextView) view.findViewById(R.id.count);
            vh.title     = (TextView) view.findViewById(R.id.title);
            vh.date      = (TextView) view.findViewById(R.id.date);
            vh.reminder  = (TextView) view.findViewById(R.id.reminder);
            vh.alarm     = (TextView) view.findViewById(R.id.alarm);
            vh.delete    = (TextView) view.findViewById(R.id.delete);
            vh.detail    = (TextView) view.findViewById(R.id.detail);
            vh.btnLayout = (LinearLayout) view.findViewById(R.id.btnLayout);
            vh.row       = (RelativeLayout) view.findViewById(R.id.row);

            int pos = 0;

            vh.delete.setTag(cr.getInt(pos++));
            vh.delete.setOnClickListener(HomeFrgmt.this);
            vh.title.setText(cr.getString(pos++));

            long date = Long.parseLong(cr.getString(pos++));
            long gap = (currDate.getTime() - date) / 1000 / 86400;
            vh.count.setText("+" + (gap + 1)  + "");
            vh.count.setBackgroundColor(0xffdedede);
            vh.date.setText(DateFormat.getDateInstance().format(new Date(date)));
            pos++;

            vh.reminder.setText(cr.getInt(pos++) + "");
            vh.alarm.setText(cr.getInt(pos++) + "");

            vh.detail.setOnClickListener(HomeFrgmt.this);
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
    }
}
