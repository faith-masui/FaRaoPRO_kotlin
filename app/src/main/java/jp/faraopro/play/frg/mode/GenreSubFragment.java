package jp.faraopro.play.frg.mode;

import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.model.ChannelMode;
import jp.faraopro.play.view.CustomListItem;

public class GenreSubFragment extends ListBaseFragment {
	/** member **/
	private ArrayList<CustomListItem> genres;
	private ArrayList<CustomListItem> showItems;
	private int position = -1;

	// private boolean eventeBlock = false;

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

		if (transit == FragmentTransaction.TRANSIT_FRAGMENT_OPEN) {
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_center);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.no_animation);
			}
		}

		if (transit == FragmentTransaction.TRANSIT_FRAGMENT_CLOSE) {
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_center_to_left);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.no_animation);
			}
		}
		if (transit == 715) {
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_center);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_center_to_left);
			}
		}
		if (transit == 716) {
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_center);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_center_to_left);
			}
		}
		/*** スマホ用アニメーション ***/
		if (transit == MainActivity.ANIM_HIDE_SLIDE_CTR) {
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_center_to_right);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_center_to_right);
			}
		}
		if (transit == MainActivity.ANIM_SHOW_SLIDE_RTC) {
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right_to_center);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right_to_center);
			}
		}

		return super.onCreateAnimation(transit, enter, nextAnim);
	}

	@Override
	public void getData() {
		position = FROForm.getInstance().genreSubPos;
		if (position != -1) {
			show();
		} else {
			((IModeActivity) getActivity()).showMode(MainActivity.GENRE);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void show() {
		if (genres != null)
			genres.clear();
		genres = (ArrayList<CustomListItem>) FROForm.getInstance().genre3List.clone();
		showItems = createSubGenres();
		setContents(showItems);
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
				((MainActivity) getActivity()).bootPlayer(ChannelMode.GENRE, showItems.get(arg2).getId(), null);
			}
		});
		refresh();
	}

	private ArrayList<CustomListItem> createSubGenres() {
		ArrayList<CustomListItem> lists = new ArrayList<CustomListItem>();
		// int startId = FROForm.getInstance().fgenre2List.get(position).getId();

		/** 暫定対応 **/
		int startId = GenreFragment.id;
		/** 暫定対応 **/

		CustomListItem all = new CustomListItem();
		all.setmName(getString(R.string.cap_play_all));
		all.setmNameEn(getString(R.string.cap_play_all));
		all.setId(startId);
		lists.add(all);

		// logD("[FRO] ------------- CREATE SUBSUB -------------");
		// logD("[FRO] pos = " + secondPos + ", start = " + startId + ", end = "
		// + (startId + GENRE2_INTERVAL));
		for (int i = 0; i < genres.size(); i++) {
			CustomListItem item = genres.get(i);
			if ((startId <= item.getId()) && (item.getId() < (startId + FROForm.GENRE2_INTERVAL))) {
				lists.add(item);
			}
		}

		return lists;
	}

	@Override
	public void onBackPress() {
		((IModeActivity) getActivity()).showMode(MainActivity.GENRE2);
	}
}
