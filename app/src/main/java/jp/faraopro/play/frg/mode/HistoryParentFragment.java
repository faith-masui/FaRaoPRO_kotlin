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
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;

public class HistoryParentFragment extends Fragment implements IModeFragment {
	/** const **/
	private static final int LEFT = 2;
	private static final int RIGHT = 1;
	private static final int CENTER = 0;
	private static final int BUTTON_SIZE = LEFT + 1;
	private static final String FRAGMENT_TAG_CHANNEL_HISTORY = "CHANNEL_HISTORY";
	private static final String FRAGMENT_TAG_MUSIC_HISTORY = "MUSIC_HISTORY";

	/** bundle key **/
	public static final String LEFT_TAB_NAME = "LEFT_TAB_NAME";
	public static final String RIGHT_TAB_NAME = "RIGHT_TAB_NAME";
	public static final String CENTER_TAB_NAME = "CENTER_TAB_NAME";

	/** view **/
	private Button mButtonLeft;
	private Button mButtonRight;
	private Button mButtonCenter;
	private Button[] mButtonArrays = new Button[BUTTON_SIZE];
	// private ChannelHistoryFragment mChannelHistoryFragment;
	// private HistoryFragment mMusicHistoryFragment;

	/** member **/
	// private ArrayList<CustomListItem> historyList;
	// private ArrayList<CustomListItem> goodList;
	private int selected = LEFT;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// マップクラスの初期化
		View view = inflater.inflate(R.layout.layout_simple_2block, container, false);
		initViews(view);

		return view;
	}

	protected void initViews(View view) {
		String left = null;
		String right = null;
		String center = null;
		Bundle args = getArguments();
		if (args != null) {

			left = args.getString(LEFT_TAB_NAME);
			right = args.getString(RIGHT_TAB_NAME);
			center = args.getString(CENTER_TAB_NAME);
		}
		if (left == null)
			left = "LEFT";
		if (right == null)
			right = "RIGHT";
		if (center == null)
			center = "CENTER";

		LinearLayout header = (LinearLayout) view.findViewById(R.id.simple2_lnr_header);
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//View tab = inflater.inflate(R.layout.new_res_3tab, null);
		View tab = inflater.inflate(R.layout.new_res_2tab, null);
		tab.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		header.addView(tab);

		mButtonLeft = (Button) tab.findViewById(R.id.tab_btn_left);
		mButtonLeft.setText(left);
		mButtonLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selected != LEFT) {
					setSelected(LEFT);
					show();
				}
			}
		});
		mButtonArrays[LEFT] = mButtonLeft;

		mButtonRight = (Button) tab.findViewById(R.id.tab_btn_right);
		mButtonRight.setText(right);
		mButtonRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selected != RIGHT) {
					setSelected(RIGHT);
					show();
				}
			}
		});
		mButtonArrays[RIGHT] = mButtonRight;
		//mButtonCenter = (Button) tab.findViewById(R.id.tab_btn_center);
		//mButtonCenter.setText(center);
		//mButtonCenter.setOnClickListener(new View.OnClickListener() {
		//	@Override
		//	public void onClick(View v) {
		//		if (selected != CENTER) {
		//			setSelected(CENTER);
		//			show();
		//		}
		//	}
		//});
		//mButtonArrays[CENTER] = mButtonCenter;

		setSelected(LEFT);
	}

	private void setSelected(int type) {
		//for (int i = 0; i < BUTTON_SIZE; i++) {
		for (int i = 1; i < BUTTON_SIZE; i++) {
			// 選択されたボタン
			if (i == type) {
				mButtonArrays[i].setSelected(true);
				selected = i;
			}
			// 選択されなかったボタン
			else {
				mButtonArrays[i].setSelected(false);
			}
		}
	}

	@Override
	public void getData() {
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden)
			show();
	}

	@Override
	public void show() {
		((BaseActivity) getActivity()).dismissProgress();
		Fragment frg;
		String tag;
		FragmentManager manager = getChildFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		// チャンネル履歴
		if (selected == LEFT) {
			frg = manager.findFragmentByTag(FRAGMENT_TAG_CHANNEL_HISTORY);
			if (frg == null) {
				frg = new ChannelHistoryFragment();
				Bundle args = new Bundle();
				args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_NORMAL);
				frg.setArguments(args);
			} else {
				((IModeFragment) frg).getData();
			}
			tag = FRAGMENT_TAG_CHANNEL_HISTORY;
			if (isAdded()) {
				((MainActivity) getActivity()).setHeaderLeftButton(null);
			}
			if (isAdded()) {
				((MainActivity) getActivity()).setHeaderTitle(getString(R.string.page_title_goodlist_pro));
			}
		}
		// 再生履歴 or グッドリスト
		else {
			frg = manager.findFragmentByTag(FRAGMENT_TAG_MUSIC_HISTORY);
			if (frg == null) {
				frg = new HistoryFragment();
				Bundle args = new Bundle();
				args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_HISTORY);
				args.putInt(HistoryFragment.BUNDLE_KEY_SELECT, selected);
				frg.setArguments(args);
			} else {
				((HistoryFragment) frg).change(selected);
			}
			tag = FRAGMENT_TAG_MUSIC_HISTORY;
			if (isAdded()) {
				((MainActivity) getActivity()).setHeaderLeftButton(null);
			}
			if (isAdded()) {
				((MainActivity) getActivity()).setHeaderTitle(getString(R.string.page_title_goodlist_pro));
			}
		}
		transaction.replace(R.id.simple2_lnr_body, frg, tag);
		transaction.commitAllowingStateLoss();
	}

	@Override
	public void onBackPress() {
		if (FROForm.getInstance().isMenu(Consts.TAG_MODE_HISTORY, getActivity())) {
			((MainActivity) getActivity()).showMode(MainActivity.TOP);
		} else {
			((MainActivity) getActivity()).showMode(MainActivity.OTHER);
		}
	}

	@Override
	public void updateViews() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(int when, int code) {
		// TODO Auto-generated method stub

	}

}
