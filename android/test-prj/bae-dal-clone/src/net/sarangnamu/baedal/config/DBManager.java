package net.sarangnamu.baedal.config;

import java.util.HashMap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 *
 * @author kurome
 *
 */

/*
 * 1.0 Storage Classes and Datatypes
 * Each value stored in an SQLite database (or manipulated by
 * the database engine) has one of the following storage classes:
 *
 * NULL.	The value is a NULL value.
 *
 * INTEGER. The value is a signed integer, stored in 1, 2, 3, 4, 6, or 8
 *			bytes depending on the magnitude of the value.
 *
 * REAL.	The value is a floating point value, stored as an 8-byte IEEE floating point number.
 *
 * TEXT.	The value is a text string, stored using the database encoding
 *			(UTF-8, UTF-16BE or UTF-16LE).
 *
 * BLOB.	The value is a blob of data, stored exactly as it was input.
 */
public class DBManager {
	private DBOpenHelper helper;
	private static final int dbVersion = 1;
	private static final String[] tableNames = {"favorite", "callhistory"};
	private static final String dbName = "baedal.db";
	private static final HashMap<String, String> tables  = new HashMap<String, String>();

	static {
		tables.put(tableNames[0],
			"(" +
			"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"name TEXT " 		+
			");"
		);

		tables.put(tableNames[1],
			"(" +
			"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"name TEXT" 			+
			");"
		);
	}

	/**
	 *
	 * @param ctx
	 */
	public DBManager(Context ctx) {
		helper = new DBOpenHelper(ctx);

	}

	/**
	 *
	 * @return
	 */
	public static String getTableName(int index) {
		return tableNames[index];
	}

	/**
	 *
	 * @return
	 */
	public String getDBName() {
		return dbName;
	}

	/**
	 *
	 * @return
	 */
	public SQLiteDatabase getDB() {
		return helper.getWritableDatabase();
	}

	class DBOpenHelper extends SQLiteOpenHelper {
	    DBOpenHelper(Context context) {
	        super(context, dbName, null, dbVersion);
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) {
	    	Log.d("", "@@@@@@@@@@@@@@@@@@@@@@ db onCreate @@@@@@@@@@@@@@@@@@@@@@");

	    	String query;
			for (String key : tables.keySet()) {
				query  = "CREATE TABLE " + key + tables.get(key);
				Log.d("", query);

				db.execSQL(query);
			}
	    }

	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        Log.d("", "@@@@@@@@@@@@@@@@@@@@@@ db onUpgrade @@@@@@@@@@@@@@@@@@@@@@");

	        for (String key : tables.keySet()) {
	        	db.execSQL("DROP TABLE IF EXISTS " + key);
	        }

	        onCreate(db);
	    }
	}
}
