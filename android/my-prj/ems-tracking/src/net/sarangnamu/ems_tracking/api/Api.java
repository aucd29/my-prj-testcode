/*
 * Api.java
 * Copyright 2013 Burke.Choi All rights reserved.
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
package net.sarangnamu.ems_tracking.api;

import net.sarangnamu.common.network.BkHttp;

public class Api {
    private static final String TAG = "Api";

    public static final String URL = "http://smart.epost.go.kr:8080/servlet/kpl.vis.common.svl.VisSVL";

    private static BkHttp http;

    private static void initHttp() {
        if (http == null) {
            http = new BkHttp();
        }
    }

    public static void tracking(String num) {

    }

    //    public static ArrayList<Notice> notices(int page) throws Exception {
    //        initHttp();
    //        http.setMethod("GET");
    //
    //        Map<String, String> params = new HashMap<String, String>();
    //        params.put("nPage", "" + page);
    //        String res = http.submit(URI_NOTICE, params);
    //
    //        return (ArrayList<Notice>) JsonTool.toObj(res, new TypeReference<List<Notice>>(){});
    //    }
    //
    //    public static boolean noticeWrite(String msg) throws Exception {
    //        initHttp();
    //        http.setMethod("POST");
    //
    //        Map<String, String> params = new HashMap<String, String>();
    //        params.put("sContent", msg);
    //        String res = http.submit(URI_NOTICE_WRITE, params);
    //
    //        return true;
    //    }
    //
    //    public static ArrayList<Study> study(int page) throws Exception {
    //        initHttp();
    //        http.setMethod("GET");
    //
    //        Map<String, String> params = new HashMap<String, String>();
    //        params.put("nPage", "" + page);
    //        String res = http.submit(URI_STUDY, params);
    //
    //        return (ArrayList<Study>) JsonTool.toObj(res, new TypeReference<List<Study>>(){});
    //    }
    //
    //    public static void categories() throws Exception {
    //        initHttp();
    //        http.setMethod("GET");
    //
    //        String res = http.submit(URI_CATEGORIES, null);
    //        JSONObject obj = new JSONObject(res);
    //    }
    //
    //    public static boolean login(String id, String pw) throws Exception {
    //        initHttp();
    //        http.setMethod("POST");
    //
    //        Map<String, String> params = new HashMap<String, String>();
    //        params.put("id", id);
    //        params.put("passwd", pw);
    //        params.put("rnd", "pre-" + System.currentTimeMillis() / 1000);
    //
    //        String res = http.submit(URI_LOGIN, params);
    //
    //        JSONObject json = new JSONObject(res);
    //        String login = "";
    //
    //        try {
    //            login = json.getString("res");
    //        } catch (Exception e) {
    //            DLog.e(TAG, "login", e);
    //        }
    //
    //        List<Cookie> cookies = http.getCookie();
    //        for (Cookie cookie : cookies) {
    //            if (cookie.getName().equals("sMemberID")) {
    //                userId = cookie.getName();
    //            }
    //        }
    //
    //        return login.equals("success") && userId != null;
    //    }

}
