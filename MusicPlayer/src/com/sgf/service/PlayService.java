package com.sgf.service;

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
	private IBinder binder = new AudioControl();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return binder;
	}

	public void play() {
		try {
			mediaPlayer.start();
			String msg=String.valueOf(mediaPlayer.isPlaying());
			Log.e("sgf",msg );

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void pause(){
		Log.e("sgf",String.valueOf(mediaPlayer.isPlaying()));
		if(mediaPlayer.isPlaying()){
			mediaPlayer.pause();
		}
	}
	
	public class AudioControl extends Binder {

		public PlayService getPlayService(){
			return PlayService.this;
		}
		
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.e("sgf", "void onCreate()");
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.e("sgf", " int onStartCommand()");
		path = intent.getStringExtra("path");
		Log.e("sgf", path+"¿ÉÒÔµÄ£¡£¡£¡£¡");

		try {
//			Log.e("sgf", path + "+service");
			mediaPlayer=new MediaPlayer();
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
//			mediaPlayer.start();
//			String msg=String.valueOf(mediaPlayer.isPlaying());
//			Log.e("sgf",msg );

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
