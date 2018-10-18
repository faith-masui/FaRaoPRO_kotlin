package jp.faraopro.play.mclient;

import android.content.Context;

/**
 * MusicClient操作用インスタンスファクトリークラス
 * 
 * @author AIM Corporation
 * 
 */
public class MusicClientFactory {
	private static IMusicClientHandler mMusicClientHandler = null;

	/**
	 * インスタンスの取得
	 * 
	 * @param context
	 * @param resultListener
	 * @return
	 */
	public synchronized static IMusicClientHandler getInstance(Context context, IMCResultListener resultListener) {
		if (mMusicClientHandler == null) {
			mMusicClientHandler = new MusicClientHandler(context, resultListener);
		}
		return mMusicClientHandler;
	}

	public synchronized static void deleteInstance() {
		if (mMusicClientHandler != null) {
			mMusicClientHandler = null;
		}
	}
}
