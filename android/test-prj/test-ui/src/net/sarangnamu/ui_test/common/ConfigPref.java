/*
 * ConfigPref.java
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.ui_test.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @author @aucd29
 *
 */
public class ConfigPref {
    private static final String PREF = "base.pref";
    private static final String VALUE = "value";

    public static void setValue(Context context, String name, String value) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editer = sp.edit();
        editer.putString(name, value);
        editer.commit();
    }

    public static String getValue(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(name, "0");
    }
}
