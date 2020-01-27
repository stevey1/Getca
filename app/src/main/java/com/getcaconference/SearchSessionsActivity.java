package com.getcaconference;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class SearchSessionsActivity extends BaseActivity implements LoaderCallbacks<Cursor> {
	Spinner spinnerGrade;
	Spinner spinnerSubject ;
	Spinner spinnerTimeslot ;		

	SimpleCursorAdapter mAdapterGrade; 
    SimpleCursorAdapter mAdapterSubject; 
    SimpleCursorAdapter mAdapterTimeslot;
    private static final int LOADER_GRADE = 0x02;
    private static final int LOADER_SUBJECT = 0x03;
    private static final int LOADER_TIMESLOT = 0x04;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);		
		//menu.findItem(R.id.item_search_sessions).setVisible(false);
		return true;
	}
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    GetVersion();
                    RefreshData();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_sessions);
		int[] uiBindTo = {R.id.text1};
		
		 spinnerGrade = (Spinner) findViewById(R.id.spinnerGrade);
		String[] uiBindFrom1 = {ConferenceTable.Grade.C_GradeName};		
       mAdapterGrade = new SimpleCursorAdapter(this,R.layout.spin_item, null, uiBindFrom1, uiBindTo,0);  
       getLoaderManager()
   		.initLoader(LOADER_GRADE, savedInstanceState, this);
		spinnerGrade.setAdapter(mAdapterGrade);

		
  		spinnerTimeslot = (Spinner) findViewById(R.id.spinnerTimeslot);		
		String[] uiBindFrom3 = {ConferenceTable.Timeslot.C_PrettyStartDate};		
        mAdapterTimeslot= new SimpleCursorAdapter(this,R.layout.spin_item, null, uiBindFrom3, uiBindTo,0);  
        getLoaderManager()
    		.initLoader(LOADER_TIMESLOT, savedInstanceState, this);
        spinnerTimeslot.setAdapter(mAdapterTimeslot);
        
    	
		spinnerSubject = (Spinner) findViewById(R.id.spinnerSubject);
		String[] uiBindFrom2 = {ConferenceTable.Subject.C_Name};		
        mAdapterSubject= new SimpleCursorAdapter(this,R.layout.spin_item, null, uiBindFrom2, uiBindTo,0);  
        getLoaderManager()
    		.initLoader(LOADER_SUBJECT, savedInstanceState, this);
        spinnerSubject.setAdapter(mAdapterSubject);
        
        ((EditText)this.findViewById(R.id.editKeyword)).setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
	               if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
	                    //if the enter key was pressed, then hide the keyboard and do whatever needs doing.
	                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	                    imm.hideSoftInputFromWindow(v.getWindowToken(), keyCode);
	                    //do what you need on your enter key press here

	                    return true;
	                }

	                return false;
	                }
        });
	}
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		switch (id) {
			case LOADER_GRADE:
		        return new CursorLoader(this
		        		, ConferenceTable.Grade.CONTENT_URI
		                , new String[]{"_id",ConferenceTable.Grade.C_GradeID,ConferenceTable.Grade.C_GradeName}
		        		, null, null, null);
	
			case LOADER_SUBJECT:
		        return new CursorLoader(this
	        		, ConferenceTable.Subject.CONTENT_URI
	                , new String[]{"_id",ConferenceTable.Subject.C_SessionTrackID,ConferenceTable.Subject.C_Name}
	        		, null, null, null);
			case LOADER_TIMESLOT:
		        return new CursorLoader(this
		        	, ConferenceTable.Timeslot.CONTENT_URI
		            , new String[]{"distinct _id",ConferenceTable.Timeslot.C_StartDate,ConferenceTable.Timeslot.C_PrettyStartDate}
		        	, null, null, null);
			}
			return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		switch (loader.getId()) {
			case LOADER_GRADE:
				mAdapterGrade.swapCursor(cursor);
				break;
			case LOADER_SUBJECT:
				mAdapterSubject.swapCursor(cursor);
				break;
			case LOADER_TIMESLOT:
				mAdapterTimeslot.swapCursor(cursor);
				break;
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		switch (loader.getId()) {
			case LOADER_GRADE:
				mAdapterGrade.swapCursor(null);
				break;
			case LOADER_SUBJECT:
				mAdapterSubject.swapCursor(null);
				break;
			case LOADER_TIMESLOT:
				mAdapterTimeslot.swapCursor(null);
				break;
		}
	}
    public void DownloadPDFClicked(View v) {
        Uri uri = Uri.parse("http://getca.com/app/onlineschedule/scheduleInpdf");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void DownloadInitialDataClicked(View v) {
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    SetNewVersion("1");
                    RefreshData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

        Toast.makeText(getApplicationContext(),
                R.string.CheckForUpdatesMessage, Toast.LENGTH_LONG).show();
    }
	public void SearchClicked(View v) {
		String where="";
		String whereArgs="";
		EditText editKeyword = (EditText) findViewById(R.id.editKeyword);
		String keywrod=editKeyword.getText().toString();
		if (keywrod  != null && !keywrod.isEmpty() && !keywrod.trim().isEmpty()){
			where=where  + " And (" +  ConferenceTable.Session.C_Description + " like ? or "+  ConferenceTable.Session.C_Title + " like ?)";
			whereArgs=whereArgs+">>>%"+keywrod+"%";
			whereArgs=whereArgs+">>>%"+keywrod+"%";
		}
		Cursor cc = (Cursor)(spinnerGrade.getSelectedItem());
		String grade=cc.getString(cc.getColumnIndex(ConferenceTable.Grade.C_GradeName));

		if(!grade.contains("All Grades"))
		{
			where=where  + " And (" +  ConferenceTable.Session.C_Audience + "='' or "+ConferenceTable.Session.C_Audience +" like ?)";
			whereArgs=whereArgs+">>>%"+grade+"%";
		};
		cc = (Cursor)(spinnerSubject.getSelectedItem());
		String subject=cc.getString(cc.getColumnIndex(ConferenceTable.Subject.C_Name));
		if(!subject.contains("All Subjects"))
		{
			where=where  + " And " + ConferenceTable.Session.C_SessionTrack+" like ?";
			whereArgs=whereArgs+">>>%"+subject+"%";
		};
		cc = (Cursor)(spinnerTimeslot.getSelectedItem());
		Long timeSlot= cc.getLong(cc.getColumnIndex(ConferenceTable.Timeslot.C_StartDate));;
		if(timeSlot>0)
		{
			where=where  + " And " +  ConferenceTable.Session.C_StartDateLocalTime+ ">=?" ;
			whereArgs=whereArgs+">>>"+Long.toString(timeSlot-25200000);
        	
		};

		Intent intent=new Intent(this,SessionsActivity.class);
		if (where!=""){
			where=where.substring(5);
			whereArgs=whereArgs.substring(3);
			intent.putExtra("whereClause",where);
			intent.putExtra("whereArgs",whereArgs);
		}
		startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS));
	}
    private void SetNewVersion(String value){
        ContentValues values = new ContentValues();
        values.put(ConferenceTable.ConferenceSetting.C_NewVersion, value);
        getContentResolver().update(ConferenceTable.ConferenceSetting.CONTENT_URI, values, null, null);
    }
    private void GetVersion(){
        String url="http://www.getca.com/app/services/GetVersion";
        String result=HttpGetFrom(url);
       try {
            JSONObject jsonObj = new JSONObject(result);
            String value=jsonObj.getString("AppVersionID");
            if (value!=null) {
                ContentValues values = new ContentValues();
                values.put(ConferenceTable.ConferenceSetting.C_NewVersion, "1");
                values.put(ConferenceTable.ConferenceSetting.C_VersionNumber,value);
               getContentResolver()
                        .update(ConferenceTable.ConferenceSetting.CONTENT_URI,
                                values,
                                ConferenceTable.ConferenceSetting.C_VersionNumber
                                        + "<?"
                                ,new String[]{ value});
            }

        } catch (JSONException e) {
           e.printStackTrace();

       }
    }
    private void RefreshData()
    {
        Cursor cursor = getContentResolver().query(
                ConferenceTable.ConferenceSetting.CONTENT_URI,null, null, null, null);
        cursor.moveToNext();
        int newVersion=cursor.getInt(cursor
                .getColumnIndex(ConferenceTable.ConferenceSetting.C_NewVersion));
        cursor.close();
        if (newVersion==1)
        {
            ClearAllData();
            SetNewVersion("0");
            DownloadConferenceData();
        }
        DownloadKeynotePicture();
    }

    private void ClearAllData()
    {

        getContentResolver().delete(ConferenceTable.Grade.CONTENT_URI, ConferenceTable.Grade.C_GradeName+"!=?",
                new String[]{"All Grades"});
        getContentResolver().delete(ConferenceTable.Subject.CONTENT_URI, ConferenceTable.Subject.C_Name+"!=?",
                new String[]{"All Subjects"});
        getContentResolver().delete(ConferenceTable.Timeslot.CONTENT_URI, ConferenceTable.Timeslot.C_PrettyStartDate+"!=?",
                new String[]{"All Timeslots"});
        getContentResolver().delete(ConferenceTable.Session.CONTENT_URI, null,
                null);
        getContentResolver().delete(ConferenceTable.Keynote.CONTENT_URI, null,
                null);

        ContentValues values = new ContentValues();
        values.put(ConferenceTable.AllTable.C_LastDownLoadTime, 0);
        getContentResolver().update(ConferenceTable.AllTable.CONTENT_URI, values, null, null);
    }
    private void DownloadConferenceData()
    {
        String WebApiBaseURL;
        WebApiBaseURL="http://www.getca.com/app/services/";

        // String WebApiURL = null;
        Cursor cursor = getContentResolver().query(
                ConferenceTable.AllTable.CONTENT_URI,
                new String[] { ConferenceTable.AllTable.C_TableName,
                        ConferenceTable.AllTable.C_Path,
                        ConferenceTable.AllTable.C_LastDownLoadTime },
                null, null, null);

        while (cursor.moveToNext()) {
            String WebApiURL =
                    WebApiBaseURL
                            + cursor.getString(cursor
                            .getColumnIndex(ConferenceTable.AllTable.C_Path));
            Uri uri = ConferenceTable.getTableUri(WebApiURL);
            String result=HttpGetFrom(WebApiURL);
            try {
                JSONArray array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = array.getJSONObject(i);
                    ContentValues values = new ContentValues();
                    Iterator<String> keys = row.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        String columnName=key.toLowerCase();
                        if (!columnName.equals("audiovideorequest")){
                            String value=getValue(uri,key,row.getString(key));
                            values.put(columnName, value);
                        }
                    }
                    if (values.size() > 0) {
                        getContentResolver().insert(uri, values);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }        }
        cursor.close();
    }
    private String getValue(Uri uri,String key, String value)
    {
        if (uri.getPath()==ConferenceTable.Timeslot.CONTENT_URI.getPath())
        {
            if (key.contentEquals("StartDate") || key.contentEquals("EndDate"))
            {
                try {
                    Date date = new SimpleDateFormat("EEEE, MMMM d, yyyy h:mm a", Locale.ENGLISH).parse(value);
                    value=Long.toString(date.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if ((uri.getPath()==ConferenceTable.Session.CONTENT_URI.getPath())
                && ((key.contentEquals("StartDateLocalTime") || key.contentEquals("EndDateLocalTime"))))
        {
            value=value.replace("/Date(","");
            value=value.replace(")/","");
            value=""+(Long.valueOf(value));
        }
        else if (key.toLowerCase().indexOf("date")>0)
        {
            value=value.replace("/Date(","");
            value=value.replace(")/","");

        }
        return value;

    }
    private void DownloadKeynotePicture()
    {

        Cursor cursor = getContentResolver().query(
                ConferenceTable.Keynote.CONTENT_URI,
                new String[] { "_id","replace("+ConferenceTable.Keynote.C_FirstName +" || "+ConferenceTable.Keynote.C_LastName + ", ' ','') As Name",
                        ConferenceTable.Keynote.C_ImageUrl},
                ConferenceTable.Keynote.C_DownloadImage+"=?",new String[]{"1"}, null);
        while (cursor.moveToNext()) {
            new DownloadImagesTask().execute(cursor.getString(cursor
                    .getColumnIndex("Name"))+ "-"+cursor.getString(cursor
                    .getColumnIndex(ConferenceTable.Keynote.C_ImageUrl)));

            ContentValues values = new ContentValues();
            values.put(ConferenceTable.Keynote.C_DownloadImage,"0");


            getContentResolver()
                    .update(ConferenceTable.Keynote.CONTENT_URI,
                            values,
                            "_id=?"
                            ,new String[]{ cursor.getString(cursor
                                    .getColumnIndex("_id"))});
        }
        cursor.close();


    }

    private class DownloadImagesTask extends AsyncTask<String, Void, Bitmap> {
        String fileName;
        @Override
        protected Bitmap doInBackground(String... urls){
            return download_Image(urls[0]);
        }


        private Bitmap download_Image(String url) {

            String[] inputString= url.split("-",2);
            fileName=inputString[0];
            Bitmap bmp =null;
            try{
                URL ulrn = new URL(inputString[1]);
                HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
                InputStream is = con.getInputStream();
                bmp = BitmapFactory.decodeStream(is);
                if (null != bmp){
                    FileOutputStream outputStream = openFileOutput(fileName+".png", Context.MODE_PRIVATE);
                    bmp.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
                    outputStream.close();
                }
                is.close();
                con.disconnect();
            }catch(Exception e){
                e.printStackTrace();

            }
            return bmp;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
        }

    }

/*
deprecated
    private static String HttpGetFrom(String url){
	    Log.i("test", url);
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try{
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200){
                InputStream content =response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line = reader.readLine()) != null){
                    builder.append(line);
                }
            } else {
                Log.e("ConferenceApplication","Failedet JSON object");
            }
        }catch(ClientProtocolException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return builder.toString();
    }*/
    private static String HttpGetFrom(String urlString){
        StringBuffer response = new StringBuffer();

        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("User-Agent", "");
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();
            connection.disconnect();
        }
        catch (IOException e) {
            // Writing exception to log
            e.printStackTrace();
        }

        return response.toString();
    }
}
