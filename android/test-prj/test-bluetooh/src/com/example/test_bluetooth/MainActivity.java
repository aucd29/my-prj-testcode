package com.example.test_bluetooth;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tv = new TextView(this);
        tv.setText("hello world");

        setContentView(tv);
    }

    public int getResourceId(final String type, final String name) {
        return getResources().getIdentifier(name, type, getPackageName());
    }

}
