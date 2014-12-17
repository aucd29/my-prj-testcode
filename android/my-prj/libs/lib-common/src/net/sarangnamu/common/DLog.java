/*
 * DLog.java
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

import android.util.Log;

/**
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class DLog {
    public static final boolean DEBUG_MODE = true;

    public static void d(final String tag, final String msg) {
        if (DEBUG_MODE) {
            Log.d(tag, msg);
        }
    }

    public static void e(final String tag, final String msg) {
        Log.e(tag, msg);
    }

    public static void i(final String tag, final String msg) {
        if (DEBUG_MODE) {
            Log.i(tag, msg);
        }
    }

    public static void d(final String tag, final String msg, Exception e) {
        if (DEBUG_MODE) {
            printStackTrace(e);
        }
    }

    public static void e(final String tag, final String msg, Exception e) {
        if (DEBUG_MODE) {
            printStackTrace(e);
        } else {
            Log.e(tag, msg + " " + e);
        }
    }

    public static void i(final String tag, final String msg, Exception e) {
        if (DEBUG_MODE) {
            printStackTrace(e);
        }
    }

    public static void v(final String tag, final String msg, Exception e) {
        if (DEBUG_MODE) {
            printStackTrace(e);
        }
    }

    public static void w(final String tag, final String msg, Exception e) {
        if (DEBUG_MODE) {
            printStackTrace(e);
        }
    }

    private static void printStackTrace(Exception e) {
        if (e != null) {
            e.printStackTrace();
        }
    }
}
