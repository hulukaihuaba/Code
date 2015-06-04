package com.sgf.helper;

import java.util.ArrayList;
import java.util.List;


import com.sgf.model.SongList;

public class SonglistDB {

	public static List<SongList> songlists = new ArrayList<SongList>();

	public static List<SongList> initFirst() {
		SongList obj = new SongList();
		obj.setName("我最喜欢");
//		obj.g
		songlists.add(obj);
		return songlists;
	}
	
	public static List<SongList> initfrom_db(){
		
		return songlists;
	}
}
