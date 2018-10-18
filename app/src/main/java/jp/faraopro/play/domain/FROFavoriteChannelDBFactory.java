package jp.faraopro.play.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class FROFavoriteChannelDBFactory {
	private static FROFavoriteChannelDB database = null;

	public synchronized static FROFavoriteChannelDB getInstance(Context context) {
		if (database == null) {
			FRODBHelper helper = new FRODBHelper(context);
			SQLiteDatabase db;
			db = helper.getWritableDatabase();
			database = new FROFavoriteChannelDB(db);
		}

		return database;
	}
}
