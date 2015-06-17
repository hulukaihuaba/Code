package com.sgf.activity;

import java.io.IOException;
import java.io.Serializable;
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

import com.sgf.adapter.MusicAdapter;
import com.sgf.adapter.SongListAdapter;
import com.sgf.download.DownloadActivity;
import com.sgf.download.JsonInfoService;
import com.sgf.download.MusicInfo;
import com.sgf.download.MyApplication;
import com.sgf.helper.DBOpenHelper;
import com.sgf.helper.MediaUtil;
import com.sgf.model.Music;
import com.sgf.model.SongList;
import com.sgf.mymusic.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	ViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		Log.e("sgf", "MainActivity onCreate");

		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		final ActionBar actionBar = getActionBar();

		actionBar.setHomeButtonEnabled(false);

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener
		// for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.e("sgf", " MainActivity onStart()");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("sgf", " MainActivity onResume()");
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		String str[] = { "音乐", "播放列表", "在线" };

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				return new LaunchpadSectionFragment();
			case 1:
				return new DummySectionFragment1();
			default:
				return new DummySectionFragment2();
			}
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {

			return str[position];
		}
	}

	public static class LaunchpadSectionFragment extends Fragment {

		List<Music> musicList;

		@Override
		public void onCreate(Bundle savedInstanceState) {

			super.onCreate(savedInstanceState);
			musicList = MediaUtil.getMusicList(getActivity());
			Log.e("sgf", "LaunchpadSectionFragment onCreate");
		}

		MusicAdapter musicAdapter;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			// Log.e("sgf", "LaunchpadSectionFragment onCreateView");
			final View rootView = inflater.inflate(
					R.layout.fragment_section_listview, container, false);

			musicAdapter = new MusicAdapter(rootView.getContext(),
					R.layout.music_item, musicList);
			ListView listView = (ListView) rootView
					.findViewById(R.id.musicList);
			listView.setAdapter(musicAdapter);

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Music music = musicList.get(position);

					Intent intent = new Intent(rootView.getContext(),
							PlayMusicActivity.class);

					intent.putExtra("musicArtist", music.getArtist());
					intent.putExtra("url", music.getUrl());
					intent.putExtra("title", music.getTitle());
					intent.putExtra("position", position);
					startActivity(intent);
				}
			});
			return rootView;
		}

		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();

			IntentFilter intentfilter = new IntentFilter(
					Intent.ACTION_MEDIA_SCANNER_STARTED);

			intentfilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);

			intentfilter.addDataScheme("file");

			Object scanSdReceiver = new ScanSdReceiver();

			getActivity().registerReceiver((BroadcastReceiver) scanSdReceiver,
					intentfilter);

		}

		public class ScanSdReceiver extends BroadcastReceiver {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String action = intent.getAction();
		        if(Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)){
		            Log.e("sgf", "开始扫描了。。。");
		              
		        }else if(Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)){
		        	 Log.e("sgf", "广播成功！！！");
		        	 Log.e("sgf", "有值吗？"+getActivity().toString());
		        	 
		        	 musicList.clear();
		        	 
					 List<Music> newmusicList=MediaUtil.getMusicList(getActivity());
					 Log.e("sgf", "读取结果是否为空："+newmusicList.isEmpty());
					 Log.e("sgf", "newmusicList的大小："+String.valueOf(newmusicList.size()));
					

					 musicList=newmusicList;
					 Log.e("sgf", "newmusicList的大小222："+String.valueOf(newmusicList.size()));
					 Log.e("sgf", "musicList的大小："+String.valueOf(musicList.size()));
//					 musicList.addAll(newmusicList);
//					 Music music=new Music(11, "sdf", 12, "222", "45", 123);
//					 musicList.add(music);
					 musicAdapter.notifyDataSetChanged();
		        }   
		        Log.e("sgf", "現在的歌曲數目："+String.valueOf(musicList.size()));
			}
		}

	}

	public static class DummySectionFragment1 extends Fragment {

		public static List<SongList> songlists = new ArrayList<SongList>();
		private Activity mActivity;

		@Override
		public void onAttach(Activity activity) {

			super.onAttach(activity);

			Log.e("sgf", "DummySectionFragment1 的 onAttach(Activity activity)");
			mActivity = activity;
			DBOpenHelper dbOpenHelper = new DBOpenHelper(activity);
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

			String query = "select * from songlist;";

			Cursor result = db.rawQuery(query, null);

			if (result.getCount() == 0) {
				Log.e("sgf", "第一次读取数据库，此时没有播放列表");
				SongList songlist = new SongList("我最喜欢", 0);
				songlists.add(songlist);
				db.execSQL(
						"insert into songlist (list_name,length) values(?,?); ",
						new String[] { songlist.getName(),
								String.valueOf(songlist.getSize()) });

			} else {
				Log.e("sgf", "从数据库中读取播放列表的信息");
				while (result.moveToNext()) {
					SongList songlist = new SongList(result.getString(0),
							Integer.parseInt(result.getString(1)));
					songlists.add(songlist);
				}
			}
			db.close();

		}

		SongListAdapter adapter;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			Log.e("sgf", "DummySectionFragment1  的  onCreateView()");
			View rootView = inflater.inflate(
					R.layout.fragment_section_songlist, container, false);

			adapter = new SongListAdapter(rootView.getContext(), songlists);

			ListView listView = (ListView) rootView.findViewById(R.id.songlist);

			LinearLayout footView = (LinearLayout) inflater.inflate(
					R.layout.songlist_footer, null);

			listView.addFooterView(footView);
			listView.setAdapter(adapter);
			footView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					AlertDialog.Builder builder = new AlertDialog.Builder(v
							.getContext());
					builder.setTitle("播放列表名称");
					LayoutInflater layoutInflater = LayoutInflater.from(v
							.getContext());
					final View view = layoutInflater.inflate(
							R.layout.custom_dialoglayout, null);
					builder.setView(view);

					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									EditText songlistTitle = (EditText) view
											.findViewById(R.id.songlistTitle);
									String listTitle = songlistTitle.getText()
											.toString();

									Intent intent = new Intent(view
											.getContext(),
											AddMusicActivity.class);
									intent.putExtra("songlistTitle", listTitle);
									Bundle bundle = new Bundle();
									bundle.putSerializable("songlists",
											(Serializable) songlists);
									intent.putExtras(bundle);
									startActivity(intent);
								}
							});
					builder.setNeutralButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});

					AlertDialog dialog = builder.create();
					dialog.show();
				}
			});

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Log.e("sgf",
							"listView.setOnItemClickListener  onItemClick ");

					SongList songlist = songlists.get(position);
					if (songlist.getSize() > 0) {

						DBOpenHelper dbOpenHelper = new DBOpenHelper(view
								.getContext());
						SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
						String query = "select music.[id],music.[artist],music.[size],music.[url],music.[title],"
								+ "music.[duration] from music,section where music.M_ID=section.[music_id] and section.[l_name]=?;";
						Cursor result = db.rawQuery(query,
								new String[] { songlist.getName() });
						List<Music> songlist_music = new ArrayList<Music>();

						while (result.moveToNext()) {
							Music music = new Music(Integer.valueOf(result
									.getString(0)), result.getString(1),
									Integer.valueOf(result.getString(2)),
									result.getString(3), result.getString(4),
									Integer.valueOf(result.getString(5)));
							songlist_music.add(music);
						}

						Intent intent = new Intent(view.getContext(),
								SongListsItemActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("songlist_music",
								(Serializable) songlist_music);
						intent.putExtras(bundle);
						startActivity(intent);
					} else {
						Intent intent = new Intent(view.getContext(),
								AddMusicActivity.class);
						intent.putExtra("songlist_isexit", true);
						intent.putExtra("songlistTitle", songlist.getName());
						Bundle bundle = new Bundle();
						bundle.putSerializable("songlists",
								(Serializable) songlists);
						intent.putExtras(bundle);
						startActivity(intent);
					}

				}
			});

			return rootView;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			Log.e("sgf", "播放列表 fragment的 onResume()");
			if (mActivity.getIntent().getBooleanExtra("flag", false)) {

				List<SongList> newsonglists = (List<SongList>) mActivity
						.getIntent().getSerializableExtra("SONGLIST_UPDATE");
				songlists.clear();
				songlists.addAll(newsonglists);
				adapter.notifyDataSetChanged();

				Log.e("sgf", "更新歌单列表 success");
			} else {
				Log.e("sgf", "首次进入歌单列表的onResume()");
			}
		}

	}

	public static class DummySectionFragment2 extends Fragment {

		private String Json_ForId;
		private List<MusicInfo> listInfo;
		private ListView list;
		private AutoCompleteTextView autoText;
		private ImageButton imgbt;
		private SimpleAdapter simpleAdapter;

		private ArrayList<String> song_ids = new ArrayList<String>();
		private ArrayList<String> songInfo = new ArrayList<String>();
		private ArrayList<String> jsons = new ArrayList<String>();
		final List<Map<String, String>> listMusic = new ArrayList<Map<String, String>>();
		private Map<String, String> jsonList = new HashMap<String, String>();
		private String musicName = null;
		private String url;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(
					R.layout.download_activity_main, container, false);

			super.onCreate(savedInstanceState);
			list = (ListView) rootView.findViewById(R.id.list);
			autoText = (AutoCompleteTextView) rootView
					.findViewById(R.id.autoSearch);
			imgbt = (ImageButton) rootView.findViewById(R.id.searchMusic);

			imgbt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

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
							// if (music.getSinger().equals("纯音乐")) {
							// continue;
							// }
							map.put("singer", music.getSinger());
							map.put("song", music.getSong());
							listMusic.add(map);
							song_ids.add(music.getSong_id());
							songInfo.add("http://ting.baidu.com/data/music/links?songIds="
									+ music.getSong_id());

						}
						new JSONThread().start();
						simpleAdapter = new SimpleAdapter(v.getContext(),
								listMusic, R.layout.info, new String[] {
										"singer", "song" }, new int[] {
										R.id.singer, R.id.song });
						list.setAdapter(simpleAdapter);
					} catch (Exception e) {

						e.printStackTrace();
					}

				}
			});
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					try {

						jsonList = parseJson(jsons.get(position));

					} catch (Exception e) {
						Log.e("sgf", "onItemClick进入异常捕获");
						e.printStackTrace();
					}
					if (!("".equals(jsonList) || jsonList == null || jsonList
							.get("songLink") == null)) {
						String songLink = jsonList.get("songLink");
						String songName = jsonList.get("songName");
						String singer = jsonList.get("artistName");

						Intent intent = new Intent(rootView.getContext(),
								DownloadActivity.class);
						intent.putExtra("url", songLink);
						intent.putExtra("songName", songName);
						intent.putExtra("singer", singer);
						startActivity(intent);

					} else {
						Toast.makeText(rootView.getContext(), "sorry,找不到歌曲",
								Toast.LENGTH_LONG).show();

					}

				}
			});

			return rootView;
		}

		public Map<String, String> parseJson(String str) throws Exception {

			Map<String, String> map = new HashMap<String, String>();
			if ("".equals(str) || str == null) {
				Log.e("sgf", "通过歌曲id找不到歌曲的下载地址的json数据");
				return null;
			} else {
				try {
					JSONObject allData = new JSONObject(str);
					// Log.e("sgf", "allData的数据:" + allData.toString());
					JSONObject data = allData.getJSONObject("data");
					// Log.e("sgf", "data的数据:" + data.toString());
					JSONArray songlist = data.getJSONArray("songList");
					JSONObject music = songlist.getJSONObject(0);
					Log.e("sgf", "music的数据:" + music.toString());

					String songLink = music.getString("songLink");
					Log.e("sgf", "歌曲地址原始信息： " + songLink);

					map.put("songLink", songLink);
					map.put("songName", music.getString("songName"));
					map.put("artistName", music.getString("artistName"));

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
				MyApplication app = (MyApplication) getActivity()
						.getApplication();
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

	}

}
