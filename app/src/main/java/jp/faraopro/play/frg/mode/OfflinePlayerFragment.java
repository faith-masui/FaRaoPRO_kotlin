package jp.faraopro.play.frg.mode;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.domain.ChannelInfo;
import jp.faraopro.play.util.SimpleTimer2;

/**
 * タイトルバーを構成するフラグメント<br>
 * 
 * @author Aim
 */
public class OfflinePlayerFragment extends Fragment implements IModeFragment, SimpleTimer2.TimerListener {
	/** const **/
	private static final int TIMER_TYPE_PROGRESS = 0;

	/** bundle key **/

	/** view **/
	private Button btnFavorite;
	private Button btnPrevious;
	private Button btnPause;
	private Button btnNext;
	private TextView txtFirstLine;
	private TextView txtSecondLine;
	private ProgressBar prgDuration;

	/** member **/
	private ChannelInfo favorite;
	private SimpleTimer2 progressTimer;

	public static OfflinePlayerFragment newInstance() {
		OfflinePlayerFragment instance = new OfflinePlayerFragment();

		return instance;
	}

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

		/*** スマホ用アニメーション ***/
		if (transit == MainActivity.ANIM_HIDE_FLICK) {
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_flip_horizontal_exit);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_flip_horizontal_exit);
			}
		}
		if (transit == MainActivity.ANIM_SHOW_FLICK) {
			if (enter) {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_flip_horizontal_enter);
			} else {
				return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_flip_horizontal_enter);
			}
		}

		return super.onCreateAnimation(transit, enter, nextAnim);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_frg_local_player, container, false);
		initViews(view);

		return view;
	}

	// Viewの初期化(参照取得、イベント設定など)
	@SuppressLint("SetJavaScriptEnabled")
	private void initViews(View view) {
		Bundle args = getArguments();
		if (args != null) {
		}

		btnFavorite = (Button) view.findViewById(R.id.local_player_btn_favorite);
		btnFavorite.setVisibility(View.GONE);

		btnPrevious = (Button) view.findViewById(R.id.local_player_btn_previous);
		btnPrevious.setVisibility(View.GONE);

		btnNext = (Button) view.findViewById(R.id.local_player_btn_next);
		btnNext.setVisibility(View.GONE);

		btnPause = (Button) view.findViewById(R.id.local_player_btn_pause);
		btnPause.setVisibility(View.GONE);

		txtFirstLine = (TextView) view.findViewById(R.id.local_player_txt_first_line);
		txtFirstLine.setVisibility(View.GONE);
		txtSecondLine = (TextView) view.findViewById(R.id.local_player_txt_second_line);
		prgDuration = (ProgressBar) view.findViewById(R.id.player_prg_duration);
		prgDuration.setVisibility(View.GONE);
	}

	@Override
	public void updateViews() {
		// File file = new
		// File(FROForm.getInstance().getNowPlaying().getUrlSearch());
		// txtFirstLine.setText("unknown");
		txtSecondLine.setText(R.string.msg_offline_player);
		txtSecondLine.requestFocus();
		// file = new File(FROForm.getInstance().getNowPlaying().getUrl());
		((MainActivity) getActivity()).setHeaderTitle(getString(R.string.page_title_offline_player));
	}

	public void initProgress() {
		// prgDuration.setProgress(0);
		// MusicInfo info = FROForm.getInstance().getNowPlaying();
		// if (info == null)
		// return;
		//
		// int length = info.getLength() / 1000;
		// currentPos = FROForm.getInstance().getCurrentPlayerPos() / 1000;
		// prgDuration.setMax(length);
		// prgDuration.setProgress(currentPos);
		// if (progressTimer == null) {
		// progressTimer = new SimpleTimer2(this, true, TIMER_TYPE_PROGRESS);
		// }
		// progressTimer.start(1000);
	}

	@Override
	public void onTimerEvent(int type) {
		switch (type) {
		case TIMER_TYPE_PROGRESS:
			// if (getActivity() != null)
			// ((BaseActivity) getActivity()).getUiHandler().post(new Runnable()
			// {
			// @Override
			// public void run() {
			// if (FROForm.getInstance().isPlaying())
			// prgDuration.setProgress(++currentPos);
			// }
			// });
		}
	}

	public void stopTimer() {
		if (progressTimer != null) {
			progressTimer.stop();
			progressTimer = null;
		}
	}

	public ChannelInfo getFavorite() {
		return this.favorite;
	}

	int currentPos = -1;

	@Override
	public void show() {
	}

	@Override
	public void onBackPress() {
		((IModeActivity) getActivity()).showMode(MainActivity.TOP);
	}

	public void onBtnClicked(int resId) {
		switch (resId) {
		case R.id.header_btn_right:
			break;
		}
	}

	public void onItemClicked(int tag, int pos) {
		switch (tag) {
		case MainActivity.DIALOG_TAG_CART:
			break;
		}
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
