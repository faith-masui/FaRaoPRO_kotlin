package jp.faraopro.play.mclient;

/**
 * 楽曲情報(アイテム)リストアクセス用IF
 * 
 * @author AIM Corporation
 * 
 */
public interface IMCMusicItemList {

	/**
	 * アイテム取得
	 * 
	 * @param index
	 * @return
	 */
	IMCMusicItemInfo getInfo(int index);

	/**
	 * 件数取得
	 * 
	 * @return
	 */
	int getSize();

	/**
	 * アイテム追加
	 * 
	 * @param info
	 */
	void setInfo(IMCMusicItemInfo info);

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
