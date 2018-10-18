package jp.faraopro.play.app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Parcel;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import jp.faraopro.play.R;
import jp.faraopro.play.act.TimerReceiver;
import jp.faraopro.play.app.InterruptFileDownloader.IDownloaderListener;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.common.DataComparator;
import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.common.LocationHelper;
import jp.faraopro.play.common.LocationHelper.IFROLocationListener;
import jp.faraopro.play.common.MyUncaughtExceptionHandler;
import jp.faraopro.play.common.SimpleCounter;
import jp.faraopro.play.domain.ChannelHistoryInfo;
import jp.faraopro.play.domain.FROChannelHistoryDBFactory;
import jp.faraopro.play.domain.FROFavoriteChannelDBFactory;
import jp.faraopro.play.domain.FROGoodHistoryDB;
import jp.faraopro.play.domain.FROGoodHistoryDBFactory;
import jp.faraopro.play.domain.FROMusicHistoryDB;
import jp.faraopro.play.domain.FROMusicHistoryDBFactory;
import jp.faraopro.play.domain.FROPatternContentDB;
import jp.faraopro.play.domain.FROPatternContentDBFactory;
import jp.faraopro.play.domain.FROPatternScheduleDB;
import jp.faraopro.play.domain.FROPatternScheduleDBFactory;
import jp.faraopro.play.domain.FROTemplateTimerAutoListDBFactory;
import jp.faraopro.play.domain.FROTimerAutoDBFactory;
import jp.faraopro.play.domain.FROUnsentRatingDB;
import jp.faraopro.play.domain.FROUnsentRatingDBFactory;
import jp.faraopro.play.domain.ITimerDB;
import jp.faraopro.play.domain.ListDataInfo;
import jp.faraopro.play.domain.MCListDataInfo;
import jp.faraopro.play.domain.MainPreference;
import jp.faraopro.play.domain.MusicInfo;
import jp.faraopro.play.domain.MusicInfoEx;
import jp.faraopro.play.domain.RatingInfo;
import jp.faraopro.play.domain.UserDataPreference;
import jp.faraopro.play.mclient.IMCItem;
import jp.faraopro.play.mclient.IMCItemList;
import jp.faraopro.play.mclient.IMCMusicInfoDB;
import jp.faraopro.play.mclient.IMCMusicItemInfo;
import jp.faraopro.play.mclient.IMCMusicItemList;
import jp.faraopro.play.mclient.IMCPostActionParam;
import jp.faraopro.play.mclient.IMCResultInfo;
import jp.faraopro.play.mclient.IMCResultListener;
import jp.faraopro.play.mclient.IMusicClientHandler;
import jp.faraopro.play.mclient.MCAudioItem;
import jp.faraopro.play.mclient.MCBenifitItem;
import jp.faraopro.play.mclient.MCBenifitItemList;
import jp.faraopro.play.mclient.MCBookmarkItem;
import jp.faraopro.play.mclient.MCBookmarkItemList;
import jp.faraopro.play.mclient.MCBusinessItem;
import jp.faraopro.play.mclient.MCBusinessItemList;
import jp.faraopro.play.mclient.MCCampaignItem;
import jp.faraopro.play.mclient.MCCdnMusicItem;
import jp.faraopro.play.mclient.MCChannelItem;
import jp.faraopro.play.mclient.MCChannelItemList;
import jp.faraopro.play.mclient.MCDefAction;
import jp.faraopro.play.mclient.MCDefParam;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCDefUser;
import jp.faraopro.play.mclient.MCError;
import jp.faraopro.play.mclient.MCFrameItem;
import jp.faraopro.play.mclient.MCFrameItemList;
import jp.faraopro.play.mclient.MCGenreItem;
import jp.faraopro.play.mclient.MCGenreItemList;
import jp.faraopro.play.mclient.MCHttpClient.IMCHttpClientListener;
import jp.faraopro.play.mclient.MCLocationItem;
import jp.faraopro.play.mclient.MCLocationItemList;
import jp.faraopro.play.mclient.MCMusicInfoDBFactory;
import jp.faraopro.play.mclient.MCOfflineMusicInfoDB;
import jp.faraopro.play.mclient.MCOfflineMusicInfoDBFactory;
import jp.faraopro.play.mclient.MCPostActionFactory;
import jp.faraopro.play.mclient.MCPostActionParam;
import jp.faraopro.play.mclient.MCScheduleItem;
import jp.faraopro.play.mclient.MCSearchItem;
import jp.faraopro.play.mclient.MCSearchItemList;
import jp.faraopro.play.mclient.MCStreamItem;
import jp.faraopro.play.mclient.MCStreamItemList;
import jp.faraopro.play.mclient.MCTemplateItem;
import jp.faraopro.play.mclient.MCTemplateItemList;
import jp.faraopro.play.mclient.MCTimetableItem;
import jp.faraopro.play.mclient.MCTimetableItemList;
import jp.faraopro.play.mclient.MCTrackItem;
import jp.faraopro.play.mclient.MCTrackItemList;
import jp.faraopro.play.mclient.MCUserInfoPreference;
import jp.faraopro.play.mclient.MusicClientFactory;
import jp.faraopro.play.mclient.MusicClientHandler;
import jp.faraopro.play.model.ChannelMode;
import jp.faraopro.play.model.FROUserData;
import jp.faraopro.play.model.ServiceLevel;
import jp.faraopro.play.model.TimerInfo;
import jp.faraopro.play.util.FROUtils;
import jp.faraopro.play.util.Utils;

/**
 * ネットワークを介する処理、ローカル領域へのデータ読み書き、 音楽プレイヤーの制御、ローカルサーバの制御を行う
 *
 * @author AIM
 */
public class FROHandler extends Service
        implements OnCompletionListener, IMCResultListener, IMCHttpClientListener {

    private boolean pref;

    private static class ChannelInfo {
        private static String channelId; // チャンネルID
        private static String mode; // 再生モード(genre,artist,date,hot,mychannel)
        private static String range; // 発売日の範囲
        private static String streamId;
        private static int permission;

        public static void setParams(String channelId, String mode, String range, int permission) {
            FRODebug.logD(FROHandler.class, "setParams:mode " + mode + ", channel " + channelId
                    + ", range " + range + ", permission " + permission, DEBUG);
            ChannelInfo.channelId = channelId;
            ChannelInfo.mode = mode;
            ChannelInfo.range = range;
            ChannelInfo.streamId = null;
            ChannelInfo.permission = permission;
        }

        public static void setParams(String streamId) {
            ChannelInfo.channelId = null;
            ChannelInfo.mode = null;
            ChannelInfo.range = null;
            ChannelInfo.streamId = streamId;
        }
    }

    // const
    private static final boolean DEBUG = true;
    public static final int PREMIUM_USER = 0;
    public static final int FREE_USER = 1;
    public static final String THUMBNAIL_PATH = FROUtils.getJacketPath();
    public static final String AD_PATH = FROUtils.getAdPath();
    private static final String TAG_WAKELOCK = "FARAOPRO_WAKELOCK";
    private static final String TAG_WIFILOCK = "FARAOPRO_WIFILOCK";
    private static final Object mMCResultLock = new Object();
    private static final Object mFormLock = new Object();
    private static final Object MUSIC_TERM_LOCK = new Object();
    private static final int HISTORY_LIMIT = 100;

    // member
    private RemoteCallbackList<Service_Callback_IF> listeners = new RemoteCallbackList<Service_Callback_IF>();
    private IMusicClientHandler mMCHandler = null;
    private IMCPostActionParam mMCParam = null;
    private FROPlayer mFaraoPlayer;
    private SimpleCounter counter;
    // private SDCardReceiver mSDReceiver;
    private TelephonyManager mTelephonyManager;
    // private Notification ntc;
    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String number) {
            phoneCallEvent(state, number);
        }
    };
    private int listType; // 取得しているリストの種別(genre,artist,hot,mychannel)
    private int playTime = 0; // 視聴した時間
    private int skipRemaining = 5; // スキップ残り回数
    private String nowPlayingId; // 再生中の楽曲のID
    private String decision = MCDefParam.MCP_PARAM_STR_RATING_NOP; // 楽曲の評価
    // private int paymentState = FREE_USER; // (UserInfo)アカウントの課金状態
    // private String trackingKey; // (UserInfo)トラッキングキー
    private RatingInfo lastRatingInfo;
    // private MCTrackItemList trackList; // 次にDLする楽曲のQueue
    private List<MCTrackItem> mTrackDownloadList;
    private MusicInfo infoJp; // 再生中の楽曲の情報
    private MusicInfo infoEn; // 同上

    // flags
    private boolean isWaittingForDL = false; // 再生待ちか否か
    private boolean isDownloading = false;
    private boolean isRating = false;
    private boolean resendFlag = false; // 再送シーケンスか否か
    private boolean isTerminating = false; // 終了シーケンスか否か
    private boolean isNotifyProgress = false;
    private boolean isAutoTimerRenewal = false;

    // JPN SUPPORT IN
    private final Object mWakelockGuard = new Object();
    // private boolean isPlayingAds;
    // private SimpleTimer count90sec;
    private static WakeLock mWakelock = null;
    private static WifiLock mWifilock = null;
    private ArrayList<RatingInfo> resendList;
    // JPN SUPPORT OUT

    private boolean isTalking; // 通話中判別フラグ

    private boolean skipControl = false;
    private MCBenifitItemList benifitList; // 特集チャンネルのリスト
    private String ticketDomain;
    private String ticketSerial;
    private String campaignId;
    private String channelName;
    private String channelNameEn;

    // cdn
    private MCCdnMusicItem cdnInfo;
    private MCStreamItem nowplayingStream;
    // private SimpleCounter countPlayTime;
    private ApiTaskManager apiManager;
    public HashMap<Integer, Runnable> extensionCallback = new HashMap<>();
    public List<String> hlsJacketUrl = new ArrayList<String>();

    // interrupt
    // private String mPatternScheduleDate;
    private List<String> mPatternScheduleDateList;
    private MCScheduleItem mCurrentScheduleItem;
    private NetworkStateChecker mStateCheckThread;

    // channel history
    private ChannelHistoryInfo mChannelHistory;

    private BroadcastReceiver mSdcardEjectReceiver;

    // member's setter and getter
    private synchronized void setWaittingForDL(boolean waitting) {
        if (isWaittingForDL != waitting) {
            FRODebug.logI(getClass(), "waitting flag : " + isWaittingForDL + " -> " + waitting, DEBUG);
            isWaittingForDL = waitting;
        }
    }

    private synchronized void setDownloading(boolean download) {
        if (isDownloading != download) {
            FRODebug.logI(getClass(), "downloading flag : " + isDownloading + " -> " + download, DEBUG);
            isDownloading = download;
        }
    }

    private synchronized void setRating(boolean rating) {
        if (isRating != rating) {
            FRODebug.logI(getClass(), "rating flag : " + isRating + " -> " + rating, DEBUG);
            isRating = rating;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Context context = getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(context));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int type = bundle.getInt(TimerReceiver.INTENT_TAG_TIMER_TYPE, -1);
                if (type != -1)
                    timerAction(type, intent);
            } else {
                mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
                setBroadcastReceiver();

                // プレイヤー起動中はCPUスリープ、Wifiスリープを抑止する
                synchronized (mWakelockGuard) {
                    if (mWakelock == null || !mWakelock.isHeld()) {
                        // CPUスリープが発生するのを抑止するため、Partial Wake Lockを取得
                        mWakelock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
                                .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG_WAKELOCK);
                        mWakelock.acquire();
                    }
                    if (mWifilock == null || !mWifilock.isHeld()) {
                        mWifilock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                                .createWifiLock(WifiManager.WIFI_MODE_FULL, TAG_WIFILOCK);
                        mWifilock.acquire();
                    }
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void setBroadcastReceiver() {
        if (mSdcardEjectReceiver != null)
            unregisterReceiver(mSdcardEjectReceiver);
        mSdcardEjectReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    // URI を取得し接続されたメディアが何であるか確認する
                    String dateString = intent.getDataString();
                    // SD カードであれば再生を停止する
                    if (dateString != null && dateString.contains("/storage/sdcard") && mFaraoPlayer != null) {
                        clearMusicData(true);
                        notifyToForm(Consts.NOTIFY_ERROR, Consts.EJECT_STORAGE, null);
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addDataScheme("file");
        registerReceiver(mSdcardEjectReceiver, filter);
    }

    private void removeBroadcastReceiver() {
        if (mSdcardEjectReceiver != null)
            unregisterReceiver(mSdcardEjectReceiver);
        mSdcardEjectReceiver = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // WakeLockを解放する
        synchronized (mWakelockGuard) {
            if (mWakelock != null && mWakelock.isHeld()) {
                // Partial Wake Lockを解放
                mWakelock.release();
                mWakelock = null;
            }
            if (mWifilock != null && mWifilock.isHeld()) {
                mWifilock.release();
                mWifilock = null;
            }
        }

        term(); // TODO 必要なければ外す
        if (mTelephonyManager != null) {
            mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
            mTelephonyManager = null;
        }
        removeBroadcastReceiver();
    }

    private Runnable mMusicTimerRunnable;
    private Runnable mPendingChannelAction;

    private void timerAction(int type, Intent intent) {
        Bundle bundle;
        MCPostActionParam copy = null;
        Runnable task;

        switch (type) {
            case TimerReceiver.TYPE_LICENSE_TIMER:
                bundle = intent.getExtras();
                int licenseType = bundle.getInt(TimerReceiver.INTENT_TAG_LICENSE_TYPE, -1);
                if (licenseType != -1) {
                    if (licenseType == TimerReceiver.LICENSE_CHECK_TYPE_FORCE || mFaraoPlayer != null) {
                        getLocation(MCDefAction.MCA_KIND_LICENSE_TRACKING);
                    } else {
                        // TODO サービスの終了
                    }
                }
                break;
            case TimerReceiver.TYPE_MUSIC_TIMER:
                byte[] byteArrayExtra = intent.getByteArrayExtra(TimerReceiver.INTENT_TAG_TIMER);
                if (byteArrayExtra != null) {
                    Parcel parcel = Parcel.obtain();
                    parcel.unmarshall(byteArrayExtra, 0, byteArrayExtra.length);
                    parcel.setDataPosition(0);
                    TimerInfo info = TimerInfo.CREATOR.createFromParcel(parcel);

                    // 割り込み再生時間と被っていないか確認する
                    MCScheduleItem schedule = FROPatternScheduleDBFactory.getInstance(getApplicationContext())
                            .findByDate(Utils.getNowDateString("yyyyMMdd"));
                    String scheduleMask = null;
                    if (schedule != null)
                        scheduleMask = schedule.getString(MCDefResult.MCR_KIND_SCHEDULE_MASK);
                    // Service クラスが稼動中で、scheduleMask = 1(禁止)となっている場合、または割り込み再生が ON
                    // になっている場合は再生する
                    boolean enableInterrupt = (scheduleMask != null && scheduleMask.equalsIgnoreCase("1"))
                            || MainPreference.getInstance(getApplicationContext()).getInterruptTimerEnable();
                    boolean isDuplicated = false;
                    if (enableInterrupt) {
                        Calendar current = Calendar.getInstance();
                        String patternId = (schedule != null) ? schedule.getString(MCDefResult.MCR_KIND_PATTERN_ID) : null;
                        MCFrameItem nextInterruptFrame = null;
                        if (!TextUtils.isEmpty(patternId)) {
                            nextInterruptFrame = FROPatternContentDBFactory.getInstance(getApplicationContext())
                                    .findSpecificDateFrame(patternId, Utils.getDateString(current, "kk:mm"));
                        }
                        FRODebug.logD(getClass(), "nextInterruptFrame = " + nextInterruptFrame, DEBUG);
                        if (nextInterruptFrame != null) {
                            String interruptTime = nextInterruptFrame.getString(MCDefResult.MCR_KIND_ONAIR_TIME);
                            String[] time = interruptTime.split(":");
                            int interruptOnair = Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1]);
                            int timerOnair = (info != null) ? info.getTime() : 0;
                            FRODebug.logD(getClass(), "timerOnair = " + timerOnair, DEBUG);
                            FRODebug.logD(getClass(), "interruptOnair = " + interruptOnair, DEBUG);
                            if (interruptOnair == timerOnair)
                                isDuplicated = true;
                        }
                        // プレイヤーが割り込み再生中 or 割込み開始時間 = タイマー開始時間
                        isDuplicated = (mFaraoPlayer != null && mFaraoPlayer.isInterrupt()) || isDuplicated;
                    }

                    FRODebug.logD(getClass(), "isDuplicated = " + isDuplicated, DEBUG);

                    if (info != null) {
                        // オフタイマーの場合、無条件で実施
                        if (info.getType() == Consts.MUSIC_TYPE_STOP) {
                            // 割込み再生の確認
                            if (!isDuplicated)
                                termMusic(0);
                            else {
                                // 割り込み再生中の場合は、行いたい動作を Runnable インスタンスに格納し先送りにする
                                mMusicTimerRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        termMusic(0);
                                    }
                                };
                            }
                        }
                        // オンタイマーの場合、オフラインモードでない場合にのみ実施
                        else {
                            if (mFaraoPlayer == null || mFaraoPlayer.getPlayerType() != FROPlayer.PLAYER_TYPE_OFFLINE) {
                                switch (info.getType()) {
                                    // タイマーON(ローカル再生)
                                    case Consts.MUSIC_TYPE_LOCAL:
                                        if (!isDuplicated)
                                            playLocal(info.getResource());
                                        else {
                                            final String localPath = info.getResource();
                                            mMusicTimerRunnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    playLocal(localPath);
                                                }
                                            };
                                        }
                                        break;
                                    // タイマーON(サイマル再生)
                                    case Consts.MUSIC_TYPE_SIMUL:
                                        if (!isDuplicated) {
                                            if (getStreamPlaylist(info.getResource()))
                                                apiManager.doTask();
                                        } else {
                                            final String streamId = info.getResource();
                                            mMusicTimerRunnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (getStreamPlaylist(streamId))
                                                        apiManager.doTask();
                                                }
                                            };
                                        }
                                        break;
                                    // タイマーON(お気に入り再生)
                                    case Consts.MUSIC_TYPE_NORMAL:
                                        if (!isDuplicated) {
                                            if (getPlayList(Integer.toString(info.getMode()), info.getChannelId(),
                                                    info.getRange(), 3, info.getPermission()))
                                                apiManager.doTask();
                                        } else {
                                            final String mode = Integer.toString(info.getMode());
                                            final int channel = info.getChannelId();
                                            final String range = info.getRange();
                                            final int permission = info.getPermission();
                                            mMusicTimerRunnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (getPlayList(mode, channel, range, 3, permission))
                                                        apiManager.doTask();
                                                }
                                            };
                                        }
                                        break;
                                }
                            } else {
                                switch (info.getType()) {
                                    // タイマーON(ローカル再生)
                                    case Consts.MUSIC_TYPE_LOCAL:
                                        if (mStateCheckThread != null) {
                                            localPath = info.getResource();
                                            mStateCheckThread.setPlayerType(FROPlayer.PLAYER_TYPE_LOCAL);
                                        }
                                        break;
                                    // タイマーON(サイマル再生)
                                    case Consts.MUSIC_TYPE_SIMUL:
                                        if (mStateCheckThread != null) {
                                            ChannelInfo.setParams(info.getResource());
                                            mStateCheckThread.setPlayerType(FROPlayer.PLAYER_TYPE_SIMUL);
                                        }
                                        break;
                                    // タイマーON(お気に入り再生)
                                    case Consts.MUSIC_TYPE_NORMAL:
                                        if (mStateCheckThread != null) {
                                            setModeParams(Integer.toString(info.getMode()), info.getChannelId(),
                                                    info.getRange(), info.getPermission());
                                            mStateCheckThread.setPlayerType(FROPlayer.PLAYER_TYPE_NOMAL);
                                        }
                                }
                            }
                        }
                    }
                    parcel.recycle();
                }
                break;
            case TimerReceiver.TYPE_TEMPLATE_TIMER:
                MCTemplateItem item = FROTemplateTimerAutoListDBFactory.getInstance(getApplicationContext()).find();
                if (item == null)
                    break;

                MCInit();
                mMCParam.setStringValue(MCDefParam.MCP_KIND_TEMPLATE_TYPE, "2");
                mMCParam.setStringValue(MCDefParam.MCP_KIND_TEMPLATE_ID, item.getString(MCDefResult.MCR_KIND_TEMPLATE_ID));
                isAutoTimerRenewal = true;

                copy = new MCPostActionParam();
                copy.copyParam(mMCParam);
                task = new ApiRunnable(copy) {
                    @Override
                    public void run() {
                        MCInit();
                        ((MCPostActionParam) mMCParam).copyParam(params);
                        mMCHandler.actDoAction(MCDefAction.MCA_KIND_TEMPLATE_LIST, mMCParam);
                    }
                };
                if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_TEMPLATE_LIST, copy, task)))
                    apiManager.doTask();
                break;
            case TimerReceiver.TYPE_INTERRUPT_SCHEDULE_TIMER:
                if (getInterruptSchedule(Utils.getNowDateString("yyyyMMdd")))
                    apiManager.doTask();
                break;
            case TimerReceiver.TYPE_INTERRUPT_MUSIC_TIMER:
                bundle = intent.getExtras();
                String patternId = null;
                String onairTime = null;
                if (bundle != null) {
                    patternId = bundle.getString(TimerReceiver.INTENT_TAG_PATTERN_ID);
                    onairTime = bundle.getString(TimerReceiver.INTENT_TAG_ONAIR_TIME);
                }
                synchronized (MUSIC_TERM_LOCK) {
                // 既に割り込み再生中であるかチェック
                boolean isInterruptPlaying = (mFaraoPlayer != null && mFaraoPlayer.isInterrupt());
                // patternId, onairTime が空でなく、かつ割り込み再生中でなければ、割り込み再生を開始する
                if (!TextUtils.isEmpty(patternId) && !TextUtils.isEmpty(onairTime) && !isInterruptPlaying) {
                    MCFrameItem frame = FROPatternContentDBFactory.getInstance(getApplicationContext())
                            .findSpecificDateFrame(patternId, onairTime);
                    List<MCAudioItem> audios = frame.getItem(MCDefResult.MCR_KIND_PATTERN_AUDIO);

                    // FRODebug.logD(getClass(), "patternId = " + patternId + ",
                    // onairTime = " + onairTime, DEBUG);
                    // FRODebug.logD(getClass(),
                    // "frame : " + frame.getmPatternId() + ", " +
                    // frame.getString(MCDefResult.MCR_KIND_ONAIR_TIME),
                    // DEBUG);
                    // for (int i = 0; i < audios.size(); i++) {
                    // MCAudioItem aaa = audios.get(i);
                    // FRODebug.logD(getClass(), "audioId = " +
                    // aaa.getString(MCDefResult.MCR_KIND_AUDIO_ID), DEBUG);
                    // }

                    mFrameId = frame.getString(MCDefResult.MCR_KIND_FRAME_ID);
                    // 音源の不足が無いか確認する
                    boolean isLacked = false;
                    MCListDataInfo dst = new MCListDataInfo();
                    dst.itemName = MCAudioItem.class.getSimpleName();
                    List<String> lackedList = new ArrayList<String>();
                    for (MCAudioItem audio : audios) {
                        if (!FROUtils.existPackageFile(getApplicationContext(),
                                audio.getString(MCDefResult.MCR_KIND_AUDIO_ID))) {
                            isLacked = true;
                            lackedList.add(audio.getString(MCDefResult.MCR_KIND_AUDIO_ID));
                        } else {
                            dst.add(audio);
                        }
                    }
                    // 既に再生中の場合は処理しない
                    if (!isLacked) {
                            FRODebug.logD(getClass(), "synchronized (MUSIC_TERM_LOCK)", DEBUG);
                            notifyToForm(Consts.INTERRUPT_LIST, Consts.STATUS_OK, dst);
                            if (mFaraoPlayer != null) { // プレイヤーが起動しており
                                if (mFaraoPlayer.isPlaying()) { // 楽曲再生中であれば
                                    // フェードをかけて停止し、割り込み再生
                                    mFaraoPlayer.fadeOut(3000);
                                    mFaraoPlayer.startInterrupt(audios, mInterruptStateListener, true);
                                    notifyToForm(Consts.START_INTERRUPT, Consts.STATUS_OK, "0");
                                } else { // そうでなければフェードをかけずに再生
                                    mFaraoPlayer.startInterrupt(audios, mInterruptStateListener, false);
                                    notifyToForm(Consts.START_INTERRUPT, Consts.STATUS_OK, "0");
                                }
                            } else { // プレイヤーが起動していない場合
                                mFaraoPlayer = new FROPlayer(getApplicationContext());
                                mFaraoPlayer.startInterrupt(audios, mInterruptStateListener, false);
                                mFaraoPlayer.attach(this);
                                notifyToForm(Consts.START_INTERRUPT, Consts.STATUS_OK, "0");
                            }
                            NotificationHelper.showNotification(this, "Fans' Shop BGM",getString(R.string.msg_interrupt_playing_switch));
                    } else {
                        mInterruptStateListener.onFinish(lackedList, "no",
                                (mFaraoPlayer != null && mFaraoPlayer.isPlaying()) ? true : false);
                        }
                    }
                }
                // タイマーの更新は必ず行う
                refreshInterruptMusicTimer(getApplicationContext(), false);
                break;
        }
    }

    private String mFrameId;
    final FROPlayer.IInterruptStateListener mInterruptStateListener = new FROPlayer.IInterruptStateListener() {
        @Override
        public void onNext(int nextIndex) {
            notifyToForm(Consts.START_INTERRUPT, Consts.STATUS_OK, Integer.toString(nextIndex));
        }

        @Override
        public void onFinish(List<String> playedAudios, String complete, boolean isPlayingBeforeInterrupting) {
            // 割り込み再生により先送りされたタイマーがあるか確認する
            if (mMusicTimerRunnable != null) {
                // タイマーがあった場合はそれを実行するだけ
                if (mPendingChannelAction != null) {
                    clearMusicData();
                    mPendingChannelAction = null;
                }
                mMusicTimerRunnable.run();
                mMusicTimerRunnable = null;
            }
            // ペンディング中の再生アクションがあれば、それを実行する
            else if (mPendingChannelAction != null) {
                mPendingChannelAction.run();
                mPendingChannelAction = null;
            }
            // タイマーがなければ、割込み前に再生していたプレイヤーの状態を復元する
            else {
                if (mFaraoPlayer != null) {
                    switch (mFaraoPlayer.getPlayerType()) {
                        case FROPlayer.PLAYER_TYPE_NOMAL:
                            if (isPlayingBeforeInterrupting)
                                mFaraoPlayer.fadeIn(3000);
                            MusicInfo info;
                            if (FROUtils.isPrimaryLanguage())
                                info = infoJp;
                            else
                                info = infoEn;
                            NotificationHelper.showNotification(FROHandler.this, info.getTitle(), info.getArtist(),
                                    NotificationHelper.getPendingIntent(FROHandler.this));
                            break;
                        case FROPlayer.PLAYER_TYPE_SIMUL:
                            NotificationHelper.showNotification(FROHandler.this, "Fans' Shop BGM",
                                    nowplayingStream.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME),
                                    NotificationHelper.getPendingIntent(FROHandler.this));
                            if (isPlayingBeforeInterrupting) {
                            if (getStreamPlaylist(FROHandler.ChannelInfo.streamId)) {
                                apiManager.doTask();
                                }
                            }
                            break;
                        case FROPlayer.PLAYER_TYPE_LOCAL:
                            if (isPlayingBeforeInterrupting)
                                mFaraoPlayer.fadeIn(3000);
                            NotificationHelper.showNotification(FROHandler.this, "Fans' Shop BGM",
                                    getString(R.string.cap_local_play),
                                    NotificationHelper.getPendingIntent(FROHandler.this));
                            break;
                        case FROPlayer.PLAYER_TYPE_OFFLINE:
                            if (isPlayingBeforeInterrupting)
                                mFaraoPlayer.fadeIn(3000);
                            NotificationHelper.showNotification(FROHandler.this, "Fans' Shop BGM", getString(R.string.page_title_offline_player));
                            break;
                        case FROPlayer.PLAYER_TYPE_NONE:
                            mFaraoPlayer.term();
                            mFaraoPlayer = null;
                            stopForeground(true);
                            break;
                    }
                }
            }
            notifyToForm(Consts.END_INTERRUPT, Consts.STATUS_OK, null);
            if (callapiOnair(mFrameId, playedAudios, complete))
                apiManager.doTask();
        }
    };

    /**
     * 着信時の処理
     *
     * @param state  : 着信状態
     * @param number : 番号
     */
    private void phoneCallEvent(int state, String number) {
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isTalking = true;
                if (mFaraoPlayer != null && mFaraoPlayer.isPlaying()) {
                    pauseOrStartMusic();
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                isTalking = false;
                break;
            default:
                break;
        }
    }

    // 自動再生版、デバックはしておらず
    // private void phoneCallEvent(int state, String number){
    // if( faraoPlayer != null ) {
    // if( !nowState ) {
    // nowState = faraoPlayer.isPlaying();
    // }
    // if( nowState ) {
    // switch( state ) {
    // case TelephonyManager.CALL_STATE_RINGING:
    // case TelephonyManager.CALL_STATE_OFFHOOK:
    // if( faraoPlayer != null && faraoPlayer.isPlaying() ) {
    // faraoPlayer.startOrPause();
    // }
    // break;
    // case TelephonyManager.CALL_STATE_IDLE:
    // if( faraoPlayer != null && !faraoPlayer.isPlaying() ) {
    // faraoPlayer.startOrPause();
    // nowState = false;
    // }
    // break;
    // }
    // }
    // }
    // }

    // MusicClientの初期化処理
    // IMusicClientHandler.actDoActionを呼び出す前に必ず実行すること
    private void MCInit() {
        if (mMCHandler == null) {
            mMCHandler = MusicClientFactory.getInstance(getApplicationContext(), this);
        }
        if (mMCParam != null) {
            mMCParam.clear();
            mMCParam = null;
        }
        mMCParam = MCPostActionFactory.getInstance();

        if (apiManager == null)
            apiManager = new ApiTaskManager();
    }

    /**
     * 現在選択されているモードのセット
     *
     * @param mode    : モード番号
     * @param channel : チャンネルID
     * @param range   : リリース範囲
     */
    public void setModeParams(String mode, int channel, String range, int permission) {
        String channelId = Integer.toString(channel);
        if (permission == 0x0000)
            permission = 0xFFFF;
        ChannelInfo.setParams(channelId, mode, range, permission);
    }

    /**
     * Formからの要求
     */
    private final IBindService.Stub binder = new IBindService.Stub() {

        /**
         * リスナー登録
         *
         * @param listener
         *            リスナーIF
         */
        @Override
        public void addListener(Service_Callback_IF listener) throws RemoteException {
            listeners.register(listener);
        }

        /**
         * リスナー削除
         *
         * @param listener
         *            リスナーIF
         */
        @Override
        public void removeListener(Service_Callback_IF listener) throws RemoteException {
            listeners.unregister(listener);
        }

        /**
         * サインイン（アカウント新規登録）
         *
         * @param mail
         *            メール
         * @param pass
         *            パスワード
         */
        @Override
        public void sSignin(String mail, String pass) throws RemoteException {
            MCInit();
            mMCParam.setStringValue(MCDefParam.MCP_KIND_EMAIL, mail);
            mMCParam.setStringValue(MCDefParam.MCP_KIND_PASSWORD, pass);
            mMCParam.setStringValue(MCDefParam.MCP_KIND_LANGUAGE, Utils.getLanguage());

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_SIGNUP, copy, null)))
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_SIGNUP, mMCParam);
        }

        /**
         * ユーザアクティベーション 未使用
         */
        @Override
        public void sActivation() throws RemoteException {
            // MCInit();
            // mMCParam.setStringValue(MCDefParam.MCP_KIND_ACTIVATIONKEY,
            // null);//mActivationKey);
            // mMCHandler.actDoAction(MCDefAction.MCA_KIND_ACTIVATION,
            // mMCParam);
        }

        /**
         * ログイン
         *
         * @param mail
         *            メール
         * @param pass
         *            パスワード
         * @param force
         *            強制ログインの可否
         */
        @Override
        public void sLogin(String mail, String pass, final String force) throws RemoteException {
            if (emergencyModeHelper != null && emergencyModeHelper.isRunning()) {
                notifyToForm(MCDefAction.MCA_KIND_LOGIN, Consts.NOTIFY_BEGIN_EMERGENCY_MODE, null);
                return;
            }
            if (mail == null || pass == null) {
                if (!TextUtils.isEmpty(MCUserInfoPreference.getInstance(getApplicationContext()).getSessionKey())) {
                    mail = MCUserInfoPreference.getInstance(getApplicationContext()).getEmail();
                    pass = MCUserInfoPreference.getInstance(getApplicationContext()).getPassword();
                }
            }
            if (!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pass)) {
                loginWithCheck(mail, pass, force);
            } else {
                notifyToForm(MCDefAction.MCA_KIND_LOGIN, MCError.MC_BAD_REQUEST, null);
            }
        }

        @Override
        public boolean sIsEmergency() throws RemoteException {
            return emergencyModeHelper != null && emergencyModeHelper.isRunning();
        }

        @Override
        public void sPlayEmergency() throws RemoteException {
            if (emergencyModeHelper != null)
                emergencyModeHelper.play();
        }

        @Override
        public void sPauseEmergency() throws RemoteException {
            if (emergencyModeHelper != null) {
                emergencyModeHelper.pause();
                if (emergencyModeHelper.isRecovery()) {
                    NotificationHelper.removeNotification(FROHandler.this);
                    emergencyModeHelper.stopEmergencyMode();
                    new Thread(loginAction).start();
                    loginAction = null;
                } else if (emergencyModeHelper.isTimeout()) {
                    NotificationHelper.removeNotification(FROHandler.this);
                    stopEmergency();
                    notifyToForm(MCDefAction.MCA_KIND_LOGIN, Consts.NOTIFY_TIMEOUT_EMERGENCY_MODE, null);
                }
            }
        }

        @Override
        public void sCancelEmergency() throws RemoteException {
            stopEmergency();
            NotificationHelper.removeNotification(FROHandler.this);
        }

        @Override
        public int sGetEmergencyLength() throws RemoteException {
            return (emergencyModeHelper != null) ? emergencyModeHelper.getLength() : 0;
        }

        @Override
        public int sGetEmergencyPosition() throws RemoteException {
            return (emergencyModeHelper != null) ? emergencyModeHelper.getPosition() : 0;
        }

        @Override
        public boolean sIsPlayingEmergency() throws RemoteException {
            return emergencyModeHelper != null && emergencyModeHelper.isPlaying();
        }

        @Override
        public boolean sIsRecoveredEmergency() throws RemoteException {
            return emergencyModeHelper != null && emergencyModeHelper.isRecovery();
        }

        /**
         * ログアウト
         */
        @Override
        public void sLogout() {
            if (logout())
                apiManager.doTask();
        }

        /**
         * ユーザ情報取得
         */
        @Override
        public void sGetStatus() throws RemoteException {
            MCInit();
            if (!apiManager.isContained(MCDefAction.MCA_KIND_STATUS)) {
                MCPostActionParam copy = new MCPostActionParam();
                copy.copyParam(mMCParam);
                if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_STATUS, copy, null)))
                    mMCHandler.actDoAction(MCDefAction.MCA_KIND_STATUS, mMCParam);
            }
        }

        /**
         * ジャンル一覧ダウンロード
         */
        @Override
        public void sGetGenreList() throws RemoteException {
            MCInit();
            listType = IMCResultInfo.MC_LIST_KIND_GENRE;
            mMCParam.setStringValue(MCDefParam.MCP_KIND_MODE_NO, "1");

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_LIST, copy, null)))
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_LIST, mMCParam);
        }

        /**
         * アーティスト候補ダウンロード
         */
        @Override
        public void sGetArtistList(String word) throws RemoteException {
            MCInit();
            listType = IMCResultInfo.MC_LIST_KIND_SEARCH;
            mMCParam.setStringValue(MCDefParam.MCP_KIND_MODE_NO, "2");
            mMCParam.setStringValue(MCDefParam.MCP_KIND_KEYWORD, word);

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_SEARCH, copy, null)))
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_SEARCH, mMCParam);
        }

        /**
         * ホット100一覧ダウンロード
         */
        @Override
        public void sGetHot100List() throws RemoteException {
            MCInit();
            listType = IMCResultInfo.MC_LIST_KIND_CHART;
            mMCParam.setStringValue(MCDefParam.MCP_KIND_MODE_NO, "8");

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_LIST, copy, null)))
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_LIST, mMCParam);
        }

        /**
         * マイチャンネル一覧ダウンロード
         */
        @Override
        public void sGetMychannelList() throws RemoteException {
            MCInit();
            listType = IMCResultInfo.MC_LIST_KIND_CHANNEL;
            mMCParam.setStringValue(MCDefParam.MCP_KIND_MODE_NO, "5");

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_LIST, copy, null)))
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_LIST, mMCParam);
        }

        /**
         * ウェルカムメッセージダウンロード
         */
        @Override
        public void sGetMsg(String type) throws RemoteException {
            MCInit();
            mMCParam.setStringValue(MCDefParam.MCP_KIND_TYPE_DL, type);

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_MSG_DL, copy, null)))
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_MSG_DL, mMCParam);
        }

        /**
         * プレイリストダウンロード
         *
         * @param mode
         *            嗜好性パラメータ
         * @param channel
         *            嗜好性パラメータ
         * @param range
         *            嗜好性パラメータ
         * @param size
         *            ダウンロード数、基本1固定
         */
        @Override
        public void sGetPlayLists(int mode, int channel, String range, int size, int permisson) throws RemoteException {
            if (getPlayList(Integer.toString(mode), channel, range, size, permisson)) {
                apiManager.doTask();
            }
        }

        /**
         * 評価+次曲再生処理
         */
        @Override
        public void sRequestNext(String decison) throws RemoteException {
            mFaraoPlayer.stop();
            playTime = counter.getCount();
            counter.reset();
            if (ratingAndPlay("no")) {
                apiManager.doTask();
            }
        }

        @Override
        public void sRequestNextLocal(int direction) {
            skipLocal(direction);
        }

        /**
         * マイチャンネル設定
         */
        @Override
        public void sSetMyChannel(int id, String name, int lock) throws RemoteException {
        }

        /**
         * 評価のセット
         */
        @Override
        public void sSendDecision(String userDecision) throws RemoteException {
            decision = userDecision;
        }

        /**
         * 再生停止・再開
         */
        @Override
        public void sPauseMusic() throws RemoteException {
            if (mFaraoPlayer != null) {
                switch (mFaraoPlayer.getPlayerType()) {
                    case FROPlayer.PLAYER_TYPE_LOCAL:
                        mFaraoPlayer.startOrPause();
                        break;
                    case FROPlayer.PLAYER_TYPE_SIMUL:
                        if (mFaraoPlayer.isPlaying()) {
                            mFaraoPlayer.stop();
                        } else {
                            if (getStreamPlaylist(FROHandler.ChannelInfo.streamId)) {
                                apiManager.doTask();
                            }
                        }
                        break;
                    case FROPlayer.PLAYER_TYPE_NOMAL:
                        pauseOrStartMusic();
                        break;
                }
            }
        }

        /**
         * 再生中か否か
         */
        @Override
        public boolean sIsPlaying() throws RemoteException {
            boolean bool = false;
            if (mFaraoPlayer != null) {
                bool = mFaraoPlayer.isPlaying();
            }

            return bool;
        }

        /**
         * PlayerInstanceが存在するか否か
         */
        @Override
        public int sCheckPlayerInstance() throws RemoteException {
            int status = Consts.PLAYER_STATUS_NOINSTANCE;

            if (mFaraoPlayer != null) {
                IMCMusicItemInfo nextMusicInfo = mMCHandler
                        .mdbGet2(String.valueOf(IMCMusicItemInfo.MCDB_STATUS_COMPLETE));
                if (nextMusicInfo != null || isRating || isDownloading) {
                    if (mFaraoPlayer.isPlaying()) {
                        status = Consts.PLAYER_STATUS_PLAYING;
                    } else {
                        status = Consts.PLAYER_STATUS_PAUSING;
                    }
                } else {
                    status = Consts.PLAYER_STATUS_NODATA;
                }
            }

            return status;
        }

        /**
         * サービス終了
         */
        @Override
        public void sTerm() throws RemoteException {
            term();
            notifyToForm(Consts.TERMINATION, Consts.STATUS_OK, null);
        }

        /**
         * プレイヤーの終了要求 Formからこの要求が来た場合は 1.エラー発生 2.エラー発生(評価不要) 3.通常の終了処理
         * のいずれかに区分される 2．の評価不要の場合のみ、音楽データを解放してレスポンスを返す 他二つは評価APIを呼び出すため非同期処理
         */
        @Override
        public void sTermMusic(int type) throws RemoteException {
            termMusic(type);
        }

        /**
         * 課金通知
         */
        @Override
        public void sendPurchaseInfomation(String key, int type, int market, String receipt) throws RemoteException {
        }

        /**
         * 購入前ロック
         */
        @Override
        public void purchaseLock(String key) throws RemoteException {
        }

        /**
         * 購入適用
         */
        @Override
        public void purchaseCommit(String key) throws RemoteException {
        }

        /**
         * 購入キャンセル
         */
        @Override
        public void purchaseCancel(String key) throws RemoteException {
        }

        /**
         * 1Hタイマーの通知
         */
        @Override
        public void oneHourNotify() throws RemoteException {
            if (mFaraoPlayer != null && mFaraoPlayer.isPlaying()) {
                pauseOrStartMusic();
            }
        }

        /**
         * 楽曲情報更新要請
         */
        @Override
        public void updateMusicInfo() throws RemoteException {
            // 先にユーザデータを送信する
            notifyToForm(MCDefAction.MCA_KIND_STATUS, Consts.STATUS_OK, new FROUserData(getApplicationContext()));

            if (mFaraoPlayer != null) {
                // 割り込み再生中の場合は INTERRUPT_LIST 割り込み音源のリストを送る
                if (mFaraoPlayer.isInterrupt()) {
                    MCListDataInfo dst = new MCListDataInfo();
                    dst.itemName = MCAudioItem.class.getSimpleName();
                    for (MCAudioItem audio : mFaraoPlayer.getInterruptList()) {
                        dst.add(audio);
                    }
                    notifyToForm(Consts.INTERRUPT_LIST, Consts.STATUS_OK, dst);
                }
                MusicInfo info = null;
                // 更に別の音源が再生している場合は UPDATE_MUSIC_INFO で楽曲情報を送る
                if (mFaraoPlayer.getPlayerType() == FROPlayer.PLAYER_TYPE_LOCAL) {
                    info = new MusicInfo();
                    mFaraoPlayer.getMusicData(info);
                    info.setPlayerType(FROPlayer.PLAYER_TYPE_LOCAL);
                    info.setId(Integer.toString(localTrackIndex));
                    info.setUrl(localPath);
                    info.setUrlSearch(localTrackList.get(localTrackIndex).getAbsolutePath());
                } else if (mFaraoPlayer.getPlayerType() == FROPlayer.PLAYER_TYPE_SIMUL) {
                    info = new MusicInfo(nowplayingStream);
                    info.setPlayerType(FROPlayer.PLAYER_TYPE_SIMUL);
                } else if (mFaraoPlayer.getPlayerType() == FROPlayer.PLAYER_TYPE_OFFLINE) {
                    info = new MusicInfo();
                    info.setPlayerType(FROPlayer.PLAYER_TYPE_OFFLINE);
                } else if (infoJp != null && infoEn != null) {
                    ListDataInfo playlistInfo = new ListDataInfo();
                    playlistInfo.add(ChannelInfo.mode);
                    playlistInfo.add(ChannelInfo.channelId);
                    playlistInfo.add(ChannelInfo.range);
                    playlistInfo.add(Integer.toString(ChannelInfo.permission));
                    notifyToForm(Consts.UPDATE_MODE_INFO, Consts.STATUS_OK, playlistInfo);
                    notifyToForm(Consts.NOTIFY_SKIP_REMAINING, Consts.STATUS_OK, Integer.toString(skipRemaining));
                    notifyToForm(Consts.NOTIFY_PLAYLIST_INFO, Consts.STATUS_OK, Boolean.toString(skipControl));
                    info = (FROUtils.isPrimaryLanguage()) ? infoJp : infoEn;
                }
                notifyToForm(Consts.UPDATE_MUSIC_INFO, Consts.STATUS_OK, info);
            }
        }

        @Override
        public void sUpdateInterruptData() throws RemoteException {
            if (mFaraoPlayer == null)
                notifyToForm(Consts.UPDATE_MUSIC_INFO, Consts.STATUS_OK, null);

            notifyToForm(Consts.UPDATE_MUSIC_INFO, Consts.STATUS_OK, null);
        }

        // /**
        // * 評価再送要請
        // */
        // public boolean sResendRating() throws RemoteException {
        // // return checkUnsentData();
        // }

        /**
         * モード種別の取得
         */
        @Override
        public String sGetModeType() throws RemoteException {
            return ChannelInfo.mode;
        }

        /**
         * チャンネルシェア
         */
        @Override
        public void sChannelShare(String id) throws RemoteException {
            MCInit();
            // パラメータ名がかぶっているので流用する
            mMCParam.setStringValue(MCDefParam.MCP_KIND_MYCHANNEL_ID, id);

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_CHANNEL_SHARE, copy, null)))
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_CHANNEL_SHARE, mMCParam);
        }

        /**
         * チャンネル展開
         */
        @Override
        public void sChannelExpand(String expandKey) throws RemoteException {
            MCInit();
            mMCParam.setStringValue(MCDefParam.MCP_KIND_SHARE_SHAREKEY, expandKey);

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_CHANNEL_EXPAND, copy, null)))
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_CHANNEL_EXPAND, mMCParam);

        }

        /**
         * プレイヤーの起動をキャンセルする
         */
        @Override
        public void sCancelBoot() throws RemoteException {
            synchronized (MUSIC_TERM_LOCK) {
                setWaittingForDL(false);
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        clearMusicData(true);
                        apiManager.clearTask();
                        notifyToForm(Consts.NOTIFY_COMPLETE_CANCEL, Consts.STATUS_OK, null);
                    }
                };
                if (apiManager.setTask(new ApiTask(Consts.NOTIFY_COMPLETE_CANCEL, null, task)))
                    apiManager.doTask();

            }
        }

        @Override
        public void sInterruptCancel() throws RemoteException {
        }

        /**
         * セッションを取得する
         */
        @Override
        public String sGetSession() throws RemoteException {
            String session = mMCHandler.usrGetValue(MCDefUser.MCU_KIND_SESSIONKEY);
            // notifyToForm(Consts.UPDATE_SESSION_INFO,
            // Consts.STATUS_OK,
            // session);
            return session;
        }

        /**
         * Facebook連携<br>
         * FaRaoアカウントとして登録されているか確認する
         */
        @Override
        public void sFacebookLookup(String email) throws RemoteException {
        }

        /**
         * Facebook連携<br>
         * FacebookアカウントでFaRaoにログインする
         */
        @Override
        public void sFacebookLogin(String token, String email, String force) throws RemoteException {
        }

        /**
         * Facebook連携<br>
         * FacebookのアカウントでFaRaoのアカウントを作成する
         */
        @Override
        public void sFacebookAccount(String mail, String pass, int gender, int year, String province, int region,
                                     String country) throws RemoteException {
        }

        /**
         * オススメアーティスト一覧ダウンロード
         */
        @Override
        public void sGetFeaturedArtistList() throws RemoteException {
            MCInit();
            listType = IMCResultInfo.MC_LIST_KIND_FEATURED;
            mMCParam.setStringValue(MCDefParam.MCP_KIND_MODE_NO, "2");

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_FEATURED, copy, null)))
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_FEATURED, mMCParam);
        }

        /**
         * 地域データの取得
         */
        @Override
        public void sGetLocationList() throws RemoteException {
            MCInit();

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_LOCATION, copy, null)))
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_LOCATION, mMCParam);
        }

        @Override
        public void sAdRating(String rating) throws RemoteException {
        }

        @Override
        public void sCheckTicket(String domain, String serial) throws RemoteException {
            MCInit();
            String trackingKey = UserDataPreference.getTrackingKey(getApplicationContext());
            if (trackingKey == null) {
                // TODO エラーケース
            }
            ticketDomain = domain;
            ticketSerial = serial;
            mMCParam.setStringValue(MCDefParam.MCP_KIND_TRACKING_KEY, trackingKey);
            mMCParam.setStringValue(MCDefParam.MCP_KIND_TICKET_DOMAIN, domain);
            mMCParam.setStringValue(MCDefParam.MCP_KIND_TICKET_SERIAL, serial);

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_TICKET_CHECK, copy, null)))
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_TICKET_CHECK, mMCParam);
        }

        @Override
        public void sAddTicket() throws RemoteException {
            MCInit();
            mMCParam.setStringValue(MCDefParam.MCP_KIND_TRACKING_KEY,
                    UserDataPreference.getTrackingKey(getApplicationContext()));
            mMCParam.setStringValue(MCDefParam.MCP_KIND_TICKET_DOMAIN, ticketDomain);
            mMCParam.setStringValue(MCDefParam.MCP_KIND_TICKET_SERIAL, ticketSerial);
            mMCParam.setStringValue(MCDefParam.MCP_KIND_CAMPAIGN_ID, campaignId);

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_TICKET_ADD, copy, null)))
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_TICKET_ADD, mMCParam);
        }

        @Override
        public void sSetSleepTimer(int time) throws RemoteException {
            MCUserInfoPreference.getInstance(getApplicationContext()).setSleepTime(time);
        }

        @Override
        public int sGetCurrentPos() throws RemoteException {
            int pos = 0;
            MusicInfo info = new MusicInfo();
            if (mFaraoPlayer != null) {
                mFaraoPlayer.getMusicData(info);
                pos = info.getCurrentPos();
            }

            return pos;
        }

        @Override
        public void sSearchShop(String latitude, String longitude, String distance, String industry)
                throws RemoteException {
            MCInit();
            mMCParam.setStringValue(MCDefParam.MCP_KIND_LATITUDE, latitude);
            mMCParam.setStringValue(MCDefParam.MCP_KIND_LONGITUDE, longitude);
            mMCParam.setStringValue(MCDefParam.MCP_KIND_DISTANCE, distance);
            mMCParam.setStringValue(MCDefParam.MCP_KIND_INDUSTRY, industry);

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_SEARCH_SHOP, copy, null)))
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_SEARCH_SHOP, mMCParam);
        }

        @Override
        public void sGetBusinessList(String node) throws RemoteException {
            MCInit();
            listType = IMCResultInfo.MC_LIST_KIND_BUSINESS;
            mMCParam.setStringValue(MCDefParam.MCP_KIND_MODE_NO, "11");
            mMCParam.setStringValue(MCDefParam.MCP_KIND_PARENT_NODE, node);

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_LIST, copy, null)))
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_LIST, mMCParam);
        }

        @Override
        public void sPlayLocal(String path) throws RemoteException {
            playLocal(path);
        }

        @Override
        public void sGetTemplateList(int type) throws RemoteException {
            String param = Integer.toString(type);
            MCInit();
            mMCParam.setStringValue(MCDefParam.MCP_KIND_TEMPLATE_TYPE, param);

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_TEMPLATE_LIST, copy, null)))
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_TEMPLATE_LIST, mMCParam);
        }

        @Override
        public void sDownloadTemplateList(int type, int id) throws RemoteException {
            String param1 = Integer.toString(type);
            String param2 = Integer.toString(id);
            MCInit();
            mMCParam.setStringValue(MCDefParam.MCP_KIND_TEMPLATE_TYPE, param1);
            mMCParam.setStringValue(MCDefParam.MCP_KIND_TEMPLATE_ID, param2);

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_TEMPLATE_DOWNLOAD, copy, null)))
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_TEMPLATE_DOWNLOAD, mMCParam);
        }

        @Override
        public void callAPI() throws RemoteException {
        }

        @Override
        public void sGetStreamList(int type) throws RemoteException {
            String param1 = Integer.toString(type);
            MCInit();
            mMCParam.setStringValue(MCDefParam.MCP_KIND_SOURCE_TYPE, param1);

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_STREAM_LIST, copy, null)))
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_STREAM_LIST, mMCParam);
        }

        @Override
        public void sPlayStream(int streamId) throws RemoteException {
            String param1 = Integer.toString(streamId);
            if (getStreamPlaylist(param1))
                apiManager.doTask();
        }

        @Override
        public void sSendTemplateState() throws RemoteException {
            if (updateTemplateState(null))
                apiManager.doTask();
        }

        @Override
        public boolean sIsInterrupt() throws RemoteException {
            boolean isInterrupt = false;
            if (mFaraoPlayer != null)
                isInterrupt = mFaraoPlayer.isInterrupt();

            return isInterrupt;
        }

        @Override
        public void sCheckInterruptSchedule() throws RemoteException {
            if (getInterruptSchedule(Utils.getNowDateString("yyyyMMdd"))) {
                apiManager.doTask();
            }
        }

        @Override
        public void sSetPreferenceBoolean(int type, boolean value) throws RemoteException {
            switch (type) {
                case MCUserInfoPreference.BOOLEAN_DATA_ENABLE_MUSIC_TIMER:
                    MCUserInfoPreference.getInstance(getApplicationContext()).setMusicTimerEnable(value);
                    break;
            }
        }

        @Override
        public void sSetPreferenceInteger(int type, int value) throws RemoteException {
            switch (type) {
                case MCUserInfoPreference.INTEGER_DATA_MUSIC_TIMER_TYPE:
                    MCUserInfoPreference.getInstance(getApplicationContext()).setMusicTimerType(value);
                    break;
            }
        }

        @Override
        public void sLicenseStatus() throws RemoteException {
            if (licenseStatus())
                apiManager.doTask();
        }

        @Override
        public int sGetPlayerType() throws RemoteException {
            int type = FROPlayer.PLAYER_TYPE_NONE;
            if (mFaraoPlayer != null) {
                type = mFaraoPlayer.getPlayerType();
            }

            return type;
        }

        @Override
        public void sChangeChannelVolume(float volume) throws RemoteException {
            MCUserInfoPreference.getInstance(getApplicationContext()).setChannelVolume(volume);
            if (mFaraoPlayer != null)
                mFaraoPlayer.changeChannelVolume();
        }

        @Override
        public void sChangeInterruptVolume(float volume) throws RemoteException {
            MCUserInfoPreference.getInstance(getApplicationContext()).setInterruptVolume(volume);
            if (mFaraoPlayer != null)
                mFaraoPlayer.changeInterruptVolume();
        }

        @Override
        public float sGetChannelVolume() throws RemoteException {
            return MCUserInfoPreference.getInstance(getApplicationContext()).getChannelVolume();
        }

        @Override
        public float sGetInterruptVolume() throws RemoteException {
            return MCUserInfoPreference.getInstance(getApplicationContext()).getInterruptVolume();
        }

        @Override
        public void sChangeAudioFocusedVolume(float volume) throws RemoteException {
            MCUserInfoPreference.getInstance(getApplicationContext()).setAudioFocusedVolume(volume);
            if (mFaraoPlayer != null)
                mFaraoPlayer.changeChannelVolume();
        }

        @Override
        public float sGetAudioFocusedVolume() throws RemoteException {
            return MCUserInfoPreference.getInstance(getApplicationContext()).getAudioFocusedVolume();
        }

        @Override
        public String sGetPreferenceString(int type) throws RemoteException {
            switch (type) {
                case MCUserInfoPreference.STRING_DATA_MAIL:
                    return MCUserInfoPreference.getInstance(getApplicationContext()).getEmail();
                case MCUserInfoPreference.STRING_DATA_PASS:
                    return MCUserInfoPreference.getInstance(getApplicationContext()).getPassword();
                default:
                    return null;
            }
        }

        @Override
        public int sGetInterruptIndex() throws RemoteException {
            return (mFaraoPlayer != null) ? mFaraoPlayer.getInterruptIndex() - 1 : 0;
        }

        @Override
        public int sGetInterruptPosition() throws RemoteException {
            return (mFaraoPlayer != null) ? mFaraoPlayer.getInterruptPosition() : 0;
        }
    };
    private String localPath;
    private ArrayList<File> localTrackList;
    private int localTrackIndex = 0;
    private String node = "0";

    /**
     * ログイン処理
     *
     * @param mail  : メールアドレス
     * @param pass  : パスワード
     * @param force : 強制ログインの有無
     */
    private boolean login(String mail, String pass, String force) {
        MCInit();
        mMCParam.setStringValue(MCDefParam.MCP_KIND_EMAIL, mail);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_PASSWORD, pass);

        String deviceToken = FROUtils.getDeviceId(getApplicationContext());

        mMCParam.setStringValue(MCDefParam.MCP_KIND_DEVICE_TOKEN, deviceToken);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_FORCE, force);
        MCPostActionParam copy = new MCPostActionParam();
        copy.copyParam(mMCParam);
        return apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_LOGIN, copy, new ApiRunnable(copy) {
            @Override
            public void run() {
                MCInit();
                ((MCPostActionParam) mMCParam).copyParam(params);
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_LOGIN, mMCParam);
            }
        }));
    }

    private void authStatus() {
        MCInit();
        MCPostActionParam copy = new MCPostActionParam();
        copy.copyParam(mMCParam);
        apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_STATUS, copy, null));
    }

    private Runnable loginAction;
    private Runnable recoveryAction;
    private EmergencyModeHelper emergencyModeHelper;
    private final EmergencyModeHelper.EmergencyCallback emergencyCallback = new EmergencyModeHelper.EmergencyCallback() {
        @Override
        public void onTimeout() {
            NotificationHelper.removeNotification(FROHandler.this);
            stopEmergency();
            notifyToForm(MCDefAction.MCA_KIND_LOGIN, Consts.NOTIFY_TIMEOUT_EMERGENCY_MODE, null);
        }

        @Override
        public void onRecovery(boolean isPlaying) {
            FRODebug.logD(getClass(), "onRecovery", true);
            if (isPlaying) {
                notifyToForm(MCDefAction.MCA_KIND_LOGIN, Consts.NOTIFY_RECOVERY_EMERGENCY_MODE, null);
            } else {
                NotificationHelper.removeNotification(FROHandler.this);
                if (emergencyModeHelper != null)
                    emergencyModeHelper.stopEmergencyMode();
                new Thread(loginAction).start();
                loginAction = null;
            }
        }

        @Override
        public void onNext() {
            notifyToForm(MCDefAction.MCA_KIND_LOGIN, Consts.NOTIFY_PLAY_NEXT_EMERGENCY, null);
        }
    };

    private void loginWithCheck(final String mail, final String pass, String force) {
        String cause = null;
        loginAction = new Runnable() {
            @Override
            public void run() {
                recoveryAction = new Runnable() {
                    @Override
                    public void run() {
                        if (sendNetworkRecovery(emergencyModeHelper.getBeginTime(), Utils.getNowTime(),
                                emergencyModeHelper.getCause()))
                            apiManager.doTask();
                    }
                };
                loginWithCheck(mail, pass, "yes");
            }
        };
        if ((cause = FROUtils.canAccess(getApplicationContext())) != null
                && !TextUtils.isEmpty(MCUserInfoPreference.getInstance(getApplicationContext()).getSessionKey())) {
            startEmergency(cause);
            notifyToForm(MCDefAction.MCA_KIND_LOGIN, Consts.NOTIFY_BEGIN_EMERGENCY_MODE, null);
            return;
        }
        if (login(mail, pass, force)) {
            apiManager.doTask();
        }
    }

    private void startEmergency(String cause) {
        NotificationHelper.showNotification(FROHandler.this, "Fans' Shop BGM", "emergency mode");
        if (emergencyModeHelper == null)
            emergencyModeHelper = new EmergencyModeHelper(getApplicationContext(), emergencyCallback, cause);
        emergencyModeHelper.startEmergencyMode();
    }

    private void stopEmergency() {
        if (emergencyModeHelper != null) {
            emergencyModeHelper.destroy();
            emergencyModeHelper = null;
        }
        loginAction = null;
        recoveryAction = null;
    }

    private boolean logout() {
        MCInit();
        MCPostActionParam copy = new MCPostActionParam();
        copy.copyParam(mMCParam);
        return apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_LOGOUT, copy, new ApiRunnable(copy) {
            @Override
            public void run() {
                MCInit();
                ((MCPostActionParam) mMCParam).copyParam(params);
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_LOGOUT, mMCParam);
            }
        }));
    }

    private boolean licenseStatus() {
        MCInit();
        MCPostActionParam copy = new MCPostActionParam();
        copy.copyParam(mMCParam);
        return apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_LICENSE_STATUS, copy, new ApiRunnable(copy) {
            @Override
            public void run() {
                MCInit();
                ((MCPostActionParam) mMCParam).copyParam(params);
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_LICENSE_STATUS, mMCParam);
            }
        }));
    }

    private boolean licenseTracking(final int type, double lat, double lon) {
        MCInit();
        String sLat = null;
        String sLon = null;
        if (lat != -1)
            sLat = Double.toString(lat);
        if (lon != -1)
            sLon = Double.toString(lon);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_LATITUDE, sLat);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_LONGITUDE, sLon);
        MCPostActionParam copy = new MCPostActionParam();
        copy.copyParam(mMCParam);
        return apiManager.setTask(new ApiTask(type, copy, new ApiRunnable(copy) {
            @Override
            public void run() {
                MCInit();
                ((MCPostActionParam) mMCParam).copyParam(params);
                mMCHandler.actDoAction(type, mMCParam);
            }
        }));
    }

    // 現在のプレイヤー状態から再生/停止のいずれかを行う
    // 状態に関わらず停止にしたい場合などは、
    // 事前にプレイヤーの状態を確認してこのメソッドを呼び出すかどうか判断して下さい
    private void pauseOrStartMusic() {
        if (mFaraoPlayer.isPlaying()) {
            counter.pause();
            // if (count90sec != null) {
            // count90sec.stop();
            // }
        } else {
            // if (count90sec != null) {
            // int past = counter.getCount();
            // count90sec.start((sampleTime - past) * 1000);
            // }
            if (counter != null)
                counter.start();
        }
        mFaraoPlayer.startOrPause();
    }

    private boolean getPlayList(final String mode, final int channel, final String range, final int size,
                                final int permission) {
        synchronized (MUSIC_TERM_LOCK) {
            boolean setTask = false;
            // playerの状態を確認し、play || pause であればsTermMusic(-1)と同様の動作を行う
            if (mFaraoPlayer != null) {
                if (mFaraoPlayer.isInterrupt()) {
                    mPendingChannelAction = new Runnable() {
                        @Override
                        public void run() {
                            if (getPlayList(mode, channel, range, size, permission)) {
                                apiManager.doTask();
                            }
                        }
                    };
                    return false;
                }
                // ローカル再生を行っていた場合
                if (mFaraoPlayer.getPlayerType() == FROPlayer.PLAYER_TYPE_LOCAL) {
                    // mFaraoPlayer.stop();
                    mFaraoPlayer.fadeOut(3000);
                    mFaraoPlayer.stop();
                    clearMusicData();
                }
                // サイマル再生を行っていた場合
                else if (mFaraoPlayer.getPlayerType() == FROPlayer.PLAYER_TYPE_SIMUL) {
                    // mFaraoPlayer.stop();
                    mFaraoPlayer.fadeOut(3000);
                    // 再生中の曲をrating/offlineする
                    extensionCallback.put(MCDefAction.MCA_KIND_STREAM_LOGGING, new Runnable() {
                        @Override
                        public void run() {
                            if (mFaraoPlayer != null) {
                                mFaraoPlayer.stop();
                            }
                            clearMusicData();
                            if (getPlayList(mode, channel, range, size, permission)) {
                                apiManager.doTask();
                            }
                        }
                    });
                    setTask = stopSimulTimer();
//                    clearMusicData();
                    return setTask;

                } else if (mFaraoPlayer.getPlayerType() == FROPlayer.PLAYER_TYPE_OFFLINE) {
                    clearMusicData();
                }
                // 通常の再生を行っていた場合
                else if (mFaraoPlayer.getPlayerType() == FROPlayer.PLAYER_TYPE_NOMAL) {
                    mFaraoPlayer.fadeOut(3000);
                    // 再生中の曲をrating/offlineする
                    extensionCallback.put(MCDefAction.MCA_KIND_RATING_OFFLINE, new Runnable() {
                        @Override
                        public void run() {
                            if (mFaraoPlayer != null) {
                                mFaraoPlayer.stop();
                            }
                            clearMusicData();
                            if (getPlayList(mode, channel, range, size, permission)) {
                                apiManager.doTask();
                            }
                        }
                    });
                    setTask = sendRatingNoResponse();
                    return setTask;
                }
            }

            skipControl = false;
            // 特集リストの場合
            if (mode.equals(ChannelMode.BENIFIT.text)) {
                // プレイリスト毎にスキップと音声広告の制御をチェックする
                if (benifitList != null && benifitList.find(channel) != null) {
                    IMCItem item = benifitList.find(channel);
                    String skip = item.getString(MCDefResult.MCR_KIND_BENIFITITEM_SKIP_CONTROL);
                    // スキップ制御（0=制限なし、1=制限あり=スキップ不可）
                    if (skip.equals("1"))
                        skipControl = true;
                    else
                        skipControl = false;
                } else {
                    jp.faraopro.play.domain.ChannelInfo info = FROFavoriteChannelDBFactory
                            .getInstance(getApplicationContext()).find(mode, Integer.toString(channel));
                    skipControl = (info != null && info.getSkipControl() == Consts.SKIP_CONTROL_DISABLE) ? true : false;
                }
            }
            // TODO 情報格納用のクラスを作成する
            notifyToForm(Consts.NOTIFY_PLAYLIST_INFO, Consts.STATUS_OK, Boolean.toString(skipControl));
            // 初回は起動時の進捗率を通知する
            isNotifyProgress = true;

            MCInit();
            MCPostActionParam copy = new MCPostActionParam();
            // copy.copyParam(mMCParam);

            setTask = apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_LISTEN, copy, new ApiRunnable(copy) {
                @Override
                public void run() {
                    MCInit();
                    setModeParams(mode, channel, range, permission);
                    mMCParam.setStringValue(MCDefParam.MCP_KIND_MODE_NO, mode);
                    mMCParam.setStringValue(MCDefParam.MCP_KIND_MYCHANNELID, Integer.toString(channel));
                    mMCParam.setStringValue(MCDefParam.MCP_KIND_RANGE, range);
                    mMCHandler.actDoAction(MCDefAction.MCA_KIND_LISTEN, mMCParam);
                    setWaittingForDL(true);
                }
            }));

            return setTask;
        }
    }

    private boolean getStreamPlaylist(final String streamId) {
        synchronized (MUSIC_TERM_LOCK) {
        boolean setTask = false;
        // playerの状態を確認し、play || pause であればsTermMusic(-1)と同様の動作を行う
        if (mFaraoPlayer != null) {
                if (mFaraoPlayer.isInterrupt()) {
                    mPendingChannelAction = new Runnable() {
                        @Override
                        public void run() {
                            if (getStreamPlaylist(streamId)) {
                                apiManager.doTask();
                            }
                        }
                    };
                    return false;
                }
            // ローカル再生を行っていた場合
            if (mFaraoPlayer.getPlayerType() == FROPlayer.PLAYER_TYPE_LOCAL) {
                mFaraoPlayer.fadeOut(3000);
                mFaraoPlayer.stop();
                clearMusicData();
            }
            // サイマル再生を行っていた場合
            else if (mFaraoPlayer.getPlayerType() == FROPlayer.PLAYER_TYPE_SIMUL) {
                // mFaraoPlayer.stop();
                mFaraoPlayer.fadeOut(3000);
                // 再生中の曲をrating/offlineする
                extensionCallback.put(new Integer(MCDefAction.MCA_KIND_STREAM_LOGGING), new Runnable() {
                    @Override
                    public void run() {
                        clearMusicData();
                        if (getStreamPlaylist(streamId))
                            apiManager.doTask();
                    }
                });
                setTask = stopSimulTimer();
                return setTask;
            }
            // 通常の再生を行っていた場合
            else if (mFaraoPlayer.getPlayerType() == FROPlayer.PLAYER_TYPE_NOMAL) {
                // 再生中の曲をrating/offlineする
                setTask = sendRatingNoResponse();
                mFaraoPlayer.fadeOut(3000);
                extensionCallback.put(new Integer(MCDefAction.MCA_KIND_RATING_OFFLINE), new Runnable() {
                    @Override
                    public void run() {
                        // faraoPlayer.stop();
                        clearMusicData();
                        if (getStreamPlaylist(streamId))
                            apiManager.doTask();
                    }
                });
                return setTask;
            }
        }

        skipControl = false;
        // 初回は起動時の進捗率を通知する
        isNotifyProgress = true;
        ChannelInfo.setParams(streamId);

        MCInit();
        mMCParam.setStringValue(MCDefParam.MCP_KIND_STREAM_ID, streamId);

        MCPostActionParam copy = new MCPostActionParam();
        copy.copyParam(mMCParam);
        setTask = apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_STREAM_PLAY, copy, new ApiRunnable(copy) {
            @Override
            public void run() {
                MCInit();
                ((MCPostActionParam) mMCParam).copyParam(params);
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_STREAM_PLAY, mMCParam);
            }
        }));

        return setTask;
    }
    }

    private void playLocal(final String path) {
        synchronized (MUSIC_TERM_LOCK) {
        localTrackIndex = 0;
        localTrackList = null;
        localPath = null;

        // playerの状態を確認し、play || pause であればsTermMusic(-1)と同様の動作を行う
        if (mFaraoPlayer != null) {
                if (mFaraoPlayer.isInterrupt()) {
                    mPendingChannelAction = new Runnable() {
                        @Override
                        public void run() {
                            playLocal(path);
                        }
                    };
                    return;
                }
            // ローカル再生を行っていた場合
            if (mFaraoPlayer.getPlayerType() == FROPlayer.PLAYER_TYPE_LOCAL) {
                mFaraoPlayer.fadeOut(3000);
                mFaraoPlayer.stop();
                clearMusicData();
            }
            // サイマル再生を行っていた場合
            else if (mFaraoPlayer.getPlayerType() == FROPlayer.PLAYER_TYPE_SIMUL) {
                // mFaraoPlayer.stop();
                mFaraoPlayer.fadeOut(3000);
                // 再生中の曲をrating/offlineする
                extensionCallback.put(new Integer(MCDefAction.MCA_KIND_STREAM_LOGGING), new Runnable() {
                    @Override
                    public void run() {
                        clearMusicData();
                        playLocal(path);
                    }
                });
                if (stopSimulTimer())
                    apiManager.doTask();
                return;
            }
            // 通常の再生を行っていた場合
            else if (mFaraoPlayer.getPlayerType() == FROPlayer.PLAYER_TYPE_NOMAL) {
                mFaraoPlayer.fadeOut(3000);
                // 再生中の曲をrating/offlineする
                if (sendRatingNoResponse())
                    apiManager.doTask();
                extensionCallback.put(new Integer(MCDefAction.MCA_KIND_RATING_OFFLINE), new Runnable() {
                    @Override
                    public void run() {
                        mFaraoPlayer.stop();
                        clearMusicData();
                        playLocal(path);
                    }
                });
                return;
            }
        }

        skipControl = true;
        localPath = path;

        localTrackList = new ArrayList<File>();
        File targetDir = new File(localPath);
        if (targetDir != null && targetDir.exists()) {
            File[] files = targetDir.listFiles(Utils.getMusicFileFilter());
            localTrackList = new ArrayList<File>(Arrays.asList(files));
        } else {
            notifyToForm(Consts.START_LOCAL_MUSIC, Consts.STATUS_NO_DATA, null);
            return;
        }
        if (localTrackList.size() < 1) {
            notifyToForm(Consts.START_LOCAL_MUSIC, Consts.STATUS_NO_DATA, null);
            return;
        }

        mFaraoPlayer = new FROPlayer(getApplicationContext());
        mFaraoPlayer.attach(this);
        try {
            mFaraoPlayer.setLocalMedia(localTrackList.get(localTrackIndex).getAbsolutePath(), false,
                    FROPlayer.PLAYER_TYPE_LOCAL);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (!isTalking) {
            // 再生開始
            mFaraoPlayer.startOrPause();
        }

        NotificationHelper.showNotification(this, "Fans' Shop BGM", getString(R.string.cap_local_play));

        MusicInfo info = new MusicInfo();
        mFaraoPlayer.getMusicData(info);
        info.setPlayerType(FROPlayer.PLAYER_TYPE_LOCAL);
        info.setId(Integer.toString(localTrackIndex));
        info.setUrl(localPath);
        info.setUrlSearch(localTrackList.get(localTrackIndex).getAbsolutePath());
        notifyToForm(Consts.START_LOCAL_MUSIC, Consts.STATUS_OK, info);
        }
    }

    public void skipLocal(int direction) {
        synchronized (MUSIC_TERM_LOCK) {
            if (mFaraoPlayer != null && mFaraoPlayer.isInterrupt()) {
                if (mMusicTimerRunnable == null) {
                    final int fDirection = direction;
                    mMusicTimerRunnable = new Runnable() {
                        @Override
                        public void run() {
                            NotificationHelper.showNotification(FROHandler.this, "Fans' Shop BGM", getString(R.string.cap_local_play));
                            skipLocal(fDirection);
                        }
                    };
                }
                return;
            }

            // 次の曲
            if (direction == Consts.LOCAL_PLAYER_NEXT)
                localTrackIndex++;
                // 前の曲
            else if (direction == Consts.LOCAL_PLAYER_PREVIOUS)
                localTrackIndex--;
            // プレイリストが一周した
            if (localTrackIndex >= localTrackList.size()) {
                localTrackIndex = 0;
                localTrackList = new ArrayList<File>();
                File targetDir = new File(localPath);
                if (targetDir != null && targetDir.exists()) {
                    File[] files = targetDir.listFiles(Utils.getMusicFileFilter());
                    localTrackList = new ArrayList<File>(Arrays.asList(files));
                }
                if (localTrackList.size() < 1) {
                    notifyToForm(Consts.START_LOCAL_MUSIC, Consts.STATUS_NO_DATA, null);
                    return;
                }
            }
            if (localTrackIndex < 0) {
                localTrackIndex = localTrackList.size() - 1;
            }

            if (mFaraoPlayer != null) {
                mFaraoPlayer.stop();
                try {
                    mFaraoPlayer.setLocalMedia(localTrackList.get(localTrackIndex).getAbsolutePath(), false,
                            FROPlayer.PLAYER_TYPE_LOCAL);
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (!isTalking)
                    mFaraoPlayer.startOrPause();

                MusicInfo info = new MusicInfo();
                mFaraoPlayer.getMusicData(info);
                info.setPlayerType(FROPlayer.PLAYER_TYPE_LOCAL);
                info.setId(Integer.toString(localTrackIndex));
                info.setUrl(localPath);
                info.setUrlSearch(localTrackList.get(localTrackIndex).getAbsolutePath());
                notifyToForm(Consts.START_LOCAL_MUSIC, Consts.STATUS_OK, info);
            }
        }
    }

    private boolean doPlaying = false; // 暫定対応

    // 再生中の楽曲のレーティングを行い、次の曲を再生を開始する
    // シーケンスは
    // 再生中の楽曲情報の一時保存 -> 次の楽曲の再生開始 -> レーティング API の呼び出し
    // となっており、必ず次の楽曲が再生され始めてからレーティングを行う
    // 他の API が実行中で API を呼び出せなかった場合 false を返す
    private boolean ratingAndPlay(String complete) {
        boolean setTask = false;

        String playTime = Integer.toString(this.playTime * 1000);
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        // レーティングデータの保存
        final RatingInfo ratingInfo = new RatingInfo(UserDataPreference.getTrackingKey(getApplicationContext()),
                ChannelInfo.mode, ChannelInfo.channelId, ChannelInfo.range, nowPlayingId, decision, complete, playTime,
                timeStamp, null);
        saveUnsentRating(ratingInfo);
        // 楽曲情報の更新(再生中 -> 再生済み)
        if (!TextUtils.isEmpty(nowPlayingId))
            mMCHandler.mdbSetStatus(nowPlayingId, IMCMusicItemInfo.MCDB_STATUS_PLAYED);

        // 履歴データの保存
        if (!TextUtils.isEmpty(ChannelInfo.mode)) {
            MusicInfoEx infoEx = new MusicInfoEx();
            infoEx.setId(Integer.parseInt(nowPlayingId));
            infoEx.setValue(decision);
            infoEx.setMusicInfo(infoJp);
            infoEx.setTimestamp(System.currentTimeMillis() / 1000);
            String params = ChannelInfo.mode + "," + ChannelInfo.channelId;
            if (!TextUtils.isEmpty(ChannelInfo.range))
                params = params + "," + ChannelInfo.range;
            infoEx.setParams(params);
            saveMusicHistory(infoEx);
        }

        /********** 再生中の楽曲情報の一時保存 ここまで **********/

        // 再生が完了した楽曲の情報は、#playMusic 呼び出し後に消えてしまう(メンバ変数を次の再生楽曲のものに更新する)ため、
        // 残しておきたい情報がある場合は、これ以前に終わらせておくこと
        if (!isTerminating) {
            decision = MCDefParam.MCP_PARAM_STR_RATING_NOP;
            // 終了フラグが立っていなければ、次の楽曲を再生する
            playMusic();
        }

        /********** 次の楽曲の再生開始 ここまで **********/

        if (Utils.getNetworkState(getApplicationContext())) {
            // ネットワーク状態が正常であればレーティングを行う
            FROUnsentRatingDB unsentDb = (FROUnsentRatingDB) FROUnsentRatingDBFactory
                    .getInstance(getApplicationContext());
            if (unsentDb.getSize() > 0) {
                // 未送信評価データがある場合、DBからデータを取得し送信する
                setTask = sendRatingFormDb();
            }
        }

        /********** レーティング API の呼び出し ここまで **********/

        return setTask;
    }

    // TODO 暫定、動作に問題なければ上のメソッドと統合する
    private boolean errorRatingAndPlay(int when, int statusCode, IMCItem trackData) {
        boolean setTask = false;

        String playTime = "0";
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String userAction = "error";
        String complete = "no";
        String errorReason = MCError.getErrorReason(when, statusCode);
        String trackId = trackData.getString(MCDefResult.MCR_KIND_TRACKITEM_ID);
        // レーティングデータの保存
        final RatingInfo ratingInfo = new RatingInfo(UserDataPreference.getTrackingKey(getApplicationContext()),
                ChannelInfo.mode, ChannelInfo.channelId, ChannelInfo.range, trackId, userAction, complete, playTime,
                timeStamp, errorReason);
        saveUnsentRating(ratingInfo);
        // 楽曲情報の更新(再生中 -> 再生済み)
        if (!TextUtils.isEmpty(nowPlayingId))
            mMCHandler.mdbSetStatus(nowPlayingId, IMCMusicItemInfo.MCDB_STATUS_PLAYED);

        /********** 再生中の楽曲情報の一時保存 ここまで **********/

        // 再生が完了した楽曲の情報は、#playMusic 呼び出し後に消えてしまう(メンバ変数を次の再生楽曲のものに更新する)ため、
        // 残しておきたい情報がある場合は、これ以前に終わらせておくこと
        if (!isTerminating && isWaittingForDL) {
            decision = MCDefParam.MCP_PARAM_STR_RATING_NOP;
            playMusic();
        }

        /********** 次の楽曲の再生開始 ここまで **********/

        if (Utils.getNetworkState(getApplicationContext())) {
            // ネットワーク状態が正常であればレーティングを行う
            FROUnsentRatingDB unsentDb = (FROUnsentRatingDB) FROUnsentRatingDBFactory
                    .getInstance(getApplicationContext());
            if (unsentDb.getSize() > 0) {
                // 未送信評価データがある場合、DBからデータを取得し送信する
                setTask = sendRatingFormDb();
            }
        }

        /********** レーティング API の呼び出し ここまで **********/

        return setTask;
    }

    // 楽曲のレーティングを行う
    // レーティング対象は未送信DBに保存されている楽曲のうち、最も日付が古いもの
    // このメソッドは通常の/radio/ratingのAPI呼び出しであるため、
    // 返りのXMLには次にDLする楽曲の情報が含まれます
    // 単純に評価のみを送信したい場合は下記のsendRatingNoResponseを使用して下さい
    private boolean sendRatingFormDb(boolean force) {
        FROUnsentRatingDB unsentDb = (FROUnsentRatingDB) FROUnsentRatingDBFactory.getInstance(getApplicationContext());
        if (force || !apiManager.isContained(MCDefAction.MCA_KIND_RATING)) {
            FRODebug.logD(getClass(), "SET MCA_KIND_RATING FROM UNSETDB", DEBUG);

            // 未送信評価データがある場合
            // DBからデータを取得し、それを送信する
            final RatingInfo info = unsentDb.findOldest(false);
            MCInit();
            mMCParam.setStringValue(MCDefParam.MCP_KIND_MODE_NO, info.getMode());
            mMCParam.setStringValue(MCDefParam.MCP_KIND_MYCHANNELID, info.getChannelId());
            mMCParam.setStringValue(MCDefParam.MCP_KIND_RANGE, info.getRange());
            mMCParam.setStringValue(MCDefParam.MCP_KIND_RATING_TRACKID, info.getTrackId());
            mMCParam.setStringValue(MCDefParam.MCP_KIND_RATE_ACTION, info.getDecision());
            mMCParam.setStringValue(MCDefParam.MCP_KIND_PLAY_COMPLETE, info.getComplete());
            mMCParam.setStringValue(MCDefParam.MCP_KIND_PLAY_DURATIN, info.getDuration());
            mMCParam.setStringValue(MCDefParam.MCP_KIND_ERROR_REASON, info.getErrorReason());

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            return apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_RATING, copy, new ApiRunnable(copy) {
                @Override
                public void run() {
                    MCInit();
                    ((MCPostActionParam) mMCParam).copyParam(params);
                    mMCHandler.actDoAction(MCDefAction.MCA_KIND_RATING, mMCParam);
                    setRating(true);
                    lastRatingInfo = info;
                }
            }));
        } else {
            return false;
        }
    }

    private boolean sendRatingFormDb() {
        return sendRatingFormDb(false);
    }

    // 楽曲のレーティングを行う
    // 現在再生している楽曲に対してレーティングを行います
    // sendRatingと違い/radio/rating_offlineを呼び出すため、
    // 返りのXMLに次の曲の情報は含まれません
    // プレイヤー終了時や、エラー発生時などに使用して下さい
    // これもsendRating同様、再生中の楽曲に対して1度だけ呼び出されるように使用して下さい
    private boolean sendRatingNoResponse() {
        boolean setTask = false;

        if (counter != null)
            playTime = counter.getCount();

        String pTime = Integer.toString(playTime * 1000);
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);

        // レーティングデータの保存
        final RatingInfo ratingInfo = new RatingInfo(UserDataPreference.getTrackingKey(getApplicationContext()),
                ChannelInfo.mode, ChannelInfo.channelId, ChannelInfo.range, nowPlayingId, decision, "no", pTime,
                timeStamp, null);
        saveUnsentRating(ratingInfo);

        MCInit();
        mMCParam.setStringValue(MCDefParam.MCP_KIND_TRACKING_KEY,
                UserDataPreference.getTrackingKey(getApplicationContext()));
        mMCParam.setStringValue(MCDefParam.MCP_KIND_MODE_NO, ChannelInfo.mode);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_MYCHANNELID, ChannelInfo.channelId);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_RANGE, ChannelInfo.range);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_RATING_TRACKID, nowPlayingId);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_RATE_ACTION, decision);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_PLAY_COMPLETE, "no");
        mMCParam.setStringValue(MCDefParam.MCP_KIND_PLAY_DURATIN, Integer.toString(playTime * 1000));

        MCPostActionParam copy = new MCPostActionParam();
        copy.copyParam(mMCParam);

        setTask = apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_RATING_OFFLINE, copy, new ApiRunnable(copy) {
            @Override
            public void run() {
                MCInit();
                ((MCPostActionParam) mMCParam).copyParam(params);
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_RATING_OFFLINE, mMCParam);
                lastRatingInfo = ratingInfo;
            }
        }));
        // 履歴データの保存
        if (!TextUtils.isEmpty(ChannelInfo.mode)) {
            MusicInfoEx infoEx = new MusicInfoEx();
            infoEx.setId(Integer.parseInt(nowPlayingId));
            infoEx.setValue(decision);
            infoEx.setMusicInfo(infoJp);
            infoEx.setTimestamp(System.currentTimeMillis() / 1000);
            String params = ChannelInfo.mode + "," + ChannelInfo.channelId;
            if (!TextUtils.isEmpty(ChannelInfo.range))
                params = params + "," + ChannelInfo.range;
            infoEx.setParams(params);
            saveMusicHistory(infoEx);
        }
        decision = MCDefParam.MCP_PARAM_STR_RATING_NOP;

        return setTask;
    }

    // 楽曲のレーティングを行う
    // 未送信DBに保存されている全楽曲に対してレーティングを行います
    // /radio/rating_offlineを呼び出すため、返りのXMLに次の曲の情報は含まれません
    private boolean resendRatingNoResponse() {
        boolean setTask = false;
        // 再送フラグを立てる、これが立っていない場合は再送以外の用途でRATING_OFFLINEが呼び出されている場合であり
        // RATING_OFFLINEのコールバックの動作が変わってくる
        resendFlag = true;
        // 要素数を数え、0以下の場合は再送処理を抜ける
        if (resendList.size() <= 0) {
            notifyToForm(Consts.CHECK_UNSENT_DATA, Consts.STATUS_OK, null);
            return setTask;
        }
        // リストの先頭のレーティング内容を取得し
        RatingInfo info = resendList.get(0);
        MCInit();
        // APIを呼び出す
        mMCParam.setStringValue(MCDefParam.MCP_KIND_TRACKING_KEY, info.getTrackingKey());
        mMCParam.setStringValue(MCDefParam.MCP_KIND_MODE_NO, info.getMode());
        mMCParam.setStringValue(MCDefParam.MCP_KIND_MYCHANNELID, info.getChannelId());
        mMCParam.setStringValue(MCDefParam.MCP_KIND_RANGE, info.getRange());
        mMCParam.setStringValue(MCDefParam.MCP_KIND_RATING_TRACKID, info.getTrackId());
        mMCParam.setStringValue(MCDefParam.MCP_KIND_RATE_ACTION, info.getDecision());
        mMCParam.setStringValue(MCDefParam.MCP_KIND_PLAY_COMPLETE, info.getComplete());
        mMCParam.setStringValue(MCDefParam.MCP_KIND_PLAY_DURATIN, info.getDuration());

        MCPostActionParam copy = new MCPostActionParam();
        copy.copyParam(mMCParam);
        setTask = apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_RATING_OFFLINE, copy, new ApiRunnable(copy) {
            @Override
            public void run() {
                MCInit();
                ((MCPostActionParam) mMCParam).copyParam(params);
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_RATING_OFFLINE, mMCParam);
            }
        }));

        return setTask;
    }

    private boolean sendNetworkRecovery() {
        if (mStateCheckThread == null)
            return true;

        boolean setTask = false;

        String begin = Long.toString(mStateCheckThread.getBeginTime());
        String end = Long.toString(mStateCheckThread.getEndTime());
        MCInit();
        // APIを呼び出す
        mMCParam.setStringValue(MCDefParam.MCP_KIND_TRACKING_KEY,
                UserDataPreference.getTrackingKey(getApplicationContext()));
        mMCParam.setStringValue(MCDefParam.MCP_KIND_DOWNTIME_BEGIN, begin);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_DOWNTIME_END, end);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_OFFLINE_COUSE, mStateCheckThread.getCause());

        MCPostActionParam copy = new MCPostActionParam();
        copy.copyParam(mMCParam);
        setTask = apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_NETWORK_RECOVERY, copy, new ApiRunnable(copy) {
            @Override
            public void run() {
                MCInit();
                ((MCPostActionParam) mMCParam).copyParam(params);
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_NETWORK_RECOVERY, mMCParam);
            }
        }));

        return setTask;
    }

    private boolean sendNetworkRecovery(long beginTime, long endTime, String cause) {
        boolean setTask = false;
        String begin = Long.toString(beginTime);
        String end = Long.toString(endTime);
        MCInit();
        // APIを呼び出す
        mMCParam.setStringValue(MCDefParam.MCP_KIND_TRACKING_KEY,
                UserDataPreference.getTrackingKey(getApplicationContext()));
        mMCParam.setStringValue(MCDefParam.MCP_KIND_DOWNTIME_BEGIN, begin);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_DOWNTIME_END, end);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_OFFLINE_COUSE, cause);
        MCPostActionParam copy = new MCPostActionParam();
        copy.copyParam(mMCParam);
        setTask = apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_NETWORK_RECOVERY, copy, new ApiRunnable(copy) {
            @Override
            public void run() {
                MCInit();
                ((MCPostActionParam) mMCParam).copyParam(params);
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_NETWORK_RECOVERY, mMCParam);
            }
        }));
        return setTask;
    }

    private synchronized void saveUnsentRating(RatingInfo info) {
        FROUnsentRatingDB unsentDb = (FROUnsentRatingDB) FROUnsentRatingDBFactory.getInstance(getApplicationContext());
        unsentDb.insert(info);
    }

    private synchronized void deleteUnsentRating(RatingInfo info) {
        FROUnsentRatingDB unsentDb = (FROUnsentRatingDB) FROUnsentRatingDBFactory.getInstance(getApplicationContext());
        unsentDb.delete(Integer.parseInt(info.getTrackId()));
    }

    private synchronized void saveMusicHistory(MusicInfoEx info) {
        FROMusicHistoryDB historyDb = (FROMusicHistoryDB) FROMusicHistoryDBFactory.getInstance(getApplicationContext());
        FROGoodHistoryDB goodHistoryDb = (FROGoodHistoryDB) FROGoodHistoryDBFactory
                .getInstance(getApplicationContext());
        boolean isGood = MCDefParam.MCP_PARAM_STR_RATING_GOOD.equalsIgnoreCase(info.getValue()) ? true : false;
        String path = null;
        // 履歴の上限に達していた場合
        if (historyDb.getSize() >= HISTORY_LIMIT) {
            MusicInfoEx oldest = historyDb.findOldest(false);
            if (oldest != null) {
                historyDb.delete(oldest.getId());
                // Good リストでも使用していないサムネイルであれば削除する
                if (goodHistoryDb.find(oldest.getId()) == null) {
                    path = Consts.PRIVATE_PATH_THUMB_HISTORY + oldest.getId();
                    Utils.deleteBitmap(path, getApplicationContext());
                }
            }
        }
        // Good リストの上限に達していた場合
        if (isGood && goodHistoryDb.getSize() >= HISTORY_LIMIT) {
            MusicInfoEx oldest = goodHistoryDb.findOldest(false);
            if (oldest != null) {
                goodHistoryDb.delete(oldest.getId());
                // 履歴リストでも使用していないサムネイルであれば削除する
                if (historyDb.find(oldest.getId()) == null) {
                    path = Consts.PRIVATE_PATH_THUMB_HISTORY + oldest.getId();
                    Utils.deleteBitmap(path, getApplicationContext());
                }
            }
        }

        historyDb.insert(info);
        if (isGood)
            goodHistoryDb.insert(info);
        path = FROUtils.getJacketPath() + info.getMusicInfo().getArtwork().replace(".jpg", "");
        File file = new File(path);
        if (file.isFile() && path != null) {
            // outOfMemoryが出る可能性があるので削除
            // Bitmap thumb = BitmapFactory.decodeFile(path);
            Bitmap thumb = Utils.loadBitmap(path);
            if (thumb != null) {
                String fileName = Consts.PRIVATE_PATH_THUMB_HISTORY + info.getId();
                Utils.saveBitmap(thumb, fileName, getApplicationContext());
            }
        }
    }

    private void termMusic(int type) {
        if (mFaraoPlayer != null) {
            mFaraoPlayer.stop();
            // 再生している曲の種類によって動作を変える
            switch (mFaraoPlayer.getPlayerType()) {
                // 通常再生
                case FROPlayer.PLAYER_TYPE_NOMAL:
                    if (counter != null) {
                        playTime = counter.getCount();
                        counter.reset();
                    }
                    // if (count90sec != null)
                    // count90sec.stop();
                    // 終了方法が3つに分岐
                    switch (type) {
                        // rating_offlineで評価
                        case Consts.TERM_TYPE_RATING_NORESPONSE:
                            if (sendRatingNoResponse()) {
                                apiManager.doTask();
                            }
                            break;
                        // 評価せずに終了
                        case Consts.TERM_TYPE_NO_RATING:
                            clearMusicData(true);
                            notifyToForm(Consts.MUSIC_TERMINATION, Consts.STATUS_OK, null);
                            break;
                        case Consts.TERM_TYPE_FORCE:
                            apiManager.isTerminate = true;
                            String pTime = Integer.toString(playTime * 1000);
                            String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
                            RatingInfo ratingInfo = new RatingInfo(UserDataPreference.getTrackingKey(getApplicationContext()),
                                    ChannelInfo.mode, ChannelInfo.channelId, ChannelInfo.range, nowPlayingId, decision, "no",
                                    pTime, timeStamp, null);
                            saveUnsentRating(ratingInfo);
                            clearMusicData(true);
                            notifyToForm(Consts.MUSIC_TERMINATION, Consts.STATUS_OK, null);
                            break;
                        // ratingで評価
                        default:
                            isTerminating = true; // 立てると評価だけ行い以降の処理を全て放棄
                            if (ratingAndPlay("no")) {
                                apiManager.doTask();
                            }
                            break;
                    }
                    break;
                // ローカル再生
                case FROPlayer.PLAYER_TYPE_LOCAL:
                    clearMusicData(true);
                    notifyToForm(Consts.MUSIC_TERMINATION, Consts.STATUS_OK, null);
                    break;
                // サイマル放送
                case FROPlayer.PLAYER_TYPE_SIMUL:
                    if (stopSimulTimer())
                        apiManager.doTask();
                    clearMusicData(true);
                    notifyToForm(Consts.MUSIC_TERMINATION, Consts.STATUS_OK, null);
                    break;
                // オフラインプレイヤー
                case FROPlayer.PLAYER_TYPE_OFFLINE:
                    clearMusicData(true);
                    if (mStateCheckThread != null) {
                        mStateCheckThread.release();
                        mStateCheckThread = null;
                        offlineTrackIndex = 0;
                        offlineTracklist = null;
                    }
                    notifyToForm(Consts.MUSIC_TERMINATION, Consts.STATUS_OK, null);
                    break;
                default:
                    clearMusicData(true);
                    notifyToForm(Consts.MUSIC_TERMINATION, Consts.STATUS_OK, null);
                    break;
            }
        } else {
            clearMusicData(true);
            notifyToForm(Consts.MUSIC_TERMINATION, Consts.STATUS_OK, null);
        }
    }

    /**
     * CDNデータ取得
     */
    private void getCdnMusicInfo(boolean isPeek) {
        MCTrackItem item = (mTrackDownloadList != null && mTrackDownloadList.size() > 0) ? mTrackDownloadList.get(0)
                : null;
        setDownloading(true);
        MCInit();
        if (item != null) {
            String trackId = item.getString(MCDefResult.MCR_KIND_TRACKITEM_ID);
            String jacketId = item.getString(MCDefResult.MCR_KIND_TRACKITEM_JACKET_ID);
            mMCHandler.mdbSetStatus(trackId, IMCMusicItemInfo.MCDB_STATUS_DOWNLOADING);
            mMCParam.setStringValue(MCDefParam.MCP_KIND_TRACKID, trackId);
            mMCParam.setStringValue(MCDefParam.MCP_KIND_JACKETID, jacketId);
            // 音質の設定、現状はlow固定
            mMCParam.setStringValue(MCDefParam.MCP_KIND_QUARITY, "high");

            MCPostActionParam copy = new MCPostActionParam();
            copy.copyParam(mMCParam);
            if (isPeek) {
                apiManager.setPeekTask(new ApiTask(MCDefAction.MCA_KIND_DOWNLOAD_CDN, copy, null));
            } else {
                apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_DOWNLOAD_CDN, copy, null));
            }
        } else {
            onNotifyMCError(MCDefAction.MCA_KIND_DOWNLOAD_CDN, MCError.MC_NOT_FOUND);
        }
    }

    private void getCdnMusicInfo() {
        getCdnMusicInfo(false);
    }

    private void getCdnJacketInfo() {
        MCInit();
        mMCParam.setStringValue(MCDefParam.MCP_KIND_TRACKID, null);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_JACKETID, hlsJacketUrl.get(0));
        // 音質の設定、現状はlow固定
        mMCParam.setStringValue(MCDefParam.MCP_KIND_QUARITY, "high");
        MCPostActionParam copy = new MCPostActionParam();
        copy.copyParam(mMCParam);
        apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_DOWNLOAD_CDN, copy, null));
    }

    /**
     * 音楽ファイル取得
     */
    private boolean getMusicFile(boolean isPeek) {
        String trackId = (mTrackDownloadList != null && mTrackDownloadList.size() > 0)
                ? mTrackDownloadList.get(0).getString(MCDefResult.MCR_KIND_TRACKITEM_ID) : "";
        String url = cdnInfo.getString(MCDefResult.MCR_KIND_ITEM_CDN_TRACK_URL);

        // URL の形式チェック
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            return false;
        }

        MCInit();
        mMCHandler.mdbSetStatus(trackId, IMCMusicItemInfo.MCDB_STATUS_DOWNLOADING);
        MCCdnMusicItem copy = new MCCdnMusicItem();
        copy = cdnInfo.copy();
        if (isPeek) {
            apiManager.setPeekTask(new ApiTask(MCDefAction.MCA_KIND_TRACK_DL, copy, null));
        } else {
            apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_TRACK_DL, copy, null));
        }

        return true;
    }

    /**
     * 画像ファイル取得
     */
    private boolean getPictureFile(boolean isPeek) {
        boolean callApi = true;

        MCInit();
        String url = cdnInfo.getString(MCDefResult.MCR_KIND_ITEM_CDN_JACKET_URL);
        MCCdnMusicItem copy = new MCCdnMusicItem();
        copy = cdnInfo.copy();
        if (!TextUtils.isEmpty(url)) {
            if (isPeek) {
                apiManager.setPeekTask(new ApiTask(MCDefAction.MCA_KIND_ARTWORK_DL, copy, new ApiRunnable(copy) {
                    @Override
                    public void run() {
                        mMCHandler.actDoAction(MCDefAction.MCA_KIND_ARTWORK_DL, params);
                    }
                }));
            } else {
                apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_ARTWORK_DL, copy, new ApiRunnable(copy) {
                    @Override
                    public void run() {
                        mMCHandler.actDoAction(MCDefAction.MCA_KIND_ARTWORK_DL, params);
                    }
                }));
            }
        } else {
            callApi = false;
        }

        return callApi;
    }

    private void getPictureFile(String url, String id) {
        MCInit();
        if (!TextUtils.isEmpty(url)) {
            MCCdnMusicItem item = new MCCdnMusicItem();
            item.setString(MCDefResult.MCR_KIND_ITEM_CDN_JACKET_URL, url);
            item.setString(MCDefResult.MCR_KIND_TRACKITEM_JACKET_ID, id);

            MCCdnMusicItem copy = new MCCdnMusicItem();
            copy = item.copy();
            apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_ARTWORK_DL, copy, new ApiRunnable(copy) {
                @Override
                public void run() {
                    mMCHandler.actDoAction(MCDefAction.MCA_KIND_ARTWORK_DL, params);
                }
            }));
        } else {
            if (hlsJacketUrl != null && hlsJacketUrl.size() > 0) {
                getCdnJacketInfo();
            } else {
                // 拡張コールバックがあるか確認する
                Runnable callback = extensionCallback.get(new Integer(MCDefAction.MCA_KIND_ARTWORK_DL));
                hlsJacketUrl = null;
                if (callback != null)// 有ればコールバックを実行
                    callback.run();
            }
        }
    }

    // 楽曲を再生する
    private void playMusic() {
        try {
            //FaRaoVoice再生中かPrefarenceを参照
            Thread.sleep(500);
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            pref = sp.getBoolean("voice",false);
            Log.d("pref", String.valueOf(pref));
            //FaRaoVoice再生中である場合、FaRaoVoiceに通知
            if(pref = true) {
                Intent pro = new Intent();
                pro.setAction("jp.fans_shop_voice.ProReceiver");
                pro.putExtra("pro", "start");
                sendBroadcast(pro);
            }
            //Prefarenceを初期化
            sp.edit().putBoolean("voice",false).commit();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (MUSIC_TERM_LOCK) {
            if (mFaraoPlayer != null && mFaraoPlayer.isInterrupt()) {
                if (mMusicTimerRunnable == null) {
                    mMusicTimerRunnable = new Runnable() {
                        @Override
                        public void run() {
                            playMusic();
                        }
                    };
                }
                return;
            }

            // プレミアムユーザである、音声広告に制限がかかっている、
            // 再生回数が広告出現頻度より小さい、車載器連携中、のいずれかの場合
            if (UserDataPreference.isPremium(getApplicationContext())) {
                IMCMusicItemInfo nextMusicInfo = null;
                IMCItem trackData = null;

                // DL済みの楽曲データをDBから取得
                // DLが完了していない場合はフラグを立てて抜ける
                nextMusicInfo = mMCHandler.mdbGet2(String.valueOf(IMCMusicItemInfo.MCDB_STATUS_COMPLETE));
                if (nextMusicInfo == null) {
                    if (Utils.getNetworkState(getApplicationContext())) {
                        setWaittingForDL(true);
                    } else {
                        setOfflineMode(NetworkStateChecker.ERROR_CAUSES_CDN_RETRY_OVER, FROPlayer.PLAYER_TYPE_NOMAL);
                    }
                    return;
                }

                trackData = nextMusicInfo.getTrackItem();
                File file = new File(FROUtils.getTrackPath()
                        + nextMusicInfo.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ID));
                if (file == null || !file.exists()) {
                    notifyToForm(Consts.START_MUSIC, Consts.NO_EXIST_FILE, null);
                    termMusic(0);
                    return;
                }

                // 初回はプレイヤーの起動とリスナの設定、Notificationの表示
                if (mFaraoPlayer == null) {
                    mFaraoPlayer = new FROPlayer(getApplicationContext());
                    mFaraoPlayer.attach(this);
                    mFaraoPlayer.attachUpgradeListener();
                }
                // 2曲目以降は直前に聞いていた曲をローカルから削除する
                else if (mFaraoPlayer.getPlayerType() == FROPlayer.PLAYER_TYPE_NOMAL) {
                    deleteLocalData(nowPlayingId);
                }
                readNextData(nextMusicInfo);
                // プレイヤーに必要な情報(パス、キー、初期化ベクタ、シグネチャ)を取得
                String filePath = mFaraoPlayer.getPathHeader() + FROUtils.getTrackPath()
                        + nextMusicInfo.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ID);
                String key = nextMusicInfo.getStringValue(IMCMusicItemInfo.MCDB_ITEM_ENC_KEY);
                String iv = nextMusicInfo.getStringValue(IMCMusicItemInfo.MCDB_ITEM_ENC_IV);
                String sig = nextMusicInfo.getStringValue(IMCMusicItemInfo.MCDB_ITEM_API_SIG);

                // プレイヤーの再生準備
                String tmp = nextMusicInfo.getTrackItem().getString(MCDefResult.MCR_KIND_REPLAY_GAIN);
                float replayGain = -100.0f;
                if (!TextUtils.isEmpty(tmp))
                    replayGain = Float.parseFloat(tmp);
                try {
                    mFaraoPlayer.setMedia(filePath, key, iv, sig, FROPlayer.PLAYER_TYPE_NOMAL, replayGain);
                } catch (IllegalArgumentException e) {
                    // notifyToForm(Consts.NOTIFY_ERROR,
                    // Consts.MEDIA_PLAYER_ERROR, null);
                    // e.printStackTrace();
                    if (errorRatingAndPlay(Consts.START_MUSIC, -2, trackData)) {
                        apiManager.doTask();
                    }
                } catch (IllegalStateException e) {
                    // notifyToForm(Consts.NOTIFY_ERROR,
                    // Consts.MEDIA_PLAYER_ERROR, null);
                    // e.printStackTrace();
                    if (errorRatingAndPlay(Consts.START_MUSIC, -2, trackData)) {
                        apiManager.doTask();
                    }
                } catch (IOException e) {
                    // notifyToForm(Consts.NOTIFY_ERROR,
                    // Consts.MEDIA_PLAYER_ERROR, null);
                    // e.printStackTrace();
                    if (errorRatingAndPlay(Consts.START_MUSIC, -2, trackData)) {
                        apiManager.doTask();
                    }
                }

                // プレイリストが切り替わった場合
                if (changePlaylist) {
                    changePlaylist = false;
                    clearCachedMusic();
                }
                // 楽曲がDLされるたびにキャッシュする
                cacheMusic(nextMusicInfo.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ID));

                // ステータスバーの更新
                MusicInfo info;
                if (FROUtils.isPrimaryLanguage()) {
                    info = infoJp;
                } else {
                    info = infoEn;
                }
                info.setId(nowPlayingId);
                NotificationHelper.showNotification(this, info.getTitle(), info.getArtist(), NotificationHelper.getPendingIntent(this));

                // 再生開始、通話中の場合は再生しない
                if (counter == null)
                    counter = new SimpleCounter();
                if (!isTalking) {
                    mFaraoPlayer.startOrPause();
                    counter.start();

                    if (mChannelHistory != null) {
                        FROChannelHistoryDBFactory.getInstance(getApplicationContext()).insert(mChannelHistory);
                        mChannelHistory = null;
                    }
                }
                playTime = 0;

                mFaraoPlayer.getMusicData(info);
                notifyToForm(Consts.NOTIFY_SKIP_REMAINING, Consts.STATUS_OK, Integer.toString(skipRemaining));
                notifyToForm(Consts.START_MUSIC, Consts.STATUS_OK, info);
                isNotifyProgress = false;
            }
        }
    }

    // 次の楽曲の情報を Handler 内のメンバ変数に読み込ませる処理
    // nowPlayingId:track の ID
    // infoJp:楽曲情報、日本語
    // infoEn:楽曲情報、英語
    private void readNextData(IMCMusicItemInfo nextData) {
        MCTrackItem trackItem = (MCTrackItem) nextData.getTrackItem();
        // 次に再生する楽曲のデータをUI層に渡す準備
        nowPlayingId = nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ID);
        mMCHandler.mdbSetStatus(nowPlayingId, IMCMusicItemInfo.MCDB_STATUS_PLAYING);

        infoJp = new MusicInfo();
        infoEn = new MusicInfo();
        infoJp.setPlayerType(FROPlayer.PLAYER_TYPE_NOMAL);
        infoEn.setPlayerType(FROPlayer.PLAYER_TYPE_NOMAL);
        // artist name
        infoJp.setArtist(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ARTIST));
        infoEn.setArtist(TextUtils.isEmpty(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ARTIST_EN))
                ? nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ARTIST)
                : nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ARTIST_EN));
        // album name
        infoJp.setThumb(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ALBUM));
        infoEn.setThumb(TextUtils.isEmpty(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ALBUM_EN))
                ? nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ALBUM)
                : nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ALBUM_EN));
        // アーティストの和名
        infoJp.setUrlSearch(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ARTIST));
        infoEn.setUrlSearch(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ARTIST));
        // music title
        infoJp.setTitle(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_TITLE));
        infoEn.setTitle(TextUtils.isEmpty(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_TITLE_EN))
                ? nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_TITLE)
                : nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_TITLE_EN));
        // genre
        infoJp.setGenre(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_GENRE_LIST));
        infoEn.setGenre(TextUtils.isEmpty(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_GENRE_EN))
                ? nextData.getTrackItem().getString(MCDefResult.MCR_KIND_GENRE_LIST)
                : nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_GENRE_EN));
        // description
        infoJp.setInfo(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_DESCRIPTION));
        infoEn.setInfo(
                TextUtils.isEmpty(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_DESCRIPTION_EN))
                        ? nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_DESCRIPTION)
                        : nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_DESCRIPTION_EN));
        // release date
        infoJp.setRelese(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_RELEASE_DATE));
        infoEn.setRelese(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_RELEASE_DATE));
        // affiliate url
        infoJp.setUrl(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_URL));
        infoEn.setUrl(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_URL));
        // TODO フィールド追加
        // JPN
        String affUrl = trackItem.getString(MCDefResult.MCR_KIND_TRACKITEM_URL);
        infoJp.setUrl(affUrl);
        infoEn.setUrl(affUrl);
        // JPN
        // jacket id
        infoJp.setArtwork(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_JACKET_ID));
        infoEn.setArtwork(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_JACKET_ID));
        // artist id
        infoJp.setArtistId(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ARTIST_ID));
        infoEn.setArtistId(nextData.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ARTIST_ID));

        infoJp.setChannelName(channelName);
        infoEn.setChannelName(TextUtils.isEmpty(channelNameEn) ? channelName : channelNameEn);
    }

    private void deleteLocalData(String id) {
        IMCMusicItemInfo musicInfo;
        File musicFile;
        File imageFile;
        String jacketPath;

        musicInfo = mMCHandler.mdbGet(id);
        if (musicInfo == null) {
            return;
        }
        mMCHandler.mdbDelete(id);
        jacketPath = musicInfo.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_JACKET_ID);
        musicFile = new File(FROUtils.getTrackPath() + id);
        imageFile = new File(FROUtils.getJacketPath() + id);
        if (musicFile.isFile())
            Utils.deleteFile(musicFile);

        IMCMusicItemInfo otherOne = mMCHandler.mdbFindByImage(jacketPath);
        if (imageFile.isFile() && otherOne == null)
            Utils.deleteFile(imageFile);
    }

    CountDownTimer mSimulCountTimer;
    long mSimulStartTime;
    int mSimulTotalTime;
    private static final int SIMUL_LOGGING_INTERVAL = 60 * 60;

    private void setSimulTimer() {
        mSimulStartTime = Utils.getNowTime();
        if (mSimulCountTimer != null)
            mSimulCountTimer.cancel();
        mSimulCountTimer = new CountDownTimer(SIMUL_LOGGING_INTERVAL * 1000, SIMUL_LOGGING_INTERVAL * 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                mSimulTotalTime += SIMUL_LOGGING_INTERVAL;
                MCInit();
                mMCParam.setStringValue(MCDefParam.MCP_KIND_STREAM_ID, ChannelInfo.streamId);
                mMCParam.setStringValue(MCDefParam.MCP_KIND_PLAY_DURATIN, Integer.toString(mSimulTotalTime));
                mMCParam.setStringValue(MCDefParam.MCP_KIND_PLAY_COMPLETE, "no");
                MCPostActionParam copy = new MCPostActionParam();
                copy.copyParam(mMCParam);
                if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_STREAM_LOGGING, copy, null)))
                    mMCHandler.actDoAction(MCDefAction.MCA_KIND_STREAM_LOGGING, mMCParam);
                setSimulTimer();
            }
        };
        mSimulCountTimer.start();
    }

    // private void stopSimulTimer() {
    // if (mSimulCountTimer == null)
    // return;
    //
    // mSimulCountTimer.cancel();
    // mSimulCountTimer = null;
    // long playDuration = Utils.getNowTime() - mSimulStartTime;
    // mSimulTotalTime += (int) (playDuration / 1000);
    // MCInit();
    // mMCParam.setStringValue(MCDefParam.MCP_KIND_STREAM_ID,
    // ChannelInfo.streamId);
    // mMCParam.setStringValue(MCDefParam.MCP_KIND_PLAY_DURATIN,
    // Integer.toString(mSimulTotalTime));
    // mMCParam.setStringValue(MCDefParam.MCP_KIND_PLAY_COMPLETE, "yes");
    // MCPostActionParam copy = new MCPostActionParam();
    // copy.copyParam(mMCParam);
    // if (apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_STREAM_LOGGING,
    // copy, null)))
    // mMCHandler.actDoAction(MCDefAction.MCA_KIND_STREAM_LOGGING, mMCParam);
    // mSimulStartTime = 0;
    // mSimulTotalTime = 0;
    // }

    private boolean stopSimulTimer() {
        boolean setTask = false;
        if (mSimulCountTimer == null)
            return setTask;

        mSimulCountTimer.cancel();
        mSimulCountTimer = null;
        long playDuration = Utils.getNowTime() - mSimulStartTime;
        mSimulTotalTime += (int) (playDuration / 1000);
        MCInit();
        mMCParam.setStringValue(MCDefParam.MCP_KIND_STREAM_ID, ChannelInfo.streamId);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_PLAY_DURATIN, Integer.toString(mSimulTotalTime));
        mMCParam.setStringValue(MCDefParam.MCP_KIND_PLAY_COMPLETE, "yes");
        MCPostActionParam copy = new MCPostActionParam();
        copy.copyParam(mMCParam);
        setTask = apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_STREAM_LOGGING, copy, new ApiRunnable(copy) {
            @Override
            public void run() {
                MCInit();
                ((MCPostActionParam) mMCParam).copyParam(params);
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_STREAM_LOGGING, mMCParam);
            }
        }));

        mSimulStartTime = 0;
        mSimulTotalTime = 0;

        return setTask;
    }

    private void playSimul(final String path) {
        synchronized (MUSIC_TERM_LOCK) {

            if (mFaraoPlayer != null && mFaraoPlayer.isInterrupt()) {
                if (mMusicTimerRunnable == null) {
                    mMusicTimerRunnable = new Runnable() {
                        @Override
                        public void run() {
                            playSimul(path);
                        }
                    };
                }
                return;
            }

            if (TextUtils.isEmpty(path)) {
                setOfflineMode(NetworkStateChecker.ERROR_CAUSES_MALFORMED_URL, FROPlayer.PLAYER_TYPE_SIMUL);
                return;
            }

            // プレイヤーの準備

            if (mFaraoPlayer != null)
                mFaraoPlayer.term();
            mFaraoPlayer = new FROPlayer(getApplicationContext());
            mFaraoPlayer.attach(this);
            mFaraoPlayer.attachUpgradeListener();
            String tmp = nowplayingStream.getString(MCDefResult.MCR_KIND_REPLAY_GAIN);
            float replayGain = -100.0f;
            if (!TextUtils.isEmpty(tmp))
                replayGain = Float.parseFloat(tmp);
            try {
                mFaraoPlayer.setSimulMedia(new FROPlayer.SimulCallbackListener() {

                    @Override
                    public void onSuccess() {
                        Runnable playbackAction = new Runnable() {
                                    @Override
                                    public void run() {
                                        NotificationHelper.showNotification(FROHandler.this, "Fans' Shop BGM",nowplayingStream.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME),
                                                NotificationHelper.getPendingIntent(FROHandler.this));
                                        if (!isTalking) { // 通話中でなければ再生開始
                                            mFaraoPlayer.fadeIn(3000);
                                            setSimulTimer();
                                        }
                                        // 楽曲情報をFORMに送る
                                        MusicInfo info = new MusicInfo(nowplayingStream);
                                        info.setPlayerType(FROPlayer.PLAYER_TYPE_SIMUL);
                                        notifyToForm(Consts.START_STREAM_MUSIC, Consts.STATUS_OK, info);
                                    }
                                };
                        if (mFaraoPlayer != null && mFaraoPlayer.isInterrupt()) {
                            if (mMusicTimerRunnable == null) {
                                mMusicTimerRunnable = playbackAction;
                            }
                        } else {
                            playbackAction.run();
                        }
                    }

                    @Override
                    public void onError(int code) {
                        if (mFaraoPlayer.isInterrupt()) {
                            return;
                        }
                        if (code == MCError.MC_APPERR_IO_HTTP) {
                            notifyToForm(Consts.SHOW_PROGRESS, Consts.STATUS_OK, null);
                            if (getStreamPlaylist(FROHandler.ChannelInfo.streamId)) {
                                apiManager.doTask();
                            }
                        } else if (code == MCError.MC_NOT_FOUND) {
                            String url = nowplayingStream.getString(MCDefResult.MCR_KIND_CONTENT_LINK);
                            setOfflineMode(NetworkStateChecker.ERROR_CAUSES_TRACK_RETRY_OVER,
                                    FROPlayer.PLAYER_TYPE_SIMUL);
                        }
                    }
                }, path, false, FROPlayer.PLAYER_TYPE_SIMUL, replayGain);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private int offlineTrackIndex = 0;
    private IMCMusicItemList offlineTracklist;
    private boolean isEncrypted = false;

    // オフラインプレイの初期化を行い再生を開始する
    // 既に再生中の状態で呼び出された場合は次の楽曲を再生する
    // return true:オフライン再生が開始された, false:それ以外
    private boolean playOffline() {
        // 初期化処理、トラックリストメンバの有無で判断する
        if (offlineTracklist == null || offlineTracklist.getSize() < 1) {
            MCOfflineMusicInfoDB cacheDb = (MCOfflineMusicInfoDB) MCOfflineMusicInfoDBFactory
                    .getInstance(getApplicationContext());
            if (cacheDb.getSize() < 1) {
                isEncrypted = false;
                offlineTracklist = FROUtils.getOfflineTrackList();
            } else {
                isEncrypted = true;
                offlineTracklist = cacheDb.findAll();
            }
        }
        if (offlineTracklist == null || offlineTracklist.getSize() < 1) {
            return false;
        }

        synchronized (MUSIC_TERM_LOCK) {

            if (mFaraoPlayer != null && mFaraoPlayer.isInterrupt()) {
                if (mMusicTimerRunnable == null) {
                    mMusicTimerRunnable = new Runnable() {
                        @Override
                        public void run() {
                            playOffline();
                        }
                    };
                }
                return true;
            }

            IMCMusicItemInfo musicInfo = offlineTracklist.getInfo(offlineTrackIndex++);
            // インデックスがトラックリストのサイズを超過したら、インデックスを頭に戻す
            if (offlineTracklist.getSize() <= offlineTrackIndex) {
                offlineTrackIndex = 0;
            }

            if (mFaraoPlayer == null || (mFaraoPlayer.getPlayerType() != FROPlayer.PLAYER_TYPE_OFFLINE
                    && mFaraoPlayer.getPlayerType() != FROPlayer.PLAYER_TYPE_NOMAL)) {
                mFaraoPlayer = new FROPlayer(getApplicationContext());
                mFaraoPlayer.attach(this);
                mFaraoPlayer.attachUpgradeListener();
            }
            try {
                if (isEncrypted) {

                    // プレイヤーの再生準備
                    String tmp = musicInfo.getTrackItem().getString(MCDefResult.MCR_KIND_REPLAY_GAIN);
                    float replayGain = -100.0f;
                    if (!TextUtils.isEmpty(tmp))
                        replayGain = Float.parseFloat(tmp);

                    mFaraoPlayer.setMedia(
                            mFaraoPlayer.getPathHeader()
                                    + musicInfo.getStringValue(IMCMusicItemInfo.MCDB_ITEM_OUTPUT_PATH),
                            musicInfo.getStringValue(IMCMusicItemInfo.MCDB_ITEM_ENC_KEY),
                            musicInfo.getStringValue(IMCMusicItemInfo.MCDB_ITEM_ENC_IV),
                            musicInfo.getStringValue(IMCMusicItemInfo.MCDB_ITEM_API_SIG), FROPlayer.PLAYER_TYPE_OFFLINE,
                            replayGain);
                } else {
                    mFaraoPlayer.setLocalMedia(musicInfo.getStringValue(IMCMusicItemInfo.MCDB_ITEM_OUTPUT_PATH), false,
                            FROPlayer.PLAYER_TYPE_OFFLINE);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (RuntimeException e) {
                e.printStackTrace();
                return false;
            }

            if (!isTalking)
                mFaraoPlayer.startOrPause();

            NotificationHelper.showNotification(this, "Fans' Shop BGM", getString(R.string.page_title_offline_player));
            MusicInfo info = new MusicInfo();
            mFaraoPlayer.getMusicData(info);
            info.setPlayerType(FROPlayer.PLAYER_TYPE_OFFLINE);
            info.setId(musicInfo.getStringValue(MCDefResult.MCR_KIND_TRACKITEM_ID));
            info.setUrl(musicInfo.getStringValue(IMCMusicItemInfo.MCDB_ITEM_OUTPUT_PATH));
            notifyToForm(Consts.START_OFFLINE_MUSIC, Consts.STATUS_OK, info);

            return true;
        }
    }

    // オフラインプレイヤーの再生 + ネットワーク確認スレッドの初期化
    private void setOfflineMode(String cause, int playedType, int begin) {

        if (apiManager != null) {
            apiManager.showQueue("before starting offline");
        }

        if (playOffline()) {
            if (mStateCheckThread == null || !mStateCheckThread.isRunning()) {
                mStateCheckThread = new NetworkStateChecker(getApplicationContext(), cause, playedType, begin);
                mStateCheckThread.start();
            }
        } else {
            if (mStateCheckThread == null || !mStateCheckThread.isRunning()) {
                mStateCheckThread = new NetworkStateChecker(getApplicationContext(), cause, playedType, begin);
                // 復旧時のコールバック
                mStateCheckThread.setRecoveryCallback(new Runnable() {
                    @Override
                    public void run() {
                        notifyToForm(Consts.SHOW_PROGRESS, Consts.STATUS_OK, null);
                        if (sendNetworkRecovery())
                            apiManager.doTask();
                    }
                });
                // タイムアウト時のコールバック
                mStateCheckThread.setTimeoutCallback(new Runnable() {
                    @Override
                    public void run() {
                        notifyToForm(Consts.NOTIFY_FAILED_TO_RECOVER, Consts.STATUS_OK, null);
                        clearMusicData(true);
                        mStateCheckThread.release();
                        mStateCheckThread = null;
                        offlineTrackIndex = 0;
                        offlineTracklist = null;
                    }
                });
                MusicInfo info = new MusicInfo();
                info.setPlayerType(FROPlayer.PLAYER_TYPE_OFFLINE);
                notifyToForm(Consts.START_OFFLINE_MUSIC, Consts.STATUS_OK, info);
                mStateCheckThread.start();
            }
            clearMusicData();
            offlineTrackIndex = 0;
            offlineTracklist = null;
        }
    }

    private void setOfflineMode(String cause, int playedType) {
        this.setOfflineMode(cause, playedType, -1);
    }

    /**
     * // // // 通知バーの内容を決める // ntc = new //
     * NotificationCompat.Builder(this).setSmallIcon(R.drawable.
     * icon_notification).setTicker("FaRao") 終了処理
     */
    private void term() {
        cancelAllTimer();
        clearMusicData(true);
        if (resendList != null) {
            resendList.clear();
            resendList = null;
        }
        mMCHandler.term();
        MusicClientFactory.deleteInstance();
        mMCHandler = null;
        if (apiManager != null)
            apiManager.clearTask();
        apiManager = null;

        // WakeLockを解放する
        synchronized (mWakelockGuard) {
            if (mWakelock != null) {
                // Partial Wake Lockを解放
                mWakelock.release();
                mWakelock = null;
            }
        }
    }

    /**
     * 音楽情報消去
     */
    private void clearMusicData(boolean removeNotification) {
        synchronized (MUSIC_TERM_LOCK) {
            // isCancel = false;
            if (mFaraoPlayer != null) {
                mFaraoPlayer.term();
            }
            mFaraoPlayer = null;
            MCInit();
            mMCHandler.mdbDeleteAll();
            // if (trackList != null) {
            // trackList.clear();
            // }
            if (mTrackDownloadList != null)
                mTrackDownloadList.clear();
            mTrackDownloadList = null;
            if (counter != null) {
                counter.stop();
                counter = null;
            }
            isWaittingForDL = false;
            nowPlayingId = null;
            if (removeNotification)
                NotificationHelper.removeNotification(this);
        }
    }

    private void clearMusicData() {
        clearMusicData(false);
    }

    private boolean updateTemplateState(String templateId) {
        MCInit();
        mMCParam.setStringValue(MCDefParam.MCP_KIND_TEMPLATE_ID, templateId);
        MCPostActionParam copy = new MCPostActionParam();
        copy.copyParam(mMCParam);
        boolean setTask = apiManager
                .setTask(new ApiTask(MCDefAction.MCA_KIND_TEMPLATE_UPDATE, copy, new ApiRunnable(copy) {
                    @Override
                    public void run() {
                        MCInit();
                        ((MCPostActionParam) mMCParam).copyParam(params);
                        mMCHandler.actDoAction(MCDefAction.MCA_KIND_TEMPLATE_UPDATE, mMCParam);
                    }
                }));

        return setTask;
    }

    private boolean getInterruptSchedule(String date) {
        MCInit();
        if (mPatternScheduleDateList == null)
            mPatternScheduleDateList = new ArrayList<String>();
        mPatternScheduleDateList.add(date);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_TARGET_DATE, date);
        MCPostActionParam copy = new MCPostActionParam();
        copy.copyParam(mMCParam);
        ApiRunnable task = new ApiRunnable(copy) {
            @Override
            public void run() {
                MCInit();
                ((MCPostActionParam) mMCParam).copyParam(params);
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_PATTERN_SCHEDULE, mMCParam);
            }
        };
        boolean setTask = apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_PATTERN_SCHEDULE, copy, task));

        return setTask;
    }

    private boolean updateInterruptSchedule(String targetDate, String patternId) {
        if (TextUtils.isEmpty(patternId))
            patternId = null;
        MCInit();
        mMCParam.setStringValue(MCDefParam.MCP_KIND_TARGET_DATE, targetDate);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_PATTERN_ID, patternId);
        MCPostActionParam copy = new MCPostActionParam();
        copy.copyParam(mMCParam);
        ApiRunnable task = new ApiRunnable(copy) {
            @Override
            public void run() {
                MCInit();
                ((MCPostActionParam) mMCParam).copyParam(params);
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_PATTERN_UPDATE, mMCParam);
            }
        };
        boolean setTask = apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_PATTERN_UPDATE, copy, task));

        return setTask;
    }

    private boolean getInterruptContents(String patternId) {
        // mPatternDownloadDate = targetDate;
        MCInit();
        mMCParam.setStringValue(MCDefParam.MCP_KIND_PATTERN_ID, patternId);
        MCPostActionParam copy = new MCPostActionParam();
        copy.copyParam(mMCParam);
        boolean setTask = apiManager
                .setTask(new ApiTask(MCDefAction.MCA_KIND_PATTERN_DOWNLOAD, copy, new ApiRunnable(copy) {
                    @Override
                    public void run() {
                        MCInit();
                        ((MCPostActionParam) mMCParam).copyParam(params);
                        mMCHandler.actDoAction(MCDefAction.MCA_KIND_PATTERN_DOWNLOAD, mMCParam);
                    }
                }));

        return setTask;
    }

    private boolean callapiOnair(String frameId, List<String> audioList, String complete) {
        if(audioList == null || audioList.size() < 1) {
            return false;
        }

        String audioIds = "";
        for (String id : audioList) {
            audioIds += (id + "&");
        }
        audioIds = audioIds.substring(0, audioIds.length() - 1);
        MCUserInfoPreference.getInstance(getApplicationContext()).setOnairOffline(frameId, audioList);

        MCInit();
        mMCParam.setStringValue(MCDefParam.MCP_KIND_FRAME_ID, frameId);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_AUDIO_ID, audioIds);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_COMPLETE, complete);
        MCPostActionParam copy = new MCPostActionParam();
        copy.copyParam(mMCParam);
        ApiRunnable task = new ApiRunnable(copy) {
            @Override
            public void run() {
                MCInit();
                ((MCPostActionParam) mMCParam).copyParam(params);
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_PATTERN_ONAIR, mMCParam);
            }
        };
        boolean setTask = apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_PATTERN_ONAIR, copy, task));
        return setTask;
    }

    private boolean callapiOnairOffline() {
        String data = MCUserInfoPreference.getInstance(getApplicationContext()).getOnairOffline();
        if (TextUtils.isEmpty(data))
            return true;

        String[] tmp = data.split("@", 0);
        String frameId = tmp[0];
        String audioIds = tmp[1].substring(0, tmp[1].length() - 1);

        MCInit();
        mMCParam.setStringValue(MCDefParam.MCP_KIND_TRACKING_KEY,
                UserDataPreference.getTrackingKey(getApplicationContext()));
        mMCParam.setStringValue(MCDefParam.MCP_KIND_FRAME_ID, frameId);
        mMCParam.setStringValue(MCDefParam.MCP_KIND_AUDIO_ID, audioIds);
        MCPostActionParam copy = new MCPostActionParam();
        copy.copyParam(mMCParam);
        ApiRunnable task = new ApiRunnable(copy) {
            @Override
            public void run() {
                MCInit();
                ((MCPostActionParam) mMCParam).copyParam(params);
                mMCHandler.actDoAction(MCDefAction.MCA_KIND_PATTERN_ONAIR_OFFLINE, mMCParam);
            }
        };
        boolean setTask = apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_PATTERN_ONAIR_OFFLINE, copy, task));
        return setTask;
    }

    private boolean checkPaymentStatus(int when, IMCResultInfo result) {
        if (!UserDataPreference.isPremium(getApplicationContext()) || result == null)
            return false;

        int times = Integer.parseInt(result.getString(MCDefResult.MCR_KIND_SUBSCRIPTION_STATUS));
        if (times <= 0) {
            UserDataPreference.setRemainingTime(getApplicationContext(), times);
            termMusic(0);
            logout();
            notifyToForm(when, Consts.STATUS_NO_REMAIN_TIMES, null);
            return true;
        }
        return false;
    }

    private boolean checkPermission(int when, IMCResultInfo result) {
        return checkPermission(when, result, true);
    }

    private boolean checkPermission(int when, IMCResultInfo result, boolean doTerminate) {
        FRODebug.logD(getClass(), "checkPermission", DEBUG);
        if (!UserDataPreference.isPremium(getApplicationContext()) || result == null)
            return false;

        // permission が正しく設定(0xffff)されているならば BUSINESS に限定する必要ない
        FROUserData servicePlan = new FROUserData(result, Utils.getNowTime());
        UserDataPreference.setServicePlan(getApplicationContext(), servicePlan);
        FRODebug.logD(getClass(), "ChannelInfo.mode = " + ChannelInfo.mode, DEBUG);
        FRODebug.logD(getClass(), "ChannelInfo.permission = " + ChannelInfo.permission, DEBUG);
        FRODebug.logD(getClass(),
                "UserDataPreference.getPermissions = " + UserDataPreference.getPermissions(getApplicationContext()),
                DEBUG);
        if (ChannelMode.BUSINESS.text.equals(ChannelInfo.mode)
                && (UserDataPreference.getPermissions(getApplicationContext()) & ChannelInfo.permission) <= 0) {
            if (doTerminate) {
                termMusic(0);
                notifyToForm(when, Consts.NOTIFY_PERMISSION_IS_CHANGED, null);
            }
            return true;
        }
        return false;
    }

    private boolean checkFeature(int when, IMCResultInfo result) {
        FRODebug.logD(getClass(), "checkFeature " + when, DEBUG);
        if (!UserDataPreference.isPremium(getApplicationContext()) || result == null)
            return false;

        FROUserData servicePlan = new FROUserData(result, Utils.getNowTime());
        UserDataPreference.setServicePlan(getApplicationContext(), servicePlan);

        ServiceLevel feature = null;
        switch (when) {
            case MCDefAction.MCA_KIND_STREAM_PLAY:
            case MCDefAction.MCA_KIND_STREAM_LOGGING:
                feature = ServiceLevel.HLS;
                break;
            case MCDefAction.MCA_KIND_LISTEN:
            case MCDefAction.MCA_KIND_RATING:
                if (ChannelMode.ARTIST.text.equals(ChannelInfo.mode) || ChannelMode.RELEASE.text.equals(ChannelInfo.mode)) {
                    feature = ServiceLevel.SEARCH;
                }
                break;
        }
        FRODebug.logD(getClass(), "feature " + feature, DEBUG);
        FRODebug.logD(getClass(), "UserDataPreference " + UserDataPreference.getFeatures(getApplicationContext()),
                DEBUG);
        if (feature != null)
            FRODebug.logD(getClass(),
                    "contain " + feature.isContained(UserDataPreference.getFeatures(getApplicationContext())), DEBUG);
        if (feature != null && !feature.isContained(UserDataPreference.getFeatures(getApplicationContext()))) {
            termMusic(0);
            notifyToForm(when, Consts.NOTIFY_FEATURE_IS_CHANGED, null);
            return true;
        }
        return false;
    }

    // TODO tracking key ならびに user status は preference から取得するように変更し、変数は削除すること
    // private String trackingKey;
    // private int paymentState;

    private boolean statusCheckOnBoot = false; // TOOO 旧 login api 用の暫定対応

    /**
     * MusicClientからの通知(正常系)
     *
     * @param when   どの要求に対する通知か
     * @param result 要求に対する結果
     */
    @Override
    public void onNotifyMCResult(int when, IMCResultInfo result) {
        FRODebug.logD(getClass(), "onNotifyMCResult", DEBUG);
        FRODebug.logD(getClass(), "when = " + MCDefAction.getApi(when), DEBUG);
        if (result != null)
            FRODebug.logD(getClass(), "msg = " + result.getString(MCDefResult.MCR_KIND_MESSAGE), DEBUG);

        IMCItemList playList;
        String msg;
        int kind;
        Runnable callback;
        FROPatternContentDB contentDb;

        if (apiManager.isTerminate)
            return;
        synchronized (mMCResultLock) {
            switch (when) {

                case MCDefAction.MCA_KIND_SIGNUP:
                    msg = result.getString(MCDefResult.MCR_KIND_MESSAGE);
                    if (Consts.HTTP_MSG_OK.equals(msg)) {
                        notifyToForm(when, Consts.STATUS_OK, null);
                    } else {
                        kind = MCDefResult.getMsgKind(msg);
                        notifyToForm(when, kind, null);
                    }
                    break;
                case MCDefAction.MCA_KIND_LOGIN:
                    msg = result.getString(MCDefResult.MCR_KIND_MESSAGE);
                    if (Consts.HTTP_MSG_OK.equals(msg)) {
                        MCUserInfoPreference.getInstance(getApplicationContext())
                                .setLatestVersionCode(Utils.getVersionCode(getApplicationContext()));
                        MCUserInfoPreference.getInstance(getApplicationContext()).term();

                        statusCheckOnBoot = true;
                        authStatus();

                    } else {
                        kind = MCDefResult.getMsgKind(msg);
                        notifyToForm(when, kind, null);
                    }
                    break;
                case MCDefAction.MCA_KIND_LICENSE_STATUS:
                    // トラッキングキーが空でなければ、未送信データの確認を別スレッドで行う
                    String trackingKey = UserDataPreference.getTrackingKey(getApplicationContext());
                    if (!TextUtils.isEmpty(trackingKey))
                        UnsentDataSender.send(getApplicationContext(), trackingKey);
                    String lat = result.getString(MCDefResult.MCR_KIND_LATITUDE);
                    String lon = result.getString(MCDefResult.MCR_KIND_LONGITUDE);
                    String interval = result.getString(MCDefResult.MCR_KIND_TRACKING_INTERVAL);
                    if (!TextUtils.isEmpty(interval) && TextUtils.isDigitsOnly(interval)) {
                        int tmp = Integer.parseInt(interval);
                        MCUserInfoPreference.getInstance(getApplicationContext()).setLicenseInterval(tmp);
                    }

                    int callbackApi;
                    if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lon)) {
                        callbackApi = MCDefAction.MCA_KIND_LICENSE_INSTALL;
                    } else {
                        callbackApi = MCDefAction.MCA_KIND_LICENSE_TRACKING;
                    }
                    getLocation(callbackApi);
                    notifyToForm(when, Consts.STATUS_OK, null);
                    break;
                case MCDefAction.MCA_KIND_LOGOUT:
                    deleteUserSpecificData();
                    cancelAllTimer();
                    notifyToForm(when, Consts.STATUS_OK, null);
                    break;
                case MCDefAction.MCA_KIND_STATUS:
                    statusCheckOnBoot = false;
                    FROUserData userData = new FROUserData(result, Utils.getNowTime());
                    UserDataPreference.setUserData(getApplicationContext(), userData);
                    if (checkPaymentStatus(when, result)) {
                        break;
                    }

                    if (recoveryAction != null) {
                        new Thread(recoveryAction).start();
                    } else {
                        notifyToForm(MCDefAction.MCA_KIND_STATUS, Consts.STATUS_OK, userData);
                    }
                    if (loginAction != null)
                        loginAction = null;

                    break;
                case MCDefAction.MCA_KIND_LIST:
                case MCDefAction.MCA_KIND_SEARCH:
                case MCDefAction.MCA_KIND_FEATURED:
                    skipControl = false;
                    MCListDataInfo dst;
                    switch (listType) {
                        case IMCResultInfo.MC_LIST_KIND_GENRE:
                            MCGenreItemList genre = (MCGenreItemList) result.getList(IMCResultInfo.MC_LIST_KIND_GENRE);
                            dst = new MCListDataInfo();
                            if (genre != null) {
                                for (MCGenreItem item : genre) {
                                    dst.add(item);
                                }
                            }
                            dst.itemName = MCGenreItem.class.getSimpleName();
                            notifyToForm(Consts.CHANNEL_LIST_GENRE, Consts.STATUS_OK, dst);
                            break;
                        case IMCResultInfo.MC_LIST_KIND_SEARCH:
                            MCSearchItemList artist = (MCSearchItemList) result.getList(IMCResultInfo.MC_LIST_KIND_SEARCH);
                            dst = new MCListDataInfo();
                            if (artist != null) {
                                for (MCSearchItem item : artist) {
                                    dst.add(item);
                                }
                            }
                            dst.itemName = MCSearchItem.class.getSimpleName();
                            notifyToForm(Consts.CHANNEL_LIST_ARTIST, Consts.STATUS_OK, dst);
                            break;
                        case IMCResultInfo.MC_LIST_KIND_FEATURED:
                            MCSearchItemList featured = (MCSearchItemList) result.getList(IMCResultInfo.MC_LIST_KIND_FEATURED);
                            dst = new MCListDataInfo();
                            if (featured != null) {
                                for (MCSearchItem item : featured) {
                                    dst.add(item);
                                }
                            }
                            dst.itemName = MCSearchItem.class.getSimpleName();
                            notifyToForm(Consts.CHANNEL_LIST_FEATURED, Consts.STATUS_OK, dst);
                            break;
                        case IMCResultInfo.MC_LIST_KIND_CHANNEL:
                            MCChannelItemList mychannel = (MCChannelItemList) result
                                    .getList(IMCResultInfo.MC_LIST_KIND_CHANNEL);
                            // 日付順にソート
                            if (mychannel != null && mychannel.size() > 0) {
                                Object[] itemAry = mychannel.toArray();
                                Arrays.sort(itemAry, new DataComparator());
                                mychannel.clear();
                                mychannel = new MCChannelItemList();
                                for (Object obj : itemAry) {
                                    mychannel.add((MCChannelItem) obj);
                                }
                                dst = new MCListDataInfo();
                                if (mychannel != null) {
                                    for (MCChannelItem item : mychannel) {
                                        dst.add(item);
                                    }
                                }
                                dst.itemName = MCChannelItem.class.getSimpleName();
                                notifyToForm(Consts.CHANNEL_LIST_MYCHANNEL, Consts.STATUS_OK, dst);
                            } else {
                                notifyToForm(Consts.CHANNEL_LIST_MYCHANNEL, Consts.STATUS_NO_DATA, null);
                            }
                            break;
                        case IMCResultInfo.MC_LIST_KIND_CHART:
                            // 特典リスト
                            MCBenifitItemList list = (MCBenifitItemList) result.getList(IMCResultInfo.MC_LIST_KIND_BENIFIT);
                            dst = new MCListDataInfo();
                            if (list != null) {
                                benifitList = (MCBenifitItemList) list.clone();
                                for (MCBenifitItem item : list) {
                                    dst.add(item);
                                }
                            }
                            dst.itemName = MCBenifitItem.class.getSimpleName();
                            notifyToForm(Consts.CHANNEL_LIST_BENIFIT, Consts.STATUS_OK, dst);
                            // // ホットチャート
                            // MCChartItemList chart = (MCChartItemList)
                            // result.getList(IMCResultInfo.MC_LIST_KIND_CHART);
                            // MCListDataInfo dstSpecial = new MCListDataInfo();
                            // if (chart != null) {
                            // MCChartItemList chartList = (MCChartItemList)
                            // chart.clone();
                            // for (MCChartItem item : chartList) {
                            // item.mMode = Consts.MODE_HOT100;
                            // int royalty;
                            // try {
                            // royalty =
                            // Integer.parseInt(item.getString(MCDefResult.MCR_KIND_CHARTITEM_ROYALTY));
                            // } catch (NumberFormatException e) {
                            // royalty = -1;
                            // }
                            // if (paymentState == royalty)
                            // dstSpecial.add(item);
                            // }
                            // }
                            // // シャッフルチャート
                            // MCChartItemList shuffle = (MCChartItemList)
                            // result.getList(IMCResultInfo.MC_LIST_KIND_SHUFFLE);
                            // if (shuffle != null) {
                            // MCChartItemList shuffleList = (MCChartItemList)
                            // shuffle.clone();
                            // for (MCChartItem item : shuffleList) {
                            // item.mMode = Consts.MODE_SHUFFLE;
                            // int royalty;
                            // try {
                            // royalty =
                            // Integer.parseInt(item.getString(MCDefResult.MCR_KIND_CHARTITEM_ROYALTY));
                            // } catch (NumberFormatException e) {
                            // royalty = -1;
                            // }
                            // if (paymentState == royalty)
                            // dstSpecial.add(item);
                            // }
                            // }
                            // dstSpecial.itemName = MCChartItem.class.getSimpleName();
                            // notifyToForm(Consts.CHANNEL_LIST_CHART, Consts.STATUS_OK,
                            // dstSpecial);
                            break;
                        case IMCResultInfo.MC_LIST_KIND_BUSINESS:
                            MCBusinessItemList business = (MCBusinessItemList) result
                                    .getList(IMCResultInfo.MC_LIST_KIND_BUSINESS);
                            dst = new MCListDataInfo();
                            if (business != null) {
                                for (MCBusinessItem item : business) {
                                    dst.add(item);
                                }
                            }
                            dst.itemName = MCBusinessItem.class.getSimpleName();
                            notifyToForm(Consts.CHANNEL_LIST_BUSINESS, Consts.STATUS_OK, dst);
                            break;
                    }
                    break;
                case MCDefAction.MCA_KIND_MSG_DL:
                    ListDataInfo messageContainer = new ListDataInfo();
                    messageContainer.add(result.getString(MCDefResult.MCR_KIND_MESSAGE).replaceAll("\r\n", "\n"));
                    messageContainer.add(result.getString(MCDefResult.MCR_KIND_MESSAGE_EN).replaceAll("\r\n", "\n"));
                    notifyToForm(when, Consts.STATUS_OK, messageContainer);
                    break;
                case MCDefAction.MCA_KIND_CHANNEL_SHARE:
                    String shareKey = result.getString(MCDefResult.MCR_KIND_SHAREITEM_SHARE_KEY);
                    String name = result.getString(MCDefResult.MCR_KIND_SHAREITEM_SHARE_NAME);
                    notifyToForm(when, Consts.STATUS_OK, shareKey + " " + name);
                    break;
                case MCDefAction.MCA_KIND_CHANNEL_EXPAND:
                    msg = result.getString(MCDefResult.MCR_KIND_MESSAGE);
                    if (Consts.HTTP_MSG_OK.equals(msg)) {
                        String idEx = (result.getString(MCDefResult.MCR_KIND_SHAREITEM_SHARE_ID));
                        notifyToForm(when, Consts.STATUS_OK, idEx);
                    } else {
                        kind = MCDefResult.getMsgKind(msg);
                        notifyToForm(when, kind, null);
                    }
                    break;
                // 地域リスト取得結果
                case MCDefAction.MCA_KIND_LOCATION:
                    MCLocationItemList src = (MCLocationItemList) result.getList(IMCResultInfo.MC_LIST_KIND_LOCATION);
                    dst = new MCListDataInfo();
                    for (MCLocationItem item : src) {
                        dst.add(item);
                    }
                    dst.itemName = MCLocationItem.class.getSimpleName();
                    notifyToForm(when, Consts.STATUS_OK, dst);
                    break;
                case MCDefAction.MCA_KIND_TICKET_CHECK:
                    msg = result.getString(MCDefResult.MCR_KIND_MESSAGE);
                    // 正常系
                    if (Consts.HTTP_MSG_OK.equals(msg)) {
                        playList = result.getList(IMCResultInfo.MC_LIST_KIND_CAMPAIGN);
                        // データが存在する場合
                        if (playList != null && playList.getSize() > 0) {
                            MCCampaignItem item = (MCCampaignItem) playList.getItem(0);
                            campaignId = item.getString(MCDefResult.MCR_KIND_CAMPAIGN_ID);
                            notifyToForm(when, Consts.STATUS_OK, null);
                        } else {
                            // TODO ERROR NO DATA
                        }
                    }
                    // 異常系
                    else {
                        kind = MCDefResult.getMsgKind(msg);
                        notifyToForm(when, kind, null);
                    }
                    break;
                case MCDefAction.MCA_KIND_TICKET_ADD:
                    msg = result.getString(MCDefResult.MCR_KIND_MESSAGE);
                    // 正常系
                    if (Consts.HTTP_MSG_OK.equals(msg)) {
                        playList = result.getList(IMCResultInfo.MC_LIST_KIND_CAMPAIGN);
                        // データが存在する場合
                        if (playList != null && playList.getSize() > 0) {
                            notifyToForm(when, Consts.STATUS_OK, null);
                        } else {
                            notifyToForm(when, Consts.STATUS_NO_DATA, null);
                        }
                    }
                    // 異常系
                    else {
                        kind = MCDefResult.getMsgKind(msg);
                        notifyToForm(when, kind, null);
                    }
                    break;
                case MCDefAction.MCA_KIND_TEMPLATE_LIST:
                    MCTemplateItemList template = (MCTemplateItemList) result.getList(IMCResultInfo.MC_LIST_KIND_TEMPLATE);
                    // タイマーの自動更新を行うケース
                    if (isAutoTimerRenewal) {
                        MCTemplateItem last = FROTemplateTimerAutoListDBFactory.getInstance(getApplicationContext()).find();
                        MCTemplateItem newone = template != null ? template.get(0) : null;
                        String lastDigest = last != null ? last.getString(MCDefResult.MCR_KIND_TEMPLATE_DIGEST) : "";
                        String lastRule = last != null ? last.getString(MCDefResult.MCR_KIND_TEMPLATE_RULE) : "";
                        // XML の返りが空のケース
                        if (newone == null) {
                            // テンプレートのコンテンツ情報 + テンプレートの digest を破棄する
                            FROTimerAutoDBFactory.getInstance(getApplicationContext()).deleteAll();
                            FROTemplateTimerAutoListDBFactory.getInstance(getApplicationContext()).clearDigest();
                            updateTemplateState(null);
                            isAutoTimerRenewal = false;
                            notifyToForm(Consts.NOTIFY_RENEWAL_TIMER_IS_CHANGED, Consts.STATUS_OK, null);
                        } else {
                            String nowDigest = newone.getString(MCDefResult.MCR_KIND_TEMPLATE_DIGEST);
                            String nowRule = newone.getString(MCDefResult.MCR_KIND_TEMPLATE_RULE);
                            // DB に保存されている digest, rule が空、またはいずれかが新しい値と異なっている場合
                            if (TextUtils.isEmpty(lastDigest) || TextUtils.isEmpty(lastRule)
                                    || !lastDigest.equals(nowDigest) || !lastRule.equals(nowRule)) {
                                // テンプレート更新情報を保存している DB の内容をアップデートする
                                FROTemplateTimerAutoListDBFactory.getInstance(getApplicationContext()).deleteAll();
                                FROTemplateTimerAutoListDBFactory.getInstance(getApplicationContext()).insert(newone);
                            }
                            if (TextUtils.isEmpty(lastDigest) || !lastDigest.equals(nowDigest)) {
                                MCInit();
                                mMCParam.setStringValue(MCDefParam.MCP_KIND_TEMPLATE_TYPE,
                                        newone.getString(MCDefResult.MCR_KIND_TEMPLATE_TYPE));
                                mMCParam.setStringValue(MCDefParam.MCP_KIND_TEMPLATE_ID,
                                        newone.getString(MCDefResult.MCR_KIND_TEMPLATE_ID));
                                MCPostActionParam copy = new MCPostActionParam();
                                copy.copyParam(mMCParam);
                                apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_TEMPLATE_DOWNLOAD, copy, null));
                            } else {
                                updateTemplateState(last.getString(MCDefResult.MCR_KIND_TEMPLATE_ID));
                                isAutoTimerRenewal = false;
                            }
                        }
                        TimerHelper.setTemplateTimer(getApplicationContext(), last);
                    }
                    // テンプレートリストを表示するケース
                    else {
                        dst = new MCListDataInfo();
                        if (template != null) {
                            for (MCTemplateItem item : template) {
                                dst.add(item);
                            }
                        }
                        dst.itemName = MCTemplateItem.class.getSimpleName();
                        notifyToForm(when, Consts.STATUS_OK, dst);
                    }
                    break;
                case MCDefAction.MCA_KIND_TEMPLATE_DOWNLOAD:
                    MCTimetableItemList timetableList = null;
                    timetableList = (MCTimetableItemList) result.getList(IMCResultInfo.MC_LIST_KIND_TIMETABLE);
                    // タイマーの自動更新を行うケース
                    if (isAutoTimerRenewal) {
                        isAutoTimerRenewal = false;
                        ITimerDB timerDb = FROTimerAutoDBFactory.getInstance(getApplicationContext());
                        timerDb.deleteAll();
                        if (timetableList != null) {
                            for (IMCItem mcItem : timetableList) {
                                TimerInfo info = new TimerInfo((MCTimetableItem) mcItem);
                                timerDb.insert(info);
                            }
                        } else {
                            FROTemplateTimerAutoListDBFactory.getInstance(getApplicationContext()).clearDigest();
                        }
                        TimerHelper.setMusicTimer(getApplicationContext());
                        MCTemplateItem db = FROTemplateTimerAutoListDBFactory.getInstance(getApplicationContext()).find();
                        String templateId = (MCUserInfoPreference.getInstance(getApplicationContext()).getMusicTimerEnable()
                                && db != null) ? db.getString(MCDefResult.MCR_KIND_TEMPLATE_ID) : null;
                        // 更新の通知を行う
                        updateTemplateState(templateId);
                        notifyToForm(Consts.NOTIFY_RENEWAL_TIMER_IS_CHANGED, Consts.STATUS_OK, null);
                    }
                    // テンプレートリストを表示するケース
                    else {
                        MCBookmarkItemList bookmarkList = null;
                        bookmarkList = (MCBookmarkItemList) result.getList(IMCResultInfo.MC_LIST_KIND_BOOKMARK);
                        dst = new MCListDataInfo();
                        if (timetableList != null && timetableList.size() > 0) {
                            for (MCTimetableItem item : timetableList) {
                                dst.add(item);
                            }
                            dst.itemName = MCTimetableItem.class.getSimpleName();
                        } else {
                            for (MCBookmarkItem item : bookmarkList) {
                                dst.add(item);
                            }
                            dst.itemName = MCBookmarkItem.class.getSimpleName();
                        }
                        notifyToForm(when, Consts.STATUS_OK, dst);
                    }
                    break;
                case MCDefAction.MCA_KIND_STREAM_LIST:
                    MCStreamItemList streamList = null;
                    streamList = (MCStreamItemList) result.getList(IMCResultInfo.MC_LIST_KIND_STREAM);
                    dst = new MCListDataInfo();
                    if (streamList != null && streamList.size() > 0) {
                        for (MCStreamItem item : streamList) {
                            dst.add(item);
                        }
                        dst.itemName = MCStreamItem.class.getSimpleName();
                    }
                    notifyToForm(when, Consts.STATUS_OK, dst);
                    break;

                case MCDefAction.MCA_KIND_LISTEN:
                    // プレミアム会員の場合
                    if (checkPaymentStatus(when, result)) {
                        break;
                    }
                    if (checkPermission(when, result)) {
                        break;
                    }
                    if (checkFeature(when, result)) {
                        break;
                    }
                    // オフラインデータ送信フラグをfalse
                    resendFlag = false;
                    // 終了処理フラグをfalse
                    isTerminating = false;
                    // トラックリストを初期化
                    // if (trackList != null) {
                    // trackList.clear();
                    // trackList = null;
                    // }
                    // trackList = new MCTrackItemList();
                    if (mTrackDownloadList != null)
                        mTrackDownloadList.clear();
                    mTrackDownloadList = new CopyOnWriteArrayList<MCTrackItem>();
                    channelName = null;
                    channelNameEn = null;
                    changePlaylist = true;
                    // プレイリストを取得
                    playList = result.getList(IMCResultInfo.MC_LIST_KIND_TRACK);
                    // 得られたプレイリストが0より大きければ
                    if (playList != null && playList.getSize() > 0) {
                        channelName = result.getString(MCDefResult.MCR_KIND_CHANNEL_NAME);
                        channelNameEn = result.getString(MCDefResult.MCR_KIND_CHANNEL_NAME_EN);
                        // スキップ可能回数の更新
                        String skipCount = result.getString(MCDefResult.MCR_KIND_SKIPREMAINING);
                        skipRemaining = Integer.parseInt(skipCount);
                        // トラックリストに得られたプレイリストを追加する
                        mTrackDownloadList.addAll((MCTrackItemList) playList);
                        result.clear();
                        mChannelHistory = new ChannelHistoryInfo(channelName, channelNameEn, ChannelInfo.mode,
                                ChannelInfo.channelId, ChannelInfo.range, -1);
                        // 楽曲のDL
                        getCdnMusicInfo(true);
                    } else {
                        // データ取得失敗通知の発行
                        notifyToForm(when, Consts.STATUS_NO_DATA, null);
                    }
                    break;
                case MCDefAction.MCA_KIND_DOWNLOAD_CDN:
                    MCCdnMusicItem item = ((MCCdnMusicItem) result.getItem(MCDefResult.MCR_KIND_CDN_LIST));
                    // 拡張コールバックがあるか確認する
                    callback = extensionCallback.get(MCDefAction.MCA_KIND_ARTWORK_DL);
                    // 有ればコールバックを実行
                    if (callback != null) {
                        getPictureFile(item.getString(MCDefResult.MCR_KIND_ITEM_CDN_JACKET_URL), hlsJacketUrl.remove(0));
                        break;
                    }
                    // 拡張コールバックが存在しない = トラック、ジャケット両方をダウンロードしたいケース
                    else {
                        this.cdnInfo = item.copy();
                        if (mTrackDownloadList != null && !getMusicFile(true)) {
                            MCTrackItem trackData = mTrackDownloadList.remove(0);
                            setDownloading(false);
                            errorRatingAndPlay(MCDefAction.MCA_KIND_TRACK_DL, -1, trackData);
                        }
                    }
                    break;
                case MCDefAction.MCA_KIND_TRACK_DL:
                    // #term 後にAPIがキャンセルされていないケースがあるので、保険で trackList インスタンスの有無を確認
                    // null であれば再生のシーケンスから外れている(=終了が選択された)
                    if (mTrackDownloadList != null && !isTerminating) {
                        notifyToForm(when, Consts.STATUS_OK, null);
                        if (getPictureFile(true)) {
                            break;
                        } else {
                            downloadNextIfNeeded();
                        }
                    }
                    break;
                case MCDefAction.MCA_KIND_ARTWORK_DL:
                    callback = extensionCallback.get(when);
                    // 有ればコールバックを実行
                    if (callback != null) {
                        if (hlsJacketUrl != null && hlsJacketUrl.size() > 0) {
                            getCdnJacketInfo();
                        } else {
                            hlsJacketUrl = null;
                            runExtentionIfThere(when);
                        }
                        break;
                    }
                    // term後にAPIがキャンセルされていないケースがあるので、保険でtrackListインスタンスの有無を確認
                    // nullであれば再生のシーケンスから外れている(=終了が選択された)
                    if (mTrackDownloadList != null && !isTerminating) {
                        downloadNextIfNeeded();
                    }
                    break;
                case MCDefAction.MCA_KIND_RATING:
                    /** 暫定対応、あとでsynchronized化すること **/
                    while (doPlaying) {
                        Thread.yield();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                    /** 暫定対応、あとでsynchronized化すること **/

                    // term後にAPIがキャンセルされていないケースがあるので、保険でtrackListインスタンスの有無を確認
                    // nullであれば再生のシーケンスから外れている(=終了が選択された)
                    if (mTrackDownloadList == null)
                        break;

                    // 直前に評価した楽曲情報を未送信DBから削除
                    if (lastRatingInfo != null) {
                        deleteUnsentRating(lastRatingInfo);
                        lastRatingInfo = null;
                    }

                    // 終了フラグが立っている場合
                    if (isTerminating) {
                        // 楽曲情報を全て破棄
                        clearMusicData(true);
                        try {
                            // APIのキャンセル
                            mMCHandler.cancelAPIs();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // 終了完了通知の発行
                        notifyToForm(Consts.MUSIC_TERMINATION, Consts.STATUS_OK, null);
                        // 以降の処理は行わない
                        break;
                    }
                    // レーティング中フラグを落とす
                    setRating(false);

                    if (checkPaymentStatus(when, result)) {
                        break;
                    }
//                    if (checkFeature(when, result)) {
//                        break;
//                    }
                    if (!checkPermission(when, result, false)) {
                        // レーティングの結果による次のプレイリストを取得
                        playList = result.getList(IMCResultInfo.MC_LIST_KIND_TRACK);
                        // 得られたプレイリストのサイズが1以上の場合
                        if (playList != null && playList.getSize() > 0) {
                            // スキップ算回数の取得
                            String skipRemain = result.getString(MCDefResult.MCR_KIND_SKIPREMAINING);
                            skipRemaining = Integer.parseInt(skipRemain);
                            // 得られたプレイリストをトラックリストに追加
                            mTrackDownloadList.addAll((MCTrackItemList) playList);
                            result.clear();
                            // 未送信レーティングがある場合、楽曲のDLより先にそれらを送信する
                        }

                        // 未送信レーティングがある場合
                        FROUnsentRatingDB unsentDb = (FROUnsentRatingDB) FROUnsentRatingDBFactory
                                .getInstance(getApplicationContext());
                        int downloadedTrackSize = ((MusicClientHandler) mMCHandler).mdbGetDownloadedTrackSize();
                        if (unsentDb.getSize() > 0) {
                            sendRatingFormDb(true);
                            break;
                        } else {
                            if (!isDownloading && mTrackDownloadList != null && mTrackDownloadList.size() > 0
                                    && downloadedTrackSize < 3) {
                                getCdnMusicInfo();
                            }
                        }
                    }

                    // ダウンロードもレーティングも行っていない状態でダウンロード待ち状態
                    if (isWaittingForDL && !isDownloading && !isRating) {
                        isWaittingForDL = false;
                        getPlayList(ChannelInfo.mode, Integer.parseInt(ChannelInfo.channelId), ChannelInfo.range, 3,
                                ChannelInfo.permission);
                    }
                    break;
                case MCDefAction.MCA_KIND_RATING_OFFLINE:
                    if (resendFlag) {
                        resendFlag = false;
                        RatingInfo info = resendList.remove(0);
                        deleteUnsentRating(info);
                        if (resendList.size() > 0) {
                            resendRatingNoResponse();
                        } else {
                            resendList.clear();
                            resendList = null;

                            // TODO 暫定
                            String onairOffline = MCUserInfoPreference.getInstance(getApplicationContext())
                                    .getOnairOffline();
                            if (!TextUtils.isEmpty(onairOffline)) {
                                callapiOnairOffline();
                            } else {
                                notifyToForm(Consts.CHECK_UNSENT_DATA, Consts.STATUS_OK, null);
                            }
                        }
                    } else {
                        if (lastRatingInfo != null) {
                            deleteUnsentRating(lastRatingInfo);
                            lastRatingInfo = null;
                        }
                        if (!runExtentionIfThere(when)) {
                            clearMusicData(true);
                            notifyToForm(Consts.MUSIC_TERMINATION, Consts.STATUS_OK, null);
                        }
                    }
                    break;
                case MCDefAction.MCA_KIND_STREAM_PLAY:
                    msg = result.getString(MCDefResult.MCR_KIND_MESSAGE);
                    if (Consts.HTTP_MSG_OK.equals(msg)) {
                        if (checkPaymentStatus(when, result)) {
                            break;
                        }
                        if (checkFeature(when, result)) {
                            break;
                        }
                        nowplayingStream = (MCStreamItem) result.getItem(MCDefResult.MCR_KIND_PLAYING_LIST);
                        String jacketUrl = nowplayingStream.getString(MCDefResult.MCR_KIND_TRACKITEM_JACKET_ID);
                        if (!TextUtils.isEmpty(jacketUrl)) {
                            extensionCallback.put(MCDefAction.MCA_KIND_ARTWORK_DL, new Runnable() {
                                @Override
                                public void run() {
                                    playSimul(nowplayingStream.getString(MCDefResult.MCR_KIND_CONTENT_LINK));
                                }
                            });
                            List<String> urls = nowplayingStream.getList(MCDefResult.MCR_KIND_SHOW_JACKET);
                            hlsJacketUrl = new ArrayList<String>();
                            hlsJacketUrl.add(jacketUrl);
                            if (urls != null)
                                hlsJacketUrl.addAll(urls);
                            getCdnJacketInfo();
                        } else {
                            playSimul(nowplayingStream.getString(MCDefResult.MCR_KIND_CONTENT_LINK));
                        }
                    } else {
                        if (checkFeature(when, result)) {
                            break;
                        }
                        kind = MCDefResult.getMsgKind(msg);
                        switch (kind) {
                            case MCDefResult.MCR_KIND_MSG_ERROR_STATUS:
                            case MCDefResult.MCR_KIND_MSG_ERROR_NETWORK:
                            case MCDefResult.MCR_KIND_MSG_ERROR_UNKNOWN:
                                setOfflineMode(NetworkStateChecker.ERROR_CAUSES_CDN_RETRY_OVER, FROPlayer.PLAYER_TYPE_SIMUL);
                                break;
                            default:
                                notifyToForm(when, kind, null);
                        }
                    }
                    break;
                case MCDefAction.MCA_KIND_NETWORK_RECOVERY:
                    if (recoveryAction != null) {
                        stopEmergency();
                        notifyToForm(MCDefAction.MCA_KIND_LOGIN, Consts.STATUS_OK,
                                new FROUserData(getApplicationContext()));
                        break;
                    }
                    clearMusicData();
                    if (mStateCheckThread != null) {
                        if (mStateCheckThread.getPlayerType() == FROPlayer.PLAYER_TYPE_NOMAL) {
                            getPlayList(ChannelInfo.mode, Integer.parseInt(ChannelInfo.channelId), ChannelInfo.range, 3,
                                    ChannelInfo.permission);
                        } else if (mStateCheckThread.getPlayerType() == FROPlayer.PLAYER_TYPE_SIMUL) {
                            getStreamPlaylist(ChannelInfo.streamId);
                        } else if (mStateCheckThread.getPlayerType() == FROPlayer.PLAYER_TYPE_LOCAL) {
                            playLocal(localPath);
                        }
                        mStateCheckThread.release();
                    }
                    mStateCheckThread = null;
                    offlineTrackIndex = 0;
                    offlineTracklist = null;
                    break;
                case MCDefAction.MCA_KIND_STREAM_LOGGING:
                    if (checkPaymentStatus(when, result)) {
                        break;
                    }
                    if (checkFeature(when, result)) {
                        break;
                    }
                    runExtentionIfThere(when);
                    break;
                case MCDefAction.MCA_KIND_PATTERN_SCHEDULE:
                    if (mPatternScheduleDateList == null || mPatternScheduleDateList.size() < 1)
                        break;

                    String scheduleDate = mPatternScheduleDateList.remove(0);
                    String dateOfToday = Utils.getNowDateString("yyyyMMdd");
                    MCScheduleItem schedule = (MCScheduleItem) result.getItem(MCDefResult.MCR_KIND_PATTERN_RESPONSE);
                    FROPatternScheduleDB scheduleDb = FROPatternScheduleDBFactory.getInstance(getApplicationContext());
                    contentDb = FROPatternContentDBFactory.getInstance(getApplicationContext());
                    // どの店舗にも所属していないケース
                    if (schedule == null || TextUtils.isEmpty(schedule.getString(MCDefResult.MCR_KIND_SCHEDULE_RULE))) {
                        // 24H後に再度チェックを行うようにタイマーをセットし、スケジュール DB、割り込み DB を削除する
                        Calendar current = Calendar.getInstance();
                        current.add(Calendar.HOUR_OF_DAY, 24);
                        TimerHelper.setInterruptScheduleTimer(getApplicationContext(), current);
                        TimerHelper.cancelTimer(getApplicationContext(), TimerHelper.PENDING_INTENT_INTERRUPT_MUSIC_TIMER);
                        scheduleDb.deleteAll();
                        contentDb.deleteAll();
                        // 未所属の場合もスケジュール DB に登録する
                        schedule = new MCScheduleItem();
                        schedule.setTargetDate(scheduleDate);
                        schedule.setLastUpdate(Long.toString(Utils.getNowTime()));
                        schedule.setUpdateStatus(Consts.PATTERN_UPDATE_STATUS_NOT_BLONG);
                        scheduleDb.insert(schedule);
                        // update は必ず呼び出す
                        updateInterruptSchedule(scheduleDate, null);
                        notifyToForm(when, Consts.STATUS_OK, null);
                    }
                    // いずれかの店舗に所属しているケース
                    else {
                        // scheduleMask = 1(禁止)の場合、割り込みの設定を強制的に有効にする
                        String scheduleMask = schedule.getString(MCDefResult.MCR_KIND_SCHEDULE_MASK);
                        if (scheduleMask != null && scheduleMask.equals("1"))
                            MainPreference.getInstance(getApplicationContext()).setInterruptTimerEnable(true);

                        schedule.setTargetDate(scheduleDate);
                        mCurrentScheduleItem = schedule.clone(); // pattern/download呼び出し後に使用するためクローンを保持

                        // 更新スケジュールの DB を更新する
                        String patternDigest = schedule.getString(MCDefResult.MCR_KIND_PATTERN_DIGEST);
                        String patternId = schedule.getString(MCDefResult.MCR_KIND_PATTERN_ID);
                        MCScheduleItem local = scheduleDb.findByDate(scheduleDate);
                        String localDigest = (local != null) ? local.getString(MCDefResult.MCR_KIND_PATTERN_DIGEST) : null;
                        String localPatternId = (local != null) ? local.getString(MCDefResult.MCR_KIND_PATTERN_ID) : null;
                        schedule.setString(MCDefResult.MCR_KIND_PATTERN_DIGEST, localDigest); // この時点で
                        // digest
                        // の更新は行わない
                        schedule.setLastUpdate(Long.toString(Utils.getNowTime()));
                        schedule.setUpdateStatus(Consts.PATTERN_UPDATE_STATUS_OK);
                        // ローカルにその日のスケジュールが無ければ、取得したスケジュールを挿入
                        if (local == null) {
                            scheduleDb.insert(schedule);
                        }
                        // ローカルに既にその日のスケジュールがあれば、取得したスケジュールでアップデート
                        else
                            scheduleDb.update(schedule);

                        FRODebug.logD(getClass(), "api digest = " + patternDigest, DEBUG);
                        FRODebug.logD(getClass(), "local digest = " + localDigest, DEBUG);

                        boolean digestIsSame = false;
                        // 指定日の schedulePattern が設定されていない場合
                        if (TextUtils.isEmpty(patternDigest) || TextUtils.isEmpty(patternId)) {
                            // API の内容が今日の日付であったら、割り込み再生のタイマーをキャンセルする
                            if (scheduleDate.equals(dateOfToday))
                                TimerHelper.cancelTimer(getApplicationContext(),
                                        TimerHelper.PENDING_INTENT_INTERRUPT_MUSIC_TIMER);
                        }
                        // 指定日の schedulePattern が設定されている場合
                        else {
                            digestIsSame = !TextUtils.isEmpty(localDigest) && localDigest.equals(patternDigest);
                            // digest がローカルの内容と不一致の場合、pattern/content を呼び出す
                            if (!digestIsSame) {
                                // 指定日の更新前のパターン ID が他で使用されていなければ、コンテンツ DB
                                // の該当するデータを消しておく
                                if (!TextUtils.isEmpty(localPatternId)
                                        && scheduleDb.findByPatternId(localPatternId) == null) {
                                    contentDb.deletebyPatternId(localPatternId);
                                }
                                getInterruptContents(patternId);
                            }
                            // digest がローカルの内容と一致している場合、過去に失敗したコンテンツが無いか確認を行う
                            else {
                                InterruptFileDownloader checkFailed = InterruptFileDownloader
                                        .getInstance(getApplicationContext());
                                FRODebug.logD(getClass(), "checkFailed = " + checkFailed, DEBUG);
                                if (checkFailed != null)
                                    setDownloader("FAILED", checkFailed);
                            }

                            if (!TimerHelper.isSetPending(getApplicationContext(),
                                    TimerHelper.PENDING_INTENT_INTERRUPT_MUSIC_TIMER))
                                refreshInterruptMusicTimer(getApplicationContext(), true);
                        }
                        // update は必ず呼び出す
                        updateInterruptSchedule(scheduleDate, patternId);

                        Calendar nextSchedule = TimerHelper.setInterruptScheduleTimer(getApplicationContext());
                        String nextScheduleDate = Utils.getDateString(nextSchedule, "yyyyMMdd");
                        // API の内容が今日の日付であり、かつ次の更新時間が今日でない場合、翌日の更新スケジュールを取得する
                        if (scheduleDate.equals(dateOfToday) && !nextScheduleDate.equals(dateOfToday)) {
                            getInterruptSchedule(nextScheduleDate);
                            break;
                        }
                        notifyToForm(when, Consts.STATUS_OK, null);
                    }
                    break;
                case MCDefAction.MCA_KIND_PATTERN_DOWNLOAD:
                    MCFrameItemList contentList = (MCFrameItemList) result.getList(IMCResultInfo.MC_LIST_KIND_FRAME); // 取得したコンテンツのリスト
                    contentDb = FROPatternContentDBFactory.getInstance(getApplicationContext()); // コンテンツDB
                    FROPatternScheduleDB scheduleDb2 = FROPatternScheduleDBFactory.getInstance(getApplicationContext()); // スケジュールDB
                    MCScheduleItem local = scheduleDb2.findByDate(mCurrentScheduleItem.getTargetDate()); // 当日のスケジュール
                    String dateOfToday2 = Utils.getNowDateString("yyyyMMdd");

                    // 割り込みパターンが空の場合
                    if (contentList == null || contentList.size() < 1) {
                        // API の内容が今日の日付であったら、割り込み再生のタイマーをキャンセルする
                        if (dateOfToday2.equals(mCurrentScheduleItem.getTargetDate()))
                            TimerHelper.cancelTimer(getApplicationContext(),
                                    TimerHelper.PENDING_INTENT_INTERRUPT_MUSIC_TIMER);
                        // 次回のスケジュール更新時にコンテンツの更新も行ってほしいので、digest の値を null にする
                        if (local != null) {
                            local.setString(MCDefResult.MCR_KIND_PATTERN_DIGEST, null);
                            scheduleDb2.update(local);
                        }
                    }
                    // 割り込みパターンが空でない場合
                    else {
                        ArrayList<MCAudioItem> downloadList = new ArrayList<MCAudioItem>();
                        // id と targetDate は XML からは取得しないので自前でセットする
                        // また次の処理のために audio タグ以下のデータをリストとして用意する
                        for (MCFrameItem mcfi : contentList) {
                            mcfi.setmPatternId(mCurrentScheduleItem.getString(MCDefResult.MCR_KIND_PATTERN_ID));
                            downloadList.addAll(mcfi.getItem(MCDefResult.MCR_KIND_PATTERN_AUDIO));
                        }
                        // 既にローカルにダウンロード済みでないかチェックする
                        InterruptFileDownloader downloader = InterruptFileDownloader.getInstance(getApplicationContext(),
                                downloadList);
                        FRODebug.logD(getClass(), "downloader = " + downloader, DEBUG);
                        if (downloader != null)
                            setDownloader(mCurrentScheduleItem.getString(MCDefResult.MCR_KIND_PATTERN_ID), downloader);
                        // コンテンツダウンロード後に DB を更新し、タイマーをセットする
                        contentDb.deletebyPatternId(mCurrentScheduleItem.getString(MCDefResult.MCR_KIND_PATTERN_ID));
                        contentDb.insertAll(contentList);
                        refreshInterruptMusicTimer(getApplicationContext(), true);
                        // コンテンツの取得に成功したため、digest を更新しておく
                        if (local != null) {
                            local.setString(MCDefResult.MCR_KIND_PATTERN_DIGEST,
                                    mCurrentScheduleItem.getString(MCDefResult.MCR_KIND_PATTERN_DIGEST));
                            scheduleDb2.update(local);
                        }
                        // 不要になったコンテンツ情報を削除する
                        List<MCScheduleItem> scheduleList = scheduleDb2.findAll();
                        ArrayList<String> usingPatterns = new ArrayList<String>();
                        if (scheduleList.size() > 0) {
                            for (MCScheduleItem scheduleItem : scheduleList) {
                                String pattenrId = scheduleItem.getString(MCDefResult.MCR_KIND_PATTERN_ID);
                                if (!TextUtils.isEmpty(pattenrId) && !usingPatterns.contains(pattenrId))
                                    usingPatterns.add(scheduleItem.getString(MCDefResult.MCR_KIND_PATTERN_ID));
                            }
                        }
                        contentDb.deleteUnusingPatters(usingPatterns);
                    }
                    mCurrentScheduleItem = null;

                    notifyToForm(when, Consts.STATUS_OK, null);
                    break;
                case MCDefAction.MCA_KIND_PATTERN_ONAIR:
                    MCUserInfoPreference.getInstance(getApplicationContext()).setOnairOffline(null, null);
                    break;
                case MCDefAction.MCA_KIND_PATTERN_ONAIR_OFFLINE:
                    MCUserInfoPreference.getInstance(getApplicationContext()).setOnairOffline(null, null);
                    notifyToForm(Consts.CHECK_UNSENT_DATA, Consts.STATUS_OK, null);
                    break;
                // 成否にかかわらず何もしない
                case MCDefAction.MCA_KIND_PATTERN_UPDATE:
                case MCDefAction.MCA_KIND_LICENSE_INSTALL:
                case MCDefAction.MCA_KIND_LICENSE_TRACKING:
                    break;
            }
            checkTask(when);
        }
    }

    private static void refreshInterruptMusicTimer(Context context, boolean includeCurrent) {
        Calendar current = Calendar.getInstance();
        if (!includeCurrent)
            current.add(Calendar.MINUTE, 1);
        if (TimerHelper.setInterruptMusicTimer(context, current) == null) {
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DAY_OF_MONTH, 1);
            tomorrow.set(Calendar.HOUR_OF_DAY, 0);
            tomorrow.set(Calendar.MINUTE, 0);
            TimerHelper.setInterruptMusicTimer(context, tomorrow);
        }
    }

    private HashMap<String, InterruptFileDownloader> mInterruptDownloaderMap;

    public void setDownloader(String key, InterruptFileDownloader downloader) {
        if (mInterruptDownloaderMap == null)
            mInterruptDownloaderMap = new HashMap<String, InterruptFileDownloader>();
        if (!mInterruptDownloaderMap.containsKey(key)) {
            mInterruptDownloaderMap.put(key, downloader);
            downloader.start(key, new IDownloaderListener() {
                @Override
                public void onFinish(String key) {
                    if (mInterruptDownloaderMap != null && key != null) {
                        mInterruptDownloaderMap.remove(key);
                    }
                }
            });
        }
    }

    private void downloadNextIfNeeded() {
        if (isWaittingForDL) {
            setWaittingForDL(false);
            playMusic();
        }
        if (mTrackDownloadList != null && mTrackDownloadList.size() > 0) {
            // 楽曲のDL予定キューからDLし終わったデータを削除
            mTrackDownloadList.remove(0);
            int downloadedTrackSize = ((MusicClientHandler) mMCHandler).mdbGetDownloadedTrackSize();
            // 予定キューが空でなければ再びDLしに行く
            if (mTrackDownloadList.size() > 0 && downloadedTrackSize < 3) {
                getCdnMusicInfo(true);
            } else {
                setDownloading(false);
            }
        }
    }

    private void checkTask(int when) {
        synchronized (ApiTaskManager.QUEUE_LOCK) {
            ApiTask nextTask;
            ApiTask task = apiManager.poll(when);
            if (task == null) {
                FRODebug.logE(getClass(), "task is already removed", true);
            } else {
                task.clear();
            }
            nextTask = apiManager.hasHighPriorityTask(when);
            if (nextTask != null)
                apiManager.setPeekTask(nextTask);
            else
                nextTask = apiManager.peek();

            if (nextTask != null) {
                if (nextTask.getProcessing() != null) {
                    nextTask.getProcessing().run();
                    return;
                }
                MCInit();
                IMCPostActionParam params = nextTask.getParams();
                if (params instanceof MCPostActionParam) {
                    ((MCPostActionParam) mMCParam).copyParam(params);
                } else {
                    cdnInfo = ((MCCdnMusicItem) params).copy();
                }
                if (nextTask.getAction() == MCDefAction.MCA_KIND_ARTWORK_DL)
                    mMCHandler.actDoAction(nextTask.getAction(), cdnInfo.copy());
                else if (nextTask.getAction() != MCDefAction.MCA_KIND_TRACK_DL)
                    mMCHandler.actDoAction(nextTask.getAction(), mMCParam);
                else
                    mMCHandler.actDoActionWithListener(nextTask.getAction(), cdnInfo.copy(), this);
            } else {
            }
        }
    }

    /**
     * MusicClientからのエラー通知
     *
     * @param when      どの要求に対する通知か
     * @param errorCode エラーコード
     */
    @Override
    public void onNotifyMCError(int when, int errorCode) {
        synchronized (mMCResultLock) {
            if (apiManager.isTerminate)
                return;

            FRODebug.logD(getClass(), "onNotifyMCError", DEBUG);
            FRODebug.logD(getClass(), "when = " + MCDefAction.getApi(when), DEBUG);
            FRODebug.logD(getClass(), "error code = " + errorCode, DEBUG);

            // APIキャンセルの場合はFORMに通知しない
            if (errorCode == MCError.MC_APPERR_CANCEL)
                return;

            IMCItem trackData = null;

            switch (when) {
                case MCDefAction.MCA_KIND_ARTWORK_DL:
                    // 画像はネットワークエラー、もしくはリソース無しで取得できなかった場合でも、エラーとして処理しない
                    // この処理は例外的で、onNotifyMCError メソッドを抜ける際にタスクのチェックを行わない
                    // 代わりに onNotifyMCResult の呼び出しを行い、その中でタスクのチェックを行う
                    if (errorCode == MCError.MC_UNAUTHORIZED || errorCode == MCError.MC_FORBIDDEN) {
                        notifyToForm(when, errorCode, null);
                    } else {
                        onNotifyMCResult(when, null);
                        return;
                    }
                    break;
                case MCDefAction.MCA_KIND_LISTEN:
                    if (MCError.isServerError(when, errorCode) && isWaittingForDL) {
                        setOfflineMode(NetworkStateChecker.ERROR_CAUSES_LISTEN_RETRY_OVER, FROPlayer.PLAYER_TYPE_NOMAL);
                    } else {
                        notifyToForm(when, errorCode, null);
                    }
                    break;
                case MCDefAction.MCA_KIND_RATING:
                    if (MCError.isServerError(when, errorCode)) {
                        // 楽曲ストックが枯渇しており、かつトラック取得系の API(radio/rating, download/cdn, GET CDN Contents)が
                        // 自身以外登録されていない場合、再生の継続が不可であるためオフラインモードへ
                        if (isWaittingForDL && apiManager.getTrackTaskSize() <= 1) {
                            setOfflineMode(NetworkStateChecker.ERROR_CAUSES_RATING_RETRY_OVER, FROPlayer.PLAYER_TYPE_NOMAL);
                        } else {
                            break;
                        }
                    } else {
                        lastRatingInfo = null;
                        clearMusicData(true);
                        notifyToForm(when, errorCode, null);
                    }
                    break;
                case MCDefAction.MCA_KIND_RATING_OFFLINE:
                    if (resendFlag) {
                        resendFlag = false;
                        notifyToForm(Consts.CHECK_UNSENT_DATA, errorCode, null);
                    } else {
                        lastRatingInfo = null;
                        if (!runExtentionIfThere(when)) {
                        clearMusicData(true);
                            notifyToForm(when, errorCode, null);
                        }
                    }
                    break;
                // CDNデータの取得に失敗した場合
                case MCDefAction.MCA_KIND_DOWNLOAD_CDN:
                    // DL中フラグを落とす
                    setDownloading(false);
                    // DLキューから失敗したトラックの情報を削除
                    if (mTrackDownloadList != null && mTrackDownloadList.size() > 0)
                        trackData = mTrackDownloadList.remove(0);
                    if (MCError.isServerError(when, errorCode)) {
                        // 楽曲ストックが枯渇しており、かつトラック取得系の API(radio/rating, download/cdn, GET CDN Contents)が
                        // 自身以外登録されていない場合、再生の継続が不可であるためオフラインモードへ
                        if (isWaittingForDL && apiManager.getTrackTaskSize() <= 1) {
                            setOfflineMode(NetworkStateChecker.ERROR_CAUSES_CDN_RETRY_OVER, FROPlayer.PLAYER_TYPE_NOMAL);
                        } else {
                            break;
                        }
                    }
                    // サーバエラー or ネットワークエラーの場合、エラー理由を添えて radio/rating を呼び出す
                    else if (errorCode == MCError.MC_NOT_FOUND) {
                        errorRatingAndPlay(when, errorCode, trackData);
                        // それ以外の場合は通常のエラー処理を行う
                        // } else if (errorCode == MCError.MC_APPERR_IO_HTTP &&
                        // !isWaittingForDL && !isTerminating) {
                        // // ネットワークエラー、楽曲ストックがある、終了フラグが立っていない場合、
                        // // ratingとtrackのIO_HTTPエラーは無視する
                        // break;
                    } else
                        notifyToForm(when, errorCode, null);
                    break;
                case MCDefAction.MCA_KIND_TRACK_DL:
                    // DL中フラグを落とす
                    setDownloading(false);
                    // DLキューから失敗したトラックの情報を削除
                    if (mTrackDownloadList != null && mTrackDownloadList.size() > 0)
                        trackData = mTrackDownloadList.remove(0);
                    if (MCError.isServerError(when, errorCode)) {
                        // 楽曲ストックが枯渇しており、かつトラック取得系の API(radio/rating, download/cdn, GET CDN Contents)が
                        // 自身以外登録されていない場合、再生の継続が不可であるためオフラインモードへ
                        if (isWaittingForDL && apiManager.getTrackTaskSize() <= 1) {
                            setOfflineMode(NetworkStateChecker.ERROR_CAUSES_TRACK_RETRY_OVER, FROPlayer.PLAYER_TYPE_NOMAL);
                        } else {
                            break;
                        }
                    }
                    // サーバエラー or ネットワークエラーの場合、エラー理由を添えて radio/rating を呼び出す
                    else if (errorCode == MCError.MC_FORBIDDEN || errorCode == MCError.MC_NOT_FOUND) {
                        errorRatingAndPlay(when, errorCode, trackData);
                        // それ以外の場合は通常のエラー処理を行う
                    } else {
                        notifyToForm(when, errorCode, null);
                    }
                    break;
                case MCDefAction.MCA_KIND_STREAM_PLAY:
                    if (MCError.isServerError(when, errorCode)) {
                        setOfflineMode(NetworkStateChecker.ERROR_CAUSES_TRACK_RETRY_OVER, FROPlayer.PLAYER_TYPE_SIMUL);
                    } else {
                        notifyToForm(when, errorCode, null);
                    }
                    break;
                case MCDefAction.MCA_KIND_NETWORK_RECOVERY:
                    if (recoveryAction != null) {
                        recoveryAction = null;
                        if (MCError.isEmergencyError(when, errorCode)) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    startEmergency(Consts.EMERGENCY_CAUSE_LOGIN);
                                }
                            }).start();
                            break;
                        }
                    }
                    if (MCError.isServerError(when, errorCode)) {
                        String errorCouse = mStateCheckThread.getCause();
                        int playerType = mStateCheckThread.getPlayerType();
                        mStateCheckThread.release();
                        mStateCheckThread = null;
                        setOfflineMode(errorCouse, playerType);
                    } else {
                        mStateCheckThread.release();
                        mStateCheckThread = null;
                        notifyToForm(when, errorCode, null);
                        termMusic(0);
                    }
                    break;
                case MCDefAction.MCA_KIND_STREAM_LOGGING:
                    runExtentionIfThere(when);
                    if (errorCode == MCError.MC_FORBIDDEN) {
                        notifyToForm(when, errorCode, null);
                    }
                case MCDefAction.MCA_KIND_PATTERN_ONAIR_OFFLINE:
                    notifyToForm(Consts.CHECK_UNSENT_DATA, Consts.STATUS_OK, null);
                    break;
                case MCDefAction.MCA_KIND_LICENSE_STATUS:
                    TimerHelper.setLicenseIntent(getApplicationContext());
                    notifyToForm(when, Consts.STATUS_OK, null);
                    break;
                // 成否にかかわらず完了通知を送る
                case MCDefAction.MCA_KIND_PATTERN_SCHEDULE:
                    if (mPatternScheduleDateList == null || mPatternScheduleDateList.size() < 1) {
                        notifyToForm(when, Consts.STATUS_OK, null);
                        break;
                    }

                    String scheduleDate = mPatternScheduleDateList.remove(0);
                    // エラーの場合は30分後にもう一度確認を行う
                    Calendar current = Calendar.getInstance();
                    //current.add(Calendar.MINUTE, 30);
                    current.add(Calendar.MINUTE, 5);
                    TimerHelper.setInterruptScheduleTimer(getApplicationContext(), current);
                    FROPatternScheduleDB scheduleDb = FROPatternScheduleDBFactory.getInstance(getApplicationContext());
                    MCScheduleItem local = scheduleDb.findByDate(scheduleDate);
                    if (local != null) {
                        local.setLastUpdate(Long.toString(Utils.getNowTime()));
                        String lastStatus = local.getUpdateStatus();
                        if (Consts.PATTERN_UPDATE_STATUS_OK.equals(lastStatus)) {
                            local.setUpdateStatus(Consts.PATTERN_UPDATE_STATUS_NG);
                        }
                        scheduleDb.update(local);
                    } else {
                        local = new MCScheduleItem();
                        local.setTargetDate(scheduleDate);
                        local.setLastUpdate(Long.toString(Utils.getNowTime()));
                        local.setUpdateStatus(Consts.PATTERN_UPDATE_STATUS_ALL_NG);
                        scheduleDb.insert(local);
                    }
                    // 翌日の割り込みデータも更新する
                    Calendar calendar = Calendar.getInstance();
                    if (scheduleDate.equals(Utils.getDateString(calendar, "yyyyMMdd"))) {
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        MCScheduleItem tomorrow = scheduleDb.findByDate(Utils.getDateString(calendar, "yyyyMMdd"));
                        if (tomorrow != null) {
                            tomorrow.setLastUpdate(Long.toString(Utils.getNowTime()));
                            String lastStatus = tomorrow.getUpdateStatus();
                            if (Consts.PATTERN_UPDATE_STATUS_OK.equals(lastStatus)) {
                                tomorrow.setUpdateStatus(Consts.PATTERN_UPDATE_STATUS_NG);
                            }
                            scheduleDb.update(tomorrow);
                        }
                    }
                    notifyToForm(when, Consts.STATUS_OK, null);
                    break;
                case MCDefAction.MCA_KIND_PATTERN_DOWNLOAD:
                    FROPatternScheduleDB scheduleDb2 = FROPatternScheduleDBFactory.getInstance(getApplicationContext());
                    MCScheduleItem local2 = scheduleDb2.findByDate(mCurrentScheduleItem.getTargetDate());
                    if (local2 != null) {
                        local2.setUpdateStatus(Consts.PATTERN_UPDATE_STATUS_NG);
                        scheduleDb2.update(local2);
                    }
                    mCurrentScheduleItem = null;
                    notifyToForm(when, Consts.STATUS_OK, null);
                    break;
                case MCDefAction.MCA_KIND_TEMPLATE_LIST:
                    if (isAutoTimerRenewal) {
                        TimerHelper.setTemplateTimer(getApplicationContext());
                        isAutoTimerRenewal = false;
                    } else {
                        notifyToForm(when, errorCode, null);
                    }
                    break;
                case MCDefAction.MCA_KIND_TEMPLATE_DOWNLOAD:
                    if (isAutoTimerRenewal) {
                        if (errorCode == MCError.MC_NOT_FOUND) {
                            FROTimerAutoDBFactory.getInstance(getApplicationContext()).deleteAll();
                            updateTemplateState(null);
                            notifyToForm(Consts.NOTIFY_RENEWAL_TIMER_IS_CHANGED, Consts.STATUS_OK, null);
                        }
                        FROTemplateTimerAutoListDBFactory.getInstance(getApplicationContext()).clearDigest();
                        // TimerHelper.setTemplateTimer(getApplicationContext());
                        isAutoTimerRenewal = false;
                    } else {
                        notifyToForm(when, errorCode, null);
                    }
                    break;
                // エラー時でも何もしない
                case MCDefAction.MCA_KIND_LICENSE_TRACKING:
                case MCDefAction.MCA_KIND_LICENSE_INSTALL:
                case MCDefAction.MCA_KIND_AD_DL:
                case MCDefAction.MCA_KIND_PATTERN_ONAIR:
                    break;
                case MCDefAction.MCA_KIND_LOGIN:
                    if (MCError.isEmergencyError(when, errorCode) && !TextUtils
                            .isEmpty(MCUserInfoPreference.getInstance(getApplicationContext()).getSessionKey())) {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                startEmergency(Consts.EMERGENCY_CAUSE_LOGIN);
                                notifyToForm(MCDefAction.MCA_KIND_LOGIN, Consts.NOTIFY_BEGIN_EMERGENCY_MODE, null);
                            }

                        }).start();
                    } else {
                        notifyToForm(when, errorCode, null);
                    }
                    break;
                // TODO 旧 API 暫定
                case MCDefAction.MCA_KIND_STATUS:
                    if (MCError.isEmergencyError(when, errorCode) && statusCheckOnBoot && !TextUtils
                            .isEmpty(MCUserInfoPreference.getInstance(getApplicationContext()).getSessionKey())) {
                        statusCheckOnBoot = false;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                startEmergency(Consts.EMERGENCY_CAUSE_LOGIN);
                                notifyToForm(MCDefAction.MCA_KIND_LOGIN, Consts.NOTIFY_BEGIN_EMERGENCY_MODE, null);
                            }
                        }).start();
                    } else {
                        notifyToForm(when, errorCode, null);
                    }
                    break;
                default:
                    notifyToForm(when, errorCode, null);
            }
            checkTask(when);
        }
    }

    /**
     * Formへの通知
     *
     * @param when       どの要求に対する通知か
     * @param statusCode 成否
     * @param obj        要求に対する結果
     */
    private void notifyToForm(int when, int statusCode, Object obj) {
        synchronized (mFormLock) {
            int numListeners;

            numListeners = listeners.beginBroadcast();
            switch (when) {
                // リスト取得通知
                case Consts.ID_LIST:
                case Consts.LOCK_LIST:
                case Consts.CHANNEL_LIST:
                case MCDefAction.MCA_KIND_LIST:
                case MCDefAction.MCA_KIND_SEARCH:
                case MCDefAction.MCA_KIND_FEATURED:
                    // case MCDefAction.MCA_KIND_STATUS:
                case MCDefAction.MCA_KIND_MSG_DL:
                case MCDefAction.MCA_KIND_PAYMENT_SUB:
                case MCDefAction.MCA_KIND_PAYMENT_ITEM:
                case Consts.UPDATE_MODE_INFO:
                    for (int i = 0; i < numListeners; i++) {
                        try {
                            listeners.getBroadcastItem(i).onNotifyChannelList(when, statusCode, (ListDataInfo) obj);
                        } catch (RemoteException e) {
                            Log.e(getClass().getSimpleName(), "RemoteException", e);
                        }
                    }
                    break;

                case MCDefAction.MCA_KIND_STATUS:
                case MCDefAction.MCA_KIND_LOGIN:
                    for (int i = 0; i < numListeners; i++) {
                        try {
                            listeners.getBroadcastItem(i).onNotifyUserData(when, statusCode, (FROUserData) obj);
                        } catch (RemoteException e) {
                            Log.e(getClass().getSimpleName(), e.toString(), e);
                        }
                    }
                    break;

                // 楽曲再生開始通知
                case Consts.START_MUSIC:
                case Consts.START_LOCAL_MUSIC:
                case Consts.START_STREAM_MUSIC:
                case Consts.START_OFFLINE_MUSIC:
                case Consts.UPDATE_MUSIC_INFO:
                    // case Consts.UPDATE_MUSIC_INFO_LOCAL:
                    // case Consts.UPDATE_MUSIC_INFO_SIMUL:
                    // case Consts.START_INTERRUPT:
                    for (int i = 0; i < numListeners; i++) {
                        try {
                            listeners.getBroadcastItem(i).onNotifyMusicData(when, statusCode, (MusicInfo) obj);
                        } catch (RemoteException e) {
                            Log.e(getClass().getSimpleName(), "RemoteException", e);
                        }
                    }
                    break;

                case MCDefAction.MCA_KIND_CHANNEL_SHARE:
                case MCDefAction.MCA_KIND_CHANNEL_EXPAND:
                case Consts.NOTIFY_DOWNLOAD_PROGRESS:
                case Consts.UPDATE_SESSION_INFO:
                case Consts.NOTIFY_SKIP_REMAINING:
                case MCDefAction.MCA_KIND_FACEBOOK_LOOKUP:
                case Consts.NOTIFY_PLAYLIST_INFO:
                case Consts.START_INTERRUPT:
                case Consts.NOTIFY_FAILED_TO_RECOVER:
                    for (int i = 0; i < numListeners; i++) {
                        try {
                            listeners.getBroadcastItem(i).onNotifyWithString(when, statusCode, (String) obj);
                        } catch (RemoteException e) {
                            Log.e(getClass().getSimpleName(), "RemoteException", e);
                        }
                    }
                    break;

                case MCDefAction.MCA_KIND_LOCATION:
                case Consts.START_AD:
                case Consts.UPDATE_ADS_INFO:
                case Consts.CHANNEL_LIST_BENIFIT:
                case Consts.CHANNEL_LIST_CHART:
                case Consts.CHANNEL_LIST_GENRE:
                case Consts.CHANNEL_LIST_ARTIST:
                case Consts.CHANNEL_LIST_FEATURED:
                case Consts.CHANNEL_LIST_MYCHANNEL:
                case Consts.CHANNEL_LIST_BUSINESS:
                case MCDefAction.MCA_KIND_TEMPLATE_LIST:
                case MCDefAction.MCA_KIND_TEMPLATE_DOWNLOAD:
                case MCDefAction.MCA_KIND_STREAM_LIST:
                case Consts.INTERRUPT_LIST:
                    for (int i = 0; i < numListeners; i++) {
                        try {
                            listeners.getBroadcastItem(i).onNotifyMCItemList(when, statusCode, (MCListDataInfo) obj);
                        } catch (RemoteException e) {
                            Log.e(getClass().getSimpleName(), "RemoteException", e);
                        }
                    }
                    break;

                default:
                    for (int i = 0; i < numListeners; i++) {
                        try {
                            listeners.getBroadcastItem(i).onNotifyResult(when, statusCode);
                        } catch (RemoteException e) {
                            Log.e(getClass().getSimpleName(), "RemoteException", e);
                        }
                    }
                    break;
            }
            listeners.finishBroadcast();
        }

    }

    /**
     * 曲終了時の処理
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mFaraoPlayer == null)
            return;

        switch (mFaraoPlayer.getPlayerType()) {
            case FROPlayer.PLAYER_TYPE_NOMAL:
                notifyToForm(Consts.SHOW_PROGRESS, Consts.STATUS_OK, null);
                mFaraoPlayer.stop();
                if (counter != null) {
                    playTime = counter.getCount();
                    counter.reset();
                }
                if (ratingAndPlay("yes"))
                    apiManager.doTask();
                break;
            case FROPlayer.PLAYER_TYPE_LOCAL:
                skipLocal(Consts.LOCAL_PLAYER_NEXT);
                break;
            case FROPlayer.PLAYER_TYPE_SIMUL:
                // FROUtils.outputLog("MediaPlayer onCompletion");
                // 通常は通らないが、特定のデバイスだとエラーハンドリングがなされていてもここを通る
                break;
            case FROPlayer.PLAYER_TYPE_OFFLINE:
                mFaraoPlayer.stop();
                if (mStateCheckThread != null) {
                    long start = mStateCheckThread.getBeginTime();
                    long now = Utils.getNowTime();
                    if ((now - start) >= OFFLINE_PLAYING_LIMIT) {
                        notifyToForm(Consts.NOTIFY_FAILED_TO_RECOVER, Consts.STATUS_OK, null);
                        clearMusicData(true);
                        mStateCheckThread.release();
                        mStateCheckThread = null;
                        offlineTrackIndex = 0;
                        offlineTracklist = null;
                        break;
                    }

                    switch (mStateCheckThread.getNetworkState()) {
                        case NetworkStateChecker.STATE_RESTORED:
                            notifyToForm(Consts.SHOW_PROGRESS, Consts.STATUS_OK, null);
                            if (sendNetworkRecovery())
                                apiManager.doTask();
                            break;
                        case NetworkStateChecker.STATE_ERROR_OCCURRED:
                            String cause = mStateCheckThread.getCause();
                            int type = mStateCheckThread.getPlayerType();
                            mStateCheckThread.release();
                            setOfflineMode(cause, type);
                            break;
                        default:
                            playOffline();
                    }
                } else {
                    setOfflineMode(NetworkStateChecker.ERROR_CAUSES_UNKNOWN, FROPlayer.PLAYER_TYPE_NONE);
                }
                break;
        }
    }

    /**
     * バインド時の処理
     */
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private int contentLength;

    @Override
    public void onStartDownload(int size) {
        if (!isNotifyProgress)
            return;
        contentLength = size;
        notifyToForm(Consts.NOTIFY_START_DOWNLOAD, Consts.STATUS_OK, null);
    }

    @Override
    public void onProgress(int progress) {
        if (!isNotifyProgress)
            return;

        float per;
        per = ((float) progress / (float) contentLength) * 80;
        notifyToForm(Consts.NOTIFY_DOWNLOAD_PROGRESS, Consts.STATUS_OK, Float.toString(per));
    }

    LocationHelper mLocation;

    private void getLocation(int apiType) {
        mLocation = new LocationHelper(new IFROLocationListener() {
            @Override
            public void onSuccess(int type, double lat, double lon) {
                TimerHelper.setLicenseIntent(getApplicationContext());
                if (licenseTracking(type, lat, lon))
                    apiManager.doTask();
            }

            @Override
            public void onError(int type) {
                TimerHelper.setLicenseIntent(getApplicationContext());
                if (licenseTracking(type, -1, -1))
                    apiManager.doTask();
            }
        }, getApplicationContext(), apiType);
        mLocation.getLocation(0);
    }

    private boolean changePlaylist = false;
    // TODO 暫定対応、リリース時はアプリ領域に抱えること
//    private static final String CACHE_PATH = Environment.getExternalStorageDirectory().getPath() + "/FaRaoCache";
    private static final int OFFLINE_CACHE_SIZE = 20; // max 20 tracks
    public static final long OFFLINE_PLAYING_LIMIT = 60 * 60 * 72 * 1000; // 72H

    // public static final long OFFLINE_PLAYING_LIMIT = 60 * 5 * 1000; // debug

    // private static final int OFFLINE_CACHE_SIZE = 2; // debug
    // public static final long OFFLINE_PLAYING_LIMIT = 60 * 5 * 1000; // debug

    private void clearCachedMusic() {
        // 楽曲のDLが成功したタイミングでキャッシュを全て破棄する
        MCOfflineMusicInfoDB cacheDb = (MCOfflineMusicInfoDB) MCOfflineMusicInfoDBFactory
                .getInstance(getApplicationContext());
        IMCMusicItemList caches = cacheDb.findAll();
        if (caches != null && caches.getSize() > 0) {
            for (int i = 0; i < caches.getSize(); i++) {
                IMCMusicItemInfo info = caches.getInfo(i);
                // 各楽曲ソースの削除
                Utils.deleteFile(new File(info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_OUTPUT_PATH)));
            }
            // キャッシュDBのクリア
            cacheDb.deleteAll();
            // Utils.deleteFile(new File(CACHE_PATH + "/output.txt"));
        }
    }

    private void cacheMusic(String trackId) {
        IMCMusicInfoDB musicDb = MCMusicInfoDBFactory.getInstance(getApplicationContext());
        MCOfflineMusicInfoDB cacheDb = (MCOfflineMusicInfoDB) MCOfflineMusicInfoDBFactory
                .getInstance(getApplicationContext());
        if (cacheDb.getSize() > OFFLINE_CACHE_SIZE) {
            IMCMusicItemInfo oldest = cacheDb.findOldest(false);
            cacheDb.delete(oldest.getTrackItem().getString(MCDefResult.MCR_KIND_TRACKITEM_ID));
            File deleteFile = new File(oldest.getStringValue(IMCMusicItemInfo.MCDB_ITEM_OUTPUT_PATH));
            Utils.deleteFile(deleteFile);
        }
        IMCMusicItemInfo info = musicDb.find(trackId);
        if (info != null) {
            String srcPath = FROUtils.getTrackPath() + trackId;
            String dstPath = FROUtils.PATH_CACHE + "/" + trackId;
            File parent = new File(FROUtils.PATH_CACHE);
            if (!parent.exists())
                parent.mkdir();

            copyFile(srcPath, dstPath);
            info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_OUTPUT_PATH, dstPath);
            cacheDb.insert(info);
        }
    }

    private void copyFile(String src, String dst) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(src);
            fos = new FileOutputStream(dst);
            int length = -1;
            byte[] buffer = new byte[2048];
            while ((length = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
            fos.flush();
            fis.close();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // ユーザ固有のデータを削除する
    private void deleteUserSpecificData() {
        // TimerHelper.cancelTimer(getApplicationContext(),
        // TimerHelper.PENDING_INTENT_INTERRUPT_MUSIC_TIMER);
        // TimerHelper.cancelTimer(getApplicationContext(),
        // TimerHelper.PENDING_INTENT_INTERRUPT_SCHEDULE_TIMER);
        // TimerHelper.cancelTimer(getApplicationContext(),
        // TimerHelper.PENDING_INTENT_MUSIC_TIMER);
        // TimerHelper.cancelTimer(getApplicationContext(),
        // TimerHelper.PENDING_INTENT_TEMPLATE_TIMER);
        // TimerHelper.cancelTimer(getApplicationContext(),
        // TimerHelper.PENDING_INTENT_UPDATE_CHECK_TIMER);
        // TimerHelper.cancelTimer(getApplicationContext(),
        // TimerHelper.PENDING_INTENT_LICENSE_TIMER);
        FROPatternScheduleDBFactory.getInstance(getApplicationContext()).deleteAll();
        FROPatternContentDBFactory.getInstance(getApplicationContext()).deleteAll();
    }

    private void cancelAllTimer() {
        TimerHelper.cancelTimer(getApplicationContext(), TimerHelper.PENDING_INTENT_INTERRUPT_MUSIC_TIMER);
        TimerHelper.cancelTimer(getApplicationContext(), TimerHelper.PENDING_INTENT_INTERRUPT_SCHEDULE_TIMER);
        TimerHelper.cancelTimer(getApplicationContext(), TimerHelper.PENDING_INTENT_MUSIC_TIMER);
        TimerHelper.cancelTimer(getApplicationContext(), TimerHelper.PENDING_INTENT_TEMPLATE_TIMER);
        TimerHelper.cancelTimer(getApplicationContext(), TimerHelper.PENDING_INTENT_UPDATE_CHECK_TIMER);
        TimerHelper.cancelTimer(getApplicationContext(), TimerHelper.PENDING_INTENT_LICENSE_TIMER);
        if (mLocation != null) {
            mLocation.term();
            mLocation = null;
        }
    }
    /**
     * 拡張コールバックが存在し、かつ割り込み再生が実行中でない場合は実行する
     *
     * @param when WebAPI 名
     * @return true:コールバックが存在する, false:その他
     */
    private boolean runExtentionIfThere(int when) {
        FRODebug.logD(getClass(), "runExtentionIfThere " + when, true);
        // 拡張コールバックがあるか確認する
        Runnable callback = extensionCallback.remove(when);
        FRODebug.logD(getClass(), "callback " + callback, true);
        if (callback != null) {
            FRODebug.logD(getClass(), "mFaraoPlayer " + mFaraoPlayer, true);
            FRODebug.logD(getClass(), "mFaraoPlayer.isInterrupt() " + (mFaraoPlayer != null && mFaraoPlayer.isInterrupt()), true);
            // 割り込み再生中の場合は、割り込み再生後にコールバックが呼ばれるようにセット
            if (mFaraoPlayer != null && mFaraoPlayer.isInterrupt()) {
                mPendingChannelAction = callback;
            }
            // 割り込み再生中でなければ即実行
            else {
                callback.run();
            }
        }
        return callback != null;
    }
}
