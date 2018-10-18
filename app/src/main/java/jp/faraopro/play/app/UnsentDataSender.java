package jp.faraopro.play.app;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.domain.FROAsyncRequest;
import jp.faraopro.play.domain.FROUnsentRatingDB;
import jp.faraopro.play.domain.FROUnsentRatingDBFactory;
import jp.faraopro.play.domain.RatingInfo;
import jp.faraopro.play.mclient.MCDefAction;
import jp.faraopro.play.mclient.MCDefParam;
import jp.faraopro.play.mclient.MCError;
import jp.faraopro.play.mclient.MCPostActionParam;
import jp.faraopro.play.mclient.MCUserInfoPreference;

public class UnsentDataSender {
    private static final boolean DEBUG = true;

    private static boolean sIsSending = false;
    private List<Pair<Integer, MCPostActionParam>> mRequests;
    private List<Future<Integer>> mCallback;
    private ExecutorService mExecutor;
    private Context mContext;
    private String mTrackingKey;

    private UnsentDataSender(Context context, String trackingKey) {
        mContext = context;
        mTrackingKey = trackingKey;
    }

    public static synchronized void send(Context context, String trackingKey) {
        FRODebug.logD(UnsentDataSender.class, "in send", DEBUG);
        if (sIsSending)
            return;

        sIsSending = true;
        UnsentDataSender sender = new UnsentDataSender(context, trackingKey);
        sender.start();
        FRODebug.logD(UnsentDataSender.class, "out send", DEBUG);
    }

    private void createRequest() {
        mRequests = new ArrayList<>();
        FROUnsentRatingDB unsentRatingDb = (FROUnsentRatingDB) FROUnsentRatingDBFactory.getInstance(mContext);
        List<RatingInfo> unsentRatingList = unsentRatingDb.findAll();
        MCPostActionParam param;
        if (unsentRatingList != null && unsentRatingList.size() > 0) {
            Integer api = MCDefAction.MCA_KIND_RATING_OFFLINE;
            for (RatingInfo info : unsentRatingList) {
                param = new MCPostActionParam();
                param.setStringValue(MCDefParam.MCP_KIND_TRACKING_KEY, info.getTrackingKey());
                param.setStringValue(MCDefParam.MCP_KIND_MODE_NO, info.getMode());
                param.setStringValue(MCDefParam.MCP_KIND_MYCHANNELID, info.getChannelId());
                param.setStringValue(MCDefParam.MCP_KIND_RANGE, info.getRange());
                param.setStringValue(MCDefParam.MCP_KIND_RATING_TRACKID, info.getTrackId());
                param.setStringValue(MCDefParam.MCP_KIND_RATE_ACTION, info.getDecision());
                param.setStringValue(MCDefParam.MCP_KIND_PLAY_COMPLETE, info.getComplete());

                // DB 内部では時間の単位が msec のため、webapi 向けに単位を sec に変換する
                String msecString = info.getDuration();
                int msec = TextUtils.isDigitsOnly(msecString) ? Integer.parseInt(msecString) : 0;
                String duration = String.valueOf(msec / 1000);

                param.setStringValue(MCDefParam.MCP_KIND_PLAY_DURATIN, duration);
                mRequests.add(Pair.create(api, param));
            }
        }
        String data = MCUserInfoPreference.getInstance(mContext).getOnairOffline();
        if (!TextUtils.isEmpty(data)) {
            String[] tmp = data.split("@", 0);
            String frameId = tmp[0];
            String audioIds = tmp[1].substring(0, tmp[1].length() - 1);
            param = new MCPostActionParam();
            param.setStringValue(MCDefParam.MCP_KIND_TRACKING_KEY, mTrackingKey);
            param.setStringValue(MCDefParam.MCP_KIND_FRAME_ID, frameId);
            param.setStringValue(MCDefParam.MCP_KIND_AUDIO_ID, audioIds);
            mRequests.add(Pair.create(MCDefAction.MCA_KIND_PATTERN_ONAIR_OFFLINE, param));
        }
    }

    public void start() {
        createRequest();
        if (mRequests.size() < 1) {
            release();
            return;
        }

        mCallback = new ArrayList<>();
        mExecutor = Executors.newSingleThreadExecutor();
        for (Pair<Integer, MCPostActionParam> requestPair : mRequests) {
            Future<Integer> future = mExecutor.submit(new FROAsyncRequest(requestPair.first, requestPair.second));
            mCallback.add(future);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mCallback.size(); i++) {
                    try {
                        Integer result = mCallback.get(i).get();
                        Pair<Integer, MCPostActionParam> request = mRequests.get(i);

                        FRODebug.logD(getClass(), "result = " + result, DEBUG);

                        switch (request.first) {
                            case MCDefAction.MCA_KIND_RATING_OFFLINE:
                                if (result == MCError.MC_NO_ERROR)
                                    deleteUnsentRating(request.second.getStringValue(MCDefParam.MCP_KIND_RATING_TRACKID));
                                break;
                            case MCDefAction.MCA_KIND_PATTERN_ONAIR_OFFLINE:
                                if (result == MCError.MC_NO_ERROR)
                                    MCUserInfoPreference.getInstance(mContext).setOnairOffline(null, null);
                                break;
                        }
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
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
        mTrackingKey = null;
        mContext = null;
        sIsSending = false;
    }

    private void deleteUnsentRating(String trackId) {
        FROUnsentRatingDB unsentDb = (FROUnsentRatingDB) FROUnsentRatingDBFactory.getInstance(mContext);
        unsentDb.delete(Integer.parseInt(trackId));
    }

}
