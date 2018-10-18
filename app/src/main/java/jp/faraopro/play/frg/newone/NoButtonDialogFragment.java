package jp.faraopro.play.frg.newone;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * メッセージとボタン(最大で2つ)からなるダイアログを表示するFragment
 * 
 * @author AIM
 * 
 */
@SuppressLint("ValidFragment")
public class NoButtonDialogFragment extends DialogFragment {
	/** const **/

	/** bundle key **/
	public static final String TITLE = "TITLE";
	public static final String MESSAGE = "MESSAGE";

	/** member **/
	private String title;
	private String message;

	private AlertDialog.Builder builder;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle args = getArguments();
		if (args != null) {
			title = args.getString(TITLE);
			message = args.getString(MESSAGE);
		}
		if (title == null)
			title = "";
		if (message == null)
			message = "";

		builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setMessage(message);
		Dialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);

		return dialog;
	}

}
