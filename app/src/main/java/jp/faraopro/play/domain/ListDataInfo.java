package jp.faraopro.play.domain;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 文字列リスト格納クラス
 * 
 * @author Aim
 * 
 */
public class ListDataInfo implements Parcelable {
	private ArrayList<String> listData;

	/**
	 * コンストラクタ
	 */
	public ListDataInfo() {
		listData = new ArrayList<String>();
	}

	public ArrayList<String> getListData() {
		return listData;
	}

	public void setListData(ArrayList<String> listData) {
		this.listData = listData;
	}

	public void add(String str) {
		listData.add(str);
	}

	public void add(String str, int index) {
		listData.add(index, str);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringList(listData);
	}

	public void readFromParcel(Parcel in) {
		in.readStringList(listData);
	}

	public static final Parcelable.Creator<ListDataInfo> CREATOR = new Parcelable.Creator<ListDataInfo>() {
		@Override
		public ListDataInfo createFromParcel(Parcel in) {
			return new ListDataInfo(in);
		}

		@Override
		public ListDataInfo[] newArray(int size) {
			return new ListDataInfo[size];
		}
	};

	private ListDataInfo(Parcel in) {
		this();
		in.readStringList(listData);
	}

	public ListDataInfo(ArrayList<String> listData) {
		this.listData = listData;
	}

}
