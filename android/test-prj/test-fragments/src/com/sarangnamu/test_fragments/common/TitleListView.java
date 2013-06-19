/*
 * TitleListView.java
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package com.sarangnamu.test_fragments.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * <pre>
 * ListItemLayoutInfo info = new ListItemLayoutInfo(R.layout.test_layout);
 * info.setImageId(R.id.imageId);
 * info.addTextId(R.id.txt1);
 * info.addTextId(R.id.txt2);
 * info.addTextId(R.id.txt3);
 * 
 * ListItemBase items = new ListItemBase();
 * items.texts.add("1");
 * items.texts.add("2");
 * items.texts.add("3");
 * 
 * TitleListView list = (TitleListView)findViewId(R.id.list);
 * list.setLayoutInfo(info);
 * list.setListItems(items);
 * </pre>
 * 
 * @author @aucd29
 *
 */
public class TitleListView extends TitleLayout {
    private static final String TAG = "TitleListView";

    protected ListView list;
    protected ListAdapterBase adapter;
    protected ListItemLayoutInfo info;
    protected View.OnClickListener click = null;

    public TitleListView(Context context) {
        super(context);
    }

    public TitleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        list = new ListView(getContext());
        list.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        addView(list);
    }

    public void setLayoutInfo(ListItemLayoutInfo info) {
        this.info = info;

        adapter = new ListAdapterBase();
        list.setAdapter(adapter);
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    public void setOnClickEvent(View.OnClickListener click) {
        this.click = click;
    }

    class ListAdapterBase extends BaseAdapter {
        @Override
        public int getCount() {
            return info.items.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ListLayoutBase view;

            if (convertView == null) {
                view = new ListLayoutBase(getContext(), info);
                view.setOnClickListener(click);
            } else {
                view = (ListLayoutBase)convertView;
            }

            if (info.imageId != -1) {
                view.setImage(position);
            }

            view.setText(position);

            return view;
        }
    }
}
