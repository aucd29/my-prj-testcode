/*
 * LibsTest.java
 * Copyright 2014 Burke Choi All right reserverd.
 *             http://www.sarangnamu.net
 */
package net.sarangnamu.diablo.api.tests;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.json.JsonTool;
import net.sarangnamu.common.network.BkHttp;
import net.sarangnamu.common.network.BkWifiManager;
import net.sarangnamu.diablo.api.ApiBase;
import net.sarangnamu.diablo.api.json.Profile;
import net.sarangnamu.diablo.api.json.profile.Heroes;
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
        assertTrue(BkWifiManager.getInstance(context).isEnabled());

        String response = null;

        try {
            response = downloadUrlData(url);
            assertNotNull(response);

            Profile profile = (Profile) JsonTool.toObj(response, Profile.class);
            assertNotNull(profile);

            DLog.d(TAG, "===================================================================");
            DLog.d(TAG, profile.battleTag);
            DLog.d(TAG, BID + "#" + BTAG);
            DLog.d(TAG, "===================================================================");

            assertNotSame(profile.battleTag.toLowerCase(), BID + "#" + BTAG);

            parseHeroes(profile);

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }
    }

    public String downloadUrlData(final String url) {
        String response = null;

        try {
            BkHttp http = new BkHttp();
            http.setTimeout(20000, 20000);
            http.setMethod("GET");
            return http.submit(url, null);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }

        return response;
    }

    public void parseHeroes(Profile profile) {
        if (profile == null) {
            DLog.e(TAG, "===================================================================");
            DLog.e(TAG, "parseHeroes");
            DLog.e(TAG, "===================================================================");
            assertTrue(false);
            return ;
        }

        for (Heroes hero : profile.heroes) {
            Context context = getContext();

            DLog.e(TAG, "===================================================================");
            DLog.d(TAG, "NAME : " + hero.name);

            String url = ApiBase.getHeroInfoUrl(context, LANG, BID, BTAG, hero.id);
            DLog.e(TAG, "===================================================================");
            DLog.d(TAG, url);
            assertNotNull(url);

            String response = downloadUrlData(url);
            DLog.e(TAG, "===================================================================");
            DLog.d(TAG, "response");
            DLog.e(TAG, "===================================================================");
            DLog.d(TAG, response);


            assertNotNull(response);
        }

        DLog.e(TAG, "===================================================================");
    }
}
