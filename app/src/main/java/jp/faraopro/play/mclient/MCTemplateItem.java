package jp.faraopro.play.mclient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Musicサーバーからの情報(Channelアイテム)管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCTemplateItem implements IMCItem, Parcelable {
	private String mType;
	private String mId;
	private String mName;
	private String mNameEn;
	private String mDigest;
	private String mAction;
	private String mRule;

	/**
	 * コンストラクタ
	 */
	public MCTemplateItem() {
		lclear();
	}

	public MCTemplateItem(String type, String id, String name, String nameEn) {
		lclear();

		this.mType = type;
		this.mId = id;
		this.mName = name;
		this.mNameEn = nameEn;
	}

	public MCTemplateItem(String type, String id, String name, String nameEn, String digest, String action,
			String rule) {
		lclear();

		this.mType = type;
		this.mId = id;
		this.mName = name;
		this.mNameEn = nameEn;
		this.mDigest = digest;
		this.mAction = action;
		this.mRule = rule;
	}

	@Override
	public void clear() {
		lclear();
	}

	private void lclear() {
		mType = null;
		mId = null;
		mName = null;
		mNameEn = null;
	}

	@Override
	public void setString(int kind, String value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_TEMPLATE_TYPE:
			mType = value;
			break;
		case MCDefResult.MCR_KIND_TEMPLATE_ID:
			mId = value;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME:
			mName = value;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN:
			mNameEn = value;
			break;
		case MCDefResult.MCR_KIND_TEMPLATE_DIGEST:
			mDigest = value;
			break;
		case MCDefResult.MCR_KIND_TEMPLATE_ACTION:
			mAction = value;
			break;
		case MCDefResult.MCR_KIND_TEMPLATE_RULE:
			mRule = value;
			break;
		default:
			break;
		}
	}

	@Override
	public String getString(int kind) {
		String value = null;
		switch (kind) {
		case MCDefResult.MCR_KIND_TEMPLATE_TYPE:
			value = mType;
			break;
		case MCDefResult.MCR_KIND_TEMPLATE_ID:
			value = mId;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME:
			value = mName;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN:
			value = mNameEn;
			break;
		case MCDefResult.MCR_KIND_TEMPLATE_DIGEST:
			value = mDigest;
			break;
		case MCDefResult.MCR_KIND_TEMPLATE_ACTION:
			value = mAction;
			break;
		case MCDefResult.MCR_KIND_TEMPLATE_RULE:
			value = mRule;
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
		dest.writeString(mType);
		dest.writeString(mId);
		dest.writeString(mName);
		dest.writeString(mNameEn);
		dest.writeString(mDigest);
		dest.writeString(mAction);
		dest.writeString(mRule);
	}

	private MCTemplateItem(Parcel in) {
		mType = in.readString();
		mId = in.readString();
		mName = in.readString();
		mNameEn = in.readString();
		mDigest = in.readString();
		mAction = in.readString();
		mRule = in.readString();
	}

	public static final Parcelable.Creator<MCTemplateItem> CREATOR = new Parcelable.Creator<MCTemplateItem>() {
		@Override
		public MCTemplateItem createFromParcel(Parcel in) {
			return new MCTemplateItem(in);
		}

		@Override
		public MCTemplateItem[] newArray(int size) {
			return new MCTemplateItem[size];
		}
	};
}
