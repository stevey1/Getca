package com.getcaconference;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class FAQActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faq);
		TextView textFAQ=(TextView) findViewById(R.id.textFAQ);
	
		textFAQ.setText(Html.fromHtml(getResources().getString(R.string.QuestionAndAnswer)));
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);		
		//menu.findItem(R.id.item_FAQ).setVisible(false);
		return true;
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
}
