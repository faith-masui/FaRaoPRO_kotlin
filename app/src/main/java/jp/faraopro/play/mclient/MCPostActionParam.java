package jp.faraopro.play.mclient;

/**
 * MusicClient(Web API)アクセス用パラメータ管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCPostActionParam implements IMCPostActionParam {
	private String mEmail;
	private String mPassword;
	private String mActivationKey;
	private String mSessionKey;
	private String mMode;
	private String mChannel;
	private String mRange;
	private String mRatingTrackId;
	private String mRatingAction;
	private String mPlayComplete;
	private String mPlayDuration;
	private String mKeyword;
	private String mSlot;
	private String mName;
	private String mTrackId;
	private String mQuarity;
	// private String mFormat;
	private String mForce;
	private String mModeSerach;
	private String mJacketId;
	private String mAdId;
	private String mTypeDL;
	private String mRatingAdId;
	private String mLock;
	private String mBillingType;
	private String mMarketType;
	private String mReceipt;
	private String mTrackingKey;
	private String mPaid;
	private String mLanguage;
	private String mPaymentType;
	private String mPaymentId;
	private String mShareKey;
	private String mAccessToken;
	private String mGender;
	private String mBirthYear;
	private String mProvince;
	private String mRegion;
	private String mCountry;
	private String mThumbId;
	private String mCampaignDomain;
	private String mCampaignSerial;
	private String mCampaignId;
	private String mLatitude;
	private String mLongitude;
	private String mDistance;
	private String mIndustry;

	private String mDeviceToken;
	private String mParentNode;
	private String mTemplateType;
	private String mTemplateId;

	private String mSourceType;
	private String mStreamId;

	private String mErrorReason;

	private String mDowntimeBegin;
	private String mDowntimeEnd;
	private String mOfflineCause;

	private String mTargetDate;
	private String mPatternId;
	private String mAudioId;
	private String mFrameId;

	private String mComplete;
	private String mStatus;

	private IMCPostActionParam getParam;

	/**
	 * コンストラクタ
	 */
	public MCPostActionParam() {
		lclear();
	}

	@Override
	public void clear() {
		lclear();
	}

	private void lclear() {
		mEmail = null;
		mPassword = null;
		mActivationKey = null;
		mSessionKey = null;
		mMode = null;
		mChannel = null;
		mRange = null;
		mRatingTrackId = null;
		mRatingAction = null;
		mPlayComplete = null;
		mPlayDuration = null;
		mKeyword = null;
		mSlot = null;
		mName = null;
		mTrackId = null;
		mQuarity = null;
		// mFormat = null;
		mForce = null;
		mModeSerach = null;
		mJacketId = null;
		mAdId = null;
		mTypeDL = null;
		mRatingAdId = null;
		mLock = null;
		mBillingType = null;
		mMarketType = null;
		mReceipt = null;
		mTrackingKey = null;
		mPaid = null;
		mLanguage = null;
		mPaymentType = null;
		mPaymentId = null;
		mShareKey = null;
		mAccessToken = null;
		mGender = null;
		mBirthYear = null;
		mProvince = null;
		mRegion = null;
		mCountry = null;
		mThumbId = null;
		mCampaignDomain = null;
		mCampaignSerial = null;
		mCampaignId = null;
		mLatitude = null;
		mLongitude = null;
		mDistance = null;
		mIndustry = null;
		mDeviceToken = null;
		mParentNode = null;
		if (getParam != null)
			getParam.clear();
		getParam = null;
		mTemplateType = null;
		mTemplateId = null;

		mSourceType = null;
		mStreamId = null;

		mErrorReason = null;

		mDowntimeBegin = null;
		mDowntimeEnd = null;
		mOfflineCause = null;

		mTargetDate = null;
		mPatternId = null;
		mAudioId = null;
		mFrameId = null;

		mComplete = null;
		mStatus = null;
	}

	@Override
	public void setStringValue(int kind, String value) {
		switch (kind) {
		case MCDefParam.MCP_KIND_EMAIL:
			mEmail = value;
			break;
		case MCDefParam.MCP_KIND_PASSWORD:
			mPassword = value;
			break;
		case MCDefParam.MCP_KIND_ACTIVATIONKEY:
			mActivationKey = value;
			break;
		case MCDefParam.MCP_KIND_SESSIONKEY:
			mSessionKey = value;
			break;
		case MCDefParam.MCP_KIND_MODE_NO:
			mMode = value;
			break;
		case MCDefParam.MCP_KIND_MYCHANNELID:
			mChannel = value;
			break;
		case MCDefParam.MCP_KIND_RANGE:
			mRange = value;
			break;
		case MCDefParam.MCP_KIND_RATE_ACTION:
			mRatingAction = value;
			break;
		case MCDefParam.MCP_KIND_PLAY_COMPLETE:
			mPlayComplete = value;
			break;
		case MCDefParam.MCP_KIND_PLAY_DURATIN:
			mPlayDuration = value;
			break;
		case MCDefParam.MCP_KIND_KEYWORD:
			mKeyword = value;
			break;
		case MCDefParam.MCP_KIND_MYCHANNEL_ID:
			mSlot = value;
			break;
		case MCDefParam.MCP_KIND_MYCHANNEL_NAME:
			mName = value;
			break;
		case MCDefParam.MCP_KIND_TRACKID:
			mTrackId = value;
			break;
		case MCDefParam.MCP_KIND_RATING_TRACKID:
			mRatingTrackId = value;
			break;
		case MCDefParam.MCP_KIND_QUARITY:
			mQuarity = value;
			break;
		// case MCDefParam.MCP_KIND_FORMAT:
		// mFormat = value;
		// break;

		case MCDefParam.MCP_KIND_FORCE:
			mForce = value;
			break;
		case MCDefParam.MCP_KIND_MODE_SEARCH:
			mModeSerach = value;
			break;
		case MCDefParam.MCP_KIND_JACKETID:
			mJacketId = value;
			break;
		case MCDefParam.MCP_KIND_ADID:
			mAdId = value;
			break;
		case MCDefParam.MCP_KIND_TYPE_DL:
			mTypeDL = value;
			break;
		case MCDefParam.MCP_KIND_RATE_ADID:
			mRatingAdId = value;
			break;
		case MCDefParam.MCP_KIND_MYCHANNEL_LOCK:
			mLock = value;
			break;
		case MCDefParam.MCP_KIND_BILLING_TYPE:
			mBillingType = value;
			break;
		case MCDefParam.MCP_KIND_MARKET_TYPE:
			mMarketType = value;
			break;
		case MCDefParam.MCP_KIND_RECEIPT:
			mReceipt = value;
			break;
		case MCDefParam.MCP_KIND_TRACKING_KEY:
			mTrackingKey = value;
			break;
		case MCDefParam.MCP_KIND_PAID:
			mPaid = value;
			break;
		case MCDefParam.MCP_KIND_LANGUAGE:
			mLanguage = value;
			break;
		case MCDefParam.MCP_KIND_PAYMENT_TYPE:
			mPaymentType = value;
			break;
		case MCDefParam.MCP_KIND_PAYMENT_ID:
			mPaymentId = value;
			break;
		case MCDefParam.MCP_KIND_SHARE_SHAREKEY:
			mShareKey = value;
			break;
		case MCDefParam.MCP_KIND_ACCESS_TOKEN:
			mAccessToken = value;
			break;
		case MCDefParam.MCP_KIND_GENDER:
			mGender = value;
			break;
		case MCDefParam.MCP_KIND_BIRTH_YEAR:
			mBirthYear = value;
			break;
		case MCDefParam.MCP_KIND_PROVINCE:
			mProvince = value;
			break;
		case MCDefParam.MCP_KIND_REGION:
			mRegion = value;
			break;
		case MCDefParam.MCP_KIND_COUNTRY:
			mCountry = value;
			break;
		case MCDefParam.MCP_KIND_THUMB_ID:
			mThumbId = value;
			break;
		case MCDefParam.MCP_KIND_TICKET_DOMAIN:
			mCampaignDomain = value;
			break;
		case MCDefParam.MCP_KIND_TICKET_SERIAL:
			mCampaignSerial = value;
			break;
		case MCDefParam.MCP_KIND_CAMPAIGN_ID:
			mCampaignId = value;
			break;
		case MCDefParam.MCP_KIND_LATITUDE:
			mLatitude = value;
			break;
		case MCDefParam.MCP_KIND_LONGITUDE:
			mLongitude = value;
			break;
		case MCDefParam.MCP_KIND_DISTANCE:
			mDistance = value;
			break;
		case MCDefParam.MCP_KIND_INDUSTRY:
			mIndustry = value;
			break;

		case MCDefParam.MCP_KIND_DEVICE_TOKEN:
			mDeviceToken = value;
			break;
		case MCDefParam.MCP_KIND_PARENT_NODE:
			mParentNode = value;
			break;
		case MCDefParam.MCP_KIND_TEMPLATE_TYPE:
			mTemplateType = value;
			break;
		case MCDefParam.MCP_KIND_TEMPLATE_ID:
			mTemplateId = value;
			break;

		case MCDefParam.MCP_KIND_SOURCE_TYPE:
			mSourceType = value;
			break;
		case MCDefParam.MCP_KIND_STREAM_ID:
			mStreamId = value;
			break;

		case MCDefParam.MCP_KIND_ERROR_REASON:
			mErrorReason = value;
			break;

		case MCDefParam.MCP_KIND_DOWNTIME_BEGIN:
			mDowntimeBegin = value;
			break;
		case MCDefParam.MCP_KIND_DOWNTIME_END:
			mDowntimeEnd = value;
			break;
		case MCDefParam.MCP_KIND_OFFLINE_COUSE:
			mOfflineCause = value;
			break;

		case MCDefParam.MCP_KIND_TARGET_DATE:
			mTargetDate = value;
			break;
		case MCDefParam.MCP_KIND_PATTERN_ID:
			mPatternId = value;
			break;
		case MCDefParam.MCP_KIND_AUDIO_ID:
			mAudioId = value;
			break;
		case MCDefParam.MCP_KIND_FRAME_ID:
			mFrameId = value;
			break;

		case MCDefParam.MCP_KIND_COMPLETE:
			mComplete = value;
			break;
		case MCDefParam.MCP_KIND_STATUS:
			mStatus = value;
			break;

		default:
			break;
		}
	}

	@Override
	public String getStringValue(int kind) {
		String value = null;
		switch (kind) {
		case MCDefParam.MCP_KIND_EMAIL:
			value = mEmail;
			break;
		case MCDefParam.MCP_KIND_PASSWORD:
			value = mPassword;
			break;
		case MCDefParam.MCP_KIND_ACTIVATIONKEY:
			value = mActivationKey;
			break;
		case MCDefParam.MCP_KIND_SESSIONKEY:
			value = mSessionKey;
			break;
		case MCDefParam.MCP_KIND_MODE_NO:
			value = mMode;
			break;
		case MCDefParam.MCP_KIND_MYCHANNELID:
			value = mChannel;
			break;
		case MCDefParam.MCP_KIND_RANGE:
			value = mRange;
			break;
		case MCDefParam.MCP_KIND_RATE_ACTION:
			value = mRatingAction;
			break;
		case MCDefParam.MCP_KIND_PLAY_COMPLETE:
			value = mPlayComplete;
			break;
		case MCDefParam.MCP_KIND_PLAY_DURATIN:
			value = mPlayDuration;
			break;
		case MCDefParam.MCP_KIND_KEYWORD:
			value = mKeyword;
			break;
		case MCDefParam.MCP_KIND_MYCHANNEL_ID:
			value = mSlot;
			break;
		case MCDefParam.MCP_KIND_MYCHANNEL_NAME:
			value = mName;
			break;
		case MCDefParam.MCP_KIND_TRACKID:
			value = mTrackId;
			break;
		case MCDefParam.MCP_KIND_RATING_TRACKID:
			value = mRatingTrackId;
			break;
		case MCDefParam.MCP_KIND_QUARITY:
			value = mQuarity;
			break;
		// case MCDefParam.MCP_KIND_FORMAT:
		// value = mFormat;
		// break;
		case MCDefParam.MCP_KIND_FORCE:
			value = mForce;
			break;
		case MCDefParam.MCP_KIND_MODE_SEARCH:
			value = mModeSerach;
			break;
		case MCDefParam.MCP_KIND_JACKETID:
			value = mJacketId;
			break;
		case MCDefParam.MCP_KIND_ADID:
			value = mAdId;
			break;
		case MCDefParam.MCP_KIND_TYPE_DL:
			value = mTypeDL;
			break;
		case MCDefParam.MCP_KIND_RATE_ADID:
			value = mRatingAdId;
			break;
		case MCDefParam.MCP_KIND_MYCHANNEL_LOCK:
			value = mLock;
			break;
		case MCDefParam.MCP_KIND_BILLING_TYPE:
			value = mBillingType;
			break;
		case MCDefParam.MCP_KIND_MARKET_TYPE:
			value = mMarketType;
			break;
		case MCDefParam.MCP_KIND_RECEIPT:
			value = mReceipt;
			break;
		case MCDefParam.MCP_KIND_TRACKING_KEY:
			value = mTrackingKey;
			break;
		case MCDefParam.MCP_KIND_PAID:
			value = mPaid;
			break;
		case MCDefParam.MCP_KIND_LANGUAGE:
			value = mLanguage;
			break;
		case MCDefParam.MCP_KIND_PAYMENT_TYPE:
			value = mPaymentType;
			break;
		case MCDefParam.MCP_KIND_PAYMENT_ID:
			value = mPaymentId;
			break;
		case MCDefParam.MCP_KIND_SHARE_SHAREKEY:
			value = mShareKey;
			break;
		case MCDefParam.MCP_KIND_ACCESS_TOKEN:
			value = mAccessToken;
			break;
		case MCDefParam.MCP_KIND_GENDER:
			value = mGender;
			break;
		case MCDefParam.MCP_KIND_BIRTH_YEAR:
			value = mBirthYear;
			break;
		case MCDefParam.MCP_KIND_PROVINCE:
			value = mProvince;
			break;
		case MCDefParam.MCP_KIND_REGION:
			value = mRegion;
			break;
		case MCDefParam.MCP_KIND_COUNTRY:
			value = mCountry;
			break;
		case MCDefParam.MCP_KIND_THUMB_ID:
			value = mThumbId;
			break;
		case MCDefParam.MCP_KIND_TICKET_DOMAIN:
			value = mCampaignDomain;
			break;
		case MCDefParam.MCP_KIND_TICKET_SERIAL:
			value = mCampaignSerial;
			break;
		case MCDefParam.MCP_KIND_CAMPAIGN_ID:
			value = mCampaignId;
			break;
		case MCDefParam.MCP_KIND_LATITUDE:
			value = mLatitude;
			break;
		case MCDefParam.MCP_KIND_LONGITUDE:
			value = mLongitude;
			break;
		case MCDefParam.MCP_KIND_DISTANCE:
			value = mDistance;
			break;
		case MCDefParam.MCP_KIND_INDUSTRY:
			value = mIndustry;
			break;

		case MCDefParam.MCP_KIND_DEVICE_TOKEN:
			value = mDeviceToken;
			break;
		case MCDefParam.MCP_KIND_PARENT_NODE:
			value = mParentNode;
			break;
		case MCDefParam.MCP_KIND_TEMPLATE_TYPE:
			value = mTemplateType;
			break;
		case MCDefParam.MCP_KIND_TEMPLATE_ID:
			value = mTemplateId;
			break;

		case MCDefParam.MCP_KIND_SOURCE_TYPE:
			value = mSourceType;
			break;
		case MCDefParam.MCP_KIND_STREAM_ID:
			value = mStreamId;
			break;

		case MCDefParam.MCP_KIND_ERROR_REASON:
			value = mErrorReason;
			break;

		case MCDefParam.MCP_KIND_DOWNTIME_BEGIN:
			value = mDowntimeBegin;
			break;
		case MCDefParam.MCP_KIND_DOWNTIME_END:
			value = mDowntimeEnd;
			break;
		case MCDefParam.MCP_KIND_OFFLINE_COUSE:
			value = mOfflineCause;
			break;

		case MCDefParam.MCP_KIND_TARGET_DATE:
			value = mTargetDate;
			break;
		case MCDefParam.MCP_KIND_PATTERN_ID:
			value = mPatternId;
			break;
		case MCDefParam.MCP_KIND_AUDIO_ID:
			value = mAudioId;
			break;
		case MCDefParam.MCP_KIND_FRAME_ID:
			value = mFrameId;
			break;

		case MCDefParam.MCP_KIND_COMPLETE:
			value = mComplete;
			break;
		case MCDefParam.MCP_KIND_STATUS:
			value = mStatus;
			break;

		default:
			break;
		}
		return value;
	}

	/**
	 * 全パラメータ情報のコピー
	 * 
	 * @param param
	 */
	public void copyParam(IMCPostActionParam param) {
		String value = null;

		setStringValue(MCDefParam.MCP_KIND_EMAIL, param.getStringValue(MCDefParam.MCP_KIND_EMAIL));
		setStringValue(MCDefParam.MCP_KIND_PASSWORD, param.getStringValue(MCDefParam.MCP_KIND_PASSWORD));
		setStringValue(MCDefParam.MCP_KIND_ACTIVATIONKEY, param.getStringValue(MCDefParam.MCP_KIND_ACTIVATIONKEY));
		setStringValue(MCDefParam.MCP_KIND_SESSIONKEY, param.getStringValue(MCDefParam.MCP_KIND_SESSIONKEY));
		setStringValue(MCDefParam.MCP_KIND_MODE_NO, param.getStringValue(MCDefParam.MCP_KIND_MODE_NO));
		setStringValue(MCDefParam.MCP_KIND_MYCHANNELID, param.getStringValue(MCDefParam.MCP_KIND_MYCHANNELID));
		setStringValue(MCDefParam.MCP_KIND_RANGE, param.getStringValue(MCDefParam.MCP_KIND_RANGE));
		setStringValue(MCDefParam.MCP_KIND_RATE_ACTION, param.getStringValue(MCDefParam.MCP_KIND_RATE_ACTION));
		setStringValue(MCDefParam.MCP_KIND_PLAY_COMPLETE, param.getStringValue(MCDefParam.MCP_KIND_PLAY_COMPLETE));
		setStringValue(MCDefParam.MCP_KIND_PLAY_DURATIN, param.getStringValue(MCDefParam.MCP_KIND_PLAY_DURATIN));
		setStringValue(MCDefParam.MCP_KIND_KEYWORD, param.getStringValue(MCDefParam.MCP_KIND_KEYWORD));
		setStringValue(MCDefParam.MCP_KIND_MYCHANNEL_ID, param.getStringValue(MCDefParam.MCP_KIND_MYCHANNEL_ID));
		setStringValue(MCDefParam.MCP_KIND_MYCHANNEL_NAME, param.getStringValue(MCDefParam.MCP_KIND_MYCHANNEL_NAME));

		value = param.getStringValue(MCDefParam.MCP_KIND_TRACKID);
		if (value != null)
			setStringValue(MCDefParam.MCP_KIND_TRACKID, param.getStringValue(MCDefParam.MCP_KIND_TRACKID));

		setStringValue(MCDefParam.MCP_KIND_RATING_TRACKID, param.getStringValue(MCDefParam.MCP_KIND_RATING_TRACKID));
		setStringValue(MCDefParam.MCP_KIND_QUARITY, param.getStringValue(MCDefParam.MCP_KIND_QUARITY));
		// setStringValue(MCDefParam.MCP_KIND_FORMAT,
		// param.getStringValue(MCDefParam.MCP_KIND_FORMAT));
		setStringValue(MCDefParam.MCP_KIND_FORCE, param.getStringValue(MCDefParam.MCP_KIND_FORCE));
		setStringValue(MCDefParam.MCP_KIND_MODE_SEARCH, param.getStringValue(MCDefParam.MCP_KIND_MODE_SEARCH));

		value = param.getStringValue(MCDefParam.MCP_KIND_JACKETID);
		if (value != null)
			setStringValue(MCDefParam.MCP_KIND_JACKETID, param.getStringValue(MCDefParam.MCP_KIND_JACKETID));

		setStringValue(MCDefParam.MCP_KIND_ADID, param.getStringValue(MCDefParam.MCP_KIND_ADID));
		setStringValue(MCDefParam.MCP_KIND_TYPE_DL, param.getStringValue(MCDefParam.MCP_KIND_TYPE_DL));
		setStringValue(MCDefParam.MCP_KIND_RATE_ADID, param.getStringValue(MCDefParam.MCP_KIND_RATE_ADID));
		setStringValue(MCDefParam.MCP_KIND_MYCHANNEL_LOCK, param.getStringValue(MCDefParam.MCP_KIND_MYCHANNEL_LOCK));
		setStringValue(MCDefParam.MCP_KIND_BILLING_TYPE, param.getStringValue(MCDefParam.MCP_KIND_BILLING_TYPE));
		setStringValue(MCDefParam.MCP_KIND_MARKET_TYPE, param.getStringValue(MCDefParam.MCP_KIND_MARKET_TYPE));
		setStringValue(MCDefParam.MCP_KIND_RECEIPT, param.getStringValue(MCDefParam.MCP_KIND_RECEIPT));
		setStringValue(MCDefParam.MCP_KIND_TRACKING_KEY, param.getStringValue(MCDefParam.MCP_KIND_TRACKING_KEY));
		setStringValue(MCDefParam.MCP_KIND_PAID, param.getStringValue(MCDefParam.MCP_KIND_PAID));
		setStringValue(MCDefParam.MCP_KIND_LANGUAGE, param.getStringValue(MCDefParam.MCP_KIND_LANGUAGE));
		setStringValue(MCDefParam.MCP_KIND_PAYMENT_TYPE, param.getStringValue(MCDefParam.MCP_KIND_PAYMENT_TYPE));
		setStringValue(MCDefParam.MCP_KIND_PAYMENT_ID, param.getStringValue(MCDefParam.MCP_KIND_PAYMENT_ID));
		setStringValue(MCDefParam.MCP_KIND_SHARE_SHAREKEY, param.getStringValue(MCDefParam.MCP_KIND_SHARE_SHAREKEY));
		setStringValue(MCDefParam.MCP_KIND_ACCESS_TOKEN, param.getStringValue(MCDefParam.MCP_KIND_ACCESS_TOKEN));
		setStringValue(MCDefParam.MCP_KIND_GENDER, param.getStringValue(MCDefParam.MCP_KIND_GENDER));
		setStringValue(MCDefParam.MCP_KIND_BIRTH_YEAR, param.getStringValue(MCDefParam.MCP_KIND_BIRTH_YEAR));
		setStringValue(MCDefParam.MCP_KIND_PROVINCE, param.getStringValue(MCDefParam.MCP_KIND_PROVINCE));
		setStringValue(MCDefParam.MCP_KIND_REGION, param.getStringValue(MCDefParam.MCP_KIND_REGION));
		setStringValue(MCDefParam.MCP_KIND_COUNTRY, param.getStringValue(MCDefParam.MCP_KIND_COUNTRY));
		setStringValue(MCDefParam.MCP_KIND_THUMB_ID, param.getStringValue(MCDefParam.MCP_KIND_THUMB_ID));
		setStringValue(MCDefParam.MCP_KIND_TICKET_DOMAIN, param.getStringValue(MCDefParam.MCP_KIND_TICKET_DOMAIN));
		setStringValue(MCDefParam.MCP_KIND_TICKET_SERIAL, param.getStringValue(MCDefParam.MCP_KIND_TICKET_SERIAL));
		setStringValue(MCDefParam.MCP_KIND_CAMPAIGN_ID, param.getStringValue(MCDefParam.MCP_KIND_CAMPAIGN_ID));
		setStringValue(MCDefParam.MCP_KIND_LATITUDE, param.getStringValue(MCDefParam.MCP_KIND_LATITUDE));
		setStringValue(MCDefParam.MCP_KIND_LONGITUDE, param.getStringValue(MCDefParam.MCP_KIND_LONGITUDE));
		setStringValue(MCDefParam.MCP_KIND_DISTANCE, param.getStringValue(MCDefParam.MCP_KIND_DISTANCE));
		setStringValue(MCDefParam.MCP_KIND_INDUSTRY, param.getStringValue(MCDefParam.MCP_KIND_INDUSTRY));

		setStringValue(MCDefParam.MCP_KIND_DEVICE_TOKEN, param.getStringValue(MCDefParam.MCP_KIND_DEVICE_TOKEN));
		value = param.getStringValue(MCDefParam.MCP_KIND_PARENT_NODE);
		if (value != null)
			setStringValue(MCDefParam.MCP_KIND_PARENT_NODE, param.getStringValue(MCDefParam.MCP_KIND_PARENT_NODE));
		setStringValue(MCDefParam.MCP_KIND_TEMPLATE_TYPE, param.getStringValue(MCDefParam.MCP_KIND_TEMPLATE_TYPE));
		setStringValue(MCDefParam.MCP_KIND_TEMPLATE_ID, param.getStringValue(MCDefParam.MCP_KIND_TEMPLATE_ID));

		setStringValue(MCDefParam.MCP_KIND_SOURCE_TYPE, param.getStringValue(MCDefParam.MCP_KIND_SOURCE_TYPE));
		setStringValue(MCDefParam.MCP_KIND_STREAM_ID, param.getStringValue(MCDefParam.MCP_KIND_STREAM_ID));

		value = param.getStringValue(MCDefParam.MCP_KIND_ERROR_REASON);
		if (value != null)
			setStringValue(MCDefParam.MCP_KIND_ERROR_REASON, param.getStringValue(MCDefParam.MCP_KIND_ERROR_REASON));

		setStringValue(MCDefParam.MCP_KIND_DOWNTIME_BEGIN, param.getStringValue(MCDefParam.MCP_KIND_DOWNTIME_BEGIN));
		setStringValue(MCDefParam.MCP_KIND_DOWNTIME_END, param.getStringValue(MCDefParam.MCP_KIND_DOWNTIME_END));
		value = param.getStringValue(MCDefParam.MCP_KIND_OFFLINE_COUSE);
		if (value != null)
			setStringValue(MCDefParam.MCP_KIND_OFFLINE_COUSE, param.getStringValue(MCDefParam.MCP_KIND_OFFLINE_COUSE));
		setStringValue(MCDefParam.MCP_KIND_TARGET_DATE, param.getStringValue(MCDefParam.MCP_KIND_TARGET_DATE));
		setStringValue(MCDefParam.MCP_KIND_PATTERN_ID, param.getStringValue(MCDefParam.MCP_KIND_PATTERN_ID));
		setStringValue(MCDefParam.MCP_KIND_AUDIO_ID, param.getStringValue(MCDefParam.MCP_KIND_AUDIO_ID));
		setStringValue(MCDefParam.MCP_KIND_FRAME_ID, param.getStringValue(MCDefParam.MCP_KIND_FRAME_ID));

		setStringValue(MCDefParam.MCP_KIND_COMPLETE, param.getStringValue(MCDefParam.MCP_KIND_COMPLETE));

		setStringValue(MCDefParam.MCP_KIND_STATUS, param.getStringValue(MCDefParam.MCP_KIND_STATUS));
	}

	public void copyGetParam(IMCPostActionParam param) {
		this.getParam = param;
	}

	public IMCPostActionParam getGetParam() {
		return this.getParam;
	}
}
