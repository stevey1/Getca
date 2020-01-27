package com.getcaconference;

import android.os.Bundle;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class KeynotesActivity extends BaseActivity implements LoaderCallbacks<Cursor>  {

	public static final String TAG = KeynotesActivity.class.getSimpleName();
    private static final int LOADER_ID = 0x05;
    ImageRowAdapter mAdapter; 
	ListView listView;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);		
		//menu.findItem(R.id.item_keynotes).setVisible(false);
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

		setContentView(R.layout.list_view);
		listView = (ListView) findViewById(R.id.listView);
		TextView textView = (TextView) findViewById(R.id.empty_list_item);
		textView.setText(R.string.NoKeynoteData);
		listView.setEmptyView(textView);
		
		String[] uiBindFrom = {"Name"};		
		int[] uiBindTo = {R.id.label};
		
        mAdapter = new ImageRowAdapter(this,R.layout.keynote_row, null, uiBindFrom, uiBindTo,0);  
        listView.setAdapter(mAdapter);
        getLoaderManager()
    		.initLoader(LOADER_ID, savedInstanceState, this);
        listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
			
				Cursor cursor = (Cursor) listView.getAdapter().getItem(
						position);
				Intent intent= new Intent(KeynotesActivity.this,KeynoteActivity.class);
		        intent.putExtra("id",cursor.getString(0));
		        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
			}
        });
	}

    @Override
    protected void onResume() {
        super.onResume();
        //( (ConferenceApplication) getApplication()).DownloadKeynotePicture();
    }
    
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        return new CursorLoader(this,
                ConferenceTable.Keynote.CONTENT_URI
                , new String[]{"_id",
        		"replace("+ConferenceTable.Keynote.C_FirstName +" || "+ConferenceTable.Keynote.C_LastName + ", ' ','') As PicName",
				"trim("+ ConferenceTable.Keynote.C_FirstName +  ") || ' ' || trim(" +ConferenceTable.Keynote.C_LastName +  ") As Name"
        		}, null, null, null);
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
