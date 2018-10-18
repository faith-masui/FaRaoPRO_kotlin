package jp.faraopro.play.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class FROMusicHistoryDBFactory {
	private static IFROMusicInfoDB FROMusicHistoryDB = null;

	public synchronized static IFROMusicInfoDB getInstance(Context context) {
		if (FROMusicHistoryDB == null) {
			FRODBHelper helper = new FRODBHelper(context);
			SQLiteDatabase db;
			db = helper.getWritableDatabase();
			FROMusicHistoryDB = new FROMusicHistoryDB(db);
		}

		return FROMusicHistoryDB;
	}
}
