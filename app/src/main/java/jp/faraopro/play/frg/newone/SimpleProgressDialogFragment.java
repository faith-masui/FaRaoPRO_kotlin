package jp.faraopro.play.frg.newone;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import jp.faraopro.play.R;
import jp.faraopro.play.common.ITimerListener;
import jp.faraopro.play.common.SimpleTimer;

/**
 * メッセージとボタン(最大で2つ)からなるダイアログを表示するFragment
 * 
 * @author AIM
 * 
 */
public class SimpleProgressDialogFragment extends DialogFragment implements ITimerListener {
	/** const **/
	public static final int TYPE_NOMAL = 0; // ボタンなし
	public static final int TYPE_CANCELABLE = 1; // Cancelボタン

	/** bundle key **/
	public static final String BUTTON_TYPE = "BUTTON_TYPE";
	public static final String TITLE = "TITLE";
	public static final String MESSAGE = "MESSAGE";
	public static final String TIMEOUT = "TIMEOUT";

	/** member **/
	private int type = TYPE_NOMAL;
	private int mTimeout = 30;
	private String title;
	private String message;
	private ProgressDialog dialog;
	private onCancelListener listener;
	private SimpleTimer limit;

	/**
	 * リスナーに対するボタン押下イベントの通知
	 * 
	 * @author AIM
	 * 
	 */
	public interface onCancelListener {
		public void onCancel();
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
			title = args.getString(TITLE);
			message = args.getString(MESSAGE);
			mTimeout = args.getInt(TIMEOUT, 30);
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
		dialog.setIndeterminate(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		if (type == TYPE_CANCELABLE) {
			dialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					if (listener != null)
						listener.onCancel();
				}
			});
		}

		// タイマーセット
		if (mTimeout > 0) {
			limit = new SimpleTimer(this, false);
			limit.start(mTimeout * 1000);
		}

		return dialog;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		listener = null;
		if (limit != null) {
			limit.stop();
			limit = null;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		listener = null;
		dialog = null;
		if (limit != null) {
			limit.stop();
			limit = null;
		}
	}

	public void dismissDialog() {
		if (getDialog() != null && isAdded())
			getDialog().dismiss();
		listener = null;
		if (limit != null) {
			limit.stop();
			limit = null;
		}
	}

	@Override
	public void onTimerEvent(int type) {
		if (limit != null) {
			limit.stop();
			limit = null;
		}
		dismissDialog();
	}
}
