package jp.faraopro.play.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.util.Log;

import jp.faraopro.play.model.FROUserData;

public class UserDataPreference {
	private static final String TAG = UserDataPreference.class.getSimpleName();
	private static final String PREF_FILE_NAME = "farao_user_data_pref_file";
	private static final String PREF_KEY_LAST_UPDATE = "farao_user_data_last_update";
	private static final String PREF_KEY_SERVICE_TYPE = "farao_user_data_service_type";
	private static final String PREF_KEY_SUBSCRIPTION_TYPE = "farao_user_data_subscription_type";
	private static final String PREF_KEY_REMAINING_TIME = "farao_user_data_remaining_time";
	private static final String PREF_KEY_PLAYABLE_TRACKS = "farao_user_data_playable_tracks";
	private static final String PREF_KEY_TRACKING_KEY = "farao_user_data_tracking_key";
	private static final String PREF_KEY_SERVICE_LEVEL = "farao_user_data_service_level";
	private static final String PREF_KEY_FEATURES = "farao_user_data_features";
	private static final String PREF_KEY_PERMISSIONS = "farao_user_data_permissions";
	// private static final String PREF_KEY_MARKET_TYPE =
	// "farao_user_data_market_type";
	// private static final String PREF_KEY_EXPIRES_DATE =
	// "farao_user_data_expires_date";
	// private static final String PREF_KEY_EXPIRES_LIMIT =
	// "farao_user_data_expires_limit";

	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
	}

	public static Editor getPreferencesEditor(Context context) {
		return getPreferences(context).edit();
	}

	private static void commitOrApply(SharedPreferences.Editor editor) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			editor.apply();
		} else {
			editor.commit();
		}
	}

	public static void showAllData(Context context) {
		Log.i(TAG, "PREF_KEY_LAST_UPDATE:" + getPreferences(context).getLong(PREF_KEY_LAST_UPDATE, 0));
		Log.i(TAG, "PREF_KEY_SERVICE_TYPE:" + getPreferences(context).getInt(PREF_KEY_SERVICE_TYPE, 0));
		Log.i(TAG, "PREF_KEY_SUBSCRIPTION_TYPE:" + getPreferences(context).getInt(PREF_KEY_SUBSCRIPTION_TYPE, 0));
		Log.i(TAG, "PREF_KEY_REMAINING_TIME:" + getPreferences(context).getLong(PREF_KEY_REMAINING_TIME, 0));
		Log.i(TAG, "PREF_KEY_PLAYABLE_TRACKS:" + getPreferences(context).getInt(PREF_KEY_PLAYABLE_TRACKS, 0));
		Log.i(TAG, "PREF_KEY_TRACKING_KEY:" + getPreferences(context).getString(PREF_KEY_TRACKING_KEY, null));
		Log.i(TAG, "PREF_KEY_SERVICE_LEVEL:" + getPreferences(context).getInt(PREF_KEY_SERVICE_LEVEL, 0));
		Log.i(TAG, "PREF_KEY_FEATURES:" + getPreferences(context).getInt(PREF_KEY_FEATURES, 0));
		Log.i(TAG, "PREF_KEY_PERMISSIONS:" + getPreferences(context).getInt(PREF_KEY_PERMISSIONS, 0));
	}

	public static void setUserData(Context context, FROUserData data) {
		final SharedPreferences.Editor editor = getPreferencesEditor(context);
		editor.putLong(PREF_KEY_LAST_UPDATE, data.getLastUpdate());
		editor.putInt(PREF_KEY_SERVICE_TYPE, data.getServiceType());
		editor.putInt(PREF_KEY_SUBSCRIPTION_TYPE, data.getSubscriptionType());
		editor.putLong(PREF_KEY_REMAINING_TIME, data.getRemainingTime());
		editor.putInt(PREF_KEY_PLAYABLE_TRACKS, data.getPlayableTracks());
		editor.putString(PREF_KEY_TRACKING_KEY, data.getTrackingKey());
		editor.putInt(PREF_KEY_SERVICE_LEVEL, data.getServiceLevel());
		editor.putInt(PREF_KEY_FEATURES, data.getFeatures());
		editor.putInt(PREF_KEY_PERMISSIONS, data.getPermissions());
		// editor.putInt(PREF_KEY_MARKET_TYPE, data.getMarketType());
		// editor.putLong(PREF_KEY_EXPIRES_DATE, data.getExpiresDate());
		// editor.putLong(PREF_KEY_EXPIRES_LIMIT, data.getExpiresLimit());
		commitOrApply(editor);
	}

	public static long getLastUpdate(Context context) {
		return getPreferences(context).getLong(PREF_KEY_LAST_UPDATE, 0);
	}

	public static int getServiceType(Context context) {
		return getPreferences(context).getInt(PREF_KEY_SERVICE_TYPE, 1);
	}

	public static int getSubscriptionType(Context context) {
		return getPreferences(context).getInt(PREF_KEY_SUBSCRIPTION_TYPE, 0);
	}

	public static long getRemainingTime(Context context) {
		return getPreferences(context).getLong(PREF_KEY_REMAINING_TIME, 0);
	}

	public static void setRemainingTime(Context context, long time) {
		final SharedPreferences.Editor editor = getPreferencesEditor(context);
		editor.putLong(PREF_KEY_REMAINING_TIME, time);
		commitOrApply(editor);
	}

	public static boolean isPremium(Context context) {
		return getPreferences(context).getLong(PREF_KEY_REMAINING_TIME, 0) > 0;
	}

	public static int getPlayableTracks(Context context) {
		return getPreferences(context).getInt(PREF_KEY_PLAYABLE_TRACKS, 0);
	}

	public static String getTrackingKey(Context context) {
		return getPreferences(context).getString(PREF_KEY_TRACKING_KEY, null);
	}

	public static void setServicePlan(Context context, FROUserData data) {
		final SharedPreferences.Editor editor = getPreferencesEditor(context);
		editor.putInt(PREF_KEY_SERVICE_LEVEL, data.getServiceLevel());
		editor.putInt(PREF_KEY_FEATURES, data.getFeatures());
		editor.putInt(PREF_KEY_PERMISSIONS, data.getPermissions());
		commitOrApply(editor);
	}

	public static int getServiceLevel(Context context) {
		return getPreferences(context).getInt(PREF_KEY_SERVICE_LEVEL, 0);
	}

	public static int getFeatures(Context context) {
		return getPreferences(context).getInt(PREF_KEY_FEATURES, 0);
	}

	public static int getPermissions(Context context) {
		return getPreferences(context).getInt(PREF_KEY_PERMISSIONS, 0);
	}

	// public static int getMarketType(Context context) {
	// return getPreferences(context).getInt(PREF_KEY_MARKET_TYPE, 0);
	// }
	//
	// public static long getExpiresDate(Context context) {
	// return getPreferences(context).getLong(PREF_KEY_EXPIRES_DATE, 0);
	// }
	//
	// public static long getExpiresLimit(Context context) {
	// return getPreferences(context).getLong(PREF_KEY_EXPIRES_LIMIT, 0);
	// }
}
