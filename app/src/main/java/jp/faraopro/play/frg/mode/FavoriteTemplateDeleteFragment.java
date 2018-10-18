package jp.faraopro.play.frg.mode;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.domain.FROFavoriteTemplateDBFactory;
import jp.faraopro.play.domain.FROTemplateFavoriteListDB;
import jp.faraopro.play.domain.FROTemplateFavoriteListDBFactory;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCTemplateItem;
import jp.faraopro.play.view.CustomListItem;

public class FavoriteTemplateDeleteFragment extends ListBaseFragment {
	/** const **/

	/** bundle key **/

	/** view **/
	// private Button btnEdit;
	// private TextView txtIntro;
	// private TextView txtNodata;

	/** member **/
	// private ArrayList<CustomListItem> list;
	private ArrayList<MCTemplateItem> favorite;

	@Override
	protected void initViews(View view) {
		super.initViews(view);

		Bundle args = getArguments();
		if (args != null) {
		}

		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.new_res_channel_edit, null);
		v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setOptionalView(1, v);
		Button btnImport = (Button) v.findViewById(R.id.mychannel_btn_edit);
		btnImport.setText(R.string.btn_import_template_favorite);
		btnImport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).showMode(MainActivity.IMPORT_FAVORITE_TEMPLATE);
			}
		});
		TextView txtIntro = (TextView) v.findViewById(R.id.mychannel_txt_intro);
		txtIntro.setText(R.string.msg_favorite_description);
	}

	@Override
	public void getData() {
		// favorite = (ArrayList<ChannelInfo>) FROFavoriteChannelDBFactory
		// .getInstance(getActivity()).findAll();
		favorite = new ArrayList<MCTemplateItem>();
		MCTemplateItem local = new MCTemplateItem();
		local.setString(MCDefResult.MCR_KIND_CHANNELITEM_NAME, getString(R.string.page_title_favorite));
		local.setString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN, getString(R.string.page_title_favorite));
		local.setString(MCDefResult.MCR_KIND_TEMPLATE_ID, "-1");
		local.setString(MCDefResult.MCR_KIND_TEMPLATE_TYPE, "1");
		favorite.add(local);
		FROTemplateFavoriteListDB templateListDb = FROTemplateFavoriteListDBFactory.getInstance(getActivity());
		ArrayList<MCTemplateItem> templateList = (ArrayList<MCTemplateItem>) templateListDb.findAll();
		if (templateList != null && templateList.size() > 0) {
			for (MCTemplateItem ti : templateList) {
				favorite.add(ti);
			}
		}
		show();
	}

	@Override
	public void show() {
		((BaseActivity) getActivity()).dismissProgress();
		ArrayList<CustomListItem> list = new ArrayList<CustomListItem>();
		if (favorite != null && favorite.size() > 0) {
			for (MCTemplateItem item : favorite) {
				list.add(new CustomListItem(item));
			}
			setContents(list);
		} else {
			((BaseActivity) getActivity()).showToast(getString(R.string.msg_nodata));
		}
		refresh();
	}

	@Override
	public void onListItemClicked(int resId, int position) {
		switch (resId) {
		case R.id.deletelist_ibtn_delete:
			// FROFavoriteChannelDBFactory.getInstance(getActivity()).delete(
			// favorite.get(position).getId());
			FROTemplateFavoriteListDBFactory.getInstance(getActivity())
					.delete(favorite.get(position).getString(MCDefResult.MCR_KIND_TEMPLATE_ID));
			FROFavoriteTemplateDBFactory.getInstance(getActivity())
					.deleteByParent(favorite.get(position).getString(MCDefResult.MCR_KIND_TEMPLATE_ID));
			removeContent(position);
			favorite.remove(position);
			((BaseActivity) getActivity()).getUiHandler().post(new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			});
			break;
		// case R.id.deletelist_txt_text:
		// FROForm.getInstance().editedChannel = favorite.get(position);
		// ((IModeActivity) getActivity())
		// .showMode(MainActivity.FAVORITE_EDIT);
		// break;
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
