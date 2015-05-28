package com.sgf.model;

import java.io.Serializable;

public class Music implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String artist;
	private long size;
	private String url;
	private String title;
	private long duration;

	public Music(long id, String artist, long size, String url, String title,
			long duration) {
		super();
		this.id = id;
		this.artist = artist;
		this.size = size;
		this.url = url;
		this.title = title;
		this.duration = duration;
	}

	public Music() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

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

}
