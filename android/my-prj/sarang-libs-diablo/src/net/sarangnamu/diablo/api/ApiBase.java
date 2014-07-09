/*
 * ApiBase.java
 * Copyright 2014 Burke Choi All right reserverd.
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
package net.sarangnamu.diablo.api;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.network.BkHttp;
import net.sarangnamu.common.network.BkWifiManager;
import android.content.Context;
import android.os.AsyncTask;

/**
 * <pre>
 * {@code
 *
 * }
 * </pre>
 *
 * @see http://blizzard.github.io/api-wow-docs/
 * @see http://blizzard.github.io/d3-api-docs/#
 *
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class ApiBase {
    private static final String TAG = "ApiBase";

    // APIS
    public static final String USER_PROFILE = "/api/d3/profile/";
//    public static final String HER0_PROFILE = "/api/d3/profile/hero/";
    public static final String ITEM_INFO    = "/api/d3/data/";

    // HOSTS
    public static final String HOST_DEFAULT = ".battle.net";
    public static final String HOST_CH = "www.battlenet.com.cn";

    // PROTOCOL
    public static final String PROTOCOL = "http://";

    public static String getProfileUrl(Context context, String  hostType, String tagName, int tagCode) {
        // ex) http://kr.battle.net/api/d3/profile/burke-1935/

        try {
            String host = null;
            if (hostType.equals("ch")) {
                host = HOST_CH;
            } else {
                host = hostType + HOST_DEFAULT;
            }

            return String.format("%s%s%s%s-%d/", PROTOCOL, host, USER_PROFILE, tagName, tagCode);
        } catch (Exception e) {
            DLog.e(TAG, "getProfileUrl", e);
        }

        return null;
    }

    public static String getHeroInfoUrl(Context context, String hostType, String tagName, int tagCode, int heroId) {
        // ex) http://kr.battle.net/api/d3/profile/burke-1935/hero/$heroId
        try {
            String userProfileUrl = getProfileUrl(context, hostType, tagName, tagCode);
            return userProfileUrl + "hero/" + heroId;
        } catch (Exception e) {
            DLog.e(TAG, "getItemInfoUrl", e);
        }

        return null;
    }

    public static String getItemInfoUrl(Context context, String hostType, String itemCode) {
        // ex) /api/d3/data/item/COGHsoAIEgcIBBXIGEoRHYQRdRUdnWyzFB2qXu51MA04kwNAAFAKYJMD
        try {
            String host = null;
            if (hostType.equals("ch")) {
                host = HOST_CH;
            } else {
                host = hostType + HOST_DEFAULT;
            }

            return String.format("%s%s%s%s", PROTOCOL, host, ITEM_INFO, itemCode);
        } catch (Exception e) {
            DLog.e(TAG, "getItemInfoUrl", e);
        }

        return null;
    }

    public String getLocale(Context context) throws Exception {
        if (context == null) {
            throw new Exception("getLocale <context == null>");
        }

        return context.getResources().getConfiguration().locale.getDisplayName();
    }

    public String getLang() {
//        String locale = getLocale(context);
//        if (locale.equals(Locale.KOREA)) {
//            host = HOST_KR;
//        } else if (locale.equals(Locale.TAIWAN)) {
//            host = HOST_TW;
//        } else if (locale.equals(Locale.CHINA)) {
//            host = HOST_CH;
//        } else if (locale.equals(Locale.GERMAN) || locale.equals(Locale.ENGLISH) || locale.equals(Locale.FRANCE)
//                || locale.equals(Locale.ITALY) || locale.equals(Locale.p) || locale.equals(Locale.GERMAN)
//                || locale.equals(Locale.GERMAN)) {
//            en_GB
//            es_ES
//            fr_FR
//            ru_RU
//            de_DE
//            pt_PT
//            it_IT
//        } else {
//            host = HOST_US;
//        }

        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // NETWORK
    //
    ////////////////////////////////////////////////////////////////////////////////////

    private static boolean isNetwork(Context context) {
        return BkWifiManager.getInstance(context).isEnabled();
    }

    public static void getDataSync(Context context, final String url, final NetworkListener listener) {
        if (listener == null) {
            DLog.e(TAG, "ERROR::getJson <NetworkListener == null>");
            return ;
        }

        if (!isNetwork(context)) {
            DLog.e(TAG, "getJson <Not avaliable network");
            // TODO r.string
            listener.onError("Please check your a network status");
            return ;
        }

        String errMsg = null, response = null;
        listener.onStart();

        try {
            BkHttp http = new BkHttp();
            http.setMethod("GET");
            response = http.submit(url, null);
        } catch (NullPointerException e) {
            DLog.e(TAG, "doInBackground", e);
            errMsg = e.getMessage();
        } catch (Exception e) {
            DLog.e(TAG, "doInBackground", e);
            errMsg = e.getMessage();
        }

        if (response == null) {
            DLog.e(TAG, "ERROR::onPostExecute <result == null>");
            if (errMsg == null) {
                listener.onError("error result == null");
            } else {
                listener.onError(errMsg);
            }

            return ;
        }

        listener.onEnd(response);
    }

    public static void getData(Context context, final String url, final NetworkListener listener) {
        if (listener == null) {
            DLog.e(TAG, "ERROR::getJson <NetworkListener == null>");
            return ;
        }

        if (!isNetwork(context)) {
            DLog.e(TAG, "getJson <Not avaliable network");
            // TODO r.string
            listener.onError("Please check your a network status");
            return ;
        }

        new AsyncTask<Void, Void, String>() {
            private String errMsg = null;

            @Override
            protected void onPreExecute() {
                listener.onStart();
            }

            @Override
            protected String doInBackground(Void... data) {
                String response = null;

                try {
                    BkHttp http = new BkHttp();
                    http.setMethod("GET");
                    response = http.submit(url, null);
                } catch (NullPointerException e) {
                    DLog.e(TAG, "doInBackground", e);
                    errMsg = e.getMessage();
                } catch (Exception e) {
                    DLog.e(TAG, "doInBackground", e);
                    errMsg = e.getMessage();
                }

                return response;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result == null) {
                    DLog.e(TAG, "ERROR::onPostExecute <result == null>");
                    if (errMsg == null) {
                        listener.onError("error result == null");
                    } else {
                        listener.onError(errMsg);
                    }

                    return ;
                }

                listener.onEnd(result);
            }
        }.execute();
    }

    public interface NetworkListener {
        public void onStart();
        public void onEnd(final String response);
        public void onError(final String errMsg);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // FROM BATTLE NET
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public void getProfile(Context context) {

    }

}
