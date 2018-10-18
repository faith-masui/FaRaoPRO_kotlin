package jp.faraopro.play.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * OSバージョンによるメソッドの差異を吸収するクラス
 * 
 * @author Aim
 * 
 */
public class Reflection {
	private static Method method;

	// メソッドの有無確認
	private static void initCompatibility(String methodName, Object obj, Class[] cls) {
		try {
			method = obj.getClass().getMethod(methodName, cls);
		} catch (NoSuchMethodException e) {
			method = null;
		}
	}

	/**
	 * getExternalFilesDir(APILevel8)と同等の動作をするメソッド
	 * 
	 * @param context
	 * @return
	 * @throws FileNotFoundException
	 */
	public static File getExternalDir(Context context) throws FileNotFoundException {
		File file = null;

		initCompatibility("getExternalFilesDir", context, new Class[] { String.class });
		if (method != null) {
			try {
				file = (File) method.invoke(context, (String) null);
			} catch (Exception e) {
				Log.e("utils.Reflection", "exception", e);
			}
		} else {
			file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/"
					+ context.getPackageName() + "/files");
		}
		if (file == null)
			throw new FileNotFoundException();
		method = null;

		return file;
	}

	/**
	 * getExternalDirで取得したファイルのパスを返す
	 * 
	 * @param context
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String getExternalDirPath(Context context) throws FileNotFoundException {
		File file = getExternalDir(context);
		String path = null;

		if (file != null) {
			path = file.getAbsolutePath();
		}

		return path;
	}

	/**
	 * overridePendingTransition(APILevel5)と同等の動作をするメソッド
	 * 
	 * @param activity
	 * @param enter
	 * @param exit
	 */
	public static void overridePendingTransition(Activity activity, int enter, int exit) {
		initCompatibility("overridePendingTransition", activity, new Class[] { int.class, int.class });
		if (method != null) {
			try {
				method.invoke(activity, enter, exit);
			} catch (Exception e) {
				Log.e("utils.Reflection", "exception", e);
			}
		}
		method = null;
	}

}
