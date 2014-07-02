/*
 * LibsTest.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.diablo.api.tests;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.json.JsonTool;
import net.sarangnamu.diablo.api.ApiBase;
import net.sarangnamu.diablo.api.ApiBase.NetworkListener;
import net.sarangnamu.diablo.api.json.Profile;
import android.content.Context;
import android.test.AndroidTestCase;

public class LibsTest extends AndroidTestCase {
    private static final String TAG = "DiabloLibsTest";
    private static final String LANG = "kr";
    private static final String BID = "burke";
    private static final int BTAG = 1935;

    public void testProfileUrl() {
        Context context = getContext();
        String url = ApiBase.getProfileUrl(context, LANG, BID, BTAG);
        assertEquals(url, "http://kr.battle.net/api/d3/profile/burke-1935/");
    }

    public void testGetProfileData() {
        Context context = getContext();
        String url = ApiBase.getProfileUrl(context, LANG, BID, BTAG);
        assertEquals(url, "http://kr.battle.net/api/d3/profile/burke-1935/");

        ApiBase.getDataSync(context, url, new NetworkListener() {
            @Override
            public void onStart() {
                DLog.d(TAG, "on start ==");
            }

            @Override
            public void onError(String errMsg) {
                DLog.e(TAG, "onError ==" + errMsg);
                assertTrue(true);
            }

            @Override
            public void onEnd(String response) {
                assertNotNull(response);

                DLog.d(TAG, "===================================================================");
                DLog.d(TAG, response);
                DLog.d(TAG, "===================================================================");

                parsingProfile(response);
            }
        });
    }

    public void parsingProfile(String json) {
        Profile profile = (Profile) JsonTool.toObj(json, Profile.class);


        assertNotNull(profile);
    }
}
