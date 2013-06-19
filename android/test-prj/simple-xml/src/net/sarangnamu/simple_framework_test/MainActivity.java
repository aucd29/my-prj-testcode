package net.sarangnamu.simple_framework_test;

import java.util.Date;

import net.sarangnamu.simple_framework_test.xml.Device;
import net.sarangnamu.simple_framework_test.xml.XMLMarshaller;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SET XML
        Device device = new Device();
        device.type = "type";
        device.time = new Date().toLocaleString();
        device.ID = "id";
        device.description = "description";
        device.ipAddress = "192.168.0.11";

        XMLMarshaller marshaller = new XMLMarshaller();
        String xml = marshaller.objToXml(device);

        Log.d(TAG, "===================================================================");
        Log.d(TAG, "OBJECT TO XML <1>");
        Log.d(TAG, xml);
        Log.d(TAG, "===================================================================");

        try {
            Device device2 = (Device)marshaller.xmlToObj(xml, Device.class);
            String xml2 = marshaller.objToXml(device2);
            Log.d(TAG, "===================================================================");
            Log.d(TAG, "XML TO OBJECT");
            Log.d(TAG, "device.type " + device2.type);
            Log.d(TAG, "device.time " + device2.time);
            Log.d(TAG, "device.ID " + device2.ID);
            Log.d(TAG, "device.description " + device2.description);
            Log.d(TAG, "device.ipAddress " + device2.ipAddress);
            Log.d(TAG, "===================================================================");
            Log.d(TAG, "OBJECT TO XML <2>");
            Log.d(TAG, xml2);
            Log.d(TAG, "===================================================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
