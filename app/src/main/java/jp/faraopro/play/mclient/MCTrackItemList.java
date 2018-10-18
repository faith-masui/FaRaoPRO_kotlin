package jp.faraopro.play.mclient;

import java.util.ArrayList;

/**
 * Trackアイテム一覧管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCTrackItemList extends ArrayList<MCTrackItem> implements IMCItemList {

	@Override
	public IMCItem getItem(int index) {
		if (index < 0 || index > (super.size() - 1)) {
			return null;
		}
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
			super.add((MCTrackItem) item);
	}

	@Override
	public void delete(int index) {
		if (index < 0 || index > (super.size() - 1)) {
			return;
		}
		super.remove(index);
	}

	@Override
	public void clear() {
		super.clear();
	}
}
