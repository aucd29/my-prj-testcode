/*
 * ListAdapterBase.java
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package com.sarangnamu.test_fragments.common;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListAdapterBase extends BaseAdapter {
    private static final String TAG = "ListAdapterBase";

    protected Context context;
    protected ListItemLayoutInfo layoutInfo;
    protected ArrayList<ListItemLayoutInfo> items = new ArrayList<ListItemLayoutInfo>();

    public ListAdapterBase(Context context, ListItemLayoutInfo layoutInfo) {
        super();

        this.context = context;
        this.layoutInfo = layoutInfo;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View currentView;

        if (convertView == null) {
            try {
                currentView = LayoutInflater.from(context).inflate(layoutInfo.layoutId, null);
            } catch (Exception e) {
                Log.e(TAG, "getView", e);
            }
        }

        return null;
    }


}
