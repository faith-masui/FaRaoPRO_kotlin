package jp.faraopro.play.mclient;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * DB操作用インスタンスファクトリークラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCOfflineMusicInfoDBFactory {
	private static IMCMusicInfoDB mMCMusicInfoDB = null;

	/**
	 * インスタンスの取得
	 * 
	 * @param context
	 * @return
	 */
	public synchronized static IMCMusicInfoDB getInstance(Context context) {
		if (mMCMusicInfoDB == null) {
			MCOfflineMusicInfoDBHelper musicInfoDBHelper = new MCOfflineMusicInfoDBHelper(context);
			SQLiteDatabase dbMusicInfo;
			dbMusicInfo = musicInfoDBHelper.getWritableDatabase();
			mMCMusicInfoDB = new MCOfflineMusicInfoDB(dbMusicInfo);
		}

		return mMCMusicInfoDB;
	}
}
