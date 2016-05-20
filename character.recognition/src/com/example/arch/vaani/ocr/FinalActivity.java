package com.example.arch.vaani.ocr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;


public class FinalActivity extends Activity {
	TextView recognised;
	TextView translated;
	ImageView imageDisp;
	public static File imgFile;
	String recognizedText;
	String choice;
	
	
	public static String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Vaani";
	public static final String lang="mal";
	private static final String TAG = "FinalActivity.java";
	private static ArrayList<String> charList = new ArrayList<String>(0);
	 
	private static byte[][] Dict = new byte[][] {
		{ -32, -76, -102, -32, -76, -107, -32, -75, -115, -32, -76, -107, -32, -76, -86, -32, -75, -115, -32, -76, -86, -32, -76, -76, -32, -76, -96 },
		{ -32, -76, -107, -32, -75, -115, -32, -76, -92, -32, -76, -103, -32, -75, -115, -32, -76, -103 },
		{ -32, -75, -122, -32, -76, -92, -32, -76, -123 },
		{ -32, -75, -121, -32, -76, -82, -32, -75, -115, -32, -76, -86, -32, -76, -66, -32, -76, -89, -32, -75, -122, -32, -76, -86, -32, -75, -115, -32, -76, -86, -32, -76, -97, -32, -76, -77, -32, -76, -72, -32, -76, -77, -32, -76, -107 },
		{ -32, -76, -88, -32, -75, -115, -32, -76, -91, -32, -76, -65, -32, -76, -80, -32, -75, -128, -32, -76, -107, -32, -76, -80, -32, -76, -65, -32, -76, -102, -32, -75, -115, -32, -76, -102, -32, -76, -82, -32, -75, -125 },
		{ -32, -76, -80, -32, -76, -107, -32, -75, -126, -32, -76, -86, -32, -76, -65, -32, -76, -107, -32, -76, -80, -32, -76, -65, -32, -76, -102, -32, -75, -115, -32, -76, -102 },
		{ -32, -76, -102, -32, -76, -65, -32, -76, -92, -32, -76, -92 }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		charList.clear();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_final);
		String myRef = this.getIntent().getStringExtra("name");
		choice= this.getIntent().getStringExtra("choice");
	    imgFile = new  File(myRef);
	    String path1 =imgFile.getAbsolutePath();
	    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
	    imageDisp = (ImageView) findViewById(R.id.imageId);
	   
	   String[] paths = new String[] { DATA_PATH, DATA_PATH + "/tessdata/" };
		    for (String path : paths) {
		    	File dir = new File(path);
		    	if (!dir.exists()) {
		                dir.mkdirs();// Create the storage directory if it does not exist
		    	}
		    }
		    
		    if (!(new File(DATA_PATH + "/tessdata/" + lang + ".traineddata")).exists()) {
		    	AssetManager assetManager = getAssets();
		        String[] files; 
		        try {
	    	        files = assetManager.list("");
	    	    }
	    	catch (IOException e) {
	    	        Log.e("asset list", "Failed to get asset file list.", e);
	    	    }
	    	
		        try {
					files = assetManager.list("tessdata");
					 InputStream in = null;
					 OutputStream out = null;
					 
					 in = assetManager.open("tessdata/"+ lang + ".traineddata");
					 out = new FileOutputStream(DATA_PATH + "/tessdata/" + lang + ".traineddata");
					 copyFile(in, out);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					Log.e("write", "Failed to get asset file list.", e1);
				}
		    	
		    }
		   TessBaseAPI baseApi = new TessBaseAPI();
		   baseApi.init(DATA_PATH, lang);
		   baseApi.setImage(bitmap);
		   String recognizedText = baseApi.getUTF8Text();
		   recognizedText=correctText(recognizedText);

		   System.out.println("Recognized Text : "+recognizedText);
			
		   recognised=(TextView)findViewById(R.id.textView1);
   	       translated=(TextView)findViewById(R.id.textView2);
   	       recognised.setText(recognizedText);
   	       if(recognizedText.equals("ശരീരം")){
   	    	translated.setText("body");
   	       }
   	       else {
   	    	   translated.setText(readTxt(recognizedText));
   	       }
   	       imageDisp.setImageBitmap(bitmap);
   	 
   	
}

/** Function to correct vowel position */
public String correctText(String recognizedText) // swap vowels
{
	Context context = getApplicationContext();
	int duration = Toast.LENGTH_SHORT;
	int len,i,j,p = 0;
	String temp;
	StringBuilder builder;
	String text;
	String [] c = new String[1000];
	String [] a ={"േ", "െ", "ൈ", "ൊ", "ോ", "ൌ"};
	len= recognizedText.length();
    c = recognizedText.split("");
    for(i=0;i<(c.length-1);i++)
    {
    	for(j=0;j<a.length;j++)
        {
    		if(a[j].equals(c[i]))
    		{
    			p=1;
    			break;
    		}
		}
		if(p==1)
		{
		   temp = c[i];
		   c[i] = c[i+1];
		   c[i+1] = temp;
		   i++;
		   p=0;
		 }
		}		    
		builder = new StringBuilder();
        for(String s:c) {
            builder.append(s);
        }
        text= builder.toString();
        
	return coCorrect(text);
	
}
public String coCorrect(String recognizedText) // swap vowels
{
	int len,i,j,p,k = 0;
	String temp;
	StringBuilder builder;
	String text;
	String [] c = new String[1000];
	String [] a ={"[", "_", "]", ";", ":", "!", "@","#","$", "%", "^","&","(",")","/","|","{","}","'","-"};
	len= recognizedText.length();
    c = recognizedText.split("");
    int clength=c.length;
    for(i=0;i<clength;i++)
    {
        p=0;
        for(j=0;j<a.length;j++)
        {
            if(a[j].equals(c[i]))
            {
                    
	              p=1;
	              break;
            }
        }
        
        if(p!=1)
        {
            charList.add(c[i]);
            
        }
    }		    
	builder = new StringBuilder();
    for(String s:charList) {
            builder.append(s);
        }
        text= builder.toString();
        
	return text;
}
/** Function to find English translation of the recognized text */
public String readTxt(String recognizedText)
{
	BufferedReader reader;
	String line = "Sorry Unable to translate text!";
	String[] array;
   	AssetManager assetManager = getAssets();
   	InputStream in=null;
   	Context context = getApplicationContext();
	try {
   		in = this.getAssets().open("malayalamdict.txt");
		boolean i = true;
		reader = new BufferedReader(new InputStreamReader(in));
		
		while ((line = reader.readLine()) != null) {
			String malayalam = line.split(":")[0];
			String english = line.split(":")[1];
			
			if(recognizedText.equals(malayalam)){
				return english;
			}
		}
		
		return "Didn't find";
   	}catch (IOException e) {
		// TODO Auto-generated catch block
		Log.e("translation", "error opening assets", e);
	}
    	 
   	return "Sorry unable to translate!";
}
	
	/** Function to copy file to outputstream */
	private void copyFile(InputStream in, OutputStream out) throws IOException 
	{
		byte[] buffer = new byte[1024];
		int read;
		while((read = in.read(buffer)) != -1){
			out.write(buffer, 0, read);
		}
	}
}

