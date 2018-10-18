package jp.faraopro.play.app;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;

public class AudioFocusHelper {
	private static int state = 0;
	private static IAudioFocusListener mListener;

	public interface IAudioFocusListener {
        void onFocusChanged(int focusChange);
	}

	static OnAudioFocusChangeListener mAudioFocusChangeListener = new OnAudioFocusChangeListener() {
		@Override
		public void onAudioFocusChange(int focusChange) {
			state = focusChange;
			if (mListener != null)
				mListener.onFocusChanged(focusChange);
		}
	};

	public static boolean requestFocus(Context context, IAudioFocusListener focusChangeListener) {
		boolean success = false;
		mListener = focusChangeListener;
		AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		// 再生のためにオーディオフォーカスを要求します。
		int result = am.requestAudioFocus(mAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
				AudioManager.AUDIOFOCUS_GAIN);
		if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
			success = true;
			state = AudioManager.AUDIOFOCUS_GAIN;
		}

		return success;
	}

	public static void abandonFocus(Context context) {
		mListener = null;
		AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		am.abandonAudioFocus(mAudioFocusChangeListener);
		state = 0;
	}

    private static String getFocusName(int focus) {
        String name = "UNKNOWN";
        switch (focus) {
            case AudioManager.AUDIOFOCUS_GAIN:
                name = "AUDIOFOCUS_GAIN";
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                name = "AUDIOFOCUS_LOSS_TRANSIENT";
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                name = "AUDIOFOCUS_LOSS";
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                name = "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK";
                break;
        }

        return name;
    }
	public static int getFocusState() {
		return state;
	}
}
