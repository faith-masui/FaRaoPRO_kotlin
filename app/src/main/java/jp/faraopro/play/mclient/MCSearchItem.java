package jp.faraopro.play.mclient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Musicサーバーからの情報(検索結果)管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCSearchItem implements IMCItem, Parcelable {
	private String mId;
	private String mName;
	private String mName_en;
	private String mThumbIcon;
	private String mThumbnailSmall;

	/**
	 * コンストラクタ
	 */
	public MCSearchItem() {
		lclear();
	}

	@Override
	public void clear() {
		lclear();
	}

	private void lclear() {
		mId = null;
		mName = null;
		mName_en = null;
		mThumbIcon = null;
		mThumbnailSmall = null;
	}

	@Override
	public void setString(int kind, String value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_SEARCHITEM_ID:
			mId = value;
			break;
		case MCDefResult.MCR_KIND_SEARCHITEM_NAME:
			mName = value;
			break;
		case MCDefResult.MCR_KIND_SEARCHITEM_NAME_EN:
			mName_en = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_THUMB_ICON:
			mThumbIcon = value;
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
		case MCDefResult.MCR_KIND_SEARCHITEM_ID:
			value = mId;
			break;
		case MCDefResult.MCR_KIND_SEARCHITEM_NAME:
			value = mName;
			break;
		case MCDefResult.MCR_KIND_SEARCHITEM_NAME_EN:
			value = mName_en;
			break;
		case MCDefResult.MCR_KIND_ITEM_THUMB_ICON:
			value = mThumbIcon;
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
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(mId);
		arg0.writeString(mName);
		arg0.writeString(mName_en);
		arg0.writeString(mThumbIcon);
		arg0.writeString(mThumbnailSmall);
	}

	private MCSearchItem(Parcel in) {
		mId = in.readString();
		mName = in.readString();
		mName_en = in.readString();
		mThumbIcon = in.readString();
		mThumbnailSmall = in.readString();
	}

	public static final Parcelable.Creator<MCSearchItem> CREATOR = new Parcelable.Creator<MCSearchItem>() {
		@Override
		public MCSearchItem createFromParcel(Parcel in) {
			return new MCSearchItem(in);
		}

		@Override
		public MCSearchItem[] newArray(int size) {
			return new MCSearchItem[size];
		}
	};
}
