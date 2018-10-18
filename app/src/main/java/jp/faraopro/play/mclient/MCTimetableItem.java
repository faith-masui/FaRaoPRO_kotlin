package jp.faraopro.play.mclient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Musicサーバーからの情報(Channelアイテム)管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCTimetableItem implements IMCItem, Parcelable {
	private String mId;
	private String mName;
	private String mNameEn;
	private String mTimerType;
	private String mTimerRule;
	private String mSourceType;
	private String mSourceName;
	private String mSourceUrl;

	/**
	 * コンストラクタ
	 */
	public MCTimetableItem() {
		lclear();
	}

	@Override
	public void clear() {
		lclear();
	}

	private void lclear() {
		mId = null;
		mName = null;
		mNameEn = null;
		mTimerType = null;
		mTimerRule = null;
		mSourceType = null;
		mSourceName = null;
		mSourceUrl = null;
	}

	@Override
	public void setString(int kind, String value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_CHANNELITEM_ID:
			mId = value;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME:
			mName = value;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN:
			mNameEn = value;
			break;
		case MCDefResult.MCR_KIND_TIMER_TYPE:
			mTimerType = value;
			break;
		case MCDefResult.MCR_KIND_TIMER_RULE:
			mTimerRule = value;
			break;
		case MCDefResult.MCR_KIND_SOURCE_TYPE:
			mSourceType = value;
			break;
		case MCDefResult.MCR_KIND_SOURCE_NAME:
			mSourceName = value;
			break;
		case MCDefResult.MCR_KIND_SOURCE_URL:
			mSourceUrl = value;
			break;
		default:
			break;
		}
	}

	@Override
	public String getString(int kind) {
		String value = null;
		switch (kind) {
		case MCDefResult.MCR_KIND_CHANNELITEM_ID:
			value = mId;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME:
			value = mName;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN:
			value = mNameEn;
			break;
		case MCDefResult.MCR_KIND_TIMER_TYPE:
			value = mTimerType;
			break;
		case MCDefResult.MCR_KIND_TIMER_RULE:
			value = mTimerRule;
			break;
		case MCDefResult.MCR_KIND_SOURCE_TYPE:
			value = mSourceType;
			break;
		case MCDefResult.MCR_KIND_SOURCE_NAME:
			value = mSourceName;
			break;
		case MCDefResult.MCR_KIND_SOURCE_URL:
			value = mSourceUrl;
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
		dest.writeString(mId);
		dest.writeString(mName);
		dest.writeString(mNameEn);
		dest.writeString(mTimerType);
		dest.writeString(mTimerRule);
		dest.writeString(mSourceType);
		dest.writeString(mSourceName);
		dest.writeString(mSourceUrl);
	}

	private MCTimetableItem(Parcel in) {
		mId = in.readString();
		mName = in.readString();
		mNameEn = in.readString();
		mTimerType = in.readString();
		mTimerRule = in.readString();
		mSourceType = in.readString();
		mSourceName = in.readString();
		mSourceUrl = in.readString();
	}

	public static final Parcelable.Creator<MCTimetableItem> CREATOR = new Parcelable.Creator<MCTimetableItem>() {
		@Override
		public MCTimetableItem createFromParcel(Parcel in) {
			return new MCTimetableItem(in);
		}

		@Override
		public MCTimetableItem[] newArray(int size) {
			return new MCTimetableItem[size];
		}
	};
}
