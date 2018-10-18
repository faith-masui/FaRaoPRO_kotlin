package jp.faraopro.play.mclient;

import java.util.ArrayList;

/**
 * 楽曲情報(アイテム)リスト管理クラス
 * 
 * @author AIM Corporation
 * 
 */

public class MCMusicItemList extends ArrayList<MCMusicItemInfo> implements IMCMusicItemList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1138529640212065965L;

	@Override
	public IMCMusicItemInfo getInfo(int index) {
		IMCMusicItemInfo info = null;
		info = super.get(index);
		return info;
	}

	@Override
	public int getSize() {
		int size = 0;
		size = super.size();
		return size;
	}

	@Override
	public void setInfo(IMCMusicItemInfo info) {
		if (info != null)
			super.add((MCMusicItemInfo) info);
	}

	@Override
	public void delete(int index) {
		super.remove(index);
	}

	@Override
	public void clear() {
		super.clear();
	}
}
