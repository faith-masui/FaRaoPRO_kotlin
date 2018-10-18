package jp.faraopro.play.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import jp.faraopro.play.mclient.IMCItem;
import jp.faraopro.play.mclient.MCAdItem;
import jp.faraopro.play.mclient.MCAudioItem;
import jp.faraopro.play.mclient.MCBenifitItem;
import jp.faraopro.play.mclient.MCBookmarkItem;
import jp.faraopro.play.mclient.MCBusinessItem;
import jp.faraopro.play.mclient.MCChannelItem;
import jp.faraopro.play.mclient.MCChartItem;
import jp.faraopro.play.mclient.MCGenreItem;
import jp.faraopro.play.mclient.MCLocationItem;
import jp.faraopro.play.mclient.MCSearchItem;
import jp.faraopro.play.mclient.MCStreamItem;
import jp.faraopro.play.mclient.MCTemplateItem;
import jp.faraopro.play.mclient.MCTimetableItem;

/**
 * 文字列リスト格納クラス
 * 
 * @author Aim
 * 
 */
public class MCListDataInfo implements Parcelable {
	private List<IMCItem> listData;

	public String itemName;

	/**
	 * コンストラクタ
	 */
	public MCListDataInfo() {
		listData = new ArrayList<IMCItem>();
	}

	public List<IMCItem> getListData() {
		return listData;
	}

	public void setListData(List<IMCItem> listData) {
		this.listData = listData;
	}

	public void add(IMCItem item) {
		listData.add(item);
	}

	public void add(int index, IMCItem item) {
		listData.add(index, item);
	}

	public int size() {
		return listData.size();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(itemName);
		dest.writeTypedList(listData);
	}

	public void readFromParcel(Parcel in) {
		if (itemName.equals(MCLocationItem.class.getSimpleName())) {
			ArrayList<MCLocationItem> location = new ArrayList<MCLocationItem>();

			in.readTypedList(location, MCLocationItem.CREATOR);
			for (IMCItem it : location) {
				listData.add(it);
			}
		} else if (itemName.equals(MCAdItem.class.getSimpleName())) {
			ArrayList<MCAdItem> ad = new ArrayList<MCAdItem>();

			in.readTypedList(ad, MCAdItem.CREATOR);
			for (IMCItem it : ad) {
				listData.add(it);
			}
		} else if (itemName.equals(MCBenifitItem.class.getSimpleName())) {
			ArrayList<MCBenifitItem> benifit = new ArrayList<MCBenifitItem>();

			in.readTypedList(benifit, MCBenifitItem.CREATOR);
			for (IMCItem it : benifit) {
				listData.add(it);
			}
		} else if (itemName.equals(MCChartItem.class.getSimpleName())) {
			ArrayList<MCChartItem> shuffle = new ArrayList<MCChartItem>();

			in.readTypedList(shuffle, MCChartItem.CREATOR);
			for (IMCItem it : shuffle) {
				listData.add(it);
			}
		} else if (itemName.equals(MCGenreItem.class.getSimpleName())) {
			ArrayList<MCGenreItem> genre = new ArrayList<MCGenreItem>();

			in.readTypedList(genre, MCGenreItem.CREATOR);
			for (IMCItem it : genre) {
				listData.add(it);
			}
		} else if (itemName.equals(MCSearchItem.class.getSimpleName())) {
			ArrayList<MCSearchItem> artist = new ArrayList<MCSearchItem>();

			in.readTypedList(artist, MCSearchItem.CREATOR);
			for (IMCItem it : artist) {
				listData.add(it);
			}
		} else if (itemName.equals(MCChannelItem.class.getSimpleName())) {
			ArrayList<MCChannelItem> mychannel = new ArrayList<MCChannelItem>();

			in.readTypedList(mychannel, MCChannelItem.CREATOR);
			for (IMCItem it : mychannel) {
				listData.add(it);
			}
		} else if (itemName.equals(MCBusinessItem.class.getSimpleName())) {
			ArrayList<MCBusinessItem> business = new ArrayList<MCBusinessItem>();

			in.readTypedList(business, MCBusinessItem.CREATOR);
			for (IMCItem it : business) {
				listData.add(it);
			}
		} else if (itemName.equals(MCTemplateItem.class.getSimpleName())) {
			ArrayList<MCTemplateItem> template = new ArrayList<MCTemplateItem>();

			in.readTypedList(template, MCTemplateItem.CREATOR);
			for (IMCItem it : template) {
				listData.add(it);
			}
		} else if (itemName.equals(MCTimetableItem.class.getSimpleName())) {
			ArrayList<MCTimetableItem> timetable = new ArrayList<MCTimetableItem>();

			in.readTypedList(timetable, MCTimetableItem.CREATOR);
			for (IMCItem it : timetable) {
				listData.add(it);
			}
		} else if (itemName.equals(MCBookmarkItem.class.getSimpleName())) {
			ArrayList<MCBookmarkItem> bookmark = new ArrayList<MCBookmarkItem>();

			in.readTypedList(bookmark, MCBookmarkItem.CREATOR);
			for (IMCItem it : bookmark) {
				listData.add(it);
			}
		} else if (itemName.equals(MCStreamItem.class.getSimpleName())) {
			ArrayList<MCStreamItem> stream = new ArrayList<MCStreamItem>();

			in.readTypedList(stream, MCStreamItem.CREATOR);
			for (IMCItem it : stream) {
				listData.add(it);
			}
		} else if (itemName.equals(MCAudioItem.class.getSimpleName())) {
			ArrayList<MCAudioItem> audio = new ArrayList<MCAudioItem>();

			in.readTypedList(audio, MCAudioItem.CREATOR);
			for (IMCItem it : audio) {
				listData.add(it);
			}
		} else {
			in.readTypedList(listData, null);
		}
	}

	public static final Parcelable.Creator<MCListDataInfo> CREATOR = new Parcelable.Creator<MCListDataInfo>() {
		@Override
		public MCListDataInfo createFromParcel(Parcel in) {
			return new MCListDataInfo(in);
		}

		@Override
		public MCListDataInfo[] newArray(int size) {
			return new MCListDataInfo[size];
		}
	};

	private MCListDataInfo(Parcel in) {
		this();
		itemName = in.readString();
		if (itemName.equals(MCLocationItem.class.getSimpleName())) {
			ArrayList<MCLocationItem> location = new ArrayList<MCLocationItem>();

			in.readTypedList(location, MCLocationItem.CREATOR);
			for (IMCItem it : location) {
				listData.add(it);
			}
		} else if (itemName.equals(MCAdItem.class.getSimpleName())) {
			ArrayList<MCAdItem> ad = new ArrayList<MCAdItem>();

			in.readTypedList(ad, MCAdItem.CREATOR);
			for (IMCItem it : ad) {
				listData.add(it);
			}
		} else if (itemName.equals(MCBenifitItem.class.getSimpleName())) {
			ArrayList<MCBenifitItem> benifit = new ArrayList<MCBenifitItem>();

			in.readTypedList(benifit, MCBenifitItem.CREATOR);
			for (IMCItem it : benifit) {
				listData.add(it);
			}
		} else if (itemName.equals(MCChartItem.class.getSimpleName())) {
			ArrayList<MCChartItem> shuffle = new ArrayList<MCChartItem>();

			in.readTypedList(shuffle, MCChartItem.CREATOR);
			for (IMCItem it : shuffle) {
				listData.add(it);
			}
		} else if (itemName.equals(MCGenreItem.class.getSimpleName())) {
			ArrayList<MCGenreItem> genre = new ArrayList<MCGenreItem>();

			in.readTypedList(genre, MCGenreItem.CREATOR);
			for (IMCItem it : genre) {
				listData.add(it);
			}
		} else if (itemName.equals(MCSearchItem.class.getSimpleName())) {
			ArrayList<MCSearchItem> artist = new ArrayList<MCSearchItem>();

			in.readTypedList(artist, MCSearchItem.CREATOR);
			for (IMCItem it : artist) {
				listData.add(it);
			}
		} else if (itemName.equals(MCChannelItem.class.getSimpleName())) {
			ArrayList<MCChannelItem> mychannel = new ArrayList<MCChannelItem>();

			in.readTypedList(mychannel, MCChannelItem.CREATOR);
			for (IMCItem it : mychannel) {
				listData.add(it);
			}
		} else if (itemName.equals(MCBusinessItem.class.getSimpleName())) {
			ArrayList<MCBusinessItem> business = new ArrayList<MCBusinessItem>();

			in.readTypedList(business, MCBusinessItem.CREATOR);
			for (IMCItem it : business) {
				listData.add(it);
			}
		} else if (itemName.equals(MCTemplateItem.class.getSimpleName())) {
			ArrayList<MCTemplateItem> template = new ArrayList<MCTemplateItem>();

			in.readTypedList(template, MCTemplateItem.CREATOR);
			for (IMCItem it : template) {
				listData.add(it);
			}
		} else if (itemName.equals(MCTimetableItem.class.getSimpleName())) {
			ArrayList<MCTimetableItem> timetable = new ArrayList<MCTimetableItem>();

			in.readTypedList(timetable, MCTimetableItem.CREATOR);
			for (IMCItem it : timetable) {
				listData.add(it);
			}
		} else if (itemName.equals(MCBookmarkItem.class.getSimpleName())) {
			ArrayList<MCBookmarkItem> bookmark = new ArrayList<MCBookmarkItem>();

			in.readTypedList(bookmark, MCBookmarkItem.CREATOR);
			for (IMCItem it : bookmark) {
				listData.add(it);
			}
		} else if (itemName.equals(MCStreamItem.class.getSimpleName())) {
			ArrayList<MCStreamItem> stream = new ArrayList<MCStreamItem>();

			in.readTypedList(stream, MCStreamItem.CREATOR);
			for (IMCItem it : stream) {
				listData.add(it);
			}
		} else if (itemName.equals(MCAudioItem.class.getSimpleName())) {
			ArrayList<MCAudioItem> audio = new ArrayList<MCAudioItem>();

			in.readTypedList(audio, MCAudioItem.CREATOR);
			for (IMCItem it : audio) {
				listData.add(it);
			}
		} else {
			in.readTypedList(listData, null);
		}
	}

	public MCListDataInfo(ArrayList<IMCItem> listData) {
		this.listData = listData;
	}

}
