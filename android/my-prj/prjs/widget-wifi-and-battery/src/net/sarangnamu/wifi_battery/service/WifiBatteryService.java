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

import net.sarangnamu.common.BkSystem;
import net.sarangnamu.common.DLog;
import net.sarangnamu.common.service.immortal.ImmortalService;
import net.sarangnamu.wifi_battery.BatteryInfo;
import net.sarangnamu.wifi_battery.BatteryInfo.BatteryInfoListener;
import net.sarangnamu.wifi_battery.widget.WifiBatteryWidget;
import android.content.Intent;
import android.os.IBinder;

public class WifiBatteryService extends ImmortalService {
    private static final String TAG = "WifiBatteryService";

    private BatteryInfo batteryInfo;

    public static final String BATTERY_INFO = "BATTERY_INFO";
    public static final String LEVEL = "level";
    public static final String SCALE = "scale";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initBatteryInfo();
    }

    @Override
    public void onDestroy() {
        if (batteryInfo != null) {
            batteryInfo.unregister(this);
        }

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void initBatteryInfo() {
        if (batteryInfo == null) {
            batteryInfo = new BatteryInfo();
        }

        batteryInfo.register(this, new BatteryInfoListener() {
            @Override
            public void onChangeBattery(int battery) {
                DLog.d(TAG, "===================================================================");
                DLog.d(TAG, " on change battery " + battery);
                DLog.d(TAG, "===================================================================");


                BkSystem.sendBroadcast(getApplicationContext(),
                        WifiBatteryWidget.class, BATTERY_INFO, "Battery : " + battery + "%");
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ImmortalService
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String getActionString() {
        return ResurrectionReceiver.ACTION;
    }
}
