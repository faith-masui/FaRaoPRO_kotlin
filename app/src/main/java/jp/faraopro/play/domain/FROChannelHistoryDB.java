package jp.faraopro.play.domain;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.util.Utils;

/**
 * 再生履歴テーブルへのアクセスを行うクラス
 * 
 * @author AIM
 * 
 */
public class FROChannelHistoryDB {
	private static final boolean DEBUG = true;
	private static final long LIMIT_SIZE = 100L;

	static final String TABLE_NAME = "CHANNEL_HISTORY_TABLE";
	static final String COLUMN_ID = "ID";
	static final String COLUMN_CHANNEL_NAME = "CHANNEL_NAME";
	static final String COLUMN_CHANNEL_NAME_EN = "CHANNEL_NAME_EN";
	static final String COLUMN_MODE = "MODE";
	static final String COLUMN_CHANNEL_ID = "CHANNEL_ID";
	static final String COLUMN_RANGE = "RANGE";
	static final String COLUMN_TIMESTAMP = "TIME_STAMP";

	private static final String[] COLUMNS = { COLUMN_ID, COLUMN_CHANNEL_NAME, COLUMN_CHANNEL_NAME_EN, COLUMN_MODE,
			COLUMN_CHANNEL_ID, COLUMN_RANGE, COLUMN_TIMESTAMP, };

	private SQLiteDatabase mDatabase;

	/**
	 * コンストラクタ
	 * 
	 * @param database
	 */
	public FROChannelHistoryDB(SQLiteDatabase database) {
		this.mDatabase = database;
	}

	public long insert(ChannelHistoryInfo info) {
		long ret = -1L;
		// サイズが100を超えていたら一番古いものを消す
		if (getSize() >= LIMIT_SIZE) {
			deleteOldest();
		}
		// 既に存在するアイテムだった場合、更新する
		ChannelHistoryInfo exist = find(info);
		if (exist != null) {
			return update(info);
		}
		ContentValues values = new ContentValues();
		values.put(COLUMN_CHANNEL_NAME, info.getName());
		values.put(COLUMN_CHANNEL_NAME_EN, info.getNameEn());
		values.put(COLUMN_MODE, info.getMode());
		values.put(COLUMN_CHANNEL_ID, info.getChannelId());
		values.put(COLUMN_RANGE, info.getRange());
		// if (TextUtils.isEmpty(info.getRange()))
		// values.put(COLUMN_RANGE, "");
		values.put(COLUMN_TIMESTAMP, Utils.getNowDateString("yyyyMMddkkmmss"));
		ret = mDatabase.insert(TABLE_NAME, null, values);

		// output("INSERT", info);

		return ret;
	}

	private int update(ChannelHistoryInfo info) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_CHANNEL_NAME, info.getName());
		values.put(COLUMN_CHANNEL_NAME_EN, info.getNameEn());
		values.put(COLUMN_MODE, info.getMode());
		values.put(COLUMN_CHANNEL_ID, info.getChannelId());
		values.put(COLUMN_RANGE, info.getRange());
		// if (TextUtils.isEmpty(info.getRange()))
		// values.put(COLUMN_RANGE, "");
		values.put(COLUMN_TIMESTAMP, Utils.getNowDateString("yyyyMMddkkmmss"));
		String selection = "";
		String[] args;
		if (info.getRange() != null) {
			selection = COLUMN_MODE + " = ? AND " + COLUMN_CHANNEL_ID + " = ? AND " + COLUMN_RANGE + " = ?";
			args = new String[] { info.getMode(), info.getChannelId(), info.getRange() };
		} else {
			selection = COLUMN_MODE + " = ? AND " + COLUMN_CHANNEL_ID + " = ?";
			args = new String[] { info.getMode(), info.getChannelId() };
		}

		// output("UPDATE", info);

		return mDatabase.update(TABLE_NAME, values, selection, args);
	}

	public ChannelHistoryInfo find(ChannelHistoryInfo info) {
		ChannelHistoryInfo result = null;
		String selection = "";
		String[] args;
		if (info.getRange() != null) {
			selection = COLUMN_MODE + " = ? AND " + COLUMN_CHANNEL_ID + " = ? AND " + COLUMN_RANGE + " = ?";
			args = new String[] { info.getMode(), info.getChannelId(), info.getRange() };
		} else {
			selection = COLUMN_MODE + " = ? AND " + COLUMN_CHANNEL_ID + " = ?";
			args = new String[] { info.getMode(), info.getChannelId() };
		}
		Cursor cursor = mDatabase.query(TABLE_NAME, COLUMNS, selection, args, null, null, COLUMN_TIMESTAMP + " DESC",
				"0,1");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				result = setDataFromCursor(cursor);
				// output("FIND", result);
			}
			cursor.close();
		}

		return result;
	}

	public ChannelHistoryInfo findByName(String name) {
		ChannelHistoryInfo result = null;
		String selection = COLUMN_CHANNEL_NAME + " = ?";
		String[] args = new String[] { name };
		Cursor cursor = mDatabase.query(TABLE_NAME, COLUMNS, selection, args, null, null, COLUMN_TIMESTAMP + " DESC",
				"0,1");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				result = setDataFromCursor(cursor);
				// output("FIND", result);
			}
			cursor.close();
		}

		return result;
	}

	public List<ChannelHistoryInfo> findAll() {
		ArrayList<ChannelHistoryInfo> infoList = new ArrayList<ChannelHistoryInfo>();
		Cursor cursor = mDatabase.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_TIMESTAMP + " DESC");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					ChannelHistoryInfo info = setDataFromCursor(cursor);
					infoList.add(info);

					// output("FIND ALL", info);

				} while (cursor.moveToNext());
			}
			cursor.close();
		}

		return infoList;
	}

	// TODO 取得する内容を100件以降のリストに変更し、delete ですべて消すようにすると GOOD
	public ChannelHistoryInfo findOldest(boolean rev) {
		String order = " ASC";
		if (rev)
			order = " DESC";
		ChannelHistoryInfo info = null;
		Cursor cursor = mDatabase.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_TIMESTAMP + order, "0,1");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				info = setDataFromCursor(cursor);

				// output("FIND OLDEST", info);
			}
			cursor.close();
		}

		return info;
	}

	public int deleteOldest() {
		int ret = -1;
		ChannelHistoryInfo info = findOldest(false);
		if (info == null)
			return ret;
		String selection = COLUMN_TIMESTAMP + " = ?";
		String[] args = new String[] { Long.toString(info.getTimestamp()) };

		return mDatabase.delete(TABLE_NAME, selection, args);
	}

	// delete from xxx where id in()
	// select id from xxxx_db.tttt_tbl order by timestamp ASC limit 1

	// @Override
	// public int delete(int id) {
	// int ret = 0;
	//
	// String selection = COLUMN_ID + " = ?";
	// String param = Integer.toString(id);
	// ret = mDatabase.delete(TABLE_NAME, selection, new String[] { param });
	//
	// return ret;
	// }

	public void deleteAll() {
		mDatabase.execSQL(FRODBHelper.DROP_TABLE_SQL + TABLE_NAME);
		mDatabase.execSQL(FRODBHelper.CREATE_CHANNEL_HISTORY_TABLE_SQL);
	}

	public long getSize() {
		return DatabaseUtils.queryNumEntries(mDatabase, TABLE_NAME);
		// return 5L;
	}

	private ChannelHistoryInfo setDataFromCursor(Cursor cursor) {
		ChannelHistoryInfo info = new ChannelHistoryInfo();
		int index = 1; // COLUMN_ID はデータとして不要なので index = 0 は読み込まない
		info.setName(cursor.getString(index++));
		info.setNameEn(cursor.getString(index++));
		info.setMode(cursor.getString(index++));
		info.setChannelId(cursor.getString(index++));
		info.setRange(cursor.getString(index++));
		info.setTimestamp(cursor.getLong(index));

		return info;
	}


}
