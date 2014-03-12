/*
 * BatteryInfo.java
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
package net.sarangnamu.wifi_battery;

import net.sarangnamu.common.DLog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BatteryInfo extends BroadcastReceiver {
    private static final String TAG = "BatteryInfo";
    private static final String LEVEL = "level";
    private static final String SCALE = "scale";

    private BatteryInfoListener l;
    private int batteryValue;

    @Override
    public void onReceive(Context context, Intent intent) {
        int level, scale;

        level = intent.getIntExtra(LEVEL, -1);
        scale = intent.getIntExtra(SCALE, -1);

        int battery = level * 100 / scale;
        if (l != null && batteryValue != battery) {
            batteryValue = battery;
            l.onChangeBattery(batteryValue);
        }
    }

    public void register(Context context, BatteryInfoListener l) {
        if (context == null) {
            DLog.e(TAG, "register");
            return;
        }

        this.l = l;
        context.registerReceiver(this, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
    }

    public void unregister(Context context) {
        if (context == null) {
            DLog.e(TAG, "unregister");
            return;
        }

        context.unregisterReceiver(this);
    }

    // //////////////////////////////////////////////////////////////////////////////////
    //
    // BatteryInfoListener
    //
    // //////////////////////////////////////////////////////////////////////////////////

    public interface BatteryInfoListener {
        public void onChangeBattery(int battery);
    }
}
