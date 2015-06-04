package com.sgf.service;

import java.io.IOException;
import java.util.ArrayList;

import com.sgf.helper.MediaUtil;
import com.sgf.model.Music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class PlayService extends Service implements
		MediaPlayer.OnCompletionListener {

	private MediaPlayer mediaPlayer;
	private String path;
	private int position;
	private ArrayList<Music> musiclist = (ArrayList<Music>) MediaUtil.musicList;
	private IBinder binder = new AudioControl();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return binder;
	}

	public void play() {
		try {
			mediaPlayer.start();
			String msg = String.valueOf(mediaPlayer.isPlaying());
			Log.e("sgf", msg);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pause() {
		Log.e("sgf", String.valueOf(mediaPlayer.isPlaying()));
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		}
	}

	public void next() {
		
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.reset();
		}
		if (position == musiclist.size() - 1) {
			position = 0;
		} else {
			++position;
		}
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(musiclist.get(position).getUrl());
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void prev() {
		
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.reset();
		}
		if (position == 0) {
			position = musiclist.size() - 1;
		} else {
			--position;
		}
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(musiclist.get(position).getUrl());
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public class AudioControl extends Binder {

		public PlayService getPlayService() {
			return PlayService.this;
		}

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.e("sgf", "service 服务建立");
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.e("sgf", " int onStartCommand()");
		// path = intent.getStringExtra("path");
		position = intent.getIntExtra("position", 0);

		try {
			// Log.e("sgf", path + "+service");
			mediaPlayer = new MediaPlayer();
			mediaPlayer.reset();
			mediaPlayer.setDataSource(musiclist.get(position).getUrl());
			mediaPlayer.prepare();
			// mediaPlayer.start();
			// String msg=String.valueOf(mediaPlayer.isPlaying());
			// Log.e("sgf",msg );

		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub

	}
}
