/*
 * BkSystem.java
 * Copyright 2014 Burke.Choi All rights reserved.
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
package net.sarangnamu.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.os.SystemClock;

/**
 * <pre>
 * {@code
 * BkSystem.restartService(getApplicationContext());
 * }
 * </pre>
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class BkSystem {
    private static final String TAG = "BkSystem";
    private static final int MAX_TASK = 20;

    public static void restartService(Context appContext) {
        // @see http://stackoverflow.com/questions/20920536/android-kitkat-4-4-kills-my-service-stared-by-alarm-manager
        // in kitkat,use the code snippet below to restart te service automatically:
        //
        // call in onTaskRemoved

        Intent restartService = new Intent(appContext, appContext.getClass());
        restartService.setPackage(appContext.getPackageName());

        PendingIntent restartServicePI = PendingIntent.getService(appContext, 1, restartService,PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);

        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePI);
    }

    public static void restartService(final Context context, Class<?> clazz) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // A restart intent - this never changes...
            final int restartAlarmInterval = 20 * 60 * 1000;
            final int resetAlarmTimer = 2 * 60 * 1000;
            final Intent restartIntent = new Intent(context, clazz);
            restartIntent.putExtra("ALARM_RESTART_SERVICE_DIED", true);
            final AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Handler restartServiceHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    PendingIntent pintent = PendingIntent.getService(context, 0, restartIntent, 0);
                    alarmMgr.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + restartAlarmInterval, pintent);
                    sendEmptyMessageDelayed(0, resetAlarmTimer);
                }
            };

            restartServiceHandler.sendEmptyMessageDelayed(0, 0);
        }
    }

    public static boolean isRunningApp(final Context context) {
        return isRunningApp(context, context.getPackageName());
    }

    public static boolean isRunningApp(final Context context, final String packageName) {
        try {
            if (context == null) {
                throw new Exception("context is null");
            }

            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(MAX_TASK);

            return taskInfo.contains(packageName);
        } catch (Exception e) {
            DLog.e(TAG, "isRunningApp", e);
            return false;
        }
    }

    public static boolean isExistSdCard() {
        return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

//    public static boolean getStorageSize(long fileSize) {
//        long extMemory = 0;
//        long intMemory = getBlockSize(Environment.getDataDirectory());
//
//        if (isExistSdCard()) {
//            extMemory = getBlockSize(Environment.getExternalStorageDirectory());
//            //extMemory = byteToMb(extMemory);
//        }
////
////        intMemory = byteToMb(intMemory);
////
////        if (fileSize < extMemory || fileSize < intMemory) {
////            return true;
////        } else {
////            return false;
////        }
//
//        return true;
//    }

    @SuppressWarnings("deprecation")
    private static long getBlockSize(File path) {
        long blockSize;
        long availableBlocks;
        StatFs stat = new StatFs(path.getPath());

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = stat.getBlockSize();
            availableBlocks = stat.getAvailableBlocks();
        }

        return availableBlocks * blockSize;
    }

    public static StringBuffer ls(String path) throws RuntimeException, IOException, InterruptedException, NullPointerException {
        return exec("ls " + path);
    }

    public static StringBuffer exec(final String command) throws RuntimeException, IOException, InterruptedException, NullPointerException {
        return exec(command, null);
    }

    public static StringBuffer exec(String command, String[] envp) throws RuntimeException, IOException, InterruptedException, NullPointerException {
        if (command == null) {
            throw new NullPointerException("command is null");
        }

        Process process;
        if (envp == null) {
            process = Runtime.getRuntime().exec(command);
        } else {
            process = Runtime.getRuntime().exec(command, envp);
        }

        StringBuffer res = new StringBuffer();
        res.append(readStream(process.getInputStream()));
        res.append(readStream(process.getErrorStream()));

        process.waitFor();

        return res;
    }

    public static StringBuffer execNoWait(String command, String[] envp) throws RuntimeException, IOException, InterruptedException, NullPointerException {
        if (command == null) {
            throw new NullPointerException("command is null");
        }

        Process process;
        if (envp == null) {
            process = Runtime.getRuntime().exec(command);
        } else {
            process = Runtime.getRuntime().exec(command, envp);
        }

        StringBuffer res = new StringBuffer();
        res.append(readStream(process.getInputStream()));
        res.append(readStream(process.getErrorStream()));

        return res;
    }


    public static StringBuffer readStream(InputStream stream) throws IOException {
        int read = 0;
        char[] buffer = new char[1024];
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuffer output = new StringBuffer();

        while ((read = reader.read(buffer)) > 0) {
            output.append(buffer, 0, read);
        }

        reader.close();

        return output;
    }
}
