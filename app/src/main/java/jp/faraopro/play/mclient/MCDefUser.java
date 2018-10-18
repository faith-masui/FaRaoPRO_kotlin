package jp.faraopro.play.mclient;

/**
 * ユーザー情報(永続化) 関連定義群
 * 
 * @author AIM Corporation
 * 
 */
public class MCDefUser {

	/**
	 * 不明・Default
	 */
	public static final int MCU_KIND_UNKNOWN = 0;
	/**
	 * 種別ID：Email
	 */
	public static final int MCU_KIND_EMAIL = 1;
	/**
	 * 種別ID：パスワード
	 */
	public static final int MCU_KIND_PASSWORD = 2;
	/**
	 * 種別ID：セッションキー
	 */
	public static final int MCU_KIND_SESSIONKEY = 3;
	/**
	 * 種別ID：セッションキー
	 */
	public static final int MCU_KIND_DEVICETOKEN = 4;
}
