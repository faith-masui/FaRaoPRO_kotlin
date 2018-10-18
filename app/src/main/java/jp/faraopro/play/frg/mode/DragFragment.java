package jp.faraopro.play.frg.mode;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.domain.ListAdapterForDrag;
import jp.faraopro.play.domain.MainPreference;
import jp.faraopro.play.view.DragListView;

public class DragFragment extends Fragment implements IModeFragment {
	/** view **/
	private Button btnCommit;
	private DragListView listView;

	/** member **/
	private ArrayList<Integer> items;
	private ListAdapterForDrag adapter;

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

		if (transit == FragmentTransaction.TRANSIT_FRAGMENT_OPEN) {
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_top);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.no_animation);
			}
		}

		if (transit == FragmentTransaction.TRANSIT_FRAGMENT_CLOSE) {
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_to_top);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.no_animation);
			}
		}

		return super.onCreateAnimation(transit, enter, nextAnim);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_frg_dragtest, container, false);
		initViews(view);

		return view;
	}

	protected void initViews(View view) {
		Bundle args = getArguments();

		if (args != null) {
		}
		items = new ArrayList<Integer>();
		// items.add(0, "メニュー表示");
		// items.add(4, "非メニュー表示");
		adapter = new ListAdapterForDrag(getActivity(), items);
		listView = (DragListView) view.findViewById(R.id.list);
		listView.setAdapter(adapter);
		btnCommit = (Button) view.findViewById(R.id.drag_btn_commit);
		btnCommit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayList<Integer> list = adapter.getContents();
				MainPreference.getInstance(getActivity()).setMenuList(list);
				FROForm.getInstance().updateMenuList(getActivity());
				MainPreference.getInstance(getActivity()).term();
				((MainActivity) getActivity()).showMode(MainActivity.OTHER);
			}
		});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if (!hidden) {
			if (items != null)
				items.clear();
			items.addAll(FROForm.getInstance().getMenuList(getActivity()));
			if (adapter != null)
				adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateViews() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPress() {
		((MainActivity) getActivity()).showMode(MainActivity.OTHER);
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(int when, int code) {
		// TODO Auto-generated method stub

	}
}
