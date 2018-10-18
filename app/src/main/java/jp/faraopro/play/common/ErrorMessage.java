package jp.faraopro.play.common;

import android.content.Context;
import jp.faraopro.play.R;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCError;

public class ErrorMessage {
	private int when;
	private int code;

	public ErrorMessage(int when, int code) {
		this.when = when;
		this.code = code;
	}

	public int getWhen() {
		return this.when;
	}

	public int getCode() {
		return this.code;
	}

	public String getMessage(Context context) {
		String msg = context.getString(R.string.msg_system_error) + "(" + code + ")";
		switch (code) {
		case MCError.MC_UNAUTHORIZED:
			switch (when) {
			case Consts.START_STREAM_MUSIC:
				msg = "UNAUTHORIZED";
				break;
			default:
				msg = context.getString(R.string.msg_incorrect_email_or_password_pro);
			}
			return msg;
		case MCError.MC_NOT_FOUND:
			switch (when) {
			case Consts.START_STREAM_MUSIC:
				msg = "NOT_FOUND";
				return msg;
			default:
			}
			break;
		case MCError.MC_FORBIDDEN:
			msg = context.getString(R.string.msg_wrong_session);
			return msg;
		case MCError.MC_INTERNAL_SERVER_ERROR:
		case MCError.MC_BAD_GATEWAY:
		case MCError.MC_SERVICE_UNAVAILABLE:
			msg = context.getString(R.string.msg_server_error);
			return msg;
		case MCError.MC_APPERR_IO_HTTP:
			msg = context.getString(R.string.msg_network_error);
			return msg;
		case MCError.MC_APPERR_UNSUPPORTED_ENC:
		case MCError.MC_APPERR_CLIENT_PROTOCOL:
		case MCError.MC_APPERR_FILE:
		case Consts.MEDIA_PLAYER_ERROR:
			msg = context.getString(R.string.msg_music_save_error);
			return msg;
		case Consts.STATUS_NO_DATA:
			msg = context.getString(R.string.msg_nodata);
			return msg;
		case Consts.STATUS_NO_REMAIN_TIMES:
			msg = context.getString(R.string.msg_remaining_time_none);
			return msg;
		// case Consts.BILLING_ISNT_COMPLETE:
		// msg = context.getString(R.string.msg_unfinished_billing);
		// return msg;
		}
		// エラーコードでなく、エラーメッセージだった場合
		if (5000 <= code && code < 6000) {
			int resId = R.string.msg_system_error;
			resId = MCDefResult.getStringId(code);
			msg = context.getString(resId);
			if (resId == R.string.msg_system_error)
				msg = msg + "(" + code + ")";
		}

		return msg;
	}
}
