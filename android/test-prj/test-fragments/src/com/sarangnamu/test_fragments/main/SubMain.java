/*
 * SubMain.java
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package com.sarangnamu.test_fragments.main;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sarangnamu.test_fragments.R;
import com.sarangnamu.test_fragments.common.FragmentEventListener;

public class SubMain extends Fragment implements FragmentEventListener {
    private static final String TAG = "SubMain";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_sub, null);
        view.setBackgroundColor(0xff00ffff);

        return view;
    }

    @Override
    public void sendMessage(String msg) {
        Log.e(TAG, "SEND MESSAGE = " + msg);
    }
}
