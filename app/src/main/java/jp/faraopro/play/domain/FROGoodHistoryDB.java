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
public class FROGoodHistoryDB implements IFROMusicInfoDB {
	static final String TABLE_NAME = "GOOD_HISTORY_TABLE";
	static final String COLUMN_ID = "ID";
	static final String COLUMN_ARTIST = "ARTIST_NAME";
	static final String COLUMN_TITLE = "TITLE";
	static final String COLUMN_RELEASE = "RELEASED_DATE";
	static final String COLUMN_GENRE = "GENRE";
	static final String COLUMN_INFO = "MUSIC_INFO";
	static final String COLUMN_URL = "AFFILIATE_URL";
	static final String COLUMN_JACKET = "JACKET_PATH";
	static final String COLUMN_THUMBNAIL = "THUMBNAL_PATH";
	static final String COLUMN_KEYWORD = "KWYWORD";
	static final String COLUMN_RATING = "USER_RATING";
	static final String COLUMN_TIMESTAMP = "TIME_STAMP";
	static final String COLUMN_ARTIST_ID = "ARTIST_ID";
	static final String COLUMN_PARAMS = "PARAMS";

	private static final String[] COLUMNS = { COLUMN_ID, COLUMN_RATING, COLUMN_TIMESTAMP, COLUMN_PARAMS, COLUMN_ARTIST,
			COLUMN_TITLE, COLUMN_RELEASE, COLUMN_GENRE, COLUMN_INFO, COLUMN_URL, COLUMN_JACKET, COLUMN_THUMBNAIL,
			COLUMN_KEYWORD, COLUMN_ARTIST_ID };

	private SQLiteDatabase database;

	/**
	 * コンストラクタ
	 * 
	 * @param database
	 */
	public FROGoodHistoryDB(SQLiteDatabase database) {
		this.database = database;
	}

	@Override
	public long insert(MusicInfoEx infoEx) {
		long rowId = -1;
		ContentValues values = new ContentValues();
		MusicInfo info = infoEx.getMusicInfo();
		values.put(COLUMN_ID, infoEx.getId());
		values.put(COLUMN_RATING, infoEx.getValue());
		values.put(COLUMN_TIMESTAMP, infoEx.getTimestamp());
		values.put(COLUMN_PARAMS, infoEx.getParams());
		values.put(COLUMN_ARTIST, info.getArtist());
		values.put(COLUMN_TITLE, info.getTitle());
		values.put(COLUMN_RELEASE, info.getRelese());
		values.put(COLUMN_GENRE, info.getGenre());
		values.put(COLUMN_INFO, info.getInfo());
		values.put(COLUMN_URL, info.getUrl());
		values.put(COLUMN_JACKET, "");
		values.put(COLUMN_THUMBNAIL, info.getThumb());
		values.put(COLUMN_KEYWORD, "");
		values.put(COLUMN_ARTIST_ID, info.getArtistId());
		rowId = database.insert(TABLE_NAME, null, values);

		return rowId;
	}

	@Override
	public int update(int id, MusicInfoEx app) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MusicInfoEx find(int id) {
		MusicInfoEx info = null;
		String selection = COLUMN_ID + " = ?";
		String[] selectionArgs = new String[] { Integer.toString(id) };
		String limit = "0,1";
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, selection, selectionArgs, null, null,
				COLUMN_TIMESTAMP + " DESC", limit);
		if (cursor != null && cursor.moveToNext()) {
			info = setDataFromCursor(cursor);
			cursor.close();
		}

		return info;
	}

	@Override
	public List<MusicInfoEx> findAll() {
		ArrayList<MusicInfoEx> infoExList = null;
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_TIMESTAMP + " DESC");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				infoExList = new ArrayList<MusicInfoEx>();
				do {
					MusicInfoEx infoEx = setDataFromCursor(cursor);
					infoExList.add(infoEx);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}

		return infoExList;
	}

	@Override
	public int delete(int id) {
		int ret = 0;

		String selection = COLUMN_ID + " = ?";
		String param = Integer.toString(id);
		ret = database.delete(TABLE_NAME, selection, new String[] { param });

		return ret;
	}

	public MusicInfoEx findOldest(boolean rev) {
		String order = " ASC";
		if (rev)
			order = " DESC";
		MusicInfoEx infoEx = null;
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_TIMESTAMP + order);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				infoEx = setDataFromCursor(cursor);
			}
			cursor.close();
		}

		return infoEx;
	}

	@Override
	public void deleteAll() {
		database.execSQL(FRODBHelper.DROP_TABLE_SQL + TABLE_NAME);
		database.execSQL(FRODBHelper.CREATE_GOOD_HISTORY_TABLE_SQL);
	}

	@Override
	public int getSize() {
		int size = -1;

		List<MusicInfoEx> list = findAll();
		if (list != null) {
			size = list.size();
			list.clear();
			list = null;
		}

		return size;
	}

	private MusicInfoEx setDataFromCursor(Cursor cursor) {
		MusicInfoEx infoEx = new MusicInfoEx();
		MusicInfo info = new MusicInfo();
		int index = 0;

		infoEx.setId(cursor.getInt(index++));
		infoEx.setValue(cursor.getString(index++));
		infoEx.setTimestamp(cursor.getLong(index++));
		infoEx.setParams(cursor.getString(index++));
		info.setArtist(cursor.getString(index++));
		info.setTitle(cursor.getString(index++));
		info.setRelese(cursor.getString(index++));
		info.setGenre(cursor.getString(index++));
		info.setInfo(cursor.getString(index++));
		info.setUrl(cursor.getString(index++));
		info.setArtwork(cursor.getString(index++));
		info.setThumb(cursor.getString(index++));
		info.setUrlSearch(cursor.getString(index++));
		info.setArtistId(cursor.getString(index++));
		infoEx.setMusicInfo(info);

		return infoEx;
	}

}
