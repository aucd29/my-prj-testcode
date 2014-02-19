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
import net.sarangnamu.wifi_battery.R;
import net.sarangnamu.wifi_battery.service.WifiBatteryService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WifiBatteryWidget extends AppWidgetProvider {
    private static final String TAG = "WifiBatteryWidget";
    private String battery;

    @Override
    public void onUpdate(Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i = 0; i < N; i++) {
            final int appWidgetId = appWidgetIds[i];
            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            views.setTextViewText(R.id.battery, battery);

            // views.setOnClickPendingIntent(R.id.widgetLayout,
            // getPendingSelfIntent(context, BTN_REFRESH, appWidgetId));
            // loadWidgetInfo(context, views, appWidgetManager, appWidgetId, 0);
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

            DLog.d(TAG, "===================================================================");
            DLog.d(TAG, "RECEIVED BATTERY INFO " + batteryInfo);
            DLog.d(TAG, "===================================================================");

            views.setTextViewText(R.id.battery, batteryInfo);
        } else if (action.equals(WifiBatteryService.WIFI_CONNECTED)) {
            DLog.d(TAG, "===================================================================");
            DLog.d(TAG, "RECEIVED WIFI CONNECTED");
            DLog.d(TAG, "===================================================================");

            views.setTextViewText(R.id.wifiStatus, "WIFI CONNTECTED");
        } else if (action.equals(WifiBatteryService.WIFI_DISCONNECTED)) {
            DLog.d(TAG, "===================================================================");
            DLog.d(TAG, "RECEIVED WIFI DICONNECTED");
            DLog.d(TAG, "===================================================================");

            views.setTextViewText(R.id.wifiStatus, "WIFI DISCONNECTED");
        }

        ComponentName watchWidget = new ComponentName(context, WifiBatteryWidget.class);
        appWidgetManager.updateAppWidget(watchWidget, views);

        // if (BTN_REFRESH.equals(intent.getAction())) {
        // DbManager.getInstance().open(context, new EmsDbHelper(context));
        //
        // final AppWidgetManager appWidgetManager =
        // AppWidgetManager.getInstance(context);
        // final RemoteViews views = new RemoteViews(context.getPackageName(),
        // R.layout.widget);
        //
        // loadWidgetInfo(context, views, appWidgetManager,
        // intent.getIntExtra("id", 0), 1);
        // }
    }


    protected void loadWidgetInfo(final Context context, final RemoteViews views, final AppWidgetManager appWidgetManager, final int appWidgetId, final int type) {

    }

    // protected void loadWidgetInfo(final Context context, final RemoteViews
    // views, final AppWidgetManager appWidgetManager, final int appWidgetId,
    // final int type) {
    // new AsyncTask<Void, Void, Boolean>() {
    // @Override
    // protected void onPreExecute() {
    // views.setViewVisibility(R.id.prog, View.VISIBLE);
    //
    // if (type == 1) {
    // ComponentName watchWidget = new ComponentName(context,
    // StatusWidget.class);
    // appWidgetManager.updateAppWidget(watchWidget, views);
    // }
    // }
    //
    // @Override
    // protected Boolean doInBackground(Void... vd) {
    // Cursor cr = EmsDbHelper.select();
    //
    // while (cr.moveToNext()) {
    // String num = cr.getString(0);
    // String status = cr.getString(2);
    //
    // if (!status.equals("배달완료")) {
    // Ems ems = Api.tracking(num);
    // EmsDataManager.getInstance().setEmsData(num, ems);
    // EmsDbHelper.update(cr.getInt(1), ems);
    // }
    // }
    //
    // return false;
    // }
    //
    // @Override
    // protected void onPostExecute(Boolean result) {
    // views.setViewVisibility(R.id.prog, View.GONE);
    //
    // Cursor cr = EmsDbHelper.selectDesc();
    // int total = cr.getCount();
    // int unknown = 0;
    // int delivered = 0;
    //
    // while (cr.moveToNext()) {
    // String status = cr.getString(3);
    //
    // if (status.equals("미등록")) {
    // ++unknown;
    // } else if (status.equals("배달완료")) {
    // ++delivered;
    // }
    // }
    //
    // views.setTextViewText(R.id.unknown, "미등록 : " + unknown + "건");
    // views.setTextViewText(R.id.delivering, "배송중 : " + (total - unknown -
    // delivered) + "건");
    // views.setTextViewText(R.id.delivered, "완 \u00A0\u00A0료 : " + delivered +
    // "건");
    //
    // if (type == 1) {
    // ComponentName watchWidget = new ComponentName(context,
    // StatusWidget.class);
    // appWidgetManager.updateAppWidget(watchWidget, views);
    // } else {
    // appWidgetManager.updateAppWidget(appWidgetId, views);
    // }
    // }
    // }.execute();
    // }
}
