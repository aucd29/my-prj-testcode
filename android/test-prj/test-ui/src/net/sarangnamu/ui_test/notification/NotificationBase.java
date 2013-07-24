/*
 * NotificationBase.java
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.ui_test.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class NotificationBase {
    private static final String FILTER = "net.sarangnamu.ui_test.receiver.NOTIFICATION_RECEIVER";

    protected Context context;
    protected RemoteViews remoteViews;
    protected NotificationManager nm;

    public NotificationBase(Context context, int layoutId) {
        this.context = context;

        remoteViews = new RemoteViews(context.getPackageName(), layoutId);
    }

    public RemoteViews getRemoteViews() {
        return remoteViews;
    }

    public PendingIntent sendData(int reqCode, String name, String cmd) {
        Intent intent = new Intent(FILTER);
        intent.putExtra(name, cmd);

        return PendingIntent.getBroadcast(this.context, reqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void invalidate() {
        //        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
        //        .setSmallIcon(R.drawable.logosmall)
        //        .setAutoCancel(false)
        //        .setPriority(Notification.PRIORITY_HIGH)
        //        .setOngoing(true);
        //
        //        Notification noti = builder.build();
        //        noti.bigContentView = remoteViews;
        //
        //        nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        //        nm.notify(0, noti);
    }

    public void setOnClickPendingIntent(int id, int reqCode, String name, String data) {
        remoteViews.setOnClickPendingIntent(id, sendData(reqCode, name, data));
    }
}
