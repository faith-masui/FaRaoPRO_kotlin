package jp.faraopro.play.mclient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Musicサーバーからの情報(広告関連アイテム)管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCAdItem implements IMCItem, Parcelable {
	private String mId;
	private String mTitle;
	private String mTitleEn;
	private String mDescription;
	private String mDescriptionEn;
	private String mSponsor;
	private String mSponsorEn;
	private String mUrl;

	/**
	 * コンストラクタ
	 */
	public MCAdItem() {
		lclear();
	}

	@Override
	public void clear() {
		lclear();
	}

	private void lclear() {
		mId = null;
		mTitle = null;
		mTitleEn = null;
		mDescription = null;
		mDescriptionEn = null;
		mSponsor = null;
		mSponsorEn = null;
		mUrl = null;
	}

	@Override
	public void setString(int kind, String value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_ADITEM_ID:
			mId = value;
			break;
		case MCDefResult.MCR_KIND_ADITEM_TITLE:
			mTitle = value;
			break;
		case MCDefResult.MCR_KIND_ADITEM_TITLE_EN:
			mTitleEn = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_DESCRIPTION:
			mDescription = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_DESCRIPTION_EN:
			mDescriptionEn = value;
			break;
		case MCDefResult.MCR_KIND_ADITEM_SPONSOR:
			mSponsor = value;
			break;
		case MCDefResult.MCR_KIND_ADITEM_SPONSOR_EN:
			mSponsorEn = value;
			break;
		case MCDefResult.MCR_KIND_ADITEM_URL:
			mUrl = value;
			break;
		default:
			break;
		}
	}

	@Override
	public String getString(int kind) {
		String value = null;
		switch (kind) {
		case MCDefResult.MCR_KIND_ADITEM_ID:
			value = mId;
			break;
		case MCDefResult.MCR_KIND_ADITEM_TITLE:
			value = mTitle;
			break;
		case MCDefResult.MCR_KIND_ADITEM_TITLE_EN:
			value = mTitleEn;
			break;
		case MCDefResult.MCR_KIND_ITEM_DESCRIPTION:
			value = mDescription;
			break;
		case MCDefResult.MCR_KIND_ITEM_DESCRIPTION_EN:
			value = mDescriptionEn;
			break;
		case MCDefResult.MCR_KIND_ADITEM_SPONSOR:
			value = mSponsor;
			break;
		case MCDefResult.MCR_KIND_ADITEM_SPONSOR_EN:
			value = mSponsorEn;
			break;
		case MCDefResult.MCR_KIND_ADITEM_URL:
			value = mUrl;
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
		dest.writeString(mTitle);
		dest.writeString(mTitleEn);
		dest.writeString(mDescription);
		dest.writeString(mDescriptionEn);
		dest.writeString(mSponsor);
		dest.writeString(mSponsorEn);
		dest.writeString(mUrl);
	}

	private MCAdItem(Parcel in) {
		mId = in.readString();
		mTitle = in.readString();
		mTitleEn = in.readString();
		mDescription = in.readString();
		mDescriptionEn = in.readString();
		mSponsor = in.readString();
		mSponsorEn = in.readString();
		mUrl = in.readString();
	}

	public static final Parcelable.Creator<MCAdItem> CREATOR = new Parcelable.Creator<MCAdItem>() {
		@Override
		public MCAdItem createFromParcel(Parcel in) {
			return new MCAdItem(in);
		}

		@Override
		public MCAdItem[] newArray(int size) {
			return new MCAdItem[size];
		}
	};
}
