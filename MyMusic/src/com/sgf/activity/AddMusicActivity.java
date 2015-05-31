package com.sgf.activity;

import java.util.ArrayList;

import com.sgf.helper.DBAdapter;
import com.sgf.model.Music;
import com.sgf.mymusic.R;

import android.app.Activity;
import android.content.Context;
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
	SimpleAdapter adapter;
	ArrayList<Boolean> checkedItem = new ArrayList<Boolean>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.music_select);
		list = (ListView) findViewById(R.id.listView);
		button = (Button) findViewById(R.id.result);
		button.setOnClickListener(this);
		// get music names from other activity
		array = (ArrayList<Music>) getIntent()
				.getSerializableExtra("musiclist");

		// init the check box state to unchecked
		for (int i = 0; i < array.size(); i++) {
			checkedItem.add(i, false);
		}
		CheckAdapter chadapter = new CheckAdapter(this);
		list.setAdapter(chadapter);
	}

	public void onClick(View v) {
		String s = "You have choosed ";
		for (int i = 0; i < array.size(); i++) {
			if (checkedItem.get(i)) {
				s = s + "," + array.get(i);
			}
		}
		
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
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
