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

import net.sarangnamu.common.BkCfg;
import net.sarangnamu.common.DLog;
import net.sarangnamu.common.network.BkWifiManager;
import net.sarangnamu.wifi_battery.R;
import net.sarangnamu.wifi_battery.service.WifiBatteryService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.view.View;
import android.widget.RemoteViews;

public class WifiBatteryWidget extends AppWidgetProvider {
    private static final String TAG = "WifiBatteryWidget";
    private static final String TOGGLE_WIFI = "toggleWifi";

    private Boolean changingWifi = false;

    @Override
    public void onUpdate(Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        startService(context);

        for (int i = 0; i < N; i++) {
            final int appWidgetId = appWidgetIds[i];
            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            views.setViewVisibility(R.id.prog, View.GONE);
            views.setOnClickPendingIntent(R.id.widgetLayout, getPendingSelfIntent(context, TOGGLE_WIFI, appWidgetId));

            setBatteryWithCfg(context, views);
            setIpAddrWithCfg(context, views);
            setWifiStateWithCfg(context, views);

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

        if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            DLog.d(TAG, "===================================================================");
            DLog.d(TAG, "WIFI_STATE_CHANGED_ACTION");
            DLog.d(TAG, "===================================================================");
            int status = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
            if (status == WifiManager.WIFI_STATE_DISABLED) {
                BkCfg.set(context, "wifi-state", "0");

                views.setViewVisibility(R.id.prog, View.GONE);
                views.setTextViewText(R.id.wifiStatus, context.getString(R.string.wifiOff));
                views.setTextViewText(R.id.ip, context.getString(R.string.invalidIp));
            } else if (status == WifiManager.WIFI_STATE_ENABLED) {
                DLog.d(TAG, "===================================================================");
                DLog.d(TAG, "WIFI_STATE_ENABLED");
                DLog.d(TAG, "===================================================================");
                BkCfg.set(context, "wifi-state", "1");

                new AsyncTask<Context, Void, String>() {
                    Context context;

                    @Override
                    protected String doInBackground(Context... contexts) {
                        context = contexts[0];
                        String ip = context.getString(R.string.invalidIp);

                        try {
                            while(true) {
                                ip = BkWifiManager.getInstance(context).getIPAddr();

                                if (ip.equals("0.0.0.0") || ip == "") {
                                    Thread.sleep(500);
                                } else {
                                    BkCfg.set(context, "ip", ip);
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            DLog.e(TAG, "doInBackground", e);
                        }

                        return ip;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        changingWifi = true;

                        views.setViewVisibility(R.id.prog, View.GONE);
                        views.setTextViewText(R.id.wifiStatus, context.getString(R.string.wifiOn));
                        views.setTextViewText(R.id.ip, result);

                        appWidgetManager.updateAppWidget(new ComponentName(context, WifiBatteryWidget.class), views);
                    }
                }.execute(context);
            }
        } else if (action.equals(WifiBatteryService.BATTERY_INFO)) {
            DLog.d(TAG, "===================================================================");
            DLog.d(TAG, "BATTERY_INFO");
            DLog.d(TAG, "===================================================================");

            setBatteryWithCfg(context, views);
        } else if (action.equals(TOGGLE_WIFI)) {
            DLog.d(TAG, "===================================================================");
            DLog.d(TAG, "TOGGLE_WIFI");
            DLog.d(TAG, "===================================================================");
            if (changingWifi) {
                return;
            }

            synchronized (changingWifi) {
                changingWifi = true;

                views.setViewVisibility(R.id.prog, View.VISIBLE);

                if (BkWifiManager.getInstance(context).isEnabled()) {
                    BkWifiManager.getInstance(context).wifiDisable();

                    changingWifi = false;
                } else {
                    BkWifiManager.getInstance(context).wifiEnable();
                }
            }
        } else if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            DLog.d(TAG, "===================================================================");
            DLog.d(TAG, "ACTION_BOOT_COMPLETED");
            DLog.d(TAG, "===================================================================");
            startService(context);
        } else if (action.equals("android.appwidget.action.APPWIDGET_UPDATE_OPTIONS")) {
            DLog.d(TAG, "===================================================================");
            DLog.d(TAG, "android.appwidget.action.APPWIDGET_UPDATE_OPTIONS ");
            DLog.d(TAG, "===================================================================");

            setGoneProgressBar(context, views);
            setBatteryWithCfg(context, views);
            setIpAddrWithCfg(context, views);
            setWifiStateWithCfg(context, views);
        } else {
            DLog.d(TAG, "===================================================================");
            DLog.d(TAG, "ELSE " + action);
            DLog.d(TAG, "===================================================================");
        }

        ComponentName watchWidget = new ComponentName(context, WifiBatteryWidget.class);
        appWidgetManager.updateAppWidget(watchWidget, views);
    }

    private void setBatteryWithCfg(final Context context, final RemoteViews views) {
        String battery = BkCfg.get(context, "battery", null);
        if (battery == null) {
            views.setTextViewText(R.id.battery, context.getString(R.string.getBatteryInfo));
        } else {
            views.setTextViewText(R.id.battery, battery);
        }
    }

    private void setGoneProgressBar(final Context context, final RemoteViews views) {
        views.setViewVisibility(R.id.prog, View.GONE);
        //        views.setOnClickPendingIntent(R.id.widgetLayout, getPendingSelfIntent(context, TOGGLE_WIFI, appWidgetId));
    }

    private void setWifiStateWithCfg(final Context context, final RemoteViews views) {
        String wifiState = BkCfg.get(context, "wifi-state", null);

        if (wifiState == null) {
            if (BkWifiManager.getInstance(context).isEnabled()) {
                views.setTextViewText(R.id.wifiStatus, context.getString(R.string.wifiOn));
            } else {
                views.setTextViewText(R.id.wifiStatus, context.getString(R.string.wifiOff));
                views.setTextViewText(R.id.ip, context.getString(R.string.invalidIp));
            }
        } else {
            if (wifiState.equals("1")) {
                views.setTextViewText(R.id.wifiStatus, context.getString(R.string.wifiOn));
            } else {
                views.setTextViewText(R.id.wifiStatus, context.getString(R.string.wifiOff));
                views.setTextViewText(R.id.ip, context.getString(R.string.invalidIp));
            }
        }
    }

    private void setIpAddrWithCfg(final Context context, final RemoteViews views) {
        String ip = BkCfg.get(context, "ip", null);

        if (ip == null) {
            views.setTextViewText(R.id.ip, BkWifiManager.getInstance(context).getIPAddr());
        } else {
            views.setTextViewText(R.id.ip, ip);
        }
    }

    private PendingIntent getPendingSelfIntent(Context context, String action, int id) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        intent.putExtra("id", id);

        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private void startService(Context context) {
        context.stopService(new Intent(context, WifiBatteryService.class));
        context.startService(new Intent(context, WifiBatteryService.class));
    }
}
