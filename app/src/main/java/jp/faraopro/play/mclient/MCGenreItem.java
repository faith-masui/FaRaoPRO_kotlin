package jp.faraopro.play.mclient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Musicサーバーからの情報(GenreChannelアイテム)管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCGenreItem implements IMCItem, Parcelable {
	private String mId;
	private String mName;
	private String mName_en;

	/**
	 * コンストラクタ
	 */
	public MCGenreItem() {
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
	}

	@Override
	public void setString(int kind, String value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_GENREITEM_ID:
			mId = value;
			break;
		case MCDefResult.MCR_KIND_GENREITEM_NAME:
			mName = value;
			break;
		case MCDefResult.MCR_KIND_GENREITEM_NAME_EN:
			mName_en = value;
			break;
		default:
			break;
		}
	}

	@Override
	public String getString(int kind) {
		String value = null;
		switch (kind) {
		case MCDefResult.MCR_KIND_GENREITEM_ID:
			value = mId;
			break;
		case MCDefResult.MCR_KIND_GENREITEM_NAME:
			value = mName;
			break;
		case MCDefResult.MCR_KIND_GENREITEM_NAME_EN:
			value = mName_en;
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
		dest.writeString(mName_en);
	}

	private MCGenreItem(Parcel in) {
		mId = in.readString();
		mName = in.readString();
		mName_en = in.readString();
	}

	public static final Parcelable.Creator<MCGenreItem> CREATOR = new Parcelable.Creator<MCGenreItem>() {
		@Override
		public MCGenreItem createFromParcel(Parcel in) {
			return new MCGenreItem(in);
		}

		@Override
		public MCGenreItem[] newArray(int size) {
			return new MCGenreItem[size];
		}
	};
}
