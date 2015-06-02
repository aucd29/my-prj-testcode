/*
 * BkWifiManager.java
 * Copyright 2014 Burke Choi All rights reserved.
 *             http://www.sarangnamu.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sarangnamu.common.network;

import net.sarangnamu.common.DLog;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * <pre>
 * {@code
 * BkWifiManager.getInstance(getApplicationContext()).isEnabled();
 * }
 * </pre>
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class BkWifiManager {
    private static final String TAG = "BkWifiManager";

    private static BkWifiManager sInst = null;
    private WifiManager mManager = null;

    private BkWifiManager(Context context) {
        try {
            mManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        } catch (NullPointerException e) {
            DLog.e(TAG, "BkWifiManager", e);
        }
    }

    public static BkWifiManager getInstance(Context context) {
        if (sInst == null) {
            sInst = new BkWifiManager(context);
        }

        return sInst;
    }

    public boolean isEnabled() {
        if (mManager == null) {
            DLog.e(TAG, "manager is null");
            return false;
        }

        return mManager.isWifiEnabled();
    }

    public String getIPAddr() {
        String ipAddr = "";

        try {
            WifiInfo wifiInfo = mManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ipAddr = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));

        } catch (Exception e) {
            DLog.e(TAG, "getIPAddr", e);
            return "";
        }

        return ipAddr;
    }

    public void wifiEnable() {
        try {
            mManager.setWifiEnabled(true);
        } catch (NullPointerException e) {
            DLog.e(TAG, "wifiEnable", e);
        }
    }

    public void wifiDisable() {
        try {
            mManager.setWifiEnabled(false);
        } catch (NullPointerException e) {
            DLog.e(TAG, "wifiDisable", e);
        }
    }
}
