package jp.faraopro.play.frg.mode;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import java.util.ArrayList;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.domain.ChannelInfo;
import jp.faraopro.play.domain.FROFavoriteChannelDBFactory;
import jp.faraopro.play.model.ChannelMode;
import jp.faraopro.play.view.CustomListItem;

public class FavoriteLocalFragment extends ListBaseFragment {
	/** const **/

	/** bundle key **/

	/** view **/
	private TextView txtNodata;

	/** member **/
	// private ArrayList<CustomListItem> list;
	private ArrayList<ChannelInfo> favorite;

	@Override
	protected void initViews(View view) {
		super.initViews(view);

		Bundle args = getArguments();
		if (args != null) {
		}

		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.new_res_plane_text, null);
		v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setOptionalView(0, v);
		txtNodata = (TextView) v.findViewById(R.id.planetext_txt_text);
		txtNodata.setText(R.string.msg_no_favorites);
	}

	@Override
	public void getData() {
		favorite = (ArrayList<ChannelInfo>) FROFavoriteChannelDBFactory.getInstance(getActivity()).findAll();
		show();
	}

	@Override
	public void show() {
		((BaseActivity) getActivity()).dismissProgress();
		ArrayList<CustomListItem> list = new ArrayList<CustomListItem>();
		if (favorite != null && favorite.size() > 0) {
			for (ChannelInfo ci : favorite) {
				CustomListItem item = new CustomListItem(ci);
				list.add(item);
			}
			setContents(list);
		}
		setContents(list);
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

				ChannelInfo info = favorite.get(arg2);
				if (info.getType() == Consts.MUSIC_TYPE_LOCAL) {
					startLocalMusic(info.getSource());
				} else if (info.getType() == Consts.MUSIC_TYPE_SIMUL) {
					startStreamMusic(info.getChannelId());
				} else {
					((MainActivity) getActivity()).bootPlayer(ChannelMode.valueOfString(info.getMode()),
							Integer.parseInt(info.getChannelId()), info.getRange(), info.getPermisson());
				}
			}
		});

		if (list.size() < 1) {
			txtNodata.setVisibility(View.VISIBLE);
		} else {
			txtNodata.setVisibility(View.GONE);
		}
		refresh();
	}

	@Override
	public void onBackPress() {
		((MainActivity) getActivity()).showMode(MainActivity.FAVORITE);
	}

	@Override
	public void onError(int when, int code) {
	}
}
