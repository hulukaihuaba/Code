package com.sgf.adapter;

import java.util.List;

import com.sgf.model.Music;
import com.sgf.musicplayer.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MusicAdapter extends ArrayAdapter<Music> {
	private int resourceId;
	public MusicAdapter(Context context, int textViewResourceId,
			List<Music> objects) {
		super(context, textViewResourceId, objects);
		resourceId=textViewResourceId;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Music music=getItem(position);
		View view=LayoutInflater.from(getContext()).inflate(resourceId, null);
		TextView musicTitle=(TextView)view.findViewById(R.id.title);
		TextView duration=(TextView)view.findViewById(R.id.duration);
		musicTitle.setText(music.getTitle());
		duration.setText(Long.toString(music.getDuration()));
	
		return view;
	}

}
