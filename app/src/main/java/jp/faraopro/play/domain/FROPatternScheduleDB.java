package jp.faraopro.play.domain;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCScheduleItem;

/**
 * 再生履歴テーブルへのアクセスを行うクラス
 * 
 * @author AIM
 * 
 */
public class FROPatternScheduleDB {
	@SuppressWarnings("unused")
	private static final String TAG = FROPatternScheduleDB.class.getSimpleName();
	@SuppressWarnings("unused")
	private static final boolean DEBUG = false;
	public static final int MAX_SIZE = 2;

	static final String TABLE_NAME = "PATTERN_SCHEDULE_TABLE";
	static final String COLUMN_ID = "ID";
	static final String COLUMN_TARGET_DATE = "TARGET_DATE";
	static final String COLUMN_SCHEDULE_RULE = "SCHEDULE_RULE";
	static final String COLUMN_SCHEDULE_PERIOD = "SCHEDULE_PERIOD";
	static final String COLUMN_PATTERN_ID = "PATTERN_ID";
	static final String COLUMN_PATTERN_DIGEST = "PATTERN_DIGEST";
	static final String COLUMN_PATTERN_LAST_UPDATE = "PATTERN_LAST_UPDATE";
	static final String COLUMN_PATTERN_UPDATE_STATUS = "PATTERN_UPDATE_STATUS";
	static final String COLUMN_SCHEDULE_MASK = "SCHEDULE_MASK";

	private static final String[] COLUMNS = { COLUMN_ID, COLUMN_TARGET_DATE, COLUMN_SCHEDULE_RULE,
			COLUMN_SCHEDULE_PERIOD, COLUMN_PATTERN_ID, COLUMN_PATTERN_DIGEST, COLUMN_PATTERN_LAST_UPDATE,
			COLUMN_PATTERN_UPDATE_STATUS, COLUMN_SCHEDULE_MASK };

	private SQLiteDatabase database;

	/**
	 * コンストラクタ
	 * 
	 * @param database
	 */
	public FROPatternScheduleDB(SQLiteDatabase database) {
		this.database = database;
	}

	public long insert(MCScheduleItem item) {
		// サイズ数が2を超えていた場合、古いデータは消す
		while (getSize() >= MAX_SIZE) {
			deleteOldest();
		}

		long rowId = -1;
		ContentValues values = new ContentValues();

		values.put(COLUMN_TARGET_DATE, item.getTargetDate());
		values.put(COLUMN_SCHEDULE_RULE, item.getString(MCDefResult.MCR_KIND_SCHEDULE_RULE));
		values.put(COLUMN_SCHEDULE_PERIOD, item.getString(MCDefResult.MCR_KIND_SCHEDULE_PERIOD));
		values.put(COLUMN_PATTERN_ID, item.getString(MCDefResult.MCR_KIND_PATTERN_ID));
		values.put(COLUMN_PATTERN_DIGEST, item.getString(MCDefResult.MCR_KIND_PATTERN_DIGEST));
		values.put(COLUMN_PATTERN_LAST_UPDATE, item.getLastUpdate());
		values.put(COLUMN_PATTERN_UPDATE_STATUS, item.getUpdateStatus());
		values.put(COLUMN_SCHEDULE_MASK, item.getString(MCDefResult.MCR_KIND_SCHEDULE_MASK));
		rowId = database.insert(TABLE_NAME, null, values);

		return rowId;
	}

	public int update(MCScheduleItem item) {
		String selection = COLUMN_TARGET_DATE + " = ?";
		String param = item.getTargetDate();
		ContentValues values = new ContentValues();

		values.put(COLUMN_TARGET_DATE, item.getTargetDate());
		values.put(COLUMN_SCHEDULE_RULE, item.getString(MCDefResult.MCR_KIND_SCHEDULE_RULE));
		values.put(COLUMN_SCHEDULE_PERIOD, item.getString(MCDefResult.MCR_KIND_SCHEDULE_PERIOD));
		values.put(COLUMN_PATTERN_ID, item.getString(MCDefResult.MCR_KIND_PATTERN_ID));
		values.put(COLUMN_PATTERN_DIGEST, item.getString(MCDefResult.MCR_KIND_PATTERN_DIGEST));
		values.put(COLUMN_PATTERN_LAST_UPDATE, item.getLastUpdate());
		values.put(COLUMN_PATTERN_UPDATE_STATUS, item.getUpdateStatus());
		values.put(COLUMN_SCHEDULE_MASK, item.getString(MCDefResult.MCR_KIND_SCHEDULE_MASK));

		return database.update(TABLE_NAME, values, selection, new String[] { param });
	}

	// public MCScheduleItem find() {
	// MCScheduleItem item = null;
	// Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null,
	// null, COLUMN_TARGET_DATE + " ASC");
	// if (cursor != null) {
	// if (cursor.moveToFirst()) {
	// item = setDataFromCursor(cursor);
	// // 以下は日付固有のデータであるため消す
	// item.setTargetDate("");
	// item.setString(MCDefResult.MCR_KIND_PATTERN_ID, "");
	// item.setString(MCDefResult.MCR_KIND_PATTERN_DIGEST, "");
	// }
	// cursor.close();
	// }
	//
	// return item;
	// }

	public MCScheduleItem findByDate(String targetDate) {
		if (targetDate == null)
			return null;

		MCScheduleItem item = null;
		String selection = COLUMN_TARGET_DATE + " = ?";
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, selection, new String[] { targetDate }, null, null,
				COLUMN_TARGET_DATE + " ASC");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				item = setDataFromCursor(cursor);
			}
			cursor.close();
		}

		return item;
	}

	public MCScheduleItem findByPatternId(String patternId) {
		if (patternId == null)
			return null;

		MCScheduleItem item = null;
		String selection = COLUMN_PATTERN_ID + " = ?";
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, selection, new String[] { patternId }, null, null,
				COLUMN_TARGET_DATE + " ASC");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				item = setDataFromCursor(cursor);
			}
			cursor.close();
		}

		return item;
	}

	public List<MCScheduleItem> findAll() {
		ArrayList<MCScheduleItem> scheduleList = new ArrayList<MCScheduleItem>();
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_TARGET_DATE + " ASC");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					MCScheduleItem item = setDataFromCursor(cursor);
					scheduleList.add(item);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}

		return scheduleList;
	}

	public long getSize() {
		return DatabaseUtils.queryNumEntries(database, TABLE_NAME);
	}

	public MCScheduleItem findOldest(boolean rev) {
		String order = " ASC";
		if (rev)
			order = " DESC";
		MCScheduleItem item = null;
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_TARGET_DATE + order, "0,1");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				item = setDataFromCursor(cursor);
			}
			cursor.close();
		}

		return item;
	}

	public int deleteOldest() {
		int ret = -1;
		MCScheduleItem item = findOldest(false);
		if (item == null)
			return ret;
		String selection = COLUMN_TARGET_DATE + " = ?";
		String[] args = new String[] { item.getTargetDate() };

		return database.delete(TABLE_NAME, selection, args);
	}

	public void deleteAll() {
		database.execSQL(FRODBHelper.DROP_TABLE_SQL + TABLE_NAME);
		database.execSQL(FRODBHelper.CREATE_PATTERN_SCHEDULE_TABLE_SQL);
	}

	private MCScheduleItem setDataFromCursor(Cursor cursor) {
		MCScheduleItem item = new MCScheduleItem();
		int index = 0;
		// DB内のIDは不要のためスルー
		index++;
		item.setTargetDate(cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_SCHEDULE_RULE, cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_SCHEDULE_PERIOD, cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_PATTERN_ID, cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_PATTERN_DIGEST, cursor.getString(index++));
		item.setLastUpdate(cursor.getString(index++));
		item.setUpdateStatus(cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_SCHEDULE_MASK, cursor.getString(index++));

		return item;
	}

	public void showAllData() {
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_TARGET_DATE + " ASC");
		FRODebug.logI(getClass(), "---------- SHOW ALL START ----------", DEBUG);
		if (cursor != null) {
			FRODebug.logI(getClass(), "SIZE = " + cursor.getCount(), DEBUG);
			if (cursor.moveToFirst()) {
				do {
					MCScheduleItem item = setDataFromCursor(cursor);
					FRODebug.logI(getClass(), "COLUMN_TARGET_DATE = " + item.getTargetDate(), DEBUG);
					FRODebug.logI(getClass(),
							"COLUMN_SCHEDULE_RULE = " + item.getString(MCDefResult.MCR_KIND_SCHEDULE_RULE), DEBUG);
					FRODebug.logI(getClass(),
							"COLUMN_SCHEDULE_PERIOD = " + item.getString(MCDefResult.MCR_KIND_SCHEDULE_PERIOD), DEBUG);
					FRODebug.logI(getClass(), "COLUMN_PATTERN_ID = " + item.getString(MCDefResult.MCR_KIND_PATTERN_ID),
							DEBUG);
					FRODebug.logI(getClass(),
							"COLUMN_PATTERN_DIGEST = " + item.getString(MCDefResult.MCR_KIND_PATTERN_DIGEST), DEBUG);
					FRODebug.logI(getClass(), "COLUMN_PATTERN_LAST_UPDATE = " + item.getLastUpdate(), DEBUG);
					FRODebug.logI(getClass(), "COLUMN_PATTERN_UPDATE_STATUS = " + item.getUpdateStatus(), DEBUG);
					FRODebug.logI(getClass(),
							"MCR_KIND_SCHEDULE_MASK = " + item.getString(MCDefResult.MCR_KIND_SCHEDULE_MASK), DEBUG);
					FRODebug.logI(getClass(), "---------- SEPARATE ----------", DEBUG);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		FRODebug.logI(getClass(), "---------- SHOW ALL END ----------", DEBUG);
	}
}
