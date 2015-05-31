package com.sgf.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	private static final String DB_NAME = "people.db";
	private static final String DB_TABLE = "peopleinfo";
	private static final int DB_VERSION = 1;

	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_AGE = "age";
	public static final String KEY_HEIGHT = "height";

	private SQLiteDatabase db;
	private final Context context;
	private DBOpenHelper dbOpenHelper;

	public DBAdapter(Context context) {
		this.context = context;
	}

	private static class DBOpenHelper extends SQLiteOpenHelper {

		public DBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		private static final String DB_CREATE = "create table " + DB_TABLE + "("
				+ KEY_ID + " integer primary key autoincrement," + KEY_NAME
				+ " text not null," + KEY_AGE + " integer," + KEY_HEIGHT
				+ " float);";

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(DB_CREATE);
			Log.e("sgf", "建表成功！");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS" + DB_NAME);
			onCreate(db);
		}

	}

	public void open() throws SQLiteException {
		dbOpenHelper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
		try {
			db = dbOpenHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			// TODO: handle exception
			db = dbOpenHelper.getReadableDatabase();
		}
	}

	public void close() {
		if (db != null) {
			db.close();
			db = null;
		}
	}
	public long insert(){
		ContentValues contentValues=new ContentValues();
		contentValues.put(KEY_AGE, 10);
		contentValues.put(KEY_HEIGHT, 1.75);
		contentValues.put(KEY_NAME, "葫芦");
		
		return db.insert(DB_TABLE, null, contentValues);
		
	}
}
