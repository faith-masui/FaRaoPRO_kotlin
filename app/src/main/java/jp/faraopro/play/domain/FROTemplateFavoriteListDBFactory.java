package jp.faraopro.play.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class FROTemplateFavoriteListDBFactory {
	private static FROTemplateFavoriteListDB database = null;

	public synchronized static FROTemplateFavoriteListDB getInstance(Context context) {
		if (database == null) {
			FRODBHelper helper = new FRODBHelper(context);
			SQLiteDatabase db;
			db = helper.getWritableDatabase();
			database = new FROTemplateFavoriteListDB(db);
		}

		return database;
	}
}
