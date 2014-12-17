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

    private final Context context;
    private boolean isGpsEnabled     = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation   = false;
    private double latitude;
    private double longitude;
    private Location location;
    private GpsListener listener;

    protected LocationManager locationManager;

    public GpsHelper(Context context) {
        this.context = context;

        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager  = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            isGpsEnabled     = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGpsEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGpsEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            DLog.e(TAG, "getLocation", e);
        }

        return location;
    }

    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // GpsListener
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public void setOnGpsListener(GpsListener l) {
        listener = l;
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
        if (listener != null) {
            listener.onLocationChanged(location);
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
