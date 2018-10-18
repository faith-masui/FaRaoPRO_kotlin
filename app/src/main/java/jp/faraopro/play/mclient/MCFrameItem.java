package jp.faraopro.play.mclient;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Musicサーバーからの情報(Channelアイテム)管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCFrameItem implements IMCItem, Parcelable {
	private String mPatternId;
	// private String mTargetDate;
	private String mFrameId;
	private String mOnairTime;
	private List<MCAudioItem> mAudios;

	/**
	 * コンストラクタ
	 */
	public MCFrameItem() {
		lclear();
	}

	// public MCScheduleItem(
	// String type, String id, String name, String nameEn) {
	// lclear();
	//
	// this.mType = type;
	// this.mId = id;
	// this.mName = name;
	// this.mNameEn = nameEn;
	// }

	// public MCScheduleItem(
	// String type, String id, String name, String nameEn, String digest, String
	// action, String rule) {
	// lclear();
	//
	// this.mType = type;
	// this.mId = id;
	// this.mName = name;
	// this.mNameEn = nameEn;
	// this.mDigest = digest;
	// this.mAction = action;
	// this.mRule = rule;
	// }

	@Override
	public void clear() {
		lclear();
	}

	private void lclear() {
		mPatternId = null;
		// mTargetDate = null;
		mFrameId = null;
		mOnairTime = null;
		if (mAudios != null)
			mAudios.clear();
		mAudios = new ArrayList<MCAudioItem>();
	}

	public String getmPatternId() {
		return mPatternId;
	}

	public void setmPatternId(String mPatternId) {
		this.mPatternId = mPatternId;
	}

	// public String getmTargetDate() {
	// return mTargetDate;
	// }
	//
	// public void setmTargetDate(String mTargetDate) {
	// this.mTargetDate = mTargetDate;
	// }

	@Override
	public void setString(int kind, String value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_FRAME_ID:
			mFrameId = value;
			break;
		case MCDefResult.MCR_KIND_ONAIR_TIME:
			mOnairTime = value;
			break;
		default:
			break;
		}
	}

	@Override
	public String getString(int kind) {
		String value = null;
		switch (kind) {
		case MCDefResult.MCR_KIND_FRAME_ID:
			value = mFrameId;
			break;
		case MCDefResult.MCR_KIND_ONAIR_TIME:
			value = mOnairTime;
			break;
		default:
			break;
		}
		return value;
	}

	public void setItem(int kind, IMCItem item) {
		switch (kind) {
		case MCDefResult.MCR_KIND_PATTERN_AUDIO:
			mAudios.add((MCAudioItem) item);
			break;
		default:
			break;
		}
	}

	public List<MCAudioItem> getItem(int kind) {
		List<MCAudioItem> itemList = null;
		switch (kind) {
		case MCDefResult.MCR_KIND_PATTERN_AUDIO:
			itemList = mAudios;
			break;
		default:
			break;
		}
		return itemList;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mPatternId);
		// dest.writeString(mTargetDate);
		dest.writeString(mFrameId);
		dest.writeString(mOnairTime);
		dest.writeTypedList(mAudios);
	}

	private MCFrameItem(Parcel in) {
		mPatternId = in.readString();
		// mTargetDate = in.readString();
		mFrameId = in.readString();
		mOnairTime = in.readString();
		mAudios = in.createTypedArrayList(MCAudioItem.CREATOR);
	}

	public static final Parcelable.Creator<MCFrameItem> CREATOR = new Parcelable.Creator<MCFrameItem>() {
		@Override
		public MCFrameItem createFromParcel(Parcel in) {
			return new MCFrameItem(in);
		}

		@Override
		public MCFrameItem[] newArray(int size) {
			return new MCFrameItem[size];
		}
	};
}
