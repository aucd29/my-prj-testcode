/*
 * Default.java
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package com.sarangnamu.test_fragments.main;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sarangnamu.test_fragments.R;
import com.sarangnamu.test_fragments.common.FragmentEventListener;


public class Default extends Fragment implements FragmentEventListener {
    private static final String TAG = "Default";

    private FragmentManager manager;
    private DefaultLeft left;
    private DefaultRight right;

    private boolean flag = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_default, null);

        manager = getFragmentManager();

        left  = (DefaultLeft) manager.findFragmentById(R.id.left);
        right = (DefaultRight) manager.findFragmentById(R.id.right);

        return view;
    }

    @Override
    public void sendMessage(String msg) {
        Log.e(TAG, "SEND MESSAGE = " + msg);

        if (flag) {
            left.sendMessage(msg);
        } else {
            right.sendMessage(msg);
        }

        flag = !flag;
    }
}
