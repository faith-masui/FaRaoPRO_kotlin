package jp.faraopro.play.domain;

import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.mclient.IMCPostActionParam;
import jp.faraopro.play.mclient.IMCResultInfo;
import jp.faraopro.play.mclient.MCDefAction;
import jp.faraopro.play.mclient.MCDefParam;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCError;
import jp.faraopro.play.mclient.MCHttpClient;
import jp.faraopro.play.mclient.MCPostActionFactory;
import jp.faraopro.play.mclient.MCPostActionInfo;
import jp.faraopro.play.mclient.MCPostActionParam;
import jp.faraopro.play.util.Utils;

public class FROAsyncTaskLoader extends AsyncTaskLoader<Bitmap> {
	public static final String TAG_THUMB_ID = "THUMBNAIL_ID";
	public static final String TAG_FRG_NAME = "FRAGMENT_NAME";
	public static final String TAG_LIST_POS = "LIST_POSITION";
	public static final String TAG_LIST_TYPE = "LIST_TYPE";
	public static final String TAG_THUMBNAIL_URL = "THUMBNAIL_URL";
	public static final String TAG_LAST_MODIFIED = "LAST_MODIFIED";

	private static final String TAG = FROAsyncTaskLoader.class.getSimpleName();
	private static final boolean DEBUG = false;

	private Bundle args;
	private Bitmap result;

	public FROAsyncTaskLoader(Context context, Bundle args) {
		super(context);
		this.args = args;
	}

	@Override
	public Bitmap loadInBackground() {
		int retry = 3;

		IMCPostActionParam param = MCPostActionFactory.getInstance();
		String session = FROForm.getInstance().getSessionKey();
		if (session != null)
			param.setStringValue(MCDefParam.MCP_KIND_SESSIONKEY, session);

		String thumbId = args.getString(TAG_THUMB_ID);
		String mode = args.getString(TAG_LIST_TYPE);
		param.setStringValue(MCDefParam.MCP_KIND_TRACKID, args.getString(TAG_THUMBNAIL_URL));
		param.setStringValue(MCDefParam.MCP_KIND_THUMB_ID, thumbId);
		param.setStringValue(MCDefParam.MCP_KIND_RANGE, args.getString(TAG_LAST_MODIFIED));

		IMCPostActionParam mPostActionParamThread = null;
		mPostActionParamThread = MCPostActionFactory.getInstance();
		((MCPostActionParam) mPostActionParamThread).copyParam(param);

		IMCResultInfo resultInfo = null;
		IMCPostActionParam iparam = mPostActionParamThread;
		int actionKind = MCDefAction.MCA_KIND_THUMB_DL;
		int errorCode = 0;
		MCPostActionInfo actionInfo = new MCPostActionInfo(actionKind, iparam);

		MCHttpClient httpClient = MCHttpClient.getInstance();
		httpClient.setPrepare(actionInfo);

		while (retry-- > 0) {
			resultInfo = httpClient.actionGet();
			errorCode = resultInfo.getInt(MCDefResult.MCR_KIND_STATUS_CODE);
			if (errorCode != MCError.MC_APPERR_IO_HTTP) {
				break;
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}
		}
		// resultInfo = httpClient.action();
		errorCode = resultInfo.getInt(MCDefResult.MCR_KIND_STATUS_CODE);
		actionInfo.clear();
		actionInfo = null;

		Bitmap thumb = null;
		String fileName = Consts.PRIVATE_PATH_THUMB_LIST + mode + thumbId.replace(".jpg", "").replace(".png", "");

		FRODebug.logD(getClass(), "status code = " + errorCode, DEBUG);
		FRODebug.logD(getClass(), "id = " + thumbId, DEBUG);
		FRODebug.logD(getClass(), "fileName = " + fileName, DEBUG);

		if (errorCode == MCError.MC_NO_ERROR) {
			thumb = Utils.loadBitmap(resultInfo.getString(MCDefResult.MCR_KIND_DL_PATH));
			if (thumb != null) {
				Utils.saveBitmap(thumb, fileName, getContext());
				FROImageCache.setImage(args.getString(TAG_THUMB_ID), thumb);
			}
			Utils.deleteFile(new File(resultInfo.getString(MCDefResult.MCR_KIND_DL_PATH)));
		} else if (errorCode == 304) {
			thumb = Utils.loadBitmap(fileName, 1, getContext());
			if (thumb != null)
				FROImageCache.setImage(thumbId, thumb);
		}

		return thumb;
	}

	@Override
	public void deliverResult(Bitmap data) {
		if (isReset()) {
			if (this.result != null) {
				this.result = null;
			}
			return;
		}

		this.result = data;

		if (isStarted()) {
			super.deliverResult(data);
		}
	}

	@Override
	protected void onStartLoading() {
		if (this.result != null) {
			deliverResult(this.result);
		}
		if (takeContentChanged() || this.result == null) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		super.onStopLoading();
		cancelLoad();
	}

	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();
	}

	public Bundle getArgs() {
		return args;
	}

	@Override
	public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
		super.dump(prefix, fd, writer, args);
		writer.print(prefix);
		writer.print("thumbId=");
		writer.println(this.args.getString(TAG_THUMB_ID));
		writer.print(prefix);
		writer.print("result=");
		writer.println(this.result);
	}

}