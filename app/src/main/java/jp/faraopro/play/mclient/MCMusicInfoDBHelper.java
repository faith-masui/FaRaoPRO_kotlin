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
public class MCMusicInfoDBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "MusicInfoDataBase";
	private static final int DATABASE_VERSION = 7; // 14/10/24
	/**
	 * DB作成用SQL
	 */
	public static final String CREATE_TABLE_SQL = "create table " + MCMusicInfoDB.TABLE_NAME + " " + "("
			+ MCMusicInfoDB.COLUM_DATAID + " integer primary key autoincrement" + ", " + MCMusicInfoDB.COLUM_FILE_STATUS
			+ " integer default " + IMCMusicItemInfo.MCDB_STATUS_DEFAULT + ", " + MCMusicInfoDB.COLUM_PLAYABLE_TRACKS
			+ " text not null" + ", " + MCMusicInfoDB.COLUM_ADVERTISE_RATIO + " text not null" + ", "
			+ MCMusicInfoDB.COLUM_SKIP_REMAINING + " text not null" + ", " + MCMusicInfoDB.COLUM_OUTPUT_PATH
			+ " text not null" + ", " + MCMusicInfoDB.COLUM_ENC_METHOD + " text not null" + ", "
			+ MCMusicInfoDB.COLUM_ENC_KEY + " text not null" + ", " + MCMusicInfoDB.COLUM_ENC_IV + " text not null"
			+ ", " + MCMusicInfoDB.COLUM_ENC_API_SIG + " text not null" + ", " + MCMusicInfoDB.COLUM_REG_TIME
			+ " text not null" + ", " + MCMusicInfoDB.COLUM_PLAY_DURATION + " text not null" + ", "
			+ MCMusicInfoDB.COLUM_TRACK_ID + " text not null" + ", " + MCMusicInfoDB.COLUM_TRACK_TITLE
			+ " text not null" + ", " + MCMusicInfoDB.COLUM_TRACK_TITLE_EN + " text not null" + ", "
			+ MCMusicInfoDB.COLUM_TRACK_ARTIST + " text not null" + ", " + MCMusicInfoDB.COLUM_TRACK_ARTIST_EN
			+ " text not null" + ", " + MCMusicInfoDB.COLUM_TRACK_RELEASE_DATE + " text not null" + ", "
			+ MCMusicInfoDB.COLUM_TRACK_GENRE + " text not null" + ", " + MCMusicInfoDB.COLUM_TRACK_GENRE_EN
			+ " text not null" + ", " + MCMusicInfoDB.COLUM_TRACK_DESCRIPTION + " text not null" + ", "
			+ MCMusicInfoDB.COLUM_TRACK_DESCRIPTION_EN + " text not null" + ", "
			+ MCMusicInfoDB.COLUM_TRACK_AFFILIATE_URL + " text not null" + ", " + MCMusicInfoDB.COLUM_TRACK_JACKET_ID
			+ " text not null" + ", " + MCMusicInfoDB.COLUM_TRACK_ALBUM + " text not null" + ", "
			+ MCMusicInfoDB.COLUM_TRACK_ALBUM_EN + " text not null" + ", " + MCMusicInfoDB.COLUM_TRACK_TRIAL_TIME + ", "
			+ MCMusicInfoDB.COLUM_TRACK_ARTIST_ID + " text not null" + ", " + MCMusicInfoDB.COLUM_CHANNEL_NAME + ", "
			+ MCMusicInfoDB.COLUM_CHANNEL_NAME_EN + ", " + MCMusicInfoDB.COLUM_REPLAY_GAIN + ")";

	/**
	 * DBクリア用SQL
	 */
	public static final String DROP_TABLE_SQL = "drop table if exists " + MCMusicInfoDB.TABLE_NAME;

	/**
	 * コンストラクタ
	 * 
	 * @param context
	 */
	public MCMusicInfoDBHelper(Context context) {
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