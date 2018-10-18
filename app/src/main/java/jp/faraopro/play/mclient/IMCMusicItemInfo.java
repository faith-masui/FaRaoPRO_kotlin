package jp.faraopro.play.mclient;

/**
 * 楽曲情報(アイテム)アクセス用IF
 * 
 * @author AIM Corporation
 * 
 */
public interface IMCMusicItemInfo {

	/**
	 * String値取得用(広告出現頻度)
	 */
	public static final int MCDB_ITEM_ADVERTISE_RATIO = 1;
	/**
	 * String値取得用(残スキップ回数)
	 */
	public static final int MCDB_ITEM_SKIP_REMAINING = 2;
	/**
	 * String値取得用(DL済みコンテンツのパス)
	 */
	public static final int MCDB_ITEM_OUTPUT_PATH = 3;
	/**
	 * String値取得用(エンコードに使用されたメソッド)
	 */
	public static final int MCDB_ITEM_ENC_METHOD = 4;
	/**
	 * String値取得用(エンコードに使用されたKEY)
	 */
	public static final int MCDB_ITEM_ENC_KEY = 5;
	/**
	 * String値取得用(エンコードに使用されたIV)
	 */
	public static final int MCDB_ITEM_ENC_IV = 6;
	/**
	 * String値取得用(DL完了時刻)
	 */
	public static final int MCDB_ITEM_REG_TIME = 7;
	/**
	 * String値取得用(再生した時間・秒)
	 */
	public static final int MCDB_ITEM_PLAY_DURATION = 8;
	/**
	 * カラム(Track詳細：月間残再生回数)
	 */
	public static final int MCDB_ITEM_PLAYABLE_TRACKS = 9;
	/**
	 * カラム(Track詳細：視聴時間)
	 */
	public static final int MCDB_ITEM_TRIAL_TIME = 10;
	/**
	 * カラム(Track詳細：チャンネル名)
	 */
	public static final int MCDB_ITEM_CHANNEL_NAME = 11;
	/**
	 * カラム(Track詳細：チャンネル名)
	 */
	public static final int MCDB_ITEM_CHANNEL_NAME_EN = 12;

	/**
	 * String値取得用(APIシグネチャ) テスト用
	 */
	public static final int MCDB_ITEM_API_SIG = 100;

	/**
	 * DL楽曲状態(DEFAULT)
	 */
	public static final int MCDB_STATUS_DEFAULT = 20;
	/**
	 * DL楽曲状態(DL中)
	 */
	public static final int MCDB_STATUS_DOWNLOADING = 21;
	/**
	 * DL楽曲状態(DL完了)
	 */
	public static final int MCDB_STATUS_COMPLETE = 22;
	/**
	 * DL楽曲状態(再生中)
	 */
	public static final int MCDB_STATUS_PLAYING = 23;
	/**
	 * DL楽曲状態(再生済み)
	 */
	public static final int MCDB_STATUS_PLAYED = 24;

	/**
	 * 解放処理
	 */
	void clear();

	/**
	 * String値の登録
	 * 
	 * @param kind
	 *            (IMCMusicItemInfo参照)
	 * @param value
	 */
	void setStringValue(int kind, String value);

	/**
	 * String値の取得
	 * 
	 * @param kind
	 *            (IMCMusicItemInfo参照)
	 */
	String getStringValue(int kind);

	/**
	 * トラック情報詳細登録
	 * 
	 * @param Item
	 */
	void setTrackItem(IMCItem item);

	/**
	 * トラック情報詳細取得
	 * 
	 * @return
	 */
	IMCItem getTrackItem();

	/**
	 * 楽曲DBに登録されているコンテンツのDL又は再生状況登録
	 * 
	 * @param status
	 *            (IMCMusicItemInfo参照)
	 */
	void setStatus(int status);

	/**
	 * 楽曲DBに登録されているコンテンツのDL又は再生状況取得
	 * 
	 * @return (IMCMusicItemInfo参照)
	 */
	int getStatus();
}
