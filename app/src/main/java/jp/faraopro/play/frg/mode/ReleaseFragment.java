package jp.faraopro.play.frg.mode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import net.simonvt.numberpicker.NumberPicker;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.model.ChannelMode;
import jp.faraopro.play.util.Utils;

/**
 * 
 * @author Aim タイトルバー
 */
public class ReleaseFragment extends Fragment implements IModeFragment {
	/** const **/
	private static final int CHANNEL_ID_JP = 1000000;
	private static final int CHANNEL_ID_EN = 2000000;
	private static final int CURRENT_YEAR = Integer.parseInt(Utils.getNowDateString("yyyy"));

	/** bundle key **/

	/** view **/
	private net.simonvt.numberpicker.NumberPicker npkStart;
	private net.simonvt.numberpicker.NumberPicker npkEnd;
	// private DatePicker dpkStart;
	// private DatePicker dpkEnd;
	private CheckBox chkEn;
	private CheckBox chkJp;
	private RelativeLayout rltEngOnly;
	private RelativeLayout rltJpnOnly;
	private ImageButton ibtnStart;
	private LinearLayout lnrBottom;

	/** member **/

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		/*** スマホ用アニメーション ***/
		if (transit == MainActivity.ANIM_HIDE_FLICK) {
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_flip_horizontal_exit);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_flip_horizontal_exit);
			}
		}
		if (transit == MainActivity.ANIM_SHOW_FLICK) {
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_flip_horizontal_enter);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_flip_horizontal_enter);
			}
		}

		return super.onCreateAnimation(transit, enter, nextAnim);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_frg_release, container, false);
		initViews(view);

		return view;
	}

	// Viewの初期化(参照取得、イベント設定など)
	@SuppressLint("SetJavaScriptEnabled")
	private void initViews(View view) {
		Bundle args = getArguments();
		if (args != null) {
		}

		npkStart = (NumberPicker) view.findViewById(R.id.release_npk_start);
		npkStart.setMinValue(1960);
		npkStart.setMaxValue(CURRENT_YEAR);
		npkStart.setValue(1992);
		npkStart.setWrapSelectorWheel(false);
		npkStart.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				if (newVal > npkEnd.getValue()) {
					npkStart.setValue(npkEnd.getValue());
				}
			}
		});

		npkEnd = (NumberPicker) view.findViewById(R.id.release_npk_end);
		npkEnd.setMinValue(1960);
		npkEnd.setMaxValue(CURRENT_YEAR);
		npkEnd.setValue(CURRENT_YEAR);
		npkEnd.setWrapSelectorWheel(false);
		npkEnd.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				if (newVal < npkStart.getValue()) {
					npkEnd.setValue(npkStart.getValue());
				}
			}
		});

		// checkbox,button
		chkEn = (CheckBox) view.findViewById(R.id.release_chk_en);
		chkEn.setChecked(true);
		// // チェックボタンの排他
		// chkEn.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if (chkJp.isChecked())
		// chkJp.setChecked(false);
		// }
		// });
		chkJp = (CheckBox) view.findViewById(R.id.release_chk_jp);
		chkJp.setChecked(false);
		// // チェックボタンの排他
		// chkJp.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if (chkEn.isChecked())
		// chkEn.setChecked(false);
		// }
		// });
		rltEngOnly = (RelativeLayout) view.findViewById(R.id.release_rlt_en);
		rltEngOnly.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chkEn.setChecked(!chkEn.isChecked());
				if (chkJp.isChecked()) {
					chkJp.setChecked(false);
				} else {
					if (!chkEn.isChecked())
						chkEn.setChecked(true);
				}
			}
		});
		rltJpnOnly = (RelativeLayout) view.findViewById(R.id.release_rlt_jp);
		rltJpnOnly.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chkJp.setChecked(!chkJp.isChecked());
				if (chkEn.isChecked()) {
					chkEn.setChecked(false);
				} else {
					if (!chkJp.isChecked())
						chkJp.setChecked(true);
				}
			}
		});
		ibtnStart = (ImageButton) view.findViewById(R.id.release_ibtn_start);
		ibtnStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int lang = 0;
				String range;
				if (chkJp.isChecked()) {
					lang = CHANNEL_ID_JP;
				} else if (chkEn.isChecked()) {
					lang = CHANNEL_ID_EN;
				}
				range = npkStart.getValue() + ":" + npkEnd.getValue();
				((MainActivity) getActivity()).bootPlayer(ChannelMode.RELEASE, lang, range);
			}
		});
		lnrBottom = (LinearLayout) view.findViewById(R.id.release_lnr_bottom);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if (hidden)
			hideKeyboard();
	}

	// ソフトウェアキーボードを隠す
	private void hideKeyboard() {
		if (npkStart == null)
			return;

		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(npkStart.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
		if (FROForm.getInstance().isMenu(Consts.TAG_MODE_RELEASE, getActivity())) {
			((IModeActivity) getActivity()).showMode(MainActivity.TOP);
		} else {
			((IModeActivity) getActivity()).showMode(MainActivity.OTHER);
		}
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
