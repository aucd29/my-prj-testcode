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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class BkSystem {
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
}
