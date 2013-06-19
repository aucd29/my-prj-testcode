package net.sarangnamu.baedal;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

public class TabRepositionByGoogleMap extends MapActivity {
	private MapView view;
	private MapController controller;
	private GeoPoint point;
	private double currentLat = 37.656713;
	private double currentLong = 126.76609;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabreposition_googlemap);
		
		view = (MapView) findViewById(R.id.mapview);
		controller = view.getController();
		
		setMapPosition();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	private void setMapPosition() {
		point = new GeoPoint((int)(currentLat * 1E6), (int)(currentLong * 1E6));
		
		controller.setZoom(19);
		controller.setCenter(point);
		
		view.setBuiltInZoomControls(true);
		view.setClickable(true);
		view.setStreetView(true);
		view.invalidate();
		
		replaceMarker((int)(currentLat * 1E6), (int)(currentLong * 1E6));
	}
	
	private void replaceMarker(int latitude, int longitude) {
		Drawable marker = getResources().getDrawable(R.drawable.reposition_marker);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
		
		view.getOverlays().add(new InterestingLocations(marker, latitude, longitude));
	}
	
	private void CenterLocation(GeoPoint centerGeoPoint) {
		controller.animateTo(centerGeoPoint);
		
		replaceMarker(centerGeoPoint.getLatitudeE6(), centerGeoPoint.getLongitudeE6());
		
		currentLat  = (double)centerGeoPoint.getLatitudeE6()  / 1000000;
		currentLong = (double)centerGeoPoint.getLongitudeE6() / 1000000;
	};
	
	class InterestingLocations extends ItemizedOverlay<OverlayItem> {
		private List<OverlayItem> locations = new ArrayList<OverlayItem>();
		private Drawable marker;
		private OverlayItem myOverlayItem;
			
		public InterestingLocations(Drawable defaultMarker, int LatitudeE6, int LongitudeE6) {
			super(defaultMarker);
			
			this.marker = defaultMarker;
			// create locations of interest
			GeoPoint myPlace = new GeoPoint(LatitudeE6,LongitudeE6);
			myOverlayItem = new OverlayItem(myPlace, "My Place", "My Place");
			locations.add(myOverlayItem);
			
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return locations.get(i);
		}

		@Override
		public int size() {
			return locations.size();
		}
		
		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);
			boundCenterBottom(marker);
		}
		
		@Override
		public boolean onTap(GeoPoint p, MapView mapView) {
			mapView.getOverlays().remove(0);
			CenterLocation(p);
			
			return true;
		}
	}
}
