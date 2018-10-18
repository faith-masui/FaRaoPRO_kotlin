package jp.faraopro.play.frg.mode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.domain.ChannelInfo;
import jp.faraopro.play.domain.FROFavoriteChannelDB;
import jp.faraopro.play.domain.FROFavoriteChannelDBFactory;

/**
 * 
 * @author Aim タイトルバー
 */
public class FavoriteEditFragment extends Fragment implements IModeFragment {
	/** const **/

	/** bundle key **/

	/** view **/
	// private net.simonvt.numberpicker.NumberPicker npkHour;
	// private net.simonvt.numberpicker.NumberPicker npkMinute;
	private EditText edtName;
	private LinearLayout lnrBottom;

	// private Button btnOn;
	// private Button btnOff;
	// private Button btnSet;
	// private LinearLayout lnrResource;
	// private LinearLayout lnrWeek;
	// private TextView txtSelectedResource;
	// private TextView txtSelectedWeek;
	// private LinearLayout lnrBottom;

	/** member **/
	// private TimerInfo editedData;

	// @Override
	// public Animation onCreateAnimation(int transit, boolean enter, int
	// nextAnim) {
	// /*** スマホ用アニメーション ***/
	// if (transit == MainActivity.ANIM_HIDE_FLICK) {
	// if (enter) {
	// return AnimationUtils.loadAnimation(getActivity(),
	// R.anim.activity_flip_horizontal_exit);
	// } else {
	// return AnimationUtils.loadAnimation(getActivity(),
	// R.anim.activity_flip_horizontal_exit);
	// }
	// }
	// if (transit == MainActivity.ANIM_SHOW_FLICK) {
	// if (enter) {
	// return AnimationUtils.loadAnimation(getActivity(),
	// R.anim.activity_flip_horizontal_enter);
	// } else {
	// return AnimationUtils.loadAnimation(getActivity(),
	// R.anim.activity_flip_horizontal_enter);
	// }
	// }
	//
	// return super.onCreateAnimation(transit, enter, nextAnim);
	// }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_frg_favorite_edit, container, false);
		initViews(view);

		return view;
	}

	// Viewの初期化(参照取得、イベント設定など)
	@SuppressLint("SetJavaScriptEnabled")
	private void initViews(View view) {
		Bundle args = getArguments();
		if (args != null) {
		}

		RelativeLayout root = (RelativeLayout) view.findViewById(R.id.favorite_edit_rlt_root);
		root.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				hideKeyboard();
				return false;
			}
		});
		edtName = (EditText) view.findViewById(R.id.favorite_edit_edt_name);
		lnrBottom = (LinearLayout) view.findViewById(R.id.favorite_edit_lnr_bottom);
	}

	public void save() {
		String newName = edtName.getText().toString();
		if (TextUtils.isEmpty(newName)) {
			((BaseActivity) getActivity()).showPositiveDialog(R.string.msg_favorite_no_input_name,
					BaseActivity.DIALOG_TAG_MESSAGE, true);
			return;
		}
		FROForm.getInstance().editedChannel.setChannelName(edtName.getText().toString());
		FROFavoriteChannelDB db = FROFavoriteChannelDBFactory.getInstance(getActivity());
		db.update(FROForm.getInstance().editedChannel);
		rest();
		((IModeActivity) getActivity()).showMode(MainActivity.FAVORITE_LOCAL);
		FROForm.getInstance().editedChannel = null;
	}

	private void rest() {
		edtName.setText("");
	}

	// DBから読み込んだタイマー情報を反映する
	private void readData() {
		edtName.setText(FROForm.getInstance().editedChannel.getChannelName());
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if (hidden) {
			hideKeyboard();
		} else {
			if (FROForm.getInstance().editedChannel != null)
				readData();
			else
				FROForm.getInstance().editedChannel = new ChannelInfo();
		}
	}

	// ソフトウェアキーボードを隠す
	private void hideKeyboard() {
		if (edtName == null)
			return;

		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edtName.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public void setBottomPadding(int height) {
		lnrBottom.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, height));
	}

	public void setTopPadding(int height) {

	}

	@Override
	public void show() {
	}

	@Override
	public void updateViews() {
	}

	@Override
	public void onBackPress() {
		rest();
		((IModeActivity) getActivity()).showMode(MainActivity.FAVORITE_DELETE);
		FROForm.getInstance().editedChannel = null;
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(int when, int code) {
		// TODO Auto-generated method stub

	}

}
