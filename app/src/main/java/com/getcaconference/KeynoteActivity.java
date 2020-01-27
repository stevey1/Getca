package com.getcaconference;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.util.Log;

public class KeynoteActivity extends Activity {
	String speakerID="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_keynote);
		Bundle b = getIntent().getExtras();
		String keynoteID=""; 
		if (b != null) {
			keynoteID = b.getString("id");
		}
		// File storagePath = Environment.getExternalStorageDirectory();
		Context ctx = getApplicationContext();
		File storagePath = ctx.getFilesDir();

		Cursor cursor = getContentResolver().query(
				ConferenceTable.Keynote.CONTENT_URI
        		,new String[]{
						ConferenceTable.Keynote.C_SpeakerID,"replace("+ConferenceTable.Keynote.C_FirstName +" || "+ConferenceTable.Keynote.C_LastName + ", ' ','') As PicName",
				"trim("+ ConferenceTable.Keynote.C_FirstName +  ") || ' ' || trim(" +ConferenceTable.Keynote.C_LastName +  ") As Name"
				,ConferenceTable.Keynote.C_Biography
				}
				, "_id=?",
				new String[] { keynoteID }, null);
		cursor.moveToNext();
		speakerID=cursor.getString(cursor.getColumnIndex(ConferenceTable.Keynote.C_SpeakerID));
		ImageView imagePic = (ImageView) findViewById(R.id.imagePicture);
        String fileName = 
        		cursor.getString(cursor.getColumnIndex("PicName"));
        File imgFile =new File( storagePath,fileName+".png");

		if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imagePic.setImageBitmap(myBitmap);
            imagePic.setContentDescription(cursor.getString(cursor.getColumnIndex("Name")));
        }
        TextView textName= (TextView) findViewById(R.id.textName);
        textName.setText(cursor.getString(cursor
				.getColumnIndex("Name")));
		TextView textBio = (TextView) findViewById(R.id.textBio);
		textBio.setText(cursor.getString(cursor
				.getColumnIndex(ConferenceTable.Keynote.C_Biography))
				);

	}
	public void SessionsClicked(View v) {
		String where = ConferenceTable.Session.C_SpeakerId+ "=?" ;
		String whereArgs=speakerID;
		Intent intent=new Intent(this,SessionsActivity.class);
		intent.putExtra("whereClause",where);
		intent.putExtra("whereArgs",whereArgs);
		startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS));
	}
}
