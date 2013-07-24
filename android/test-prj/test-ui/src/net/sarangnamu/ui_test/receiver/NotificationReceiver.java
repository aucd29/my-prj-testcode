/*
 * NotificationReceiver.java
 * Copyright 2013 sarangnamu.net All rights reserved.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.ui_test.receiver;

import net.sarangnamu.ui_test.MainActivity;
import net.sarangnamu.ui_test.common.ConfigPref;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


/**
 * 
 * @author @aucd29
 *
 */
public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationReceiver";

    public static final String NOTI = "NOTI";
    public static final String CMD = "cmd";
    public static final String BTN = "btn";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bd = intent.getExtras();
        String cmd = bd.getString(CMD);
        String btn = bd.getString(BTN);

        if (cmd != null) {
            int value;
            if (cmd.equals("plus")) {
                value = Integer.parseInt(ConfigPref.getValue(context, CMD));
                ConfigPref.setValue(context, CMD, value + 1 + "");
                sendToActivity(context);
            } else {
                value = 0;
            }
        }

        if (btn != null) {
            Log.d(TAG, "onReceive " + btn);
            ConfigPref.setValue(context, BTN, btn);
            sendToActivity(context);
        }
    }

    private void sendToActivity(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra(NOTI, NOTI);
        //        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        try {
            pi.send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }
    }
}
