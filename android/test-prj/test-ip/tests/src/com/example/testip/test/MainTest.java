/*
 * Copyright 2013 OBIGO Inc. All rights reserved.
 *             http://www.obigo.com
 */
package com.example.testip.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;

import com.example.testip.MainActivity;

public class MainTest extends ActivityUnitTestCase<MainActivity> {
    private static final String TAG = "MainTest";

    public MainTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        startActivity(intent, null, null);
    }

    public void testResID() {
        MainActivity activity = getActivity();

        int va = activity.getResourceId("integer", "config_notification_server_port");

        Log.d(TAG, "===================================================================");
        Log.d(TAG, "va " + va);
        Log.d(TAG, "===================================================================");

        if (va == 0) {
            fail();
        }
        //assertEquals("", va);
    }
}
