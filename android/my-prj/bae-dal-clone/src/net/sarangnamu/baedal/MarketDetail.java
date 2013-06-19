package net.sarangnamu.baedal;

import net.sarangnamu.baedal.config.ConfigBaedal;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MarketDetail extends Activity {
	public static final String DATA = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {			
			Bundle bundle = getIntent().getExtras();		
			if (bundle == null) {
				new Exception("bundle error");
			}

			JSONObject json = new JSONObject(bundle.getString(DATA));
			setContentView(R.layout.market_detail);
			
			TextView title = (TextView) findViewById(R.id.detailTitle);
			TextView number = (TextView) findViewById(R.id.detailNumber);
			TextView address = (TextView) findViewById(R.id.detailAddress);
			
			title.setText(json.getString(ConfigBaedal.MARKETLIST_TITLE));
			number.setText(json.getString(ConfigBaedal.MARKETLIST_PHONENUM));
			address.setText(json.getString(ConfigBaedal.MARKETLIST_ADDR));
			
			ImageView btnCall = (ImageView) findViewById(R.id.detailCall);			
			// set click event listener
			
			ImageView tabMarketInfo = (ImageView) findViewById(R.id.detailMarketInfo);
			ImageView tabReview = (ImageView) findViewById(R.id.detailReview);
			// set click event listener
			
			ImageButton btnShowMenu = (ImageButton) findViewById(R.id.detailShowMenu);
			// set click event listener
			
			
			// vote
			
			
			// Imagebutton
			ImageButton btnShowMap = (ImageButton) findViewById(R.id.detailShowMap);
			ImageButton btnFavorite = (ImageButton) findViewById(R.id.detailFavorite);
			ImageButton btnReqModify = (ImageButton) findViewById(R.id.detailReqModify);
			// set click event listener
			
			
			TextView possibleTime = (TextView) findViewById(R.id.detailPossibleTime);
			TextView possibleCard = (TextView) findViewById(R.id.detailPossibleCard);
			TextView favoritedUser = (TextView) findViewById(R.id.detailFavoriteUser);
			TextView calledUser = (TextView) findViewById(R.id.detailCalledUser);
			TextView possibleArea = (TextView) findViewById(R.id.detailPossibleArea);
			
			String beginTime = json.getString(ConfigBaedal.MARKETLIST_DELIVERY_BEGINTIME);
        	String endTime   = json.getString(ConfigBaedal.MARKETLIST_DELIVERY_ENDTIME);
        	if (beginTime.length() == 0 && endTime.length() == 0) {
        		possibleTime.setText(String.format(getResources().getString(R.string.detail_possible_time), beginTime, endTime));
        	} else {
        		possibleTime.setText(R.string.detail_possible_time_unknown);
        	}
        	
        	String szPossibleCard = getResources().getString(R.string.detail_possible_card);
        	szPossibleCard += " ";
			if (json.getInt(ConfigBaedal.MARKETLIST_CARD) == 1) {
				szPossibleCard += getResources().getString(R.string.detail_possible);
        		possibleCard.setText(szPossibleCard);
        	} else {
        		szPossibleCard += getResources().getString(R.string.detail_impossible);
        		possibleCard.setText(szPossibleCard);
        	}
			
			String szFavoritedUser = getResources().getString(R.string.detail_favorite_user);
			favoritedUser.setText(szFavoritedUser);
			
			String szCallUser = getResources().getString(R.string.detail_count_called);
			szCallUser += " " + json.getString(ConfigBaedal.MARKETLIST_ORDER);
			calledUser.setText(szCallUser);
			
			String szPossibleArea = getResources().getString(R.string.detail_possible_area);
			szPossibleArea += json.getString(ConfigBaedal.MARKETLIST_DELIVERY_AREA);
			possibleArea.setText(szPossibleArea);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}


