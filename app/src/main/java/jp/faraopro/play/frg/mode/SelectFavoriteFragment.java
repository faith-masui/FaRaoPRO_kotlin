package jp.faraopro.play.frg.mode;

import java.util.ArrayList;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.domain.ChannelInfo;
import jp.faraopro.play.domain.FROFavoriteChannelDBFactory;
import jp.faraopro.play.domain.FROFavoriteTemplateDBFactory;
import jp.faraopro.play.domain.TemplateChannelInfo;
import jp.faraopro.play.view.CustomListItem;

public class SelectFavoriteFragment extends ListBaseFragment {
	/** view **/

	/** member **/
	private ArrayList<CustomListItem> list;
	private ArrayList<ChannelInfo> favorite;

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		// 画面から離れる際に選択をクリアする
		FROForm.getInstance().selectedFavorite = null;
	}

	@Override
	public void getData() {
		if (list != null)
			list.clear();
		list = new ArrayList<CustomListItem>();
		if (FROForm.getInstance().selectedFavorite != null) {
			String parentId = Integer.toString(FROForm.getInstance().selectedFavorite.getId());
			ArrayList<TemplateChannelInfo> favoriteTemplate = (ArrayList<TemplateChannelInfo>) FROFavoriteTemplateDBFactory
					.getInstance(getActivity()).findByParent(parentId);
			favorite = new ArrayList<ChannelInfo>();
			// ジェネリクスのキャストは出来ないため、TemplateChannelInfo -> ChannelInfo
			// のキャストを行ってからリストに押し込む
			// この Fragment 内では TemplateChannelInfo の変数、関数は使わないため、以降の処理でも
			// ChannelInfo として扱う
			for (TemplateChannelInfo tci : favoriteTemplate) {
				favorite.add(tci);
			}
		} else {
			favorite = (ArrayList<ChannelInfo>) FROFavoriteChannelDBFactory.getInstance(getActivity()).findAll();
		}
		if (favorite != null && favorite.size() > 0) {
			for (int i = 0; i < favorite.size(); i++) {
				CustomListItem item = new CustomListItem();
				ChannelInfo info = favorite.get(i);
				item.setmName(info.getChannelName());
				item.setmNameEn(info.getChannelName());
				item.setDummy(true);
				list.add(item);
			}
		} else {
			((BaseActivity) getActivity()).showToast(getString(R.string.msg_nodata));
		}
		show();
	}

	@Override
	public void show() {
		setContents(list);
		setClickEvent(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ChannelInfo info = favorite.get(arg2);
				String source = null;
				FROForm.getInstance().editedTimer.setType(info.getType());
				switch (info.getType()) {
				case Consts.MUSIC_TYPE_LOCAL:
					source = info.getSource();
					break;
				case Consts.MUSIC_TYPE_SIMUL:
					source = info.getChannelId();
					break;
				case Consts.MUSIC_TYPE_NORMAL:
					source = info.getMode() + "," + info.getChannelId();
					if (!TextUtils.isEmpty(info.getRange()))
						source = source + "," + favorite.get(arg2).getRange();
					FROForm.getInstance().editedTimer.setMode(Integer.parseInt(info.getMode()));
					FROForm.getInstance().editedTimer.setChannelId(Integer.parseInt(info.getChannelId()));
					FROForm.getInstance().editedTimer.setRange(info.getRange());
					FROForm.getInstance().editedTimer.setPermission(info.getPermisson());
					break;
				}
				FROForm.getInstance().editedTimer.setResource(source);
				FROForm.getInstance().editedTimer.setResourceName(favorite.get(arg2).getChannelName());
				((IModeActivity) getActivity()).showMode(MainActivity.TIMER_EDIT);
			}
		});
		refresh();
	}

	@Override
	public void onBackPress() {
		((IModeActivity) getActivity()).showMode(MainActivity.TIMER_SELECT_SOURCE);
	}
}
