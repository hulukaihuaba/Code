package com.sgf.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sgf.model.SongList;
import com.sgf.mymusic.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SongListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<SongList> songlists;
	
	public SongListAdapter(Context context){
		this.mInflater=LayoutInflater.from(context);
		songlists=new ArrayList<SongList>();
		SongList obj=new SongList();
		obj.setName("ÎÒ×î°®Ìý");
		songlists.add(obj);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return songlists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return songlists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view;
		ViewHolder viewHolder;
		SongList songList=(SongList) getItem(position);
		if (convertView == null) {
			view = mInflater.inflate(R.layout.songlist_item, null);
			viewHolder = new ViewHolder();
			viewHolder.songListTitle=(TextView) view.findViewById(R.id.list_name);
			viewHolder.size=(TextView) view.findViewById(R.id.size);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		Log.e("sgf", songList.getName());
		Log.e("sgf", viewHolder.toString());
		viewHolder.songListTitle.setText(songList.getName());
		viewHolder.size.setText("10");
		
		return view;
	}

	class ViewHolder {
		TextView songListTitle;
		TextView size;
	}
}
