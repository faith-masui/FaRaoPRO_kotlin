package jp.faraopro.play.domain;

/**
 * レーティング情報格納クラス
 * 
 * @author Aim
 * 
 */
public class RatingInfo { // implements Parcelable {
	private String trackingKey;
	private String mode;
	private String channelId;
	private String range;
	private String trackId;
	private String decision; // 評価
	private String complete; // 完走したか
	private String duration; // 視聴時間
	private String timestamp; // タイムスタンプ
	private String errorReason; // エラー原因

	public RatingInfo() {
	}

	public RatingInfo(String trackingKey, String mode, String channelId, String range, String trackId, String decision,
			String complete, String duration, String timestamp, String errorReason) {
		this.trackingKey = trackingKey;
		this.mode = mode;
		this.channelId = channelId;
		this.range = range;
		this.trackId = trackId;
		this.decision = decision;
		this.complete = complete;
		this.duration = duration;
		this.timestamp = timestamp;
		this.errorReason = errorReason;
	}

	public String getTrackingKey() {
		return trackingKey;
	}

	public void setTrackingKey(String key) {
		this.trackingKey = key;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public String getComplete() {
		return complete;
	}

	public void setComplete(String complete) {
		this.complete = complete;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getErrorReason() {
		return errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}

	// @Override
	// public int describeContents() {
	// return 0;
	// }
	//
	// @Override
	// public void writeToParcel(Parcel dest, int flags) {
	// dest.writeString(mode);
	// dest.writeString(channelId);
	// dest.writeString(range);
	// dest.writeString(trackId);
	// dest.writeString(decision);
	// dest.writeString(complete);
	// dest.writeString(duration);
	// }
	//
	// public void readFromParcel(Parcel in) {
	// mode = in.readString();
	// channelId = in.readString();
	// range = in.readString();
	// trackId = in.readString();
	// decision = in.readString();
	// complete = in.readString();
	// duration = in.readString();
	// }
	//
	// public static final Parcelable.Creator<RatingInfo> CREATOR = new
	// Parcelable.Creator<RatingInfo>() {
	// @Override
	// public RatingInfo createFromParcel(Parcel in) {
	// return new RatingInfo(in);
	// }
	//
	// @Override
	// public RatingInfo[] newArray(int size) {
	// return new RatingInfo[size];
	// }
	// };
	//
	// private RatingInfo(Parcel in) {
	// this();
	// mode = in.readString();
	// channelId = in.readString();
	// range = in.readString();
	// trackId = in.readString();
	// decision = in.readString();
	// complete = in.readString();
	// duration = in.readString();
	// }

}
