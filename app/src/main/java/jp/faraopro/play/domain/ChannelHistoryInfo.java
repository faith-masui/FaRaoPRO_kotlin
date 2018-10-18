package jp.faraopro.play.domain;

/**
 * 楽曲情報格納クラス
 * 
 * @author Aim
 * 
 */
public class ChannelHistoryInfo {
	private String mName;
	private String mNameEn;
	private String mMode;
	private String mChannelId;
	private String mRange;
	private long mTimestamp;

	public ChannelHistoryInfo() {
	}

	public ChannelHistoryInfo(String name, String nameEn, String mode, String channelId, String range, long timestamp) {
		this.mName = name;
		this.mNameEn = nameEn;
		this.mMode = mode;
		this.mChannelId = channelId;
		this.mRange = range;
		this.mTimestamp = timestamp;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getNameEn() {
		return mNameEn;
	}

	public void setNameEn(String nameEn) {
		mNameEn = nameEn;
	}

	public String getMode() {
		return mMode;
	}

	public void setMode(String mode) {
		mMode = mode;
	}

	public String getChannelId() {
		return mChannelId;
	}

	public void setChannelId(String channelId) {
		mChannelId = channelId;
	}

	public String getRange() {
		return mRange;
	}

	public void setRange(String range) {
		mRange = range;
	}

	public long getTimestamp() {
		return mTimestamp;
	}

	public void setTimestamp(long timestamp) {
		mTimestamp = timestamp;
	}

	// @Override
	// public int describeContents() {
	// return 0;
	// }
	//
	// @Override
	// public void writeToParcel(Parcel dest, int flags) {
	// dest.writeInt(playerType);
	// dest.writeString(artist);
	// dest.writeString(title);
	// dest.writeString(relese);
	// dest.writeString(genre);
	// dest.writeString(info);
	// dest.writeString(url);
	// dest.writeString(artworkPath);
	// dest.writeString(thumbPath);
	// dest.writeString(urlSearch);
	// dest.writeString(id);
	// dest.writeInt(length);
	// dest.writeInt(currentPos);
	// dest.writeString(artistId);
	// dest.writeString(channelName);
	//
	// dest.writeString(name);
	// dest.writeString(nameEn);
	// dest.writeString(titleEn);
	// dest.writeString(description);
	// dest.writeString(descriptionEn);
	// dest.writeString(previewLink);
	// dest.writeString(externalLink1);
	// dest.writeString(stationName);
	// dest.writeString(stationNameEn);
	// }
	//
	// public void readFromParcel(Parcel in) {
	// playerType = in.readInt();
	// artist = in.readString();
	// title = in.readString();
	// relese = in.readString();
	// genre = in.readString();
	// info = in.readString();
	// url = in.readString();
	// artworkPath = in.readString();
	// thumbPath = in.readString();
	// urlSearch = in.readString();
	// id = in.readString();
	// length = in.readInt();
	// currentPos = in.readInt();
	// artistId = in.readString();
	// channelName = in.readString();
	//
	// name = in.readString();
	// nameEn = in.readString();
	// titleEn = in.readString();
	// description = in.readString();
	// descriptionEn = in.readString();
	// previewLink = in.readString();
	// externalLink1 = in.readString();
	// stationName = in.readString();
	// stationNameEn = in.readString();
	// }
	//
	// public static final Parcelable.Creator<ChannelHistoryInfo> CREATOR = new
	// Parcelable.Creator<ChannelHistoryInfo>()
	// {
	// @Override
	// public ChannelHistoryInfo createFromParcel(Parcel in) {
	// return new ChannelHistoryInfo(in);
	// }
	//
	// @Override
	// public ChannelHistoryInfo[] newArray(int size) {
	// return new ChannelHistoryInfo[size];
	// }
	// };
	//
	// private ChannelHistoryInfo(
	// Parcel in) {
	// this();
	// playerType = in.readInt();
	// artist = in.readString();
	// title = in.readString();
	// relese = in.readString();
	// genre = in.readString();
	// info = in.readString();
	// url = in.readString();
	// artworkPath = in.readString();
	// thumbPath = in.readString();
	// urlSearch = in.readString();
	// id = in.readString();
	// length = in.readInt();
	// currentPos = in.readInt();
	// artistId = in.readString();
	// channelName = in.readString();
	//
	// name = in.readString();
	// nameEn = in.readString();
	// titleEn = in.readString();
	// description = in.readString();
	// descriptionEn = in.readString();
	// previewLink = in.readString();
	// externalLink1 = in.readString();
	// stationName = in.readString();
	// stationNameEn = in.readString();
	// }

}
