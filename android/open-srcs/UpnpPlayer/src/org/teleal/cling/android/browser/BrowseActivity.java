/*
 * Copyright (C) 2010 Teleal GmbH, Switzerland, 2011 Aquilegia, South Korea
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

import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.teleal.cling.android.AndroidUpnpService;
import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.model.action.ActionArgumentValue;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.meta.Action;
import org.teleal.cling.model.meta.Device;
import org.teleal.cling.model.meta.DeviceIdentity;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.UDAServiceId;
import org.teleal.cling.registry.DefaultRegistryListener;
import org.teleal.cling.registry.Registry;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Christian Bauer, Aquilegia
 */
public class BrowseActivity extends ListActivity {

    // private static final Logger log = Logger.getLogger(BrowseActivity.class.getName());
	
    private ArrayAdapter<DeviceDisplay> deviceListAdapter;
    private ContentBrowserAdapter contentAdapter;

	private ListView listview;

    private BrowseRegistryListener registryListener = new BrowseRegistryListener();

    private AndroidUpnpService upnpService;

    private ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            upnpService = (AndroidUpnpService) service;

            // Refresh the list with all known devices
            deviceListAdapter.clear();
            for (Device device : upnpService.getRegistry().getDevices()) {
                registryListener.deviceAdded(device);
            }

            // Getting ready for future device advertisements
            upnpService.getRegistry().addListener(registryListener);
        }

        public void onServiceDisconnected(ComponentName className) {
            upnpService = null;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deviceListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        listview = getListView();
        switchToDeviceList();        

        getApplicationContext().bindService(
                new Intent(this, BrowserUpnpService.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE
        );
    }
    
    public void switchToContentList(DeviceIdentity identity) {
    	Intent intent = new Intent(this, ContentActivity.class);
    	intent.putExtra (ContentActivity.DEVICE_UUID, identity.getUdn().toString());
		startActivity(intent);
    }
    
    public void switchToDeviceList() {
		setListAdapter(deviceListAdapter);
		
		/* Executes when the user (long) clicks on a device:
         */
        listview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        DeviceDisplay clickedDisplay = deviceListAdapter.getItem(position);
                        if (clickedDisplay != null) {
                            //... clickedDisplay.getDevice();
                        	Service service = clickedDisplay.getDevice().findService(new UDAServiceId("SwitchPower"));
                        	if (service != null) {
                        		Action getStatusAction = service.getAction("GetTarget");
                        		ActionInvocation getStatusInvocation = new ActionInvocation(getStatusAction);
                            
                        		new ActionCallback.Default(getStatusInvocation, upnpService.getControlPoint()).run();
                        		
                        		boolean value = ((Boolean) getStatusInvocation.getOutput("RetTargetValue").getValue()).booleanValue();
                        		
                        		Action setTargetAction = service.getAction("SetTarget");
                        		ActionInvocation setTargetInvocation = new ActionInvocation(setTargetAction);
                        		setTargetInvocation.setInput("NewTargetValue", !value);
                        		
                        		ActionCallback setTargetCallback = new ActionCallback(setTargetInvocation) {
                        			
                        			@Override
                        			public void success(ActionInvocation invocation) {
                        				ActionArgumentValue[] output = invocation.getOutput();
                        				//assertEquals(output.length, 0);
                        			}
                        			
                        			@Override
                        			public void failure(ActionInvocation invocation,
                        					UpnpResponse operation,
                        					String defaultMsg) {
                        				System.err.println(defaultMsg);
                        			}
                        		};
                        		
                        		upnpService.getControlPoint().execute(setTargetCallback);
                        	}
                        	
                        	Service service2 = clickedDisplay.getDevice().findService(new UDAServiceId("ContentDirectory"));
                        	if (service2 != null) {
                        		DeviceIdentity identity = clickedDisplay.getDevice().getIdentity();
                        		switchToContentList(identity);
                        	}
                        }                        
                    }
                }
        );
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (upnpService != null) {
            upnpService.getRegistry().removeListener(registryListener);
        }
        getApplicationContext().unbindService(serviceConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, R.string.search_lan).setIcon(android.R.drawable.ic_menu_search);
        menu.add(0, 1, 0, R.string.toggle_debug_logging).setIcon(android.R.drawable.ic_menu_info_details);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                searchNetwork();
                break;
            case 1:
                Logger logger = Logger.getLogger("org.teleal.cling");
                if (logger.getLevel().equals(Level.FINEST)) {
                    Toast.makeText(this, R.string.disabling_debug_logging, Toast.LENGTH_SHORT).show();
                    logger.setLevel(Level.INFO);
                } else {
                    Toast.makeText(this, R.string.enabling_debug_logging, Toast.LENGTH_SHORT).show();
                    logger.setLevel(Level.FINEST);
                }
                break;
        }
        return false;
    }

    protected void searchNetwork() {
        if (upnpService == null) return;
        Toast.makeText(this, R.string.searching_lan, Toast.LENGTH_SHORT).show();
        upnpService.getRegistry().removeAllRemoteDevices();
        upnpService.getControlPoint().search();
    }

    protected class BrowseRegistryListener extends DefaultRegistryListener {

        /* Discovery performance optimization for very slow Android devices! */
        @Override
        public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
            deviceAdded(device);
        }

        @Override
        public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device, final Exception ex) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(
                            BrowseActivity.this,
                            "Discovery failed of '" + device.getDisplayString() + "': " +
                                    (ex != null ? ex.toString() : "Couldn't retrieve device/service descriptors"),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
            deviceRemoved(device);
        }
        /* End of optimization, you can remove the whole block if your Android handset is fast (>= 600 Mhz) */

        @Override
        public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
            deviceAdded(device);
        }

        @Override
        public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
            deviceRemoved(device);
        }

        @Override
        public void localDeviceAdded(Registry registry, LocalDevice device) {
            deviceAdded(device);
        }

        @Override
        public void localDeviceRemoved(Registry registry, LocalDevice device) {
            deviceRemoved(device);
        }

        public void deviceAdded(final Device device) {
            runOnUiThread(new Runnable() {
                public void run() {
                    DeviceDisplay d = new DeviceDisplay(device);

                    int position = deviceListAdapter.getPosition(d);
                    if (position >= 0) {
                        // Device already in the list, re-set new value at same position
                        deviceListAdapter.remove(d);
                        deviceListAdapter.insert(d, position);
                    } else {
                        deviceListAdapter.add(d);
                    }

                    // Sort it?
                    // listAdapter.sort(DISPLAY_COMPARATOR);
                    // listAdapter.notifyDataSetChanged();
                }
            });
        }

        public void deviceRemoved(final Device device) {
            runOnUiThread(new Runnable() {
                public void run() {
                    deviceListAdapter.remove(new DeviceDisplay(device));
                }
            });
        }
    }

    protected class DeviceDisplay {

        Device device;

        public DeviceDisplay(Device device) {
            this.device = device;
        }

        public Device getDevice() {
            return device;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DeviceDisplay that = (DeviceDisplay) o;
            return device.equals(that.device);
        }

        @Override
        public int hashCode() {
            return device.hashCode();
        }

        @Override
        public String toString() {
            // Display a little star while the device is being loaded (see performance optimization earlier)
            return device.isFullyHydrated() ? device.getDisplayString() : device.getDisplayString() + " *";
        }
    }

    static final Comparator<DeviceDisplay> DISPLAY_COMPARATOR =
            new Comparator<DeviceDisplay>() {
                public int compare(DeviceDisplay a, DeviceDisplay b) {
                    return a.toString().compareTo(b.toString());
                }
            };
}
