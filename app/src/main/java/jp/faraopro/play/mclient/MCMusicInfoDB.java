package jp.faraopro.play.mclient;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import jp.faraopro.play.util.Utils;

/**
 * 楽曲情報DB管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCMusicInfoDB implements IMCMusicInfoDB {

	/**
	 * DB名称
	 */
	public static final String TABLE_NAME = "MusicInfoData";
	/**
	 * データID
	 */
	public static final String COLUM_DATAID = "DataId";

	/**
	 * カラム(広告出現頻度)
	 */
	public static final String COLUM_ADVERTISE_RATIO = "AdvertiseRatio";
	/**
	 * カラム(残スキップ回数)
	 */
	public static final String COLUM_SKIP_REMAINING = "SkipRmaining";
	/**
	 * カラム(DL済みコンテンツのパス)
	 */
	public static final String COLUM_OUTPUT_PATH = "OutputPath";
	/**
	 * カラム(エンコードに使用されたメソッド)
	 */
	public static final String COLUM_ENC_METHOD = "EncMethod";
	/**
	 * カラム(エンコードに使用されたKEY)
	 */
	public static final String COLUM_ENC_KEY = "EncKey";
	/**
	 * カラム(エンコードに使用されたIV)
	 */
	public static final String COLUM_ENC_IV = "EncIv";

	/**
	 * テスト
	 */
	public static final String COLUM_ENC_API_SIG = "ApiSig";

	/**
	 * カラム(DL完了時刻)
	 */
	public static final String COLUM_REG_TIME = "RegTime";
	/**
	 * カラム(再生した時間・msec)
	 */
	public static final String COLUM_PLAY_DURATION = "PlayDuration";

	/**
	 * カラム(Track詳細：trackId)
	 */
	public static final String COLUM_TRACK_ID = "trackId";

	/**
	 * カラム(Track詳細：タイトル)
	 */
	public static final String COLUM_TRACK_TITLE = "title";

	/**
	 * カラム(Track詳細：アーティスト)
	 */
	public static final String COLUM_TRACK_ARTIST = "artist";

	// 2011/11/11追加
	/**
	 * カラム(Track詳細：月間残再生回数)
	 */
	public static final String COLUM_PLAYABLE_TRACKS = "playableTracks";
	/**
	 * カラム(Track詳細：タイトル en)
	 */
	public static final String COLUM_TRACK_TITLE_EN = "title_en";
	/**
	 * カラム(Track詳細：アーティスト en)
	 */
	public static final String COLUM_TRACK_ARTIST_EN = "artist_en";
	/**
	 * カラム(Track詳細：リリース日)
	 */
	public static final String COLUM_TRACK_RELEASE_DATE = "release_date";
	/**
	 * カラム(Track詳細：ジャンル)
	 */
	public static final String COLUM_TRACK_GENRE = "genre";
	/**
	 * カラム(Track詳細：ジャンル en)
	 */
	public static final String COLUM_TRACK_GENRE_EN = "genre_en";
	/**
	 * カラム(Track詳細：description)
	 */
	public static final String COLUM_TRACK_DESCRIPTION = "description";
	/**
	 * カラム(Track詳細：description en)
	 */
	public static final String COLUM_TRACK_DESCRIPTION_EN = "description_en";
	/**
	 * カラム(Track詳細：アフィリエイトURL)
	 */
	public static final String COLUM_TRACK_AFFILIATE_URL = "affiliate_url";
	/**
	 * カラム(楽曲ファイルのステータス)
	 */
	public static final String COLUM_FILE_STATUS = "file_status";
	/**
	 * カラム(Track詳細：jacketId)
	 */
	public static final String COLUM_TRACK_JACKET_ID = "jacketId";
	/**
	 * カラム(Track詳細：album)
	 */
	public static final String COLUM_TRACK_ALBUM = "album";
	/**
	 * カラム(Track詳細：album_en)
	 */
	public static final String COLUM_TRACK_ALBUM_EN = "album_en";
	/**
	 * カラム(Track詳細：album_en)
	 */
	public static final String COLUM_TRACK_TRIAL_TIME = "trial_time";
	/**
	 * カラム(Track詳細：アーティストID)
	 */
	public static final String COLUM_TRACK_ARTIST_ID = "artist_id";
	/**
	 * カラム(Track詳細：チャンネル名)
	 */
	public static final String COLUM_CHANNEL_NAME = "channel_name";
	/**
	 * カラム(Track詳細：チャンネル名)
	 */
	public static final String COLUM_CHANNEL_NAME_EN = "channel_name_en";

	public static final String COLUM_REPLAY_GAIN = "replay_gain";

	private static final String[] COLUMNS = { COLUM_DATAID, COLUM_FILE_STATUS, COLUM_PLAYABLE_TRACKS,
			COLUM_ADVERTISE_RATIO, COLUM_SKIP_REMAINING, COLUM_OUTPUT_PATH, COLUM_ENC_METHOD, COLUM_ENC_KEY,
			COLUM_ENC_IV, COLUM_ENC_API_SIG, COLUM_REG_TIME, COLUM_PLAY_DURATION, COLUM_TRACK_ID, COLUM_TRACK_TITLE,
			COLUM_TRACK_TITLE_EN, COLUM_TRACK_ARTIST, COLUM_TRACK_ARTIST_EN, COLUM_TRACK_RELEASE_DATE,
			COLUM_TRACK_GENRE, COLUM_TRACK_GENRE_EN, COLUM_TRACK_DESCRIPTION, COLUM_TRACK_DESCRIPTION_EN,
			COLUM_TRACK_AFFILIATE_URL, COLUM_TRACK_JACKET_ID, COLUM_TRACK_ALBUM, COLUM_TRACK_ALBUM_EN,
			COLUM_TRACK_TRIAL_TIME, COLUM_TRACK_ARTIST_ID, COLUM_CHANNEL_NAME, COLUM_CHANNEL_NAME_EN,
			COLUM_REPLAY_GAIN };

	private SQLiteDatabase mDatabase;

	/**
	 * コンストラクタ
	 * 
	 * @param database
	 */
	public MCMusicInfoDB(SQLiteDatabase database) {
		mDatabase = database;
	}

	/**
	 * 現在時刻取得
	 * 
	 * @return
	 */
	private String getNowTime() {
		String strDate;
		// SimpleDateFormat SDF;
		// Date date;

		strDate = Utils.getNowDateString("yyyyMMddkkmmss");
		// SDF = new SimpleDateFormat("yyyyMMddHHmmss");
		// date = new Date();
		// strDate = SDF.format(date);

		return strDate;
	}

	private String getString(String str) {
		String value = "";
		if (str != null)
			value = str;
		return value;
	}

	@Override
	public int checkTable() {
		int num;

		String query = "SELECT COUNT(*) FROM sqlite_master WHERE type=" + "'table' AND name='"
				+ MCMusicInfoDB.TABLE_NAME + "';";
		Cursor cursor = mDatabase.rawQuery(query, null);
		cursor.moveToFirst();
		num = Integer.parseInt(cursor.getString(0));
		cursor.close();

		return num;
	}

	@Override
	public long insert(IMCMusicItemInfo info) {
		long rowId = -1;
		String data = null;
		ContentValues values = new ContentValues();

		values.put(COLUM_FILE_STATUS, info.getStatus());

		data = getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_PLAYABLE_TRACKS));
		values.put(COLUM_PLAYABLE_TRACKS, data);

		data = getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_ADVERTISE_RATIO));
		values.put(COLUM_ADVERTISE_RATIO, data);

		data = getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_SKIP_REMAINING));
		values.put(COLUM_SKIP_REMAINING, data);

		data = getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_OUTPUT_PATH));
		values.put(COLUM_OUTPUT_PATH, data);

		data = getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_ENC_METHOD));
		values.put(COLUM_ENC_METHOD, data);

		data = getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_ENC_KEY));
		values.put(COLUM_ENC_KEY, data);

		data = getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_ENC_IV));
		values.put(COLUM_ENC_IV, data);

		data = getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_API_SIG));
		values.put(COLUM_ENC_API_SIG, data);

		// data =
		// getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_REG_TIME));
		data = getNowTime();
		values.put(COLUM_REG_TIME, data);

		data = getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_PLAY_DURATION));
		values.put(COLUM_PLAY_DURATION, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ID));
		values.put(COLUM_TRACK_ID, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_TITLE));
		values.put(COLUM_TRACK_TITLE, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_TITLE_EN));
		values.put(COLUM_TRACK_TITLE_EN, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ARTIST));
		values.put(COLUM_TRACK_ARTIST, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ARTIST_EN));
		values.put(COLUM_TRACK_ARTIST_EN, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_RELEASE_DATE));
		values.put(COLUM_TRACK_RELEASE_DATE, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_GENRE_LIST));
		values.put(COLUM_TRACK_GENRE, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_GENRE_EN));
		values.put(COLUM_TRACK_GENRE_EN, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_DESCRIPTION));
		values.put(COLUM_TRACK_DESCRIPTION, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_DESCRIPTION_EN));
		values.put(COLUM_TRACK_DESCRIPTION_EN, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_URL));
		values.put(COLUM_TRACK_AFFILIATE_URL, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_JACKET_ID));
		values.put(COLUM_TRACK_JACKET_ID, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ALBUM));
		values.put(COLUM_TRACK_ALBUM, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ALBUM_EN));
		values.put(COLUM_TRACK_ALBUM_EN, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_TRIAL_TIME));
		values.put(COLUM_TRACK_TRIAL_TIME, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ARTIST_ID));
		values.put(COLUM_TRACK_ARTIST_ID, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_CHANNEL_NAME));
		values.put(COLUM_CHANNEL_NAME, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_CHANNEL_NAME_EN));
		values.put(COLUM_CHANNEL_NAME_EN, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_REPLAY_GAIN));
		values.put(COLUM_REPLAY_GAIN, data);

		data = null;

		rowId = mDatabase.insert(TABLE_NAME, null, values);
		return rowId;
	}

	@Override
	public int update(String trackId, IMCMusicItemInfo info) {
		String data = null;
		ContentValues values = new ContentValues();

		values.put(COLUM_FILE_STATUS, info.getStatus());

		data = getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_PLAYABLE_TRACKS));
		values.put(COLUM_PLAYABLE_TRACKS, data);

		data = getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_ADVERTISE_RATIO));
		values.put(COLUM_ADVERTISE_RATIO, data);

		data = getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_SKIP_REMAINING));
		values.put(COLUM_SKIP_REMAINING, data);

		data = getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_OUTPUT_PATH));
		values.put(COLUM_OUTPUT_PATH, data);

		data = getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_ENC_METHOD));
		values.put(COLUM_ENC_METHOD, data);

		data = getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_ENC_KEY));
		values.put(COLUM_ENC_KEY, data);

		data = getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_ENC_IV));
		values.put(COLUM_ENC_IV, data);

		data = getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_API_SIG));
		values.put(COLUM_ENC_API_SIG, data);

		// data =
		// getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_REG_TIME));
		data = getNowTime();
		values.put(COLUM_REG_TIME, data);

		data = getString(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_PLAY_DURATION));
		values.put(COLUM_PLAY_DURATION, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ID));
		values.put(COLUM_TRACK_ID, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_TITLE));
		values.put(COLUM_TRACK_TITLE, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_TITLE_EN));
		values.put(COLUM_TRACK_TITLE_EN, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ARTIST));
		values.put(COLUM_TRACK_ARTIST, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ARTIST_EN));
		values.put(COLUM_TRACK_ARTIST_EN, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_RELEASE_DATE));
		values.put(COLUM_TRACK_RELEASE_DATE, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_GENRE_LIST));
		values.put(COLUM_TRACK_GENRE, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_GENRE_EN));
		values.put(COLUM_TRACK_GENRE_EN, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_DESCRIPTION));
		values.put(COLUM_TRACK_DESCRIPTION, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_DESCRIPTION_EN));
		values.put(COLUM_TRACK_DESCRIPTION_EN, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_URL));
		values.put(COLUM_TRACK_AFFILIATE_URL, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_JACKET_ID));
		values.put(COLUM_TRACK_JACKET_ID, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ALBUM));
		values.put(COLUM_TRACK_ALBUM, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ALBUM_EN));
		values.put(COLUM_TRACK_ALBUM_EN, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_TRIAL_TIME));
		values.put(COLUM_TRACK_TRIAL_TIME, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ARTIST_ID));
		values.put(COLUM_TRACK_ARTIST_ID, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_CHANNEL_NAME));
		values.put(COLUM_CHANNEL_NAME, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_CHANNEL_NAME_EN));
		values.put(COLUM_CHANNEL_NAME_EN, data);

		data = getString(info.getTrackItem().getString(MCDefResult.MCR_KIND_REPLAY_GAIN));
		values.put(COLUM_REPLAY_GAIN, data);

		data = null;

		String selection = COLUM_TRACK_ID + " = ?";
		String param = trackId;
		return mDatabase.update(TABLE_NAME, values, selection, new String[] { param });
	}

	@Override
	public IMCMusicItemList findAll() {
		IMCMusicItemList list = null;
		Cursor cursor = mDatabase.query(TABLE_NAME, COLUMNS, null, null, null, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				list = new MCMusicItemList();
				do {
					IMCMusicItemInfo info = setInfoFromCursor(cursor);
					list.setInfo(info);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
	}

	@Override
	public IMCMusicItemInfo find(String trackId) {
		IMCMusicItemInfo info = null;
		String selection = COLUM_TRACK_ID + " = ?";
		String param = trackId;
		Cursor cursor = mDatabase.query(TABLE_NAME, COLUMNS, selection, new String[] { param }, null, null, null);
		if (cursor.moveToFirst()) {
			info = setInfoFromCursor(cursor);
		}
		cursor.close();
		return info;
	}

	@Override
	public IMCMusicItemInfo find2(String status) {
		IMCMusicItemInfo info = null;
		String selection = COLUM_FILE_STATUS + " = ?";
		String param = status;
		String orderBy = COLUM_REG_TIME + " ASC";
		Cursor cursor = mDatabase.query(TABLE_NAME, COLUMNS, selection, new String[] { param }, null, null, orderBy);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				info = setInfoFromCursor(cursor);
			}
			cursor.close();
		}
		return info;
	}

	@Override
	public IMCMusicItemInfo findByImage(String path) {
		IMCMusicItemInfo info = null;
		String selection = COLUM_TRACK_JACKET_ID + " = ?";
		String param = path;
		String orderBy = COLUM_REG_TIME + " ASC";
		Cursor cursor = mDatabase.query(TABLE_NAME, COLUMNS, selection, new String[] { param }, null, null, orderBy);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				info = setInfoFromCursor(cursor);
			}
			cursor.close();
		}
		return info;
	}

	@Override
	public IMCMusicItemInfo findNext() {
		IMCMusicItemInfo info = null;

		String[] params = new String[] {
				// String.valueOf(IMCMusicItemInfo.MCDB_STATUS_DOWNLOADING),
				// //DL中
				String.valueOf(IMCMusicItemInfo.MCDB_STATUS_COMPLETE), // DL済み(再生未)
				// String.valueOf(IMCMusicItemInfo.MCDB_STATUS_DEFAULT)
				// //情報のみDBに存在する(DL未)
		};
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUM_FILE_STATUS + " = '%' || ? || '%'";
		Cursor cursor = mDatabase.rawQuery(query, params);
		// Cursor cursor = mDatabase.query(TABLE_NAME, COLUMNS, selection,
		// params, null, null, null);
		if (cursor.moveToFirst())
			info = setInfoFromCursor(cursor);
		cursor.close();
		return info;
	}

	private IMCMusicItemInfo setInfoFromCursor(Cursor cursor) {
		IMCMusicItemInfo info = new MCMusicItemInfo();
		IMCItem trackItem;
		int index = 0;

		cursor.getInt(index++);
		info.setStatus(cursor.getInt(index++));
		info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_PLAYABLE_TRACKS, cursor.getString(index++));
		info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_ADVERTISE_RATIO, cursor.getString(index++));
		info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_SKIP_REMAINING, cursor.getString(index++));
		info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_OUTPUT_PATH, cursor.getString(index++));
		info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_ENC_METHOD, cursor.getString(index++));
		info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_ENC_KEY, cursor.getString(index++));
		info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_ENC_IV, cursor.getString(index++));
		info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_API_SIG, cursor.getString(index++));
		info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_REG_TIME, cursor.getString(index++));
		info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_PLAY_DURATION, cursor.getString(index++));

		trackItem = setTrackItemFromCursor(cursor, index);
		info.setTrackItem(trackItem);

		return info;
	}

	private IMCItem setTrackItemFromCursor(Cursor cursor, int index) {
		IMCItem trackItem = new MCTrackItem();
		int indexTr = index;

		trackItem.setString(MCDefResult.MCR_KIND_TRACKITEM_ID, cursor.getString(indexTr++));
		trackItem.setString(MCDefResult.MCR_KIND_TRACKITEM_TITLE, cursor.getString(indexTr++));
		trackItem.setString(MCDefResult.MCR_KIND_TRACKITEM_TITLE_EN, cursor.getString(indexTr++));
		trackItem.setString(MCDefResult.MCR_KIND_TRACKITEM_ARTIST, cursor.getString(indexTr++));
		trackItem.setString(MCDefResult.MCR_KIND_TRACKITEM_ARTIST_EN, cursor.getString(indexTr++));
		trackItem.setString(MCDefResult.MCR_KIND_TRACKITEM_RELEASE_DATE, cursor.getString(indexTr++));
		trackItem.setString(MCDefResult.MCR_KIND_GENRE_LIST, cursor.getString(indexTr++));
		trackItem.setString(MCDefResult.MCR_KIND_TRACKITEM_GENRE_EN, cursor.getString(indexTr++));
		trackItem.setString(MCDefResult.MCR_KIND_TRACKITEM_DESCRIPTION, cursor.getString(indexTr++));
		trackItem.setString(MCDefResult.MCR_KIND_TRACKITEM_DESCRIPTION_EN, cursor.getString(indexTr++));
		trackItem.setString(MCDefResult.MCR_KIND_TRACKITEM_URL, cursor.getString(indexTr++));
		trackItem.setString(MCDefResult.MCR_KIND_TRACKITEM_JACKET_ID, cursor.getString(indexTr++));
		trackItem.setString(MCDefResult.MCR_KIND_TRACKITEM_ALBUM, cursor.getString(indexTr++));
		trackItem.setString(MCDefResult.MCR_KIND_TRACKITEM_ALBUM_EN, cursor.getString(indexTr++));
		trackItem.setString(MCDefResult.MCR_KIND_TRACKITEM_TRIAL_TIME, cursor.getString(indexTr++));
		trackItem.setString(MCDefResult.MCR_KIND_TRACKITEM_ARTIST_ID, cursor.getString(indexTr++));
		trackItem.setString(MCDefResult.MCR_KIND_CHANNEL_NAME, cursor.getString(indexTr++));
		trackItem.setString(MCDefResult.MCR_KIND_CHANNEL_NAME_EN, cursor.getString(indexTr++));

		trackItem.setString(MCDefResult.MCR_KIND_REPLAY_GAIN, cursor.getString(indexTr++));

		return trackItem;
	}

	@Override
	public int delete(String trackId) {
		int ret = 0;
		String selection = COLUM_TRACK_ID + " = ?";
		String param = trackId;
		ret = mDatabase.delete(TABLE_NAME, selection, new String[] { param });
		return ret;
	}

	@Override
	public void deleteAll() {
		mDatabase.execSQL(MCMusicInfoDBHelper.DROP_TABLE_SQL);
		mDatabase.execSQL(MCMusicInfoDBHelper.CREATE_TABLE_SQL);
	}

	@Override
	public int getSize() {
		int size = 0;
		Cursor cursor = mDatabase.query(TABLE_NAME, COLUMNS, null, null, null, null, null);
		if (cursor != null) {
			size = cursor.getCount();
			cursor.close();
		}
		return size;
	}

	public int getSize(int fileStatus) {
		int size = 0;
		String selection = COLUM_FILE_STATUS + " = ?";
		String[] selectionArgs = new String[] { Integer.toString(fileStatus) };
		Cursor cursor = mDatabase.query(TABLE_NAME, COLUMNS, selection, selectionArgs, null, null, null);
		if (cursor != null) {
			size = cursor.getCount();
			cursor.close();
		}
		return size;
	}
}
