package com.sgf.helper;

import java.util.ArrayList;
import java.util.List;


import com.sgf.model.SongList;

public class SonglistDB {

	public static List<SongList> songlists = new ArrayList<SongList>();

	public static List<SongList> init() {
		SongList obj = new SongList();
		obj.setName("ÎÒ×î°®Ìý");
		songlists.add(obj);
		return songlists;
	}
}
