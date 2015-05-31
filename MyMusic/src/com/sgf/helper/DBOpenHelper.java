package com.sgf.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "mymusic.db";
	private static final int DB_VERSION = 1;
	private static final String CREATE_MUSIC="CREATE TABLE [music] ([music_id] INTEGER PRIMARY KEY, [artist] TEXT NOT NULL, [url] TEXT NOT NULL, [title] TEXT NOT NULL, [duration] TEXT NOT NULL);";
	private static final String CREATE_SONGLIST = "CREATE TABLE [songlist] ([songlist_id] INTEGER PRIMARY KEY AUTOINCREMENT, [name] TEXT NOT NULL, [length] INTEGER NOT NULL);";
	private static final String CREATE_SECTION ="CREATE TABLE [section] ([ID] INTEGER PRIMARY KEY AUTOINCREMENT, [M_id] INTEGER NOT NULL CONSTRAINT [mus_id] REFERENCES [music]([music_id]) ON DELETE CASCADE, [S_id] INTEGER NOT NULL CONSTRAINT [slist_id] REFERENCES [songlist]([songlist_id]) ON DELETE CASCADE);";
	
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
