/*
 * BkCfg.java
 * Copyright 2013 Burke Choi All rights reserved.
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
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * <pre>
 * {@code
 * - preferences
    BkCfg.get(context, "name", "");
    BkCfg.set(context, "name", "burke.choi");

    - get sdcard path
    BkCfg.sdPath();

    - toggle keyboard
    BkCfg.showKeyboard(context, view);
    BkCfg.hideKeyboard(context);
 * }
 * </pre>
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class BkCfg {
    private static final String TAG = "BkCfg";
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

    public static String sdDataPath() {
        if (!Environment.getDataDirectory().exists()) {
            Environment.getDataDirectory().mkdirs();
        }

        return Environment.getDataDirectory().getAbsolutePath();
    }

    public static void showKeyboard(final View view) {
        if (view == null) {
            DLog.e(TAG, "showKeyboard view == null");
            return;
        }

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.requestFocus();

                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }, 400);
    }

    public static void hideKeyboard(View view) {
        if (view == null) {
            DLog.e(TAG, "hideKeyboard view == null");
            return;
        }

        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        // imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void forceHideKeyboard(Window window) {
        if (window == null) {
            return;
        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void engKeyboard(EditText edt) {
        edt.setPrivateImeOptions("defaultInputmode=english;");
    }
}