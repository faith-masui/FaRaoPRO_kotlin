package jp.faraopro.play.mclient;

import android.os.Parcel;
import android.os.Parcelable;
import jp.faraopro.play.util.FROUtils;

/**
 * Musicサーバーからの情報(Channelアイテム)管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCCdnMusicItem implements IMCItem, Parcelable, IMCPostActionParam {
	private String type;
	private String user;
	private String secret;
	private String jacketId;
	private String jacketUrl;
	private String trackId;
	private String trackUrl;
	private String method;
	private String key;
	private String iv;
	private String signature;

	/**
	 * コンストラクタ
	 */
	public MCCdnMusicItem() {
		lclear();
	}

	@Override
	public void clear() {
		lclear();
	}

	private void lclear() {
		type = null;
		user = null;
		secret = null;
		jacketId = null;
		jacketUrl = null;
		trackId = null;
		trackUrl = null;
		method = null;
		key = null;
		iv = null;
		signature = null;
	}

	@Override
	public void setString(int kind, String value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_ITEM_TYPE:
			type = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_USER:
			user = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_SECRET:
			secret = value;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_JACKET_ID:
			jacketId = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_CDN_JACKET_URL:
			jacketUrl = value;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_ID:
			trackId = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_CDN_TRACK_URL:
			trackUrl = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_METHOD:
			method = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_KEY:
			key = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_IV:
			iv = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_SIGNATURE:
			signature = value;
			break;
		default:
			break;
		}
	}

	@Override
	public String getString(int kind) {
		String value = null;
		switch (kind) {
		case MCDefResult.MCR_KIND_ITEM_TYPE:
			value = type;
			break;
		case MCDefResult.MCR_KIND_ITEM_USER:
			value = user;
			break;
		case MCDefResult.MCR_KIND_ITEM_SECRET:
			value = secret;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_JACKET_ID:
			value = jacketId;
			break;
		case MCDefResult.MCR_KIND_ITEM_CDN_JACKET_URL:
			value = jacketUrl;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_ID:
			value = trackId;
			break;
		case MCDefResult.MCR_KIND_ITEM_CDN_TRACK_URL:
			value = trackUrl;
			break;
		case MCDefResult.MCR_KIND_ITEM_METHOD:
			value = method;
			break;
		case MCDefResult.MCR_KIND_ITEM_KEY:
			value = key;
			break;
		case MCDefResult.MCR_KIND_ITEM_IV:
			value = iv;
			break;
		case MCDefResult.MCR_KIND_ITEM_SIGNATURE:
			value = signature;
			break;
		default:
			break;
		}
		return value;
	}

	public String getOutputFileName(int kind) {
		String output = null;
		String ex_dir = null;

		switch (kind) {
		case MCDefAction.MCA_KIND_TRACK_DL:
			ex_dir = FROUtils.getTrackPath();
			if (ex_dir != null && trackId != null)
				output = ex_dir + trackId;
			break;
		case MCDefAction.MCA_KIND_ARTWORK_DL:
			ex_dir = FROUtils.getJacketPath();
			if (ex_dir != null && jacketId != null) {
				jacketId = jacketId.replace(".jpg", "");
				jacketId = jacketId.replace(".png", "");
				output = ex_dir + jacketId;
			}
			break;
		case MCDefAction.MCA_KIND_THUMB_DL:
			String thumbId = trackId;
			ex_dir = FROUtils.getFaraoDirectory();
			if (ex_dir != null && thumbId != null) {
				thumbId = thumbId.replace(".jpg", "");
				thumbId = thumbId.replace(".png", "");
				output = ex_dir + "/" + thumbId;
			}
			break;
		default:
			break;
		}
		return output;
	}

	public MCCdnMusicItem copy() {
		MCCdnMusicItem clone = new MCCdnMusicItem();

		clone.setString(MCDefResult.MCR_KIND_ITEM_TYPE, this.type);
		clone.setString(MCDefResult.MCR_KIND_ITEM_USER, this.user);
		clone.setString(MCDefResult.MCR_KIND_ITEM_SECRET, this.secret);
		clone.setString(MCDefResult.MCR_KIND_TRACKITEM_JACKET_ID, this.jacketId);
		clone.setString(MCDefResult.MCR_KIND_ITEM_CDN_JACKET_URL, this.jacketUrl);
		clone.setString(MCDefResult.MCR_KIND_TRACKITEM_ID, this.trackId);
		clone.setString(MCDefResult.MCR_KIND_ITEM_CDN_TRACK_URL, this.trackUrl);
		clone.setString(MCDefResult.MCR_KIND_ITEM_METHOD, this.method);
		clone.setString(MCDefResult.MCR_KIND_ITEM_KEY, this.key);
		clone.setString(MCDefResult.MCR_KIND_ITEM_IV, this.iv);
		clone.setString(MCDefResult.MCR_KIND_ITEM_SIGNATURE, this.signature);

		return clone;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(type);
		dest.writeString(user);
		dest.writeString(secret);
		dest.writeString(jacketId);
		dest.writeString(jacketUrl);
		dest.writeString(trackId);
		dest.writeString(trackUrl);
		dest.writeString(method);
		dest.writeString(key);
		dest.writeString(iv);
		dest.writeString(signature);
	}

	private MCCdnMusicItem(Parcel in) {
		type = in.readString();
		user = in.readString();
		secret = in.readString();
		jacketId = in.readString();
		jacketUrl = in.readString();
		trackId = in.readString();
		trackUrl = in.readString();
		method = in.readString();
		key = in.readString();
		iv = in.readString();
		signature = in.readString();
	}

	public static final Parcelable.Creator<MCCdnMusicItem> CREATOR = new Parcelable.Creator<MCCdnMusicItem>() {
		@Override
		public MCCdnMusicItem createFromParcel(Parcel in) {
			return new MCCdnMusicItem(in);
		}

		@Override
		public MCCdnMusicItem[] newArray(int size) {
			return new MCCdnMusicItem[size];
		}
	};

	@Override
	public void setStringValue(int kind, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getStringValue(int kind) {
		// TODO Auto-generated method stub
		return null;
	}
}
