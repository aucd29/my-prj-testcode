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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TabFavorite extends Activity implements OnClickListener {
	private LinearLayout title, tab;
	private Button btnFavorite, btnCallHistory;
	private boolean tabFavorite = true;
	private ImageView reposition;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.market_list);	// 
		
		title			= (LinearLayout) findViewById(R.id.listTopTitle);
		tab				= (LinearLayout) findViewById(R.id.listTabLayout);
		reposition 		= (ImageView) findViewById(R.id.btnReposition);
		btnFavorite 	= (Button) findViewById(R.id.btnNearMarket);
		btnCallHistory 	= (Button) findViewById(R.id.btnFranchise);
		
		reposition.setVisibility(ImageView.GONE);
		
		btnFavorite.setOnClickListener(this);
		btnCallHistory.setOnClickListener(this);
		
		title.setBackgroundResource(R.drawable.title_favorite);
		tab.setBackgroundResource(R.drawable.tab_favorite);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnNearMarket:
			if (tabFavorite == true) {
				return ;
			}
			
			tab.setBackgroundResource(R.drawable.tab_favorite);
			tabFavorite = true;
			break;
		case R.id.btnFranchise:
			if (tabFavorite == false) {
				return ;
			}
			
			tab.setBackgroundResource(R.drawable.tab_call_history);
			tabFavorite = false;
			break;
		}
	}
}
