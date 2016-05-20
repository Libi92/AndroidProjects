package com.example.jsonparser;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jsonparser.models.Flickr;
import com.example.jsonparser.models.FlickrItem;
import com.example.jsonparser.utils.BasicJsonReader;
import com.example.jsonparser.utils.DBUtils;
import com.example.jsonparser.utils.FlickrAdapter;

public class FlickerActivity extends Activity {

	TextView textViewTitile;
	TextView textViewLink;
	ListView listViewImages;
	
	private long enqueue;
    private DownloadManager dm;
    Flickr flickr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flicker);
		
		BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    Query query = new Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {
 
                            
                            String uriString = c
                                    .getString(c
                                            .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            Intent intent1 = new Intent();
                            intent1.setAction(Intent.ACTION_VIEW);
                            intent1.setDataAndType(Uri.parse(uriString), "image/*");
                            startActivity(intent1);

                        }
                    }
                }
                
                unregisterReceiver(this);
            }
        };
 
        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		
		textViewTitile = (TextView)findViewById(R.id.textViewTitle);
		textViewLink = (TextView)findViewById(R.id.textViewLink);
		listViewImages = (ListView)findViewById(R.id.listViewImages);
		
		String tag = getIntent().getStringExtra("tag");
		
		try {
			
			DBUtils db = new DBUtils(FlickerActivity.this);
			if(isNetworkConnected()){
				flickr = new BasicJsonReader(FlickerActivity.this, tag).execute().get();
				db.save(tag, flickr);
			}
			else {
				flickr = db.getData(tag);
			}
			
			if(flickr != null){
				textViewTitile.setText(flickr.getTitle());
				textViewLink.setText(flickr.getLink());
				FlickrAdapter adapter = new FlickrAdapter(FlickerActivity.this, flickr);
				listViewImages.setAdapter(adapter);
				listViewImages.setOnItemClickListener(new OnItemClickListener() {
	
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						FlickrItem item = flickr.get(position);
						
						Uri uri = Uri.parse(item.getMedia());
						dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
				        Request request = new Request(uri);
				        enqueue = dm.enqueue(request);
						
					}
				});
			}
			else {
				Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		return cm.getActiveNetworkInfo() != null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.flicker, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
