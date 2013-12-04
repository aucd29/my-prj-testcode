/*
 * HomeFrgmt.java
 * Copyright 2013 OBIGO All rights reserved.
 *             http://www.obigo.com
 */
package net.sarangnamu.d_day.sub;

import net.sarangnamu.common.FrgmtBase;
import net.sarangnamu.d_day.R;

public class HomeFrgmt extends FrgmtBase {
    private static final int SLIDING_MARGIN = 124;

    @Override
    protected int getLayoutId() {
        return R.layout.page_home;
    }

    @Override
    protected void initLayout() {
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
