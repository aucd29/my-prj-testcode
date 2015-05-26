/*
 * AppList.java
 * Copyright 2013 Burke Choi All rights reserved.
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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.WeakHashMap;

import net.sarangnamu.common.BkMath;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

public class AppList {
    private static AppList inst;
    private WeakHashMap<String, Drawable> iconMap;

    public static AppList getInstance() {
        if (inst == null) {
            inst = new AppList();
        }

        return inst;
    }

    private AppList() {

    }

    public ArrayList<PkgInfo> getInstalledApps(Context context, String sortBy) {
        return getAllApps(context, true, sortBy);
    }

    public ArrayList<PkgInfo> getAllApps(Context context, boolean hideSystemApp, String sortBy) {
        ArrayList<PkgInfo> res = new ArrayList<PkgInfo>();
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);

        if (iconMap == null) {
            iconMap = new WeakHashMap<String, Drawable>();
        }

        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if (hideSystemApp) {
                if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    continue;
                }
            }

            PkgInfo newInfo = new PkgInfo();
            newInfo.appName = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
            newInfo.pkgName = p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;

            if (!iconMap.containsKey(p.packageName)) {
                iconMap.put(p.packageName, p.applicationInfo.loadIcon(context.getPackageManager()).getConstantState().newDrawable());
            }

            newInfo.icon = iconMap.get(p.packageName);
            newInfo.srcDir = p.applicationInfo.sourceDir;
            newInfo.size = new File(p.applicationInfo.sourceDir).length();
            newInfo.appSize = BkMath.toFileSizeString(newInfo.size);
            newInfo.firstInstallTime = p.firstInstallTime;

            res.add(newInfo);
        }

        if (sortBy.equals("installTime")) {
            Collections.sort(res, new SortByInstallTime());
        } else if (sortBy.equals("alphabet")) {
            Collections.sort(res, new SortByAlphabet());
        }

        return res;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // Comparator
    //
    ////////////////////////////////////////////////////////////////////////////////////

    class SortByInstallTime implements Comparator<PkgInfo> {
        @Override
        public int compare(PkgInfo lhs, PkgInfo rhs) {
            if (lhs.firstInstallTime > rhs.firstInstallTime) {
                return 1;
            } else if (lhs.firstInstallTime < rhs.firstInstallTime) {
                return -1;
            }

            return 0;
        }
    }

    class SortByAlphabet implements Comparator<PkgInfo> {
        @Override
        public int compare(PkgInfo lhs, PkgInfo rhs) {
            return lhs.appName.compareTo(rhs.appName);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // PkgInfo
    //
    ////////////////////////////////////////////////////////////////////////////////////

    public static class PkgInfo {
        public String appName;
        public String pkgName;
        public String versionName;
        public String appSize;
        public String srcDir;
        public int versionCode = 0;
        public long size;
        public Drawable icon;
        public long firstInstallTime = 0;
    }
}
