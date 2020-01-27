package com.getcaconference;
import android.app.Application;


public class ConferenceApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
        //DownloadVersion();
    	//RefreshData();
	}
/*	public void RefreshData()
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
		}
		DownloadConferenceData();
		ReDownloadKeynotePicture();

		ContentValues values = new ContentValues();
		values.put(ConferenceTable.ConferenceSetting.C_NewVersion, "0");
		getContentResolver().update(ConferenceTable.ConferenceSetting.CONTENT_URI, values, null, null);
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
		
		values.clear();
		values.put(ConferenceTable.ConferenceSetting.C_NewVersion, "1");
		getContentResolver().update(ConferenceTable.ConferenceSetting.CONTENT_URI, values, null, null);

	}
 	public void DownloadVersion()
	{
	    String WebApiBaseURL;
        WebApiBaseURL="http://www.getca.com/app/services/";
		URL WebApiURL=null;
		try {

				WebApiURL = new URL(
						WebApiBaseURL + "GetVersion");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		new DownloadVersionApi().execute(WebApiURL);
	}
	private void DownloadConferenceData()
	{
	    String WebApiBaseURL;
	    	WebApiBaseURL="http://www.getca.com/app/services/";

		URL WebApiURL = null;
		Cursor cursor = getContentResolver().query(
				ConferenceTable.AllTable.CONTENT_URI,
				new String[] { ConferenceTable.AllTable.C_TableName,
						ConferenceTable.AllTable.C_Path,
						ConferenceTable.AllTable.C_LastDownLoadTime }, 
				null, null, null);

		while (cursor.moveToNext()) {
			try {
				WebApiURL = new URL(
						WebApiBaseURL
								+ cursor.getString(cursor
										.getColumnIndex(ConferenceTable.AllTable.C_Path)));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

			new DownloadConferenceDataApi().execute(WebApiURL);

			ContentValues values = new ContentValues();
			values.put(ConferenceTable.AllTable.C_LastDownLoadTime,
					System.currentTimeMillis());
			getContentResolver()
			.update(ConferenceTable.AllTable.CONTENT_URI,
					values,
					ConferenceTable.AllTable.C_TableName
							+ "=?"
							,new String[]{ cursor.getString(cursor
									.getColumnIndex(ConferenceTable.AllTable.C_TableName))});
		}
		cursor.close();
	}
	
	private class DownloadVersionApi extends AsyncTask<URL, Void, Long> {
		private String result;
		Uri uri;

		@Override
		protected Long doInBackground(URL... urls) {
			int count = urls.length;
			long totalSize = 0;
			StringBuilder resultBuilder = new StringBuilder();
			for (int i = 0; i < count; i++) {
				try {
					// Read all the text returned by the server
					uri = ConferenceTable.getTableUri(urls[i].getPath());
					InputStreamReader reader = new InputStreamReader(
							urls[i].openStream());
					BufferedReader in = new BufferedReader(reader);
					String resultPiece;
					while ((resultPiece = in.readLine()) != null) {
						resultBuilder.append(resultPiece);
					}
					in.close();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// if cancel() is called, leave the loop early
				if (isCancelled()) {
					break;
				}
			}
			// save the result
			this.result = resultBuilder.toString();
			return totalSize;
		}

		// called after doInBackground finishes
		@Override
		protected void onPostExecute(Long result) {
			try {
				JSONObject jsonObj = new JSONObject(this.result);
				String value=jsonObj.getString("AppVersionID");
				if (value!=null) {
					ContentValues values = new ContentValues();
					values.put(ConferenceTable.ConferenceSetting.C_NewVersion, "1");
					values.put(ConferenceTable.ConferenceSetting.C_VersionNumber,value);
	
					getContentResolver()
					.update(ConferenceTable.ConferenceSetting.CONTENT_URI,
							values,
							ConferenceTable.ConferenceSetting.C_VersionNumber
									+ ">?"
									,new String[]{ value});
					
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	private class DownloadConferenceDataApi extends AsyncTask<URL, Void, Long> {
		private String result;
		Uri uri;

		@Override
		protected Long doInBackground(URL... urls) {
			int count = urls.length;
			long totalSize = 0;
			StringBuilder resultBuilder = new StringBuilder();
			for (int i = 0; i < count; i++) {
				try {
					// Read all the text returned by the server
					uri = ConferenceTable.getTableUri(urls[i].getPath());
					InputStreamReader reader = new InputStreamReader(
							urls[i].openStream());
					BufferedReader in = new BufferedReader(reader);
					String resultPiece;
					while ((resultPiece = in.readLine()) != null) {
						resultBuilder.append(resultPiece);
					}
					in.close();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// if cancel() is called, leave the loop early
				if (isCancelled()) {
					break;
				}
			}
			// save the result
			this.result = resultBuilder.toString();
			return totalSize;
		}

		// called after doInBackground finishes
		@Override
		protected void onPostExecute(Long result) {
			try {
				JSONArray array = new JSONArray(this.result);
				for (int i = 0; i < array.length(); i++) {
					JSONObject row = array.getJSONObject(i);
					ContentValues values = new ContentValues();
					Iterator<String> keys = row.keys();
					while (keys.hasNext()) {
						String key = keys.next();
						String value=getValue(key,row.getString(key));
						values.put(key.toLowerCase(), value);
					}
					if (values.size() > 0) {
							getContentResolver().insert(uri, values);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		private String getValue(String key, String value)
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
	}
	public void ReDownloadKeynotePicture()
	{
		ContentValues values = new ContentValues();
		values.put(ConferenceTable.Keynote.C_DownloadImage,"1");
		getContentResolver()
				.update(ConferenceTable.Keynote.CONTENT_URI,
						values, null,null);
		DownloadKeynotePicture();
	}
	
	public void DownloadKeynotePicture()
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
	            if (null != bmp)
	                return bmp;

	            }catch(Exception e){}
	        return bmp;
	    }
		@Override
		protected void onPostExecute(Bitmap result) {
		    File storagePath = Environment.getExternalStorageDirectory();
		    OutputStream output = null;
			try {
				output = new FileOutputStream (new File(storagePath,fileName+".png"));
	    		result.compress(Bitmap.CompressFormat.PNG, 90, output);
		        output.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {

		    }
		}

	}*/
}
