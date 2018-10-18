package jp.faraopro.play.act.newone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.ArrayList;

import jp.faraopro.play.R;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.app.FROPlayer;
import jp.faraopro.play.app.TimerHelper;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.common.LocationHelper;
import jp.faraopro.play.domain.FROFavoriteChannelDB;
import jp.faraopro.play.domain.FROFavoriteChannelDBFactory;
import jp.faraopro.play.domain.FROTemplateFavoriteListDB;
import jp.faraopro.play.domain.FROTemplateFavoriteListDBFactory;
import jp.faraopro.play.domain.FROTemplateTimerAutoListDB;
import jp.faraopro.play.domain.FROTemplateTimerAutoListDBFactory;
import jp.faraopro.play.domain.FROTimerAutoDBFactory;
import jp.faraopro.play.domain.MainPreference;
import jp.faraopro.play.domain.MusicInfo;
import jp.faraopro.play.frg.mode.AboutFragment;
import jp.faraopro.play.frg.mode.BannerFragment;
import jp.faraopro.play.frg.mode.DragFragment;
import jp.faraopro.play.frg.mode.FavoriteDeleteFragment;
import jp.faraopro.play.frg.mode.FavoriteEditFragment;
import jp.faraopro.play.frg.mode.FavoriteFragment;
import jp.faraopro.play.frg.mode.FavoriteLocalFragment;
import jp.faraopro.play.frg.mode.FavoriteTemplateDeleteFragment;
import jp.faraopro.play.frg.mode.FavoriteTemplateFragment;
import jp.faraopro.play.frg.mode.GenreFragment;
import jp.faraopro.play.frg.mode.GenreSectionFragment;
import jp.faraopro.play.frg.mode.GenreSubFragment;
import jp.faraopro.play.frg.mode.HistoryParentFragment;
import jp.faraopro.play.frg.mode.IModeFragment;
import jp.faraopro.play.frg.mode.ImportTemplateFragment;
import jp.faraopro.play.frg.mode.ListBaseFragment;
import jp.faraopro.play.frg.mode.LocalPlayerFragment;
import jp.faraopro.play.frg.mode.OfflinePlayerFragment;
import jp.faraopro.play.frg.mode.OtherFragment;
import jp.faraopro.play.frg.mode.OtherSettingFragment;
import jp.faraopro.play.frg.mode.PatternParentFragment;
import jp.faraopro.play.frg.mode.PlayerFragment;
import jp.faraopro.play.frg.mode.SelectFavoriteFragment;
import jp.faraopro.play.frg.mode.SelectLocalFragment2;
import jp.faraopro.play.frg.mode.SelectSourceFragment;
import jp.faraopro.play.frg.mode.SettingFragment;
import jp.faraopro.play.frg.mode.SpecialFragment;
import jp.faraopro.play.frg.mode.StreamPlayerFragment;
import jp.faraopro.play.frg.mode.StreamingFragment;
import jp.faraopro.play.frg.mode.TimerDeleteFragment;
import jp.faraopro.play.frg.mode.TimerEditFragment;
import jp.faraopro.play.frg.mode.TimerFragment;
import jp.faraopro.play.frg.mode.TimerImportFragment;
import jp.faraopro.play.frg.mode.VolumeControlFragment;
import jp.faraopro.play.frg.mode.WeekFragment;
import jp.faraopro.play.frg.newone.HeaderFragment;
import jp.faraopro.play.frg.newone.InterruptDialogFragment;
import jp.faraopro.play.frg.newone.MenuFragment;
import jp.faraopro.play.mclient.MCDefAction;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCError;
import jp.faraopro.play.mclient.MCTemplateItem;
import jp.faraopro.play.mclient.MCUserInfoPreference;
import jp.faraopro.play.model.ChannelMode;
import jp.faraopro.play.model.ChannelParams;
import jp.faraopro.play.util.FROUtils;
import jp.faraopro.play.util.Utils;
import jp.faraopro.play.view.CustomListItem;

/**
 * メイン画面を構成するアクティビティ<br>
 *
 * @author AIM Corporation
 */
public class MainActivity extends BaseActivity implements IModeActivity, MenuFragment.OnBtnClickListener,
        HeaderFragment.OnBtnClickListener, InterruptDialogFragment.InterruptDialogListener {
    private static final boolean DEBUG = true;

    /**
     * dialog tag
     **/
    public static final int DIALOG_TAG_CART = 1000;
    public static final int DIALOG_TAG_SHARE_HISTORY = 1002;
    public static final int DIALOG_TAG_SHARE_TYPE = 1003;
    public static final int DIALOG_TAG_GOTO_SETTING_CANCEL = 1005;
    public static final int DIALOG_TAG_SLEEP_TIMER = 1006;
    public static final int DIALOG_TAG_LOGOUT_POS = 1009;
    public static final int DIALOG_TAG_LOGOUT_NEG = 1010;
    public static final int DIALOG_TAG_SKIP_LIMIT = 1011;
    public static final int DIALOG_TAG_BOOT_INTENT_POS = 1012;
    public static final int DIALOG_TAG_BOOT_INTENT_NEG = 1013;
    public static final int DIALOG_TAG_ADD_TICKET_POS = 1014;
    public static final int DIALOG_TAG_ADD_TICKET_NEG = 1015;
    public static final int DIALOG_TAG_NO_NEXT_MUSIC = 1016;
    public static final int DIALOG_TAG_NO_NEXT_MUSIC_BENIFIT = 1017;
    public static final int DIALOG_TAG_TERM_APP_POS = 1018;
    public static final int DIALOG_TAG_TERM_APP_NEG = 1019;
    public static final int DIALOG_TAG_ADD_TO_FAVORITE_POS = 1020;
    public static final int DIALOG_TAG_ADD_TO_FAVORITE_NEG = 1021;
    public static final int DIALOG_TAG_NO_NEXT_MUSIC_LOCAL = 1022;
    public static final int DIALOG_TAG_NO_NEXT_MUSIC_FAVORITE = 1023;
    public static final int DIALOG_TAG_IMPORT_FAVORITE_POS = 1024;
    public static final int DIALOG_TAG_IMPORT_FAVORITE_NEG = 1025;
    public static final int DIALOG_TAG_IMPORT_TIMER_POS = 1026;
    public static final int DIALOG_TAG_IMPORT_TIMER_NEG = 1027;
    public static final int DIALOG_TAG_UNMAUNTED_STRAGE = 1028;
    public static final int DIALOG_TAG_FINISH_OFFLINE_PLAYER_POS = 1029;
    public static final int DIALOG_TAG_FINISH_OFFLINE_PLAYER_NEG = 1030;
    public static final int DIALOG_TAG_FAILED_TO_NETWORK_RECOVER = 1031;
    public static final int DIALOG_TAG_STOP_AUTO_UPDATE_TIMER = 1032;
    public static final int DIALOG_TAG_DELETE_TIMER_POS = 1033;
    public static final int DIALOG_TAG_DELETE_TIMER_NEG = 1034;
    public static final int DIALOG_TAG_EJECT_STORAGE = 1035;
    public static final int DIALOG_TAG_PATTERN_FORCE_UPDATE = 1036;

    /**
     * const
     **/
    public static final int TOP = 0;
    public static final int GENRE = 1;
    public static final int GENRE2 = 2;
    public static final int GENRE3 = 3;
    public static final int SPECIAL = 4;
    public static final int HISTORY = 5;
    public static final int OTHER = 6;
    public static final int RELEASE = 7;
    public static final int SETTING = 8;
    public static final int PLAYER = 9;
    public static final int DRAG = 10;
    public static final int FAVORITE_LOCAL = 11;
    public static final int ARTIST = 12;
    //    public static final int MYPAGE = 13;
    public static final int FAVORITE_EDIT = 14;
    public static final int FAVORITE_DELETE = 15;
    // public static final int SLEEP = 16;
    public static final int ABOUT = 17;
    public static final int TIMER_EDIT = 18;
    public static final int WEEK = 19;
    public static final int TIMER = 20;
    public static final int LOCAL_PLAYER = 21;
    public static final int TIMER_SELECT_SOURCE = 22;
    public static final int TIMER_SELECT_FAVORITE = 23;
    public static final int TIMER_SELECT_LOCAL = 24;
    public static final int TIMER_DELETE = 25;
    public static final int FAVORITE = 26;
    public static final int FAVORITE_TEMPLATE = 27;
    public static final int IMPORT_FAVORITE_TEMPLATE = 28;
    public static final int IMPORT_TIMER_TEMPLATE = 29;
    public static final int FAVORITE_TEMPLATE_DELETE = 30;
    public static final int STREAMING = 31;
    public static final int STREAM_PLAYER = 32;
    public static final int OFFLINE_PLAYER = 33;
    public static final int PATTERN_PARENT = 34;
    public static final int VOLUME_CONTROL = 35;
    public static final int OTHER_SETTING = 36;
    public static final int FRAGMENT_SIZE = OTHER_SETTING + 1;

    public static final int SHARE_ON_FACEBOOK = 0;
    public static final int SHARE_ON_TWITTER = 1;
    public static final int SHARE_ON_MAIL = 2;

    /**
     * fragment tag
     **/
    public static final String TAG_FRG_HEADER = "HEADER";
    public static final String TAG_FRG_MENU = "MENU";
    public static final String TAG_FRG_TOP = "TOP";
    public static final String TAG_FRG_GENRE = "GENRE";
    public static final String TAG_FRG_GENRE2 = "GENRE2";
    public static final String TAG_FRG_GENRE3 = "GENRE3";
    public static final String TAG_FRG_SPECIAL = "SPECIAL";
    public static final String TAG_FRG_HISTORY = "HISTORY";
    public static final String TAG_FRG_FAVORITE_LOCAL = "FAVORITE_LOCAL";
    public static final String TAG_FRG_OTHER = "OTHER";
    public static final String TAG_FRG_RELEASE = "RELEASE";
    public static final String TAG_FRG_SETTING = "SETTING";
    public static final String TAG_FRG_PLAYER = "PLAYER";
    public static final String TAG_FRG_DRAG = "DRAG";
    public static final String TAG_FRG_ARTIST = "ARTIST";
    //    public static final String TAG_FRG_MYPAGE = "MYPAGE";
    public static final String TAG_FRG_FAVORITE_EDIT = "FAVORITE_EDIT";
    public static final String TAG_FRG_FAVORITE_DELETE = "FAVORITE_DELETE";
    public static final String TAG_FRG_SLEEP = "SLEEP";
    public static final String TAG_FRG_ABOUT = "ABOUT";
    public static final String TAG_FRG_TIMER_EDIT = "TIMER_EDIT";
    public static final String TAG_FRG_WEEK = "WEEK";
    public static final String TAG_FRG_TIMER = "TIMER";
    public static final String TAG_FRG_LOCAL_PLAYER = "LOCAL_PLAYER";
    public static final String TAG_FRG_TIMER_SELECT_SOURCE = "TIMER_SELECT_SOURCE";
    public static final String TAG_FRG_TIMER_SELECT_FAVORITE = "TIMER_SELECT_FAVORITE";
    public static final String TAG_FRG_TIMER_SELECT_LOCAL = "TIMER_SELECT_LOCAL";
    public static final String TAG_FRG_TIMER_DELETE = "TIMER_DELETE";
    public static final String TAG_FRG_FAVORITE = "FAVORITE";
    public static final String TAG_FRG_FAVORITE_TEMPLATE = "FAVORITE_TEMPLATE";
    public static final String TAG_FRG_IMPORT_FAVORITE_TEMPLATE = "IMPORT_FAVORITE_TEMPLATE";
    public static final String TAG_FRG_IMPORT_TIMER_TEMPLATE = "IMPORT_TIMER_TEMPLATE";
    public static final String TAG_FRG_FAVORITE_TEMPLATE_DELETE = "FAVORITE_TEMPLATE_DELETE";
    public static final String TAG_FRG_STREAMING = "STREAMING";
    public static final String TAG_FRG_STREAM_PLAYER = "STREAM_PLAYER";
    public static final String TAG_FRG_OFFLINE_PLAYER = "OFFLINE_PLAYER";
    public static final String TAG_FRG_PATTERN_PARENT = "PATTERN_PARENT";
    public static final String TAG_FRG_VOLUME_CONTROL = "VOLUME_CONTROL";
    public static final String TAG_FRG_OTHER_SETTING = "OTHER_SETTING";

    public static final String TAG_DIALOG_INTERRUPT = "DIALOG_INTERRUPT";

    /**
     * view
     **/
    private Fragment fragments[] = new Fragment[FRAGMENT_SIZE];
    private HeaderFragment headerFrg;
    private MenuFragment footerFrg;
    private BannerFragment browserFrg;
    private GenreSectionFragment genreFrg;
    private GenreFragment genreFrg2;
    private GenreSubFragment genreFrg3;
    private SpecialFragment specialFrg;
    // private HistoryFragment historyFrg;
    private HistoryParentFragment historyFrg;
    private FavoriteLocalFragment favoriteLocalFragment;
    private OtherFragment otherFrg;
    //private ReleaseFragment releaseFrg;
    private SettingFragment settingFrg;
    private PlayerFragment playerFrg;
    private DragFragment dragFrg;
    //private ArtistFragment artFrg;
    //    private MyPageFragment mypageFrg;
    private FavoriteDeleteFragment favoriteDeleteFrg;
    private FavoriteEditFragment favoriteEditFrg;
    // private SleepTimerFragment sleepFrg;
    private AboutFragment aboutFrg;
    private TimerEditFragment timerEditFrg;
    private WeekFragment weekFrg;
    private TimerFragment timerFrg;
    private LocalPlayerFragment localPlayerFrg;
    private SelectSourceFragment selectSourceFrg;
    private SelectFavoriteFragment selectFavoriteFrg;
    private SelectLocalFragment2 selectLocalFrg;
    private TimerDeleteFragment timerDeleteFrg;
    private FavoriteFragment favoriteFragment;
    private ImportTemplateFragment importTemlpateFragment;
    private TimerImportFragment importTimerTemlpateFragment;
    private FavoriteTemplateFragment favoriteTemplateFragment;
    private FavoriteTemplateDeleteFragment favoriteTemplateDeleteFragment;
    private StreamingFragment streamingFrg;
    private StreamPlayerFragment streamPlayerFrg;
    private OfflinePlayerFragment offlinePlayerFrg;
    // private PatternDetailFragment mPatternDetailFrg;
    private PatternParentFragment patternParentFragment;
    private VolumeControlFragment volumeControlFragment;
    private OtherSettingFragment otherSettingFragment;

    /**
     * member
     **/
    public static int menuSize = -1;
    private IModeFragment displayedFrg;
    private int displayedTag = -1;
    private int shareWith;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getPointerCount() > 1)
            return true;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (playerFrg != null && playerFrg.isVisible()) {
            if (keyCode == KeyEvent.KEYCODE_BACK && FROForm.getInstance().isPlayingAds()) {
                return true;
            }
        } else if (offlinePlayerFrg != null && offlinePlayerFrg.isVisible()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                onBtnClicked(R.id.header_btn_left);
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (browserFrg != null && browserFrg.isVisible()) {
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        } else if (offlinePlayerFrg != null && offlinePlayerFrg.isVisible()) {
            onBtnClicked(R.id.header_btn_left);
        } else {
            if (displayedFrg != null)
                displayedFrg.onBackPress();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // システムやクリーナーによってService以外のメモリが解放されていた場合
        if (savedInstanceState != null) {
            // 一旦Bootを起動して初期化を行う
            startActivity(BootActivity.class, true);
            return;
        }

        setContentView(R.layout.new_lay_base);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        // ヘッダー
        headerFrg = (HeaderFragment) manager.findFragmentByTag(TAG_FRG_HEADER);
        if (headerFrg == null) {
            headerFrg = new HeaderFragment();
            Bundle headerArgs = new Bundle();
            headerArgs.putBoolean(HeaderFragment.LEFT_VISIBLE, false);
            headerArgs.putBoolean(HeaderFragment.RIGHT_VISIBLE, false);
            headerArgs.putInt(HeaderFragment.CAPTION, R.drawable.logo_white);
            headerFrg.setArguments(headerArgs);
            transaction.add(R.id.base_lnr_header, headerFrg, TAG_FRG_HEADER);
        }
        // メニュー
        footerFrg = (MenuFragment) manager.findFragmentByTag(TAG_FRG_MENU);
        if (footerFrg == null) {
            footerFrg = new MenuFragment();
            transaction.add(R.id.base_lnr_footer, footerFrg, TAG_FRG_MENU);
        }
        // HOME
        browserFrg = (BannerFragment) manager.findFragmentByTag(TAG_FRG_TOP);
        if (browserFrg == null) {
            browserFrg = new BannerFragment();
            transaction.add(R.id.base_lnr_body, browserFrg, TAG_FRG_TOP);
            transaction.hide(browserFrg);
        }
        // GENRE
        genreFrg = (GenreSectionFragment) manager.findFragmentByTag(TAG_FRG_GENRE);
        if (genreFrg == null) {
            genreFrg = new GenreSectionFragment();
            transaction.add(R.id.base_lnr_body, genreFrg, TAG_FRG_GENRE);
            transaction.hide(genreFrg);
        }
        // GENRE2
        genreFrg2 = (GenreFragment) manager.findFragmentByTag(TAG_FRG_GENRE2);
        if (genreFrg2 == null) {
            genreFrg2 = new GenreFragment();
            transaction.add(R.id.base_lnr_body, genreFrg2, TAG_FRG_GENRE2);
            transaction.hide(genreFrg2);
        }
        // GENRE3
        genreFrg3 = (GenreSubFragment) manager.findFragmentByTag(TAG_FRG_GENRE3);
        if (genreFrg3 == null) {
            genreFrg3 = new GenreSubFragment();
            transaction.add(R.id.base_lnr_body, genreFrg3, TAG_FRG_GENRE3);
            transaction.hide(genreFrg3);
        }

        // PRO SPECIAL
        specialFrg = (SpecialFragment) manager.findFragmentByTag(TAG_FRG_SPECIAL);
        if (specialFrg == null) {
            specialFrg = new SpecialFragment();
            transaction.add(R.id.base_lnr_body, specialFrg, TAG_FRG_SPECIAL);
            transaction.hide(specialFrg);
        }

        // HISTORY
        historyFrg = (HistoryParentFragment) manager.findFragmentByTag(TAG_FRG_HISTORY);
        if (historyFrg == null) {
            historyFrg = new HistoryParentFragment();
            Bundle args = new Bundle();
            args.putString(HistoryParentFragment.LEFT_TAB_NAME, getString(R.string.btn_channel_history_pro));
        /*
        args.putString(HistoryParentFragment.RIGHT_TAB_NAME, getString(R.string.btn_good_history_pro));
        args.putString(HistoryParentFragment.CENTER_TAB_NAME, getString(R.string.btn_play_history));
        */
            args.putString(HistoryParentFragment.RIGHT_TAB_NAME, getString(R.string.btn_play_history));
            historyFrg.setArguments(args);
            transaction.add(R.id.base_lnr_body, historyFrg, TAG_FRG_HISTORY);
            transaction.hide(historyFrg);
        }
        // MYCHANNEL
        favoriteLocalFragment = (FavoriteLocalFragment) manager.findFragmentByTag(TAG_FRG_FAVORITE_LOCAL);
        if (favoriteLocalFragment == null) {
            favoriteLocalFragment = new FavoriteLocalFragment();
            transaction.add(R.id.base_lnr_body, favoriteLocalFragment, TAG_FRG_FAVORITE_LOCAL);
            transaction.hide(favoriteLocalFragment);
        }
        // OTHER
        otherFrg = (OtherFragment) manager.findFragmentByTag(TAG_FRG_OTHER);
        if (otherFrg == null) {
            otherFrg = new OtherFragment();
            Bundle args = new Bundle();
            args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_OTHER);
            otherFrg.setArguments(args);
            transaction.add(R.id.base_lnr_body, otherFrg, TAG_FRG_OTHER);
            transaction.hide(otherFrg);
        }
        // RELEASE
//        releaseFrg = (ReleaseFragment) manager.findFragmentByTag(TAG_FRG_RELEASE);
//        if (releaseFrg == null) {
//            releaseFrg = new ReleaseFragment();
//            transaction.add(R.id.base_lnr_body, releaseFrg, TAG_FRG_RELEASE);
//            transaction.hide(releaseFrg);
//        }
        // SETTING
        settingFrg = (SettingFragment) manager.findFragmentByTag(TAG_FRG_SETTING);
        if (settingFrg == null) {
            settingFrg = new SettingFragment();
            Bundle args = new Bundle();
            args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_SETTING);
            settingFrg.setArguments(args);
            transaction.add(R.id.base_lnr_body, settingFrg, TAG_FRG_SETTING);
            transaction.hide(settingFrg);
        }
        // PLAYER
        playerFrg = (PlayerFragment) manager.findFragmentByTag(TAG_FRG_PLAYER);
        if (playerFrg == null) {
            playerFrg = new PlayerFragment();
            transaction.add(R.id.base_lnr_body, playerFrg, TAG_FRG_PLAYER);
            transaction.hide(playerFrg);
        }
        // DRAG
        dragFrg = (DragFragment) manager.findFragmentByTag(TAG_FRG_DRAG);
        if (dragFrg == null) {
            dragFrg = new DragFragment();
            transaction.add(R.id.base_lnr_body, dragFrg, TAG_FRG_DRAG);
            transaction.hide(dragFrg);
        }
        // ARTIST
//        artFrg = (ArtistFragment) manager.findFragmentByTag(TAG_FRG_ARTIST);
//        if (artFrg == null) {
//            artFrg = new ArtistFragment();
//            Bundle args = new Bundle();
//            args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_IMAGE);
//            artFrg.setArguments(args);
//            transaction.add(R.id.base_lnr_body, artFrg, TAG_FRG_ARTIST);
//            transaction.hide(artFrg);
//        }
        favoriteDeleteFrg = (FavoriteDeleteFragment) manager.findFragmentByTag(TAG_FRG_FAVORITE_DELETE);
        if (favoriteDeleteFrg == null) {
            favoriteDeleteFrg = new FavoriteDeleteFragment();
            Bundle args = new Bundle();
            args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_DELETE);
            favoriteDeleteFrg.setArguments(args);
            transaction.add(R.id.base_lnr_body, favoriteDeleteFrg, TAG_FRG_FAVORITE_DELETE);
            transaction.hide(favoriteDeleteFrg);
        }

        favoriteEditFrg = (FavoriteEditFragment) manager.findFragmentByTag(TAG_FRG_FAVORITE_EDIT);
        if (favoriteEditFrg == null) {
            favoriteEditFrg = new FavoriteEditFragment();
            transaction.add(R.id.base_lnr_body, favoriteEditFrg, TAG_FRG_FAVORITE_EDIT);
            transaction.hide(favoriteEditFrg);
        }

        // SLEEP
        // sleepFrg = (SleepTimerFragment)
        // manager.findFragmentByTag(TAG_FRG_SLEEP);
        // if (sleepFrg == null) {
        // sleepFrg = new SleepTimerFragment();
        // transaction.add(R.id.base_lnr_body, sleepFrg, TAG_FRG_SLEEP);
        // transaction.hide(sleepFrg);
        // }
        // ABOUT
        aboutFrg = (AboutFragment) manager.findFragmentByTag(TAG_FRG_ABOUT);
        if (aboutFrg == null) {
            aboutFrg = new AboutFragment();
            transaction.add(R.id.base_lnr_body, aboutFrg, TAG_FRG_ABOUT);
            transaction.hide(aboutFrg);
        }
        // TIMER
        timerEditFrg = (TimerEditFragment) manager.findFragmentByTag(TAG_FRG_TIMER_EDIT);
        if (timerEditFrg == null) {
            timerEditFrg = new TimerEditFragment();
            transaction.add(R.id.base_lnr_body, timerEditFrg, TAG_FRG_TIMER_EDIT);
            transaction.hide(timerEditFrg);
        }
        // OTHER
        weekFrg = (WeekFragment) manager.findFragmentByTag(TAG_FRG_WEEK);
        if (weekFrg == null) {
            weekFrg = new WeekFragment();
            Bundle args = new Bundle();
            args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_CHECK);
            weekFrg.setArguments(args);
            transaction.add(R.id.base_lnr_body, weekFrg, TAG_FRG_WEEK);
            transaction.hide(weekFrg);
        }
        // TIMER
        timerFrg = (TimerFragment) manager.findFragmentByTag(TAG_FRG_TIMER);
        if (timerFrg == null) {
            timerFrg = new TimerFragment();
            Bundle args = new Bundle();
            args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_TIMER);
            timerFrg.setArguments(args);
            transaction.add(R.id.base_lnr_body, timerFrg, TAG_FRG_TIMER);
            transaction.hide(timerFrg);
        }
        // PLAYER
        localPlayerFrg = (LocalPlayerFragment) manager.findFragmentByTag(TAG_FRG_LOCAL_PLAYER);
        if (localPlayerFrg == null) {
            localPlayerFrg = new LocalPlayerFragment();
            transaction.add(R.id.base_lnr_body, localPlayerFrg, TAG_FRG_LOCAL_PLAYER);
            transaction.hide(localPlayerFrg);
        }

        selectSourceFrg = (SelectSourceFragment) manager.findFragmentByTag(TAG_FRG_TIMER_SELECT_SOURCE);
        if (selectSourceFrg == null) {
            selectSourceFrg = new SelectSourceFragment();
            transaction.add(R.id.base_lnr_body, selectSourceFrg, TAG_FRG_TIMER_SELECT_SOURCE);
            transaction.hide(selectSourceFrg);
        }

        selectFavoriteFrg = (SelectFavoriteFragment) manager.findFragmentByTag(TAG_FRG_TIMER_SELECT_FAVORITE);
        if (selectFavoriteFrg == null) {
            selectFavoriteFrg = new SelectFavoriteFragment();
            transaction.add(R.id.base_lnr_body, selectFavoriteFrg, TAG_FRG_TIMER_SELECT_FAVORITE);
            transaction.hide(selectFavoriteFrg);
        }

        selectLocalFrg = (SelectLocalFragment2) manager.findFragmentByTag(TAG_FRG_TIMER_SELECT_LOCAL);
        if (selectLocalFrg == null) {
            selectLocalFrg = new SelectLocalFragment2();
            Bundle args = new Bundle();
            args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_FOLDER);
            selectLocalFrg.setArguments(args);
            transaction.add(R.id.base_lnr_body, selectLocalFrg, TAG_FRG_TIMER_SELECT_LOCAL);
            transaction.hide(selectLocalFrg);
        }

        timerDeleteFrg = (TimerDeleteFragment) manager.findFragmentByTag(TAG_FRG_TIMER_DELETE);
        if (timerDeleteFrg == null) {
            timerDeleteFrg = new TimerDeleteFragment();
            Bundle args = new Bundle();
            args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_DELETE_TIMER);
            timerDeleteFrg.setArguments(args);
            transaction.add(R.id.base_lnr_body, timerDeleteFrg, TAG_FRG_TIMER_DELETE);
            transaction.hide(timerDeleteFrg);
        }
        favoriteFragment = (FavoriteFragment) manager.findFragmentByTag(TAG_FRG_FAVORITE);
        if (favoriteFragment == null) {
            favoriteFragment = new FavoriteFragment();
            transaction.add(R.id.base_lnr_body, favoriteFragment, TAG_FRG_FAVORITE);
            transaction.hide(favoriteFragment);
        }
        importTemlpateFragment = (ImportTemplateFragment) manager.findFragmentByTag(TAG_FRG_IMPORT_FAVORITE_TEMPLATE);
        if (importTemlpateFragment == null) {
            importTemlpateFragment = new ImportTemplateFragment();
            transaction.add(R.id.base_lnr_body, importTemlpateFragment, TAG_FRG_IMPORT_FAVORITE_TEMPLATE);
            transaction.hide(importTemlpateFragment);
        }
        favoriteTemplateFragment = (FavoriteTemplateFragment) manager.findFragmentByTag(TAG_FRG_FAVORITE_TEMPLATE);
        if (favoriteTemplateFragment == null) {
            favoriteTemplateFragment = new FavoriteTemplateFragment();
            transaction.add(R.id.base_lnr_body, favoriteTemplateFragment, TAG_FRG_FAVORITE_TEMPLATE);
            transaction.hide(favoriteTemplateFragment);
        }

        importTimerTemlpateFragment = (TimerImportFragment) manager.findFragmentByTag(TAG_FRG_IMPORT_TIMER_TEMPLATE);
        if (importTimerTemlpateFragment == null) {
            importTimerTemlpateFragment = new TimerImportFragment();
            transaction.add(R.id.base_lnr_body, importTimerTemlpateFragment, TAG_FRG_IMPORT_TIMER_TEMPLATE);
            transaction.hide(importTimerTemlpateFragment);
        }

        favoriteTemplateDeleteFragment = (FavoriteTemplateDeleteFragment) manager
                .findFragmentByTag(TAG_FRG_FAVORITE_TEMPLATE_DELETE);
        if (favoriteTemplateDeleteFragment == null) {
            favoriteTemplateDeleteFragment = new FavoriteTemplateDeleteFragment();
            Bundle args = new Bundle();
            args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_DELETE);
            favoriteTemplateDeleteFragment.setArguments(args);
            transaction.add(R.id.base_lnr_body, favoriteTemplateDeleteFragment, TAG_FRG_FAVORITE_TEMPLATE_DELETE);
            transaction.hide(favoriteTemplateDeleteFragment);
        }

        streamingFrg = (StreamingFragment) manager.findFragmentByTag(TAG_FRG_STREAMING);
        if (streamingFrg == null) {
            streamingFrg = new StreamingFragment();
            transaction.add(R.id.base_lnr_body, streamingFrg, TAG_FRG_STREAMING);
            transaction.hide(streamingFrg);
        }

        streamPlayerFrg = (StreamPlayerFragment) manager.findFragmentByTag(TAG_FRG_STREAM_PLAYER);
        if (streamPlayerFrg == null) {
            streamPlayerFrg = new StreamPlayerFragment();
            transaction.add(R.id.base_lnr_body, streamPlayerFrg, TAG_FRG_STREAM_PLAYER);
            transaction.hide(streamPlayerFrg);
        }

        // PLAYER
        offlinePlayerFrg = (OfflinePlayerFragment) manager.findFragmentByTag(TAG_FRG_OFFLINE_PLAYER);
        if (offlinePlayerFrg == null) {
            offlinePlayerFrg = new OfflinePlayerFragment();
            transaction.add(R.id.base_lnr_body, offlinePlayerFrg, TAG_FRG_OFFLINE_PLAYER);
            transaction.hide(offlinePlayerFrg);
        }
        patternParentFragment = (PatternParentFragment) manager.findFragmentByTag(TAG_FRG_PATTERN_PARENT);
        if (patternParentFragment == null) {
            patternParentFragment = PatternParentFragment.newInstance();
            transaction.add(R.id.base_lnr_body, patternParentFragment, TAG_FRG_PATTERN_PARENT);
            transaction.hide(patternParentFragment);
        }
        volumeControlFragment = (VolumeControlFragment) manager.findFragmentByTag(TAG_FRG_VOLUME_CONTROL);
        if (volumeControlFragment == null) {
            volumeControlFragment = VolumeControlFragment.newInstance();
            transaction.add(R.id.base_lnr_body, volumeControlFragment, TAG_FRG_VOLUME_CONTROL);
            transaction.hide(volumeControlFragment);
        }
        otherSettingFragment = (OtherSettingFragment) manager.findFragmentByTag(TAG_FRG_OTHER_SETTING);
        if (otherSettingFragment == null) {
            otherSettingFragment = OtherSettingFragment.newInstance();
            transaction.add(R.id.base_lnr_body, otherSettingFragment, TAG_FRG_OTHER_SETTING);
            transaction.hide(otherSettingFragment);
        }

        fragments[TOP] = browserFrg;
        fragments[GENRE] = genreFrg;
        fragments[GENRE2] = genreFrg2;
        fragments[GENRE3] = genreFrg3;
        fragments[SPECIAL] = specialFrg;
        fragments[HISTORY] = historyFrg;
        fragments[FAVORITE_LOCAL] = favoriteLocalFragment;
        fragments[OTHER] = otherFrg;
//        fragments[RELEASE] = releaseFrg;
        fragments[SETTING] = settingFrg;
        fragments[PLAYER] = playerFrg;
        fragments[DRAG] = dragFrg;
//        fragments[ARTIST] = artFrg;
//        fragments[MYPAGE] = mypageFrg;
        fragments[FAVORITE_EDIT] = favoriteEditFrg;
        fragments[FAVORITE_DELETE] = favoriteDeleteFrg;
        // fragments[SLEEP] = sleepFrg;
        fragments[ABOUT] = aboutFrg;
        fragments[TIMER_EDIT] = timerEditFrg;
        fragments[WEEK] = weekFrg;
        fragments[TIMER] = timerFrg;
        fragments[LOCAL_PLAYER] = localPlayerFrg;
        fragments[TIMER_SELECT_SOURCE] = selectSourceFrg;
        fragments[TIMER_SELECT_FAVORITE] = selectFavoriteFrg;
        fragments[TIMER_SELECT_LOCAL] = selectLocalFrg;
        fragments[TIMER_DELETE] = timerDeleteFrg;
        fragments[FAVORITE] = favoriteFragment;
        fragments[IMPORT_FAVORITE_TEMPLATE] = importTemlpateFragment;
        fragments[FAVORITE_TEMPLATE] = favoriteTemplateFragment;
        fragments[IMPORT_TIMER_TEMPLATE] = importTimerTemlpateFragment;
        fragments[FAVORITE_TEMPLATE_DELETE] = favoriteTemplateDeleteFragment;
        fragments[STREAMING] = streamingFrg;
        fragments[STREAM_PLAYER] = streamPlayerFrg;
        fragments[OFFLINE_PLAYER] = offlinePlayerFrg;
        fragments[PATTERN_PARENT] = patternParentFragment;
        fragments[VOLUME_CONTROL] = volumeControlFragment;
        fragments[OTHER_SETTING] = otherSettingFragment;

        transaction.commitAllowingStateLoss();
        manager.executePendingTransactions();

        if (!TimerHelper.checkTemplateTimer(getApplicationContext()))
            TimerHelper.restoreTemplateTimer(getApplicationContext());
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Utils.getNetworkState(getApplicationContext()))
            FROForm.getInstance().getStatus();
        moveTo(getIntent());
        // setIntent(null);
    }

    private void moveTo(Intent intent) {
        Bundle args = null;
        if (intent != null)
            args = intent.getExtras();
        if (args != null) {
            int show = args.getInt("SHOW_MODE", -1);
            switch (show) {
                case SETTING:
                    if (settingFrg != null && settingFrg.isAdded()) {
                        showMode(SETTING);
                        footerFrg.setSelected(4);
                        intent.putExtra("SHOW_MODE", -1);
                    }
                    break;
                case PLAYER:
                case LOCAL_PLAYER:
                case STREAM_PLAYER:
                    showPlayer();
                    intent.putExtra("SHOW_MODE", -1);
                    break;
                case OFFLINE_PLAYER:
                    showMode(OFFLINE_PLAYER);
                    offlinePlayerFrg.updateViews();
                    intent.putExtra("SHOW_MODE", -1);
                    break;
                default:
                    if (FROForm.getInstance().getNowPlaying() != null
                            && FROForm.getInstance().getNowPlaying().getPlayerType() == FROPlayer.PLAYER_TYPE_OFFLINE) {
                        showMode(OFFLINE_PLAYER);
                        offlinePlayerFrg.updateViews();
                    } else if (browserFrg != null && browserFrg.isAdded()) {
                        showMode(TOP);
                        footerFrg.setSelected(0);
                    }
                    if (FROForm.getInstance().isInterruptPlaying()) {
                        showInterruptDialog(FROForm.getInstance().getInterruptIndex(),
                                FROForm.getInstance().getInterruptPosition());
                    }
                    break;
            }
        }
    }

    public static final int ANIM_HIDE_SLIDE_CTL = 201;
    public static final int ANIM_HIDE_SLIDE_CTR = 202;
    public static final int ANIM_HIDE_FLICK = 203;
    public static final int ANIM_SHOW_SLIDE_LTC = 301;
    public static final int ANIM_SHOW_SLIDE_RTC = 302;
    public static final int ANIM_SHOW_FLICK = 303;

    /**
     * @param tag         表示する画面のタグ
     * @param isBackStack true:バックスタックする, false:バックスタックしない
     */
    private void changeFragment(int tag, boolean isBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (displayedFrg != null)
            transaction.hide((Fragment) displayedFrg);
        // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        if ((displayedTag == GENRE && tag == GENRE2) || (displayedTag == GENRE2 && tag == GENRE3)) {
            transaction.setTransition(ANIM_HIDE_SLIDE_CTL);
        }
        if ((displayedTag == GENRE2 && tag == GENRE) || (displayedTag == GENRE3 && tag == GENRE2)) {
            transaction.setTransition(ANIM_HIDE_SLIDE_CTR);
        }
        if (tag == PLAYER || displayedTag == PLAYER) {
            transaction.setTransition(ANIM_HIDE_FLICK);
        }
        transaction.commitAllowingStateLoss();

        transaction = getSupportFragmentManager().beginTransaction();
        if ((displayedTag == GENRE && tag == GENRE2) || (displayedTag == GENRE2 && tag == GENRE3)) {
            transaction.setTransition(ANIM_SHOW_SLIDE_RTC);
        }
        if ((displayedTag == GENRE2 && tag == GENRE) || (displayedTag == GENRE3 && tag == GENRE2)) {
            transaction.setTransition(ANIM_SHOW_SLIDE_LTC);
        }
        if (tag == PLAYER || displayedTag == PLAYER) {
            transaction.setTransition(ANIM_SHOW_FLICK);
        }
        for (int i = 0; i < fragments.length; i++) {
            if (i == tag) {
                transaction.show(fragments[i]);
                setCaption(tag);
                displayedFrg = (IModeFragment) fragments[i];
                displayedTag = tag;
                break;
            }
        }
        // if (tag != MYCHANNEL_SELECT)
        // transaction
        // .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 各モードの画面を表示する
     *
     * @param tag         表示する画面のタグ
     * @param isBackStack true:スタックする, false:スタックしない
     */
    public void showMode(int tag, boolean isBackStack) {
        updateHeaderIcon(tag);
        changeFragment(tag, isBackStack);
    }

    private void updateHeaderIcon(int tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MusicInfo nowplaying;
        headerFrg.changeColor(R.color.jp_farao_theme);
        switch (tag) {
            case TOP:
                if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                    headerFrg.setRightVisibility(true);
                    headerFrg.setRightText("Play");
                } else {
                    headerFrg.setRightVisibility(false);
                    headerFrg.setRightImageVisibility(false);
                }
                headerFrg.setLeftVisibility(true);
                headerFrg.setLeftText(getString(R.string.menu_close));
                if (footerFrg != null && footerFrg.isHidden()) {
                    transaction.show(footerFrg);
                }
                footerFrg.setSelected(0);
                break;
            case OTHER:
                headerFrg.setRightVisibility(true);
                headerFrg.setRightText(getString(R.string.page_title_mychannel_edit));
                headerFrg.setLeftVisibility(false);
                if (footerFrg != null && footerFrg.isHidden()) {
                    transaction.show(footerFrg);
                }
                break;
            case PLAYER:
                nowplaying = FROForm.getInstance().getNowPlaying();
                // 音声広告再生中の場合
                if (FROForm.getInstance().isPlayingAds()) {
                    // ボタンを非表示にする
                    headerFrg.setLeftVisibility(false);
                    headerFrg.setLeftImageVisibility(false);
                }
                // それ以外の場合
                else {
                    // ホームボタンを表示する
                    headerFrg.setLeftVisibility(true);
                    headerFrg.setLeftText(getString(R.string.page_title_home));
                }
                // 音声広告再生中である、またはアフィリエイト先がない場合
                if (nowplaying == null || !nowplaying.isContainBuyCd() || FROForm.getInstance().isPlayingAds()) {
                    // ボタンを非表示にする
                    headerFrg.setRightVisibility(false);
                    headerFrg.setRightImageVisibility(false);
                }
                // それ以外の場合
                else {
                    // カートボタンを表示する
                    headerFrg.setRightVisibility(true);
                    headerFrg.setRightText("Cart");
                }
                transaction.hide(footerFrg);
                break;
            case OFFLINE_PLAYER:
                headerFrg.changeColor(R.color.jp_farao_gray_offline);
                // ホームボタンを表示する
                headerFrg.setLeftVisibility(true);
                headerFrg.setLeftText(getString(R.string.page_title_home));
                // ボタンを非表示にする
                headerFrg.setRightVisibility(false);
                headerFrg.setRightImageVisibility(false);
                transaction.hide(footerFrg);
                break;
            case LOCAL_PLAYER:
                // ホームボタンを表示する
                headerFrg.setLeftVisibility(true);
                headerFrg.setLeftText(getString(R.string.page_title_home));
                // ボタンを非表示にする
                headerFrg.setRightVisibility(false);
                headerFrg.setRightImageVisibility(false);
                transaction.hide(footerFrg);
                break;
            case STREAM_PLAYER:
                nowplaying = FROForm.getInstance().getNowPlaying();
                // ホームボタンを表示する
                headerFrg.setLeftVisibility(true);
                headerFrg.setLeftText(getString(R.string.page_title_home));
                // ボタンを非表示にする
                if (nowplaying != null && streamPlayerFrg.hasLink())
                    headerFrg.setRightText("Info");
                else {
                    headerFrg.setRightVisibility(false);
                    headerFrg.setRightImageVisibility(false);
                }
                transaction.hide(footerFrg);
                break;
            case DRAG:
                headerFrg.setLeftVisibility(true);
                headerFrg.setLeftText("<" + getString(R.string.page_title_more));
                headerFrg.setRightVisibility(false);
                headerFrg.setRightImageVisibility(false);
                transaction.hide(footerFrg);
                break;
            case PATTERN_PARENT:
                headerFrg.setLeftVisibility(true);
                headerFrg.setLeftText("<" + getString(R.string.menu_setting));
                headerFrg.setRightVisibility(true);
                headerFrg.setRightText("Update");
                transaction.show(footerFrg);
                break;
            case TIMER:
                headerFrg.setLeftVisibility(true);
                headerFrg.setLeftText("<" + getString(R.string.menu_setting));
                int timerType = MainPreference.getInstance(getApplicationContext()).getMusicTimerType();
                if (timerType == Consts.MUSIC_TIMER_TYPE_AUTO) {
                    headerFrg.setRightVisibility(false);
                } else {
                headerFrg.setRightVisibility(true);
                headerFrg.setRightText(getString(R.string.page_title_mychannel_edit));
                }
                transaction.show(footerFrg);
                break;
            case TIMER_EDIT:
                headerFrg.setLeftVisibility(true);
                headerFrg.setLeftText("<" + getString(R.string.btn_back));
                headerFrg.setRightVisibility(true);
                headerFrg.setRightText(getString(R.string.btn_done));
                transaction.show(footerFrg);
                break;
            case TIMER_DELETE:
                headerFrg.setLeftVisibility(true);
                headerFrg.setLeftText("<" + getString(R.string.btn_back));
                headerFrg.setRightVisibility(true);
                headerFrg.setRightText(getString(R.string.btn_commit));
                transaction.show(footerFrg);
                break;
            case ABOUT:
                if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                    headerFrg.setRightVisibility(true);
                    headerFrg.setRightText("Play");
                } else {
                    headerFrg.setRightVisibility(false);
                    headerFrg.setRightImageVisibility(false);
                }
                headerFrg.setLeftVisibility(true);
                headerFrg.setLeftText("<" + getString(R.string.menu_setting));
                transaction.show(footerFrg);
                break;
            case SETTING:
                if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                    headerFrg.setRightVisibility(true);
                    headerFrg.setRightText("Play");
                } else {
                    headerFrg.setRightVisibility(false);
                    headerFrg.setRightImageVisibility(false);
                }
                headerFrg.setLeftText("<" + getString(R.string.page_title_more));
                headerFrg.setRightVisibility(false);
                transaction.show(footerFrg);
                break;
            case GENRE:
                if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                    headerFrg.setRightVisibility(true);
                    headerFrg.setRightText("Play");
                } else {
                    headerFrg.setRightVisibility(false);
                    headerFrg.setRightImageVisibility(false);
                }
                if (footerFrg != null && footerFrg.isHidden()) {
                    transaction.show(footerFrg);
                }
                if (FROForm.getInstance().isMenu(Consts.TAG_MODE_GENRE, this)) {
                    headerFrg.setLeftVisibility(false);
                } else {
                    headerFrg.setLeftVisibility(true);
                    headerFrg.setLeftText("<" + getString(R.string.page_title_more));
                }
                break;
            case GENRE2:
                if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                    headerFrg.setRightVisibility(true);
                    headerFrg.setRightText("Play");
                } else {
                    headerFrg.setRightVisibility(false);
                    headerFrg.setRightImageVisibility(false);
                }
                if (footerFrg != null && footerFrg.isHidden()) {
                    transaction.show(footerFrg);
                }
                headerFrg.setLeftVisibility(true);
                headerFrg.setLeftText("<" + getString(R.string.page_title_genre_pro));
                break;
            case GENRE3:
                if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                    headerFrg.setRightVisibility(true);
                    headerFrg.setRightText("Play");
                } else {
                    headerFrg.setRightVisibility(false);
                    headerFrg.setRightImageVisibility(false);
                }
                if (footerFrg != null && footerFrg.isHidden()) {
                    transaction.show(footerFrg);
                }
                headerFrg.setLeftVisibility(true);
                String select;
                if (FROUtils.isPrimaryLanguage()) {
                    select = genreFrg.getContent(FROForm.getInstance().genrePos).getmName();
                } else {
                    select = genreFrg.getContent(FROForm.getInstance().genrePos).getmNameEn();
                }
                headerFrg.setLeftText("<" + select);
                break;
            case ARTIST:
                if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                    headerFrg.setRightVisibility(true);
                    headerFrg.setRightText("Play");
                } else {
                    headerFrg.setRightVisibility(false);
                    headerFrg.setRightImageVisibility(false);
                }
                if (footerFrg != null && footerFrg.isHidden()) {
                    transaction.show(footerFrg);
                }
                if (FROForm.getInstance().isMenu(Consts.TAG_MODE_ARTIST, this)) {
                    headerFrg.setLeftVisibility(false);
                } else {
                    headerFrg.setLeftVisibility(true);
                    headerFrg.setLeftText("<" + getString(R.string.page_title_more));
                }
                break;
            case SPECIAL:
                if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                    headerFrg.setRightVisibility(true);
                    headerFrg.setRightText("Play");
                } else {
                    headerFrg.setRightVisibility(false);
                    headerFrg.setRightImageVisibility(false);
                }
                if (footerFrg != null && footerFrg.isHidden()) {
                    transaction.show(footerFrg);
                }
                if (FROForm.getInstance().isMenu(Consts.TAG_MODE_SPECIAL, this)) {
                    headerFrg.setLeftVisibility(false);
                } else {
                    headerFrg.setLeftVisibility(true);
                    headerFrg.setLeftText("<" + getString(R.string.page_title_more));
                }
                if (specialFrg != null) {
                    specialFrg.updateHeaderIfNeeded();
                }
                footerFrg.setSelected(FROForm.getInstance().getMenuIndex(Consts.TAG_MODE_SPECIAL, this));
                break;
            case HISTORY:
                if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                    headerFrg.setRightVisibility(true);
                    headerFrg.setRightText("Play");
                } else {
                    headerFrg.setRightVisibility(false);
                    headerFrg.setRightImageVisibility(false);
                }
                if (footerFrg != null && footerFrg.isHidden()) {
                    transaction.show(footerFrg);
                }
                if (FROForm.getInstance().isMenu(Consts.TAG_MODE_HISTORY, this)) {
                    headerFrg.setLeftVisibility(false);
                } else {
                    headerFrg.setLeftVisibility(true);
                    headerFrg.setLeftText("<" + getString(R.string.page_title_more));
                }
                break;
            case RELEASE:
                if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                    headerFrg.setRightVisibility(true);
                    headerFrg.setRightText("Play");
                } else {
                    headerFrg.setRightVisibility(false);
                    headerFrg.setRightImageVisibility(false);
                }
                if (footerFrg != null && footerFrg.isHidden()) {
                    transaction.show(footerFrg);
                }
                if (FROForm.getInstance().isMenu(Consts.TAG_MODE_RELEASE, this)) {
                    headerFrg.setLeftVisibility(false);
                } else {
                    headerFrg.setLeftVisibility(true);
                    headerFrg.setLeftText("<" + getString(R.string.page_title_more));
                }
                break;
            case FAVORITE_LOCAL:
                headerFrg.setLeftVisibility(false);
                headerFrg.setLeftText("<" + getString(R.string.page_title_favorite));
                headerFrg.setRightVisibility(true);
                headerFrg.setRightText(getString(R.string.page_title_mychannel_edit));
                transaction.show(footerFrg);
                break;
            case TIMER_SELECT_FAVORITE:
            case TIMER_SELECT_SOURCE:
                if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                    headerFrg.setRightVisibility(true);
                    headerFrg.setRightText("Play");
                } else {
                    headerFrg.setRightVisibility(false);
                    headerFrg.setRightImageVisibility(false);
                }
                headerFrg.setLeftVisibility(true);
                headerFrg.setLeftText("<" + getString(R.string.btn_back));
                transaction.show(footerFrg);
                break;
            case WEEK:
                headerFrg.setLeftVisibility(true);
                headerFrg.setLeftText("<" + getString(R.string.btn_back));
                headerFrg.setRightVisibility(false);
                headerFrg.setRightImageVisibility(false);
                transaction.show(footerFrg);
                break;
            case FAVORITE_DELETE:
                headerFrg.setLeftVisibility(false);
                headerFrg.setLeftText("<" + getString(R.string.page_title_favorite));
                headerFrg.setRightVisibility(true);
                headerFrg.setRightText(getString(R.string.btn_commit));
                transaction.show(footerFrg);
                break;
            case FAVORITE_TEMPLATE_DELETE:
                headerFrg.setLeftVisibility(false);
                headerFrg.setRightVisibility(true);
                headerFrg.setRightText(getString(R.string.btn_commit));
                transaction.show(footerFrg);
                break;
            case FAVORITE_EDIT:
                headerFrg.setLeftVisibility(true);
                headerFrg.setLeftText("<" + getString(R.string.page_title_favorite));
                headerFrg.setRightVisibility(true);
                headerFrg.setRightText(getString(R.string.btn_done));
                transaction.show(footerFrg);
                break;
            case TIMER_SELECT_LOCAL:
                headerFrg.setLeftVisibility(true);
                headerFrg.setLeftText("<" + getString(R.string.btn_back));
                headerFrg.setRightVisibility(false);
                headerFrg.setRightVisibility(true);
                headerFrg.setRightText(getString(R.string.btn_done));
                transaction.show(footerFrg);
                break;
            case IMPORT_TIMER_TEMPLATE:
                headerFrg.setLeftVisibility(false);
                headerFrg.setLeftText("<" + getString(R.string.btn_back));
                if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                    headerFrg.setRightVisibility(true);
                    headerFrg.setRightText("Play");
                } else {
                    headerFrg.setRightVisibility(false);
                    headerFrg.setRightImageVisibility(false);
                }
                break;
            case FAVORITE:
                headerFrg.setRightVisibility(true);
                headerFrg.setRightText(getString(R.string.page_title_mychannel_edit));
                if (FROForm.getInstance().isMenu(Consts.TAG_MODE_MYCHANNEL, this)) {
                    headerFrg.setLeftVisibility(false);
                } else {
                    headerFrg.setLeftVisibility(true);
                    headerFrg.setLeftText("<" + getString(R.string.page_title_more));
                }
                transaction.show(footerFrg);
                break;
            case FAVORITE_TEMPLATE:
            case IMPORT_FAVORITE_TEMPLATE:
                headerFrg.setLeftVisibility(false);
                headerFrg.setLeftText("<" + getString(R.string.btn_back));
                if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                    headerFrg.setRightVisibility(true);
                    headerFrg.setRightText("Play");
                } else {
                    headerFrg.setRightVisibility(false);
                    headerFrg.setRightImageVisibility(false);
                }
                break;
            case VOLUME_CONTROL:
                if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                    headerFrg.setRightVisibility(true);
                    headerFrg.setRightText("Play");
                } else {
                    headerFrg.setRightVisibility(false);
                    headerFrg.setRightImageVisibility(false);
                }
                headerFrg.setLeftVisibility(true);
                headerFrg.setLeftText("<" + getString(R.string.menu_setting));
                transaction.show(footerFrg);
                break;
            case OTHER_SETTING:
                if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                    headerFrg.setRightVisibility(true);
                    headerFrg.setRightText("Play");
                } else {
                    headerFrg.setRightVisibility(false);
                    headerFrg.setRightImageVisibility(false);
                }
                headerFrg.setLeftVisibility(true);
                headerFrg.setLeftText("<" + getString(R.string.menu_setting));
                transaction.show(footerFrg);
                break;
            default:
                if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                    headerFrg.setRightVisibility(true);
                    headerFrg.setRightText("Play");
                } else {
                    headerFrg.setRightVisibility(false);
                    headerFrg.setRightImageVisibility(false);
                }
                headerFrg.setLeftVisibility(false);
                if (footerFrg != null && footerFrg.isHidden()) {
                    transaction.show(footerFrg);
                }
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void setHeaderTitle(String title) {
        if (headerFrg != null)
            headerFrg.setTextCaption(title);
    }

    public void setHeaderLeftButton(String left) {
        if (TextUtils.isEmpty(left)) {
            headerFrg.setLeftImageVisibility(false);
            headerFrg.setLeftVisibility(false);
        } else {
            headerFrg.setLeftText(left);
        }
    }

    public void setHeaderRightButton(String right) {
        if (TextUtils.isEmpty(right)) {
            headerFrg.setRightImageVisibility(false);
            headerFrg.setRightVisibility(false);
        } else if (right.equalsIgnoreCase("Play")) {
            if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                headerFrg.setRightVisibility(true);
                headerFrg.setRightText("Play");
            }
        } else {
            headerFrg.setRightText(right);
        }
    }

    /**
     * 各モードの画面を表示する
     *
     * @param tag 表示する画面のタグ
     */
    @Override
    public void showMode(int tag) {
        if (tag == TOP) {
            dismissListDialog();
            dismissTextBoxDialog();
        }
        this.showMode(tag, false);
    }

    private void setCaption(int tag) {
        int resId = -1;
        String txt = "";
        switch (tag) {
            case GENRE:
                resId = R.string.page_title_genre_pro;
                break;
            case GENRE2:
                if (FROUtils.isPrimaryLanguage())
                    txt = genreFrg.getContent(FROForm.getInstance().genrePos).getmName();
                else
                    txt = genreFrg.getContent(FROForm.getInstance().genrePos).getmNameEn();
                break;
            case GENRE3:
                if (FROUtils.isPrimaryLanguage())
                    txt = genreFrg2.getContent(FROForm.getInstance().genreSubPos + 1).getmName();
                else
                    txt = genreFrg2.getContent(FROForm.getInstance().genreSubPos + 1).getmNameEn();
                break;
            case SPECIAL:
                resId = R.string.page_title_special_pro;
                break;
            case HISTORY:
                resId = R.string.page_title_goodlist_pro;
                break;
//            case RELEASE:
//                resId = R.string.page_title_released_year_pro;
//                break;
            case SETTING:
                resId = R.string.menu_setting;
                break;
            case FAVORITE:
            case FAVORITE_LOCAL:
            case FAVORITE_DELETE:
            case FAVORITE_TEMPLATE_DELETE:
                resId = R.string.page_title_favorite;
                break;
            case FAVORITE_EDIT:
                resId = R.string.page_title_mychannel_edit;
                break;
            // case MYCHANNEL_SELECT:
            // resId = R.string.page_title_mychannel;
            // break;
            // case MYCHANNEL_EDIT:
            // resId = R.string.page_title_mychannel_edit;
            // break;
//            case ARTIST:
//                resId = R.string.page_title_artist;
//                break;
            case OTHER:
                resId = R.string.page_title_more;
                break;
            case DRAG:
                txt = "";
                break;
            case TIMER_SELECT_FAVORITE:
                resId = R.string.page_title_setting_time_shedule_select_favorite;
                break;
            case TIMER_SELECT_LOCAL:
                txt = "";
                break;
            case TIMER:
            case TIMER_DELETE:
                resId = R.string.page_title_setting_time_shedule;
                break;
            case TIMER_EDIT:
                if (FROForm.getInstance().editedTimer != null && FROForm.getInstance().editedTimer.getId() >= 0) {
                    resId = R.string.page_title_edit_timer_info;
                } else {
                    resId = R.string.page_title_setting_time_shedule_new;
                }
                break;
            case TIMER_SELECT_SOURCE:
                resId = R.string.page_title_setting_select_source;
                break;
            case WEEK:
                resId = R.string.page_title_setting_select_weekday;
                break;
            case IMPORT_FAVORITE_TEMPLATE:
                resId = R.string.page_title_favorite_template;
                break;
            case IMPORT_TIMER_TEMPLATE:
                resId = R.string.page_title_setting_time_shedule_template;
                break;
            case FAVORITE_TEMPLATE:
                if (favoriteFragment != null) {
                    MCTemplateItem selected = favoriteFragment.getSelectedChannel();
                    if (selected != null) {
                        if (FROUtils.isPrimaryLanguage())
                            txt = selected.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME);
                        else
                            txt = selected.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN);
                    }
                }
                break;
            case STREAMING:
                resId = R.string.page_title_simulcast_list;
                break;
            case ABOUT:
                resId = R.string.menu_about;
                break;
            case PATTERN_PARENT:
                resId = R.string.msg_interrupt_playing_switch;
                break;
            case VOLUME_CONTROL:
                resId = R.string.cap_volume_setting;
                break;
            case OTHER_SETTING:
                resId = R.string.page_title_setting_enable_autoboot;
                break;
            default:
                resId = R.drawable.logo_white;
                headerFrg.setImageCaption(R.drawable.logo_white);
                return;
        }
        if (resId != -1)
            headerFrg.setTextCaption(resId);
        else
            headerFrg.setTextCaption(txt);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // メニュー部分のフラグメントのサイズを取得し、サイズ分だけWebViewの下段に余白を付ける
        if (footerFrg != null) {
            menuSize = footerFrg.getHeight();
        }
        // if (browserFrg != null && browserFrg.isVisible()) {
        // if (mShowRequest != -1) {
        // showMode(mShowRequest);
        // mShowRequest = -1;
        // } else {
        // showMode(TOP);
        // }
        // }
    }

    /**
     * ビジネスチャンネル(mode 8)のチャンネル権限を考慮して再生する
     *
     * @param mode    チャンネルモード
     * @param channel チャンネル ID
     * @param range   チャンネル年代(年代検索のみ)
     */
    public void bootPlayer(ChannelMode mode, int channel, String range, int permission) {
        // SD カードがマウントされていない場合、エラーメッセージを表示して抜ける
        if (!Utils.externalStrageIsMaunted()) {
            showDialogUnmauntedStrage();
            return;
        }

        FROForm.getInstance().setPlaylistParams(mode.id, channel, range, permission);
        bootPlayerForce();
    }

    /**
     * ビジネスチャンネル(mode 8)のチャンネル権限を無視して再生する
     *
     * @param mode    チャンネルモード
     * @param channel チャンネル ID
     * @param range   チャンネル年代(年代検索のみ)
     */
    public void bootPlayer(ChannelMode mode, int channel, String range) {
        this.bootPlayer(mode, channel, range, 0xffff);
    }

    private void bootPlayerForce() {
        showLoadingProgressSMP(getString(R.string.msg_boot_player), getString(R.string.progress_bar_label));
        if (FROForm.getInstance().bootPlayer()) {
            setStopAtSMP(10);
            startLoadingProgressSMP();
            showMode(MainActivity.PLAYER);
        } else {
            showToast("error:invalid params");
            dismissLoadingProgressSMP();
        }
    }

    public void showDialogUnmauntedStrage() {
        showPositiveDialog(R.string.msg_sdcard_error, DIALOG_TAG_UNMAUNTED_STRAGE, true);
    }

    private int withMessage = -1;

    @Override
    public void onNotifyResult(int when, Object obj) {
        super.onNotifyResult(when, obj);

        switch (when) {
            case Consts.SHOW_PROGRESS:
                dismissPositiveDialog();
                showProgress();
                break;
            case MCDefAction.MCA_KIND_STATUS:
                if (footerFrg != null && footerFrg.isAdded() && footerFrg.isVisible())
                    footerFrg.renewIcons();
                if (settingFrg != null && settingFrg.isAdded() && settingFrg.isVisible()) {
                    dismissProgress();
                    settingFrg.updateViews();
                }
                break;
            case MCDefAction.MCA_KIND_CHANNEL_EXPAND:
                dismissProgress();
                int channelId = (FROForm.getInstance().expandId != null
                        && TextUtils.isDigitsOnly(FROForm.getInstance().expandId))
                        ? Integer.parseInt(FROForm.getInstance().expandId) : 0;
                bootPlayer(ChannelMode.SHARELINK, channelId, null);
                break;
            case Consts.CHANNEL_LIST:
            case Consts.CHANNEL_LIST_BENIFIT:
            case Consts.UPDATE_SESSION_INFO:
            case Consts.CHANNEL_LIST_GENRE:
            case Consts.CHANNEL_LIST_ARTIST:
            case Consts.CHANNEL_LIST_FEATURED:
            case Consts.CHANNEL_LIST_MYCHANNEL:
            case Consts.CHANNEL_LIST_BUSINESS:
            case MCDefAction.MCA_KIND_TEMPLATE_LIST:
            case MCDefAction.MCA_KIND_STREAM_LIST:
                getUiHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (((Fragment) displayedFrg).isVisible()) {
                            dismissProgress();
                            displayedFrg.show();
                        }
                    }
                });
                break;
            case MCDefAction.MCA_KIND_TEMPLATE_DOWNLOAD:
                Integer type = (Integer) obj;
                dismissProgress();
                if (type.intValue() == Consts.TEMPLATE_TYPE_BOOKMARK) {
                    FROForm.getInstance().parentId = null;
                    showMode(FAVORITE);
                } else if (type.intValue() == Consts.TEMPLATE_TYPE_TIMETABLE) {
                    int timerType = MainPreference.getInstance(getApplicationContext()).getMusicTimerType();
                    TimerHelper.setMusicTimer(this);
                    if (timerType == Consts.MUSIC_TIMER_TYPE_AUTO) {
                        TimerHelper.setTemplateTimerFirst(this);
                    }
                    showMode(TIMER);
                }
                break;
            // case Consts.CHANNEL_LIST_BENIFIT:
            // break;
            case MCDefAction.MCA_KIND_SET:
                dismissProgress();
                showMode(FAVORITE_LOCAL);
                break;
            case Consts.START_MUSIC:
                dismissProgress();
                dismissLoadingProgressSMP();

                getUiHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        // 通常プレイヤー以外のプレイヤーが表示されている場合、通常プレイヤーに遷移
                        if ((offlinePlayerFrg != null && offlinePlayerFrg.isVisible())
                                || (streamPlayerFrg != null && streamPlayerFrg.isVisible())
                                || (localPlayerFrg != null && localPlayerFrg.isVisible()))
                            showPlayer();
                        // プレイヤー画面表示中であれば、ヘッダタイトル、カートアイコンを更新
                        if ((playerFrg != null && playerFrg.isVisible())) {
                            if (headerFrg != null && headerFrg.isVisible()) {
                                headerFrg.setLeftText(getString(R.string.page_title_home));
                                MusicInfo nowplaying = FROForm.getInstance().getNowPlaying();
                                if (nowplaying != null && nowplaying.isContainBuyCd()) {
                                    headerFrg.setRightText("Cart");
                                } else {
                                    headerFrg.setRightImageVisibility(false);
                                }
                            }
                            playerFrg.updateViews();
                            playerFrg.initProgress();
                        }
                        // そうでなければ Play アイコンを更新
                        else {
                            if (headerFrg != null && headerFrg.isVisible() && canDisplayingPlayIcon()) {
                                headerFrg.setRightVisibility(true);
                                headerFrg.setRightText("Play");
                            }
                        }
                    }
                });
                break;
            case Consts.START_LOCAL_MUSIC:
                dismissProgress();
                dismissLoadingProgressSMP();

                getUiHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if ((offlinePlayerFrg != null && offlinePlayerFrg.isVisible())
                                || (streamPlayerFrg != null && streamPlayerFrg.isVisible())
                                || (playerFrg != null && playerFrg.isVisible()))
                            showPlayer();
                        if (localPlayerFrg != null && localPlayerFrg.isVisible()) {
                            if (headerFrg != null && headerFrg.isVisible()) {
                                headerFrg.setLeftText(getString(R.string.page_title_home));
                                headerFrg.setRightImageVisibility(false);
                            }
                            localPlayerFrg.updateViews();
                            localPlayerFrg.initProgress();
                        } else {
                            if (headerFrg != null && headerFrg.isVisible() && canDisplayingPlayIcon()) {
                                headerFrg.setRightVisibility(true);
                                headerFrg.setRightText("Play");
                            }
                        }
                    }
                });
                break;
            case Consts.START_OFFLINE_MUSIC:
                dismissProgress();
                dismissLoadingProgressSMP();

                getUiHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (offlinePlayerFrg != null) {
                            if (!offlinePlayerFrg.isVisible())
                                showMode(OFFLINE_PLAYER);
                            if (headerFrg != null && headerFrg.isVisible()) {
                                headerFrg.setLeftText(getString(R.string.page_title_home));
                                headerFrg.setRightImageVisibility(false);
                            }
                            offlinePlayerFrg.updateViews();
                        }
                    }
                });
                MusicInfo info = FROForm.getInstance().getNowPlaying();
                // if (info == null || TextUtils.isEmpty(info.getUrl())) {
                // showPositiveDialog(R.string.msg_offline_player_nocache,
                // BaseActivity.DIALOG_TAG_MESSAGE, true);
                // }
                break;
            case Consts.START_STREAM_MUSIC:
                dismissProgress();
                dismissLoadingProgressSMP();

                getUiHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if ((offlinePlayerFrg != null && offlinePlayerFrg.isVisible())
                                || (localPlayerFrg != null && localPlayerFrg.isVisible())
                                || (playerFrg != null && playerFrg.isVisible()))
                            showPlayer();
                        if (streamPlayerFrg != null && streamPlayerFrg.isVisible()) {
                            if (headerFrg != null && headerFrg.isVisible()) {
                                headerFrg.setLeftText(getString(R.string.page_title_home));
                                if (streamPlayerFrg.hasLink())
                                    headerFrg.setRightText("Info");
                                else {
                                    headerFrg.setRightVisibility(false);
                                    headerFrg.setRightImageVisibility(false);
                                }
                            }
                            streamPlayerFrg.onStartMusic();
                            streamPlayerFrg.updateViews();
                        } else {
                            if (headerFrg != null && headerFrg.isVisible() && canDisplayingPlayIcon()) {
                                headerFrg.setRightVisibility(true);
                                headerFrg.setRightText("Play");
                            }
                        }
                    }
                });
                break;
            case Consts.START_AD:
                dismissProgress();
                getUiHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (playerFrg != null && playerFrg.isVisible()) {
                            if (headerFrg != null && headerFrg.isVisible()) {
                                headerFrg.setLeftImageVisibility(false);
                                headerFrg.setRightImageVisibility(false);
                            }
                            playerFrg.updateViews();
                            playerFrg.initProgress();
                        }
                    }
                });
                break;
            case Consts.START_INTERRUPT:
                final int index = Integer.parseInt(((String) obj));
                getUiHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        showInterruptDialog(index, 0);
                    }
                });
                break;
            case Consts.END_INTERRUPT:
                getUiHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        hideInterruptDialog();
                        updateHeaderIcon(displayedTag);
                    }
                });
                break;
            case Consts.NOTIFY_SKIP_REMAINING:
                dismissProgress();
                getUiHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (playerFrg != null && playerFrg.isVisible()) {
                            playerFrg.updateViews();
                        }
                    }
                });
                break;
            case Consts.NOTIFY_PAST_15SEC:
                getUiHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (playerFrg != null && playerFrg.isVisible())
                            playerFrg.onPast15Sec();
                    }
                });
                break;
            case Consts.NO_SKIP_REMAINING:
                dismissProgress();
                // showPositiveDialog(R.string.msg_skip_limit_reached,
                // DIALOG_TAG_SKIP_LIMIT, true);
                break;
            case MCDefAction.MCA_KIND_CHANNEL_SHARE:
                // if (shareWith == SHARE_ON_TWITTER) {
                // shareOnTwitter(R.string.msg_share_channel_twitter, null);
                // } else {
                // shareOnMail(R.string.msg_share_channel_facebook, null);
                // }
                break;
            case MCDefAction.MCA_KIND_LOGOUT:
                dismissProgress();
                if (withMessage > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(LoginActivity.BUNDLE_KEY_SHOW_DIALOG, withMessage);
                    startActivity(LoginActivity.class, true, bundle);
                    withMessage = -1;
                } else {
                    startActivity(LoginActivity.class, true);
                }
                break;
            case Consts.MUSIC_TERMINATION:
            case Consts.NOTIFY_COMPLETE_CANCEL:
                dismissLoadingProgressSMP();
                if (FROForm.getInstance().termType == FROForm.TERM_TYPE_LOGOUT) {
                    FROForm.getInstance().termType = FROForm.TERM_TYPE_SETTING;
                    FROForm.getInstance().logout();
                } else if (FROForm.getInstance().termType == FROForm.TERM_TYPE_END) {
                    FROForm.getInstance().termType = FROForm.TERM_TYPE_SETTING;
                    FROForm.getInstance().term();
                } else if (FROForm.getInstance().termType == FROForm.TERM_TYPE_BENIFIT) {
                    dismissProgress();
                    FROForm.getInstance().termType = FROForm.TERM_TYPE_SETTING;
                    showMode(SPECIAL);
                } else if (FROForm.getInstance().termType == FROForm.TERM_TYPE_TOP) {
                    dismissProgress();
                    FROForm.getInstance().termType = FROForm.TERM_TYPE_SETTING;
                    showMode(TOP);
                } else if (isBootCancel) {
                    isBootCancel = false;
                    dismissProgress();
                    showMode(TOP);
                } else {
                    dismissProgress();
                    showMode(TOP);
                }
                break;
            case Consts.TERMINATION:
                dismissProgress();
                finish();
                break;
            case Consts.NOTIFY_START_DOWNLOAD:
                stopLoadingProgressSMP();
                setStopAtSMP(90);
                break;
            case Consts.NOTIFY_DOWNLOAD_PROGRESS:
                if (obj != null) {
                    float per = Float.parseFloat((String) obj);
                    setLoadingValueSMP((int) per + 10);
                }
                break;
            case MCDefAction.MCA_KIND_TRACK_DL:
                disableLoadingCancelSMP(false);
                setLoadingValueSMP(90);
                setStopAtSMP(100);
                startLoadingProgressSMP();
                break;
            case Consts.REQUEST_CHANGE_FRAGMENT:
                showProgress();
                int mode = ((Integer) obj).intValue();
                showMode(mode);
                break;
            case Consts.NOTIFY_FAILED_TO_RECOVER:
                dismissProgress();
                dismissLoadingProgressSMP();
                dismissPositiveDialog();
                showPositiveDialog(R.string.msg_offline_player_playover, DIALOG_TAG_FAILED_TO_NETWORK_RECOVER, false);
                break;
            case Consts.NOTIFY_RENEWAL_TIMER_IS_CHANGED:
                if (timerFrg != null && timerFrg.isVisible()) {
                    getUiHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            timerFrg.getData();
                        }
                    });
                }
                break;
            case MCDefAction.MCA_KIND_PATTERN_SCHEDULE:
            case MCDefAction.MCA_KIND_PATTERN_DOWNLOAD:
                if (patternParentFragment != null && patternParentFragment.isVisible()) {
                    dismissProgress();
                    patternParentFragment.show();
                }
                break;
        }
    }

    @Override
    public void onNotifyError(int when, int statusCode) {
        dismissProgress();
        dismissLoadingProgressSMP();
        dismissPositiveDialog();
        if (when == MCDefAction.MCA_KIND_STATUS) {
            // // 設定画面表示時以外は status api のエラーは無視する
            // if (settingFrg == null || !settingFrg.isVisible()) {
            FROForm.getInstance().errorMsg = null;
            return;
            // }
        } else if (when == Consts.CHANNEL_LIST_MYCHANNEL) {
            if (statusCode == Consts.STATUS_NO_DATA) {
                if (displayedFrg != null) {
                    displayedFrg.onError(when, statusCode);
                }
            }
            FROForm.getInstance().errorMsg = null;
        } else if (when == Consts.START_STREAM_MUSIC) {
            if (statusCode == MCError.MC_APPERR_IO_HTTP) {
                // showPositiveDialog(R.string.msg_music_save_error,
                // DIALOG_TAG_NO_NEXT_MUSIC, false);
                FROForm.getInstance().errorMsg = null;
                showProgress();
                FROForm.getInstance().playStream(FROForm.getInstance().streamId);
            } else if (statusCode == MCError.MC_UNAUTHORIZED) {
                showPositiveDialog(R.string.msg_music_save_error, DIALOG_TAG_NO_NEXT_MUSIC, false);
                FROForm.getInstance().errorMsg = null;
            } else {
                super.onNotifyError(when, statusCode);
            }
        } else if (statusCode == MCError.MC_APPERR_IO_HTTP) {
            if (displayedTag == TOP || displayedTag == GENRE || displayedTag == ARTIST || displayedTag == SPECIAL
                    || displayedTag == FAVORITE_LOCAL || displayedTag == STREAMING
                    || displayedTag == IMPORT_FAVORITE_TEMPLATE || displayedTag == IMPORT_TIMER_TEMPLATE) {
                showPositiveDialog(FROForm.getInstance().errorMsg.getMessage(this), -1, true);
                FROForm.getInstance().errorMsg = null;
            } else if (displayedTag == STREAM_PLAYER) {
                showPositiveDialog(R.string.msg_music_save_error, DIALOG_TAG_NO_NEXT_MUSIC, false);
                FROForm.getInstance().errorMsg = null;
            } else {
                super.onNotifyError(when, statusCode);
            }
        } else if (when == MCDefAction.MCA_KIND_TICKET_CHECK || when == MCDefAction.MCA_KIND_TICKET_ADD) {
            FROForm.getInstance().setTicketData(null, null);
            showPositiveDialog(FROForm.getInstance().errorMsg.getMessage(this), -1, true);
            FROForm.getInstance().errorMsg = null;
        } else if (when == MCDefAction.MCA_KIND_LISTEN || when == MCDefAction.MCA_KIND_RATING) {
            dismissLoadingProgressSMP();
            if (statusCode == Consts.STATUS_NO_DATA) {
                dismissProgress();
                dismissLoadingProgressSMP();
                if (ChannelMode.BENIFIT == ChannelParams.getMode()) {
                    showPositiveDialog(R.string.msg_benefit_playlist_empty, DIALOG_TAG_NO_NEXT_MUSIC_BENIFIT, false);
                } else {
                    showPositiveDialog(R.string.msg_playlist_empty, DIALOG_TAG_NO_NEXT_MUSIC, false);
                }
                FROForm.getInstance().errorMsg = null;
            } else if (statusCode == Consts.STATUS_NO_REMAIN_TIMES) {
                if (!LocationHelper.isEnableProvider(getApplicationContext())) {
                    withMessage = R.string.msg_logout_failed_location;
                } else {
                    withMessage = R.string.msg_remaining_time_none;
                }
                if (playerFrg != null)
                    playerFrg.stopTimer();
                FROForm.getInstance().errorMsg = null;
            } else if (statusCode == Consts.NOTIFY_PERMISSION_IS_CHANGED
                    || statusCode == Consts.NOTIFY_FEATURE_IS_CHANGED) {
                int msg = (when == MCDefAction.MCA_KIND_LISTEN) ? R.string.msg_serviceplan_can_not_play
                        : R.string.msg_serviceplan_stop_playing;
                if (playerFrg != null && playerFrg.isVisible()) {
                    showMode(TOP);
                } else {
                    showMode(displayedTag);
                }
                showPositiveDialog(msg, BaseActivity.DIALOG_TAG_MESSAGE, true);
                FROForm.getInstance().errorMsg = null;
                FROForm.getInstance().getStatus();
            } else {
                super.onNotifyError(when, statusCode);
            }
        } else if (when == MCDefAction.MCA_KIND_STREAM_LOGGING || when == MCDefAction.MCA_KIND_STREAM_PLAY) {
            if (statusCode == Consts.STATUS_NO_REMAIN_TIMES) {
                if (!LocationHelper.isEnableProvider(getApplicationContext())) {
                    withMessage = R.string.msg_logout_failed_location;
                } else {
                    withMessage = R.string.msg_remaining_time_none;
                }
                FROForm.getInstance().errorMsg = null;
            }
            if (statusCode == Consts.NOTIFY_FEATURE_IS_CHANGED) {
                int msg = (when == MCDefAction.MCA_KIND_STREAM_PLAY) ? R.string.msg_serviceplan_can_not_play
                        : R.string.msg_serviceplan_stop_playing;
                showPositiveDialog(msg, BaseActivity.DIALOG_TAG_MESSAGE, true);
                FROForm.getInstance().errorMsg = null;
                FROForm.getInstance().getStatus();
            } else {
                super.onNotifyError(when, statusCode);
            }
        } else if (when == Consts.START_LOCAL_MUSIC) {
            if (statusCode == Consts.STATUS_NO_DATA) {
                showPositiveDialog(R.string.msg_error_musicfile_not_found, DIALOG_TAG_NO_NEXT_MUSIC_LOCAL, false);
                FROForm.getInstance().errorMsg = null;
            } else {
                super.onNotifyError(when, statusCode);
            }
        } else if (when == Consts.REQUEST_CHANGE_FRAGMENT) {
            if (statusCode == Consts.STATUS_NO_DATA) {
                showPositiveDialog(R.string.msg_error_musicfile_not_found, DIALOG_TAG_NO_NEXT_MUSIC_FAVORITE, false);
                FROForm.getInstance().errorMsg = null;
            } else {
                super.onNotifyError(when, statusCode);
            }
        } else if (when == Consts.START_MUSIC) {
            if (statusCode == Consts.NO_EXIST_FILE) {
                showPositiveDialog(R.string.msg_sdcard_error, BaseActivity.DIALOG_TAG_MESSAGE, false);
                FROForm.getInstance().errorMsg = null;
            } else {
                super.onNotifyError(when, statusCode);
            }
        } else if (when == Consts.NOTIFY_ERROR) {
            if (statusCode == Consts.EJECT_STORAGE) {
                showPositiveDialog(R.string.msg_sdcard_error, DIALOG_TAG_EJECT_STORAGE, false);
                FROForm.getInstance().errorMsg = null;
            } else {
                super.onNotifyError(when, statusCode);
            }
        } else if (when == MCDefAction.MCA_KIND_NETWORK_RECOVERY) {
            String msg = getString(R.string.msg_system_error) + "(" + statusCode + ")";
            showPositiveDialog(msg, DIALOG_TAG_MESSAGE, false);
            FROForm.getInstance().errorMsg = null;
        } else {
            super.onNotifyError(when, statusCode);
        }
    }

    /**
     * InterruptDialogListener#onViewCreated
     */
    @Override
    public void onViewCreated() {
        FragmentManager manager = getSupportFragmentManager();
        InterruptDialogFragment interruptDialog = (InterruptDialogFragment) manager
                .findFragmentByTag(TAG_DIALOG_INTERRUPT);
        if (interruptDialog != null) {
            interruptDialog.start(FROForm.getInstance().getInterruptIndex(),
                    FROForm.getInstance().getInterruptPosition());
        }
    }

    private void showInterruptDialog(int index, long position) {
        FragmentManager manager = getSupportFragmentManager();
        InterruptDialogFragment interruptDialog = (InterruptDialogFragment) manager
                .findFragmentByTag(TAG_DIALOG_INTERRUPT);
        if (interruptDialog == null) {
            interruptDialog = InterruptDialogFragment.newInstance(R.string.msg_interrupt_playing_audio,
                    FROForm.getInstance().mInterruptAudios);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(interruptDialog, TAG_DIALOG_INTERRUPT);
            transaction.commitAllowingStateLoss();
        } else {
            interruptDialog.start(index, position);
        }
    }

    private void hideInterruptDialog() {
        FragmentManager manager = getSupportFragmentManager();
        InterruptDialogFragment fragment = (InterruptDialogFragment) manager.findFragmentByTag(TAG_DIALOG_INTERRUPT);
        if (fragment != null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(fragment);
            transaction.commitAllowingStateLoss();
        }
    }

    private void showPlayer() {
        MusicInfo info = FROForm.getInstance().getNowPlaying();
        if (info != null) {
            if (info.getPlayerType() == FROPlayer.PLAYER_TYPE_NOMAL && playerFrg != null) {
                showMode(PLAYER);
                playerFrg.updateViews();
                playerFrg.initProgress();
            } else if (info.getPlayerType() == FROPlayer.PLAYER_TYPE_LOCAL && localPlayerFrg != null) {
                showMode(LOCAL_PLAYER);
                localPlayerFrg.updateViews();
                localPlayerFrg.initProgress();
            } else if (info.getPlayerType() == FROPlayer.PLAYER_TYPE_SIMUL && streamPlayerFrg != null) {
                showMode(STREAM_PLAYER);
                streamPlayerFrg.onStartMusic();
                streamPlayerFrg.updateViews();
            }
        }
    }

    @Override
    public void onBtnClicked(int resId) {
        switch (resId) {
            case R.id.header_btn_left:
                if (playerFrg.isVisible() && !FROForm.getInstance().isPlayingAds()) {
                    // 広告再生をしていなければ、トップ画面に遷移する
                    showMode(TOP);
                } else if (browserFrg.isVisible()) {
                    // トップ画面で押下された場合は、アプリ終了確認のダイアログを表示
                    showNegativeDialog(R.string.msg_exit_app_pro, DIALOG_TAG_TERM_APP_POS, DIALOG_TAG_TERM_APP_NEG, true);
                } else if (offlinePlayerFrg.isVisible()) {
                    showNegativeDialog(R.string.msg_offline_player_confirm, DIALOG_TAG_FINISH_OFFLINE_PLAYER_POS,
                            DIALOG_TAG_FINISH_OFFLINE_PLAYER_NEG, true);
                } else {
                    this.onBackPressed();
                }
                break;
            case R.id.menu_lnr_home:
                // 表示されているフラグメントごとにケース分けする
                showMode(TOP);
                break;
            case R.id.menu_lnr_btn_1:
                showMode(getModeType(0));
                break;
            case R.id.menu_lnr_btn_2:
                showMode(getModeType(1));
                break;
            case R.id.menu_lnr_btn_3:
                showMode(getModeType(2));
                break;
            case R.id.menu_lnr_other:
                showMode(OTHER);
                break;
            case R.id.header_btn_right:
                if (otherFrg.isVisible()) {
                    showMode(DRAG);
                } else if (playerFrg.isVisible()) {
                    playerFrg.onBtnClicked(resId);
                } else if (localPlayerFrg.isVisible()) {
                    localPlayerFrg.onBtnClicked(resId);
                } else if (streamPlayerFrg.isVisible()) {
                    streamPlayerFrg.onBtnClicked(resId);
                } else if (timerFrg.isVisible()) {
                    showMode(TIMER_DELETE);
                } else if (timerDeleteFrg.isVisible()) {
                    showMode(TIMER);
                } else if (favoriteLocalFragment.isVisible()) {
                    showMode(FAVORITE_DELETE);
                } else if (favoriteFragment.isVisible()) {
                    showMode(FAVORITE_TEMPLATE_DELETE);
                } else if (favoriteTemplateDeleteFragment.isVisible()) {
                    showMode(FAVORITE);
                } else if (favoriteDeleteFrg.isVisible()) {
                    showMode(FAVORITE_LOCAL);
                } else if (favoriteEditFrg.isVisible()) {
                    favoriteEditFrg.save();
                } else if (selectLocalFrg.isVisible()) {
                    selectLocalFrg.setCurrentDirectory();
                } else if (timerEditFrg.isVisible()) {
                    timerEditFrg.saveTimer();
                } else if (patternParentFragment.isVisible()) {
                    patternParentFragment.forceUpdate();
                } else {
                    showPlayer();
                }
                break;
            case DIALOG_TAG_LOGOUT_POS:
                showProgress();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                            FROForm.getInstance().termType = FROForm.TERM_TYPE_LOGOUT;
                            if (playerFrg != null)
                                playerFrg.stopTimer();
                            FROForm.getInstance().termMusic(0);
                        } else {
                            FROForm.getInstance().logout();
                        }
                    }
                }, 3000);
                break;
            case DIALOG_TAG_BOOT_INTENT_POS:
                if (FROForm.getInstance().expandKey != null) {
                    showProgress();
                    FROForm.getInstance().channelExpand();
                } else if (FROForm.getInstance().getArtistId() != null) {
                    bootPlayer(ChannelMode.ARTIST, Integer.parseInt(FROForm.getInstance().getArtistId()), null);
                }
                break;
            case DIALOG_TAG_BOOT_INTENT_NEG:
                FROForm.getInstance().expandKey = null;
                FROForm.getInstance().setArtistId(null);
                break;
            // 特典コードの使用を選択
            case DIALOG_TAG_ADD_TICKET_POS:
                showProgress();
                FROForm.getInstance().addTicket();
                break;
            // 特典コードの使用をキャンセル
            case DIALOG_TAG_ADD_TICKET_NEG:
                FROForm.getInstance().setTicketData(null, null);
                break;
            case DIALOG_TAG_NO_NEXT_MUSIC:
                showProgress();
                FROForm.getInstance().termType = FROForm.TERM_TYPE_TOP;
                if (playerFrg != null)
                    playerFrg.stopTimer();
                FROForm.getInstance().termMusic(0);
                break;
            case DIALOG_TAG_NO_NEXT_MUSIC_BENIFIT:
                showProgress();
                FROForm.getInstance().termType = FROForm.TERM_TYPE_BENIFIT;
                if (playerFrg != null)
                    playerFrg.stopTimer();
                FROForm.getInstance().termMusic(0);
                break;
            // 終了確認ダイアログでOKを押した際の動作
            case DIALOG_TAG_TERM_APP_POS:
                showProgress();
                FROForm.getInstance().setNowPlaying(null);
                if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE) {
                    // 楽曲再生している場合はプレイヤーを終了する
                    FROForm.getInstance().termType = FROForm.TERM_TYPE_END;
                    if (playerFrg != null)
                        playerFrg.stopTimer();
                    FROForm.getInstance().termMusic(Consts.TERM_TYPE_FORCE);
                } else {
                    // そうでなければアプリ終了
                    FROForm.getInstance().term();
                }
                break;
            case DIALOG_TAG_NO_NEXT_MUSIC_LOCAL:
            case DIALOG_TAG_NO_NEXT_MUSIC_FAVORITE:
                showMode(TOP);
                break;
            case DIALOG_TAG_IMPORT_FAVORITE_POS:
                if (importTemlpateFragment != null) {
                    CustomListItem selected = importTemlpateFragment.getImportItem();
                    MCTemplateItem item = new MCTemplateItem(Integer.toString(selected.getTemplateType()),
                            Integer.toString(selected.getId()), selected.getmName(), selected.getmNameEn());
                    FROTemplateFavoriteListDB templateListDb = FROTemplateFavoriteListDBFactory
                            .getInstance(getApplicationContext());
                    MCTemplateItem templateListItem = templateListDb.find(Integer.toString(selected.getId()));
                    if (templateListItem != null) {
                        templateListDb.update(item);
                    } else {
                        templateListDb.insert(item);
                    }
                    showProgress();
                    FROForm.getInstance().parentId = item.getString(MCDefResult.MCR_KIND_TEMPLATE_ID);
                    FROForm.getInstance().downloadTemplate(
                            Integer.parseInt(item.getString(MCDefResult.MCR_KIND_TEMPLATE_TYPE)),
                            Integer.parseInt(item.getString(MCDefResult.MCR_KIND_TEMPLATE_ID)));
                }
                break;
            case DIALOG_TAG_IMPORT_FAVORITE_NEG:
                if (importTemlpateFragment != null)
                    importTemlpateFragment.releaseSelected();
                break;
            case DIALOG_TAG_IMPORT_TIMER_POS:
                if (importTimerTemlpateFragment != null) {
                    CustomListItem selected = importTimerTemlpateFragment.getImportItem();
                    MCTemplateItem item = new MCTemplateItem(Integer.toString(selected.getTemplateType()),
                            Integer.toString(selected.getId()), selected.getmName(), selected.getmNameEn(),
                            selected.getDigest(), selected.getAction(), selected.getRule());

                    if (!TextUtils.isEmpty(selected.getAction())
                            && selected.getAction().equals(Consts.TAG_MUSIC_TIMER_TYPE_AUTO)) {
                        MainPreference.getInstance(getApplicationContext()).setMusicTimerType(Consts.MUSIC_TIMER_TYPE_AUTO);
                        FROForm.getInstance().setPreferenceInteger(MCUserInfoPreference.INTEGER_DATA_MUSIC_TIMER_TYPE,
                                Consts.MUSIC_TIMER_TYPE_AUTO);
                        // 選択されたタイマーテンプレートを DB に保存する
                        FROTemplateTimerAutoListDB templateListDb = FROTemplateTimerAutoListDBFactory
                                .getInstance(getApplicationContext());
                        // 以前に登録されていたデータは削除
                        templateListDb.deleteAll();
                        templateListDb.insert(item);
                    }

                    showProgress();
                    FROForm.getInstance().downloadTemplate(
                            Integer.parseInt(item.getString(MCDefResult.MCR_KIND_TEMPLATE_TYPE)),
                            Integer.parseInt(item.getString(MCDefResult.MCR_KIND_TEMPLATE_ID)));
                }
                break;
            case DIALOG_TAG_IMPORT_TIMER_NEG:
                if (importTimerTemlpateFragment != null)
                    importTimerTemlpateFragment.releaseSelected();
                break;
            case DIALOG_TAG_FINISH_OFFLINE_PLAYER_POS:
                showProgress();
                FROForm.getInstance().termType = FROForm.TERM_TYPE_TOP;
                FROForm.getInstance().termMusic(0);
                break;
            case DIALOG_TAG_FAILED_TO_NETWORK_RECOVER:
                showProgress();
                FROForm.getInstance().termType = FROForm.TERM_TYPE_TOP;
                FROForm.getInstance().termMusic(0);
                break;
            case DIALOG_TAG_STOP_AUTO_UPDATE_TIMER:
                MainPreference.getInstance(getApplicationContext()).setMusicTimerType(Consts.MUSIC_TIMER_TYPE_MANUAL);
                FROForm.getInstance().setPreferenceInteger(0, Consts.MUSIC_TIMER_TYPE_MANUAL);
                // 自動テンプレートを停止した際に、タイマー設定も併せて OFF にする
                MainPreference.getInstance(getApplicationContext()).setMusicTimerEnable(false);
                FROForm.getInstance().setPreferenceBoolean(MCUserInfoPreference.BOOLEAN_DATA_ENABLE_MUSIC_TIMER, false);
                // テンプレート更新タイマーのキャンセル
                TimerHelper.cancelTimer(getApplicationContext(), TimerHelper.PENDING_INTENT_TEMPLATE_TIMER);
                FROTemplateTimerAutoListDBFactory.getInstance(getApplicationContext()).deleteAll();
                FROTimerAutoDBFactory.getInstance(getApplicationContext()).deleteAll();
                FROForm.getInstance().sendTemplateState();
                showMode(TIMER);
                break;
            case DIALOG_TAG_DELETE_TIMER_POS:
                if (timerEditFrg != null && timerEditFrg.isVisible())
                    timerEditFrg.deleteTimer();
                else if (timerDeleteFrg != null && timerDeleteFrg.isVisible())
                    timerDeleteFrg.deleteTimer();
                break;
            case DIALOG_TAG_ON_NOTIFY_ERROR:
                if (FROForm.getInstance().getPlayerType() != FROPlayer.PLAYER_TYPE_OFFLINE) {
                    super.onBtnClicked(resId);
                }
                break;
            case DIALOG_TAG_EJECT_STORAGE:
                if (displayedTag != TOP)
                    showMode(TOP);
                break;
            case DIALOG_TAG_PATTERN_FORCE_UPDATE:
                showProgress();
                FROForm.getInstance().checkInterruptSchedule();
                break;
            default:
                super.onBtnClicked(resId);
        }
    }

    @Override
    public void onBtnClicked(int resId, String txt) {
        switch (resId) {
            case DIALOG_TAG_ADD_TO_FAVORITE_POS:
                if (playerFrg.getFavorite() != null && playerFrg.isVisible()) {
                    playerFrg.getFavorite().setChannelName(txt);
                    FROFavoriteChannelDB db = FROFavoriteChannelDBFactory.getInstance(this);
                    db.insert(playerFrg.getFavorite());
                } else if (localPlayerFrg.getFavorite() != null && localPlayerFrg.isVisible()) {
                    localPlayerFrg.getFavorite().setChannelName(txt);
                    FROFavoriteChannelDB db = FROFavoriteChannelDBFactory.getInstance(this);
                    db.insert(localPlayerFrg.getFavorite());
                } else if (streamPlayerFrg.getFavorite() != null && streamPlayerFrg.isVisible()) {
                    streamPlayerFrg.getFavorite().setChannelName(txt);
                    FROFavoriteChannelDB db = FROFavoriteChannelDBFactory.getInstance(this);
                    db.insert(streamPlayerFrg.getFavorite());
                }
                break;
            case DIALOG_TAG_ADD_TO_FAVORITE_NEG:
                break;
        }
    }

    @Override
    public void onItemClicked(int tag, int pos) {
        switch (tag) {
            case DIALOG_TAG_CART:
                // case DIALOG_TAG_SHARE_PLAYER:
                playerFrg.onItemClicked(tag, pos);
                break;
            case DIALOG_TAG_SHARE_HISTORY:
                // historyFrg.onItemClicked(tag, pos);
                break;
            case DIALOG_TAG_SHARE_TYPE:
                switch (shareWith) {
                    case SHARE_ON_TWITTER:
                        // if
                        // (shareType[pos].equals(getString(R.string.menu_share_channel)))
                        // {
                        // FROForm.getInstance().channelShare();
                        // } else {
                        // shareOnTwitter(R.string.msg_share_track_twitter,
                        // FROForm.getInstance().getNowPlaying());
                        // }
                        break;
                    case SHARE_ON_MAIL:
                        // if
                        // (shareType[pos].equals(getString(R.string.menu_share_channel)))
                        // {
                        // FROForm.getInstance().channelShare();
                        // } else {
                        // shareOnMail(R.string.msg_share_track_facebook,
                        // FROForm.getInstance().getNowPlaying());
                        // }
                        break;
                }
                break;
            // case 12345:
            // String value = PlayerFragment.VOLUME_SET[pos];
            // if (!TextUtils.isEmpty(value) && !value.equalsIgnoreCase("Cancel")) {
            // FROForm.getInstance().testMethod1(value);
            // String msg = "音量の基準値を変更しました\n";
            // msg += "基準音量 : " + value + "\n";
            // double tmp = Double.parseDouble(value);
            // tmp = 1 / tmp;
            // double rg = Math.log10(tmp) * 20;
            // msg += "有効なゲイン値の上限 : " + rg + "db";
            // showNoButtonDialog(msg);
            // }
            // break;
        }
    }

    @Override
    public void showShareDialog(int type) {
        // switch (type) {
        // case SHARE_ON_TWITTER:
        // case SHARE_ON_MAIL:
        // shareWith = type;
        // ArrayList<String> shareMenu = new ArrayList<String>();
        // int mode = FROForm.getInstance().getModeIntInService();
        // if (mode != Consts.MODE_INT_HOT100 && mode != Consts.MODE_INT_SHUFFLE
        // && mode != Consts.MODE_INT_BENIFIT) {
        // shareMenu.add(getString(R.string.menu_share_channel));
        // }
        // shareMenu.add(getString(R.string.menu_share_track));
        // shareType = shareMenu.toArray(new String[0]);
        // showListDialog(DIALOG_TAG_SHARE_TYPE, null, shareType);
        // break;
        // }
    }

    private int getModeType(int index) {
        int type = TOP;
        ArrayList<Integer> menus = FROForm.getInstance().getMenuList(this);
        int tag = menus.get(index);
        switch (tag) {
            // case Consts.TAG_MODE_GENRE:
            // type = GENRE;
            // break;
            case Consts.TAG_MODE_SPECIAL:
                type = SPECIAL;
                break;
            case Consts.TAG_MODE_HISTORY:
                type = HISTORY;
                break;
 //           case Consts.TAG_MODE_ARTIST:
 //               type = ARTIST;
 //               break;
 //           case Consts.TAG_MODE_RELEASE:
 //               type = RELEASE;
 //               break;
            case Consts.TAG_MODE_MYCHANNEL:
                type = FAVORITE;
                break;
            case Consts.TAG_MODE_SETTING:
                type = SETTING;
                break;
             case Consts.TAG_MODE_STREAMING:
             type = STREAMING;
             break;
        }

        MainPreference.getInstance(this).term();
        return type;
    }

    @Override
    public int getMenuSize() {
        return menuSize;
    }

    private boolean canDisplayingPlayIcon() {
        switch (displayedTag) {
            case TOP:
            case GENRE:
            case GENRE2:
            case GENRE3:
            case SPECIAL:
            case HISTORY:
//            case RELEASE:
            case SETTING:
//            case ARTIST:
            case ABOUT:
            case STREAMING:
                return true;
            default:
                return false;
        }
    }
    // @Override
    // public boolean dispatchKeyEvent(KeyEvent event) {
    // FROForm.getInstance().resetTimer();
    // return super.dispatchKeyEvent(event);
    // }

    // @Override
    // public boolean dispatchTouchEvent(MotionEvent ev) {
    // FROForm.getInstance().resetTimer();
    // return super.dispatchTouchEvent(ev);
    // }
}
