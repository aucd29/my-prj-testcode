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
import net.sarangnamu.utils.EventTextView;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TabSearch extends Activity {
	private EditText edit;
	private ImageView searchBtn;
	private LinearLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabsearch);
		
		edit = (EditText) findViewById(R.id.searchKeyword);
		searchBtn = (ImageView) findViewById(R.id.searchButton);
		
		try {
			JSONArray array = ConfigBaedal.mainContent.getJSONArray(ConfigBaedal.SEARCH_KEYWORD);
			int max = array.length();
			edit.setText(array.getJSONObject(0).getString(ConfigBaedal.KEYWORD));
			
			layout = (LinearLayout) findViewById(R.id.searchMapLayout);			
			LinearLayout baseLayout = layout;
			
			int cnt = 0;
			for (int i=2; i<max; ++i) {
				if (cnt++ % 4 == 0) {
					LinearLayout subLayout = new LinearLayout(this);
					subLayout.setOrientation(LinearLayout.HORIZONTAL);
					layout.addView(subLayout);
					baseLayout = subLayout;
				}
				
				String keyword = array.getJSONObject(i).getString(ConfigBaedal.KEYWORD);
				
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConfigBaedal.GOOGLE_SEARCH_ENGINE + keyword));
				EventTextView searchKeyword = new EventTextView(this, keyword, intent);
				searchKeyword.setPadding(0, 0, 10, 5);
				baseLayout.addView(searchKeyword);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("", "search keyword: " + edit.getText());
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConfigBaedal.GOOGLE_SEARCH_ENGINE + edit.getText()));
//				intent.putExtra("keyword", edit.getText());
				startActivity(intent);
			}
		});
	}
}
