package jp.faraopro.play.frg.newone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import jp.faraopro.play.R;

/**
 * メッセージとボタン(最大で2つ)からなるダイアログを表示するFragment
 * 
 * @author AIM
 * 
 */
@SuppressLint("ValidFragment")
public class TextBoxDialogFragment extends DialogFragment implements OnClickListener {
	/** const **/
	// public static final int TYPE_POSITIVE = 0; // OKボタンのみ
	// public static final int TYPE_NEGATIVE = 1; // OKボタンとCancelボタン

	/** bundle key **/
	// public static final String BUTTON_TYPE = "BUTTON_TYPE";
	public static final String BUTTON_ID_POS = "BUTTON_ID_POS";
	public static final String BUTTON_ID_NEG = "BUTTON_ID_NEG";
	public static final String BUTTON_NAME_POS = "BUTTON_NAME_POS";
	public static final String BUTTON_NAME_NEG = "BUTTON_NAME_NEG";
	public static final String TITLE = "TITLE";
	public static final String MESSAGE = "MESSAGE";

	/** view **/
	private EditText edtInput;
	private Button btnPos;

	/** member **/
	// private int type;
	private int posBtnId = 0;
	private int negBtnId = 0;
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
		public void onBtnClicked(int resId, String txt);
	}

	public static TextBoxDialogFragment getInstance(int posId, int negId, String title, String message, int posName,
			int negName) {
		TextBoxDialogFragment dialog = new TextBoxDialogFragment();

		Bundle args = new Bundle();
		args.putInt(BUTTON_ID_POS, posId);
		args.putInt(BUTTON_ID_NEG, negId);
		if (posName != -1)
			args.putInt(BUTTON_NAME_POS, posName);
		if (negName != -1)
			args.putInt(BUTTON_NAME_NEG, negName);
		args.putString(TITLE, title);
		args.putString(MESSAGE, message);
		dialog.setArguments(args);

		return dialog;
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
			posBtnId = args.getInt(BUTTON_ID_POS);
			negBtnId = args.getInt(BUTTON_ID_NEG);
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
		// if (title == null)
		// title = "";
		// if (message == null)
		// message = "";

		// ダイアログのコンテンツ部分
		LayoutInflater i = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View content = i.inflate(R.layout.new_res_dialog_textbox, null);

		edtInput = (EditText) content.findViewById(R.id.textdialog_edt_input);
		edtInput.setText(message);
		builder = new AlertDialog.Builder(getActivity());
		builder.setView(content);
		builder.setTitle(title);
		// builder.setMessage(message);
		builder.setPositiveButton(posBtnName, this);
		builder.setNegativeButton(negBtnName, this);
		AlertDialog dialog = builder.create();

		// ダイアログがshowされた後でないとButtonのインスタンスが取得できないため、
		// onShowが呼ばれたのちにイベントをセットする
		dialog.setOnShowListener(new OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				btnPos = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
				edtInput.addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						if (btnPos != null) {
							if (TextUtils.isEmpty(s)) {
								btnPos.setEnabled(false);
							} else {
								btnPos.setEnabled(true);
							}
						}
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
					}
				});
			}
		});

		// ダイアログ外タップで消えないように設定
		dialog.setCanceledOnTouchOutside(false);

		return dialog;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// リスナーがnullでなければ押されたボタンのリソースIDを通知する
		if (btnClickListener != null) {
			int resId = -1;
			switch (which) {
			// OKボタンが押された場合
			case DialogInterface.BUTTON_POSITIVE:
				resId = posBtnId;
				break;
			// Cancelボタンが押された場合
			case DialogInterface.BUTTON_NEGATIVE:
				resId = negBtnId;
				break;
			}
			btnClickListener.onBtnClicked(resId, edtInput.getText().toString());
		}
	}

}
