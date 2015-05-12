package com.sgf.model;

public class Music {
	public String title;
	public long duration;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public Music(String title, long duration) {
		super();
		this.title = title;
		this.duration = duration;
	}
	public Music() {
		super();
		// TODO Auto-generated constructor stub
	}


}
