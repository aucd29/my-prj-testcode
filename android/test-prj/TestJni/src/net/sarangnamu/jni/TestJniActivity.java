package net.sarangnamu.jni;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TestJniActivity extends Activity {
    private static final String TAG = "TestJniActivity";
    private TestJNI jni = new TestJNI();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Log.d(TAG, "===================================================================");
        Log.d(TAG, "jni " + jni.testJni());
        Log.d(TAG, "===================================================================");
    }

    static {
        System.loadLibrary("testjni");
    }
}