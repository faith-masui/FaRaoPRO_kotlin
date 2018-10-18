package jp.faraopro.play.mclient;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * DB操作用インスタンスファクトリークラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCMusicInfoDBFactory {
	private static IMCMusicInfoDB mMCMusicInfoDB = null;

	/**
	 * インスタンスの取得
	 * 
	 * @param context
	 * @return
	 */
	public synchronized static IMCMusicInfoDB getInstance(Context context) {
		if (mMCMusicInfoDB == null) {
			MCMusicInfoDBHelper musicInfoDBHelper = new MCMusicInfoDBHelper(context);
			SQLiteDatabase dbMusicInfo;
			dbMusicInfo = musicInfoDBHelper.getWritableDatabase();
			mMCMusicInfoDB = new MCMusicInfoDB(dbMusicInfo);
		}

		return mMCMusicInfoDB;
	}
}
