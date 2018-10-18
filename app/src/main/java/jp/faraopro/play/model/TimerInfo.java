package jp.faraopro.play.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

import jp.faraopro.play.common.Consts;
import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCTimetableItem;

/**
 * 楽曲情報格納クラス
 *
 * @author Aim
 *
 */
public class TimerInfo implements Parcelable {
	public static final byte[] WEEK_BYTE_ARRAY = { 0x01, // 0000 0001
			0x02, // 0000 0010
			0x04, // 0000 0100
			0x08, // 0000 1000
			0x10, // 0001 0000
			0x20, // 0010 0000
			0x40, // 0100 0000
	};

	public static final Comparator<TimerInfo> COMPARATOR_BY_TIME_AND_WEEK = new Comparator<TimerInfo>() {
		@Override
		public int compare(TimerInfo lhs, TimerInfo rhs) {
			int diffOfTime = lhs.getTime() - rhs.getTime();
			if (diffOfTime == 0) {
				for (byte week : WEEK_BYTE_ARRAY) {
					if ((week & lhs.getWeek()) != 0)
						return -1;
					else if ((week & rhs.getWeek()) != 0)
						return 1;
				}
				return 0;
			} else {
				return diffOfTime;
			}
		}
	};

	private byte week;
	private int id = -1;
	private int time;
	private int type = Consts.MUSIC_TYPE_NORMAL;
	private int index;
	private String resource;
	private String name;
	private String resourceName;
	private int mode;
	private int channelId;
	private String range;
	private int permission;

	public TimerInfo() {
	}

	public TimerInfo(MCTimetableItem item) {
		name = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME);
		// ONタイマーの場合
		if (item.getString(MCDefResult.MCR_KIND_TIMER_TYPE).equalsIgnoreCase("on")) {
			// お気に入り再生
			if (item.getString(MCDefResult.MCR_KIND_SOURCE_TYPE).equalsIgnoreCase(Consts.TEMPLATE_SOURCE_TYPE_FARAO)) {
				type = Consts.MUSIC_TYPE_NORMAL;
			}
		}
		// OFFタイマーの場合
		else {
			type = Consts.MUSIC_TYPE_STOP;
		}
		// 時間と曜日を分割
		// hh:mm w1,w2,w3... -> [hh:mm] [w1,w2,w3...]
		String[] tmp = item.getString(MCDefResult.MCR_KIND_TIMER_RULE).split(" ");
		// 時間を分単位に変換する
		String[] aryTime = tmp[0].split(":");
		time = Integer.parseInt(aryTime[0]) * 60 + Integer.parseInt(aryTime[1]);
		// 曜日をbyteに変換する
		ArrayList<Integer> weekList = new ArrayList<Integer>();
		String[] aryWeek = tmp[1].split(",");
		for (int i = 0; i < aryWeek.length; i++) {
			int week = Integer.parseInt(aryWeek[i]) + 1;
			if (week > 7 || week < 1)
				week = 1;
			weekList.add(week);
		}
		setWeek(weekList);
		resourceName = item.getString(MCDefResult.MCR_KIND_SOURCE_NAME);
		if (type != Consts.MUSIC_TYPE_STOP) {
			// パラメータをカンマ区切りに変換する
			String tmp2 = item.getString(MCDefResult.MCR_KIND_SOURCE_URL).substring(11);
			FRODebug.logD(getClass(), "param = " + tmp2, true);
			String[] params = tmp2.split("/");
			if (params[0].equalsIgnoreCase("listen")) {
				resource = params[1] + "," + params[2];
				mode = Integer.parseInt(params[1]);
				channelId = Integer.parseInt(params[2]);
				if (params.length > 3) {
					resource += "," + params[3];
					range = params[3];
				}
				// TODO インポートしたテンプレートのチャンネルは全て権限がかからなくなる
				permission = 0xFFFF;
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public byte getWeek() {
		return week;
	}

	public void setWeek(byte week) {
		this.week = week;
	}

	public void setWeek(ArrayList<Integer> weekList) {
		int tmp = 0;
		if (weekList != null && weekList.size() > 0) {
			for (Integer i : weekList) {
				tmp += Math.pow(2, i - 1);
			}
		}
		this.week = (byte) tmp;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Integer> getWeekList() {
		ArrayList<Integer> weekList = new ArrayList<Integer>();
		for (int i = 0; i < WEEK_BYTE_ARRAY.length; i++) {
			if ((week & WEEK_BYTE_ARRAY[i]) == WEEK_BYTE_ARRAY[i]) {
				weekList.add(new Integer(i + 1));
			}
		}

		return weekList;
	}

	public void setWeekList(ArrayList<Integer> weekList) {
		setWeek(weekList);
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public int getPermission() {
		return permission;
	}

	public void setPermission(int permission) {
		this.permission = permission;
	}

	// public void copy(TimerInfo dest) {
	// dest.week = this.week;
	// dest.id = this.id;
	// dest.time = this.time;
	// dest.type = this.type;
	// dest.index = this.index;
	// dest.resource = this.resource;
	// dest.name = this.name;
	// dest.resourceName = this.resourceName;
	// dest.setWeekList(this.week);
	// dest.mode = this.mode;
	// dest.channelId = this.channelId;
	// dest.range = this.range;
	// dest.permission = this.permission;
	// }

	/**
	 * 現在時刻以降で最も近い自分のタイマーの曜日を返す<br>
	 * 返り値は{@link Calendar}クラスの各曜日
	 */
	public int getNearestDay() {
		Calendar nowTime = Calendar.getInstance();
		nowTime.setTimeInMillis(System.currentTimeMillis()); // 現在時刻を取得
		int time = nowTime.get(Calendar.HOUR_OF_DAY) * 60 + nowTime.get(Calendar.MINUTE);
		time = this.time - time;

		ArrayList<Integer> weekList = getWeekList();

		int day = 8;
		for (Integer i : weekList) {
			int tmp = i - nowTime.get(Calendar.DAY_OF_WEEK);
			if (tmp < 0)
				tmp += 7;
			if (tmp == 0 && time <= 0)
				// if (tmp == 0 && time < 0)
				tmp += 7;
			if (day > tmp)
				day = tmp;
		}

		return day;
	}

	public static Parcelable.Creator<TimerInfo> getCreator() {
		return CREATOR;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte(week);
		dest.writeInt(id);
		dest.writeInt(time);
		dest.writeInt(type);
		dest.writeInt(index);
		dest.writeString(resource);
		dest.writeString(name);
		dest.writeString(resourceName);
		dest.writeInt(mode);
		dest.writeInt(channelId);
		dest.writeString(range);
		dest.writeInt(permission);
	}

	public void readFromParcel(Parcel in) {
		week = in.readByte();
		id = in.readInt();
		time = in.readInt();
		type = in.readInt();
		index = in.readInt();
		resource = in.readString();
		name = in.readString();
		resourceName = in.readString();
		mode = in.readInt();
		channelId = in.readInt();
		range = in.readString();
		permission = in.readInt();
	}

	public static final Parcelable.Creator<TimerInfo> CREATOR = new Parcelable.Creator<TimerInfo>() {
		@Override
		public TimerInfo createFromParcel(Parcel in) {
			return new TimerInfo(in);
		}

		@Override
		public TimerInfo[] newArray(int size) {
			return new TimerInfo[size];
		}
	};

	private TimerInfo(Parcel in) {
		this();
		week = in.readByte();
		id = in.readInt();
		time = in.readInt();
		type = in.readInt();
		index = in.readInt();
		resource = in.readString();
		name = in.readString();
		resourceName = in.readString();
		mode = in.readInt();
		channelId = in.readInt();
		range = in.readString();
		permission = in.readInt();
	}

}
