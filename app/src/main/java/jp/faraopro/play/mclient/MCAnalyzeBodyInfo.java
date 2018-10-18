package jp.faraopro.play.mclient;

/**
 * Musicサーバーからの受信結果管理クラス(HttpHeader+XML)
 * 
 * @author AIM Corporation
 * 
 */
public class MCAnalyzeBodyInfo implements IMCResultInfo {

	private int mStatusCode;
	private String mMessage;
	private String mActivationKey;
	private String mSessionKey;
	private String mAdvertiseRatio;
	private String mSkipRemaining;
	private MCTrackItemList mTrackList;
	// private MCGenreChannelItemList mGenreChannelList;
	// private MCPersonalChannelItemList mPersonalChannelList;

	// 2011/11/10 追加
	private String mMessage_en;
	private String mStatus_usr;
	private String mSubscriptionType;
	private String mSubscriptionStatus;
	private String mPlayableTracks;
	private String mTrackingKey;

	private MCGenreItemList mGenreList;
	private MCChannelItemList mChannelList;
	private MCChartItemList mChartList;
	private MCSearchItemList mSearchList;
	private MCAdItemList mAdList;

	private String mPaymentType;
	private String mPaymentId;

	private String mShareKey;
	private String mShareId;
	private String mShareName;

	private MCLocationItemList mLocationList;
	private MCBenifitItemList mBenifitList;
	private MCCampaignItemList mCampaignList;
	private MCChartItemList mShuffleList;
	private MCShopItemList mShopList;
	private MCBusinessItemList mBusinessList;
	private MCTemplateItemList mTemplateList;
	private MCTimetableItemList mTimetableList;
	private MCBookmarkItemList mBookmarkList;
	private MCStreamItemList mStreamList;

	private MCFrameItemList mFrameList;

	private String mChannelName;
	private String mChannelNameEn;

	private MCCdnMusicItem mCdnItem;
	private MCStreamItem mPlayingItem;
	private MCScheduleItem mScheduleItem;

	private String trackingInterval;
	private String trackingLatitude;
	private String trackingLongitude;

	private String serviceType;
	private String serviceLevel;
	private String features;
	private String permissions;
	private String marketType;
	private String expiresDate;
	private String expiresLimit;

	/**
	 * コンストラクタ
	 */
	public MCAnalyzeBodyInfo() {
		lclear();
	}

	@Override
	public void clear() {
		lclear();
	}

	private void lclear() {
		mStatusCode = 0;
		mMessage = null;
		mActivationKey = null;
		mSessionKey = null;
		mAdvertiseRatio = null;
		mSkipRemaining = null;
		if (mTrackList != null)
			mTrackList.clear();
		mTrackList = null;

		// 2011/11/10 追加
		if (mGenreList != null)
			mGenreList.clear();
		mGenreList = null;

		if (mChannelList != null)
			mChannelList.clear();
		mChannelList = null;

		if (mChartList != null)
			mChartList.clear();
		mChartList = null;

		if (mSearchList != null)
			mSearchList.clear();
		mSearchList = null;

		if (mAdList != null)
			mAdList.clear();
		mAdList = null;

		// 2011/11/10 追加
		mMessage_en = null;
		mStatus_usr = null;
		mSubscriptionType = null;
		mSubscriptionStatus = null;
		mPlayableTracks = null;
		mTrackingKey = null;
		mPaymentType = null;
		mPaymentId = null;
		mShareKey = null;
		mShareId = null;
		mShareName = null;

		if (mLocationList != null)
			mLocationList.clear();
		mLocationList = null;
		if (mBenifitList != null)
			mBenifitList.clear();
		mBenifitList = null;
		if (mCampaignList != null)
			mCampaignList.clear();
		mCampaignList = null;
		if (mShuffleList != null)
			mShuffleList.clear();
		mShuffleList = null;
		if (mBusinessList != null)
			mBusinessList.clear();
		mBusinessList = null;
		if (mTemplateList != null)
			mTemplateList.clear();
		mTemplateList = null;
		if (mTimetableList != null)
			mTimetableList.clear();
		mTimetableList = null;
		if (mBookmarkList != null)
			mBookmarkList.clear();
		mBookmarkList = null;
		if (mStreamList != null)
			mStreamList.clear();
		mStreamList = null;
		if (mFrameList != null)
			mFrameList.clear();
		mFrameList = null;

		mChannelName = null;
		mChannelNameEn = null;
		if (mShopList != null)
			mShopList.clear();
		mShopList = null;
		mCdnItem = null;
		mPlayingItem = null;

		trackingInterval = null;
		trackingLatitude = null;
		trackingLongitude = null;

		mScheduleItem = null;
		serviceType = null;
		serviceLevel = null;
		features = null;
		permissions = null;
		marketType = null;
		expiresDate = null;
		expiresLimit = null;
	}

	@Override
	public void setString(int kind, String value) {
		switch (kind) {
		// case MCDefResult.MCR_KIND_STATUS_CODE:
		// mStatusCode = value;
		// break;
		case MCDefResult.MCR_KIND_MESSAGE:
			mMessage = value;
			break;
		case MCDefResult.MCR_KIND_ACTIVATIONKEY:
			mActivationKey = value;
			break;
		case MCDefResult.MCR_KIND_SESSIONKEY:
			mSessionKey = value;
			break;
		case MCDefResult.MCR_KIND_ADVERTISERATIO:
			mAdvertiseRatio = value;
			break;
		case MCDefResult.MCR_KIND_SKIPREMAINING:
			mSkipRemaining = value;
			break;

		// 2011/11/10 追加
		case MCDefResult.MCR_KIND_MESSAGE_EN:
			mMessage_en = value;
			break;
		case MCDefResult.MCR_KIND_USER_STATUS:
			mStatus_usr = value;
			break;
		case MCDefResult.MCR_KIND_SUBSCRIPTION_TYPE:
			mSubscriptionType = value;
			break;
		case MCDefResult.MCR_KIND_SUBSCRIPTION_STATUS:
			mSubscriptionStatus = value;
			break;
		case MCDefResult.MCR_KIND_PLAYABLE_TRACKS:
			mPlayableTracks = value;
			break;
		case MCDefResult.MCR_KIND_TRACKING_KEY:
			mTrackingKey = value;
			break;

		// 2011/12/21
		case MCDefResult.MCR_KIND_PAYMENT_TYPE:
			mPaymentType = value;
			break;
		case MCDefResult.MCR_KIND_PAYMENT_ID:
			mPaymentId = value;
			break;
		case MCDefResult.MCR_KIND_SHAREITEM_SHARE_KEY:
			mShareKey = value;
			break;
		case MCDefResult.MCR_KIND_SHAREITEM_SHARE_ID:
			mShareId = value;
			break;
		case MCDefResult.MCR_KIND_SHAREITEM_SHARE_NAME:
			mShareName = value;
			break;
		case MCDefResult.MCR_KIND_CHANNEL_NAME:
			mChannelName = value;
			break;
		case MCDefResult.MCR_KIND_CHANNEL_NAME_EN:
			mChannelNameEn = value;

		case MCDefResult.MCR_KIND_TRACKING_INTERVAL:
			trackingInterval = value;
			break;
		case MCDefResult.MCR_KIND_LATITUDE:
			trackingLatitude = value;
			break;
		case MCDefResult.MCR_KIND_LONGITUDE:
			trackingLongitude = value;
			break;

		case MCDefResult.MCR_KIND_SERVICE_TYPE:
			serviceType = value;
			break;
		case MCDefResult.MCR_KIND_SERVICE_LEVEL:
			serviceLevel = value;
			break;
		case MCDefResult.MCR_KIND_FEATURES:
			features = value;
			break;
		case MCDefResult.MCR_KIND_PERMISSIONS:
			permissions = value;
			break;
		case MCDefResult.MCR_KIND_MARKET_TYPE:
			marketType = value;
			break;
		case MCDefResult.MCR_KIND_EXPIRES_DATE:
			expiresDate = value;
			break;
		case MCDefResult.MCR_KIND_EXPIRES_LIMIT:
			expiresLimit = value;
			break;

		default:
			break;
		}
	}

	@Override
	public String getString(int kind) {
		String value = null;
		switch (kind) {
		// case MCDefResult.MCR_KIND_STATUS_CODE:
		// value = mStatusCode;
		// break;
		case MCDefResult.MCR_KIND_MESSAGE:
			value = mMessage;
			break;
		case MCDefResult.MCR_KIND_ACTIVATIONKEY:
			value = mActivationKey;
			break;
		case MCDefResult.MCR_KIND_SESSIONKEY:
			value = mSessionKey;
			break;
		case MCDefResult.MCR_KIND_ADVERTISERATIO:
			value = mAdvertiseRatio;
			break;
		case MCDefResult.MCR_KIND_SKIPREMAINING:
			value = mSkipRemaining;
			break;

		// 2011/11/10 追加
		case MCDefResult.MCR_KIND_MESSAGE_EN:
			value = mMessage_en;
			break;
		case MCDefResult.MCR_KIND_USER_STATUS:
			value = mStatus_usr;
			break;
		case MCDefResult.MCR_KIND_SUBSCRIPTION_TYPE:
			value = mSubscriptionType;
			break;
		case MCDefResult.MCR_KIND_SUBSCRIPTION_STATUS:
			value = mSubscriptionStatus;
			break;
		case MCDefResult.MCR_KIND_PLAYABLE_TRACKS:
			value = mPlayableTracks;
			break;
		case MCDefResult.MCR_KIND_TRACKING_KEY:
			value = mTrackingKey;
			break;

		// 2011/12/21
		case MCDefResult.MCR_KIND_PAYMENT_TYPE:
			value = mPaymentType;
			break;
		case MCDefResult.MCR_KIND_PAYMENT_ID:
			value = mPaymentId;
			break;
		case MCDefResult.MCR_KIND_SHAREITEM_SHARE_KEY:
			value = mShareKey;
			break;
		case MCDefResult.MCR_KIND_SHAREITEM_SHARE_ID:
			value = mShareId;
			break;
		case MCDefResult.MCR_KIND_SHAREITEM_SHARE_NAME:
			value = mShareName;
			break;
		case MCDefResult.MCR_KIND_CHANNEL_NAME:
			value = mChannelName;
			break;
		case MCDefResult.MCR_KIND_CHANNEL_NAME_EN:
			value = mChannelNameEn;
			break;

		case MCDefResult.MCR_KIND_TRACKING_INTERVAL:
			value = trackingInterval;
			break;
		case MCDefResult.MCR_KIND_LATITUDE:
			value = trackingLatitude;
			break;
		case MCDefResult.MCR_KIND_LONGITUDE:
			value = trackingLongitude;
			break;

		case MCDefResult.MCR_KIND_SERVICE_TYPE:
			value = serviceType;
			break;
		case MCDefResult.MCR_KIND_SERVICE_LEVEL:
			value = serviceLevel;
			break;
		case MCDefResult.MCR_KIND_FEATURES:
			value = features;
			break;
		case MCDefResult.MCR_KIND_PERMISSIONS:
			value = permissions;
			break;
		case MCDefResult.MCR_KIND_MARKET_TYPE:
			value = marketType;
			break;
		case MCDefResult.MCR_KIND_EXPIRES_DATE:
			value = expiresDate;
			break;
		case MCDefResult.MCR_KIND_EXPIRES_LIMIT:
			value = expiresLimit;
			break;

		}
		return value;
	}

	@Override
	public void setList(int kind, IMCItemList list) {
		switch (kind) {
		case IMCResultInfo.MC_LIST_KIND_TRACK:
			mTrackList = (MCTrackItemList) list;
			break;
		case IMCResultInfo.MC_LIST_KIND_GENRE:
			mGenreList = (MCGenreItemList) list;
			break;
		case IMCResultInfo.MC_LIST_KIND_CHANNEL:
			mChannelList = (MCChannelItemList) list;
			break;
		case IMCResultInfo.MC_LIST_KIND_CHART:
			mChartList = (MCChartItemList) list;
			break;
		case IMCResultInfo.MC_LIST_KIND_SEARCH:
			mSearchList = (MCSearchItemList) list;
			break;
		case IMCResultInfo.MC_LIST_KIND_AD:
			mAdList = (MCAdItemList) list;
			break;
		case IMCResultInfo.MC_LIST_KIND_FEATURED:
			mSearchList = (MCSearchItemList) list;
			break;
		case IMCResultInfo.MC_LIST_KIND_LOCATION:
			mLocationList = (MCLocationItemList) list;
			break;
		case IMCResultInfo.MC_LIST_KIND_BENIFIT:
			mBenifitList = (MCBenifitItemList) list;
			break;
		case IMCResultInfo.MC_LIST_KIND_CAMPAIGN:
			mCampaignList = (MCCampaignItemList) list;
			break;
		case IMCResultInfo.MC_LIST_KIND_SHUFFLE:
			mShuffleList = (MCChartItemList) list;
			break;
		case IMCResultInfo.MC_LIST_KIND_SHOP:
			mShopList = (MCShopItemList) list;
			break;
		case IMCResultInfo.MC_LIST_KIND_BUSINESS:
			mBusinessList = (MCBusinessItemList) list;
			break;
		case IMCResultInfo.MC_LIST_KIND_TEMPLATE:
			mTemplateList = (MCTemplateItemList) list;
			break;
		case IMCResultInfo.MC_LIST_KIND_TIMETABLE:
			mTimetableList = (MCTimetableItemList) list;
			break;
		case IMCResultInfo.MC_LIST_KIND_BOOKMARK:
			mBookmarkList = (MCBookmarkItemList) list;
			break;
		case IMCResultInfo.MC_LIST_KIND_STREAM:
			mStreamList = (MCStreamItemList) list;
			break;

		case IMCResultInfo.MC_LIST_KIND_FRAME:
			mFrameList = (MCFrameItemList) list;
			break;
		default:
			break;
		}
	}

	@Override
	public IMCItemList getList(int kind) {
		IMCItemList item = null;
		switch (kind) {
		case IMCResultInfo.MC_LIST_KIND_TRACK:
			item = mTrackList;
			break;
		case IMCResultInfo.MC_LIST_KIND_GENRE:
			item = mGenreList;
			break;
		case IMCResultInfo.MC_LIST_KIND_CHANNEL:
			item = mChannelList;
			break;
		case IMCResultInfo.MC_LIST_KIND_CHART:
			item = mChartList;
			break;
		case IMCResultInfo.MC_LIST_KIND_SEARCH:
			item = mSearchList;
			break;
		case IMCResultInfo.MC_LIST_KIND_AD:
			item = mAdList;
			break;
		case IMCResultInfo.MC_LIST_KIND_FEATURED:
			item = mSearchList;
			break;
		case IMCResultInfo.MC_LIST_KIND_LOCATION:
			item = mLocationList;
			break;
		case IMCResultInfo.MC_LIST_KIND_BENIFIT:
			item = mBenifitList;
			break;
		case IMCResultInfo.MC_LIST_KIND_CAMPAIGN:
			item = mCampaignList;
			break;
		case IMCResultInfo.MC_LIST_KIND_SHUFFLE:
			item = mShuffleList;
			break;
		case IMCResultInfo.MC_LIST_KIND_SHOP:
			item = mShopList;
			break;
		case IMCResultInfo.MC_LIST_KIND_BUSINESS:
			item = mBusinessList;
			break;
		case IMCResultInfo.MC_LIST_KIND_TEMPLATE:
			item = mTemplateList;
			break;
		case IMCResultInfo.MC_LIST_KIND_TIMETABLE:
			item = mTimetableList;
			break;
		case IMCResultInfo.MC_LIST_KIND_BOOKMARK:
			item = mBookmarkList;
			break;
		case IMCResultInfo.MC_LIST_KIND_STREAM:
			item = mStreamList;
			break;

		case IMCResultInfo.MC_LIST_KIND_FRAME:
			item = mFrameList;
			break;
		default:
			break;
		}
		return item;
	}

	@Override
	public void setInt(int kind, int value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_STATUS_CODE:
			mStatusCode = value;
			break;
		default:
			break;
		}
	}

	@Override
	public int getInt(int kind) {
		int value = 0;
		switch (kind) {
		case MCDefResult.MCR_KIND_STATUS_CODE:
			value = mStatusCode;
			break;
		default:
			break;
		}
		return value;
	}

	@Override
	public void setItem(int kind, IMCItem item) {
		switch (kind) {
		case MCDefResult.MCR_KIND_CDN_LIST:
			mCdnItem = (MCCdnMusicItem) item;
			break;
		case MCDefResult.MCR_KIND_PLAYING_LIST:
			mPlayingItem = (MCStreamItem) item;
			break;
		case MCDefResult.MCR_KIND_PATTERN_RESPONSE:
			mScheduleItem = (MCScheduleItem) item;
			break;
		}
	}

	@Override
	public IMCItem getItem(int kind) {
		IMCItem item = null;
		switch (kind) {
		case MCDefResult.MCR_KIND_CDN_LIST:
			item = mCdnItem;
			break;
		case MCDefResult.MCR_KIND_PLAYING_LIST:
			item = mPlayingItem;
			break;
		case MCDefResult.MCR_KIND_PATTERN_RESPONSE:
			item = mScheduleItem;
			break;
		default:
			break;
		}
		return item;
	}
}
