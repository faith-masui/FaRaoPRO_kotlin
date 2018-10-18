package jp.faraopro.play.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class FROTimerAutoDBFactory {
	private static FROTimerAutoDB database = null;

	public synchronized static FROTimerAutoDB getInstance(Context context) {
		if (database == null) {
			FRODBHelper helper = new FRODBHelper(context);
			SQLiteDatabase db;
			db = helper.getWritableDatabase();
			database = new FROTimerAutoDB(db);
		}

		return database;
	}
}
