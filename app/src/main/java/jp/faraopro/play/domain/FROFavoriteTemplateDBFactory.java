package jp.faraopro.play.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class FROFavoriteTemplateDBFactory {
	private static FROFavoriteTemplateDB database = null;

	public synchronized static FROFavoriteTemplateDB getInstance(Context context) {
		if (database == null) {
			FRODBHelper helper = new FRODBHelper(context);
			SQLiteDatabase db;
			db = helper.getWritableDatabase();
			database = new FROFavoriteTemplateDB(db);
		}

		return database;
	}
}
