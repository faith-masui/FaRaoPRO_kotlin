package jp.faraopro.play.act.newone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import jp.faraopro.play.BuildConfig;
import jp.faraopro.play.R;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.app.FROPlayer;
import jp.faraopro.play.app.TimerHelper;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.common.LocationHelper;
import jp.faraopro.play.common.MyUncaughtExceptionHandler;
import jp.faraopro.play.domain.MusicInfo;
import jp.faraopro.play.frg.newone.SimpleDialogFragment;
import jp.faraopro.play.mclient.MCDefAction;
import jp.faraopro.play.mclient.MCDefParam;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCError;
import jp.faraopro.play.model.UserDataHelper;
import jp.faraopro.play.util.FROUtils;

/**
 * スプラッシュ画面
 * 
 * @author AIM Corporation
 * 
 */
public class BootActivity extends BaseActivity implements SimpleDialogFragment.OnBtnClickListener {
	/** dialog tag **/
	public static final int DIALOG_TAG_UNAUTHORIZED_ON_CAR = 1000;

	/** intent tag **/
	public static final String INTENT_TAG_IS_FIRST = "IS_FIRST_TIME";
	public static final String INTENT_TAG_SHOW_PLAYER = "INTENT_TAG_SHOW_PLAYER";

	private int moveTo = -1;

	/**
	 * アクティビティ作成時の処理
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_lay_boot);

		// 処理されていない例外をキャッチして外部ファイルに出力する
		Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(getApplicationContext()));

		// 不要なデータが残っていないか確認
		FROUtils.deleteOldHistoryThumbnails(getApplicationContext()); // 使用していないサムネイルが残っていた場合削除
		FROForm.getInstance().setHardwareInfo(this); // 画面サイズを取得する
		if (FROForm.getInstance().init(getApplicationContext())) {
			checkIntent();
			checkupCurrentState();
		}
	}

	// Intentのチェックを行う
	private void checkIntent() {
		moveTo = -1;
		Intent intent = getIntent();
		Bundle args = intent.getExtras();
		if (args != null) {
			moveTo = args.getInt("SHOW_MODE", -1);
			FROUtils.cancelUpdateNotification(getApplicationContext());
		}
	}

	private void checkupCurrentState() {
		if ((FROForm.getInstance().getPlayerType() != FROPlayer.PLAYER_TYPE_NONE)
				|| FROForm.getInstance().isInterruptPlaying()) {
			FROForm.getInstance().updateMusicInfo();
		} else {
			FROForm.getInstance().login(null, null, MCDefParam.MCP_PARAM_STR_YES);
		}
	}

	@Override
	public void onNotifyResult(int when, Object obj) {
		switch (when) {
		case Consts.SERVICE_BOOT:
			if (FROForm.getInstance().isConnectedService())
				checkupCurrentState();
			else
				finish();
			break;
		case Consts.CHECK_UNSENT_DATA:
			FROForm.getInstance().login(null, null, MCDefParam.MCP_PARAM_STR_YES);
			break;
		case MCDefAction.MCA_KIND_LOGIN:
		case MCDefAction.MCA_KIND_STATUS:
			// 課金切れ
			if (!UserDataHelper.isPremium() || !LocationHelper.isEnableProvider(getApplicationContext())) {
				int msg;
				if (!UserDataHelper.isPremium()) {
					msg = R.string.msg_remaining_time_none;
				} else {
					msg = R.string.msg_logout_failed_location;
				}
				Bundle bundle = new Bundle();
				bundle.putInt(LoginActivity.BUNDLE_KEY_SHOW_DIALOG, msg);
				startActivity(LoginActivity.class, true, bundle);
			} else {
				// if music player still working
				if (FROForm.getInstance().checkPlayerInstance() != Consts.PLAYER_STATUS_NOINSTANCE && moveTo < 0) {
					FROForm.getInstance().updateMusicInfo();
				} else {
					if (moveTo != MainActivity.SETTING)
						FROForm.getInstance().checkLicenseStatus();
					else
						FROForm.getInstance().checkInterruptSchedule();
				}
			}
			break;
		case MCDefAction.MCA_KIND_LICENSE_STATUS:
			FROForm.getInstance().checkInterruptSchedule();
			break;
		case Consts.UPDATE_MUSIC_INFO:
			TimerHelper.setMusicTimer(getApplicationContext());
			MusicInfo nowplaying = FROForm.getInstance().getNowPlaying();
			if (nowplaying != null && !FROForm.getInstance().isInterruptPlaying()) {
				switch (nowplaying.getPlayerType()) {
				case FROPlayer.PLAYER_TYPE_LOCAL:
					moveTo = MainActivity.LOCAL_PLAYER;
					break;
				case FROPlayer.PLAYER_TYPE_SIMUL:
					moveTo = MainActivity.STREAM_PLAYER;
					break;
				case FROPlayer.PLAYER_TYPE_NOMAL:
					moveTo = MainActivity.PLAYER;
					break;
				case FROPlayer.PLAYER_TYPE_OFFLINE:
					moveTo = MainActivity.OFFLINE_PLAYER;
					break;
				}
			}
			showHomeActivity();
			break;
		case Consts.TERMINATION:
			dismissProgress();
			if (BuildConfig.DEBUG) {
				Toast.makeText(getApplicationContext(), "Thank you for listening!!", Toast.LENGTH_LONG).show();
			}
			finish();
			break;
		case MCDefAction.MCA_KIND_PATTERN_SCHEDULE:
		case MCDefAction.MCA_KIND_PATTERN_DOWNLOAD:
			TimerHelper.setMusicTimer(getApplicationContext());
			TimerHelper.restoreLastMusicTimer(getApplicationContext());
			showHomeActivity();
			break;
		}
	}

	private void showHomeActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("SHOW_MODE", moveTo);
		intent.setAction(Intent.ACTION_VIEW);
		intent.putExtras(bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
		moveTo = -1;
	}

	@Override
	public void onNotifyError(int when, int statusCode) {
		switch (when) {
		case MCDefAction.MCA_KIND_LOGIN:
		case MCDefAction.MCA_KIND_STATUS:
			// ログイン済み、ロック状態であった場合、自動ログインはせずにログイン選択画面に遷移する
			if (statusCode == MCDefResult.MCR_KIND_MSG_USER_LOCKED
					|| statusCode == MCDefResult.MCR_KIND_MSG_USER_LOGGEDIN
					|| statusCode == MCDefResult.MCR_KIND_MSG_INVALID_DEVICE_TOKEN
					|| statusCode == MCDefResult.MCR_KIND_MSG_ERROR_UNKNOWN || statusCode == MCError.MC_UNAUTHORIZED
					|| statusCode == MCError.MC_BAD_REQUEST || statusCode == Consts.NOTIFY_BEGIN_EMERGENCY_MODE) {
				FROForm.getInstance().errorMsg = null;
				startActivity(LoginActivity.class, true);
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
		case DIALOG_TAG_UNAUTHORIZED_ON_CAR:
			startActivity(LoginActivity.class, true);
			break;
		case DIALOG_TAG_ON_NOTIFY_ERROR:
			dismissProgress();
			FROForm.getInstance().errorMsg = null;
			startActivity(LoginActivity.class, true);
			break;
		}
	}

}