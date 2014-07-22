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
import net.sarangnamu.diablo.api.json.Hero;
import net.sarangnamu.diablo.api.json.ItemInfo;
import net.sarangnamu.diablo.api.json.Profile;
import net.sarangnamu.diablo.api.json.hero.Items;
import net.sarangnamu.diablo.api.json.hero.items.Item;
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
//            DLog.e(TAG, "===================================================================");
//            DLog.d(TAG, "response");
//            DLog.e(TAG, "===================================================================");
//            DLog.d(TAG, response);
            assertNotNull(response);

            Hero objHero = (Hero) JsonTool.toObj(response, Hero.class);
            DLog.e(TAG, "===================================================================");
            DLog.d(TAG, "name " + objHero.name);
            DLog.d(TAG, "class " + objHero.classType);
            DLog.d(TAG, "gender " + objHero.gender);
            DLog.e(TAG, "===================================================================");

            Items items = objHero.items;

            DLog.d(TAG, "===================================================================");
            DLog.d(TAG, "head item");
            printAndParseItem(items.head);

            DLog.d(TAG, "shoulders item");
            printAndParseItem(items.shoulders);

            DLog.d(TAG, "neck item");
            printAndParseItem(items.neck);

            DLog.d(TAG, "hands item");
            printAndParseItem(items.hands);

            DLog.d(TAG, "torso item");
            printAndParseItem(items.torso);

            DLog.d(TAG, "bracers item");
            printAndParseItem(items.bracers);

            DLog.d(TAG, "leftFinger item");
            printAndParseItem(items.leftFinger);

            DLog.d(TAG, "waist item");
            printAndParseItem(items.waist);

            DLog.d(TAG, "rightFinger item");
            printAndParseItem(items.rightFinger);

            DLog.d(TAG, "mainHand item");
            printAndParseItem(items.mainHand);

            DLog.d(TAG, "legs item");
            printAndParseItem(items.legs);

            DLog.d(TAG, "offHand item");
            printAndParseItem(items.offHand);

            DLog.d(TAG, "feet item");
            printAndParseItem(items.feet);
            DLog.d(TAG, "===================================================================");
        }

        DLog.e(TAG, "===================================================================");
    }

    public void printAndParseItem(Item out) {
        if (out != null) {
            DLog.d(TAG, "name  : " + out.name);
            DLog.d(TAG, "icon  : " + out.icon);
            DLog.d(TAG, "color : " + out.displayColor);
            DLog.d(TAG, "tip   : " + out.tooltipParams);

            parseItem(out.tooltipParams);
        }
    }

    public void parseItem(final String itemCode) {
        // http://kr.battle.net/api/d3/data/item/CoYBCKOn8NoJEgcIBBUVuTOJHdPMB2MdcYt38B07J28kHfiEvYwdZiMGUB3J-rygMIsCOJMCQABQElgEYJMCaikKDAgAEKP_1_eAgICAKxIZCKughPUBEgcIBBWFJ5SxMI8COABAAZABAIABRqUB-IS9jK0B5hXbDbUBf_lOXbgBtY7nvgLAAQEY1Zvi7A5QAFgCoAHVm-LsDqABlZ3y4Q6gAZO9xvwFoAHqg-iTBqABvsGNqw8
        Context context = getContext();
        String url = ApiBase.getItemInfoUrl(context, LANG, itemCode);
        DLog.d(TAG, "url : " + url);

        String response = downloadUrlData(url);
        assertNotNull(response);

        ItemInfo itemInfo = (ItemInfo) JsonTool.toObj(response, ItemInfo.class);
        assertNotNull(itemInfo);
    }
}
