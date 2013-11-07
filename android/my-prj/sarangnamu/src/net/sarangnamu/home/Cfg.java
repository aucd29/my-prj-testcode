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
package net.sarangnamu.home;

import net.sarangnamu.common.BkCfg;
import android.content.Context;

public class Cfg extends BkCfg {
    public static final String ID = "id";
    public static final String PW = "pw";

    public static void rememberMe(Context context, String id, String pw) {
        set(context, ID, id);
        set(context, PW, pw);
    }

    public static String getId(Context context) {
        return get(context, ID, "");
    }

    public static String getPw(Context context) {
        return get(context, PW, "");
    }
}
