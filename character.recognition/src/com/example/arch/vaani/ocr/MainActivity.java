package com.example.arch.vaani.ocr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;


public class MainActivity extends Activity {
	
	public Uri fileUri;
	ImageView showImg,camera,gallery;
	public static File fileName;
	private String selectedImagePath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		camera = (ImageView)findViewById(R.id.viewImage);
		gallery=(ImageView)findViewById(R.id.viewImage1);
	    camera.setOnClickListener(new OnClickListener() {
	    	@Override
	        public void onClick(View v) { open();
	        }
	    });
	    
	    gallery.setOnClickListener(new View.OnClickListener() {
	    	/** Open Gallery */
	    	@Override
	    	public void onClick(View v) { 
	    	Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	    	startActivityForResult(Intent.createChooser(intent,"Complete action using"), 2);
	    	}
	    	
	    });
	}
	/** Invoke inbuilt camera app*/
	public void open(){
	      Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	      fileUri = getOutputImageFileUri(); 
		  intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileUri);
		  if (intent.resolveActivity(getPackageManager()) != null) {
		  	startActivityForResult(intent, 1);
		  }
		
	}
	private static Uri getOutputImageFileUri(){
		fileName=getOutputImageFile();
		return Uri.fromFile(fileName);
	}
	/** Create a File for saving the image */
	private static File getOutputImageFile(){
	    File imageStorageDir = new File(Environment.getExternalStorageDirectory(),"Vaani");
	    if (!imageStorageDir.exists())
        {
            imageStorageDir.mkdirs();// Create the storage directory if it does not exist
        }
	    String imgName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File imageFile;
	    imageFile = new File(imageStorageDir.getPath() + File.separator + "IMG_"+ imgName +".png");
	    return imageFile;
	}
	
	@Override
	/** Goto SecondActivity */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	     super.onActivityResult(requestCode, resultCode, data);
		 if (requestCode==1 && resultCode == RESULT_OK) { 
			 if(null == data) {
	               	Intent first = new Intent(this, SecondActivity.class);
	               	first.putExtra("name", fileName.toString() );
                    startActivity(first);
              }
		 }
		 else if(requestCode==2 && resultCode == RESULT_OK) {
			 if(null!=data){
				 Uri selectedImageUri = data.getData();
	             selectedImagePath = getPath(selectedImageUri);
	             Intent first = new Intent(this, SecondActivity.class);
				 first.putExtra("name", selectedImagePath);
				 startActivity(first);
			 }
		 }
	}
	/** Get ImageUri */
	public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        return cursor.getString(column_index);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
}
