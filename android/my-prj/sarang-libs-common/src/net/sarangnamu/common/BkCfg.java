/*
 * BkCfg.java
 * Copyright 2013 Burke.Choi All rights reserved.
 *             http://www.sarangnamu.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sarangnamu.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class BkCfg {
    private static final String SHARED_PREF = "burke.pref";

    public static String get(Context context, String name, String defVal) {
        SharedPreferences spRefresh = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return spRefresh.getString(name, defVal);
    }

    public static void set(Context context, String name, String data) {
        SharedPreferences spRefresh = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spRefresh.edit();
        editor.putString(name, data);
        editor.commit();
    }

    public static String sdPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static void showKeyboard(Context context, View view) {
        view.requestFocus();

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
