package jp.faraopro.play.mclient;

import java.util.concurrent.ArrayBlockingQueue;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import jp.faraopro.play.mclient.MCHttpClient.IMCHttpClientListener;

/**
 * Musicサーバーアクセス実行クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MusicClientAction {
	// private static Object mHDRLock = new Object();
	// private static Object mACTLock = new Object();
	private static final int MC_THREAD_QUEUE_CAPACITY = 15;
	private MCThreadExQueue mThreadExQueue = null;

	// private final Handler mHandler;

	private final LooperThread lThread; // ksu追記

	/**
	 * コンストラクタ
	 */
	public MusicClientAction() {
		try {
			lclear();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mThreadExQueue = new MCThreadExQueue(MC_THREAD_QUEUE_CAPACITY);
		// mHDRLock = new Object();
		// mACTLock = new Object();

		lThread = new LooperThread();
		lThread.start();
	}

	/**
	 * 終了処理
	 */
	public void term() {
		// if( Looper.myLooper() != null ) {
		// Looper.myLooper().quit();
		// }
		try {
			lclear();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// mHDRLock = null;
		// mACTLock = null;
	}

	public void cancelAPIs() throws InterruptedException {
		lclear();
		mThreadExQueue = new MCThreadExQueue(MC_THREAD_QUEUE_CAPACITY);
	}

	private void lclear() throws InterruptedException {
		if (mThreadExQueue != null) {
			MCThreadEx threadEx = null;
			do {
				threadEx = mThreadExQueue.poll();
				if (threadEx != null) {
					threadEx.cancel();
				}
			} while (threadEx != null);

			mThreadExQueue.clear();
		}
		mThreadExQueue = null;
	}

	class MCThreadExQueue extends ArrayBlockingQueue<MCThreadEx> {
		private static final long serialVersionUID = 1L;
		private int mCapacity = 0;

		public MCThreadExQueue(int capacity) {
			super(capacity);
			mCapacity = capacity;
		}

		public boolean isFull() {
			boolean ret = false;
			ret = size() >= mCapacity;
			return ret;
		}
	}

	class MCThreadEx extends Thread {
		private IMCPostActionParam mPostActionParamThread;
		private int mActionKindThread;
		private Object mObjThread = new Object();
		private MCHttpClient mHttpClient = null;

		public MCThreadEx(int kind, IMCPostActionParam param, Runnable r) {
			mPostActionParamThread = MCPostActionFactory.getInstance();
			// パラメータオブジェクトの型によって動作を変える(POST or GET)
			if (param instanceof MCCdnMusicItem)
				((MCPostActionParam) mPostActionParamThread).copyGetParam(param);
			else
				((MCPostActionParam) mPostActionParamThread).copyParam(param);
			mActionKindThread = kind;
			mHttpClient = MCHttpClient.getInstance();
		}

		@Override
		public void run() {
			actionTask(mActionKindThread, mPostActionParamThread, mHttpClient);
			synchronized (mObjThread) {
				if (mThreadExQueue != null) {
					mThreadExQueue.remove(this);
				}
				mActionKindThread = 0;
				mPostActionParamThread.clear();
				mPostActionParamThread = null;
				mHttpClient = null;
			}
		}

		public void cancel() {
			synchronized (mObjThread) {
				if (mHttpClient != null)
					mHttpClient.cancel();
			}
		}
	}

	/**
	 * Musicサーバーアクセス実行
	 * 
	 * @param kind
	 * @param param
	 */
	public synchronized void action(int kind, IMCPostActionParam param, IMCHttpClientListener listener) {
		// QueueFullだったらどうすんの？
		progressListener = listener;
		MCThreadEx threadEx = null;
		if (mThreadExQueue.isFull() != true) {
			threadEx = new MCThreadEx(kind, param, new Runnable() {
				@Override
				public void run() {
				}
			});
			mThreadExQueue.add(threadEx);
			threadEx.start();
		} else {
			// ここ即時復帰にしたほうがいい？
			Message hdlMsg = new Message();
			hdlMsg.what = kind;
			hdlMsg.arg1 = MCError.MC_APPERR_QUEUEFULL;
			hdlMsg.arg2 = 0;
			hdlMsg.obj = null;

			lThread.mHandler.sendMessage(hdlMsg);
			// final Message fmsg = hdlMsg;
			// new Thread(new Runnable() {
			// @Override
			// public void run() {
			// handleMessage(fmsg);
			// }
			// }).start();
		}
	}

	IMCHttpClientListener progressListener;

	private void actionTask(int kind, IMCPostActionParam param, MCHttpClient httpClient) {
		int retry = 2;
		int sleep = 1000;
		int errorCode;
		IMCResultInfo resultInfo = null;
		MCPostActionInfo actionInfo = null;

		actionInfo = new MCPostActionInfo(kind, param);
		httpClient.setPrepare(actionInfo);
		if (progressListener != null)
			httpClient.setProgressListener(progressListener);

		while (retry-- > 0) {
			resultInfo = httpClient.action();
			errorCode = resultInfo.getInt(MCDefResult.MCR_KIND_STATUS_CODE);
			if (errorCode == MCError.MC_APPERR_IO_HTTP) // IO エラーの場合1秒のスリープ
				sleep = 1000;
			else if (errorCode == MCError.MC_APPERR_FILE) // ファイルエラーの場合3秒のスリープ
				sleep = 3000;
			else
				break; // それ以外の場合はリトライしない
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}
		}

		errorCode = resultInfo.getInt(MCDefResult.MCR_KIND_STATUS_CODE);
		if (MCError.MC_NO_ERROR == errorCode && true == actionInfo.isAnalyzeXML()) {
			String encMethod = resultInfo.getString(MCDefResult.MCR_KIND_ENCMETHOD);
			String encKey = resultInfo.getString(MCDefResult.MCR_KIND_ENCKEY);
			String encIv = resultInfo.getString(MCDefResult.MCR_KIND_ENCIV);
			String apiSgi = resultInfo.getString(MCDefResult.MCR_KIND_API_SIG);
			resultInfo.clear();
			resultInfo = null;
			MCXMLParser parser = MCXMLParser.getInstance();
			resultInfo = parser.parse(httpClient.getStream());
			errorCode = resultInfo.getInt(MCDefResult.MCR_KIND_STATUS_CODE);
			if (MCError.MC_NO_ERROR == errorCode) {
				resultInfo.setString(MCDefResult.MCR_KIND_ENCMETHOD, encMethod);
				resultInfo.setString(MCDefResult.MCR_KIND_ENCKEY, encKey);
				resultInfo.setString(MCDefResult.MCR_KIND_ENCIV, encIv);
				resultInfo.setString(MCDefResult.MCR_KIND_API_SIG, apiSgi);
			}
		}
		Message hdlMsg = new Message();
		hdlMsg.what = kind;
		hdlMsg.arg1 = errorCode;
		hdlMsg.arg2 = 0;
		hdlMsg.obj = resultInfo;

		lThread.mHandler.sendMessage(hdlMsg);
		// final Message fmsg = hdlMsg;
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// handleMessage(fmsg);
		// }
		// }).start();

		actionInfo.clear();
		actionInfo = null;
		// resultInfo.clear();
		resultInfo = null;
	}

	/**
	 * 正常終了イベント
	 * 
	 * @param when
	 * @param result
	 */
	public void onCompleteAction(int when, IMCResultInfo result) {

	}

	/**
	 * エンコードに使用されているアイテムをDBに登録するためのイベント
	 * 
	 * @param encItem
	 */
	public void onRegistENC(IMCResultInfo encItem) {

	}

	/**
	 * エラー発生時のイベント
	 * 
	 * @param when
	 * @param errorCode
	 */
	public void onErrorAction(int when, int errorCode) {

	}

	// private synchronized void handleMessage(Message msg) {
	// int actionKind = msg.what;
	// int errorCode = msg.arg1;
	// IMCResultInfo resultInfo = (IMCResultInfo) msg.obj;
	//
	// switch (errorCode) {
	// case MCError.MC_NO_ERROR:
	// if (null != resultInfo.getString(MCDefResult.MCR_KIND_ENCMETHOD)) {
	// onRegistENC(resultInfo);
	// }
	// onCompleteAction(actionKind, resultInfo);
	// break;
	// default:
	// onErrorAction(actionKind, errorCode);
	// break;
	// }
	// if (resultInfo != null)
	// resultInfo.clear();
	// resultInfo = null;
	// }

	class LooperThread extends Thread { // UIThreadじゃないため、自前でLooperを用意しなければならないらしい
		public Handler mHandler;

		@Override
		public void run() {
			Looper.prepare();
			mHandler = new Handler() {
				/**
				 * ハンドルメッセージ
				 **/
				@Override
				public void handleMessage(Message msg) {
					int actionKind = msg.what;
					int errorCode = msg.arg1;
					IMCResultInfo resultInfo = (IMCResultInfo) msg.obj;

					switch (errorCode) {
					case MCError.MC_NO_ERROR:
						if (null != resultInfo.getString(MCDefResult.MCR_KIND_ENCMETHOD)) {
							onRegistENC(resultInfo);
						}
						onCompleteAction(actionKind, resultInfo);
						break;
					default:
						onErrorAction(actionKind, errorCode);
						break;
					}
					if (resultInfo != null)
						resultInfo.clear();
					resultInfo = null;
				}
			};
			Looper.loop();
		}

	}

}