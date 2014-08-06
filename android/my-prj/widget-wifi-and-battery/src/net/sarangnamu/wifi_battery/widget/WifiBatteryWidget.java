/*
 * WifiBatteryWidget.java
 * Copyright 2014 Burke.Choi All rights reserved.
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
package net.sarangnamu.wifi_battery.widget;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.network.BkWifiManager;
import net.sarangnamu.wifi_battery.BatteryInfo;
import net.sarangnamu.wifi_battery.R;
import net.sarangnamu.wifi_battery.service.WifiBatteryService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.RemoteViews;

public class WifiBatteryWidget extends AppWidgetProvider {
    private static final String TAG = "WifiBatteryWidget";
    private static final String TOGGLE_WIFI = "toggleWifi";
    private static final String LEVEL = "level";
    private static final String SCALE = "scale";

    private String batteryValue;
    private BatteryInfo batteryInfo;
    private Boolean changingWifi = false;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "on enabled");
        DLog.d(TAG, "===================================================================");
        //        setBatteryReceiver(context);

        startService(context);

        //        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        //        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        //
        //        if (BkWifiManager.getInstance(context).isEnabled()) {
        //            views.setTextViewText(R.id.wifiStatus, context.getString(R.string.wifiOn));
        //        } else {
        //            views.setTextViewText(R.id.wifiStatus, context.getString(R.string.wifiOff));
        //        }
        //
        //        ComponentName watchWidget = new ComponentName(context, WifiBatteryWidget.class);
        //        appWidgetManager.updateAppWidget(watchWidget, views);
    }

    @Override
    public void onDisabled(Context context) {
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "on disabled");
        DLog.d(TAG, "===================================================================");
        //
        super.onDisabled(context);
    }

    @Override
    public void onUpdate(Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "on update length " + N);
        DLog.d(TAG, "===================================================================");

        for (int i = 0; i < N; i++) {
            final int appWidgetId = appWidgetIds[i];
            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            views.setOnClickPendingIntent(R.id.widgetLayout, getPendingSelfIntent(context, TOGGLE_WIFI, appWidgetId));
            views.setTextViewText(R.id.battery, batteryValue);
            views.setTextViewText(R.id.ip, BkWifiManager.getInstance(context).getIPAddr());

            if (BkWifiManager.getInstance(context).isEnabled()) {
                views.setTextViewText(R.id.wifiStatus, context.getString(R.string.wifiOn));
            } else {
                views.setTextViewText(R.id.wifiStatus, context.getString(R.string.wifiOff));
            }

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, final Intent intent) {
        super.onReceive(context, intent);

        final String action = intent.getAction();
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "on receive " + action);
        DLog.d(TAG, "===================================================================");

        if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
            DLog.d(TAG, "===================================================================");
            DLog.d(TAG, "battery changed ");
            DLog.d(TAG, "===================================================================");

            int level, scale;
            level = intent.getIntExtra(LEVEL, -1);
            scale = intent.getIntExtra(SCALE, -1);

            int battery = level * 100 / scale;
            views.setTextViewText(R.id.battery, battery +"%");
        } else if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            DLog.d(TAG, "===================================================================");
            DLog.d(TAG, "wifi state changed");
            DLog.d(TAG, "===================================================================");

            views.setViewVisibility(R.id.prog, View.GONE);
            int status = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
            if (status == WifiManager.WIFI_STATE_DISABLED) {
                views.setTextViewText(R.id.wifiStatus, context.getString(R.string.wifiOff));
                views.setTextViewText(R.id.ip, context.getString(R.string.invalidIp));
            } else if (status == WifiManager.WIFI_STATE_ENABLED) {
                views.setTextViewText(R.id.wifiStatus, context.getString(R.string.wifiOn));
                views.setTextViewText(R.id.ip, BkWifiManager.getInstance(context).getIPAddr());
            }
        }

        else if (action.equals(WifiBatteryService.BATTERY_INFO)) {
            String batteryInfo = intent.getStringExtra(WifiBatteryService.BATTERY_INFO);
            views.setTextViewText(R.id.battery, batteryInfo);
        }/* else if (action.equals(WifiBatteryService.WIFI_CONNECTED)) {
            String ip = intent.getStringExtra(action);

            views.setTextViewText(R.id.wifiStatus, context.getString(R.string.wifiOn));
            if (ip != null) {
                views.setTextViewText(R.id.ip, ip);
            }

            views.setViewVisibility(R.id.prog, View.GONE);
        } else if (action.equals(WifiBatteryService.WIFI_DISCONNECTED)) {
            views.setTextViewText(R.id.wifiStatus, context.getString(R.string.wifiOff));
            views.setTextViewText(R.id.ip, context.getString(R.string.invalidIp));
            views.setViewVisibility(R.id.prog, View.GONE);
        }*/
        else if (action.equals(TOGGLE_WIFI)) {
            if (changingWifi) {
                return;
            }

            synchronized (changingWifi) {
                changingWifi = true;
            }

            views.setViewVisibility(R.id.prog, View.VISIBLE);

            if (BkWifiManager.getInstance(context).isEnabled()) {
                DLog.d(TAG, "call disable");
                BkWifiManager.getInstance(context).wifiDisable();
            } else {
                DLog.d(TAG, "call enable");
                BkWifiManager.getInstance(context).wifiEnable();
            }

            synchronized (changingWifi) {
                changingWifi = false;
            }
        }

        /*else if (action.equals(WifiBatteryService.ADD_CLICK_EVENT)) {
            views.setOnClickPendingIntent(R.id.widgetLayout, getPendingSelfIntent(context, TOGGLE_WIFI, 0));
        } else if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            context.startService(new Intent(context, WifiBatteryService.class));
        }*/

        ComponentName watchWidget = new ComponentName(context, WifiBatteryWidget.class);
        appWidgetManager.updateAppWidget(watchWidget, views);
    }

    private PendingIntent getPendingSelfIntent(Context context, String action, int id) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        intent.putExtra("id", id);

        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private void startService(Context context) {
        context.startService(new Intent(context, WifiBatteryService.class));
    }

    //    private void setBatteryReceiver(final Context context) {
    //        if (batteryInfo == null) {
    //            batteryInfo = new BatteryInfo();
    //            batteryInfo.register(context, new BatteryInfoListener() {
    //                @Override
    //                public void onChangeBattery(int battery) {
    //                    batteryValue = battery + "%";
    //                }
    //            });
    //        }
    //    }
    //
    //    private void resetBatteryReceiver(Context context) {
    //        if (batteryInfo != null) {
    //            batteryInfo.unregister(context);
    //        }
    //    }

}
