package jp.faraopro.play.mclient;

/**
 * Musicサーバーからの受信結果アクセス用インターフェース
 * 
 * @author AIM Corporation
 * 
 */
public interface IMCResultInfo {

	/**
	 * Trackアイテムリスト取得
	 */
	public static final int MC_LIST_KIND_TRACK = 3;
	/**
	 * Genre(Channel)アイテムリスト取得
	 */
	public static final int MC_LIST_KIND_GENRE = 1;
	/**
	 * Channel(Channel)アイテムリスト取得
	 */
	public static final int MC_LIST_KIND_CHANNEL = 5;
	/**
	 * Chart(Channel)アイテムリスト取得
	 */
	public static final int MC_LIST_KIND_CHART = 4;
	/**
	 * SEARCHアイテムリスト取得
	 */
	public static final int MC_LIST_KIND_SEARCH = 2;
	/**
	 * 広告関連アイテムリスト取得
	 */
	public static final int MC_LIST_KIND_AD = 6;
	/**
	 * オススメアイテムリスト取得
	 */
	public static final int MC_LIST_KIND_FEATURED = 7;
	/**
	 * 地域マスタアイテムリスト取得
	 */
	public static final int MC_LIST_KIND_LOCATION = 8;
	/**
	 * 特典マスタアイテムリスト取得
	 */
	public static final int MC_LIST_KIND_BENIFIT = 9;
	/**
	 * チケットマスタアイテムリスト取得
	 */
	public static final int MC_LIST_KIND_CAMPAIGN = 10;
	/**
	 * シャッフルマスタアイテムリスト取得
	 */
	public static final int MC_LIST_KIND_SHUFFLE = 11;
	/**
	 * 店舗マスタアイテムリスト取得
	 */
	public static final int MC_LIST_KIND_SHOP = 12;
	/**
	 * ビジネスチャンネル
	 */
	public static final int MC_LIST_KIND_BUSINESS = 13;
	/**
	 * テンプレート
	 */
	public static final int MC_LIST_KIND_TEMPLATE = 14;
	/**
	 * タイマーテンプレート
	 */
	public static final int MC_LIST_KIND_TIMETABLE = 15;
	/**
	 * お気に入りテンプレート
	 */
	public static final int MC_LIST_KIND_BOOKMARK = 16;
	/**
	 * ストリーミングチャンネルリスト
	 */
	public static final int MC_LIST_KIND_STREAM = 17;
	/**
	 * 割り込みパターンリスト
	 */
	public static final int MC_LIST_KIND_FRAME = 18;

	/**
	 * String値の登録
	 * 
	 * @param kind
	 * @param value
	 */
	void setString(int kind, String value);

	/**
	 * String値の取得
	 * 
	 * @param kind
	 */
	String getString(int kind);

	/**
	 * リストの登録
	 * 
	 * @param kind
	 *            (IMCResultInfo参照)
	 * @param item
	 */
	void setList(int kind, IMCItemList list);

	/**
	 * リスト取得
	 * 
	 * @param kind
	 *            (IMCResultInfo参照)
	 */
	IMCItemList getList(int kind);

	/**
	 * int値の登録
	 * 
	 * @param kind
	 * @param value
	 */
	void setInt(int kind, int value);

	/**
	 * int値の取得
	 * 
	 * @param kind
	 */
	int getInt(int kind);

	void setItem(int kind, IMCItem item);

	IMCItem getItem(int kind);

	/**
	 * 解放
	 */
	void clear();

}
