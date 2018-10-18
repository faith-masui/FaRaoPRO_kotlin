package jp.faraopro.play.hls;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpServerConnection;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

import android.util.Log;

/**
 *
 */
public class HlsProxy {
	private static final String TAG = "HlsProxy";

	private final HlsClient client;
	private final ProxyServer proxyServer;
	private final String redirectUrl;
	private int indexOrBitrate;
	private HlsClientListener clientListener;
	private HlsProxyListener proxyListener;
	boolean selectionAdaptive = false;

	/**
	 * HlsProxy bypass adaptive stream selection bypass aes encryption
	 * 
	 * @param port
	 * @param redirectUrl
	 * @param indexOrBitrate
	 * @throws IOException
	 */
	public HlsProxy(int port, String redirectUrl, int indexOrBitrate, HlsClientListener clientListener,
			HlsProxyListener proxyListener) throws IOException {
		this.redirectUrl = redirectUrl;
		this.indexOrBitrate = indexOrBitrate;
		this.clientListener = clientListener;
		this.proxyListener = proxyListener;

		client = new HlsClient("FaRaoHlsProxy/1.0.0");
		proxyServer = new ProxyServer(port);
		proxyServer.start();
//		Log.v(TAG, "starting hls proxy for " + redirectUrl);
//		Log.v(TAG, "browse to http://localhost:" + proxyServer.getListeningPort() + "/0.m3u8");
	}

	public int getIndexOrBitrate() {
		return indexOrBitrate;
	}

	public void setIndexOrBitrate(int indexOrBitrate) {
		this.indexOrBitrate = indexOrBitrate;
	}

	public boolean isSelectionAdaptive() {
		return selectionAdaptive;
	}

	public void setSelectionAdaptive(boolean selectionAdaptive) {
		this.selectionAdaptive = selectionAdaptive;
	}

	URI source;
	URI playlist;
	List<String> adaptiveStreams;
	List<String> segments;
	private static final int SEGMENT_CACHE_SIZE = 24;
	LinkedHashMap<Long, HlsSegment> segmentsCache = new LinkedHashMap<Long, HlsSegment>(SEGMENT_CACHE_SIZE) {
		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(Map.Entry<Long, HlsSegment> eldest) {
			return size() > SEGMENT_CACHE_SIZE;
		}
	};

	public class HlsPlaylistHandler implements HttpRequestHandler {

		@Override
		public void handle(HttpRequest request, HttpResponse response, HttpContext httpContext)
				throws HttpException, IOException {
			String path = request.getRequestLine().getUri();
//			Log.v(TAG, "playlistHandler: path=" + path);

			if (playlist == null || selectionAdaptive) {
				// Android 4.0 adaptive stream selection bypass!
				try {
					source = client.toURI(redirectUrl);
//					Log.v(TAG, "source location: " + source);
					adaptiveStreams = client.downloadPlaylist(source, clientListener);
				} catch (HlsHttpRedirectException hr) {
					// redirect url retry
					source = hr.getLocation();
//					Log.v(TAG, "redirect source location: " + source);
					adaptiveStreams = client.downloadPlaylist(source, clientListener);
				}

				if (HlsUtils.isSegment(adaptiveStreams)) {
					// no adaptive streams
					playlist = source;
					segments = adaptiveStreams;
				} else {
					if (!HlsUtils.isVariant(adaptiveStreams)) {
//						Log.v(TAG, "adaptiveHandler: not variant");
						response.setStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_NOT_FOUND);
						return;
					}

//					Log.d(TAG, "--- adaptive");
//					for (String s : adaptiveStreams) {
//						Log.d(TAG, s);
//					}
//					Log.d(TAG, "--- adaptive end");

					// variant stream selection (default: near bitrate)
					String url = HlsUtils.findAdaptivePlaylist(adaptiveStreams, indexOrBitrate);
					playlist = client.toURI(source, url);
//					Log.v(TAG, "playlist location: " + playlist);
					segments = client.downloadPlaylist(playlist, clientListener);
					if (!HlsUtils.isSegment(segments)) {
//						Log.v(TAG, "adaptiveHandler: not segment");
						response.setStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_NOT_FOUND);
						return;
					}
				}
			} else {
				// segment download
				segments = client.downloadPlaylist(playlist, clientListener);
				if (!HlsUtils.isSegment(segments)) {
//					Log.v(TAG, "adaptiveHandler: not segment");
					response.setStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_NOT_FOUND);
					return;
				}
			}
			// segment
			try {
//                Log.d(TAG, "--- original segment");
//                StringBuilder dumpSegments = new StringBuilder();
//                for (String s : segments) {
//                    dumpSegments.append(s);
//                    dumpSegments.append("\r\n");
//                }
//                Log.d(TAG, dumpSegments.toString());
//                Log.d(TAG, "--- original segment end");

				List<HlsSegment> mediaSegments = client.downloadSegments(playlist, segments, false, clientListener);
				// add to cache
				for (HlsSegment hs : mediaSegments) {
					segmentsCache.put(hs.getSequence(), hs);
//					String keyHex = "";
//					if (hs.getKeyContent() != null)
//						keyHex = new String(Hex.encodeHex(hs.getKeyContent()));
//					Log.v(TAG, "sequence=" + hs.getSequence() + ", mediaPath=" + hs.getMediaPath() + ", keyPath="
//							+ hs.getKeyPath() + ", key=" + keyHex);
				}
				long sequence = mediaSegments.get(0).getSequence();

				// build proxy segments
				StringBuilder s = new StringBuilder();
				s.append("#EXTM3U\r\n");
				s.append("#EXT-X-TARGETDURATION:10\r\n");
				s.append("#EXT-X-MEDIA-SEQUENCE:" + sequence + "\r\n");
				s.append("#EXT-X-ALLOW-CACHE:NO\r\n");
				for (HlsSegment hs : mediaSegments) {
					s.append("#EXTINF:" + hs.getDuration() + "," + hs.getTitle() + "\r\n");
					s.append("/segments/" + hs.getSequence() + ".ts\r\n");
				}
				if (!HlsUtils.isLive(segments)) {
					s.append("#EXT-X-ENDLIST\r\n");
				}

				String responseBody = s.toString();
//				Log.d(TAG, "-- proxy segment");
//				Log.d(TAG, responseBody);
//				Log.d(TAG, "-- proxy segment end");

				// 送信するプレイリストの内容を監視する
				// 前回送信したプレイリストと今回送信するプレイリストが一致している
				if (mLastPlaylist != null && mLastPlaylist.equals(responseBody)) {
					mDuplicatedCount++; // カウンタをインクリメント
					// 重複した回数が5回を超えた場合
					if (mDuplicatedCount > 5) {
						// サーバ側でエラーが発生していると判断する
						response.setStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_INTERNAL_SERVER_ERROR);
						return;
					}
				}
				// 初めてプレイリストを送信する or 最後に送信したプレイリストと一致していない
				else {
					mDuplicatedCount = 0; // カウンタを初期化
					mLastPlaylist = responseBody; // 最後に送信したプレイリストを更新
				}

				byte[] body = responseBody.getBytes("UTF-8");
				HttpEntity entity = new ByteArrayEntity(body);
				response.setHeader("Content-Type", "application/x-mpegurl");
				response.setHeader("Connection", "close");
				response.setEntity(entity);

				if (proxyListener != null)
					proxyListener.onProxyPlaylist(segments);
			} catch (GeneralSecurityException gse) {
				gse.printStackTrace();
				response.setStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_INTERNAL_SERVER_ERROR);
				return;
			}
		}

	}

	// String loopBody = null;
	private String mLastPlaylist = null; // 最後に送信したプレイリスト
	private int mDuplicatedCount = 0; // プレイリストの重複回数

	public class HlsSegmentHandler implements HttpRequestHandler {

		@Override
		public void handle(HttpRequest request, HttpResponse response, HttpContext httpContext)
				throws HttpException, IOException {
			String path = request.getRequestLine().getUri();
//			Log.v(TAG, "segmentHandler: path=" + path);

			// proxy via mediaSequence segment (ts stream)
			// with AES decryption
			Matcher m = Pattern.compile("/segments/(\\d+)\\.ts").matcher(path);
			if (m.matches()) {
				long sequence = Long.parseLong(m.group(1));
//				Log.v(TAG, "sequence = " + sequence);
				HlsSegment segment = segmentsCache.get(sequence);
				if (segment != null) {
//					Log.v(TAG, "original path = " + segment.getMediaPath());
					try {
						// download media and AES decryption
						byte[] mediaContent = client.downloadMedia(playlist, segment.getMediaPath(), 0, 0,
								clientListener);
						if (segment.getKeyMethod() != null && segment.getKeyContent() != null)
							mediaContent = client.decode(segment.getKeyMethod(), segment.getKeyContent(),
									segment.getKeyIv(), mediaContent, segment.getSequence(), clientListener);

						HttpEntity entity = new ByteArrayEntity(mediaContent);
						response.setHeader("Content-Type", "video/mp2t");
						response.setHeader("Connection", "close");
						response.setEntity(entity);

						if (proxyListener != null)
							proxyListener.onProxyMedia(segment.getSequence(), segment.getMediaPath());

						return;
					} catch (GeneralSecurityException gse) {
						gse.printStackTrace();
						response.setStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_INTERNAL_SERVER_ERROR);
						return;
					}
				}
			}

			response.setHeader("Content-Type", "video/mp2t");
			response.setHeader("Connection", "close");
			response.setStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_NOT_FOUND);
		}

	}

	public int getListeningPort() {
		return proxyServer.getListeningPort();
	}

	public synchronized void shutdown() {
		if (client != null)
			client.shutdown();
		if (proxyServer != null)
			proxyServer.stop();
	}

	public class ProxyServer {

		private boolean isRunning = false;
		private int serverPort = 0;

		private BasicHttpProcessor httpProcessor = null;
		private BasicHttpParams httpParams = null;
		private HttpService httpService = null;
		private HttpRequestHandlerRegistry handlerRegistry = null;

		private ListenerThread listenerThread;

		public class ListenerThread extends Thread {
			private final ServerSocket serverSocket;

			public ListenerThread(int port) throws IOException {
				this(port, 10, InetAddress.getByName("localhost"));
			}

			public ListenerThread(int port, int backlog, InetAddress bindAddr) throws IOException {
				serverSocket = new ServerSocket(port, backlog, bindAddr);
				serverSocket.setReuseAddress(true);
			}

			@Override
			public void run() {
				while (!Thread.interrupted()) {
					try {
						final Socket socket = serverSocket.accept();
						DefaultHttpServerConnection serverConnection = new DefaultHttpServerConnection();
						serverConnection.bind(socket, httpParams);

						// // single thread worker
						// try {
						// httpService.handleRequest(serverConnection, new
						// BasicHttpContext());
						// }
						// catch (HttpException e) {
						// Log.e(TAG, "client HTTP exception cause=" + e);
						// }
						// catch (IOException e) {
						// Log.e(TAG, "client I/O exception cause=" + e);
						// }
						// serverConnection.shutdown();

						// multi-thread worker
						Thread t = new WorkerThread(httpService, serverConnection, socket);
						t.setDaemon(true);
						t.start();
					} catch (SocketException e) {
						Log.e(TAG, "listener Socket exception cause=" + e);
						break;
					} catch (IOException e) {
						// I/O error connection thread
						Log.e(TAG, "listener I/O exception cause=" + e);
					}
				}
				Log.v(TAG, "listener complete.");
			}
		}

		public ProxyServer(int port) throws IOException {
			this.serverPort = port;

			httpProcessor = new BasicHttpProcessor();
			httpProcessor.addInterceptor(new ResponseDate());
			httpProcessor.addInterceptor(new ResponseServer());
			httpProcessor.addInterceptor(new ResponseContent());
			httpProcessor.addInterceptor(new ResponseConnControl());

			httpParams = new BasicHttpParams();
			httpParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
			httpParams.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);

			handlerRegistry = new HttpRequestHandlerRegistry();
			handlerRegistry.register("/0.m3u8", new HlsPlaylistHandler());
			handlerRegistry.register("/segments/*", new HlsSegmentHandler());

			httpService = new HttpService(httpProcessor, new DefaultConnectionReuseStrategy(),
					new DefaultHttpResponseFactory());
			httpService.setHandlerResolver(handlerRegistry);
			httpService.setParams(httpParams);
		}

		public int getListeningPort() {
			if (listenerThread != null)
				return listenerThread.serverSocket.getLocalPort();
			return 0;
		}

		public synchronized void start() throws IOException {
			if (isRunning)
				return;
			listenerThread = new ListenerThread(serverPort);
			listenerThread.start();
			isRunning = true;
		}

		public synchronized void stop() {
			if (!isRunning)
				return;
			try {
				listenerThread.serverSocket.close();
				listenerThread = null;
			} catch (Exception e) {
			}
			isRunning = false;
		}
	}

	private class WorkerThread extends Thread {
		private final HttpService httpService;
		private final HttpServerConnection serverConnection;
		private final Socket socket;

		public WorkerThread(final HttpService httpService, final HttpServerConnection serverConnection,
				final Socket socket) {
			this.httpService = httpService;
			this.serverConnection = serverConnection;
			this.socket = socket;
		}

		@Override
		public void run() {
			HttpContext context = new WorkerHttpContext(this.socket);
			try {
				while (!Thread.interrupted() && this.serverConnection.isOpen()) {
					this.httpService.handleRequest(this.serverConnection, context);
					this.serverConnection.shutdown();
				}
			} catch (ConnectionClosedException e) {
				Log.e(TAG, "client connection closed cause=" + e);
			} catch (SocketTimeoutException e) {
				Log.e(TAG, "client socket timeout cause=" + e);
			} catch (HttpException e) {
				Log.e(TAG, "client HTTP exception cause=" + e, e);
			} catch (HlsHttpException e) {
				Log.e(TAG, "client HlsHttp exception cause=" + e + ": statusCode:" + e.getStatusCode(), e);
			} catch (IOException e) {
				Log.e(TAG, "client I/O exception cause=" + e, e);
			} finally {
				try {
					if (serverConnection.isOpen()) {
						serverConnection.shutdown();
					}
				} catch (IOException ignore) {
				}
			}
			Log.v(TAG, "worker complete.");
		}
	}

	public static class WorkerHttpContext extends BasicHttpContext {
		private final Socket socket;

		public WorkerHttpContext(Socket socket) {
			super(null);
			this.socket = socket;
		}

		public Socket getSocket() {
			return socket;
		}
	}

}
