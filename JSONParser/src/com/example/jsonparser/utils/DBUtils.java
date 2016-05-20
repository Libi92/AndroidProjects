package com.example.jsonparser.utils;

import com.example.jsonparser.models.Flickr;
import com.example.jsonparser.models.FlickrItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBUtils extends SQLiteOpenHelper {
	
	final static String DATABASE = "FLICKR_DATABSE";
	final String TABLE = "IMAGES";
	
	final String TAG = "TAG";
	final String TITLE = "TITLE";
	final String LINK = "LINK";
	final String MEDIA = "MEDIA";
	final String PUBLISHED = "PUBLISHED";
	final String AUTHER = "AUTHER";
	final String TAGS = "TAGS";

	public DBUtils(Context context){
		this(context, DATABASE, null, 1);
	}
	private DBUtils(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + TABLE + "" +
				"(ID INTEGER PRIMARY KEY, " + TAG + " VARCHAR, " + TITLE + " VARCHAR," + LINK + " VARCHAR," +
						"" + MEDIA + " VARCHAR," + PUBLISHED + " VARCHAR," + AUTHER + " VARCHAR," + TAGS + " VARCHAR)";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS " + TABLE;
		db.execSQL(sql);
		onCreate(db);
	}
	
	public void save(String tag, Flickr flickr){
		for (FlickrItem flickrItem : flickr) {
			addData(tag, flickrItem.getTitle(), flickrItem.getLink(), flickrItem.getMedia(), flickrItem.getPublished(), flickrItem.getAuthor(), flickrItem.getTags());
		}
	}
	
	public void addData(String tag, String title, String link, String media, String published, String auther, String tags){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TAG, tag);
		values.put(TITLE, title);
		values.put(LINK, link);
		values.put(MEDIA, media);
		values.put(PUBLISHED, published);
		values.put(AUTHER, auther);
		values.put(TAGS, tags);
		
		db.insert(TABLE, null, values);
	}
	
	public Flickr getData(String tag){
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT * FROM " + TABLE + " WHERE " + TAG + " = '" + tag + "'";
		Cursor cs = db.rawQuery(sql, null);
		
		cs.moveToFirst();
		if(!cs.isAfterLast()){
			Flickr flickr = new Flickr();
			flickr.setTitle(tag);
			flickr.setLink("---");
			
			while (!cs.isAfterLast()) {
				FlickrItem item = new FlickrItem();
				item.setTitle(cs.getString(cs.getColumnIndex(TITLE)));
				item.setLink(cs.getString(cs.getColumnIndex(LINK)));
				item.setMedia(cs.getString(cs.getColumnIndex(MEDIA)));
				item.setPublished(cs.getString(cs.getColumnIndex(PUBLISHED)));
				item.setAuthor(cs.getString(cs.getColumnIndex(AUTHER)));
				item.setTags(cs.getString(cs.getColumnIndex(TAGS)));
				
				flickr.add(item);
				cs.moveToNext();
			}
			
			return flickr;
		}
		
		return null;
	}

}
