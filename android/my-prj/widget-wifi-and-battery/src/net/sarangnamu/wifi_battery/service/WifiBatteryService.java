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
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class WifiBatteryService extends Service {
    private static final String TAG = "WifiBatteryService";

    private String battery;
    private Intent batteryStatus;

    BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DLog.d(TAG, "battery receiver");

            getBatteryStatus(context);

            /*try {
                if (!mReceivedBatteryStatus) {
                    mReceivedBatteryStatus = true;
                    int level = intent.getIntExtra("level", 0);

                    if (level < mThreshold) {
                        showNotification("Battery down to " + String.valueOf(level) + "%");
                    } else {
                        mNM.cancel(R.string.alarm_service_started);
                    }
                }
            } finally {
                unregisterReceiver(mBatInfoReceiver);
                BatteryAlarmService.this.stopSelf();
            }*/
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(batteryReceiver);

        super.onDestroy();
    }

    private void getBatteryStatus(Context context) {
        batteryStatus = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        if (batteryStatus == null) {
            DLog.e(TAG, "batteryStatus == null");
            return ;
        }

        int level, scale;
        level = batteryStatus.getIntExtra("level", -1);
        scale = batteryStatus.getIntExtra("scale", -1);

        battery = String.format("Battery : %d%% ", (level * 100 / scale));

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "" + battery);
        DLog.d(TAG, "===================================================================");
    }
}
