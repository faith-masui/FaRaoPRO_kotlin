package jp.faraopro.play.mclient;

import java.net.HttpURLConnection;

import jp.faraopro.play.common.Consts;

/**
 * MusicClient エラー管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCError {

	/**
	 * 正常終了
	 */
	public static final int MC_NO_ERROR = 200;

	/**
	 * Bad Request
	 */
	public static final int MC_BAD_REQUEST = 400;

	/**
	 * 認証エラー
	 */
	public static final int MC_UNAUTHORIZED = 401;

	/**
	 * APIアクセス権限エラー
	 */
	public static final int MC_FORBIDDEN = 403;

	/**
	 * 存在しないAPI等、不正なリソースへのアクセス
	 */
	public static final int MC_NOT_FOUND = 404;

	/**
	 * サーバー側で例外が発生
	 */
	public static final int MC_INTERNAL_SERVER_ERROR = 500;

	/**
	 * サーバーが停止、またはメンテナンス中等
	 */
	public static final int MC_BAD_GATEWAY = 502;

	/**
	 * サーバーが停止、または不可等
	 */
	public static final int MC_SERVICE_UNAVAILABLE = 503;

	/**
	 * Application独自エラー（パラメータエラー）
	 */
	public static final int MC_APPERR_PARAM = 100;
	/**
	 * Application独自エラー（Unsupported Encoding）
	 */
	public static final int MC_APPERR_UNSUPPORTED_ENC = 101;
	/**
	 * Application独自エラー（Client Protocol）
	 */
	public static final int MC_APPERR_CLIENT_PROTOCOL = 102;
	/**
	 * Application独自エラー（IO InputStream Close Error）
	 */
	public static final int MC_APPERR_IO_CLOSEERR = 103;
	/**
	 * Application独自エラー（IO HTTP）
	 */
	public static final int MC_APPERR_IO_HTTP = 104;
	/**
	 * Application独自エラー（IO キャンセル）
	 */
	public static final int MC_APPERR_CANCEL = 105;
	/**
	 * Application独自エラー（ファイルエラー）
	 */
	public static final int MC_APPERR_FILE = 106;
	/**
	 * Application独自エラー（XMLパースエラー）
	 */
	public static final int MC_APPERR_PARSE = 107;
	/**
	 * Application独自エラー（通信スレッド キューフル）
	 */
	public static final int MC_APPERR_QUEUEFULL = 108;

	public static final String MC_ERROR_REASON_UNKNOWN = "android/unknown";
	public static final String MC_ERROR_REASON_RETRY_CDN = "android/download_cdn/retry_over";
	public static final String MC_ERROR_REASON_MALFORMED = "android/cdn/malformed_url";
	public static final String MC_ERROR_REASON_HTTP = "android/cdn/track/http_error/";
	public static final String MC_ERROR_REASON_RETRY_TRACK = "android/cdn/track/retry_over";
	public static final String MC_ERROR_REASON_DECODE = "android/track/decode_failed";
	public static final String MC_ERROR_REASON_MEDIA_PLAYER = "android/player/invalid_media";

	/**
	 * エラーコード
	 */
	private int mErrorCode;

	/**
	 * エラー発生タイミング
	 */
	private int mErrorWhen;

	/**
	 * エラーメッセージ
	 */
	private String mMessage;

	private String[] mParams;

	public MCError(int errorCode, int when, String message, String[] params) {
		mErrorCode = errorCode;
		mErrorWhen = when;
		mMessage = message;
		mParams = params;
	}

	/**
	 * エラー情報設定
	 * 
	 * @param errorCode
	 * @param when
	 * @param message
	 */
	public MCError(int errorCode, int when, String message) {
		mErrorCode = errorCode;
		mErrorWhen = when;
		mMessage = message;
		mParams = null;
	}

	/**
	 * エラー情報設定
	 * 
	 * @param error
	 */
	public MCError(MCError error) {
		mErrorCode = error.getErrorCode();
		mErrorWhen = error.getErrorWhen();
		mMessage = error.getMessage();
	}

	/**
	 * エラー情報取得
	 * 
	 * @return
	 */
	public int getErrorCode() {
		return mErrorCode;
	}

	/**
	 * エラー発生タイミング情報設定
	 * 
	 * @return
	 */
	public void setErrorWhen(int when) {
		mErrorWhen = when;
	}

	/**
	 * エラー発生タイミング情報取得
	 * 
	 * @return
	 */
	public int getErrorWhen() {
		return mErrorWhen;
	}

	/**
	 * エラー情報取得
	 * 
	 * @return
	 */
	public String getMessage() {
		return mMessage;
	}

	public static String getErrorReason(int when, int statusCode) {
		String errorReason = MC_ERROR_REASON_UNKNOWN;

		switch (when) {
		case MCDefAction.MCA_KIND_DOWNLOAD_CDN:
			if (statusCode == MC_APPERR_IO_HTTP) {
				errorReason = MC_ERROR_REASON_RETRY_CDN;
			} else {
				errorReason = MC_ERROR_REASON_HTTP + statusCode;
			}
			break;
		case MCDefAction.MCA_KIND_TRACK_DL:
			// TODO magic number
			if (statusCode == -1) {
				errorReason = MC_ERROR_REASON_MALFORMED;
			} else if (statusCode == MC_APPERR_IO_HTTP) {
				errorReason = MC_ERROR_REASON_RETRY_TRACK;
			}
			break;
		case Consts.START_MUSIC:
			// TODO magic number
			if (statusCode == -2) {
				errorReason = MC_ERROR_REASON_MEDIA_PLAYER;
			}
			break;
		}

		return errorReason;
	}

	public static boolean isServerError(int api, int statusCode) {
		boolean serverError = false;

		if (statusCode < 200 || 500 <= statusCode)
			serverError = true;
		else {
			switch (api) {
			case MCDefAction.MCA_KIND_LISTEN:
			case MCDefAction.MCA_KIND_RATING:
			case MCDefAction.MCA_KIND_STREAM_PLAY:
				if (statusCode == 404)
					serverError = true;
				break;
			}
		}

		return serverError;
	}

	public static boolean isEmergencyError(int api, int responseCode) {
		switch (api) {
		case MCDefAction.MCA_KIND_LOGIN:
		case MCDefAction.MCA_KIND_STATUS:
		case MCDefAction.MCA_KIND_NETWORK_RECOVERY:
			if (responseCode < HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NOT_FOUND
					|| responseCode >= HttpURLConnection.HTTP_INTERNAL_ERROR)
				return true;
			break;
		}
		return false;
	}

}
