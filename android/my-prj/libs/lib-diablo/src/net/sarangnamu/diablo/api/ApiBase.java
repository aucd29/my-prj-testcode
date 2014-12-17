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

import java.io.File;

import net.sarangnamu.common.BkCfg;
import net.sarangnamu.common.DLog;
import net.sarangnamu.common.network.BkDownloader;
import net.sarangnamu.common.network.BkHttp;
import net.sarangnamu.common.network.BkWifiManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
    public static final String USER_PROFILE = "http://%s/api/d3/profile/%s/";           // domain, battle tag
    public static final String HER0_PROFILE = "http://%s/api/d3/profile/%s/hero/%d";    // domain, battle tag, hero id
    public static final String ITEM_INFO    = "http://%s/api/d3/data/%s";               // domain, item code
    public static final String ICON_INFO    = "http://media.blizzard.com/d3/icons/items/large/%s.png";

    // HOSTS
    public static final String HOST_DEFAULT = ".battle.net";
    public static final String HOST_CH = "www.battlenet.com.cn";

    // PROTOCOL
    public static final String PROTOCOL = "http://";

    private static String getHost(String hostType) {
        if (hostType.equals("ch")) {
            return HOST_CH;
        }

        return hostType + HOST_DEFAULT;
    }

    public static String getProfileUrl(Context context, String hostType, String battleTag) {
        // ex) http://kr.battle.net/api/d3/profile/burke-1935/
        return String.format(USER_PROFILE, getHost(hostType), battleTag);
    }

    public static String getHeroInfoUrl(Context context, String hostType, String battleTag, int heroId) {
        // ex) http://kr.battle.net/api/d3/profile/burke-1935/hero/$heroId
        return String.format(HER0_PROFILE, getHost(hostType), battleTag, heroId);
    }

    public static String getItemInfoUrl(Context context, String hostType, String itemCode) {
        // ex) /api/d3/data/item/COGHsoAIEgcIBBXIGEoRHYQRdRUdnWyzFB2qXu51MA04kwNAAFAKYJMD
        return String.format(ITEM_INFO, getHost(hostType), itemCode);
    }

    public static String getItemPngUrl(Context context, String itemCode) {
        // ex) http://media.blizzard.com/d3/icons/items/large/unique_helm_set_07_x1_demonhunter_male.png
        return String.format(ICON_INFO, itemCode);
    }

    public static Drawable getItemDrawable(Context context, String url, String itemCode) {
        StringBuilder sb = new StringBuilder();
        sb.append(BkCfg.externalFilePath(context));
        sb.append("/");
        sb.append(itemCode);
        sb.append(".png");

        boolean exist = true;
        File img = new File(sb.toString());
        if (!img.exists()) {
            try {
                BkDownloader dn = new BkDownloader();
                dn.download(url, sb.toString(), null);
            } catch (Exception e) {
                exist = false;
                DLog.e(TAG, "getItemDrawable", e);
            }
        }

        if (exist) {
            return Drawable.createFromPath(sb.toString());
        } else {
            return null;
        }
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
