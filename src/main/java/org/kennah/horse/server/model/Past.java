package org.kennah.horse.server.model;

import java.io.Serializable;

public class Past implements Serializable{

	private static final long serialVersionUID = 1L;
	private String url;
	private String date;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "\n                            Past [date=" + date  + " url=" + url + "]";
	}
}
