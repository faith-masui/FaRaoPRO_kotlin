package jp.faraopro.play.common;

import jp.faraopro.play.mclient.MCChannelItem;
import jp.faraopro.play.mclient.MCDefResult;

/**
 * オブジェクト間比較定義クラス
 * 
 * @author ksu
 * 
 */
public class DataComparator implements java.util.Comparator<Object> {
	@Override
	public int compare(Object o1, Object o2) {
		return (int) ((Long.parseLong(((MCChannelItem) o2).getString(MCDefResult.MCR_KIND_CHANNELITEM_TIMESTAMP))
				- Long.parseLong(((MCChannelItem) o1).getString(MCDefResult.MCR_KIND_CHANNELITEM_TIMESTAMP))));
	}
}
