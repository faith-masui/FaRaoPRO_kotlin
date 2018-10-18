package jp.faraopro.play.frg.mode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.simonvt.numberpicker.NumberPicker;

import java.util.ArrayList;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.app.TimerHelper;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.domain.FROTimerDB;
import jp.faraopro.play.domain.FROTimerDBFactory;
import jp.faraopro.play.model.TimerInfo;

/**
 * 
 * @author Aim タイトルバー
 */
public class TimerEditFragment extends Fragment implements IModeFragment {
	/** const **/
	private static final int STATUS_NO_ERROR = 0;
	private static final int STATUS_NAME_IS_EMPTY = 1;
	private static final int STATUS_SOURCE_IS_NOT_SELECTED = 2;
	private static final int STATUS_WEEK_IS_NOT_SELECTED = 4;

	/** bundle key **/

	/** view **/
	private net.simonvt.numberpicker.NumberPicker npkHour;
	private net.simonvt.numberpicker.NumberPicker npkMinute;
	private EditText edtName;
	private Button mButtonDelete;
	private Button btnOn;
	private Button btnOff;
	// private Button btnSet;
	private LinearLayout lnrResource;
	private LinearLayout lnrWeek;
	private TextView txtSelectedResource;
	private TextView txtSelectedWeek;
	private LinearLayout lnrBottom;

	/** member **/
	private boolean timerType = true;

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
		View view = inflater.inflate(R.layout.new_frg_timer, container, false);
		initViews(view);

		return view;
	}

	// Viewの初期化(参照取得、イベント設定など)
	@SuppressLint("SetJavaScriptEnabled")
	private void initViews(View view) {
		Bundle args = getArguments();
		if (args != null) {
		}

		RelativeLayout root = (RelativeLayout) view.findViewById(R.id.timer_rlt_root);
		root.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				hideKeyboard();
				return false;
			}
		});

		edtName = (EditText) view.findViewById(R.id.timer_edt_name);
		edtName.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event != null && event.getAction() == KeyEvent.ACTION_UP
						&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					hideKeyboard();
					return true;
				}
				return false;
			}
		});

		mButtonDelete = (Button) view.findViewById(R.id.timer_btn_delete);
		mButtonDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((BaseActivity) getActivity()).showNegativeDialog(R.string.msg_delete_timer,
						MainActivity.DIALOG_TAG_DELETE_TIMER_POS, MainActivity.DIALOG_TAG_DELETE_TIMER_NEG, true);
			}
		});

		npkHour = (NumberPicker) view.findViewById(R.id.timer_npk_hour);
		npkHour.setMinValue(00);
		npkHour.setMaxValue(23);
		npkHour.setWrapSelectorWheel(true);
		npkHour.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

		npkMinute = (NumberPicker) view.findViewById(R.id.timer_npk_minute);
		npkMinute.setMinValue(00);
		npkMinute.setMaxValue(59);
		npkMinute.setWrapSelectorWheel(true);
		npkMinute.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

		btnOn = (Button) view.findViewById(R.id.timer_btn_on);
		btnOn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!btnOn.isSelected()) {
					btnOn.setSelected(true);
					btnOff.setSelected(false);
					lnrResource.setEnabled(btnOn.isSelected());
					timerType = true;
				}
			}
		});
		btnOn.setSelected(true);

		btnOff = (Button) view.findViewById(R.id.timer_btn_off);
		btnOff.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!btnOff.isSelected()) {
					btnOff.setSelected(true);
					btnOn.setSelected(false);
					lnrResource.setEnabled(btnOn.isSelected());
					timerType = false;
					// txtSelectedResource.setText("");
					// FROForm.getInstance().editedTimer.setResource(null);
					// FROForm.getInstance().editedTimer.setResourceName(null);
					// FROForm.getInstance().editedTimer.setType(Consts.MUSIC_TYPE_STOP);
				}
			}
		});

		lnrResource = (LinearLayout) view.findViewById(R.id.timer_lnr_source);
		lnrResource.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setCurrentState();
				((IModeActivity) getActivity()).showMode(MainActivity.TIMER_SELECT_SOURCE);
			}
		});

		lnrWeek = (LinearLayout) view.findViewById(R.id.timer_lnr_week);
		lnrWeek.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setCurrentState();
				((IModeActivity) getActivity()).showMode(MainActivity.WEEK);
			}
		});

		txtSelectedResource = (TextView) view.findViewById(R.id.timer_txt_selected_resource);
		txtSelectedWeek = (TextView) view.findViewById(R.id.timer_txt_selected_week);
		lnrBottom = (LinearLayout) view.findViewById(R.id.release_lnr_bottom);
	}

	private void reset() {
		edtName.setText("");
		txtSelectedResource.setText("");
		txtSelectedWeek.setText("");
		npkHour.setValue(0);
		npkMinute.setValue(0);
		btnOn.setSelected(true);
		btnOff.setSelected(false);
		lnrResource.setEnabled(btnOn.isSelected());
		isFirst = true;
	}

	// DBから読み込んだタイマー情報を反映する
	private void readData() {
		int hour = FROForm.getInstance().editedTimer.getTime() / 60;
		int minute = FROForm.getInstance().editedTimer.getTime() % 60;
		// int type = FROForm.getInstance().editedTimer.getType();
		edtName.setText(FROForm.getInstance().editedTimer.getName());
		npkHour.setValue(hour);
		npkMinute.setValue(minute);
		txtSelectedResource.setText(FROForm.getInstance().editedTimer.getResourceName());
		String week = "";
		byte selectedWeek = FROForm.getInstance().editedTimer.getWeek();
		for (int i = 0; i < Consts.WEEK_BYTE_ARRAY.length; i++) {
			if ((selectedWeek & TimerInfo.WEEK_BYTE_ARRAY[i]) > 0) {
				week += Consts.getWeekStringArray()[i] + "  ";
			}
		}
		txtSelectedWeek.setText(week);
		btnOff.setSelected(!timerType);
		btnOn.setSelected(timerType);
		lnrResource.setEnabled(btnOn.isSelected());

		if (FROForm.getInstance().editedTimer.getId() != -1) {
			mButtonDelete.setVisibility(View.VISIBLE);
		} else {
			mButtonDelete.setVisibility(View.GONE);
		}
	}

	// 現在の編集内容をメンバクラスに保存する
	private void setCurrentState() {
		int time = npkHour.getValue() * 60 + npkMinute.getValue();
		String name = edtName.getText().toString();

		FROForm.getInstance().editedTimer.setTime(time);
		FROForm.getInstance().editedTimer.setName(name);
	}

	boolean isFirst = true;

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if (hidden) {
			hideKeyboard();
		} else {
			if (FROForm.getInstance().editedTimer == null) {
				FROForm.getInstance().editedTimer = new TimerInfo();
				timerType = true;
			} else {
				// タイマー編集の場合、画面遷移直後だけタイプを読み込む、それ以降はボタンの状態を保存する
				if (isFirst) {
					isFirst = false;
					if (FROForm.getInstance().editedTimer.getType() > 0) {
						timerType = true;
					} else {
						timerType = false;
					}
				}
			}
			readData();
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
		reset();
		((IModeActivity) getActivity()).showMode(MainActivity.TIMER);
		FROForm.getInstance().editedTimer = null;
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(int when, int code) {
		// TODO Auto-generated method stub

	}

	private int checkInput() {
		int result = STATUS_NO_ERROR;

		String name = edtName.getText().toString();
		TimerInfo edited = FROForm.getInstance().editedTimer;
		if (TextUtils.isEmpty(name))
			result += STATUS_NAME_IS_EMPTY;
		if (edited != null && (edited.getWeekList() == null || edited.getWeekList().size() < 1))
			result += STATUS_WEEK_IS_NOT_SELECTED;
		if (btnOn.isSelected() && TextUtils.isEmpty(edited.getResource()))
			result += STATUS_SOURCE_IS_NOT_SELECTED;

		return result;
	}

	public void deleteTimer() {
		String id = Integer.toString(FROForm.getInstance().editedTimer.getId());
		FROTimerDBFactory.getInstance(getActivity()).delete(id);
		TimerHelper.setMusicTimer(getActivity());
		onBackPress();
	}

	public void saveTimer() {
		String errorMsg = "";
		switch (checkInput()) {

		// 入力に不備がない場合
		case STATUS_NO_ERROR:
			setCurrentState();
			FROTimerDB db = FROTimerDBFactory.getInstance(getActivity());
			ArrayList<TimerInfo> timerList = (ArrayList<TimerInfo>) db.check(FROForm.getInstance().editedTimer);
			if (timerList == null || timerList.size() < 1) {
				if (btnOff.isSelected()) {
					FROForm.getInstance().editedTimer.setResource(null);
					FROForm.getInstance().editedTimer.setResourceName(null);
					FROForm.getInstance().editedTimer.setType(Consts.MUSIC_TYPE_STOP);
				}
				// 既存のタイマーを編集している場合
				if (FROForm.getInstance().editedTimer.getId() != -1) {
					db.update(FROForm.getInstance().editedTimer);
				}
				// 新規タイマーを作成している場合
				else {
					db.insert(FROForm.getInstance().editedTimer);
				}
				TimerHelper.setMusicTimer(getActivity());
				onBackPress();
			} else {
				((BaseActivity) getActivity()).showToast(getString(R.string.msg_time_schedule_duplication));
			}
			break;

		// 入力に不備がある場合
		case STATUS_NAME_IS_EMPTY:
			errorMsg = getString(R.string.msg_time_schedule_no_input_name);
			break;
		case STATUS_SOURCE_IS_NOT_SELECTED:
			errorMsg = getString(R.string.msg_time_schedule_no_select_source);
			break;
		case STATUS_WEEK_IS_NOT_SELECTED:
			errorMsg = getString(R.string.msg_time_schedule_no_select_weekday);
			break;
		default:
			errorMsg = getString(R.string.msg_time_schedule_no_select_all);
			break;
		}
		if (!TextUtils.isEmpty(errorMsg))
			((BaseActivity) getActivity()).showToast(errorMsg);
	}
}
