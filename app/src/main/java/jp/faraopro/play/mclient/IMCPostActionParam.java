package jp.faraopro.play.mclient;

/**
 * MusicClient(Web API)アクセス用情報インターフェース
 * 
 * @author AIM Corporation
 * 
 */
public interface IMCPostActionParam {

	/**
	 * String値の登録
	 * 
	 * @param kind
	 *            (MCDefParam参照)
	 * @param value
	 */
	void setStringValue(int kind, String value);

	/**
	 * String値の取得
	 * 
	 * @param kind
	 *            (MCDefParam参照)
	 */
	String getStringValue(int kind);

	/**
	 * 解放
	 */
	void clear();

}
