package jp.faraopro.play.act.newone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;

import jp.faraopro.play.BuildConfig;
import jp.faraopro.play.R;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.app.FROForm.IActivityListener;
import jp.faraopro.play.domain.MusicInfo;
import jp.faraopro.play.frg.newone.ListDialogFragment;
import jp.faraopro.play.frg.newone.NoButtonDialogFragment;
import jp.faraopro.play.frg.newone.PlayerInitDialogFragment;
import jp.faraopro.play.frg.newone.SimpleDialogFragment;
import jp.faraopro.play.frg.newone.SimpleProgressDialogFragment;
import jp.faraopro.play.frg.newone.TextBoxDialogFragment;
import jp.faraopro.play.mclient.MCDefAction;

/**
 * 全アクティビティのベースとなるクラス<br>
 * どのアクティビティでも共通して使用する処理をまとめてある。
 * 
 * @author AIM Corporation
 * 
 */
public class BaseActivity extends FragmentActivity
		implements IActivityListener, SimpleDialogFragment.OnBtnClickListener, ListDialogFragment.ItemClickListener,
		TextBoxDialogFragment.OnBtnClickListener, PlayerInitDialogFragment.onCancelListener {
	/** dialog tag **/
	public static final int DIALOG_TAG_ON_NOTIFY_ERROR = 1;
	public static final int DIALOG_TAG_MESSAGE = 2;
	public static final int DIALOG_TAG_UNMAUNTED_STRAGE = 3;

	/** fragment tags **/
	public static final String TAG_DIALOG_POS = "DIALOG_POS";
	public static final String TAG_DIALOG_NEG = "DIALOG_NEG";
	public static final String TAG_DIALOG_LIST = "DIALOG_LIST";
	public static final String TAG_DIALOG_BOOT = "TAG_DIALOG_BOOT";
	public static final String TAG_DIALOG_TEXT = "TAG_DIALOG_TEXT";

	/** const **/
	private static final int DIALOG_CB_REJECT_SD = 100000;

	/** member **/
	protected boolean DEBUG = true; // ログ出力の可否
	protected boolean isBootCancel = false;
	protected int when;
	protected int statusCode;
	private Handler mHandler;
	// private SDReceiver mSDReceiver;

	/** view **/
	private SimpleProgressDialogFragment progressBar;

	/**
	 * タイトルバーを非表示にし、キーボードの出現を抑止する
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 共通設定
		requestWindowFeature(Window.FEATURE_NO_TITLE); // タイトルバー非表示
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // キーボード出現抑制
	}

	/**
	 * UIスレッドのハンドラ―を返す
	 * 
	 * @return UIスレッドのハンドラ―
	 */
	public Handler getUiHandler() {
		if (mHandler == null)
			mHandler = new Handler(Looper.getMainLooper());

		return mHandler;
	}

	/**
	 * {@link FROForm}クラスとの接続を行う
	 */
	@Override
	public void onStart() {
		super.onStart();
		FROForm.getInstance().attach(this);
		// setReceiver();
	}

	public void onStartWithoutAttach() {
		super.onStart();
		// setReceiver();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// システムやクリーナーによってService以外のメモリが解放されていた場合
		if (FROForm.getInstance().getContext() == null) {
			// 一旦Bootを起動して初期化を行う
			startActivity(BootActivity.class, true);
			return;
		}
	}

	// // SDカードレシーバーの解除
	// @Override
	// public void onStop() {
	// super.onStop();
	//
	// if (mSDReceiver != null) {
	// unregisterReceiver(mSDReceiver);
	// mSDReceiver = null;
	// }
	// }

	@Override
	public void onDestroy() {
		super.onDestroy();
		mHandler = null;
	}

	/**
	 * ダイアログが表示中か否かを確認する
	 * 
	 * @return true:ダイアログが表示中, false:その他の場合
	 */
	public boolean isShowingDialog() {
		boolean isVisible = false;

		if (musicLoadingDialog != null && musicLoadingDialog.isShowing()) {
			isVisible = true;
		}
		FragmentManager manager = getSupportFragmentManager();
		Fragment pos = manager.findFragmentByTag(TAG_DIALOG_POS);
		Fragment neg = manager.findFragmentByTag(TAG_DIALOG_NEG);
		if (pos != null) {
			isVisible = true;
		}
		if (neg != null) {
			isVisible = true;
		}

		return isVisible;
	}

	// // Intent.ACTION_MEDIA_EJECTを感知するための設定
	// private void setReceiver() {
	// mSDReceiver = new SDReceiver();
	// IntentFilter filter = new IntentFilter();
	// filter.addAction(Intent.ACTION_MEDIA_EJECT);
	// filter.addDataScheme("file");
	// registerReceiver(mSDReceiver, filter);
	// }

	// /**
	// * 内部クラス、SDカード排出を受信した際の処理
	// */
	// public class SDReceiver
	// extends
	// BroadcastReceiver {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// showPositiveDialog(R.string.msg_sdcard_error, DIALOG_CB_REJECT_SD,
	// false);
	// }
	// }

	/**
	 * activity開始リスナーセット
	 * 
	 * @param view
	 *            被セットビュー
	 * @param cls
	 *            開始するactivity
	 * @param finish
	 *            遷移時に終了
	 */
	public void setStartActListener(View view, final Class<?> cls, boolean finish) {
		final boolean mFinish = finish;

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(cls, mFinish);
			}
		});
	}

	/**
	 * activity開始
	 * 
	 * @param cls
	 *            開始するactivity
	 * @param isFinish
	 *            遷移時に終了
	 */
	public void startActivity(Class<?> cls, boolean isFinish) {
		Intent intent = new Intent(this, cls);
		Bundle bundle = new Bundle();
		intent.setAction(Intent.ACTION_VIEW);
		intent.putExtras(bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		if (isFinish)
			finish();
	}

	public void startActivity(Class<?> cls, boolean isFinish, Bundle bundle) {
		Intent intent = new Intent(this, cls);
		intent.setAction(Intent.ACTION_VIEW);
		intent.putExtras(bundle);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		if (isFinish)
			finish();
	}

	/**
	 * Webブラウザ起動
	 * 
	 * @param url
	 */
	public void startBrowser(String url) {
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}

	/**
	 * 楽曲情報をTwitterにツイートする
	 * 
	 * @param resId
	 *            ツイートする文言のResourceId
	 * @param info
	 *            楽曲情報
	 */
	public void shareOnTwitter(int resId, MusicInfo info) {
	}

	/**
	 * 楽曲情報をメールで送信する
	 * 
	 * @param resId
	 *            送信する文言のResourceId
	 * @param info
	 *            楽曲情報
	 */
	public void shareOnMail(int resId, MusicInfo info) {
	}

	@Override
	public void onNotifyResult(int when, Object obj) {
	}

	@Override
	public void onNotifyChanged(int what, int statusCode, ArrayList<String> strList, int index) {
	}

	@Override
	public void onNotifyError(int when, int statusCode) {
		String title = "Error";
		String msg = null;
        if (BuildConfig.DEBUG)
			title = MCDefAction.getApi(when);
		if (FROForm.getInstance().errorMsg != null)
			msg = FROForm.getInstance().errorMsg.getMessage(this);

		dismissProgress();
		dismissLoadingProgressSMP();
		// ダイアログ表示
		SimpleDialogFragment dialog = new SimpleDialogFragment();
		Bundle args = new Bundle();
		args.putInt(SimpleDialogFragment.BUTTON_TYPE, SimpleDialogFragment.TYPE_POSITIVE);
		args.putInt(SimpleDialogFragment.BUTTON_ID_POS, DIALOG_TAG_ON_NOTIFY_ERROR);
		args.putString(SimpleDialogFragment.TITLE, title);
		args.putString(SimpleDialogFragment.MESSAGE, msg);
		dialog.setArguments(args);
		dialog.setCancelable(false);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(dialog, TAG_DIALOG_POS);
		transaction.commitAllowingStateLoss();
		FROForm.getInstance().errorMsg = null;
	}

	/**
	 * ボタンなしのダイアログを表示する
	 * 
	 * @param title
	 *            ダイアログのタイトル
	 * @param txt
	 *            ダイアログの文言
	 */
	public void showNoButtonDialog(String title, String txt) {
		NoButtonDialogFragment dialog = new NoButtonDialogFragment();
		Bundle args = new Bundle();
		args.putString(NoButtonDialogFragment.TITLE, title);
		args.putString(NoButtonDialogFragment.MESSAGE, txt);
		dialog.setArguments(args);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(dialog, null);
		transaction.commitAllowingStateLoss();
	}

	/**
	 * ボタンなしのダイアログを表示する
	 * 
	 * @param txt
	 *            ダイアログの文言
	 */
	public void showNoButtonDialog(String txt) {
		this.showNoButtonDialog(null, txt);
	}

	/**
	 * ボタンが1つのダイアログを表示する
	 * 
	 * @param title
	 *            ダイアログのタイトル
	 * @param txt
	 *            文言
	 * @param posId
	 *            ボタンID
	 * @param cancelable
	 *            true:キャンセル可, false:キャンセル不可
	 */
	public void showPositiveDialog(String title, String txt, int posId, boolean cancelable) {
		SimpleDialogFragment dialog = new SimpleDialogFragment();
		Bundle args = new Bundle();
		args.putInt(SimpleDialogFragment.BUTTON_TYPE, SimpleDialogFragment.TYPE_POSITIVE);
		args.putInt(SimpleDialogFragment.BUTTON_ID_POS, posId);
		args.putString(SimpleDialogFragment.TITLE, title);
		args.putString(SimpleDialogFragment.MESSAGE, txt);
		dialog.setArguments(args);
		dialog.setCancelable(cancelable);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(dialog, TAG_DIALOG_POS);
		transaction.commitAllowingStateLoss();
	}

	/**
	 * ボタンが1つのダイアログを表示する
	 * 
	 * @param txt
	 *            文言
	 * @param posId
	 *            ボタンID
	 * @param cancelable
	 *            true:キャンセル可, false:キャンセル不可
	 */
	public void showPositiveDialog(String txt, int posId, boolean cancelable) {
		showPositiveDialog(null, txt, posId, cancelable);
	}

	/**
	 * ボタンが1つのダイアログを表示する
	 * 
	 * @param resId
	 *            文言のResourceId
	 * @param posId
	 *            ボタンID
	 * @param cancelable
	 *            true:キャンセル可, false:キャンセル不可
	 */
	public void showPositiveDialog(int resId, int posId, boolean cancelable) {
		this.showPositiveDialog(getString(resId), posId, cancelable);
	}

	/**
	 * ボタンが2つのダイアログを表示する
	 * 
	 * @param txt
	 *            文言
	 * @param posId
	 *            左のボタンID
	 * @param negId
	 *            右のボタンID
	 * @param cancelable
	 *            true:キャンセル可, false:キャンセル不可
	 */
	public void showNegativeDialog(String txt, int posId, int negId, boolean cancelable) {
		showNegativeDialog(txt, posId, negId, -1, -1, cancelable);
	}

	/**
	 * ボタンが2つのダイアログを表示する
	 * 
	 * @param txt
	 *            文言
	 * @param posId
	 *            左のボタンID
	 * @param negId
	 *            右のボタンID
	 * @param cancelable
	 *            true:キャンセル可, false:キャンセル不可
	 */
	public void showNegativeDialog(String txt, int posId, int negId, int resIdPos, int resIdNeg, boolean cancelable) {
		SimpleDialogFragment dialog = new SimpleDialogFragment();
		Bundle args = new Bundle();
		args.putInt(SimpleDialogFragment.BUTTON_TYPE, SimpleDialogFragment.TYPE_NEGATIVE);
		args.putInt(SimpleDialogFragment.BUTTON_ID_POS, posId);
		args.putInt(SimpleDialogFragment.BUTTON_ID_NEG, negId);
		if (resIdPos != -1)
			args.putInt(SimpleDialogFragment.BUTTON_NAME_POS, resIdPos);
		if (resIdNeg != -1)
			args.putInt(SimpleDialogFragment.BUTTON_NAME_NEG, resIdNeg);
		args.putString(SimpleDialogFragment.MESSAGE, txt);
		dialog.setArguments(args);
		dialog.setCancelable(cancelable);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(dialog, TAG_DIALOG_NEG);
		transaction.commitAllowingStateLoss();
		// dialog.show(getSupportFragmentManager(), TAG_DIALOG_NEG);
	}

	public void dismissPositiveDialog() {
		Fragment dialog = getSupportFragmentManager().findFragmentByTag(TAG_DIALOG_POS);
		if (dialog != null && dialog.isAdded()) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.remove(dialog);
			transaction.commitAllowingStateLoss();
		}
	}

    public void dismissNegativeDialog() {
        Fragment dialog = getSupportFragmentManager().findFragmentByTag(TAG_DIALOG_NEG);
        if (dialog != null && dialog.isAdded()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(dialog);
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * リスト形式のダイアログを表示する
     *
     * @param tag   ダイアログのタグ
     * @param title ダイアログのタイトル
     * @param list  表示するリスト
     */
	public void showListDialog(int tag, String title, String[] list) {
		ListDialogFragment dialog = new ListDialogFragment();
		Bundle args = new Bundle();
		args.putInt(ListDialogFragment.KEY_TAG, tag);
		args.putString(ListDialogFragment.KEY_TITLE, title);
		args.putStringArray(ListDialogFragment.KEY_STRING_TABLE, list);
		dialog.setArguments(args);
		dialog.setCancelable(true);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(dialog, TAG_DIALOG_LIST);
		transaction.commitAllowingStateLoss();
		// dialog.show(getSupportFragmentManager(), TAG_DIALOG_LIST);
	}

	public void dismissListDialog() {
		Fragment dialog = getSupportFragmentManager().findFragmentByTag(TAG_DIALOG_LIST);
		if (dialog != null && dialog.isAdded()) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.remove(dialog);
			transaction.commitAllowingStateLoss();
		}
	}

	/**
	 * ボタンが2つのダイアログを表示する
	 * 
	 * @param resId
	 *            文言のリソースID
	 * @param posId
	 *            左のボタンID
	 * @param negId
	 *            右のボタンID
	 * @param cancelable
	 *            true:キャンセル可, false:キャンセル不可
	 */
	public void showNegativeDialog(int resId, int posId, int negId, boolean cancelable) {
		this.showNegativeDialog(getString(resId), posId, negId, cancelable);
	}

	/**
	 * プログレスバーを表示する
	 * 
	 * @param msg
	 *            表示する文言
	 */
	public void showProgress(String msg, int timeout) {
		if (progressBar == null) {
			progressBar = new SimpleProgressDialogFragment();
			Bundle args = new Bundle();
			args.putInt(SimpleProgressDialogFragment.BUTTON_TYPE, SimpleProgressDialogFragment.TYPE_NOMAL);
			args.putString(SimpleProgressDialogFragment.MESSAGE, msg);
			if (timeout != -1)
				args.putInt(SimpleProgressDialogFragment.TIMEOUT, timeout);
			progressBar.setArguments(args);
			progressBar.setCancelable(false);
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.add(progressBar, null);
			transaction.commitAllowingStateLoss();
		} else if (!progressBar.isVisible()) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.show(progressBar);
			transaction.commitAllowingStateLoss();
		}
	}

	public void showProgress(String msg) {
		showProgress(null, -1);
	}

	/**
	 * プログレスバーを表示する<br>
	 * 文言は「Now loading...」
	 */
	public void showProgress() {
		showProgress(null);
	}

	public void showProgress(int timeout) {
		showProgress(null, timeout);
	}

	/**
	 * プログレスバーを非表示にする
	 */
	public void dismissProgress() {
		if (progressBar != null) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.remove(progressBar);
			transaction.commitAllowingStateLoss();
			progressBar = null;
		}
	}

	/**
	 * プログレス表示
	 */
	public final Runnable showProgress = new Runnable() {
		@Override
		public void run() {
			showProgress();
		}
	};

	/**
	 * プログレス非表示
	 */
	public final Runnable dismissProgress = new Runnable() {
		@Override
		public void run() {
			dismissProgress();
		}
	};

	public void showTextBoxDialog(String title, String txt, int posId, int negId, int posName, int negName) {
		TextBoxDialogFragment dialog = TextBoxDialogFragment.getInstance(posId, negId, title, txt, posName, negName);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(dialog, TAG_DIALOG_TEXT);
		transaction.commitAllowingStateLoss();
	}

	public void dismissTextBoxDialog() {
		Fragment dialog = getSupportFragmentManager().findFragmentByTag(TAG_DIALOG_TEXT);
		if (dialog != null && dialog.isAdded()) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.remove(dialog);
			transaction.commitAllowingStateLoss();
		}
	}

	private ProgressDialog musicLoadingDialog;

	private PlayerInitDialogFragment bootProgress;

	/**
	 * ダウンロードの進捗を表示するプログレスダイアログを表示する
	 * 
	 * @param title
	 *            ダイアログのタイトル
	 * @param message
	 *            ダイアログの文言
	 */
	public void showLoadingProgressSMP(String title, String message) {
		bootProgress = new PlayerInitDialogFragment();
		Bundle args = new Bundle();
		args.putInt(PlayerInitDialogFragment.BUTTON_TYPE, PlayerInitDialogFragment.TYPE_CANCELABLE);
		args.putInt(PlayerInitDialogFragment.MAX_VALUE, 100);
		args.putString(PlayerInitDialogFragment.TITLE, title);
		args.putString(PlayerInitDialogFragment.MESSAGE, message);
		bootProgress.setArguments(args);
		bootProgress.setCancelable(false);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(bootProgress, TAG_DIALOG_BOOT);
		transaction.commitAllowingStateLoss();
	}

	@Override
	public void onCancelProgress() {
		isBootCancel = true;
		FROForm.getInstance().cancelBoot();
		showProgress(0);
	}

	/**
	 * ダウンロードの進捗を表示するプログレスダイアログが非表示となった際に実行する処理
	 */
	public void dismissLoadingProgressSMP() {
		if (bootProgress != null) {
			bootProgress.stopLoading();
			bootProgress.setMessage(getString(R.string.msg_start_music_shortly));
			bootProgress.setLoadingValue(100);
			getUiHandler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Fragment boot = getSupportFragmentManager().findFragmentByTag(TAG_DIALOG_BOOT);
					if (boot != null) {
						FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
						transaction.remove(boot);
						transaction.commitAllowingStateLoss();
					}
					bootProgress = null;
				}
			}, 300);
		}
	}

	/**
	 * ダウンロードの進捗を表示するプログレスダイアログの進捗を開始する
	 */
	public void startLoadingProgressSMP() {
		if (bootProgress != null)
			bootProgress.startLoading();
	}

	/*
	 * ダウンロードの進捗を表示するプログレスダイアログの進捗を停止する
	 */
	public void stopLoadingProgressSMP() {
		if (bootProgress != null)
			bootProgress.stopLoading();
	}

	/**
	 * ダウンロードの進捗を表示するプログレスダイアログの進捗率を変更する
	 * 
	 * @param value
	 *            設定したい値
	 */
	public void setLoadingValueSMP(int value) {
		if (bootProgress != null) {
			bootProgress.setLoadingValue(value);
		}
	}

	/**
	 * ダウンロードの進捗を表示するプログレスダイアログのメッセージを変更する
	 * 
	 * @param message
	 *            設定したいメッセージ
	 */
	public void setLoadingMessageSMP(String message) {
		if (bootProgress != null) {
			bootProgress.setMessage(message);
		}
	}

	/**
	 * ダウンロードの進捗を表示するプログレスダイアログの停止地点を変更する
	 * 
	 * @param stopAt
	 *            設定したい値
	 */
	public void setStopAtSMP(int stopAt) {
		if (bootProgress != null) {
			bootProgress.setStopAt(stopAt);
		}
	}

	/**
	 * ダウンロードの進捗を表示するプログレスダイアログのキャンセルボタンの表示を切り替える
	 * 
	 * @param enabled
	 *            true:表示, false:非表示
	 */
	public void disableLoadingCancelSMP(boolean enabled) {
		if (bootProgress != null) {
			bootProgress.setCancelEnable(enabled);
		}
	}

	/**
	 * トーストを表示する
	 * 
	 * @param txt
	 *            表示する文言
	 */
	public void showToast(String txt) {
		Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
	}

	public void onReceiveDriveStopping(boolean isParking) {
	}

	@Override
	public void onBtnClicked(int resId) {
		switch (resId) {
		case DIALOG_TAG_ON_NOTIFY_ERROR:
			startActivity(LoginActivity.class, true);
			break;
		}
	}

	@Override
	public void onBtnClicked(int resId, String txt) {
	}

	@Override
	public void onItemClicked(int tag, int pos) {
	}
}
