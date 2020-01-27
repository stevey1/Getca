package com.getcaconference;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentValues;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.net.URLEncoder;
import java.io.DataOutputStream;

public class FeedbackActivity extends Activity {
	String bookingID;
	String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		 id = "0";
		Bundle b = getIntent().getExtras();
		if (b != null) {
			id = b.getString("id");
		}

		Cursor cursor = getContentResolver().query(
				ConferenceTable.Session.CONTENT_URI, null, "_id=?",
				new String[] { id }, null);
		cursor.moveToNext();
		bookingID=
		cursor.getString(cursor
				.getColumnIndex(ConferenceTable.Session.C_BookingID));
		TextView textSession=(TextView) findViewById(R.id.textSessionTitle);
		textSession.setText(cursor.getString(cursor
				.getColumnIndex(ConferenceTable.Session.C_Title)));
		TextView textSessionSpeaker=(TextView) findViewById(R.id.textSessionSpeaker);
		textSessionSpeaker.setText(cursor.getString(cursor.getColumnIndex(ConferenceTable.Session.C_FirstName))+ ' '+
				cursor.getString(cursor.getColumnIndex(ConferenceTable.Session.C_LastName)));
		cursor.close();
	}

	public void ClearFormClicked(View v) {
		Toast.makeText(getApplicationContext(),
				R.string.ClearFormMessage, Toast.LENGTH_LONG).show();
	}

	public void SubmitFeedbackClicked(View v) {
		EditText editComment= findViewById(R.id.editComment);
		String comment=editComment.getText().toString();

		RatingBar ratingSpeakerContent= findViewById(R.id.ratingSpeakerContent);
		RatingBar ratingPresentationQuality= findViewById(R.id.ratingPresentationQuality);
		RatingBar ratingTeachingAssignment= findViewById(R.id.ratingTeachingAssignment);
		RatingBar ratingPotentialImpact= findViewById(R.id.ratingPotentialImpact);
		RatingBar ratingOverall= findViewById(R.id.ratingOverall);
        //ratingSpeakerContent.getRating();
		//new GETCAWebApi().execute();
        final HashMap<String, String> param = new HashMap<String, String>();
        param.put("bookingID", bookingID);
        param.put("comments",comment);
        param.put("q1",Integer.toString((int) ratingSpeakerContent.getRating()));
        param.put("q2", Integer.toString((int) ratingPresentationQuality.getRating()));
        param.put("q3", Integer.toString((int) ratingTeachingAssignment.getRating()));
        param.put("q4", Integer.toString((int) ratingPotentialImpact.getRating()));
        param.put("q5", Integer.toString( (int)ratingOverall.getRating()));
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    HttpPost("http://www.getca.com/app/Services/SubmitFeedback", param);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

        ContentValues values = new ContentValues();
        values.put(ConferenceTable.Session.C_FeedBackSend,"1");
        getContentResolver().update(ConferenceTable.Session.CONTENT_URI,
                values,"_id=?",new String[]{id});
		Toast.makeText(getApplicationContext(),
				R.string.SubmitFeedbackMessage, Toast.LENGTH_LONG).show();
	}
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
    private void HttpPost(String urlString, HashMap<String, String> param){
        try{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("USER-AGENT","Mozilla/5.0");
            conn.setRequestProperty("ACCEPT-LANGUAGE","en-US,en;0.5");
            String data = getPostDataString(param);
            conn.setDoOutput(true);
            DataOutputStream dataOutput = new DataOutputStream(conn.getOutputStream());
            dataOutput.writeBytes(data);
            dataOutput.flush();
            dataOutput.close();
            conn.getResponseCode();
        }
        catch (IOException e) {
            // Writing exception to log
            e.printStackTrace();
        }
    }
}
