
package com.example.test_wps_button;

import android.app.Activity;
import android.net.wifi.WpsInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    private Button showWps;
    private TextView label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        label = (TextView)findViewById(R.id.title);
        showWps = (Button)findViewById(R.id.showWps);
        showWps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WpsDialog(MainActivity.this, WpsInfo.PBC).show();
            }
        });

        label.setText("Push button configuration");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
