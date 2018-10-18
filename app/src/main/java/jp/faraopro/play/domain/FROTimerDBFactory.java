package jp.faraopro.play.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class FROTimerDBFactory {
	private static FROTimerDB database = null;

	public synchronized static FROTimerDB getInstance(Context context) {
		if (database == null) {
			FRODBHelper helper = new FRODBHelper(context);
			SQLiteDatabase db;
			db = helper.getWritableDatabase();
			database = new FROTimerDB(db);
		}

		return database;
	}
}
