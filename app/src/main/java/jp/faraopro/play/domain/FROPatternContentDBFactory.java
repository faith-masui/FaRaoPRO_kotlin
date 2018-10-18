package jp.faraopro.play.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class FROPatternContentDBFactory {
	private static FROPatternContentDB database = null;

	public synchronized static FROPatternContentDB getInstance(Context context) {
		if (database == null) {
			FRODBHelper helper = new FRODBHelper(context);
			SQLiteDatabase db;
			db = helper.getWritableDatabase();
			database = new FROPatternContentDB(db);
		}

		return database;
	}
}
