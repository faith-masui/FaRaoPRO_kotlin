package jp.faraopro.play.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import jp.faraopro.play.BuildConfig;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCStreamItem;

/**
 * 楽曲情報格納クラス
 * 
 * @author Aim
 * 
 */
public class MusicInfo implements Parcelable {
	private int playerType = -1;
	private String artist; // アーティスト名
	private String title; // 楽曲名
	private String relese; // リリース日
	private String genre; // ジャンル
	private String info; // 楽曲説明
	private String url; // 楽曲購入ページへのURL
	private String artworkPath; // 画像のパス
	private String thumbPath; // サムネイル画像のパス
	private String urlSearch; // アーティスト名(和名)
	private String id;
	private int length;
	private int currentPos;
	private String artistId;
	private String channelName;

	// 有線放送対応
	private String name;
	private String nameEn;
	private String titleEn;
	private String description;
	private String descriptionEn;
	private String previewLink;
	private String externalLink1;
	private String stationName;
	private String stationNameEn;
    private String showDuration;
    private List<String> showJackets;

    public void show() {
        if (!BuildConfig.DEBUG)
            return;

        FRODebug.logD(getClass(), "playerType " + playerType, true);
        FRODebug.logD(getClass(), "artist " + artist, true);
        FRODebug.logD(getClass(), "title " + title, true);
        FRODebug.logD(getClass(), "release " + relese, true);
        FRODebug.logD(getClass(), "genre " + genre, true);
        FRODebug.logD(getClass(), "info " + info, true);
        FRODebug.logD(getClass(), "url " + url, true);
        FRODebug.logD(getClass(), "artworkPath " + artworkPath, true);
        FRODebug.logD(getClass(), "thumbPath " + thumbPath, true);
        FRODebug.logD(getClass(), "urlSearch " + urlSearch, true);
        FRODebug.logD(getClass(), "id " + id, true);
        FRODebug.logD(getClass(), "length " + length, true);
        FRODebug.logD(getClass(), "currentPos " + currentPos, true);
        FRODebug.logD(getClass(), "artistId " + artistId, true);
        FRODebug.logD(getClass(), "channelName " + channelName, true);
        FRODebug.logD(getClass(), "name " + name, true);
        FRODebug.logD(getClass(), "nameEn " + nameEn, true);
        FRODebug.logD(getClass(), "titleEn " + titleEn, true);
        FRODebug.logD(getClass(), "description " + description, true);
        FRODebug.logD(getClass(), "descriptionEn " + descriptionEn, true);
        FRODebug.logD(getClass(), "previewLink " + previewLink, true);
        FRODebug.logD(getClass(), "externalLink1 " + externalLink1, true);
        FRODebug.logD(getClass(), "stationName " + stationName, true);
        FRODebug.logD(getClass(), "stationNameEn " + stationNameEn, true);
        FRODebug.logD(getClass(), "showDuration " + showDuration, true);
        if (showJackets != null && showJackets.size() > 0) {
            for (String s : showJackets) {
                FRODebug.logD(getClass(), "showJackets " + s, true);
            }
        }
    }

	public MusicInfo() {
	}

	public MusicInfo(MCStreamItem item) {
		artworkPath = item.getString(MCDefResult.MCR_KIND_TRACKITEM_JACKET_ID);
		id = item.getString(MCDefResult.MCR_KIND_STREAM_ID);
		name = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME);
		nameEn = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN);
		title = item.getString(MCDefResult.MCR_KIND_TRACKITEM_TITLE);
		titleEn = item.getString(MCDefResult.MCR_KIND_TRACKITEM_TITLE_EN);
		description = item.getString(MCDefResult.MCR_KIND_ITEM_DESCRIPTION);
		descriptionEn = item.getString(MCDefResult.MCR_KIND_ITEM_DESCRIPTION_EN);
		previewLink = item.getString(MCDefResult.MCR_KIND_PREVIEW_LINK);
		externalLink1 = item.getString(MCDefResult.MCR_KIND_EXTERNAL_LINK1);
		stationName = item.getString(MCDefResult.MCR_KIND_STATION_NAME);
		stationNameEn = item.getString(MCDefResult.MCR_KIND_STATION_NAME_EN);
        showDuration = item.getString(MCDefResult.MCR_KIND_SHOW_DURATION);
        List<String> jackets = item.getList(MCDefResult.MCR_KIND_SHOW_JACKET);
        if (jackets != null)
            showJackets = new ArrayList<>(jackets);
	}

	public int getPlayerType() {
		return playerType;
	}

	public void setPlayerType(int playerType) {
		this.playerType = playerType;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRelese() {
		return relese;
	}

	public void setRelese(String relese) {
		this.relese = relese;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getUrl() {
		return url;
	}

	public ArrayList<Pair<String, String>> getUrlPairList() {
		ArrayList<Pair<String, String>> pairList = new ArrayList<Pair<String, String>>();
		if (TextUtils.isEmpty(url))
			return pairList;

		String[] urlPair = url.split(",");
		for (String s : urlPair) {
			if (!TextUtils.isEmpty(s)) {
				String[] tmp = s.split(" ");
				if (tmp[0].equals(Consts.AFFILIATE_BUY_CD) || tmp[0].equals(Consts.AFFILIATE_DOWNLOAD_ANDROID)) {
                    pairList.add(new Pair<>(tmp[0], tmp[1]));
				}
			}
		}

		return pairList;
	}

	public boolean isContainBuyCd() {
		boolean contain = false;
		ArrayList<Pair<String, String>> pairList = getUrlPairList();
		if (pairList != null && pairList.size() > 0) {
			for (Pair<String, String> pair : pairList) {
				if (pair.first.equals(Consts.AFFILIATE_BUY_CD)) {
					contain = true;
					break;
				}
			}
		}

		return contain;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getArtwork() {
		return artworkPath;
	}

	public void setArtwork(String path) {
		this.artworkPath = path;
	}

	public String getThumb() {
		return thumbPath;
	}

	public void setThumb(String path) {
		this.thumbPath = path;
	}

	public String getUrlSearch() {
		return urlSearch;
	}

	public void setUrlSearch(String url) {
		this.urlSearch = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getCurrentPos() {
		return currentPos;
	}

	public void setCurrentPos(int currentPos) {
		this.currentPos = currentPos;
	}

	public String getArtistId() {
		return artistId;
	}

	public void setArtistId(String artistId) {
		this.artistId = artistId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getTitleEn() {
		return titleEn;
	}

	public void setTitleEn(String titleEn) {
		this.titleEn = titleEn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescriptionEn() {
		return descriptionEn;
	}

	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}

	public String getPreviewLink() {
		return previewLink;
	}

	public void setPreviewLink(String previewLink) {
		this.previewLink = previewLink;
	}

	public String getExternalLink1() {
		return externalLink1;
	}

	public void setExternalLink1(String externalLink1) {
		this.externalLink1 = externalLink1;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationNameEn() {
		return stationNameEn;
	}

	public void setStationNameEn(String stationNameEn) {
		this.stationNameEn = stationNameEn;
	}

    public String getShowDuration() {
        return showDuration;
    }

    public void setShowDuration(String showDuration) {
        this.showDuration = showDuration;
    }

    public List<String> getShowJackets() {
        return showJackets;
    }

    public void setShowJackets(List<String> showJackets) {
        this.showJackets = showJackets;
    }

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(playerType);
		dest.writeString(artist);
		dest.writeString(title);
		dest.writeString(relese);
		dest.writeString(genre);
		dest.writeString(info);
		dest.writeString(url);
		dest.writeString(artworkPath);
		dest.writeString(thumbPath);
		dest.writeString(urlSearch);
		dest.writeString(id);
		dest.writeInt(length);
		dest.writeInt(currentPos);
		dest.writeString(artistId);
		dest.writeString(channelName);

		dest.writeString(name);
		dest.writeString(nameEn);
		dest.writeString(titleEn);
		dest.writeString(description);
		dest.writeString(descriptionEn);
		dest.writeString(previewLink);
		dest.writeString(externalLink1);
		dest.writeString(stationName);
		dest.writeString(stationNameEn);
        dest.writeString(showDuration);
        dest.writeStringList(showJackets);
	}

	public void readFromParcel(Parcel in) {
		playerType = in.readInt();
		artist = in.readString();
		title = in.readString();
		relese = in.readString();
		genre = in.readString();
		info = in.readString();
		url = in.readString();
		artworkPath = in.readString();
		thumbPath = in.readString();
		urlSearch = in.readString();
		id = in.readString();
		length = in.readInt();
		currentPos = in.readInt();
		artistId = in.readString();
		channelName = in.readString();

		name = in.readString();
		nameEn = in.readString();
		titleEn = in.readString();
		description = in.readString();
		descriptionEn = in.readString();
		previewLink = in.readString();
		externalLink1 = in.readString();
		stationName = in.readString();
		stationNameEn = in.readString();
        showDuration = in.readString();
        showJackets = new ArrayList<>();
        in.readStringList(showJackets);
	}

	public static final Parcelable.Creator<MusicInfo> CREATOR = new Parcelable.Creator<MusicInfo>() {
		@Override
		public MusicInfo createFromParcel(Parcel in) {
			return new MusicInfo(in);
		}

		@Override
		public MusicInfo[] newArray(int size) {
			return new MusicInfo[size];
		}
	};

	private MusicInfo(Parcel in) {
		this();
		playerType = in.readInt();
		artist = in.readString();
		title = in.readString();
		relese = in.readString();
		genre = in.readString();
		info = in.readString();
		url = in.readString();
		artworkPath = in.readString();
		thumbPath = in.readString();
		urlSearch = in.readString();
		id = in.readString();
		length = in.readInt();
		currentPos = in.readInt();
		artistId = in.readString();
		channelName = in.readString();

		name = in.readString();
		nameEn = in.readString();
		titleEn = in.readString();
		description = in.readString();
		descriptionEn = in.readString();
		previewLink = in.readString();
		externalLink1 = in.readString();
		stationName = in.readString();
		stationNameEn = in.readString();
        showDuration = in.readString();
        showJackets = new ArrayList<>();
        in.readStringList(showJackets);
	}

}
