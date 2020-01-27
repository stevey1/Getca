package com.getcaconference;

import android.app.Activity;
import android.content.Intent;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends Activity {
	ConferenceApplication conferenceApplication;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		conferenceApplication = (ConferenceApplication) getApplication();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
/*		case R.id.item_other:
			startActivity(new Intent(this, CheckForUpdateActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;*/
		case R.id.item_search_sessions:
			startActivity(new Intent(this, SearchSessionsActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME));
			break;
		case R.id.item_keynotes:
			startActivity(new Intent(this, KeynotesActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		case R.id.item_favorites:
			//Intent intent = new Intent(this, FavoriteSessionsActivity.class);
			Intent intent=new Intent(this,SessionsActivity.class);
			intent.putExtra("whereClause","favorite=?");
			intent.putExtra("whereArgs","1");

			startActivity(intent
					.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS));
			break;

		case R.id.item_PresidentMessage:
			startActivity(new Intent(this, MessageActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		case R.id.item_FAQ:
			startActivity(new Intent(this, FAQActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;			
		default:
			break;
		}
		return true;
	}
}
