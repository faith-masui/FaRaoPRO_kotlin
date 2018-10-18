package jp.faraopro.play.frg.mode;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.ArrayList;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.common.Updater;
import jp.faraopro.play.domain.MainPreference;
import jp.faraopro.play.model.UserDataHelper;
import jp.faraopro.play.view.CustomListItem;

public class OtherFragment extends ListBaseFragment {
	/** member **/
	private ArrayList<CustomListItem> lists;

	@Override
	protected void initViews(View view) {
		super.initViews(view);
	}

	@Override
	public void getData() {
		if (lists != null)
			lists.clear();
		lists = new ArrayList<CustomListItem>();
		ArrayList<Integer> menus = new ArrayList<Integer>();
		menus = FROForm.getInstance().getMenuList(getActivity());
		for (int i = 3; i < menus.size(); i++) {
			CustomListItem item = new CustomListItem();
			item.setText(Consts.getMenuString(getActivity(), menus.get(i)));
			item.setId(menus.get(i));
			if (Consts.isPremiumMenu(menus.get(i))) {
				item.setNext(false);
			} else {
				item.setNext(true);
			}
			lists.add(item);
		}
		MainPreference.getInstance(getActivity()).term();
		CustomListItem setting = new CustomListItem();
		setting.setText(getString(R.string.menu_setting));
		setting.setId(Consts.TAG_MODE_SETTING);
		setting.setNext(true);
		if (Updater.getInstance().ismAvailableUpdate()) {
			setting.setUpdate(true);
		} else {
			setting.setUpdate(false);
		}
		lists.add(setting);
		show();
	}

	@Override
	public void show() {
		setContents(lists);
		setClickEvent(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				int tag = lists.get(arg2).getId();
				showFragment(tag);
			}
		});
		refresh();
	}

	private void showFragment(int tag) {
		int modeInt = -1;
		switch (tag) {
		case Consts.TAG_MODE_ARTIST:
			if (!UserDataHelper.hasSearch()) {
				Toast.makeText(getActivity(), R.string.msg_serviceplan_can_not_play, Toast.LENGTH_LONG).show();
				return;
			}
			modeInt = MainActivity.ARTIST;
			break;
		case Consts.TAG_MODE_GENRE:
			modeInt = MainActivity.GENRE;
			break;
		case Consts.TAG_MODE_HISTORY:
			modeInt = MainActivity.HISTORY;
			break;
		case Consts.TAG_MODE_MYCHANNEL:
			modeInt = MainActivity.FAVORITE;
			break;
		case Consts.TAG_MODE_RELEASE:
			if (!UserDataHelper.hasSearch()) {
				Toast.makeText(getActivity(), R.string.msg_serviceplan_can_not_play, Toast.LENGTH_LONG).show();
				return;
			}
			modeInt = MainActivity.RELEASE;
			break;
		case Consts.TAG_MODE_SETTING:
			modeInt = MainActivity.SETTING;
			break;
		case Consts.TAG_MODE_SPECIAL:
			modeInt = MainActivity.SPECIAL;
			break;
		case Consts.TAG_MODE_STREAMING:
			break;
		}
		if (modeInt != -1)
			((IModeActivity) getActivity()).showMode(modeInt);
	}

	@Override
	public void onBackPress() {
		((MainActivity) getActivity()).showMode(MainActivity.TOP);
	}
}
