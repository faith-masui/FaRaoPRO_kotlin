package jp.faraopro.play.frg.mode;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.view.CustomListItem;

public class ImportTemplateFragment extends ListBaseFragment {
	/** const **/

	/** bundle key **/

	/** view **/

	/** member **/
	private ArrayList<CustomListItem> template;
	private CustomListItem importItem;

	@Override
	protected void initViews(View view) {
		super.initViews(view);

		Bundle args = getArguments();
		if (args != null) {
		}
	}

	@Override
	public void getData() {
		((BaseActivity) getActivity()).showProgress();
		FROForm.getInstance().getTemplateList(Consts.TEMPLATE_TYPE_BOOKMARK);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void show() {
		((BaseActivity) getActivity()).dismissProgress();
		template = (ArrayList<CustomListItem>) FROForm.getInstance().templateList.clone();
		ArrayList<CustomListItem> list = new ArrayList<CustomListItem>();
		if (template != null && template.size() > 0) {
			for (CustomListItem cli : template) {
				CustomListItem item = new CustomListItem(cli);
				// item.isImport = true;
				list.add(item);
			}
			setContents(list);
		}
		setContents(list);
		setClickEvent(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				importItem = template.get(arg2);
				((BaseActivity) getActivity()).showNegativeDialog(getString(R.string.msg_favorite_import_confirm),
						MainActivity.DIALOG_TAG_IMPORT_FAVORITE_POS, MainActivity.DIALOG_TAG_IMPORT_FAVORITE_NEG,
						R.string.btn_favorite_regist, -1, true);
			}
		});

		if (list.size() < 1)
			((BaseActivity) getActivity()).showToast(getString(R.string.msg_nodata));
		refresh();
	}

	public CustomListItem getImportItem() {
		return this.importItem;
	}

	public void releaseSelected() {
		this.importItem = null;
	}

	@Override
	public void onBackPress() {
		((MainActivity) getActivity()).showMode(MainActivity.FAVORITE);
	}

	@Override
	public void onError(int when, int code) {
	}
}
