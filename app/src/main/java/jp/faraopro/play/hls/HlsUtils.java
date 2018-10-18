package jp.faraopro.play.hls;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

/**
 *
 */
public class HlsUtils {
	private static final String TAG = "HlsUtils";

	private HlsUtils() {
	}

	public static String findAdaptivePlaylist(List<String> m3u8, int indexOrBitrate) {
		if (indexOrBitrate < 0) {
			return findVariantPlaylist(m3u8, countVariant(m3u8) - indexOrBitrate);
		}
		if (indexOrBitrate < 1000) {
			return findVariantPlaylist(m3u8, indexOrBitrate % countVariant(m3u8));
		}
		int bitrate = 0;
		String result = null;
		for (int i = 0; i < countVariant(m3u8); i++) {
			String info = findVariant(m3u8, i);
			String playlist = findVariantPlaylist(m3u8, i);
			Matcher bw = Pattern.compile(".*BANDWIDTH=(\\d+).*").matcher(info);
			if (bw.matches()) {
				int bandwidth = Integer.parseInt(bw.group(1));
				int d1 = Math.max(indexOrBitrate, bandwidth) - Math.min(indexOrBitrate, bandwidth);
				int d2 = Math.max(indexOrBitrate, bitrate) - Math.min(indexOrBitrate, bitrate);
				if (d1 < d2) {
					bitrate = bandwidth;
					result = playlist;
				}
				Log.v(TAG, "candidate bitrate=" + bandwidth);
			}
		}
		Log.v(TAG, "adaptive bitrate=" + bitrate);
		return result;
	}

	public static String findVariant(List<String> m3u8, int index) {
		int count = 0;
		for (String s : m3u8) {
			if (s.trim().length() == 0)
				continue;
			if (s.startsWith("#EXT-X-STREAM-INF:")) {
				if (index == count) {
					return s;
				}
				count++;
			}
		}
		return null;
	}

	public static String findVariantPlaylist(List<String> m3u8, int index) {
		int count = 0;
		boolean match = false;
		for (String s : m3u8) {
			if (s.trim().length() == 0)
				continue;
			if (match) {
				return s;
			}
			if (s.startsWith("#EXT-X-STREAM-INF:")) {
				if (index == count) {
					match = true;
				}
				count++;
			}
		}
		return null;
	}

	public static int countVariant(List<String> m3u8) {
		int count = 0;
		for (String s : m3u8) {
			if (s.startsWith("#EXT-X-STREAM-INF:")) {
				count++;
			}
		}
		return count;
	}

	public static boolean isExtendedM3U(List<String> m3u8) {
		for (String s : m3u8) {
			if (s.startsWith("#EXTM3U"))
				return true;
		}
		return false;
	}

	public static boolean isVariant(List<String> m3u8) {
		for (String s : m3u8) {
			if (s.startsWith("#EXT-X-STREAM-INF:"))
				return true;
		}
		return false;
	}

	public static boolean isSegment(List<String> m3u8) {
		for (String s : m3u8) {
			if (s.startsWith("#EXTINF:"))
				return true;
		}
		return false;
	}

	public static boolean isLive(List<String> m3u8) {
		for (String s : m3u8) {
			if (s.startsWith("#EXT-X-ENDLIST"))
				return false;
		}
		return true;
	}

}
