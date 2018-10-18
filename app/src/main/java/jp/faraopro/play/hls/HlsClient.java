package jp.faraopro.play.hls;

import android.util.Log;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 */
public class HlsClient {
	private static final String TAG = "HlsClient";

	// private final AndroidHttpClient client;
	private final DefaultHttpClient client;
	private final HttpContext httpContext;
	private final String userAgent;

	// private boolean dump = false;
	public HlsClient(String userAgent) {
		// this.client = AndroidHttpClient.newInstance(userAgent);
		this.client = new DefaultHttpClient();
		this.httpContext = new BasicHttpContext();
		this.httpContext.setAttribute(ClientContext.COOKIE_STORE, new BasicCookieStore());
		this.client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);

		HttpConnectionParams.setConnectionTimeout(this.client.getParams(), 10000);
		HttpConnectionParams.setSoTimeout(this.client.getParams(), 10000);
		this.client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, userAgent);

		this.userAgent = userAgent;
	}

	public URI toURI(String path) throws MalformedURLException {
		try {
			return new URI(path);
		} catch (URISyntaxException ue) {
			throw new MalformedURLException(path);
		}
	}

	public URI toURI(URI source, String path) throws MalformedURLException {
		try {
			if (path.indexOf("://") > 0) {
				return new URI(path);
			}
			return new URL(source.toURL(), path).toURI();
		} catch (URISyntaxException ue) {
			throw new MalformedURLException(path);
		}
	}

	private HttpGet request(URI source) {
		HttpGet method = new HttpGet(source);
		method.addHeader("Connection", "close");
		if (userAgent != null)
			method.addHeader("User-Agent", userAgent);
		return method;
	}

	public void shutdown() {
		// this.client.close();
		this.client.getConnectionManager().shutdown();
	}

	private boolean httpRetryException(Throwable t) {
		if (t instanceof NoHttpResponseException)
			return true;
		if (t instanceof HlsHttpEntityLengthException)
			return true;
		if (t instanceof HlsHttpException && ((HlsHttpException) t).getStatusCode() == 404)
			return true;
		return false;
	}

	public List<String> downloadPlaylist(URI source, HlsClientListener listener) throws IOException {
		int httpRetry = 0;
		while (true) {
			try {
				// retry for null playlist
				int retry = 0;
				for (; retry < 3; retry++) {
					byte[] bodyContent = executeDownloadPlaylist(source);
					List<String> m3u8 = IOUtils.readLines(new ByteArrayInputStream(bodyContent), "UTF-8");
					if (!m3u8.isEmpty() && HlsUtils.isExtendedM3U(m3u8))
						return m3u8;
					try {
						if (listener != null)
							listener.onErrorEmptyPlaylist(source.toString(), bodyContent);
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
				throw new IOException("Empty playlist retry over: retry=" + retry);
			} catch (IOException e) {
				if (listener != null)
					listener.onErrorDownloadPlaylist(source.toString(), e);
				if (httpRetryException(e) && httpRetry < 3) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ignore) {
					}
				} else
					throw e;
			}
			httpRetry++;
		}
	}

	private byte[] executeDownloadPlaylist(URI source) throws IOException {
		HttpGet method = request(source);
		HttpResponse response = client.execute(method, httpContext);
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY
					|| response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY
					|| response.getStatusLine().getStatusCode() == HttpStatus.SC_TEMPORARY_REDIRECT) {
				try {
					URI location = new URI(response.getFirstHeader("Location").getValue());
					throw new HlsHttpRedirectException(HttpStatus.SC_TEMPORARY_REDIRECT, location);
				} catch (URISyntaxException ue) {
				}
			}
			throw new HlsHttpException(response.getStatusLine().getStatusCode());
		}
//		Log.d(TAG, "downloadPlaylist: url=" + source);

		try {
			long contentLength = response.getEntity().getContentLength();
			byte[] bodyContent = EntityUtils.toByteArray(response.getEntity());
			if (contentLength >= 0) {
				if (contentLength != bodyContent.length) {
					Log.w(TAG, "invalid playlistSize: contentLength=" + contentLength + ", playlistSize="
							+ bodyContent.length);
					throw new HlsHttpEntityLengthException(
							"contentLength=" + contentLength + ", responseLength=" + bodyContent.length);
				}
			}
//			List<String> m3u8 = IOUtils.readLines(new ByteArrayInputStream(bodyContent), "UTF-8");
//			if (m3u8.isEmpty() || !HlsUtils.isExtendedM3U(m3u8)) {
//				Log.d(TAG, "--- unknown response");
//				Log.d(TAG, new String(Hex.encodeHex(bodyContent)));
//				Log.d(TAG, "--- unknown response end");
//				Log.d(TAG, "Not EXTM3U contentLength=" + response.getEntity().getContentLength() + ", contentType="
//						+ response.getEntity().getContentType().getValue());
//			}
			return bodyContent;
		} finally {
			response.getEntity().consumeContent();
		}
	}

	private static final Pattern PATTERN_MEDIA_SEQUENCE = Pattern.compile("#EXT-X-MEDIA-SEQUENCE:(\\d+)");
	private static final Pattern PATTERN_KEY = Pattern
			.compile("#EXT-X-KEY:METHOD=(AES-128|NONE),URI=\\\"([^\"]+)\\\".*");
	private static final Pattern PATTERN_IV = Pattern.compile(".*IV=0[xX]([0-9a-fA-F]+).*");
	private static final Pattern PATTERN_BYTERANGE = Pattern.compile("#EXT-X-BYTERANGE:([0-9@]+)");
	private static final Pattern PATTERN_EXTINF = Pattern.compile("#EXTINF:([0-9\\.]+),(.*)");

	public List<HlsSegment> downloadSegments(URI source, List<String> segments, boolean withMedia,
			HlsClientListener listener) throws GeneralSecurityException, IOException {
		List<HlsSegment> results = new ArrayList<HlsSegment>();
		long sequence = 0;
		String keyMethod = null;
		String keyPath = null;
		byte[] keyContent = null;
		byte[] keyIv = null;
		double duration = 0;
		String title = null;
		String mediaPath;

		long byteRangeOffset = 0;
		long byteRangeLength = 0;

		byte[] mediaContent = null;

		for (String s : segments) {
			// header
			Matcher ms = PATTERN_MEDIA_SEQUENCE.matcher(s);
			if (ms.matches()) {
				sequence = Long.parseLong(ms.group(1));
//				Log.v(TAG, "media sequence=" + sequence);
				continue;
			}
			Matcher mk = PATTERN_KEY.matcher(s);
			if (mk.matches()) {
				keyMethod = mk.group(1);
				keyPath = mk.group(2);

				keyIv = null;
				Matcher mi = PATTERN_IV.matcher(s);
				if (mi.matches()) {
					try {
						keyIv = Hex.decodeHex(mi.group(1).toCharArray());
					} catch (DecoderException de) {
					}
				}
				keyContent = downloadKey(source, keyMethod, keyPath, listener);
				continue;
			}
			Matcher mb = PATTERN_BYTERANGE.matcher(s);
			if (mb.matches()) {
				String byteRange = mb.group(1);
//				Log.v(TAG, "byterange value=" + byteRange);

				String[] pair = byteRange.split("@");
				byteRangeLength = Long.parseLong(pair[0]);
				if (pair.length > 1)
					byteRangeOffset = Long.parseLong(pair[1]);
				continue;
			}
			Matcher me = PATTERN_EXTINF.matcher(s);
			if (me.matches()) {
				duration = Double.parseDouble(me.group(1));
				title = me.group(2);
				// Log.v(TAG, "extinf duration=" + duration + ", title=" +
				// title);
				continue;
			}
			if (s.startsWith("#"))
				continue;

			if (s.trim().length() == 0)
				continue;

			// segment
			mediaPath = s;
			if (withMedia) {
				mediaContent = downloadMedia(source, mediaPath, byteRangeOffset, byteRangeLength, listener);
				if (keyMethod != null && keyContent != null)
					mediaContent = decode(keyMethod, keyContent, keyIv, mediaContent, sequence, listener);
			}

			HlsSegment ei = new HlsSegment(sequence, duration, title, keyMethod, keyPath, keyContent, keyIv, mediaPath,
					mediaContent, byteRangeOffset, byteRangeLength);
			results.add(ei);
			sequence++;
		}
		return results;
	}

	public byte[] downloadKey(URI source, String keyMethod, String keyPath, HlsClientListener listener)
			throws IOException {
		int httpRetry = 0;
		while (true) {
			try {
				return executeDownloadKey(source, keyMethod, keyPath);
			} catch (IOException e) {
				if (listener != null)
					listener.onErrorDownloadKey(keyPath, e);
				if (httpRetryException(e) && httpRetry < 3) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ignore) {
					}
				} else
					throw e;
			}
			httpRetry++;
		}
	}

	public byte[] executeDownloadKey(URI source, String keyMethod, String keyPath) throws IOException {
//		long keyLap = System.currentTimeMillis();
		URI keySource = toURI(source, keyPath);

		HttpResponse response = client.execute(request(keySource), httpContext);
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			throw new HlsHttpException(response.getStatusLine().getStatusCode());
		}
		try {
			long contentLength = response.getEntity().getContentLength();
			byte[] keyContent = EntityUtils.toByteArray(response.getEntity());
//			keyLap = System.currentTimeMillis() - keyLap;
//			Log.d(TAG, "downloadKey: url=" + keySource);
//			Log.d(TAG, "downloadKey: method=" + keyMethod + ", uri=" + keyPath + ", key="
//					+ new String(Hex.encodeHex(keyContent)) + ", lap=" + (keyLap / 1000.0));
			if (contentLength >= 0) {
				if (contentLength != keyContent.length) {
					Log.w(TAG, "invalid keySize: contentLength=" + contentLength + ", keySize=" + keyContent.length);
					throw new HlsHttpEntityLengthException(
							"contentLength=" + contentLength + ", responseLength=" + keyContent.length);
				}
			}
			return keyContent;
		} finally {
			response.getEntity().consumeContent();
		}
	}

	public byte[] downloadMedia(URI source, String mediaPath, long offset, long length, HlsClientListener listener)
			throws IOException {
		int httpRetry = 0;
		while (true) {
			try {
				return executeDownloadMedia(source, mediaPath, offset, length);
			} catch (IOException e) {
				if (listener != null)
					listener.onErrorDownloadMedia(mediaPath, e);
				if (httpRetryException(e) && httpRetry < 3) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ignore) {
					}
				} else
					throw e;
			}
			httpRetry++;
		}
	}

	public byte[] executeDownloadMedia(URI source, String mediaPath, long offset, long length) throws IOException {
		long mediaLap = System.currentTimeMillis();

		URI mediaSource = toURI(source, mediaPath);
		HttpGet method = request(mediaSource);
		if (offset > 0 && length > 0) {
			String rangeValue = "bytes=" + offset + "-" + (offset + length - 1);
//			Log.v(TAG, rangeValue);
			method.addHeader("Range", rangeValue);
		}
		HttpResponse response = client.execute(method, httpContext);
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK
				&& response.getStatusLine().getStatusCode() != HttpStatus.SC_PARTIAL_CONTENT) {
			throw new HlsHttpException(response.getStatusLine().getStatusCode());
		}

		try {
			long contentLength = response.getEntity().getContentLength();
			byte[] mediaContent = EntityUtils.toByteArray(response.getEntity());
//			mediaLap = System.currentTimeMillis() - mediaLap;
//			Log.d(TAG, "downloadMedia: url=" + mediaSource);
//			Log.d(TAG, "downloadMedia: path=" + mediaPath + ", mediaSize=" + mediaContent.length + ", lap="
//					+ (mediaLap / 1000.0));
			if (contentLength >= 0) {
				if (contentLength != mediaContent.length) {
					Log.w(TAG,
							"invalid mediaSize: contentLength=" + contentLength + ", mediaSize=" + mediaContent.length);
					throw new HlsHttpEntityLengthException(
							"contentLength=" + contentLength + ", responseLength=" + mediaContent.length);
				}
			}
			return mediaContent;
		} finally {
			response.getEntity().consumeContent();
		}
	}

	public byte[] decode(String method, byte[] keyContent, byte[] keyIv, byte[] mediaContent, long sequence,
			HlsClientListener listener) throws GeneralSecurityException, IOException {
		try {
			return executeDecode(method, keyContent, keyIv, mediaContent, sequence);
		} catch (GeneralSecurityException e) {
			if (listener != null)
				listener.onErrorMediaDecryption(sequence, e);
			throw e;
		} catch (IOException e) {
			if (listener != null)
				listener.onErrorMediaDecryption(sequence, e);
			throw e;
		}
	}

	public byte[] executeDecode(String method, byte[] keyContent, byte[] keyIv, byte[] mediaContent, long sequence)
			throws GeneralSecurityException, IOException {
		if ("AES-128".equals(method)) {
			byte[] iv = keyIv;
			if (iv == null) {
				// sequence to iv
				try {
					String hexSequence = String.format("%032x", sequence);
//					Log.d(TAG, "decode: key=" + new String(Hex.encodeHex(keyContent)) + ", iv=" + hexSequence
//							+ ", sequence=" + sequence);
					iv = Hex.decodeHex(hexSequence.toCharArray());
				} catch (DecoderException de) {
					throw new GeneralSecurityException(de);
				}
			}
			long lap = System.currentTimeMillis();
			SecretKeySpec skeySpec = new SecretKeySpec(keyContent, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(iv));
			byte[] original = cipher.doFinal(mediaContent);
//			lap = System.currentTimeMillis() - lap;

//			Log.d(TAG, "decode: mediaSize=" + mediaContent.length + ", originalSize=" + original.length + ", lap="
//					+ (lap / 1000.0));
			// if (dump)
			// HexDump.dump(original, 0, System.out, 0);
			return original;
		}

//		Log.d(TAG, "decode: mediaSize=" + mediaContent.length);
		// if (dump)
		// HexDump.dump(mediaContent, 0, System.out, 0);
		return mediaContent;
	}

//	public URI parseFromHtml5Video(String uri) throws IOException {
//		URI source = toURI(uri);
//		HttpResponse response = client.execute(request(source));
//		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//			throw new HlsHttpException(response.getStatusLine().getStatusCode());
//		}
//		try {
//			String html = EntityUtils.toString(response.getEntity());
//			Log.v(TAG, html);
//			Matcher m = Pattern.compile("(?m)(?s).*<video\\s+(.*)src\\s*=\\s*\"([^\"]+)\"(.*).*").matcher(html);
//			if (m.matches())
//				return toURI(m.group(2));
//			return null;
//		} finally {
//			response.getEntity().consumeContent();
//		}
//	}

}
