package jp.faraopro.play.frg.mode;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.domain.FROTaskQueue;
import jp.faraopro.play.domain.ListAdapterForBase;
import jp.faraopro.play.util.Utils;
import jp.faraopro.play.view.CustomListItem;

public abstract class PulltorefreshListBaseFragment extends Fragment
		implements IModeFragment, ListAdapterForBase.IImageLoadListener, ListAdapterForBase.IListClickListener {

	private static final int[] resIds = { R.layout.new_ele_simplelist_item, R.layout.new_ele_imagelist_item,
			R.layout.new_ele_historylist_item, R.layout.new_ele_otherlist_item, R.layout.new_ele_draglist_item, -1,
			R.layout.new_ele_setting_item };

	/** bundle key **/
	public static final String LIST_TYPE = "LIST_TYPE";
	public static final int LIST_TYPE_NORMAL = 0;
	public static final int LIST_TYPE_IMAGE = 1;
	public static final int LIST_TYPE_HISTORY = 2;
	public static final int LIST_TYPE_OTHER = 3;
	public static final int LIST_TYPE_DRAG = 4;
	public static final int LIST_TYPE_EDIT = 5;
	public static final int LIST_TYPE_SETTING = 6;

	/** view **/
	protected ListView listView;
	private LinearLayout lnrOptionHeader;
	private LinearLayout lnrOptionFooter;
	private LinearLayout lnrMargin;

	/** member **/
	private List<CustomListItem> contents;
	private ListAdapterForBase adapter;
	private int listType = LIST_TYPE_NORMAL;

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
		View view = inflater.inflate(getInflateLayoutId(), container, false);
		initViews(view);
		return view;
	}

	private PullToRefreshListView ptrlv;

	protected void initViews(View view) {
		Bundle args = getArguments();
		if (args != null) {
			listType = args.getInt(LIST_TYPE);
		}
		contents = new ArrayList<CustomListItem>();
		// アイコン付リスト(特集、アーティスト)、履歴リストに該当しない場合、リスナーは不要
		if (listType != LIST_TYPE_IMAGE && listType != LIST_TYPE_HISTORY) {
			adapter = new ListAdapterForBase(getActivity(), resIds[listType], (ArrayList<CustomListItem>) contents);
		} else {
			adapter = new ListAdapterForBase(getActivity(), resIds[listType], (ArrayList<CustomListItem>) contents,
					this, this);
		}

		int ptrli = getPullToRefreshListViewLayoutId();
		if (ptrli > 0) {
			ptrlv = (PullToRefreshListView) view.findViewById(R.id.list_pulltorefresh);

			ptrlv.setOnRefreshListener(new OnRefreshListener<ListView>() {
				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					onRefreshImpl(refreshView);
				}
			});

			// Add an end-of-list listener
			ptrlv.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
				@Override
				public void onLastItemVisible() {
					onLastItemVisibleImpl();
				}
			});
			listView = ptrlv.getRefreshableView();
			registerForContextMenu(listView);

			// 影を消す
			listView.setFadingEdgeLength(0);
			// 境界線の設定
			// listView.setDivider(getResources().getDrawable(R.drawable.parts_dot_gray));
			// listView.setDividerHeight(1);
			listView.setAdapter(adapter);
			refresh();
			lnrOptionHeader = (LinearLayout) view.findViewById(R.id.simplelist_lnr_opt_header);
			lnrOptionFooter = (LinearLayout) view.findViewById(R.id.simplelist_lnr_opt_footer);
			lnrMargin = (LinearLayout) view.findViewById(R.id.simplelist_lnr_margin);
			// if (MainActivity.menuSize > 0) {
			// lnrMargin.setLayoutParams(new LayoutParams(
			// LayoutParams.MATCH_PARENT, MainActivity.menuSize));
			// }

		}
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
		if (!hidden)
			getData();
	}

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

		// if (MainActivity.menuSize > 0) {
		// lnrMargin.setLayoutParams(new LayoutParams(
		// LayoutParams.MATCH_PARENT, MainActivity.menuSize));
		// }
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

	public void refresh() {
		if (adapter != null)
			adapter.notifyDataSetChanged();
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
				getLoaderManager().initLoader(paramInt, args, callback);
			}
		});
	}

	/**
	 * layoutに記載したPullToRefreshListViewのLayoutIdを返すこと example : return
	 * R.id.list_pulltorefresh;
	 * 
	 * @return
	 */
	protected int getPullToRefreshListViewLayoutId() {
		return R.id.list_pulltorefresh;
	}

	/**
	 * inflateするLayoutIdを返す example : return R.layout.frg_pulltorefresh_list;
	 * 
	 * @return
	 */
	protected int getInflateLayoutId() {
		return R.layout.frg_pulltorefresh_list;
	}

	/**
	 * リフレッシュ時のコールバック関数
	 * 
	 * @param refreshView
	 */
	protected abstract void onRefreshImpl(PullToRefreshBase<ListView> refreshView);

	protected abstract void onLastItemVisibleImpl();

	protected final ListView getListView() {
		return listView;
	}

	/**
	 * リフレッシュヘッダーの非表示化
	 */
	protected final void refreshComplete() {
		if (ptrlv == null) {
			return;
		}
		ptrlv.onRefreshComplete();
	}

	/**
	 * リフレッシュヘッダーの文言更新
	 */
	protected final void setLastUpdatedLabel(String label) {
		ptrlv.getLoadingLayoutProxy().setLastUpdatedLabel(label);
	}

}
