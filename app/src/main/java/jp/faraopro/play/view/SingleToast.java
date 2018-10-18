package jp.faraopro.play.view;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class SingleToast {
	private Handler handler = new Handler();
	private boolean isDisplay = false;

	public void showToast(Context context, int resId, int duration) {
		if (isDisplay)
			return;

		isDisplay = true;
		Toast.makeText(context, resId, duration).show();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				isDisplay = false;
			}
		}, 2000);
	}

}
