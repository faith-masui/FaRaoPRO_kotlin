package jp.faraopro.play.common;

import android.content.Context;
import android.graphics.Color;

import java.util.Locale;

import jp.faraopro.play.R;
import jp.faraopro.play.util.Utils;

/**
 * 定数クラス
 *
 * @author ksu
 */
public class Consts {
    /**
     * 通常通知
     */
    public static final int NOTIFY_RESULT = 2000;
    /**
     * チャンネルリスト取得
     */
    public static final int CHANNEL_LIST = 2001;
    // public static final int GET_PLAY_LIST = 2002;
    // public static final int TYPE_MUSIC_MULTI = 2003;
    // public static final int TYPE_IMAGE_MULTI = 2004;
    // public static final int TYPE_MUSIC_SINGLE = 2005;
    // public static final int TYPE_IMAGE_SINGLE = 2006;
    /**
     * 楽曲再生開始
     */
    public static final int START_MUSIC = 2007;
    /**
     * 音声広告再生開始
     */
    public static final int START_AD = 2008;
    /**
     * リストID取得
     */
    public static final int ID_LIST = 2009;
    /**
     * リリース範囲取得
     */
    public static final int GET_RANGE = 2010;
    /**
     * ロック情報取得(マイチャンネル)
     */
    public static final int LOCK_LIST = 2011;
    /**
     * マイチャンネル編集完了
     */
    public static final int SET_MYCHANNEL = 2012;
    /**
     * ユーザステータス取得
     */
    public static final int USER_STATUS = 2013;
    /**
     * 評価再送
     */
    public static final int CHECK_UNSENT_DATA = 2015;

    public static final int IN_BILLING = 2016;

    public static final int CHANNEL_LIST_BENIFIT = 2017;
    public static final int CHANNEL_LIST_CHART = 2018;
    public static final int CHANNEL_LIST_GENRE = 2019;
    public static final int CHANNEL_LIST_ARTIST = 2020;
    public static final int CHANNEL_LIST_FEATURED = 2021;
    public static final int CHANNEL_LIST_MYCHANNEL = 2022;
    public static final int CHANNEL_LIST_BUSINESS = 2023;
    /**
     * 楽曲再生開始
     */
    public static final int START_LOCAL_MUSIC = 2024;
    /**
     * 画面切り替え要求
     */
    public static final int REQUEST_CHANGE_FRAGMENT = 2025;
    /**
     * 楽曲再生開始
     */
    public static final int START_STREAM_MUSIC = 2026;
    /**
     * 楽曲再生開始
     */
    public static final int START_OFFLINE_MUSIC = 2027;

    /**
     * サービス起動
     */
    public static final int SERVICE_BOOT = 2028;

    /**
     * 正常
     */
    public static final int STATUS_OK = 200;
    /**
     * データ無し
     */
    public static final int STATUS_NO_DATA = 2029;
    /**
     * ローカルDBが破損している
     */
    public static final int DB_IS_CORRUPTED = 2030;

    public static final int START_INTERRUPT = 2031;
    public static final int INTERRUPT_LIST = 2032;
    public static final int END_INTERRUPT = 2033;

    /**
     * アカウントの有効期限切れ
     */
    public static final int STATUS_NO_REMAIN_TIMES = 3000;
    /**
     * アカウントの残り曲数切れ
     */
    public static final int STATUS_NO_TRACK_TIMES = 3001;
    /**
     * スキップ可能通知
     */
    public static final int NOTIFY_PAST_15SEC = 3002;
    /**
     * スキップ回数切れ
     */
    public static final int NO_SKIP_REMAINING = 3003;
    // /**
    // * 既にロックされている
    // */
    // public static final int ALREADY_LOCKED = 304;
    // /**
    // * ロックが行われていない
    // */
    // public static final int APIMSG_ISNT_LOCKED = 305;
    /**
     * 1H経過した
     */
    public static final int PAST_ONE_HOUR = 3006;
    /**
     * 楽曲情報更新
     */
    public static final int UPDATE_MUSIC_INFO = 3007;
    /**
     * 課金処理が完了していない
     */
    public static final int BILLING_ISNT_COMPLETE = 3008;
    /**
     * プログレスダイアログの表示要求
     */
    public static final int SHOW_PROGRESS = 3009;
    /**
     * 音声広告の表示
     */
    public static final int PLAY_ADS = 3010;
    // /**
    // * APIエラーメッセージ：自動継続課金においてレシートの照合に失敗
    // */
    // public static final int APIMSG_PAYMENT_ERROR_VERIFY = 311;
    // /**
    // * APIエラーメッセージ：自動継続課金においてネットワークエラーor認証に失敗
    // */
    // public static final int APIMSG_PAYMENT_ERROR_NETWORK = 312;
    /**
     * 広告情報更新
     */
    public static final int UPDATE_ADS_INFO = 3013;
    // /**
    // * APIエラーメッセージ：シェアリンク、指定チャンネルが見つからない
    // */
    // public static final int APIMSG_SHARE_ERROR_NOT_FOUND = 314;
    // /**
    // * APIエラーメッセージ：シェアリンク、公開期限切れ
    // */
    // public static final int APIMSG_SHARE_ERROR_EXPIRED = 315;
    /**
     * モードの取得
     */
    public static final int UPDATE_MODE_INFO = 3016;
    /**
     * キャンセル完了通知
     */
    public static final int NOTIFY_COMPLETE_CANCEL = 3017;
    /**
     * プログレスキャンセル抑止
     */
    public static final int NOTIFY_DISABLE_CANCEL = 3018;
    /**
     * 楽曲のDL開始通知
     */
    public static final int NOTIFY_START_DOWNLOAD = 3019;
    /**
     * 楽曲のDLの進捗通知
     */
    public static final int NOTIFY_DOWNLOAD_PROGRESS = 3020;
    /**
     * セッションキーの通知
     */
    public static final int UPDATE_SESSION_INFO = 3021;
    /**
     * スキップ残り回数の通知
     */
    public static final int NOTIFY_SKIP_REMAINING = 3022;
    // /**
    // * ログイン状態(アカウント凍結) auth/login, auth/login_facebook用メッセージ
    // */
    // public static final int STATUS_LOCKED = 323;
    /**
     * プレイリストの情報通知
     */
    public static final int NOTIFY_PLAYLIST_INFO = 3024;
    /**
     * 楽曲情報更新
     */
    // public static final int UPDATE_MUSIC_INFO_LOCAL = 3025;

    public static final int NOTIFY_DOWNLOAD_FAVORITE_TEMPLATE = 3026;
    public static final int NOTIFY_DOWNLOAD_TIMER_TEMPLATE = 3027;
    /**
     * 楽曲情報更新
     */
    // public static final int UPDATE_MUSIC_INFO_SIMUL = 3028;

    public static final int NOTIFY_FAILED_TO_RECOVER = 3029;

    public static final int NOTIFY_RENEWAL_TIMER_IS_CHANGED = 3030;
    public static final int NOTIFY_FEATURE_IS_CHANGED = 3031;
    public static final int NOTIFY_PERMISSION_IS_CHANGED = 3032;

    public static final int NOTIFY_BEGIN_EMERGENCY_MODE = 3033;
    public static final int NOTIFY_TIMEOUT_EMERGENCY_MODE = 3034;
    public static final int NOTIFY_PLAY_NEXT_EMERGENCY = 3035;
    public static final int NOTIFY_RECOVERY_EMERGENCY_MODE = 3036;

    /**
     * エラー通知
     */
    public static final int NOTIFY_ERROR = 6000;
    /**
     * メディアプレイヤーのエラー
     */
    public static final int MEDIA_PLAYER_ERROR = 6001;
    /**
     * エンコードエラー
     */
    public static final int ENCODE_ERROR = 6002;
    /**
     * サービスとの接続に失敗
     */
    public static final int SERVICE_CONNECTION_ERROR = 6003;

    public static final int DISABLE_LOCATION_PROVIDER = 6004;

    public static final int NO_EXIST_FILE = 6005;

    public static final int EJECT_STORAGE = 6006;

    /**
     * 終了通知
     */
    public static final int TERMINATION = 7000;
    /**
     * プレイヤー終了通知
     */
    public static final int MUSIC_TERMINATION = 7001;

    /**
     * 楽曲の再生が継続できなくなるような致命的なエラーかどうかの判断
     *
     * @param when エラーが発生した動作
     * @param code エラー内容
     * @return true:継続不可能な致命的エラーの場合, false:それ以外の場合
     */
    public static boolean isFatalMusicError(int when, int code) {
        boolean isFatal = false;

        switch (when) {
            case START_MUSIC:
            case START_AD:
            case START_LOCAL_MUSIC:
            case START_STREAM_MUSIC:

                isFatal = true;
                break;
            case NOTIFY_ERROR:
                if (code == MEDIA_PLAYER_ERROR)
                    isFatal = true;
                break;
        }

        return isFatal;
    }

    public static final String DECODER_EXCEPTION = "DecoderException";


    /**
     * 評価 : ノーマル
     */
    public static final int RATING_NOP = 1000;
    /**
     * 評価 : グッド
     */
    public static final int RATING_GOOD = 1001;
    /**
     * 評価 : バッド
     */
    public static final int RATING_BAD = 1002;
    /**
     * フリーユーザ
     */
    public static final int FREE_USER = 1003;
    /**
     * プレミアムユーザ
     */
    public static final int PREMIUM_USER = 1004;
    /**
     * 残り楽曲数が切れているユーザ
     */
    // public static final int NOTRACK_USER = 1005;
    /**
     * 残り有効時間が切れているユーザ
     */
    public static final int NOREMAIN_USER = 1006;
    /**
     * スーパーユーザ
     */
    // public static final int SUPER_USER = 1007;

    // プレーヤーの状態を表す定数
    public static final int PLAYER_STATUS_NODATA = 0;
    public static final int PLAYER_STATUS_PAUSING = 1;
    public static final int PLAYER_STATUS_PLAYING = 2;
    public static final int PLAYER_STATUS_NOINSTANCE = 3;

    /**
     * new UI
     **/
    public static final int TAG_MODE_GENRE = 1;
    public static final int TAG_MODE_SPECIAL = 2;
    public static final int TAG_MODE_HISTORY = 3;
    public static final int TAG_MODE_ARTIST = 4;
    public static final int TAG_MODE_RELEASE = 5;
    public static final int TAG_MODE_MYCHANNEL = 6;
    public static final int TAG_MODE_SETTING = 10;
    public static final int TAG_MODE_STREAMING = 11;
    public static final int TAG_MODE_TOP = 0;

    public static String getMenuString(Context context, int tag) {
        String str = null;
        switch (tag) {
            case TAG_MODE_GENRE:
                str = context.getString(R.string.page_title_genre_pro);
                break;
            case TAG_MODE_SPECIAL:
                str = context.getString(R.string.page_title_special_pro);
                break;
            case TAG_MODE_HISTORY:
                str = context.getString(R.string.page_title_goodlist_pro);
                break;
            case TAG_MODE_ARTIST:
                str = context.getString(R.string.page_title_artist);
                break;
            case TAG_MODE_RELEASE:
                str = context.getString(R.string.page_title_released_year_pro);
                break;
            case TAG_MODE_MYCHANNEL:
                str = context.getString(R.string.page_title_favorite);
                break;
            case TAG_MODE_SETTING:
                str = context.getString(R.string.menu_setting);
                break;
            case TAG_MODE_STREAMING:
                str = context.getString(R.string.page_title_simulcast_list);
                break;
        }

        return str;
    }

    public static boolean isPremiumMenu(int tag) {
        if (tag == TAG_MODE_ARTIST || tag == TAG_MODE_RELEASE || tag == TAG_MODE_MYCHANNEL) {
            return true;
        } else {
            return false;
        }
    }

    public static final int LOCKED = 2;
    public static final int UNLOCK = 1;

    public static final int[] COLOR_PALETTE_CIRCLE = {Color.parseColor("#83affa"), Color.parseColor("#927bfb"),
            Color.parseColor("#f993c0"), Color.parseColor("#e47070"), Color.parseColor("#f58c5f"),
            Color.parseColor("#f5d85f"), Color.parseColor("#aef460"), Color.parseColor("#88ecf7")};

    public static final int MUSIC_TYPE_STOP = 0;
    public static final int MUSIC_TYPE_LOCAL = 1;
    public static final int MUSIC_TYPE_SIMUL = 2;
    public static final int MUSIC_TYPE_NORMAL = 3;

    public static final byte WEEK_BIT_SUNDAY = 0x01;
    public static final byte WEEK_BIT_MONDAY = 0x02;
    public static final byte WEEK_BIT_TUESDAYT = 0x04;
    public static final byte WEEK_BIT_WEDNSDAY = 0x08;
    public static final byte WEEK_BIT_THURSDAY = 0x10;
    public static final byte WEEK_BIT_FRIDAY = 0x20;
    public static final byte WEEK_BIT_SATURDAY = 0x40;
    public static final byte WEEK_BIT_EVERYDAY = 0x7F;

    public static final byte[] WEEK_BYTE_ARRAY = {WEEK_BIT_SUNDAY, WEEK_BIT_MONDAY, WEEK_BIT_TUESDAYT,
            WEEK_BIT_WEDNSDAY, WEEK_BIT_THURSDAY, WEEK_BIT_FRIDAY, WEEK_BIT_SATURDAY};

    public static String[] getWeekStringArray() {
        String country = Utils.getLanguage();
        if (country.equals(Locale.JAPAN.getLanguage())) {
            return WEEK_STRING_ARRAY_JP;
        } else {
            return WEEK_STRING_ARRAY_EN;
        }
    }

    public static final String[] WEEK_STRING_ARRAY_JP = {"日", "月", "火", "水", "木", "金", "土"};
    public static final String[] WEEK_STRING_ARRAY_EN = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    public static final int NODE_TYPE_MIDDLE = 0;
    public static final int NODE_TYPE_CHANNEL = 1;

    public static final int LOCAL_PLAYER_NEXT = 0;
    public static final int LOCAL_PLAYER_PREVIOUS = 1;

    public static final int TEMPLATE_TYPE_BOOKMARK = 1;
    public static final int TEMPLATE_TYPE_TIMETABLE = 2;
    public static final String TEMPLATE_SOURCE_TYPE_FARAO = "farao";

    public static final int STREAM_TYPE_CABLE = 1;
    // public static final int STREAM_TYPE_COMMUNITY = 2;
    // public static final int STREAM_TYPE_LIVE = 3;
    public static final String HTTP_MSG_OK = "OK";

    public static final int TERM_TYPE_RATING_NORESPONSE = -1;
    public static final int TERM_TYPE_NO_RATING = -2;
    public static final int TERM_TYPE_FORCE = -3;

    public static final String PRIVATE_PATH_THUMB_LIST = "thumb_list_";
    public static final String PRIVATE_PATH_THUMB_HISTORY = "thumb_his_";

    public static final int MUSIC_TIMER_TYPE_MANUAL = 0;
    public static final int MUSIC_TIMER_TYPE_AUTO = 1;
    public static final String TAG_MUSIC_TIMER_TYPE_MANUAL = "0";
    public static final String TAG_MUSIC_TIMER_TYPE_AUTO = "1";

    public static final int SKIP_CONTROL_ENABLE = 0;
    public static final int SKIP_CONTROL_DISABLE = 1;

    public static final String AFFILIATE_BUY_CD = "Buy_CD";
    public static final String AFFILIATE_DOWNLOAD_ANDROID = "Android_DL";

    public static final String PATTERN_UPDATE_STATUS_OK = "0";
    public static final String PATTERN_UPDATE_STATUS_NG = "1";
    public static final String PATTERN_UPDATE_STATUS_ALL_NG = "2";
    public static final String PATTERN_UPDATE_STATUS_NOT_BLONG = "3";

    public static final String EMERGENCY_CAUSE_BASE = "android/emergency/";
    public static final String EMERGENCY_CAUSE_NETWORK = EMERGENCY_CAUSE_BASE + "network";
    public static final String EMERGENCY_CAUSE_PING = EMERGENCY_CAUSE_BASE + "ping";
    public static final String EMERGENCY_CAUSE_LOGIN = EMERGENCY_CAUSE_BASE + "login";
}
