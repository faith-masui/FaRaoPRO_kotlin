package jp.faraopro.play.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.Display;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.common.ErrorMessage;
import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.common.ITimerListener;
import jp.faraopro.play.common.SimpleTimer;
import jp.faraopro.play.domain.ChannelHistoryInfo;
import jp.faraopro.play.domain.ChannelInfo;
import jp.faraopro.play.domain.FROChannelHistoryDB;
import jp.faraopro.play.domain.FROChannelHistoryDBFactory;
import jp.faraopro.play.domain.FROFavoriteTemplateDB;
import jp.faraopro.play.domain.FROFavoriteTemplateDBFactory;
import jp.faraopro.play.domain.FROGoodHistoryDBFactory;
import jp.faraopro.play.domain.FROMusicHistoryDB;
import jp.faraopro.play.domain.FROMusicHistoryDBFactory;
import jp.faraopro.play.domain.FROTimerAutoDBFactory;
import jp.faraopro.play.domain.FROTimerDBFactory;
import jp.faraopro.play.domain.FacebookUserInfo;
import jp.faraopro.play.domain.GuideItemHolder;
import jp.faraopro.play.domain.ITimerDB;
import jp.faraopro.play.domain.ListDataInfo;
import jp.faraopro.play.domain.MCListDataInfo;
import jp.faraopro.play.domain.MainPreference;
import jp.faraopro.play.domain.MusicInfo;
import jp.faraopro.play.domain.MusicInfoEx;
import jp.faraopro.play.domain.TemplateChannelInfo;
import jp.faraopro.play.mclient.IMCItem;
import jp.faraopro.play.mclient.MCAdItem;
import jp.faraopro.play.mclient.MCAudioItem;
import jp.faraopro.play.mclient.MCBenifitItem;
import jp.faraopro.play.mclient.MCBookmarkItem;
import jp.faraopro.play.mclient.MCChannelItem;
import jp.faraopro.play.mclient.MCChartItem;
import jp.faraopro.play.mclient.MCDefAction;
import jp.faraopro.play.mclient.MCDefParam;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCGenreItem;
import jp.faraopro.play.mclient.MCLocationItem;
import jp.faraopro.play.mclient.MCSearchItem;
import jp.faraopro.play.mclient.MCStreamItem;
import jp.faraopro.play.mclient.MCTemplateItem;
import jp.faraopro.play.mclient.MCTimetableItem;
import jp.faraopro.play.model.ChannelMode;
import jp.faraopro.play.model.ChannelParams;
import jp.faraopro.play.model.FROUserData;
import jp.faraopro.play.model.TimerInfo;
import jp.faraopro.play.model.UserDataHelper;
import jp.faraopro.play.util.FROUtils;
import jp.faraopro.play.util.Utils;
import jp.faraopro.play.view.CustomListItem;
import jp.faraopro.play.view.GuidesItem;

/**
 * ActivityとService間のやり取りを仲介するクラス
 *
 * @author AIM
 */
public class FROForm implements ITimerListener {
    // put out logs
    protected boolean DEBUG = false;

    // const
    private final static int unskippableTime = 1000 * 15; // 15sec
    private final static int TIMER_TYPE_SKIPABLE = 1;

    // object
    private static FROForm instance;
    private static final Object mLock = new Object();
    private static final Object LOCK_API_REQUEST = new Object();

    private Context mContext;

    /**
     * コンテキストを返す
     *
     * @return {@link Context}
     */
    public Context getContext() {
        return mContext;
    }

    private IBindService mService;
    private IActivityListener mActivityListener;
    private SimpleTimer fifteenSecTimer;

    // flag
    // private boolean termFlag = false;
    private boolean menuFlag = false;
    private boolean logoutFlag = false;
    private boolean skippable = true;
    private boolean skipControl = false;

    /**
     * スキップ可能かどうかを返す
     *
     * @return true:スキップ可能、false:それ以外
     */
    public boolean isSkippable() {
        return (skippable && !skipControl);
    }

    /**
     * スキップ制御がかかっているかどうかを返す
     *
     * @return true:制御あり(スキップ不可)、false:それ以外
     */
    public boolean isSkipControl() {
        return skipControl;
    }

    /**
     * 音声広告の再生状態を返す
     *
     * @return true:音声広告再生中, false:それ以外
     */
    public boolean isPlayingAds() {
        return isPlayingAds;
    }

    // field
    private MusicInfo nowPlaying; // 再生中の楽曲情報
    private String rating = MCDefParam.MCP_PARAM_STR_RATING_NOP; // 再生中の楽曲の評価
    private ArrayList<CustomListItem> listData; // チャンネルリスト
    private int mychannelDiffIndex;
    public String sessionKey;
    private String transitionManager = "ModeSelect";

    // JPN SUPPORT IN
    public ArrayList<CustomListItem> genre1List; // ジャンル区分
    public ArrayList<CustomListItem> genre2List; // ジャンル
    public ArrayList<CustomListItem> genre3List; // サブジャンル
    private boolean isGenre; // ジャンルリスト取得中フラグ
    public static final int GENRE1_INTERVAL = 1000000;
    public static final int GENRE2_INTERVAL = 1000;
    private ArrayList<MusicInfoEx> historyDataList;
    private ArrayList<CustomListItem> historyList;
    // JPN SUPPORT OUT

    // SHARELINK
    public static final int SHARE_ON_FACEBOOK = 0;
    public static final int SHARE_ON_TWITTER = 1;
    public static final int SHARE_ON_MAIL = 3;
    public int shareTo;
    public String shareKey;
    public String shareName;
    public String expandKey;
    public String expandId;
    public boolean isShowFb = false;
    public String skipRemaining;
    // SHARELINK

    // FACEBOOK LOGIN
    private ArrayList<MCLocationItem> locationList; // 地域情報リスト

    // FARAO LINE
    private String artistId;

    public boolean doCancel = false;

    private MCAdItem adItem;
    private String ticketDomain; // コネカ連携、識別ドメイン
    private String ticketSerial; // コネカ連携、シリアルコード

    // New UI
    public static final int TERM_TYPE_LOGOUT = 0;
    public static final int TERM_TYPE_END = 1;
    public static final int TERM_TYPE_SETTING = 2;
    public static final int TERM_TYPE_PURCHASE = 3;
    public static final int TERM_TYPE_BENIFIT = 4;
    public static final int TERM_TYPE_TOP = 5;
    public static final int BOOT_STATE_NONE = 0;
    public static final int BOOT_STATE_INITIALIZE = 1;
    public static final int BOOT_STATE_DOWNLOAD = 2;
    public static final int BOOT_STATE_DECODE = 3;
    public int termType = TERM_TYPE_SETTING;
    public int genrePos = -1;
    public int genreSubPos = -1;
    public int mychannelPos = -1;
    public int width = -1;
    public int height = -1;
    public int bootState = BOOT_STATE_NONE;
    public int moveTo = MainActivity.TOP;
    // public String mode = null;
    // public String channel = null;
    // public String range = null;
    // private String permission;
    private boolean isPlayingAds = false;
    // private boolean isBooting = false;
    public ArrayList<CustomListItem> benifitList; // マイ特集チャンネルリスト
    public ArrayList<CustomListItem> chartList; // 特集チャンネルリスト
    public ArrayList<CustomListItem> genreList; // ジャンルリスト
    public ArrayList<CustomListItem> artistList; // アーティスト検索結果リスト
    public ArrayList<CustomListItem> featuredList; // おすすめアーティストリスト
    public ArrayList<CustomListItem> mychannelList; // マイチャンネルリスト
    public ArrayList<CustomListItem> tmpMychannelList; // マイチャンネル編集リスト
    public ArrayList<CustomListItem> templateList; // テンプレートリスト
    public ArrayList<CustomListItem> downloadTemplateList; // ダウンロードテンプレートリスト
    public ArrayList<CustomListItem> streamList; // ダウンロードテンプレートリスト
    public ArrayList<MCAudioItem> mInterruptAudios; // ダウンロードテンプレートリスト
    public ArrayList<Integer> menuList;
    public MusicInfo shareInfo;
    public ErrorMessage errorMsg;

    public ArrayList<ChannelInfo> favorites;
    public TimerInfo nowPlayingLocal;

    public String parentId = null;

    // private int playerType = -1;

    /**
     * ここまでメンバ
     **/

    public void setPlaylistParams(int mode, int channel, String range, int permission) {
        ChannelParams.setParams(mode, channel, range, permission);
        FRODebug.logD(getClass(), "setPlaylistParams:mode " + mode + ", channel " + channel
                + ", range " + range + ", permission " + permission, DEBUG);
    }

    // 自身のみ生成可能
    private FROForm() {
    }

    /**
     * FROFormのインスタンスを返す
     *
     * @return {@link FROForm}
     */
    public static FROForm getInstance() {
        synchronized (mLock) {
            if (instance == null) {
                instance = new FROForm();
            }
            return instance;
        }
    }

    /**
     * 初期化処理<br>
     * 最初に{@link FROForm}にアクセスするアクティビティで必ず実行すること<br>
     * Serviceの生成と接続を行う<br>
     * Serviceの起動が完了すると{@link IActivityListener#onNotifyResult}、<br>
     * 失敗すると{@link IActivityListener#onNotifyError}にて通知される
     *
     * @param context
     */
    public boolean init(Context context) {
        mContext = context;
        return connectService();
    }

    public void setHardwareInfo(Activity act) {
        // 画面サイズを取得する
        Display display = act.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Point p = new Point();
            display.getSize(p);
            FROForm.getInstance().width = p.x;
            FROForm.getInstance().height = p.y;
        } else {
            FROForm.getInstance().width = display.getWidth();
            FROForm.getInstance().height = display.getHeight();
        }
    }

    /**
     * メニューフラグのセット
     *
     * @param bool
     */
    public void setMenuFlag(boolean bool) {
        menuFlag = bool;
    }

    /**
     * @return : メニューフラグ
     */
    public boolean getMenuFlag() {
        return menuFlag;
    }

    /**
     * ログアウトフラグのセット
     *
     * @param bool
     */
    public void setLogoutFlag(boolean bool) {
        logoutFlag = bool;
    }

    /**
     * @return : ログアウトフラグ
     */
    public boolean getLogoutFlag() {
        return logoutFlag;
    }

    /**
     * 選択されているモードのチャンネルリストを返す
     *
     * @return 各チャンネルデータのリスト
     */
    public ArrayList<CustomListItem> getListData() {
        return listData;
    }

    /**
     * アカウント作成時に必要な地域データを返す
     *
     * @return 地域データのリスト
     */
    public ArrayList<MCLocationItem> getLocationList() {
        return locationList;
    }

    public void setTicketData(String domain, String serial) {
        this.ticketDomain = domain;
        this.ticketSerial = serial;
    }

    public boolean isTicketBoot() {
        boolean ret = false;

        if (ticketDomain != null && ticketSerial != null)
            ret = true;

        return ret;
    }

    /**
     * 終了処理
     */
    public void term() {
        try {
            if (mService != null) {
                mService.sTerm();
                disconnectService();
                mService = null;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        String path = FROUtils.getFaraoDirectory();
        if (path != null) {
            File file = new File(path);
            Utils.deleteFile(file);
        }
        // disconnectService();
        if (fifteenSecTimer != null) {
            fifteenSecTimer.stop();
            fifteenSecTimer = null;
        }
        MainPreference.getInstance(mContext).term();
        mContext = null;
        instance = null;
    }

    public ArrayList<Integer> getMenuList(Context context) {
        if (menuList == null || menuList.size() < 1) {
            menuList = MainPreference.getInstance(context).getMenuList();
        }

        return menuList;
    }

    public void updateMenuList(Context context) {
        if (menuList != null)
            menuList.clear();
        menuList = MainPreference.getInstance(context).getMenuList();
    }

    public boolean isMenu(int tag, Context context) {
        boolean isMenu = false;
        if (!isTablet(context)) {
            ArrayList<Integer> menu = getMenuList(context);
            for (int i = 0; i < 3; i++) {
                if (menu.get(i).intValue() == tag) {
                    isMenu = true;
                    break;
                }
            }
        } else {
            isMenu = true;
        }

        return isMenu;
    }

    public int getMenuIndex(int tag, Context context) {
        int index = 0;
        getMenuList(context);
        for (int i = 0; i < menuList.size(); i++) {
            if (menuList.get(i) == tag) {
                index = i + 1;
                break;
            }
            if (!isTablet(context) && i >= 3) {
                index = 4;
                break;
            }
        }

        return index;
    }

    /**
     * Handlerからのコールバック
     */
    private Service_Callback_IF listener = new Service_Callback_IF.Stub() {

        @Override
        public void onNotifyResult(int what, int statusCode) throws RemoteException {
            Message msg = new Message();
            msg.what = what;
            msg.arg1 = statusCode;
            msg.obj = null;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onNotifyChannelList(int what, int statusCode, ListDataInfo list) throws RemoteException {
            Message msg = new Message();
            msg.what = what;
            msg.arg1 = statusCode;
            msg.obj = list;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onNotifyMusicData(int what, int statusCode, MusicInfo info) throws RemoteException {
            nowPlaying = info;
            Message msg = new Message();
            msg.what = what;
            msg.arg1 = statusCode;
            msg.obj = info;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onNotifyWithString(int what, int statusCode, String data) throws RemoteException {
            Message msg = new Message();
            msg.what = what;
            msg.arg1 = statusCode;
            msg.obj = data;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onNotifyMCItemList(int when, int statusCode, MCListDataInfo list) throws RemoteException {
            Message msg = new Message();
            msg.what = when;
            msg.arg1 = statusCode;
            msg.obj = list;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onNotifyUserData(int what, int statusCode, FROUserData userData) throws RemoteException {
            Message msg = new Message();
            msg.what = what;
            msg.arg1 = statusCode;
            msg.obj = userData;
            mHandler.sendMessage(msg);
        }

    };

    /**
     * コールバック後の処理
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int when = msg.what;
            int statusCode = msg.arg1;
            Object obj = msg.obj;

            if (statusCode == Consts.STATUS_OK) {
                switch (when) {

                    case Consts.NOTIFY_RESULT:
                    case MCDefAction.MCA_KIND_SIGNUP:
                    case MCDefAction.MCA_KIND_LOGOUT:
                    case MCDefAction.MCA_KIND_PAYMENT_LOCK:
                    case MCDefAction.MCA_KIND_PAYMENT_COMMIT:
                    case MCDefAction.MCA_KIND_PAYMENT_CANCEL:
                    case MCDefAction.MCA_KIND_ARTWORK_DL:
                    case MCDefAction.MCA_KIND_LIST:
                    case MCDefAction.MCA_KIND_SEARCH:
                    case MCDefAction.MCA_KIND_FEATURED:
                    case MCDefAction.MCA_KIND_LISTEN:
                    case MCDefAction.MCA_KIND_RATING:
                    case Consts.NO_SKIP_REMAINING:
                    case Consts.PAST_ONE_HOUR:
                    case Consts.CHECK_UNSENT_DATA:
                    case Consts.TERMINATION:
                    case Consts.SHOW_PROGRESS:
                    case Consts.NOTIFY_DISABLE_CANCEL:
                    case MCDefAction.MCA_KIND_FACEBOOK_LOGIN:
                    case MCDefAction.MCA_KIND_FACEBOOK_ACCOUNT:
                    case MCDefAction.MCA_KIND_TICKET_CHECK:
                    case MCDefAction.MCA_KIND_TICKET_ADD:
                    case Consts.END_INTERRUPT:
                    case MCDefAction.MCA_KIND_PATTERN_SCHEDULE:
                    case MCDefAction.MCA_KIND_PATTERN_DOWNLOAD:
                    case MCDefAction.MCA_KIND_LICENSE_STATUS:
                    case MCDefAction.MCA_KIND_LICENSE_INSTALL:
                    case MCDefAction.MCA_KIND_LICENSE_TRACKING:
                    case Consts.NOTIFY_RENEWAL_TIMER_IS_CHANGED:
                        notifyResult(when, null);
                        break;
                    case Consts.CHANNEL_LIST_GENRE:
                        // Serviceから渡されたWebAPIの結果を取得
                        List<IMCItem> genres = ((MCListDataInfo) obj).getListData();
                        if (genreList != null) {
                            genreList.clear();
                        }
                        genreList = new ArrayList<CustomListItem>();
                        // IMCItem型からCustomListItem型に変換し、メンバに格納する
                        if (obj != null) {
                            for (IMCItem mcItem : genres) {
                                genreList.add(new CustomListItem((MCGenreItem) mcItem));
                            }
                        }
                        createGenreList();
                        notifyResult(when, null);
                        break;
                    case Consts.CHANNEL_LIST_ARTIST:
                        List<IMCItem> artist = ((MCListDataInfo) obj).getListData();
                        if (artistList != null) {
                            artistList.clear();
                        }
                        artistList = new ArrayList<CustomListItem>();
                        if (obj != null) {
                            for (IMCItem mcItem : artist) {
                                artistList.add(new CustomListItem((MCSearchItem) mcItem));
                            }
                        }
                        notifyResult(when, null);
                        break;
                    case Consts.CHANNEL_LIST_FEATURED:
                        List<IMCItem> featured = ((MCListDataInfo) obj).getListData();
                        if (featuredList != null) {
                            featuredList.clear();
                        }
                        featuredList = new ArrayList<CustomListItem>();
                        if (obj != null) {
                            for (IMCItem mcItem : featured) {
                                featuredList.add(new CustomListItem((MCSearchItem) mcItem));
                            }
                        }

                        deleteThumbnail(ChannelMode.ARTIST);

                        notifyResult(when, null);
                        break;
                    case Consts.CHANNEL_LIST_MYCHANNEL:
                        List<IMCItem> mychannel = ((MCListDataInfo) obj).getListData();
                        if (mychannelList != null) {
                            mychannelList.clear();
                        }
                        // 既に日付順でソートされているが、さらにロック状態の要素は上に持ってくる
                        mychannelList = new ArrayList<CustomListItem>();
                        ArrayList<CustomListItem> unlockList = new ArrayList<CustomListItem>();
                        if (obj != null) {
                            for (IMCItem mcItem : mychannel) {
                                // ロック状態であれば
                                if (mcItem.getString(MCDefResult.MCR_KIND_CHANNELITEM_LOCK).equalsIgnoreCase("2")) {
                                    // メンバに格納
                                    mychannelList.add(new CustomListItem((MCChannelItem) mcItem));
                                } else {
                                    // ロック状態でなければ、一時変数に格納
                                    unlockList.add(new CustomListItem((MCChannelItem) mcItem));
                                }
                            }
                            // メンバ変数に一時変数を格納
                            mychannelList.addAll(unlockList);
                        }
                        notifyResult(when, null);
                        break;
                    case Consts.CHANNEL_LIST_BENIFIT:
                        List<IMCItem> benifits = ((MCListDataInfo) obj).getListData();
                        if (benifitList != null) {
                            benifitList.clear();
                        }
                        benifitList = new ArrayList<CustomListItem>();
                        if (obj != null) {
                            for (IMCItem mcItem : benifits) {
                                benifitList.add(new CustomListItem((MCBenifitItem) mcItem));
                            }
                        }

                        deleteThumbnail(ChannelMode.BENIFIT);

                        notifyResult(when, null);
                        break;
                    case Consts.CHANNEL_LIST_CHART:
                        List<IMCItem> chart = ((MCListDataInfo) obj).getListData();
                        if (chartList != null) {
                            chartList.clear();
                        }
                        chartList = new ArrayList<CustomListItem>();
                        if (obj != null) {
                            for (IMCItem mcItem : chart) {

                                // TODO あとで整形すること
                                int seriesA = Integer
                                        .parseInt(((MCChartItem) mcItem).getString(MCDefResult.MCR_KIND_ITEM_SERIES_NO));
                                int contentA = Integer.parseInt(
                                        ((MCChartItem) mcItem).getString(MCDefResult.MCR_KIND_ITEM_SERIES_CONTENT_NO));
                                int beforeSize = chartList.size();
                                for (int i = 0; i < chartList.size(); i++) {
                                    CustomListItem listItem = chartList.get(i);
                                    int seriesB = Integer.parseInt(listItem.getmSeriesNo());
                                    int contentB = Integer.parseInt(listItem.getmSeriesContentNo());
                                    if (seriesA < seriesB) {
                                        chartList.add(i, new CustomListItem((MCChartItem) mcItem));
                                        break;
                                    } else if (seriesA == seriesB) {
                                        if (contentA < contentB) {
                                            chartList.add(i, new CustomListItem((MCChartItem) mcItem));
                                            break;
                                        }
                                    }
                                }
                                if (beforeSize == chartList.size()) {
                                    chartList.add(new CustomListItem((MCChartItem) mcItem));
                                }
                            }
                        }

                        deleteThumbnail(ChannelMode.HOT);
                        deleteThumbnail(ChannelMode.SHUFFLE);

                        notifyResult(when, null);
                        break;
                    case Consts.CHANNEL_LIST_BUSINESS:
                        List<IMCItem> guides = ((MCListDataInfo) obj).getListData();
                        GuideItemHolder.getInstance().addChild(guides);
                        notifyResult(when, null);
                        break;
                    case MCDefAction.MCA_KIND_TEMPLATE_LIST:
                        List<IMCItem> template = ((MCListDataInfo) obj).getListData();
                        if (templateList != null) {
                            templateList.clear();
                        }
                        templateList = new ArrayList<CustomListItem>();
                        if (obj != null) {
                            for (IMCItem mcItem : template) {
                                templateList.add(new CustomListItem((MCTemplateItem) mcItem));
                            }
                        }
                        notifyResult(when, null);
                        break;
                    case MCDefAction.MCA_KIND_TEMPLATE_DOWNLOAD:
                        List<IMCItem> templateDownload = ((MCListDataInfo) obj).getListData();
                        String className = ((MCListDataInfo) obj).itemName;
                        if (downloadTemplateList != null) {
                            downloadTemplateList.clear();
                        }
                        downloadTemplateList = new ArrayList<CustomListItem>();
                        Integer type = null;
                        if (obj != null) {
                            // タイマーテンプレート
                            if (className.equalsIgnoreCase(MCTimetableItem.class.getSimpleName())) {
                                ITimerDB timerDb;
                                int timerType = MainPreference.getInstance(getContext()).getMusicTimerType();
                                // 自動更新用テンプレート
                                if (timerType == Consts.MUSIC_TIMER_TYPE_AUTO) {
                                    timerDb = FROTimerAutoDBFactory.getInstance(getContext());
                                }
                                // 通常のテンプレート
                                else {
                                    timerDb = FROTimerDBFactory.getInstance(getContext());
                                }
                                timerDb.deleteAll();
                                for (IMCItem mcItem : templateDownload) {
                                    TimerInfo info = new TimerInfo((MCTimetableItem) mcItem);
                                    timerDb.insert(info);
                                }
                                type = new Integer(Consts.TEMPLATE_TYPE_TIMETABLE);
                            }
                            // お気に入りテンプレート
                            else {
                                FROFavoriteTemplateDB favoriteDb = FROFavoriteTemplateDBFactory.getInstance(getContext());
                                favoriteDb.deleteByParent(parentId);
                                for (IMCItem mcItem : templateDownload) {
                                    downloadTemplateList.add(new CustomListItem((MCBookmarkItem) mcItem));

                                    TemplateChannelInfo info = new TemplateChannelInfo((MCBookmarkItem) mcItem);
                                    info.setParentId(parentId);
                                    favoriteDb.insert(info);
                                }
                                type = new Integer(Consts.TEMPLATE_TYPE_BOOKMARK);
                            }
                        }
                        notifyResult(when, type);
                        break;
                    case MCDefAction.MCA_KIND_STREAM_LIST:
                        List<IMCItem> stream = ((MCListDataInfo) obj).getListData();
                        if (streamList != null) {
                            streamList.clear();
                        }
                        streamList = new ArrayList<CustomListItem>();
                        if (obj != null) {
                            for (IMCItem mcItem : stream) {
                                streamList.add(new CustomListItem((MCStreamItem) mcItem));
                            }
                        }
                        notifyResult(when, null);
                        break;
                    case MCDefAction.MCA_KIND_STATUS:
                    case MCDefAction.MCA_KIND_LOGIN:
                        FROUserData userData = (FROUserData) obj;
                        UserDataHelper.setData(userData);
                        notifyResult(when, null);
                        break;
                    case Consts.NOTIFY_START_DOWNLOAD:
                        bootState = BOOT_STATE_DOWNLOAD;
                        notifyResult(when, null);
                        break;
                    case Consts.NOTIFY_DOWNLOAD_PROGRESS:
                        notifyResult(when, obj);
                        break;
                    case MCDefAction.MCA_KIND_TRACK_DL:
                        if (bootState == BOOT_STATE_DOWNLOAD)
                            bootState = BOOT_STATE_DECODE;
                        notifyResult(when, obj);
                        break;
                    case Consts.START_MUSIC:
                        bootState = BOOT_STATE_NONE;
                        isPlayingAds = false;
                        shareKey = null;
                        // if (oneHourTimer == null && pauseInterval > 0) {
                        // oneHourTimer = new SimpleTimer(getInstance(), false);
                        // oneHourTimer.start(pauseInterval);
                        // }
                        if (fifteenSecTimer == null) {
                            fifteenSecTimer = new SimpleTimer(getInstance(), false);
                        } else {
                            fifteenSecTimer.stop();
                        }
                        fifteenSecTimer.setType(TIMER_TYPE_SKIPABLE);
                        fifteenSecTimer.start(unskippableTime);
                        skippable = false;
                        rating = MCDefParam.MCP_PARAM_STR_RATING_NOP;
                        notifyResult(when, obj);
                        break;
                    case Consts.START_LOCAL_MUSIC:
                    case Consts.START_OFFLINE_MUSIC:
                        bootState = BOOT_STATE_NONE;
                        isPlayingAds = false;
                        shareKey = null;
                        skippable = false;
                        rating = MCDefParam.MCP_PARAM_STR_RATING_NOP;
                        notifyResult(when, obj);
                        break;
                    case Consts.START_STREAM_MUSIC:
                        bootState = BOOT_STATE_NONE;
                        isPlayingAds = false;
                        shareKey = null;
                        skippable = false;
                        rating = MCDefParam.MCP_PARAM_STR_RATING_NOP;
                        notifyResult(when, obj);
                        break;
                    case Consts.START_INTERRUPT:
                        notifyResult(when, obj);
                        break;
                    case Consts.NOTIFY_COMPLETE_CANCEL:
                        bootState = BOOT_STATE_NONE;
                        notifyResult(when, null);
                        break;
                    case MCDefAction.MCA_KIND_SET:
                        if (!diffMychannel()) {
                            notifyResult(when, obj);
                        }
                        break;
                    case MCDefAction.MCA_KIND_MSG_DL:
                        ArrayList<String> messageData = ((ListDataInfo) obj).getListData();
                        notifyResult(when, messageData);
                        break;
                    case MCDefAction.MCA_KIND_CHANNEL_SHARE:
                        String[] tmp = ((String) obj).split(" ", 2);
                        shareKey = tmp[0];
                        shareName = tmp[1];
                        notifyResult(when, null);
                        break;
                    case MCDefAction.MCA_KIND_CHANNEL_EXPAND:
                        expandId = (String) obj;
                        notifyResult(when, null);
                        break;
                    case Consts.UPDATE_MODE_INFO:
                        ArrayList<String> info = ((ListDataInfo) obj).getListData();
                        setPlaylistParams(Integer.parseInt(info.get(0)), Integer.parseInt(info.get(1)), info.get(2),
                                Integer.parseInt(info.get(3)));
                        break;
                    case Consts.UPDATE_MUSIC_INFO:
                    case Consts.UPDATE_ADS_INFO:
                        if (obj instanceof MusicInfo) {
                            isPlayingAds = false;
                        } else if (obj instanceof MCListDataInfo) {
                            List<IMCItem> adList = ((MCListDataInfo) obj).getListData();
                            if (adList != null && adList.size() > 0) {
                                adItem = (MCAdItem) adList.get(0);
                            }
                            isPlayingAds = true;
                        }
                        notifyResult(when, null);
                        break;
                    case Consts.UPDATE_SESSION_INFO:
                        sessionKey = (String) obj;
                        notifyResult(when, obj);
                        break;
                    case Consts.NOTIFY_SKIP_REMAINING:
                        skipRemaining = (String) obj;
                        notifyResult(when, obj);
                        break;
                    case MCDefAction.MCA_KIND_FACEBOOK_LOOKUP:
                        notifyResult(when, obj);
                        break;
                    case MCDefAction.MCA_KIND_LOCATION:
                        List<IMCItem> list = ((MCListDataInfo) obj).getListData();
                        if (locationList == null)
                            locationList = new ArrayList<MCLocationItem>();
                        for (IMCItem mcItem : list) {
                            locationList.add((MCLocationItem) mcItem);
                        }
                        notifyResult(when, null);
                        break;
                    case Consts.START_AD:
                        isPlayingAds = true;
                        List<IMCItem> adList = ((MCListDataInfo) obj).getListData();
                        if (adList != null && adList.size() > 0) {
                            adItem = (MCAdItem) adList.get(0);
                            notifyResult(when, adItem);
                        }
                        break;
                    case Consts.NOTIFY_PLAYLIST_INFO:
                        String skip = (String) obj;
                        skipControl = Boolean.parseBoolean(skip);
                        break;

                    case Consts.MUSIC_TERMINATION:
                        bootState = BOOT_STATE_NONE;
                        nowPlaying = null;
                        if (errorMsg != null) {
                            notifyError(errorMsg.getWhen(), errorMsg.getCode());
                        } else {
                            notifyResult(when, null);
                        }
                        break;
                    case Consts.REQUEST_CHANGE_FRAGMENT:
                        notifyResult(when, obj);
                        break;
                    case Consts.INTERRUPT_LIST:
                        if (obj == null)
                            break;

                        List<IMCItem> audio = ((MCListDataInfo) obj).getListData();
                        if (mInterruptAudios != null) {
                            mInterruptAudios.clear();
                        }
                        mInterruptAudios = new ArrayList<MCAudioItem>();
                        if (obj != null) {
                            for (IMCItem mcItem : audio) {
                                MCAudioItem audioItem = (MCAudioItem) mcItem;
                                mInterruptAudios.add(audioItem.clone());
                            }
                        }
                        break;
                    case Consts.NOTIFY_FAILED_TO_RECOVER:
                        nowPlaying = null;
                        notifyResult(when, obj);
                        break;
                }
            } else {
                bootState = BOOT_STATE_NONE;
                if (statusCode != Consts.NOTIFY_BEGIN_EMERGENCY_MODE && statusCode != Consts.NOTIFY_PLAY_NEXT_EMERGENCY
                        && statusCode != Consts.NOTIFY_RECOVERY_EMERGENCY_MODE
                        && statusCode != Consts.NOTIFY_TIMEOUT_EMERGENCY_MODE)
                    errorMsg = new ErrorMessage(when, statusCode);
                int pStatus = checkPlayerInstance();
                if (pStatus != Consts.PLAYER_STATUS_NOINSTANCE) {
                    // 再生の継続が不可能な致命的エラーであった場合
                    if (Consts.isFatalMusicError(when, statusCode) || MCDefAction.isFatalMusicError(when, statusCode))
                        // 再生を終了する
                        termMusic(-2);
                    else
                        notifyError(when, statusCode);
                } else {
                    notifyError(when, statusCode);
                }
            }
        }
    };

    private void notifyResult(int when, Object obj) {
        if (mActivityListener != null)
            mActivityListener.onNotifyResult(when, obj);
    }

    private void notifyError(int when, int statusCode) {
        if (mActivityListener != null)
            mActivityListener.onNotifyError(when, statusCode);
    }

    private void createGenreList() {
        if (genreList == null)
            return;

        if (genre1List != null)
            genre1List.clear();
        genre1List = new ArrayList<CustomListItem>();
        if (genre2List != null)
            genre2List.clear();
        genre2List = new ArrayList<CustomListItem>();
        if (genre3List != null)
            genre3List.clear();
        genre3List = new ArrayList<CustomListItem>();

        for (int i = 0; i < genreList.size(); i++) {
            CustomListItem item = genreList.get(i);
            // IDが1000000で割り切れれば、ジャンル区分を表すアイテム
            if ((item.getId() % GENRE1_INTERVAL) == 0) {
                item.setNext(true);
                genre1List.add(item);
            }
            // IDが1000000で割り切れず、かつ1000で割り切れればジャンルを表すアイテム
            else if ((item.getId() % GENRE2_INTERVAL) == 0) {
                genre2List.add(item);
            }
            // それ以外はサブジャンルを表すアイテム
            else {
                genre3List.add(item);
            }
        }
    }

    /**
     * サービス接続時、サービス切断時の処理
     */
    private ServiceConnection con = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IBindService.Stub.asInterface(service);
            try {
                mService.addListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
                notifyError(Consts.SERVICE_BOOT, Consts.SERVICE_CONNECTION_ERROR);
            }
            notifyResult(Consts.SERVICE_BOOT, null);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                mService.removeListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mService = null;
        }

    };

    /**
     * @return : サービスが接続されているか否か
     */
    public boolean isConnectedService() {
        boolean bool = false;

        if (mService != null && (mService.asBinder().isBinderAlive() & mService.asBinder().pingBinder()))
            bool = true;

        return bool;
    }

    /**
     * サービスの起動と接続
     */
    public boolean connectService() {
        if (isConnectedService()) {
            return true;
        }
        // TODO service 起動時、一度 unbind してから bind し直さないと activity に引きずられて service
        // が落ちる問題の対応
        FRODebug.logD(getClass(), "rebind", true);
        Intent intent = new Intent(mContext, FROHandler.class);
        mContext.startService(intent);
        // 無名クラスで ServiceConnection を作成し、onServiceConnected で unbind した後に再度 bind
        // し直す
        mContext.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                FRODebug.logD(getClass(), "rebind onServiceDisconnected", true);
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                FRODebug.logD(getClass(), "rebind onServiceConnected", true);
                mContext.unbindService(this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        bindService();
                    }
                }).start();
            }
        }, Context.BIND_AUTO_CREATE);
        return false;
    }

    /**
     * サービスの停止と切断
     */
    public void disconnectService() {
        Intent intent = new Intent(mContext, FROHandler.class);
        try {
            if (isConnectedService())
                mContext.unbindService(con);
        } catch (IllegalArgumentException e) {
        }
        mContext.stopService(intent);
    }

    public void bindService() {
        FRODebug.logD(getClass(), "bindService", true);
        Intent intent = new Intent(mContext, FROHandler.class);
        mContext.startService(intent);
        mContext.bindService(intent, con, Context.BIND_AUTO_CREATE);
    }

    public void unbindService() {
        FRODebug.logD(getClass(), "unbindService", true);
        if (isConnectedService()) {
            mContext.unbindService(con);
        }
    }

    /**
     * Fromからのコールバックを受けるリスナーの設定
     */
    public void attach(IActivityListener formCallbackListener) {
        if (mActivityListener != null)
            mActivityListener = null;
        mActivityListener = formCallbackListener;
    }

    public void detach(IActivityListener formCallbackListener) {
        if (mActivityListener != null) {
            if (mActivityListener == formCallbackListener)
                mActivityListener = null;
        }
    }

    private void checkService() {
        if (mService == null) {
            // FROUtils.outputLog("mService == null");
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    /**
     * セッションキーを返す
     *
     * @return セッションキー
     */
    public String getSessionKey() {
        checkService();
        if (sessionKey == null) {
            try {
                sessionKey = mService.sGetSession();
            } catch (RemoteException e) {
                e.printStackTrace();
                sessionKey = null;
            }
        }

        return sessionKey;
    }

    /**
     * 楽曲評価の設定
     */
    public void setRating(String value) {
        this.rating = value;
        sendDecision(rating);
    }

    /**
     * @return : 楽曲の評価
     */
    public String getRating() {
        return this.rating;
    }

    /**
     * @return : 楽曲の評価
     */
    public int getIntRating() {
        int ratingInt = Consts.RATING_NOP;
        if (rating != null) {
            if (rating.equalsIgnoreCase(MCDefParam.MCP_PARAM_STR_RATING_GOOD)) {
                ratingInt = Consts.RATING_GOOD;
            } else if (rating.equalsIgnoreCase(MCDefParam.MCP_PARAM_STR_RATING_BAD)) {
                ratingInt = Consts.RATING_BAD;
            }
        }
        return ratingInt;
    }

    /**
     * @return : 現在の再生楽曲の情報
     */
    public MusicInfo getNowPlaying() {
        return nowPlaying;
    }

    public void setNowPlaying(MusicInfo info) {
        nowPlaying = info;
    }


    /** ここからHandlerのメソッド呼び出し **/

    /**
     * ログイン
     */
    public void login(String email, String password, String force) {
        checkService();
        sessionKey = null;
        final String param1 = email;
        final String param2 = password;
        final String param3 = force;
        // final String param4 = deviceToken;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sLogin(param1, param2, param3);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ログアウト
     */
    public void logout() {
        checkService();
        sessionKey = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sLogout();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * アカウント作成
     */
    public void signin(String email, String password) {
        checkService();
        final String param1 = email;
        final String param2 = password;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sSignin(param1, param2);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ステータス取得
     */
    public void getStatus() {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sGetStatus();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ジャンルリスト取得
     */
    public void getGenreList() {
        checkService();
        isGenre = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sGetGenreList();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * アーティストリスト取得
     *
     * @param word :検索ワード
     */
    public void getArtistList(String word) {
        checkService();
        final String param1 = word;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sGetArtistList(param1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * HOT100リスト取得
     */
    public void getHot100List() {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sGetHot100List();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * マイチャンネルリスト取得
     */
    public void getMychannelList() {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sGetMychannelList();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ウェルカムメッセージ取得
     */
    // public void getMsgWelcome() {
    // checkService();
    // new Thread(new Runnable() {
    // @Override
    // public void run() {
    // String param;
    // if (getPaymentStatus() == Consts.FREE_USER) {
    // param = "welcome_join";
    // } else {
    // param = "welcome";
    // }
    // try {
    // mService.sGetMsg(param);
    // } catch (RemoteException e) {
    // e.printStackTrace();
    // }
    // }
    // }).start();
    // }

    /**
     * 利用規約取得
     */
    public void getMsgTermsOfService() {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sGetMsg("tos");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 利用規約取得
     */
    public void getMsgPrivacyPolicy() {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sGetMsg("privacy");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // /**
    // * プレイヤー起動
    // */
    // public void bootPlayer(final String mode, final int channel,
    // final String range) {
    // checkService();
    // new Thread(new Runnable() {
    // @Override
    // public void run() {
    // try {
    // mService.sGetPlayLists(mode, channel, range, 5);
    // } catch (RemoteException e) {
    // e.printStackTrace();
    // }
    // }
    // }).start();
    // artistId = null;
    // }

    /**
     * プレイヤー起動
     */
    public boolean bootPlayer() {
        synchronized (LOCK_API_REQUEST) {
            if (bootState != BOOT_STATE_NONE)
                return false;

            checkService();
            bootState = BOOT_STATE_INITIALIZE;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mService.sGetPlayLists(ChannelParams.getMode().id, ChannelParams.getChaneelId(),
                                ChannelParams.getRange(), 0, ChannelParams.getPermission());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            artistId = null;

            return true;
        }
    }

    public void cancelBoot() {
        synchronized (LOCK_API_REQUEST) {
            checkService();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mService.sCancelBoot();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void interruptCancel() {

    }

    /**
     * 楽曲評価
     */
    public void requestNext() {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sRequestNext(rating);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void requestNextLocal(final int direction) {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sRequestNextLocal(direction);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * マイチャンネル編集の適用
     */
    public void setMyChannel(int pos) {
        checkService();
        final int fPos = pos;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    CustomListItem after = tmpMychannelList.get(fPos);
                    mService.sSetMyChannel(after.getId(), after.getmName(), after.getLock());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // /**
    // * マイチャンネル編集の適用
    // */
    // public void setMyChannel() {
    // checkService();
    // mychannelDiffIndex = 0;
    // if (!diffMychannel())
    // mActivityListener.onNotifyResult(MCDefAction.MCA_KIND_SET, null);
    // }

    private boolean diffMychannel() {
        boolean diff = false;
        try {
            CustomListItem before;
            CustomListItem after;
            for (int i = mychannelDiffIndex; i < mychannelList.size(); i++) {
                before = mychannelList.get(i);
                after = tmpMychannelList.get(i);
                // 名前が変更されている、またはロック状態が変更されている場合
                if (!before.getmName().equals(after.getmName()) || before.getLock() != after.getLock()) {
                    diff = true;
                    mService.sSetMyChannel(after.getId(), after.getmName(), after.getLock());
                    mychannelDiffIndex = i + 1;
                    break;
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return diff;
    }

    /**
     * Handlerに評価の値を送る
     *
     * @param decision : 評価
     */
    public void sendDecision(String decision) { // 値だけハンドラーに投げる
        checkService();
        try {
            mService.sSendDecision(decision);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 楽曲一時停止
     */
    public synchronized void pauseMusic() {
        checkService();
        try {
            mService.sPauseMusic();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 楽曲一時停止
     */
    public synchronized void pauseMusicAsync() {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sPauseMusic();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * @return : 再生中か否か
     */
    public boolean isPlaying() {
        boolean ret = false;
        try {
            if (mService != null) {
                ret = mService.sIsPlaying();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return ret;
    }

    // /**
    // * @return : Handlerがプレイ中(停止or再生)状態にあるか否か
    // */
    // public boolean checkPlayerInstance() {
    // boolean ret = false;
    // try {
    // if (mService != null) {
    // ret = mService.sCheckPlayerInstance();
    // }
    // } catch (RemoteException e) {
    // e.printStackTrace();
    // }
    //
    // return ret;
    // }

    /**
     * @return : Handlerがプレイ中(停止or再生)状態にあるか否か
     */
    public int checkPlayerInstance() {
        int ret = Consts.PLAYER_STATUS_NOINSTANCE;
        try {
            if (mService != null) {
                ret = mService.sCheckPlayerInstance();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * プレイヤー終了時の処理
     *
     * @param type 終了タイプ, -1:エラー&評価送信またはプレイヤー終了時, -2:エラー&評価送信不要, else:正常終了
     */
    public void termMusic(final int type) {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sTermMusic(type);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 楽曲情報更新要求
     */
    public void updateMusicInfo() {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.updateMusicInfo();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // public void updateAdsInfo() {
    // new Thread(new Runnable() {
    // @Override
    // public void run() {
    // try {
    // mService.updateAdsInfo();
    // }
    // catch(RemoteException e) {
    // e.printStackTrace();
    // }
    // }
    // }).start();
    // }

    /**
     * エラー時の評価再送信
     */
    // public boolean checkUnsentData() {
    // checkService();
    // boolean hasTask = false;
    // try {
    // hasTask = mService.sResendRating();
    // } catch (RemoteException e) {
    // e.printStackTrace();
    // }
    //
    // return hasTask;
    // // new Thread(new Runnable() {
    // // @Override
    // // public void run() {
    // // try {
    // // mService.sResendRating();
    // // } catch (RemoteException e) {
    // // e.printStackTrace();
    // // }
    // // }
    // // }).start();
    // }

    /**
     * @return str : 選択したモード番号
     */
    public String getModeInService() {
        checkService();
        String str = null;

        try {
            str = mService.sGetModeType();
        } catch (RemoteException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
        }

        return str;
    }

    public int getModeIntInService() {
        int modeType = 0;
        String mode = getModeInService();
        if (!TextUtils.isEmpty(mode)) {
            modeType = Integer.parseInt(mode);
        }

        return modeType;
    }

    /**
     * チャンネルシェア
     */
    public void channelShare() {
        checkService();
        if (shareKey == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mService.sChannelShare("0");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            notifyResult(MCDefAction.MCA_KIND_CHANNEL_SHARE, null);
        }
    }

    /**
     * チャンネル展開
     */
    public void channelExpand() {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sChannelExpand(expandKey);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                expandKey = null;
            }
        }).start();
    }

    /**
     * facebookログイン(アカウントチェック)
     *
     * @param email
     */
    public void facebookLookup(String email) {
        checkService();
        final String param = email;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sFacebookLookup(param);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Facebookログイン(ログイン)
     *
     * @param token アクセストークン
     * @param email メールアドレス
     * @param force 強制ログインの可否
     */
    public void facebookLogin(String token, String email, String force) {
        checkService();
        final String param1 = token;
        final String param2 = email;
        final String param3 = force;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sFacebookLogin(param1, param2, param3);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * アカウント作成(Facebook連携)
     *
     * @param info Facebookから取得したユーザ情報
     */
    public void facebookAccount(FacebookUserInfo info) {
        checkService();
        final String param1 = info.getMail();
        final String param2 = info.getPass();
        final int param3 = info.getGender();
        final int param4 = info.getBirthYear();
        final String param5 = info.getProvince();
        final int param6 = info.getRegion();
        final String param7 = info.getCountry();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sFacebookAccount(param1, param2, param3, param4, param5, param6, param7);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * オススメアーティストリスト取得
     */
    public void getFeaturedArtistList() {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sGetFeaturedArtistList();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 日本の地域情報を取得する
     */
    public void getLocationInfo() {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sGetLocationList();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setAdRating(String rating) {
        checkService();
        final String value = rating;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sAdRating(value);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void checkTicket() {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sCheckTicket(ticketDomain, ticketSerial);
                    // mService.sCheckTicket("notyet.stg", "FaraNW0UAdmzbFeq");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void addTicket() {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sAddTicket();
                    setTicketData(null, null);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public int getCurrentPlayerPos() {
        checkService();
        int pos = 0;
        try {
            pos = mService.sGetCurrentPos();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return pos;
    }

    /**
     * ビジネスチャンネル取得
     */
    public void getGuidesList(int index) {
        int tmp = 0;
        if (GuideItemHolder.getInstance().getSelected() != null) {
            GuidesItem child = GuideItemHolder.getInstance().changeSelected(index);
            if (child != null)
                tmp = child.getId();
        }
        final String fNode = Integer.toString(tmp);
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sGetBusinessList(fNode);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean playLocal(final String path, final boolean loop) {
        synchronized (LOCK_API_REQUEST) {
            if (bootState != BOOT_STATE_NONE)
                return false;

            bootState = BOOT_STATE_INITIALIZE;
            checkService();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mService.sPlayLocal(path);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            return true;
        }
    }

    public void getTemplateList(final int type) {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sGetTemplateList(type);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void downloadTemplate(final int type, final int id) {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sDownloadTemplateList(type, id);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * マイチャンネルリスト取得
     */
    public void getStreamList(final int type) {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sGetStreamList(type);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public int streamId;

    /**
     * マイチャンネルリスト取得
     */
    public void playStream(final int streamId) {
        synchronized (LOCK_API_REQUEST) {
            if (bootState != BOOT_STATE_NONE) {
                return;
            }

            this.streamId = streamId;
            bootState = BOOT_STATE_INITIALIZE;
            checkService();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mService.sPlayStream(streamId);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void sendTemplateState() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sSendTemplateState();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean isInterruptPlaying() {
        boolean isInterrupt = false;
        try {
            isInterrupt = mService.sIsInterrupt();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return isInterrupt;
    }

    public void checkInterruptSchedule() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sCheckInterruptSchedule();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setPreferenceBoolean(int type, boolean value) {
        try {
            mService.sSetPreferenceBoolean(type, value);
        } catch (RemoteException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    public void setPreferenceInteger(int type, int value) {
        try {
            mService.sSetPreferenceInteger(type, value);
        } catch (RemoteException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    public String getPreferenceString(int type) throws Exception {
        if (mContext == null || mService == null) {
            throw new Exception("Uninitialized");
        }
        return mService.sGetPreferenceString(type);
    }

    public void checkLicenseStatus() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sLicenseStatus();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public int getPlayerType() {
        int type = FROPlayer.PLAYER_TYPE_NONE;
        try {
            type = mService.sGetPlayerType();
        } catch (RemoteException e) {
        }

        return type;
    }

    /**
     * 割り込み再生データ更新
     */
    public void updateInterruptData() {
        checkService();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.sUpdateInterruptData();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void changeChannelVolume(String value) {
        checkService();
        float percent = Float.parseFloat(value);
        percent = percent / 100;
        try {
            mService.sChangeChannelVolume(percent);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void changeInterruptVolume(String value) {
        checkService();
        float percent = Float.parseFloat(value);
        percent = percent / 100;
        try {
            mService.sChangeInterruptVolume(percent);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public float getChannelVolume() {
        checkService();
        float volume = 1.0f;
        try {
            volume = mService.sGetChannelVolume();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return volume;
    }

    public float getInterruptVolume() {
        checkService();
        float volume = 1.0f;
        try {
            volume = mService.sGetInterruptVolume();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return volume;
    }

    public void changeAudioFocusedVolume(int value) {
        checkService();
        float percent = ((float) value) / 100f;
        try {
            mService.sChangeAudioFocusedVolume(percent);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public float getAudioFocusedVolume() {
        checkService();
        float volume = 0.1f;
        try {
            volume = mService.sGetAudioFocusedVolume();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return volume;
    }


    public boolean isEmergency() {
        try {
            return (mService != null) && mService.sIsEmergency();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void playEmergency() {
        try {
            mService.sPlayEmergency();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void pauseEmergency() {
        try {
            mService.sPauseEmergency();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cancelEmergency() {
        try {
            mService.sCancelEmergency();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public int getEmergencyLength() {
        try {
            return mService.sGetEmergencyLength();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getEmergencyPosition() {
        try {
            return mService.sGetEmergencyPosition();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean isPlayingEmergency() {
        try {
            return mService.sIsPlayingEmergency();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isRecoveredEmergency() {
        try {
            return mService.sIsRecoveredEmergency();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getInterruptIndex() {
        try {
            return mService.sGetInterruptIndex();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getInterruptPosition() {
        try {
            return mService.sGetInterruptPosition();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void callAPI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mService.callAPI();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 未完了購入処理の通知
     *
     * @param what       : エラー発生場所
     * @param statusCode : エラーコード
     */
    public void notifyFromBillingService(int what, int statusCode) {
        Message msg = new Message();
        msg.what = what;
        msg.arg1 = statusCode;
        msg.obj = null;
        mHandler.sendMessage(msg);
    }

    /**
     * 1Hタイマーイベント
     */
    @Override
    public void onTimerEvent(int type) {
        if (type == TIMER_TYPE_SKIPABLE) {
            fifteenSecTimer.stop();
            if (!skipControl)
                skippable = true;
            notifyResult(Consts.NOTIFY_PAST_15SEC, null);
        } else {
            try {
                mService.oneHourNotify();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            notifyResult(Consts.PAST_ONE_HOUR, null);
        }
    }

    /**
     * アクティビティ通知用インタフェース
     */
    public interface IActivityListener {
        /**
         * FROFormからの通知を受け取るためのコールバックメソッド
         *
         * @param when 通知内容
         * @param obj  通知内容に付随したオブジェクト
         */
        void onNotifyResult(int when, Object obj);

        /**
         * FROFormからのエラー通知を受け取るためのコールバックメソッド
         *
         * @param when       通知内容
         * @param statusCode エラーコード
         */
        void onNotifyError(int when, int statusCode);

        /**
         * FROFormからの課金完了通知を受け取るためのコールバックメソッド
         *
         * @param what
         * @param statusCode ステータスコード
         * @param strList    トランザクションリスト
         * @param index      トランザクションID
         */
        void onNotifyChanged(int what, int statusCode, ArrayList<String> strList, int index);
    }

    /**
     * @return : プレイヤー終了時の遷移先
     */
    public String getTransitionManager() { // MEMO 一度取得されたら初期化、初期値はModeSelect
        String tmp = transitionManager;
        transitionManager = "ModeSelect";
        return tmp;
    }

    /**
     * 遷移先のセット
     *
     * @param transitionManager
     */
    public void setTransitionManager(String transitionManager) {
        this.transitionManager = transitionManager;
    }

    private ChannelHistoryInfo getChannelNameByParam(String param, FROChannelHistoryDB db) {
        ChannelHistoryInfo channelInfo = new ChannelHistoryInfo();
        String[] params = param.split(",");
        channelInfo.setMode(params[0]);
        channelInfo.setChannelId(params[1]);
        if (params.length == 3) {
            channelInfo.setRange(params[2]);
        }
        return db.find(channelInfo);
    }

    // JPN SUPPORT IN

    /**
     * 履歴リストの取得
     *
     * @return 履歴情報(CustomListItem)をリスト化したもの
     */
    public ArrayList<CustomListItem> getHistoryList(Context context) {
        getHistoryData(context);
        if (historyDataList == null)
            return null;
        historyList = new ArrayList<CustomListItem>();
        FROChannelHistoryDB channelDb = FROChannelHistoryDBFactory.getInstance(context);
        ChannelHistoryInfo channelHistory;
        for (MusicInfoEx infoEx : historyDataList) {
            MusicInfo info = infoEx.getMusicInfo();
            CustomListItem item = new CustomListItem();
            item.setId(infoEx.getId());
            item.setText(info.getTitle());
            item.setmThumbIcon(info.getThumb());
            item.setmName(info.getArtist());
            if (!TextUtils.isEmpty(infoEx.getParams())) {
                item.setmContentText(infoEx.getParams());
                channelHistory = getChannelNameByParam(infoEx.getParams(), channelDb);
                if (channelHistory != null)
                    item.setStationName(channelHistory.getName());
            }

            int value;
            if (infoEx.getValue().equals(MCDefParam.MCP_PARAM_STR_RATING_NOP))
                value = 0;
            else if (infoEx.getValue().equals(MCDefParam.MCP_PARAM_STR_RATING_GOOD))
                value = 1;
            else
                value = 2;
            item.setLock(value);

            ArrayList<Pair<String, String>> urlList = infoEx.getMusicInfo().getUrlPairList();
            if (urlList != null && urlList.size() > 0) {
                for (Pair<String, String> pair : urlList) {
                    if (pair.first.equals(Consts.AFFILIATE_BUY_CD))
                        item.setBuyUrl(pair.second);
                    else if (pair.first.equals(Consts.AFFILIATE_DOWNLOAD_ANDROID))
                        item.setDownloadUrl(pair.second);
                }
            }
            historyList.add(item);
        }

        return historyList;
    }

    public ArrayList<CustomListItem> getGoodHistoryList(Context context) {
        ArrayList<MusicInfoEx> tmpGoodList = (ArrayList<MusicInfoEx>) FROGoodHistoryDBFactory.getInstance(context)
                .findAll();
        if (tmpGoodList == null || tmpGoodList.size() < 1)
            return null;

        ArrayList<CustomListItem> goodList = new ArrayList<CustomListItem>();
        FROChannelHistoryDB channelDb = FROChannelHistoryDBFactory.getInstance(context);
        ChannelHistoryInfo channelHistory;
        for (MusicInfoEx infoEx : tmpGoodList) {
            MusicInfo info = infoEx.getMusicInfo();
            CustomListItem item = new CustomListItem();
            item.setId(infoEx.getId());
            item.setText(info.getTitle());
            item.setmThumbIcon(info.getThumb());
            item.setmName(info.getArtist());
            if (!TextUtils.isEmpty(infoEx.getParams())) {
                item.setmContentText(infoEx.getParams());
                channelHistory = getChannelNameByParam(infoEx.getParams(), channelDb);
                if (channelHistory != null)
                    item.setStationName(channelHistory.getName());
            }
            item.setLock(1);
            ArrayList<Pair<String, String>> urlList = infoEx.getMusicInfo().getUrlPairList();
            if (urlList != null && urlList.size() > 0) {
                for (Pair<String, String> pair : urlList) {
                    if (pair.first.equals(Consts.AFFILIATE_BUY_CD))
                        item.setBuyUrl(pair.second);
                    else if (pair.first.equals(Consts.AFFILIATE_DOWNLOAD_ANDROID))
                        item.setDownloadUrl(pair.second);
                }
            }
            goodList.add(item);
        }

        return goodList;
    }

    /**
     * 履歴データを取得する
     *
     * @param id 楽曲ID
     * @return 履歴データ(MusicInfoEx)
     */
    public MusicInfoEx getHistoryData(int id) {
        if (historyDataList == null)
            return null;

        for (MusicInfoEx infoEx : historyDataList) {
            if (infoEx.getId() == id) {
                return infoEx;
            }
        }

        return null;
    }

    // 履歴データを取得する
    private void getHistoryData(Context context) {
        FROMusicHistoryDB db = (FROMusicHistoryDB) FROMusicHistoryDBFactory.getInstance(context);
        historyDataList = (ArrayList<MusicInfoEx>) db.findAll();
    }

    /**
     * 外部アプリから指定されたアーティストIDを取得する
     *
     * @return アーティストID、まだ設定されていない場合はnull
     */
    public String getArtistId() {
        return artistId;
    }

    /**
     * 外部アプリから指定されたアーティストIDを保存
     *
     * @param artistId アーティストID
     */
    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public MCAdItem getAdItem() {
        return this.adItem;
    }

    public boolean isTablet(Context context) {
        // return context.getResources().getBoolean(R.bool.is_tablet);
        return false;
    }

    public TimerInfo editedTimer = null;

    public void messageToListener(int when, int code, Object obj) {
        Message msg = new Message();
        msg.what = when;
        msg.arg1 = code;
        msg.obj = obj;
        mHandler.sendMessage(msg);
    }

    public void changeFragment(int mode, int statusCode) {
        Message msg = new Message();
        msg.what = Consts.REQUEST_CHANGE_FRAGMENT;
        msg.arg1 = statusCode;
        msg.obj = new Integer(mode);
        mHandler.sendMessage(msg);
    }

    public ChannelInfo editedChannel = null;

    private void deleteThumbnail(ChannelMode mode) {
        File farao = mContext.getFilesDir();
        File[] thumbs = farao.listFiles(Utils.getFilenameFilterStartWith(Consts.PRIVATE_PATH_THUMB_LIST + mode.text));
        ArrayList<CustomListItem> list;

        switch (mode) {
            case ARTIST:
                list = featuredList;
                break;
            case BENIFIT:
                list = benifitList;
                break;
            case HOT:
            case SHUFFLE:
                list = chartList;
                break;
            default:
                return;
        }
        if (list == null || list.size() < 1)
            return;

        for (int j = 0; j < thumbs.length; j++) {
            boolean contained = false;
            String thumbName = thumbs[j].getName();
            for (int i = 0; i < list.size(); i++) {
                String name = Consts.PRIVATE_PATH_THUMB_LIST + list.get(i).getMode().text + list.get(i).getmThumbIcon();
                name = (name != null) ? name.replace(".jpg", "").replace(".png", "") : null;
                if (name != null && thumbName.equalsIgnoreCase(name)) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                Utils.deleteFile(thumbs[j]);
            }
        }
    }

    public CustomListItem selectedFavorite;

    // public void test(Context context) {
    // DebugSpace d = new DebugSpace();
    // d.doAction(context);
    // }
}
