package net.sarangnamu.simple_framework_test;

import net.sarangnamu.simple_framework_test.xml.Notification;
import net.sarangnamu.simple_framework_test.xml.Resource;
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

        XMLMarshaller xmlObj = new XMLMarshaller();

        Resource res = new Resource();
        res.type = "Operation";
        res.power = "On";
        res.state = "Stop";
        res.remainingTime = "0";
        res.progressPercentage = "100";
        res.progress = "Finish";
        res.supportedProgress = "Weight Sensing|Pre-Wash|Wash|Rinse|Spin|Drying|Air-Wash|Cooling|Wrinkle prevent|Finish";

        Notification noti = new Notification();
        noti.subscribedResource = "/devices/1/operation";
        noti.event = "Changed";
        noti.Resource = res;
        noti.subscriptionURI = "/subscriptions/1";

        String xml = xmlObj.objToXml(noti);
        Log.d(TAG, "===================================================================");
        Log.d(TAG, "object to xml");
        Log.d(TAG, "===================================================================");
        Log.d(TAG, xml);
        Log.d(TAG, "===================================================================");

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version='1.0' encoding='UTF-8' ?><Notification>");
        sb.append("<Resource xsi:type=\"Operation\">");
        sb.append("<power>On</power>");
        sb.append("<progress>Finish</progress>");
        sb.append("<progressPercentage>100</progressPercentage>");
        sb.append("<remainingTime>0</remainingTime>");
        sb.append("<state>Stop</state>");
        sb.append("<supportedProgress>Weight Sensing|Pre-Wash|Wash|Rinse|Spin|Drying|Air-Wash|Cooling|Wrinkle prevent|Finish</supportedProgress>");
        sb.append("</Resource>");
        sb.append("<event>Changed</event>");
        sb.append("<subscribedResource>/devices/1/operation</subscribedResource>");
        sb.append("<subscriptionURI>/subscriptions/1</subscriptionURI>");
        sb.append("</Notification>");

        Notification noti2 = (Notification)xmlObj.xmlToObj(sb.toString(),Notification.class);

        if (noti2 == null) {
            Log.e(TAG, "testXmlNotification error noti2 null");
            return ;
        }

        Log.d(TAG, "===================================================================");
        Log.d(TAG, "" + noti2.subscribedResource);




        //        // SET XML
        //        Device device = new Device();
        //        device.type = "type";
        //        device.time = new Date().toLocaleString();
        //        device.ID = "id";
        //        device.description = "description";
        //        device.ipAddress = "192.168.0.11";
        //        device.version = "1.0";
        //
        //        T1Param t1 = new T1Param();
        //        t1.name = "t1";
        //        t1.param1 = "param1";
        //
        //        device.parameter = t1;
        //
        //        //        Parameter param = new Parameter();
        //        //        param.name = "hoon";
        //        //        device.parameters.add(param);
        //        //
        //        //        param = new Parameter();
        //        //        param.name = "ray";
        //        //        device.parameters.add(param);
        //        //
        //        //        param = new Parameter();
        //        //        param.name = "burke";
        //        //        device.parameters.add(param);
        //        //
        //        //        param = new Parameter();
        //        //        param.name = "kurt";
        //        //        device.parameters.add(param);
        //
        //        XMLMarshaller marshaller = new XMLMarshaller();
        //        //        String xml = marshaller.objToXml(device);
        //        //
        //        //        Log.d(TAG, "===================================================================");
        //        //        Log.d(TAG, "OBJECT TO XML <1>");
        //        //        Log.d(TAG, xml);
        //        //        Log.d(TAG, "===================================================================");
        //
        //        String xml = "<?xml version='1.0' encoding='UTF-8' ?><Device version=\"1.0\"><ID>id</ID><description>description</description><ipAddress>192.168.0.11</ipAddress><parameter class=\"net.sarangnamu.simple_framework_test.xml.T1Param\" name=\"t2\"><param1>param1</param1></parameter><time>2013. 2. 12. 오후 2:11:59</time><type>type</type></Device>";
        //
        //        try {
        //            Device device2 = (Device)marshaller.xmlToObj(xml, Device.class);
        //            String xml2 = marshaller.objToXml(device2);
        //            Log.d(TAG, "===================================================================");
        //            Log.d(TAG, "XML TO OBJECT");
        //            Log.d(TAG, "device.type " + device2.type);
        //            Log.d(TAG, "device.time " + device2.time);
        //            Log.d(TAG, "device.ID " + device2.ID);
        //            Log.d(TAG, "device.description " + device2.description);
        //            Log.d(TAG, "device.ipAddress " + device2.ipAddress);
        //            Log.d(TAG, "===================================================================");
        //            Log.d(TAG, "OBJECT TO XML <2>");
        //            Log.d(TAG, xml2);
        //            Log.d(TAG, "===================================================================");
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }



    }
}
