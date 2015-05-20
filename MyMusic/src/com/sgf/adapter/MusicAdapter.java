package com.sgf.adapter;

import java.util.List;

import com.sgf.model.Music;
import com.sgf.mymusic.R;
import com.sgf.helper.MediaUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textservice.TextInfo;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MusicAdapter extends ArrayAdapter<Music> {

	private int resourceId;

	public MusicAdapter(Context context, int textViewResourceId,
			List<Music> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Music music = getItem(position);
		View view;
		ViewHolder viewHolder;

		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.musicTitle = (TextView) view.findViewById(R.id.relativelayout);
			viewHolder.duration = (TextView) view.findViewById(R.id.duration);
			viewHolder.artist=(TextView)view.findViewById(R.id.artist);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.musicTitle.setText(music.getTitle());
		viewHolder.duration.setText(MediaUtil.formatTime(music.getDuration()));
		viewHolder.artist.setText(music.getArtist());
		return view;
	}

	class ViewHolder {
		TextView musicTitle;
		TextView duration;
		TextView artist;
	}

}
