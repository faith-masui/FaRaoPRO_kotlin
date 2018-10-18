package jp.faraopro.play.frg.mode;

import java.util.ArrayList;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.view.CustomListItem;

public class StreamingFragment extends ListBaseFragment {
	/** const **/

	/** member **/
	private ArrayList<CustomListItem> streams;

	@Override
	public void getData() {
		// if (FROForm.getInstance().streamList == null
		// || FROForm.getInstance().streamList.size() < 1) {
		((BaseActivity) getActivity()).showProgress();
		FROForm.getInstance().getStreamList(Consts.STREAM_TYPE_CABLE);
		// } else {
		// show();
		// }
	}

	@Override
	@SuppressWarnings("unchecked")
	public void show() {
		((BaseActivity) getActivity()).dismissProgress();
		if (streams != null)
			streams.clear();
		streams = (ArrayList<CustomListItem>) FROForm.getInstance().streamList.clone();
		setContents(streams);
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

				((BaseActivity) getActivity()).showProgress();
				FROForm.getInstance().changeFragment(MainActivity.STREAM_PLAYER, Consts.STATUS_OK);
				FROForm.getInstance().playStream(streams.get(arg2).getId());
			}
		});
		refresh();
	}

	@Override
	public void onBackPress() {
		// if (FROForm.getInstance().isMenu(Consts.TAG_MODE_STREAMING,
		// getActivity())) {
		// ((IModeActivity) getActivity()).showMode(MainActivity.TOP);
		// } else {
		// ((IModeActivity) getActivity()).showMode(MainActivity.OTHER);
		// }
	}
}
