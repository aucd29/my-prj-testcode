/*
 * DlgChecked.java
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

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

/**
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class DlgChecked extends DlgBtnBase {
    protected int layoutId;
    protected String[] items;
    protected ListView list;

    public DlgChecked(Context context, int layoutId) {
        super(context);

        this.list = new ListView(getContext());
        this.list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        this.layoutId = layoutId;
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        content.addView(list);

        if (items != null) {
            list.setAdapter(new DlgAdapter());
        }
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        if (list != null) {
            list.setOnItemClickListener(l);
        }
    }

    public ListView getListView() {
        return list;
    }

    public void setItem(int resid) {
        items = getContext().getResources().getStringArray(resid);
    }

    public void setItem(String[] item) {
        items = item;
    }

    public void setMultiChoice() {
        this.list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    public String[] getCheckedItems() {
        if (list == null) {
            return null;
        }

        SparseBooleanArray spItems = list.getCheckedItemPositions();
        String[] trigger = new String[list.getCheckedItemCount()];

        int j = 0;
        for (int i = 0; i < spItems.size(); i++) {
            if (spItems.get(i) == true) {
                int pos = spItems.keyAt(i);
                trigger[j++] = items[pos];
            }
        }

        return trigger;
    }

    public int getCheckedItemCount() {
        if (list == null) {
            return 0;
        }

        return list.getCheckedItemCount();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // LIST ADAPTER
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class ViewHolder {
        public CheckedTextView checked;
    }

    class DlgAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inflate(layoutId);

                holder = new ViewHolder();
                int editId = convertView.getResources().getIdentifier("checked", "id", getContext().getPackageName());
                holder.checked = (CheckedTextView) convertView.findViewById(editId);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.checked.setText(items[position]);

            return convertView;
        }
    }
}
