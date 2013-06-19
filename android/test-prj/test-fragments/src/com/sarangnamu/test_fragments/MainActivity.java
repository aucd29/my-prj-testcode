/*
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package com.sarangnamu.test_fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.sarangnamu.test_fragments.common.CustomActionBar;
import com.sarangnamu.test_fragments.common.FragmentController;
import com.sarangnamu.test_fragments.main.SubMain;


public class MainActivity extends Activity {
    private CustomActionBar topMenu;
    private FragmentController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTopMenu();
        setFragments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    private void setTopMenu() {
        topMenu = new CustomActionBar(this);
        topMenu.init(R.layout.main_top_menu);
    }

    private void setFragments() {
        controller = new FragmentController(this, R.id.mainFrm);
        controller.add("default", R.id.mainFrag);
        controller.add("sub", new SubMain());
    }

    public void changeMain(final String name) {
        if (name.equals("default")) {
            controller.showDefault();
        } else {
            controller.show(name);
        }
    }

    public void sendMessageToFragment(final String name, final String msg) {
        controller.sendMessage(name, msg);
    }
}
