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

import net.sarangnamu.common.network.BkWifiManager;
import net.sarangnamu.common.network.BkWifiStateReceiver;
import net.sarangnamu.common.network.BkWifiStateReceiver.IWiFIConnected;
import net.sarangnamu.common.network.BkWifiStateReceiver.IWiFiDisconnecting;
import net.sarangnamu.wifi_battery.BatteryInfo;
import net.sarangnamu.wifi_battery.BatteryInfo.BatteryInfoListener;
import net.sarangnamu.wifi_battery.widget.WifiBatteryWidget;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WifiBatteryService extends Service {
    public static final String BATTERY_INFO = "BATTERY_INFO";
    public static final String ADD_CLICK_EVENT = "ADD_CLICK_EVENT";
    public static final String WIFI_CONNECTED = "WIFI_CONNECTED";
    public static final String WIFI_DISCONNECTED = "WIFI_DISCONNECTED";
    public static final String WIFI_IP = "WIFI_IP";

    public static final String LEVEL = "level";
    public static final String SCALE = "scale";

    private BkWifiStateReceiver wifiReceiver;
    private BatteryInfo batteryInfo;

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

        initBatteryInfo();
        initWifiStatus();
    }

    @Override
    public void onDestroy() {
        if (batteryInfo != null) {
            batteryInfo.unregister(this);
        }

        if (wifiReceiver != null) {
            wifiReceiver.clearListener();
            wifiReceiver.unregister(this);
        }

        super.onDestroy();
    }

    private void initBatteryInfo() {
        if (batteryInfo == null) {
            batteryInfo = new BatteryInfo();
        }

        batteryInfo.register(this);
        batteryInfo.setListener(new BatteryInfoListener() {
            @Override
            public void onChangeBattery(int battery) {
                sendIntentToWidget(BATTERY_INFO, "Battery : " + battery + "%");
            }
        });
    }

    private void initWifiStatus() {
        if (wifiReceiver == null) {
            wifiReceiver = new BkWifiStateReceiver();
        }

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

    private void sendIntentToWidget(final String action, final String extraValue) {
        Intent intent = new Intent(this, WifiBatteryWidget.class);
        intent.setAction(action);

        if (extraValue != null) {
            intent.putExtra(action, extraValue);
        }

        sendBroadcast(intent);
    }
}
