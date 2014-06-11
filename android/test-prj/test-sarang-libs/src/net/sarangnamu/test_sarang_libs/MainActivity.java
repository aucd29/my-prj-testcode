package net.sarangnamu.test_sarang_libs;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.ui.list.swipe.SwipeListView;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testProgressbar();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    //
    //
    ////////////////////////////////////////////////////////////////////////////////////

    private ProgressBar pb, pb2;

    private void testProgressbar() {
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setMax(100);
        pb.setProgress(50);

        pb2 = (ProgressBar) findViewById(R.id.progressBar2);
        pb2.setMax(100);
        pb2.setProgress(50);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    //
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public String loadXml(String addr) throws Exception {
        URL url = new URL(addr);
        URLConnection conn = url.openConnection();

        conn.setDoOutput(true);
        conn.setDoInput(true);
//        conn.setConnectTimeout(connTimeout);
//        conn.setReadTimeout(readTimeout);
        conn.connect();

        InputStream input   = new BufferedInputStream(url.openStream());
        int count;
        byte data[] = new byte[2048];

        StringBuilder sb = new StringBuilder();
        DLog.d(TAG, "======================== NEXT =========================");
        boolean bvalue = false;
        while ((count = input.read(data)) != -1) {
            if (bvalue == false) {
                for (int i=0; i<20; ++i) {
                    DLog.d(TAG, "data " + data[i]);
                }
                bvalue = true;
            }
            sb.append(new String(data, "EUC-KR"));
            Thread.sleep(1);
        }

        input.close();

        return sb.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private SwipeListView list;

        String[] values = new String[] {
                "Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View",
                "Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View","Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
               };

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_2, android.R.id.text1, values);

//            list = (SwipeListView) rootView.findViewById(R.id.hello);
//            list.setAdapter(adapter);

            return rootView;
        }
    }
}

