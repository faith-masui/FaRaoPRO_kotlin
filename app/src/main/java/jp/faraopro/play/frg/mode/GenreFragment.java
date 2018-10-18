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

public class GenreFragment extends ListBaseFragment {
	/** member **/
	private ArrayList<CustomListItem> genres;
	private ArrayList<CustomListItem> showItems;
	private int position = -1;
	public static int id = -1;

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
		if (transit == 714) {
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_center);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_center_to_left);
			}
		}
		if (transit == 715) {
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.no_animation);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.no_animation);
			}
		}
		if (transit == 717) {
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
		if (transit == MainActivity.ANIM_HIDE_SLIDE_CTL) {
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_center_to_left);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_center_to_left);
			}
		}
		if (transit == MainActivity.ANIM_SHOW_SLIDE_RTC) {
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right_to_center);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right_to_center);
			}
		}
		if (transit == MainActivity.ANIM_SHOW_SLIDE_LTC) {
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_center);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_center);
			}
		}

		return super.onCreateAnimation(transit, enter, nextAnim);
	}

	@Override
	public void getData() {
		position = FROForm.getInstance().genrePos;
		if (position != -1)
			show();
		else
			((IModeActivity) getActivity()).showMode(MainActivity.GENRE);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void show() {
		if (genres != null)
			genres.clear();
		genres = (ArrayList<CustomListItem>) FROForm.getInstance().genre2List.clone();
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

				// 再生
				if (arg2 == 0) {
					((MainActivity) getActivity()).bootPlayer(ChannelMode.GENRE, showItems.get(arg2).getId(), null);
				}
				// 画面遷移
				else {
					FROForm.getInstance().genreSubPos = arg2 - 1;

					/** 暫定対応 **/
					CustomListItem item = showItems.get(arg2);
					id = item.getId();
					/** 暫定対応 **/

					((IModeActivity) getActivity()).showMode(MainActivity.GENRE3);
				}
			}
		});
		refresh();
	}

	private ArrayList<CustomListItem> createSubGenres() {
		ArrayList<CustomListItem> lists = new ArrayList<CustomListItem>();
		int startId = FROForm.getInstance().genre1List.get(position).getId();
		CustomListItem all = new CustomListItem();
		all.setmName(getString(R.string.cap_play_all));
		all.setmNameEn(getString(R.string.cap_play_all));
		all.setId(startId);
		lists.add(all);

		// logD("[FRO] ------------- CREATE SUB -------------");
		// logD("[FRO] pos = " + firstPos + ", start = " + startId + ", end = "
		// + (startId + GENRE1_INTERVAL));
		for (int i = 0; i < genres.size(); i++) {
			CustomListItem item = genres.get(i);
			if ((startId <= item.getId()) && (item.getId() < (startId + FROForm.GENRE1_INTERVAL))) {
				item.setNext(true);
				lists.add(item);
			}
		}

		return lists;
	}

	@Override
	public void onBackPress() {
		((IModeActivity) getActivity()).showMode(MainActivity.GENRE);
	}
}
