package jp.faraopro.play.domain;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCTemplateItem;

/**
 * 再生履歴テーブルへのアクセスを行うクラス
 * 
 * @author AIM
 * 
 */
public class FROTemplateFavoriteListDB {
	@SuppressWarnings("unused")
	private static final String TAG = FROTemplateFavoriteListDB.class.getSimpleName();
	@SuppressWarnings("unused")
	private static final boolean DEBUG = true;

	static final String TABLE_NAME = "TEMPLATE_FAVORITE_LIST_TABLE";
	static final String COLUMN_ID = "ID";
	static final String COLUMN_TEMPLATE_ID = "TEMPLATE_ID";
	static final String COLUMN_TEMPLATE_TYPE = "TEMPLATE_TYPE";
	static final String COLUMN_NAME = "NAME";
	static final String COLUMN_NAME_EN = "NAME_EN";

	private static final String[] COLUMNS = { COLUMN_ID, COLUMN_TEMPLATE_ID, COLUMN_TEMPLATE_TYPE, COLUMN_NAME,
			COLUMN_NAME_EN, };

	private SQLiteDatabase database;

	/**
	 * コンストラクタ
	 * 
	 * @param database
	 */
	public FROTemplateFavoriteListDB(SQLiteDatabase database) {
		this.database = database;
	}

	public long insert(MCTemplateItem item) {
		long rowId = -1;
		ContentValues values = new ContentValues();

		values.put(COLUMN_TEMPLATE_ID, item.getString(MCDefResult.MCR_KIND_TEMPLATE_ID));
		values.put(COLUMN_TEMPLATE_TYPE, item.getString(MCDefResult.MCR_KIND_TEMPLATE_TYPE));
		values.put(COLUMN_NAME, item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME));
		values.put(COLUMN_NAME_EN, item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN));

		// FRODebug.logD(getClass(), "---------- INSERT START ----------",
		// DEBUG);
		// FRODebug.logD(
		// getClass(),
		// "MCR_KIND_TEMPLATE_ID = "
		// + item.getString(MCDefResult.MCR_KIND_TEMPLATE_ID),
		// DEBUG);
		// FRODebug.logD(
		// getClass(),
		// "MCR_KIND_TEMPLATE_TYPE = "
		// + item.getString(MCDefResult.MCR_KIND_TEMPLATE_TYPE),
		// DEBUG);
		// FRODebug.logD(
		// getClass(),
		// "MCR_KIND_CHANNELITEM_NAME = "
		// + item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME),
		// DEBUG);
		// FRODebug.logD(
		// getClass(),
		// "MCR_KIND_CHANNELITEM_NAME_EN = "
		// + item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN),
		// DEBUG);
		// FRODebug.logD(getClass(), "---------- INSERT END ----------", DEBUG);

		rowId = database.insert(TABLE_NAME, null, values);

		return rowId;
	}

	public int update(MCTemplateItem item) {
		String selection = COLUMN_TEMPLATE_ID + " = ?";
		String param = item.getString(MCDefResult.MCR_KIND_TEMPLATE_ID);
		ContentValues values = new ContentValues();

		values.put(COLUMN_TEMPLATE_ID, item.getString(MCDefResult.MCR_KIND_TEMPLATE_ID));
		values.put(COLUMN_TEMPLATE_TYPE, item.getString(MCDefResult.MCR_KIND_TEMPLATE_TYPE));
		values.put(COLUMN_NAME, item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME));
		values.put(COLUMN_NAME_EN, item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN));

		return database.update(TABLE_NAME, values, selection, new String[] { param });
	}

	public MCTemplateItem find(String id) {
		MCTemplateItem item = null;
		String selection = COLUMN_TEMPLATE_ID + " = ?";

		Cursor cursor = database.query(TABLE_NAME, COLUMNS, selection, new String[] { id }, null, null, null);

		// FRODebug.logD(getClass(), "---------- FIND START ----------", DEBUG);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				item = setDataFromCursor(cursor);

				// FRODebug.logD(
				// getClass(),
				// "MCR_KIND_TEMPLATE_ID = "
				// + item.getString(MCDefResult.MCR_KIND_TEMPLATE_ID),
				// DEBUG);
				// FRODebug.logD(
				// getClass(),
				// "MCR_KIND_TEMPLATE_TYPE = "
				// + item.getString(MCDefResult.MCR_KIND_TEMPLATE_TYPE),
				// DEBUG);
				// FRODebug.logD(
				// getClass(),
				// "MCR_KIND_CHANNELITEM_NAME = "
				// + item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME),
				// DEBUG);
				// FRODebug.logD(
				// getClass(),
				// "MCR_KIND_CHANNELITEM_NAME_EN = "
				// + item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN),
				// DEBUG);

			}
			cursor.close();
		}

		// FRODebug.logD(getClass(), "---------- FIND END ----------", DEBUG);

		return item;
	}

	public List<MCTemplateItem> findAll() {
		ArrayList<MCTemplateItem> itemList = null;
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_TEMPLATE_ID + " ASC");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				itemList = new ArrayList<MCTemplateItem>();
				// FRODebug.logD(getClass(),
				// "---------- FINDALL START ----------", DEBUG);
				do {
					MCTemplateItem item = setDataFromCursor(cursor);
					itemList.add(item);

					// FRODebug.logD(
					// getClass(),
					// "MCR_KIND_TEMPLATE_ID = "
					// + item.getString(MCDefResult.MCR_KIND_TEMPLATE_ID),
					// DEBUG);
					// FRODebug.logD(
					// getClass(),
					// "MCR_KIND_TEMPLATE_TYPE = "
					// + item.getString(MCDefResult.MCR_KIND_TEMPLATE_TYPE),
					// DEBUG);
					// FRODebug.logD(
					// getClass(),
					// "MCR_KIND_CHANNELITEM_NAME = "
					// + item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME),
					// DEBUG);
					// FRODebug.logD(
					// getClass(),
					// "MCR_KIND_CHANNELITEM_NAME_EN = "
					// +
					// item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN),
					// DEBUG);

				} while (cursor.moveToNext());
				// FRODebug.logD(getClass(), "---------- FINDALL END
				// ----------",
				// DEBUG);
			}
			cursor.close();
		}

		return itemList;
	}

	public int delete(String id) {
		int ret = 0;

		String selection = COLUMN_TEMPLATE_ID + " = ?";
		ret = database.delete(TABLE_NAME, selection, new String[] { id });

		return ret;
	}

	public void deleteAll() {
		database.execSQL(FRODBHelper.DROP_TABLE_SQL + TABLE_NAME);
		database.execSQL(FRODBHelper.CREATE_TEMPLATE_FAVORITE_LIST_TABLE_SQL);
	}

	public int getSize() {
		int size = 0;

		List<MCTemplateItem> list = findAll();
		if (list != null) {
			size = list.size();
			list.clear();
			list = null;
		}

		return size;
	}

	private MCTemplateItem setDataFromCursor(Cursor cursor) {
		MCTemplateItem item = new MCTemplateItem();
		int index = 0;

		// DB内のIDは不要のためスルー
		index++;
		item.setString(MCDefResult.MCR_KIND_TEMPLATE_ID, cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_TEMPLATE_TYPE, cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_CHANNELITEM_NAME, cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN, cursor.getString(index++));

		return item;
	}

}
