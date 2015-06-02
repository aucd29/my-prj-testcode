/*
 * GpsHelper.java
 */
package net.sarangnamu.common.gps;

import net.sarangnamu.common.DLog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * @see http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/
 */
public class GpsHelper implements LocationListener {
    private static final String TAG = "GpsHelper";

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    private final Context mContext;
    private boolean mIsGpsEnabled     = false;
    private boolean mIsNetworkEnabled = false;
    private boolean mCanGetLocation   = false;
    private double mLatitude;
    private double mLongitude;
    private Location mLocation;
    private GpsListener mListener;

    protected LocationManager mLocationManager;

    public GpsHelper(Context context) {
        this.mContext = context;

        getLocation();
    }

    public Location getLocation() {
        try {
            mLocationManager  = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            mIsGpsEnabled     = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            mIsNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!mIsGpsEnabled && !mIsNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.mCanGetLocation = true;

                if (mIsNetworkEnabled) {
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (mLocationManager != null) {
                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (mLocation != null) {
                            mLatitude = mLocation.getLatitude();
                            mLongitude = mLocation.getLongitude();
                        }
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (mIsGpsEnabled) {
                    if (mLocation == null) {
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (mLocationManager != null) {
                            mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (mLocation != null) {
                                mLatitude = mLocation.getLatitude();
                                mLongitude = mLocation.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "getLocation", e);
        }

        return mLocation;
    }

    public void stopUsingGPS() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
        }
    }

    public boolean canGetLocation() {
        return this.mCanGetLocation;
    }

    public double getLatitude() {
        if (mLocation != null) {
            mLatitude = mLocation.getLatitude();
        }

        return mLatitude;
    }

    public double getLongitude() {
        if (mLocation != null) {
            mLongitude = mLocation.getLongitude();
        }

        return mLongitude;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // GpsListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public void setOnGpsListener(GpsListener l) {
        mListener = l;
    }

    public interface GpsListener {
        public void onLocationChanged(Location location);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // LocationListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onLocationChanged(Location location) {
        if (mListener != null) {
            mListener.onLocationChanged(location);
        }
    }

    @Override
    public void onProviderDisabled(String arg0) {
    }

    @Override
    public void onProviderEnabled(String arg0) {
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }
}
