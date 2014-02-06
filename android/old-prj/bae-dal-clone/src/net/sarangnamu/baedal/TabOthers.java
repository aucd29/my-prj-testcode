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

import net.sarangnamu.baedal.config.ConfigBaedal;
import net.sarangnamu.utils.EventImageButtonList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class TabOthers extends Activity {
	private EventImageButtonList btnlist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.tabothers);		
		
		btnlist = new EventImageButtonList(this);
		btnlist.setLayout(R.id.othersLayout);
		
		btnlist.addImageView(R.drawable.etc_title);
		
		btnlist.addButton(R.drawable.others_menu01, null);
		btnlist.addSeperator(R.drawable.main_line);
		
		// 공지
		btnlist.addButtonWithBrowser(R.drawable.others_menu02, ConfigBaedal.NOTICE_URI);

		// 이벤트
		btnlist.addButtonWithBrowser(R.drawable.others_menu03, ConfigBaedal.EVENT_URI);
		
		// 위치
		btnlist.addButton(R.drawable.others_menu04, null);
		
		// 올레 콜센터
		btnlist.addButton(R.drawable.others_menu05, null);
		
		// 트위터
		btnlist.addButton(R.drawable.others_menu06, new Intent(Intent.ACTION_VIEW, Uri.parse(ConfigBaedal.TWITTER_URI)));
		
		// 소개
		btnlist.addButton(R.drawable.others_menu07, null);
		
		// 제휴
		btnlist.addButton(R.drawable.others_menu08, null);
		
		// 메일
		btnlist.addButton(R.drawable.others_menu09, null);
		
		// 추천
		btnlist.addButton(R.drawable.others_menu10, null);
	}
}
