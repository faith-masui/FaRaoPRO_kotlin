package jp.faraopro.play.frg.mode;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.domain.FROAsyncTaskLoader;
import jp.faraopro.play.domain.FROTaskQueue;
import jp.faraopro.play.frg.newone.SimpleDialogFragment;
import jp.faraopro.play.model.ChannelMode;
import jp.faraopro.play.util.Utils;
import jp.faraopro.play.view.CustomListItem;

public class BenifitFragment extends ListBaseFragment implements LoaderManager.LoaderCallbacks<Bitmap> {

	/** const **/
	private static final boolean DEBUG = true;

	/** bundle key **/

	/** view **/
	// private TextView txtIntro;

	/** member **/
	private ArrayList<CustomListItem> benifitList;

	@Override
	protected void initViews(View view) {
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footer = inflater.inflate(R.layout.new_ele_footer_intro, null);
		footer.setEnabled(false);
		footer.setClickable(false);
		super.initViews(view);
	}

	@Override
	public void onStart() {
		super.onStart();

		if (FROForm.getInstance().benifitList != null)
			show();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void show() {
		((BaseActivity) getActivity()).dismissProgress();
        if (FROForm.getInstance().benifitList == null) {
            return;
        }
		benifitList = (ArrayList<CustomListItem>) FROForm.getInstance().benifitList.clone();
		setContents(benifitList);
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

				CustomListItem item;
				String channel;
				String mode;
				if (arg2 >= benifitList.size())
					return;

				item = benifitList.get(arg2);
				channel = Integer.toString(item.getId());
                mode = item.getMode().text;
				if (mode == null)
                    mode = ChannelMode.BENIFIT.text;
                ((MainActivity) getActivity()).bootPlayer(item.getMode(), Integer.parseInt(channel), null);
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
				// ファイルを削除してから画像を取得する
				// file.delete();
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
				benifitList.get(position).setmIcon(fileName);
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
		String mode = args.getString(FROAsyncTaskLoader.TAG_LIST_TYPE);
		String thumbId = args.getString(FROAsyncTaskLoader.TAG_THUMB_ID);
		FROTaskQueue.remove(thumbId);
		if (data != null) {
			String fileName = Consts.PRIVATE_PATH_THUMB_LIST + mode + thumbId.replace(".jpg", "").replace(".png", "");
			benifitList.get(position).setmIcon(fileName);
		} else {
			benifitList.get(position).setmThumbIcon(null);
		}
		refresh();
	}

	@Override
	public void onLoaderReset(Loader<Bitmap> data) {
	}

	@Override
	public void onListItemClicked(int resId, int position) {
		super.onListItemClicked(resId, position);

		switch (resId) {
		case R.id.imagelist_img_thumb:
			String info = null;
			String title = null;
			info = benifitList.get(position).getmContentText();
			title = benifitList.get(position).getmContentTitle();
			if (!TextUtils.isEmpty(info)) {
				SimpleDialogFragment dialog = new SimpleDialogFragment();
				Bundle args = new Bundle();
				args.putInt(SimpleDialogFragment.BUTTON_TYPE, SimpleDialogFragment.TYPE_POSITIVE);
				args.putString(SimpleDialogFragment.TITLE, title);
				args.putString(SimpleDialogFragment.MESSAGE, info);
				dialog.setArguments(args);
				dialog.setCancelable(true);
				FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
				transaction.add(dialog, BaseActivity.TAG_DIALOG_POS);
				transaction.commitAllowingStateLoss();
			}
			break;
		}
	}

	@Override
	public void onBackPress() {
		if (FROForm.getInstance().isMenu(Consts.TAG_MODE_SPECIAL, getActivity())) {
			((MainActivity) getActivity()).showMode(MainActivity.TOP);
		} else {
			((MainActivity) getActivity()).showMode(MainActivity.OTHER);
		}
	}
}
