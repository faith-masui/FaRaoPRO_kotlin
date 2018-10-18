package jp.faraopro.play.mclient;

import java.util.ArrayList;

/**
 * Channelアイテム一覧管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCBenifitItemList extends ArrayList<MCBenifitItem> implements IMCItemList {

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
			super.add((MCBenifitItem) item);
	}

	@Override
	public void delete(int index) {
		super.remove(index);
	}

	@Override
	public void clear() {
		super.clear();
	}

	public IMCItem find(int channelId) {
		for (int i = 0; i < size(); i++) {
			IMCItem item = getItem(i);
			int id = Integer.parseInt(item.getString(MCDefResult.MCR_KIND_CHANNELITEM_ID));
			if (channelId == id)
				return item;
		}
		return null;
	}
}
