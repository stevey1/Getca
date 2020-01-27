package com.getcaconference;

import java.net.URL;

import android.net.Uri;
import android.provider.BaseColumns;

public class ConferenceTable {
	public static final String AUTHORITY = "com.getcaconference.ConferenceDataProvider";
	private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

	private ConferenceTable() {

	}

	public static Uri getTableUri(String path) {
		if (path.indexOf(Grade.NAME) > 0) {
			return Grade.CONTENT_URI;
		}
		if (path.indexOf(Subject.NAME) > 0) {
			return Subject.CONTENT_URI;
		}
		if (path.indexOf("Time") > 0) {
			return Timeslot.CONTENT_URI;
		}
		if (path.indexOf(Keynote.NAME) > 0) {
			return Keynote.CONTENT_URI;
		}
		if (path.indexOf(Session.NAME) > 0) {
			return Session.CONTENT_URI;
		}
		if (path.indexOf(AllTable.NAME) > 0) {
			return AllTable.CONTENT_URI;
		}
		if (path.indexOf(ConferenceSetting.NAME) > 0|| path.indexOf("Version")>0) {
			return ConferenceSetting.CONTENT_URI;
		}
		return null;
	}

	public static String getTableName(String path) {

		if (path.indexOf(Grade.NAME) > 0) {
			return Grade.NAME;
		}
		if (path.indexOf(Subject.NAME) > 0) {
			return Subject.NAME;
		}
		if (path.indexOf(Timeslot.NAME) > 0) {
			return Timeslot.NAME;
		}
		if (path.indexOf(Keynote.NAME) > 0) {
			return Keynote.NAME;
		}
		if (path.indexOf(Session.NAME) > 0) {
			return Session.NAME;
		}
		if (path.indexOf(AllTable.NAME) > 0) {
			return AllTable.NAME;
		}
		if (path.indexOf(ConferenceSetting.NAME)>0 || path.indexOf("Version") > 0) {
			return ConferenceSetting.NAME;
		}
		return null;
	}

	public static class Grade implements BaseColumns {
		static final String NAME = "Grade";
		public static final String PATH = "Grades";
		public static final String PATH_FOR_ID = "Grades/#";

		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();
		public static final String C_GradeID = "gradeid";
		public static final String C_GradeName = "gradename";
	}

	public static class Subject implements BaseColumns {
		static final String NAME = "Subject";
		public static final String PATH = "Subjects";
		public static final String PATH_FOR_ID = "Subjects/#";

		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();
		public static final String C_SessionTrackID = "sessiontrackid";
		public static final String C_Name = "name";
	}

	public static class Timeslot implements BaseColumns {
		static final String NAME = "Timeslot";
		public static final String PATH = "Timeslots";
		public static final String PATH_FOR_ID = "Timeslots/#";
		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();
		public static final String C_TimeslotID = "timeslotid";
		public static final String C_StartDate = "startdate";
		public static final String C_EndDate = "enddate";
		public static final String C_PrettyStartDate = "prettystartdate";
	}

	public static class Session implements BaseColumns {
		static final String NAME = "Session";
		public static final String PATH = "Sessions";
		public static final String PATH_FOR_ID = "Sessions/#";
		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();

		public static final String C_BookingID = "bookingid";
		public static final String C_SessionProposalID = "sessionproposalid";
		public static final String C_Title = "title";
		public static final String C_StartDate = "startdate";
		public static final String C_EndDate = "enddate";
		public static final String C_Room = "room";
		public static final String C_Building = "building";
		public static final String C_PreRegistration = "preregistration";
		public static final String C_SpecialRequest = "specialrequest";
		public static final String C_SessionTrack = "sessiontrack";
		public static final String C_Audience = "audience";
		public static final String C_SpeakerId = "speakerid";
		public static final String C_Description = "description";
		public static final String C_FirstName = "firstName";
		public static final String C_LastName = "lastName";
		public static final String C_StartDateLocalTime = "startdatelocaltime";
		public static final String C_EndDateLocalTime = "enddatelocaltime";
		public static final String C_StartDateUTC = "startdateutc";
		public static final String C_EndDateUTC = "enddateutc";
		public static final String C_Favorite = "favorite";
		public static final String C_FeedBackSend = "feedbacksend";
	}

	public static class Keynote implements BaseColumns {
		static final String NAME = "Keynote";
		public static final String PATH = "Keynotes";
		public static final String PATH_FOR_ID = "Keynotes/#";
		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();

		public static final String C_KeynoteID = "keynoteid";
		public static final String C_SpeakerID = "speakerid";
		public static final String C_Biography = "biography";
		public static final String C_FirstName = "firstname";
		public static final String C_LastName = "lastname";
		public static final String C_ImageUrl = "imageurl";
        public static final String C_ImageHash = "imagehash";
        public static final String C_Introduction = "introduction";
        public static final String C_DownloadImage = "downloadimage";
	}

	public static class AllTable implements BaseColumns {
		static final String NAME = "AllTable";
		public static final String PATH = "AllTables";
		public static final String PATH_FOR_ID = "AllTables/#";

		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();
		public static final String C_TableName = "tablename";
		public static final String C_Path = "path";
		public static final String C_LastDownLoadTime = "lastdownloadtime";
	}
	public static class ConferenceSetting implements BaseColumns {
		static final String NAME = "ConferenceSetting";
		public static final String PATH = "ConferenceSettings";
		public static final String PATH_FOR_ID = "ConferenceSettings/#";

		public static final Uri CONTENT_URI = BASE_URI.buildUpon()
				.appendPath(PATH).build();
		public static final String C_VersionNumber = "versionnumber";
		public static final String C_TestVersion = "testversion";
		public static final String C_NewVersion = "newversion";

	}
}