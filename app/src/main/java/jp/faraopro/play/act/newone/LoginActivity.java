package jp.faraopro.play.act.newone;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import jp.faraopro.play.R;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.app.TimerHelper;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.domain.FROChannelHistoryDBFactory;
import jp.faraopro.play.domain.FROGoodHistoryDBFactory;
import jp.faraopro.play.domain.FROMusicHistoryDBFactory;
import jp.faraopro.play.domain.MainPreference;
import jp.faraopro.play.frg.mode.LoginSelectFragment;
import jp.faraopro.play.frg.newone.EmergencyDialogFragment;
import jp.faraopro.play.frg.newone.HeaderFragment;
import jp.faraopro.play.mclient.MCDefAction;
import jp.faraopro.play.mclient.MCDefParam;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCUserInfoPreference;
import jp.faraopro.play.model.UserDataHelper;

/**
 * ログイン画面
 *
 * @author AIM Corporation
 */
public class LoginActivity extends BaseActivity
        implements HeaderFragment.OnBtnClickListener, EmergencyDialogFragment.EmergencyDialogListener {
    /**
     * bundle key
     **/
    public static final String BUNDLE_KEY_SHOW_DIALOG = "SHOW_DIALOG";
    private static final String FLAGMENT_EMERGENCY = "EMERGENCY";

    /**
     * dialog tags
     **/
    public static final int DIALOG_LOGIN_FORCE = 10;
    public static final int DIALOG_LOGIN_CANCEL = 11;
    public static final int DIALOG_TAG_GOTO_SETTING_FOR_LOCATION = 12;
    public static final int DIALOG_TAG_EXIT_APP_POS = 13;
    public static final int DIALOG_TAG_EXIT_APP_NEG = 14;

    /**
     * fragment tag
     **/
    public static final String TAG_FRG_LOGIN = "LOGIN";

    /**
     * view
     **/
    private LoginSelectFragment loginFrg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 画面の生成
        setContentView(R.layout.layout_simple_2block);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        // ボディ
        loginFrg = (LoginSelectFragment) manager.findFragmentByTag(TAG_FRG_LOGIN);
        if (loginFrg == null) {
            loginFrg = new LoginSelectFragment();
            Bundle headerArgs = new Bundle();
            loginFrg.setArguments(headerArgs);
            transaction.add(R.id.simple2_lnr_body, loginFrg, TAG_FRG_LOGIN);
        }
        transaction.commitAllowingStateLoss();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int msgId = bundle.getInt(BUNDLE_KEY_SHOW_DIALOG, -1);
            if (msgId > 0) {
                showPositiveDialog(msgId, BaseActivity.DIALOG_TAG_MESSAGE, true);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (FROForm.getInstance().isEmergency()) {
            showEmergency();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideEmergency();
    }

    @Override
    public void onNotifyResult(int when, Object obj) {
        switch (when) {
            case MCDefAction.MCA_KIND_LOGIN:
            case MCDefAction.MCA_KIND_STATUS:
                showProgress();
                hideEmergency();
                // // ログイン成功時のメールとパスワードを保存する
                String email;
                try {
                    email = FROForm.getInstance().getPreferenceString(MCUserInfoPreference.STRING_DATA_MAIL);
                } catch (Exception e) {
                    startActivity(BootActivity.class, true);
                    return;
                }
                String last = MainPreference.getInstance(getApplicationContext()).getLastAccount();
                // 前回ログイン時のアカウントと今回ログインしたアカウントが異なっている場合
                if (last != null && !last.equals(email)) {
                    FROMusicHistoryDBFactory.getInstance(getApplicationContext()).deleteAll(); // 履歴のクリア
                    FROChannelHistoryDBFactory.getInstance(getApplicationContext()).deleteAll(); // チャンネル履歴のクリア
                    FROGoodHistoryDBFactory.getInstance(getApplicationContext()).deleteAll(); // Good履歴のクリア
                }
                // 最後にログインしたアカウントの更新
                MainPreference.getInstance(getApplicationContext()).setLastAccount(email);
                // 課金切れ
                if (!UserDataHelper.isPremium()) {
                    dismissProgress();
                    showPositiveDialog(R.string.msg_remaining_time_none, BaseActivity.DIALOG_TAG_MESSAGE, false);
                } else {
                    FROForm.getInstance().checkLicenseStatus();
                }
                break;
            case MCDefAction.MCA_KIND_LICENSE_STATUS:
                FROForm.getInstance().checkInterruptSchedule();
                break;
            case MCDefAction.MCA_KIND_PATTERN_SCHEDULE:
            case MCDefAction.MCA_KIND_PATTERN_DOWNLOAD:
                dismissProgress();
                TimerHelper.setMusicTimer(getApplicationContext());
                TimerHelper.restoreLastMusicTimer(getApplicationContext());
                startActivity(MainActivity.class, true);
                FROForm.getInstance().detach(this);
                break;
        }
    }

    @Override
    public void onNotifyError(int when, int statusCode) {
        switch (when) {
            case MCDefAction.MCA_KIND_LOGIN:
            case MCDefAction.MCA_KIND_STATUS:
                if (statusCode == Consts.NOTIFY_BEGIN_EMERGENCY_MODE) {
                    dismissProgress();
                    if (FROForm.getInstance().isEmergency()) {
                        showEmergency();
                    }
                    return;
                } else if (statusCode == Consts.NOTIFY_PLAY_NEXT_EMERGENCY
                        || statusCode == Consts.NOTIFY_RECOVERY_EMERGENCY_MODE) {
                    updateEmergency();
                    return;
                } else if (statusCode == Consts.NOTIFY_TIMEOUT_EMERGENCY_MODE) {
                    hideEmergency();
                    return;
                }

                hideEmergency();
                if (statusCode == MCDefResult.MCR_KIND_MSG_USER_LOGGEDIN) {
                    FROForm.getInstance().errorMsg = null;
                    dismissProgress();
                    showNegativeDialog(R.string.msg_confirm_to_force_login, DIALOG_LOGIN_FORCE, DIALOG_LOGIN_CANCEL, true);
                } else if (statusCode == MCDefResult.MCR_KIND_MSG_USER_LOCKED) {
                    FROForm.getInstance().errorMsg = null;
                    dismissProgress();
                    showPositiveDialog(R.string.msg_locked_account, BaseActivity.DIALOG_TAG_MESSAGE, true);
                } else if (statusCode == MCDefResult.MCR_KIND_MSG_INVALID_DEVICE_TOKEN) {
                    FROForm.getInstance().errorMsg = null;
                    dismissProgress();
                    showPositiveDialog(R.string.msg_invalid_device_token, BaseActivity.DIALOG_TAG_MESSAGE, true);
                } else if (statusCode == MCDefResult.MCR_KIND_MSG_ERROR_UNKNOWN) {
                    FROForm.getInstance().errorMsg = null;
                    dismissProgress();
                    showPositiveDialog(R.string.msg_invalid_account_type, BaseActivity.DIALOG_TAG_MESSAGE, true);
                } else {
                    super.onNotifyError(when, statusCode);
                }
                break;
            default:
                super.onNotifyError(when, statusCode);
        }
    }

    @Override
    public void onBtnClicked(int resId) {
        switch (resId) {
            case DIALOG_LOGIN_FORCE:
                showProgress();
                FROForm.getInstance().login(loginFrg.getEmail(), loginFrg.getPass(), MCDefParam.MCP_PARAM_STR_YES);
                break;
            case DIALOG_TAG_GOTO_SETTING_FOR_LOCATION:
                try {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                } catch (final ActivityNotFoundException e) {
                    showToast("ActivityNotFoundException");
                }
                break;
            case DIALOG_TAG_EXIT_APP_POS:
                exitApp();
                break;
        }
    }

    @Override
    public void onClick(int viewId, int action) {
        switch (viewId) {
            case R.id.image_button_play:
                if (action == EmergencyDialogFragment.ACTION_PLAY)
                    FROForm.getInstance().playEmergency();
                else
                    FROForm.getInstance().pauseEmergency();
                break;
//		case R.id.button_back:
//			FROForm.getInstance().cancelEmergency();
//			break;
            case EmergencyDialogFragment.NO_VIEW_ID:
                switch (action) {
                    case EmergencyDialogFragment.ACTION_BACK:
                        showNegativeDialog(R.string.msg_exit_app_pro,
                                DIALOG_TAG_EXIT_APP_POS, DIALOG_TAG_EXIT_APP_NEG, true);
                        break;
                }
                break;
        }
    }

    @Override
    public void onViewCreated() {
        updateEmergency();
    }

    private void showEmergency() {
        FragmentManager manager = getSupportFragmentManager();
        EmergencyDialogFragment fragment = (EmergencyDialogFragment) manager.findFragmentByTag(FLAGMENT_EMERGENCY);
        if (fragment == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            fragment = EmergencyDialogFragment.newInstance(0);
            transaction.add(fragment, FLAGMENT_EMERGENCY);
            transaction.commit();
        } else {
            updateEmergency();
        }
    }

    private void hideEmergency() {
        dismissNegativeDialog();
        FragmentManager manager = getSupportFragmentManager();
        EmergencyDialogFragment fragment = (EmergencyDialogFragment) manager.findFragmentByTag(FLAGMENT_EMERGENCY);
        if (fragment != null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
        }
    }

    private void updateEmergency() {
        EmergencyDialogFragment fragment = (EmergencyDialogFragment) getSupportFragmentManager()
                .findFragmentByTag(FLAGMENT_EMERGENCY);
        if (fragment != null) {
            if (FROForm.getInstance().isRecoveredEmergency()) {
                fragment.setMessages(R.string.msg_success_to_connection, R.string.msg_waitting_for_track_completion);
            } else {
                fragment.setMessages(R.string.msg_confirm_to_connection, R.string.msg_emergency_mode);
            }
            fragment.setTrack(FROForm.getInstance().getEmergencyLength(), FROForm.getInstance().getEmergencyPosition());
            if (FROForm.getInstance().isPlayingEmergency())
                fragment.play();
            else
                fragment.pause();
        }
    }

    private void exitApp() {
        FROForm.getInstance().cancelEmergency();
        hideEmergency();
        FROForm.getInstance().term();
        finish();
    }
}
