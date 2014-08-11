package net.sarangnamu.d_day;

import net.sarangnamu.common.fonts.FontLoader;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
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
    }
}

