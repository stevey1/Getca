/*package com.getcaconference;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CheckForUpdateActivity extends BaseActivity {
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.findItem(R.id.item_other).setVisible(false);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_other);
		GetSystemInfo();
	}
	            
	public void SwitchClicked(View v) {
		   AlertDialog.Builder alert = new AlertDialog.Builder(this);                 
		   alert.setTitle("Enter Admin Password");  
		   
		   final EditText input = new EditText(this); 
		   alert.setView(input);
	      alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
	      public void onClick(DialogInterface dialog, int whichButton) {  
	          String password= input.getText().toString();
	        	if( password.toLowerCase().compareTo("getca2013") == 0)
	          {
	        		Cursor cursor = getContentResolver().query(
	        				ConferenceTable.ConferenceSetting.CONTENT_URI,null, null, null, null);
	        		cursor.moveToNext();
	        		int testVersion=cursor.getInt(cursor
	        				.getColumnIndex(ConferenceTable.ConferenceSetting.C_TestVersion));
	        		cursor.close();
	      		ContentValues values = new ContentValues();
	    		values.put(ConferenceTable.ConferenceSetting.C_TestVersion,(testVersion+1)%2);//	""+ ConferenceTable.ConferenceSetting.C_TestVersion+"+1)%2");
	    		values.put(ConferenceTable.ConferenceSetting.C_NewVersion,"1");
	    		values.put(ConferenceTable.ConferenceSetting.C_VersionNumber,"-1");
	    		getContentResolver().update(ConferenceTable.ConferenceSetting.CONTENT_URI, values, null, null);
	    	    
        	  GetSystemInfo();
	          }

	          return;                  
	         }  
	       });  

	      alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

	          public void onClick(DialogInterface dialog, int which) {
	              return;
	          }
	      });
          alert.show();
          
	}
	private void GetSystemInfo()
	{
		Cursor cursor = getContentResolver().query(
				ConferenceTable.ConferenceSetting.CONTENT_URI,null, null, null, null);
		cursor.moveToNext();
		int testVersion=cursor.getInt(cursor
				.getColumnIndex(ConferenceTable.ConferenceSetting.C_TestVersion));
		String currentVersion=".";//Producation
		if(testVersion==1)
		{
			currentVersion="...."; //UAT
		}

		TextView textSystemInfo=(TextView) findViewById(R.id.textSysInfo);
		textSystemInfo.setText(currentVersion);
		cursor.close();
	}
	public void RefreshClicked(View v) {
		conferenceApplication.RefreshData();
		
		Toast.makeText(getApplicationContext(),
				R.string.CheckForUpdatesMessage, Toast.LENGTH_LONG).show();
		//GetSystemInfo();
	}
}
*/