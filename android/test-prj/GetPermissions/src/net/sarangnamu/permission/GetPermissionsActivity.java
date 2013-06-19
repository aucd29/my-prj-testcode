package net.sarangnamu.permission;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;

public class GetPermissionsActivity extends Activity {
    private static final String TAG = "GetPermissionsActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final List<ResolveInfo> pkglist = getPackageManager().queryIntentActivities(mainIntent, 0);
        try {
            PackageInfo pkgInfo;
            String appName;
            for (ResolveInfo info : pkglist) {
                appName = info.loadLabel(getPackageManager()).toString();
                Log.d(TAG, "permission " + info.activityInfo.packageName + " " + appName);
                pkgInfo = getPackageManager().getPackageInfo(info.activityInfo.packageName, PackageManager.GET_PERMISSIONS);

                if (pkgInfo.requestedPermissions == null) {
                    continue;
                }

                for (String perm : pkgInfo.requestedPermissions) {
                    Log.d(TAG, "        " + perm);
                }
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}