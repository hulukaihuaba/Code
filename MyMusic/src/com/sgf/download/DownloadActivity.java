package com.sgf.download;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.sgf.activity.MainActivity;
import com.sgf.mymusic.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class DownloadActivity extends Activity implements OnClickListener {

	private static final String TAG = "sgf";

	private String url;
	private String songName;
	private TextView mMessageView;
	private TextView artist;
	private TextView music_name;
	private ProgressBar mProgressbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_activity);
		findViewById(R.id.download_btn).setOnClickListener(this);
		music_name = (TextView) findViewById(R.id.music_name);
		artist = (TextView) findViewById(R.id.artist);
		mMessageView = (TextView) findViewById(R.id.download_message);
		mProgressbar = (ProgressBar) findViewById(R.id.download_progress);
		Intent intent = getIntent();

		url = intent.getStringExtra("url");
		songName = intent.getStringExtra("songName");
		music_name.setText(songName);
		artist.setText(intent.getStringExtra("singer"));
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.download_btn) {
			doDownload();
		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			mProgressbar.setProgress(msg.getData().getInt("size"));

			float temp = (float) mProgressbar.getProgress()
					/ (float) mProgressbar.getMax();

			int progress = (int) (temp * 100);
			if (progress == 100) {
				Toast.makeText(DownloadActivity.this, "下載完成！",
						Toast.LENGTH_LONG).show();

				
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
						Uri.parse("file://"
								+ Environment.getExternalStorageDirectory()
										.getAbsolutePath())));
				Intent intent = new Intent(DownloadActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
			mMessageView.setText("下载进度:" + progress + " %");

		}
	};

	private void doDownload() {

		String path = Environment.getExternalStorageDirectory() + "/SGF_Music/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
		mProgressbar.setProgress(0);

		String fileName = songName + ".mp3";
		int threadNum = 5;
		String filepath = path + fileName;
		Log.e(TAG, "download file  path:" + filepath);
		downloadTask task = new downloadTask(url, threadNum, filepath);
		task.start();
	}

	class downloadTask extends Thread {
		private String downloadUrl;
		private int threadNum;
		private String filePath;
		private int blockSize;

		public downloadTask(String downloadUrl, int threadNum, String fileptah) {
			this.downloadUrl = downloadUrl;
			this.threadNum = threadNum;
			this.filePath = fileptah;
		}

		@Override
		public void run() {

			FileDownloadThread[] threads = new FileDownloadThread[threadNum];
			try {
				URL url = new URL(downloadUrl);

				URLConnection conn = url.openConnection();

				int fileSize = conn.getContentLength();
				if (fileSize <= 0) {
					return;
				}

				mProgressbar.setMax(fileSize);

				blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum
						: fileSize / threadNum + 1;

				Log.e(TAG, "fileSize:" + fileSize + "  blockSize:" + blockSize);

				File file = new File(filePath);
				for (int i = 0; i < threads.length; i++) {
					threads[i] = new FileDownloadThread(url, file, blockSize,
							(i + 1));
					threads[i].setName("Thread:" + i);
					threads[i].start();
				}

				boolean isfinished = false;
				int downloadedAllSize = 0;
				while (!isfinished) {
					isfinished = true;

					downloadedAllSize = 0;
					for (int i = 0; i < threads.length; i++) {
						downloadedAllSize += threads[i].getDownloadLength();
						if (!threads[i].isCompleted()) {
							isfinished = false;
						}
					}

					Message msg = new Message();
					msg.getData().putInt("size", downloadedAllSize);
					mHandler.sendMessage(msg);
					Log.e(TAG, "current downloadSize:" + downloadedAllSize);
					Thread.sleep(1000);
				}
				Log.e(TAG, " all of downloadSize:" + downloadedAllSize);

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

}
