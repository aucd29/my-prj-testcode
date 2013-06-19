/*
 * DefaultLeft.java
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

public class DefaultLeft extends Fragment implements FragmentEventListener {
    private static final String TAG = "DefaultLeft";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_default_left, null);
        view.setBackgroundColor(0xff00ff00);

        return view;
    }

    @Override
    public void sendMessage(String msg) {
        Log.e(TAG, "SEND MESSAGE = " + msg);
    }
}
