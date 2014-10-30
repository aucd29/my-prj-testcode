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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

public class EditGridView extends GridView {
    private int maxCount = 2;
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
    }

    public void setMax(int max) {
        this.maxCount = max;

        ((BaseAdapter)getAdapter()).notifyDataSetChanged();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ADAPTER
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class ViewHolder {
        EditText edit;
        Button btn;
    }

    class EditGridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return maxCount;
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
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            EditGridData data = dataList.get(position);

            if (convertView == null) {
                holder = new ViewHolder();
            } else {

            }


            return null;
        }
    }
}
