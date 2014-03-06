/*
 * WifiBatteryWidget.java
 * Copyright 2013 Burke.Choi All rights reserved.
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

import net.sarangnamu.common.network.BkWifiManager;
import net.sarangnamu.wifi_battery.R;
import net.sarangnamu.wifi_battery.service.WifiBatteryService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

public class WifiBatteryWidget extends AppWidgetProvider {
    private static final String TAG = "WifiBatteryWidget";
    private static final String TOGGLE_WIFI = "toggleWifi";

    private String battery;
    private Boolean changingWifi = false;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        context.startService(new Intent(context, WifiBatteryService.class));

        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        if (BkWifiManager.getInstance(context).isEnabled()) {
            views.setTextViewText(R.id.wifiStatus, context.getString(R.string.wifiOn));
        } else {
            views.setTextViewText(R.id.wifiStatus, context.getString(R.string.wifiOff));
        }

        ComponentName watchWidget = new ComponentName(context, WifiBatteryWidget.class);
        appWidgetManager.updateAppWidget(watchWidget, views);
    }

    @Override
    public void onDisabled(Context context) {
        context.stopService(new Intent(context, WifiBatteryService.class));

        super.onDisabled(context);
    }

    @Override
    public void onUpdate(Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        context.startService(new Intent(context, WifiBatteryService.class));

        for (int i = 0; i < N; i++) {
            final int appWidgetId = appWidgetIds[i];
            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            views.setTextViewText(R.id.battery, battery);
            views.setTextViewText(R.id.ip, BkWifiManager.getInstance(context).getIPAddr());

            if (BkWifiManager.getInstance(context).isEnabled()) {
                views.setTextViewText(R.id.wifiStatus, context.getString(R.string.wifiOn));
            } else {
                views.setTextViewText(R.id.wifiStatus, context.getString(R.string.wifiOff));
            }

            views.setOnClickPendingIntent(R.id.widgetLayout, getPendingSelfIntent(context, TOGGLE_WIFI, appWidgetId));
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action, int id) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        intent.putExtra("id", id);

        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, final Intent intent) {
        super.onReceive(context, intent);

        final String action = intent.getAction();
        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        if (action.equals(WifiBatteryService.BATTERY_INFO)) {
            String batteryInfo = intent.getStringExtra(WifiBatteryService.BATTERY_INFO);
            views.setTextViewText(R.id.battery, batteryInfo);
        } else if (action.equals(WifiBatteryService.WIFI_CONNECTED)) {
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
        } else if (action.equals(TOGGLE_WIFI)) {
            if (changingWifi) {
                return;
            }

            synchronized (changingWifi) {
                changingWifi = true;
            }

            views.setViewVisibility(R.id.prog, View.VISIBLE);

            if (BkWifiManager.getInstance(context).isEnabled()) {
                BkWifiManager.getInstance(context).wifiDisable();
            } else {
                BkWifiManager.getInstance(context).wifiEnable();
            }

            synchronized (changingWifi) {
                changingWifi = false;
            }
        } else if (action.equals(WifiBatteryService.ADD_CLICK_EVENT)) {
            views.setOnClickPendingIntent(R.id.widgetLayout, getPendingSelfIntent(context, TOGGLE_WIFI, 0));
        } else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            context.startService(new Intent(context, WifiBatteryService.class));
        }

        ComponentName watchWidget = new ComponentName(context, WifiBatteryWidget.class);
        appWidgetManager.updateAppWidget(watchWidget, views);
    }
}
