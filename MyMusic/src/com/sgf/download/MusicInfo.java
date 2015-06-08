package com.sgf.download;

public class MusicInfo {
	private String song;
	private String song_id;
	private String singer;
	public String getSong() {
		return song;
	}
	public void setSong(String song) {
		this.song = song;
	}
	public String getSong_id() {
		return song_id;
	}
	public void setSong_id(String song_id) {
		this.song_id = song_id;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	public MusicInfo(String song, String song_id, String singer) {
		super();
		this.song = song;
		this.song_id = song_id;
		this.singer = singer;
	}
	public MusicInfo() {
	}

	
	
}
