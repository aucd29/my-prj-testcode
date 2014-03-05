/*
 * BkNetwork.java
 * Copyright 2013 Burke Choi All rights reserved.
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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * {@code
 * <pre>

 * </pre>}
 * 
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class BkNetwork {
    private static final int BLEUTOOH = 0x00000007; // not defined at android sdk

    public static boolean checkNetworkInfo(Context context) {
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo bluetooth = manager.getNetworkInfo(BLEUTOOH);

            if (mobile != null && mobile.isConnected()) {
                return true;
            }

            if (wifi != null && wifi.isConnected()) {
                return true;
            }

            if (bluetooth != null && bluetooth.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
