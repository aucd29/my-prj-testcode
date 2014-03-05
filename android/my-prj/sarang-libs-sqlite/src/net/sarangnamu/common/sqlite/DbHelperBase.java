/*
 * DbHelperBase.java
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
package net.sarangnamu.common.sqlite;

import java.util.HashMap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * {@code
 * <pre>
    public class TestDbHelper extends DbHelperBase {
        private static final String TAG = "TestDbHelper";

        private static final String DB_NAME = "db.db";
        private static final int VERSION = 1;
        public static final String[] FIELDS = new String[] {
            Columns.DATE,
            Columns.DETAIL
        };

        public EmsDbHelper(Context context) {
            super(context, DB_NAME, VERSION);

            tables = new HashMap<String, String>();
            tables.put(Columns.TABLE, Columns.CREATE);
        }

        public static Cursor select() {
            String[] fields = new String[] {Columns._ID, Columns.DATE};
            return DbManager.getInstance().query(Columns.TABLE, fields, null);
        }

        public static Cursor selectDesc() {
            return DbManager.getInstance().query(Columns.TABLE, null, null, "_id DESC");
        }

        public static boolean insert() {
            try {
                ContentValues values = new ContentValues();

                values.put(Columns.DATE, "");
                values.put(Columns.DETAIL, "");

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

                values.put(Columns.DATE, "");
                values.put(Columns.DETAIL, "");

                res = DbManager.getInstance().update(Columns.TABLE, values, "_id=" + id);

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
            public static final String DATE     = "date";
            public static final String DETAIL   = "detail";

            public static final String TABLE = "ems";
            public static final String CREATE = "CREATE TABLE " + TABLE + "("
                    + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DATE + " TEXT NOT NULL, "
                    + DETAIL + " TEXT NOT NULL"
                    + ");";
        }
    }
 * </pre>}
 * 
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public abstract class DbHelperBase extends SQLiteOpenHelper {
    protected HashMap<String, String> tables;

    public DbHelperBase(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (tables == null) {
            return ;
        }

        for (String key : tables.keySet()) {
            db.execSQL(tables.get(key));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String key : tables.keySet()) {
            db.execSQL("DROP TABLE IF EXISTS " + key);
        }

        onCreate(db);
    }

    public void setTables(HashMap<String, String> tables) {
        this.tables = tables;
    }
}
