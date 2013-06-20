package com.example.testip;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WiFiManagerEx {
    private static WiFiManagerEx inst = null;
    private WifiManager manager = null;

    private WiFiManagerEx(Context context) {
        manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    }

    public static WiFiManagerEx getInstance(Context context) {
        if (inst == null) {
            inst = new WiFiManagerEx(context);
        }

        return inst;
    }

    public boolean isEnabled() {
        return manager.isWifiEnabled();
    }

    public String getIPAddr() {
        String ipAddr = "";

        try {
            WifiInfo wifiInfo = manager.getConnectionInfo();

            int ipAddress = wifiInfo.getIpAddress();
            ipAddr = String.format("%d.%d.%d.%d", (ipAddress & 0xff),
                    (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
                    (ipAddress >> 24 & 0xff));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ipAddr;
    }
}
