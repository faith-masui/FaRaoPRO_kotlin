package jp.faraopro.play.mclient;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import jp.faraopro.play.R;
import jp.faraopro.play.common.Consts;

/**
 * Musicサーバーからの情報(Channelアイテム)管理クラス
 *
 * @author AIM Corporation
 *
 */
public class MCScheduleItem implements IMCItem, Parcelable {
	private String mRule;
	private String mPeriod;
	private String mPatternId;
	private String mPatternDigest;
	private String mTargetDate;
	private String mLastUpdate;
	private String mUpdateStatus;
	private String mScheduleMask;

	/**
	 * コンストラクタ
	 */
	public MCScheduleItem() {
		lclear();
	}

	@Override
	public void clear() {
		lclear();
	}

	private void lclear() {
		mRule = null;
		mPeriod = null;
		mPatternId = null;
		mPatternDigest = null;
		mTargetDate = null;
		mLastUpdate = null;
		mUpdateStatus = null;
		mScheduleMask = null;
	}

	@Override
	public MCScheduleItem clone() {
		MCScheduleItem item = new MCScheduleItem();
		item.setString(MCDefResult.MCR_KIND_SCHEDULE_RULE, this.mRule);
		item.setString(MCDefResult.MCR_KIND_SCHEDULE_PERIOD, this.mPeriod);
		item.setString(MCDefResult.MCR_KIND_PATTERN_ID, this.mPatternId);
		item.setString(MCDefResult.MCR_KIND_PATTERN_DIGEST, this.mPatternDigest);
		item.setTargetDate(mTargetDate);
		item.setString(MCDefResult.MCR_KIND_SCHEDULE_MASK, this.mScheduleMask);

		return item;
	}

	public String getTargetDate() {
		return mTargetDate;
	}

	public void setTargetDate(String mTargetDate) {
		this.mTargetDate = mTargetDate;
	}

	public String getLastUpdate() {
		return mLastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		mLastUpdate = lastUpdate;
	}

	public String getUpdateStatus() {
		return mUpdateStatus;
	}

	public String getUpdateStatusText(Context context) {
		if (TextUtils.isEmpty(mUpdateStatus))
			mUpdateStatus = "";
		else if (mUpdateStatus.equals(Consts.PATTERN_UPDATE_STATUS_NG))
			mUpdateStatus = context.getString(R.string.cap_failed);
		else
			mUpdateStatus = context.getString(R.string.cap_success);
		return mUpdateStatus;
	}

	public void setUpdateStatus(String updateStatus) {
		mUpdateStatus = updateStatus;
	}

	@Override
	public void setString(int kind, String value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_SCHEDULE_RULE:
			mRule = value;
			break;
		case MCDefResult.MCR_KIND_SCHEDULE_PERIOD:
			mPeriod = value;
			break;
		case MCDefResult.MCR_KIND_PATTERN_ID:
			mPatternId = value;
			break;
		case MCDefResult.MCR_KIND_PATTERN_DIGEST:
			mPatternDigest = value;
			break;
		case MCDefResult.MCR_KIND_SCHEDULE_MASK:
			mScheduleMask = value;
			break;
		default:
			break;
		}
	}

	@Override
	public String getString(int kind) {
		String value = null;
		switch (kind) {
		case MCDefResult.MCR_KIND_SCHEDULE_RULE:
			value = mRule;
			break;
		case MCDefResult.MCR_KIND_SCHEDULE_PERIOD:
			value = mPeriod;
			break;
		case MCDefResult.MCR_KIND_PATTERN_ID:
			value = mPatternId;
			break;
		case MCDefResult.MCR_KIND_PATTERN_DIGEST:
			value = mPatternDigest;
			break;
		case MCDefResult.MCR_KIND_SCHEDULE_MASK:
			value = mScheduleMask;
			break;
		default:
			break;
		}
		return value;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mRule);
		dest.writeString(mPeriod);
		dest.writeString(mPatternId);
		dest.writeString(mPatternDigest);
		dest.writeString(mTargetDate);
		dest.writeString(mLastUpdate);
		dest.writeString(mUpdateStatus);
		dest.writeString(mScheduleMask);
	}

	private MCScheduleItem(Parcel in) {
		mRule = in.readString();
		mPeriod = in.readString();
		mPatternId = in.readString();
		mPatternDigest = in.readString();
		mTargetDate = in.readString();
		mLastUpdate = in.readString();
		mUpdateStatus = in.readString();
		mScheduleMask = in.readString();
	}

	public static final Parcelable.Creator<MCScheduleItem> CREATOR = new Parcelable.Creator<MCScheduleItem>() {
		@Override
		public MCScheduleItem createFromParcel(Parcel in) {
			return new MCScheduleItem(in);
		}

		@Override
		public MCScheduleItem[] newArray(int size) {
			return new MCScheduleItem[size];
		}
	};
}
