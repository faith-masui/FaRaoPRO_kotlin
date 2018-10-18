package jp.faraopro.play.frg.mode;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.domain.GuideItemHolder;
import jp.faraopro.play.model.ChannelMode;
import jp.faraopro.play.model.UserDataHelper;
import jp.faraopro.play.view.CustomListItem;
import jp.faraopro.play.view.GuidesItem;

public class GuidesFragment extends ListBaseFragment {
	private static final boolean DEBUG = true;

	@Override
	public void onStart() {
		super.onStart();
		show();
	}

	@Override
	public void show() {
		GuidesItem selectedItem = GuideItemHolder.getInstance().getSelected();
		if (selectedItem != null) {
			setContents(selectedItem.getListItem());
		}
		setClickEvent(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// クリックしてから 500ms は次のクリックイベントを受け付けない
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

				// 選択中のアイテムの子要素を取得
				CustomListItem item = null;
				if (GuideItemHolder.getInstance().getSelected() != null)
					item = GuideItemHolder.getInstance().getSelected().getChild(arg2);
				if (item != null && !UserDataHelper.hasPermission(item.getPermission())) {
					return;
				}
				// 子要素がある場合は、ノードタイプに応じて再生開始 or ページ遷移
				if (item != null && item.getNodeType() == 1) {
					((MainActivity) getActivity()).bootPlayer(ChannelMode.BUSINESS, item.getId(), null,
							item.getPermission());
				} else {
					((BaseActivity) getActivity()).showProgress();
					FROForm.getInstance().getGuidesList(arg2);
				}
			}
		});
		refresh();
	}

	@Override
	public void onBackPress() {
	}
}
