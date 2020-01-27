package com.getcaconference;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
public class SessionActivity extends Activity {
	String sessionID;
	CheckBox checkBoxFavorite;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_session);
		Bundle b = getIntent().getExtras();
		
		if (b != null) {
			sessionID = b.getString("id");
		}
		Cursor cursor = getContentResolver().query(
				ConferenceTable.Session.CONTENT_URI, null, "_id=?",
				new String[] { sessionID }, null);
		cursor.moveToNext();

		TextView textSessionTitle = (TextView) findViewById(R.id.textSessionDisplay);
		textSessionTitle.setText(cursor.getString(cursor
				.getColumnIndex(ConferenceTable.Session.C_Title)));
		TextView textSpeaker = (TextView) findViewById(R.id.textSessionSpeaker);
        			textSpeaker.setText(cursor.getString(cursor
				.getColumnIndex(ConferenceTable.Session.C_FirstName))+ ' '+cursor.getString(cursor
						.getColumnIndex(ConferenceTable.Session.C_LastName)));

		TextView textTime = (TextView) findViewById(R.id.textTime);
        SimpleDateFormat startDateFormat = new SimpleDateFormat("E - HH:mm");
        SimpleDateFormat endDateFormat = new SimpleDateFormat("HH:mm");
        startDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        endDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        String text=startDateFormat.format(cursor.getLong(cursor
			.getColumnIndex(ConferenceTable.Session.C_StartDateLocalTime)))+ " to "+ 
        endDateFormat.format(cursor.getLong(cursor
						.getColumnIndex(ConferenceTable.Session.C_EndDateLocalTime)));
        textTime.setText( text);
	
		TextView textTrack = (TextView) findViewById(R.id.textTrack);
		textTrack.setText(cursor.getString(cursor
				.getColumnIndex(ConferenceTable.Session.C_Audience))
				+ " - "+ cursor.getString(cursor
				.getColumnIndex(ConferenceTable.Session.C_SessionTrack)));     
		TextView textBuilding = (TextView) findViewById(R.id.textBuilding);
		textBuilding.setText(cursor.getString(cursor
				.getColumnIndex(ConferenceTable.Session.C_Building))+ ' '+cursor.getString(cursor
						.getColumnIndex(ConferenceTable.Session.C_Room))); 
		TextView textDescription = (TextView) findViewById(R.id.textDescription);
		textDescription.setText(cursor.getString(cursor
				.getColumnIndex(ConferenceTable.Session.C_Description)));
	    checkBoxFavorite=(CheckBox)findViewById(R.id.checkBoxFavorite);
		checkBoxFavorite.setChecked(cursor.getInt(cursor
				.getColumnIndex(ConferenceTable.Session.C_Favorite))==1? true:false);

		Button buttonFeedback=(Button)findViewById(R.id.buttonFeedback);
		if(cursor.getInt(cursor
					.getColumnIndex(ConferenceTable.Session.C_FeedBackSend))==0)
		{
			buttonFeedback.setVisibility( View.VISIBLE);
			}
		else
		{
			buttonFeedback.setVisibility( View.INVISIBLE);
		}
		//buttonFeedback.setVisibility( View.VISIBLE);
		cursor.close();
	}
	public void submitFeedbackClicked(View v)
	{
		Intent intent= new Intent(SessionActivity.this,FeedbackActivity.class);
        intent.putExtra("id",sessionID);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS));
	}
	public void favoriteClicked(View v)
	{
		int favorite=checkBoxFavorite.isChecked()?1:0;
		ContentValues values = new ContentValues(); 
		values.put(ConferenceTable.Session.C_Favorite,Integer.toString(favorite));
		getContentResolver().update(ConferenceTable.Session.CONTENT_URI,
				values,"_id=?",new String[]{sessionID});
	}

}
