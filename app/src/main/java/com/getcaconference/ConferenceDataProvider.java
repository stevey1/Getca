package com.getcaconference;

import com.getcaconference.ConferenceTable;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;


public class ConferenceDataProvider extends ContentProvider {
	public static final String SINGLE_SESSION_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd.getcaconference.session";
	public static final String MULTIPLE_SESSION_RECORDS_MIME_TYPE = "vnd.android.cursor.dir/vnd.getcaconference.session";
	DbHelper dbHelper;
	SQLiteDatabase db;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		db = dbHelper.getReadableDatabase();
		String tableName = ConferenceTable.getTableName(uri.getPath());
		int returnValue = db.delete(tableName, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return returnValue;
	}

	@Override
	public String getType(Uri uri) {
		return this.getId(uri) < 0 ? SINGLE_SESSION_RECORD_MIME_TYPE
				: MULTIPLE_SESSION_RECORDS_MIME_TYPE;
	}

	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		db = dbHelper.getWritableDatabase();
		// Uri _uri = null;
		String tableName = ConferenceTable.getTableName(uri.getPath());

		long id = db.insertWithOnConflict(tableName, null, values,
				SQLiteDatabase.CONFLICT_IGNORE);
		
		if(tableName.contains(ConferenceTable.Keynote.NAME))
		{
			String keyNoteID=values.getAsString(ConferenceTable.Keynote.C_KeynoteID);
			String imageHash=values.getAsString(ConferenceTable.Keynote.C_ImageHash);
			ContentValues updateValues=new ContentValues();
			updateValues.put(ConferenceTable.Keynote.C_DownloadImage, 1);
			updateValues.put(ConferenceTable.Keynote.C_ImageHash, imageHash);
			db.update(ConferenceTable.Keynote.NAME, updateValues,ConferenceTable.Keynote.C_KeynoteID+"=? and "+ConferenceTable.Keynote.C_ImageHash+"!=?"
					, new String[]{keyNoteID,imageHash});
		}
		getContext().getContentResolver().notifyChange(uri, null);
		if (id != -1)
			return Uri.withAppendedPath(uri, Long.toString(id));
		return uri;
	}

	@Override
	public boolean onCreate() {
		dbHelper = new DbHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// Uri _uri = null;

		String tableName = ConferenceTable.getTableName(uri.getPath());
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(tableName, projection, selection,
				selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		db = dbHelper.getReadableDatabase();
		String tableName = ConferenceTable.getTableName(uri.getPath());
		int returnValue = db
				.update(tableName, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return returnValue;

	}

	// Helper method to extract ID from Uri
	private long getId(Uri uri) {
		String lastPathSegment = uri.getLastPathSegment();
		if (lastPathSegment != null) {
			try {
				return Long.parseLong(lastPathSegment);
			} catch (NumberFormatException e) {
				// at least we tried
			}
		}
		return -1;
	}

	class DbHelper extends SQLiteOpenHelper {
		static final int VERSION = 4;
		static final String DATABASE = "conference.db";

		public DbHelper(Context context) {
			super(context, DATABASE, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table " + ConferenceTable.Grade.NAME
					+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ ConferenceTable.Grade.C_GradeID + " text Unique, "
					+ ConferenceTable.Grade.C_GradeName + " text)");

			db.execSQL("create table " + ConferenceTable.Subject.NAME
					+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ ConferenceTable.Subject.C_SessionTrackID
					+ " text Unique, " + ConferenceTable.Subject.C_Name
					+ " text)");

			db.execSQL("create table " + ConferenceTable.Timeslot.NAME
					+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ ConferenceTable.Timeslot.C_TimeslotID + " text, "
					+ ConferenceTable.Timeslot.C_StartDate + " Long Unique,"
					+ ConferenceTable.Timeslot.C_EndDate + " Long, "
					+ ConferenceTable.Timeslot.C_PrettyStartDate
					+ " text )");

			db.execSQL("create table " + ConferenceTable.Keynote.NAME
					+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ ConferenceTable.Keynote.C_KeynoteID + " text Unique, "
					+ ConferenceTable.Keynote.C_SpeakerID + " text, "
					+ ConferenceTable.Keynote.C_Biography + " text, "
					+ ConferenceTable.Keynote.C_FirstName + " text, "
					+ ConferenceTable.Keynote.C_LastName + " text, "
					+ ConferenceTable.Keynote.C_ImageUrl + " text, "
					+ ConferenceTable.Keynote.C_ImageHash + " text,"
                    + ConferenceTable.Keynote.C_Introduction + " text,"
					+ ConferenceTable.Keynote.C_DownloadImage + " INTEGER default 1)");

			db.execSQL("create table " + ConferenceTable.Session.NAME
					+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ ConferenceTable.Session.C_BookingID + " text Unique, "
					+ ConferenceTable.Session.C_SessionProposalID + " text, "
					+ ConferenceTable.Session.C_Title + " text, "
					+ ConferenceTable.Session.C_StartDate + " long, "
					+ ConferenceTable.Session.C_EndDate + " long, "
					+ ConferenceTable.Session.C_Room + " text, "
					+ ConferenceTable.Session.C_Building + " text, "
					+ ConferenceTable.Session.C_PreRegistration + " text, "
					+ ConferenceTable.Session.C_SpecialRequest + " text, "
					+ ConferenceTable.Session.C_SessionTrack + " text, "
					+ ConferenceTable.Session.C_Audience + " text, "
					+ ConferenceTable.Session.C_SpeakerId + " text, "
					+ ConferenceTable.Session.C_Description + " text, "
					+ ConferenceTable.Session.C_FirstName + " text, "
					+ ConferenceTable.Session.C_LastName + " text, "
					+ ConferenceTable.Session.C_StartDateLocalTime + " long, "
					+ ConferenceTable.Session.C_EndDateLocalTime + " long,"
					+ ConferenceTable.Session.C_StartDateUTC + " long, "
					+ ConferenceTable.Session.C_EndDateUTC + " long,"
					+ ConferenceTable.Session.C_Favorite
					+ " INTEGER default 0,"
					+ ConferenceTable.Session.C_FeedBackSend
					+ " INTEGER default 0)");

			db.execSQL("create table " + ConferenceTable.AllTable.NAME
					+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ ConferenceTable.AllTable.C_TableName + " text Unique, "
					+ ConferenceTable.AllTable.C_Path + " text, "
					+ ConferenceTable.AllTable.C_LastDownLoadTime + " INTEGER)");
			
			db.execSQL("create table " + ConferenceTable.ConferenceSetting.NAME
					+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ ConferenceTable.ConferenceSetting.C_TestVersion + " INTEGER, "
					+ ConferenceTable.ConferenceSetting.C_NewVersion + " INTEGER, "
					+ ConferenceTable.ConferenceSetting.C_VersionNumber + " INTEGER)");
			AddBasicData(db);

			}
		
		private void AddBasicData(SQLiteDatabase db) {
			ContentValues values = new ContentValues();

			values.put(ConferenceTable.AllTable.C_TableName,
					ConferenceTable.Grade.NAME);
			values.put(ConferenceTable.AllTable.C_Path, "Get"
					+ ConferenceTable.Grade.NAME + "s");
			values.put(ConferenceTable.AllTable.C_LastDownLoadTime, 0);
			db.insert(ConferenceTable.AllTable.NAME,null,
					values);

			values.clear();
			values.put(ConferenceTable.AllTable.C_TableName,
					ConferenceTable.Subject.NAME);
			values.put(ConferenceTable.AllTable.C_Path, "Get"
					+ ConferenceTable.Subject.NAME + "s");
			values.put(ConferenceTable.AllTable.C_LastDownLoadTime, 0);
			db.insert(ConferenceTable.AllTable.NAME,null,
					values);
			values.clear();
			values.put(ConferenceTable.AllTable.C_TableName,
					ConferenceTable.Timeslot.NAME);
			values.put(ConferenceTable.AllTable.C_Path, "GetTimes");
			values.put(ConferenceTable.AllTable.C_LastDownLoadTime, 0);
			db.insert(ConferenceTable.AllTable.NAME,null,
					values);

			values.clear();
			values.put(ConferenceTable.AllTable.C_TableName,
					ConferenceTable.Keynote.NAME);
			values.put(ConferenceTable.AllTable.C_Path, "Get"
					+ ConferenceTable.Keynote.NAME + "s");
			values.put(ConferenceTable.AllTable.C_LastDownLoadTime, 0);
			db.insert(ConferenceTable.AllTable.NAME,null,
					values);
			values.clear();
			values.put(ConferenceTable.AllTable.C_TableName,
					ConferenceTable.Session.NAME);
			values.put(ConferenceTable.AllTable.C_Path, "GetSessions");
			values.put(ConferenceTable.AllTable.C_LastDownLoadTime, 0);
			db.insert(ConferenceTable.AllTable.NAME,null,
					values);
			
			values.clear();
			values.put(ConferenceTable.Grade.C_GradeName, "All Grades");
			db.insert(ConferenceTable.Grade.NAME,null, values);
			values.clear();
			values.put(ConferenceTable.Subject.C_Name, "All Subjects");
			db
					.insert(ConferenceTable.Subject.NAME,null, values);
			values.clear();
			values.put(ConferenceTable.Timeslot.C_PrettyStartDate, "All Timeslots");
			values.put(ConferenceTable.Timeslot.C_StartDate, "0");
			db.insert(ConferenceTable.Timeslot.NAME,null,
					values);
			
			values.clear();
			values.put(ConferenceTable.ConferenceSetting.C_TestVersion, "0");
			values.put(ConferenceTable.ConferenceSetting.C_VersionNumber, "-1");
			values.put(ConferenceTable.ConferenceSetting.C_NewVersion, "1");
			db.insert(ConferenceTable.ConferenceSetting.NAME,null,
					values);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table " + ConferenceTable.Grade.NAME);
			db.execSQL("drop table " + ConferenceTable.Subject.NAME);
			db.execSQL("drop table " + ConferenceTable.Timeslot.NAME);
			db.execSQL("drop table " + ConferenceTable.Keynote.NAME);
			db.execSQL("drop table " + ConferenceTable.Session.NAME);
			db.execSQL("drop table " + ConferenceTable.AllTable.NAME);
			db.execSQL("drop table " + ConferenceTable.ConferenceSetting.NAME);			
			this.onCreate(db);
		}
	}
}
