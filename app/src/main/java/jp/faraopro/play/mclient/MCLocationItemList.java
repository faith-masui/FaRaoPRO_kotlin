package jp.faraopro.play.mclient;

import java.util.ArrayList;

/**
 * 広告関連アイテム一覧管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCLocationItemList extends ArrayList<MCLocationItem> implements IMCItemList {

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
			super.add((MCLocationItem) item);
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