package jp.faraopro.play.frg.mode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;

import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.domain.FROAsyncTaskLoader;
import jp.faraopro.play.domain.FROTaskQueue;
import jp.faraopro.play.model.ChannelMode;
import jp.faraopro.play.util.Utils;
import jp.faraopro.play.view.CustomListItem;

public class ArtistFeaturedFragment extends ListBaseFragment implements LoaderManager.LoaderCallbacks<Bitmap> {
	/** const **/

	/** bundle key **/

	/** view **/

	/** member **/
	private ArrayList<CustomListItem> lists;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void getData() {
		((BaseActivity) getActivity()).showProgress();
		FROForm.getInstance().getFeaturedArtistList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void show() {
		((BaseActivity) getActivity()).dismissProgress();
		if (lists != null)
			lists.clear();

		lists = (ArrayList<CustomListItem>) FROForm.getInstance().featuredList.clone();
		setContents(lists);
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
				((MainActivity) getActivity()).bootPlayer(ChannelMode.ARTIST, lists.get(arg2).getId(), null);
			}
		});
		refresh();
	}

	private int loaderIndex = 0;

	@Override
	public void loadImage(CustomListItem item, int position) {
		// 端末によっては new File(null) でexceptionとなるため、確認を行う
		String fileName = Consts.PRIVATE_PATH_THUMB_LIST + item.getMode().text
				+ item.getmThumbIcon().replace(".jpg", "").replace(".png", "");
		File file = new File(getActivity().getFilesDir().getAbsolutePath(), fileName);
		// 既にサムネイルが存在している場合
		if (file != null && file.exists()) {
			// 日付のチェック
			long last;
			long now;
			// 最終更新日時
			last = file.lastModified();
			// 現在時刻
			now = Utils.getNowTime();
			long diff = now - last;
			// 差が24H以上の場合
			if (diff >= Utils.VALUE_OF_ONEDAY) {
				Bundle args = new Bundle();
				args.putString(FROAsyncTaskLoader.TAG_THUMB_ID, item.getmThumbIcon());
				args.putString(FROAsyncTaskLoader.TAG_FRG_NAME, getTag());
				args.putInt(FROAsyncTaskLoader.TAG_LIST_POS, position);
				args.putString(FROAsyncTaskLoader.TAG_LIST_TYPE, item.getMode().text);
				args.putString(FROAsyncTaskLoader.TAG_THUMBNAIL_URL, item.getThumnailSmall());
				args.putString(FROAsyncTaskLoader.TAG_LAST_MODIFIED, Utils.getRFC1123Time(last));
				initLoaderImpl(loaderIndex++, args, this);
			}
			// 差が24H未満の場合
			else {
				lists.get(position).setmIcon(fileName);
				refresh();
			}
		}
		// サムネイルが存在していない場合
		else {
			// 画像を取得する
			Bundle args = new Bundle();
			args.putString(FROAsyncTaskLoader.TAG_THUMB_ID, item.getmThumbIcon());
			args.putString(FROAsyncTaskLoader.TAG_FRG_NAME, getTag());
			args.putInt(FROAsyncTaskLoader.TAG_LIST_POS, position);
			args.putString(FROAsyncTaskLoader.TAG_LIST_TYPE, item.getMode().text);
			args.putString(FROAsyncTaskLoader.TAG_THUMBNAIL_URL, item.getThumnailSmall());
			initLoaderImpl(loaderIndex++, args, this);
		}

	}

	@Override
	public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
		String urlStr = args.getString(FROAsyncTaskLoader.TAG_THUMB_ID);
		if (!TextUtils.isEmpty(urlStr)) {
			if (FROTaskQueue.isAdded(urlStr) == false) {
				FROTaskQueue.addTask(urlStr);
				return new FROAsyncTaskLoader(getActivity(), args);
			}
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Bitmap> loader, Bitmap data) {
		Bundle args = ((FROAsyncTaskLoader) loader).getArgs();
		int position = args.getInt(FROAsyncTaskLoader.TAG_LIST_POS);
		String thumbId = args.getString(FROAsyncTaskLoader.TAG_THUMB_ID);
		String mode = args.getString(FROAsyncTaskLoader.TAG_LIST_TYPE);
		FROTaskQueue.remove(thumbId);
		if (data != null) {
			String fileName = Consts.PRIVATE_PATH_THUMB_LIST + mode + thumbId.replace(".jpg", "").replace(".png", "");
			lists.get(position).setmIcon(fileName);
		} else {
			lists.get(position).setmThumbIcon(null);
		}
		refresh();
	}

	@Override
	public void onLoaderReset(Loader<Bitmap> data) {
	}

	@Override
	public void onBackPress() {
		if (FROForm.getInstance().isMenu(Consts.TAG_MODE_ARTIST, getActivity())) {
			((IModeActivity) getActivity()).showMode(MainActivity.TOP);
		} else {
			((IModeActivity) getActivity()).showMode(MainActivity.OTHER);
		}
	}
}
