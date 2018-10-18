package jp.faraopro.play.frg.mode;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.app.TimerHelper;
import jp.faraopro.play.domain.FROTimerDB;
import jp.faraopro.play.domain.FROTimerDBFactory;
import jp.faraopro.play.model.TimerInfo;
import jp.faraopro.play.view.CustomListItem;

public class TimerDeleteFragment extends ListBaseFragment {
	/** const **/

	/** bundle key **/

	/** view **/
	private Button btnEdit;
	private Button btnImport;
	// private TextView txtIntro;
	// private TextView txtNodata;
	private TextView mTextNodata;

	/** member **/
	// private ArrayList<CustomListItem> list;
	private ArrayList<TimerInfo> timerList;

	@Override
	protected void initViews(View view) {
		super.initViews(view);

		Bundle args = getArguments();
		if (args != null) {
		}

		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.new_res_2button, null);
		v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setOptionalView(1, v);
		btnEdit = (Button) v.findViewById(R.id.button_btn_first);
		btnEdit.setText(R.string.page_title_setting_time_shedule_new);
		btnEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FROTimerDB db = FROTimerDBFactory.getInstance(getActivity());
				if (db.getSize() < 99) {
					((IModeActivity) getActivity()).showMode(MainActivity.TIMER_EDIT);
				} else {
					((BaseActivity) getActivity())
							.showToast(getString(R.string.msg_time_schedule_regist_error_maxsize));
				}
			}
		});

		btnImport = (Button) v.findViewById(R.id.button_btn_second);
		btnImport.setText(R.string.btn_import_template);
		btnImport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((IModeActivity) getActivity()).showMode(MainActivity.IMPORT_TIMER_TEMPLATE);
			}
		});

		View v2 = inflater.inflate(R.layout.new_res_plane_text, null);
		v2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setOptionalView(0, v2);
		mTextNodata = (TextView) v2.findViewById(R.id.planetext_txt_text);
		mTextNodata.setText(R.string.msg_toast_no_time_schedule_data);

		// txtNodata = (TextView) v.findViewById(R.id.mychannel_txt_nodata);
	}

	@Override
	public void getData() {
		timerList = (ArrayList<TimerInfo>) FROTimerDBFactory.getInstance(getActivity()).findAll();
        if (timerList != null)
            Collections.sort(timerList, TimerInfo.COMPARATOR_BY_TIME_AND_WEEK);
		show();
	}

	@Override
	public void show() {
		btnEdit.setVisibility(View.VISIBLE);
		((BaseActivity) getActivity()).dismissProgress();
		ArrayList<CustomListItem> list = new ArrayList<CustomListItem>();
		if (timerList != null && timerList.size() > 0) {
			for (TimerInfo ti : timerList) {
				CustomListItem item = new CustomListItem(ti);
				list.add(item);
			}
			setContents(list);
			setClickEvent(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

					FROForm.getInstance().editedTimer = timerList.get(arg2);
					// FROForm.getInstance().selectedWeek = timerList.get(arg2)
					// .getWeekList();
					((IModeActivity) getActivity()).showMode(MainActivity.TIMER_EDIT);
				}
			});
		} else {
			setContents(list);
			setClickEvent(null);
		}
		if (list.size() < 1) {
			mTextNodata.setVisibility(View.VISIBLE);
		} else {
			mTextNodata.setVisibility(View.GONE);
		}
		refresh();
	}

	@Override
	public void onListItemClicked(int resId, int position) {
		switch (resId) {
		case R.id.deletelist_timer_ibtn_delete:
			mSelectedItem = position;
			((BaseActivity) getActivity()).showNegativeDialog(R.string.msg_delete_timer,
					MainActivity.DIALOG_TAG_DELETE_TIMER_POS, MainActivity.DIALOG_TAG_DELETE_TIMER_NEG, true);
			break;
		}
	}

	private int mSelectedItem = -1;

	public void deleteTimer() {
		if (mSelectedItem < 0)
			return;

		FROTimerDBFactory.getInstance(getActivity()).delete(Integer.toString(timerList.get(mSelectedItem).getId()));
		TimerHelper.setMusicTimer(getActivity());
		removeContent(mSelectedItem);
		timerList.remove(mSelectedItem);
		((BaseActivity) getActivity()).getUiHandler().post(new Runnable() {
			@Override
			public void run() {
				refresh();
			}
		});
		mSelectedItem = -1;
	}

	@Override
	public void onBackPress() {
		((MainActivity) getActivity()).showMode(MainActivity.TIMER);
	}
}
