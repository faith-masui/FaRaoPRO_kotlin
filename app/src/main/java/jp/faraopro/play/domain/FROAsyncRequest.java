package jp.faraopro.play.domain;

import java.util.concurrent.Callable;

import jp.faraopro.play.mclient.IMCResultInfo;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCError;
import jp.faraopro.play.mclient.MCHttpClient;
import jp.faraopro.play.mclient.MCPostActionInfo;
import jp.faraopro.play.mclient.MCPostActionParam;

public class FROAsyncRequest implements Callable<Integer> {
	private int mApikind;
	private MCPostActionParam mParam;

	public FROAsyncRequest(int apiKind, MCPostActionParam requestParam) {
		mApikind = apiKind;
		mParam = requestParam;
	}

	@Override
	public Integer call() throws Exception {
		if (Thread.interrupted()) {
			release();
			return MCError.MC_APPERR_CANCEL;
		}

		MCHttpClient client = MCHttpClient.getInstance();
		client.setPrepare(new MCPostActionInfo(mApikind, mParam));
		IMCResultInfo result = client.action();
		release();

		return result.getInt(MCDefResult.MCR_KIND_STATUS_CODE);
	}

	private void release() {
		mParam = null;
	}
}
