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
package net.sarangnamu.home.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sarangnamu.common.json.JsonTool;
import net.sarangnamu.common.network.BkHttp;
import net.sarangnamu.home.api.json.Notice;
import net.sarangnamu.home.api.json.Study;

import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;

public class Api {
    private static final String TAG = "Api";

    public static final String URL = "http://www.sarangnamu.net/api";
    public static final String URI_NOTICE       = URL + "/notice.php";
    public static final String URI_STUDY        = URL + "/study.php";
    public static final String URI_CATEGORIES   = URL + "/categories.php";

    public static ArrayList<Notice> notices(int page) throws Exception {
        BkHttp http = new BkHttp();
        http.setMethod("GET");

        Map<String, String> params = new HashMap<String, String>();
        params.put("nPage", "" + page);
        String res = http.submit(URI_NOTICE, params);

        return (ArrayList<Notice>) JsonTool.toObj(res, new TypeReference<List<Notice>>(){});
    }

    public static ArrayList<Study> study(int page) throws Exception {
        BkHttp http = new BkHttp();
        http.setMethod("GET");

        Map<String, String> params = new HashMap<String, String>();
        params.put("nPage", "" + page);
        String res = http.submit(URI_STUDY, params);

        return (ArrayList<Study>) JsonTool.toObj(res, new TypeReference<List<Study>>(){});
    }

    public static void categories() throws Exception {
        BkHttp http = new BkHttp();
        http.setMethod("GET");
        String res = http.submit(URI_CATEGORIES, null);

        JSONObject obj = new JSONObject(res);

    }
}
