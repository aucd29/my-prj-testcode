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
import android.content.Context;

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

    // HOSTS
    public static final String HOST_DEFAULT = ".battle.net";
    public static final String HOST_CH = "www.battlenet.com.cn";

    // PROTOCOL
    public static final String PROTOCOL = "http://";

    public String getProfileUrl(Context context, String  hostType, final String tagName, final int tagCode) {
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

}
