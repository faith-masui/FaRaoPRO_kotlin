package jp.faraopro.play.mclient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Musicサーバーからの情報(Channelアイテム)管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCCampaignItem implements IMCItem, Parcelable {
	private String mId;
	private String mName;
	private String mNameEn;
	private String mValidBegin;
	private String mValidEnd;

	/**
	 * コンストラクタ
	 */
	public MCCampaignItem() {
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
		mValidBegin = null;
		mValidEnd = null;
	}

	@Override
	public void setString(int kind, String value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_CAMPAIGN_ID:
			mId = value;
			break;
		case MCDefResult.MCR_KIND_CAMPAIGN_NAME:
			mName = value;
			break;
		case MCDefResult.MCR_KIND_CAMPAIGN_NAME_EN:
			mNameEn = value;
			break;
		case MCDefResult.MCR_KIND_VALID_BEGIN:
			mValidBegin = value;
			break;
		case MCDefResult.MCR_KIND_VALID_END:
			mValidEnd = value;
			break;
		default:
			break;
		}
	}

	@Override
	public String getString(int kind) {
		String value = null;
		switch (kind) {
		case MCDefResult.MCR_KIND_CAMPAIGN_ID:
			value = mId;
			break;
		case MCDefResult.MCR_KIND_CAMPAIGN_NAME:
			value = mName;
			break;
		case MCDefResult.MCR_KIND_CAMPAIGN_NAME_EN:
			value = mNameEn;
			break;
		case MCDefResult.MCR_KIND_VALID_BEGIN:
			value = mValidBegin;
			break;
		case MCDefResult.MCR_KIND_VALID_END:
			value = mValidEnd;
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
		dest.writeString(mValidBegin);
		dest.writeString(mValidEnd);
	}

	private MCCampaignItem(Parcel in) {
		mId = in.readString();
		mName = in.readString();
		mNameEn = in.readString();
		mValidBegin = in.readString();
		mValidEnd = in.readString();
	}

	public static final Parcelable.Creator<MCCampaignItem> CREATOR = new Parcelable.Creator<MCCampaignItem>() {
		@Override
		public MCCampaignItem createFromParcel(Parcel in) {
			return new MCCampaignItem(in);
		}

		@Override
		public MCCampaignItem[] newArray(int size) {
			return new MCCampaignItem[size];
		}
	};
}
