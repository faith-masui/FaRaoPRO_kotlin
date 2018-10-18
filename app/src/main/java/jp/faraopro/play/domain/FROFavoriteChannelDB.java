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
public class FROFavoriteChannelDB {
	@SuppressWarnings("unused")
	private static final String TAG = FROFavoriteChannelDB.class.getSimpleName();
	@SuppressWarnings("unused")
	private static final boolean DEBUG = true;

	static final String TABLE_NAME = "FAVORITE_CHANNEL_TABLE";
	static final String COLUMN_ID = "ID";
	static final String COLUMN_MODE = "MODE";
	static final String COLUMN_CHANNEL_ID = "CHANNEL_ID";
	static final String COLUMN_RANGE = "RANGE";
	static final String COLUMN_CHANNEL_NAME = "CHANNEL_NAME";
	static final String COLUMN_SOURCE = "SOURCE";
	static final String COLUMN_SORT_INDEX = "SORT_INDEX";
	static final String COLUMN_SOURCE_TYPE = "SOURCE_TYPE";
	static final String COLUMN_SKIP_CONTROL = "SKIP_CONTROL";
    static final String COLUMN_PERMISSION = "PERMISSION";

	private static final String[] COLUMNS = { COLUMN_ID, COLUMN_MODE, COLUMN_CHANNEL_ID, COLUMN_RANGE,
            COLUMN_CHANNEL_NAME, COLUMN_SOURCE, COLUMN_SORT_INDEX, COLUMN_SOURCE_TYPE, COLUMN_SKIP_CONTROL,
            COLUMN_PERMISSION};

	private SQLiteDatabase database;

	/**
	 * コンストラクタ
	 * 
	 * @param database
	 */
	public FROFavoriteChannelDB(SQLiteDatabase database) {
		this.database = database;
	}

	public long insert(ChannelInfo info) {
		int lastIndex = this.getSize();
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(COLUMN_MODE, info.getMode());
		values.put(COLUMN_CHANNEL_ID, info.getChannelId());
		values.put(COLUMN_RANGE, info.getRange());
		values.put(COLUMN_CHANNEL_NAME, info.getChannelName());
		values.put(COLUMN_SOURCE, info.getSource());
		values.put(COLUMN_SORT_INDEX, lastIndex);
		values.put(COLUMN_SOURCE_TYPE, info.getType());
		values.put(COLUMN_SKIP_CONTROL, info.getSkipControl());
        values.put(COLUMN_PERMISSION, info.getPermisson());
		rowId = database.insert(TABLE_NAME, null, values);

		return rowId;
	}

	public int update(ChannelInfo info) {
		String selection = COLUMN_ID + " = ?";
		String param = Integer.toString(info.getId());
		ContentValues values = new ContentValues();

		values.put(COLUMN_ID, info.getId());
		values.put(COLUMN_MODE, info.getMode());
		values.put(COLUMN_CHANNEL_ID, info.getChannelId());
		values.put(COLUMN_RANGE, info.getRange());
		values.put(COLUMN_CHANNEL_NAME, info.getChannelName());
		values.put(COLUMN_SOURCE, info.getSource());
		values.put(COLUMN_SORT_INDEX, info.getIndex());
		values.put(COLUMN_SOURCE_TYPE, info.getType());
		values.put(COLUMN_SKIP_CONTROL, info.getSkipControl());
        values.put(COLUMN_PERMISSION, info.getPermisson());

		return database.update(TABLE_NAME, values, selection, new String[] { param });
	}

	public ChannelInfo find(String mode, String channelId) {
		ChannelInfo info = null;
		String selection = COLUMN_MODE + " = ? AND " + COLUMN_CHANNEL_ID + " = ?";

		Cursor cursor = database.query(TABLE_NAME, COLUMNS, selection, new String[] { mode, channelId }, null, null,
				null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				info = setDataFromCursor(cursor);
			}
			cursor.close();
		}

		return info;
	}

	public List<ChannelInfo> findAll() {
		ArrayList<ChannelInfo> infoList = null;
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_SORT_INDEX + " ASC");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				infoList = new ArrayList<ChannelInfo>();
				do {
					ChannelInfo info = setDataFromCursor(cursor);
					infoList.add(info);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}

		return infoList;
	}

	public int delete(int id) {
		int ret = 0;

		String selection = COLUMN_ID + " = ?";
		String param = Integer.toString(id);
		ret = database.delete(TABLE_NAME, selection, new String[] { param });

		return ret;
	}

	public void deleteAll() {
		database.execSQL(FRODBHelper.DROP_TABLE_SQL + TABLE_NAME);
		database.execSQL(FRODBHelper.CREATE_FAVORITE_CHANNEL_TABLE_SQL);
	}

	public int getSize() {
		int size = 0;

		List<ChannelInfo> list = findAll();
		if (list != null) {
			size = list.size();
			list.clear();
			list = null;
		}

		return size;
	}

	private ChannelInfo setDataFromCursor(Cursor cursor) {
		ChannelInfo info = new ChannelInfo();
		int index = 0;

		info.setId(cursor.getInt(index++));
		info.setMode(cursor.getString(index++));
		info.setChannelId(cursor.getString(index++));
		info.setRange(cursor.getString(index++));
		info.setChannelName(cursor.getString(index++));
		info.setSource(cursor.getString(index++));
		info.setIndex(cursor.getInt(index++));
		info.setType(cursor.getInt(index++));
		info.setSkipControl(cursor.getInt(index++));
        info.setPermisson(cursor.getInt(index++));

		return info;
	}

}
