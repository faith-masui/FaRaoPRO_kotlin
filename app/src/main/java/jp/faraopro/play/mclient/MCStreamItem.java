package jp.faraopro.play.mclient;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Musicサーバーからの情報(Channelアイテム)管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCStreamItem implements IMCItem, Parcelable {
	private String mStreamId;
	private String mName;
	private String mName_en;
	private String mTitle;
	private String mTitle_en;
	private String mDescription;
	private String mDescription_en;
	private String mSourceType;
	private String mMediaType;
	private String mSessionType;
	private String mContentLink;
	private String mPreviewLink;
	private String mExternalLink1;
	private String mExternalLink2;
	private String mJacketId;
	private String mStationId;
	private String mStationName;
	private String mStationName_en;
	private String mReplayGain;
	private String showDuration;
	private List<String> showJackets;
	private String serviceLevel;
	private String features;
	private String permissions;

	/**
	 * コンストラクタ
	 */
	public MCStreamItem() {
		lclear();
	}

	@Override
	public void clear() {
		lclear();
	}

	private void lclear() {
		mStreamId = null;
		mName = null;
		mName_en = null;
		mTitle = null;
		mTitle_en = null;
		mDescription = null;
		mDescription_en = null;
		mSourceType = null;
		mMediaType = null;
		mSessionType = null;
		mContentLink = null;
		mPreviewLink = null;
		mExternalLink1 = null;
		mExternalLink2 = null;
		mJacketId = null;
		mStationId = null;
		mStationName = null;
		mStationName_en = null;
		mReplayGain = null;
		if (showJackets != null) {
			showJackets.clear();
			showJackets = null;
		}
		showDuration = null;
	}

	@Override
	public void setString(int kind, String value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_STREAM_ID:
			mStreamId = value;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME:
			mName = value;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN:
			mName_en = value;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_TITLE:
			mTitle = value;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_TITLE_EN:
			mTitle_en = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_DESCRIPTION:
			mDescription = value;
			break;
		case MCDefResult.MCR_KIND_ITEM_DESCRIPTION_EN:
			mDescription_en = value;
			break;
		case MCDefResult.MCR_KIND_SOURCE_TYPE:
			mSourceType = value;
			break;
		case MCDefResult.MCR_KIND_MEDIA_TYPE:
			mMediaType = value;
			break;
		case MCDefResult.MCR_KIND_SESSION_TYPE:
			mSessionType = value;
			break;
		case MCDefResult.MCR_KIND_CONTENT_LINK:
			mContentLink = value;
			break;
		case MCDefResult.MCR_KIND_PREVIEW_LINK:
			mPreviewLink = value;
			break;
		case MCDefResult.MCR_KIND_EXTERNAL_LINK1:
			mExternalLink1 = value;
			break;
		case MCDefResult.MCR_KIND_EXTERNAL_LINK2:
			mExternalLink2 = value;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_JACKET_ID:
			mJacketId = value;
			break;
		case MCDefResult.MCR_KIND_STATION_ID:
			mStationId = value;
			break;
		case MCDefResult.MCR_KIND_STATION_NAME:
			mStationName = value;
			break;
		case MCDefResult.MCR_KIND_STATION_NAME_EN:
			mStationName_en = value;
			break;
		case MCDefResult.MCR_KIND_REPLAY_GAIN:
			mReplayGain = value;
			break;
		case MCDefResult.MCR_KIND_SHOW_DURATION:
			showDuration = value;
			break;
		case MCDefResult.MCR_KIND_SHOW_JACKET:
			if (showJackets == null)
				showJackets = new ArrayList<String>();
			showJackets.add(value);
			break;
		case MCDefResult.MCR_KIND_SERVICE_LEVEL:
			serviceLevel = value;
			break;
		case MCDefResult.MCR_KIND_FEATURES:
			features = value;
			break;
		case MCDefResult.MCR_KIND_PERMISSIONS:
			permissions = value;
			break;
		}
	}

	@Override
	public String getString(int kind) {
		String value = null;
		switch (kind) {
		case MCDefResult.MCR_KIND_STREAM_ID:
			value = mStreamId;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME:
			value = mName;
			break;
		case MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN:
			value = mName_en;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_TITLE:
			value = mTitle;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_TITLE_EN:
			value = mTitle_en;
			break;
		case MCDefResult.MCR_KIND_ITEM_DESCRIPTION:
			value = mDescription;
			break;
		case MCDefResult.MCR_KIND_ITEM_DESCRIPTION_EN:
			value = mDescription_en;
			break;
		case MCDefResult.MCR_KIND_SOURCE_TYPE:
			value = mSourceType;
			break;
		case MCDefResult.MCR_KIND_MEDIA_TYPE:
			value = mMediaType;
			break;
		case MCDefResult.MCR_KIND_SESSION_TYPE:
			value = mSessionType;
			break;
		case MCDefResult.MCR_KIND_CONTENT_LINK:
			value = mContentLink;
			break;
		case MCDefResult.MCR_KIND_PREVIEW_LINK:
			value = mPreviewLink;
			break;
		case MCDefResult.MCR_KIND_EXTERNAL_LINK1:
			value = mExternalLink1;
			break;
		case MCDefResult.MCR_KIND_EXTERNAL_LINK2:
			value = mExternalLink2;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_JACKET_ID:
			value = mJacketId;
			break;
		case MCDefResult.MCR_KIND_STATION_ID:
			value = mStationId;
			break;
		case MCDefResult.MCR_KIND_STATION_NAME:
			value = mStationName;
			break;
		case MCDefResult.MCR_KIND_STATION_NAME_EN:
			value = mStationName_en;
			break;
		case MCDefResult.MCR_KIND_REPLAY_GAIN:
			value = mReplayGain;
			break;
		case MCDefResult.MCR_KIND_SHOW_DURATION:
			value = showDuration;
			break;
		case MCDefResult.MCR_KIND_SERVICE_LEVEL:
			value = serviceLevel;
			break;
		case MCDefResult.MCR_KIND_FEATURES:
			value = features;
			break;
		case MCDefResult.MCR_KIND_PERMISSIONS:
			value = permissions;
			break;
		}
		return value;
	}

	public List<String> getList(int kind) {
		switch (kind) {
		case MCDefResult.MCR_KIND_SHOW_JACKET:
			return showJackets;
		}
		return null;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(mStreamId);
		arg0.writeString(mName);
		arg0.writeString(mName_en);
		arg0.writeString(mTitle);
		arg0.writeString(mTitle_en);
		arg0.writeString(mDescription);
		arg0.writeString(mDescription_en);
		arg0.writeString(mSourceType);
		arg0.writeString(mMediaType);
		arg0.writeString(mSessionType);
		arg0.writeString(mContentLink);
		arg0.writeString(mPreviewLink);
		arg0.writeString(mExternalLink1);
		arg0.writeString(mExternalLink2);
		arg0.writeString(mJacketId);
		arg0.writeString(mStationId);
		arg0.writeString(mStationName);
		arg0.writeString(mStationName_en);
		arg0.writeString(mReplayGain);
		arg0.writeString(showDuration);
		String jackets = "";
		if (showJackets != null && showJackets.size() > 0) {
			for (String jacket : showJackets) {
				jackets += (jacket + ",");
			}
		}
		arg0.writeString(jackets);
		// arg0.writeStringList(showJackets);
		arg0.writeString(serviceLevel);
		arg0.writeString(features);
		arg0.writeString(permissions);
	}

	private MCStreamItem(Parcel in) {
		mStreamId = in.readString();
		mName = in.readString();
		mName_en = in.readString();
		mTitle = in.readString();
		mTitle_en = in.readString();
		mDescription = in.readString();
		mDescription_en = in.readString();
		mSourceType = in.readString();
		mMediaType = in.readString();
		mSessionType = in.readString();
		mContentLink = in.readString();
		mPreviewLink = in.readString();
		mExternalLink1 = in.readString();
		mExternalLink2 = in.readString();
		mJacketId = in.readString();
		mStationId = in.readString();
		mStationName = in.readString();
		mStationName_en = in.readString();
		mReplayGain = in.readString();
		showDuration = in.readString();
		String jackets = in.readString();
		showJackets = new ArrayList<String>();
		if (!TextUtils.isEmpty(jackets)) {
			String[] jacketAry = jackets.split(",");
			for (String jacket : jacketAry) {
				if (!TextUtils.isEmpty(jackets))
					showJackets.add(jacket);
			}
		}
		// in.readStringList(showJackets);
		serviceLevel = in.readString();
		features = in.readString();
		permissions = in.readString();
	}

	public static final Parcelable.Creator<MCStreamItem> CREATOR = new Parcelable.Creator<MCStreamItem>() {
		@Override
		public MCStreamItem createFromParcel(Parcel in) {
			return new MCStreamItem(in);
		}

		@Override
		public MCStreamItem[] newArray(int size) {
			return new MCStreamItem[size];
		}
	};
}
