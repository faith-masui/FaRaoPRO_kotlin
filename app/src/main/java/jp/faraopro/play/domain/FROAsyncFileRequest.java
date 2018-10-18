package jp.faraopro.play.domain;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import jp.faraopro.play.util.FROUtils;
import jp.faraopro.play.util.NetworkUtil;

public class FROAsyncFileRequest implements Callable<Integer> {
    private static final boolean DEBUG = true;
    private static final int TIMEOUT = 3 * 1000;
	private String mUrl;
	private String mAudioId;
	private Context mContext;

	public FROAsyncFileRequest(Context context, String url, String audioId) {
		mContext = context;
		mUrl = url;
		mAudioId = audioId;
	}

	@Override
	public Integer call() throws Exception {
        HttpURLConnection connection = null;
        int responseCode = -1;
        try {
            if (StringUtils.isEmpty(mAudioId) || StringUtils.isEmpty(mUrl)) {
                throw new IllegalArgumentException();
            }
            connection = (HttpURLConnection) new URL(mUrl).openConnection();
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            connection.connect();
            responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                NetworkUtil.writeResponseBodyToApplicationPrivate(
                        mContext,
                        FROUtils.getInterruptTrackName(mAudioId),
                        connection.getInputStream(),
                        connection.getContentLength());
            }
            connection.getInputStream().close();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            release();
        }
        return responseCode;
	}

	private void release() {
		mUrl = null;
		mAudioId = null;
		mContext = null;
	}

}
