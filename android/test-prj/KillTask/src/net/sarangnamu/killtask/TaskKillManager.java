package net.sarangnamu.killtask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class TaskKillManager {
    private static final String TAG = "TaskKillManager";
    private static ArrayList<String> denyList = new ArrayList<String>(
            Arrays.asList(
                    "system",
                    "com.android.phone",
                    "com.android.bluetooth",
                    "com.android.alarmclock",
                    "com.android.systemui",
                    "com.android.settings",
                    "com.android.inputmethod.latin",
                    "com.android.inputmethod.pinyin",
                    "com.android.alarmclock",
                    "com.android.providers.media",
                    "com.android.mms",
                    "com.android.deskclock",
                    "com.android.calendar",
                    "com.android.voicedialer",
                    "com.android.clock",
                    "com.android.providers.telephony",
                    "com.android.providers.calendar",
                    "com.android.heroled",
                    "com.android.wallpaper",
                    "com.android.exchange",
                    "com.android.bluetooth",
                    "android.process.acore",
                    "android.process.media",
                    "com.htc.android.mail",
                    "com.skt.skaf.Z0000OMPDL",
                    "com.google.android.partnersetup",
                    "com.svox.pico",
                    "com.wssyncmldm",
                    "com.android.defcontainer",
                    "com.sec.android.widgetapp.programmonitorwidget"));

    public static long getAvailbleMemory(Context context) {
        long mem = 0;

        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            android.app.ActivityManager.MemoryInfo memoryinfo = new android.app.ActivityManager.MemoryInfo();
            manager.getMemoryInfo(memoryinfo);
            mem = memoryinfo.availMem;

            Log.d(TAG, "===================================================================");
            Log.d(TAG, "Available Memory : " + (mem / 1024L / 1024L) + "MB");
            Log.d(TAG, "===================================================================");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mem;
    }

    public static long getAvailbleMemoryMB(Context context) {
        return getAvailbleMemory(context) / 1024L / 1024L;
    }

    public static ArrayList<String> getRunningProcessList(Context context) {
        ArrayList<String> list = new ArrayList<String>();

        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            PackageManager pkManager = context.getPackageManager();
            List<RunningAppProcessInfo> runningAppList = manager.getRunningAppProcesses();

            for (RunningAppProcessInfo info : runningAppList) {
                if (info.processName.startsWith("com.android.inputmethod") ||
                        info.processName.startsWith("com.sec.android.widgetapp") ||
                        info.processName.startsWith("com.sec.android.provider") ||
                        denyList.contains(info.processName)) {
                    continue;
                }

                // info.importance
                // RunningAppProcessInfo.IMPORTANCE_FOREGROUND (100)
                // RunningAppProcessInfo.IMPORTANCE_SERVICE (300)
                // RunningAppProcessInfo.IMPORTANCE_BACKGROUND (400)
                if (info.importance != RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    continue;
                }

                try {
                    ApplicationInfo appInfo = pkManager.getApplicationInfo(info.processName, 0);
                    String name = appInfo.loadLabel(pkManager).toString();

                    boolean icon = true;
                    if (appInfo.loadIcon(pkManager) == null) {
                        icon = false;
                    }

                    if (info.processName.equals(name)) {
                        continue;
                    }

                    //Log.d(TAG, "SNAME : " + " (" + info.importance + ")" + " (f " + appInfo.flags + ")" + " (" + info.importanceReasonPid+ ")" + info.processName + " (" + name + ")");

                    list.add(info.processName);
                } catch (Exception e) {
                    // invalid app
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void killProcess(Context context, ArrayList<String> list) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (String packageName : list) {
            //Log.d(TAG, "KILL PROCESS NAME : " + packageName);
            manager.killBackgroundProcesses(packageName);
        }
    }
}
