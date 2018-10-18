package jp.faraopro.play.hls;

/**
 */
public interface HlsClientListener {
	void onErrorEmptyPlaylist(String playlistPath, byte[] contents);

	void onErrorMediaDecryption(long sequence, Throwable cause);

	void onErrorDownloadKey(String keyPath, Throwable cause);

	void onErrorDownloadMedia(String mediaPath, Throwable cause);

	void onErrorDownloadPlaylist(String playlistPath, Throwable cause);
}
