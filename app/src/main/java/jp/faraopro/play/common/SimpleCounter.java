package jp.faraopro.play.common;

import jp.faraopro.play.util.SimpleTimer2;

/**
 * 1秒単位で時間をカウントするクラス
 * 
 * @author AIM Corporation
 * 
 */
public class SimpleCounter implements SimpleTimer2.TimerListener {
	private static final Object lock = new Object();
	private SimpleTimer2 mTimer;
	private int count = 0;

	/**
	 * コンストラクタ
	 */
	public SimpleCounter() {
		mTimer = new SimpleTimer2(this, true);
	}

	/**
	 * 開始
	 */
	public void start() {
		synchronized (lock) {
			if (mTimer == null) {
				mTimer = new SimpleTimer2(this, true);
			}
			mTimer.start(1000 * 1);
		}
	}

	/**
	 * 一時停止 start()で再開
	 */
	public void pause() {
		synchronized (lock) {
			if (mTimer != null) {
				mTimer.pause();
			}
		}
	}

	/**
	 * リセット start()で再開、カウントは0から
	 */
	public void reset() {
		pause();
		count = 0;
	}

	/**
	 * 停止
	 */
	public void stop() {
		if (mTimer != null) {
			mTimer.stop();
		}
		mTimer = null;
		count = 0;
	}

	/**
	 * 経過時間を返す
	 * 
	 * @return 経過時間
	 */
	public int getCount() {
		return count;
	}

	@Override
	public void onTimerEvent(int type) {
		count++;
	}
}
