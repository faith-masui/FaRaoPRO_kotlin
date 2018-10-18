package jp.faraopro.play.util;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ksu on 2016/06/27.
 */
public class NetworkUtil {
    private static final String TAG = NetworkUtil.class.getSimpleName();
    private static final int DOWNLOAD_BUFFER_SIZE = 4096;

    public interface ProgressCallback {
        void onProgress(long contentLength, long downloadedSize);
    }

    /**
     * HTTP 通信で取得した ResponseBody をファイル書き込みする
     *
     * @param fileName      ファイルのパス
     * @param content       ResponseBody
     * @param contentLength ファイルの content-length
     * @param callback      ダウンロードの進捗状態を受け取るコールバック
     * @throws IOException
     */
    public static void writeResponseBodyToDisk(
            String fileName, InputStream content, long contentLength, ProgressCallback callback) throws IOException {
        if (TextUtils.isEmpty(fileName) || content == null)
            throw new IllegalArgumentException();

        File outputFile = new File(fileName);
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdir();
        }
        OutputStream outputStream = null;
        try {
            byte[] buffer = new byte[DOWNLOAD_BUFFER_SIZE];
            long downloadedSize = 0;
            if (callback != null) callback.onProgress(contentLength, downloadedSize);
            outputStream = new FileOutputStream(outputFile);
            while (true) {
                int read = content.read(buffer);
                if (read == -1) {
                    break;
                }
                if (contentLength > 0 && (contentLength - downloadedSize) < read)
                    read = (int) (contentLength - downloadedSize);
                outputStream.write(buffer, 0, read);
                downloadedSize += read;
                if (callback != null) callback.onProgress(contentLength, downloadedSize);
            }
            outputStream.flush();
            checkFileSize(fileName, contentLength);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public static void writeResponseBodyToApplicationPrivate(
            Context context, String fileName, InputStream content, long contentLength) throws IOException {
        if (context == null || TextUtils.isEmpty(fileName) || content == null)
            throw new IllegalArgumentException();

        FileOutputStream fos = null;
        try {
            byte[] buffer = new byte[DOWNLOAD_BUFFER_SIZE];
            long downloadedSize = 0;
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            while (true) {
                int read = content.read(buffer);
                if (read == -1) {
                    break;
                }
                if (contentLength > 0 && (contentLength - downloadedSize) < read)
                    read = (int) (contentLength - downloadedSize);
                fos.write(buffer, 0, read);
                downloadedSize += read;
            }
            fos.flush();
            checkFileSize(context.getFilesDir().getPath() + "/" + fileName, contentLength);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 指定されたファイルの実サイズと content-length を比較し、異なっていれば例外をスローし、ファイルを削除する
     *
     * @param fileName      ファイルパス
     * @param contentLength ファイルの Content-Length
     * @throws IOException ファイルが存在しない、またはサイズが一致しない場合
     */
    public static void checkFileSize(String fileName, long contentLength) throws IOException {
        if (fileName == null) {
            throw new IllegalArgumentException();
        }
        if (contentLength <= 0)
            return;
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        if (file.length() != contentLength) {
            file.delete();
            throw new IOException();
        }
    }
}
