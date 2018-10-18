package jp.faraopro.play.mclient;

/**
 * Post Action (API) 関連定義群
 * 
 * @author AIM Corporation
 * 
 */
public class MCDefAction {

	/**
	 * 不明・Default
	 */
	public static final int MCA_KIND_UNKNOWN = 0;
	/**
	 * 種別ID：新規登録
	 */
	public static final int MCA_KIND_SIGNUP = 1;
	/**
	 * 種別ID：有効化
	 */
	public static final int MCA_KIND_ACTIVATION = 2;
	/**
	 * 種別ID：ログイン
	 */
	public static final int MCA_KIND_LOGIN = 3;
	/**
	 * 種別ID：ログアウト
	 */
	public static final int MCA_KIND_LOGOUT = 4;
	/**
	 * 種別ID：ユーザーステータス取得
	 */
	public static final int MCA_KIND_STATUS = 5;
	/**
	 * 種別ID：プレイリスト取得
	 */
	public static final int MCA_KIND_LISTEN = 6;
	/**
	 * 種別ID：楽曲評価設定
	 */
	public static final int MCA_KIND_RATING = 7;
	/**
	 * 種別ID：チャンネルリスト取得
	 */
	public static final int MCA_KIND_LIST = 8;
	/**
	 * 種別ID：チャンネル検索
	 */
	public static final int MCA_KIND_SEARCH = 9;
	/**
	 * 種別ID：マイチャンネル設定
	 */
	public static final int MCA_KIND_SET = 10;
	/**
	 * 種別ID：楽曲データ取得
	 */
	public static final int MCA_KIND_TRACK_DL = 11;
	/**
	 * 種別ID：アートワーク取得
	 */
	public static final int MCA_KIND_ARTWORK_DL = 12;
	/**
	 * 種別ID：ネットワーク疎通確認
	 */
	public static final int MCA_KIND_PING = 13;

	// 2011/11/10 追加
	/**
	 * 種別ID：チャンネル保存
	 */
	public static final int MCA_KIND_SAVE = 14;
	/**
	 * 種別ID：音声広告取得
	 */
	public static final int MCA_KIND_AD_DL = 15;
	/**
	 * 種別ID：各種メッセージ取得
	 */
	public static final int MCA_KIND_MSG_DL = 16;
	/**
	 * 種別ID：広告取得
	 */
	public static final int MCA_KIND_AD_LIST = 17;
	/**
	 * 種別ID：広告結果通知
	 */
	public static final int MCA_KIND_AD_RATING = 18;
	/**
	 * 種別ID：課金完了通知(プレミアム)
	 */
	public static final int MCA_KIND_PAYMENT_SUB = 19;
	/**
	 * 種別ID：課金完了通知(追加)
	 */
	public static final int MCA_KIND_PAYMENT_ITEM = 20;
	/**
	 * 種別ID：課金前施錠
	 */
	public static final int MCA_KIND_PAYMENT_LOCK = 21;
	/**
	 * 種別ID：課金適用
	 */
	public static final int MCA_KIND_PAYMENT_COMMIT = 22;
	/**
	 * 種別ID：課金中断
	 */
	public static final int MCA_KIND_PAYMENT_CANCEL = 23;
	/**
	 * 種別ID：楽曲評価設定(返り値なし)
	 */
	public static final int MCA_KIND_RATING_OFFLINE = 24;
	/**
	 * 種別ID：チャンネルのシェア
	 */
	public static final int MCA_KIND_CHANNEL_SHARE = 25;
	/**
	 * 種別ID：チャンネルの展開
	 */
	public static final int MCA_KIND_CHANNEL_EXPAND = 26;
	/**
	 * 種別ID：Facebookログイン(アカウントチェック)
	 */
	public static final int MCA_KIND_FACEBOOK_LOOKUP = 27;
	/**
	 * 種別ID：Facebookログイン(ログイン)
	 */
	public static final int MCA_KIND_FACEBOOK_LOGIN = 28;
	/**
	 * 種別ID：Facebookログイン(アカウント作成)
	 */
	public static final int MCA_KIND_FACEBOOK_ACCOUNT = 31;
	/**
	 * 種別ID：オススメリスト取得
	 */
	public static final int MCA_KIND_FEATURED = 29;
	/**
	 * 種別ID：地域マスタ取得
	 */
	public static final int MCA_KIND_LOCATION = 30;
	/**
	 * 種別ID：サムネイル取得
	 */
	public static final int MCA_KIND_THUMB_DL = 32;
	/**
	 * 種別ID：チケット参照
	 */
	public static final int MCA_KIND_TICKET_CHECK = 33;
	/**
	 * 種別ID：チケット追加
	 */
	public static final int MCA_KIND_TICKET_ADD = 34;

	public static final int MCA_KIND_SEARCH_SHOP = 35;

	public static final int MCA_KIND_DOWNLOAD_CDN = 36;

	public static final int MCA_KIND_LICENSE_STATUS = 37;
	public static final int MCA_KIND_LICENSE_INSTALL = 38;
	public static final int MCA_KIND_LICENSE_TRACKING = 39;
	public static final int MCA_KIND_TEMPLATE_LIST = 40;
	public static final int MCA_KIND_TEMPLATE_DOWNLOAD = 41;

	public static final int MCA_KIND_STREAM_LIST = 42;
	public static final int MCA_KIND_STREAM_PLAY = 43;
	public static final int MCA_KIND_STREAM_LOGGING = 44;
	public static final int MCA_KIND_STREAM_LOGGING_OFFLINE = 45;

	public static final int MCA_KIND_NETWORK_RECOVERY = 46;
	public static final int MCA_KIND_TEMPLATE_UPDATE = 47;

	public static final int MCA_KIND_PATTERN_SCHEDULE = 48;
	public static final int MCA_KIND_PATTERN_DOWNLOAD = 49;
	public static final int MCA_KIND_PATTERN_UPDATE = 50;
	public static final int MCA_KIND_PATTERN_ONAIR = 51;
	public static final int MCA_KIND_PATTERN_ONAIR_OFFLINE = 52;

	/**
	 * 楽曲の再生が継続できなくなるような致命的なエラーかどうかの判断
	 * 
	 * @param when
	 *            エラーが発生した動作
	 * @param code
	 *            エラー内容
	 * @return true:継続不可能な致命的エラーの場合, false:それ以外の場合
	 */
	public static boolean isFatalMusicError(int when, int code) {
		boolean isFatal = false;

		switch (when) {
		case MCA_KIND_UNKNOWN:
		case MCA_KIND_RATING:
		case MCA_KIND_TRACK_DL:
		case MCA_KIND_STREAM_PLAY:
		case MCA_KIND_LISTEN:
		case MCA_KIND_DOWNLOAD_CDN:
			isFatal = true;
			break;
		}
		switch (code) {
		case MCError.MC_UNAUTHORIZED:
		case MCError.MC_FORBIDDEN:
		case MCError.MC_INTERNAL_SERVER_ERROR:
		case MCError.MC_APPERR_FILE:
			isFatal = true;
			break;
		}

		return isFatal;
	}

	/**
	 * 不明・Default
	 */
	public static final String MCA_API_UNKNOWN = "";
	/**
	 * API：新規登録
	 */
	public static final String MCA_API_SIGNUP = "/registration/signup";
	/**
	 * API：有効化
	 */
	public static final String MCA_API_ACTIVATION = "/registration/activation";
	/**
	 * API：ログイン
	 */
	public static final String MCA_API_LOGIN = "/auth/login_business";
	/**
	 * API：ログアウト
	 */
	public static final String MCA_API_LOGOUT = "/auth/logout";
	/**
	 * API：ユーザーステータス取得
	 */
	public static final String MCA_API_STATUS = "/auth/status";
	/**
	 * API：プレイリスト取得
	 */
	public static final String MCA_API_LISTEN = "/radio/listen";
	/**
	 * API：楽曲評価設定
	 */
	public static final String MCA_API_RATING = "/radio/rating";
	/**
	 * API：チャンネルリスト取得
	 */
	public static final String MCA_API_LIST = "/channel/list";
	/**
	 * API：チャンネル検索
	 */
	public static final String MCA_API_SEARCH = "/channel/search";
	/**
	 * API：マイチャンネル設定
	 */
	public static final String MCA_API_SET = "/channel/set";
	/**
	 * API：楽曲データ取得
	 */
	public static final String MCA_API_TRACK_DL = "/download/track";
	/**
	 * API：アートワーク取得
	 */
	public static final String MCA_API_ARTWORK_DL = "/download/jacket";
	/**
	 * API：ネットワーク疎通確認
	 */
	public static final String MCA_API_PING = "/network/ping";

	// 2011/11/10 追加
	/**
	 * API：チャンネル保存
	 */
	public static final String MCA_API_SAVE = "/channel/save";
	/**
	 * API：音声広告取得
	 */
	public static final String MCA_API_AD_DL = "/download/ad";
	/**
	 * API：各種メッセージ取得
	 */
	public static final String MCA_API_MSG_DL = "/download/message";
	/**
	 * API：広告取得
	 */
	public static final String MCA_API_AD_LIST = "/ad/list";
	/**
	 * API：広告結果通知
	 */
	public static final String MCA_API_AD_RATING = "/ad/rating";
	/**
	 * API：課金完了通知
	 */
	public static final String MCA_API_PAYMENT_SUB = "/payment/subscription";
	/**
	 * API：課金完了通知
	 */
	public static final String MCA_API_PAYMENT_ITEM = "/payment/item";
	/**
	 * API：課金完了通知
	 */
	public static final String MCA_API_PAYMENT_LOCK = "/payment/lock";
	/**
	 * API：課金完了通知
	 */
	public static final String MCA_API_PAYMENT_COMMIT = "/payment/commit";
	/**
	 * API：課金完了通知
	 */
	public static final String MCA_API_PAYMENT_CANCEL = "/payment/cancel";
	/**
	 * API：楽曲評価設定
	 */
	public static final String MCA_API_RATING_OFFLINE = "/radio/rating_offline";
	/**
	 * API：チャンネルのシェア
	 */
	public static final String MCA_API_CHANNEL_SHARE = "/channel/share";
	/**
	 * API：チャンネルの展開
	 */
	public static final String MCA_API_CHANNEL_EXPAND = "/channel/expand";
	/**
	 * API：Facebookログイン(アカウントチェック)
	 */
	public static final String MCA_API_FACEBOOK_LOOKUP = "/registration/lookup";
	/**
	 * API：Facebookログイン(ログイン)
	 */
	public static final String MCA_API_FACEBOOK_LOGIN = "/auth/login_facebook";
	/**
	 * API：Facebookログイン(アカウント作成)
	 */
	public static final String MCA_API_FACEBOOK_ACCONUT = "/registration/signup_activation";
	/**
	 * API：オススメリスト取得
	 */
	public static final String MCA_API_FEATURED = "/channel/featured";
	/**
	 * API：地域マスタ取得
	 */
	public static final String MCA_API_LOCATION = "/registration/location";
	/**
	 * API：サムネイル取得
	 */
	public static final String MCA_API_THUMB_DL = "/download/thumb";
	/**
	 * API：チケット参照
	 */
	public static final String MCA_API_TICKET_CHECK = "/ticket/check";
	/**
	 * API：チケット追加
	 */
	public static final String MCA_API_TICKET_ADD = "/ticket/add";

	public static final String MCA_API_SEARCH_SHOP = "/search/shop";

	public static final String MCA_API_DOWNLOAD_CDN = "/download/cdn";

	public static final String MCA_API_LICENSE_STATUS = "/license/status";
	public static final String MCA_API_LICENSE_INSTALL = "/license/install";
	public static final String MCA_API_LICENSE_TRACKING = "/license/tracking";
	public static final String MCA_API_TEMPLATE_LIST = "/template/list";
	public static final String MCA_API_TEMPLATE_DOWNLOAD = "/template/download";

	public static final String MCA_API_STREAM_LIST = "/stream/list";
	public static final String MCA_API_STREAM_PLAY = "/stream/play";
	public static final String MCA_API_STREAM_LOGGING = "/stream/logging";
	public static final String MCA_API_STREAM_LOGGING_OFFLINE = "/stream/logging_offline";

	public static final String MCA_API_NETWORK_RECOVERY = "/network/recovery";
	public static final String MCA_API_TEMPLATE_UPDATE = "/template/update";

	public static final String MCA_API_PATTERN_SCHEDULE = "/pattern/schedule";
	public static final String MCA_API_PATTERN_DOWNLOAD = "/pattern/download";
	public static final String MCA_API_PATTERN_UPDATE = "/pattern/update";
	public static final String MCA_API_PATTERN_ONAIR = "/pattern/onair";
	public static final String MCA_API_PATTERN_ONAIR_OFFLINE = "/pattern/onair_offline";

	/**
	 * API名称取得
	 * 
	 * @param kind
	 * @return API名称
	 */
	public static String getApi(int kind) {
		String api = null;

		switch (kind) {
		case MCA_KIND_SIGNUP:
			api = MCA_API_SIGNUP;
			break;
		case MCA_KIND_ACTIVATION:
			api = MCA_API_ACTIVATION;
			break;
		case MCA_KIND_LOGIN:
			api = MCA_API_LOGIN;
			break;
		case MCA_KIND_LOGOUT:
			api = MCA_API_LOGOUT;
			break;
		case MCA_KIND_STATUS:
			api = MCA_API_STATUS;
			break;
		case MCA_KIND_LISTEN:
			api = MCA_API_LISTEN;
			break;
		case MCA_KIND_RATING:
			api = MCA_API_RATING;
			break;
		case MCA_KIND_LIST:
			api = MCA_API_LIST;
			break;
		case MCA_KIND_SEARCH:
			api = MCA_API_SEARCH;
			break;
		case MCA_KIND_SET:
			api = MCA_API_SET;
			break;
		case MCA_KIND_TRACK_DL:
			api = MCA_API_TRACK_DL;
			break;
		case MCA_KIND_ARTWORK_DL:
			api = MCA_API_ARTWORK_DL;
			break;
		case MCA_KIND_PING:
			api = MCA_API_PING;
			break;
		// 2011/11/10 追加
		case MCA_KIND_SAVE:
			api = MCA_API_SAVE;
			break;
		case MCA_KIND_AD_DL:
			api = MCA_API_AD_DL;
			break;
		case MCA_KIND_MSG_DL:
			api = MCA_API_MSG_DL;
			break;
		case MCA_KIND_AD_LIST:
			api = MCA_API_AD_LIST;
			break;
		case MCA_KIND_AD_RATING:
			api = MCA_API_AD_RATING;
			break;
		case MCA_KIND_PAYMENT_SUB:
			api = MCA_API_PAYMENT_SUB;
			break;
		case MCA_KIND_PAYMENT_ITEM:
			api = MCA_API_PAYMENT_ITEM;
			break;
		case MCA_KIND_PAYMENT_LOCK:
			api = MCA_API_PAYMENT_LOCK;
			break;
		case MCA_KIND_PAYMENT_COMMIT:
			api = MCA_API_PAYMENT_COMMIT;
			break;
		case MCA_KIND_PAYMENT_CANCEL:
			api = MCA_API_PAYMENT_CANCEL;
			break;
		case MCA_KIND_RATING_OFFLINE:
			api = MCA_API_RATING_OFFLINE;
			break;

		case MCA_KIND_CHANNEL_SHARE:
			api = MCA_API_CHANNEL_SHARE;
			break;
		case MCA_KIND_CHANNEL_EXPAND:
			api = MCA_API_CHANNEL_EXPAND;
			break;
		case MCA_KIND_FACEBOOK_LOOKUP:
			api = MCA_API_FACEBOOK_LOOKUP;
			break;
		case MCA_KIND_FACEBOOK_LOGIN:
			api = MCA_API_FACEBOOK_LOGIN;
			break;
		case MCA_KIND_FACEBOOK_ACCOUNT:
			api = MCA_API_FACEBOOK_ACCONUT;
			break;
		case MCA_KIND_FEATURED:
			api = MCA_API_FEATURED;
			break;
		case MCA_KIND_LOCATION:
			api = MCA_API_LOCATION;
			break;
		case MCA_KIND_THUMB_DL:
			api = MCA_API_THUMB_DL;
			break;
		case MCA_KIND_TICKET_CHECK:
			api = MCA_API_TICKET_CHECK;
			break;
		case MCA_KIND_TICKET_ADD:
			api = MCA_API_TICKET_ADD;
			break;
		case MCA_KIND_SEARCH_SHOP:
			api = MCA_API_SEARCH_SHOP;
			break;

		case MCA_KIND_DOWNLOAD_CDN:
			api = MCA_API_DOWNLOAD_CDN;
			break;

		case MCA_KIND_LICENSE_STATUS:
			api = MCA_API_LICENSE_STATUS;
			break;
		case MCA_KIND_LICENSE_INSTALL:
			api = MCA_API_LICENSE_INSTALL;
			break;
		case MCA_KIND_LICENSE_TRACKING:
			api = MCA_API_LICENSE_TRACKING;
			break;
		case MCA_KIND_TEMPLATE_LIST:
			api = MCA_API_TEMPLATE_LIST;
			break;
		case MCA_KIND_TEMPLATE_DOWNLOAD:
			api = MCA_API_TEMPLATE_DOWNLOAD;
			break;

		case MCA_KIND_STREAM_LIST:
			api = MCA_API_STREAM_LIST;
			break;
		case MCA_KIND_STREAM_PLAY:
			api = MCA_API_STREAM_PLAY;
			break;
		case MCA_KIND_STREAM_LOGGING:
			api = MCA_API_STREAM_LOGGING;
			break;
		case MCA_KIND_STREAM_LOGGING_OFFLINE:
			api = MCA_API_STREAM_LOGGING_OFFLINE;
			break;

		case MCA_KIND_NETWORK_RECOVERY:
			api = MCA_API_NETWORK_RECOVERY;
			break;
		case MCA_KIND_TEMPLATE_UPDATE:
			api = MCA_API_TEMPLATE_UPDATE;
			break;
		case MCA_KIND_PATTERN_SCHEDULE:
			api = MCA_API_PATTERN_SCHEDULE;
			break;
		case MCA_KIND_PATTERN_DOWNLOAD:
			api = MCA_API_PATTERN_DOWNLOAD;
			break;
		case MCA_KIND_PATTERN_UPDATE:
			api = MCA_API_PATTERN_UPDATE;
			break;
		case MCA_KIND_PATTERN_ONAIR:
			api = MCA_API_PATTERN_ONAIR;
			break;
		case MCA_KIND_PATTERN_ONAIR_OFFLINE:
			api = MCA_API_PATTERN_ONAIR_OFFLINE;
			break;

		default:
			api = "UNKNOWN";
			break;
		}

		return api;
	}

	/**
	 * 種別取得
	 * 
	 * @param api
	 * @return 種別
	 */
	public static int getKind(String api) {
		int kind = MCA_KIND_UNKNOWN;
		return kind;
	}

	/**
	 * ステータスコードは200だが、XMLにメッセージが別途はいっているか否か？
	 * 
	 * @param kind
	 * @return
	 */
	public static boolean isIncXMLResMSG(int kind) {
		// レスポンスのXMLからメッセージを抽出し、エラー判断する必要があるか否か？
		switch (kind) {
		case MCDefAction.MCA_KIND_SIGNUP:
		case MCDefAction.MCA_KIND_ACTIVATION:
		case MCDefAction.MCA_KIND_LOGIN:
		case MCDefAction.MCA_KIND_CHANNEL_EXPAND:
		case MCDefAction.MCA_KIND_FACEBOOK_LOOKUP:
		case MCDefAction.MCA_KIND_FACEBOOK_LOGIN:
		case MCDefAction.MCA_KIND_FACEBOOK_ACCOUNT:
		case MCDefAction.MCA_KIND_TICKET_CHECK:
		case MCDefAction.MCA_KIND_TICKET_ADD:
			return true;
		default:
			return false;
		}
	}

	public static boolean isAnalyzeXml(int kind) {
		switch (kind) {
		case MCDefAction.MCA_KIND_TRACK_DL:
		case MCDefAction.MCA_KIND_ARTWORK_DL:
		case MCDefAction.MCA_KIND_LOGOUT:
			// case MCDefAction.MCA_KIND_STATUS: // 今後課金制度に伴う情報等がXMLに含まれる予定
		case MCDefAction.MCA_KIND_SET:
		case MCDefAction.MCA_KIND_PING:
		case MCDefAction.MCA_KIND_SAVE:
		case MCDefAction.MCA_KIND_AD_DL:
			// case MCDefAction.MCA_KIND_PAYMENT_SUB:
			// case MCDefAction.MCA_KIND_PAYMENT_ITEM:
			// case MCDefAction.MCA_KIND_PAYMENT_LOCK:
			// case MCDefAction.MCA_KIND_PAYMENT_COMMIT:
			// case MCDefAction.MCA_KIND_PAYMENT_CANCEL:
		case MCDefAction.MCA_KIND_RATING_OFFLINE:
		case MCDefAction.MCA_KIND_THUMB_DL:
			return false; // XMLデータ無
		default:
			return true;
		}
	}
}
