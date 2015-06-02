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
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class BkWifiStateReceiver extends BroadcastReceiver {
    private static final String TAG = "BkWifiStateReceiver";

    private Thread mThread = null;
    private WiFIConnectedListener mListenerConnected;
    private WiFiDisconnectedListener mListenerDisconnected;

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

        this.mListenerConnected = listenerConntected;
        this.mListenerDisconnected = listenerDisconnected;

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

        this.mListenerConnected = null;
        this.mListenerDisconnected = null;
    }

    private synchronized void sendConnected(final Context context) {
        if (mThread == null) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            if (BkWifiManager.getInstance(context).getIPAddr().equals("0.0.0.0") ||
                                    BkWifiManager.getInstance(context).getIPAddr() == "") {

                                Thread.sleep(500);
                            } else {
                                if (mListenerConnected != null) {
                                    mListenerConnected.onWiFiConnected();
                                } else {
                                    DLog.e(TAG, "sendConnected listenerConnected is null");
                                }

                                mThread = null;

                                break;
                            }
                        }
                    } catch (InterruptedException e) {
                    }
                }
            });
            mThread.start();
        }
    }

    private synchronized void sendDisconnected() {
        killIpCheckThread();

        if (mListenerDisconnected != null) {
            mListenerDisconnected.onWiFiDisconnected();
        } else {
            DLog.e(TAG, "sendDisconnected listenerDisconnected is null");
        }
    }

    private void killIpCheckThread() {
        if (mThread != null && mThread.isAlive()) {
            mThread.interrupt();
            mThread = null;
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
