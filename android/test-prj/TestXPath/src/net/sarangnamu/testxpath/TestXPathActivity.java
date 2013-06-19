package net.sarangnamu.testxpath;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TestXPathActivity extends Activity {
    private static final String TAG = "TestXPathActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        GroupbyXmlParser xml = new GroupbyXmlParser();
        try {
            xml.loadXml(getResources().getAssets().open("appby/config.xml"));

            Log.d(TAG, "===================================================================");
            Log.d(TAG, "xml.getID() " + xml.getID());
            Log.d(TAG, "xml.getName() " + xml.getName());
            Log.d(TAG, "xml.getCount() " + xml.getCount());

            for (int i=0; i<xml.getCount(); ++i) {
                Log.d(TAG, "xml.getPath() " + xml.getPath(i));
            }

            Log.d(TAG, "===================================================================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}