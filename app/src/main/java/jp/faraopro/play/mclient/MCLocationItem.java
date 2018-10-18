package jp.faraopro.play.mclient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Facebookユーザ情報格納クラス
 * 
 * @author Aim
 * 
 */
public class MCLocationItem implements Parcelable, IMCItem {
	public static final String LOCATION_INFO_CONST_JP = "JP";

	private String code;
	private String name;
	private String nameEn;
	private String country = LOCATION_INFO_CONST_JP;
	private String region;

	public MCLocationItem() {
	}

	@Override
	public void setString(int kind, String value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_LOCATION_CODE:
			code = value;
			break;
		case MCDefResult.MCR_KIND_LOCATION_NAME:
			name = value;
			break;
		case MCDefResult.MCR_KIND_LOCATION_NAME_EN:
			nameEn = value;
			break;
		case MCDefResult.MCR_KIND_LOCATION_COUNTRY:
			country = value;
			break;
		case MCDefResult.MCR_KIND_LOCATION_REGION:
			region = value;
			break;
		default:
			break;
		}
	}

	@Override
	public String getString(int kind) {
		String value = null;
		switch (kind) {
		case MCDefResult.MCR_KIND_LOCATION_CODE:
			value = code;
			break;
		case MCDefResult.MCR_KIND_LOCATION_NAME:
			value = name;
			break;
		case MCDefResult.MCR_KIND_LOCATION_NAME_EN:
			value = nameEn;
			break;
		case MCDefResult.MCR_KIND_LOCATION_COUNTRY:
			value = country;
			break;
		case MCDefResult.MCR_KIND_LOCATION_REGION:
			value = region;
			break;
		default:
			break;
		}
		return value;
	}

	@Override
	public void clear() {
		code = null;
		name = null;
		nameEn = null;
		country = null;
		region = null;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(code);
		dest.writeString(name);
		dest.writeString(nameEn);
		dest.writeString(country);
		dest.writeString(region);
	}

	private MCLocationItem(Parcel in) {
		code = in.readString();
		name = in.readString();
		nameEn = in.readString();
		country = in.readString();
		region = in.readString();
	}

	public static final Parcelable.Creator<MCLocationItem> CREATOR = new Parcelable.Creator<MCLocationItem>() {
		@Override
		public MCLocationItem createFromParcel(Parcel in) {
			return new MCLocationItem(in);
		}

		@Override
		public MCLocationItem[] newArray(int size) {
			return new MCLocationItem[size];
		}
	};

}
