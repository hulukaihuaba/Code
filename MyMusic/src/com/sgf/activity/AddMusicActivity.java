package com.sgf.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.sgf.helper.DBOpenHelper;
import com.sgf.helper.MediaUtil;
import com.sgf.model.Music;
import com.sgf.model.SongList;
import com.sgf.mymusic.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AddMusicActivity extends Activity implements
		android.view.View.OnClickListener {

	ListView list;
	Button button;
	ArrayList<Music> array;
	// ArrayList<SongList> songlists = new ArrayList<SongList>();
	ArrayList<SongList> songlists;
	SimpleAdapter adapter;
	ArrayList<Boolean> checkedItem = new ArrayList<Boolean>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.music_select);
		list = (ListView) findViewById(R.id.listView);
		button = (Button) findViewById(R.id.result);
		button.setOnClickListener(this);

		array = (ArrayList<Music>) MediaUtil.musicList;
		songlists = (ArrayList<SongList>) getIntent().getSerializableExtra(
				"songlists");
		Log.e("sgf", "AddMusicActivity中的onCreate播放列表名：  "
				+ songlists.get(0).getName());

		for (int i = 0; i < array.size(); i++) {
			checkedItem.add(i, false);
		}
		CheckAdapter chadapter = new CheckAdapter(this);
		list.setAdapter(chadapter);
	}

	public void onClick(View v) {
		String s = "You have choosed ";

		DBOpenHelper dbOpenHelper = new DBOpenHelper(v.getContext());
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

		// 获得一个歌单的内容
		String name = getIntent().getStringExtra("songlistTitle");
		int size = 0;
		for (int i = 0; i < array.size(); i++) {

			if (checkedItem.get(i)) {
				s = s + "," + array.get(i);
				Music obj = array.get(i);
				db.execSQL(
						"insert into music (M_ID,id,artist,size,url,title,duration) values(?,?,?,?,?,?,?);",
						new String[] { null, String.valueOf(obj.getId()),
								obj.getArtist(), String.valueOf(obj.getSize()),
								obj.getUrl(), obj.getTitle(),
								String.valueOf(obj.getDuration()) });
				
				String queryM_ID="select music.[M_ID]  from music where music.[artist]=?;";
				Cursor result = db.rawQuery(queryM_ID,new String[]{ obj.getArtist()});
				if(result.moveToFirst()){
					Log.e("sgf", "result中的记录数："+String.valueOf(result.getCount()));
					
					db.execSQL(
							"insert into section (id,music_id,l_name) values(?,?,?);",
							new String[] { null, result.getString(0), name });
					size++;
				}
			}
		}
		db.execSQL("insert into songlist (list_name,length) values(?,?); ",
				new String[] { name, String.valueOf(size) });
		db.close();

		SongList songlist = new SongList(name, size);
		Log.e("sgf", "新添加的播放列表名：" + songlist.getName());
		Log.e("sgf", "里面的歌曲数量 ：" + songlist.getSize());
		songlists.add(songlist);
		Log.e("sgf", "AddMusicActivity中新加的播放列表名：  "
				+ songlists.get(1).getName());
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

		// Intent broadcast=new Intent("com.sgf.adapterupdate");
		Intent intent = new Intent(v.getContext(), MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("SONGLIST_UPDATE", (Serializable) songlists);
		intent.putExtras(bundle);
		Boolean flag = true;
		intent.putExtra("flag", flag);
		// v.getContext().sendBroadcast(broadcast);

		startActivity(intent);
		// finish();
	}

	class CheckAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public CheckAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return array.size();
		}

		public Object getItem(int position) {
			return array.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final int p = position;
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.music_select_item,
						null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.checked = (CheckBox) convertView
						.findViewById(R.id.select);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.img.setBackgroundResource(R.drawable.music);
			final String s = array.get(position).getTitle();
			holder.name.setText(s);
			holder.checked
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								// update the status of checkbox to checked
								checkedItem.set(p, true);

							} else {
								// update the status of checkbox to unchecked
								checkedItem.set(p, false);
							}

						}

					});
			return convertView;
		}
	}

	public final class ViewHolder {
		public ImageView img;
		public TextView name;
		public CheckBox checked;
	}

}
