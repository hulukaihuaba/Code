package com.sgf.model;

import java.util.List;

public class SongList {

	private String name;
	List<Music> musicList;
	
	public SongList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SongList(String name, List<Music> musicList) {
		super();
		this.name = name;
		this.musicList = musicList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Music> getMusicList() {
		return musicList;
	}

	public void setMusicList(List<Music> musicList) {
		this.musicList = musicList;
	}
	
	
}
