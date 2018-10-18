package jp.faraopro.play.frg.mode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.domain.FROTimerAutoDBFactory;
import jp.faraopro.play.domain.FROTimerDB;
import jp.faraopro.play.domain.FROTimerDBFactory;
import jp.faraopro.play.domain.MainPreference;
import jp.faraopro.play.mclient.MCUserInfoPreference;
import jp.faraopro.play.model.TimerInfo;
import jp.faraopro.play.view.CustomListItem;

public class TimerFragment extends ListBaseFragment {
	/** const **/

	/** bundle key **/

	/** view **/
	private Button mButton1st; // 1つ目のボタン
	private Button mButton2nd; // 2つ目のボタン
	private Switch mToggleEnableTimer; // タイマー機能の有効無効切り替えボタン
	private Switch mToggleEnableRestoration; // 復元機能の有効無効切り替えボタン
	private TextView mTextNodata;
	// private TextView mTextNodata;

	/** member **/
	private ArrayList<TimerInfo> timerList;

	@Override
	protected void initViews(View view) {
		super.initViews(view);

		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// タイマー機能の有効無効切り替えボタン
		View toggleSwitches = inflater.inflate(R.layout.parts_timer_2toggle_switch, null);
		setOptionalView(1, toggleSwitches);
		mToggleEnableTimer = (Switch) toggleSwitches.findViewById(R.id.timer_switch_enable_timer);
		// mToggleEnableTimer.setText(R.string.page_title_setting_time_shedule);
		// mToggleEnableTimer.setChecked(MainPreference.getInstance(getActivity()).getMusicTimerEnable());
		mToggleEnableTimer.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				setTimerEnable(isChecked);
			}
		});
		// タイマー復元機能の有効無効切り替えボタン
		mToggleEnableRestoration = (Switch) toggleSwitches.findViewById(R.id.timer_switch_enable_restoration);
		// mToggleEnableRestoration.setText("直近タイマーの復元");
		// mToggleEnableRestoration.setChecked(MainPreference.getInstance(getActivity()).getRestoreMusicTimerEnable());
		mToggleEnableRestoration.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				MainPreference.getInstance(getActivity()).setRestoreMusicTimerEnable(isChecked);
			}
		});

		// 表示要素が無かった際の文言表示エリア
		View nodata = inflater.inflate(R.layout.new_res_plane_text, null);
		nodata.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setOptionalView(0, nodata);
		mTextNodata = (TextView) nodata.findViewById(R.id.planetext_txt_text);
		mTextNodata.setText(R.string.msg_toast_no_time_schedule_data);

		// 新規作成、テンプレートインポートボタン
		View timerCreateButtons = inflater.inflate(R.layout.new_res_2button, null);
		timerCreateButtons.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setOptionalView(1, timerCreateButtons);
		// 1つ目のボタン
		mButton1st = (Button) timerCreateButtons.findViewById(R.id.button_btn_first);
		// 2つ目のボタン
		mButton2nd = (Button) timerCreateButtons.findViewById(R.id.button_btn_second);
		mButton2nd.setText(R.string.btn_import_template);
		mButton2nd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((IModeActivity) getActivity()).showMode(MainActivity.IMPORT_TIMER_TEMPLATE);
			}
		});
	}

	private void updateUi() {
		int timerType = MainPreference.getInstance(getActivity()).getMusicTimerType();
		if (timerType == Consts.MUSIC_TIMER_TYPE_AUTO) {
			mButton1st.setText(R.string.btn_quit_automatic_updating);
			mButton1st.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					((BaseActivity) getActivity()).showNegativeDialog(R.string.msg_quit_automatic_updating,
							MainActivity.DIALOG_TAG_STOP_AUTO_UPDATE_TIMER, BaseActivity.DIALOG_TAG_MESSAGE, true);
				}
			});
			mButton2nd.setVisibility(View.INVISIBLE);
			((MainActivity) getActivity()).setHeaderRightButton(null);
		} else {
			mButton1st.setText(R.string.page_title_setting_time_shedule_new);
			mButton1st.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					FROTimerDB db = FROTimerDBFactory.getInstance(getActivity());
					if (db.getSize() < 99) {
						FROForm.getInstance().editedTimer = null;
						((IModeActivity) getActivity()).showMode(MainActivity.TIMER_EDIT);
					} else {
						((BaseActivity) getActivity())
								.showToast(getString(R.string.msg_time_schedule_regist_error_maxsize));
					}
				}
			});
			mButton2nd.setVisibility(View.VISIBLE);
			// ((MainActivity)
			// getActivity()).setHeaderRightButton(getString(R.string.page_title_edit));
		}
		mToggleEnableRestoration.setChecked(MainPreference.getInstance(getActivity()).getRestoreMusicTimerEnable());
		mToggleEnableTimer.setChecked(MainPreference.getInstance(getActivity()).getMusicTimerEnable());
		// 値に差異が無い場合、onCheckedChanged が呼び出されない為、意図的に呼び出す
		setTimerEnable(mToggleEnableTimer.isChecked());
	}

	// タイマー設定の値を変更し、同時に復元設定の表示も更新する
	private void setTimerEnable(boolean enabled) {
		MainPreference.getInstance(getActivity()).setMusicTimerEnable(enabled);
		FROForm.getInstance().setPreferenceBoolean(MCUserInfoPreference.BOOLEAN_DATA_ENABLE_MUSIC_TIMER, enabled);
		// enable = false の場合はタイマー復元機能も併せて OFF にする
		if (!enabled) {
			mToggleEnableRestoration.setChecked(enabled);
		}
		mToggleEnableRestoration.setEnabled(enabled);
	}

	@Override
	public void getData() {
		int timerType = MainPreference.getInstance(getActivity()).getMusicTimerType();
		if (timerType == Consts.MUSIC_TIMER_TYPE_AUTO) {
            timerList = (ArrayList<TimerInfo>) FROTimerAutoDBFactory.getInstance(getActivity()).findAllOrderByTime();
        } else {
            timerList = (ArrayList<TimerInfo>) FROTimerDBFactory.getInstance(getActivity()).findAllOrderByTime();
        }
        if (timerList != null)
            Collections.sort(timerList, TimerInfo.COMPARATOR_BY_TIME_AND_WEEK);
		show();
	}

	@Override
	public void show() {
		updateUi();
		((BaseActivity) getActivity()).dismissProgress();
		ArrayList<CustomListItem> list = new ArrayList<CustomListItem>();
		if (timerList != null && timerList.size() > 0) {
			for (TimerInfo ti : timerList) {
				CustomListItem item = new CustomListItem(ti);
				item.setDummy(true);
				list.add(item);
			}
		}
		setContents(list);
		if (list.size() < 1) {
			mTextNodata.setVisibility(View.VISIBLE);
		} else {
			mTextNodata.setVisibility(View.GONE);
		}
		// ((BaseActivity)
		// getActivity()).showToast(getString(R.string.msg_toast_no_time_schedule_data));
		refresh();
	}

	@Override
	public void onListItemClicked(int resId, int position) {
        int timerType = MainPreference.getInstance(getActivity()).getMusicTimerType();
        // 自動更新テンプレートは編集不可
        if (timerType == Consts.MUSIC_TIMER_TYPE_AUTO) {
            return;
        }

		switch (resId) {
		case R.id.timerlist_lnr_element:
			FROForm.getInstance().editedTimer = timerList.get(position);
			((IModeActivity) getActivity()).showMode(MainActivity.TIMER_EDIT);
			break;
		}
	}

	@Override
	public void onBackPress() {
		((MainActivity) getActivity()).showMode(MainActivity.SETTING);
	}
}
