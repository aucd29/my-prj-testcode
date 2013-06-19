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
 * - SAMPLE CODE
 * @code
 * try {
 *		tabCtl = new TabController(this, getTabHost());
 *		tabCtl.addImageList(R.drawable.menu1_on, R.drawable.menu1_off);
 *		tabCtl.addImageList(R.drawable.menu2_on, R.drawable.menu2_off);
 *		tabCtl.addImageList(R.drawable.menu3_on, R.drawable.menu3_off);
 *		tabCtl.addImageList(R.drawable.menu4_on, R.drawable.menu4_off);
 *		tabCtl.addImageList(R.drawable.menu5_on, R.drawable.menu5_off);
 *
 *		tabCtl.addTab(new Intent(this, TabMain.class));
 *		tabCtl.addTab(new Intent(this, TabFavorite.class));
 *		tabCtl.addTab(new Intent(this, TabRegistration.class));
 *		tabCtl.addTab(new Intent(this, TabSearch.class));
 *		tabCtl.addTab(new Intent(this, TabOthers.class));
 *
 *		tabCtl.setCurrentTab(0);
 *
 *		tabCtl.setListener(new OnTabChangeListener() {
 *			@Override
 *			public void onTabChanged(String tabId) {
 *				int pos = Integer.parseInt(tabId.substring(3));
 *				tabCtl.changeBackground(pos);
 *			}
 *		});
 *
 * } catch (Exception e) {
 *	   	e.printStackTrace();
 * }
 * @endcode
 *
 */

package net.sarangnamu.utils;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;

public class TabController {
	private final Context context;
	private final TabWidget tabWidget;
	private final TabHost tabHost;
	private final ArrayList<CustomTabView> customViews = new ArrayList<CustomTabView>();

	private final ArrayList<Integer> normalList = new ArrayList<Integer>();
	private final ArrayList<Integer> overList = new ArrayList<Integer>();

	private int count = 0;

	public TabController(Context context, TabHost tabHost) {
		this.context = context;

		tabWidget = new TabWidget(context);
		tabWidget.setId(android.R.id.tabs);

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        tabWidget.setLayoutParams(lp);

        this.tabHost = tabHost;
	}

	/**
	 * 탭 컨트롤에 적용할 이미지를 리스트에 넣는다.
	 *  
	 * @param normal
	 * @param over
	 */
	public void addImageList(final int normal, final int over) throws Exception {
		normalList.add(normal);
		overList.add(over);
	}

	/**
	 * 탭을 추가한다. 
	 * 
	 * @param intent 
	 */
	public void addTab(final Intent intent) throws Exception {
		if (count > normalList.size()) {
			throw new ArrayIndexOutOfBoundsException();
		}

		CustomTabView view = new CustomTabView(this.context, normalList.get(count));
		customViews.add(view);

		String tabName = "tab" + Integer.toString(count);
		TabHost.TabSpec tab = tabHost.newTabSpec(tabName);
        tabHost.addTab(tab.setIndicator(view).setContent(intent));
        ++count;
	}

	/**
	 * 탭에 위치를 설정한다. 
	 */
	public void setCurrentTab(final int tabId) {
		customViews.get(tabId).setBackgroundResource(overList.get(tabId));
		tabHost.setCurrentTab(tabId);
	}

	/**
	 * 선택된 탭에 정보를 이용 이미지를 변경한다.
	 */
	public void changeBackground(final int tabId) {
		for (int i=0; i<count; ++i) {
			if (tabId == i) {
				customViews.get(i).setBackgroundResource(overList.get(i));
			} else {
				customViews.get(i).setBackgroundResource(normalList.get(i));
			}
		}
	}

	/**
	 * 리스너 설정
	 */
	public void setListener(TabHost.OnTabChangeListener listener) {
		tabHost.setOnTabChangedListener(listener);
	}

	/**
	 * 
	 */
	class CustomTabView extends LinearLayout {
		int res;

		public CustomTabView(Context context, int res) {
			super(context);
			this.res = res;
			setBackgroundResource(res);
			setOrientation(1);
		}
	}
}
