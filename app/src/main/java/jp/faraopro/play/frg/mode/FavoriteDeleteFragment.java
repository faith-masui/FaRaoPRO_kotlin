package jp.faraopro.play.frg.mode;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.domain.ChannelInfo;
import jp.faraopro.play.domain.FROFavoriteChannelDBFactory;
import jp.faraopro.play.domain.FROTimerDBFactory;
import jp.faraopro.play.view.CustomListItem;

public class FavoriteDeleteFragment extends ListBaseFragment {
	/** const **/

	/** bundle key **/

	/** view **/
	// private Button btnEdit;
	// private TextView txtIntro;
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
		if (list.size() < 1) {
			txtNodata.setVisibility(View.VISIBLE);
		} else {
			txtNodata.setVisibility(View.GONE);
		}
		refresh();
	}

	@Override
	public void onListItemClicked(int resId, int position) {
		switch (resId) {
		case R.id.deletelist_ibtn_delete:
			FROFavoriteChannelDBFactory.getInstance(getActivity()).findAll();
			FROFavoriteChannelDBFactory.getInstance(getActivity()).delete(favorite.get(position).getId());
			FROTimerDBFactory.getInstance(getActivity()).findAll();
			removeContent(position);
			favorite.remove(position);
			((BaseActivity) getActivity()).getUiHandler().post(new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			});
			break;
		case R.id.deletelist_txt_text:
			FROForm.getInstance().editedChannel = favorite.get(position);
			((IModeActivity) getActivity()).showMode(MainActivity.FAVORITE_EDIT);
			break;
		}
	}

	@Override
	public void onBackPress() {
		((MainActivity) getActivity()).showMode(MainActivity.FAVORITE);
	}

	@Override
	public void onError(int when, int code) {
		// if (code == Consts.STATUS_NO_DATA) {
		// btnEdit.setVisibility(View.INVISIBLE);
		// txtIntro.setVisibility(View.INVISIBLE);
		// txtNodata.setVisibility(View.VISIBLE);
		// }
	}
}
