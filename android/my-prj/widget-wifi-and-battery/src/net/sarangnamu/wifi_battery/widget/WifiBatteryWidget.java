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
import android.view.View;
import android.widget.RemoteViews;

public class WifiBatteryWidget extends AppWidgetProvider {
	private static final String TAG = "WifiBatteryWidget";
	private static final String TOGGLE_WIFI = "toggleWifi";

	private String battery;
	private Boolean changingWifi = false;

	@Override
	public void onUpdate(Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final int N = appWidgetIds.length;

		DLog.d(TAG, "===================================================================");
		DLog.d(TAG, "WIFI AND BATTERY WIDGET START");
		DLog.d(TAG, "===================================================================");

		for (int i = 0; i < N; i++) {
			final int appWidgetId = appWidgetIds[i];
			final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

			views.setTextViewText(R.id.battery, battery);
			views.setOnClickPendingIntent(R.id.widgetLayout, getPendingSelfIntent(context, TOGGLE_WIFI, appWidgetId));
		}

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	protected PendingIntent getPendingSelfIntent(Context context, String action, int id) {
		DLog.d(TAG, "===================================================================");
		DLog.d(TAG, "CLICKED EVENT " + action);
		DLog.d(TAG, "===================================================================");

		Intent intent = new Intent(context, getClass());
		intent.setAction(action);
		intent.putExtra("id", id);

		return PendingIntent.getBroadcast(context, 0, intent, 0);
	}

	@Override
	public void onReceive(Context context, final Intent intent) {
		final String action = intent.getAction();
		final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

		if (action.equals(WifiBatteryService.BATTERY_INFO)) {
			String batteryInfo = intent.getStringExtra(WifiBatteryService.BATTERY_INFO);
			views.setTextViewText(R.id.battery, batteryInfo);
		} else if (action.equals(WifiBatteryService.WIFI_CONNECTED)) {
			views.setTextViewText(R.id.wifiStatus, "WIFI ON");
			views.setViewVisibility(R.id.prog, View.GONE);
		} else if (action.equals(WifiBatteryService.WIFI_DISCONNECTED)) {
			views.setTextViewText(R.id.wifiStatus, "WIFI OFF");
			views.setViewVisibility(R.id.prog, View.GONE);
		} else if (action.equals(TOGGLE_WIFI)) {
			if (changingWifi) {
				return;
			}

			synchronized (changingWifi) {
				changingWifi = true;
			}

			views.setViewVisibility(R.id.prog, View.VISIBLE);

			DLog.d(TAG, "===================================================================");
			DLog.d(TAG, "TOGGLE WIFI ");
			DLog.d(TAG, "===================================================================");

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
		}

		ComponentName watchWidget = new ComponentName(context, WifiBatteryWidget.class);
		appWidgetManager.updateAppWidget(watchWidget, views);
	}
}
