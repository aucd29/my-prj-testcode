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

package net.sarangnamu.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class NMEALoader implements LocationListener {
	private LocationManager manager;
	private Location location = null;

	public NMEALoader(Context context) {
		manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

		if (manager == null) {
			Log.d("", "@@@@@@@@@@@@@@@@@@@@ manager == null @@@@@@@@@@@@@@@@@@@@@@@");
			return ;
		}

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE); 	// ��Ȯ��
		criteria.setPowerRequirement(Criteria.POWER_LOW); 	// ���� �Ҹ���
		criteria.setAltitudeRequired(false); 				// ��, ���� ���� ��� ������ ���� �ϴµ� �մϴ�..;;
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);					// �ӵ�
		criteria.setCostAllowed(true);						// ��ġ ������ ��� ���µ� ���� ������ ���

		String bestProvider = manager.getBestProvider(criteria, true); //���� ���� �ϳ��� ������
		Log.d("", "@@@@@@@@@@@@@@@@@@@@@@ best provider: " + bestProvider);

		manager.requestLocationUpdates(bestProvider, 1000L, 10.0f, this);
	}

	public void stopLocation() {
		manager.removeUpdates(this);
	}

	public Location getLocation() {
		return location;
	}
	
	public void setDefaultLocation() {
		location = new Location("gps");
		location.setLatitude(37.656713);
		location.setLongitude(126.76609);
	}

	/**
	 * NMEA ���� �ùٸ��� Ȯ�� �Ѵ�.
	 * 
	 * @throws InterruptedException
	 */
	public void checkNMEA() throws InterruptedException {
		int count = 0;
		while(true) {
			if (location == null) {
				Thread.sleep(500);
				if (++count > 16) {
					Log.d("", "@@@ NMEA error");
					break;
				}				
			} else {
				Log.d("@NMEA@", "Latitude: " + Double.toString(location.getLatitude()) + 
							  ", Longitude: " + Double.toString(location.getLongitude()));
				break;
			}
		}
	}

	@Override
	public void onLocationChanged(Location arg0) {
		location = arg0;
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}
}
