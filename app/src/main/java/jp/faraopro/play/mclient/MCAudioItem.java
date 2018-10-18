package jp.faraopro.play.mclient;

import android.os.Parcel;
import android.os.Parcelable;

import jp.faraopro.play.BuildConfig;
import jp.faraopro.play.common.FRODebug;

/**
 * Musicサーバーからの情報(Channelアイテム)管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCAudioItem implements IMCItem, Parcelable {
	private String mAudioId;
	private String mAudioVersion;
	private String mAudioTime;
	private String mAudioUrl;

    public void show() {
        if (!BuildConfig.DEBUG)
            return;

        FRODebug.logD(getClass(), "mAudioId " + mAudioId, true);
        FRODebug.logD(getClass(), "mAudioVersion " + mAudioVersion, true);
        FRODebug.logD(getClass(), "mAudioTime " + mAudioTime, true);
        FRODebug.logD(getClass(), "mAudioUrl " + mAudioUrl, true);
    }

    /**
     * コンストラクタ
     */
	public MCAudioItem() {
		lclear();
	}

	// public MCScheduleItem(
	// String type, String id, String name, String nameEn) {
	// lclear();
	//
	// this.mType = type;
	// this.mId = id;
	// this.mName = name;
	// this.mNameEn = nameEn;
	// }

	// public MCScheduleItem(
	// String type, String id, String name, String nameEn, String digest, String
	// action, String rule) {
	// lclear();
	//
	// this.mType = type;
	// this.mId = id;
	// this.mName = name;
	// this.mNameEn = nameEn;
	// this.mDigest = digest;
	// this.mAction = action;
	// this.mRule = rule;
	// }

	@Override
	public void clear() {
		lclear();
	}

	private void lclear() {
		mAudioId = null;
		mAudioVersion = null;
		mAudioTime = null;
		mAudioUrl = null;
	}

	@Override
	public MCAudioItem clone() {
		MCAudioItem item = new MCAudioItem();
		item.setString(MCDefResult.MCR_KIND_AUDIO_ID, this.mAudioId);
		item.setString(MCDefResult.MCR_KIND_AUDIO_VERSION, this.mAudioVersion);
		item.setString(MCDefResult.MCR_KIND_AUDIO_TIME, this.mAudioTime);
		item.setString(MCDefResult.MCR_KIND_AUDIO_URL, this.mAudioUrl);

		return item;
	}

	@Override
	public void setString(int kind, String value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_AUDIO_ID:
			mAudioId = value;
			break;
		case MCDefResult.MCR_KIND_AUDIO_VERSION:
			mAudioVersion = value;
			break;
		case MCDefResult.MCR_KIND_AUDIO_TIME:
			mAudioTime = value;
			break;
		case MCDefResult.MCR_KIND_AUDIO_URL:
			mAudioUrl = value;
			break;
		default:
			break;
		}
	}

	@Override
	public String getString(int kind) {
		String value = null;
		switch (kind) {
		case MCDefResult.MCR_KIND_AUDIO_ID:
			value = mAudioId;
			break;
		case MCDefResult.MCR_KIND_AUDIO_VERSION:
			value = mAudioVersion;
			break;
		case MCDefResult.MCR_KIND_AUDIO_TIME:
			value = mAudioTime;
			break;
		case MCDefResult.MCR_KIND_AUDIO_URL:
			value = mAudioUrl;
			break;
		default:
			break;
		}
		return value;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mAudioId);
		dest.writeString(mAudioVersion);
		dest.writeString(mAudioTime);
		dest.writeString(mAudioUrl);
	}

	private MCAudioItem(Parcel in) {
		mAudioId = in.readString();
		mAudioVersion = in.readString();
		mAudioTime = in.readString();
		mAudioUrl = in.readString();
	}

	public static final Parcelable.Creator<MCAudioItem> CREATOR = new Parcelable.Creator<MCAudioItem>() {
		@Override
		public MCAudioItem createFromParcel(Parcel in) {
			return new MCAudioItem(in);
		}

		@Override
		public MCAudioItem[] newArray(int size) {
			return new MCAudioItem[size];
		}
	};
}
