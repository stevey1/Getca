package com.getcaconference;
import android.os.Bundle;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
public class SessionsActivity extends BaseActivity implements LoaderCallbacks<Cursor> {
	public static final String TAG = SessionsActivity.class.getSimpleName();
    private static final int LOADER_ID = 0x01;
    SessionRowAdapter mAdapter; 
    String mWhereClause;
    String mWhereArgs;
    ListView listView;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);		
		/*if(mWhereClause!=null && mWhereClause.contains("favorite"))
		{
			menu.findItem(R.id.item_favorites).setVisible(false);
		}*/
		return true;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);
		listView = (ListView) findViewById(R.id.listView);
		TextView textView = (TextView) findViewById(R.id.empty_list_item);
		textView.setText(R.string.NoSessionWithinSearch);
	    Bundle b = getIntent().getExtras();
	    if (b!=null)
	    {
		    mWhereClause = b.getString("whereClause");
		    mWhereArgs=b.getString("whereArgs");
	        if(mWhereClause!=null && mWhereClause.contains("favorite"))
	        {
	        	setTitle("Favorites");
	    		textView.setText(R.string.NoFavoriteSession);
	        }
	    }
		listView.setEmptyView(textView);

		String[] uiBindFrom = {ConferenceTable.Session.C_Title};		
		int[] uiBindTo = {R.id.textSessionDisplay};
        mAdapter = new SessionRowAdapter(this,R.layout.session_row, null, uiBindFrom, uiBindTo,0);  
        listView.setAdapter(mAdapter);

        getLoaderManager()
    		.initLoader(LOADER_ID, savedInstanceState, this);
        listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
			
				Cursor cursor = (Cursor) listView.getAdapter().getItem(
						position);
				Intent intent= new Intent(SessionsActivity.this,SessionActivity.class);
		        intent.putExtra("id",cursor.getString(0));
		        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
			}
        });
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new CursorLoader(this,
                ConferenceTable.Session.CONTENT_URI
                , new String[]{"_id",ConferenceTable.Session.C_BookingID
								, ConferenceTable.Session.C_Title,ConferenceTable.Session.C_SessionTrack,  ConferenceTable.Session.C_StartDateLocalTime ,ConferenceTable.Session.C_EndDateLocalTime}
        		, mWhereClause
        		, mWhereClause==null? null:mWhereArgs.split(">>>")
        		, ConferenceTable.Session.C_StartDateLocalTime+","+ConferenceTable.Session.C_EndDateLocalTime+","+ConferenceTable.Session.C_Title); // 
	} 

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
        mAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
	}

}
