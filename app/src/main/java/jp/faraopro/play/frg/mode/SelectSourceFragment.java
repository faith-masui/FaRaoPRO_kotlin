package jp.faraopro.play.frg.mode;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.domain.FROTemplateFavoriteListDB;
import jp.faraopro.play.domain.FROTemplateFavoriteListDBFactory;
import jp.faraopro.play.mclient.MCTemplateItem;
import jp.faraopro.play.view.CustomListItem;

public class SelectSourceFragment extends ListBaseFragment {
	/** view **/

	/** member **/
	private ArrayList<CustomListItem> list;

	@Override
	public void getData() {
		if (list != null)
			list.clear();
		list = new ArrayList<CustomListItem>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// �@�擪�̃A�C�e��(���C�ɓ�肩��I��)
				CustomListItem head = new CustomListItem();
				head.setmName(getString(R.string.page_title_setting_select_favorite));
				head.setmNameEn(getString(R.string.page_title_setting_select_favorite));
				head.setNext(true);
				list.add(head);

				FROTemplateFavoriteListDB templateListDb = FROTemplateFavoriteListDBFactory.getInstance(getActivity());
				ArrayList<MCTemplateItem> templateList = (ArrayList<MCTemplateItem>) templateListDb.findAll();
				if (templateList != null && templateList.size() > 0) {
					for (MCTemplateItem ti : templateList) {
						CustomListItem item = new CustomListItem(ti);
						item.setNext(true);
						list.add(item);
					}
				}

				// �@�����̃A�C�e��(���[�J������I��)
				//CustomListItem foot = new CustomListItem();
				//foot.setmName(getString(R.string.page_title_setting_select_ipod));
				//foot.setmNameEn(getString(R.string.page_title_setting_select_ipod));
				//foot.setNext(true);
				//list.add(foot);

				new Handler(Looper.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						show();
					}
				});
			}
		}).start();
	}

	@Override
	public void show() {
		setContents(list);
		setClickEvent(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// �擪�̃A�C�e��
				if (arg2 == 0) {
					FROForm.getInstance().selectedFavorite = null;
					((IModeActivity) getActivity()).showMode(MainActivity.TIMER_SELECT_FAVORITE);
				}
				// �����̃A�C�e��
				//else if (arg2 == list.size() - 1) {
				//	((IModeActivity) getActivity()).showMode(MainActivity.TIMER_SELECT_LOCAL);
				//}
				// ����ȊO�̃A�C�e��
				else {
					FROForm.getInstance().selectedFavorite = list.get(arg2);
					((IModeActivity) getActivity()).showMode(MainActivity.TIMER_SELECT_FAVORITE);
				}
			}
		});
		refresh();
	}

	@Override
	public void onBackPress() {
		((IModeActivity) getActivity()).showMode(MainActivity.TIMER_EDIT);
	}
}
