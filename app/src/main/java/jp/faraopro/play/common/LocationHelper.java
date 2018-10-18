package jp.faraopro.play.common;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;

/**
 * 位置情報を取得するためのクラス
 * 
 * @author AIM Corporation
 * 
 */
public class LocationHelper implements LocationListener {
	private LocationManager mManager;
	private Context mContext;
	private IFROLocationListener mCallback;
	private int mType;
	private boolean isWorker = false;
	private CountDownTimer mTimeoutTimer;

	/**
	 * 位置情報が取得完了した際に通知を受けるリスナー
	 * 
	 * @author AIM Corporation
	 * 
	 */
	public interface IFROLocationListener {
		/**
		 * 位置情報が正しく取得できた際に呼び出される
		 * 
		 * @param type
		 * @param lat
		 *            緯度
		 * @param lon
		 *            経度
		 */
		public void onSuccess(int type, double lat, double lon);

		/**
		 * エラーが発生した際に呼び出される
		 */
		public void onError(int type);
	}

	public LocationHelper(IFROLocationListener listener, Context context, int type) {
		this(listener, context, type, false);
	}

	public LocationHelper(IFROLocationListener listener, Context context, int type, boolean isWorkerThread) {
		this.mCallback = listener;
		this.mContext = context;
		this.mType = type;
		isWorker = isWorkerThread;
		getManager();
	}

	private void getManager() {
		if (mContext != null)
			mManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
	}

	/**
	 * 位置情報を非同期で取得する<br>
	 * 取得できた場合は{@link IFROLocationListener}が呼び出される
	 */
	public void getLocation(int timeout) {
		if (mManager == null)
			getManager();
		if (!isEnableProvider(mContext)) {
			if (mCallback != null)
				mCallback.onError(mType);
			term();
		} else {
			if (timeout > 0) {
				mTimeoutTimer = new CountDownTimer(timeout, timeout) {
					@Override
					public void onTick(long millisUntilFinished) {
						// DO NOTHING
					}

					@Override
					public void onFinish() {
						mCallback.onError(mType);
						term();
					}
				};
				mTimeoutTimer.start();
			}
			if (mManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
				mManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
			if (mManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
				mManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		}
	}

	/**
	 * 終了処理
	 */
	public void term() {
		if (mManager != null)
			mManager.removeUpdates(this);
		if (mTimeoutTimer != null)
			mTimeoutTimer.cancel();
		this.mManager = null;
		this.mTimeoutTimer = null;
		this.mCallback = null;
		this.mContext = null;
		this.mType = -1;
	}

	@Override
	public void onLocationChanged(Location location) {
		// DEBUG
		// FRODebug.logI(getClass(), "asd end time " +
		// Utils.getNowDateString("yyyyMMdd kk:mm;ss"), true);
		// FRODebug.logI(getClass(), "provider = " + location.getProvider(),
		// true);

		if (mCallback != null)
			mCallback.onSuccess(mType, location.getLatitude(), location.getLongitude());
		term();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// FRODebug.logI(getClass(), "onProviderDisabled", true);
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		// FRODebug.logI(getClass(), "onProviderEnabled", true);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		// FRODebug.logI(getClass(), "onStatusChanged", true);
	}

	public static boolean isEnableProvider(Context context) {
		boolean gps;
		boolean network;
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		gps = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		network = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		// FRODebug.logI(LocationHelper.class, "gps = " + gps, true);
		// FRODebug.logI(LocationHelper.class, "network = " + network, true);

		return gps | network;
	}

}
