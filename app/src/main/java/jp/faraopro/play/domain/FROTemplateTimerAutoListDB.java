package jp.faraopro.play.domain;

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
public class FROTemplateTimerAutoListDB {
	@SuppressWarnings("unused")
	private static final String TAG = FROTemplateTimerAutoListDB.class.getSimpleName();
	@SuppressWarnings("unused")
	private static final boolean DEBUG = true;

	static final String TABLE_NAME = "TEMPLATE_TIMER_AUTO_LIST_TABLE";
	static final String COLUMN_ID = "ID";
	static final String COLUMN_TEMPLATE_ID = "TEMPLATE_ID";
	static final String COLUMN_TEMPLATE_TYPE = "TEMPLATE_TYPE";
	static final String COLUMN_NAME = "NAME";
	static final String COLUMN_NAME_EN = "NAME_EN";
	static final String COLUMN_DIGEST = "DIGEST";
	static final String COLUMN_ACTION = "ACTION";
	static final String COLUMN_RULE = "RULE";

	private static final String[] COLUMNS = { COLUMN_ID, COLUMN_TEMPLATE_ID, COLUMN_TEMPLATE_TYPE, COLUMN_NAME,
			COLUMN_NAME_EN, COLUMN_DIGEST, COLUMN_ACTION, COLUMN_RULE };

	private SQLiteDatabase database;

	/**
	 * コンストラクタ
	 * 
	 * @param database
	 */
	public FROTemplateTimerAutoListDB(SQLiteDatabase database) {
		this.database = database;
	}

	public long insert(MCTemplateItem item) {
		long rowId = -1;
		ContentValues values = new ContentValues();

		values.put(COLUMN_TEMPLATE_ID, item.getString(MCDefResult.MCR_KIND_TEMPLATE_ID));
		values.put(COLUMN_TEMPLATE_TYPE, item.getString(MCDefResult.MCR_KIND_TEMPLATE_TYPE));
		values.put(COLUMN_NAME, item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME));
		values.put(COLUMN_NAME_EN, item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN));
		values.put(COLUMN_DIGEST, item.getString(MCDefResult.MCR_KIND_TEMPLATE_DIGEST));
		values.put(COLUMN_ACTION, item.getString(MCDefResult.MCR_KIND_TEMPLATE_ACTION));
		values.put(COLUMN_RULE, item.getString(MCDefResult.MCR_KIND_TEMPLATE_RULE));

		// FRODebug.logD(getClass(), "---------- INSERT START ----------",
		// DEBUG);
		// FRODebug.logD(getClass(), "COLUMN_TEMPLATE_ID = " +
		// item.getString(MCDefResult.MCR_KIND_TEMPLATE_ID), DEBUG);
		// FRODebug.logD(getClass(), "COLUMN_TEMPLATE_TYPE = " +
		// item.getString(MCDefResult.MCR_KIND_TEMPLATE_TYPE),
		// DEBUG);
		// FRODebug.logD(getClass(), "COLUMN_NAME = " +
		// item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME), DEBUG);
		// FRODebug.logD(getClass(), "COLUMN_NAME_EN = " +
		// item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN),
		// DEBUG);
		// FRODebug.logD(getClass(), "COLUMN_DIGEST = " +
		// item.getString(MCDefResult.MCR_KIND_TEMPLATE_DIGEST), DEBUG);
		// FRODebug.logD(getClass(), "COLUMN_ACTION = " +
		// item.getString(MCDefResult.MCR_KIND_TEMPLATE_ACTION), DEBUG);
		// FRODebug.logD(getClass(), "COLUMN_RULE = " +
		// item.getString(MCDefResult.MCR_KIND_TEMPLATE_RULE), DEBUG);
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
		values.put(COLUMN_DIGEST, item.getString(MCDefResult.MCR_KIND_TEMPLATE_DIGEST));
		values.put(COLUMN_ACTION, item.getString(MCDefResult.MCR_KIND_TEMPLATE_ACTION));
		values.put(COLUMN_RULE, item.getString(MCDefResult.MCR_KIND_TEMPLATE_RULE));

		return database.update(TABLE_NAME, values, selection, new String[] { param });
	}

	// public MCTemplateItem find(String id) {
	// MCTemplateItem item = null;
	// String selection = COLUMN_TEMPLATE_ID + " = ?";
	// Cursor cursor = database.query(TABLE_NAME, COLUMNS, selection, new
	// String[] { id }, null, null, null);
	// FRODebug.logD(getClass(), "---------- FIND START ----------", DEBUG);
	// if (cursor != null) {
	// if (cursor.moveToFirst()) {
	// item = setDataFromCursor(cursor);
	// FRODebug.logD(getClass(), "COLUMN_TEMPLATE_ID = " +
	// item.getString(MCDefResult.MCR_KIND_TEMPLATE_ID),
	// DEBUG);
	// FRODebug.logD(getClass(),
	// "COLUMN_TEMPLATE_TYPE = " +
	// item.getString(MCDefResult.MCR_KIND_TEMPLATE_TYPE), DEBUG);
	// FRODebug.logD(getClass(), "COLUMN_NAME = " +
	// item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME),
	// DEBUG);
	// FRODebug.logD(getClass(),
	// "COLUMN_NAME_EN = " +
	// item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN), DEBUG);
	// FRODebug.logD(getClass(), "COLUMN_DIGEST = " +
	// item.getString(MCDefResult.MCR_KIND_TEMPLATE_DIGEST),
	// DEBUG);
	// FRODebug.logD(getClass(), "COLUMN_ACTION = " +
	// item.getString(MCDefResult.MCR_KIND_TEMPLATE_ACTION),
	// DEBUG);
	// FRODebug.logD(getClass(), "COLUMN_RULE = " +
	// item.getString(MCDefResult.MCR_KIND_TEMPLATE_RULE), DEBUG);
	// }
	// cursor.close();
	// }
	// FRODebug.logD(getClass(), "---------- FIND END ----------", DEBUG);
	// return item;
	// }

	public MCTemplateItem find() {
		MCTemplateItem item = null;
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_TEMPLATE_ID + " ASC");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				item = setDataFromCursor(cursor);

				// FRODebug.logD(getClass(), "---------- FIND START ----------",
				// DEBUG);
				// FRODebug.logD(getClass(), "MCR_KIND_TEMPLATE_ID = " +
				// item.getString(MCDefResult.MCR_KIND_TEMPLATE_ID),
				// DEBUG);
				// FRODebug.logD(getClass(),
				// "MCR_KIND_TEMPLATE_TYPE = " +
				// item.getString(MCDefResult.MCR_KIND_TEMPLATE_TYPE), DEBUG);
				// FRODebug.logD(getClass(),
				// "MCR_KIND_CHANNELITEM_NAME = " +
				// item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME),
				// DEBUG);
				// FRODebug.logD(getClass(),
				// "MCR_KIND_CHANNELITEM_NAME_EN = " +
				// item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN),
				// DEBUG);
				// FRODebug.logD(getClass(), "COLUMN_DIGEST = " +
				// item.getString(MCDefResult.MCR_KIND_TEMPLATE_DIGEST),
				// DEBUG);
				// FRODebug.logD(getClass(), "COLUMN_ACTION = " +
				// item.getString(MCDefResult.MCR_KIND_TEMPLATE_ACTION),
				// DEBUG);
				// FRODebug.logD(getClass(), "COLUMN_RULE = " +
				// item.getString(MCDefResult.MCR_KIND_TEMPLATE_RULE),
				// DEBUG);
				// FRODebug.logD(getClass(), "---------- FIND END ----------",
				// DEBUG);
			}
			cursor.close();
		}

		return item;
	}

	// public List<MCTemplateItem> findAll() {
	// ArrayList<MCTemplateItem> itemList = null;
	// Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null,
	// null, COLUMN_TEMPLATE_ID + " ASC");
	// if (cursor != null) {
	// if (cursor.moveToFirst()) {
	// itemList = new ArrayList<MCTemplateItem>();
	// FRODebug.logD(getClass(), "---------- FINDALL START ----------", DEBUG);
	// do {
	// MCTemplateItem item = setDataFromCursor(cursor);
	// itemList.add(item);
	//
	// FRODebug.logD(getClass(),
	// "MCR_KIND_TEMPLATE_ID = " +
	// item.getString(MCDefResult.MCR_KIND_TEMPLATE_ID), DEBUG);
	// FRODebug.logD(getClass(),
	// "MCR_KIND_TEMPLATE_TYPE = " +
	// item.getString(MCDefResult.MCR_KIND_TEMPLATE_TYPE), DEBUG);
	// FRODebug.logD(getClass(),
	// "MCR_KIND_CHANNELITEM_NAME = " +
	// item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME),
	// DEBUG);
	// FRODebug.logD(
	// getClass(),
	// "MCR_KIND_CHANNELITEM_NAME_EN = "
	// + item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN), DEBUG);
	// FRODebug.logD(getClass(),
	// "COLUMN_DIGEST = " +
	// item.getString(MCDefResult.MCR_KIND_TEMPLATE_DIGEST), DEBUG);
	// FRODebug.logD(getClass(),
	// "COLUMN_ACTION = " +
	// item.getString(MCDefResult.MCR_KIND_TEMPLATE_ACTION), DEBUG);
	// FRODebug.logD(getClass(), "COLUMN_RULE = " +
	// item.getString(MCDefResult.MCR_KIND_TEMPLATE_RULE),
	// DEBUG);
	//
	// } while (cursor.moveToNext());
	// FRODebug.logD(getClass(), "---------- FINDALL END ----------", DEBUG);
	// }
	// cursor.close();
	// }
	//
	// return itemList;
	// }

	public int delete(String id) {
		int ret = 0;

		String selection = COLUMN_TEMPLATE_ID + " = ?";
		ret = database.delete(TABLE_NAME, selection, new String[] { id });

		return ret;
	}

	public void clearDigest() {
		MCTemplateItem templateItem = find();
		if (templateItem != null) {
			templateItem.setString(MCDefResult.MCR_KIND_TEMPLATE_DIGEST, null);
			update(templateItem);
		}
	}

	public void deleteAll() {
		database.execSQL(FRODBHelper.DROP_TABLE_SQL + TABLE_NAME);
		database.execSQL(FRODBHelper.CREATE_TEMPLATE_TIMER_AUTO_LIST_TABLE_SQL);
	}
	// public int getSize() {
	// int size = 0;
	//
	// List<MCTemplateItem> list = findAll();
	// if (list != null) {
	// size = list.size();
	// list.clear();
	// list = null;
	// }
	//
	// return size;
	// }

	private MCTemplateItem setDataFromCursor(Cursor cursor) {
		MCTemplateItem item = new MCTemplateItem();
		int index = 0;

		// DB内のIDは不要のためスルー
		index++;
		item.setString(MCDefResult.MCR_KIND_TEMPLATE_ID, cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_TEMPLATE_TYPE, cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_CHANNELITEM_NAME, cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN, cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_TEMPLATE_DIGEST, cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_TEMPLATE_ACTION, cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_TEMPLATE_RULE, cursor.getString(index++));

		return item;
	}

}
