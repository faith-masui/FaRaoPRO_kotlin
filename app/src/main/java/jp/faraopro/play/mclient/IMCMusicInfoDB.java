package jp.faraopro.play.mclient;

/**
 * 楽曲情報DBのデータ操作用IF
 * 
 * @author AIM Corporation
 * 
 */
public interface IMCMusicInfoDB {

	/**
	 * 楽曲情報の登録
	 * 
	 * @param info
	 * @return
	 */
	public long insert(IMCMusicItemInfo info);

	/**
	 * 楽曲情報の更新(trackId指定)
	 * 
	 * @param trackId
	 * @param info
	 * @return
	 */
	public int update(String trackId, IMCMusicItemInfo info);

	/**
	 * 全件検索
	 * 
	 * @return
	 */
	public IMCMusicItemList findAll();

	/**
	 * 検索(次再生曲の取得)
	 * 
	 * @return
	 */
	public IMCMusicItemInfo findNext();

	/**
	 * 検索(trackId指定による検索)
	 * 
	 * @param trackId
	 * @return
	 */
	public IMCMusicItemInfo find(String trackId);

	public IMCMusicItemInfo find2(String trackId);

	public IMCMusicItemInfo findByImage(String path);

	/**
	 * 削除(trackId指定による削除)
	 * 
	 * @param trackId
	 * @return
	 */
	public int delete(String trackId);

	/**
	 * 削除(全件)
	 */
	public void deleteAll();

	/**
	 * 件数取得
	 * 
	 * @return
	 */
	public int getSize();

	public int checkTable();

	// public void hogehoge();

}
