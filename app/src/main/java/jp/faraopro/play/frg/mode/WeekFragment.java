package jp.faraopro.play.frg.mode;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.view.CustomListItem;

public class WeekFragment extends ListBaseFragment {
	/** view **/
	private Button btnSet;

	/** member **/
	private ArrayList<CustomListItem> weekList;
	private int[] contents = { R.string.msg_weekday_sunday, R.string.msg_weekday_monday, R.string.msg_weekday_tuesday,
			R.string.msg_weekday_wednesday, R.string.msg_weekday_thursday, R.string.msg_weekday_friday,
			R.string.msg_weekday_saturday };

	@Override
	protected void initViews(View view) {
		super.initViews(view);

		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.new_res_1button, null);
		v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setOptionalView(1, v);
		btnSet = (Button) v.findViewById(R.id.onebutton_btn_plane);
		btnSet.setText(R.string.btn_set);
		btnSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ArrayList<Integer> selected = new ArrayList<Integer>();
				for (int i = 0; i < weekList.size(); i++) {
					CustomListItem item = weekList.get(i);
					if (item.isNext()) {
						selected.add(new Integer(i + 1));
					}
				}
				if (selected.size() > 0) {
					FROForm.getInstance().editedTimer.setWeekList(selected);
					FROForm.getInstance().editedTimer.setWeek(selected);
				}
				onBackPress();
			}
		});
	}

	@Override
	public void getData() {
		if (weekList != null)
			weekList.clear();
		weekList = new ArrayList<CustomListItem>();
		ArrayList<Integer> selected = FROForm.getInstance().editedTimer.getWeekList();
		for (int i = 0; i < contents.length; i++) {
			CustomListItem item = new CustomListItem();
			item.setmName(getString(contents[i]));
			item.setmNameEn(getString(contents[i]));
			if (selected != null && selected.size() > 0 && selected.contains(new Integer(i + 1))) {
				item.setNext(true);
			} else {
				item.setNext(false);
			}
			weekList.add(item);
		}
		show();
	}

	@Override
	public void show() {
		setContents(weekList);
		setClickEvent(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				weekList.get(arg2).setNext(!weekList.get(arg2).isNext());
				refresh();
			}
		});
		refresh();
	}

	@Override
	public void onBackPress() {
		((IModeActivity) getActivity()).showMode(MainActivity.TIMER_EDIT);
	}
}
