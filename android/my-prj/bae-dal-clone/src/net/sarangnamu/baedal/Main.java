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

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost.OnTabChangeListener;
import net.sarangnamu.utils.TabController;

public class Main extends TabActivity {	
	private TabController tabCtl;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        try {
	        tabCtl = new TabController(this, getTabHost());
	        tabCtl.addImageList(R.drawable.menu1_on, R.drawable.menu1_off);
	        tabCtl.addImageList(R.drawable.menu2_on, R.drawable.menu2_off);
	        tabCtl.addImageList(R.drawable.menu3_on, R.drawable.menu3_off);
	        tabCtl.addImageList(R.drawable.menu4_on, R.drawable.menu4_off);
	        tabCtl.addImageList(R.drawable.menu5_on, R.drawable.menu5_off);
	        
	        tabCtl.addTab(new Intent(this, TabMain.class));
	        tabCtl.addTab(new Intent(this, TabFavorite.class));
	        tabCtl.addTab(new Intent(this, TabRegistration.class));
	        tabCtl.addTab(new Intent(this, TabSearch.class));
	        tabCtl.addTab(new Intent(this, TabOthers.class));
        
	        tabCtl.setCurrentTab(0);
	        
	        tabCtl.setListener(new OnTabChangeListener() {
				@Override
				public void onTabChanged(String tabId) {
					int pos = Integer.parseInt(tabId.substring(3));				
					tabCtl.changeBackground(pos);
				}
	        });
	        
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
}