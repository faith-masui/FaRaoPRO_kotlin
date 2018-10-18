package jp.faraopro.play.frg.mode;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.domain.FROFavoriteTemplateDB;
import jp.faraopro.play.domain.FROFavoriteTemplateDBFactory;
import jp.faraopro.play.domain.FROTemplateFavoriteListDB;
import jp.faraopro.play.domain.FROTemplateFavoriteListDBFactory;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCTemplateItem;
import jp.faraopro.play.view.CustomListItem;

public class FavoriteFragment extends ListBaseFragment {
	/** const **/

	/** bundle key **/

	/** view **/

	/** member **/
	private ArrayList<MCTemplateItem> favorite;
	private MCTemplateItem selectedItem;

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
				FROFavoriteTemplateDB favoriteTemplateDb = FROFavoriteTemplateDBFactory.getInstance(getActivity());
				if (favoriteTemplateDb.getSize() > 10) {
					((BaseActivity) getActivity()).showToast(getString(R.string.msg_favorite_import_error_maxsize));
				} else {
					((MainActivity) getActivity()).showMode(MainActivity.IMPORT_FAVORITE_TEMPLATE);
				}
			}
		});
		TextView txtIntro = (TextView) v.findViewById(R.id.mychannel_txt_intro);
		txtIntro.setText(R.string.msg_favorite_description);
	}

	@Override
	public void getData() {
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
		// if (FROForm.getInstance().TEMPLATE_DB_STUB != null
		// && FROForm.getInstance().TEMPLATE_DB_STUB.size() > 0) {
		// for (TemplateDBItem tdbi : FROForm.getInstance().TEMPLATE_DB_STUB) {
		// MCTemplateItem tmp = new MCTemplateItem();
		// tmp.setString(MCDefResult.MCR_KIND_TEMPLATE_ID,
		// Integer.toString(tdbi.id));
		// tmp.setString(MCDefResult.MCR_KIND_TEMPLATE_TYPE,
		// Integer.toString(tdbi.type));
		// tmp.setString(MCDefResult.MCR_KIND_CHANNELITEM_NAME, tdbi.name);
		// tmp.setString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN,
		// tdbi.name_en);
		// favorite.add(tmp);
		// }
		// }
		show();
	}

	@Override
	public void show() {
		((BaseActivity) getActivity()).dismissProgress();
		ArrayList<CustomListItem> list = new ArrayList<CustomListItem>();
		if (favorite != null && favorite.size() > 0) {
			for (MCTemplateItem ci : favorite) {
				CustomListItem item = new CustomListItem(ci);
				item.setNext(true);
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

				// ローカルのお気に入りを選択した場合
				if (arg2 == 0) {
					((MainActivity) getActivity()).showMode(MainActivity.FAVORITE_LOCAL);
				}
				// テンプレートを選択した場合
				else {
					selectedItem = favorite.get(arg2);
					FROForm.getInstance().parentId = selectedItem.getString(MCDefResult.MCR_KIND_TEMPLATE_ID);
					((MainActivity) getActivity()).showMode(MainActivity.FAVORITE_TEMPLATE);
				}
			}
		});
		refresh();
	}

	public MCTemplateItem getSelectedChannel() {
		return selectedItem;
	}

	@Override
	public void onBackPress() {
		selectedItem = null;
		if (FROForm.getInstance().isMenu(Consts.TAG_MODE_MYCHANNEL, getActivity())) {
			((MainActivity) getActivity()).showMode(MainActivity.TOP);
		} else {
			((MainActivity) getActivity()).showMode(MainActivity.OTHER);
		}
	}

	@Override
	public void onError(int when, int code) {
	}
}
