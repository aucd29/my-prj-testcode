/*
 * Copyright (C) 2011 Aquilegia, South Korea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.teleal.cling.android.browser;

import org.teleal.cling.android.AndroidUpnpService;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.types.UDAServiceId;
import org.teleal.cling.model.types.UDN;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Aquilegia
 */
public class ContentActivity extends ListActivity {

    // private static final Logger log = Logger.getLogger(BrowseActivity.class.getName());
	
    public static String DEVICE_UUID = "device_uuid";

    private ContentBrowserAdapter contentAdapter;

	private ListView listview;
	
    private AndroidUpnpService upnpService;
    
    private String uuid;
    
    private Device device;
    
    private ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            upnpService = (AndroidUpnpService) service;
            
            device = upnpService.getRegistry().getDevice(UDN.valueOf(uuid), true);
            
            createContentAdapter() ;
        }

        public void onServiceDisconnected(ComponentName className) {
            upnpService = null;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		uuid = getIntent().getStringExtra(DEVICE_UUID);
		
        getApplicationContext().bindService(
                new Intent(this, BrowserUpnpService.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE
        );
    }
    
    public void createContentAdapter() {
    	contentAdapter = new ContentBrowserAdapter(this, upnpService, 
    			device.findService(new UDAServiceId("ContentDirectory")));
    	
    	setListAdapter(contentAdapter);
    	listview = getListView();
    	listview.setOnItemClickListener(contentAdapter);
    }
      
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                break;
            case 1:
                break;
        }
        return false;
    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(contentAdapter.onKey(null, keyCode, event))
			return true;
		else
			return super.onKeyDown(keyCode, event);
	}

    protected void searchNetwork() {
        if (upnpService == null) return;
        Toast.makeText(this, R.string.searching_lan, Toast.LENGTH_SHORT).show();
        upnpService.getRegistry().removeAllRemoteDevices();
        upnpService.getControlPoint().search();
    }
}
