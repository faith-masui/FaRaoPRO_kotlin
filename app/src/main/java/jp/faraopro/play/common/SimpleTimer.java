package jp.faraopro.play.common;

import java.util.Timer;
import java.util.TimerTask;

/**
 * タイマークラス
 * 
 * @author ksu
 * 
 */
public class SimpleTimer {
	protected int mInterval;
	protected int mType;
	protected Timer mTimer;
	protected ITimerListener mListener;
	protected boolean mLoop;

	/**
	 * コンストラクタ
	 * 
	 * @param listener
	 *            : リスナー
	 * @param loop
	 *            : ループするか否か
	 */
	public SimpleTimer(ITimerListener listener, boolean loop) {
		mListener = listener;
		mLoop = loop;
	}

	private class TimerThread extends TimerTask {
		public TimerThread() {
		}

		@Override
		public void run() {
			if (mListener != null)
				mListener.onTimerEvent(mType);
			if (!mLoop) {
				stop();
			}
			if (mTimer != null) {
				mTimer.schedule(new TimerThread(), mInterval);
			}
		}
	}

	/**
	 * 開始
	 * 
	 * @param msec
	 *            : 間隔
	 */
	public void start(int msec) {
		if (mTimer == null) {
			mTimer = new Timer();
		}
		if (msec < 0)
			msec = 0;
		mInterval = msec;
		mTimer.schedule(new TimerThread(), mInterval);
	}

	/**
	 * 停止
	 */
	public void stop() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}

	/**
	 * タイマーの判別用にIDを割り当てる
	 * 
	 * @param type
	 *            ID
	 */
	public void setType(int type) {
		mType = type;
	}
}
