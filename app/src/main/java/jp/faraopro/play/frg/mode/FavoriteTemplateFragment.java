package jp.faraopro.play.frg.mode;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.domain.FROFavoriteTemplateDB;
import jp.faraopro.play.domain.FROFavoriteTemplateDBFactory;
import jp.faraopro.play.domain.TemplateChannelInfo;
import jp.faraopro.play.model.ChannelMode;
import jp.faraopro.play.view.CustomListItem;

public class FavoriteTemplateFragment extends ListBaseFragment {
	/** const **/

	/** bundle key **/

	/** view **/

	/** member **/
	// private ArrayList<CustomListItem> list;
	private ArrayList<TemplateChannelInfo> favorite;

	@Override
	protected void initViews(View view) {
		super.initViews(view);

		Bundle args = getArguments();
		if (args != null) {
		}
	}

	@Override
	public void getData() {
		FROFavoriteTemplateDB favoriteTemplateDb = FROFavoriteTemplateDBFactory.getInstance(getActivity());
		favorite = (ArrayList<TemplateChannelInfo>) favoriteTemplateDb.findByParent(FROForm.getInstance().parentId);
		show();
	}

	@Override
	public void show() {
		((BaseActivity) getActivity()).dismissProgress();
		ArrayList<CustomListItem> list = new ArrayList<CustomListItem>();
		if (favorite != null && favorite.size() > 0) {
			for (TemplateChannelInfo templateItem : favorite) {
				CustomListItem item = new CustomListItem();
				item.setId(templateItem.getId());
				item.setmName(templateItem.getChannelName());
				item.setmNameEn(templateItem.getNameEn());
				list.add(item);
			}
			setContents(list);
		}
		setContents(list);
		setClickEvent(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (!eventeBlock) {
					eventeBlock = true;
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							eventeBlock = false;
						}
					}, 500);
				} else {
					return;
				}

				TemplateChannelInfo info = favorite.get(arg2);
				if (info.getSourceType().equalsIgnoreCase(Consts.TEMPLATE_SOURCE_TYPE_FARAO)) {
					String tmp = info.getSourceUrl().substring(18);
					String[] params = tmp.split("/");
					String range = null;
					if (params.length > 2)
						range = params[2];
					((MainActivity) getActivity()).bootPlayer(ChannelMode.valueOfString(params[0]),
							Integer.parseInt(params[1]), range);
				}
			}
		});

		if (list.size() < 1)
			((BaseActivity) getActivity()).showToast(getString(R.string.msg_nodata));
		refresh();
	}

	@Override
	public void onBackPress() {
		((MainActivity) getActivity()).showMode(MainActivity.FAVORITE);
	}

	@Override
	public void onError(int when, int code) {
	}
}
