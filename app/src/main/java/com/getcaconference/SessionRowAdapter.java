package com.getcaconference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SessionRowAdapter extends SimpleCursorAdapter {
	public SessionRowAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}

	@Override
	public void bindView(View view, Context arg1, Cursor cursor) {
		super.bindView(view,arg1,cursor);
        TextView textView = (TextView)view.findViewById(R.id.textTrackTimeSlot);
        Date start=
        		new Date(cursor.getLong(cursor.getColumnIndex(ConferenceTable.Session.C_StartDateLocalTime)));
        Date end=
        		new Date(cursor.getLong(cursor.getColumnIndex(ConferenceTable.Session.C_EndDateLocalTime)));
        SimpleDateFormat startDateFormat = new SimpleDateFormat("E, HH:mm");
        SimpleDateFormat endDateFormat = new SimpleDateFormat("HH:mm");
        startDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        endDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    	//Log.d("Search","Search2"+cursor.getShort(cursor.getColumnIndex(ConferenceTable.Session.C_StartDateLocalTime)));
       
        String text=cursor.getString(cursor.getColumnIndex(ConferenceTable.Session.C_SessionTrack)) +" - "+startDateFormat.format(start)+ " to "+ endDateFormat.format(end);
        textView.setText( text);
	}
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final View row = super.getView(position, convertView, parent);
        if (position % 2 == 0)
            row.setBackgroundResource(android.R.color.darker_gray);
        else
            row.setBackgroundResource(android.R.color.background_light);
        return row;
    }
}
