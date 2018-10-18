package jp.faraopro.play.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class FROUnsentRatingDBFactory {
	private static IFROUnsentRatingDB FROUnsentRatingDB = null;

	public synchronized static IFROUnsentRatingDB getInstance(Context context) {
		if (FROUnsentRatingDB == null) {
			FRODBHelper helper = new FRODBHelper(context);
			SQLiteDatabase db;
			db = helper.getWritableDatabase();
			FROUnsentRatingDB = new FROUnsentRatingDB(db);
		}

		return FROUnsentRatingDB;
	}
}
