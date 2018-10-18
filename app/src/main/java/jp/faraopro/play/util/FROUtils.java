package jp.faraopro.play.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Locale;

import jp.faraopro.play.BuildConfig;
import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BootActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.app.FROHandler;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.common.Flavor;
import jp.faraopro.play.mclient.IMCMusicItemInfo;
import jp.faraopro.play.mclient.IMCMusicItemList;
import jp.faraopro.play.mclient.MCMusicItemInfo;
import jp.faraopro.play.mclient.MCMusicItemList;
import jp.faraopro.play.view.CustomListItem;

/**
 * FaRao 用のユーティリティクラス
 *
 * @author AIM Corporation
 */
public class FROUtils {
    public static final String PATH_FARAO;
    public static final String PATH_CACHE;

    static {
        PATH_FARAO = BuildConfig.DEBUG ? "/FaraoPRO_DEBUG" : "/FaraoPRO";
        PATH_CACHE = Environment.getExternalStorageDirectory().getPath() + (BuildConfig.DEBUG ? "/FaRaoCache_DEBUG" : "/FaRaoCache");
    }

    public static final String PATH_TRACK = "/mcmic";
    public static final String PATH_JACKET = "/mcjck";
    public static final String PATH_AD = "/mcad";
    public static final String PATH_INTERRUPT = "interrupt_";
    public static final String PATH_OFFLINE_TRACK = "/system/media/farao";

    /**
     * FaRao のデータが格納されるディレクトリを返す
     *
     * @return 外部ストレージのパス + /FaraoPRO
     */
    public static String getFaraoDirectory() {
        String path;
        if ((path = Utils.getExternalDirectory()) != null)
            path += PATH_FARAO;

        return path;
    }

    /**
     * 楽曲データが保存されるディレクトリを返す
     *
     * @return 外部ストレージのパス + /FaraoPRO + /mcmic
     */
    public static String getTrackPath() {
        String path = null;
        if ((path = getFaraoDirectory()) != null)
            path += PATH_TRACK;

        return path;
    }

    /**
     * ジャケットデータが保存されるディレクトリを返す
     *
     * @return 外部ストレージのパス + /FaraoPRO + /mcjck
     */
    public static String getJacketPath() {
        String path = null;
        if ((path = getFaraoDirectory()) != null)
            path += PATH_JACKET;

        return path;
    }

    /**
     * 広告データが保存されるディレクトリを返す
     *
     * @return 外部ストレージのパス + /FaraoPRO + /mcad
     */
    public static String getAdPath() {
        String path = null;
        if ((path = getFaraoDirectory()) != null)
            path += PATH_AD;

        return path;
    }

    public static String getInterruptTrackName(String id) {
        return PATH_INTERRUPT + id;
    }

    /**
     * 該当する割り込み音源があるかどうか確認する
     *
     * @param context
     * @param id
     * @return
     */
    public static boolean existPackageFile(Context context, String id) {
        boolean exist = false;
        try {
            exist = context.openFileInput(getInterruptTrackName(id)) != null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return exist;
    }

    public static void showPackageFiles(Context context) {
        if (context == null)
            return;

        String[] files = context.fileList();
        FRODebug.logI(FROUtils.class, "---------- START PACKAGE FILE", true);
        if (files != null && files.length > 0) {
            for (String s : files) {
                FRODebug.logI(FROUtils.class, s, true);
            }
        }
        FRODebug.logI(FROUtils.class, "---------- END PACKAGE FILE", true);
    }

    public static IMCMusicItemList getOfflineTrackList() {
        IMCMusicItemList trackList = new MCMusicItemList();
        // String externalDir = Utils.getExternalDirectory() +
        // PATH_OFFLINE_TRACK;
        File dir = new File(PATH_OFFLINE_TRACK);
        if (dir.exists()) {
            String[] filenameList = dir.list();
            if (filenameList != null && filenameList.length > 0) {
                for (String filename : filenameList) {
                    File file = new File(PATH_OFFLINE_TRACK + "/" + filename);
                    if (Utils.isMusicFile(file)) {
                        MCMusicItemInfo info = new MCMusicItemInfo();
                        info.setStringValue(IMCMusicItemInfo.MCDB_ITEM_OUTPUT_PATH, file.getAbsolutePath());
                        trackList.setInfo(info);
                    }
                }
            }
        }

        return trackList;
    }

    public static void outputLog(String msg) {
        if (!BuildConfig.DEBUG)
            return;

        String path = Utils.getExternalDirectory() + "/FaRaoDebug/log.txt";
        File file = new File(path);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdir();
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            // 作れないならあきらめろん
            catch (IOException e) {
            }
        }
        Utils.writeExStorage(file, Utils.getTag() + " " + msg);
    }

    /**
     * サーバに対して network/ping を呼び出し ResponseCode を取得する<br>
     * このメソッド自体は同期処理のため、呼び出し元で必要に応じて非同期で実行してください
     *
     * @return ステータスコード
     */
    public static int ping() {
        String url = Flavor.MC_URL_BASE + "/network/ping";
        return Utils.checkServerResponse(url);
    }

    public static String canAccess(Context context) {
        String cause = null;
        if (!Utils.getNetworkState(context)) {
            cause = Consts.EMERGENCY_CAUSE_NETWORK;
        } else if (!(ping() == HttpURLConnection.HTTP_OK)) {
            cause = Consts.EMERGENCY_CAUSE_PING;
        }
        return cause;
    }

    public static boolean isFROHandlerRunning(Context context) {
        return Utils.isServiceRunning(context, FROHandler.class);
    }

    /**
     * パッケージプライベートなファイルの最終更新日を現在日時に変更する
     *
     * @param context
     * @param fileName ファイル名称
     */
    public static void updateLastModified(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        file.setLastModified(Utils.getNowTime());
    }

    /**
     * 最終更新日が1週間以上前の割り込みファイルをすべて削除する
     *
     * @param context
     */
    public static void deleteOldInterruptFile(Context context) {
        deleteOldFile(context, PATH_INTERRUPT, 7);
    }

    /**
     * パッケージプライベートフォルダ内にある最終更新日が指定日数以上のファイルを全て削除する<br>
     * 対象ファイルは fileHeader で指定された文字列と前方一致するもののみ
     *
     * @param context
     * @param fileHeader   ファイル識別文字列、null or 空文字 を指定すると全ファイルを対象とする
     * @param numberOfDays 指定日数
     */
    public static void deleteOldFile(Context context, String fileHeader, int numberOfDays) {
        File dir = context.getFilesDir();
        long currentTime = Utils.getNowTime();
        String[] fileList = context.fileList();
        for (int i = 0; i < fileList.length; i++) {
            if (fileHeader.equals("") || fileList[i].startsWith(fileHeader)) {
                File file = new File(dir, fileList[i]);
                if ((currentTime - file.lastModified()) > (Utils.VALUE_OF_ONEDAY * numberOfDays)) {
                    context.deleteFile(fileList[i]);
                }
            }
        }
    }

    private static boolean METHOD_LOCK_DELETE_OLDHISTORY_THUMBNAILS = false;

    public static void deleteOldHistoryThumbnails(Context context) {
        if (!METHOD_LOCK_DELETE_OLDHISTORY_THUMBNAILS) {
            METHOD_LOCK_DELETE_OLDHISTORY_THUMBNAILS = true;
            // getting file names array in app private area.
            String[] fileNames = context.fileList();
            if (fileNames == null || fileNames.length <= 0)
                return;

            // create thumbnail names list from above.
            ArrayList<String> thumbnaiPathlList = new ArrayList<String>();
            for (String s : fileNames) {
                if (s.contains(Consts.PRIVATE_PATH_THUMB_HISTORY))
                    thumbnaiPathlList.add(s);
            }
            if (thumbnaiPathlList.size() <= 0)
                return;

            // getting music and good history data from music history db.
            ArrayList<CustomListItem> musicHistoryList = FROForm.getInstance().getHistoryList(context);
            ArrayList<CustomListItem> goodHistoryList = FROForm.getInstance().getHistoryList(context);
            if ((musicHistoryList == null || musicHistoryList.size() <= 0)
                    && (goodHistoryList == null || goodHistoryList.size() <= 0)) {
                // there is no history data, so delete all thumbnails.
                for (String path : thumbnaiPathlList) {
                    Utils.deleteBitmap(path, context);
                }
                return;
            }

            // create thumbnail list from history data.
            ArrayList<String> thumnailPathListFromDb = new ArrayList<String>();
            if (musicHistoryList != null && musicHistoryList.size() > 0) {
                for (CustomListItem musicHistoryItem : musicHistoryList) {
                    String path = Consts.PRIVATE_PATH_THUMB_HISTORY + musicHistoryItem.getId();
                    thumnailPathListFromDb.add(path);
                }
            }
            if (goodHistoryList != null && goodHistoryList.size() > 0) {
                for (CustomListItem goodHistoryItem : goodHistoryList) {
                    String path = Consts.PRIVATE_PATH_THUMB_HISTORY + goodHistoryItem.getId();
                    thumnailPathListFromDb.add(path);
                }
            }

            // checking whether target path is included in history data in db.
            for (String checkTarget : thumbnaiPathlList) {
                if (!thumnailPathListFromDb.contains(checkTarget)) {
                    Utils.deleteBitmap(checkTarget, context);
                }
            }
            METHOD_LOCK_DELETE_OLDHISTORY_THUMBNAILS = false;
        }
    }

    public static void showUpdateNotification(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, BootActivity.class);
        Bundle args = new Bundle();
        args.putInt("SHOW_MODE", MainActivity.SETTING);
        intent.putExtras(args);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, R.string.notification_id_update, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

    //    // 通知バーの内容を決める
        Notification ntc = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.icon_notification)
                .setTicker("Fans' Shop BGM").setWhen(System.currentTimeMillis())
                .setContentTitle(context.getString(R.string.msg_available_new_version))
                .setContentText(context.getString(R.string.msg_encourage_update)).setContentIntent(contentIntent)
                .build();

        manager.notify(R.string.notification_id_update, ntc);
    }

    public static void cancelUpdateNotification(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(R.string.notification_id_update);
    }

    public static String getDeviceId(Context context) {
        final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (!TextUtils.isEmpty(deviceId)) {
            return deviceId;
        } else {
            return android.os.Build.SERIAL;
        }
    }

    public static String getDeviceIdRef(Context context) {
        String serialNo = null;
        Class<?> systemProperties;
        try {
            systemProperties = Class.forName("android.os.SystemProperties");
            Method get = systemProperties.getMethod("get", String.class);
            serialNo = (String) get.invoke(systemProperties, "ro.custom_serialno");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(serialNo))
            serialNo = getDeviceId(context);

        return serialNo;
    }

    public static void dumpNativeHeap() {
        FRODebug.logI(FROUtils.class,
                "NATIVE HEAP : " + Debug.getNativeHeapAllocatedSize() + " / " + Debug.getNativeHeapSize(), true);
    }

    /**
     * 現在の端末の設定が日本語か判別する
     *
     * @return true:Japanese, false:other wise
     */
    public static boolean isPrimaryLanguage() {
        return Utils.getLanguage().equalsIgnoreCase(Locale.JAPANESE.getLanguage());
    }

    public static final void showToast(Context context, String message) {
        if (BuildConfig.DEBUG)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
