package jp.faraopro.play.act;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import jp.faraopro.play.act.newone.BootActivity;
import jp.faraopro.play.app.FROHandler;
import jp.faraopro.play.common.Updater;
import jp.faraopro.play.domain.MainPreference;
import jp.faraopro.play.util.Utils;

public class PackageReceiver extends BroadcastReceiver {
	private long mBootedTime = 0;
	private static final long AUTO_BOOT_TIMEOUT = 60 * 3 * 1000; // 3minute

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		String packagePath = intent.getDataString(); // package:app.package.name
		if (Intent.ACTION_PACKAGE_REPLACED.equals(action)
				&& packagePath.equals("package:" + context.getPackageName())) {
			Updater.deleteApk();
		}
		// 自動起動処理
		else if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
			if (!MainPreference.getInstance(context).isEnableAutoBoot())
				return;

			final Context fcon = context;
			mBootedTime = Utils.getNowTime();
			new Thread(new Runnable() {
				@Override
				public void run() {
					// 端末起動から3分間はネットワークが有効になるまで待機する
					// 3分を超えた場合、ネットワーク状態に関わらずアプリを起動する
					while (!Utils.getNetworkState(fcon)) {
						try {
							Thread.sleep(500);
							long past = Utils.getNowTime() - mBootedTime;
							if (past > AUTO_BOOT_TIMEOUT) {
								break;
							}
						} catch (InterruptedException e) {
							Log.e(PackageReceiver.class.getSimpleName(), e.getMessage());
							break;
						}
					}
					// 既にアプリが起動中でなければアプリを起動する
					if (!Utils.isServiceRunning(fcon, FROHandler.class)) {
						Intent intent = new Intent();
						intent.setClass(fcon, BootActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
						fcon.startActivity(intent);
					}
				}
			}).start();
		}
	}
}
