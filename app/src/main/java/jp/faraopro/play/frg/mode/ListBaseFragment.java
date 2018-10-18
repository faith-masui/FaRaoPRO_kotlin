package jp.faraopro.play.frg.mode;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.domain.FROTaskQueue;
import jp.faraopro.play.domain.ListAdapterForBase;
import jp.faraopro.play.util.Utils;
import jp.faraopro.play.view.CustomListItem;

/**
 * 
 * @author Aim タイトルバー
 */
public class ListBaseFragment extends ListFragment
		implements IModeFragment, ListAdapterForBase.IImageLoadListener, ListAdapterForBase.IListClickListener {
	/** const **/
	private static final int[] resIds = { R.layout.new_ele_simplelist_item, R.layout.new_ele_imagelist_item,
			R.layout.new_ele_historylist_item, R.layout.new_ele_otherlist_item, R.layout.new_ele_draglist_item, -1,
			R.layout.new_ele_setting_item, R.layout.new_ele_checklist_item, R.layout.new_ele_deletelist_timer_item,
			R.layout.new_ele_folderlist_item, R.layout.new_ele_timerlist_item, R.layout.new_ele_deletelist_item,
			R.layout.layout_pattern_list_element };

	/** bundle key **/
	public static final String LIST_TYPE = "LIST_TYPE";
	public static final int LIST_TYPE_NORMAL = 0;
	public static final int LIST_TYPE_IMAGE = 1;
	public static final int LIST_TYPE_HISTORY = 2;
	public static final int LIST_TYPE_OTHER = 3;
	public static final int LIST_TYPE_DRAG = 4;
	public static final int LIST_TYPE_EDIT = 5;
	public static final int LIST_TYPE_SETTING = 6;
	public static final int LIST_TYPE_CHECK = 7;
	public static final int LIST_TYPE_DELETE_TIMER = 8;
	public static final int LIST_TYPE_FOLDER = 9;
	public static final int LIST_TYPE_TIMER = 10;
	public static final int LIST_TYPE_DELETE = 11;
	public static final int LIST_TYPE_PATTERN_DETAIL = 12;

	/** view **/
	protected ListView listView;
	protected View footer;
	private LinearLayout lnrOptionHeader;
	private LinearLayout lnrOptionFooter;
	private LinearLayout lnrMargin;

	/** member **/
	private List<CustomListItem> contents;
	private ListAdapterForBase adapter;
	// private int listType = LIST_TYPE_NORMAL;
	protected boolean hasToolbar = true;
	protected static boolean eventeBlock = false;

	// protected boolean isListener = false;

	public static ListBaseFragment newInstance() {
		ListBaseFragment instance = new ListBaseFragment();

		return instance;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
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
		View view = inflater.inflate(R.layout.layout_simple_3block_list, container, false);
		initViews(view);

		return view;
	}

	protected void initViews(View view) {
		int listtype = LIST_TYPE_NORMAL;
		Bundle args = getArguments();
		if (args != null) {
			listtype = args.getInt(LIST_TYPE);
		}
		contents = new ArrayList<CustomListItem>();
		// アイコン付リスト(特集、アーティスト)、履歴リストに該当しない場合、リスナーは不要
		if (needToSetListener(listtype)) {
			adapter = new ListAdapterForBase(getActivity(), resIds[listtype], (ArrayList<CustomListItem>) contents,
					this, this);
		} else {
			adapter = new ListAdapterForBase(getActivity(), resIds[listtype], (ArrayList<CustomListItem>) contents);
		}
		listView = (ListView) view.findViewById(android.R.id.list);
		// 影を消す
		listView.setFadingEdgeLength(0);
		if (footer != null) {
			listView.addFooterView(footer);
		}
		listView.setAdapter(adapter);
		refresh();
		lnrOptionHeader = (LinearLayout) view.findViewById(R.id.simplelist_lnr_opt_header);
		lnrOptionFooter = (LinearLayout) view.findViewById(R.id.simplelist_lnr_opt_footer);
		lnrMargin = (LinearLayout) view.findViewById(R.id.simplelist_lnr_margin);
		// if (MainActivity.menuSize > 0 && hasToolbar) {
		// lnrMargin.setLayoutParams(new LayoutParams(
		// LayoutParams.MATCH_PARENT, MainActivity.menuSize));
		// }
	}

	private boolean needToSetListener(int listtype) {
		boolean need = false;
		switch (listtype) {
		case LIST_TYPE_IMAGE:
		case LIST_TYPE_HISTORY:
		case LIST_TYPE_OTHER:
		case LIST_TYPE_TIMER:
		case LIST_TYPE_DELETE:
		case LIST_TYPE_DELETE_TIMER:
		case LIST_TYPE_SETTING:
			need = true;
			break;
		}

		return need;
	}

	protected void setOptionalView(int type, View v) {
		switch (type) {
		case 0:
			lnrOptionHeader.addView(v);
			break;
		case 1:
			lnrOptionFooter.addView(v);
			break;
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if (!hidden) {
			getData();
		}
	}

	// public void setBottomPadding(int height) {
	// listView.setLayoutParams(new LayoutParams(listView.getWidth(), listView
	// .getWidth() + height));
	// }

	/**
	 * リストに表示するデータを取得するメソッド<br>
	 * {@link ListBaseFragment}が可視状態(hide -> show)になった際に呼ばれる
	 */
	@Override
	public void getData() {
	}

	/**
	 * 受け取ったリストをリストビューに表示する
	 * 
	 * @param list
	 *            表示する内容
	 */
	public void setContents(List<CustomListItem> list) {
		if (contents != null)
			contents.clear();
		if (list != null)
			contents.addAll(list);

		// int menuSize = ((IModeActivity) getActivity()).getMenuSize();
		// if (menuSize > 0 && hasToolbar) {
		// lnrMargin.setLayoutParams(new LayoutParams(
		// LayoutParams.MATCH_PARENT, menuSize));
		// }
	}

	public void addContents(List<CustomListItem> list) {
		if (list != null)
			contents.addAll(list);
	}

	/**
	 * リストタップ時のイベントを設定する
	 * 
	 * @param listener
	 *            イベントリスナ
	 */
	public void setClickEvent(OnItemClickListener listener) {
		listView.setOnItemClickListener(listener);
	}

	/**
	 * 指定されたインデックスのリスト要素を取得する
	 * 
	 * @param index
	 *            リスト番号
	 * @return リスト情報({@link CustomListItem})
	 */
	public CustomListItem getContent(int index) {
		CustomListItem item = null;

		if (contents != null && contents.size() > index)
			item = contents.get(index);

		return item;
	}

	/**
	 * 指定されたインデックスのリスト要素を削除する
	 * 
	 * @param index
	 *            リスト番号
	 * @return リスト情報({@link CustomListItem})
	 */
	public void removeContent(int index) {
		if (contents != null && contents.size() > index)
			contents.remove(index);
	}

	public void refresh() {
		if (adapter != null)
			adapter.notifyDataSetChanged();
		if (listView != null)
			listView.invalidateViews();
	}

	public void startLocalMusic(String path) {
		FROForm.getInstance().changeFragment(MainActivity.LOCAL_PLAYER, Consts.STATUS_OK);
		FROForm.getInstance().playLocal(path, true);
	}

	public void startStreamMusic(String streamId) {
		FROForm.getInstance().changeFragment(MainActivity.STREAM_PLAYER, Consts.STATUS_OK);
		FROForm.getInstance().playStream(Integer.parseInt(streamId));
	}

	@Override
	public void loadImage(CustomListItem item, int position) {
	}

	@Override
	public void onListItemClicked(int resId, int position) {
	}

	@Override
	public void show() {
	}

	@Override
	public void updateViews() {
	}

	@Override
	public void onBackPress() {
	}

	protected void initLoaderImpl(final int paramInt, final Bundle args, final LoaderCallbacks<Bitmap> callback) {
		FROTaskQueue.addRunnable(new Runnable() {
			@Override
			public void run() {
				// Fragmentがattachされていない場合は実行しない
				if (isAdded())
					getLoaderManager().initLoader(paramInt, args, callback);
			}
		});
	}

	@Override
	public void onError(int when, int code) {
		// TODO Auto-generated method stub

	}

}
