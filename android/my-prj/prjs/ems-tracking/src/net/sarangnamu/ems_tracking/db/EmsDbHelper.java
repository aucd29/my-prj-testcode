/*
 * EmsDbHelper.java
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
package net.sarangnamu.ems_tracking.db;

import java.util.HashMap;

import net.sarangnamu.common.DLog;
import net.sarangnamu.common.sqlite.DbHelperBase;
import net.sarangnamu.common.sqlite.DbManager;
import net.sarangnamu.ems_tracking.api.xml.Ems;
import net.sarangnamu.ems_tracking.api.xml.Ems.EmsData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

public class EmsDbHelper extends DbHelperBase {
    private static final String TAG = "EmsDbHelper";

    private static final String DB_NAME = "ems.db";
    private static final int VERSION = 1;
    public static final String[] FIELDS = new String[] {
        Columns.EMS_NUM,
        Columns.DATE,
        Columns.STATUS,
        Columns.DETAIL
    };

    public EmsDbHelper(Context context) {
        super(context, DB_NAME, VERSION);

        mTables = new HashMap<String, String>();
        mTables.put(Columns.TABLE, Columns.CREATE);
    }

    public static Cursor select() {
        String[] fields = new String[] {Columns.EMS_NUM, Columns._ID, Columns.STATUS};
        return DbManager.getInstance().query(Columns.TABLE, fields, null);
    }

    public static Cursor selectDesc() {
        return DbManager.getInstance().query(Columns.TABLE, null, null, "_id DESC");
    }

    public static boolean insert(Ems ems) {
        try {
            ContentValues values = new ContentValues();
            values.put(Columns.EMS_NUM, ems.mEmsNum);

            EmsData emsData = ems.getLastEmsData();
            values.put(Columns.DATE, emsData.date);
            values.put(Columns.STATUS, emsData.status);
            values.put(Columns.OFFICE, emsData.office);
            values.put(Columns.DETAIL, emsData.detail);

            return DbManager.getInstance().insert(Columns.TABLE, values) > 0 ? true : false;
        } catch (NullPointerException e) {
            DLog.e(TAG, "insert", e);
        } catch (Exception e) {
            DLog.e(TAG, "insert", e);
        }

        return false;
    }

    public static boolean update(int id, Ems ems) {
        try {
            ContentValues values = new ContentValues();

            EmsData emsData = ems.getLastEmsData();
            values.put(Columns.DATE, emsData.date);
            values.put(Columns.STATUS, emsData.status);
            values.put(Columns.OFFICE, emsData.office);
            values.put(Columns.DETAIL, emsData.detail);

            int res;
            if (id == 0) {
                res = DbManager.getInstance().update(Columns.TABLE, values, Columns.EMS_NUM + "='" + id + "'");
            } else {
                res = DbManager.getInstance().update(Columns.TABLE, values, "_id=" + id);
            }

            return res > 0 ? true : false;
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
        public static final String EMS_NUM  = "emsNum";
        public static final String DATE     = "date";
        public static final String STATUS   = "status";
        public static final String OFFICE   = "office";
        public static final String DETAIL   = "detail";

        public static final String TABLE = "ems";
        public static final String CREATE = "CREATE TABLE " + TABLE + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EMS_NUM + " TEXT NOT NULL, "
                + DATE + " TEXT NOT NULL, "
                + STATUS + " TEXT NOT NULL, "
                + OFFICE + " TEXT NOT NULL, "
                + DETAIL + " TEXT NOT NULL"
                + ");";
    }
}
