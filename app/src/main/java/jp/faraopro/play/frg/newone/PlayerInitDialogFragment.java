package jp.faraopro.play.frg.newone;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import jp.faraopro.play.R;

/**
 * メッセージとボタン(最大で2つ)からなるダイアログを表示するFragment
 * 
 * @author AIM
 * 
 */
public class PlayerInitDialogFragment extends DialogFragment {
	/** const **/
	public static final int TYPE_NOMAL = 0; // ボタンなし
	public static final int TYPE_CANCELABLE = 1; // Cancelボタン

	/** bundle key **/
	public static final String BUTTON_TYPE = "BUTTON_TYPE";
	public static final String MAX_VALUE = "MAX_VALUE";
	public static final String TITLE = "TITLE";
	public static final String MESSAGE = "MESSAGE";

	/** member **/
	private int type;
	private int max;
	private int progress;
	private int stopAt;
	private String title;
	private String message;
	private ProgressDialog dialog;
	private onCancelListener listener;
	private LoadingThread counter;

	/**
	 * リスナーに対するボタン押下イベントの通知
	 * 
	 * @author AIM
	 * 
	 */
	public interface onCancelListener {
		public void onCancelProgress();
	}

	public PlayerInitDialogFragment() {
		type = TYPE_NOMAL;
		max = 100;
		progress = 0;
		stopAt = 0;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (activity instanceof onCancelListener == true)
			listener = ((onCancelListener) activity);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle args = getArguments();
		if (args != null) {
			type = args.getInt(BUTTON_TYPE);
			int tmp = args.getInt(MAX_VALUE);
			if (tmp != 0)
				max = tmp;
			title = args.getString(TITLE);
			message = args.getString(MESSAGE);
		}
		if (title == null)
			title = "";
		if (message == null)
			message = "";

		dialog = new ProgressDialog(getActivity());
		dialog.setTitle(title);
		if (!TextUtils.isEmpty(message)) {
			dialog.setMessage(message);
		} else {
			dialog.setMessage(getText(R.string.msg_now_loading));
		}
		dialog.setMax(max);
		dialog.setIndeterminate(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		if (type == TYPE_CANCELABLE) {
			dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.btn_cancel), new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (listener != null) {
						listener.onCancelProgress();
					}
				}
			});
			dialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					stopLoading();
					dismissDialog();
				}
			});
		}
		dialog.setProgress(0);

		return dialog;
	}

	public void startLoading() {
		if (counter == null)
			counter = new LoadingThread();
		counter.startThread();
	}

	public void stopLoading() {
		if (counter != null)
			counter.stopThread();
		counter = null;
	}

	/**
	 * プレイヤー起動時のローディングダイアログの進捗率を変更する
	 * 
	 * @param value
	 *            設定したい値
	 */
	public void setLoadingValue(int value) {
		if (dialog != null && dialog.getProgress() < value) {
			dialog.setProgress(value);
			progress = value;
		}
	}

	/**
	 * プレイヤー起動時のローディングダイアログのメッセージを変更する
	 * 
	 * @param message
	 *            設定したいメッセージ
	 */
	public void setMessage(String message) {
		if (dialog != null)
			dialog.setMessage(message);
	}

	/**
	 * プレイヤー起動時のローディングダイアログのメッセージの停止地点を変更する
	 * 
	 * @param stopAt
	 *            設定したい値
	 */
	public void setStopAt(int stopAt) {
		if (stopAt > this.stopAt)
			this.stopAt = stopAt;
	}

	public void setCancelEnable(boolean enabled) {
		if (dialog == null)
			return;

		Button cancel = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		cancel.setEnabled(enabled);
		if (enabled) {
			cancel.setVisibility(View.VISIBLE);
		} else {
			cancel.setVisibility(View.INVISIBLE);
		}
	}

	public class LoadingThread {
		private boolean stop;
		private Thread thread;

		public LoadingThread() {
			stop = false;
		}

		public void startThread() {
			stop = false;
			thread = new Thread(new Runnable() {

				@Override
				public void run() {
					while (progress < max && !stop) {
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (progress < stopAt) {
							progress += 1;
							if (!stop && dialog != null)
								dialog.setProgress(progress);
						} else {
							if (!stop && dialog != null)
								dialog.setProgress(stopAt);
						}
					}
				}
			});
			thread.start();
		}

		public void stopThread() {
			stop = true;
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (counter != null)
			counter.stopThread();
		counter = null;
		listener = null;
		dialog = null;
	}

	public void dismissDialog() {
		if (counter != null)
			counter.stopThread();
		counter = null;
		listener = null;
		dismissAllowingStateLoss();
	}
}
