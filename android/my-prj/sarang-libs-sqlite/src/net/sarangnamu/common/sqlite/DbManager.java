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

/**
 * {@code
 * <pre>

 * </pre>}
 * 
 * @author <a href="mailto:aucd29@gmail.com.com">Burke Choi</a>
 */
public class DbManager {
    private static final String TAG = "DbManager";
    private static DbManager inst;

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
        return db.insert(table, null, inputValues);
    }

    public int update(String table, ContentValues inputValues, String where) {
        return db.update(table, inputValues, where, null);
    }

    public int delete(String table, String where) {
        return db.delete(table, where, null);
    }
}
