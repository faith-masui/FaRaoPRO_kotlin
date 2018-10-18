package jp.faraopro.play.mclient;

/**
 * Musicサーバーからの情報リスト管理インターフェース
 * 
 * @author AIM Corporation
 * 
 */
public interface IMCItemList {

	/**
	 * アイテム取得
	 * 
	 * @param index
	 * @return
	 */
	IMCItem getItem(int index);

	/**
	 * 件数取得
	 * 
	 * @return
	 */
	int getSize();

	/**
	 * アイテム追加
	 * 
	 * @param item
	 */
	void setItem(IMCItem item);

	/**
	 * アイテム削除
	 * 
	 * @param index
	 */
	void delete(int index);

	/**
	 * 全アイテムの削除
	 */
	void clear();

}
