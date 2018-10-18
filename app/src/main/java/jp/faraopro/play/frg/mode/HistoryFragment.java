package jp.faraopro.play.frg.mode;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.util.ArrayList;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.model.ChannelMode;
import jp.faraopro.play.view.CustomListItem;

public class HistoryFragment extends PulltorefreshListBaseFragment {
	/** const **/
	public static final int MUSIC_HISTORY = 0;
	public static final int GOOD_HISTORY = 1;

	/** bundle key **/
	public static final String BUNDLE_KEY_SELECT = "SELECT";

	/** view **/
	private TextView txtNodata;

	/** member **/
	private ArrayList<CustomListItem> historyList;
	private ArrayList<CustomListItem> goodList;
	private int selected = MUSIC_HISTORY;

	// private MusicInfo shareInfo;

	@Override
	protected void initViews(View view) {
		super.initViews(view);

		selected = MUSIC_HISTORY;
		Bundle args = getArguments();
		if (args != null) {
			selected = args.getInt(BUNDLE_KEY_SELECT);
		}
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View nodata = inflater.inflate(R.layout.new_res_plane_text, null);
		nodata.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setOptionalView(0, nodata);
		txtNodata = (TextView) nodata.findViewById(R.id.planetext_txt_text);
		txtNodata.setText(R.string.msg_toast_no_history_data);
		//change(selected);
		show();
	}

	public void change(int type) {
		selected = type;
		show();
	}

	@Override
	public void getData() {
	}

	@Override
	public void show() {
		//if (selected == MUSIC_HISTORY) {
			if (historyList == null)
				historyList = FROForm.getInstance().getHistoryList(getActivity());
			setContents(historyList);
			if (historyList == null || historyList.size() < 1) {
				txtNodata.setText(R.string.msg_toast_no_history_data);
				txtNodata.setVisibility(View.VISIBLE);
			} else {
				txtNodata.setVisibility(View.GONE);
			}
		//} else if (selected == GOOD_HISTORY) {
		//	if (goodList == null)
		//		goodList = FROForm.getInstance().getGoodHistoryList(getActivity());
		//	setContents(goodList);
		//	if (goodList == null || goodList.size() < 1) {
		//		txtNodata.setText(R.string.msg_toast_no_good_data);
		//		txtNodata.setVisibility(View.VISIBLE);
		//	} else {
		//		txtNodata.setVisibility(View.GONE);
		//	}
		//}
		setClickEvent(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

			}
		});
		refresh();
	}

	@Override
	public void onListItemClicked(int resId, int position) {
		super.onListItemClicked(resId, position);
		CustomListItem item = new CustomListItem();
		//if (selected == MUSIC_HISTORY) {
			item = historyList.get(position);
		//} else {
		//	item = goodList.get(position);
		//}

		switch (resId) {
		case R.id.historylist_lnr_parent:
			String tmp = item.getmContentText();
			if (!TextUtils.isEmpty(tmp)) {
				String[] params = tmp.split(",");
				String range = null;
				if (params.length == 3)
					range = params[2];
				((MainActivity) getActivity()).bootPlayer(ChannelMode.valueOfString(params[0]),
						Integer.parseInt(params[1]), range);
			}
			break;
		}
	}

	@Override
	public void onBackPress() {
		if (FROForm.getInstance().isMenu(Consts.TAG_MODE_HISTORY, getActivity())) {
			((MainActivity) getActivity()).showMode(MainActivity.TOP);
		} else {
			((MainActivity) getActivity()).showMode(MainActivity.OTHER);
		}
	}

	public void onItemClicked(int tag, int pos) {
		switch (tag) {
		case MainActivity.DIALOG_TAG_SHARE_HISTORY:
		}
	}

	@Override
	protected void onRefreshImpl(PullToRefreshBase<ListView> refreshView) {
		new AsyncTask<Void, Void, String[]>() {
			@Override
			protected String[] doInBackground(Void... params) {
				//if (selected == MUSIC_HISTORY) {
					if (historyList != null)
						historyList.clear();
					historyList = FROForm.getInstance().getHistoryList(getActivity());
				//} else {
				//	if (goodList != null)
				//		goodList.clear();
				//	goodList = FROForm.getInstance().getGoodHistoryList(getActivity());
				//}
				return null;
			}

			@Override
			protected void onPostExecute(String[] result) {
				//if (selected == MUSIC_HISTORY) {
					setContents(historyList);
					if (historyList == null || historyList.size() < 1) {
						txtNodata.setVisibility(View.VISIBLE);
					} else {
						txtNodata.setVisibility(View.GONE);
					}
				//} else {
				//	setContents(goodList);
				//	if (goodList == null || goodList.size() < 1) {
				//		txtNodata.setVisibility(View.VISIBLE);
				//	} else {
				//		txtNodata.setVisibility(View.GONE);
				//	}
				//}
				refresh();
				refreshComplete();
				super.onPostExecute(result);
			}
		}.execute();
	}

	@Override
	protected void onLastItemVisibleImpl() {
	}

	@Override
	public void onError(int when, int code) {
		// TODO Auto-generated method stub

	}

}
