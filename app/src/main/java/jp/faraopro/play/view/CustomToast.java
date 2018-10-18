package jp.faraopro.play.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

public class CustomToast extends AsyncTask<Long, Integer, Integer> implements Runnable {
	private Toast toast = null;
	private long duration = 0;
	private Handler handler = new Handler();
	private boolean isDisplay = false;

	@SuppressLint("ShowToast")
	public static CustomToast makeText(Context context, int resId, long duration) {
		CustomToast ct = new CustomToast();
		ct.toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
		ct.duration = duration;

		return ct;
	}

	@SuppressLint("ShowToast")
	public static CustomToast makeText(Context context, CharSequence text, long duration) {
		CustomToast ct = new CustomToast();
		ct.toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		ct.duration = duration;

		return ct;
	}

	public void show() {
		isDisplay = true;
		if (duration > 2000) {
			for (int i = 0; i < duration - 2000; i += 2000) {
				handler.postDelayed(this, i);
			}
			handler.postDelayed(this, duration - 2000);
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					isDisplay = false;
				}
			}, duration);
		} else {
			// this.execute(new Long(duration));
			this.execute();
		}
	}

	public boolean isDisplay() {
		return isDisplay;
	}

	@Override
	public void run() {
		toast.show();
	}

	@Override
	protected Integer doInBackground(Long... params) {
		// long duration = params[0];
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	protected void onPreExecute() {
		toast.show();
	}

	@Override
	protected void onPostExecute(final Integer i) {
		toast.cancel();
		isDisplay = false;
	}

}
