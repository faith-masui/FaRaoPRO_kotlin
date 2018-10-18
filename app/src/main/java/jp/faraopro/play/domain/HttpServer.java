// Copyright 2010 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package jp.faraopro.play.domain;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import android.text.TextUtils;
import jp.faraopro.play.common.FRODebug;

/**
 * サーバ機能を提供する基底クラス
 * 
 * @author ksu
 * 
 */
public abstract class HttpServer implements Runnable {
	private static final boolean DEBUG = true;
	private int port = 0;
	private boolean isRunning = true;
	private ServerSocket socket;
	private Thread thread;
	private boolean simulateStream = false;
	protected String errorCode = null;
	protected String internalPath;

	/**
	 * Returns the port that the server is running on. The host is localhost
	 * (127.0.0.1).
	 * 
	 * @return A port number assigned by the OS.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Prepare the server to start.
	 * 
	 * This only needs to be called once per instance. Once initialized, the
	 * server can be started and stopped as needed.
	 */
	public void init() {
		try {
			socket = new ServerSocket(port, 0, InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 }));
			// TODO ソケットの使用可否
			socket.setSoTimeout(5000);
			port = socket.getLocalPort();
		} catch (UnknownHostException e) {
			// Log.e(TAG, "Error initializing server", e);
		} catch (IOException e) {
			// Log.e(TAG, "Error initializing server", e);
		}
	}

	/**
	 * Start the server.
	 */
	public void start() {
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Stop the server.
	 * 
	 * This stops the thread listening to the port. It may take up to five
	 * seconds to close the service and this call blocks until that occurs.
	 */
	public void stop() {
		isRunning = false;
		if (thread == null) {
			// Log.w(TAG, "Server was stopped without being started.");
			return;
		}
		thread.interrupt();
		try {
			thread.join(5000);
		} catch (InterruptedException e) {
			// Log.w(TAG, "Server was interrupted while stopping", e);
		}
	}

	/**
	 * Determines if the server is running (i.e. has been <code>start</code>ed
	 * and has not been <code>stop</code>ed.
	 * 
	 * @return <code>true</code> if the server is running, otherwise
	 *         <code>false</code>
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Sets a value that determines whether the server will simulate an
	 * open-ended stream by looping the content of the DataSource. This is
	 * false, by default.
	 * 
	 * @param simulateStreaming
	 *            <code>true</code> to loop content, else <code>false</code>
	 */
	protected void setSimulateStream(boolean simulateStreaming) {
		simulateStream = simulateStreaming;
	}

	/**
	 * Determines if the server is configured to loop content, simulating an
	 * open-ended stream. This is false, by default.
	 * 
	 * @return <code>true</code> to loop content, else <code>false</code>
	 */
	public boolean isSimulatingStream() {
		return simulateStream;
	}

	// TODO: This could be hidden inside a private class.
	/**
	 * This is used internally by the server and should not be called directly.
	 */

	@Override
	public void run() {
		while (isRunning) {
			try {
				Socket client = socket.accept();
				if (client == null) {
					continue;
				}
				String request = readRequest(client);
				if (!TextUtils.isEmpty(request)) {
					DataSource data = getData(request);
					processRequest(data, client);
				}
			} catch (SocketTimeoutException e) {
				// Do nothing
			} catch (IOException e) {
				FRODebug.logE(getClass(), e.getMessage(), DEBUG);
			}
		}
	}

	/**
	 * Returns a DataSource object for a given request.
	 * 
	 * This method must be implemented by subclasses.
	 * 
	 * @param request
	 *            The path of the resource requested. e.g. /index.html
	 * @return A DataSource that provides meta-data and a stream to the
	 *         resource.
	 */
	protected abstract DataSource getData(String request);

	/*
	 * Get the HTTP request line from the client and parse out the path of the
	 * request.
	 * 
	 * @return a URL-decoded string of the request.
	 */
	private String readRequest(Socket client) {
		InputStream is;
		String firstLine = null;
		mRangeRequest = -1;
		try {
			is = client.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is), 2048);
			firstLine = reader.readLine();
			// クライアントからリクエストが来るが、1行目を読むと空であるケースがある
			if (firstLine == null)
				return null;

			String line;
			while (!TextUtils.isEmpty((line = reader.readLine()))) {
				if (line.contains("Range:")) {
					String[] rangetag = (line.split("="))[1].split("-");
					mRangeRequest = Long.parseLong(rangetag[0]);
				}
			}
			StringTokenizer st = new StringTokenizer(firstLine);
			st.nextToken(); // Skip method
			return URLDecoder.decode(st.nextToken(), "UTF-8");// "x-www-form-urlencoded");
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	private long mRangeRequest = -1;

	/*
	 * Sends the HTTP response to the client, including headers (as applicable)
	 * and content.
	 */
	private void processRequest(DataSource dataSource, Socket client) throws IllegalStateException, IOException {
		if (dataSource == null) {
			client.close();
			return;
		}

		// StringBuilder httpString = new StringBuilder();
		// httpString.append(new BasicStatusLine(new ProtocolVersion("HTTP", 1,
		// 0), HttpStatus.SC_OK, "OK"));
		// httpString.append("\r\n");
		//
		// httpString.append("Content-Type:
		// ").append(dataSource.getContentType());
		// httpString.append("\r\n");
		//
		// // Some content (e.g. streams) does not define a length
		// long length = dataSource.getContentLength();
		// if (length >= 0) {
		// httpString.append("Content-Length: ").append(length);
		// httpString.append("\r\n");
		// }

		String headers = "";
		String mime = dataSource.getContentType();
		if (mRangeRequest > 0) {
			long length = dataSource.getContentLength();
			headers += "HTTP/1.1 206 Partial Content\r\n";
			headers += "Content-Type: " + mime + "\r\n";
			headers += "Accept-Ranges: bytes\r\n";
			headers += "Content-Length: " + (length - mRangeRequest) + "\r\n";
			headers += "Content-Range: bytes " + mRangeRequest + "-" + (length - 1) + "/" + length + "\r\n";
			headers += "Connection: Keep-Alive\r\n";
			headers += "\r\n";
		} else {
			long length = dataSource.getContentLength();
			headers += "HTTP/1.1 200 OK\r\n";
			headers += "Content-Type: " + mime + "\r\n";
			headers += "Accept-Ranges: bytes\r\n";
			headers += "Content-Length: " + length + "\r\n";
			headers += "Connection: Keep-Alive\r\n";
			headers += "\r\n";
		}

		InputStream data = null;
		try {
			data = dataSource.createInputStream();
			if (mRangeRequest > 0) {
				skipFully(data, mRangeRequest);
			}
			byte[] buffer = headers.getBytes();
			int readBytes = -1;
			client.getOutputStream().write(buffer, 0, buffer.length);

			// Start sending content.
			byte[] buff = new byte[1024 * 64];
			while (isRunning) {
				readBytes = data.read(buff, 0, buff.length);
				if (readBytes == -1) {
					break;
				}
				client.getOutputStream().write(buff, 0, readBytes);
			}
		} catch (SocketException e) {
			// Ignore when the client breaks connection
			FRODebug.logE(getClass(), e.getMessage(), DEBUG);
		} catch (IOException e) {
			FRODebug.logE(getClass(), e.getMessage(), DEBUG);
		} catch (Exception e) {
			FRODebug.logE(getClass(), e.getMessage(), DEBUG);
		} finally {
			if (data != null) {
				data.close();
			}
			client.close();
		}
	}

	public void skipFully(InputStream in, long n) throws IOException {
		while (n > 0) {
			long amt = in.skip(n);
			if (amt == 0) {
				// Force a blocking read to avoid infinite loop
				if (in.read() == -1) {
					throw new EOFException();
				}
				n--;
			} else {
				n -= amt;
			}
		}
	}

	/**
	 * An abstract class that provides meta-data and access to a stream for
	 * resources.
	 */
	protected abstract class DataSource {

		/**
		 * Returns a MIME-compatible content type (e.g. "text/html") for the
		 * resource. This method must be implemented.
		 * 
		 * @return A MIME content type.
		 */
		public abstract String getContentType();

		/**
		 * Creates and opens an input stream that returns the contents of the
		 * resource. This method must be implemented.
		 * 
		 * @return An <code>InputStream</code> to access the resource.
		 * @throws IOException
		 *             If the implementing class produces an error when opening
		 *             the stream.
		 */
		public abstract InputStream createInputStream() throws IOException;

		/**
		 * Returns the length of resource in bytes.
		 * 
		 * By default this returns -1, which causes no content-type header to be
		 * sent to the client. This would make sense for a stream content of
		 * unknown or undefined length. If your resource has a defined length
		 * you should override this method and return that.
		 * 
		 * @return The length of the resource in bytes.
		 */
		public long getContentLength() {
			return -1;
		}

	}

}