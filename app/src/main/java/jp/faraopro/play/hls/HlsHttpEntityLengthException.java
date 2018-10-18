package jp.faraopro.play.hls;

import java.io.IOException;

public class HlsHttpEntityLengthException extends IOException {
	private static final long serialVersionUID = 1L;

	public HlsHttpEntityLengthException(String message) {
		super(message);
	}
}
