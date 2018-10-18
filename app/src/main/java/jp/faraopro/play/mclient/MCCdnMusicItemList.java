package jp.faraopro.play.mclient;

import java.util.ArrayList;

/**
 * CDNアイテム一覧管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCCdnMusicItemList extends ArrayList<MCCdnMusicItem> implements IMCItemList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public IMCItem getItem(int index) {
		IMCItem item = super.get(index);
		return item;
	}

	@Override
	public int getSize() {
		int size = super.size();
		return size;
	}

	@Override
	public void setItem(IMCItem item) {
		if (item != null)
			super.add((MCCdnMusicItem) item);
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