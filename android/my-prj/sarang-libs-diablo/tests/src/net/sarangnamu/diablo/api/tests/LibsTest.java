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
import android.content.Context;
import android.test.AndroidTestCase;

public class LibsTest extends AndroidTestCase {
    private static final String TAG = "DiabloLibsTest";
    private static final String LANG = "kr";
    private static final String BID = "burke";
    private static final int BTAG = 1935;
    private Profile profile;

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
            BkHttp http = new BkHttp();
            http.setMethod("GET");
            response = http.submit(url, null);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }

        assertNotNull(response);

        profile = (Profile) JsonTool.toObj(response, Profile.class);
        assertNotNull(profile);

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, profile.battleTag);
        DLog.d(TAG, BID + "#" + BTAG);
        DLog.d(TAG, "===================================================================");

        assertNotSame(profile.battleTag.toLowerCase(), BID + "#" + BTAG);
    }

    public void testGetHeroData() {
        // http://kr.battle.net/api/d3/profile/burke-1935/hero/12541198
        assertNotNull(profile);

        Context context = getContext();
        String url = ApiBase.getHeroInfoUrl(context, LANG, BID, BTAG, profile.heroes.get(0).id);
        assertNotNull(url);

        String response = null;

        try {
            BkHttp http = new BkHttp();
            http.setMethod("GET");
            response = http.submit(url, null);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(e.getMessage(), false);
        }

        assertNotNull(response);
    }
}
