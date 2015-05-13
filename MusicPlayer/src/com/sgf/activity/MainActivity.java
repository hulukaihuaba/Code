package com.sgf.activity;

import java.util.ArrayList;
import java.util.List;

import com.sgf.adapter.MusicAdapter;
import com.sgf.model.Music;
import com.sgf.musicplayer.R;
import com.sgf.service.PlayService;
import com.sgf.helper.MediaUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	List<Music> musicList = new ArrayList<Music>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		musicList = MediaUtil.getMusicList(getApplicationContext());
		MusicAdapter musicAdapter = new MusicAdapter(MainActivity.this,
				R.layout.music_item, musicList);
		ListView listView = (ListView) findViewById(R.id.musicList);
		listView.setAdapter(musicAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Music music = musicList.get(position);
				Toast.makeText(MainActivity.this, "∏Ë«˙ÈL∂»£∫" + music.getDuration(),
						Toast.LENGTH_LONG).show();
				// Intent intent=new Intent();
				// intent.putExtra("url", music.getUrl());
				// intent.setClass(getApplicationContext(), PlayService.class);
				// startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
