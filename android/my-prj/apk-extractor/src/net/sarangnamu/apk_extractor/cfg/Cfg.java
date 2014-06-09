/*
 * Cfg.java
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
package net.sarangnamu.apk_extractor.cfg;

import net.sarangnamu.common.BkCfg;
import net.sarangnamu.common.BkString;
import android.content.Context;

public class Cfg extends BkCfg {
    public static final String PATH = "/apks/";

    private static final String EMAIL = "email";
    private static final String USERPATH = "usrPath";
    private static final String SHOW_OPT = "showOpt";

    public static String getDownPath(Context context) {
        String usrPath = getUserPath(context);

        if (usrPath == null) {
            return BkCfg.sdPath() + PATH;
        }

        return BkString.setLastSlash(usrPath);
    }

    public static String getEmail(Context context) {
        return get(context, EMAIL, null);
    }

    public static void setEmail(Context context, String email) {
        set(context, EMAIL, email);
    }

    public static void setUserPath(Context context, String path) {
        set(context, USERPATH, path);
    }

    public static String getUserPath(Context context) {
        return get(context, USERPATH, null);
    }

    public static String getShowOption(Context context) {
        return get(context, SHOW_OPT, "0");
    }

    public static void setShowOption(Context context, String val) {
        set(context, SHOW_OPT, val);
    }
}
