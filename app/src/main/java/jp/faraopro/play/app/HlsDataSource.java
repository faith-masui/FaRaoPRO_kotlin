package jp.faraopro.play.app;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;

import android.net.http.AndroidHttpClient;
import android.util.Log;

public class HlsDataSource {
	private static final String TAG = "HlsDataSource";
	private String sourceUrl;
	private String cookieValue;

	public HlsDataSource(String url) throws IOException {
		prepare(url);
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public String getCookieValue() {
		return cookieValue;
	}

	private void prepare(String url) throws IOException {
		AndroidHttpClient httpClient = AndroidHttpClient.newInstance("FaRao/1.0");
		try {
			httpClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
			HttpGet request = new HttpGet(url);

//			Log.i(TAG, "prepare URL: " + url);
			HttpResponse response = httpClient.execute(request);
			try {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY
						|| response.getStatusLine().getStatusCode() == HttpStatus.SC_TEMPORARY_REDIRECT) {
					// try redirect follow
					url = response.getFirstHeader("Location").getValue();
//					Log.w(TAG, "follow redirect: " + url);
					request = new HttpGet(url);
					HttpEntity entity = response.getEntity();
					if (entity != null)
						entity.consumeContent();

					response = httpClient.execute(request);
				}

				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//					Log.e(TAG, "" + response.getStatusLine().getReasonPhrase());
					throw new IOException("http status: " + response.getStatusLine().getReasonPhrase());
				}

				Header contentType = response.getFirstHeader("Content-Type");
				if (contentType == null || contentType.getValue().toLowerCase().endsWith("mpegurl") == false) {
//					Log.e(TAG, "Conten-Type: not mepgurl");
					throw new IOException("" + contentType);
				}
				String cookieValue = null;
				Header cookie = response.getFirstHeader("Set-Cookie");
				if (cookie != null)
					cookieValue = cookie.getValue();

//				Log.i(TAG, response.getStatusLine().getReasonPhrase());
//				Log.i(TAG, "HLS URL = " + url);
//				Log.i(TAG, "HLS Cookie = " + cookieValue);

				this.sourceUrl = url;
				this.cookieValue = cookieValue;

			} finally {
				HttpEntity entity = response.getEntity();
				if (entity != null)
					entity.consumeContent();
			}
		} finally {
			httpClient.close();
		}
	}
}
