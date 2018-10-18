package jp.faraopro.play.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BootActivity;

/**
 * Notification 関連のヘルパークラス<br>
 * Notification の表示や非表示、foreground の設定を行う
 *
 * @author AIM
 *
 */
public class NotificationHelper {
	private static final int ICON = R.drawable.icon_notification;
	private static final int REQUEST_CODE = R.string.id_start_service;
	private static final String TICKER_NAME = "Fans' Shop BGM";

	/**
	 * Notification を表示し、引数で与えられた Service を foreground 状態にする
	 *
	 * @param service
	 *            Notification を必要とする Service
	 * @param title
	 *            Notification の1行目の文字列
	 * @param text
	 *            Notification の2行目の文字列
	 * @param onTapAction
	 *            タップ時の動作
	 */
	static void showNotification(Service service, String title, String text, PendingIntent onTapAction) {
		//NotificationCompat.Builder builder = new NotificationCompat.Builder(service).setSmallIcon(ICON)
		//		.setTicker(TICKER_NAME).setWhen(System.currentTimeMillis()).setContentTitle(title).setContentText(text);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(service)
				.setTicker(TICKER_NAME).setWhen(System.currentTimeMillis()).setContentTitle(title).setContentText(text);
		if (onTapAction != null)
			builder.setContentIntent(onTapAction);
		Notification notification = builder.build();
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		NotificationManager manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(REQUEST_CODE, notification);
		service.startForeground(REQUEST_CODE, notification);
	}

	/**
	 * Notification を表示し、引数で与えられた Service を foreground 状態にする<br>
	 * タップ時に処理が必要な場合は
	 * {@link NotificationHelper#showNotification(Service, String, String, PendingIntent)}
	 * を使用すること
	 *
	 * @param service
	 *            Notification を必要とするサービス
	 * @param title
	 *            Notification の1行目の文字列
	 * @param text
	 *            Notification の2行目の文字列
	 */
	static void showNotification(Service service, String title, String text) {
		NotificationHelper.showNotification(service, title, text, null);
	}

	/**
	 * 表示されている Notification を削除し、foreground 状態を解除する
	 *
	 * @param service
	 *            Notification を表示している Service
	 */
	static void removeNotification(Service service) {
		service.stopForeground(true);
	}

	/**
	 * {@link BootActivity} を表示する PendingIntent を作成する
	 *
	 * @param context
	 * @return Class に {@link BootActivity} を指定した PendingIntent
	 */
	public static PendingIntent getPendingIntent(Context context) {
		Intent intent = new Intent(context, BootActivity.class);
		Bundle args = new Bundle();
		args.putInt("SHOW_MODE", -1);
		intent.putExtras(args);
		return PendingIntent.getActivity(context, REQUEST_CODE, intent, PendingIntent.FLAG_ONE_SHOT);
	}
}
