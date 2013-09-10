package com.example.jackson_example;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.example.common.json.JacksonUtil;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final String SAMPLE = "{\"url\":\"sarangnamu.net\"}";
    private static final String SAMPLE2 = "{\"name\":\"test\",\"data\":{\"url\":\"sarangnamu.net\",\"user\":\"aucd29\"}}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testSample();
        testSample2();
    }

    private void testSample() {
        Sample obj = (Sample) JacksonUtil.toObject(SAMPLE, Sample.class);
        Log.d(TAG, "url = " + obj.url);
        Log.d(TAG, "user = " + obj.user);

        String val = JacksonUtil.toString(obj);
        Log.d(TAG, "json string = " + val);
    }

    private void testSample2() {
        Sample2 obj = (Sample2) JacksonUtil.toObject(SAMPLE2, Sample2.class);
        Log.d(TAG, "name = " + obj.name);
        Log.d(TAG, "url = " + obj.data.url);
        Log.d(TAG, "user = " + obj.data.user);

        String val = JacksonUtil.toString(obj);
        Log.d(TAG, "json string = " + val);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
