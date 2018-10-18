package jp.faraopro.play.mclient;

import jp.faraopro.play.R;

/**
 * Post Action (Result/XML) 関連定義群
 * 
 * @author AIM Corporation
 * 
 */
public class MCDefResult {

	/**
	 * 結果アイテム(ID)のbase
	 */
	public static final int MCR_KIND_ITEM_BASE_ID = 1000;
	/**
	 * 結果アイテム(NAME)のbase
	 */
	public static final int MCR_KIND_ITEM_BASE_NAME = 1001;
	/**
	 * 結果アイテム(NAME_EN)のbase
	 */
	public static final int MCR_KIND_ITEM_BASE_NAME_EN = 1002;
	/**
	 * 不明・Default
	 */
	public static final int MCR_KIND_UNKNOWN = 0;
	/**
	 * 種別ID：エンコード（メソッド）
	 */
	public static final int MCR_KIND_ENCMETHOD = 1;
	/**
	 * 種別ID：エンコード（キー）
	 */
	public static final int MCR_KIND_ENCKEY = 2;
	/**
	 * 種別ID：エンコード（IV）
	 */
	public static final int MCR_KIND_ENCIV = 3;
	/**
	 * 種別ID：エンコード（ApiSig） for test
	 */
	public static final int MCR_KIND_API_SIG = 714;
	/**
	 * 種別ID：トラックアイテムリスト(playlist)
	 */
	public static final int MCR_KIND_TRACK_LIST = 4;
	/**
	 * 種別ID：チャンネルアイテムリスト(genre)
	 */
	public static final int MCR_KIND_GENRE_LIST = 5; // 2011/11/10
														// 仕様変更
	/**
	 * 種別ID：チャンネルアイテムリスト(channel)
	 */
	public static final int MCR_KIND_CHANNEL_LIST = 6; // 2011/11/10
														// 仕様変更
	/**
	 * 種別ID：メッセージ
	 */
	public static final int MCR_KIND_MESSAGE = 7;
	/**
	 * 種別ID：ActivationKey
	 */
	public static final int MCR_KIND_ACTIVATIONKEY = 8;
	/**
	 * 種別ID：SessionKey
	 */
	public static final int MCR_KIND_SESSIONKEY = 9;
	/**
	 * 種別ID：広告の出現頻度
	 */
	public static final int MCR_KIND_ADVERTISERATIO = 10;
	/**
	 * 種別ID：残スキップ回数
	 */
	public static final int MCR_KIND_SKIPREMAINING = 11;
	/**
	 * 種別ID：エンコード情報見出し(ヘッダー情報抽出)
	 */
	public static final int MCR_KIND_XENC = 12;

	/**
	 * 種別ID：トラックアイテムメンバー（ID/playlist）
	 */
	public static final int MCR_KIND_TRACKITEM_ID = MCR_KIND_ITEM_BASE_ID;
	/**
	 * 種別ID：トラックアイテムメンバー（Title/playlist）
	 */
	public static final int MCR_KIND_TRACKITEM_TITLE = 14;
	/**
	 * 種別ID：トラックアイテムメンバー（Title/playlist）
	 */
	public static final int MCR_KIND_TRACKITEM_ARTIST = 15;

	/**
	 * 種別ID：トラックアイテムメンバー（ID/genreItems）
	 */
	public static final int MCR_KIND_GENREITEM_ID = MCR_KIND_ITEM_BASE_ID;
	/**
	 * 種別ID：トラックアイテムメンバー（Name/genreItems）
	 */
	public static final int MCR_KIND_GENREITEM_NAME = MCR_KIND_ITEM_BASE_NAME;

	/**
	 * 種別ID：トラックアイテムメンバー（ID/personalItems）
	 */
	public static final int MCR_KIND_CHANNELITEM_ID = MCR_KIND_ITEM_BASE_ID;
	/**
	 * 種別ID：トラックアイテムメンバー（Name/personalItems）
	 */
	public static final int MCR_KIND_CHANNELITEM_NAME = MCR_KIND_ITEM_BASE_NAME;
	/**
	 * 種別ID：トラックアイテムメンバー（Lock/personalItems）
	 */
	public static final int MCR_KIND_CHANNELITEM_LOCK = 16;

	// 2011/11/10追加
	/**
	 * 種別ID：ユーザーアカウントの状態
	 */
	public static final int MCR_KIND_USER_STATUS = 20;
	/**
	 * 種別ID：ユーザーアカウントの課金状態
	 */
	public static final int MCR_KIND_SUBSCRIPTION_TYPE = 21;
	/**
	 * 種別ID：ユーザーアカウントの当月残再生回数
	 */
	public static final int MCR_KIND_PLAYABLE_TRACKS = 22;
	/**
	 * 種別ID：トラックアイテムメンバー（Title-EN/playlist）
	 */
	public static final int MCR_KIND_TRACKITEM_TITLE_EN = 23;
	/**
	 * 種別ID：トラックアイテムメンバー（ARTIST-EN/playlist）
	 */
	public static final int MCR_KIND_TRACKITEM_ARTIST_EN = 24;
	/**
	 * 種別ID：トラックアイテムメンバー（Relase-date/playlist）
	 */
	public static final int MCR_KIND_TRACKITEM_RELEASE_DATE = 25;
	/**
	 * 種別ID：トラックアイテムメンバー（genre/playlist）
	 */
	public static final int MCR_KIND_TRACKITEM_GENRE = 26;
	/**
	 * 種別ID：トラックアイテムメンバー（genre_en/playlist）
	 */
	public static final int MCR_KIND_TRACKITEM_GENRE_EN = 27;
	/**
	 * 種別ID：トラックアイテムメンバー（description/playlist）
	 */
	public static final int MCR_KIND_TRACKITEM_DESCRIPTION = 28;
	/**
	 * 種別ID：トラックアイテムメンバー（description_en/playlist）
	 */
	public static final int MCR_KIND_TRACKITEM_DESCRIPTION_EN = 29;
	/**
	 * 種別ID：トラックアイテムメンバー（affiliate_url/playlist）
	 */
	public static final int MCR_KIND_TRACKITEM_URL = 30;
	/**
	 * 種別ID：チャンネルアイテム(genre)メンバー（Name_EN/genre）
	 */
	public static final int MCR_KIND_GENREITEM_NAME_EN = MCR_KIND_ITEM_BASE_NAME_EN;
	/**
	 * 種別ID：チャンネルアイテム(channel)メンバー（Name_EN/channel）
	 */
	public static final int MCR_KIND_CHANNELITEM_NAME_EN = MCR_KIND_ITEM_BASE_NAME_EN;
	/**
	 * 種別ID：チャンネルアイテムリスト(chart)
	 */
	public static final int MCR_KIND_CHART_LIST = 32;
	/**
	 * 種別ID：チャンネルアイテム(chart)メンバー（ID/chart）
	 */
	public static final int MCR_KIND_CHARTITEM_ID = MCR_KIND_ITEM_BASE_ID;
	/**
	 * 種別ID：チャンネルアイテム(chart)メンバー（NAME/chart）
	 */
	public static final int MCR_KIND_CHARTITEM_NAME = MCR_KIND_ITEM_BASE_NAME;
	/**
	 * 種別ID：チャンネルアイテム(chart)メンバー（NAME_EN/chart）
	 */
	public static final int MCR_KIND_CHARTITEM_NAME_EN = MCR_KIND_ITEM_BASE_NAME_EN;
	/**
	 * 種別ID：検索アイテムリスト(item-search)
	 */
	public static final int MCR_KIND_SEARCH_LIST = 36;
	/**
	 * 種別ID：検索アイテム(item-search)メンバー（ID/item-search）
	 */
	public static final int MCR_KIND_SEARCHITEM_ID = MCR_KIND_ITEM_BASE_ID;
	/**
	 * 種別ID：検索アイテム(item-search)メンバー（NAME/item-search）
	 */
	public static final int MCR_KIND_SEARCHITEM_NAME = MCR_KIND_ITEM_BASE_NAME;
	/**
	 * 種別ID：検索アイテム(item-search)メンバー（NAME_EN/item-search）
	 */
	public static final int MCR_KIND_SEARCHITEM_NAME_EN = MCR_KIND_ITEM_BASE_NAME_EN;
	/**
	 * 種別ID：メッセージ(EN)
	 */
	public static final int MCR_KIND_MESSAGE_EN = 40;
	/**
	 * 種別ID：広告関連アイテムリスト(item)
	 */
	public static final int MCR_KIND_AD_LIST = 41;
	/**
	 * 種別ID：広告関連アイテム(item)メンバー（ID/item）
	 */
	public static final int MCR_KIND_ADITEM_ID = 46;
	/**
	 * 種別ID：広告関連アイテム(item)メンバー（title/item）
	 */
	public static final int MCR_KIND_ADITEM_TITLE = MCR_KIND_TRACKITEM_TITLE;
	/**
	 * 種別ID：広告関連アイテム(item)メンバー（title_en/item）
	 */
	public static final int MCR_KIND_ADITEM_TITLE_EN = MCR_KIND_TRACKITEM_TITLE_EN;
	/**
	 * 種別ID：広告関連アイテム(item)メンバー（description/item）
	 */
	public static final int MCR_KIND_ITEM_DESCRIPTION = MCR_KIND_TRACKITEM_DESCRIPTION;
	/**
	 * 種別ID：広告関連アイテム(item)メンバー（description_en/item）
	 */
	public static final int MCR_KIND_ITEM_DESCRIPTION_EN = MCR_KIND_TRACKITEM_DESCRIPTION_EN;
	/**
	 * 種別ID：広告関連アイテム(item)メンバー（url_click/item）
	 */
	public static final int MCR_KIND_ADITEM_URL = 45;
	/**
	 * 種別ID : トラックアイテムメンバー（jacketID/playlist）
	 */
	public static final int MCR_KIND_TRACKITEM_JACKET_ID = 42;
	/**
	 * 種別ID : トラックアイテムメンバー（album/playlist）
	 */
	public static final int MCR_KIND_TRACKITEM_ALBUM = 43;
	/**
	 * 種別ID : トラックアイテムメンバー（album_en/playlist）
	 */
	public static final int MCR_KIND_TRACKITEM_ALBUM_EN = 44;
	/**
	 * 種別ID：広告関連アイテム(item)メンバー（sponsor/item）
	 */
	public static final int MCR_KIND_ADITEM_SPONSOR = 47;
	/**
	 * 種別ID：広告関連アイテム(item)メンバー（sponsor_en/item）
	 */
	public static final int MCR_KIND_ADITEM_SPONSOR_EN = 48;
	/**
	 * 種別ID：特典アイテムメンバー（content_title/item）
	 */
	public static final int MCR_KIND_ITEM_CONTENT_TITLE = 49;
	/**
	 * 種別ID：特典アイテムメンバー（content_title_en/item）
	 */
	public static final int MCR_KIND_ITEM_CONTENT_TITLE_EN = 50;
	/**
	 * 種別ID：特典アイテムメンバー（content_text/item）
	 */
	public static final int MCR_KIND_ITEM_CONTENT_TEXT = 51;
	/**
	 * 種別ID：特典アイテムメンバー（content_text_en/item）
	 */
	public static final int MCR_KIND_ITEM_CONTENT_TEXT_EN = 52;
	/**
	 * 種別ID：特典アイテムメンバー（thumb_icon/item）
	 */
	public static final int MCR_KIND_ITEM_THUMB_ICON = 53;
	/**
	 * 種別ID：特典アイテムメンバー（delivery_begin/item）
	 */
	public static final int MCR_KIND_BENIFITITEM_DELIVERY_BEGIN = 54;
	/**
	 * 種別ID：特典アイテムメンバー（delivery_end/item）
	 */
	public static final int MCR_KIND_BENIFITITEM_DELIVERY_END = 55;
	/**
	 * 種別ID：特典アイテムメンバー（skip_control/item）
	 */
	public static final int MCR_KIND_BENIFITITEM_SKIP_CONTROL = 56;
	/**
	 * 種別ID：特典アイテムメンバー（ad_control/item）
	 */
	public static final int MCR_KIND_BENIFITITEM_AD_CONTROL = 57;
	/**
	 * 種別ID：特典アイテムメンバー（series_no/item）
	 */
	public static final int MCR_KIND_ITEM_SERIES_NO = 58;
	/**
	 * 種別ID：特典アイテムメンバー（series_content_no/item）
	 */
	public static final int MCR_KIND_ITEM_SERIES_CONTENT_NO = 59;
	/**
	 * 種別ID：チャンネルアイテムリスト(benifit)
	 */
	public static final int MCR_KIND_BENIFIT_LIST = 60;

	/**
	 * サーバーから受信するHTTPヘッダーのステータスコード
	 */
	public static final int MCR_KIND_STATUS_CODE = 80;

	/**
	 * リスト締結判断用（MCR_KIND_TRACK_LIST用）
	 */
	public static final int MCR_KIND_TRACK_LIST_PARENTS = 90;
	/**
	 * リスト締結判断用（MCR_KIND_GENRE_LIST用）
	 */
	public static final int MCR_KIND_GENRE_LIST_PARENTS = 91;
	/**
	 * リスト締結判断用（MCR_KIND_CHANNEL_LIST用）
	 */
	public static final int MCR_KIND_CHANNEL_LIST_PARENTS = 92;
	/**
	 * 特別：ResultからDLコンテンツのパスを取得するためのKEY
	 */
	public static final int MCR_KIND_DL_PATH = 93;

	// 2011/11/10 追加
	/**
	 * リスト締結判断用（MCR_KIND_SEARCH_LIST用）
	 */
	public static final int MCR_KIND_SEARCH_LIST_PARENTS = 94;
	/**
	 * リスト締結判断用（MCR_KIND_AD_LIST用）
	 */
	public static final int MCR_KIND_AD_LIST_PARENTS = 95;
	/**
	 * リスト締結判断用（MCR_KIND_CHART_LIST用）
	 */
	public static final int MCR_KIND_CHART_LIST_PARENTS = 96;
	/**
	 * 種別ID：ユーザーアカウントの課金期間状態
	 */
	public static final int MCR_KIND_SUBSCRIPTION_STATUS = 97;
	/**
	 * 種別ID：ユーザーアカウントのトラッキングキー
	 */
	public static final int MCR_KIND_TRACKING_KEY = 98;
	/**
	 * 種別ID：チャンネルアイテム(chart)メンバー（ROYALTY/chart)
	 */
	public static final int MCR_KIND_CHARTITEM_ROYALTY = 99;
	/**
	 * 種別ID：課金アイテム(payment)メンバー（TYPE/payment)
	 */
	public static final int MCR_KIND_PAYMENT_TYPE = 100;
	/**
	 * 種別ID：課金アイテム(payment)メンバー（ID/payment)
	 */
	public static final int MCR_KIND_PAYMENT_ID = 101;
	/**
	 * 種別ID：チャンネルアイテムメンバー（TIMESTAMP）
	 */
	public static final int MCR_KIND_CHANNELITEM_TIMESTAMP = 102;
	/**
	 * 種別ID：チャンネルアイテムメンバー（MIN/years）
	 */
	public static final int MCR_KIND_CHANNELITEM_YEARS_MIN = 103;
	/**
	 * 種別ID：チャンネルアイテムメンバー（MAX/years）
	 */
	public static final int MCR_KIND_CHANNELITEM_YEARS_MAX = 104;
	/**
	 * 種別ID：チャンネルシェアアイテム（shareKey）
	 */
	public static final int MCR_KIND_SHAREITEM_SHARE_KEY = 105;
	/**
	 * 種別ID：チャンネルシェアアイテム（shareId）
	 */
	public static final int MCR_KIND_SHAREITEM_SHARE_ID = 106;
	/**
	 * 種別ID：チャンネルシェアアイテム（shareName）
	 */
	public static final int MCR_KIND_SHAREITEM_SHARE_NAME = MCR_KIND_ITEM_BASE_NAME;
	/**
	 * 種別ID：チャンネルシェアアイテム（shareNameEn）
	 */
	public static final int MCR_KIND_SHAREITEM_SHARE_NAME_EN = MCR_KIND_ITEM_BASE_NAME_EN;
	/**
	 * 種別ID：トラックアイテムメンバ(trialTime)
	 */
	public static final int MCR_KIND_TRACKITEM_TRIAL_TIME = 107;
	/**
	 * 種別ID：地域マスタメンバ(code)
	 */
	public static final int MCR_KIND_LOCATION_CODE = 108;
	/**
	 * 種別ID：地域マスタメンバ(name)
	 */
	public static final int MCR_KIND_LOCATION_NAME = MCR_KIND_ITEM_BASE_NAME;
	/**
	 * 種別ID：地域マスタメンバ(name_en)
	 */
	public static final int MCR_KIND_LOCATION_NAME_EN = MCR_KIND_ITEM_BASE_NAME_EN;
	/**
	 * 種別ID：地域マスタメンバ(country)
	 */
	public static final int MCR_KIND_LOCATION_COUNTRY = 111;
	/**
	 * 種別ID：地域マスタメンバ(region)
	 */
	public static final int MCR_KIND_LOCATION_REGION = 112;
	/**
	 * 種別ID：地域マスタアイテムリスト(location)
	 */
	public static final int MCR_KIND_LOCATION_LIST = 113;
	/**
	 * リスト締結判断用（MCR_KIND_LOCATION_LIST用）
	 */
	public static final int MCR_KIND_LOCATION_LIST_PARENTS = 114;
	/**
	 * リスト締結判断用（MCR_KIND_BENIFIT_LIST用）
	 */
	public static final int MCR_KIND_BENIFIT_LIST_PARENTS = 115;
	/**
	 * 種別ID：チケットアイテムリスト(campaign)
	 */
	public static final int MCR_KIND_CAMPAIGN_LIST = 116;
	/**
	 * 種別ID：チケットメンバ
	 */
	public static final int MCR_KIND_CAMPAIGN_ID = 117;
	/**
	 * 種別ID：チケットメンバ
	 */
	public static final int MCR_KIND_CAMPAIGN_NAME = MCR_KIND_ITEM_BASE_NAME;
	/**
	 * 種別ID：チケットメンバ
	 */
	public static final int MCR_KIND_CAMPAIGN_NAME_EN = MCR_KIND_ITEM_BASE_NAME_EN;
	/**
	 * 種別ID：チケットメンバ
	 */
	public static final int MCR_KIND_VALID_BEGIN = 118;
	/**
	 * 種別ID：チケットメンバ
	 */
	public static final int MCR_KIND_VALID_END = 119;
	/**
	 * リスト締結判断用（MCR_KIND_CAMPAIGN_LIST用）
	 */
	public static final int MCR_KIND_CAMPAIGN_LIST_PARENTS = 120;
	/**
	 * リスト締結判断用（MCR_KIND_SHUFFLE_LIST用）
	 */
	public static final int MCR_KIND_SHUFFLE_LIST_PARENTS = 121;
	/**
	 * 種別ID：チケットアイテムリスト(shuffle)
	 */
	public static final int MCR_KIND_SHUFFLE_LIST = 122;
	/**
	 * 種別ID：トラックアイテムメンバ(trialTime)
	 */
	public static final int MCR_KIND_TRACKITEM_ARTIST_ID = 123;
	/**
	 * 種別ID：チャンネル名
	 */
	public static final int MCR_KIND_CHANNEL_NAME = 124;
	/**
	 * 種別ID：チャンネル名
	 */
	public static final int MCR_KIND_CHANNEL_NAME_EN = 125;

	public static final int MCR_KIND_LATITUDE = 126;
	public static final int MCR_KIND_LONGITUDE = 127;
	public static final int MCR_KIND_DISTANCE = 128;
	public static final int MCR_KIND_INDUSTRY_CODE = 129;
	public static final int MCR_KIND_INDUSTRY_NAME = 130;
	public static final int MCR_KIND_INDUSTRY_NAME_EN = 131;
	public static final int MCR_KIND_SHOP_NAME = 132;
	public static final int MCR_KIND_SHOP_NAME_EN = 133;
	public static final int MCR_KIND_ZIP_CODE = 134;
	public static final int MCR_KIND_PROVINCE_CODE = 135;
	public static final int MCR_KIND_PROVINCE_NAME = 136;
	public static final int MCR_KIND_PROVINCE_NAME_EN = 137;
	public static final int MCR_KIND_LOCATION_EN = 138;
	public static final int MCR_KIND_CONTACT1 = 139;
	public static final int MCR_KIND_CONTACT2 = 140;
	public static final int MCR_KIND_EXTERNAL_LINK1 = 141;
	public static final int MCR_KIND_EXTERNAL_LINK2 = 142;
	public static final int MCR_KIND_EXTERNAL_LINK3 = 143;
	public static final int MCR_KIND_EXTERNAL_LINK4 = 144;
	public static final int MCR_KIND_EXTERNAL_LINK5 = 145;
	public static final int MCR_KIND_SHOP_LIST_PARENTS = 146;
	public static final int MCR_KIND_SHOP_LIST = 147;

	public static final int MCR_KIND_ITEM_TYPE = 148;
	public static final int MCR_KIND_ITEM_USER = 149;
	public static final int MCR_KIND_ITEM_SECRET = 150;
	public static final int MCR_KIND_ITEM_ENC = 151;
	public static final int MCR_KIND_ITEM_METHOD = 152;
	public static final int MCR_KIND_ITEM_KEY = 153;
	public static final int MCR_KIND_ITEM_IV = 154;

	// additional number
	public static final int MCR_ADD_NUMBER_TRACKS = 125;
	public static final int MCR_ADD_NUMBER_JACKETS = 126;

	public static final int MCR_KIND_ITEM_CDN_TRACK_URL = MCR_KIND_TRACKITEM_URL + MCR_ADD_NUMBER_TRACKS;
	public static final int MCR_KIND_ITEM_CDN_JACKET_URL = MCR_KIND_TRACKITEM_URL + MCR_ADD_NUMBER_JACKETS;

	public static final int MCR_KIND_CDN_LIST = 157;
	public static final int MCR_KIND_ITEM_SIGNATURE = 158;
	public static final int MCR_KIND_ITEM_PARENT = 159;
	public static final int MCR_KIND_ITEM_NODE_TYPE = 160;
	public static final int MCR_KIND_BUSINESS_LIST = 161;
	public static final int MCR_KIND_BUSINESS_LIST_PARENTS = 162;

	public static final int MCR_KIND_TRACKING_INTERVAL = 163;
	public static final int MCR_KIND_INSTALL_LOCATION = 164;
	public static final int MCR_KIND_TRACKING_LOCATION = 165;
	public static final int MCR_KIND_PROXIMITY = 166;

	public static final int MCR_KIND_TEMPLATE_TYPE = 167;
	public static final int MCR_KIND_TEMPLATE_ID = 168;
	public static final int MCR_KIND_TEMPLATE_LIST = 169;
	public static final int MCR_KIND_TEMPLATE_LIST_PARENTS = 170;

	public static final int MCR_KIND_TIMETABLE_LIST_PARENTS = 171;
	public static final int MCR_KIND_TIMETABLE_LIST = 172;
	public static final int MCR_KIND_TIMER_TYPE = 173;
	public static final int MCR_KIND_TIMER_RULE = 174;
	public static final int MCR_KIND_SOURCE_TYPE = 175;
	public static final int MCR_KIND_SOURCE_NAME = 176;
	public static final int MCR_KIND_SOURCE_URL = 177;
	public static final int MCR_KIND_BOOKMARK_LIST_PARENTS = 178;
	public static final int MCR_KIND_BOOKMARK_LIST = 179;

	/*********************** SMP用のため未使用 ***********************/
	public static final int MCR_KIND_THUMBNAIL_ICON_SMALL = 180;
	public static final int MCR_KIND_THUMBNAIL_ICON_LARGE = 181;
	public static final int MCR_KIND_CAMPAIGN_REMAINING = 182;
	public static final int MCR_KIND_CAMPAIGN_BEGIN = 183;
	public static final int MCR_KIND_CAMPAIGN_END = 184;
	public static final int MCR_KIND_TIMELIMIT_CONTROL = 185;
	/*********************** SMP用のため未使用 ***********************/

	public static final int MCR_KIND_MEDIA_TYPE = 186;
	public static final int MCR_KIND_SESSION_TYPE = 187;
	public static final int MCR_KIND_CONTENT_LINK = 188;
	public static final int MCR_KIND_PREVIEW_LINK = 189;
	public static final int MCR_KIND_STATION_ID = 190;
	public static final int MCR_KIND_STATION_NAME = 191;
	public static final int MCR_KIND_STATION_NAME_EN = 192;
	public static final int MCR_KIND_STREAM_LIST = 193;
	public static final int MCR_KIND_STREAM_LIST_PARENTS = 194;
	public static final int MCR_KIND_PLAYING_LIST = 195;
	public static final int MCR_KIND_STREAM_ID = 196;
	public static final int MCR_KIND_REPLAY_GAIN = 197;
	public static final int MCR_KIND_TEMPLATE_DIGEST = 198;
	public static final int MCR_KIND_TEMPLATE_ACTION = 199;
	public static final int MCR_KIND_TEMPLATE_RULE = 200;

	public static final int MCR_KIND_PATTERN_RESPONSE = 201;
	public static final int MCR_KIND_SCHEDULE_RULE = 202;
	public static final int MCR_KIND_SCHEDULE_PERIOD = 203;
	public static final int MCR_KIND_PATTERN_ID = 204;
	public static final int MCR_KIND_PATTERN_DIGEST = 205;
	public static final int MCR_KIND_FRAME = 206;
	public static final int MCR_KIND_ONAIR_TIME = 207;
	public static final int MCR_KIND_PATTERN_AUDIO = 208;
	public static final int MCR_KIND_AUDIO_ID = 209;
	public static final int MCR_KIND_AUDIO_VERSION = 210;
	public static final int MCR_KIND_AUDIO_TIME = 211;
	public static final int MCR_KIND_AUDIO_URL = MCR_KIND_TRACKITEM_URL;
	public static final int MCR_KIND_FRAMES = 212;
	public static final int MCR_KIND_FRAME_ID = 213;
	public static final int MCR_KIND_TARGET = 214;

	public static final int MCR_KIND_SCHEDULE_MASK = 215;

	public static final int MCR_KIND_SHOW_DURATION = 216;
	public static final int MCR_KIND_SHOW_JACKETS = 217;
	public static final int MCR_KIND_SHOW_JACKET = 218;

	public static final int MCR_KIND_SERVICE_TYPE = 219;
	public static final int MCR_KIND_SERVICE_LEVEL = 220;
	public static final int MCR_KIND_FEATURES = 221;
	public static final int MCR_KIND_PERMISSIONS = 222;
	public static final int MCR_KIND_MARKET_TYPE = 223;
	public static final int MCR_KIND_EXPIRES_DATE = 224;
	public static final int MCR_KIND_EXPIRES_LIMIT = 225;

	/**
	 * 不明・Default
	 */
	public static final String MCR_TAG_UNKNOWN = "UNKNOWN";
	/**
	 * TAG(ヘッダー情報抽出)：エンコード情報見出し
	 */
	public static final String MCR_TAG_XENC = "X-ENC";
	/**
	 * TAG(ヘッダー情報抽出)：エンコード（メソッド）
	 */
	public static final String MCR_TAG_ENCMETHOD = "METHOD";
	/**
	 * TAG(ヘッダー情報抽出)：エンコード（キー）
	 */
	public static final String MCR_TAG_ENCKEY = "KEY";
	/**
	 * TAG(ヘッダー情報抽出)：エンコード（IV）
	 */
	public static final String MCR_TAG_ENCIV = "IV";
	/**
	 * TAG：トラックアイテムリスト
	 */
	public static final String MCR_TAG_TRACK_LIST = "track";
	/**
	 * TAG：チャンネルアイテムリスト
	 */
	public static final String MCR_TAG_GENRE_LIST = "genre";
	/**
	 * TAG：チャンネル(パーソナル)アイテムリスト
	 */
	public static final String MCR_TAG_CHANNEL_LIST = "channel";
	/**
	 * TAG：チャンネルアイテムリスト
	 */
	public static final String MCR_TAG_BENIFIT_LIST = "benefit";
	/**
	 * TAG：メッセージ
	 */
	public static final String MCR_TAG_MESSAGE = "message";
	/**
	 * TAG：ActivationKey
	 */
	public static final String MCR_TAG_ACTIVATIONKEY = "activationKey";
	/**
	 * TAG：SessionKey
	 */
	public static final String MCR_TAG_SESSIONKEY = "sessionKey";
	/**
	 * TAG：広告の出現頻度
	 */
	public static final String MCR_TAG_ADVERTISERATIO = "advertiseRatio";
	/**
	 * TAG：残スキップ回数
	 */
	public static final String MCR_TAG_SKIPREMAINING = "skipRemaining";

	/**
	 * TAG：トラックアイテムメンバー（ID/playlist）
	 */
	public static final String MCR_TAG_TRACKITEM_ID = "trackId";
	/**
	 * TAG：トラックアイテムメンバー（Title/playlist）
	 */
	public static final String MCR_TAG_TRACKITEM_TITLE = "title";
	/**
	 * TAG：トラックアイテムメンバー（Title/playlist）
	 */
	public static final String MCR_TAG_TRACKITEM_ARTIST = "artist";

	/**
	 * TAG：トラックアイテムメンバー（ID/genreItems）
	 */
	public static final String MCR_TAG_GENREITEM_ID = "id";
	/**
	 * TAG：トラックアイテムメンバー（Name/genreItems）
	 */
	public static final String MCR_TAG_GENREITEM_NAME = "name";

	/**
	 * TAG：トラックアイテムメンバー（ID/personalItems）
	 */
	public static final String MCR_TAG_CHANNELITEM_LOCK = "lock";
	/**
	 * TAG：トラックアイテムメンバー（Name/personalItems）
	 */
	public static final String MCR_TAG_CHANNELITEM_NAME = "name";

	/**
	 * リスト締結判断用（MCR_KIND_TRACK_LIST用）
	 */
	public static final String MCR_TAG_TRACK_LIST_PARENTS = "playlist";
	/**
	 * リスト締結判断用（MCR_KIND_GENRE_LIST用）
	 */
	public static final String MCR_TAG_GENRE_LIST_PARENTS = "genres"; // 2011/11/10
																		// 仕様変更
	/**
	 * リスト締結判断用（MCR_KIND_CHANNEL_LIST用）
	 */
	public static final String MCR_TAG_CHANNEL_LIST_PARENTS = "channels"; // 2011/11/10
																			// 仕様変更

	// 2011/11/10追加
	/**
	 * 種別ID：ユーザーアカウントの状態
	 */
	public static final String MCR_TAG_USER_STATUS = "status";
	/**
	 * 種別ID：ユーザーアカウントの課金状態
	 */
	public static final String MCR_TAG_SUBSCRIPTION_TYPE = "subscriptionType";
	/**
	 * 種別ID：ユーザーアカウントの当月残再生回数
	 */
	public static final String MCR_TAG_SUBSCRIPTION_STATUS = "remainingTime";
	/**
	 * 種別ID：ユーザーアカウントの当月残再生回数
	 */
	public static final String MCR_TAG_PLAYABLE_TRACKS = "playableTracks";
	/**
	 * 種別ID：ユーザーアカウントの当月残再生回数
	 */
	public static final String MCR_TAG_TRACKING_KEY = "trackingKey";
	/**
	 * 種別ID：トラックアイテムメンバー（Title-EN/playlist）
	 */
	public static final String MCR_TAG_TRACKITEM_TITLE_EN = "title_en";
	/**
	 * 種別ID：トラックアイテムメンバー（ARTIST-EN/playlist）
	 */
	public static final String MCR_TAG_TRACKITEM_ARTIST_EN = "artist_en";
	/**
	 * 種別ID：トラックアイテムメンバー（Relase-date/playlist）
	 */
	public static final String MCR_TAG_TRACKITEM_RELEASE_DATE = "releaseDate";
	/**
	 * 種別ID：トラックアイテムメンバー（genre/playlist）
	 */
	public static final String MCR_TAG_TRACKITEM_GENRE = "genre";
	/**
	 * 種別ID：トラックアイテムメンバー（genre_en/playlist）
	 */
	public static final String MCR_TAG_TRACKITEM_GENRE_EN = "genre_en";
	/**
	 * 種別ID：トラックアイテムメンバー（description/playlist）
	 */
	public static final String MCR_TAG_TRACKITEM_DESCRIPTION = "description";
	/**
	 * 種別ID：トラックアイテムメンバー（description_en/playlist）
	 */
	public static final String MCR_TAG_TRACKITEM_DESCRIPTION_EN = "description_en";
	/**
	 * 種別ID：トラックアイテムメンバー（affiliate_url/playlist）
	 */
	public static final String MCR_TAG_TRACKITEM_URL = "url";
	/**
	 * 種別ID：チャンネルアイテム(genre)メンバー（Name_EN/genre）
	 */
	public static final String MCR_TAG_GENREITEM_NAME_EN = "name_en";
	/**
	 * 種別ID：チャンネルアイテムリスト(chart)
	 */
	public static final String MCR_TAG_CHART_LIST = "chart";
	/**
	 * 種別ID：チャンネルアイテム(chart)メンバー（ID/chart）
	 */
	public static final String MCR_TAG_CHARTITEM_ID = "id";
	/**
	 * 種別ID：チャンネルアイテム(chart)メンバー（NAME/chart）
	 */
	public static final String MCR_TAG_CHARTITEM_NAME = "name";
	/**
	 * 種別ID：チャンネルアイテム(chart)メンバー（NAME_EN/chart）
	 */
	public static final String MCR_TAG_CHARTITEM_NAME_EN = "name_en";
	/**
	 * 種別ID：検索アイテムリスト(item-search)
	 */
	public static final String MCR_TAG_SEARCH_LIST = "hit";
	/**
	 * 種別ID：検索アイテム(item-search)メンバー（ID/item-search）
	 */
	public static final String MCR_TAG_SEARCHITEM_ID = "id";
	/**
	 * 種別ID：検索アイテム(item-search)メンバー（NAME/item-search）
	 */
	public static final String MCR_TAG_SEARCHITEM_NAME = "name";
	/**
	 * 種別ID：検索アイテム(item-search)メンバー（NAME_EN/item-search）
	 */
	public static final String MCR_TAG_SEARCHITEM_NAME_EN = "name_en";
	/**
	 * 種別ID：メッセージ(EN)
	 */
	public static final String MCR_TAG_MESSAGE_EN = "message_en";
	/**
	 * 種別ID：広告関連アイテムリスト(item)
	 */
	public static final String MCR_TAG_AD_LIST = "audioAd";
	/**
	 * 種別ID：広告関連アイテム(item)メンバー（ID/item）
	 */
	public static final String MCR_TAG_ADITEM_ID = "adId";
	/**
	 * 種別ID：広告関連アイテム(item)メンバー（title/item）
	 */
	public static final String MCR_TAG_ADITEM_TITLE = MCR_TAG_TRACKITEM_TITLE;
	/**
	 * 種別ID：広告関連アイテム(item)メンバー（title_en/item）
	 */
	public static final String MCR_TAG_ADITEM_TITLE_EN = MCR_TAG_TRACKITEM_TITLE_EN;
	/**
	 * 種別ID：広告関連アイテム(item)メンバー（description/item）
	 */
	public static final String MCR_TAG_ITEM_DESCRIPTION = MCR_TAG_TRACKITEM_DESCRIPTION;
	/**
	 * 種別ID：広告関連アイテム(item)メンバー（description_en/item）
	 */
	public static final String MCR_TAG_ITEM_DESCRIPTION_EN = MCR_TAG_TRACKITEM_DESCRIPTION_EN;
	/**
	 * 種別ID：広告関連アイテム(item)メンバー（url_click/item）
	 */
	public static final String MCR_TAG_ADITEM_URL = "url_click";
	/**
	 * リスト締結判断用（MCR_KIND_CHART_LIST用）
	 */
	public static final String MCR_TAG_CHART_LIST_PARENTS = "charts";
	/**
	 * リスト締結判断用（MCR_KIND_SEARCH_LIST用）
	 */
	public static final String MCR_TAG_SEARCH_LIST_PARENTS = "hits";
	/**
	 * リスト締結判断用（MCR_KIND_AD_LIST用）
	 */
	public static final String MCR_TAG_AD_LIST_PARENTS = "audioAds";
	/**
	 * TAG：トラックアイテムメンバー（JACKET_ID/playlist）
	 */
	public static final String MCR_TAG_TRACKITEM_JACKET_ID = "jacketId";
	/**
	 * TAG：トラックアイテムメンバー（ALBUM/playlist）
	 */
	public static final String MCR_TAG_TRACKITEM_ALBUM = "album";
	/**
	 * TAG：トラックアイテムメンバー（ALBUM_EN/playlist）
	 */
	public static final String MCR_TAG_TRACKITEM_ALBUM_EN = "album_en";
	/**
	 * 種別ID：チャンネルアイテム(chart)メンバー（ROYALTY/channel）
	 */
	public static final String MCR_TAG_CHARTITEM_ROYALTY = "royalty";
	/**
	 * 種別ID：課金アイテム(payment)メンバー（TYPE/payment）
	 */
	public static final String MCR_TAG_PAYMENT_TYPE = "paymentType";
	/**
	 * 種別ID：課金アイテム(payment)メンバー（ID/payment)
	 */
	public static final String MCR_TAG_PAYMENT_ID = "paymentId";
	/**
	 * TAG：チャンネルアイテムメンバー（TIMESTAMP）
	 */
	public static final String MCR_TAG_CHANNELITEM_TIMESTAMP = "timestamp";
	/**
	 * TAG：チャンネルシェアメンバ(shareKey)
	 */
	public static final String MCR_TAG_SHAREITEM_SHARE_KEY = "shareKey";
	/**
	 * TAG：チャンネルシェアメンバ(shareId)
	 */
	public static final String MCR_TAG_SHAREITEM_SHARE_ID = "shareId";
	/**
	 * TAG：チャンネルシェアメンバ(shareName)
	 */
	public static final String MCR_TAG_SHAREITEM_SHARE_NAME = "name";
	/**
	 * TAG：トラックアイテムメンバ(trialTime)
	 */
	public static final String MCR_TAG_TRACKITEM_TRIAL_TIME = "trialTime";
	/**
	 * TAG：地域マスタメンバ(code)
	 */
	public static final String MCR_TAG_LOCATION_CODE = "code";
	/**
	 * TAG：地域マスタメンバ(name)
	 */
	public static final String MCR_TAG_LOCATION_NAME = MCR_TAG_GENREITEM_NAME;
	/**
	 * TAG：地域マスタメンバ(name_en)
	 */
	public static final String MCR_TAG_LOCATION_NAME_EN = MCR_TAG_GENREITEM_NAME_EN;
	/**
	 * TAG：地域マスタメンバ(country)
	 */
	public static final String MCR_TAG_LOCATION_COUNTRY = "country";
	/**
	 * TAG：地域マスタメンバ(code)
	 */
	public static final String MCR_TAG_LOCATION_REGION = "region";
	/**
	 * TAG：地域マスタアイテムリスト
	 */
	public static final String MCR_TAG_LOCATION_LIST = "location";
	/**
	 * リスト締結判断用（MCR_KIND_LOCATION_LIST用）
	 */
	public static final String MCR_TAG_LOCATION_LIST_PARENTS = "locations";
	/**
	 * リスト締結判断用（MCR_KIND_BENIFIT_LIST用）
	 */
	public static final String MCR_TAG_BENIFIT_LIST_PARENTS = "benefits";
	/**
	 * 種別ID：広告関連アイテム(item)メンバー（sonsor/item）
	 */
	public static final String MCR_TAG_ADITEM_SPONSOR = "sponsor";
	/**
	 * 種別ID：広告関連アイテム(item)メンバー（sponsor_en/item）
	 */
	public static final String MCR_TAG_ADITEM_SPONSOR_EN = "sponsor_en";
	/**
	 * 種別ID：特典アイテムメンバー（content_title/item）
	 */
	public static final String MCR_TAG_ITEM_CONTENT_TITLE = "content_title";
	/**
	 * 種別ID：特典アイテムメンバー（content_title_en/item）
	 */
	public static final String MCR_TAG_ITEM_CONTENT_TITLE_EN = "content_title_en";
	/**
	 * 種別ID：特典アイテムメンバー（content_text/item）
	 */
	public static final String MCR_TAG_ITEM_CONTENT_TEXT = "content_text";
	/**
	 * 種別ID：特典アイテムメンバー（content_text_en/item）
	 */
	public static final String MCR_TAG_ITEM_CONTENT_TEXT_EN = "content_text_en";
	/**
	 * 種別ID：特典アイテムメンバー（thumb_icon/item）
	 */
	public static final String MCR_TAG_ITEM_THUMB_ICON = "thumb_icon";
	/**
	 * 種別ID：特典アイテムメンバー（delivery_begin/item）
	 */
	public static final String MCR_TAG_BENIFITITEM_DELIVERY_BEGIN = "delivery_begin";
	/**
	 * 種別ID：特典アイテムメンバー（delivery_end/item）
	 */
	public static final String MCR_TAG_BENIFITITEM_DELIVERY_END = "delivery_end";
	/**
	 * 種別ID：特典アイテムメンバー（skip_control/item）
	 */
	public static final String MCR_TAG_BENIFITITEM_SKIP_CONTROL = "skip_control";
	/**
	 * 種別ID：特典アイテムメンバー（ad_control/item）
	 */
	public static final String MCR_TAG_BENIFITITEM_AD_CONTROL = "ad_control";
	/**
	 * 種別ID：特典アイテムメンバー（series_no/item）
	 */
	public static final String MCR_TAG_ITEM_SERIES_NO = "series_no";
	/**
	 * 種別ID：特典アイテムメンバー（series_content_no/item）
	 */
	public static final String MCR_TAG_ITEM_SERIES_CONTENT_NO = "series_content_no";
	/**
	 * 種別ID：チケットアイテムリスト(campaign)
	 */
	public static final String MCR_TAG_CAMPAIGN_LIST = "campaign";
	/**
	 * 種別ID：チケットメンバ
	 */
	public static final String MCR_TAG_CAMPAIGN_ID = "campaignId";
	/**
	 * 種別ID：チケットメンバ
	 */
	public static final String MCR_TAG_CAMPAIGN_NAME = MCR_TAG_GENREITEM_NAME;
	/**
	 * 種別ID：チケットメンバ
	 */
	public static final String MCR_TAG_CAMPAIGN_NAME_EN = MCR_TAG_GENREITEM_NAME_EN;
	/**
	 * 種別ID：チケットメンバ
	 */
	public static final String MCR_TAG_VALID_BEGIN = "valid_begin";
	/**
	 * 種別ID：チケットメンバ
	 */
	public static final String MCR_TAG_VALID_END = "valid_end";
	/**
	 * リスト締結判断用（MCR_KIND_CAMPAIGN_LIST用）
	 */
	public static final String MCR_TAG_CAMPAIGN_LIST_PARENTS = "campaigns";
	/**
	 * リスト締結判断用（MCR_KIND_SHUFFLE_LIST用）
	 */
	public static final String MCR_TAG_SHUFFLE_LIST_PARENTS = "shuffles";
	/**
	 * 種別ID：チケットアイテムリスト(shuffle)
	 */
	public static final String MCR_TAG_SHUFFLE_LIST = "shuffle";
	/**
	 * TAG：トラックアイテムメンバー（artistId）
	 */
	public static final String MCR_TAG_TRACKITEM_ARTIST_ID = "artistId";
	/**
	 * TAG：残スキップ回数
	 */
	public static final String MCR_TAG_CHANNEL_NAME = "channelName";
	/**
	 * TAG：残スキップ回数
	 */
	public static final String MCR_TAG_CHANNEL_NAME_EN = "channelName_en";

	public static final String MCR_TAG_LATITUDE = "latitude";
	public static final String MCR_TAG_LONGITUDE = "longitude";
	public static final String MCR_TAG_DISTANCE = "distance";
	public static final String MCR_TAG_INDUSTRY_CODE = "industryCode";
	public static final String MCR_TAG_INDUSTRY_NAME = "industryName";
	public static final String MCR_TAG_INDUSTRY_NAME_EN = "industryName_en";
	public static final String MCR_TAG_SHOP_NAME = "shopName";
	public static final String MCR_TAG_SHOP_NAME_EN = "shopName_en";
	public static final String MCR_TAG_ZIP_CODE = "zipCode";
	public static final String MCR_TAG_PROVINCE_CODE = "provinceCode";
	public static final String MCR_TAG_PROVINCE_NAME = "provinceName";
	public static final String MCR_TAG_PROVINCE_NAME_EN = "provinceName_en";
	public static final String MCR_TAG_LOCATION_EN = "location_en";
	public static final String MCR_TAG_CONTACT1 = "contact1";
	public static final String MCR_TAG_CONTACT2 = "contact2";
	public static final String MCR_TAG_EXTERNAL_LINK1 = "externalLink1";
	public static final String MCR_TAG_EXTERNAL_LINK2 = "externalLink2";
	public static final String MCR_TAG_EXTERNAL_LINK3 = "externalLink3";
	public static final String MCR_TAG_EXTERNAL_LINK4 = "externalLink4";
	public static final String MCR_TAG_EXTERNAL_LINK5 = "externalLink5";
	public static final String MCR_TAG_SHOP_LIST_PARENTS = "shops";
	public static final String MCR_TAG_SHOP_LIST = "shop";

	public static final String MCR_TAG_ITEM_TYPE = "type";
	public static final String MCR_TAG_ITEM_USER = "user";
	public static final String MCR_TAG_ITEM_SECRET = "secret";
	public static final String MCR_TAG_ITEM_ENC = "enc";
	public static final String MCR_TAG_ITEM_METHOD = "method";
	public static final String MCR_TAG_ITEM_KEY = "key";
	public static final String MCR_TAG_ITEM_IV = "iv";
	public static final String MCR_TAG_CDN_LIST = "cdnResponse";
	public static final String MCR_TAG_ITEM_SIGNATURE = "signature";
	public static final String MCR_TAG_ITEM_PARENT = "parent";
	public static final String MCR_TAG_ITEM_NODE_TYPE = "node_type";
	public static final String MCR_TAG_BUSINESS_LIST = "guide";
	public static final String MCR_TAG_BUSINESS_LIST_PARENTS = "guides";

	public static final String MCR_TAG_TRACKING_INTERVAL = "trackingInterval";
	public static final String MCR_TAG_INSTALL_LOCATION = "installLocation";
	public static final String MCR_TAG_TRACKING_LOCATION = "trackingLocation";
	public static final String MCR_TAG_PROXIMITY = "proximity";

	public static final String MCR_TAG_TEMPLATE_TYPE = "templateType";
	public static final String MCR_TAG_TEMPLATE_ID = "templateId";
	public static final String MCR_TAG_TEMPLATE_LIST = "template";
	public static final String MCR_TAG_TEMPLATE_LIST_PARENTS = "templates";

	public static final String MCR_TAG_TIMETABLE_LIST_PARENTS = "timetables";
	public static final String MCR_TAG_TIMETABLE_LIST = "timetable";
	public static final String MCR_TAG_TIMER_TYPE = "timerType";
	public static final String MCR_TAG_TIMER_RULE = "timerRule";
	public static final String MCR_TAG_SOURCE_TYPE = "sourceType";
	public static final String MCR_TAG_SOURCE_NAME = "sourceName";
	public static final String MCR_TAG_SOURCE_URL = "sourceUrl";
	public static final String MCR_TAG_BOOKMARK_LIST_PARENTS = "bookmarks";
	public static final String MCR_TAG_BOOKMARK_LIST = "bookmark";

	public static final String MCR_TAG_THUMBNAIL_ICON_SMALL = "thumb_icon_small";
	public static final String MCR_TAG_MEDIA_TYPE = "mediaType";
	public static final String MCR_TAG_SESSION_TYPE = "sessionType";
	public static final String MCR_TAG_CONTENT_LINK = "contentLink";
	public static final String MCR_TAG_PREVIEW_LINK = "previewLink";
	public static final String MCR_TAG_STATION_ID = "stationId";
	public static final String MCR_TAG_STATION_NAME = "stationName";
	public static final String MCR_TAG_STATION_NAME_EN = "stationName_en";
	public static final String MCR_TAG_STREAM_LIST = "stream";
	public static final String MCR_TAG_STREAM_LIST_PARENTS = "streams";
	public static final String MCR_TAG_PLAYING_LIST = "playing";
	public static final String MCR_TAG_STREAM_ID = "streamId";

	public static final String MCR_TAG_REPLAY_GAIN = "replayGain";

	public static final String MCR_TAG_TEMPLATE_DIGEST = "templateDigest";
	public static final String MCR_TAG_TEMPLATE_ACTION = "templateAction";
	public static final String MCR_TAG_TEMPLATE_RULE = "templateRule";

	public static final String MCR_TAG_PATTERN_RESPONSE = "patternResponse";
	public static final String MCR_TAG_SCHEDULE_RULE = "scheduleRule";
	public static final String MCR_TAG_SCHEDULE_PERIOD = "schedulePeriod";
	public static final String MCR_TAG_PATTERN_ID = "patternId";
	public static final String MCR_TAG_PATTERN_DIGEST = "patternDigest";
	public static final String MCR_TAG_FRAME = "frame";
	public static final String MCR_TAG_ONAIR_TIME = "onairTime";
	public static final String MCR_TAG_PATTERN_AUDIO = "audio";
	public static final String MCR_TAG_AUDIO_ID = "audioId";
	public static final String MCR_TAG_AUDIO_VERSION = "audioVersion";
	public static final String MCR_TAG_AUDIO_TIME = "audioTime";
	public static final String MCR_TAG_AUDIO_URL = MCR_TAG_TRACKITEM_URL;
	public static final String MCR_TAG_FRAMES = "frames";
	public static final String MCR_TAG_FRAME_ID = "frameId";
	public static final String MCR_TAG_TARGET = "target";

	public static final String MCR_TAG_SCHEDULE_MASK = "scheduleMask";

	public static final String MCR_TAG_SHOW_DURATION = "showDuration";
	public static final String MCR_TAG_SHOW_JACKETS = "showJackets";
	public static final String MCR_TAG_SHOW_JACKET = "showJacket";

	public static final String MCR_TAG_SERVICE_TYPE = "serviceType";
	public static final String MCR_TAG_SERVICE_LEVEL = "serviceLevel";
	public static final String MCR_TAG_FEATURES = "features";
	public static final String MCR_TAG_PERMISSIONS = "permissions";
	public static final String MCR_TAG_MARKET_TYPE = "marketType";
	public static final String MCR_TAG_EXPIRES_DATE = "expiresDate";
	public static final String MCR_TAG_EXPIRES_LIMIT = "expiresLimit";

	// Message群
	public static final int MCR_KIND_MSG_PAYMRNT_NOT_LOCKED = 5001;
	public static final int MCR_KIND_MSG_PAYMRNT_VERIFY_ERROR = 5002;
	public static final int MCR_KIND_MSG_PAYMRNT_NETWORK_ERROR = 5003;
	public static final int MCR_KIND_MSG_EMAIL_REGISTERED = 5004;
	public static final int MCR_KIND_MSG_EMAIL_WRONG = 5005;
	public static final int MCR_KIND_MSG_EMAIL_WEAKNESS = 5006;
	public static final int MCR_KIND_MSG_USER_LOGGEDIN = 5007;
	public static final int MCR_KIND_MSG_USER_LOCKED = 5008;
	public static final int MCR_KIND_MSG_SHARE_NOT_FOUND = 5009;
	public static final int MCR_KIND_MSG_SHARE_EXPIRED = 5010;
	public static final int MCR_KIND_MSG_TICKET_NO_DOMAIN = 5011;
	public static final int MCR_KIND_MSG_NO_RESULT = 5012;
	public static final int MCR_KIND_MSG_TICKET_USED = 5013;
	public static final int MCR_KIND_MSG_TICKET_EXPIRED = 5014;
	public static final int MCR_KIND_MSG_ERROR_STATUS = 5015;
	public static final int MCR_KIND_MSG_ERROR_NETWORK = 5016;
	public static final int MCR_KIND_MSG_PAYMRNT_ALREADY_LOCKED = 5017;
	public static final int MCR_KIND_MSG_TICKET_CAMPAIGN_OWNED = 5018;
	public static final int MCR_KIND_MSG_TICKET_CAMPAIGN_EXPIRED = 5019;
	public static final int MCR_KIND_MSG_INVALID_DEVICE_TOKEN = 5021;
	public static final int MCR_KIND_MSG_ERROR_UNKNOWN = 5020;

	public static final int[] MCR_KIND_MSG_ARY = { MCR_KIND_MSG_PAYMRNT_NOT_LOCKED, MCR_KIND_MSG_PAYMRNT_VERIFY_ERROR,
			MCR_KIND_MSG_PAYMRNT_NETWORK_ERROR, MCR_KIND_MSG_EMAIL_REGISTERED, MCR_KIND_MSG_EMAIL_WRONG,
			MCR_KIND_MSG_EMAIL_WEAKNESS, MCR_KIND_MSG_USER_LOGGEDIN, MCR_KIND_MSG_USER_LOCKED,
			MCR_KIND_MSG_SHARE_NOT_FOUND, MCR_KIND_MSG_SHARE_EXPIRED, MCR_KIND_MSG_TICKET_NO_DOMAIN,
			MCR_KIND_MSG_NO_RESULT, MCR_KIND_MSG_TICKET_USED, MCR_KIND_MSG_TICKET_EXPIRED, MCR_KIND_MSG_ERROR_STATUS,
			MCR_KIND_MSG_ERROR_NETWORK, MCR_KIND_MSG_PAYMRNT_ALREADY_LOCKED, MCR_KIND_MSG_TICKET_CAMPAIGN_OWNED,
			MCR_KIND_MSG_TICKET_CAMPAIGN_EXPIRED, MCR_KIND_MSG_INVALID_DEVICE_TOKEN, MCR_KIND_MSG_ERROR_UNKNOWN };

	/**
	 * レシートの照合に失敗した場合にmessageタグに格納
	 */
	public static final String MCR_TAG_MSG_PAYMRNT_NOT_LOCKED = "PAYMENT_IS_NOT_LOCKED";
	/**
	 * レシートの照合に失敗した場合にmessageタグに格納
	 */
	public static final String MCR_TAG_MSG_PAYMRNT_VERIFY_ERROR = "PAYMENT_ERROR_VERIFY";
	/**
	 * ネットワーク/認証エラーの場合にmessageタグに格納
	 */
	public static final String MCR_TAG_MSG_PAYMRNT_NETWORK_ERROR = "PAYMENT_ERROR_NETWORK";
	/**
	 * サーバから返却されるXMLの<msg>タグの内容<br>
	 * 既に登録済みのメールアドレス
	 */
	public static final String MCR_TAG_MSG_EMAIL_REGISTERED = "EMAIL_IS_REGISTERED";
	/**
	 * サーバから返却されるXMLの<msg>タグの内容<br>
	 * メールアドレスの形式が不正
	 */
	public static final String MCR_TAG_MSG_EMAIL_WRONG = "EMAIL_IS_WRONG";
	/**
	 * サーバから返却されるXMLの<msg>タグの内容<br>
	 * パスワードの強度が弱い
	 */
	public static final String MCR_TAG_MSG_EMAIL_WEAKNESS = "PASSWORD_IS_WEAKNESS";
	/**
	 * サーバから返却されるXMLの<msg>タグの内容<br>
	 * 既にログイン済みのユーザアカウント
	 */
	public static final String MCR_TAG_MSG_USER_LOGGEDIN = "USER_IS_LOGGED_IN";
	/**
	 * サーバから返却されるXMLの<msg>タグの内容<br>
	 * 凍結中のユーザアカウント
	 */
	public static final String MCR_TAG_MSG_USER_LOCKED = "USER_IS_LOCKED";
	/**
	 * サーバから返却されるXMLの<msg>タグの内容<br>
	 * シェアチャンネルが見つからない
	 */
	public static final String MCR_TAG_MSG_SHARE_NOT_FOUND = "NOT_FOUND";
	/**
	 * サーバから返却されるXMLの<msg>タグの内容<br>
	 * 保存期間の過ぎたシェアチャンネル
	 */
	public static final String MCR_TAG_MSG_SHARE_EXPIRED = "EXPIRED";
	/**
	 * ネットワーク/認証エラーの場合にmessageタグに格納
	 */
	public static final String MCR_TAG_MSG_TICKET_NO_DOMAIN = "NO_DOMAIN";
	/**
	 * ネットワーク/認証エラーの場合にmessageタグに格納
	 */
	public static final String MCR_TAG_MSG_NO_RESULT = "NO_RESULT";
	/**
	 * ネットワーク/認証エラーの場合にmessageタグに格納
	 */
	public static final String MCR_TAG_MSG_TICKET_USED = "TICKET_USED";
	/**
	 * ネットワーク/認証エラーの場合にmessageタグに格納
	 */
	public static final String MCR_TAG_MSG_TICKET_EXPIRED = "TICKET_EXPIRED";
	/**
	 * ネットワーク/認証エラーの場合にmessageタグに格納
	 */
	public static final String MCR_TAG_MSG_ERROR_STATUS = "ERROR_STATUS";
	/**
	 * ネットワーク/認証エラーの場合にmessageタグに格納
	 */
	public static final String MCR_TAG_MSG_NETWORK_ERROR = "ERROR_NETWORK";
	/**
	 * レシートの照合に失敗した場合にmessageタグに格納
	 */
	public static final String MCR_TAG_MSG_PAYMRNT_ALREADY_LOCKED = "PAYMENT_IS_LOCKED";
	/**
	 * ネットワーク/認証エラーの場合にmessageタグに格納
	 */
	public static final String MCR_TAG_MSG_TICKET_CAMPAIGN_OWNED = "CAMPAIGN_OWNED";
	/**
	 * ネットワーク/認証エラーの場合にmessageタグに格納
	 */
	public static final String MCR_TAG_MSG_TICKET_CAMPAIGN_EXPIRED = "CAMPAIGN_EXPIRED";

	/**
	 * WebAPI の message<br>
	 * auth/login_business:デバイストークンが一致していない場合に返却
	 */
	public static final String MCR_TAG_MSG_INVALID_DEVICE_TOKEN = "INVALID_DEVICE_TOKEN";
	/**
	 * ネットワーク/
	 */
	public static final String MCR_TAG_MSG_ERROR_UNKNOWN = "ERROR_UNKNOWN";

	public static final String[] MCR_TAG_MSG_ARY = { MCR_TAG_MSG_PAYMRNT_NOT_LOCKED, MCR_TAG_MSG_PAYMRNT_VERIFY_ERROR,
			MCR_TAG_MSG_PAYMRNT_NETWORK_ERROR, MCR_TAG_MSG_EMAIL_REGISTERED, MCR_TAG_MSG_EMAIL_WRONG,
			MCR_TAG_MSG_EMAIL_WEAKNESS, MCR_TAG_MSG_USER_LOGGEDIN, MCR_TAG_MSG_USER_LOCKED, MCR_TAG_MSG_SHARE_NOT_FOUND,
			MCR_TAG_MSG_SHARE_EXPIRED, MCR_TAG_MSG_TICKET_NO_DOMAIN, MCR_TAG_MSG_NO_RESULT, MCR_TAG_MSG_TICKET_USED,
			MCR_TAG_MSG_TICKET_EXPIRED, MCR_TAG_MSG_ERROR_STATUS, MCR_TAG_MSG_NETWORK_ERROR,
			MCR_TAG_MSG_PAYMRNT_ALREADY_LOCKED, MCR_TAG_MSG_TICKET_CAMPAIGN_OWNED, MCR_TAG_MSG_TICKET_CAMPAIGN_EXPIRED,
			MCR_TAG_MSG_INVALID_DEVICE_TOKEN, MCR_TAG_MSG_ERROR_UNKNOWN, };

	public static int getMsgKind(String tag) {
		int msgKind = MCR_KIND_MSG_ERROR_UNKNOWN;

		for (int i = 0; i < MCR_TAG_MSG_ARY.length; i++) {
			if (MCR_TAG_MSG_ARY[i].equals(tag)) {
				msgKind = MCR_KIND_MSG_ARY[i];
				break;
			}
		}

		return msgKind;
	}

	public static String getMsgTag(int kind) {
		String msgTag = MCR_TAG_MSG_ERROR_UNKNOWN;

		for (int i = 0; i < MCR_KIND_MSG_ARY.length; i++) {
			if (MCR_KIND_MSG_ARY[i] == kind) {
				msgTag = MCR_TAG_MSG_ARY[i];
				break;
			}
		}

		return msgTag;
	}

	public static int getStringId(int kind) {
		int resId = R.string.msg_system_error;

		// switch (kind) {
		// case MCR_KIND_MSG_TICKET_USED:
		// resId = R.string.msg_ticket_failed_owing_to_ticket_used;
		// break;
		// case MCR_KIND_MSG_TICKET_EXPIRED:
		// resId = R.string.msg_ticket_failed_owing_to_ticket_expired;
		// break;
		// case MCR_KIND_MSG_TICKET_CAMPAIGN_OWNED:
		// resId = R.string.msg_ticket_failed_owing_to_campaign_owned;
		// break;
		// case MCR_KIND_MSG_TICKET_CAMPAIGN_EXPIRED:
		// resId = R.string.msg_ticket_failed_owing_to_campaign_expired;
		// break;
		// case MCR_KIND_MSG_NO_RESULT:
		// case MCR_KIND_MSG_ERROR_STATUS:
		// case MCR_KIND_MSG_TICKET_NO_DOMAIN:
		// resId = R.string.msg_ticket_invalidate;
		// break;
		// case MCR_KIND_MSG_ERROR_NETWORK:
		// resId = R.string.msg_ticket_failed_to_confirm;
		// break;
		// }

		return resId;
	}

	/**
	 * 種別取得
	 * 
	 * @param tag
	 * @return 種別
	 */
	public static int getKind(String tag) {
		int kind = MCR_KIND_UNKNOWN;

		if (MCR_TAG_ENCMETHOD.equals(tag)) {
			kind = MCR_KIND_ENCMETHOD;
		} else if (MCR_TAG_ENCKEY.equals(tag)) {
			kind = MCR_KIND_ENCKEY;
		} else if (MCR_TAG_ENCIV.equals(tag)) {
			kind = MCR_KIND_ENCIV;
		} else if (MCR_TAG_TRACK_LIST.equals(tag)) {
			kind = MCR_KIND_TRACK_LIST;
		} else if (MCR_TAG_GENRE_LIST.equals(tag)) {
			kind = MCR_KIND_GENRE_LIST;
		} else if (MCR_TAG_CHANNEL_LIST.equals(tag)) {
			kind = MCR_KIND_CHANNEL_LIST;
		} else if (MCR_TAG_BENIFIT_LIST.equals(tag)) {
			kind = MCR_KIND_BENIFIT_LIST;
		} else if (MCR_TAG_MESSAGE.equals(tag)) {
			kind = MCR_KIND_MESSAGE;
		} else if (MCR_TAG_ACTIVATIONKEY.equals(tag)) {
			kind = MCR_KIND_ACTIVATIONKEY;
		} else if (MCR_TAG_SESSIONKEY.equals(tag)) {
			kind = MCR_KIND_SESSIONKEY;
		} else if (MCR_TAG_ADVERTISERATIO.equals(tag)) {
			kind = MCR_KIND_ADVERTISERATIO;
		} else if (MCR_TAG_SKIPREMAINING.equals(tag)) {
			kind = MCR_KIND_SKIPREMAINING;
		} else if (MCR_TAG_XENC.equals(tag)) {
			kind = MCR_KIND_XENC;
		} else if (MCR_TAG_TRACKITEM_ID.equals(tag)) {
			kind = MCR_KIND_TRACKITEM_ID;
		} else if (MCR_TAG_TRACKITEM_TITLE.equals(tag)) {
			kind = MCR_KIND_TRACKITEM_TITLE;
		} else if (MCR_TAG_TRACKITEM_ARTIST.equals(tag)) {
			kind = MCR_KIND_TRACKITEM_ARTIST;
		} else if (MCR_TAG_GENREITEM_ID.equals(tag)) {
			kind = MCR_KIND_GENREITEM_ID;
		} else if (MCR_TAG_GENREITEM_NAME.equals(tag)) {
			kind = MCR_KIND_GENREITEM_NAME;
		} else if (MCR_TAG_CHANNELITEM_NAME.equals(tag)) {
			kind = MCR_KIND_CHANNELITEM_NAME;
		} else if (MCR_TAG_CHANNELITEM_LOCK.equals(tag)) {
			kind = MCR_KIND_CHANNELITEM_LOCK;
		} else if (MCR_TAG_TRACK_LIST_PARENTS.equals(tag)) {
			kind = MCR_KIND_TRACK_LIST_PARENTS;
		} else if (MCR_TAG_GENRE_LIST_PARENTS.equals(tag)) {
			kind = MCR_KIND_GENRE_LIST_PARENTS;
		} else if (MCR_TAG_CHANNEL_LIST_PARENTS.equals(tag)) {
			kind = MCR_KIND_CHANNEL_LIST_PARENTS;
		}

		// 2011/11/10 追加
		else if (MCR_TAG_USER_STATUS.equals(tag)) {
			kind = MCR_KIND_USER_STATUS;
		} else if (MCR_TAG_SUBSCRIPTION_TYPE.equals(tag)) {
			kind = MCR_KIND_SUBSCRIPTION_TYPE;
		} else if (MCR_TAG_SUBSCRIPTION_STATUS.equals(tag)) {
			kind = MCR_KIND_SUBSCRIPTION_STATUS;
		} else if (MCR_TAG_PLAYABLE_TRACKS.equals(tag)) {
			kind = MCR_KIND_PLAYABLE_TRACKS;
		} else if (MCR_TAG_TRACKING_KEY.equals(tag)) {
			kind = MCR_KIND_TRACKING_KEY;
		} else if (MCR_TAG_TRACKITEM_TITLE_EN.equals(tag)) {
			kind = MCR_KIND_TRACKITEM_TITLE_EN;
		} else if (MCR_TAG_TRACKITEM_ARTIST_EN.equals(tag)) {
			kind = MCR_KIND_TRACKITEM_ARTIST_EN;
		} else if (MCR_TAG_TRACKITEM_RELEASE_DATE.equals(tag)) {
			kind = MCR_KIND_TRACKITEM_RELEASE_DATE;
		} else if (MCR_TAG_TRACKITEM_GENRE.equals(tag)) {
			kind = MCR_KIND_TRACKITEM_GENRE;
		} else if (MCR_TAG_TRACKITEM_GENRE_EN.equals(tag)) {
			kind = MCR_KIND_TRACKITEM_GENRE_EN;
		} else if (MCR_TAG_TRACKITEM_DESCRIPTION.equals(tag)) {
			kind = MCR_KIND_TRACKITEM_DESCRIPTION;
		} else if (MCR_TAG_TRACKITEM_DESCRIPTION_EN.equals(tag)) {
			kind = MCR_KIND_TRACKITEM_DESCRIPTION_EN;
		} else if (MCR_TAG_TRACKITEM_URL.equals(tag)) {
			kind = MCR_KIND_TRACKITEM_URL;
		} else if (MCR_TAG_GENREITEM_NAME_EN.equals(tag)) {
			kind = MCR_KIND_GENREITEM_NAME_EN;
		} else if (MCR_TAG_CHART_LIST.equals(tag)) {
			kind = MCR_KIND_CHART_LIST;
		} else if (MCR_TAG_CHARTITEM_ID.equals(tag)) {
			kind = MCR_KIND_CHARTITEM_ID;
		} else if (MCR_TAG_CHARTITEM_NAME.equals(tag)) {
			kind = MCR_KIND_CHARTITEM_NAME;
		} else if (MCR_TAG_CHARTITEM_NAME_EN.equals(tag)) {
			kind = MCR_KIND_CHARTITEM_NAME_EN;
		} else if (MCR_TAG_SEARCH_LIST.equals(tag)) {
			kind = MCR_KIND_SEARCH_LIST;
		} else if (MCR_TAG_SEARCHITEM_ID.equals(tag)) {
			kind = MCR_KIND_SEARCHITEM_ID;
		} else if (MCR_TAG_SEARCHITEM_NAME.equals(tag)) {
			kind = MCR_KIND_SEARCHITEM_NAME;
		} else if (MCR_TAG_SEARCHITEM_NAME_EN.equals(tag)) {
			kind = MCR_KIND_SEARCHITEM_NAME_EN;
		} else if (MCR_TAG_MESSAGE_EN.equals(tag)) {
			kind = MCR_KIND_MESSAGE_EN;
		} else if (MCR_TAG_AD_LIST.equals(tag)) {
			kind = MCR_KIND_AD_LIST;
		} else if (MCR_TAG_ADITEM_ID.equals(tag)) {
			kind = MCR_KIND_ADITEM_ID;
		} else if (MCR_TAG_ADITEM_TITLE.equals(tag)) {
			kind = MCR_KIND_ADITEM_TITLE;
		} else if (MCR_TAG_ADITEM_TITLE_EN.equals(tag)) {
			kind = MCR_KIND_ADITEM_TITLE_EN;
		} else if (MCR_TAG_ITEM_DESCRIPTION.equals(tag)) {
			kind = MCR_KIND_ITEM_DESCRIPTION;
		} else if (MCR_TAG_ITEM_DESCRIPTION_EN.equals(tag)) {
			kind = MCR_KIND_ITEM_DESCRIPTION_EN;
		} else if (MCR_TAG_ADITEM_SPONSOR.equals(tag)) {
			kind = MCR_KIND_ADITEM_SPONSOR;
		} else if (MCR_TAG_ADITEM_SPONSOR_EN.equals(tag)) {
			kind = MCR_KIND_ADITEM_SPONSOR_EN;
		} else if (MCR_TAG_ADITEM_URL.equals(tag)) {
			kind = MCR_KIND_ADITEM_URL;
		} else if (MCR_TAG_CHART_LIST_PARENTS.equals(tag)) {
			kind = MCR_KIND_CHART_LIST_PARENTS;
		} else if (MCR_TAG_SEARCH_LIST_PARENTS.equals(tag)) {
			kind = MCR_KIND_SEARCH_LIST_PARENTS;
		} else if (MCR_TAG_AD_LIST_PARENTS.equals(tag)) {
			kind = MCR_KIND_AD_LIST_PARENTS;
		} else if (MCR_TAG_TRACKITEM_JACKET_ID.equals(tag)) {
			kind = MCR_KIND_TRACKITEM_JACKET_ID;
		} else if (MCR_TAG_TRACKITEM_ALBUM.equals(tag)) {
			kind = MCR_KIND_TRACKITEM_ALBUM;
		} else if (MCR_TAG_TRACKITEM_ALBUM_EN.equals(tag)) {
			kind = MCR_KIND_TRACKITEM_ALBUM_EN;
		} else if (MCR_TAG_CHARTITEM_ROYALTY.equals(tag)) {
			kind = MCR_KIND_CHARTITEM_ROYALTY;
		} else if (MCR_TAG_PAYMENT_TYPE.equals(tag)) {
			kind = MCR_KIND_PAYMENT_TYPE;
		} else if (MCR_TAG_PAYMENT_ID.equals(tag)) {
			kind = MCR_KIND_PAYMENT_ID;
		} else if (MCR_TAG_CHANNELITEM_TIMESTAMP.equals(tag)) {
			kind = MCR_KIND_CHANNELITEM_TIMESTAMP;
		}

		else if (MCR_TAG_SHAREITEM_SHARE_KEY.equals(tag)) {
			kind = MCR_KIND_SHAREITEM_SHARE_KEY;
		} else if (MCR_TAG_SHAREITEM_SHARE_ID.equals(tag)) {
			kind = MCR_KIND_SHAREITEM_SHARE_ID;
		} else if (MCR_TAG_SHAREITEM_SHARE_NAME.equals(tag)) {
			kind = MCR_KIND_SHAREITEM_SHARE_NAME;
		} else if (MCR_TAG_TRACKITEM_TRIAL_TIME.equals(tag)) {
			kind = MCR_KIND_TRACKITEM_TRIAL_TIME;
		} else if (MCR_TAG_LOCATION_CODE.equals(tag)) {
			kind = MCR_KIND_LOCATION_CODE;
		} else if (MCR_TAG_LOCATION_NAME.equals(tag)) {
			kind = MCR_KIND_LOCATION_NAME;
		} else if (MCR_TAG_LOCATION_NAME_EN.equals(tag)) {
			kind = MCR_KIND_LOCATION_NAME_EN;
		} else if (MCR_TAG_LOCATION_COUNTRY.equals(tag)) {
			kind = MCR_KIND_LOCATION_COUNTRY;
		} else if (MCR_TAG_LOCATION_REGION.equals(tag)) {
			kind = MCR_KIND_LOCATION_REGION;
		} else if (MCR_TAG_LOCATION_LIST.equals(tag)) {
			kind = MCR_KIND_LOCATION_LIST;
		} else if (MCR_TAG_LOCATION_LIST_PARENTS.equals(tag)) {
			kind = MCR_KIND_LOCATION_LIST_PARENTS;
		} else if (MCR_TAG_BENIFIT_LIST_PARENTS.equals(tag)) {
			kind = MCR_KIND_BENIFIT_LIST_PARENTS;
		} else if (MCR_TAG_ITEM_CONTENT_TITLE.equals(tag)) {
			kind = MCR_KIND_ITEM_CONTENT_TITLE;
		} else if (MCR_TAG_ITEM_CONTENT_TITLE_EN.equals(tag)) {
			kind = MCR_KIND_ITEM_CONTENT_TITLE_EN;
		} else if (MCR_TAG_ITEM_CONTENT_TEXT.equals(tag)) {
			kind = MCR_KIND_ITEM_CONTENT_TEXT;
		} else if (MCR_TAG_ITEM_CONTENT_TEXT_EN.equals(tag)) {
			kind = MCR_KIND_ITEM_CONTENT_TEXT_EN;
		} else if (MCR_TAG_ITEM_THUMB_ICON.equals(tag)) {
			kind = MCR_KIND_ITEM_THUMB_ICON;
		} else if (MCR_TAG_BENIFITITEM_DELIVERY_BEGIN.equals(tag)) {
			kind = MCR_KIND_BENIFITITEM_DELIVERY_BEGIN;
		} else if (MCR_TAG_BENIFITITEM_DELIVERY_END.equals(tag)) {
			kind = MCR_KIND_BENIFITITEM_DELIVERY_END;
		} else if (MCR_TAG_BENIFITITEM_SKIP_CONTROL.equals(tag)) {
			kind = MCR_KIND_BENIFITITEM_SKIP_CONTROL;
		} else if (MCR_TAG_BENIFITITEM_AD_CONTROL.equals(tag)) {
			kind = MCR_KIND_BENIFITITEM_AD_CONTROL;
		} else if (MCR_TAG_ITEM_SERIES_NO.equals(tag)) {
			kind = MCR_KIND_ITEM_SERIES_NO;
		} else if (MCR_TAG_ITEM_SERIES_CONTENT_NO.equals(tag)) {
			kind = MCR_KIND_ITEM_SERIES_CONTENT_NO;
		} else if (MCR_TAG_CAMPAIGN_LIST.equals(tag)) {
			kind = MCR_KIND_CAMPAIGN_LIST;
		} else if (MCR_TAG_CAMPAIGN_ID.equals(tag)) {
			kind = MCR_KIND_CAMPAIGN_ID;
		} else if (MCR_TAG_CAMPAIGN_NAME.equals(tag)) {
			kind = MCR_KIND_CAMPAIGN_NAME;
		} else if (MCR_TAG_CAMPAIGN_NAME_EN.equals(tag)) {
			kind = MCR_KIND_CAMPAIGN_NAME_EN;
		} else if (MCR_TAG_VALID_BEGIN.equals(tag)) {
			kind = MCR_KIND_VALID_BEGIN;
		} else if (MCR_TAG_VALID_END.equals(tag)) {
			kind = MCR_KIND_VALID_END;
		} else if (MCR_TAG_CAMPAIGN_LIST_PARENTS.equals(tag)) {
			kind = MCR_KIND_CAMPAIGN_LIST_PARENTS;
		} else if (MCR_TAG_SHUFFLE_LIST_PARENTS.equals(tag)) {
			kind = MCR_KIND_SHUFFLE_LIST_PARENTS;
		} else if (MCR_TAG_SHUFFLE_LIST.equals(tag)) {
			kind = MCR_KIND_SHUFFLE_LIST;
		} else if (MCR_TAG_TRACKITEM_ARTIST_ID.equals(tag)) {
			kind = MCR_KIND_TRACKITEM_ARTIST_ID;
		} else if (MCR_TAG_CHANNEL_NAME.equals(tag)) {
			kind = MCR_KIND_CHANNEL_NAME;
		} else if (MCR_TAG_CHANNEL_NAME_EN.equals(tag)) {
			kind = MCR_KIND_CHANNEL_NAME_EN;

		} else if (MCR_TAG_LATITUDE.equals(tag)) {
			kind = MCR_KIND_LATITUDE;
		} else if (MCR_TAG_LONGITUDE.equals(tag)) {
			kind = MCR_KIND_LONGITUDE;
		} else if (MCR_TAG_DISTANCE.equals(tag)) {
			kind = MCR_KIND_DISTANCE;
		} else if (MCR_TAG_INDUSTRY_CODE.equals(tag)) {
			kind = MCR_KIND_INDUSTRY_CODE;
		} else if (MCR_TAG_INDUSTRY_NAME.equals(tag)) {
			kind = MCR_KIND_INDUSTRY_NAME;
		} else if (MCR_TAG_INDUSTRY_NAME_EN.equals(tag)) {
			kind = MCR_KIND_INDUSTRY_NAME_EN;
		} else if (MCR_TAG_SHOP_NAME.equals(tag)) {
			kind = MCR_KIND_SHOP_NAME;
		} else if (MCR_TAG_ZIP_CODE.equals(tag)) {
			kind = MCR_KIND_ZIP_CODE;
		} else if (MCR_TAG_PROVINCE_CODE.equals(tag)) {
			kind = MCR_KIND_PROVINCE_CODE;
		} else if (MCR_TAG_PROVINCE_NAME.equals(tag)) {
			kind = MCR_KIND_PROVINCE_NAME;
		} else if (MCR_TAG_PROVINCE_NAME_EN.equals(tag)) {
			kind = MCR_KIND_PROVINCE_NAME_EN;
		} else if (MCR_TAG_LOCATION_EN.equals(tag)) {
			kind = MCR_KIND_LOCATION_EN;
		} else if (MCR_TAG_CONTACT1.equals(tag)) {
			kind = MCR_KIND_CONTACT1;
		} else if (MCR_TAG_CONTACT2.equals(tag)) {
			kind = MCR_KIND_CONTACT2;
		} else if (MCR_TAG_EXTERNAL_LINK1.equals(tag)) {
			kind = MCR_KIND_EXTERNAL_LINK1;
		} else if (MCR_TAG_EXTERNAL_LINK2.equals(tag)) {
			kind = MCR_KIND_EXTERNAL_LINK2;
		} else if (MCR_TAG_EXTERNAL_LINK3.equals(tag)) {
			kind = MCR_KIND_EXTERNAL_LINK3;
		} else if (MCR_TAG_EXTERNAL_LINK4.equals(tag)) {
			kind = MCR_KIND_EXTERNAL_LINK4;
		} else if (MCR_TAG_EXTERNAL_LINK5.equals(tag)) {
			kind = MCR_KIND_EXTERNAL_LINK5;
		} else if (MCR_TAG_SHOP_LIST.equals(tag)) {
			kind = MCR_KIND_SHOP_LIST;
		} else if (MCR_TAG_SHOP_LIST_PARENTS.equals(tag)) {
			kind = MCR_KIND_SHOP_LIST_PARENTS;

		} else if (MCR_TAG_THUMBNAIL_ICON_SMALL.equals(tag)) {
			kind = MCR_KIND_THUMBNAIL_ICON_SMALL;
		} else if (MCR_TAG_ITEM_TYPE.equals(tag)) {
			kind = MCR_KIND_ITEM_TYPE;
		} else if (MCR_TAG_ITEM_USER.equals(tag)) {
			kind = MCR_KIND_ITEM_USER;
		} else if (MCR_TAG_ITEM_SECRET.equals(tag)) {
			kind = MCR_KIND_ITEM_SECRET;
		} else if (MCR_TAG_ITEM_ENC.equals(tag)) {
			kind = MCR_KIND_ITEM_ENC;
		} else if (MCR_TAG_ITEM_METHOD.equals(tag)) {
			kind = MCR_KIND_ITEM_METHOD;
		} else if (MCR_TAG_ITEM_KEY.equals(tag)) {
			kind = MCR_KIND_ITEM_KEY;
		} else if (MCR_TAG_ITEM_IV.equals(tag)) {
			kind = MCR_KIND_ITEM_IV;
		} else if (MCR_TAG_CDN_LIST.equals(tag)) {
			kind = MCR_KIND_CDN_LIST;
		} else if (MCR_TAG_ITEM_SIGNATURE.equals(tag)) {
			kind = MCR_KIND_ITEM_SIGNATURE;
		} else if (MCR_TAG_ITEM_PARENT.equals(tag)) {
			kind = MCR_KIND_ITEM_PARENT;
		} else if (MCR_TAG_ITEM_NODE_TYPE.equals(tag)) {
			kind = MCR_KIND_ITEM_NODE_TYPE;
		} else if (MCR_TAG_BUSINESS_LIST.equals(tag)) {
			kind = MCR_KIND_BUSINESS_LIST;
		} else if (MCR_TAG_BUSINESS_LIST_PARENTS.equals(tag)) {
			kind = MCR_KIND_BUSINESS_LIST_PARENTS;

		} else if (MCR_TAG_TRACKING_INTERVAL.equals(tag)) {
			kind = MCR_KIND_TRACKING_INTERVAL;
		} else if (MCR_TAG_INSTALL_LOCATION.equals(tag)) {
			kind = MCR_KIND_INSTALL_LOCATION;
		} else if (MCR_TAG_TRACKING_LOCATION.equals(tag)) {
			kind = MCR_KIND_TRACKING_LOCATION;
		} else if (MCR_TAG_PROXIMITY.equals(tag)) {
			kind = MCR_KIND_PROXIMITY;

		} else if (MCR_TAG_TEMPLATE_TYPE.equals(tag)) {
			kind = MCR_KIND_TEMPLATE_TYPE;
		} else if (MCR_TAG_TEMPLATE_ID.equals(tag)) {
			kind = MCR_KIND_TEMPLATE_ID;
		} else if (MCR_TAG_TEMPLATE_LIST.equals(tag)) {
			kind = MCR_KIND_TEMPLATE_LIST;
		} else if (MCR_TAG_TEMPLATE_LIST_PARENTS.equals(tag)) {
			kind = MCR_KIND_TEMPLATE_LIST_PARENTS;

		} else if (MCR_TAG_TIMETABLE_LIST_PARENTS.equals(tag)) {
			kind = MCR_KIND_TIMETABLE_LIST_PARENTS;
		} else if (MCR_TAG_TIMETABLE_LIST.equals(tag)) {
			kind = MCR_KIND_TIMETABLE_LIST;
		} else if (MCR_TAG_TIMER_TYPE.equals(tag)) {
			kind = MCR_KIND_TIMER_TYPE;
		} else if (MCR_TAG_TIMER_RULE.equals(tag)) {
			kind = MCR_KIND_TIMER_RULE;
		} else if (MCR_TAG_SOURCE_TYPE.equals(tag)) {
			kind = MCR_KIND_SOURCE_TYPE;
		} else if (MCR_TAG_SOURCE_NAME.equals(tag)) {
			kind = MCR_KIND_SOURCE_NAME;
		} else if (MCR_TAG_SOURCE_URL.equals(tag)) {
			kind = MCR_KIND_SOURCE_URL;
		} else if (MCR_TAG_BOOKMARK_LIST_PARENTS.equals(tag)) {
			kind = MCR_KIND_BOOKMARK_LIST_PARENTS;
		} else if (MCR_TAG_BOOKMARK_LIST.equals(tag)) {
			kind = MCR_KIND_BOOKMARK_LIST;

		} else if (MCR_TAG_MEDIA_TYPE.equals(tag)) {
			kind = MCR_KIND_MEDIA_TYPE;
		} else if (MCR_TAG_SESSION_TYPE.equals(tag)) {
			kind = MCR_KIND_SESSION_TYPE;
		} else if (MCR_TAG_CONTENT_LINK.equals(tag)) {
			kind = MCR_KIND_CONTENT_LINK;
		} else if (MCR_TAG_PREVIEW_LINK.equals(tag)) {
			kind = MCR_KIND_PREVIEW_LINK;
		} else if (MCR_TAG_STATION_ID.equals(tag)) {
			kind = MCR_KIND_STATION_ID;
		} else if (MCR_TAG_STATION_NAME.equals(tag)) {
			kind = MCR_KIND_STATION_NAME;
		} else if (MCR_TAG_STATION_NAME_EN.equals(tag)) {
			kind = MCR_KIND_STATION_NAME_EN;
		} else if (MCR_TAG_STREAM_LIST.equals(tag)) {
			kind = MCR_KIND_STREAM_LIST;
		} else if (MCR_TAG_STREAM_LIST_PARENTS.equals(tag)) {
			kind = MCR_KIND_STREAM_LIST_PARENTS;
		} else if (MCR_TAG_PLAYING_LIST.equals(tag)) {
			kind = MCR_KIND_PLAYING_LIST;
		} else if (MCR_TAG_STREAM_ID.equals(tag)) {
			kind = MCR_KIND_STREAM_ID;

		} else if (MCR_TAG_REPLAY_GAIN.equals(tag)) {
			kind = MCR_KIND_REPLAY_GAIN;

		} else if (MCR_TAG_TEMPLATE_DIGEST.equals(tag)) {
			kind = MCR_KIND_TEMPLATE_DIGEST;
		} else if (MCR_TAG_TEMPLATE_ACTION.equals(tag)) {
			kind = MCR_KIND_TEMPLATE_ACTION;
		} else if (MCR_TAG_TEMPLATE_RULE.equals(tag)) {
			kind = MCR_KIND_TEMPLATE_RULE;

		} else if (MCR_TAG_PATTERN_RESPONSE.equals(tag)) {
			kind = MCR_KIND_PATTERN_RESPONSE;
		} else if (MCR_TAG_SCHEDULE_RULE.equals(tag)) {
			kind = MCR_KIND_SCHEDULE_RULE;
		} else if (MCR_TAG_SCHEDULE_PERIOD.equals(tag)) {
			kind = MCR_KIND_SCHEDULE_PERIOD;
		} else if (MCR_TAG_PATTERN_ID.equals(tag)) {
			kind = MCR_KIND_PATTERN_ID;
		} else if (MCR_TAG_PATTERN_DIGEST.equals(tag)) {
			kind = MCR_KIND_PATTERN_DIGEST;
		} else if (MCR_TAG_FRAME.equals(tag)) {
			kind = MCR_KIND_FRAME;
		} else if (MCR_TAG_ONAIR_TIME.equals(tag)) {
			kind = MCR_KIND_ONAIR_TIME;
		} else if (MCR_TAG_PATTERN_AUDIO.equals(tag)) {
			kind = MCR_KIND_PATTERN_AUDIO;
		} else if (MCR_TAG_AUDIO_ID.equals(tag)) {
			kind = MCR_KIND_AUDIO_ID;
		} else if (MCR_TAG_AUDIO_VERSION.equals(tag)) {
			kind = MCR_KIND_AUDIO_VERSION;
		} else if (MCR_TAG_AUDIO_TIME.equals(tag)) {
			kind = MCR_KIND_AUDIO_TIME;
		} else if (MCR_TAG_AUDIO_URL.equals(tag)) {
			kind = MCR_KIND_AUDIO_URL;
		} else if (MCR_TAG_FRAMES.equals(tag)) {
			kind = MCR_KIND_FRAMES;
		} else if (MCR_TAG_FRAME_ID.equals(tag)) {
			kind = MCR_KIND_FRAME_ID;
		} else if (MCR_TAG_TARGET.equals(tag)) {
			kind = MCR_KIND_TARGET;
		} else if (MCR_TAG_SCHEDULE_MASK.equals(tag)) {
			kind = MCR_KIND_SCHEDULE_MASK;
		} else if (MCR_TAG_SHOW_DURATION.equals(tag)) {
			kind = MCR_KIND_SHOW_DURATION;
		} else if (MCR_TAG_SHOW_JACKETS.equals(tag)) {
			kind = MCR_KIND_SHOW_JACKETS;
		} else if (MCR_TAG_SHOW_JACKET.equals(tag)) {
			kind = MCR_KIND_SHOW_JACKET;
		} else if (MCR_TAG_SERVICE_TYPE.equals(tag)) {
			kind = MCR_KIND_SERVICE_TYPE;
		} else if (MCR_TAG_SERVICE_LEVEL.equals(tag)) {
			kind = MCR_KIND_SERVICE_LEVEL;
		} else if (MCR_TAG_FEATURES.equals(tag)) {
			kind = MCR_KIND_FEATURES;
		} else if (MCR_TAG_PERMISSIONS.equals(tag)) {
			kind = MCR_KIND_PERMISSIONS;
		} else if (MCR_TAG_MARKET_TYPE.equals(tag)) {
			kind = MCR_KIND_MARKET_TYPE;
		} else if (MCR_TAG_EXPIRES_DATE.equals(tag)) {
			kind = MCR_KIND_EXPIRES_DATE;
		} else if (MCR_TAG_EXPIRES_LIMIT.equals(tag)) {
			kind = MCR_KIND_EXPIRES_LIMIT;
		}

		return kind;
	}

	/**
	 * アイテム締結確認
	 * 
	 * @param kind
	 * @return
	 */
	public static boolean isItemFinish(int kind) {
		boolean finish = false;

		switch (kind) {
		case MCR_KIND_TRACK_LIST:
		case MCR_KIND_GENRE_LIST:
		case MCR_KIND_CHANNEL_LIST:
		case MCR_KIND_CHART_LIST:
		case MCR_KIND_SEARCH_LIST:
		case MCR_KIND_AD_LIST:
		case MCR_KIND_LOCATION_LIST:
		case MCR_KIND_BENIFIT_LIST:
		case MCR_KIND_CAMPAIGN_LIST:
		case MCR_KIND_SHUFFLE_LIST:
		case MCR_KIND_SHOP_LIST:
		case MCR_KIND_CDN_LIST:
		case MCR_KIND_BUSINESS_LIST:
		case MCR_KIND_TEMPLATE_LIST:
		case MCR_KIND_TIMETABLE_LIST:
		case MCR_KIND_BOOKMARK_LIST:
		case MCR_KIND_STREAM_LIST:
		case MCR_KIND_PLAYING_LIST:
		case MCR_KIND_PATTERN_RESPONSE:
		case MCR_KIND_PATTERN_AUDIO:
		case MCR_KIND_FRAME:
			finish = true;
			break;
		default:
			finish = false;
			break;
		}
		return finish;
	}

	/**
	 * リスト締結確認
	 * 
	 * @param kind
	 * @return
	 */
	public static boolean isListFinish(int kind) {
		boolean finish = false;

		switch (kind) {
		case MCR_KIND_TRACK_LIST_PARENTS:
		case MCR_KIND_GENRE_LIST_PARENTS:
		case MCR_KIND_CHANNEL_LIST_PARENTS:
		case MCR_KIND_CHART_LIST_PARENTS:
		case MCR_KIND_SEARCH_LIST_PARENTS:
		case MCR_KIND_AD_LIST_PARENTS:
		case MCR_KIND_LOCATION_LIST_PARENTS:
		case MCR_KIND_BENIFIT_LIST_PARENTS:
		case MCR_KIND_CAMPAIGN_LIST_PARENTS:
		case MCR_KIND_SHUFFLE_LIST_PARENTS:
		case MCR_KIND_SHOP_LIST_PARENTS:
		case MCR_KIND_BUSINESS_LIST_PARENTS:
		case MCR_KIND_TEMPLATE_LIST_PARENTS:
		case MCR_KIND_TIMETABLE_LIST_PARENTS:
		case MCR_KIND_BOOKMARK_LIST_PARENTS:
		case MCR_KIND_STREAM_LIST_PARENTS:
		case MCR_KIND_FRAMES:
			finish = true;
			break;
		default:
			finish = false;
			break;
		}
		return finish;
	}
}
