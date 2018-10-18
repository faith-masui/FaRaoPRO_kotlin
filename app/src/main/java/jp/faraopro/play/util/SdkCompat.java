package jp.faraopro.play.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Build;

public class SdkCompat {
	public static void setExact(AlarmManager am, int type, long triggerAtMillis, PendingIntent operation) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
			am.setExact(type, triggerAtMillis, operation);
		else
			am.set(type, triggerAtMillis, operation);
	}
}
