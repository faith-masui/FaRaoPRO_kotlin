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
public class FROFavoriteTemplateDB {
	@SuppressWarnings("unused")
	private static final String TAG = FROFavoriteTemplateDB.class.getSimpleName();
	@SuppressWarnings("unused")
	private static final boolean DEBUG = true;

	static final String TABLE_NAME = "FAVORITE_TEMPLATE_TABLE";
	static final String COLUMN_ID = "ID";
	static final String COLUMN_TEMPLATE_ID = "TEMPLATE_ID";
	static final String COLUMN_NAME = "NAME";
	static final String COLUMN_NAME_EN = "NAME_EN";
	static final String COLUMN_SOURCE_TYPE = "SOURCE_TYPE";
	static final String COLUMN_SOURCE_NAME = "SOURCE_NAME";
	static final String COLUMN_SOURCE_URL = "SOURCE_SOURCE_URL";
	static final String COLUMN_PARENT_ID = "PARENT_ID";

	private static final String[] COLUMNS = { COLUMN_ID, COLUMN_TEMPLATE_ID, COLUMN_NAME, COLUMN_NAME_EN,
			COLUMN_SOURCE_TYPE, COLUMN_SOURCE_NAME, COLUMN_SOURCE_URL, COLUMN_PARENT_ID };

	private SQLiteDatabase database;

	/**
	 * コンストラクタ
	 * 
	 * @param database
	 */
	public FROFavoriteTemplateDB(SQLiteDatabase database) {
		this.database = database;
	}

	public long insert(TemplateChannelInfo info) {
		long rowId = -1;
		ContentValues values = new ContentValues();
		values.put(COLUMN_TEMPLATE_ID, info.getTemplateId());
		values.put(COLUMN_NAME, info.getChannelName());
		values.put(COLUMN_NAME_EN, info.getNameEn());
		values.put(COLUMN_SOURCE_TYPE, info.getSourceType());
		values.put(COLUMN_SOURCE_NAME, info.getSourceName());
		values.put(COLUMN_SOURCE_URL, info.getSourceUrl());
		values.put(COLUMN_PARENT_ID, info.getParentId());
		rowId = database.insert(TABLE_NAME, null, values);
		return rowId;
	}

	public int update(TemplateChannelInfo info) {
		String selection = COLUMN_TEMPLATE_ID + " = ?";
		String param = info.getTemplateId();
		ContentValues values = new ContentValues();
		values.put(COLUMN_TEMPLATE_ID, info.getTemplateId());
		values.put(COLUMN_NAME, info.getChannelName());
		values.put(COLUMN_NAME_EN, info.getNameEn());
		values.put(COLUMN_SOURCE_TYPE, info.getSourceType());
		values.put(COLUMN_SOURCE_NAME, info.getSourceName());
		values.put(COLUMN_SOURCE_URL, info.getSourceUrl());
		values.put(COLUMN_PARENT_ID, info.getParentId());
		return database.update(TABLE_NAME, values, selection, new String[] { param });
	}

	public TemplateChannelInfo find(String id) {
		TemplateChannelInfo info = null;
		String selection = COLUMN_TEMPLATE_ID + " = ?";

		Cursor cursor = database.query(TABLE_NAME, COLUMNS, selection, new String[] { id }, null, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				info = setDataFromCursor(cursor);
			}
			cursor.close();
		}
		return info;
	}

	public List<TemplateChannelInfo> findAll() {
		ArrayList<TemplateChannelInfo> infoList = null;
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_TEMPLATE_ID + " ASC");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
                infoList = new ArrayList<>();
				do {
					TemplateChannelInfo info = setDataFromCursor(cursor);
					infoList.add(info);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return infoList;
	}

	public List<TemplateChannelInfo> findByParent(String parentId) {
		ArrayList<TemplateChannelInfo> infoList = null;
		String selection = COLUMN_PARENT_ID + " = ?";
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, selection, new String[] { parentId }, null, null,
				COLUMN_TEMPLATE_ID + " ASC");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
                infoList = new ArrayList<>();
				do {
					TemplateChannelInfo info = setDataFromCursor(cursor);
					infoList.add(info);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return infoList;
	}

	public int delete(int id) {
		int ret = 0;
		String selection = COLUMN_TEMPLATE_ID + " = ?";
		String param = Integer.toString(id);
		ret = database.delete(TABLE_NAME, selection, new String[] { param });
		return ret;
	}

	public int deleteByParent(String parentId) {
		int ret = 0;
		String selection = COLUMN_PARENT_ID + " = ?";
		ret = database.delete(TABLE_NAME, selection, new String[] { parentId });
		return ret;
	}

	public void deleteAll() {
		database.execSQL(FRODBHelper.DROP_TABLE_SQL + TABLE_NAME);
		database.execSQL(FRODBHelper.CREATE_FAVORITE_TEMPLATE_TABLE_SQL);
	}

	public int getSize() {
		int size = 0;
		List<TemplateChannelInfo> list = findAll();
		if (list != null) {
			size = list.size();
			list.clear();
		}
		return size;
	}

	private TemplateChannelInfo setDataFromCursor(Cursor cursor) {
		TemplateChannelInfo info = new TemplateChannelInfo();
		int index = 0;
		index++;
		info.setTemplateId(cursor.getString(index++));
		info.setChannelName(cursor.getString(index++));
		info.setNameEn(cursor.getString(index++));
		info.setSourceType(cursor.getString(index++));
		info.setSourceName(cursor.getString(index++));
		info.setSourceUrl(cursor.getString(index++));
		info.setParentId(cursor.getString(index++));
		return info;
	}

}
