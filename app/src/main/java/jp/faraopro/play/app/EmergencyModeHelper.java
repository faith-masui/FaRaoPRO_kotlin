package jp.faraopro.play.app;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.os.PowerManager;
import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.mclient.MCUserInfoPreference;
import jp.faraopro.play.util.Utils;

public class EmergencyModeHelper
		implements OnCompletionListener, OnErrorListener, OnInfoListener, AudioFocusHelper.IAudioFocusListener {
	private static final String FILE_DIRECTORY = "emergency";
	// private static final String FILE_DIRECTORY = "emergency_test";

	private boolean isRunning;
	private boolean isRecovery;
	private boolean isTimeout;
	private long beginTime;
	private String cause;
	private Context context;
	private EmergencyCallback listener;
	// player 関係
	private MediaPlayer player;
	private String[] fileList;
	private int trackIndex;
	// network 関係
	private NetworkStateChecker networkChecker;

	interface EmergencyCallback {
		void onRecovery(boolean isPlaying);

		void onTimeout();

		void onNext();
	}

	public EmergencyModeHelper(Context context, EmergencyCallback listener, String cause) {
		this.context = context;
		this.listener = listener;
		this.beginTime = Utils.getNowTime();
		this.cause = cause;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void startEmergencyMode() {
		isRunning = true;
		isRecovery = false;
		isTimeout = false;
		playerInitialize();
		playNext();
		setChecker();
	}

	public void stopEmergencyMode() {
		if (player != null) {
			player.release();
		}
		player = null;
		AudioFocusHelper.abandonFocus(context);
		fileList = null;
		if (networkChecker != null) {
			networkChecker.release();
		}
		networkChecker = null;
		isRecovery = false;
		isTimeout = false;
		isRunning = false;
	}

	public void play() {
		if (player != null && !player.isPlaying())
			player.start();
	}

	public void pause() {
		if (player != null && player.isPlaying())
			player.pause();
	}

	public int getLength() {
		return (player != null) ? player.getDuration() : 0;
	}

	public int getPosition() {
		return (player != null) ? player.getCurrentPosition() : 0;
	}

	public boolean isPlaying() {
		return (player != null && player.isPlaying());
	}

	public boolean isRecovery() {
		return isRecovery;
	}

	public boolean isTimeout() {
		return isTimeout;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public String getCause() {
		return cause;
	}

	public void destroy() {
		stopEmergencyMode(); // 保険
		cause = null;
		context = null;
		listener = null;
	}

	private void playerInitialize() {
		player = new MediaPlayer();
		player.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
		if (AudioFocusHelper.getFocusState() != AudioManager.AUDIOFOCUS_GAIN)
			AudioFocusHelper.requestFocus(context, this);
		float volume = FROPlayer.getGainVolume(FROPlayer.BASE_REPLAY_GAIN,
				MCUserInfoPreference.getInstance(context).getChannelVolume());
		player.setVolume(volume, volume);
		FRODebug.logD(getClass(), "volume = " + volume, true);
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
		player.setOnInfoListener(this);
		trackIndex = 0;
	}

	private void playNext() {
		AssetManager assetManager = context.getAssets();
		try {
			fileList = assetManager.list(FILE_DIRECTORY);
			AssetFileDescriptor afdescripter = assetManager.openFd(FILE_DIRECTORY + "/" + fileList[trackIndex]);
			player.setDataSource(afdescripter.getFileDescriptor(), afdescripter.getStartOffset(),
					afdescripter.getLength());
			player.prepare();
		} catch (IllegalArgumentException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private void setChecker() {
		networkChecker = new NetworkStateChecker(context, null, -1, -1);
		networkChecker.setRecoveryCallback(onRecovery);
		networkChecker.setTimeoutCallback(onTimeout);
		networkChecker.start();
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		mp.stop();
		mp.reset();

		if (isRecovery) {
			if (listener != null)
				listener.onRecovery(false);
			return;
		}

		if (isTimeout) {
			if (listener != null)
				listener.onTimeout();
			return;
		}

		if (networkChecker.getNetworkState() == NetworkStateChecker.STATE_ERROR_OCCURRED) {
			networkChecker.release();
			setChecker();
		}
		trackIndex = (trackIndex < fileList.length - 1) ? trackIndex + 1 : 0;
		playNext();
		play();
		if (listener != null)
			listener.onNext();
	}

	private final Runnable onRecovery = new Runnable() {
		@Override
		public void run() {
			if (!isRunning)
				return;
			FRODebug.logD(getClass(), "onRecovery run", true);
			if (player.isPlaying()) {
				isRecovery = true;
			}
			if (listener != null)
				listener.onRecovery(player.isPlaying());
		}
	};

	private final Runnable onTimeout = new Runnable() {
		@Override
		public void run() {
			if (!isRunning)
				return;
			if (player.isPlaying()) {
				isTimeout = true;
			} else {
				if (listener != null)
					listener.onTimeout();
			}
		}
	};
	private static final float GAIN_DIV_VALUE = 5f;

	@Override
	public void onFocusChanged(int focusChange) {
		switch (focusChange) {
		case AudioManager.AUDIOFOCUS_GAIN:
			if (player != null) {
				float volume = FROPlayer.getGainVolume(FROPlayer.BASE_REPLAY_GAIN,
						MCUserInfoPreference.getInstance(context).getChannelVolume());
				player.setVolume(volume, volume);
				FRODebug.logD(getClass(), "volume = " + volume, true);
			}
			break;
		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
		case AudioManager.AUDIOFOCUS_LOSS:
		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
			if (player != null) {
				float volume = FROPlayer.getGainVolume(FROPlayer.BASE_REPLAY_GAIN,
						MCUserInfoPreference.getInstance(context).getChannelVolume());
				volume = volume / GAIN_DIV_VALUE;
				player.setVolume(volume, volume);
				FRODebug.logD(getClass(), "volume = " + volume, true);
			}
			break;
		}
	}
}
