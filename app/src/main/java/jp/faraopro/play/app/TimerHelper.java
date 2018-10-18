package jp.faraopro.play.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.text.TextUtils;

import java.util.Calendar;
import java.util.Random;

import jp.faraopro.play.act.TimerReceiver;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.domain.FROPatternContentDBFactory;
import jp.faraopro.play.domain.FROPatternScheduleDB;
import jp.faraopro.play.domain.FROPatternScheduleDBFactory;
import jp.faraopro.play.domain.FROTemplateTimerAutoListDBFactory;
import jp.faraopro.play.domain.FROTimerAutoDBFactory;
import jp.faraopro.play.domain.FROTimerDBFactory;
import jp.faraopro.play.domain.ITimerDB;
import jp.faraopro.play.domain.MainPreference;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCFrameItem;
import jp.faraopro.play.mclient.MCScheduleItem;
import jp.faraopro.play.mclient.MCTemplateItem;
import jp.faraopro.play.mclient.MCUserInfoPreference;
import jp.faraopro.play.model.TimerInfo;
import jp.faraopro.play.util.SdkCompat;
import jp.faraopro.play.util.Utils;

public class TimerHelper {
	private static final boolean DEBUG = true;
	public static final int PENDING_INTENT_MUSIC_TIMER = 100;
	public static final int PENDING_INTENT_TEMPLATE_TIMER = 101;
	public static final int PENDING_INTENT_INTERRUPT_MUSIC_TIMER = 102;
	public static final int PENDING_INTENT_INTERRUPT_SCHEDULE_TIMER = 103;
	public static final int PENDING_INTENT_UPDATE_CHECK_TIMER = 104;
	public static final int PENDING_INTENT_LICENSE_TIMER = 200;

	public static boolean isSetPending(Context context, int requestCode) {
		Intent intent = new Intent(context, TimerReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE);
		if (sender == null) {
			return false;
		} else {
			return true;
		}
	}

	public static Intent createIntent(Context context, int type) {
		Intent intent = new Intent(context, TimerReceiver.class);
		intent.putExtra(TimerReceiver.INTENT_TAG_TIMER_TYPE, type);
		intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);

		return intent;
	}

	public static void setLicenseIntent(Context context) {
		// Intent intent = new Intent(context, TimerReceiver.class);
		// intent.putExtra(TimerReceiver.INTENT_TAG_TIMER_TYPE,
		// TimerReceiver.TYPE_LICENSE_TIMER);
		PendingIntent sender = PendingIntent.getBroadcast(context, PENDING_INTENT_LICENSE_TIMER,
				createIntent(context, TimerReceiver.TYPE_LICENSE_TIMER), PendingIntent.FLAG_UPDATE_CURRENT);

		Calendar nextTime = Calendar.getInstance();
		int interval = MCUserInfoPreference.getInstance(context).getLicenseInterval();
		nextTime.add(Calendar.SECOND, interval);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); //
		am.set(AlarmManager.RTC, nextTime.getTimeInMillis(), sender); // AlramManagerにPendingIntentを登録
	}

	public static void setMusicTimer(Context context, boolean isExceptNow) {
		// 登録済みのPendingIntentをキャンセルする
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // AlramManager取得
		cancelTimer(context, PENDING_INTENT_MUSIC_TIMER);

		// 現時点から最も近いタイマーを取得する
		ITimerDB database;
		// タイマー種別が固定タイマー
		if (MainPreference.getInstance(context).getMusicTimerType() == Consts.MUSIC_TIMER_TYPE_MANUAL) {
			database = FROTimerDBFactory.getInstance(context);
		}
		// タイマー種別が自動タイマー
		else {
			database = FROTimerAutoDBFactory.getInstance(context);
		}
		Calendar nowTime = Calendar.getInstance();
		nowTime.setTimeInMillis(System.currentTimeMillis()); // 現在時刻を取得
		byte todayWeek = (byte) Math.pow(2, nowTime.get(Calendar.DAY_OF_WEEK) - 1);
		int todayTime = nowTime.get(Calendar.HOUR_OF_DAY) * 60 + nowTime.get(Calendar.MINUTE);
		TimerInfo timerInfo = database.findNext(todayWeek, todayTime, isExceptNow);
		// 今日のタイマーがない場合は次の日を探す
		if (timerInfo == null) {
			for (int i = 1; i < 8; i++) {
				if (todayWeek == Consts.WEEK_BIT_SATURDAY)
					todayWeek = Consts.WEEK_BIT_SUNDAY;
				else
					todayWeek = (byte) (todayWeek << 1);
				timerInfo = database.findNext(todayWeek, -1, false);
				if (timerInfo != null)
					break;
			}
		}
		if (timerInfo == null) {
			FRODebug.logD(TimerHelper.class, "next music timer is null", DEBUG);
			return;
		}

		// インテントを作成する
		Intent intent = createIntent(context, TimerReceiver.TYPE_MUSIC_TIMER);
		// タイマーデータをインテントに格納
		Parcel parcel = Parcel.obtain();
		timerInfo.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);
		intent.putExtra(TimerReceiver.INTENT_TAG_TIMER, parcel.marshall());

		// PendingIntent sender = PendingIntent.getBroadcast(context,
		// PENDING_INTENT_MUSIC_TIMER, intent,
		// PendingIntent.FLAG_UPDATE_CURRENT); // ブロードキャストを投げるPendingIntentの作成

		Calendar time = Calendar.getInstance();
		time.set(nowTime.get(Calendar.YEAR), nowTime.get(Calendar.MONTH), nowTime.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		// 時刻を設定する
		time.add(Calendar.MINUTE, timerInfo.getTime());
		// 曜日が本日でなかった場合
		if (timerInfo.getNearestDay() > 0) {
			// 日数分時間を延ばす
			time.add(Calendar.DAY_OF_MONTH, timerInfo.getNearestDay());
		}
		PendingIntent sender = PendingIntent.getBroadcast(context, PENDING_INTENT_MUSIC_TIMER, intent,
				PendingIntent.FLAG_UPDATE_CURRENT); // ブロードキャストを投げるPendingIntentの作成
		SdkCompat.setExact(am, AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), sender); // AlramManagerにPendingIntentを登録

		if (time != null) {
			FRODebug.logD(TimerHelper.class, "next music timer " + Utils.getDateString(time, "yyyy/MM/dd kk:mm"),
					DEBUG);
		}

		parcel.recycle();
	}

	public static void setMusicTimer(Context context) {
		// setMusicTimer(context, true);
		setMusicTimer(context, false);
	}

	public static void restoreLastMusicTimer(Context context) {
		// 再生が行われていなければ最後に再生する予定だったタイマーを動かす
		int playerState = FROForm.getInstance().checkPlayerInstance();
		boolean enable = MainPreference.getInstance(context).getMusicTimerEnable()
				&& MainPreference.getInstance(context).getRestoreMusicTimerEnable();
		// プレイヤーインスタンスが存在している(play 状態 or pause 状態)場合は実行しない
		// タイマー機能、復元機能自体が OFF の場合も実行しない
		if (playerState == Consts.PLAYER_STATUS_NOINSTANCE && enable) {
			TimerInfo lastTime = TimerHelper.getLastMusicTimer(context);
			if (lastTime != null) {
				int timerType = lastTime.getType();
				if (!(timerType == Consts.MUSIC_TYPE_STOP)) {
					Parcel parcel = Parcel.obtain();
					lastTime.writeToParcel(parcel, 0);
					parcel.setDataPosition(0);
					Intent intent = new Intent(context, FROHandler.class);
					intent.putExtra(TimerReceiver.INTENT_TAG_TIMER, parcel.marshall());
					intent.putExtra(TimerReceiver.INTENT_TAG_TIMER_TYPE, TimerReceiver.TYPE_MUSIC_TIMER);
					if (lastTime.getType() == Consts.MUSIC_TYPE_NORMAL) {
						FROForm.getInstance().setPlaylistParams(lastTime.getMode(), lastTime.getChannelId(),
								lastTime.getRange(), lastTime.getPermission());
					}
					// showProgress(0);
					context.startService(intent);
					parcel.recycle();
				}
			}
		}
	}

	/**
	 * テンプレート更新用タイマーをセットする<br>
	 * {@link FROTemplateTimerAutoListDB} から取得出来る templateRule
	 * と現在時刻とを比較し、翌日か当日かを判断する<br>
	 * この関数はテンプレート適用時のみ呼び出してください
	 *
	 * @param context
	 *            コンテキスト
	 */
	public static void setTemplateTimerFirst(Context context) {
		setTemplateTimer(context, null);
	}

	/**
	 * テンプレート更新用タイマーをセットする<br>
	 * {@link FROTemplateTimerAutoListDB} から取得出来る templateRule
	 * だけを用いて、翌日か当日かを判断する<br>
	 * この関数はテンプレート情報に変更がない状態で更新用タイマーをセットする際に用いてください
	 *
	 * @param context
	 *            コンテキスト
	 */
	public static void setTemplateTimer(Context context) {
		MCTemplateItem item = FROTemplateTimerAutoListDBFactory.getInstance(context).find();
		setTemplateTimer(context, item);
	}

	/**
	 * テンプレート更新用タイマーをセットする<br>
	 * {@link FROTemplateTimerAutoListDB} から取得出来る templateRule と更新前の
	 * templateRule とを比較し、翌日か当日かを判断する<br>
	 * この関数はテンプレート情報に変更があった状態で更新用タイマーをセットする際に用いてください
	 *
	 * @param context
	 *            コンテキスト
	 * @param old
	 *            更新前のテンプレート情報
	 */
	public static void setTemplateTimer(Context context, MCTemplateItem old) {
		// DB からテンプレート情報を取得
		MCTemplateItem item = FROTemplateTimerAutoListDBFactory.getInstance(context).find();
		if (item == null)
			return;

		// 現在時刻を取得
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		// 次回更新時刻を取得
		String[] nextDate = item.getString(MCDefResult.MCR_KIND_TEMPLATE_RULE).split(":");
		int nextHour = Integer.parseInt(nextDate[0]);
		int nextMinute = Integer.parseInt(nextDate[1]);
		Random r = new Random();
		// 分の単位には ±3 の乱数を加える
		nextMinute += r.nextInt(7) - 3;
		// 単位を min にして1つの数値にする
		int nextTimeInMinute = nextHour * 60 + nextMinute;

		int comparison; // 計算した次回更新時刻と比較する時間(現在時刻 or 前回の更新時間 + 3)
		if (old != null) {
			String[] oldDate = old.getString(MCDefResult.MCR_KIND_TEMPLATE_RULE).split(":");
			comparison = Integer.parseInt(oldDate[0]) * 60 + Integer.parseInt(oldDate[1]) + 3;
		} else {
			// 現在時刻を取得
			String currentTime = Utils.getNowDateString("kk:mm");
			String[] currentDate = currentTime.split(":");
			// 単位を min にして1つの数値にする
			comparison = Integer.parseInt(currentDate[0]) * 60 + Integer.parseInt(currentDate[1]);
		}

		// 次回更新時刻 > 比較対象の時刻 --> 当日
		// 次回更新時刻 <= 比較対象の時刻 --> 翌日
		if (nextTimeInMinute <= comparison)
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		// 時分を DB から取得した値にする
		calendar.set(Calendar.HOUR_OF_DAY, nextHour);
		calendar.set(Calendar.MINUTE, nextMinute);
		calendar.set(Calendar.SECOND, 0);
		setTemplateTimer(context, calendar.getTimeInMillis());
		MainPreference.getInstance(context).setTemplateLastUpdate(calendar.getTimeInMillis());
	}

	/**
	 * アプリ終了前に設定していたテンプレート更新用タイマーを復元する
	 *
	 * @param context
	 *            コンテキスト
	 */
	public static void restoreTemplateTimer(Context context) {
		setTemplateTimer(context, MainPreference.getInstance(context).getTemplateLastUpdate());
	}

	private static void setTemplateTimer(Context context, long time) {
		// 既に登録されているタイマーをキャンセルする
		cancelTimer(context, PENDING_INTENT_TEMPLATE_TIMER);
		PendingIntent sender = PendingIntent.getBroadcast(context, PENDING_INTENT_TEMPLATE_TIMER,
				createIntent(context, TimerReceiver.TYPE_TEMPLATE_TIMER), PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC, time, sender);

		if (DEBUG) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(time);
			FRODebug.logD(TimerHelper.class,
					"set template timer : " + Utils.getDateString(calendar, "yyyy/MM/dd kk:mm:ss"), DEBUG);
		}
	}

	/**
	 * 自動テンプレートの更新状態を確認し、更新が必要な場合は実施する
	 *
	 * @param context
	 *            コンテキスト
	 * @return true:更新が必要な場合、false:それ以外の場合
	 */
	public static boolean checkTemplateTimer(Context context) {
		FRODebug.logD(TimerHelper.class, "now = " + Utils.getNowTime() + ",last = "
				+ MainPreference.getInstance(context).getTemplateLastUpdate(), DEBUG);
		if (MainPreference.getInstance(context).getMusicTimerType() == Consts.MUSIC_TIMER_TYPE_AUTO
				&& Utils.getNowTime() >= MainPreference.getInstance(context).getTemplateLastUpdate()) {
			Intent intent = new Intent(context, FROHandler.class);
			intent.putExtra(TimerReceiver.INTENT_TAG_TIMER_TYPE, TimerReceiver.TYPE_TEMPLATE_TIMER);
			context.startService(intent);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 日付と時間を指定して次のスケジュールタイマーをセットする
	 * 
	 * @param context
	 *            コンテキスト
	 * @param date
	 *            指定する日付、時間
	 * @return セットできなかった場合は nullを返す, それ以外は次のスケジュールの日付、時間
	 */
	public static Calendar setInterruptScheduleTimer(Context context, Calendar date) {
		if (date == null || context == null)
			return null;

		// 既に登録されているタイマーをキャンセルする
		cancelTimer(context, PENDING_INTENT_INTERRUPT_SCHEDULE_TIMER);

		Random r = new Random();
		int random3Minute = r.nextInt(7) - 3;
		date.add(Calendar.MINUTE, random3Minute);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(context, PENDING_INTENT_INTERRUPT_SCHEDULE_TIMER,
				createIntent(context, TimerReceiver.TYPE_INTERRUPT_SCHEDULE_TIMER), PendingIntent.FLAG_UPDATE_CURRENT);
		am.set(AlarmManager.RTC, date.getTimeInMillis(), sender);
		// 次回更新時刻をプリファレンスにセット
		MainPreference.getInstance(context).setNextPatternUpdate(Utils.getDateString(date, "yyyy/MM/dd kk:mm"));

		return date;
	}

	/**
	 * 引数で与えられた日付に該当するスケジュールを DB から取得し、次のスケジュールタイマーをセットする
	 * 
	 * @param context
	 *            コンテキスト
	 * @param date
	 *            DB に指定する日付
	 * @return 該当日のスケジュールが DB に存在しない場合は nullを返す, それ以外は次のスケジュールの日付、時間
	 */
	public static Calendar setInterruptScheduleTimer(Context context, String date) {
		// 既に登録されているタイマーをキャンセルする
		cancelTimer(context, PENDING_INTENT_INTERRUPT_SCHEDULE_TIMER);

		Calendar nextTime = null;
		// DB から該当日のスケジュールを取得する
		MCScheduleItem item = FROPatternScheduleDBFactory.getInstance(context).findByDate(date);
		// スケジュールが存在すれば、rule と period から次の更新時刻を算出する
		if (item != null) {
			nextTime = calcNextTime(item.getString(MCDefResult.MCR_KIND_SCHEDULE_RULE),
					item.getString(MCDefResult.MCR_KIND_SCHEDULE_PERIOD));
			AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			PendingIntent sender = PendingIntent.getBroadcast(context, PENDING_INTENT_INTERRUPT_SCHEDULE_TIMER,
					createIntent(context, TimerReceiver.TYPE_INTERRUPT_SCHEDULE_TIMER),
					PendingIntent.FLAG_UPDATE_CURRENT);
			am.set(AlarmManager.RTC, nextTime.getTimeInMillis(), sender);
			// 次回更新時刻をプリファレンスにセット
			MainPreference.getInstance(context)
					.setNextPatternUpdate(Utils.getDateString(nextTime, "yyyy/MM/dd E kk:mm"));
		}

		return nextTime;
	}

	/**
	 * 今日のスケジュールを DB から取得し、次のスケジュールタイマーをセットする
	 * 
	 * @param context
	 *            コンテキスト
	 * @return 今日のスケジュールが DB に存在しない場合は nullを返す, それ以外は次のスケジュールの日付、時間
	 */
	public static Calendar setInterruptScheduleTimer(Context context) {
		return setInterruptScheduleTimer(context, Utils.getNowDateString("yyyyMMdd"));
	}

	private static Calendar calcNextTime(String sRule, String sPeriod) {
		String sCurrent = Utils.getNowDateString("kk:mm");
		String[] aryRule = sRule.split(":");
		int ruleHour = Integer.parseInt(aryRule[0]);
		int ruleMinute = Integer.parseInt(aryRule[1]);
		String[] aryCurrent = sCurrent.split(":");
		int currentHour = Integer.parseInt(aryCurrent[0]);
		int currentMinute = Integer.parseInt(aryCurrent[1]);
		double period = Double.parseDouble(sPeriod);

		int rule = ruleHour * 60 + ruleMinute;
		int current = currentHour * 60 + currentMinute;

		Calendar next = Calendar.getInstance();
		next.set(Calendar.HOUR_OF_DAY, ruleHour);
		next.set(Calendar.MINUTE, ruleMinute);
		double diff = current - rule;
		Random r = new Random();
		// 分の単位には ±3 の乱数を加えるため、あらかじめ計算しておく
		int random3Minute = r.nextInt(7) - 3;
		if (period == 0) {
			if (diff >= 0)
				next.add(Calendar.DAY_OF_MONTH, 1);
			next.add(Calendar.MINUTE, random3Minute);
		} else {
			int mult = 0;
			if ((diff % period != 0) && (diff < 0)) {
				mult = (int) (Math.floor(diff / period) + 1);
			} else {
				mult = (int) ((diff / period) + 1);
			}
			int addValue = (int) (mult * period + random3Minute);
			next.add(Calendar.MINUTE, addValue);
			// 実際には不要
			Calendar currentCal = Calendar.getInstance();
			currentCal.set(Calendar.HOUR_OF_DAY, currentHour);
			currentCal.set(Calendar.MINUTE, currentMinute + 3);
			while (next.getTimeInMillis() <= currentCal.getTimeInMillis()) {
				next.add(Calendar.MINUTE, (int) period);
			}
		}

		return next;
	}

	public static MCFrameItem setInterruptMusicTimer(Context context, Calendar date) {
		FRODebug.logD(TimerHelper.class, "setInterruptMusicTimer", DEBUG);
		FRODebug.logD(TimerHelper.class, "date = " + Utils.getDateString(date, "yyyy/MM/dd E kk:mm"), DEBUG);
		// 既に登録されているタイマーをキャンセルする
		cancelTimer(context, PENDING_INTENT_INTERRUPT_MUSIC_TIMER);

		// 今日の日付に該当するパターン ID を取得
		String strDate = Utils.getDateString(date, "yyyyMMdd");
		FROPatternScheduleDB scheduleDb = FROPatternScheduleDBFactory.getInstance(context);
		MCScheduleItem schedule = scheduleDb.findByDate(strDate);
		String patternId = (schedule != null) ? schedule.getString(MCDefResult.MCR_KIND_PATTERN_ID) : null;
		FRODebug.logD(TimerHelper.class, "patternId = " + patternId, DEBUG);
		// パターン ID が無い場合は何もしない
		if (!TextUtils.isEmpty(patternId)) {
			// DB から割り込み情報を取得する
			MCFrameItem item = FROPatternContentDBFactory.getInstance(context).findSpecificDateFrame(patternId,
					Utils.getDateString(date, "kk:mm"));
			// 再生フレームが存在しない場合は何もしない
			FRODebug.logD(TimerHelper.class, "frame = " + item, DEBUG);
			if (item != null) {
				Calendar nextTime = Calendar.getInstance();
				nextTime.setTimeInMillis(date.getTimeInMillis());
				String[] onairTime = item.getString(MCDefResult.MCR_KIND_ONAIR_TIME).split(":");
				nextTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(onairTime[0]));
				nextTime.set(Calendar.MINUTE, Integer.parseInt(onairTime[1]));
				nextTime.set(Calendar.SECOND, 0);
				Intent intent = createIntent(context, TimerReceiver.TYPE_INTERRUPT_MUSIC_TIMER);
				intent.putExtra(TimerReceiver.INTENT_TAG_PATTERN_ID, item.getmPatternId());
				intent.putExtra(TimerReceiver.INTENT_TAG_ONAIR_TIME, item.getString(MCDefResult.MCR_KIND_ONAIR_TIME));
				PendingIntent sender = PendingIntent.getBroadcast(context, PENDING_INTENT_INTERRUPT_MUSIC_TIMER, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				// am.setExact(AlarmManager.RTC, nextTime.getTimeInMillis(),
				// sender);
				SdkCompat.setExact(am, AlarmManager.RTC, nextTime.getTimeInMillis(), sender);
				FRODebug.logD(TimerHelper.class, "set next interrupt timer", DEBUG);
				FRODebug.logD(TimerHelper.class, "frameId = " + item.getString(MCDefResult.MCR_KIND_FRAME_ID), DEBUG);
				FRODebug.logD(TimerHelper.class, "time = " + Utils.getDateString(nextTime, "yyyy/MM/dd E kk:mm"),
						DEBUG);
				return item;
			}
		}
		return null;
	}

	public static void setUpdateCheckTimer(Context context) {
		cancelTimer(context, PENDING_INTENT_UPDATE_CHECK_TIMER); // 既に登録されているタイマーをキャンセルする
		Calendar next = Calendar.getInstance();
		next.add(Calendar.HOUR_OF_DAY, 24);
		PendingIntent sender = PendingIntent.getBroadcast(context, PENDING_INTENT_UPDATE_CHECK_TIMER,
				createIntent(context, TimerReceiver.TYPE_UPDATE_CHECK_TIMER), PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC, next.getTimeInMillis(), sender);
	}

	public static final int MINUTE_OF_DAY = 1440;

	public static TimerInfo getLastMusicTimer(Context context) {
		ITimerDB database;
		// タイマー種別が固定タイマー
		if (MainPreference.getInstance(context).getMusicTimerType() == Consts.MUSIC_TIMER_TYPE_MANUAL) {
			database = FROTimerDBFactory.getInstance(context);
		}
		// タイマー種別が自動タイマー
		else {
			database = FROTimerAutoDBFactory.getInstance(context);
		}
		Calendar nowTime = Calendar.getInstance();
		nowTime.setTimeInMillis(System.currentTimeMillis()); // 現在時刻を取得
		byte todayWeek = (byte) Math.pow(2, nowTime.get(Calendar.DAY_OF_WEEK) - 1);
		int todayTime = nowTime.get(Calendar.HOUR_OF_DAY) * 60 + nowTime.get(Calendar.MINUTE);
		TimerInfo timerInfo = database.findPrevious(todayWeek, todayTime);
		// 今日のタイマーがない場合は前の日を探す
		if (timerInfo == null) {
			for (int i = 1; i < 8; i++) {
				if (todayWeek == Consts.WEEK_BIT_SUNDAY)
					todayWeek = Consts.WEEK_BIT_SATURDAY;
				else
					todayWeek = (byte) (todayWeek >> 1);
				timerInfo = database.findPrevious(todayWeek, MINUTE_OF_DAY);
				if (timerInfo != null)
					break;
			}
		}

		return timerInfo;
	}

	public static void cancelTimer(Context context, int type) {
		FRODebug.logD(TimerHelper.class, "cancelTimer : " + type, DEBUG);
		// 登録済みのPendingIntentをキャンセルする
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		if (isSetPending(context, type)) {
			Intent intent = new Intent(context, TimerReceiver.class);
			PendingIntent sender = PendingIntent.getBroadcast(context, type, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			am.cancel(sender);
			sender.cancel();
		}
	}
}
