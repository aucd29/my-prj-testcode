/*
 * Cfg.java
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
package net.sarangnamu.ems_tracking.cfg;

import net.sarangnamu.common.BkCfg;
import android.content.Context;

public class Cfg extends BkCfg {
    public static final String ADMOB_ID = "ca-app-pub-7094629622358576/3650039138";
    private static final String OPT_NAME = "OPT_NAME";

    public static boolean isEmsNumber(String num) {
        if (!num.matches("[a-zA-Z]{1}[0-9a-zA-Z]{12}")) {
            return false;
        }

        return true;
    }

    public static void setAnotherName(Context context, String emsNum, String name) {
        set(context, emsNum, name);
    }

    public static String getAnotherName(Context context, String emsNum) {
        return get(context, emsNum, "");
    }

    public static void setOptionName(Context context, boolean type) {
        set(context, OPT_NAME, type?"1":"0");
    }

    public static boolean getOptionName(Context context) {
        if (get(context, OPT_NAME, "1").equals("0")) {
            return false;
        }

        return true;
    }
}
