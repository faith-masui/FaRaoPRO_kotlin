package jp.faraopro.play.view;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import jp.faraopro.play.common.Consts;
import jp.faraopro.play.domain.ChannelHistoryInfo;
import jp.faraopro.play.domain.ChannelInfo;
import jp.faraopro.play.mclient.MCBenifitItem;
import jp.faraopro.play.mclient.MCBookmarkItem;
import jp.faraopro.play.mclient.MCBusinessItem;
import jp.faraopro.play.mclient.MCChannelItem;
import jp.faraopro.play.mclient.MCChartItem;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCGenreItem;
import jp.faraopro.play.mclient.MCSearchItem;
import jp.faraopro.play.mclient.MCStreamItem;
import jp.faraopro.play.mclient.MCTemplateItem;
import jp.faraopro.play.mclient.MCTimetableItem;
import jp.faraopro.play.model.ChannelMode;
import jp.faraopro.play.model.TimerInfo;

/**
 * リスト要素クラス
 *
 * @author Aim
 *
 */
public class CustomListItem {
	private String text;
	private int id;
	private int lock;

	private String mName;
	private String mNameEn;
	private String mDescription;
	private String mDescriptionEn;
	private String mContentTitle;
	private String mContentTitleEn;
	private String mContentText;
	private String mContentTextEn;
	private String mThumbIcon;
	private String mDeliveryBegin;
	private String mDeliveryEnd;
	private String mSkipControl;
	private String mAdControl;
	private String mSeriesNo;
	private String mSeriesContentNo;
	// private Bitmap mIcon;
	private String mIcon;

	private ChannelMode mode;
	private boolean isNext = false;
	private boolean isHistory = false;
	private boolean isDummy = false;
	private String buyUrl;
	private String downloadUrl;

	private String thumnailSmall;

	// PRO
	private int parent;
	private int nodeType;
	private int templateType = -1;
	private String timerType;
	private String timerRule;
	private String sourceType;
	private String sourceName;
	private String sourceUrl;

	private String mediaType;
	private String sessionType;
	private String contentLink;
	private String previewLink;
	private String externalLink1;
	private String externalLink2;
	private String jacketId;
	private String stationId;
	private String stationName;
	private String stationNameEn;

	private String mDigest;
	private String mAction;
	private String mRule;

	private boolean update = false;

	private String showDuration;
	private List<String> showJackets;

	private int permission;
	private int channelId;
	private String range;

	public boolean isDummy() {
		return isDummy;
	}

	public void setDummy(boolean isDummy) {
		this.isDummy = isDummy;
	}

	public CustomListItem() {
	}

	public CustomListItem(CustomListItem copy) {
		text = copy.getText();
		id = copy.getId();
		lock = copy.getLock();
		mName = copy.getmName();
		mNameEn = copy.getmNameEn();
		mDescription = copy.getmDescription();
		mDescriptionEn = copy.getmDescriptionEn();
		mContentTitle = copy.getmContentTitle();
		mContentTitleEn = copy.getmContentTitleEn();
		mContentText = copy.getmContentText();
		mContentTextEn = copy.getmContentTextEn();
		mThumbIcon = copy.getmThumbIcon();
		mDeliveryBegin = copy.getmDeliveryBegin();
		mDeliveryEnd = copy.getmDeliveryEnd();
		mSkipControl = copy.getmSkipControl();
		mAdControl = copy.getmAdControl();
		mSeriesNo = copy.getmSeriesNo();
		mSeriesContentNo = copy.getmSeriesContentNo();
		mIcon = copy.getmIcon();
		mode = copy.getMode();
		isNext = copy.isNext();
		isHistory = copy.isHistory();
		isDummy = copy.isDummy();
		buyUrl = copy.getBuyUrl();
		downloadUrl = copy.getDownloadUrl();
		thumnailSmall = copy.getThumnailSmall();
		parent = copy.getParent();
		nodeType = copy.getNodeType();
		templateType = copy.getTemplateType();
		timerType = copy.getTimerType();
		timerRule = copy.getTimerRule();
		sourceType = copy.getSourceType();
		sourceName = copy.getSourceName();
		sourceUrl = copy.getSourceUrl();
		mediaType = copy.getMediaType();
		sessionType = copy.getSessionType();
		contentLink = copy.getContentLink();
		previewLink = copy.getPreviewLink();
		externalLink1 = copy.getExternalLink1();
		externalLink2 = copy.getExternalLink2();
		jacketId = copy.getJacketId();
		stationId = copy.getStationId();
		stationName = copy.getStationName();
		stationNameEn = copy.getStationNameEn();

		mDigest = copy.getDigest();
		mAction = copy.getAction();
		mRule = copy.getRule();

		showDuration = copy.getShowDuration();
		List<String> tmp = copy.getShowJackets();
		if (tmp != null)
			showJackets = new ArrayList<String>(copy.getShowJackets());
		else
			showJackets = new ArrayList<String>();
		permission = copy.getPermission();
	}

	public CustomListItem(MCGenreItem item) {
		this.id = Integer.parseInt(item.getString(MCDefResult.MCR_KIND_CHANNELITEM_ID));
		this.mName = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME);
		this.mNameEn = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN);
		mode = ChannelMode.GENRE;
	}

	public CustomListItem(MCSearchItem item) {
		this.id = Integer.parseInt(item.getString(MCDefResult.MCR_KIND_CHANNELITEM_ID));
		this.mName = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME);
		this.mNameEn = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN);
		this.mThumbIcon = item.getString(MCDefResult.MCR_KIND_ITEM_THUMB_ICON);
		this.thumnailSmall = item.getString(MCDefResult.MCR_KIND_THUMBNAIL_ICON_SMALL);
		mode = ChannelMode.ARTIST;
	}

	public CustomListItem(MCChannelItem item) {
		this.id = Integer.parseInt(item.getString(MCDefResult.MCR_KIND_CHANNELITEM_ID));
		try {
			this.lock = Integer.parseInt(item.getString(MCDefResult.MCR_KIND_CHANNELITEM_LOCK));
		} catch (NumberFormatException e) {
			this.lock = Consts.UNLOCK;
		}
		this.mName = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME);
		this.mNameEn = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN);
		this.mNameEn = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN);
		this.thumnailSmall = item.getString(MCDefResult.MCR_KIND_THUMBNAIL_ICON_SMALL);
		mode = ChannelMode.MYCHANNEL;
	}

	public CustomListItem(MCBenifitItem item) {
		this.id = Integer.parseInt(item.getString(MCDefResult.MCR_KIND_CHANNELITEM_ID));
		this.mName = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME);
		this.mNameEn = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN);
		this.mDescription = item.getString(MCDefResult.MCR_KIND_ITEM_DESCRIPTION);
		this.mDescriptionEn = item.getString(MCDefResult.MCR_KIND_ITEM_DESCRIPTION_EN);
		this.mContentTitle = item.getString(MCDefResult.MCR_KIND_ITEM_CONTENT_TITLE);
		this.mContentTitleEn = item.getString(MCDefResult.MCR_KIND_ITEM_CONTENT_TITLE_EN);
		this.mContentText = item.getString(MCDefResult.MCR_KIND_ITEM_CONTENT_TEXT);
		this.mContentTextEn = item.getString(MCDefResult.MCR_KIND_ITEM_CONTENT_TEXT_EN);
		this.mThumbIcon = item.getString(MCDefResult.MCR_KIND_ITEM_THUMB_ICON);
		this.mDeliveryBegin = item.getString(MCDefResult.MCR_KIND_BENIFITITEM_DELIVERY_BEGIN);
		this.mDeliveryEnd = item.getString(MCDefResult.MCR_KIND_BENIFITITEM_DELIVERY_END);
		this.mSkipControl = item.getString(MCDefResult.MCR_KIND_BENIFITITEM_SKIP_CONTROL);
		this.mAdControl = item.getString(MCDefResult.MCR_KIND_BENIFITITEM_AD_CONTROL);
		this.mSeriesNo = item.getString(MCDefResult.MCR_KIND_ITEM_SERIES_NO);
		this.mSeriesContentNo = item.getString(MCDefResult.MCR_KIND_ITEM_SERIES_CONTENT_NO);
		mode = ChannelMode.BENIFIT;
		this.thumnailSmall = item.getString(MCDefResult.MCR_KIND_THUMBNAIL_ICON_SMALL);
	}

	public CustomListItem(MCChartItem item) {
		this.id = Integer.parseInt(item.getString(MCDefResult.MCR_KIND_CHANNELITEM_ID));
		this.mName = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME);
		this.mNameEn = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN);
		this.mDescription = item.getString(MCDefResult.MCR_KIND_ITEM_DESCRIPTION);
		this.mDescriptionEn = item.getString(MCDefResult.MCR_KIND_ITEM_DESCRIPTION_EN);
		this.mContentTitle = item.getString(MCDefResult.MCR_KIND_ITEM_CONTENT_TITLE);
		this.mContentTitleEn = item.getString(MCDefResult.MCR_KIND_ITEM_CONTENT_TITLE_EN);
		this.mContentText = item.getString(MCDefResult.MCR_KIND_ITEM_CONTENT_TEXT);
		this.mContentTextEn = item.getString(MCDefResult.MCR_KIND_ITEM_CONTENT_TEXT_EN);
		this.mThumbIcon = item.getString(MCDefResult.MCR_KIND_ITEM_THUMB_ICON);
		this.mSeriesNo = item.getString(MCDefResult.MCR_KIND_ITEM_SERIES_NO);
		this.mSeriesContentNo = item.getString(MCDefResult.MCR_KIND_ITEM_SERIES_CONTENT_NO);
		mode = ChannelMode.valueOfString(item.mMode);
		this.thumnailSmall = item.getString(MCDefResult.MCR_KIND_THUMBNAIL_ICON_SMALL);
	}

	public CustomListItem(MCBusinessItem item) {
		this.id = Integer.parseInt(item.getString(MCDefResult.MCR_KIND_CHANNELITEM_ID));
		this.mName = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME);
		this.mNameEn = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN);
		this.parent = Integer.parseInt(item.getString(MCDefResult.MCR_KIND_ITEM_PARENT));
		this.nodeType = Integer.parseInt(item.getString(MCDefResult.MCR_KIND_ITEM_NODE_TYPE));
		if (this.nodeType == Consts.NODE_TYPE_MIDDLE)
			this.isNext = true;
		else
			this.isNext = false;
		this.permission = Integer.parseInt(item.getString(MCDefResult.MCR_KIND_PERMISSIONS));
	}

	public CustomListItem(ChannelInfo info) {
		this.id = info.getIndex();
		this.mName = info.getChannelName();
		this.mNameEn = info.getChannelName();
	}

	public CustomListItem(MCTemplateItem item) {
		this.id = Integer.parseInt(item.getString(MCDefResult.MCR_KIND_TEMPLATE_ID));
		this.templateType = Integer.parseInt(item.getString(MCDefResult.MCR_KIND_TEMPLATE_TYPE));
		this.mName = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME);
		String en = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN);
		this.mNameEn = !TextUtils.isEmpty(en) ? en : mName;
		this.mDigest = item.getString(MCDefResult.MCR_KIND_TEMPLATE_DIGEST);
		this.mAction = item.getString(MCDefResult.MCR_KIND_TEMPLATE_ACTION);
		this.mRule = item.getString(MCDefResult.MCR_KIND_TEMPLATE_RULE);
	}

	public CustomListItem(MCTimetableItem item) {
		this.id = Integer.parseInt(item.getString(MCDefResult.MCR_KIND_CHANNELITEM_ID));
		this.mName = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME);
		this.mNameEn = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN);
		this.timerType = item.getString(MCDefResult.MCR_KIND_TIMER_TYPE);
		this.timerRule = item.getString(MCDefResult.MCR_KIND_TIMER_RULE);
		this.sourceType = item.getString(MCDefResult.MCR_KIND_SOURCE_TYPE);
		this.sourceName = item.getString(MCDefResult.MCR_KIND_SOURCE_NAME);
		this.sourceUrl = item.getString(MCDefResult.MCR_KIND_SOURCE_URL);
	}

	public CustomListItem(MCBookmarkItem item) {
		this.id = Integer.parseInt(item.getString(MCDefResult.MCR_KIND_CHANNELITEM_ID));
		this.mName = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME);
		this.mNameEn = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN);
		this.sourceType = item.getString(MCDefResult.MCR_KIND_SOURCE_TYPE);
		this.sourceName = item.getString(MCDefResult.MCR_KIND_SOURCE_NAME);
		this.sourceUrl = item.getString(MCDefResult.MCR_KIND_SOURCE_URL);
	}

	public CustomListItem(TimerInfo info) {
		this.id = info.getIndex();
		this.mName = info.getName();
		this.mNameEn = info.getName();
		this.text = info.getResourceName();
		this.mDescription = Byte.toString(info.getWeek());
		this.parent = info.getTime();
		this.nodeType = info.getType();
	}

	public CustomListItem(MCStreamItem item) {
		this.id = Integer.parseInt(item.getString(MCDefResult.MCR_KIND_STREAM_ID));
		this.mName = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME) + "  "
				+ item.getString(MCDefResult.MCR_KIND_TRACKITEM_TITLE);
		this.mNameEn = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN) + "  "
				+ item.getString(MCDefResult.MCR_KIND_TRACKITEM_TITLE_EN);
		this.mContentTitle = item.getString(MCDefResult.MCR_KIND_TRACKITEM_TITLE);
		this.mContentTitleEn = item.getString(MCDefResult.MCR_KIND_TRACKITEM_TITLE_EN);
		this.mDescription = item.getString(MCDefResult.MCR_KIND_ITEM_DESCRIPTION);
		this.mDescriptionEn = item.getString(MCDefResult.MCR_KIND_ITEM_DESCRIPTION_EN);
		this.sourceType = item.getString(MCDefResult.MCR_KIND_SOURCE_TYPE);
		this.mediaType = item.getString(MCDefResult.MCR_KIND_MEDIA_TYPE);
		this.sessionType = item.getString(MCDefResult.MCR_KIND_SESSION_TYPE);
		this.contentLink = item.getString(MCDefResult.MCR_KIND_CONTENT_LINK);
		this.previewLink = item.getString(MCDefResult.MCR_KIND_PREVIEW_LINK);
		this.externalLink1 = item.getString(MCDefResult.MCR_KIND_EXTERNAL_LINK1);
		this.externalLink2 = item.getString(MCDefResult.MCR_KIND_EXTERNAL_LINK2);
		this.jacketId = item.getString(MCDefResult.MCR_KIND_TRACKITEM_JACKET_ID);
		this.stationId = item.getString(MCDefResult.MCR_KIND_STATION_ID);
		this.stationName = item.getString(MCDefResult.MCR_KIND_STATION_NAME);
		this.stationNameEn = item.getString(MCDefResult.MCR_KIND_STATION_NAME_EN);
		this.showDuration = item.getString(MCDefResult.MCR_KIND_SHOW_DURATION);
		List<String> tmp = item.getList(MCDefResult.MCR_KIND_SHOW_JACKETS);
		if (tmp != null)
			this.showJackets = new ArrayList<String>(tmp);
		else
			this.showJackets = new ArrayList<String>();
	}

	public CustomListItem(ChannelHistoryInfo item) {
		this.mName = item.getName();
		this.mNameEn = item.getNameEn();
		this.mode = ChannelMode.valueOfString(item.getMode());
		this.channelId = Integer.parseInt(item.getChannelId());
		this.range = item.getRange();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * ロック状態取得 マイチャンネルでのみ使用
	 *
	 * @return
	 */
	public int getLock() {
		return lock;
	}

	/**
	 * ロック状態セット マイチャンネルでのみ使用
	 *
	 * @param lock
	 */
	public void setLock(int lock) {
		this.lock = lock;
	}

	public String getmName() {
		return (!TextUtils.isEmpty(mName)) ? mName : mNameEn;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmNameEn() {
		return (!TextUtils.isEmpty(mNameEn)) ? mNameEn : mName;
	}

	public void setmNameEn(String mNameEn) {
		this.mNameEn = mNameEn;
	}

	public String getmDescription() {
		return (!TextUtils.isEmpty(mDescription)) ? mDescription : mDescriptionEn;
	}

	public void setmDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public String getmDescriptionEn() {
		return (!TextUtils.isEmpty(mDescriptionEn)) ? mDescriptionEn : mDescription;
	}

	public void setmDescriptionEn(String mDescriptionEn) {
		this.mDescriptionEn = mDescriptionEn;
	}

	public String getmContentTitle() {
		return (!TextUtils.isEmpty(mContentTitle)) ? mContentTitle : mContentTitleEn;
	}

	public void setmContentTitle(String mContentTitle) {
		this.mContentTitle = mContentTitle;
	}

	public String getmContentTitleEn() {
		return (!TextUtils.isEmpty(mContentTitleEn)) ? mContentTitleEn : mContentTitle;
	}

	public void setmContentTitleEn(String mContentTitleEn) {
		this.mContentTitleEn = mContentTitleEn;
	}

	public String getmContentText() {
		return (!TextUtils.isEmpty(mContentText)) ? mContentText : mContentTextEn;
	}

	public void setmContentText(String mContentText) {
		this.mContentText = mContentText;
	}

	public String getmContentTextEn() {
		return (!TextUtils.isEmpty(mContentTextEn)) ? mContentTextEn : mContentText;
	}

	public void setmContentTextEn(String mContentTextEn) {
		this.mContentTextEn = mContentTextEn;
	}

	public String getmThumbIcon() {
		return mThumbIcon;
	}

	public void setmThumbIcon(String mThumbIcon) {
		this.mThumbIcon = mThumbIcon;
	}

	public String getmDeliveryBegin() {
		return mDeliveryBegin;
	}

	public void setmDeliveryBegin(String mDeliveryBegin) {
		this.mDeliveryBegin = mDeliveryBegin;
	}

	public String getmDeliveryEnd() {
		return mDeliveryEnd;
	}

	public void setmDeliveryEnd(String mDeliveryEnd) {
		this.mDeliveryEnd = mDeliveryEnd;
	}

	public String getmSkipControl() {
		return mSkipControl;
	}

	public void setmSkipControl(String mSkipControl) {
		this.mSkipControl = mSkipControl;
	}

	public String getmAdControl() {
		return mAdControl;
	}

	public void setmAdControl(String mAdControl) {
		this.mAdControl = mAdControl;
	}

	public String getmSeriesNo() {
		return mSeriesNo;
	}

	public void setmSeriesNo(String mSeriesNo) {
		this.mSeriesNo = mSeriesNo;
	}

	public String getmSeriesContentNo() {
		return mSeriesContentNo;
	}

	public void setmSeriesContentNo(String mSeriesContentNo) {
		this.mSeriesContentNo = mSeriesContentNo;
	}

	// public Bitmap getmIcon() {
	// return mIcon;
	// }
	//
	// public void setmIcon(Bitmap mIcon) {
	// this.mIcon = mIcon;
	// }

	public String getmIcon() {
		return mIcon;
	}

	public void setmIcon(String mIcon) {
		this.mIcon = mIcon;
	}

	public ChannelMode getMode() {
		return mode;
	}

	public void setMode(ChannelMode mode) {
		this.mode = mode;
	}

	public boolean isNext() {
		return isNext;
	}

	public void setNext(boolean isNext) {
		this.isNext = isNext;
	}

	public boolean isHistory() {
		return isHistory;
	}

	public void setHistory(boolean isHistory) {
		this.isHistory = isHistory;
	}

	public String getBuyUrl() {
		return buyUrl;
	}

	public void setBuyUrl(String buyUrl) {
		this.buyUrl = buyUrl;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getThumnailSmall() {
		return thumnailSmall;
	}

	public void setThumnailSmall(String thumnailSmall) {
		this.thumnailSmall = thumnailSmall;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public int getNodeType() {
		return nodeType;
	}

	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}

	public int getTemplateType() {
		return templateType;
	}

	// public void setTemplateType(int templateType) {
	// this.templateType = templateType;
	// }

	public String getTimerType() {
		return timerType;
	}

	public void setTimerType(String timerType) {
		this.timerType = timerType;
	}

	public String getTimerRule() {
		return timerRule;
	}

	public void setTimerRule(String timerRule) {
		this.timerRule = timerRule;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getSessionType() {
		return sessionType;
	}

	public void setSessionType(String sessionType) {
		this.sessionType = sessionType;
	}

	public String getContentLink() {
		return contentLink;
	}

	public void setContentLink(String contentLink) {
		this.contentLink = contentLink;
	}

	public String getPreviewLink() {
		return previewLink;
	}

	public void setPreviewLink(String previewLink) {
		this.previewLink = previewLink;
	}

	public String getExternalLink1() {
		return externalLink1;
	}

	public void setExternalLink1(String externalLink1) {
		this.externalLink1 = externalLink1;
	}

	public String getExternalLink2() {
		return externalLink2;
	}

	public void setExternalLink2(String externalLink2) {
		this.externalLink2 = externalLink2;
	}

	public String getJacketId() {
		return jacketId;
	}

	public void setJacketId(String jacketId) {
		this.jacketId = jacketId;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public String getStationName() {
		return (!TextUtils.isEmpty(stationName)) ? stationName : stationNameEn;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationNameEn() {
		return (!TextUtils.isEmpty(stationNameEn)) ? stationNameEn : stationName;
	}

	public void setStationNameEn(String stationNameEn) {
		this.stationNameEn = stationNameEn;
	}

	public String getDigest() {
		return mDigest;
	}

	public String getAction() {
		return mAction;
	}

	public String getRule() {
		return mRule;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public String getShowDuration() {
		return showDuration;
	}

	public void setShowDuration(String showDuration) {
		this.showDuration = showDuration;
	}

	public List<String> getShowJackets() {
		return showJackets;
	}

	public void setShowJackets(List<String> showJackets) {
		this.showJackets = showJackets;
	}

	public int getPermission() {
		return permission;
	}

	public void setPermission(int permission) {
		this.permission = permission;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

}
