/*
 * AppList.java
 * Copyright 2013 Burke.Choi All rights reserved.
 *             http://www.sarangnamu.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sarangnamu.apk_extractor;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class AppList {
    private static final String TAG = "AppList";
    private static AppList inst;

    public static AppList getInstance() {
        if (inst == null) {
            inst = new AppList();
        }

        return inst;
    }

    private AppList() {

    }

    public ArrayList<PkgInfo> getInstalledApps(Context context, boolean getSysPackages) {
        ArrayList<PkgInfo> res = new ArrayList<PkgInfo>();
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);

        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }

            PkgInfo newInfo = new PkgInfo();
            newInfo.appName = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
            newInfo.pkgName = p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;
            newInfo.icon = p.applicationInfo.loadIcon(context.getPackageManager());

            res.add(newInfo);
        }

        return res;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // PkgInfo
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public static class PkgInfo {
        public String appName = "";
        public String pkgName = "";
        public String versionName = "";
        public int versionCode = 0;
        public Drawable icon;
    }
}

