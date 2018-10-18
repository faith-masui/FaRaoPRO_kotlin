package jp.faraopro.play.hls;

import java.util.List;

/**
 */
public interface HlsProxyListener {
	void onProxyPlaylist(List<String> m3u8);

	void onProxyMedia(long sequence, String media);
}
