package jp.faraopro.play.mclient;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;

/**
 * Musicサーバーからの情報(Trackアイテム)管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCTrackItem implements IMCItem {
	private String mId;
	private String mTitle;
	private String mArtist;
	private String mTitle_en;
	private String mArtist_en;
	private String mAlbum;
	private String mAlbum_en;
	private String mGenre;
	private String mGenre_en;
	private String mDescription;
	private String mDescription_en;
	private String mRelease_date;
	private String mAffiliate_url;
	// debugging
	private List<String> mAffiliate_urls;
	private String mJacket_id;
	private String mTrialTime;
	private String mArtistId;
	private String mReplayGain;

	/**
	 * コンストラクタ
	 */
	public MCTrackItem() {
		lclear();
		// debugging
		mAffiliate_urls = new ArrayList<String>();
	}

	@Override
	public void clear() {
		lclear();
	}

	private void lclear() {
		mId = null;
		mTitle = null;
		mArtist = null;
		mTitle_en = null;
		mArtist_en = null;
		mAlbum = null;
		mAlbum_en = null;
		mGenre = null;
		mGenre_en = null;
		mDescription = null;
		mDescription_en = null;
		mRelease_date = null;
		mAffiliate_url = null;
		// debugging
		if (mAffiliate_urls != null)
			mAffiliate_urls.clear();
		mAffiliate_urls = null;
		mJacket_id = null;
		mTrialTime = null;
		mArtistId = null;
		mReplayGain = null;
	}

	@Override
	public void setString(int kind, String value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_TRACKITEM_ID:
			mId = value;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_TITLE:
			mTitle = value;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_ARTIST:
			mArtist = value;
			break;

		case MCDefResult.MCR_KIND_TRACKITEM_TITLE_EN:
			mTitle_en = value;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_ARTIST_EN:
			mArtist_en = value;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_ALBUM:
			mAlbum = value;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_ALBUM_EN:
			mAlbum_en = value;
			break;
		case MCDefResult.MCR_KIND_GENRE_LIST:
			mGenre = value;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_GENRE_EN:
			mGenre_en = value;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_DESCRIPTION:
			mDescription = value;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_DESCRIPTION_EN:
			mDescription_en = value;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_RELEASE_DATE:
			mRelease_date = value;
			break;
		case MCDefResult.MCR_KIND_TARGET:
			if (mAffiliate_url == null) {
				mAffiliate_url = "";
			} else {
				mAffiliate_url += ",";
			}
			mAffiliate_url += value;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_URL:
			if (mAffiliate_url == null) {
				mAffiliate_url = "";
			} else {
				mAffiliate_url += " ";
			}
			mAffiliate_url += value;
			// mAffiliate_urls.add(value);
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_JACKET_ID:
			mJacket_id = value;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_TRIAL_TIME:
			mTrialTime = value;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_ARTIST_ID:
			mArtistId = value;
			break;

		case MCDefResult.MCR_KIND_REPLAY_GAIN:
			mReplayGain = value;
			break;

		default:
			break;
		}
	}

	@Override
	public String getString(int kind) {
		String value = null;
		switch (kind) {
		case MCDefResult.MCR_KIND_TRACKITEM_ID:
			value = mId;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_TITLE:
			value = mTitle;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_ARTIST:
			value = mArtist;
			break;

		case MCDefResult.MCR_KIND_TRACKITEM_TITLE_EN:
			value = mTitle_en;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_ARTIST_EN:
			value = mArtist_en;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_ALBUM:
			value = mAlbum;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_ALBUM_EN:
			value = mAlbum_en;
			break;
		case MCDefResult.MCR_KIND_GENRE_LIST:
			value = mGenre;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_GENRE_EN:
			value = mGenre_en;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_DESCRIPTION:
			value = mDescription;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_DESCRIPTION_EN:
			value = mDescription_en;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_RELEASE_DATE:
			value = mRelease_date;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_URL:
			value = mAffiliate_url;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_JACKET_ID:
			value = mJacket_id;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_TRIAL_TIME:
			value = mTrialTime;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_ARTIST_ID:
			value = mArtistId;
			break;

		case MCDefResult.MCR_KIND_REPLAY_GAIN:
			value = mReplayGain;
			break;

		default:
			break;
		}
		return value;
	}

	public String getListString(int kind, int index) {
		String value = null;
		switch (kind) {
		case MCDefResult.MCR_KIND_TRACKITEM_URL:
			value = mAffiliate_urls.get(index);
			break;
		}

		return value;
	}

	public List<String> getList(int kind) {
		switch (kind) {
		case MCDefResult.MCR_KIND_TRACKITEM_URL:
			return mAffiliate_urls;
		}

		return null;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}
}
