/*
 * EditGridView.java
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
package net.sarangnamu.common.ui.grid.edit;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

public class EditGridView extends GridView  implements View.OnClickListener {
    private int gridviewResId;
    private ArrayList<EditGridData> dataList;

    public EditGridView(Context context) {
        super(context);
        initLayout();
    }

    public EditGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public EditGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout();
    }

    protected void initLayout() {
        if (dataList == null) {
            dataList = new ArrayList<EditGridData>();
            dataList.add(new EditGridData(EditGridData.TYPE_EDIT, ""));
            dataList.add(new EditGridData(EditGridData.TYPE_BUTTON, ""));
        }

        gridviewResId = R.layout.editgridview_item;

        setAdapter(new EditGridAdapter());
    }

    public void setInflateId(int id) {
        gridviewResId = id;
    }

    protected EditGridViewHolder setViewHolder(View view) {
        EditGridViewHolder holder;

        holder = new EditGridViewHolder();
        holder.edt = (EditText) view.findViewById(R.id.edt);
        holder.btn = (Button) view.findViewById(R.id.btn);

        return holder;
    }

    protected void setAdapterView(EditGridData data, EditGridViewHolder holder) {
        if (data.type == EditGridData.TYPE_BUTTON) {
            holder.btn.setVisibility(View.VISIBLE);
            holder.edt.setVisibility(View.GONE);

            holder.btn.setOnClickListener(this);
        } else if (data.type == EditGridData.TYPE_EDIT) {
            holder.btn.setVisibility(View.GONE);
            holder.edt.setVisibility(View.VISIBLE);

            holder.edt.setText(data.value);
        }
    }

    public ArrayList<EditGridData> getGridData() {
        return dataList;
    }

    public void clearGridData() {
        if (dataList == null) {
            return ;
        }

        dataList.clear();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ADAPTER
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class EditGridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (dataList == null) {
                return 0;
            }

            return dataList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int pos) {
            return dataList.get(pos).type;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            EditGridViewHolder holder;
            EditGridData data = dataList.get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(gridviewResId, null);

                holder = setViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (EditGridViewHolder) convertView.getTag();
            }

            setAdapterView(data, holder);

            return convertView;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // View.OnClickListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onClick(View v) {
        if (dataList == null) {
            dataList = new ArrayList<EditGridData>();
        }

        dataList.get(dataList.size() - 1).type = EditGridData.TYPE_EDIT;
        dataList.add(new EditGridData(EditGridData.TYPE_BUTTON, ""));

        ((BaseAdapter) getAdapter()).notifyDataSetChanged();
    }
}
