package jp.faraopro.play.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import jp.faraopro.play.util.Utils;

public class Updater {
	private static final boolean DEBUG = true;

	public static final String TEMPORARY_FILE_NAME = "FaRao_pro_staging.apk";
	private static final String TEMPORARY_DIRECTORY_NAME = "FaRaoPRO_tmp";
	private static final String[] HEADEDR_TAG = { "X-UPDATE-PROAPK" };
	private static final String[] HEADEDR_VALUE = { "1NAe2R9zOnR1qMIGRdClLJ" };
	public static Updater instance;

	private boolean mAvailableUpdate = false;
	private int mCurrentVersion;
	private int mLatestVersion;
	private int mLatestStatusCode = HttpURLConnection.HTTP_OK;
	private int mLatestContentSize;
	private String mXmlUrl;
	private String mFileUrl;
	private String mHashByXml;
	public IUpdaterListener mListener;
	public IProgressListener mProgressListener;

	public interface IUpdaterListener {
		public void onUpdate(boolean available);

		public void onDownload(int statusCode);
	}

	public interface IProgressListener {
		public void onProgress(int currentSize, int totalSize);
	}

	private Updater() {
	}

	public static Updater getInstance() {
		if (instance == null)
			instance = new Updater();

		// FRODebug.logD(Updater.class, "instance = " + instance.hashCode(),
		// DEBUG);
		return instance;
	}

	/**
	 * アップデートの有無を確認する<br>
	 * 非同期処理であり、確認結果は listener を通じて通知される
	 * 
	 * @param context
	 * @param url
	 *            アップデート確認先の URL
	 * @param listener
	 *            コールバックリスナ
	 */
	public void checkUpdate(Context context, final String url, IUpdaterListener listener) {
		this.mListener = listener;
		mAvailableUpdate = false;

		// 現在のアプリのバージョンコードを取得、取れなかったらあきらめる
		try {
			getCurrentVersionInfo(context);
		} catch (Exception e1) {
			e1.printStackTrace();
			if (mListener != null) {
				mListener.onUpdate(mAvailableUpdate);
				mListener = null;
			}
			return;
		}

		// 別スレッドを立て、サーバに最新版のバージョンコードを問い合わせる
		Thread woker = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					getLatestVersionInfo(url);
					if (mCurrentVersion < mLatestVersion) {
						mAvailableUpdate = true;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				mListener.onUpdate(mAvailableUpdate);
			}
		});
		woker.start();
	}

	public boolean downloadApk(IUpdaterListener listener, IProgressListener progressListener) {
		this.mListener = listener;
		this.mProgressListener = progressListener;
		boolean success = true;
		// ダウンロード先のURLがないとき
		if (TextUtils.isEmpty(mFileUrl)) {
			success = false;
		} else {
			// 別スレッドを立て、サーバからapkをダウンロード
			Thread woker = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						int statusCode = downloadUpdateFile(mFileUrl);
						mListener.onDownload(statusCode);
					} catch (Exception e) {
						mListener.onDownload(HttpURLConnection.HTTP_CLIENT_TIMEOUT);
					}
				}
			});
			woker.start();
		}

		return success;
	}

	public void reset() {
		mAvailableUpdate = false;
		mCurrentVersion = -1;
		mLatestVersion = -1;
		mLatestStatusCode = HttpURLConnection.HTTP_OK;
		mXmlUrl = null;
		mFileUrl = null;
		mListener = null;
	}

	private void getCurrentVersionInfo(Context context) throws Exception {
		PackageManager pm = context.getPackageManager();
		String packageName = context.getPackageName();
		PackageInfo info = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);

		mCurrentVersion = info.versionCode;
	}

	private void getLatestVersionInfo(String url) throws Exception {
		mXmlUrl = url;
		HashMap<String, String> map = parseUpdateXml(mXmlUrl);
		mLatestVersion = Integer.parseInt(map.get("versionCode"));
		mHashByXml = map.get("digest");
		mFileUrl = map.get("url");
	}

	private InputStream httpGet(String url) throws Exception {
		InputStream is = null;
		URLConnection connection = new URL(url).openConnection();
		connection.setConnectTimeout(10 * 1000);
		connection.setReadTimeout(10 * 1000);
		for (int i = 0; i < HEADEDR_TAG.length; i++) {
			connection.setRequestProperty(HEADEDR_TAG[i], HEADEDR_VALUE[i]);
		}
		connection.connect();
		String header = connection.getHeaderField(null);
		String[] code = null;
		if (header != null)
			code = header.split(" ");
		if (code != null && TextUtils.isDigitsOnly(code[1]))
			mLatestStatusCode = Integer.parseInt(code[1]);
		else
			mLatestStatusCode = HttpURLConnection.HTTP_CLIENT_TIMEOUT;
		if (mLatestStatusCode == HttpURLConnection.HTTP_OK) {
			is = connection.getInputStream();
			mLatestContentSize = connection.getContentLength();
		}

		return is;
	}

	// private InputStream httpGet(String url) throws Exception {
	// InputStream is = null;
	// DefaultHttpClient client = new DefaultHttpClient();
	// HttpParams httpParams = client.getParams();
	// HttpConnectionParams.setConnectionTimeout(httpParams, 500 * 1000);
	// HttpConnectionParams.setSoTimeout(httpParams, 500 * 1000);
	// HttpGet get = new HttpGet(url);
	// for (int i = 0; i < HEADEDR_TAG.length; i++) {
	// get.setHeader(HEADEDR_TAG[i], HEADEDR_VALUE[i]);
	// }
	// HttpResponse res = client.execute(get);
	// mLatestStatusCode = res.getStatusLine().getStatusCode();
	// if (mLatestStatusCode == HttpURLConnection.HTTP_OK) {
	// is = res.getEntity().getContent();
	// mLatestContentSize = (int) res.getEntity().getContentLength();
	// }
	//
	// return is;
	// }

	private HashMap<String, String> parseUpdateXml(String url) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		InputStream is = httpGet(url);
		BufferedInputStream bis = new BufferedInputStream(is);
		DocumentBuilderFactory document_builder_factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder document_builder = document_builder_factory.newDocumentBuilder();
		Document document = document_builder.parse(bis);
		Element root = document.getDocumentElement();
		if (root.getTagName().equals("update")) {
			NodeList nodelist = root.getChildNodes();
			for (int j = 0; j < nodelist.getLength(); j++) {
				Node node = nodelist.item(j);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String name = element.getTagName();
					String value = element.getTextContent().trim();
					map.put(name, value);
				}
			}
		}
		bis.close();
		is.close();
		return map;
	}

	int mRetryTime = 4;

	private int downloadUpdateFile(String url) throws Exception {
		mRetryTime = 5;
		InputStream is = null;
		while (is == null && mRetryTime-- > 0) {
			try {
				is = httpGet(url);
			} catch (UnknownHostException e) {
			}
			if (is == null)
				Thread.sleep(2000);
		}

		if (is != null) {
			MessageDigest digest = MessageDigest.getInstance("SHA");
			FileOutputStream fos;
			File file = new File(getFileDir());
			fos = new FileOutputStream(file.getAbsolutePath());
			byte[] buffer = new byte[1024 * 256];
			int read = is.read(buffer);
			while (read >= 0) {
				if (mProgressListener != null)
					mProgressListener.onProgress(read, mLatestContentSize);
				fos.write(buffer, 0, read);
				digest.update(buffer, 0, read);
				read = is.read(buffer);
			}
			fos.flush();
			is.close();
			fos.close();
			byte[] hash = digest.digest();
			digest.reset();
			if (mHashByXml != null && !mHashByXml.equals(toHexString(hash))) {
				mLatestStatusCode = HttpURLConnection.HTTP_NO_CONTENT;
			}
		} else {
			mLatestStatusCode = HttpURLConnection.HTTP_CLIENT_TIMEOUT;
		}

		return mLatestStatusCode;
	}

	public static String getFileDir() {
		return Utils.getExternalDirectory() + "/" + TEMPORARY_FILE_NAME;
	}

	private String toHexString(byte[] arr) {
		StringBuffer buff = new StringBuffer(arr.length * 2);
		for (int i = 0; i < arr.length; i++) {
			String b = Integer.toHexString(arr[i] & 0xff);
			if (b.length() == 1) {
				buff.append("0");
			}
			buff.append(b);
		}
		return buff.toString();
	}

	public boolean ismAvailableUpdate() {
		return mAvailableUpdate;
	}

	public static void deleteApk() {
		File file = new File(Utils.getExternalDirectory() + "/" + TEMPORARY_FILE_NAME);
		Utils.deleteFile(file);
	}

}
