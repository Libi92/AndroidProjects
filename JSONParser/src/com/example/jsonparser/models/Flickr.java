package com.example.jsonparser.models;

import java.util.ArrayList;

public class Flickr extends ArrayList<FlickrItem> {
	String title;
	String link;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
}
