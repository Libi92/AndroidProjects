package com.example.arch.vaani.ocr;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


public class LanguageActivity extends Activity {

		public static File imgFile;
		public Uri fileUri;
		ImageView showImg;
		public static File fileName;
		String value;
		Button button;
		@Override
		
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_lang);
			String myRef = this.getIntent().getStringExtra("name");
		    imgFile = new  File(myRef);
		   
		    /** Display Image */
	        if(imgFile.exists()){
	        	Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
	        	BitmapFactory.Options options = new BitmapFactory.Options();
	        	options.inSampleSize = 4;
	       		ImageView showImg = (ImageView) findViewById(R.id.view_photo);
	        	showImg.setImageBitmap(myBitmap);
	        	}
	        /** Implement submit button  **/
	        button = (Button)findViewById(R.id.button4);
	        button.setOnClickListener(new OnClickListener() {
	        	@Override
		        public void onClick(View v) { gotoFinalActivity();}});
		}
		/** Goto FinalActivity on Submit */
		 public void gotoFinalActivity(){
			 Intent first = new Intent(this, FinalActivity.class);
			 first.putExtra("name",imgFile.toString());
			 first.putExtra("choice", value);
			 startActivity(first);
		 }
}
