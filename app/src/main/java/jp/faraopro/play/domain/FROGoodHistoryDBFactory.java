package jp.faraopro.play.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class FROGoodHistoryDBFactory {
	private static IFROMusicInfoDB FROGoodHistoryDB = null;

	public synchronized static IFROMusicInfoDB getInstance(Context context) {
		if (FROGoodHistoryDB == null) {
			FRODBHelper helper = new FRODBHelper(context);
			SQLiteDatabase db;
			db = helper.getWritableDatabase();
			FROGoodHistoryDB = new FROGoodHistoryDB(db);
		}

		return FROGoodHistoryDB;
	}
}
