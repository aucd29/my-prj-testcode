/*
 * AlarmService.java
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
package net.sarangnamu.d_day.service;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.service.immortal.ImmortalService;
import net.sarangnamu.common.sqlite.DbManager;
import net.sarangnamu.d_day.db.DbHelper;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Vibrator;

public class AlarmService extends ImmortalService {
    private static final String TAG = "AlarmService";

    private static final int REPEAT_TIME_IN_SECONDS = 60; //repeat every 60 seconds
    private Timer tm;

    @Override
    public void onCreate() {
        super.onCreate();

        setAlarmManager();
        //startAlarm();
        startTimer();
    }

    @Override
    public void onDestroy() {
        if (tm != null) {
            tm.cancel();
        }

        super.onDestroy();
    }

    private void setAlarmManager() {
        Intent intent = new Intent(getActionString());
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), REPEAT_TIME_IN_SECONDS * 1000, sender);
    }

    private void startAlarm() {
        DbManager.getInstance().open(getApplicationContext(), new DbHelper(getApplicationContext()));

        Date date = new Date();
        Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vb.vibrate(100);

        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "" + date.toLocaleString());
        DLog.d(TAG, "===================================================================");

        Cursor cr = DbHelper.selectAlarm();
        while (cr.moveToNext()) {
            if (cr.getInt(2) == 0) {
                continue;
            }

            // alram 은 이벤트 전달 오전 12시 고정으로 진행
            //
            // timer는 현재 시간에서 오후 12시 전으로 시간으로 주고
            // 시간이 맞은 timer 는 하루 1번 실행
            //
            // 타이머 에서 디비를 읽고 오늘 실행 시켜야 하는지 체크
            // 실행 될 녀석이 있으면 알림을 주고 알림 시킨 시간을
            // 디비에 갱신 재 실행 되지 않도록 설정
            //
            DLog.d(TAG, "start a alarm " + cr.getString(1));
        }
    }

    @Override
    public String getActionString() {
        return ResurrectionReceiver.ACTION;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void startTimer() {
        DLog.d(TAG, "===================================================================");
        DLog.d(TAG, "start timer");
        DLog.d(TAG, "===================================================================");
        int period;

        period = 1000 * 60 * 60 * 6;    // 6 hour
        period = 1000 * 10;

        tm = new Timer();
        tm.schedule(new TimerTask() {
            @Override
            public void run() {
                DLog.d(TAG, "===================================================================");
                DLog.d(TAG, "timer");
                DLog.d(TAG, "===================================================================");

                startAlarm();
            }
        }, 0, period); //
    }
}
