package jp.faraopro.play.common;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Calendar;

import jp.faraopro.play.BuildConfig;

public class MyUncaughtExceptionHandler implements UncaughtExceptionHandler {
	Context mContext;
	private UncaughtExceptionHandler mDefaultUEH;
	private static File BUG_REPORT_FILE = null;

	static {
		String sdcard = Environment.getExternalStorageDirectory().getPath();
		String path = sdcard + "/Farao_bug.txt";
		BUG_REPORT_FILE = new File(path);
	}

	public MyUncaughtExceptionHandler(Context context) {
		mContext = context;
		mDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
	}

	@Override
	public void uncaughtException(Thread th, Throwable e) {
		// catchされなかった例外は最終的にココに渡される
		try {
			saveState(e);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		mDefaultUEH.uncaughtException(th, e);
	}

	private void saveState(Throwable e) throws FileNotFoundException {
        if (!BuildConfig.DEBUG)
            return;

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		String date = calendar.getTime().toString();
		File file = BUG_REPORT_FILE;
		PrintWriter pw = null;
		pw = new PrintWriter(new FileOutputStream(file, true));
		pw.print(date + "\n");
		pw.print("---------------------\n");
		e.printStackTrace(pw);
		pw.print("---------------------\n");
		pw.print("\n");
		pw.close();
	}
}