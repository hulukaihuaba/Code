package com.sgf.activity;

import java.util.ArrayList;
import java.util.List;

import com.sgf.adapter.MusicAdapter;
import com.sgf.model.Music;
import com.sgf.mymusic.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SongListsItemActivity extends Activity {

	List<Music> musicList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_section_listview);

		musicList = (ArrayList<Music>) getIntent().getSerializableExtra(
				"songlist_music");

		Log.e("sgf", "SongListsItemActivity onCreate");

		MusicAdapter musicAdapter = new MusicAdapter(getBaseContext(),
				R.layout.music_item, musicList);
		ListView listView = (ListView) findViewById(R.id.musicList);
		listView.setAdapter(musicAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Music music = musicList.get(position);

				Intent intent = new Intent(getBaseContext(),
						PlayMusicActivity.class);

				intent.putExtra("musicArtist", music.getArtist());
				intent.putExtra("url", music.getUrl());
				intent.putExtra("title", music.getTitle());
				intent.putExtra("position", position);
				startActivity(intent);
			}
		});
	}
}
