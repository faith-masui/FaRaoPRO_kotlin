package jp.faraopro.play.mclient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Musicサーバーからの情報(Channelアイテム)管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCBusinessItem implements IMCItem, Parcelable {
	private String id;
	private String parent;
	private String name;
	private String nameEn;
	private String permission;
	private String nodeType;

	/**
	 * コンストラクタ
	 */
	public MCBusinessItem() {
	}

	@Override
	public void clear() {
	}

	@Override
	public void setString(int kind, String value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_CHANNELITEM_ID:
			id = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_PARENT:
			parent = value;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME:
			name = value;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN:
			nameEn = value;
			break;
		case MCDefResult.MCR_KIND_PERMISSIONS:
			permission = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_NODE_TYPE:
			nodeType = value;
			break;
		}
	}

	@Override
	public String getString(int kind) {
		String value = null;
		switch (kind) {
		case MCDefResult.MCR_KIND_CHANNELITEM_ID:
			value = id;
			break;
		case MCDefResult.MCR_KIND_ITEM_PARENT:
			value = parent;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME:
			value = name;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN:
			value = nameEn;
			break;
		case MCDefResult.MCR_KIND_PERMISSIONS:
			value = permission;
			break;
		case MCDefResult.MCR_KIND_ITEM_NODE_TYPE:
			value = nodeType;
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
		dest.writeString(id);
		dest.writeString(parent);
		dest.writeString(name);
		dest.writeString(nameEn);
		dest.writeString(permission);
		dest.writeString(nodeType);
	}

	private MCBusinessItem(Parcel in) {
		id = in.readString();
		parent = in.readString();
		name = in.readString();
		nameEn = in.readString();
		permission = in.readString();
		nodeType = in.readString();
	}

	public static final Parcelable.Creator<MCBusinessItem> CREATOR = new Parcelable.Creator<MCBusinessItem>() {
		@Override
		public MCBusinessItem createFromParcel(Parcel in) {
			return new MCBusinessItem(in);
		}

		@Override
		public MCBusinessItem[] newArray(int size) {
			return new MCBusinessItem[size];
		}
	};
}
