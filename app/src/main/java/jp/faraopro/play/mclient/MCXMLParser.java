package jp.faraopro.play.mclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.text.TextUtils;
import jp.faraopro.play.common.FRODebug;

/**
 * MusicClient用XMLパーサークラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCXMLParser {
	// put out logs
	protected boolean DEBUG = true;

	/**
	 * インスタンスの取得
	 * 
	 * @return
	 */
	public static MCXMLParser getInstance() {
		return new MCXMLParser();
	}

	/**
	 * コンストラクタ
	 */
	public MCXMLParser() {
	}

	/**
	 * XMLパース実行
	 * 
	 * @param stream
	 * @return
	 */
	public IMCResultInfo parse(InputStream stream) {
		IMCResultInfo resultInfo = new MCAnalyzeBodyInfo();
		resultInfo.setInt(MCDefResult.MCR_KIND_STATUS_CODE, MCError.MC_NO_ERROR);
		XmlPullParser parser = null;
		IMCItem item = null;
		IMCItemList itemList = null;
		try {
			parser = XmlPullParserFactory.newInstance().newPullParser();
			// } catch (XmlPullParserException e1) {
			// e1.printStackTrace();
			// }
			// IMCItem item = null;
			// IMCItemList itemList = null;
			int kind = MCDefResult.MCR_KIND_UNKNOWN;
			String value = null;
			int itemKind = MCDefResult.MCR_KIND_UNKNOWN;
			int indentNum = 0;

			// CDN対応
			final String cdnTag = "cdnResponse";
			final String playlistTag = "playlistResponse";
			final String tracksTag = "tracks";
			final String jacketsTag = "jackets";
			final String urlTag = "url";
			boolean isCdn = false;
			boolean isPlaylist = false;
			int increment = 0;

			// 多重階層item対応
			HashMap<Integer, IMCItem> temporarilyStoredParents = new HashMap<Integer, IMCItem>();

			parser.setInput(stream, null);
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String name = null;
				switch (eventType) {

				case XmlPullParser.START_DOCUMENT:
					break;

				case XmlPullParser.START_TAG:
					name = parser.getName();
					if (name.equals(playlistTag))
						isPlaylist = true;

					// cdn対応
					if (name.equals(cdnTag))
						isCdn = true;
					if (isCdn) {
						if (name.equals(tracksTag)) {
							increment = MCDefResult.MCR_ADD_NUMBER_TRACKS;
						} else if (name.equals(jacketsTag)) {
							increment = MCDefResult.MCR_ADD_NUMBER_JACKETS;
						}
					}

					kind = MCDefResult.getKind(name);

					if (isCdn) {
						if (name.equals(urlTag)) {
							kind += increment;
						}
					}

					// ログにタグを表示する
					showTag(eventType, kind, name, indentNum);

					// まだ item クラスが生成されていない
					if (item == null) {
						item = getItemInstance(kind);

						// 暫定対応 <playing>タグがタブっているため
						if (isPlaylist && kind == MCDefResult.MCR_KIND_PLAYING_LIST)
							item = null;

						if (item != null)
							itemKind = kind;
					}
					// 既に item クラスが生成されている
					else {
						// 既存の item クラスを親として、新規に子 item クラスを生成するケース
						if (getParentKind(kind) > 0) {
							temporarilyStoredParents.put(new Integer(itemKind), item);
							itemKind = kind;
							item = getItemInstance(kind);
						}
					}
					indentNum++;
					break;

				case XmlPullParser.END_TAG:
					indentNum--;
					name = parser.getName();
					kind = MCDefResult.getKind(name);

					if (isCdn) {
						if (name.equals(urlTag)) {
							kind += increment;
						}
					}

					if (isCdn) {
						if (name.equals(tracksTag) || name.equals(jacketsTag))
							increment = 0;
					}

					// ログにタグを表示する
					showTag(eventType, kind, name, indentNum);

					if (itemKind == kind && item != null && true == MCDefResult.isItemFinish(kind)) {
						if (temporarilyStoredParents.size() > 0) {
							int parentKind = getParentKind(kind);
							IMCItem parentItem = temporarilyStoredParents.remove(parentKind);
							if (parentKind == MCDefResult.MCR_KIND_FRAME) {
								((MCFrameItem) parentItem).setItem(itemKind, item);
							}
							itemKind = parentKind;
							item = parentItem;
						} else {
							if (itemList == null)
								itemList = getListInstance(kind);
							if (itemList != null)
								itemList.setItem(item);
							else {
								resultInfo.setItem(kind, item);
							}
							item = null;
							itemKind = MCDefResult.MCR_KIND_UNKNOWN;
						}
					}
					if (itemList != null && true == MCDefResult.isListFinish(kind)) {
						resultInfo.setList(mapList(kind), itemList);
						itemList = null;
					}
					kind = -1;

					// cdn対応
					if (name.equals(cdnTag))
						isCdn = false;
					if (name.equals(playlistTag))
						isPlaylist = false;

					break;

				case XmlPullParser.TEXT:
					value = parser.getText();

					// ログにタグを表示する
					showTag(eventType, kind, value, indentNum);

					if (kind != -1) {
						if (item != null) {
							item.setString(kind, value);
						} else {
							resultInfo.setString(kind, value);
						}
					}
					break;
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			resultInfo.setInt(MCDefResult.MCR_KIND_STATUS_CODE, MCError.MC_APPERR_PARSE);
		} catch (IOException e) {
			resultInfo.setInt(MCDefResult.MCR_KIND_STATUS_CODE, MCError.MC_APPERR_PARSE);
		} finally {
			if (item != null) {
				item.clear();
				item = null;
			}
			if (itemList != null)
				itemList.clear();
			itemList = null;
		}
		return resultInfo;
	}

	private void showTag(int type, int kind, String value, int indentSize) {
		if (!FRODebug.ENABLE_LOGS)
			return;

		String indent = "";
		for (int i = 0; i < indentSize; i++) {
			indent += "    ";
		}
		switch (type) {
		case XmlPullParser.START_TAG:
			FRODebug.logI(getClass(), indent + "<" + value + "(" + kind + ")>", DEBUG);
			break;
		case XmlPullParser.TEXT:
			if (!TextUtils.isEmpty(value))
				FRODebug.logI(getClass(), indent + value, DEBUG);
			break;
		case XmlPullParser.END_TAG:
			FRODebug.logI(getClass(), indent + "</" + value + ">", DEBUG);
			break;
		}
	}

	class StringContainer {
		String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

	private IMCItem getItemInstance(int kind) {
		switch (kind) {
		case MCDefResult.MCR_KIND_TRACK_LIST:
			return new MCTrackItem();
		case MCDefResult.MCR_KIND_GENRE_LIST:
			return new MCGenreItem();
		case MCDefResult.MCR_KIND_CHANNEL_LIST:
			return new MCChannelItem();
		case MCDefResult.MCR_KIND_CHART_LIST:
		case MCDefResult.MCR_KIND_SHUFFLE_LIST:
			return new MCChartItem();
		case MCDefResult.MCR_KIND_SEARCH_LIST:
			return new MCSearchItem();
		case MCDefResult.MCR_KIND_AD_LIST:
			return new MCAdItem();
		case MCDefResult.MCR_KIND_LOCATION_LIST:
			return new MCLocationItem();
		case MCDefResult.MCR_KIND_BENIFIT_LIST:
			return new MCBenifitItem();
		case MCDefResult.MCR_KIND_CAMPAIGN_LIST:
			return new MCCampaignItem();
		case MCDefResult.MCR_KIND_SHOP_LIST:
			return new MCShopItem();
		case MCDefResult.MCR_KIND_CDN_LIST:
			return new MCCdnMusicItem();
		case MCDefResult.MCR_KIND_BUSINESS_LIST:
			return new MCBusinessItem();
		case MCDefResult.MCR_KIND_TEMPLATE_LIST:
			return new MCTemplateItem();
		case MCDefResult.MCR_KIND_TIMETABLE_LIST:
			return new MCTimetableItem();
		case MCDefResult.MCR_KIND_BOOKMARK_LIST:
			return new MCBookmarkItem();
		case MCDefResult.MCR_KIND_STREAM_LIST:
			return new MCStreamItem();
		case MCDefResult.MCR_KIND_PLAYING_LIST:
			return new MCStreamItem();
		case MCDefResult.MCR_KIND_PATTERN_RESPONSE:
			return new MCScheduleItem();
		case MCDefResult.MCR_KIND_PATTERN_AUDIO:
			return new MCAudioItem();
		case MCDefResult.MCR_KIND_FRAME:
			return new MCFrameItem();
		default:
			return null;
		}
	}

	private IMCItemList getListInstance(int kind) {
		switch (kind) {
		case MCDefResult.MCR_KIND_TRACK_LIST:
			return new MCTrackItemList();
		case MCDefResult.MCR_KIND_GENRE_LIST:
			return new MCGenreItemList();
		case MCDefResult.MCR_KIND_CHANNEL_LIST:
			return new MCChannelItemList();
		case MCDefResult.MCR_KIND_CHART_LIST:
		case MCDefResult.MCR_KIND_SHUFFLE_LIST:
			return new MCChartItemList();
		case MCDefResult.MCR_KIND_SEARCH_LIST:
			return new MCSearchItemList();
		case MCDefResult.MCR_KIND_AD_LIST:
			return new MCAdItemList();
		case MCDefResult.MCR_KIND_LOCATION_LIST:
			return new MCLocationItemList();
		case MCDefResult.MCR_KIND_BENIFIT_LIST:
			return new MCBenifitItemList();
		case MCDefResult.MCR_KIND_CAMPAIGN_LIST:
			return new MCCampaignItemList();
		case MCDefResult.MCR_KIND_SHOP_LIST:
			return new MCShopItemList();
		case MCDefResult.MCR_KIND_BUSINESS_LIST:
			return new MCBusinessItemList();
		case MCDefResult.MCR_KIND_TEMPLATE_LIST:
			return new MCTemplateItemList();
		case MCDefResult.MCR_KIND_TIMETABLE_LIST:
			return new MCTimetableItemList();
		case MCDefResult.MCR_KIND_BOOKMARK_LIST:
			return new MCBookmarkItemList();
		case MCDefResult.MCR_KIND_STREAM_LIST:
			return new MCStreamItemList();
		case MCDefResult.MCR_KIND_FRAME:
			return new MCFrameItemList();
		default:
			return null;
		}
	}

	public int mapList(int kind) {
		switch (kind) {
		case MCDefResult.MCR_KIND_TRACK_LIST_PARENTS:
			return IMCResultInfo.MC_LIST_KIND_TRACK;
		case MCDefResult.MCR_KIND_GENRE_LIST_PARENTS:
			return IMCResultInfo.MC_LIST_KIND_GENRE;
		case MCDefResult.MCR_KIND_CHANNEL_LIST_PARENTS:
			return IMCResultInfo.MC_LIST_KIND_CHANNEL;
		case MCDefResult.MCR_KIND_CHART_LIST_PARENTS:
			return IMCResultInfo.MC_LIST_KIND_CHART;
		case MCDefResult.MCR_KIND_SEARCH_LIST_PARENTS:
			return IMCResultInfo.MC_LIST_KIND_SEARCH;
		case MCDefResult.MCR_KIND_AD_LIST_PARENTS:
			return IMCResultInfo.MC_LIST_KIND_AD;
		case MCDefResult.MCR_KIND_LOCATION_LIST_PARENTS:
			return IMCResultInfo.MC_LIST_KIND_LOCATION;
		case MCDefResult.MCR_KIND_BENIFIT_LIST_PARENTS:
			return IMCResultInfo.MC_LIST_KIND_BENIFIT;
		case MCDefResult.MCR_KIND_CAMPAIGN_LIST_PARENTS:
			return IMCResultInfo.MC_LIST_KIND_CAMPAIGN;
		case MCDefResult.MCR_KIND_SHUFFLE_LIST_PARENTS:
			return IMCResultInfo.MC_LIST_KIND_SHUFFLE;
		case MCDefResult.MCR_KIND_SHOP_LIST_PARENTS:
			return IMCResultInfo.MC_LIST_KIND_SHOP;
		case MCDefResult.MCR_KIND_BUSINESS_LIST_PARENTS:
			return IMCResultInfo.MC_LIST_KIND_BUSINESS;
		case MCDefResult.MCR_KIND_TEMPLATE_LIST_PARENTS:
			return IMCResultInfo.MC_LIST_KIND_TEMPLATE;
		case MCDefResult.MCR_KIND_TIMETABLE_LIST_PARENTS:
			return IMCResultInfo.MC_LIST_KIND_TIMETABLE;
		case MCDefResult.MCR_KIND_BOOKMARK_LIST_PARENTS:
			return IMCResultInfo.MC_LIST_KIND_BOOKMARK;
		case MCDefResult.MCR_KIND_STREAM_LIST_PARENTS:
			return IMCResultInfo.MC_LIST_KIND_STREAM;
		case MCDefResult.MCR_KIND_FRAMES:
			return IMCResultInfo.MC_LIST_KIND_FRAME;
		default:
			return 0;
		}
	}

	// IMCItem を親クラスとして持つ IMCItem を判別する
	private int getParentKind(int kind) {
		int parentKind = -1;
		switch (kind) {
		case MCDefResult.MCR_KIND_PATTERN_AUDIO:
			parentKind = MCDefResult.MCR_KIND_FRAME;
			break;
		}

		return parentKind;
	}
}
