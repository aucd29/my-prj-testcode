/*
 * LockScreenServiceBase.java
 * Copyright 2013 OBIGO Inc. All rights reserved.
 *             http://www.obigo.com
 */
package net.sarangnamu.ui_test.common;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public abstract class LockScreenServiceBase extends Service {
    private static final String TAG = "LockScreenServiceBase";
    private LockScreenReceiver receiver;

    @Override
    public void onCreate() {
        setLockscreenReceiver();
    }

    @Override
    public void onDestroy() {
        //resetLockscreenReceiver();
        Log.d(TAG, "LockScreenServiceBase.onDestroy");

        super.onDestroy();
    }

    public void setLockscreenReceiver() {
        receiver = new LockScreenReceiver();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, filter);
    }

    public void resetLockscreenReceiver() {
        unregisterReceiver(receiver);
        receiver = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // LockScreenReceiver
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class LockScreenReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "LockScreenReceiver.onReceive");

            Intent lockIntent = getLockScreenIntent(context);
            lockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(lockIntent);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public abstract Intent getLockScreenIntent(Context context);
}
