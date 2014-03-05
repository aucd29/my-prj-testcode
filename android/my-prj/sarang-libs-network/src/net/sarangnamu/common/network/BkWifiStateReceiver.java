/*
 * BkWifi.java
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
package net.sarangnamu.common.network;

import java.util.ArrayList;

import net.sarangnamu.common.DLog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

public class BkWifiStateReceiver extends BroadcastReceiver {
    private static final String TAG = "BkWifiStateReceiver";

    private final ArrayList<IWiFiDisconnecting> listenerList = new ArrayList<IWiFiDisconnecting>();
    private IWiFIConnected l = null;
    private Thread thread = null;

    public interface IWiFIConnected {
        public void onWiFiConnected();
    }

    public interface IWiFiDisconnecting {
        public void onWiFiDisconnecting();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            int status = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);

            if (status == WifiManager.WIFI_STATE_DISABLED) {
                sendDisconnecting();
            } else if (status == WifiManager.WIFI_STATE_ENABLED) {
                sendConnected(context);
            }
        }
    }

    public void addListener(IWiFiDisconnecting status) {
        if (status == null) {
            DLog.e(TAG, "addListener status null");
            return;
        }

        synchronized (listenerList) {
            listenerList.add(status);
        }
    }

    public void clearListener() {
        synchronized (listenerList) {
            listenerList.clear();
        }
    }

    public void register(Context context, IWiFIConnected listener) {
        if (listener == null) {
            DLog.e(TAG, "register listenr null");
            return;
        }

        l = listener;

        IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        context.registerReceiver(this, filter);
    }

    public void unregister(Context context) {
        if (context == null) {
            DLog.e(TAG, "unregister context null");
            return;
        }

        sendDisconnecting();
        context.unregisterReceiver(this);
    }

    private synchronized void sendConnected(final Context context) {
        if (thread == null) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            if (BkWifiManager.getInstance(context).getIPAddr().equals("0.0.0.0") || BkWifiManager.getInstance(context).getIPAddr() == "") {

                                Thread.sleep(500);
                            } else {
                                l.onWiFiConnected();
                                thread = null;
                                break;
                            }
                        }
                    } catch (InterruptedException e) {
                    }
                }
            });
            thread.start();
        }
    }

    private synchronized void sendDisconnecting() {
        killIpCheckThread();

        // DLog.d(TAG, "disconnecting size : " + listenerList.size());

        synchronized (listenerList) {
            for (IWiFiDisconnecting l : listenerList) {
                l.onWiFiDisconnecting();
            }

            // for (int i = 0; i < listenerList.size(); ++i) {
            // listenerList.remove(0);
            // }
        }

        // clearListener();
    }

    private void killIpCheckThread() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            thread = null;
        }
    }
}
