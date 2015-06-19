/*
 * MainFrgmt.java
 * Copyright 2014 Burke Choi All right reserverd.
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
package net.sarangnamu.scrum_poker.page.sub;

import java.util.ArrayList;

import net.sarangnamu.common.FrgmtBase;
import net.sarangnamu.scrum_poker.R;
import net.sarangnamu.scrum_poker.cfg.Cfg;
import net.sarangnamu.scrum_poker.page.PageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class MainFrgmt extends FrgmtBase {
    private static final String TAG = "MainFrgmt";

    private GridView mGrid;
    private ArrayList<String> mDefaultNumberList;

    @Override
    protected int getLayoutId() {
        return R.layout.page_main;
    }

    @Override
    protected void initLayout() {
        mGrid = (GridView) mBaseView.findViewById(R.id.grid);
        int measureWidth = mGrid.getMeasuredWidth();

        initDefaultValue();
        initAdapter();
    }

    private void initDefaultValue() {
        if (mDefaultNumberList == null) {
            mDefaultNumberList = new ArrayList<String>();
        }

        if (mDefaultNumberList.size() > 0) {
            return;
        }

        mDefaultNumberList.add("0");
        mDefaultNumberList.add("1/2");
        mDefaultNumberList.add("2");

        mDefaultNumberList.add("3");
        mDefaultNumberList.add("5");
        mDefaultNumberList.add("8");

        mDefaultNumberList.add("13");
        mDefaultNumberList.add("20");
        mDefaultNumberList.add("30");

        mDefaultNumberList.add("60");
        mDefaultNumberList.add("?");
        mDefaultNumberList.add("∞");
    }

    private void initAdapter() {
        mGrid.setAdapter(new ScrumAdapter());
        mGrid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bd = new Bundle();
                bd.putString(Cfg.SCRUM_DATA, mDefaultNumberList.get(position));

                PageManager.getInstance(getActivity()).replace(R.id.content_frame, CardFrgmt.class, bd);
            }
        });
    }

    public void reloadPage() {
        int primaryKey = Integer.parseInt(Cfg.get(getActivity(), Cfg.DB_ID, ""));
        //DBHelper.select(
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // SCRUM ADAPTER
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class ViewHolder {
        TextView number;
    }

    class ScrumAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (mDefaultNumberList == null) {
                return 0;
            }

            return mDefaultNumberList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.page_main_scrum_item, null);

                holder = new ViewHolder();
                holder.number = (TextView) convertView.findViewById(R.id.number);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.number.setText(mDefaultNumberList.get(position));

            return convertView;
        }
    }
}

