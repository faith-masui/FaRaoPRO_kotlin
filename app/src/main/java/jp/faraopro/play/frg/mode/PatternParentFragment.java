package jp.faraopro.play.frg.mode;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Switch;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.domain.FROPatternScheduleDBFactory;
import jp.faraopro.play.domain.MainPreference;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCScheduleItem;
import jp.faraopro.play.util.Utils;

public class PatternParentFragment extends Fragment implements IModeFragment {
	private static final String FRAGMENT_TAG_TODAY = "TODAY";
	private static final String FRAGMENT_TAG_TOMORROW = "TOMORROW";
	private Button tabToday;
	private Button tabTomorrow;
	private Switch switchEnableInterrupt;

	public static PatternParentFragment newInstance() {
		return new PatternParentFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.layout_simple_3block, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// create 2tab on header
		LinearLayout header = (LinearLayout) view.findViewById(R.id.simple3_lnr_top);
		View.inflate(getActivity(), R.layout.new_res_2tab, header);
		header.findViewById(R.id.linear_tab_holder).setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		// today's tab
		tabToday = (Button) header.findViewById(R.id.tab_btn_left);
		tabToday.setText(R.string.cap_interrupt_today);
		tabToday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeTab(true);
			}
		});
		// tomorrow's tab
		tabTomorrow = (Button) header.findViewById(R.id.tab_btn_right);
		tabTomorrow.setText(R.string.cap_interrupt_next_day);
		tabTomorrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeTab(false);
			}
		});
		// create toggle switch on fotter
		LinearLayout footer = (LinearLayout) view.findViewById(R.id.simple3_lnr_bottom);
		View.inflate(getActivity(), R.layout.new_res_toggle_switch, footer);
		switchEnableInterrupt = (Switch) footer.findViewById(R.id.toggle_swt_selected);
		switchEnableInterrupt.setText(R.string.btn_enable_interrupt_play);
		switchEnableInterrupt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				MainPreference.getInstance(getActivity()).setInterruptTimerEnable(isChecked);
				// Worker 側に通知いらない？
			}
		});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			getData();
			changeTab(true);
		}
	}

	/**
	 * タブを切り替える
	 *
	 * @param isToday
	 *            true:当日, false:翌日
	 */
	private void changeTab(boolean isToday) {
		tabToday.setSelected(isToday);
		tabTomorrow.setSelected(!isToday);
		changeFragment(isToday);
	}

	/**
	 * 表示中のフラグメントを切り替える
	 *
	 * @param isToday
	 *            true:当日, false:翌日
	 */
	private void changeFragment(boolean isToday) {
		FragmentManager manager = getChildFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		Fragment fragment;
		int param;
		String tag;
		if (isToday) {
			param = PatternDetailFragment.BUNDLE_PARAM_TODAY;
			tag = FRAGMENT_TAG_TODAY;
		} else {
			param = PatternDetailFragment.BUNDLE_PARAM_TOMORROW;
			tag = FRAGMENT_TAG_TOMORROW;
		}
		fragment = manager.findFragmentByTag(tag);
		if (fragment == null) {
			fragment = PatternDetailFragment.newInstance(param);
		}
		transaction.replace(R.id.simple3_lnr_center, fragment, tag);
		transaction.commitAllowingStateLoss();
	}

	public void forceUpdate() {
		((BaseActivity) getActivity()).showNegativeDialog(R.string.msg_interrupt_reload,
				MainActivity.DIALOG_TAG_PATTERN_FORCE_UPDATE, BaseActivity.DIALOG_TAG_MESSAGE, true);
	}

	@Override
	public void getData() {
		if (switchEnableInterrupt == null)
			return;

		MCScheduleItem schedule = FROPatternScheduleDBFactory.getInstance(getActivity())
				.findByDate(Utils.getNowDateString("yyyyMMdd"));
		String scheduleMask = (schedule != null) ? schedule.getString(MCDefResult.MCR_KIND_SCHEDULE_MASK) : "0";
		switchEnableInterrupt.setChecked(MainPreference.getInstance(getActivity()).getInterruptTimerEnable());
		// scheduleMask の値が1(禁止)の場合、スイッチを disable 状態にしておく
		switchEnableInterrupt.setEnabled(!"1".equals(scheduleMask));
	}

	@Override
	public void show() {
		FragmentManager manager = getChildFragmentManager();
		PatternDetailFragment today = (PatternDetailFragment) manager.findFragmentByTag(FRAGMENT_TAG_TODAY);
		if (today != null) {
			today.forceUpdate();
		}
		getData();
		changeTab(true);
	}

	@Override
	public void updateViews() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onBackPress() {
		((MainActivity) getActivity()).showMode(MainActivity.SETTING);
	}

	@Override
	public void onError(int when, int code) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
