package jp.faraopro.play.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import jp.faraopro.play.BuildConfig;
import jp.faraopro.play.common.FRODebug;

public class Utils {
    public static final long VALUE_OF_ONEDAY = 86400000;
    public static final long VALUE_OF_ONEWEEK = VALUE_OF_ONEDAY * 7;

    private static final String TAG = Utils.class.getSimpleName();
    private static final int DEFAULT_MAX_IMAGE_SIZE = 800 * 800;

    private static final String SUPPORTED_FORMAT_3GP = "3gp";
    private static final String SUPPORTED_FORMAT_MP4 = "mp4";
    private static final String SUPPORTED_FORMAT_M4A = "m4a";
    private static final String SUPPORTED_FORMAT_AAC = "aac";
    private static final String SUPPORTED_FORMAT_FLAC = "flac";
    private static final String SUPPORTED_FORMAT_MP3 = "mp3";
    private static final String SUPPORTED_FORMAT_MID = "mid";
    private static final String SUPPORTED_FORMAT_XMF = "xmf";
    private static final String SUPPORTED_FORMAT_MXMF = "mxmf";
    private static final String SUPPORTED_FORMAT_RTTTL = "rtttl";
    private static final String SUPPORTED_FORMAT_RTX = "rtx";
    private static final String SUPPORTED_FORMAT_OTA = "ota";
    private static final String SUPPORTED_FORMAT_IMY = "imy";
    private static final String SUPPORTED_FORMAT_OGG = "ogg";
    private static final String SUPPORTED_FORMAT_MKV = "mkv";
    private static final String SUPPORTED_FORMAT_WAV = "wav";
    private static final String SUPPORTED_FORMAT_3GPP = "3gpp";
    private static final String[] SUPPORTED_FORMATS = {SUPPORTED_FORMAT_3GP, SUPPORTED_FORMAT_MP4,
            SUPPORTED_FORMAT_M4A, SUPPORTED_FORMAT_AAC, SUPPORTED_FORMAT_FLAC, SUPPORTED_FORMAT_MP3,
            SUPPORTED_FORMAT_MID, SUPPORTED_FORMAT_XMF, SUPPORTED_FORMAT_MXMF, SUPPORTED_FORMAT_RTTTL,
            SUPPORTED_FORMAT_RTX, SUPPORTED_FORMAT_OTA, SUPPORTED_FORMAT_IMY, SUPPORTED_FORMAT_OGG,
            SUPPORTED_FORMAT_MKV, SUPPORTED_FORMAT_WAV, SUPPORTED_FORMAT_3GPP};

    /**
     * @param activity
     * @return
     */
    public static float getDensity(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }

    /**
     * 外部ストレージのパスを返す
     *
     * @return null : 外部ストレージの状態が MEDIA_MOUNTED 以外だった場合
     */
    public static String getExternalDirectory() {
        String ret = null;

        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            ret = Environment.getExternalStorageDirectory().getAbsolutePath();
        }

        return ret;
    }

    /**
     * 指定ファイル、またはフォルダを削除する
     *
     * @param dirOrFile ファイル、またはフォルダ
     * @return true:削除成功、false:それ以外の場合
     */
    public static boolean deleteFile(File dirOrFile) {
        return deleteFile(dirOrFile, true);
    }

    /**
     * 指定ファイル、またはフォルダを削除する<br>
     * パラメータによって、フォルダ内のファイルのみ削除することが可能
     *
     * @param dirOrFile   ファイル、またはフォルダ
     * @param isDeleteDir 第一引数がフォルダの場合、falseを指定することでフォルダを削除しない
     * @return true:削除成功、false:それ以外の場合
     */
    public static boolean deleteFile(File dirOrFile, boolean isDeleteDir) {
        if (dirOrFile.isDirectory()) {// ディレクトリの場合
            String[] children = dirOrFile.list();// ディレクトリにあるすべてのファイルを処理する
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteFile(new File(dirOrFile, children[i]));
                if (!success) {
                    return false;
                }
            }
            if (!isDeleteDir)
                return true;
            String renamePath = dirOrFile.getPath() + "_";
            dirOrFile.renameTo(new File(renamePath));
            dirOrFile = new File(renamePath);
        }

        // 削除
        return dirOrFile.delete();
    }

    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo packageInfo = null;
        String packegeName = context.getPackageName();

        try {
            packageInfo = context.getPackageManager().getPackageInfo(packegeName, PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return packageInfo;
    }

    /**
     * アプリケーションのバージョンネームを取得する
     *
     * @param context コンテキスト
     * @return バージョンネーム
     */
    public static String getVersionName(Context context) {
        String name = null;
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null)
            name = getPackageInfo(context).versionName;

        return name;
    }

    /**
     * アプリケーションのバージョンコードを取得する
     *
     * @param context コンテキスト
     * @return バージョンコード
     */
    public static int getVersionCode(Context context) {
        int code = -1;
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null)
            code = getPackageInfo(context).versionCode;

        return code;
    }

    /**
     * 引数として与えられたBitmapインスタンスをアプリケーションプライベートな内部ストレージに保存する<br>
     *
     * @param bitmap   保存対処の画像
     * @param fileName 保存ファイル名
     * @param context  アプリケーションコンテキストなど
     */
    public static void saveBitmap(Bitmap bitmap, String fileName, Context context) {
        if (bitmap == null || fileName == null) {
            Log.e(TAG, "unexpected params \"null\"");
            return;
        }

        FileOutputStream fos;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            bitmap.compress(CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param fileName
     * @param context
     */
    public static void deleteBitmap(String fileName, Context context) {
        if (fileName == null) {
            Log.e(TAG, "unexpected params \"null\"");
            return;
        }

        context.deleteFile(fileName);
    }

    /**
     * アプリケーションプライベートな内部ストレージに保存されている画像データをBitmapインスタンスとして取得する<br>
     * 画像サイズが800 * 800より大きい場合は自動でリサイズされる<br>
     *
     * @param fileName   ファイル名
     * @param sampleSize 縮小率、2の階乗分の1のサイズで指定
     * @param context    アプリケーションコンテキストなど
     * @return null:ファイルが存在しない場合<br>
     * otherwise:ファイル名で指定した画像データ
     */
    public static Bitmap loadBitmap(String fileName, int sampleSize, Context context) {
        FileInputStream fis = null;
        Bitmap bmp = null;
        if (!TextUtils.isEmpty(fileName)) {
            try {
                fis = context.openFileInput(fileName);
                int size = (int) context.getFileStreamPath(fileName).length();
                if (size > DEFAULT_MAX_IMAGE_SIZE && sampleSize < 2) {
                    sampleSize = size / DEFAULT_MAX_IMAGE_SIZE;
                    sampleSize++;
                }
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = sampleSize;
                bmp = BitmapFactory.decodeStream(fis, null, opt);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return bmp;
    }

    /**
     * @param path
     * @return
     */
    public static Bitmap loadBitmap(String path) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, op);
        int w = op.outWidth;
        int h = op.outHeight;
        int scale = w * h;
        int sampleSize = 1;
        int index = 0;
        if (scale > DEFAULT_MAX_IMAGE_SIZE) {
            scale /= DEFAULT_MAX_IMAGE_SIZE;
            scale++;
            while (scale > (sampleSize = (int) Math.pow(2, index))) {
                index++;
            }
        }
        op.inSampleSize = sampleSize;
        op.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, op);
    }

    public static String getNowDateString(String format) {
        return getDateString(Calendar.getInstance(), format);
    }

    public static String getDateString(Calendar calendar, String format) {
        return DateFormat.format(format, calendar).toString();
    }

    public static void writeExStorage(File dir, String msg) {
        if (!BuildConfig.DEBUG)
            return;
        if (dir == null)
            return;

        try {
            FileOutputStream fos = new FileOutputStream(dir, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            PrintWriter pw = new PrintWriter(osw);
            msg = Utils.getNowDateString("yyyy/MM/dd kk:mm:ss ") + msg;
            pw.append(msg + "\n");
            pw.close();
            osw.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // public synchronized static void deleteExStorage() {
    // String SDFile =
    // android.os.Environment.getExternalStorageDirectory().getPath() +
    // "/FaRaoDebug/debugLogs.txt";
    // File file = new File(SDFile);
    // file.delete();
    // }

    public static void writePkgStorage(File file, String fileName, Context context) {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024 * 256];
            while (fis.read(buffer) > 0) {
                fos.write(buffer);
            }
            fis.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 現在時刻をミリ秒で取得する
     *
     * @return 現在時刻(ミリ秒)
     */
    public static long getNowTime() {
        long time = -1;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        time = calendar.getTimeInMillis();

        return time;
    }

    /**
     * 端末の使用言語を取得する
     *
     * @return 言語コード
     */
    public static String getLanguage() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage().toLowerCase(Locale.ENGLISH);
    }

    public static boolean isMusicFile(File file) {
        boolean isMusic = false;

        String[] tmp = file.getName().split("\\.");
        if (tmp.length > 0) {
            String fileExtension = tmp[tmp.length - 1];
            for (String sf : SUPPORTED_FORMATS) {
                if (sf.equalsIgnoreCase(fileExtension)) {
                    isMusic = true;
                    break;
                }
            }
        }

        return isMusic;
    }

    /**
     * Androidに対応している音楽ファイルの拡張子をFileFilterとして返す
     *
     * @return
     */
    public static FileFilter getMusicFileFilter() {
        return new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return isMusicFile(pathname);
            }
        };
    }

    public static boolean externalStrageIsMaunted() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }


    public static String getRFC1123Time(long time) {
        SimpleDateFormat rfc1123DateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss zzz", java.util.Locale.US);
        rfc1123DateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(time);

        return rfc1123DateFormat.format(calender.getTime());
    }

    public static FilenameFilter getFilenameFilterStartWith(String extension) {
        final String _extension = extension;
        return new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                boolean ret = name.startsWith(_extension);
                return ret;
            }
        };
    }

    /**
     * ネットワークの接続状態を返す
     *
     * @param context
     * @return true:接続中, false:その他
     */
    public static boolean getNetworkState(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null) {
            return networkInfo.isConnected();
        } else {
            return false;
        }
    }

    /**
     * 指定された URL にアクセスし、ステータスコードを返す 同期処理のため、必ず別スレッドで呼び出すこと
     *
     * @param url アクセス先の URL
     * @return -1:エラー時<br>
     * その他:status code
     */
    public static int checkServerResponse(String url) {
        int statusCode = -1;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            FRODebug.logD(Utils.class, "getRequestMethod " + connection.getRequestMethod(), true);
            statusCode = connection.getResponseCode();
            FRODebug.logD(Utils.class, "getResponseCode " + statusCode, true);
        } catch (MalformedURLException e) {
            FRODebug.logE(Utils.class, e.toString(), true);
        } catch (IOException e) {
            FRODebug.logE(Utils.class, e.toString(), true);
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return statusCode;
    }

    public static boolean isServiceRunning(Context c, Class<?> cls) {
        ActivityManager am = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> runningService = am.getRunningServices(Integer.MAX_VALUE);
        for (RunningServiceInfo i : runningService) {
            FRODebug.logD(Utils.class, "service: " + i.service.getClassName() + " : " + i.started, true);
            if (cls.getName().equals(i.service.getClassName())) {
                FRODebug.logD(Utils.class, "running", true);
                return true;
            }
        }
        return false;
    }

    /**
     * logcat 用のタグを生成する<br>
     *
     * @return [ClassName]#[MethodName]:[LineNumber]
     */
    public static String getTag() {
        final StackTraceElement trace = Thread.currentThread().getStackTrace()[4];
        final String cla = trace.getClassName();
        Pattern pattern = Pattern.compile("[\\.]+");
        final String[] splitedStr = pattern.split(cla);
        final String simpleClass = splitedStr[splitedStr.length - 1];
        final String mthd = trace.getMethodName();
        final int line = trace.getLineNumber();
        final String tag = simpleClass + "#" + mthd + ":" + line;
        return tag;
    }
}
