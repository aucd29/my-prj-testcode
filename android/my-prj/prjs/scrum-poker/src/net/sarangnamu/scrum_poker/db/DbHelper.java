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
package net.sarangnamu.scrum_poker.db;

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

    private static final String DB_NAME = "data.db";
    private static final int VERSION = 1;
    public static final String[] FIELDS = new String[] {
        Columns.TITLE,
        Columns.DATE,
        Columns.CONTENT
    };

    public DbHelper(Context context) {
        super(context, DB_NAME, VERSION);

        mTables = new HashMap<String, String>();
        mTables.put(Columns.TABLE, Columns.CREATE);
    }

    public static Cursor select() {
        String[] fields = new String[] {Columns._ID, Columns.TITLE, Columns.CONTENT};
        return DbManager.getInstance().query(Columns.TABLE, fields, null);
    }

    public static Cursor selectDesc() {
        return DbManager.getInstance().query(Columns.TABLE, null, null, "_id DESC");
    }

    public static boolean insert(UserScrumData data) {
        try {
            ContentValues values = new ContentValues();
            values.put(Columns.TITLE, data.getTitle());
            values.put(Columns.CONTENT, data.getContents());
            values.put(Columns.DATE, System.currentTimeMillis());

            return DbManager.getInstance().insert(Columns.TABLE, values) > 0 ? true : false;
        } catch (NullPointerException e) {
            DLog.e(TAG, "insert", e);
        } catch (Exception e) {
            DLog.e(TAG, "insert", e);
        }

        return false;
    }

    public static boolean update(int id, UserScrumData data) {
        try {
            ContentValues values = new ContentValues();

            values.put(Columns.TITLE, data.getTitle());
            values.put(Columns.CONTENT, data.getContents());

            int res = DbManager.getInstance().update(Columns.TABLE, values, "_id=" + id);

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
        public static final String TITLE    = "title";
        public static final String CONTENT  = "content";
        public static final String DATE     = "date";

        public static final String TABLE = "userdata";
        public static final String CREATE = "CREATE TABLE " + TABLE + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE + " TEXT NOT NULL, "
                + DATE + " TEXT NOT NULL, "
                + CONTENT + " TEXT NOT NULL"
                + ");";
    }
}
