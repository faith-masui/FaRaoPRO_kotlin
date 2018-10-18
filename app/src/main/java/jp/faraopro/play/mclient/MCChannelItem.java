package jp.faraopro.play.mclient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Musicサーバーからの情報(Channelアイテム)管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCChannelItem implements IMCItem, Parcelable {
	private String mId;
	private String mLock;
	private String mName;
	private String mName_en;
	private String mTimestamp;

	/**
	 * コンストラクタ
	 */
	public MCChannelItem() {
		lclear();
	}

	@Override
	public void clear() {
		lclear();
	}

	private void lclear() {
		mId = null;
		mLock = null;
		mName = null;
		mName_en = null;
		mTimestamp = null;
	}

	@Override
	public void setString(int kind, String value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_CHANNELITEM_ID:
			mId = value;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_LOCK:
			mLock = value;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME:
			mName = value;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN:
			mName_en = value;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_TIMESTAMP:
			mTimestamp = value;
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
		case MCDefResult.MCR_KIND_CHANNELITEM_LOCK:
			value = mLock;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME:
			value = mName;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN:
			value = mName_en;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_TIMESTAMP:
			value = mTimestamp;
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
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(mId);
		arg0.writeString(mLock);
		arg0.writeString(mName);
		arg0.writeString(mName_en);
		arg0.writeString(mTimestamp);
	}

	private MCChannelItem(Parcel in) {
		mId = in.readString();
		mLock = in.readString();
		mName = in.readString();
		mName_en = in.readString();
		mTimestamp = in.readString();
	}

	public static final Parcelable.Creator<MCChannelItem> CREATOR = new Parcelable.Creator<MCChannelItem>() {
		@Override
		public MCChannelItem createFromParcel(Parcel in) {
			return new MCChannelItem(in);
		}

		@Override
		public MCChannelItem[] newArray(int size) {
			return new MCChannelItem[size];
		}
	};
}
