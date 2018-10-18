package jp.faraopro.play.frg.newone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * メッセージとボタン(最大で2つ)からなるダイアログを表示するFragment
 * 
 * @author AIM
 * 
 */
@SuppressLint("ValidFragment")
public class SimpleDialogFragment extends DialogFragment {
	/** const **/
	public static final int TYPE_POSITIVE = 0; // OKボタンのみ
	public static final int TYPE_NEGATIVE = 1; // OKボタンとCancelボタン

	/** bundle key **/
	public static final String BUTTON_TYPE = "BUTTON_TYPE";
	public static final String BUTTON_ID_POS = "BUTTON_ID_POS";
	public static final String BUTTON_ID_NEG = "BUTTON_ID_NEG";
	public static final String CANCEL_ID = "CANCEL_ID";
	public static final String BUTTON_NAME_POS = "BUTTON_NAME_POS";
	public static final String BUTTON_NAME_NEG = "BUTTON_NAME_NEG";
	public static final String TITLE = "TITLE";
	public static final String MESSAGE = "MESSAGE";

	/** member **/
	private int type;
	private int posBtnId = 0;
	private int negBtnId = 0;
	private int cancelId = 0;
	private int posBtnName = 0;
	private int negBtnName = 0;
	private String title;
	private String message;

	private AlertDialog.Builder builder;
	private OnBtnClickListener btnClickListener;

	/**
	 * リスナーに対するボタン押下イベントの通知
	 * 
	 * @author AIM
	 * 
	 */
	public interface OnBtnClickListener {
		public void onBtnClicked(int resId);
	}

	/**
	 * @deprecated use bundle
	 * @param type
	 */
	@Deprecated
	public SimpleDialogFragment(int type) {
		if (type == TYPE_POSITIVE || type == TYPE_NEGATIVE)
			this.type = type;
		else
			this.type = TYPE_POSITIVE;
	}

	public SimpleDialogFragment() {
		type = TYPE_POSITIVE;
	}

	/**
	 * ボタンIDをセットする(ボタン1つ)
	 * 
	 * @param pos
	 *            ボタンID
	 */
	public void setButtonId(int pos) {
		posBtnId = pos;
	}

	/**
	 * ボタンIDをセットする(ボタン2つ)
	 * 
	 * @param pos
	 *            PositiveボタンID
	 * @param neg
	 *            NegativeボタンID
	 */
	public void setButtonId(int pos, int neg) {
		posBtnId = pos;
		negBtnId = neg;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (activity instanceof OnBtnClickListener == true)
			btnClickListener = ((OnBtnClickListener) activity);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle args = getArguments();
		if (args != null) {
			type = args.getInt(BUTTON_TYPE);
			posBtnId = args.getInt(BUTTON_ID_POS);
			negBtnId = args.getInt(BUTTON_ID_NEG);
			cancelId = args.getInt(CANCEL_ID);
			posBtnName = args.getInt(BUTTON_NAME_POS);
			negBtnName = args.getInt(BUTTON_NAME_NEG);
			title = args.getString(TITLE);
			message = args.getString(MESSAGE);
		}
		if (posBtnId == 0)
			posBtnId = android.R.string.ok;
		if (negBtnId == 0)
			negBtnId = android.R.string.cancel;
		if (posBtnName == 0)
			posBtnName = android.R.string.ok;
		if (negBtnName == 0)
			negBtnName = android.R.string.cancel;
		if (title == null)
			title = "";
		if (message == null)
			message = "";

		builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(posBtnName, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (btnClickListener != null) {
					// if (posBtnId == -1)
					// btnClickListener.onBtnClicked(android.R.string.ok);
					// else
					btnClickListener.onBtnClicked(posBtnId);
				}
			}
		});
		if (type == TYPE_NEGATIVE) {
			builder.setNegativeButton(negBtnName, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (btnClickListener != null) {
						// if (posBtnId == -1)
						// btnClickListener
						// .onBtnClicked(android.R.string.cancel);
						// else
						btnClickListener.onBtnClicked(negBtnId);
					}
				}
			});
		}

		return builder.create();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		if (cancelId > 0 && btnClickListener != null)
			btnClickListener.onBtnClicked(cancelId);
	}

	/**
	 * @deprecated use bundle
	 * @param title
	 */
	@Deprecated
	public void setTitle(String title) {
		this.title = title;
		if (builder != null)
			builder.setTitle(title);
	}

	/**
	 * @deprecated use bundle
	 * @param message
	 *            メッセージ
	 */
	@Deprecated
	public void setMessage(String message) {
		this.message = message;
		if (builder != null)
			builder.setMessage(message);
	}

}
