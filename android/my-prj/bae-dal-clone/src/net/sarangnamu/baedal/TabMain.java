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

package net.sarangnamu.baedal;

import org.json.JSONArray;
import net.sarangnamu.baedal.config.ConfigBaedal;
import net.sarangnamu.baedal.ui.BeadalMainImageButton;
import net.sarangnamu.utils.DrawableManager;
import net.sarangnamu.utils.EventImageView;
import net.sarangnamu.utils.EventImageButtonList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class TabMain extends Activity implements OnClickListener {
	private TextView mainNoticeTxt;
	private TextView mainEventTxt;
	private TextView copyrightTxt;
	private LinearLayout bannerLayout = null;
	private EventImageButtonList btnlist = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabmain);

		try {
			//
			// main menu
			//
			btnlist = new EventImageButtonList(this);
			btnlist.setLayout(R.id.mainMenuLayout);

			LinearLayout layout1 = new LinearLayout(this);
			layout1.addView(new BeadalMainImageButton(this, R.drawable.main_menu1, R.drawable.title_chiken, 1));
			layout1.addView(new BeadalMainImageButton(this, R.drawable.main_menu2, R.drawable.title_chinse_restaurant, 2));
			btnlist.addView(layout1);

			LinearLayout layout2 = new LinearLayout(this);
			layout2.addView(new BeadalMainImageButton(this, R.drawable.main_menu3, R.drawable.title_pizza, 3));
			layout2.addView(new BeadalMainImageButton(this, R.drawable.main_menu4, R.drawable.title_pig_foot, 4));
			btnlist.addView(layout2);

			LinearLayout layout3 = new LinearLayout(this);
			layout3.addView(new BeadalMainImageButton(this, R.drawable.main_menu5, R.drawable.title_late_snack, 5));
			layout3.addView(new BeadalMainImageButton(this, R.drawable.main_menu6, R.drawable.title_stream_dish, 6));
			btnlist.addView(layout3);

			LinearLayout layout4 = new LinearLayout(this);
			layout4.addView(new BeadalMainImageButton(this, R.drawable.main_menu7, R.drawable.title_fast_food, 7));
			layout4.addView(new BeadalMainImageButton(this, R.drawable.main_menu8, R.drawable.title_korean_food, 8));
			layout4.addView(new BeadalMainImageButton(this, R.drawable.main_menu9, R.drawable.title_box_lunch, 9));
			btnlist.addView(layout4);

			LinearLayout layout5 = new LinearLayout(this);
			layout5.addView(new BeadalMainImageButton(this, R.drawable.main_menu10, R.drawable.title_poke_cuttlet, 10));
			layout5.addView(new BeadalMainImageButton(this, R.drawable.main_menu11, R.drawable.title_etc, 11));
			layout5.addView(new BeadalMainImageButton(this, R.drawable.main_menu12, R.drawable.title_all_menu, 12));
			btnlist.addView(layout5);

			//
			// main banner
			//
			bannerLayout  = (LinearLayout) findViewById(R.id.bannerLayout);

			JSONArray bannerList = ConfigBaedal.mainContent.getJSONArray(ConfigBaedal.BANNER_LIST);
			for (int i=0; i<bannerList.length(); ++i) {
				String imageUri = bannerList.getJSONObject(i).getString(ConfigBaedal.BANNER_IMG);

				ImageView view = new ImageView(this);
				view.setImageResource(R.drawable.main_line);

				EventImageView eventView = new EventImageView(this, DrawableManager.getInstance().fetchDrawable(imageUri), 110);
				eventView.setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + bannerList.getJSONObject(i).getString(ConfigBaedal.BANNER_URI))));

				bannerLayout.addView(view);
				bannerLayout.addView(eventView);
			}

			//
			// news & events & copyright
			//
			mainNoticeTxt = (TextView) findViewById(R.id.mainNoticeTxt);
			mainEventTxt  = (TextView) findViewById(R.id.mainEventTxt);
			copyrightTxt  = (TextView) findViewById(R.id.mainCopyright);
			
			mainNoticeTxt.setText(ConfigBaedal.mainContent.getString(ConfigBaedal.S_NOTICE));
			mainEventTxt.setText(ConfigBaedal.mainContent.getString(ConfigBaedal.S_EVENT));
			
			mainNoticeTxt.setOnClickListener(this);
			mainEventTxt.setOnClickListener(this);
			copyrightTxt.setOnClickListener(this);
			
			if (ConfigBaedal.nmea.getLocation() == null) {
				Toast.makeText(this, "GPS 에러 관련 문제 대응은 나중에 추가할 예정", Toast.LENGTH_LONG).show();
				ConfigBaedal.nmea.setDefaultLocation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		String uri = null;
		try {
			switch(v.getId()) {
			case R.id.mainNoticeTxt:				
				uri = ConfigBaedal.mainContent.getString(ConfigBaedal.S_NOTICE_URI);				
				break;
			case R.id.mainEventTxt:
				uri = ConfigBaedal.mainContent.getString(ConfigBaedal.S_EVENT_URI);
				break;
			case R.id.mainCopyright:
				uri = ConfigBaedal.mainContent.getString(ConfigBaedal.MY_TWITTER);
				break;
			default:
				break;
			}
		
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
