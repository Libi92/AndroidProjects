package com.example.arch.vaani.ocr;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


public class SecondActivity extends Activity {
	ImageView showImg;
	final int PIC_CROP=2;
	Button button;
	Button button1;
	Button button2;
	public Uri picUri;
	Bitmap myBitmap;
	public static File imgFile;
	private String selectedImagePath;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_next);
		String myRef = this.getIntent().getStringExtra("name");
	    imgFile = new  File(myRef);
		
		
        /*Display Image*/
        if(imgFile.exists()){
        	myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        	ImageView showImg = (ImageView) findViewById(R.id.view_photo);
        	showImg.setImageBitmap(myBitmap);
        	}
        button = (Button)findViewById(R.id.button3);
        button1= (Button)findViewById(R.id.button4);
		button.setOnClickListener(new OnClickListener() {
		    	@Override
		        public void onClick(View v) { 
		    		
		    		File dir = new File(Environment.getExternalStorageDirectory()+"/download"); 
		    		if (dir.isDirectory()) {
		    		        String[] children = dir.list();
		    		        for (int i = 0; i < children.length; i++) {
		    		            new File(dir, children[i]).delete();
		    		        }
		    		 }


		    		picUri=Uri.fromFile(imgFile);
		        	//call the standard crop action intent (the user device may not support it)
		        	Intent cropIntent = new Intent("com.android.camera.action.CROP"); 
		        	    //indicate image type and Uri
		        	cropIntent.setDataAndType(picUri, "image/*");
		        	    //set crop properties
		        	cropIntent.putExtra("crop", "true");
		        	    //indicate aspect of desired crop
		        	cropIntent.putExtra("aspectX", 0);
		        	cropIntent.putExtra("aspectY", 0);
		        	    //indicate output X and Y
		        	cropIntent.putExtra("outputX", 256);
		        	cropIntent.putExtra("outputY", 256);
		        	cropIntent.putExtra("scale", true);
		        	//cropIntent.putExtra("return-data", true);
		        	    //retrieve data on return
		        	cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, true);
		        	    //start the activity - we handle returning in onActivityResult
		        	startActivityForResult(cropIntent, PIC_CROP);
		        }
		});
		button1.setOnClickListener(new View.OnClickListener() {
	    	@Override
	    	public void onClick(View v) { /*Open Gallery*/
	    	
	    		next();
	    	}
	    });
		
		
		
	}
	
	
	/** Goto LanguageActivity on Next */
	public void next()
	{ 
		Uri temp = getImageUri(getApplicationContext(),myBitmap);
		selectedImagePath = getPath(temp);
		Intent next = new Intent(this, LanguageActivity.class);
		next.putExtra("name", selectedImagePath);
		startActivity(next);
	}
	/** GetPath of passed Bitmap */
	public Uri getImageUri(Context inContext, Bitmap inImage) {
		  ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		  inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		  String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		  return Uri.parse(path);
		}
	
	/** Goto language Activity on Crop */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // TODO Auto-generated method stub
    super.onActivityResult(requestCode, resultCode, data);
	if (requestCode == PIC_CROP && resultCode == RESULT_OK) {
		if(data!=null){
			Uri selectedImageUri = data.getData();
			selectedImagePath = getPath(selectedImageUri);
       		Intent first = new Intent(this, LanguageActivity.class);
			first.putExtra("name", selectedImagePath);
			startActivity(first);
       		}
	 }
	}
	public String getPath(Uri contentURI) {
		String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

}