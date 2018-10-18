package jp.faraopro.play.app;

import android.content.Context;
import android.text.TextUtils;

import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.util.FROUtils;
import jp.faraopro.play.util.Utils;

public class NetworkStateChecker extends Thread {
	private static final boolean DEBUG = true;
	public static final int STATE_DEVICE_OFFLINE = 1;
	public static final int STATE_SERVER_OFFLINE = 2;
	public static final int STATE_CDN_OFFLINE = 3;
	public static final int STATE_HLS_OFFLINE = 4;
	public static final int STATE_RESTORED = 10;
	public static final int STATE_ERROR_OCCURRED = 11;
	public static final String ERROR_CAUSES_UNKNOWN = "android/unknown";
	public static final String ERROR_CAUSES_LISTEN_RETRY_OVER = "android/radio_listen/retry_over";
	public static final String ERROR_CAUSES_RATING_RETRY_OVER = "android/radio_rating/retry_over";
	public static final String ERROR_CAUSES_CDN_RETRY_OVER = "android/download_cdn/retry_over";
	public static final String ERROR_CAUSES_MALFORMED_URL = "android/cdn/malformed_url";
	public static final String ERROR_CAUSES_HTTP = "android/cdn/track/http_error/";
	public static final String ERROR_CAUSES_TRACK_RETRY_OVER = "android/cdn/track/retry_over";
	public static final String ERROR_CAUSES_DECODE_FAILED = "android/track/decode_failed";
	public static final String ERROR_CAUSES_IMVALID_MEDIA = "android/player/invalid_media";
	private static final int CHECK_INTERVAL = 60 * 1000;
	private boolean running = true;
	private boolean deviceNetworkState = false;
	private boolean serverNetworkState = false;
	private boolean externalNetworkState = false;
	private long beginTime = -1;
	private long endTime;
	private int playerType = FROPlayer.PLAYER_TYPE_NONE;
	private String cause;
	private String externalUrl;
	private Context context;
	private Runnable onRecovery;
	private Runnable onTimeout;

	// private Runnable success;
	// private Runnable error;

	@SuppressWarnings("unused")
	private NetworkStateChecker() {

	}

	public NetworkStateChecker(Context context, String cause, String externalurl, int playerType, int beginTime) {
		this.context = context;
		this.cause = cause;
		this.playerType = playerType;
		this.externalUrl = externalurl;
		this.beginTime = beginTime;
	}

	public NetworkStateChecker(Context context, String cause, int playerType, int beginTime) {
		this(context, cause, null, playerType, beginTime);
	}

	public void setRecoveryCallback(Runnable callback) {
		onRecovery = callback;
	}

	public void setTimeoutCallback(Runnable callback) {
		onTimeout = callback;
	}

	@Override
	public synchronized void start() {
		running = true;
		super.start();
	}

	@Override
	public void run() {
		long interval = CHECK_INTERVAL;
		long past;
		if (beginTime < 0)
			beginTime = Utils.getNowTime();
		while (running && context != null && !(deviceNetworkState = Utils.getNetworkState(context))) {
			FRODebug.logD(getClass(), "deviceNetworkState = " + deviceNetworkState, DEBUG);
			past = Utils.getNowTime() - beginTime;
			if (past >= FROHandler.OFFLINE_PLAYING_LIMIT) {
				running = false;
				if (onTimeout != null)
					onTimeout.run();
				return;
			}
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
				release();
				running = false;
				return;
			}
		}

		while (running && context != null && !serverNetworkState) {
			past = Utils.getNowTime() - beginTime;
			if (past >= FROHandler.OFFLINE_PLAYING_LIMIT) {
				running = false;
				if (onTimeout != null)
					onTimeout.run();
				return;
			}
			if (FROUtils.ping() == 200) {
				serverNetworkState = true;
			} else {
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
					release();
					running = false;
					return;
				}
			}
		}

		if (externalUrl != null) {
			while (running && context != null && !externalNetworkState) {
				if (Utils.getNowTime() - beginTime >= FROHandler.OFFLINE_PLAYING_LIMIT) {
					running = false;
					if (onTimeout != null)
						onTimeout.run();
					return;
				}
				if (Utils.checkServerResponse(externalUrl) == 200) {
					externalNetworkState = true;
				} else {
					try {
						Thread.sleep(CHECK_INTERVAL);
					} catch (InterruptedException e) {
						e.printStackTrace();
						release();
						running = false;
						return;
					}
				}
			}
		}

		endTime = Utils.getNowTime();
		running = false;
		if (onRecovery != null) {
			onRecovery.run();
			onRecovery = null;
		}
	}

	public void release() {
		context = null;
		onRecovery = null;
		running = false;
		// success = null;
		// error = null;
	}

	public boolean isRunning() {
		return running;
	}

	public int getNetworkState() {
		// いずれかのネットワークが切断状態で、スレッドが動いていない場合
		if (!running && (!deviceNetworkState || !serverNetworkState))
			return STATE_ERROR_OCCURRED;
		// 端末のネットワークが切断状態
		if (!deviceNetworkState)
			return STATE_DEVICE_OFFLINE;
		// サーバのネットワークが切断状態
		if (!serverNetworkState)
			return STATE_SERVER_OFFLINE;
		if (!externalNetworkState && !TextUtils.isEmpty(externalUrl))
			return STATE_HLS_OFFLINE;
		// いずれにも該当しない場合は復旧と判定
		return STATE_RESTORED;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public String getCause() {
		return cause;
	}

	public int getPlayerType() {
		return playerType;
	}

	public void setPlayerType(int type) {
		playerType = type;
	}
}
