package jp.faraopro.play.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class FROTemplateTimerAutoListDBFactory {
	private static FROTemplateTimerAutoListDB database = null;

	public synchronized static FROTemplateTimerAutoListDB getInstance(Context context) {
		if (database == null) {
			FRODBHelper helper = new FRODBHelper(context);
			SQLiteDatabase db;
			db = helper.getWritableDatabase();
			database = new FROTemplateTimerAutoListDB(db);
		}

		return database;
	}
}
