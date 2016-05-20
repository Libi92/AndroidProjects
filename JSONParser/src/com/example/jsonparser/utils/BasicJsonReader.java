package com.example.jsonparser.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.jar.Attributes.Name;

import org.json.JSONObject;

import com.example.jsonparser.models.Flickr;
import com.example.jsonparser.models.FlickrItem;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

public class BasicJsonReader extends AsyncTask<String, String, Flickr> {
	
	URL url;
	final String TITLE = "title";
	final String LINK = "link";
	final String ITEMS = "items";
	final String MEDIA = "media";
	final String PUBLISHED = "published";
	final String AUTHER = "author";
	final String TAGS = "tags";
	
	private Context mContext;
	private ProgressDialog progressDialog;
	
	public BasicJsonReader(Context context, String tags){
		mContext = context;
		try {
			url = new URL("http://www.flickr.com/services/feeds/photos_public.gne?tags=" + tags + "&format=json");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(mContext);
		progressDialog.setTitle("Getting Content");
		progressDialog.setMessage("Please wait..!!");
		progressDialog.show();
		super.onPreExecute();
	}
	
	@Override
	protected Flickr doInBackground(String... params) {
		Flickr flickr = new Flickr();
		try {
			JsonReader reader = new JsonReader(new InputStreamReader(url.openStream()));
			reader.setLenient(true);
			String data = reader.nextString();
			reader.beginObject();
			
			while(reader.hasNext()){
				String tag = reader.nextName();
				Log.d("Json Tag", tag);
				
				if(tag.equals(TITLE)){
					flickr.setTitle(reader.nextString());
				}
				else if (tag.equals(LINK)) {
					flickr.setLink(reader.nextString());
				}
				else if (tag.equals(ITEMS)) {
					reader.beginArray();
					
					while (reader.hasNext()) {
						reader.beginObject();
						
						FlickrItem flickrItem = new FlickrItem();
						while (reader.hasNext()) {
							
							tag = reader.nextName();
							Log.d("Json Tag arr", tag);
							
							if(tag.equals(TITLE)){
								flickrItem.setTitle(reader.nextString());
							}
							else if(tag.equals(LINK)){
								flickrItem.setLink(reader.nextString());
							}
							else if(tag.equals(PUBLISHED)){
								flickrItem.setPublished(reader.nextString());
							}
							else if(tag.equals(AUTHER)){
								flickrItem.setAuthor(reader.nextString());
							}
							else if(tag.equals(MEDIA)){
								reader.beginObject();
								reader.nextName();
								flickrItem.setMedia(reader.nextString());
								reader.endObject();
							}
							else {
								reader.nextString();
							}
						}
						
						flickr.add(flickrItem);
						reader.endObject();
						
					}
					
					reader.endArray();
				}
				else {
					reader.nextString();
				}
				
			}
			reader.endObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("Data Size", flickr.size() + "");
		return flickr;
	}
	
	@Override
	protected void onPostExecute(Flickr result) {
		progressDialog.dismiss();
		super.onPostExecute(result);
	}
	
}
