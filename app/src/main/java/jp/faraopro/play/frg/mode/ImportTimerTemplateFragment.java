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
import jp.faraopro.play.view.CustomListItem;

public class ImportTimerTemplateFragment extends ListBaseFragment {
	/** const **/
	public static final int TIMER_TYPE_NORMAL = 0;
	public static final int TIMER_TYPE_AUTO = 1;

	/** bundle key **/
	public static final String TAG_TIMER_TYPE = "TIMER_TYPE";

	/** view **/

	/** member **/
	private int mType;
	private ArrayList<CustomListItem> mTemplateList;
	private CustomListItem mImportItem;

	@Override
	protected void initViews(View view) {
		super.initViews(view);

		Bundle args = getArguments();
		if (args != null) {
			mType = args.getInt(TAG_TIMER_TYPE, TIMER_TYPE_NORMAL);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		show();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void show() {
		if (FROForm.getInstance().templateList == null)
			return;

		if (mTemplateList != null)
			mTemplateList.clear();
		mTemplateList = new ArrayList<CustomListItem>();
		ArrayList<CustomListItem> parent = (ArrayList<CustomListItem>) FROForm.getInstance().templateList.clone();
		for (CustomListItem cli : parent) {
			if (mType == Integer.parseInt(cli.getAction()))
				mTemplateList.add(new CustomListItem(cli));
		}
		setContents(mTemplateList);
		setClickEvent(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				mImportItem = mTemplateList.get(arg2);
				((BaseActivity) getActivity()).showNegativeDialog(getString(R.string.msg_time_schedule_import_confirm),
						MainActivity.DIALOG_TAG_IMPORT_TIMER_POS, MainActivity.DIALOG_TAG_IMPORT_TIMER_NEG, true);
			}
		});

		if (mTemplateList.size() < 1)
			((BaseActivity) getActivity()).showToast(getString(R.string.msg_nodata));
		refresh();
	}

	public CustomListItem getImportItem() {
		return this.mImportItem;
	}

	public void releaseSelected() {
		this.mImportItem = null;
	}

	@Override
	public void onBackPress() {
		releaseSelected();
		((MainActivity) getActivity()).showMode(MainActivity.TIMER);
	}

	@Override
	public void onError(int when, int code) {
	}
}
