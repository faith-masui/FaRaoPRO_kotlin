package jp.faraopro.play.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class FROPatternScheduleDBFactory {
	private static FROPatternScheduleDB database = null;

	public synchronized static FROPatternScheduleDB getInstance(Context context) {
		if (database == null) {
			FRODBHelper helper = new FRODBHelper(context);
			SQLiteDatabase db;
			db = helper.getWritableDatabase();
			database = new FROPatternScheduleDB(db);
		}

		return database;
	}
}
