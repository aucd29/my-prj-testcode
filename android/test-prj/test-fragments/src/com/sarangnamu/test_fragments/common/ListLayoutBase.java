/*
 * ListLayoutBase.java
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package com.sarangnamu.test_fragments.common;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * <pre>
 * 
 * </pre>
 * @author @aucd29
 *
 */
public class ListLayoutBase extends LinearLayout {
    private static final String TAG = "ListLayoutBase";

    protected ImageView image;
    protected ListItemLayoutInfo info;
    protected ArrayList<TextView> txtViews = new ArrayList<TextView>();

    public ListLayoutBase(final Context context, final ListItemLayoutInfo info) {
        super(context);

        this.info = info;
        LayoutInflater.from(context).inflate(info.layoutId, this, true);

        if (info.imageId != -1) {
            image = (ImageView) findViewById(info.imageId);
        }

        for (Integer id : info.textIds) {
            txtViews.add((TextView) findViewById(id));
        }
    }

    public void setImage(final int pos) {
        if (info.imageId == -1) {
            return ;
        }

        Drawable draw = info.getDrawable(pos);

        if (draw != null) {
            image.setImageDrawable(draw);
        }
    }

    public void setText(final int pos) {
        try {
            ArrayList<String> texts = info.getTexts(pos);
            if (texts == null) {
                DLog.d(TAG, "texts == null");
                return ;
            }

            int size = txtViews.size();
            for (int i=0; i<size; ++i) {
                try {
                    txtViews.get(i).setText(texts.get(i));
                } catch (Exception e) {
                    DLog.e(TAG, "setText", e);
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "setText", e);
        }
    }
}
