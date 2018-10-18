package jp.faraopro.play.domain;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 再生履歴テーブルへのアクセスを行うクラス
 * 
 * @author AIM
 * 
 */
public class FROUnsentRatingDB implements IFROUnsentRatingDB {
	private static final boolean DEBUG = false;

	static final String TABLE_NAME = "UNSENT_RATING_TABLE";
	static final String COLUMN_TRACKING_KEY = "TRACKING_KEY";
	static final String COLUMN_MODE = "MODE";
	static final String COLUMN_CHANNEL_ID = "CHANNEL_ID";
	static final String COLUMN_RANGE = "RANGE";
	static final String COLUMN_TRACK_ID = "TRACK_ID";
	static final String COLUMN_DECISION = "DECISION";
	static final String COLUMN_COMPLETE = "COMPLETE";
	static final String COLUMN_DURATION = "DURATION";
	static final String COLUMN_TIMESTAMP = "TIME_STAMP";
	static final String COLUMN_ERROR_REASON = "ERROR_REASON";

	private static final String[] COLUMNS = { COLUMN_TRACKING_KEY, COLUMN_MODE, COLUMN_CHANNEL_ID, COLUMN_RANGE,
			COLUMN_TRACK_ID, COLUMN_DECISION, COLUMN_COMPLETE, COLUMN_DURATION, COLUMN_TIMESTAMP,
			COLUMN_ERROR_REASON, };

	private SQLiteDatabase database;

	/**
	 * コンストラクタ
	 * 
	 * @param database
	 */
	public FROUnsentRatingDB(SQLiteDatabase database) {
		this.database = database;
	}

	@Override
	public long insert(RatingInfo info) {
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(COLUMN_TRACKING_KEY, info.getTrackingKey());
		values.put(COLUMN_MODE, info.getMode());
		values.put(COLUMN_CHANNEL_ID, info.getChannelId());
		values.put(COLUMN_RANGE, info.getRange());
		values.put(COLUMN_TRACK_ID, info.getTrackId());
		values.put(COLUMN_DECISION, info.getDecision());
		values.put(COLUMN_COMPLETE, info.getComplete());
		values.put(COLUMN_DURATION, info.getDuration());
		values.put(COLUMN_TIMESTAMP, info.getTimestamp());
		values.put(COLUMN_ERROR_REASON, info.getErrorReason());
		rowId = database.insert(TABLE_NAME, null, values);

		return rowId;
	}

	@Override
	public int update(int id, RatingInfo app) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RatingInfo find(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RatingInfo> findAll() {
		ArrayList<RatingInfo> infoList = null;
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_TIMESTAMP + " DESC");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				infoList = new ArrayList<RatingInfo>();
				do {
					RatingInfo info = setDataFromCursor(cursor);
					infoList.add(info);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}

		return infoList;
	}

	@Override
	public int delete(int id) {
		int ret = 0;

		String selection = COLUMN_TRACK_ID + " = ?";
		String param = Integer.toString(id);
		ret = database.delete(TABLE_NAME, selection, new String[] { param });

		return ret;
	}

	public RatingInfo findOldest(boolean rev) {
		String order = " ASC";
		if (rev)
			order = " DESC";
		RatingInfo info = null;
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_TIMESTAMP + order);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				info = setDataFromCursor(cursor);
			}
			cursor.close();
		}

		return info;
	}

	@Override
	public void deleteAll() {
		database.execSQL(FRODBHelper.DROP_TABLE_SQL + TABLE_NAME);
		database.execSQL(FRODBHelper.CREATE_UNSENT_RATING_TABLE_SQL);
	}

	@Override
	public int getSize() {
		int size = -1;

		List<RatingInfo> list = findAll();
		if (list != null) {
			size = list.size();
			list.clear();
			list = null;
		}

		return size;
	}

	private RatingInfo setDataFromCursor(Cursor cursor) {
		RatingInfo info = new RatingInfo();
		int index = 0;

		info.setTrackingKey(cursor.getString(index++));
		info.setMode(cursor.getString(index++));
		info.setChannelId(cursor.getString(index++));
		info.setRange(cursor.getString(index++));
		info.setTrackId(cursor.getString(index++));
		info.setDecision(cursor.getString(index++));
		info.setComplete(cursor.getString(index++));
		info.setDuration(cursor.getString(index++));
		info.setTimestamp(cursor.getString(index++));
		info.setErrorReason(cursor.getString(index++));

		return info;
	}

}
