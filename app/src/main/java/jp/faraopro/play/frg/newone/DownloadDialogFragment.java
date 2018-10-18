package jp.faraopro.play.frg.newone;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;

/**
 * メッセージとボタン(最大で2つ)からなるダイアログを表示するFragment
 * 
 * @author AIM
 * 
 */
public class DownloadDialogFragment extends DialogFragment {
	/** const **/
	private static final boolean DEBUG = true;

	/** bundle key **/
	public static final String TITLE = "TITLE";
	public static final String MAX_SIZE = "MAX_SIZE";
	public static final String MESSAGE = "MESSAGE";
	public static final String LENGTH = "LENGTH";
	public static final String LISTENER = "LISTENER";

	/** member **/
	private int mMaxSize = 0;
	private int mTimeCounter;
	private int mCurrentIndex;
	private String mTitle = "";
	// private String mMessage = "";
	private ArrayList<String> mMessageList;
	private ArrayList<String> mLengthList;
	private ProgressDialog mDialog;
	private Handler mHandler;

	// private IDownloadDialogListener listener;

	// public interface IDownloadDialogListener {
	// public void onComplete();
	//
	// public void onError();
	// }

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// if (activity instanceof IDownloadDialogListener == true)
		// listener = ((IDownloadDialogListener) activity);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mHandler = new Handler(Looper.getMainLooper());
		Bundle args = getArguments();
		if (args != null) {
			mTitle = args.getString(TITLE);
			// mMessage = args.getString(MESSAGE);
			mMaxSize = args.getInt(MAX_SIZE, 0);
			mMessageList = args.getStringArrayList(MESSAGE);
			mLengthList = args.getStringArrayList(LENGTH);
			// listener = args.getParcelable(LISTENER);
		}

		mDialog = new ProgressDialog(getActivity());
		mDialog.setTitle("ファイルのダウンロード");
		mDialog.setIndeterminate(false);
		mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mDialog.setMax(100);
		return mDialog;
	}

	// /**
	// * UIを生成する。
	// */
	// @Override
	// public View onCreateView(LayoutInflater i, ViewGroup c, Bundle b) {
	// mHandler = new Handler(Looper.getMainLooper());
	// View content = i.inflate(R.layout.frg_interrupt_dialog, null);
	//
	// mProgress = (ProgressWheel)
	// content.findViewById(R.id.interruptdialog_progresswheel_progress);
	// ((TextView)
	// content.findViewById(R.id.interruptdialog_text_title)).setText(mTitle);
	// mTextCount = (TextView)
	// content.findViewById(R.id.interruptdialog_text_couter);
	// mTextIndex = (TextView)
	// content.findViewById(R.id.interruptdialog_text_index);
	// mTextMessage = (TextView)
	// content.findViewById(R.id.interruptdialog_text_message);
	//
	// return content;
	// }

	@Override
	public void onDestroy() {
		super.onDestroy();

		mDialog = null;
	}

	// int count = 0;

	public void progress(final int per) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mDialog.setProgress(per);
			}
		});
		// count = 0;
		// mDialog.setProgress(count);
		// Thread thread = new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// while (count < 10) {
		// try {
		// Thread.sleep(1000);
		// count++;
		// mHandler.post(new Runnable() {
		// @Override
		// public void run() {
		// mDialog.setProgress(count * 10);
		// }
		// });
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// onComplete();
		// }
		// });
		// thread.start();
	}

	public void onComplete() {
		dismissDialog();
		// if (listener != null)
		// listener.onComplete();
	}

	public void dismissDialog() {
		if (getDialog() != null && isAdded())
			getDialog().dismiss();
	}
}
