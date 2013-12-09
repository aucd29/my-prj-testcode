/*
 * StatusWidget.java
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
package net.sarangnamu.ems_tracking.widget;

import net.sarangnamu.ems_tracking.EmsDataManager;
import net.sarangnamu.ems_tracking.R;
import net.sarangnamu.ems_tracking.api.Api;
import net.sarangnamu.ems_tracking.api.xml.Ems;
import net.sarangnamu.ems_tracking.db.EmsDbHelper;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.RemoteViews;

public class StatusWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds){
        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {
            final int appWidgetId = appWidgetIds[i];
            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            new AsyncTask<Context, Void, Boolean>() {
                @Override
                protected void onPreExecute() {
                    views.setViewVisibility(R.id.prog, View.VISIBLE);
                }

                @Override
                protected Boolean doInBackground(Context... contexts) {
                    Context context = contexts[0];

                    Cursor cr = EmsDbHelper.select();

                    while (cr.moveToNext()) {
                        String num    = cr.getString(0);
                        String status = cr.getString(2);

                        // 배달완료된 항목은 로딩시 체크하지 않는다.
                        if (!status.equals("배달완료")) {
                            Ems ems = Api.tracking(num);
                            EmsDataManager.getInstance().setEmsData(num, ems);
                            EmsDbHelper.update(cr.getInt(1), ems);
                        }
                    }

                    return false;
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    views.setViewVisibility(R.id.prog, View.GONE);

                    Cursor cr = EmsDbHelper.selectDesc();
                    int total = cr.getCount();
                    int unknown = 0;
                    int delivered = 0;

                    while (cr.moveToNext()) {
                        String status = cr.getString(3);

                        if (status.equals("미등록")) {
                            ++unknown;
                        } else if (status.equals("배달완료")) {
                            ++delivered;
                        }
                    }

                    views.setTextViewText(R.id.unknown,    "미등록 : " + unknown + "건");
                    views.setTextViewText(R.id.delivering, "배송중 : " + (total - unknown - delivered) + "건");
                    views.setTextViewText(R.id.delivered,  "완  료 : " + delivered + "건");

                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }
            }.execute(context);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
