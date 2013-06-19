/*
 * ListItemLayoutInfo.java
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package com.sarangnamu.test_fragments.common;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

/**
 * 
 * @author @aucd29
 * 
 * <pre>
 * ListItemLayoutInfo info = new ListItemLayoutInfo(R.layout.test_layout);
 * info.setImageId(R.id.imageId);
 * info.addTextId(R.id.txt1);
 * info.addTextId(R.id.txt2);
 * info.addTextId(R.id.txt3);
 * 
 * </pre>
 *
 */
public class ListItemLayoutInfo {
    private static final String TAG = "ListItemLayoutInfo";

    protected int layoutId;
    protected int imageId = -1;
    protected ArrayList<Integer> textIds = new ArrayList<Integer>();
    protected ArrayList<ListItemBase> items = new ArrayList<ListItemBase>();

    public ListItemLayoutInfo(final int id) {
        layoutId = id;
    }

    public void setImageId(final int id) {
        imageId = id;
    }

    public void addTextId(final int id) {
        textIds.add(id);
    }

    public void setItems(final ArrayList<ListItemBase> items) {
        this.items = items;
    }

    public Drawable getDrawable(final int pos) {
        try {
            return items.get(pos).img;
        } catch (Exception e) {
            DLog.e(TAG, "getDrawable", e);
        }

        return null;
    }

    public ArrayList<String> getTexts(final int pos) {
        try {
            return items.get(pos).texts;
        } catch (Exception e) {
            DLog.e(TAG, "getTexts", e);
        }

        return null;
    }
}
