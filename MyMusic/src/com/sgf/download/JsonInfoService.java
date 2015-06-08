package com.sgf.download;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class JsonInfoService {

	public static List<MusicInfo> getInfos(String str) throws Exception{
		
		List<MusicInfo> infos=new ArrayList<MusicInfo>();
		JSONObject json=new JSONObject(str);
		JSONArray song=json.getJSONArray("song");
		for(int i=0;i<song.length();i++){
			JSONObject music=song.getJSONObject(i);
			MusicInfo Music=new MusicInfo(music.getString("songname"), music.getString("songid"), music.getString("artistname"));
			infos.add(Music);
		}
		return infos;
	}
}
