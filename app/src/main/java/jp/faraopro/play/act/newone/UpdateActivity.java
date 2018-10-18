package jp.faraopro.play.act.newone;

import java.io.File;
import java.net.HttpURLConnection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;
import jp.faraopro.play.common.Updater;
import jp.faraopro.play.frg.newone.DownloadDialogFragment;
import jp.faraopro.play.frg.newone.SimpleDialogFragment;
import jp.faraopro.play.util.Utils;

public class UpdateActivity extends BaseActivity {
	private static final int DIALOG_ACTION_START_DOWNLAOD = 100;
	private static final int DIALOG_ACTION_CANCEL_DOWNLAOD = 101;
	private static final int DIALOG_ACTION_BOOT_INSTALLER = 102;
	private static final String DIALOG_TAG_PROGRESS = "DOWNLOAD_PROGRESS_DIALOG";
	private static final String DIALOG_TAG_DOWNLOAD_CONFIRM = "DOWNLOAD_CONFIRM_DIALOG";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		showDownloadDialog();
	}

	@Override
	public void onStart() {
		super.onStartWithoutAttach();
	}

	private void showDownloadDialog() {
		Intent intent = getIntent();
		Bundle args = intent.getExtras();
		if (args != null && args.getBoolean("APP_UPDATE")) {
			SimpleDialogFragment dialog = new SimpleDialogFragment();
			Bundle args2 = new Bundle();
			args2.putInt(SimpleDialogFragment.BUTTON_TYPE, SimpleDialogFragment.TYPE_POSITIVE);
			args2.putInt(SimpleDialogFragment.BUTTON_ID_POS, DIALOG_ACTION_START_DOWNLAOD);
			args2.putInt(SimpleDialogFragment.CANCEL_ID, DIALOG_ACTION_CANCEL_DOWNLAOD);
			args2.putString(SimpleDialogFragment.TITLE, "アップデート");
			args2.putString(SimpleDialogFragment.MESSAGE, "最新のアプリケーションをダウンロードします");
			dialog.setArguments(args2);
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.add(dialog, DIALOG_TAG_DOWNLOAD_CONFIRM);
			transaction.commitAllowingStateLoss();
		}
	}

	int total = 0;

	@Override
	public void onBtnClicked(int resId) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		switch (resId) {
		case DIALOG_ACTION_START_DOWNLAOD:
			DownloadDialogFragment progress = new DownloadDialogFragment();
			progress.setCancelable(false);
			transaction.add(progress, DIALOG_TAG_PROGRESS);
			transaction.commit();
			getSupportFragmentManager().executePendingTransactions();
			progress.progress(0);
			total = 0;
			Updater.getInstance().downloadApk(new UpdateListener(), new Updater.IProgressListener() {
				@Override
				public void onProgress(int currentSize, int totalSize) {
					total += currentSize;
					DownloadDialogFragment dldf = (DownloadDialogFragment) getSupportFragmentManager()
							.findFragmentByTag(DIALOG_TAG_PROGRESS);
					if (dldf != null) {
						dldf.progress(total * 100 / totalSize);
					}
				}
			});
			break;
		case DIALOG_ACTION_CANCEL_DOWNLAOD:
			Fragment confirm = getSupportFragmentManager().findFragmentByTag(DIALOG_TAG_DOWNLOAD_CONFIRM);
			if (confirm != null) {
				transaction.remove(confirm);
				transaction.commitAllowingStateLoss();
				getSupportFragmentManager().executePendingTransactions();
			}
			finish();
			break;
		case DIALOG_ACTION_BOOT_INSTALLER:
			bootInstaller();
			finish();
			break;
		default:
			super.onBtnClicked(resId);
		}
	}

	public class UpdateListener implements Updater.IUpdaterListener {
		@Override
		public void onUpdate(boolean available) {
		}

		@Override
		public void onDownload(final int statusCode) {
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					dismissDownloadDialog();
					if (statusCode == HttpURLConnection.HTTP_OK) {
						// bootInstaller();
						showPositiveDialog("アップデート", "インストーラーを起動します。更新後は一度アプリケーションが終了しますので、ランチャーから再度アプリケーションを起動してください。",
								DIALOG_ACTION_BOOT_INSTALLER, false);
						getSupportFragmentManager().executePendingTransactions();
					} else {
						Toast.makeText(getApplicationContext(), "ダウンロードに失敗しました。", Toast.LENGTH_SHORT).show();
						File file = new File(Updater.getFileDir());
						if (file != null && file.exists())
							Utils.deleteFile(file);
						finish();
					}
				}
			});
		}
	}

	private void dismissDownloadDialog() {
		Fragment progress = getSupportFragmentManager().findFragmentByTag(DIALOG_TAG_PROGRESS);
		if (progress != null) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.remove(progress);
			transaction.commitAllowingStateLoss();
		}
	}

	private void bootInstaller() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		File file = new File(Utils.getExternalDirectory() + "/" + Updater.TEMPORARY_FILE_NAME);
		Uri dataUri = Uri.fromFile(file);
		intent.setDataAndType(dataUri, "application/vnd.android.package-archive");
		// startActivity(intent);
		startActivity(intent);
	}
}
