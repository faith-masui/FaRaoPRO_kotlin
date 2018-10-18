package jp.faraopro.play.frg.mode;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.view.CustomListItem;

public class TimerImportFragment extends Fragment implements IModeFragment {
	/** const **/
	private static final boolean DEBUG = true;
	private static final int TYPE_NOMAL = 0;
	private static final int TYPE_AUTO = 1;

	/** bundle key **/

	/** view **/
	private LinearLayout lnrHeader;
	private Button mButtonLeft;
	private Button mButtonRight;
	private ImportTimerTemplateFragment mNomalTimerFrag;
	private ImportTimerTemplateFragment mAutoTimerFrag;

	/** member **/
	private int type = TYPE_NOMAL;

	// private HashMap<Integer, GuidesFragment> fragmentMap; // 番組チャンネル格納用マップ

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_simple_2block, container, false);
		initViews(view);

		return view;
	}

	protected void initViews(View view) {
		// タブを追加する
		lnrHeader = (LinearLayout) view.findViewById(R.id.simple2_lnr_body);
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.new_res_2tab, null);
		v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		lnrHeader.addView(v);
		// 通常タイマー
		mButtonLeft = (Button) v.findViewById(R.id.tab_btn_left);
		mButtonLeft.setText(R.string.btn_normal_timer);
		mButtonLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mButtonLeft.setSelected(true);
				mButtonRight.setSelected(false);
				changeFragment(TYPE_NOMAL, false);
			}
		});
		// 自動更新タイマー
		mButtonRight = (Button) v.findViewById(R.id.tab_btn_right);
		mButtonRight.setText(R.string.btn_automatic_timer);
		mButtonRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mButtonLeft.setSelected(false);
				mButtonRight.setSelected(true);
				changeFragment(TYPE_AUTO, false);
			}
		});
	}

	private void changeFragment(int type, boolean isBackStack) {
		// 現在表示中のタブと一致していた場合は何もしない
		if (type == this.type)
			return;

		this.type = type;
		FragmentManager manager = getChildFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		if (type == TYPE_NOMAL) {
			if (mNomalTimerFrag == null) {
				mNomalTimerFrag = new ImportTimerTemplateFragment();
				Bundle args = new Bundle();
				args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_NORMAL);
				args.putInt(ImportTimerTemplateFragment.TAG_TIMER_TYPE, ImportTimerTemplateFragment.TIMER_TYPE_NORMAL);
				mNomalTimerFrag.setArguments(args);
			}
			// if (isAdded()) {
			// ((MainActivity) getActivity()).setHeaderLeftButton(null);
			// }
			transaction.replace(R.id.simple2_lnr_body, mNomalTimerFrag);
		} else {
			if (mAutoTimerFrag == null) {
				mAutoTimerFrag = new ImportTimerTemplateFragment();
				Bundle args = new Bundle();
				args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_NORMAL);
				args.putInt(ImportTimerTemplateFragment.TAG_TIMER_TYPE, ImportTimerTemplateFragment.TIMER_TYPE_AUTO);
				mAutoTimerFrag.setArguments(args);
			}
			// if (isAdded()) {
			// ((MainActivity) getActivity()).setHeaderLeftButton(null);
			// }
			transaction.replace(R.id.simple2_lnr_body, mAutoTimerFrag);
		}
		if (isBackStack)
			transaction.addToBackStack(null);
		transaction.commitAllowingStateLoss();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			// データを全て解放し終えたら番組チャンネルリストを取得する
			((BaseActivity) getActivity()).showProgress();
			mButtonLeft.setSelected(true);
			mButtonRight.setSelected(false);
			type = TYPE_NOMAL;
			FROForm.getInstance().getTemplateList(Consts.TEMPLATE_TYPE_TIMETABLE);
		}
	}

	@Override
	public void show() {
		((BaseActivity) getActivity()).dismissProgress();
		Fragment frg;
		FragmentManager manager = getChildFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		// 番組チャンネルを表示
		if (type == TYPE_NOMAL) {
			if (mNomalTimerFrag == null) {
				mNomalTimerFrag = new ImportTimerTemplateFragment();
				Bundle args = new Bundle();
				args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_NORMAL);
				args.putInt(ImportTimerTemplateFragment.TAG_TIMER_TYPE, ImportTimerTemplateFragment.TIMER_TYPE_NORMAL);
				mNomalTimerFrag.setArguments(args);
			}
			transaction.replace(R.id.simple2_lnr_body, mNomalTimerFrag);
			frg = mNomalTimerFrag;
		}
		// マイ特集を表示
		else {
			if (mAutoTimerFrag == null) {
				mAutoTimerFrag = new ImportTimerTemplateFragment();
				Bundle args = new Bundle();
				args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_NORMAL);
				args.putInt(ImportTimerTemplateFragment.TAG_TIMER_TYPE, ImportTimerTemplateFragment.TIMER_TYPE_AUTO);
				mAutoTimerFrag.setArguments(args);
			}
			transaction.replace(R.id.simple2_lnr_body, mAutoTimerFrag);
			frg = mAutoTimerFrag;
		}
		transaction.replace(R.id.simple2_lnr_body, frg);
		transaction.commitAllowingStateLoss();
	}

	public CustomListItem getImportItem() {
		if (type == TYPE_NOMAL) {
			return mNomalTimerFrag.getImportItem();
		} else {
			return mAutoTimerFrag.getImportItem();
		}
	}

	public void releaseSelected() {
		if (type == TYPE_NOMAL) {
			mNomalTimerFrag.releaseSelected();
		} else {
			mAutoTimerFrag.releaseSelected();
		}
	}

	@Override
	public void updateViews() {
	}

	@Override
	public void onBackPress() {
		if (type == TYPE_NOMAL) {
			mNomalTimerFrag.onBackPress();
		} else {
			mAutoTimerFrag.onBackPress();
		}
	}

	@Override
	public void getData() {
	}

	@Override
	public void onError(int when, int code) {
	}
}
