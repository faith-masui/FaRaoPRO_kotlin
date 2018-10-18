package jp.faraopro.play.common;

import android.util.Log;

import jp.faraopro.play.BuildConfig;
import jp.faraopro.play.util.Utils;

/**
 * ログ設定クラス
 *
 * @author Aim
 */
public class FRODebug {
    public static final int LOG_LEVEL;
    public static final boolean ENABLE_LOGS;
    /**
     * ログ出力しない
     */
    public static final int LOG_LEVEL_NON = 5;
    /**
     * ERRORログのみ出力
     */
    public static final int LOG_LEVEL_ERR = 4;
    /**
     * WARRNINGログ以上を出力
     */
    public static final int LOG_LEVEL_WAR = 3;
    /**
     * INFORMATIONログ以上を出力
     */
    public static final int LOG_LEVEL_INF = 2;
    /**
     * DEBUGログ以上を出力
     */
    public static final int LOG_LEVEL_DEB = 1;
    /**
     * VERBOSEログ以上を出力
     */
    public static final int LOG_LEVEL_VER = 0;

    static {
        if (BuildConfig.DEBUG) {
            LOG_LEVEL = LOG_LEVEL_VER;
            ENABLE_LOGS = true;
        } else {
            LOG_LEVEL = LOG_LEVEL_ERR;
            ENABLE_LOGS = false;
        }
    }

    private static final String FARAO_TAG = "[FRO] ";

    public static void logD(Class<?> cls, String msg, boolean enable) {
        if (ENABLE_LOGS && LOG_LEVEL <= LOG_LEVEL_DEB && enable)
            Log.d(Utils.getTag(), FARAO_TAG + msg);
    }

    public static void logI(Class<?> cls, String msg, boolean enable) {
        if (ENABLE_LOGS && LOG_LEVEL <= LOG_LEVEL_INF && enable)
            Log.i(Utils.getTag(), FARAO_TAG + msg);
    }

    public static void logW(Class<?> cls, String msg, boolean enable) {
        if (ENABLE_LOGS && LOG_LEVEL <= LOG_LEVEL_WAR && enable)
            Log.w(Utils.getTag(), FARAO_TAG + msg);
    }

    public static void logE(Class<?> cls, String msg, boolean enable) {
        if (ENABLE_LOGS && LOG_LEVEL <= LOG_LEVEL_ERR && enable)
            Log.e(Utils.getTag(), FARAO_TAG + msg);
    }
}