package jp.faraopro.play.mclient;

/**
 * MusicClientパラメータ関連定義群
 * 
 * @author AIM Corporation
 * 
 */
public class MCDefParam {

	/**
	 * 不明・Default
	 */
	public static final int MCP_KIND_UNKNOWN = 0;
	/**
	 * 種別ID：メールアドレス
	 */
	public static final int MCP_KIND_EMAIL = 1;
	/**
	 * 種別ID：パスワード
	 */
	public static final int MCP_KIND_PASSWORD = 2;
	/**
	 * 種別ID：アクティベーションキー
	 */
	public static final int MCP_KIND_ACTIVATIONKEY = 3;
	/**
	 * 種別ID：セッションキー
	 */
	public static final int MCP_KIND_SESSIONKEY = 4;
	/**
	 * 種別ID：モード番号
	 */
	public static final int MCP_KIND_MODE_NO = 5;
	/**
	 * 種別ID：マイチャンネルID
	 */
	public static final int MCP_KIND_MYCHANNELID = 6;
	/**
	 * 種別ID：コンテンツリリース範囲
	 */
	public static final int MCP_KIND_RANGE = 7;
	/**
	 * 種別ID：コンテンツ対するユーザーアクション
	 */
	public static final int MCP_KIND_RATE_ACTION = 8;
	/**
	 * 種別ID：再生した時間
	 */
	public static final int MCP_KIND_PLAY_DURATIN = 9;
	/**
	 * 種別ID：検索用キーワード
	 */
	public static final int MCP_KIND_KEYWORD = 10;
	/**
	 * 種別ID：マイチャンネルID
	 */
	public static final int MCP_KIND_MYCHANNEL_ID = 11;
	/**
	 * 種別ID：マイチャンネル名称
	 */
	public static final int MCP_KIND_MYCHANNEL_NAME = 12;
	/**
	 * 種別ID：マイチャンネルロック
	 */
	public static final int MCP_KIND_MYCHANNEL_LOCK = 24;
	/**
	 * 種別ID：トラックID（ダウンロード用）
	 */
	public static final int MCP_KIND_TRACKID = 13;
	/**
	 * 種別ID：トラックID（評価用）
	 */
	public static final int MCP_KIND_RATING_TRACKID = 14;
	/**
	 * 種別ID：コンテンツビットレート指定
	 */
	public static final int MCP_KIND_QUARITY = 15;

	// 2011/11/10 仕様変更
	// /**
	// * 種別ID：ダウンロードフォーマット
	// */
	// public static final int MCP_KIND_FORMAT = 16;

	// 2011/11/10追加
	/**
	 * 種別ID：強制ログイン指定
	 */
	public static final int MCP_KIND_FORCE = 17;
	/**
	 * 種別ID：検索モード //MEMO 名前が被っているため、現在未使用
	 */
	public static final int MCP_KIND_MODE_SEARCH = 18;
	/**
	 * 種別ID：JacketID
	 */
	public static final int MCP_KIND_JACKETID = 19;
	/**
	 * 種別ID：音声広告ID
	 */
	public static final int MCP_KIND_ADID = 20;
	/**
	 * 種別ID：ダウンロードデータType（音声広告・各種メッセージ）
	 */
	public static final int MCP_KIND_TYPE_DL = 21;
	/**
	 * 種別ID：評価対象となるAdvertiseID
	 */
	public static final int MCP_KIND_RATE_ADID = 22;
	/**
	 * 種別ID：コンテンツを最後まで視聴したか否か
	 */
	public static final int MCP_KIND_PLAY_COMPLETE = 23;

	// 12/14 ksu 追加
	/**
	 * 種別ID：課金タイプ
	 */
	public static final int MCP_KIND_BILLING_TYPE = 25;
	/**
	 * 種別ID：マーケットタイプ
	 */
	public static final int MCP_KIND_MARKET_TYPE = 26;
	/**
	 * 種別ID：購入トランザクション情報
	 */
	public static final int MCP_KIND_RECEIPT = 27;
	/**
	 * 種別ID：トラッキングキー
	 */
	public static final int MCP_KIND_TRACKING_KEY = 28;
	/**
	 * 種別ID：アイテム価格
	 */
	public static final int MCP_KIND_PAID = 29;
	/**
	 * 種別ID：言語
	 */
	public static final int MCP_KIND_LANGUAGE = 30;
	/**
	 * 種別ID：購入アイテムタイプ
	 */
	public static final int MCP_KIND_PAYMENT_TYPE = 31;
	/**
	 * 種別ID：購入アイテムID
	 */
	public static final int MCP_KIND_PAYMENT_ID = 32;
	// /**
	// * 種別ID：シェアID
	// */
	// public static final int MCP_KIND_SHARE_ID = 33;
	/**
	 * 種別ID：シェアキー
	 */
	public static final int MCP_KIND_SHARE_SHAREKEY = 34;
	/**
	 * 種別ID：アクセストークン
	 */
	public static final int MCP_KIND_ACCESS_TOKEN = 35;
	/**
	 * 種別ID：性別
	 */
	public static final int MCP_KIND_GENDER = 36;
	/**
	 * 種別ID：生まれた年
	 */
	public static final int MCP_KIND_BIRTH_YEAR = 37;
	/**
	 * 種別ID：地域コード
	 */
	public static final int MCP_KIND_REGION = 38;
	/**
	 * 種別ID：行政区コード
	 */
	public static final int MCP_KIND_PROVINCE = 39;
	/**
	 * 種別ID：国コード
	 */
	public static final int MCP_KIND_COUNTRY = 40;
	/**
	 * 種別ID：サムネイルID
	 */
	public static final int MCP_KIND_THUMB_ID = 41;
	/**
	 * 種別ID：チケット識別ドメイン
	 */
	public static final int MCP_KIND_TICKET_DOMAIN = 42;
	/**
	 * 種別ID：カードのシリアル番号
	 */
	public static final int MCP_KIND_TICKET_SERIAL = 43;
	/**
	 * 種別ID：キャンペーンID
	 */
	public static final int MCP_KIND_CAMPAIGN_ID = 44;

	public static final int MCP_KIND_LATITUDE = 45;
	public static final int MCP_KIND_LONGITUDE = 46;
	public static final int MCP_KIND_DISTANCE = 47;
	public static final int MCP_KIND_INDUSTRY = 48;

	/**
	 * 種別ID：デバイストークン
	 */
	public static final int MCP_KIND_DEVICE_TOKEN = 49;
	/**
	 * 種別ID：親ノード
	 */
	public static final int MCP_KIND_PARENT_NODE = 50;
	public static final int MCP_KIND_TEMPLATE_TYPE = 51;
	public static final int MCP_KIND_TEMPLATE_ID = 52;

	public static final int MCP_KIND_SOURCE_TYPE = 53;
	public static final int MCP_KIND_STREAM_ID = 54;

	public static final int MCP_KIND_ERROR_REASON = 55;

	public static final int MCP_KIND_DOWNTIME_BEGIN = 56;
	public static final int MCP_KIND_DOWNTIME_END = 57;
	public static final int MCP_KIND_OFFLINE_COUSE = 58;

	public static final int MCP_KIND_TARGET_DATE = 59;
	public static final int MCP_KIND_PATTERN_ID = 60;
	public static final int MCP_KIND_AUDIO_ID = 61;
	public static final int MCP_KIND_FRAME_ID = 62;

	public static final int MCP_KIND_COMPLETE = 64;

	public static final int MCP_KIND_STATUS = 65;

	/**
	 * TAG：不明・Default
	 */
	public static final String MCP_TAG_UNKNOWN = "UNKNOWN";
	/**
	 * TAG：メールアドレス
	 */
	public static final String MCP_TAG_EMAIL = "email";
	/**
	 * TAG：パスワード
	 */
	public static final String MCP_TAG_PASSWORD = "password";
	/**
	 * TAG：アクティベーションキー
	 */
	public static final String MCP_TAG_ACTIVATIONKEY = "activationKey";
	/**
	 * TAG：セッションキー
	 */
	public static final String MCP_TAG_SESSIONKEY = "sessionKey";
	/**
	 * TAG：モード番号
	 */
	public static final String MCP_TAG_MODE_NO = "mode";
	/**
	 * TAG：マイチャンネルID
	 */
	public static final String MCP_TAG_MYCHANNELID = "channel";
	/**
	 * TAG：コンテンツリリース範囲
	 */
	public static final String MCP_TAG_RANGE = "range";
	/**
	 * TAG：コンテンツ対するユーザーアクション
	 */
	public static final String MCP_TAG_RATE_ACTION = "ratingAction";
	/**
	 * TAG：コンテンツ対するユーザーアクション
	 */
	public static final String MCP_TAG_PLAY_COMPLETE = "playComplete";
	/**
	 * TAG：再生した時間
	 */
	public static final String MCP_TAG_PLAY_DURATIN = "playDuration";
	/**
	 * TAG：検索用キーワード
	 */
	public static final String MCP_TAG_KEYWORD = "keyword";
	/**
	 * TAG：マイチャンネルスロットID
	 */
	public static final String MCP_TAG_MYCHANNEL_ID = "id";
	/**
	 * TAG：マイチャンネル名称
	 */
	public static final String MCP_TAG_MYCHANNEL_NAME = "name";
	/**
	 * TAG：マイチャンネルロック
	 */
	public static final String MCP_TAG_MYCHANNEL_LOCK = "lock";
	/**
	 * TAG：トラックID（ダウンロード用）
	 */
	public static final String MCP_TAG_TRACKID = "trackId";
	/**
	 * TAG：トラックID（評価用）
	 */
	public static final String MCP_TAG_RATING_TRACKID = "ratingTrackId";
	/**
	 * TAG：コンテンツビットレート指定
	 */
	public static final String MCP_TAG_QUARITY = "quarity";
	// /**
	// * TAG：ダウンロードフォーマット
	// */
	// public static final String MCP_TAG_FORMAT = "format";

	// 2011/11/10追加
	/**
	 * 種別ID：強制ログイン指定
	 */
	public static final String MCP_TAG_FORCE = "force";
	/**
	 * 種別ID：検索モード //MEMO 名前がかぶっているため、現在未使用
	 */
	public static final String MCP_TAG_MODE_SEARCH = "mode";
	/**
	 * 種別ID：JacketID
	 */
	public static final String MCP_TAG_JACKETID = "jacketId";
	/**
	 * 種別ID：音声広告ID
	 */
	public static final String MCP_TAG_ADID = "adId";
	/**
	 * 種別ID：ダウンロードデータType（音声広告・各種メッセージ）
	 */
	public static final String MCP_TAG_TYPE_DL = "type";
	/**
	 * 種別ID：評価対象となるAdvertiseID
	 */
	public static final String MCP_TAG_RATE_ADID = "ratingAdId";

	/**
	 * 引数：ユーザーアクション(評価・good)
	 */
	public static final String MCP_PARAM_STR_RATING_GOOD = "good";
	/**
	 * 引数：ユーザーアクション(評価・bad)
	 */
	public static final String MCP_PARAM_STR_RATING_BAD = "bad";
	/**
	 * 引数：ユーザーアクション(評価・skip)
	 */
	public static final String MCP_PARAM_STR_RATING_SKIP = "skip";
	/**
	 * 引数：ユーザーアクション(評価・nop)
	 */
	public static final String MCP_PARAM_STR_RATING_NOP = "nop";

	// 2011/11/10追加
	/**
	 * 引数：ログイン要求用（強制ログイン指定）
	 */
	public static final String MCP_PARAM_STR_YES = "yes";
	/**
	 * 引数：ログイン要求用（強制ログイン指定無し）
	 */
	public static final String MCP_PARAM_STR_NO = "";
	/**
	 * 引数：音声広告データ取得用（音声データ）
	 */
	public static final String MCP_PARAM_STR_AUDIO = "audio";
	/**
	 * 引数：音声広告データ取得用（画像データ）
	 */
	public static final String MCP_PARAM_STR_IMAGE = "image";
	/**
	 * 引数：メッセージデータ取得用（Welcomeメッセージ）
	 */
	public static final String MCP_PARAM_STR_WELCOME = "welcome";
	/**
	 * 引数：メッセージデータ取得用（使用許諾）
	 */
	public static final String MCP_PARAM_STR_EULA = "eula";
	/**
	 * 引数：広告表示・クリック報告用(リンク先表示)
	 */
	public static final String MCP_PARAM_STR_CLICKED = "clicked";
	/**
	 * 引数：広告表示・クリック報告用(何もしない)
	 */
	public static final String MCP_PARAM_STR_PLAYED = "played";

	// 12/15 ksu 追加
	/**
	 * TAG：メールアドレス
	 */
	public static final String MCP_TAG_BILLING_TYPE = "subscriptionType";
	/**
	 * TAG：メールアドレス
	 */
	public static final String MCP_TAG_MARKET_TYPE = "marketType";
	/**
	 * TAG：メールアドレス
	 */
	public static final String MCP_TAG_RECEIPT = "receipt";
	/**
	 * TAG：トラッキングキー
	 */
	public static final String MCP_TAG_TRACKING_KEY = "trackingKey";
	/**
	 * TAG：アイテム価格
	 */
	public static final String MCP_TAG_PAID = "paid";
	/**
	 * TAG：言語
	 */
	public static final String MCP_TAG_LANGUAGE = "lang";
	/**
	 * TAG：購入アイテムタイプ
	 */
	public static final String MCP_TAG_PAYMENT_TYPE = "paymentType";
	/**
	 * TAG：購入アイテムID
	 */
	public static final String MCP_TAG_PAYMENT_ID = "paymentId";
	// /**
	// * TAG：購入アイテムID
	// */
	// public static final String MCP_TAG_SHARE_ID = "id";
	/**
	 * TAG：購入アイテムID
	 */
	public static final String MCP_TAG_SHARE_SHAREKEY = "shareKey";
	/**
	 * TAG：購入アイテムID
	 */
	public static final String MCP_TAG_ACCESS_TOKEN = "accessToken";
	/**
	 * TAG：性別
	 */
	public static final String MCP_TAG_GENDER = "gender";
	/**
	 * TAG：生まれた年
	 */
	public static final String MCP_TAG_BIRTH_YEAR = "birthYear";
	/**
	 * TAG：地域コード
	 */
	public static final String MCP_TAG_REGION = "region";
	/**
	 * TAG：行政区コード
	 */
	public static final String MCP_TAG_PROVINCE = "province";
	/**
	 * TAG：国コード
	 */
	public static final String MCP_TAG_COUNTRY = "country";
	/**
	 * TAG：サムネイルID
	 */
	public static final String MCP_TAG_THUMB_ID = "thumbId";
	/**
	 * TAG：チケット識別ドメイン
	 */
	public static final String MCP_TAG_TICKET_DOMAIN = "ticketDomain";
	/**
	 * TAG：カードのシリアル番号
	 */
	public static final String MCP_TAG_TICKET_SERIAL = "ticketSerial";
	/**
	 * TAG：キャンペーンID
	 */
	public static final String MCP_TAG_CAMPAIGN_ID = "campaignId";

	public static final String MCP_TAG_LATITUDE = "latitude";
	public static final String MCP_TAG_LONGITUDE = "longitude";
	public static final String MCP_TAG_DISTANCE = "distance";
	public static final String MCP_TAG_INDUSTRY = "industry";

	// デバイストークン
	public static final String MCP_TAG_DEVICE_TOKEN = "deviceToken";
	// 親ノード
	public static final String MCP_TAG_PARENT_NODE = "parent";
	public static final String MCP_TAG_TEMPLATE_TYPE = "templateType";
	public static final String MCP_TAG_TEMPLATE_ID = "templateId";

	public static final String MCP_TAG_SOURCE_TYPE = "sourceType";
	public static final String MCP_TAG_STREAM_ID = "streamId";

	public static final String MCP_TAG_ERROR_REASON = "errorReason";

	public static final String MCP_TAG_DOWNTIME_BEGIN = "downtimeBegin";
	public static final String MCP_TAG_DOWNTIME_END = "downtimeEnd";
	public static final String MCP_TAG_OFFLINE_CAUSE = "offlineCause";

	public static final String MCP_TAG_TARGET_DATE = "targetDate";
	public static final String MCP_TAG_PATTERN_ID = "patternId";
	public static final String MCP_TAG_AUDIO_ID = "audioId";
	public static final String MCP_TAG_FRAME_ID = "frameId";

	public static final String MCP_TAG_COMPLETE = "complete";

	public static final String MCP_TAG_STATUS = "status";

	/**
	 * タグ名称取得
	 * 
	 * @param kind
	 * @return タグ名称
	 */
	public static String getTag(int kind) {
		String tag = null;

		switch (kind) {
		case MCP_KIND_EMAIL:
			tag = MCP_TAG_EMAIL;
			break;
		case MCP_KIND_PASSWORD:
			tag = MCP_TAG_PASSWORD;
			break;
		case MCP_KIND_ACTIVATIONKEY:
			tag = MCP_TAG_ACTIVATIONKEY;
			break;
		case MCP_KIND_SESSIONKEY:
			tag = MCP_TAG_SESSIONKEY;
			break;
		case MCP_KIND_MODE_NO:
			tag = MCP_TAG_MODE_NO;
			break;
		case MCP_KIND_MYCHANNELID:
			tag = MCP_TAG_MYCHANNELID;
			break;
		case MCP_KIND_RANGE:
			tag = MCP_TAG_RANGE;
			break;
		case MCP_KIND_RATE_ACTION:
			tag = MCP_TAG_RATE_ACTION;
			break;
		case MCP_KIND_PLAY_COMPLETE:
			tag = MCP_TAG_PLAY_COMPLETE;
			break;
		case MCP_KIND_PLAY_DURATIN:
			tag = MCP_TAG_PLAY_DURATIN;
			break;
		case MCP_KIND_KEYWORD:
			tag = MCP_TAG_KEYWORD;
			break;
		case MCP_KIND_MYCHANNEL_ID:
			tag = MCP_TAG_MYCHANNEL_ID;
			break;
		case MCP_KIND_MYCHANNEL_NAME:
			tag = MCP_TAG_MYCHANNEL_NAME;
			break;
		case MCP_KIND_MYCHANNEL_LOCK:
			tag = MCP_TAG_MYCHANNEL_LOCK;
			break;
		case MCP_KIND_TRACKID:
			tag = MCP_TAG_TRACKID;
			break;
		case MCP_KIND_RATING_TRACKID:
			tag = MCP_TAG_RATING_TRACKID;
			break;
		case MCP_KIND_QUARITY:
			tag = MCP_TAG_QUARITY;
			break;
		// case MCP_KIND_FORMAT:
		// tag = MCP_TAG_FORMAT;
		// break;

		// 2011/11/10 追加
		case MCP_KIND_FORCE:
			tag = MCP_TAG_FORCE;
			break;
		case MCP_KIND_MODE_SEARCH:
			tag = MCP_TAG_MODE_SEARCH;
			break;
		case MCP_KIND_JACKETID:
			tag = MCP_TAG_JACKETID;
			break;
		case MCP_KIND_ADID:
			tag = MCP_TAG_ADID;
			break;
		case MCP_KIND_TYPE_DL:
			tag = MCP_TAG_TYPE_DL;
			break;
		case MCP_KIND_RATE_ADID:
			tag = MCP_TAG_RATE_ADID;
			break;
		case MCP_KIND_BILLING_TYPE:
			tag = MCP_TAG_BILLING_TYPE;
			break;
		case MCP_KIND_MARKET_TYPE:
			tag = MCP_TAG_MARKET_TYPE;
			break;
		case MCP_KIND_RECEIPT:
			tag = MCP_TAG_RECEIPT;
			break;
		case MCP_KIND_TRACKING_KEY:
			tag = MCP_TAG_TRACKING_KEY;
			break;
		case MCP_KIND_PAID:
			tag = MCP_TAG_PAID;
			break;
		case MCP_KIND_LANGUAGE:
			tag = MCP_TAG_LANGUAGE;
			break;
		case MCP_KIND_PAYMENT_TYPE:
			tag = MCP_TAG_PAYMENT_TYPE;
			break;
		case MCP_KIND_PAYMENT_ID:
			tag = MCP_TAG_PAYMENT_ID;
			break;
		case MCP_KIND_SHARE_SHAREKEY:
			tag = MCP_TAG_SHARE_SHAREKEY;
			break;
		case MCP_KIND_ACCESS_TOKEN:
			tag = MCP_TAG_ACCESS_TOKEN;
			break;
		case MCP_KIND_GENDER:
			tag = MCP_TAG_GENDER;
			break;
		case MCP_KIND_BIRTH_YEAR:
			tag = MCP_TAG_BIRTH_YEAR;
			break;
		case MCP_KIND_PROVINCE:
			tag = MCP_TAG_PROVINCE;
			break;
		case MCP_KIND_REGION:
			tag = MCP_TAG_REGION;
			break;
		case MCP_KIND_COUNTRY:
			tag = MCP_TAG_COUNTRY;
			break;
		case MCP_KIND_THUMB_ID:
			tag = MCP_TAG_THUMB_ID;
			break;
		case MCP_KIND_TICKET_DOMAIN:
			tag = MCP_TAG_TICKET_DOMAIN;
			break;
		case MCP_KIND_TICKET_SERIAL:
			tag = MCP_TAG_TICKET_SERIAL;
			break;
		case MCP_KIND_CAMPAIGN_ID:
			tag = MCP_TAG_CAMPAIGN_ID;
			break;
		case MCP_KIND_LATITUDE:
			tag = MCP_TAG_LATITUDE;
			break;
		case MCP_KIND_LONGITUDE:
			tag = MCP_TAG_LONGITUDE;
			break;
		case MCP_KIND_DISTANCE:
			tag = MCP_TAG_DISTANCE;
			break;
		case MCP_KIND_INDUSTRY:
			tag = MCP_TAG_INDUSTRY;
			break;

		case MCP_KIND_DEVICE_TOKEN:
			tag = MCP_TAG_DEVICE_TOKEN;
			break;
		case MCP_KIND_PARENT_NODE:
			tag = MCP_TAG_PARENT_NODE;
			break;
		case MCP_KIND_TEMPLATE_TYPE:
			tag = MCP_TAG_TEMPLATE_TYPE;
			break;
		case MCP_KIND_TEMPLATE_ID:
			tag = MCP_TAG_TEMPLATE_ID;
			break;

		case MCP_KIND_SOURCE_TYPE:
			tag = MCP_TAG_SOURCE_TYPE;
			break;
		case MCP_KIND_STREAM_ID:
			tag = MCP_TAG_STREAM_ID;
			break;

		case MCP_KIND_ERROR_REASON:
			tag = MCP_TAG_ERROR_REASON;
			break;

		case MCP_KIND_DOWNTIME_BEGIN:
			tag = MCP_TAG_DOWNTIME_BEGIN;
			break;
		case MCP_KIND_DOWNTIME_END:
			tag = MCP_TAG_DOWNTIME_END;
			break;
		case MCP_KIND_OFFLINE_COUSE:
			tag = MCP_TAG_OFFLINE_CAUSE;
			break;

		case MCP_KIND_TARGET_DATE:
			tag = MCP_TAG_TARGET_DATE;
			break;
		case MCP_KIND_PATTERN_ID:
			tag = MCP_TAG_PATTERN_ID;
			break;
		case MCP_KIND_AUDIO_ID:
			tag = MCP_TAG_AUDIO_ID;
			break;
		case MCP_KIND_FRAME_ID:
			tag = MCP_TAG_FRAME_ID;
			break;
		case MCP_KIND_COMPLETE:
			tag = MCP_TAG_COMPLETE;
			break;
		case MCP_KIND_STATUS:
			tag = MCP_TAG_STATUS;
			break;

		default:
			break;
		}

		return tag;
	}

	/**
	 * 種別取得
	 * 
	 * @param tag
	 * @return 種別
	 */
	public static int getKind(String tag) {
		int kind = MCP_KIND_UNKNOWN;

		if (MCP_TAG_EMAIL.equals(tag)) {
			kind = MCP_KIND_EMAIL;
		} else if (MCP_TAG_PASSWORD.equals(tag)) {
			kind = MCP_KIND_PASSWORD;
		} else if (MCP_TAG_ACTIVATIONKEY.equals(tag)) {
			kind = MCP_KIND_ACTIVATIONKEY;
		} else if (MCP_TAG_SESSIONKEY.equals(tag)) {
			kind = MCP_KIND_SESSIONKEY;
		} else if (MCP_TAG_MODE_NO.equals(tag)) {
			kind = MCP_KIND_MODE_NO;
		} else if (MCP_TAG_MYCHANNELID.equals(tag)) {
			kind = MCP_KIND_MYCHANNELID;
		} else if (MCP_TAG_RANGE.equals(tag)) {
			kind = MCP_KIND_RANGE;
		} else if (MCP_TAG_RATE_ACTION.equals(tag)) {
			kind = MCP_KIND_RATE_ACTION;
		} else if (MCP_TAG_PLAY_COMPLETE.equals(tag)) {
			kind = MCP_KIND_PLAY_COMPLETE;
		} else if (MCP_TAG_PLAY_DURATIN.equals(tag)) {
			kind = MCP_KIND_PLAY_DURATIN;
		} else if (MCP_TAG_KEYWORD.equals(tag)) {
			kind = MCP_KIND_KEYWORD;
		} else if (MCP_TAG_MYCHANNEL_ID.equals(tag)) {
			kind = MCP_KIND_MYCHANNEL_ID;
		} else if (MCP_TAG_MYCHANNEL_NAME.equals(tag)) {
			kind = MCP_KIND_MYCHANNEL_NAME;
		} else if (MCP_TAG_MYCHANNEL_LOCK.equals(tag)) {
			kind = MCP_KIND_MYCHANNEL_LOCK;
		} else if (MCP_TAG_TRACKID.equals(tag)) {
			kind = MCP_KIND_TRACKID;
		} else if (MCP_TAG_RATING_TRACKID.equals(tag)) {
			kind = MCP_KIND_RATING_TRACKID;
		} else if (MCP_TAG_QUARITY.equals(tag)) {
			kind = MCP_KIND_QUARITY;
		}
		// else if (MCP_TAG_FORMAT.equals(tag)) {
		// kind = MCP_KIND_FORMAT;
		// }

		// 2011/11/10 追加
		else if (MCP_TAG_FORCE.equals(tag)) {
			kind = MCP_KIND_FORCE;
		} else if (MCP_TAG_MODE_SEARCH.equals(tag)) {
			kind = MCP_KIND_MODE_SEARCH;
		} else if (MCP_TAG_JACKETID.equals(tag)) {
			kind = MCP_KIND_JACKETID;
		} else if (MCP_TAG_ADID.equals(tag)) {
			kind = MCP_KIND_ADID;
		} else if (MCP_TAG_TYPE_DL.equals(tag)) {
			kind = MCP_KIND_TYPE_DL;
		} else if (MCP_TAG_RATE_ADID.equals(tag)) {
			kind = MCP_KIND_RATE_ADID;
		} else if (MCP_TAG_BILLING_TYPE.equals(tag)) {
			kind = MCP_KIND_BILLING_TYPE;
		} else if (MCP_TAG_MARKET_TYPE.equals(tag)) {
			kind = MCP_KIND_MARKET_TYPE;
		} else if (MCP_TAG_RECEIPT.equals(tag)) {
			kind = MCP_KIND_RECEIPT;
		} else if (MCP_TAG_TRACKING_KEY.equals(tag)) {
			kind = MCP_KIND_TRACKING_KEY;
		} else if (MCP_TAG_PAID.equals(tag)) {
			kind = MCP_KIND_PAID;
		} else if (MCP_TAG_LANGUAGE.equals(tag)) {
			kind = MCP_KIND_LANGUAGE;
		} else if (MCP_TAG_PAYMENT_TYPE.equals(tag)) {
			kind = MCP_KIND_PAYMENT_TYPE;
		} else if (MCP_TAG_PAYMENT_ID.equals(tag)) {
			kind = MCP_KIND_PAYMENT_ID;
		} else if (MCP_TAG_SHARE_SHAREKEY.equals(tag)) {
			kind = MCP_KIND_SHARE_SHAREKEY;
		} else if (MCP_TAG_ACCESS_TOKEN.equals(tag)) {
			kind = MCP_KIND_ACCESS_TOKEN;
		} else if (MCP_TAG_GENDER.equals(tag)) {
			kind = MCP_KIND_GENDER;
		} else if (MCP_TAG_BIRTH_YEAR.equals(tag)) {
			kind = MCP_KIND_BIRTH_YEAR;
		} else if (MCP_TAG_PROVINCE.equals(tag)) {
			kind = MCP_KIND_PROVINCE;
		} else if (MCP_TAG_REGION.equals(tag)) {
			kind = MCP_KIND_REGION;
		} else if (MCP_TAG_COUNTRY.equals(tag)) {
			kind = MCP_KIND_COUNTRY;
		} else if (MCP_TAG_THUMB_ID.equals(tag)) {
			kind = MCP_KIND_THUMB_ID;
		} else if (MCP_TAG_TICKET_DOMAIN.equals(tag)) {
			kind = MCP_KIND_TICKET_DOMAIN;
		} else if (MCP_TAG_TICKET_SERIAL.equals(tag)) {
			kind = MCP_KIND_TICKET_SERIAL;
		} else if (MCP_TAG_CAMPAIGN_ID.equals(tag)) {
			kind = MCP_KIND_CAMPAIGN_ID;
		} else if (MCP_TAG_LATITUDE.equals(tag)) {
			kind = MCP_KIND_LATITUDE;
		} else if (MCP_TAG_LONGITUDE.equals(tag)) {
			kind = MCP_KIND_LONGITUDE;
		} else if (MCP_TAG_DISTANCE.equals(tag)) {
			kind = MCP_KIND_DISTANCE;
		} else if (MCP_TAG_INDUSTRY.equals(tag)) {
			kind = MCP_KIND_INDUSTRY;

		} else if (MCP_TAG_DEVICE_TOKEN.equals(tag)) {
			kind = MCP_KIND_DEVICE_TOKEN;
		} else if (MCP_TAG_PARENT_NODE.equals(tag)) {
			kind = MCP_KIND_PARENT_NODE;
		} else if (MCP_TAG_TEMPLATE_TYPE.equals(tag)) {
			kind = MCP_KIND_TEMPLATE_TYPE;
		} else if (MCP_TAG_TEMPLATE_ID.equals(tag)) {
			kind = MCP_KIND_TEMPLATE_ID;

		} else if (MCP_TAG_SOURCE_TYPE.equals(tag)) {
			kind = MCP_KIND_SOURCE_TYPE;
		} else if (MCP_TAG_STREAM_ID.equals(tag)) {
			kind = MCP_KIND_STREAM_ID;

		} else if (MCP_TAG_ERROR_REASON.equals(tag)) {
			kind = MCP_KIND_ERROR_REASON;

		} else if (MCP_TAG_DOWNTIME_BEGIN.equals(tag)) {
			kind = MCP_KIND_DOWNTIME_BEGIN;
		} else if (MCP_TAG_DOWNTIME_END.equals(tag)) {
			kind = MCP_KIND_DOWNTIME_END;
		} else if (MCP_TAG_OFFLINE_CAUSE.equals(tag)) {
			kind = MCP_KIND_OFFLINE_COUSE;

		} else if (MCP_TAG_TARGET_DATE.equals(tag)) {
			kind = MCP_KIND_TARGET_DATE;
		} else if (MCP_TAG_PATTERN_ID.equals(tag)) {
			kind = MCP_KIND_PATTERN_ID;
		} else if (MCP_TAG_AUDIO_ID.equals(tag)) {
			kind = MCP_KIND_AUDIO_ID;
		} else if (MCP_TAG_FRAME_ID.equals(tag)) {
			kind = MCP_KIND_FRAME_ID;

		} else if (MCP_TAG_COMPLETE.equals(tag)) {
			kind = MCP_KIND_COMPLETE;

		} else if (MCP_TAG_STATUS.equals(tag)) {
			kind = MCP_KIND_STATUS;
		}

		return kind;
	}
}
