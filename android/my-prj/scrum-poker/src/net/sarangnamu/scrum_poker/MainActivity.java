package net.sarangnamu.scrum_poker;

import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AsyncTask<Context, Void, String>() {
            @Override
            protected String doInBackground(Context... contexts) {
                AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
                HttpGet request = new HttpGet("http://117.16.231.212:8010/checkNewOrder");

                return builder.toString();
            }

            @Override
            protected void onPostExecute(String result) {

            }
        }.execute(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /*@Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_license:
            DlgLicense dlg = new DlgLicense(MainActivity.this);
            dlg.setTitleTypeface(FontLoader.getInstance(getApplicationContext()).getRobotoLight());
            dlg.show();
            break;

        case R.id.action_settings:
            break;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    public void showPopup(String msg) {
        DlgTimer dlg = new DlgTimer(MainActivity.this, R.layout.dlg_timer);
        dlg.setMessage(msg);
        dlg.setTime(1000);
        dlg.show();
        dlg.setTransparentBaseLayout();
    }*/
}

