package jp.faraopro.play.mclient;

import java.io.File;

import android.content.Context;
import jp.faraopro.play.mclient.MCHttpClient.IMCHttpClientListener;
import jp.faraopro.play.util.FROUtils;
import jp.faraopro.play.util.Utils;

/**
 * Music Client Handler ・Web(MusicServer)アクセス ・ユーザー情報管理・アクセス ・楽曲情報管理・アクセス
 * 
 * @author AIM Corporation
 * 
 */
public class MusicClientHandler extends MusicClientAction implements IMusicClientHandler {
	// private static final String TAG =
	// MusicClientHandler.class.getSimpleName();

	private Context mContext = null;
	private IMCResultListener mResultListener = null;
	private String mParamEmail = null;
	private String mParamPassword = null;
	private String mSessionKey = null;
	private String mDeviceToken = null;

	// public HashMap<Double, Runnable> extensionCallback = new HashMap<Double,
	// Runnable>();

	/**
	 * コンストラクタ
	 */
	public MusicClientHandler(Context context, IMCResultListener resultListener) {
		lclear();
		mContext = context;
		mResultListener = resultListener;
	}

	@Override
	public void term() {
		MCUserInfoPreference info = MCUserInfoPreference.getInstance(mContext);
		info.term();
		info = null;
		lclear();
		super.term();
	}

	@Override
	public void cancelAPIs() throws InterruptedException {
		super.cancelAPIs();
	}

	private void lclear() {
		mContext = null;
		mResultListener = null;
		mParamEmail = null;
		mParamPassword = null;
		mDeviceToken = null;
	}

	@Override
	public void actDoAction(int kind, IMCPostActionParam param) {
		switch (kind) {
		case MCDefAction.MCA_KIND_SIGNUP:
		case MCDefAction.MCA_KIND_ACTIVATION:
			break;
		case MCDefAction.MCA_KIND_LOGIN:
			mParamEmail = param.getStringValue(MCDefParam.MCP_KIND_EMAIL);
			mParamPassword = param.getStringValue(MCDefParam.MCP_KIND_PASSWORD);
			mDeviceToken = param.getStringValue(MCDefParam.MCP_KIND_DEVICE_TOKEN);
			break;
		case MCDefAction.MCA_KIND_RATING_OFFLINE:
			int off_msec = Integer.valueOf(param.getStringValue(MCDefParam.MCP_KIND_PLAY_DURATIN));
			int off_sec = off_msec / 1000; // 秒に丸め込む(WebApiの仕様変更)
			param.setStringValue(MCDefParam.MCP_KIND_PLAY_DURATIN, String.valueOf(off_sec));
			break;
		case MCDefAction.MCA_KIND_RATING:
			int msec = Integer.valueOf(param.getStringValue(MCDefParam.MCP_KIND_PLAY_DURATIN));
			int sec = msec / 1000; // 秒に丸め込む(WebApiの仕様変更)
			param.setStringValue(MCDefParam.MCP_KIND_PLAY_DURATIN, String.valueOf(sec));
			// breakはしない!
		default:
			// targetSdkをHC以前にした場合、sharedPreferenceから値を取得できない場合がある
			if (mSessionKey == null)
				mSessionKey = usrGetValue(MCDefUser.MCU_KIND_SESSIONKEY);
			param.setStringValue(MCDefParam.MCP_KIND_SESSIONKEY, mSessionKey);
			break;
		}
		super.action(kind, param, null);
	}

	// public void actDoAction(int kind, IMCPostActionParam param, Double key,
	// Runnable callback) {
	// if (extensionCallback != null && key != null && callback != null)
	// extensionCallback.put(key, callback);
	// actDoAction(kind, param);
	// }

	@Override
	public void actDoActionWithListener(int kind, IMCPostActionParam param, IMCHttpClientListener listener) {
		if (mSessionKey == null)
			mSessionKey = usrGetValue(MCDefUser.MCU_KIND_SESSIONKEY);
		param.setStringValue(MCDefParam.MCP_KIND_SESSIONKEY, mSessionKey);
		super.action(kind, param, listener);
	}

	@Override
	public String usrGetValue(int kind) {
		String value = "";
		MCUserInfoPreference info = MCUserInfoPreference.getInstance(mContext);
		switch (kind) {
		case MCDefUser.MCU_KIND_EMAIL:
			value = info.getEmail();
			break;
		case MCDefUser.MCU_KIND_PASSWORD:
			value = info.getPassword();
			break;
		case MCDefUser.MCU_KIND_SESSIONKEY:
			value = info.getSessionKey();
			break;
		default:
			break;
		}
		info.term();
		info = null;
		return value;
	}

	@Override
	public void usrClearAll() {
		String value = "";
		usrSetValue(MCDefUser.MCU_KIND_EMAIL, value);
		usrSetValue(MCDefUser.MCU_KIND_PASSWORD, value);
		usrSetValue(MCDefUser.MCU_KIND_SESSIONKEY, value);
		value = null;
	}

	@Override
	public IMCMusicItemList mdbGetAll() {
		IMCMusicItemList list;
		IMCMusicInfoDB dbMusic = MCMusicInfoDBFactory.getInstance(mContext);
		list = dbMusic.findAll();
		dbMusic = null;
		return list;
	}

	@Override
	public IMCMusicItemInfo mdbGet(String trackId) {
		IMCMusicItemInfo info;
		IMCMusicInfoDB dbMusic = MCMusicInfoDBFactory.getInstance(mContext);
		info = dbMusic.find(trackId);
		dbMusic = null;
		return info;
	}

	@Override
	public IMCMusicItemInfo mdbGet2(String status) {
		IMCMusicItemInfo info;
		IMCMusicInfoDB dbMusic = MCMusicInfoDBFactory.getInstance(mContext);
		info = dbMusic.find2(status);
		dbMusic = null;
		return info;
	}

	@Override
	public IMCMusicItemInfo mdbFindByImage(String path) {
		IMCMusicItemInfo info;
		IMCMusicInfoDB dbMusic = MCMusicInfoDBFactory.getInstance(mContext);
		info = dbMusic.findByImage(path);
		dbMusic = null;
		return info;
	}

	@Override
	public int mdbGetSize() {
		int value;
		IMCMusicInfoDB dbMusic = MCMusicInfoDBFactory.getInstance(mContext);
		value = dbMusic.getSize();
		return value;
	}

	public int mdbGetDownloadedTrackSize() {
		int value;
		MCMusicInfoDB dbMusic = (MCMusicInfoDB) MCMusicInfoDBFactory.getInstance(mContext);
		value = dbMusic.getSize(IMCMusicItemInfo.MCDB_STATUS_COMPLETE)
				+ dbMusic.getSize(IMCMusicItemInfo.MCDB_STATUS_PLAYING);
		return value;
	}

	@Override
	public IMCMusicItemInfo mdbGetNextPlay() {
		IMCMusicItemInfo info;
		IMCMusicInfoDB dbMusic = MCMusicInfoDBFactory.getInstance(mContext);
		info = dbMusic.findNext();
		dbMusic = null;
		return info;
	}

	// public void hoge(){
	// IMCMusicInfoDB dbMusic = MCMusicInfoDBFactory.getInstance(mContext);
	// dbMusic.hogehoge();
	// dbMusic = null;
	// }

	@Override
	public void mdbDeleteAll() {
		IMCMusicInfoDB dbMusic = MCMusicInfoDBFactory.getInstance(mContext);
		dbMusic.deleteAll();
		dbMusic = null;
	}

	@Override
	public void mdbDelete(String trackId) {
		IMCMusicInfoDB dbMusic = MCMusicInfoDBFactory.getInstance(mContext);
		dbMusic.delete(trackId);
		dbMusic = null;
	}

	@Override
	public void mdbSetStatus(String trackId, int status) {
		IMCMusicInfoDB dbMusic = MCMusicInfoDBFactory.getInstance(mContext);
		IMCMusicItemInfo info;

		info = dbMusic.find(trackId);
		if (info != null) {
			info.setStatus(status);
			dbMusic.update(trackId, info);
			info.clear();
			info = null;
		}
		dbMusic = null;
	}

	@Override
	public void mdbPlayDuration(String trackId, String msec) {
		IMCMusicInfoDB dbMusic = MCMusicInfoDBFactory.getInstance(mContext);
		IMCMusicItemInfo info;

		info = dbMusic.find(trackId);
		if (info != null) {
			info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_PLAY_DURATION, msec);
			dbMusic.update(trackId, info);
			info.clear();
			info = null;
		}
		dbMusic = null;
	}

	public String api_sig;

	@Override
	public void onCompleteAction(int when, IMCResultInfo result) {
		String value = null;

		switch (when) {
		case MCDefAction.MCA_KIND_LOGIN:
			value = result.getString(MCDefResult.MCR_KIND_SESSIONKEY);
			if (value != null && !value.equals("")) {
				usrSetValue(MCDefUser.MCU_KIND_EMAIL, mParamEmail);
				usrSetValue(MCDefUser.MCU_KIND_PASSWORD, mParamPassword);
				usrSetValue(MCDefUser.MCU_KIND_DEVICETOKEN, mDeviceToken);
				usrSetValue(MCDefUser.MCU_KIND_SESSIONKEY, value);
				mSessionKey = value;
			}
			break;
		case MCDefAction.MCA_KIND_FACEBOOK_LOGIN:
			value = result.getString(MCDefResult.MCR_KIND_SESSIONKEY);
			if (value != null && !value.equals("")) {
				usrSetValue(MCDefUser.MCU_KIND_SESSIONKEY, value);
				mSessionKey = value;
			}
			break;
		case MCDefAction.MCA_KIND_LOGOUT:
			// usrSetValue(MCDefUser.MCU_KIND_EMAIL, "");
			// usrSetValue(MCDefUser.MCU_KIND_PASSWORD, "");
			usrSetValue(MCDefUser.MCU_KIND_SESSIONKEY, "");
			mSessionKey = null;
			break;
		case MCDefAction.MCA_KIND_LISTEN:
			// DB初期化
			mdbDeleteAll();
			// データファイル初期化
			Utils.deleteFile(new File(FROUtils.getFaraoDirectory()), false);
		case MCDefAction.MCA_KIND_RATING:
			mdbInsert(result);
			break;
		default:
			break;
		}

		if (mResultListener != null)
			mResultListener.onNotifyMCResult(when, result);
	}

	@Override
	public void onRegistENC(IMCResultInfo encItem) {
		String value = encItem.getString(MCDefResult.MCR_KIND_ENCKEY);
		if (value != null && !"".equals(value)) {

			String trackId = encItem.getString(MCDefResult.MCR_KIND_TRACKITEM_ID);
			IMCMusicInfoDB dbMusic = MCMusicInfoDBFactory.getInstance(mContext);
			IMCMusicItemInfo info;

			info = dbMusic.find(trackId);
			if (info != null) {
				info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_ENC_KEY, value);
				info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_ENC_METHOD,
						encItem.getString(MCDefResult.MCR_KIND_ENCMETHOD));
				info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_ENC_IV, encItem.getString(MCDefResult.MCR_KIND_ENCIV));
				info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_API_SIG,
						encItem.getString(MCDefResult.MCR_KIND_API_SIG));

				// DL完了と判断
				info.setStatus(IMCMusicItemInfo.MCDB_STATUS_COMPLETE);
				info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_OUTPUT_PATH,
						encItem.getString(MCDefResult.MCR_KIND_DL_PATH));

				dbMusic.update(trackId, info);

				info.clear();
				info = null;
			}
			dbMusic = null;
		}
	}

	@Override
	public void onErrorAction(int when, int errorCode) {
		if (when == MCDefAction.MCA_KIND_LOGIN) {
			mParamEmail = null;
			mParamPassword = null;
			mDeviceToken = null;
		}
		if (mResultListener != null)
			mResultListener.onNotifyMCError(when, errorCode);
	}

	/**
	 * String値の登録
	 * 
	 * @param kind
	 * @param value
	 */
	public void usrSetValue(int kind, String value) {
		MCUserInfoPreference info = MCUserInfoPreference.getInstance(mContext);
		switch (kind) {
		case MCDefUser.MCU_KIND_EMAIL:
			info.setEmail(value);
			break;
		case MCDefUser.MCU_KIND_PASSWORD:
			info.setPassword(value);
			break;
		case MCDefUser.MCU_KIND_SESSIONKEY:
			info.setSessionKey(value);
			break;
		case MCDefUser.MCU_KIND_DEVICETOKEN:
			info.setDeviceToken(value);
			break;
		default:
			break;
		}
		info.term();
		info = null;
	}

	private void mdbInsert(IMCResultInfo result) {
		IMCMusicInfoDB dbMusic = MCMusicInfoDBFactory.getInstance(mContext);
		IMCMusicItemInfo info;
		IMCItemList list;
		int i;

		list = result.getList(IMCResultInfo.MC_LIST_KIND_TRACK);
		if (list != null) {
			for (i = 0; i < list.getSize(); i++) {
				info = new MCMusicItemInfo();
				info.setStatus(IMCMusicItemInfo.MCDB_STATUS_DEFAULT);
				info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_ADVERTISE_RATIO,
						result.getString(MCDefResult.MCR_KIND_ADVERTISERATIO));
				info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_SKIP_REMAINING,
						result.getString(MCDefResult.MCR_KIND_SKIPREMAINING));
				info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_CHANNEL_NAME,
						result.getString(MCDefResult.MCR_KIND_CHANNEL_NAME));
				info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_CHANNEL_NAME_EN,
						result.getString(MCDefResult.MCR_KIND_CHANNEL_NAME_EN));
				info.setTrackItem(list.getItem(i));
				dbMusic.insert(info);
				info = null;
			}
		}
		dbMusic = null;
	}

	/**
	 * 楽曲情報のDB登録
	 * 
	 * @param info
	 */
	public void mdbSetInfo(IMCMusicItemInfo info) {
		IMCMusicInfoDB dbMusic = MCMusicInfoDBFactory.getInstance(mContext);
		dbMusic.insert(info);
		dbMusic = null;
	}

	public int mdbCheckTable() {
		IMCMusicInfoDB dbMusic = MCMusicInfoDBFactory.getInstance(mContext);

		return dbMusic.checkTable();
	}

	// private void cacheMusic(IMCMusicInfoDB dbMusic, String trackId) {
	// MCOfflineMusicInfoDB cacheDb = (MCOfflineMusicInfoDB)
	// MCOfflineMusicInfoDBFactory.getInstance(mContext);
	// if (cacheDb.getSize() > 3) {
	// IMCMusicItemInfo oldest = cacheDb.findOldest(false);
	// cacheDb.delete(oldest.getStringValue(MCDefResult.MCR_KIND_TRACKITEM_ID));
	// File deleteFile = new
	// File(Environment.getExternalStorageDirectory().getPath() + "/CacheTest/"
	// + oldest.getStringValue(IMCMusicItemInfo.MCDB_ITEM_OUTPUT_PATH));
	// Utils.deleteFile(deleteFile);
	// }
	// IMCMusicItemInfo info = dbMusic.find(trackId);
	// String srcPath =
	// info.getStringValue(IMCMusicItemInfo.MCDB_ITEM_OUTPUT_PATH);
	// String dstPath = Environment.getExternalStorageDirectory().getPath() +
	// "/CacheTest/" + trackId;
	// File parent = new
	// File(Environment.getExternalStorageDirectory().getPath() + "/CacheTest");
	// if (!parent.exists())
	// parent.mkdir();
	//
	// copy(srcPath, dstPath);
	// info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_OUTPUT_PATH, dstPath);
	// cacheDb.insert(info);
	//
	// // TODO REMOVE
	// cacheDb.output(trackId);
	// }
	//
	// private void copy(String src, String dst) {
	// FileInputStream fis = null;
	// FileOutputStream fos = null;
	// try {
	// fis = new FileInputStream(src);
	// fos = new FileOutputStream(dst);
	// int length = -1;
	// byte[] buffer = new byte[2048];
	// while ((length = fis.read(buffer)) > 0) {
	// fos.write(buffer, 0, length);
	// }
	// fis.close();
	// fos.close();
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
}
