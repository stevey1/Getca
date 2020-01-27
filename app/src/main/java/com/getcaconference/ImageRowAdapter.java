package com.getcaconference;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
public class ImageRowAdapter extends SimpleCursorAdapter {
	File storagePath;
	public ImageRowAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	    // storagePath = Environment.getExternalStorageDirectory();
        storagePath = context.getFilesDir();
    }

	@Override
	public void bindView(View view, Context arg1, Cursor cursor) {
		super.bindView(view,arg1,cursor);
        ImageView imageView = (ImageView)view.findViewById(R.id.icon);
        
        String fileName = 
        		cursor.getString(cursor.getColumnIndex("PicName"));
        File imgFile =new File( storagePath,fileName+".png");
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }
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
