package net.sarangnamu.baedal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import net.sarangnamu.baedal.config.ConfigBaedal;
import net.sarangnamu.baedal.network.NetworkBaedal;
import net.sarangnamu.utils.EventImageView;
import net.sarangnamu.utils.ImageRatingBar;

import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MarketList extends Activity implements OnClickListener, DialogInterface.OnClickListener {
	public static final String TITLE 	= "1";
	public static final String CATEGORY = "2";
	
	private static final int LOADED_MARKET = 1;
	private static final int LOADED_NEAR_MARKET = 2;
	
	private LinearLayout titleLayout, listTabLayout, listViewLayout;
	private Button nearMarket, franchiseMarket;
	private boolean tabNearMarket = true;
	private ImageView reposition, listLoading;
	private PullToRefreshListView listNear, listFranchise;
	private LinkedList<String> mListItems2;


	private String[] mStrings2 = {
            "Abbaye du Mont des Cats", "Abertam",
            "Abondance", "Ackawi", "Acorn", "Adelost", "Affidelice au Chablis",
            "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler", "Abbaye de Belloc"};
	
	private int category;
	private ArrayList<JSONObject> marketListData = new ArrayList<JSONObject>();
	private ArrayList<JSONObject> nearMarketListData = new ArrayList<JSONObject>();
		
	public Handler handler = new Handler() {
		@Override  
	    public void handleMessage(Message msg) {
			try {
		        switch(msg.what) {  
		        case LOADED_MARKET:
		        	Log.d("", "@@@@@@ set loaded market list");
					listLoading.setVisibility(ImageView.GONE);
					setMarketListData(true);
					MarketArrayAdapter adapter = new MarketArrayAdapter(
							MarketList.this, R.layout.market_list_item,
							marketListData);
					listNear.setAdapter(adapter);
		        	break;
		        	
		        case LOADED_NEAR_MARKET:
		        	break;
		        	
		        default:
		        	break;  
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }  
	};
	
	private void setMarketListData(boolean reset) throws Exception {
		if (reset) {
			marketListData.clear();
		}
		
		JSONArray array = ConfigBaedal.marketList.getJSONArray(ConfigBaedal.MARKETLIST_ALL); 
		for (int i=0; i<array.length(); ++i) {
			marketListData.add(array.getJSONObject(i));
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		
		try {
			Bundle bundle = getIntent().getExtras();		
			if (bundle == null) {
				return ;
			}
			
			int title = bundle.getInt(TITLE, -1);
			category = bundle.getInt(CATEGORY, -1);
			
			if (title == -1 || category == -1) {
				return ;
			}
			
			setContentView(R.layout.market_list);
			
			titleLayout 	= (LinearLayout) findViewById(R.id.listTopTitle);			
			listTabLayout 	= (LinearLayout) findViewById(R.id.listTabLayout);
			listViewLayout  = (LinearLayout) findViewById(R.id.listViewLayout);
			nearMarket 	 	= (Button) findViewById(R.id.btnNearMarket);
			franchiseMarket = (Button) findViewById(R.id.btnFranchise);
			reposition		= (ImageView) findViewById(R.id.btnReposition);
			listLoading 	= (ImageView) findViewById(R.id.list_loading);
			
			titleLayout.setBackgroundResource(title);
			nearMarket.setOnClickListener(this);
			franchiseMarket.setOnClickListener(this);
			reposition.setOnClickListener(this);
						
			listNear = new PullToRefreshListView(this);
			listFranchise = new PullToRefreshListView(this);
			listFranchise.setVisibility(View.GONE);
			listViewLayout.addView(listNear);
			listViewLayout.addView(listFranchise);
			
			listNear.setFadingEdgeLength(0);
			listNear.setCacheColorHint(0);
			
			getMarketList();
			listNear.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					JSONObject obj = marketListData.get(arg2 - 1);
					try {
						Intent intent = new Intent(MarketList.this, MarketDetail.class);
						intent.putExtra(MarketDetail.DATA, obj.toString());
						startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			listNear.setOnRefreshListener(new OnRefreshListener() {
	            @Override
	            public void onRefresh() {
	                // Do work to refresh the list here.
	                new GetDataTask().execute();
	            }
	        });
						
			listFranchise.setOnRefreshListener(new OnRefreshListener() {
	            @Override
	            public void onRefresh() {
	                // Do work to refresh the list here.
	                new GetDataTask2().execute();
	            }
	        });
			
			mListItems2 = new LinkedList<String>();
			mListItems2.addAll(Arrays.asList(mStrings2));
		
			ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
	                android.R.layout.simple_list_item_1, mListItems2);

			listFranchise.setAdapter(adapter2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class MarketArrayAdapter extends ArrayAdapter<JSONObject> {
		public MarketArrayAdapter(Context context, int resId, ArrayList<JSONObject> array) {
			super(context, resId, array);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.market_list_item, null);
            }
            
            try {
	            JSONObject obj = marketListData.get(position);	            
	            
	            if (obj != null) {
	            	// 리스트 내 이미지 처리는 비트맵 문제가 있을 수 있으니 일단 추후 처리..
	            	// <!>
	            	
	            	TextView title = (TextView) v.findViewById(R.id.marketlistTitle);
	            	title.setText(obj.getString(ConfigBaedal.MARKETLIST_TITLE));
	            	
	            	TextView distanceAddr = (TextView) v.findViewById(R.id.distanceAddr);
	            	String distanceAddress = null;
	            	distanceAddress  = obj.getString(ConfigBaedal.MARKETLIST_DISTANCE);
	            	distanceAddress += "Km ";
	            	distanceAddress += obj.getString(ConfigBaedal.MARKETLIST_ADDR);
	            	distanceAddr.setText(distanceAddress);

	            	LinearLayout review = (LinearLayout) v.findViewById(R.id.review);
	            	ImageRatingBar ratingbar = new ImageRatingBar(review);
	            	ratingbar.setImage(R.drawable.small_star_full, R.drawable.small_star_half, R.drawable.small_star_empty);
	            	ratingbar.draw(getContext(), obj.getDouble(ConfigBaedal.MARKETLIST_GRADEAVG));
	            		            	
	            	TextView countReviewer = (TextView) v.findViewById(R.id.countReviewer);
	            	String reviewerCount = obj.getString(ConfigBaedal.MARKETLIST_REVIEWER_COUNT);	            	
	            	String people = getContext().getResources().getString(R.string.mk_people);
	            	countReviewer.setText(" " + reviewerCount + people);
	            	
	            	ImageView menu = (ImageView) v.findViewById(R.id.marketlistMenu);
	            	if (obj.getInt(ConfigBaedal.MARKETLIST_MENU) == 1) {	            		
	            		menu.setImageResource(R.drawable.mk_menu_disable);
	            	} else {
	            		menu.setImageResource(R.drawable.mk_menu_enable);
	            	}
	            	
	            	ImageView deliveryPossible = (ImageView) v.findViewById(R.id.marketlistDelivery);
	            	if (obj.getInt(ConfigBaedal.MARKETLIST_DELIVERY_YN) == 1) {	            		
	            		deliveryPossible.setImageResource(R.drawable.mk_delivery_enable);
	            	} else {
	            		deliveryPossible.setImageResource(R.drawable.mk_delivery_disable);
	            	}
	            	
	            	ImageView card = (ImageView) v.findViewById(R.id.marketlistCard);
	            	if (obj.getInt(ConfigBaedal.MARKETLIST_CARD) == 1) {
	            		card.setImageResource(R.drawable.mk_card_enable);
	            	} else {
	            		card.setImageResource(R.drawable.mk_card_disable);
	            	}
	            	
	            	ImageView coupon = (ImageView) v.findViewById(R.id.marketlistCoupon);
	            	if (obj.getInt(ConfigBaedal.MARKETLIST_COUPON) == 1) {	            		
	            		coupon.setImageResource(R.drawable.mk_coupon_enable);
	            	} else {
	            		coupon.setImageResource(R.drawable.mk_coupon_disable);
	            	}
	            	
	            	ImageView freecall = (ImageView) v.findViewById(R.id.marketlistFreeCall);
	            	if (obj.getString(ConfigBaedal.MARKETLIST_FREE_PHONENUM).length() != 0) {
	            		freecall.setImageResource(R.drawable.mk_freecall_enable);
	            	} else {
	            		freecall.setImageResource(R.drawable.mk_freecall_disable);
	            	}

	            	ImageView btnCall = (ImageView) v.findViewById(R.id.btnCall);
	            	btnCall.setOnClickListener(new MarketOnClickListener(obj.getString(ConfigBaedal.MARKETLIST_PHONENUM)));

	            	LinearLayout marketlistEtcData = (LinearLayout) v.findViewById(R.id.marketlistEtcData);	            	
	            	if (obj.getString(ConfigBaedal.MARKETLIST_LOGOPATH).length() != 0) {
	            		marketlistEtcData.setVisibility(View.VISIBLE);
	            		     	            		
		            	String beginTime = obj.getString(ConfigBaedal.MARKETLIST_DELIVERY_BEGINTIME);
		            	String endTime   = obj.getString(ConfigBaedal.MARKETLIST_DELIVERY_ENDTIME);

		            	TextView deliveryPossibleTime = (TextView) v.findViewById(R.id.marketlinePossibleTime);		            	
		            	if (beginTime.length() == 0 && endTime.length() == 0) {
		            		deliveryPossibleTime.setText(String.format(getContext().getResources().getString(R.string.mk_delivery_possible_time), beginTime, endTime));
		            	} else {
		            		deliveryPossibleTime.setText(R.string.mk_delivery_possible_time_error);
		            	}

		            	TextView deliveryPossibleArea = (TextView) v.findViewById(R.id.marketlinePossibleArea);
		            	deliveryPossibleArea.setText(obj.getString(ConfigBaedal.MARKETLIST_DELIVERY_AREA));
	            	} else {
	            		marketlistEtcData.setVisibility(View.GONE);
	            	}
	            } else {
	            	Log.d("", "@@@@@@@@@@@ null data @@@@@@@@@@@");
	            }
            } catch (Exception e) {
            	e.printStackTrace();
            }
            
            return v;
		}
	}
	
	private class MarketOnClickListener implements OnClickListener {
		private final String number;
		
		public MarketOnClickListener(final String number) {
			this.number = number;
		}

		@Override
		public void onClick(View v) {
			startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number)));
		}
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, ArrayList<JSONObject>> {
        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            try {
            	NetworkBaedal.getMarketData(category, 1);
            	setMarketListData(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            return marketListData;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> result) {
        	// Call onRefreshComplete when the list has been refreshed.
            listNear.onRefreshComplete();
            super.onPostExecute(result);
        }
    }

	private class GetDataTask2 extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                ;
            }
            return mStrings2;
        }

        @Override
        protected void onPostExecute(String[] result) {
            mListItems2.addFirst("Added after refresh...");

            // Call onRefreshComplete when the list has been refreshed.
            listFranchise.onRefreshComplete();

            super.onPostExecute(result);
        }
    }
	
	private void sendLocalMessage(int what) {
		Message msg = handler.obtainMessage();
		msg.what = what;
		handler.sendMessage(msg);
	}
	
	private void getMarketList() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					NetworkBaedal.getMarketData(category, 1);
					sendLocalMessage(LOADED_MARKET);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(MarketList.this, e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
		thread.start();
	}
	
	@Override
	public void onClick(View view) {		
		switch (view.getId()) {
		case R.id.btnNearMarket:
			if (tabNearMarket == true) {
				return ;
			}

			listTabLayout.setBackgroundResource(R.drawable.tab_near_market);
			listNear.setVisibility(View.VISIBLE);
			listFranchise.setVisibility(View.GONE);
			tabNearMarket = true;
			break;
			
		case R.id.btnFranchise:
			if (tabNearMarket == false) {
				return ;
			}

			listTabLayout.setBackgroundResource(R.drawable.tab_franchise);
			listFranchise.setVisibility(View.VISIBLE);
			listNear.setVisibility(View.GONE);
			tabNearMarket = false;
			break;
			
		case R.id.btnReposition:
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this); 
			alertDialog.setTitle(R.string.title_dialog_alert);
			alertDialog.setMessage(R.string.have_choice);
			alertDialog.setPositiveButton(R.string.using_map, this);
			alertDialog.setNegativeButton(R.string.using_address, this);
			alertDialog.show();			
			break;
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		Intent intent = null;
		switch (which) {
		case -1:		// Positive
			intent = new Intent(this, TabRepositionByGoogleMap.class);
			break;
		case -2:		// Negative
			intent = new Intent(this, TabRepositionByAddressKeyword.class);
			break;
		}
		
		if (intent != null) {
			startActivity(intent);
		}
	}
}


