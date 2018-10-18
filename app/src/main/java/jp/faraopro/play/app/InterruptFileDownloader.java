package jp.faraopro.play.app;

import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.domain.FROAsyncFileRequest;
import jp.faraopro.play.domain.FROPatternContentDB;
import jp.faraopro.play.domain.FROPatternContentDBFactory;
import jp.faraopro.play.mclient.MCAudioItem;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCUserInfoPreference;
import jp.faraopro.play.util.FROUtils;

public class InterruptFileDownloader {
	private static final boolean DEBUG = true;

	private List<Pair<Integer, String>> mRequests;
	private List<Future<Integer>> mCallback;
	private ExecutorService mExecutor;
	private Context mContext;
	private IDownloaderListener mListener;
	private String mKey;

	public interface IDownloaderListener {
		public void onFinish(String key);
	}

	private InterruptFileDownloader(Context context) {
		mContext = context;
	}

	public static InterruptFileDownloader getInstance(Context context) {
		InterruptFileDownloader sender = new InterruptFileDownloader(context);
        if (sender.createFailedRequest()) {
			return sender;
		} else {
			return null;
		}
	}

	public static InterruptFileDownloader getInstance(Context context, List<MCAudioItem> downloadList) {
		InterruptFileDownloader sender = new InterruptFileDownloader(context);
		if (sender.createRequest(downloadList)) {
			return sender;
		} else {
			return null;
		}
	}

    private boolean createFailedRequest() {
        FROPatternContentDB contentDb = FROPatternContentDBFactory.getInstance(mContext);
        // コンテンツ DB から Audio データを取得する(ID の重複無し)
        List<MCAudioItem> audioItemList = contentDb.findAllAudioNoDuplication();
        if (audioItemList.size() < 1)
            return false;
        // 全 Audio データの内、ファイルが端末内に存在しないものをリストに格納
        List<MCAudioItem> failedList = new ArrayList<>();
        for (MCAudioItem audioItem : audioItemList) {
            if (!FROUtils.existPackageFile(mContext, audioItem.getString(MCDefResult.MCR_KIND_AUDIO_ID))) {
                failedList.add(audioItem);
            }
        }
        // ファイルが存在しないものを全て再取得するため、リクエストを作成
        mRequests = new ArrayList<>();
        for (MCAudioItem audioItem : failedList) {
            Pair<Integer, String> requestPair = Pair.create(
                    Integer.parseInt(audioItem.getString(MCDefResult.MCR_KIND_AUDIO_ID)),
                    audioItem.getString(MCDefResult.MCR_KIND_AUDIO_URL));
            mRequests.add(requestPair);
        }
        return true;
    }

    private boolean createRequest(List<MCAudioItem> downloadList) {
        if (downloadList == null || downloadList.size() < 1)
            return false;

        mRequests = new ArrayList<>();
		ArrayList<String> duplicate = new ArrayList<String>();
		FROPatternContentDB contentDb = FROPatternContentDBFactory.getInstance(mContext);
		for (MCAudioItem mcai : downloadList) {
			String audioId = mcai.getString(MCDefResult.MCR_KIND_AUDIO_ID);
			String audioVersion = mcai.getString(MCDefResult.MCR_KIND_AUDIO_VERSION);
			MCAudioItem localAudio = contentDb.findAudioById(audioId);
			// ローカル内に id, version が同一のデータがなければダウンロードする
			if (!duplicate.contains(audioId) && (localAudio == null
					|| !localAudio.getString(MCDefResult.MCR_KIND_AUDIO_VERSION).equals(audioVersion)
					|| !FROUtils.existPackageFile(mContext, audioId))) {
				Pair<Integer, String> requestPair = Pair.create(
						Integer.parseInt(mcai.getString(MCDefResult.MCR_KIND_AUDIO_ID)),
						mcai.getString(MCDefResult.MCR_KIND_AUDIO_URL));
				mRequests.add(requestPair);
				duplicate.add(mcai.getString(MCDefResult.MCR_KIND_AUDIO_ID));
			}
		}

		return true;
	}

	public void start(String key, IDownloaderListener listener) {
		mKey = key;
		mListener = listener;
        mCallback = new ArrayList<>();
		mExecutor = Executors.newSingleThreadExecutor();
		for (Pair<Integer, String> requestPair : mRequests) {
			Future<Integer> future = mExecutor
					.submit(new FROAsyncFileRequest(mContext, requestPair.second, requestPair.first.toString()));
			mCallback.add(future);
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
                for (int i = 0; i < mCallback.size(); i++) {
                    Pair<Integer, String> request = mRequests.get(i);
                    try {
                        mCallback.get(i).get();
                    } catch (InterruptedException e) {
                        FRODebug.logE(getClass(), e.getMessage(), DEBUG);
                    } catch (ExecutionException e) {
                        FRODebug.logE(getClass(), e.getMessage(), DEBUG);
                        // 途中までファイルが作成されてしまったケースを考慮し、該当するファイルがあれば削除しておく
                        if (FROUtils.existPackageFile(mContext, request.first.toString())) {
                            mContext.deleteFile(FROUtils.getInterruptTrackName(request.first.toString()));
                        }
                    }
                }
                FROUtils.showPackageFiles(mContext);
				if (mListener != null)
					mListener.onFinish(mKey);
				release();
			}
		}).start();
	}

	private void release() {
		if (mExecutor != null)
			mExecutor.shutdown();
		if (mRequests != null)
			mRequests.clear();
		mRequests = null;
		if (mCallback != null)
			mCallback.clear();
		mCallback = null;
		mExecutor = null;
		mContext = null;
		mListener = null;
		mKey = null;
	}

}
