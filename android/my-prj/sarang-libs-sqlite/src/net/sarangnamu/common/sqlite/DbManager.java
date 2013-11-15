/*
 * DbManager.java
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
package net.sarangnamu.common.sqlite;

import net.sarangnamu.common.DLog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbManager {
    private static final String TAG = "DbManager";
    //private static final String DB = "db.db";
    private static DbManager inst;

    //private int version = 1;
    protected SQLiteDatabase db;
    protected DbHelperBase helper;

    public static DbManager getInstance() {
        if (inst == null) {
            inst = new DbManager();
        }

        return inst;
    }

    private DbManager() {

    }

    public void open(Context context, DbHelperBase helper) {
        try {
            this.helper = helper;
            this.db = helper.getWritableDatabase();
        } catch (Exception e) {
            DLog.e(TAG, "setOpenHelper", e);
        }
    }

    public void close() {
        db.close();
        helper.close();
    }

    public Cursor query(String table, String[] fields, String where) {
        return db.query(table, fields, where, null, null, null, null);
    }

    public Cursor query(String table, String[] fields, String where, String orderBy) {
        return db.query(table, fields, where, null, null, null, orderBy);
    }

    public Cursor rawQuery(String sql, String[] args) {
        return db.rawQuery(sql, args);
    }

    public long insert(String table, ContentValues inputValues) {
        long res = 0;

        db.beginTransaction();
        try {
            res = db.insert(table, null, inputValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            DLog.e(TAG, "insert", e);
        } finally {
            db.endTransaction();
        }

        return res;
    }

    public int update(String table, ContentValues inputValues, String where) {
        int res = 0;

        db.beginTransaction();
        try {
            res = db.update(table, inputValues, where, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            DLog.e(TAG, "insert", e);
        } finally {
            db.endTransaction();
        }

        return res;
    }

    public int delete(String table, String where) {
        int res = 0;

        db.beginTransaction();
        try {
            res = db.delete(table, where, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            DLog.e(TAG, "delete", e);
        } finally {
            db.endTransaction();
        }

        return res;
    }
}
