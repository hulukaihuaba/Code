package com.sgf.model;

import java.io.Serializable;

public class SongList implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	int size;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public SongList(String name, int size) {
		this.name = name;
		this.size = size;
	}
	public SongList() {
	}
	
	
//	public int getSize() {
//		if (!musicList.isEmpty()) {
//			return musicList.size();
//		} else {
//			return 0;
//		}
//	}

//	public SongList() {
//		musicList = new ArrayList<Music>();
//	}

//	public SongList(String name, List<Music> musicList) {
//		super();
//		this.name = name;
//		this.musicList = musicList;
//	}

//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public List<Music> getMusicList() {
//		return musicList;
//	}
//
//	public void setMusicList(List<Music> musicList) {
//		this.musicList = musicList;
//	}

}
