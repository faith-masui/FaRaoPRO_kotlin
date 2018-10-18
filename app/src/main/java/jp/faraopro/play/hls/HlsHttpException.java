package jp.faraopro.play.hls;

import java.io.IOException;

/**
 *
 */
public class HlsHttpException extends IOException {
	private static final long serialVersionUID = 1L;
	private final int statusCode;

	public HlsHttpException(int statusCode) {
		super("statusCode=" + statusCode);
		this.statusCode = statusCode;
	}

	public HlsHttpException(String message, int statusCode) {
		super(message);
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
