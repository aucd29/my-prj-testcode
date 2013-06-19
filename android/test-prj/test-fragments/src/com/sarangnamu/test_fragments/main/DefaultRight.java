/*
 * DefaultRight.java
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package com.sarangnamu.test_fragments.main;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sarangnamu.test_fragments.R;
import com.sarangnamu.test_fragments.common.FragmentEventListener;
import com.sarangnamu.test_fragments.common.ListItemBase;
import com.sarangnamu.test_fragments.common.ListItemLayoutInfo;
import com.sarangnamu.test_fragments.common.TitleListView;

public class DefaultRight extends Fragment implements FragmentEventListener {
    private static final String TAG = "DefaultRight";
    private TitleListView message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_default_right, null);
        view.setBackgroundColor(0xff0000ff);

        message = (TitleListView)view.findViewById(R.id.message);
        message.setTitleBackgroundResource(R.drawable.dashboard_title_color);
        message.setLayoutInfo(getLayoutInfo());
        message.setTitleText("MESSAGE", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick");
            }
        });

        return view;
    }

    private ListItemLayoutInfo getLayoutInfo() {
        ListItemLayoutInfo info = new ListItemLayoutInfo(R.layout.list_message);
        info.setImageId(R.id.image_xx);
        info.addTextId(R.id.title_xx);

        ArrayList<ListItemBase> datas = new ArrayList<ListItemBase>();
        ListItemBase val = new ListItemBase();
        val.texts.add("hello");
        datas.add(val);

        ListItemBase val2 = new ListItemBase();
        val2.texts.add("hello2");
        datas.add(val2);

        ListItemBase val3 = new ListItemBase();
        val3.texts.add("hello3");
        datas.add(val3);

        info.setItems(datas);

        return info;
    }

    @Override
    public void sendMessage(String msg) {
        Log.e(TAG, "SEND MESSAGE = " + msg);
    }
}
