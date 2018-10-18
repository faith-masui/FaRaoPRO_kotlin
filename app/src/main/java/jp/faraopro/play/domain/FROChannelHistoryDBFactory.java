package jp.faraopro.play.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class FROChannelHistoryDBFactory {
	private static FROChannelHistoryDB mDatabase = null;

	public synchronized static FROChannelHistoryDB getInstance(Context context) {
		if (mDatabase == null) {
			FRODBHelper helper = new FRODBHelper(context);
			SQLiteDatabase db;
			db = helper.getWritableDatabase();
			mDatabase = new FROChannelHistoryDB(db);
		}

		return mDatabase;
	}
}
