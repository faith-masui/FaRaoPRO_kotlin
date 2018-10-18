package jp.faraopro.play.mclient;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 楽曲情報DB操作ヘルパークラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCOfflineMusicInfoDBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "OfflineMusicInfoDataBase";
	private static final int DATABASE_VERSION = 2;
	/**
	 * DB作成用SQL
	 */
	public static final String CREATE_TABLE_SQL = "create table " + MCOfflineMusicInfoDB.TABLE_NAME + " " + "("
			+ MCOfflineMusicInfoDB.COLUM_DATAID + " integer primary key autoincrement" + ", "
			+ MCOfflineMusicInfoDB.COLUM_FILE_STATUS + " integer default " + IMCMusicItemInfo.MCDB_STATUS_DEFAULT + ", "
			+ MCOfflineMusicInfoDB.COLUM_PLAYABLE_TRACKS + " text not null" + ", "
			+ MCOfflineMusicInfoDB.COLUM_ADVERTISE_RATIO + " text not null" + ", "
			+ MCOfflineMusicInfoDB.COLUM_SKIP_REMAINING + " text not null" + ", "
			+ MCOfflineMusicInfoDB.COLUM_OUTPUT_PATH + " text not null" + ", " + MCOfflineMusicInfoDB.COLUM_ENC_METHOD
			+ " text not null" + ", " + MCOfflineMusicInfoDB.COLUM_ENC_KEY + " text not null" + ", "
			+ MCOfflineMusicInfoDB.COLUM_ENC_IV + " text not null" + ", " + MCOfflineMusicInfoDB.COLUM_ENC_API_SIG
			+ " text not null" + ", " + MCOfflineMusicInfoDB.COLUM_REG_TIME + " text not null" + ", "
			+ MCOfflineMusicInfoDB.COLUM_PLAY_DURATION + " text not null" + ", " + MCOfflineMusicInfoDB.COLUM_TRACK_ID
			+ " text not null" + ", " + MCOfflineMusicInfoDB.COLUM_TRACK_TITLE + " text not null" + ", "
			+ MCOfflineMusicInfoDB.COLUM_TRACK_TITLE_EN + " text not null" + ", "
			+ MCOfflineMusicInfoDB.COLUM_TRACK_ARTIST + " text not null" + ", "
			+ MCOfflineMusicInfoDB.COLUM_TRACK_ARTIST_EN + " text not null" + ", "
			+ MCOfflineMusicInfoDB.COLUM_TRACK_RELEASE_DATE + " text not null" + ", "
			+ MCOfflineMusicInfoDB.COLUM_TRACK_GENRE + " text not null" + ", "
			+ MCOfflineMusicInfoDB.COLUM_TRACK_GENRE_EN + " text not null" + ", "
			+ MCOfflineMusicInfoDB.COLUM_TRACK_DESCRIPTION + " text not null" + ", "
			+ MCOfflineMusicInfoDB.COLUM_TRACK_DESCRIPTION_EN + " text not null" + ", "
			+ MCOfflineMusicInfoDB.COLUM_TRACK_AFFILIATE_URL + " text not null" + ", "
			+ MCOfflineMusicInfoDB.COLUM_TRACK_JACKET_ID + " text not null" + ", "
			+ MCOfflineMusicInfoDB.COLUM_TRACK_ALBUM + " text not null" + ", "
			+ MCOfflineMusicInfoDB.COLUM_TRACK_ALBUM_EN + " text not null" + ", "
			+ MCOfflineMusicInfoDB.COLUM_TRACK_TRIAL_TIME + ", " + MCOfflineMusicInfoDB.COLUM_TRACK_ARTIST_ID
			+ " text not null" + ", " + MCOfflineMusicInfoDB.COLUM_CHANNEL_NAME + ", "
			+ MCOfflineMusicInfoDB.COLUM_CHANNEL_NAME_EN + ", " + MCOfflineMusicInfoDB.COLUM_REPLAY_GAIN + ")";

	/**
	 * DBクリア用SQL
	 */
	public static final String DROP_TABLE_SQL = "drop table if exists " + MCOfflineMusicInfoDB.TABLE_NAME;

	/**
	 * コンストラクタ
	 * 
	 * @param context
	 */
	public MCOfflineMusicInfoDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * 生成時に呼ばれる
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SQL);
	}

	/**
	 * アップグレード
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TABLE_SQL);
		onCreate(db);
	}

}