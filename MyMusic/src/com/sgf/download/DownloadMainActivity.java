package com.sgf.download;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sgf.mymusic.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class DownloadMainActivity extends Activity implements OnClickListener {

	private String Json_ForId;
	private List<MusicInfo> listInfo;
	private ListView list;
	private AutoCompleteTextView autoText;
	private ImageButton imgbt;
	private SimpleAdapter simpleAdapter;

	private ArrayList<String> song_ids = new ArrayList<String>();
	private ArrayList<String> songInfo = new ArrayList<String>();
	private ArrayList<String> jsons = new ArrayList<String>();
	private List<Map<String, String>> listMusic = new ArrayList<Map<String, String>>();
	private Map<String, String> jsonList = new HashMap<String, String>();
	private String musicName = null;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_activity_main);
		list = (ListView) findViewById(R.id.list);
		autoText = (AutoCompleteTextView) findViewById(R.id.autoSearch);
		imgbt = (ImageButton) findViewById(R.id.searchMusic);


		imgbt.setOnClickListener(this);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				try {

					jsonList = parseJson(jsons.get(position));
					Log.e("sgf", "歌曲的json数据位置：  " + position);
				} catch (Exception e) {
					Log.e("sgf", "onItemClick进入异常捕获");
					e.printStackTrace();
				}
				if (!("".equals(jsonList) || jsonList == null || jsonList
						.get("songLink") == null)) {
					String songLink = jsonList.get("songLink");
					String songName = jsonList.get("songName");
					String singer = jsonList.get("artistName");
					// Intent intent = new Intent("net.execise.download");

					Intent intent = new Intent(DownloadMainActivity.this,
							DownloadActivity.class);
					intent.putExtra("url", songLink);
					intent.putExtra("songName", songName);
					intent.putExtra("singer", singer);
					startActivity(intent);

					// sendBroadcast(intent);
				} else {
					Toast.makeText(DownloadMainActivity.this, "sorry,找不到歌曲",
							Toast.LENGTH_LONG).show();

				}

			}
		});

	}

	public Map<String, String> parseJson(String str) throws Exception {

		Map<String, String> map = new HashMap<String, String>();
		if ("".equals(str) || str == null) {
			Log.e("sgf", "通过歌曲id找不到歌曲的下载地址的json数据");
			return null;
		} else {
			try {
				JSONObject allData = new JSONObject(str);
				Log.e("sgf", "allData的数据:" + allData.toString());
				JSONObject data = allData.getJSONObject("data");
				Log.e("sgf", "data的数据:" + data.toString());
				JSONArray songlist = data.getJSONArray("songList");
				JSONObject music = songlist.getJSONObject(0);
				Log.e("sgf", "music的数据:" + music.toString());

				String songLink = music.getString("songLink");
				Log.e("sgf", "歌曲地址原始信息： " + songLink);
				// int n_pos = songLink.indexOf("&");
				// songLink = songLink.substring(0, n_pos);

				map.put("songLink", songLink);
				map.put("songName", music.getString("songName"));
				map.put("artistName", music.getString("artistName"));
				Log.e("sgf", "MainActivity parseJson中獲得的歌曲下载地址： " + songLink);

			} catch (Exception e) {
				// TODO: handle exception
				Log.e("sgf", "parseJson(String str)中有问题");
			}

			return map;
		}
	}

	class JSONFOR_IDThread extends Thread {
		public void run() {

			try {
				Json_ForId = getFromUrl(url);
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
	}

	class JSONThread extends Thread {
		public void run() {

			try {
				for (int i = 0; i < songInfo.size(); i++) {
					jsons.add(getFromUrl(songInfo.get(i)));
					Log.e("sgf", "每首歌曲的json数据：  " + i);
				}

			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	public String getFromUrl(String url) {
		String info = null;

		try {
			MyApplication app = (MyApplication) this.getApplication();
			HttpClient httpClient = app.getHttpClient();
			HttpClientParams.setCookiePolicy(httpClient.getParams(),
					CookiePolicy.BROWSER_COMPATIBILITY);
			HttpPost post = new HttpPost(url);
			HttpResponse response = httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			info = EntityUtils.toString(entity, "utf-8");
		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return info;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		listMusic.clear();
		song_ids.clear();
		jsons.clear();
		songInfo.clear();
		musicName = autoText.getText().toString();
		try {
			musicName = URLEncoder.encode(musicName, "utf-8");
		} catch (UnsupportedEncodingException e1) {

			e1.printStackTrace();
		}
		url = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=webapp_music"
				+ "&method=baidu.ting.search.catalogSug"
				+ "&format=json&callback=&query="
				+ musicName
				+ "&_=1413017198449";
		new JSONFOR_IDThread().start();
		try {
			listInfo = JsonInfoService.getInfos(Json_ForId);
			for (MusicInfo music : listInfo) {
				Map<String, String> map = new HashMap<String, String>();
				if (music.getSinger().equals("纯音乐")) {
					continue;
				}
				map.put("singer", music.getSinger());
				map.put("song", music.getSong());
				listMusic.add(map);
				song_ids.add(music.getSong_id());
				songInfo.add("http://ting.baidu.com/data/music/links?songIds="
						+ music.getSong_id());

			}
			new JSONThread().start();
			simpleAdapter = new SimpleAdapter(this, this.listMusic,
					R.layout.info, new String[] { "singer", "song" },
					new int[] { R.id.singer, R.id.song });
			list.setAdapter(simpleAdapter);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
