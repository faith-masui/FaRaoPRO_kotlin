package jp.faraopro.play.domain;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;

public class FROHistoryImageCache {
	private static final HashMap<String, Bitmap> MAP_BITMAP = new HashMap<String, Bitmap>();
	private static final ArrayList<String> MAP_KEY = new ArrayList<String>();
	private static final int CAPACITY = 60;

	/**
	 * 
	 * @param key
	 * @return
	 */
	public static Bitmap getImage(String key) {

		Bitmap retBitmap = null;
		if (MAP_BITMAP.containsKey(key)) {
			retBitmap = MAP_BITMAP.get(key);
		}
		return retBitmap;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public static boolean isImageExists(String key) {
		return MAP_BITMAP.containsKey(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public static void setImage(String key, Bitmap value) {
		if (key == null) {
			return;
		}
		if (MAP_BITMAP.size() > CAPACITY) {
			String oldest = MAP_KEY.remove(0);
			// Bitmap image = null;
			if (isImageExists(oldest))
				MAP_BITMAP.remove(oldest);
			// image = MAP_BITMAP.remove(oldest);
			// if (image != null)
			// image.recycle();
		}
		MAP_BITMAP.put(key, value);
		MAP_KEY.add(key);
	}
}