package jp.faraopro.play.mclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.List;
import java.util.Map;

import jp.faraopro.play.common.FRODebug;

/**
 * User情報(永続化)管理クラス
 *
 * @author AIM Corporation
 */
public class MCUserInfoPreference {
    // private static final String TAG = MCUserInfoPreference.class
    // .getSimpleName();

    public static final int BOOLEAN_DATA_ENABLE_MUSIC_TIMER = 0;
    // public static final int BOOLEAN_DATA_ENABLE_RESTORE_MUSIC_TIMER = 1;
    public static final int INTEGER_DATA_MUSIC_TIMER_TYPE = 0;
    public static final int STRING_DATA_MAIL = 0;
    public static final int STRING_DATA_PASS = 1;

    private static MCUserInfoPreference mUserInfoPreference = null;
    private Context mContext = null;
    private SharedPreferences mPreferences = null;

    private static final String PREFERENCES_FILE_NAME = "MCUserInfo_PRE";
    private static final String KEY_USER_EMAIL = "MC_USER_EMAIL";
    private static final String KEY_USER_PASSWORD = "MC_USER_PASSWORD";
    private static final String KEY_USER_SESSIONKEY = "MC_USER_SESSIONKEY";
    private static final String PAYMENT_TYPE = "ACT_PAYMENT_TYPE";
    private static final String PAYMENT_ID = "ACT_PAYMENT_ID";
    private static final String TAG_SLEEP_TIME = "FRO_SLEEP_TIME";

    private static final String KEY_DEVICE_TOKEN = "MC_DEVICE_TOKEN";
    private static final String TAG_ENABLE_MUSIC_TIMER = "FRO_ENABLE_MUSIC_TIMER";
    private static final String TAG_MUSIC_TIMER_TYPE = "MUSIC_TIMER_TYPE";
    public static final String TAG_DOWNLOAD_FAILED_LIST = "DOWNLOAD_FAILED_LIST"; //UNUSED
    private static final String TAG_ONAIR_OFFLINE = "ONAIR_OFFLINE";
    private static final String TAG_LICENSE_INTERVAL = "LICENSE_INTERVAL";

    private static final String TAG_LATEST_ANDROID_ID = "LATEST_ANDROID_ID";

    private static final String TAG_CHANNEL_VOLUME = "CHANNEL_VOLUME";
    private static final String TAG_INTERRUPT_VOLUME = "INTERRUPT_VOLUME";
    private static final String TAG_LATEST_VERSION_CODE = "LATEST_VERSION_CODE";

    private static final String TAG_AUDIO_FOCUSED_VOLUME = "AUDIO_FOCUSED_VOLUME";

    // private static final String UNSENDABLE_RATING = "UNSENDABLE_RATING";

    /**
     * インスタンスの取得
     *
     * @return
     */
    public synchronized static MCUserInfoPreference getInstance(Context context) {
        // Log.e("MCUserInfoPreference", "in getInstance");
        if (mUserInfoPreference == null) {
            // Log.e("MCUserInfoPreference", "mUserInfoPreference == null");
            mUserInfoPreference = new MCUserInfoPreference(context);
        }
        return mUserInfoPreference;
    }

    /**
     * コンストラクタ
     */
    public MCUserInfoPreference(Context context) {
        mContext = context;
        // mPreferences = mContext.getSharedPreferences(PREFERENCES_FILE_NAME,
        // 4);
        mPreferences = mContext.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_MULTI_PROCESS);
    }

    /**
     * 解放処理
     */
    public void term() {
        mContext = null;
        mPreferences = null;
        mUserInfoPreference = null;
    }

    public void deleteKey(String... tags) {
        SharedPreferences.Editor editor;
        editor = mPreferences.edit();
        for (String tag : tags) {
            editor.remove(tag);
        }
        editor.commit();
    }

    public void showTags() {
        Map<String, ?> all = mPreferences.getAll();
        for (Map.Entry<String, ?> entry : all.entrySet()) {
            FRODebug.logD(getClass(), entry.getKey() + " -> " + entry.getValue(), true);
        }
    }

    /**
     * 登録(Email)
     *
     * @param value
     */
    public synchronized void setEmail(String value) {
        SharedPreferences.Editor editor;
        editor = mPreferences.edit();
        editor.putString(KEY_USER_EMAIL, value);
        editor.commit();
        editor = null;
        term();
    }

    /**
     * 取得(Email)
     *
     * @return
     */
    public synchronized String getEmail() {
        String value = mPreferences.getString(KEY_USER_EMAIL, "");
        term();
        return value;
    }

    /**
     * 登録(Password)
     *
     * @param value
     */
    public void setPassword(String value) {
        SharedPreferences.Editor editor;
        editor = mPreferences.edit();
        editor.putString(KEY_USER_PASSWORD, value);
        editor.commit();
        editor = null;
        term();
    }

    /**
     * 取得(Password)
     *
     * @return
     */
    public String getPassword() {
        String value = mPreferences.getString(KEY_USER_PASSWORD, "");
        term();
        return value;
    }

    /**
     * 登録(SessionKey)
     *
     * @param value
     */
    public void setSessionKey(String value) {
        SharedPreferences.Editor editor;
        editor = mPreferences.edit();
        editor.putString(KEY_USER_SESSIONKEY, value);
        editor.commit();
        editor = null;
        term();
    }

    /**
     * 取得(SessionKey)
     *
     * @return
     */
    public String getSessionKey() {
        String value = mPreferences.getString(KEY_USER_SESSIONKEY, "");
        term();
        return value;
    }

    /**
     * 登録(PaymentType)
     *
     * @param value
     */
    public void setPaymentType(String value) {
        SharedPreferences.Editor editor;
        editor = mPreferences.edit();
        editor.putString(PAYMENT_TYPE, value);
        editor.commit();
        editor = null;
        term();
    }

    /**
     * 取得(PaymentType)
     *
     * @return
     */
    public String getPaymentType() {
        String value = mPreferences.getString(PAYMENT_TYPE, "");
        term();
        return value;
    }

    /**
     * 登録(PaymentId)
     *
     * @param value
     */
    public void setPaymentId(String value) {
        SharedPreferences.Editor editor;
        editor = mPreferences.edit();
        editor.putString(PAYMENT_ID, value);
        editor.commit();
        editor = null;
        term();
    }

    /**
     * 取得(PaymentId)
     *
     * @return
     */
    public String getPaymentId() {
        String value = mPreferences.getString(PAYMENT_ID, "");
        term();
        return value;
    }

    public int getSleepTime() {
        int value = mPreferences.getInt(TAG_SLEEP_TIME, 15);
        return value;
    }

    public void setSleepTime(int value) {
        SharedPreferences.Editor editor;
        editor = mPreferences.edit();
        editor.putInt(TAG_SLEEP_TIME, value);
        editor.commit();
        editor = null;
    }

    /**
     * 登録(Email)
     *
     * @param value
     */
    public synchronized void setDeviceToken(String value) {
        SharedPreferences.Editor editor;
        editor = mPreferences.edit();
        editor.putString(KEY_DEVICE_TOKEN, value);
        editor.commit();
        editor = null;
        term();
    }

    /**
     * 取得(Email)
     *
     * @return
     */
    public synchronized String getDeviceToken() {
        String value = mPreferences.getString(KEY_DEVICE_TOKEN, "");
        term();
        return value;
    }

    public boolean getMusicTimerEnable() {
        boolean value = mPreferences.getBoolean(TAG_ENABLE_MUSIC_TIMER, true);
        return value;
    }

    public void setMusicTimerEnable(boolean value) {
        SharedPreferences.Editor editor;
        editor = mPreferences.edit();
        editor.putBoolean(TAG_ENABLE_MUSIC_TIMER, value);
        editor.commit();
    }

    public int getMusicTimerType() {
        int value = mPreferences.getInt(TAG_MUSIC_TIMER_TYPE, 0);
        return value;
    }

    public void setMusicTimerType(int value) {
        SharedPreferences.Editor editor;
        editor = mPreferences.edit();
        editor.putInt(TAG_MUSIC_TIMER_TYPE, value);
        editor.commit();
    }

//    public ArrayList<String> getFailed() {
//        ArrayList<String> list = null;
//        String value = mPreferences.getString(TAG_DOWNLOAD_FAILED_LIST, null);
//        if (!TextUtils.isEmpty(value)) {
//            String[] tmp = value.split(",", 0);
//            list = new ArrayList<String>();
//            for (String s : tmp) {
//                list.add(s);
//            }
//        }
//        return list;
//    }
//
//    public void setFailed(ArrayList<String> list) {
//        String value = "";
//        SharedPreferences.Editor editor;
//        editor = mPreferences.edit();
//        if (list != null && list.size() > 0) {
//            value = "";
//            for (String s : list) {
//                value += (s + ",");
//            }
//        }
//        editor.putString(TAG_DOWNLOAD_FAILED_LIST, value);
//        editor.commit();
//    }

    public String getOnairOffline() {
        return mPreferences.getString(TAG_ONAIR_OFFLINE, "");
    }

    public void setOnairOffline(String frameId, List<String> audioIds) {
        String value = "";
        SharedPreferences.Editor editor;
        editor = mPreferences.edit();
        if (!TextUtils.isEmpty(frameId) && audioIds != null && audioIds.size() > 0) {
            value = frameId + "@";
            for (String s : audioIds) {
                value += (s + "&");
            }
        }
        editor.putString(TAG_ONAIR_OFFLINE, value);
        editor.commit();
    }

    public int getLicenseInterval() {
        int value = mPreferences.getInt(TAG_LICENSE_INTERVAL, 28800);
        return value;
    }

    public void setLicenseInterval(int value) {
        SharedPreferences.Editor editor;
        editor = mPreferences.edit();
        editor.putInt(TAG_LICENSE_INTERVAL, value);
        editor.commit();
    }

    public void setChannelVolume(float value) {
        SharedPreferences.Editor editor;
        editor = mPreferences.edit();
        editor.putFloat(TAG_CHANNEL_VOLUME, value);
        editor.commit();
        term();
    }

    public float getChannelVolume() {
        float value = mPreferences.getFloat(TAG_CHANNEL_VOLUME, 1.0f);
        term();
        return value;
    }

    public void setInterruptVolume(float value) {
        SharedPreferences.Editor editor;
        editor = mPreferences.edit();
        editor.putFloat(TAG_INTERRUPT_VOLUME, value);
        editor.commit();
        term();
    }

    public float getInterruptVolume() {
        float value = mPreferences.getFloat(TAG_INTERRUPT_VOLUME, 1.0f);
        term();
        return value;
    }

    public void setLatestVersionCode(int value) {
        SharedPreferences.Editor editor;
        editor = mPreferences.edit();
        editor.putInt(TAG_LATEST_VERSION_CODE, value);
        editor.commit();
        term();
    }

    public int getLatestVersionCode() {
        int value = mPreferences.getInt(TAG_LATEST_VERSION_CODE, -1);
        term();
        return value;
    }

    public void setAudioFocusedVolume(float value) {
        SharedPreferences.Editor editor;
        editor = mPreferences.edit();
        editor.putFloat(TAG_AUDIO_FOCUSED_VOLUME, value);
        editor.commit();
        term();
    }

    public float getAudioFocusedVolume() {
        float value = mPreferences.getFloat(TAG_AUDIO_FOCUSED_VOLUME, 0.1f);
        term();
        return value;
    }
}
