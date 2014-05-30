/*
 * LibsTest.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.common.network.tests;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.network.BkDownloader;
import net.sarangnamu.common.network.BkWifiStateReceiver;
import net.sarangnamu.common.network.BkWifiStateReceiver.WiFIConnectedListener;
import net.sarangnamu.common.network.BkWifiStateReceiver.WiFiDisconnectedListener;
import net.sarangnamu.common.network.ParameterEncoder;
import android.test.AndroidTestCase;

public class LibsTest extends AndroidTestCase {
    private static final String TAG = "NetworkLibsTest";

    public void testDownload() {
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "test download ");
        DLog.d(TAG, "===================================================================");

        BkDownloader downloader = new BkDownloader();
//        downloader.download(addr, dest, new BkHandlerListener() {
//            @Override
//            public void onUpdate(int read) {
//            }
//
//            @Override
//            public void onTotalSize(long size) {
//            }
//
//            @Override
//            public void onSuccess() {
//            }
//
//            @Override
//            public void onError(String errMessage) {
//            }
//
//            @Override
//            public void onCancelled() {
//            }
//
//            @Override
//            public boolean isCancelled() {
//                return false;
//            }
//        });
    }

    public void testHttp() {
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "test http");
        DLog.d(TAG, "===================================================================");
    }

    public void testNetwork() {
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "test network");
        DLog.d(TAG, "===================================================================");
    }

    public void testWifiManager() {
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "test wifi manager");
        DLog.d(TAG, "===================================================================");
    }

    public void testWifiState() {
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "test wifi state");
        DLog.d(TAG, "===================================================================");

        BkWifiStateReceiver receiver = new BkWifiStateReceiver();
        receiver.register(getContext(), new WiFIConnectedListener() {
            @Override
            public void onWiFiConnected() {
                DLog.d(TAG, "wifi connected");
            }
        }, new WiFiDisconnectedListener() {
            @Override
            public void onWiFiDisconnected() {
                DLog.d(TAG, "wifi disconnected");
            }
        });

        sleep(5000);

        receiver.unregister(getContext());
    }

    public void testParameterEncode() {
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "test parameter encode");
        DLog.d(TAG, "===================================================================");

        ParameterEncoder parameter = new ParameterEncoder();
        parameter.add("test", "kaka");
        parameter.add("test2", "한글");
        parameter.add("test3", "￡￥§ⓒ");

//      UTF-8
//        한글    : %ED%95%9C%EA%B8%80
//       ￡￥§ⓒ : %EF%BF%A1%EF%BF%A5%C2%A7%E2%93%92
        assertEquals("PARAMETER ENCODE", parameter.encode(),
                "test=kaka&test2=%ED%95%9C%EA%B8%80&test3=%EF%BF%A1%EF%BF%A5%C2%A7%E2%93%92");
    }

    private void sleep(final int ms) {
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            DLog.e(TAG, "testWifiState", e);
        }
    }
}

