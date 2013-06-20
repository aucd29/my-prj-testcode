package com.example.testip;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(R.layout.activity_main);

        tv = new TextView(this);
        tv.setText("IP ADDRESS: " + WiFiManagerEx.getInstance(this).getIPAddr());

        //        int ba = getResources().getInteger(R.integer.config_notification_server_port);
        //        Log.d(TAG, "ba " + ba);
        //
        //        ba = getResourceId("integer", "config_notification_server_port");
        //        ba = getResources().getInteger(ba);
        //        //        Log.d(TAG, "===================================================================");
        //        //        Log.d(TAG, "ba " + ba);
        //        //        Log.d(TAG, "===================================================================");
        //
        //        //        tv.setText("PORT : " + ba);
        setContentView(tv);
    }

    public int getResourceId(final String type, final String name) {
        return getResources().getIdentifier(name, type, getPackageName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
