/*
 * BkWifiStateReceiver.java
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

import net.sarangnamu.common.DLog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author aucd29@gmail.com
 * 
 */
public class BkWifiStateReceiver extends BroadcastReceiver {
    private static final String TAG = "BkWifiStateReceiver";

    private Thread thread = null;
    private WiFIConnectedListener listenerConnected;
    private WiFiDisconnectedListener listenerDisconnected;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            int status = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);

            if (status == WifiManager.WIFI_STATE_DISABLED) {
                sendDisconnected();
            } else if (status == WifiManager.WIFI_STATE_ENABLED) {
                sendConnected(context);
            }
        }
    }

    public void register(Context context, WiFIConnectedListener listenerConntected, WiFiDisconnectedListener listenerDisconnected) {
        if (listenerConntected == null) {
            DLog.e(TAG, "register listenr null");
            return;
        }

        this.listenerConnected = listenerConntected;
        this.listenerDisconnected = listenerDisconnected;

        IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        context.registerReceiver(this, filter);
    }

    public void unregister(Context context) {
        if (context == null) {
            DLog.e(TAG, "unregister context null");
            return;
        }

        sendDisconnected();
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
                                if (listenerConnected != null) {
                                    listenerConnected.onWiFiConnected();
                                } else {
                                    DLog.e(TAG, "sendConnected listenerConnected is null");
                                }

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

    private synchronized void sendDisconnected() {
        killIpCheckThread();

        if (listenerDisconnected != null) {
            listenerDisconnected.onWiFiDisconnected();
        } else {
            DLog.e(TAG, "sendDisconnected listenerDisconnected is null");
        }
    }

    private void killIpCheckThread() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            thread = null;
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////
    //
    // LISTENER
    //
    // //////////////////////////////////////////////////////////////////////////////////

    public interface WiFIConnectedListener {
        public void onWiFiConnected();
    }

    public interface WiFiDisconnectedListener {
        public void onWiFiDisconnected();
    }
}
