package net.sarangnamu.testinvoke.test;

import android.util.Log;

public class TestCode {

    private static final String TAG = "TestCode";
    public void testMethod(Integer i) {
        Log.d(TAG, "===================================================================");
        Log.d(TAG, "testMethod " + i);
        Log.d(TAG, "===================================================================");
    }

    public void testMethod2() {
        Log.d(TAG, "===================================================================");
        Log.d(TAG, "testMethod2 " );
        Log.d(TAG, "===================================================================");
    }
}
