package com.example.jsonparser.utils;

import com.example.jsonparser.R;
import com.example.jsonparser.models.Flickr;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FlickrAdapter extends BaseAdapter {

	Activity activity;
	Flickr flickr;
	LayoutInflater inflater;
	
	public FlickrAdapter(Activity activity, Flickr flickr){
		this.activity = activity;
		this.flickr = flickr;
		
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return flickr.size();
	}

	@Override
	public Object getItem(int position) {
		
		return flickr.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	class ViewHolder{
		ImageView imageView;
		TextView textViewTitile;
		TextView textViewLink;
		TextView textViewPublished;
		TextView textViewAuther;
		TextView textViewTags;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = inflater.inflate(R.layout.flickr_layout, null);
		}
		
		ViewHolder holder = new ViewHolder();
		holder.imageView = (ImageView)convertView.findViewById(R.id.imageView1);
		holder.textViewTitile = (TextView)convertView.findViewById(R.id.textViewImageTitle);
		holder.textViewLink = (TextView)convertView.findViewById(R.id.textViewImageLink);
		holder.textViewPublished = (TextView)convertView.findViewById(R.id.textViewImagePublished);
		holder.textViewAuther = (TextView)convertView.findViewById(R.id.textViewImageAuther);
		holder.textViewTags = (TextView)convertView.findViewById(R.id.textViewImageTag);
		
		holder.textViewTitile.setText(flickr.get(position).getTitle());
		holder.textViewLink.setText(flickr.get(position).getLink());
		holder.textViewPublished.setText(flickr.get(position).getPublished());
		holder.textViewAuther.setText(flickr.get(position).getAuthor());
		holder.textViewTags.setText(flickr.get(position).getTags());
		
		Picasso.with(activity).load(flickr.get(position).getMedia()).resize(300, 300).into(holder.imageView);
		return convertView;
	}

}
