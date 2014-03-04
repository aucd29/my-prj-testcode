/*
 * WifiBatteryService.java
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
package net.sarangnamu.wifi_battery.service;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.network.BkWifiManager;
import net.sarangnamu.common.network.BkWifiStateReceiver;
import net.sarangnamu.common.network.BkWifiStateReceiver.IWiFIConnected;
import net.sarangnamu.common.network.BkWifiStateReceiver.IWiFiDisconnecting;
import net.sarangnamu.wifi_battery.widget.WifiBatteryWidget;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class WifiBatteryService extends Service {
    private static final String TAG = "WifiBatteryService";

    public static final String BATTERY_INFO = "BATTERY_INFO";
    public static final String ADD_CLICK_EVENT = "ADD_CLICK_EVENT";
    public static final String WIFI_CONNECTED = "WIFI_CONNECTED";
    public static final String WIFI_DISCONNECTED = "WIFI_DISCONNECTED";
    public static final String WIFI_IP = "WIFI_IP";

    public static final String LEVEL = "level";
    public static final String SCALE = "scale";

    private String battery;
    private Intent batteryStatus;
    private BkWifiStateReceiver wifiReceiver;

    BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getBatteryStatus(context);
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sendIntentToWidget(ADD_CLICK_EVENT, null);

        // CHECK CURRENT WIFI STATUS
        if (BkWifiManager.getInstance(this).isEnabled()) {
            sendIntentToWidget(WIFI_CONNECTED, BkWifiManager.getInstance(getApplicationContext()).getIPAddr());
        } else {
            sendIntentToWidget(WIFI_DISCONNECTED, null);
        }

        // REGISTRATION A BATTERY RECEIVER
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        if (wifiReceiver == null) {
            wifiReceiver = new BkWifiStateReceiver();
        }

        // REGISTRATION A WIFI STATUS RECEIVER
        wifiReceiver.register(this, new IWiFIConnected() {
            @Override
            public void onWiFiConnected() {
                sendIntentToWidget(WIFI_CONNECTED, BkWifiManager.getInstance(getApplicationContext()).getIPAddr());
            }
        });

        wifiReceiver.addListener(new IWiFiDisconnecting() {
            @Override
            public void onWiFiDisconnecting() {
                sendIntentToWidget(WIFI_DISCONNECTED, null);
            }
        });
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(batteryReceiver);
        if (wifiReceiver != null) {
            wifiReceiver.clearListener();
            wifiReceiver.unregister(this);
        }

        super.onDestroy();
    }

    private void getBatteryStatus(Context context) {
        batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        if (batteryStatus == null) {
            DLog.e(TAG, "batteryStatus == null");
            return;
        }

        int level, scale;
        level = batteryStatus.getIntExtra(LEVEL, -1);
        scale = batteryStatus.getIntExtra(SCALE, -1);

        String tmpBattery = String.format("Battery : %d%% ", (level * 100 / scale));
        if (!tmpBattery.equals(battery)) {
            battery = tmpBattery;
            sendIntentToWidget(BATTERY_INFO, battery);
        }
    }

    private void sendIntentToWidget(final String action, final String extraValue) {
        Intent intent = new Intent(this, WifiBatteryWidget.class);
        intent.setAction(action);

        if (extraValue != null) {
            intent.putExtra(action, extraValue);
        }

        sendBroadcast(intent);
    }
}