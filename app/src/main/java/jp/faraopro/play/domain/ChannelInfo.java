package jp.faraopro.play.domain;

import android.os.Parcel;
import android.os.Parcelable;
import jp.faraopro.play.common.Consts;

/**
 * 楽曲情報格納クラス
 * 
 * @author Aim
 * 
 */
public class ChannelInfo implements Parcelable {
	protected int id;
	protected String mode;
	protected String channelId;
	protected String range;
	protected String channelName;
	protected String source;
	protected int index;
	protected int type;
	protected int mSkipControl = Consts.SKIP_CONTROL_ENABLE;
	private int permisson;

	public ChannelInfo() {
	}

	public ChannelInfo(String mode, String channelId, String range, String channelName, String source, int type) {
		this.mode = mode;
		this.channelId = channelId;
		this.range = range;
		this.channelName = channelName;
		this.source = source;
		this.type = type;
	}

	public ChannelInfo(String mode, String channelId, String range, int permisson, String channelName, String source,
			int type, boolean skipControl) {
		this(mode, channelId, range, channelName, source, type);
		mSkipControl = skipControl ? Consts.SKIP_CONTROL_DISABLE : Consts.SKIP_CONTROL_ENABLE;
		this.permisson = permisson;
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

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSkipControl() {
		return mSkipControl;
	}

	public void setSkipControl(int skipControl) {
		this.mSkipControl = skipControl;
	}

	public int getPermisson() {
		return permisson;
	}

	public void setPermisson(int permisson) {
		this.permisson = permisson;
	}

	public static Parcelable.Creator<ChannelInfo> getCreator() {
		return CREATOR;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(mode);
		dest.writeString(channelId);
		dest.writeString(range);
		dest.writeString(channelName);
		dest.writeString(source);
		dest.writeInt(index);
		dest.writeInt(type);
		dest.writeInt(mSkipControl);
		dest.writeInt(permisson);
	}

	public void readFromParcel(Parcel in) {
		id = in.readInt();
		mode = in.readString();
		channelId = in.readString();
		range = in.readString();
		channelName = in.readString();
		source = in.readString();
		index = in.readInt();
		type = in.readInt();
		mSkipControl = in.readInt();
		permisson = in.readInt();
	}

	public static final Parcelable.Creator<ChannelInfo> CREATOR = new Parcelable.Creator<ChannelInfo>() {
		@Override
		public ChannelInfo createFromParcel(Parcel in) {
			return new ChannelInfo(in);
		}

		@Override
		public ChannelInfo[] newArray(int size) {
			return new ChannelInfo[size];
		}
	};

	private ChannelInfo(Parcel in) {
		this();
		id = in.readInt();
		mode = in.readString();
		channelId = in.readString();
		range = in.readString();
		channelName = in.readString();
		source = in.readString();
		index = in.readInt();
		type = in.readInt();
		mSkipControl = in.readInt();
		permisson = in.readInt();
	}

}
