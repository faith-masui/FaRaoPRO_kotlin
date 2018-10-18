package jp.faraopro.play.act;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;

import java.util.Calendar;

import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.app.FROHandler;
import jp.faraopro.play.app.TimerHelper;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.domain.FROPatternScheduleDBFactory;
import jp.faraopro.play.domain.MainPreference;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCScheduleItem;
import jp.faraopro.play.model.TimerInfo;
import jp.faraopro.play.util.FROUtils;
import jp.faraopro.play.util.Utils;

public class TimerReceiver extends BroadcastReceiver {
	/*** const ***/
	private static final boolean DEBUG = true;
	// TimerReceiver を呼び出す Intent には必ず以下のタイマー種別を設定する
	public static final String INTENT_TAG_TIMER_TYPE = "TIMER_TYPE";
	public static final int TYPE_MUSIC_TIMER = 1; // 楽曲再生タイマー
	public static final int TYPE_LICENSE_TIMER = 2; // ライセンスチェックタイマー
	public static final int TYPE_TEMPLATE_TIMER = 3; // テンプレート更新タイマー
	public static final int TYPE_INTERRUPT_SCHEDULE_TIMER = 4;
	public static final int TYPE_INTERRUPT_MUSIC_TIMER = 5;
	public static final int TYPE_UPDATE_CHECK_TIMER = 6;
	// 各タイマーの追加情報
	public static final String INTENT_TAG_TIMER = "TIMER_INFO";
	public static final String INTENT_TAG_LICENSE_TYPE = "LICENSE_CHECK_TYPE";
	// public static final String INTENT_TAG_FRAME_ID = "FRAME_ID";
	public static final String INTENT_TAG_PATTERN_ID = "PATTERN_ID";
	public static final String INTENT_TAG_ONAIR_TIME = "ONAIR_TIME";
	public static final int LICENSE_CHECK_TYPE_IF_POSSIBLE = 1;
	public static final int LICENSE_CHECK_TYPE_FORCE = 2;

	public static String getTag(int type) {
		String tag = "UNKNOWN";
		switch (type) {
		case TYPE_MUSIC_TIMER:
			tag = "TYPE_MUSIC_TIMER";
			break;
		case TYPE_LICENSE_TIMER:
			tag = "TYPE_LICENSE_TIMER";
			break;
		case TYPE_TEMPLATE_TIMER:
			tag = "TYPE_TEMPLATE_TIMER";
			break;
		case TYPE_INTERRUPT_SCHEDULE_TIMER:
			tag = "TYPE_INTERRUPT_SCHEDULE_TIMER";
			break;
		case TYPE_INTERRUPT_MUSIC_TIMER:
			tag = "TYPE_INTERRUPT_MUSIC_TIMER";
			break;
		}
		return tag;
	}

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Bundle bundle = arg1.getExtras();
		if (bundle == null)
			return;

		Intent intent = null;
		int timerType = bundle.getInt(INTENT_TAG_TIMER_TYPE, -1);
		FRODebug.logD(getClass(), "timerType " + getTag(timerType), true);
		switch (timerType) {
		case TYPE_MUSIC_TIMER:
			// 次のタイマーの設定
			TimerHelper.setMusicTimer(arg0, false);
			// タイマー設定が OFF の場合 or Service が起動していない場合、以降は処理しない
			if (!MainPreference.getInstance(arg0).getMusicTimerEnable() && FROUtils.isFROHandlerRunning(arg0))
				break;

			// タイマー情報の取得
			byte[] byteArrayExtra = arg1.getByteArrayExtra(INTENT_TAG_TIMER);
			Parcel parcel = Parcel.obtain();
			parcel.unmarshall(byteArrayExtra, 0, byteArrayExtra.length);
			parcel.setDataPosition(0);
			TimerInfo timerInfo = TimerInfo.CREATOR.createFromParcel(parcel);
			// intent にタイマーの情報をセットし startService で Service に受け渡す
			intent = new Intent(arg0, FROHandler.class);
			timerInfo.writeToParcel(parcel, 0);
			parcel.setDataPosition(0);
			intent.putExtra(INTENT_TAG_TIMER, parcel.marshall());
			intent.putExtra(INTENT_TAG_TIMER_TYPE, TYPE_MUSIC_TIMER);
			if (timerInfo.getType() == Consts.MUSIC_TYPE_NORMAL) {
				FROForm.getInstance().setPlaylistParams(timerInfo.getMode(), timerInfo.getChannelId(),
						timerInfo.getRange(), timerInfo.getPermission());
			}
			arg0.startService(intent);
			parcel.recycle();
			break;
		case TYPE_LICENSE_TIMER:
			// Serviceとの接続を確立する
			intent = new Intent(arg0, FROHandler.class);
			int force = 1;
			// システムによってKillされていない場合
			if (FROForm.getInstance().getContext() != null) {
				force = 2;
			}
			intent.putExtra(INTENT_TAG_TIMER_TYPE, TYPE_LICENSE_TIMER);
			intent.putExtra(INTENT_TAG_LICENSE_TYPE, force);
			arg0.startService(intent);
			break;
		case TYPE_TEMPLATE_TIMER:
			// Service クラスが稼動中であれば更新要求を送る
			if (FROUtils.isFROHandlerRunning(arg0)) {
				intent = new Intent(arg0, FROHandler.class);
				intent.putExtras(bundle);
				arg0.startService(intent);
			}
			// 稼動中でなければタイマーを再度セットして終了
			else {
				TimerHelper.setTemplateTimer(arg0);
			}
			break;
		case TYPE_INTERRUPT_SCHEDULE_TIMER:
			// Service クラスが稼動中であれば更新要求を送る
			if (FROUtils.isFROHandlerRunning(arg0)) {
				intent = new Intent(arg0, FROHandler.class);
				intent.putExtras(bundle);
				arg0.startService(intent);
			}
			break;
		case TYPE_INTERRUPT_MUSIC_TIMER:
			// Service クラスが稼動中であれば更新要求を送る
			MCScheduleItem schedule = FROPatternScheduleDBFactory.getInstance(arg0)
					.findByDate(Utils.getNowDateString("yyyyMMdd"));
			String scheduleMask = null;
			if (schedule != null)
				scheduleMask = schedule.getString(MCDefResult.MCR_KIND_SCHEDULE_MASK);
			// Service クラスが稼動中で、scheduleMask = 1(禁止)となっている場合、または割り込み再生が ON
			// になっている場合は再生する
			boolean enableInterrupt = (scheduleMask != null && scheduleMask.equalsIgnoreCase("1"))
					|| MainPreference.getInstance(arg0).getInterruptTimerEnable();
			if (FROUtils.isFROHandlerRunning(arg0) && enableInterrupt) {
				intent = new Intent(arg0, FROHandler.class);
				intent.putExtras(bundle);
				arg0.startService(intent);
			}
			// そうでなければ次のタイマーをセットする
			else {
				Calendar current = Calendar.getInstance();
				current.add(Calendar.MINUTE, 1);
				if (TimerHelper.setInterruptMusicTimer(arg0, current) == null) {
					Calendar tomorrow = Calendar.getInstance();
					tomorrow.add(Calendar.DAY_OF_MONTH, 1);
					tomorrow.set(Calendar.HOUR_OF_DAY, 0);
					tomorrow.set(Calendar.MINUTE, 0);
					TimerHelper.setInterruptMusicTimer(arg0, tomorrow);
				}
			}
			break;
		}
	}
}
