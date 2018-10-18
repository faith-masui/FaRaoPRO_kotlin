package jp.faraopro.play.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Facebookユーザ情報格納クラス
 * 
 * @author AIM
 * 
 */
public class FacebookUserInfo implements Parcelable {

	public static final int FB_USER_INFO_UNKNOWN = 0;
	public static final int FB_USER_INFO_GENDER_MALE = 1;
	public static final int FB_USER_INFO_GENDER_FEMALE = 2;
	public static final String FB_USER_INFO_CONST_JP = "JP";
	public static final String FB_USER_INFO_CONST_MALE = "male";
	public static final String FB_USER_INFO_CONST_FEMALE = "female";

	private String mail;
	private String pass;
	private int gender;
	private int birthYear;
	private String location;
	private String province;
	private int region;
	private String country = FB_USER_INFO_CONST_JP;

	/**
	 * コンストラクタ<br>
	 * 性別=不明、誕生年=-1で設定される
	 */
	public FacebookUserInfo() {
		gender = FB_USER_INFO_UNKNOWN;
		birthYear = -1;
	}

	/**
	 * メールアドレスを返す
	 * 
	 * @return メールアドレス
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * メールアドレスを設定する
	 * 
	 * @param mail
	 *            メールアドレス
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * パスワードを返す
	 * 
	 * @return パスワード
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * パスワードを設定する
	 * 
	 * @param pass
	 *            パスワード
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}

	/**
	 * 性別を返す
	 * 
	 * @return 0:不明、1:男性、2:女性
	 */
	public int getGender() {
		return gender;
	}

	/**
	 * 性別を設定する
	 * 
	 * @param gender
	 *            0:不明、1:男性、2:女性
	 */
	public void setGender(int gender) {
		this.gender = gender;
	}

	/**
	 * 性別を設定する
	 * 
	 * @param gender
	 *            male:男性、female:女性、その他文字列:不明
	 */
	public void setGender(String gender) {
		if (FB_USER_INFO_CONST_MALE.equals(gender)) {
			this.gender = FB_USER_INFO_GENDER_MALE;
		} else if (FB_USER_INFO_CONST_FEMALE.equals(gender)) {
			this.gender = FB_USER_INFO_GENDER_FEMALE;
		} else {
			this.gender = FB_USER_INFO_UNKNOWN;
		}
	}

	/**
	 * 誕生年を返す
	 * 
	 * @return 誕生年
	 */
	public int getBirthYear() {
		return birthYear;
	}

	/**
	 * 誕生年を設定する
	 * 
	 * @param birthYear
	 *            誕生年
	 */
	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}

	/**
	 * 出身国を返す
	 * 
	 * @return JP:日本
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * 出身国を設定する
	 * 
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public int getRegion() {
		return region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mail);
		dest.writeString(pass);
		dest.writeInt(gender);
		dest.writeInt(birthYear);
		dest.writeString(location);
		dest.writeString(province);
		dest.writeInt(region);
		dest.writeString(country);
	}

	private FacebookUserInfo(Parcel in) {
		mail = in.readString();
		pass = in.readString();
		gender = in.readInt();
		birthYear = in.readInt();
		location = in.readString();
		province = in.readString();
		region = in.readInt();
		country = in.readString();
	}

	public static final Parcelable.Creator<FacebookUserInfo> CREATOR = new Parcelable.Creator<FacebookUserInfo>() {
		@Override
		public FacebookUserInfo createFromParcel(Parcel in) {
			return new FacebookUserInfo(in);
		}

		@Override
		public FacebookUserInfo[] newArray(int size) {
			return new FacebookUserInfo[size];
		}
	};

}
