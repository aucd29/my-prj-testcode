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

import net.sarangnamu.common.FrgmtBase;
import net.sarangnamu.common.ui.list.AniBtnListView;
import net.sarangnamu.d_day.Navigator;
import net.sarangnamu.d_day.R;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeFrgmt extends FrgmtBase {
    private static final int SLIDING_MARGIN = 124;

    private Button add;
    private TextView title, empty;
    private AniBtnListView list;


    @Override
    protected int getLayoutId() {
        return R.layout.page_home;
    }

    @Override
    protected void initLayout() {
        add   = (Button) base.findViewById(R.id.add);
        title = (TextView) base.findViewById(R.id.title);
        empty = (TextView) base.findViewById(android.R.id.empty);
        list  = (AniBtnListView) base.findViewById(android.R.id.list);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigator.getInstance(getActivity()).show(AddFrgmt.class);
            }
        });
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
    //    @Override
    //    private int dpToPixelInt(int dp) {
    //        return DimTool.dpToPixelInt(MainActivity.this, dp);
    //    }
    //    ////////////////////////////////////////////////////////////////////////////////////
    //    //
    //    // ADAPTER
    //    //
    //    ////////////////////////////////////////////////////////////////////////////////////
    //
    //    class ViewHolder {
    //        TextView emsNum, date, status, office, delete, detail;
    //        LinearLayout btnLayout;
    //        RelativeLayout row;
    //    }
    //
    //    class DetailType {
    //        String emsNum;
    //        View row;
    //
    //        DetailType(String emsNum, View row) {
    //            this.emsNum = emsNum;
    //            this.row = row;
    //        }
    //    }
    //
    //    class EmsAdapter extends CursorAdapter {
    //        public EmsAdapter(Context context, Cursor c) {
    //            super(context, c);
    //        }
    //
    //        @Override
    //        public void bindView(View view, Context context, Cursor cr) {
    //            ViewHolder vh = new ViewHolder();
    //
    //            vh.emsNum    = (TextView) view.findViewById(R.id.emsNum);
    //            vh.date      = (TextView) view.findViewById(R.id.date);
    //            vh.status    = (TextView) view.findViewById(R.id.status);
    //            vh.office    = (TextView) view.findViewById(R.id.office);
    //            vh.delete    = (TextView) view.findViewById(R.id.delete);
    //            vh.detail    = (TextView) view.findViewById(R.id.detail);
    //            vh.btnLayout = (LinearLayout) view.findViewById(R.id.btnLayout);
    //            vh.row       = (RelativeLayout) view.findViewById(R.id.row);
    //
    //            int pos = 0;
    //            vh.delete.setTag(cr.getInt(pos++));
    //            vh.delete.setOnClickListener(MainActivity.this);
    //
    //            String emsNumber = cr.getString(pos++);
    //            vh.emsNum.setText(emsNumber);
    //            vh.date.setText(cr.getString(pos++));
    //
    //            String statusValue = cr.getString(pos++);
    //            vh.status.setText(statusValue);
    //            vh.detail.setTag(new DetailType(emsNumber, vh.row));
    //            vh.detail.setOnClickListener(MainActivity.this);
    //
    //            vh.office.setText(cr.getString(pos++));
    //            vh.row.setOnClickListener(MainActivity.this);
    //
    //            view.setTag(vh);
    //        }
    //
    //        @Override
    //        public View newView(Context context, Cursor arg1, ViewGroup parent) {
    //            return LayoutInflater.from(context).inflate(R.layout.item, parent, false);
    //        }
    //    }
}
