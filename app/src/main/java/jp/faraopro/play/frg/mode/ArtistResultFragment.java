package jp.faraopro.play.frg.mode;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.model.ChannelMode;
import jp.faraopro.play.view.CustomListItem;

public class ArtistResultFragment extends ListBaseFragment {
	/** const **/

	/** bundle key **/

	/** view **/

	/** member **/
	private ArrayList<CustomListItem> lists;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void getData() {

	}

	@Override
	@SuppressWarnings("unchecked")
	public void show() {
		((BaseActivity) getActivity()).dismissProgress();
		if (lists != null)
			lists.clear();
		lists = (ArrayList<CustomListItem>) FROForm.getInstance().artistList.clone();
		setContents(lists);
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
				((MainActivity) getActivity()).bootPlayer(ChannelMode.ARTIST, lists.get(arg2).getId(), null);
			}
		});
		refresh();
	}

	@Override
	public void onBackPress() {
		((IModeActivity) getActivity()).showMode(MainActivity.ARTIST);
	}
}
