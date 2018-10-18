package jp.faraopro.play.mclient;

import android.os.Parcelable;

/**
 * Musicサーバーからの情報(リストアイテム)管理インターフェース
 * 
 * @author AIM Corporation
 * 
 */
public interface IMCItem extends Parcelable {

	/**
	 * String値の登録
	 * 
	 * @param kind
	 *            (MCDefResult参照)
	 * @param value
	 */
	void setString(int kind, String value);

	/**
	 * String値の取得
	 * 
	 * @param kind
	 *            (MCDefResult参照)
	 */
	String getString(int kind);

	/**
	 * 解放
	 */
	void clear();

}
