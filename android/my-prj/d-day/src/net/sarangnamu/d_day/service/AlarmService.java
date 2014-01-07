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

import net.sarangnamu.common.service.immortal.ImmortalService;
import android.content.Intent;

public class AlarmService extends ImmortalService {
    private static final String TAG = "AlarmService";

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public String getActionString() {
        return AlarmReceiver.ACTION;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
