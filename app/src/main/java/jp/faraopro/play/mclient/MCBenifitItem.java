package jp.faraopro.play.mclient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Musicサーバーからの情報(Channelアイテム)管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCBenifitItem implements IMCItem, Parcelable {
	private String mId;
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
	public String mMode;
	private String mThumbnailSmall;

	/**
	 * コンストラクタ
	 */
	public MCBenifitItem() {
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
		mDescription = null;
		mDescriptionEn = null;
		mContentTitle = null;
		mContentTitleEn = null;
		mContentText = null;
		mContentTextEn = null;
		mThumbIcon = null;
		mDeliveryBegin = null;
		mDeliveryEnd = null;
		mSkipControl = null;
		mAdControl = null;
		mSeriesNo = null;
		mSeriesContentNo = null;
		mMode = null;
		mThumbnailSmall = null;
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
		case MCDefResult.MCR_KIND_ITEM_DESCRIPTION:
			mDescription = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_DESCRIPTION_EN:
			mDescriptionEn = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_CONTENT_TITLE:
			mContentTitle = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_CONTENT_TITLE_EN:
			mContentTitleEn = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_CONTENT_TEXT:
			mContentText = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_CONTENT_TEXT_EN:
			mContentTextEn = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_THUMB_ICON:
			mThumbIcon = value;
			break;
		case MCDefResult.MCR_KIND_BENIFITITEM_DELIVERY_BEGIN:
			mDeliveryBegin = value;
			break;
		case MCDefResult.MCR_KIND_BENIFITITEM_DELIVERY_END:
			mDeliveryEnd = value;
			break;
		case MCDefResult.MCR_KIND_BENIFITITEM_SKIP_CONTROL:
			mSkipControl = value;
			break;
		case MCDefResult.MCR_KIND_BENIFITITEM_AD_CONTROL:
			mAdControl = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_SERIES_NO:
			mSeriesNo = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_SERIES_CONTENT_NO:
			mSeriesContentNo = value;
			break;
		case MCDefResult.MCR_KIND_THUMBNAIL_ICON_SMALL:
			mThumbnailSmall = value;
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
		case MCDefResult.MCR_KIND_ITEM_DESCRIPTION:
			value = mDescription;
			break;
		case MCDefResult.MCR_KIND_ITEM_DESCRIPTION_EN:
			value = mDescriptionEn;
			break;
		case MCDefResult.MCR_KIND_ITEM_CONTENT_TITLE:
			value = mContentTitle;
			break;
		case MCDefResult.MCR_KIND_ITEM_CONTENT_TITLE_EN:
			value = mContentTitleEn;
			break;
		case MCDefResult.MCR_KIND_ITEM_CONTENT_TEXT:
			value = mContentText;
			break;
		case MCDefResult.MCR_KIND_ITEM_CONTENT_TEXT_EN:
			value = mContentTextEn;
			break;
		case MCDefResult.MCR_KIND_ITEM_THUMB_ICON:
			value = mThumbIcon;
			break;
		case MCDefResult.MCR_KIND_BENIFITITEM_DELIVERY_BEGIN:
			value = mDeliveryBegin;
			break;
		case MCDefResult.MCR_KIND_BENIFITITEM_DELIVERY_END:
			value = mDeliveryEnd;
			break;
		case MCDefResult.MCR_KIND_BENIFITITEM_SKIP_CONTROL:
			value = mSkipControl;
			break;
		case MCDefResult.MCR_KIND_BENIFITITEM_AD_CONTROL:
			value = mAdControl;
			break;
		case MCDefResult.MCR_KIND_ITEM_SERIES_NO:
			value = mSeriesNo;
			break;
		case MCDefResult.MCR_KIND_ITEM_SERIES_CONTENT_NO:
			value = mSeriesContentNo;
			break;
		case MCDefResult.MCR_KIND_THUMBNAIL_ICON_SMALL:
			value = mThumbnailSmall;
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
		dest.writeString(mDescription);
		dest.writeString(mDescriptionEn);
		dest.writeString(mContentTitle);
		dest.writeString(mContentTitleEn);
		dest.writeString(mContentText);
		dest.writeString(mContentTextEn);
		dest.writeString(mThumbIcon);
		dest.writeString(mDeliveryBegin);
		dest.writeString(mDeliveryEnd);
		dest.writeString(mSkipControl);
		dest.writeString(mAdControl);
		dest.writeString(mSeriesNo);
		dest.writeString(mSeriesContentNo);
		dest.writeString(mMode);
		dest.writeString(mThumbnailSmall);
	}

	private MCBenifitItem(Parcel in) {
		mId = in.readString();
		mName = in.readString();
		mNameEn = in.readString();
		mDescription = in.readString();
		mDescriptionEn = in.readString();
		mContentTitle = in.readString();
		mContentTitleEn = in.readString();
		mContentText = in.readString();
		mContentTextEn = in.readString();
		mThumbIcon = in.readString();
		mDeliveryBegin = in.readString();
		mDeliveryEnd = in.readString();
		mSkipControl = in.readString();
		mAdControl = in.readString();
		mSeriesNo = in.readString();
		mSeriesContentNo = in.readString();
		mMode = in.readString();
		mThumbnailSmall = in.readString();
	}

	public static final Parcelable.Creator<MCBenifitItem> CREATOR = new Parcelable.Creator<MCBenifitItem>() {
		@Override
		public MCBenifitItem createFromParcel(Parcel in) {
			return new MCBenifitItem(in);
		}

		@Override
		public MCBenifitItem[] newArray(int size) {
			return new MCBenifitItem[size];
		}
	};
}
