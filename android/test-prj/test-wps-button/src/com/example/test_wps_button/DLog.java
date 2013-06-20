
package com.example.test_wps_button;

import android.util.Log;

/**
 * 
 * 
 * @author <a href="mailto:Burke.Choi@obigo.com">krinbuch</a>
 *
 */
public final class DLog {
    public static final boolean DEBUG_MODE = true;

    public static void d(final String tag, final String msg) {
        Log.d(tag, msg);
    }

    public static void e(final String tag, final String msg) {
        Log.e(tag, msg);
    }

    public static void d(final String tag, final String msg, Exception e) {
        if (DEBUG_MODE) {
            printStackTrace(e);
        } else {
            Log.d(tag, msg + " " + e);
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
        } else {
            Log.i(tag, msg + " " + e);
        }
    }

    public static void v(final String tag, final String msg, Exception e) {
        if (DEBUG_MODE) {
            printStackTrace(e);
        } else {
            Log.v(tag, msg + " " + e);
        }
    }

    public static void w(final String tag, final String msg, Exception e) {
        if (DEBUG_MODE) {
            printStackTrace(e);
        } else {
            Log.w(tag, msg + " " + e);
        }
    }

    private static void printStackTrace(Exception e) {
        if (e != null) {
            e.printStackTrace();
        }
    }
}
