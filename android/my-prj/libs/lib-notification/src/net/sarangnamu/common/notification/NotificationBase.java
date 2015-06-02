/*
 * NotificationBase.java
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
package net.sarangnamu.common.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public abstract class NotificationBase {
    protected NotificationManager mNotiManager;
    protected RemoteViews mRemoteView;

    protected void init(Context context) {
        mNotiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mRemoteView = new RemoteViews(context.getPackageName(), getRemoteId());

        initLayout();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        if (getSmallIcon() != 0) {
            builder.setSmallIcon(getSmallIcon());
        }

        builder.setAutoCancel(isAutoCancel());
        builder.setOngoing(isOngoing());
        builder.setContent(mRemoteView);

        if (getPendingIntent() != null) {
            builder.setContentIntent(getPendingIntent());
        }

        // TODO id 값 은 변경되어야 한다. 이와 관련된 작업은 추후 진행 한다.
        //
        mNotiManager.notify(0, builder.build());
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //
    // ABSTRACT
    //
    ////////////////////////////////////////////////////////////////////////////////////

    protected abstract int getRemoteId();
    protected abstract void initLayout();
    protected abstract int getSmallIcon();
    protected abstract boolean isAutoCancel();
    protected abstract boolean isOngoing();
    protected abstract PendingIntent getPendingIntent();
}
