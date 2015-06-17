package com.sgf.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "mymusic.db";
	private static final int DB_VERSION = 1;
	private static final String CREATE_MUSIC="CREATE TABLE [music] ( [M_ID] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
			+ " [id] INTEGER NOT NULL,  [artist] TEXT NOT NULL,  [size] INTEGER NOT NULL,  [url] TEXT NOT NULL, "
			+ " [title] TEXT NOT NULL,[duration] INTEGER NOT NULL);";
	private static final String CREATE_SONGLIST = "CREATE TABLE [songlist] ( [list_name] TEXT NOT NULL, "
			+ " [length] INTEGER NOT NULL,  CONSTRAINT [sqlite_autoindex_songlist_1] PRIMARY KEY ([list_name]));";
	private static final String CREATE_SECTION ="CREATE TABLE [section] ( [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
			+ " [music_id] INTEGER NOT NULL CONSTRAINT [m_Id] REFERENCES [music]([M_ID]) ON DELETE CASCADE,"
			+ " [l_name] TEXT NOT NULL CONSTRAINT [songlist_name] REFERENCES [songlist]([list_name]) ON DELETE CASCADE);";
	
	public DBOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_MUSIC);
		db.execSQL(CREATE_SONGLIST);
		db.execSQL(CREATE_SECTION);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS" + DB_NAME);
		onCreate(db);
	}

}
