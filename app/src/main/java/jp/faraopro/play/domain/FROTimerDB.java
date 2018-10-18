package jp.faraopro.play.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import jp.faraopro.play.model.TimerInfo;

/**
 * 再生履歴テーブルへのアクセスを行うクラス
 * 
 * @author AIM
 * 
 */
public class FROTimerDB implements ITimerDB {
	@SuppressWarnings("unused")
	private static final String TAG = FROTimerDB.class.getSimpleName();
	@SuppressWarnings("unused")
	private static final boolean DEBUG = true;

	static final String TABLE_NAME = "TIMER_TABLE";
	static final String COLUMN_ID = "ID";
	static final String COLUMN_TIME = "TIME";
	static final String COLUMN_TYPE = "TYPE";
	static final String COLUMN_INDEX = "INDEX_NUMBER";
	static final String COLUMN_WEEK = "WEEK";
	static final String COLUMN_NAME = "NAME";
	static final String COLUMN_RESOURCE = "RESOURCE";
	static final String COLUMN_RESOURCE_NAME = "RESOURCE_NAME";
    static final String COLUMN_MODE = "MODE";
    static final String COLUMN_CHANNEL_ID = "CHANNEL_ID";
    static final String COLUMN_RANGE = "RANGE";
    static final String COLUMN_PERMISSION = "PERMISSION";

	private static final String[] COLUMNS = { COLUMN_ID, COLUMN_TIME, COLUMN_TYPE, COLUMN_INDEX, COLUMN_WEEK,
            COLUMN_NAME, COLUMN_RESOURCE, COLUMN_RESOURCE_NAME, COLUMN_MODE, COLUMN_CHANNEL_ID, COLUMN_RANGE,
            COLUMN_PERMISSION};

    public static final String[] COLUMNS_VERSION_9 = {COLUMN_ID, COLUMN_TIME, COLUMN_TYPE, COLUMN_INDEX, COLUMN_WEEK,
			COLUMN_NAME, COLUMN_RESOURCE, COLUMN_RESOURCE_NAME, };

	private SQLiteDatabase database;

	/**
	 * コンストラクタ
	 * 
	 * @param database
	 */
	public FROTimerDB(SQLiteDatabase database) {
		this.database = database;
	}

	@Override
	public long insert(TimerInfo info) {
		int lastIndex = this.getSize();
		long rowId = -1;
		ContentValues values = new ContentValues();

		values.put(COLUMN_TIME, info.getTime());
		values.put(COLUMN_TYPE, info.getType());
		values.put(COLUMN_INDEX, lastIndex);
		values.put(COLUMN_WEEK, Byte.toString(info.getWeek()));
		values.put(COLUMN_NAME, info.getName());
		values.put(COLUMN_RESOURCE, info.getResource());
		values.put(COLUMN_RESOURCE_NAME, info.getResourceName());
        values.put(COLUMN_MODE, info.getMode());
        values.put(COLUMN_CHANNEL_ID, info.getChannelId());
        values.put(COLUMN_RANGE, info.getRange());
        values.put(COLUMN_PERMISSION, info.getPermission());
        rowId = database.insert(TABLE_NAME, null, values);
		return rowId;
	}

	@Override
	public int update(TimerInfo info) {
		String selection = COLUMN_ID + " = ?";
		String param = Integer.toString(info.getId());
		ContentValues values = new ContentValues();

		values.put(COLUMN_TIME, info.getTime());
		values.put(COLUMN_TYPE, info.getType());
		values.put(COLUMN_INDEX, info.getIndex());
		values.put(COLUMN_WEEK, Byte.toString(info.getWeek()));
		values.put(COLUMN_NAME, info.getName());
		values.put(COLUMN_RESOURCE, info.getResource());
		values.put(COLUMN_RESOURCE_NAME, info.getResourceName());
        values.put(COLUMN_MODE, info.getMode());
        values.put(COLUMN_CHANNEL_ID, info.getChannelId());
        values.put(COLUMN_RANGE, info.getRange());
        values.put(COLUMN_PERMISSION, info.getPermission());
		return database.update(TABLE_NAME, values, selection, new String[] { param });
	}

	@Override
    public TimerInfo findNext(byte week, int time, boolean isExceptNow) {
        TimerInfo info = null;
        // 引数の week と COLUMN_WEEK をビット演算することで、week 以外のカラムだった場合 0 が返る
        String selection = isExceptNow ? COLUMN_TIME + " >= ? AND " + "(" + COLUMN_WEEK + " & ?) <> 0"
                : COLUMN_TIME + " > ? AND " + "(" + COLUMN_WEEK + " & ?) <> 0";
		String param1 = Integer.toString(time);
		String param2 = Byte.toString(week);
		String[] selectionArg = new String[] { param1, param2 };

		Cursor cursor = database.query(TABLE_NAME, COLUMNS, selection, selectionArg, null, null, COLUMN_TIME + " ASC");
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToFirst()) {
				info = setDataFromCursor(cursor);

				// if (info != null) {
				// FRODebug.logD(getClass(), "---------- FIND NEXT ----------",
				// DEBUG);
				// FRODebug.logD(getClass(), "COLUMN_ID = " + info.getId(),
				// DEBUG);
				// FRODebug.logD(getClass(), "COLUMN_TIME = " + info.getTime(),
				// DEBUG);
				// FRODebug.logD(getClass(), "COLUMN_TYPE = " + info.getType(),
				// DEBUG);
				// FRODebug.logD(getClass(), "COLUMN_INDEX = " +
				// info.getIndex(), DEBUG);
				// FRODebug.logD(getClass(), "COLUMN_WEEK = " +
				// Byte.toString(info.getWeek()), DEBUG);
				// FRODebug.logD(getClass(), "COLUMN_NAME = " + info.getName(),
				// DEBUG);
				// FRODebug.logD(getClass(), "COLUMN_RESOURCE = " +
				// info.getResource(), DEBUG);
				// FRODebug.logD(getClass(), "COLUMN_RESOURCE_NAME = " +
				// info.getResourceName(), DEBUG);
				// FRODebug.logD(getClass(), "---------- END ----------",
				// DEBUG);
				// }

			}
		}
		if (cursor != null)
			cursor.close();

		return info;
	}

	@Override
	public TimerInfo findPrevious(byte week, int time) {
		TimerInfo info = null;
		String selection = COLUMN_TIME + " <= ? AND " + "(" + COLUMN_WEEK + " & ?) <> 0";
		String param1 = Integer.toString(time);
		String param2 = Byte.toString(week);
		String[] selectionArg = new String[] { param1, param2 };

		Cursor cursor = database.query(TABLE_NAME, COLUMNS, selection, selectionArg, null, null, COLUMN_TIME + " DESC");
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToFirst()) {
				info = setDataFromCursor(cursor);
			}
		}
		if (cursor != null)
			cursor.close();

		return info;
	}

	@Override
	public List<TimerInfo> findAll() {
		ArrayList<TimerInfo> infoList = null;
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_INDEX + " ASC");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
                infoList = new ArrayList<>();
                do {
                    TimerInfo info = setDataFromCursor(cursor);
                    infoList.add(info);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return infoList;
    }

    public List<TimerInfo> findAllOrderByTime() {
        ArrayList<TimerInfo> infoList = null;
        Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_TIME + " ASC");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                infoList = new ArrayList<>();
				do {
					TimerInfo info = setDataFromCursor(cursor);
					infoList.add(info);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}

		return infoList;
	}

	public List<TimerInfo> check(TimerInfo info) {
		ArrayList<TimerInfo> infoList = new ArrayList<TimerInfo>();
		String selection = COLUMN_TIME + " = ? AND " + "(" + COLUMN_WEEK + " & ?) <> 0 AND " + COLUMN_ID + " != ?";

		// String selection = COLUMN_TIME + " = ? AND " + "(" + COLUMN_WEEK
		// + " & ?) <> 0 AND " + COLUMN_ID + " NOT IN (1,2,3)";

		String time = Integer.toString(info.getTime());
		String week = Byte.toString(info.getWeek());
		String id = Integer.toString(info.getId());
		String[] selectionArg = new String[] { time, week, id };

		Cursor cursor = database.query(TABLE_NAME, COLUMNS, selection, selectionArg, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToFirst()) {
				do {
					TimerInfo data = setDataFromCursor(cursor);
					infoList.add(data);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}

		return infoList;
	}

	@Override
	public int delete(String id) {
		int ret = 0;

		String selection = COLUMN_ID + " = ?";
		ret = database.delete(TABLE_NAME, selection, new String[] { id });

		return ret;
	}

	@Override
	public void deleteAll() {
		database.execSQL(FRODBHelper.DROP_TABLE_SQL + TABLE_NAME);
		database.execSQL(FRODBHelper.CREATE_TIMER_TABLE_SQL);
	}

	@Override
	public int getSize() {
		int size = 0;
		List<TimerInfo> list = findAll();
		if (list != null) {
			size = list.size();
			list.clear();
		}

		return size;
	}

	private TimerInfo setDataFromCursor(Cursor cursor) {
		TimerInfo info = new TimerInfo();
		int index = 0;

		info.setId(cursor.getInt(index++));
		info.setTime(cursor.getInt(index++));
		info.setType(cursor.getInt(index++));
		info.setIndex(cursor.getInt(index++));
		info.setWeek(Byte.parseByte(cursor.getString(index++)));
		info.setName(cursor.getString(index++));
		info.setResource(cursor.getString(index++));
		info.setResourceName(cursor.getString(index++));
        info.setMode(cursor.getInt(index++));
        info.setChannelId(cursor.getInt(index++));
        info.setRange(cursor.getString(index++));
        info.setPermission(cursor.getInt(index++));
		return info;
	}

}
