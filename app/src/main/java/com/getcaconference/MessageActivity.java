package com.getcaconference;

import android.os.Bundle;
import android.view.Menu;

public class MessageActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);		
		//menu.findItem(R.id.item_PresidentMessage).setVisible(false);
		return true;
	}

}
