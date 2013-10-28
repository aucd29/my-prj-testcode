/*
 * DbHelperBase.java
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

import java.util.HashMap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
