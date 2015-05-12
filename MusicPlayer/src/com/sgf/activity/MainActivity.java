package com.sgf.activity;

import java.util.ArrayList;
import java.util.List;

import com.sgf.adapter.MusicAdapter;
import com.sgf.model.Music;
import com.sgf.musicplayer.R;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.ListView;

public class MainActivity extends Activity {

	List<Music> musicList = new ArrayList<Music>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initMusic();
		MusicAdapter musicAdapter = new MusicAdapter(MainActivity.this,
				R.layout.music_item, musicList);
		ListView listView = (ListView) findViewById(R.id.musicList);
		listView.setAdapter(musicAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void initMusic() {
		// String[] music_model = new String[] { Media.TITLE, Media.DURATION };
		Cursor cursor = getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

		while (cursor.moveToNext()) {
			Music music = new Music();
			String title = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.TITLE));
			String singer = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ARTIST));
			String album = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ALBUM));
			long size = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.SIZE));
			long time = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.DURATION));
			String url = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DATA));
			int _id = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media._ID));
			String name = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
			String sbr = name.substring(name.length() - 3, name.length());
			music.setTitle(title);
			music.setDuration(time);
			musicList.add(music);
		}

	}
}
