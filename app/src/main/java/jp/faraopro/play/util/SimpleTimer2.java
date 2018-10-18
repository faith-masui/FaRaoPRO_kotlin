package jp.faraopro.play.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 指定時間後に任意の動作を行うためのクラス<br>
 * 
 * @author AIM Corporation
 * 
 */
public class SimpleTimer2 {
	protected int mInterval; // タイマーの更新間隔
	protected int mType; // タイマーの種別
	protected Timer mTimer;
	protected TimerListener mListener; // タイマーイベントのリスナー
	protected boolean mLoop; // ループの可否
	protected boolean isPause = false;

	/**
	 * {@link SimpleTimer2}からの通知を受け取るリスナー
	 * 
	 * @author AIM Corporation
	 * 
	 */
	public interface TimerListener {
		/**
		 * 指定時間経過時のコールバック関数
		 * 
		 * @param type
		 *            タイマーの種類
		 */
		void onTimerEvent(int type);
	}

	/**
	 * 
	 * @param listener
	 *            リスナー
	 * @param loop
	 *            ループの可否
	 */
	public SimpleTimer2(TimerListener listener, boolean loop) {
		mListener = listener;
		mLoop = loop;
	}

	/**
	 * 
	 * @param listener
	 *            リスナー
	 * @param loop
	 *            ループの可否
	 * @param type
	 *            タイマーの種類、ここで設定した値がコールバック関数に渡される
	 */
	public SimpleTimer2(TimerListener listener, boolean loop, int type) {
		mListener = listener;
		mLoop = loop;
		mType = type;
	}

	private class TimerThread extends TimerTask {
		public TimerThread() {
		}

		@Override
		public void run() {
			if (mListener != null)
				mListener.onTimerEvent(mType);
			if (!mLoop || mListener == null) {
				stop();
			} else {
				if (mTimer != null)
					mTimer.schedule(new TimerThread(), mInterval);
			}
		}
	}

	/**
	 * タイマーの開始
	 * 
	 * @param msec
	 *            指定時間
	 */
	public void start(int msec) {
		if (mTimer != null)
			mTimer.cancel();
		mTimer = new Timer();
		if (msec < 0)
			msec = 0;
		mInterval = msec;
		isPause = false;
		mTimer.schedule(new TimerThread(), mInterval);
	}

	/**
	 * タイマーの一時停止<br>
	 * 一時停止後は経過時間がリセットされる
	 */
	public void pause() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
			isPause = true;
		}
	}

	/**
	 * タイマーの終了<br>
	 * このメソッド呼び出し後は再利用不可
	 */
	public void stop() {
		if (mTimer != null)
			mTimer.cancel();

		mTimer = null;
		mListener = null;
	}
}
