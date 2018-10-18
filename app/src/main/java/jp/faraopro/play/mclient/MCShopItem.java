package jp.faraopro.play.mclient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 店舗情報格納クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCShopItem implements IMCItem, Parcelable {
	private String trackingKey;
	private String latitude;
	private String longitude;
	private String distance;
	private String industryCode;
	private String industryName;
	private String industryName_en;
	private String shopName;
	private String shopName_en;
	private String zipCode;
	private String provinceCode;
	private String provinceName;
	private String provinceName_en;
	private String location;
	private String location_en;
	private String contact1;
	private String contact2;
	private String externalLink1;
	private String externalLink2;
	private String externalLink3;
	private String externalLink4;
	private String externalLink5;

	/**
	 * コンストラクタ
	 */
	public MCShopItem() {
		lclear();
	}

	@Override
	public void clear() {
		lclear();
	}

	private void lclear() {
		trackingKey = null;
		latitude = null;
		longitude = null;
		distance = null;
		industryCode = null;
		industryName = null;
		industryName_en = null;
		shopName = null;
		shopName_en = null;
		zipCode = null;
		provinceCode = null;
		provinceName = null;
		provinceName_en = null;
		location = null;
		location_en = null;
		contact1 = null;
		contact2 = null;
		externalLink1 = null;
		externalLink2 = null;
		externalLink3 = null;
		externalLink4 = null;
		externalLink5 = null;
	}

	@Override
	public void setString(int kind, String value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_TRACKING_KEY:
			trackingKey = value;
			break;
		case MCDefResult.MCR_KIND_LATITUDE:
			latitude = value;
			break;
		case MCDefResult.MCR_KIND_LONGITUDE:
			longitude = value;
			break;
		case MCDefResult.MCR_KIND_DISTANCE:
			distance = value;
			break;
		case MCDefResult.MCR_KIND_INDUSTRY_CODE:
			industryCode = value;
			break;
		case MCDefResult.MCR_KIND_INDUSTRY_NAME:
			industryName = value;
			break;
		case MCDefResult.MCR_KIND_INDUSTRY_NAME_EN:
			industryName_en = value;
			break;
		case MCDefResult.MCR_KIND_SHOP_NAME:
			shopName = value;
			break;
		case MCDefResult.MCR_KIND_SHOP_NAME_EN:
			shopName_en = value;
			break;
		case MCDefResult.MCR_KIND_ZIP_CODE:
			zipCode = value;
			break;
		case MCDefResult.MCR_KIND_PROVINCE_CODE:
			provinceCode = value;
			break;
		case MCDefResult.MCR_KIND_PROVINCE_NAME:
			provinceName = value;
			break;
		case MCDefResult.MCR_KIND_PROVINCE_NAME_EN:
			provinceName_en = value;
			break;
		case MCDefResult.MCR_KIND_LOCATION_LIST:
			location = value;
			break;
		case MCDefResult.MCR_KIND_LOCATION_EN:
			location_en = value;
			break;
		case MCDefResult.MCR_KIND_CONTACT1:
			contact1 = value;
			break;
		case MCDefResult.MCR_KIND_CONTACT2:
			contact2 = value;
			break;
		case MCDefResult.MCR_KIND_EXTERNAL_LINK1:
			externalLink1 = value;
			break;
		case MCDefResult.MCR_KIND_EXTERNAL_LINK2:
			externalLink2 = value;
			break;
		case MCDefResult.MCR_KIND_EXTERNAL_LINK3:
			externalLink3 = value;
			break;
		case MCDefResult.MCR_KIND_EXTERNAL_LINK4:
			externalLink4 = value;
			break;
		case MCDefResult.MCR_KIND_EXTERNAL_LINK5:
			externalLink5 = value;
			break;
		default:
			break;
		}
	}

	@Override
	public String getString(int kind) {
		String value = null;
		switch (kind) {
		case MCDefResult.MCR_KIND_TRACKING_KEY:
			value = trackingKey;
			break;
		case MCDefResult.MCR_KIND_LATITUDE:
			value = latitude;
			break;
		case MCDefResult.MCR_KIND_LONGITUDE:
			value = longitude;
			break;
		case MCDefResult.MCR_KIND_DISTANCE:
			value = distance;
			break;
		case MCDefResult.MCR_KIND_INDUSTRY_CODE:
			value = industryCode;
			break;
		case MCDefResult.MCR_KIND_INDUSTRY_NAME:
			value = industryName;
			break;
		case MCDefResult.MCR_KIND_INDUSTRY_NAME_EN:
			value = industryName_en;
			break;
		case MCDefResult.MCR_KIND_SHOP_NAME:
			value = shopName;
			break;
		case MCDefResult.MCR_KIND_SHOP_NAME_EN:
			value = shopName_en;
			break;
		case MCDefResult.MCR_KIND_ZIP_CODE:
			value = zipCode;
			break;
		case MCDefResult.MCR_KIND_PROVINCE_CODE:
			value = provinceCode;
			break;
		case MCDefResult.MCR_KIND_PROVINCE_NAME:
			value = provinceName;
			break;
		case MCDefResult.MCR_KIND_PROVINCE_NAME_EN:
			value = provinceName_en;
			break;
		case MCDefResult.MCR_KIND_LOCATION_LIST:
			value = location;
			break;
		case MCDefResult.MCR_KIND_LOCATION_EN:
			value = location_en;
			break;
		case MCDefResult.MCR_KIND_CONTACT1:
			value = contact1;
			break;
		case MCDefResult.MCR_KIND_CONTACT2:
			value = contact2;
			break;
		case MCDefResult.MCR_KIND_EXTERNAL_LINK1:
			value = externalLink1;
			break;
		case MCDefResult.MCR_KIND_EXTERNAL_LINK2:
			value = externalLink2;
			break;
		case MCDefResult.MCR_KIND_EXTERNAL_LINK3:
			value = externalLink3;
			break;
		case MCDefResult.MCR_KIND_EXTERNAL_LINK4:
			value = externalLink4;
			break;
		case MCDefResult.MCR_KIND_EXTERNAL_LINK5:
			value = externalLink5;
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
		dest.writeString(trackingKey);
		dest.writeString(latitude);
		dest.writeString(longitude);
		dest.writeString(distance);
		dest.writeString(industryCode);
		dest.writeString(industryName);
		dest.writeString(industryName_en);
		dest.writeString(shopName);
		dest.writeString(shopName_en);
		dest.writeString(zipCode);
		dest.writeString(provinceCode);
		dest.writeString(provinceName);
		dest.writeString(provinceName_en);
		dest.writeString(location);
		dest.writeString(location_en);
		dest.writeString(contact1);
		dest.writeString(contact2);
		dest.writeString(externalLink1);
		dest.writeString(externalLink2);
		dest.writeString(externalLink3);
		dest.writeString(externalLink4);
		dest.writeString(externalLink5);
	}

	private MCShopItem(Parcel in) {
		trackingKey = in.readString();
		latitude = in.readString();
		longitude = in.readString();
		distance = in.readString();
		industryCode = in.readString();
		industryName = in.readString();
		industryName_en = in.readString();
		shopName = in.readString();
		shopName_en = in.readString();
		zipCode = in.readString();
		provinceCode = in.readString();
		provinceName = in.readString();
		provinceName_en = in.readString();
		location = in.readString();
		location_en = in.readString();
		contact1 = in.readString();
		contact2 = in.readString();
		externalLink1 = in.readString();
		externalLink2 = in.readString();
		externalLink3 = in.readString();
		externalLink4 = in.readString();
		externalLink5 = in.readString();
	}

	public static final Parcelable.Creator<MCShopItem> CREATOR = new Parcelable.Creator<MCShopItem>() {
		@Override
		public MCShopItem createFromParcel(Parcel in) {
			return new MCShopItem(in);
		}

		@Override
		public MCShopItem[] newArray(int size) {
			return new MCShopItem[size];
		}
	};
}
