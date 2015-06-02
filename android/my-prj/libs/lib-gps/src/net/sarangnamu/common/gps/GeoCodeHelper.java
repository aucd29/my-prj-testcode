/*
 * GeoCodeHelper.java
 */
package net.sarangnamu.common.gps;

import java.util.List;
import java.util.Locale;

import net.sarangnamu.common.DLog;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.http.AndroidHttpClient;
import android.os.Handler;
import android.os.Message;

public class GeoCodeHelper extends Handler {
    private static final String TAG = "GeoCodeHelper";

    private static final String FROM_GOOGLE =
            "http://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&sensor=false&language=en";
    private static GeoCodeHelper sInst;
    private String mCityName;
    private GeoCodeListener mListener;

    public static GeoCodeHelper getInstance() {
        if (sInst == null) {
            sInst = new GeoCodeHelper();
        }

        return sInst;
    }

    private GeoCodeHelper() {

    }

    @Override
    public void handleMessage(Message msg) {
        if (mListener != null) {
            mListener.cityName((String) msg.obj);
        }
    }

    private void sendMessage(int type, Object obj) {
        Message msg = obtainMessage();
        msg.what    = type;
        msg.obj     = obj;
        sendMessage(msg);
    }

    public void fetchCity(final Context context, final GpsHelper gps) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Geocoder.isPresent()) {
                        try {
                            fromGeoCoder(context, gps);
                        } catch (Exception e) {
                            fromGoogle(context, gps);
                        }
                    } else {
                        fromGoogle(context, gps);
                    }
                } catch (Exception e) {
                    DLog.e(TAG, "run");
                }
            }
        }).start();
    }

    private void fromGeoCoder(Context context, GpsHelper gps) throws Exception {
        Geocoder geocoder = new Geocoder(context, Locale.US);
        List<Address> addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 1);

        if (addresses.size() > 0) {
            mCityName = addresses.get(0).getLocality();
            sendMessage(0, mCityName);
        }
    }

    private void fromGoogle(Context context, GpsHelper gps) throws Exception {
        String uri = String.format(FROM_GOOGLE, gps.getLatitude(), gps.getLongitude());

        AndroidHttpClient http = AndroidHttpClient.newInstance("Android");
        JSONObject res = new JSONObject(http.execute(new HttpGet(uri), new BasicResponseHandler()));
        JSONArray results = (JSONArray) res.get("results");
        http.close();

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);
            if (result.has("address_components")) {
                JSONArray addressComponents = result.getJSONArray("address_components");

                for (int j = 0; j < addressComponents.length(); j++) {
                    JSONObject addressComponent = addressComponents.getJSONObject(j);

                    if (result.has("types")) {
                        JSONArray types = addressComponent.getJSONArray("types");
                        for (int k = 0; k < types.length(); k++) {
                            if ("locality".equals(types.getString(k)) && mCityName == null) {
                                if (addressComponent.has("long_name")) {
                                    mCityName = addressComponent.getString("long_name");
                                    sendMessage(0, mCityName);

                                    return ;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String getCityName() {
        return mCityName;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // GeoCodeListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public void setOnGeoCodeListener(GeoCodeListener l) {
        mListener = l;
    }

    public interface GeoCodeListener {
        public void cityName(String name);
    }
}
