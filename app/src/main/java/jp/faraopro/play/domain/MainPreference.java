package jp.faraopro.play.domain;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import jp.faraopro.play.common.Consts;

/**
 * User情報(永続化)管理クラス
 * 
 * @author AIM Corporation
 * 
 */
public class MainPreference {
	private static MainPreference mUserInfoPreference = null;
	private SharedPreferences mPreferences = null;

	private static final String PREFERENCES_FILE_NAME = "FRO_MAIN_PROC_PREF";
	private static final String KEY_USER_EMAIL = "FRO_EMAIL";
	private static final String KEY_USER_PASSWORD = "FRO_PASS";
	private static final String PAYMENT_TRACKINGKEY = "FRO_PAYMENT_TRACKINGKEY";
	private static final String TAG_LAST_ACCOUNT = "FRO_LAST_ACCOUNT";
	private static final String TAG_ACTIVATION_MAIL = "FRO_ACTIVATION_MAIL";
	private static final String TAG_ACTIVATION_PASS = "FRO_ACTIVATION_PASS";
	private static final String TAG_ENABLE_AUTO_BOOT = "ENABLE_AUTO_BOOT";
	public static final String TAG_ENABLE_MUSIC_TIMER = "FRO_ENABLE_MUSIC_TIMER";
	public static final String TAG_ENABLE_INTERRUPT_TIMER = "FRO_ENABLE_INTERRUPT_TIMER";
	public static final String TAG_MUSIC_TIMER_TYPE = "MUSIC_TIMER_TYPE";
	public static final String TAG_NEXT_PATTERN_UPDATE = "NEXT_PATTERN_UPDATE";
	public static final String TAG_ENABLE_RESTORE_MUSIC_TIMER = "FRO_ENABLE_RESTORE_TIMER";

	private static final String TAG_LAST_PATTERN_UPDATE = "LAST_PATTERN_UPDATE";
	private static final String TAG_LAST_PATTERN_UPDATE_STATE = "LAST_PATTERN_UPDATE_STATE";
	private static final String TAG_TEMPLATE_LAST_UPDATE = "TEMPLATE_LAST_UPDATE";
	private static final String TAG_DO_NOT_SHOW_TIMER_DESCRIPTION = "DO_NOT_SHOW_TIMER_DESCRIPTION";

	/** new UI **/
	private static final String TAG_MENU0 = "FRO_MENU0";
	private static final String TAG_MENU1 = "FRO_MENU1";
	private static final String TAG_MENU2 = "FRO_MENU2";
	//private static final String TAG_MENU3 = "FRO_MENU3";
	//private static final String TAG_MENU4 = "FRO_MENU4";
	//private static final String TAG_MENU5 = "FRO_MENU5";
	// private static final String TAG_MENU6 = "FRO_MENU6";
	//private static final String[] MENU_TAGS = { TAG_MENU0, TAG_MENU1, TAG_MENU2, TAG_MENU3, TAG_MENU4, TAG_MENU5, };
	private static final String[] MENU_TAGS = { TAG_MENU0, TAG_MENU1, TAG_MENU2};
	/**
	 * インスタンスの取得
	 * 
	 * @return
	 */
	public static synchronized MainPreference getInstance(Context context) {
		if (mUserInfoPreference == null && context != null) {
			mUserInfoPreference = new MainPreference(context);
		}
		return mUserInfoPreference;
	}

	/**
	 * コンストラクタ
	 */
	private MainPreference(Context context) {
		mPreferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_MULTI_PROCESS);
	}

	/**
	 * 解放処理
	 */
	public synchronized void term() {
		mPreferences = null;
		mUserInfoPreference = null;
	}

	public boolean isContain(String tag) {
		return mPreferences.contains(tag);
	}

	public void removePreference(String tag) {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.remove(tag);
		editor.commit();
		term();
	}


	/**
	 * 取得(lastAccount)
	 * 
	 * @return 前回ログインしたアカウント
	 */
	public String getLastAccount() {
		String value = mPreferences.getString(TAG_LAST_ACCOUNT, null);
		term();
		return value;
	}

	/**
	 * 登録(lastAccount)
	 * 
	 * @param value
	 *            最後にログインしたアカウント
	 */
	public void setLastAccount(String value) {
		SharedPreferences.Editor editor;
		editor = mPreferences.edit();
		editor.putString(TAG_LAST_ACCOUNT, value);
		editor.commit();
		term();
	}

	/**
	 * 登録(アクティベーション時のメールアドレス)
	 * 
	 * @return アクティベーション時のメールアドレス
	 */
	public String getActivationMail() {
		String value = mPreferences.getString(TAG_ACTIVATION_MAIL, null);
		term();
		return value;
	}

	/**
	 * 取得(アクティベーション時のメールアドレス)
	 * 
	 * @param value
	 *            アクティベーション時のメールアドレス
	 */
	public void setActivationMail(String value) {
		SharedPreferences.Editor editor;
		editor = mPreferences.edit();
		editor.putString(TAG_ACTIVATION_MAIL, value);
		editor.commit();
		term();
	}

	/**
	 * 登録(アクティベーション時のパスワード)
	 * 
	 * @return アクティベーション時のパスワード
	 */
	public String getActivationPass() {
		String value = mPreferences.getString(TAG_ACTIVATION_PASS, null);
		term();
		return value;
	}

	/**
	 * 取得(アクティベーション時のパスワード)
	 * 
	 * @param value
	 *            アクティベーション時のパスワード
	 */
	public void setActivationPass(String value) {
		SharedPreferences.Editor editor;
		editor = mPreferences.edit();
		editor.putString(TAG_ACTIVATION_PASS, value);
		editor.commit();
		term();
	}

	public ArrayList<Integer> getMenuList() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		int value;
		for (int i = 0; i < MENU_TAGS.length; i++) {
			value = mPreferences.getInt(MENU_TAGS[i], -1);
			list.add(new Integer(value));
		}
		if (list.contains(new Integer(-1))) {
			list.clear();
			list = new ArrayList<Integer>();
			list.add(Consts.TAG_MODE_SPECIAL);
			list.add(Consts.TAG_MODE_STREAMING);
			list.add(Consts.TAG_MODE_HISTORY);
			list.add(Consts.TAG_MODE_MYCHANNEL);
			//list.add(Consts.TAG_MODE_GENRE);
			//list.add(Consts.TAG_MODE_ARTIST);
			//list.add(Consts.TAG_MODE_RELEASE);
		}
		term();
		return list;
	}

	public void setMenuList(ArrayList<Integer> list) {
		SharedPreferences.Editor editor;
		editor = mPreferences.edit();
		for (int i = 0; i < MENU_TAGS.length; i++) {
			if (list != null && list.size() > 0)
				editor.putInt(MENU_TAGS[i], list.get(i).intValue());
			else
				editor.putInt(MENU_TAGS[i], -1);
		}
		editor.commit();
		term();
	}

	public boolean getMusicTimerEnable() {
		boolean value = mPreferences.getBoolean(TAG_ENABLE_MUSIC_TIMER, true);
		term();
		return value;
	}

	public void setMusicTimerEnable(boolean value) {
		SharedPreferences.Editor editor;
		editor = mPreferences.edit();
		editor.putBoolean(TAG_ENABLE_MUSIC_TIMER, value);
		editor.commit();
		term();
	}

	public boolean getInterruptTimerEnable() {
		boolean value = mPreferences.getBoolean(TAG_ENABLE_INTERRUPT_TIMER, true);
		term();
		return value;
	}

	public void setInterruptTimerEnable(boolean value) {
		SharedPreferences.Editor editor;
		editor = mPreferences.edit();
		editor.putBoolean(TAG_ENABLE_INTERRUPT_TIMER, value);
		editor.commit();
		term();
	}

	public int getMusicTimerType() {
		int value = mPreferences.getInt(TAG_MUSIC_TIMER_TYPE, 0);
		term();
		return value;
	}

	public void setMusicTimerType(int value) {
		SharedPreferences.Editor editor;
		editor = mPreferences.edit();
		editor.putInt(TAG_MUSIC_TIMER_TYPE, value);
		editor.commit();
		term();
	}

	public boolean isEnableAutoBoot() {
		boolean value = mPreferences.getBoolean(TAG_ENABLE_AUTO_BOOT, false);
		term();
		return value;
	}

	public void setEnableAutoBoot(boolean value) {
		SharedPreferences.Editor editor;
		editor = mPreferences.edit();
		editor.putBoolean(TAG_ENABLE_AUTO_BOOT, value);
		editor.commit();
		term();
	}

	public String getNextPatternUpdate() {
		String value = mPreferences.getString(TAG_NEXT_PATTERN_UPDATE, "");
		term();
		return value;
	}

	public void setNextPatternUpdate(String value) {
		SharedPreferences.Editor editor;
		editor = mPreferences.edit();
		editor.putString(TAG_NEXT_PATTERN_UPDATE, value);
		editor.commit();
		term();
	}

	public boolean getRestoreMusicTimerEnable() {
		boolean value = mPreferences.getBoolean(TAG_ENABLE_RESTORE_MUSIC_TIMER, true);
		term();
		return value;
	}

	public void setRestoreMusicTimerEnable(boolean value) {
		SharedPreferences.Editor editor;
		editor = mPreferences.edit();
		editor.putBoolean(TAG_ENABLE_RESTORE_MUSIC_TIMER, value);
		editor.commit();
		term();
	}

	public long getTemplateLastUpdate() {
		long value = mPreferences.getLong(TAG_TEMPLATE_LAST_UPDATE, 0);
		term();
		return value;
	}

	public void setTemplateLastUpdate(long value) {
		SharedPreferences.Editor editor;
		editor = mPreferences.edit();
		editor.putLong(TAG_TEMPLATE_LAST_UPDATE, value);
		editor.commit();
		term();
	}
}
