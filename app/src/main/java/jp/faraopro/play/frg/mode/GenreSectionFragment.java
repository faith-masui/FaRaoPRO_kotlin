package jp.faraopro.play.frg.mode;

import java.util.ArrayList;

import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.view.CustomListItem;

public class GenreSectionFragment extends ListBaseFragment {
	/** const **/

	/** member **/
	private ArrayList<CustomListItem> genres;

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
				return AnimationUtils.loadAnimation(getActivity(), R.anim.no_animation);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.no_animation);
			}
		}
		/*** スマホ用アニメーション ***/
		if (transit == MainActivity.ANIM_HIDE_SLIDE_CTL) {
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_center_to_left);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.slide_center_to_left);
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
		if (FROForm.getInstance().genre1List == null || FROForm.getInstance().genre1List.size() < 1) {
			((BaseActivity) getActivity()).showProgress();
			FROForm.getInstance().getGenreList();
		} else {
			show();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void show() {
		((BaseActivity) getActivity()).dismissProgress();
		if (genres != null)
			genres.clear();
		genres = (ArrayList<CustomListItem>) FROForm.getInstance().genre1List.clone();
		setContents(genres);
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

				FROForm.getInstance().genrePos = arg2;
				((IModeActivity) getActivity()).showMode(MainActivity.GENRE2);
			}
		});
		refresh();
	}

	@Override
	public void onBackPress() {
		if (FROForm.getInstance().isMenu(Consts.TAG_MODE_GENRE, getActivity())) {
			((IModeActivity) getActivity()).showMode(MainActivity.TOP);
		} else {
			((IModeActivity) getActivity()).showMode(MainActivity.OTHER);
		}
	}
}
