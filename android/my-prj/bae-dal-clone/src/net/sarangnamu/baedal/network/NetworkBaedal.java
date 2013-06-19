/*
 * Copyright (c) 2003-2011, cheol-dong choi, twitter @aucd29
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.sarangnamu.baedal.network;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.location.Location;
import android.util.Log;

import net.sarangnamu.utils.DrawableManager;
import net.sarangnamu.utils.Network;
import net.sarangnamu.baedal.config.ConfigBaedal;

public class NetworkBaedal {
	public static JSONObject getMainData() throws Exception {
		String main = Network.openURL(ConfigBaedal.MAIN_URI, "GET", null);
		if (main == null || main.length() == 0) {
			throw new Exception();
		}

		return (JSONObject) new JSONTokener(main).nextValue();
	}

	public static void downloadMainBannerImages() throws JSONException {
		JSONArray bannerList = ConfigBaedal.mainContent.getJSONArray(ConfigBaedal.BANNER_LIST);
		for (int i=0; i<bannerList.length(); ++i) {
			DrawableManager.getInstance().fetchDrawable(bannerList.getJSONObject(i).getString(ConfigBaedal.BANNER_IMG));
		}
	}
	
	public static void getMarketData(int category, int page) throws Exception {
		Location location = ConfigBaedal.nmea.getLocation();
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("command", "list_");
		params.put("category", Integer.toString(category));
		params.put("page", Integer.toString(page));
		params.put("latitude", Double.toString(location.getLatitude()));
		params.put("longitude", Double.toString(location.getLongitude()));
		
		String data = Network.openURL(ConfigBaedal.MARKET_URI, "POST", params);		
		ConfigBaedal.marketList = (JSONObject) new JSONTokener(data).nextValue();
		
		Log.d("", "Market List: " + ConfigBaedal.marketList);
	}
}
