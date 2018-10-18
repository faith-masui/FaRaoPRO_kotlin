package jp.faraopro.play.mclient;

/**
 * 楽曲情報(アイテム)管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCMusicItemInfo implements IMCMusicItemInfo {

	private String mAdvertiseRatio;
	private String mSkipRemaining;
	private IMCItem mTrackItem;
	private String mOutputPath;
	private int mFileStatus;
	private String mEncMethod;
	private String mEncKey;
	private String mEncIv;
	private String mApiSig;
	private String mRegTime;
	private String mPlayDuration;
	private String mPlayableTracks;
	private String mChannelName;
	private String mChannelNameEn;

	/**
	 * コンストラクタ
	 * 
	 * @param kind
	 * @param param
	 */
	public MCMusicItemInfo() {
		lclear();
	}

	/**
	 * 解放処理
	 */
	@Override
	public void clear() {
		lclear();
	}

	private void lclear() {
		mAdvertiseRatio = null;
		mSkipRemaining = null;
		mOutputPath = null;
		mFileStatus = IMCMusicItemInfo.MCDB_STATUS_DEFAULT;
		mEncMethod = null;
		mEncKey = null;
		mEncIv = null;
		mApiSig = null;
		mRegTime = null;
		if (mTrackItem != null)
			mTrackItem.clear();
		mTrackItem = null;
		mPlayDuration = null;
		mPlayableTracks = null;
		mChannelName = null;
		mChannelNameEn = null;
	}

	@Override
	public void setStringValue(int kind, String value) {
		switch (kind) {
		case IMCMusicItemInfo.MCDB_ITEM_ADVERTISE_RATIO:
			mAdvertiseRatio = value;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_SKIP_REMAINING:
			mSkipRemaining = value;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_OUTPUT_PATH:
			mOutputPath = value;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_ENC_METHOD:
			mEncMethod = value;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_ENC_KEY:
			mEncKey = value;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_ENC_IV:
			mEncIv = value;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_API_SIG:
			mApiSig = value;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_REG_TIME:
			mRegTime = value;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_PLAY_DURATION:
			mPlayDuration = value;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_PLAYABLE_TRACKS:
			mPlayableTracks = value;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_CHANNEL_NAME:
			mChannelName = value;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_CHANNEL_NAME_EN:
			mChannelNameEn = value;
			break;
		default:
			break;
		}
	}

	@Override
	public String getStringValue(int kind) {
		String value = null;
		switch (kind) {
		case IMCMusicItemInfo.MCDB_ITEM_ADVERTISE_RATIO:
			value = mAdvertiseRatio;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_SKIP_REMAINING:
			value = mSkipRemaining;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_OUTPUT_PATH:
			value = mOutputPath;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_ENC_METHOD:
			value = mEncMethod;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_ENC_KEY:
			value = mEncKey;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_ENC_IV:
			value = mEncIv;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_API_SIG:
			value = mApiSig;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_REG_TIME:
			value = mRegTime;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_PLAY_DURATION:
			value = mPlayDuration;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_PLAYABLE_TRACKS:
			value = mPlayableTracks;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_CHANNEL_NAME:
			value = mChannelName;
			break;
		case IMCMusicItemInfo.MCDB_ITEM_CHANNEL_NAME_EN:
			value = mChannelNameEn;
			break;
		default:
			break;
		}
		return value;
	}

	@Override
	public void setTrackItem(IMCItem item) {
		mTrackItem = item;
	}

	@Override
	public IMCItem getTrackItem() {
		return mTrackItem;
	}

	@Override
	public void setStatus(int status) {
		mFileStatus = status;
	}

	@Override
	public int getStatus() {
		return mFileStatus;
	}
}
