package jp.faraopro.play.frg.mode;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.domain.ChannelHistoryInfo;
import jp.faraopro.play.domain.FROChannelHistoryDB;
import jp.faraopro.play.domain.FROChannelHistoryDBFactory;
import jp.faraopro.play.view.CustomListItem;

public class ChannelHistoryFragment extends PulltorefreshListBaseFragment {

	/** view **/
	private TextView mTextNodata;

	/** member **/
	private ArrayList<CustomListItem> mList;

	@Override
	protected void initViews(View view) {
		super.initViews(view);
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View nodata = inflater.inflate(R.layout.new_res_plane_text, null);
		nodata.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setOptionalView(0, nodata);
		mTextNodata = (TextView) nodata.findViewById(R.id.planetext_txt_text);
		mTextNodata.setText(R.string.msg_toast_no_channel_history_data);
		getData();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		getData();
	}

	@Override
	public void getData() {
		((BaseActivity) getActivity()).showProgress();
		if (mList != null)
			mList.clear();
		mList = new ArrayList<CustomListItem>();
		FROChannelHistoryDB db = FROChannelHistoryDBFactory.getInstance(getActivity());
		if (db.getSize() > 0) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					ArrayList<ChannelHistoryInfo> list = (ArrayList<ChannelHistoryInfo>) FROChannelHistoryDBFactory
							.getInstance(getActivity()).findAll();
					for (ChannelHistoryInfo chi : list) {
						CustomListItem item = new CustomListItem(chi);
						mList.add(item);
					}
					((BaseActivity) getActivity()).getUiHandler().post(new Runnable() {
						@Override
						public void run() {
							show();
						}
					});
				}
			}).start();
		} else {
			show();
		}
	}

	// private void getHistoryData() {
	// new Thread(new Runnable() {
	// @Override
	// public void run() {
	// ArrayList<ChannelHistoryInfo> list = (ArrayList<ChannelHistoryInfo>)
	// FROChannelHistoryDBFactory
	// .getInstance(getActivity()).findAll();
	// for (ChannelHistoryInfo chi : list) {
	// CustomListItem item = new CustomListItem(chi);
	// mList.add(item);
	// }
	// ((BaseActivity) getActivity()).getUiHandler().post(new Runnable() {
	// @Override
	// public void run() {
	// show();
	// }
	// });
	// }
	// }).start();
	// }

	@Override
	public void show() {
		if (isDetached() || getActivity() == null)
			return;

		((BaseActivity) getActivity()).dismissProgress();
		if (mList != null && mList.size() > 0) {
			mTextNodata.setVisibility(View.GONE);
			setContents(mList);
			setClickEvent(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					arg2 -= 1;
					if (arg2 >= mList.size())
						return;

					CustomListItem item = mList.get(arg2);
					((MainActivity) getActivity()).bootPlayer(item.getMode(), item.getChannelId(), item.getRange());
				}
			});
		} else {
			mTextNodata.setVisibility(View.VISIBLE);
		}
		refresh();
	}

	@Override
	public void onBackPress() {
		if (FROForm.getInstance().isMenu(Consts.TAG_MODE_HISTORY, getActivity())) {
			((MainActivity) getActivity()).showMode(MainActivity.TOP);
		} else {
			((MainActivity) getActivity()).showMode(MainActivity.OTHER);
		}
	}

	@Override
	public void onError(int when, int code) {
	}

	@Override
	protected void onRefreshImpl(PullToRefreshBase<ListView> refreshView) {
		new AsyncTask<Void, Void, String[]>() {
			@Override
			protected String[] doInBackground(Void... params) {
				((BaseActivity) getActivity()).showProgress();
				if (mList != null)
					mList.clear();
				mList = new ArrayList<CustomListItem>();
				FROChannelHistoryDB db = FROChannelHistoryDBFactory.getInstance(getActivity());
				if (db.getSize() > 0) {
					ArrayList<ChannelHistoryInfo> list = (ArrayList<ChannelHistoryInfo>) FROChannelHistoryDBFactory
							.getInstance(getActivity()).findAll();
					for (ChannelHistoryInfo chi : list) {
						CustomListItem item = new CustomListItem(chi);
						mList.add(item);
					}
				}
				((BaseActivity) getActivity()).dismissProgress();
				return null;
			}

			@Override
			protected void onPostExecute(String[] result) {
				setContents(mList);
				if (mList == null || mList.size() < 1) {
					mTextNodata.setVisibility(View.VISIBLE);
				} else {
					mTextNodata.setVisibility(View.GONE);
				}
				refresh();
				refreshComplete();
				super.onPostExecute(result);
			}
		}.execute();
	}

	@Override
	protected void onLastItemVisibleImpl() {
	}
}
