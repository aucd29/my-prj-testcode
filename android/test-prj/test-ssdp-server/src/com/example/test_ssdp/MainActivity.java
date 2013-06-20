package com.example.test_ssdp;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.teleal.cling.android.AndroidUpnpService;
import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.model.action.ActionArgumentValue;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.message.header.RootDeviceHeader;
import org.teleal.cling.model.message.header.STAllHeader;
import org.teleal.cling.model.meta.Action;
import org.teleal.cling.model.meta.Device;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.test_ssdp.service.UpnpService;
import com.example.test_ssdp_server.R;

public class MainActivity extends ListActivity {
    private static final String TAG = MainActivity.class.toString();

    private ArrayAdapter<DeviceDisplay> listAdapter;

    private BrowseRegistryListener registryListener = new BrowseRegistryListener();

    private AndroidUpnpService upnpService;

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            upnpService = (AndroidUpnpService) service;

            // Clear the list
            listAdapter.clear();

            // Get ready for future device advertisements
            upnpService.getRegistry().addListener(registryListener);

            // Now add all devices to the list we already know about
            for (Device device : upnpService.getRegistry().getDevices()) {
                registryListener.deviceAdded(device);
            }

            // Search asynchronously for all devices, they will respond soon
            upnpService.getControlPoint().search();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            upnpService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);

        listAdapter = new ArrayAdapter<DeviceDisplay>(this,
                android.R.layout.simple_list_item_1);
        setListAdapter(listAdapter);
        switchToDeviceList();

        // This will start the UPnP service if it wasn't already started
        getApplicationContext().bindService(
                new Intent(this, UpnpService.class), serviceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (upnpService != null) {
            upnpService.getRegistry().removeListener(registryListener);
        }
        // This will stop the UPnP service if nobody else is bound to it
        getApplicationContext().unbindService(serviceConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, R.string.searchLAN).setIcon(
                android.R.drawable.ic_menu_search);
        // DOC:OPTIONAL
        menu.add(0, 1, 0, "Searching root device").setIcon(
                android.R.drawable.ic_menu_revert);
        menu.add(0, 2, 0, R.string.toggleDebugLogging).setIcon(
                android.R.drawable.ic_menu_info_details);
        // DOC:OPTIONAL
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case 0:
            searchNetwork();
            break;

        case 1:
            searchRootDevice();
            break;

        case 2:
            Logger logger = Logger.getLogger("org.teleal.cling");
            if (logger.getLevel().equals(Level.FINEST)) {
                Toast.makeText(this, "disabling_debug_logging", Toast.LENGTH_SHORT).show();
                logger.setLevel(Level.INFO);
            } else {
                Toast.makeText(this, "enabling_debug_logging",Toast.LENGTH_SHORT).show();
                logger.setLevel(Level.FINEST);
            }
            break;
        }
        return false;
    }

    protected void searchNetwork() {
        if (upnpService == null) {
            return;
        }
        Toast.makeText(this, "searching_lan", Toast.LENGTH_SHORT).show();
        upnpService.getRegistry().removeAllRemoteDevices();
        upnpService.getControlPoint().search(new STAllHeader());
    }

    protected void searchRootDevice() {
        if (upnpService == null) {
            return;
        }

        upnpService.getRegistry().removeAllRemoteDevices();
        upnpService.getControlPoint().search(new RootDeviceHeader());
    }

    // //////////////////////////////////////////////////////////////////////////////////
    //
    // CLICK EVENTS
    //
    // //////////////////////////////////////////////////////////////////////////////////

    public void switchToDeviceList() {
        setListAdapter(listAdapter);

        /* Executes when the user (long) clicks on a device:
         */
        getListView().setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final DeviceDisplay clickedDisplay = listAdapter.getItem(position);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
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

                                        //                            Service service2 = clickedDisplay.getDevice().findService(new UDAServiceId("ContentDirectory"));
                                        //                            if (service2 != null) {
                                        //                                DeviceIdentity identity = clickedDisplay.getDevice().getIdentity();
                                        //                                switchToContentList(identity);
                                        //                            }
                                    } // end
                                } catch (NullPointerException e) {
                                    Log.e(TAG, "onItemClick", e);
                                } catch (Exception e) {
                                    Log.e(TAG, "onItemClick", e);
                                }
                            }
                        }).start();
                    }
                }
                );

    }

    // public void switchToContentList(DeviceIdentity identity) {
    // Intent intent = new Intent(this, ContentActivity.class);
    // intent.putExtra (ContentActivity.DEVICE_UUID,
    // identity.getUdn().toString());
    // startActivity(intent);
    // }

    // //////////////////////////////////////////////////////////////////////////////////
    //
    // SUB CLASS
    //
    // //////////////////////////////////////////////////////////////////////////////////

    protected class BrowseRegistryListener extends DefaultRegistryListener {

        /* Discovery performance optimization for very slow Android devices! */
        @Override
        public void remoteDeviceDiscoveryStarted(Registry registry,
                RemoteDevice device) {
            Log.d(TAG, "===================================================================");
            Log.d(TAG, "remoteDeviceDiscoveryStarted");
            Log.d(TAG, "===================================================================");
            Log.d(TAG, device.getDetails().getManufacturerDetails().getManufacturer());
            Log.d(TAG, device.getDetails().getModelDetails().getModelName());
            Log.d(TAG, device.getDetails().getModelDetails().getModelNumber());
            Log.d(TAG, "===================================================================");

            deviceAdded(device);
        }

        @Override
        public void remoteDeviceDiscoveryFailed(Registry registry,
                final RemoteDevice device, final Exception ex) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(
                            MainActivity.this,
                            "Discovery failed of '"
                                    + device.getDisplayString()
                                    + "': "
                                    + (ex != null ? ex.toString()
                                            : "Couldn't retrieve device/service descriptors"),
                                            Toast.LENGTH_LONG).show();
                }
            });
            deviceRemoved(device);
        }

        /*
         * End of optimization, you can remove the whole block if your Android
         * handset is fast (>= 600 Mhz)
         */

        @Override
        public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
            Log.d(TAG,
                    "===================================================================");
            Log.d(TAG, "remoteDeviceAdded");
            Log.d(TAG,
                    "===================================================================");
            Log.d(TAG, device.getDetails().getManufacturerDetails()
                    .getManufacturer());
            Log.d(TAG, device.getDetails().getModelDetails().getModelName());
            Log.d(TAG, device.getDetails().getModelDetails().getModelNumber());
            Log.d(TAG,
                    "===================================================================");

            deviceAdded(device);
        }

        @Override
        public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
            Log.d(TAG,
                    "===================================================================");
            Log.d(TAG, "remoteDeviceRemoved");
            Log.d(TAG,
                    "===================================================================");
            Log.d(TAG, device.getDetails().getManufacturerDetails()
                    .getManufacturer());
            Log.d(TAG, device.getDetails().getModelDetails().getModelName());
            Log.d(TAG, device.getDetails().getModelDetails().getModelNumber());
            Log.d(TAG,
                    "===================================================================");

            deviceRemoved(device);
        }

        @Override
        public void localDeviceAdded(Registry registry, LocalDevice device) {
            Log.d(TAG,
                    "===================================================================");
            Log.d(TAG, "localDeviceAdded");
            Log.d(TAG,
                    "===================================================================");
            Log.d(TAG, device.getDetails().getManufacturerDetails()
                    .getManufacturer());
            Log.d(TAG, device.getDetails().getModelDetails().getModelName());
            Log.d(TAG, device.getDetails().getModelDetails().getModelNumber());
            Log.d(TAG,
                    "===================================================================");

            deviceAdded(device);
        }

        @Override
        public void localDeviceRemoved(Registry registry, LocalDevice device) {
            Log.d(TAG,
                    "===================================================================");
            Log.d(TAG, "localDeviceRemoved");
            Log.d(TAG,
                    "===================================================================");
            Log.d(TAG, device.getDetails().getManufacturerDetails()
                    .getManufacturer());
            Log.d(TAG, device.getDetails().getModelDetails().getModelName());
            Log.d(TAG, device.getDetails().getModelDetails().getModelNumber());
            Log.d(TAG,
                    "===================================================================");

            deviceRemoved(device);
        }

        public void deviceAdded(final Device device) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DeviceDisplay d = new DeviceDisplay(device);
                    int position = listAdapter.getPosition(d);
                    if (position >= 0) {
                        // Device already in the list, re-set new value at same
                        // position
                        listAdapter.remove(d);
                        listAdapter.insert(d, position);
                    } else {
                        listAdapter.add(d);
                    }
                }
            });
        }

        public void deviceRemoved(final Device device) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listAdapter.remove(new DeviceDisplay(device));
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

        // DOC:DETAILS
        public String getDetailsMessage() {
            StringBuilder sb = new StringBuilder();
            if (getDevice().isFullyHydrated()) {
                sb.append(getDevice().getDisplayString());
                sb.append("\n\n");
                for (Service service : getDevice().getServices()) {
                    sb.append(service.getServiceType()).append("\n");
                }
            } else {
                sb.append(getString(R.string.deviceDetailsNotYetAvailable));
            }
            return sb.toString();
        }

        // DOC:DETAILS

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            DeviceDisplay that = (DeviceDisplay) o;
            return device.equals(that.device);
        }

        @Override
        public int hashCode() {
            return device.hashCode();
        }

        @Override
        public String toString() {
            // String name =
            // getDevice().getDetails() != null &&
            // getDevice().getDetails().getFriendlyName() != null
            // ? getDevice().getDetails().getFriendlyName()
            // : getDevice().getDisplayString();

            String name = device.getDisplayString();
            // Display a little star while the device is being loaded (see
            // performance optimization earlier)
            return device.isFullyHydrated() ? name : name + " *";
        }
    }
}
