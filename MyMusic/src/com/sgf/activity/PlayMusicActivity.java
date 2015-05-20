package com.sgf.activity;

import com.sgf.mymusic.R;
import com.sgf.service.PlayService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PlayMusicActivity extends Activity implements OnClickListener {

	private Button play;
	private Button pause;
	private Button prev;
	private Button next;
	private PlayService playService;

	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			playService = ((PlayService.AudioControl) service).getPlayService();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.detail_music);
//		play = (Button) findViewById(R.id.play_music);
//		pause=(Button)findViewById(R.id.pause);
//		prev=(Button)findViewById(R.id.pre);
//		next=(Button)findViewById(R.id.next);
		setContentView(R.layout.play_activity_layout);
		play = (Button) findViewById(R.id.play);
		pause=(Button)findViewById(R.id.play_pause);
		prev=(Button)findViewById(R.id.previous_music);
		next=(Button)findViewById(R.id.next_music);

		Intent intent = getIntent();
		final String path = intent.getStringExtra("url");
		final String title=intent.getStringExtra("title");
		final int position=intent.getIntExtra("position", 0);

		Intent serviceIntent = new Intent(PlayMusicActivity.this,
				PlayService.class);
		serviceIntent.putExtra("path", path);
		serviceIntent.putExtra("title", title);
		serviceIntent.putExtra("position", position);
		startService(serviceIntent);
		bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
		
		prev.setOnClickListener(this);
		play.setOnClickListener(this);
		pause.setOnClickListener(this);
		next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.play:
			playService.play();
			break;
		case R.id.play_pause:
			playService.pause();
			break;
		case R.id.previous_music:
			playService.prev();
			break;
		case R.id.next_music:
			playService.next();
			break;
		default:
			break;
		}
	}

}
