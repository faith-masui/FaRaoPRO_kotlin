package jp.faraopro.play.mclient;

import jp.faraopro.play.mclient.MCHttpClient.IMCHttpClientListener;

/**
 * Music Client アクセス用IF ・Web(MusicServer)アクセス ・ユーザー情報管理・アクセス ・楽曲情報管理・アクセス
 * 
 * @author AIM Corporation
 * 
 */
public interface IMusicClientHandler {

	/**
	 * 終了処理
	 */
	void term();

	/**
	 * Web(MusicServer)アクセス
	 * 
	 * @param kind
	 *            アクション種別（MCDefAction参照）
	 * @param param
	 *            パラメータ（MCDefParam参照）
	 * @return
	 */
	void actDoAction(int kind, IMCPostActionParam param);

	void actDoActionWithListener(int kind, IMCPostActionParam param, IMCHttpClientListener listener);

	// /**
	// * ユーザー情報(永続化)管理（登録）
	// * @param kind ユーザー情報種別（MCDefUser参照）
	// * @param Value
	// */
	// void usrSetValue(int kind, String value);

	/**
	 * ユーザー情報(永続化)管理（取得）
	 * 
	 * @param kind
	 *            ユーザー情報種別（MCDefUser参照）
	 * @return
	 */
	String usrGetValue(int kind);

	/**
	 * ユーザー情報(永続化)管理（全情報のクリア）
	 */
	void usrClearAll();

	/**
	 * （楽曲DB関連）楽曲情報(アイテム)リスト取得
	 * 
	 * @return
	 */
	IMCMusicItemList mdbGetAll();

	/**
	 * （楽曲DB関連）楽曲情報(アイテム)取得
	 * 
	 * @param trackId
	 *            （コンテンツ毎に振られているID）
	 * @return
	 */
	IMCMusicItemInfo mdbGet(String trackId);

	IMCMusicItemInfo mdbGet2(String trackId);

	IMCMusicItemInfo mdbFindByImage(String path);

	/**
	 * （楽曲DB関連）登録数の取得
	 * 
	 * @return
	 */
	int mdbGetSize();

	/**
	 * （楽曲DB関連）次再生曲の情報取得
	 * 
	 * @return
	 */
	IMCMusicItemInfo mdbGetNextPlay();

	/**
	 * （楽曲DB関連）全情報の削除
	 */
	void mdbDeleteAll();

	/**
	 * （楽曲DB関連）指定したtrackIdの楽曲情報を削除
	 * 
	 * @param trackId
	 *            （コンテンツ毎に振られているID）
	 */
	void mdbDelete(String trackId);

	/**
	 * （楽曲DB関連）指定したtrackIdの楽曲情報に再生状態を登録する
	 * 
	 * @param trackId
	 *            （コンテンツ毎に振られているID）
	 * @param status
	 *            (IMCMusicItemInfo参照)
	 */
	void mdbSetStatus(String trackId, int status);

	/**
	 * 楽曲を実際に再生した時間(秒)
	 * 
	 * @param trackId
	 * @param status
	 */
	void mdbPlayDuration(String trackId, String msec);

	void cancelAPIs() throws InterruptedException;

}
