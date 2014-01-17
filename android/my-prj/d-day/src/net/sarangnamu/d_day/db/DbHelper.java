/*
 * DbHelper.java
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
package net.sarangnamu.d_day.db;

import java.util.HashMap;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.sqlite.DbHelperBase;
import net.sarangnamu.common.sqlite.DbManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

public class DbHelper extends DbHelperBase {
    private static final String TAG = "DbHelper";

    private static final String DB_NAME = "db.db";
    private static final int VERSION = 1;
    public static final String[] FIELDS = new String[] {
        Columns.REMINDER,
        Columns.DATE,
        Columns.TITLE,
        Columns.DESCRIPTION
    };

    public DbHelper(Context context) {
        super(context, DB_NAME, VERSION);

        tables = new HashMap<String, String>();
        tables.put(Columns.TABLE, Columns.CREATE);
    }

    public static Cursor selectAlarm() {
        String where = Columns.ALARM + "=1";
        return DbManager.getInstance().query(Columns.TABLE, null, where);
    }

    public static Cursor selectDesc() {
        return DbManager.getInstance().query(Columns.TABLE, null, null, "_id DESC");
    }

    public static Cursor select(int id) {
        Cursor cr = DbManager.getInstance().query(Columns.TABLE, null, "_id=" + id);
        if (cr != null) {
            cr.moveToFirst();
        } else {
            DLog.e(TAG, "select cr == null");
        }

        return cr;
    }

    public static boolean insert(ScheduleData data) {
        try {
            ContentValues values = new ContentValues();
            values.put(Columns.TITLE, data.title);
            values.put(Columns.DATE, data.date);
            values.put(Columns.DESCRIPTION, data.description);
            values.put(Columns.REMINDER, data.reminder);
            values.put(Columns.ALARM, data.alarm);
            values.put(Columns.ALARM_TIME, "");

            return DbManager.getInstance().insert(Columns.TABLE, values) > 0 ? true : false;
        } catch (NullPointerException e) {
            DLog.e(TAG, "insert", e);
        } catch (Exception e) {
            DLog.e(TAG, "insert", e);
        }

        return false;
    }

    public static boolean update(int id, ScheduleData data) {
        try {
            ContentValues values = new ContentValues();
            values.put(Columns.TITLE, data.title);
            values.put(Columns.DATE, data.date);
            values.put(Columns.DESCRIPTION, data.description);
            values.put(Columns.REMINDER, data.reminder);
            values.put(Columns.ALARM, data.alarm);

            return DbManager.getInstance().update(Columns.TABLE, values, "_id=" + id) > 0 ? true : false;
        } catch (NullPointerException e) {
            DLog.e(TAG, "update", e);
        } catch (Exception e) {
            DLog.e(TAG, "update", e);
        }

        return false;
    }

    public static boolean delete(int id) {
        try {
            int res = DbManager.getInstance().delete(Columns.TABLE, "_id=" + id);
            return res > 0 ? true : false;
        } catch (NullPointerException e) {
            DLog.e(TAG, "delete", e);
        } catch (Exception e) {
            DLog.e(TAG, "delete", e);
        }

        return false;
    }

    public static final class Columns implements BaseColumns {
        public static final String TITLE        = "title";
        public static final String DATE         = "date";
        public static final String DESCRIPTION  = "description";
        public static final String REMINDER     = "reminder";
        public static final String ALARM        = "alarm";
        public static final String ALARM_TIME   = "alarmTime";

        public static final String TABLE = "dday";
        public static final String CREATE = "CREATE TABLE " + TABLE + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE + " TEXT NOT NULL, "
                + DATE + " TEXT NOT NULL, "
                + DESCRIPTION + " TEXT NOT NULL, "
                + REMINDER + " INTEGER NOT NULL, "
                + ALARM + " INTEGER NOT NULL, "
                + ALARM_TIME + " TEXT NOT NULL"
                + ");";
    }
}
