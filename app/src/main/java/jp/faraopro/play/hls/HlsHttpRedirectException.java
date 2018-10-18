package jp.faraopro.play.hls;

import java.net.URI;

/**
 *
 */
public class HlsHttpRedirectException extends HlsHttpException {
	private static final long serialVersionUID = 1L;
	private final URI location;

	public HlsHttpRedirectException(int statusCode, URI location) {
		super(statusCode);
		this.location = location;
	}

	public URI getLocation() {
		return this.location;
	}
}
