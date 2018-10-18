package jp.faraopro.play.frg.newone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * メッセージとボタン(最大で2つ)からなるダイアログを表示するFragment
 * 
 * @author AIM
 * 
 */
@SuppressLint("ValidFragment")
public class ListDialogFragment extends DialogFragment {
	/** const **/

	/** bundle key **/
	public static final String KEY_TAG = "KEY_TAG";
	public static final String KEY_TITLE = "BUTTON_ID_POS";
	public static final String KEY_STRING_TABLE = "LEY_STRING_TABLE";

	/** member **/
	private int dialogTag;
	private String title;
	private String[] items;
	private AlertDialog.Builder builder;
	private ItemClickListener itemClickListener;

	/**
	 * リスナーに対するボタン押下イベントの通知
	 * 
	 * @author AIM
	 * 
	 */
	public interface ItemClickListener {
		public void onItemClicked(int tag, int pos);
	}

	public ListDialogFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (activity instanceof ItemClickListener == true)
			itemClickListener = ((ItemClickListener) activity);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle args = getArguments();
		if (args != null) {
			dialogTag = args.getInt(KEY_TAG);
			title = args.getString(KEY_TITLE);
			items = args.getStringArray(KEY_STRING_TABLE);
		}

		builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (itemClickListener != null)
					itemClickListener.onItemClicked(dialogTag, which);
			}
		});

		return builder.create();
	}

}
