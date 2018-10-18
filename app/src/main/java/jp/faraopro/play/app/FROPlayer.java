package jp.faraopro.play.app;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.domain.HttpRawResourceServer;
import jp.faraopro.play.domain.MusicInfo;
import jp.faraopro.play.hls.HlsClientListener;
import jp.faraopro.play.hls.HlsProxy;
import jp.faraopro.play.hls.HlsProxyListener;
import jp.faraopro.play.mclient.MCAudioItem;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCError;
import jp.faraopro.play.mclient.MCUserInfoPreference;
import jp.faraopro.play.util.FROUtils;

/**
 * 音楽プレイヤークラス
 *
 * @author Aim
 */
public class FROPlayer implements AudioFocusHelper.IAudioFocusListener {
    /**
     * const
     **/
    public static final int PLAYER_TYPE_NONE = 0;
    public static final int PLAYER_TYPE_NOMAL = 1;
    public static final int PLAYER_TYPE_LOCAL = 2;
    public static final int PLAYER_TYPE_SIMUL = 3;
    public static final int PLAYER_TYPE_OFFLINE = 4;

    // put out logs
    private static final boolean DEBUG = true;
    private static final String TAG = FROPlayer.class.getSimpleName();

    public static final float BASE_REPLAY_GAIN = 6.0f;

    private static boolean isPreparing = false;

    private static final String urlHeadr = "http://127.0.0.1:";
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private HttpRawResourceServer mediaServer;
    private int mPlayerType = PLAYER_TYPE_NONE;

    // 11/25 ksu
    public String key;
    public String iv;
    public String sig;
    private String mHlsUrl;

    private float channelVolume;
    private float replayGain = 6.0f;
    private MediaPlayer mInterruptPlayer;

    public interface IFadeoutListener {
        public void onComplete();
    }

    /**
     * MediaPlayer・HRRSのインスタンス生成、HRRSの起動
     *
     * @param context
     */
    public FROPlayer(Context context) {
        mContext = context;
        mMediaPlayer = new MediaPlayer();
        channelVolume = MCUserInfoPreference.getInstance(mContext).getChannelVolume();
        mMediaPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);
        if (AudioFocusHelper.getFocusState() != AudioManager.AUDIOFOCUS_GAIN)
            AudioFocusHelper.requestFocus(mContext, this);
        mediaServer = new HttpRawResourceServer(mContext, false);
        mediaServer.init();
        mediaServer.start();
    }

    /**
     * リスナーの設定
     *
     * @param listener : リスナー
     */
    public void attach(OnCompletionListener listener) {
        if (mMediaPlayer != null)
            mMediaPlayer.setOnCompletionListener(listener);
    }

    public void attachUpgradeListener() {
        mMediaPlayer.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                boolean isHandled = false;
                FRODebug.logE(getClass(), "MediaPlayer onError what=" + what + ", extra=" + extra, DEBUG);

                if (simulCallback != null && what != -38) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (simulCallback != null) {
                                simulCallback.onError(MCError.MC_APPERR_IO_HTTP);
                                simulCallback = null;
                            }
                        }
                    }).start();
                    isHandled = true;
                }

                FRODebug.logD(getClass(), "isHandled = " + isHandled, DEBUG);
                return isHandled;
            }
        });
        mMediaPlayer.setOnInfoListener(new OnInfoListener() {

            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                FRODebug.logE(getClass(), "MediaPlayer onInfo what=" + what + ", extra=" + extra, DEBUG);

                if (what == 862) {
                    switch (extra) {
                        case -1004:
                        case -1003:
                            if (simulCallback != null) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (simulCallback != null) {
                                            simulCallback.onError(MCError.MC_APPERR_IO_HTTP);
                                            simulCallback = null;
                                        }
                                    }
                                }).start();
                                return true;
                            }
                            break;
                    }
                }
                return false;
            }
        });
        mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
            }
        });
        mMediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
            }
        });
    }

    /**
     * URLのドメイン部分取得
     *
     * @return
     */
    public String getPathHeader() {
        return urlHeadr + mediaServer.getPort() + "/";
    }

    public int getPlayerType() {
        return this.mPlayerType;
    }

    /**
     * @param path
     * @param key
     * @param iv
     * @param sig
     * @param type
     * @param gain
     * @throws IOException
     * @throws RuntimeException
     */
    public void setMedia(String path, String key, String iv, String sig, int type, float gain)
            throws IOException, RuntimeException {
        this.mPlayerType = type;
        isPreparing = true;
        try {
            mediaServer.setEncParam(key, iv, sig, path);
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
            replayGain = gain;
            changeChannelVolume();
        } catch (IllegalArgumentException e) {
            isPreparing = false;
        } catch (IOException e) {
            isPreparing = false;
        } catch (SecurityException e) {
            isPreparing = false;
        } catch (RuntimeException e) {
            isPreparing = false;
        }

        isPreparing = false;
    }

    public void setLocalMedia(String path, boolean loop, int type) {
        this.mPlayerType = type;
        isPreparing = true;
        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
            replayGain = BASE_REPLAY_GAIN;
            changeChannelVolume();
        } catch (IllegalArgumentException e) {
            isPreparing = false;
        } catch (IOException e) {
            isPreparing = false;
        } catch (SecurityException e) {
            isPreparing = false;
        } catch (RuntimeException e) {
            isPreparing = false;
        }

        isPreparing = false;
    }

    private SimulCallbackListener simulCallback;
    private String mCookieValue;
    private HlsProxy mHlsProxy;

    public void setSimulMedia(SimulCallbackListener callbackListener, String path, boolean loop, int type, float gain) {
        if (isPreparing != true) {
            isPreparing = true;
            this.mPlayerType = type;
            mHlsUrl = path;
            simulCallback = callbackListener;
            replayGain = gain;
//            changeChannelVolume();
            new PlayTask().execute(path);
        }
    }

    public interface SimulCallbackListener {
        public void onSuccess();

        public void onError(int code);
    }

    public class PlayTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            resetPlayer();
            String doNext = "";
            int retry = 3;
            while (retry > 0) {
                if (prepareDataSource(params[0])) {
                    doNext = "OK";
                    break;
                }
                retry--;
            }
            isPreparing = false;
            return doNext;
        }

        @Override
        protected void onPostExecute(String result) {
            if (simulCallback != null) {
                if (result.equals("OK")) {
                    simulCallback.onSuccess();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (simulCallback != null) {
                                simulCallback.onError(MCError.MC_NOT_FOUND);
                                simulCallback = null;
                            }
                        }
                    }).start();
                }
            }
        }

        public boolean prepareDataSource(String url) {
            try {
                HlsDataSource hlsDataSource = new HlsDataSource(url);
                setDataSource(hlsDataSource.getSourceUrl(), hlsDataSource.getCookieValue());
                return true;
            } catch (IOException ioe) {
                resetPlayer();
                resetProxy(null);
                Log.e(TAG, ioe.getMessage(), ioe);
            }
            return false;
        }
    }

    public void setDataSource(String url, String cookieValue) throws IOException {
        resetPlayer();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("x-hide-urls-from-log", "true");
        if (cookieValue != null)
            headers.put("Cookie", cookieValue);
        // proxy
        resetProxy(url);
        Uri uri = Uri.parse("http://localhost:" + mHlsProxy.getListeningPort() + "/0.m3u8");
        mMediaPlayer.setDataSource(mContext, uri, headers);
        mMediaPlayer.prepare();
        changeChannelVolume();
        mHlsUrl = uri.toString();
        mCookieValue = cookieValue;
    }

    public void resetProxy(String url) {
        if (mHlsProxy != null) {
            mHlsProxy.shutdown();
            mHlsProxy = null;
        }
        if (url != null) {
            try {
                mHlsProxy = new HlsProxy(0, url, 64000, new HlsClientListener() {
                    @Override
                    public void onErrorEmptyPlaylist(final String playlistPath, final byte[] contents) {
                    }

                    @Override
                    public void onErrorMediaDecryption(final long sequence, final Throwable cause) {
                    }

                    @Override
                    public void onErrorDownloadKey(final String keyPath, final Throwable cause) {
                    }

                    @Override
                    public void onErrorDownloadMedia(final String mediaPath, final Throwable cause) {
                    }

                    @Override
                    public void onErrorDownloadPlaylist(final String playlistPath, final Throwable cause) {
                    }
                }, new HlsProxyListener() {
                    @Override
                    public void onProxyPlaylist(final List<String> m3u8) {
                        StringBuilder dump = new StringBuilder();
                        for (String s : m3u8) {
                            dump.append(s);
                            dump.append("\r\n");
                        }
                    }

                    @Override
                    public void onProxyMedia(final long sequence, final String media) {
                    }
                });
            } catch (IOException ioe) {
            }
        }
    }

    public void resetPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        } else {
            mMediaPlayer.reset();
        }
        mHlsUrl = null;
        mCookieValue = null;
    }

    /**
     * 楽曲の再生・一時停止
     */
    public void startOrPause() {
        // MediaPlayerの初期化中は処理を行わない
        while (isPreparing) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            } else {
                mMediaPlayer.start();
            }
        } catch (IllegalStateException e) {
            FRODebug.logE(getClass(), e.getMessage(), DEBUG);
        }
    }

    /**
     * 楽曲の停止、開放
     */
    public void stop() {
        // MediaPlayerの初期化中は処理を行わない
        while (isPreparing) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }

        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
            } catch (IllegalStateException e) {
                FRODebug.logE(getClass(), e.getMessage(), DEBUG);
            }
            mMediaPlayer.reset();
        }
    }

    public static float getGainVolume(float gain, float sliderVolume) {
        float calculatedValume = 0.0f;
        // スライダーの値 0.0~1.0
        if (sliderVolume > 0.0f) {
            // 10*log2(s)
            double log = 10d * (Math.log(sliderVolume) / Math.log(2.0));
            // ((rg - 6.0 + 10*log2(s)) / 20.0)
            float tmp = (gain - 6.0f + (float) log) / 20f;
            // 10^((rg - 6.0 + 10*log2(s)) / 20.0)
            calculatedValume = (float) (Math.pow(10f, tmp));
            if (calculatedValume >= 1.0f)
                calculatedValume = 1.0f;
        }
        return calculatedValume;
    }

    public static float getDuckVolume(float baseVolume, float duckRatio) {
        return baseVolume * duckRatio;
    }

    public static float getChannelVolume(float gain, float sliderVolume, float duckRatio, boolean focusIsLost) {
        float volume = getGainVolume(gain, sliderVolume);
        if (focusIsLost)
            volume = getDuckVolume(volume, duckRatio);
        return volume;
    }

    /**
     * 楽曲をフェードアウトしながら一時停止する
     *
     * @param time 停止するまでの時間(msec)
     */
    public void fadeOut(long time) {
        if (mMediaPlayer == null)
            return;

        final int MAX_STEP = 10;
        long sleepInterval = time / MAX_STEP; // 最大ステップ数100
        float maxVolume = this.channelVolume;
        for (int i = 1; i <= MAX_STEP; i++) {
            double d = ((double) i) / MAX_STEP;
            double cos = ((Math.cos(d * Math.PI) + 1) / 2);
            double pow = Math.pow(cos, 2);
            float vol = (float) (maxVolume * pow);
            mMediaPlayer.setVolume(vol, vol);
            try {
                Thread.sleep(sleepInterval);
            } catch (InterruptedException e) {
                mMediaPlayer.setVolume(0, 0);
                e.printStackTrace();
            }
        }
        try {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        } catch (IllegalStateException e) {
            FRODebug.logE(getClass(), e.getMessage(), DEBUG);
        }
    }

    /**
     * 楽曲をフェードインしながら再生開始する
     *
     * @param time 停止するまでの時間(msec)
     */
    public void fadeIn(long time) {
        if (mMediaPlayer == null)
            return;

        final int MAX_STEP = 10;
        try {
            mMediaPlayer.start();
        } catch (IllegalStateException e) {
            FRODebug.logE(getClass(), e.getMessage(), DEBUG);
        }
        long sleepInterval = time / MAX_STEP; // 最大ステップ数100
        float maxVolume = this.channelVolume;
        for (int i = 1; i <= MAX_STEP; i++) {
            double d = ((double) i) / MAX_STEP;
            double cos = 1 - ((Math.cos(d * Math.PI) + 1) / 2);
            double pow = Math.pow(cos, 2);
            float vol = (float) (maxVolume * pow);
            mMediaPlayer.setVolume(vol, vol);
            try {
                Thread.sleep(sleepInterval);
            } catch (InterruptedException e) {
                mMediaPlayer.setVolume(maxVolume, maxVolume);
                e.printStackTrace();
            }
        }
    }

    public interface IInterruptStateListener {
        void onNext(int nextIndex);

        /**
         * 割り込み再生完了の通知を行うコールバック
         *
         * @param playedAudios                再生した音声ファイルの audioId
         * @param complete                    true:割り込み再生が行われた場合, false:何らかの原因で再生が行われなかった場合
         * @param isPlayingBeforeInterrupting true:割り込み再生前に再生状態にあった, false:それ以外
         */
        void onFinish(List<String> playedAudios, String complete, boolean isPlayingBeforeInterrupting);
    }

    IInterruptStateListener mInterruptStateListener;
    List<MCAudioItem> mInterruptAudios;
    List<String> mPlayedAudios;
    int mInterruptIndex;
    boolean mPlayerStateAtTheInterrupted; // true:play, false:pause
    private boolean isInterrupt;

    public boolean isInterrupt() {
        return isInterrupt;
    }

    public void startInterrupt(List<MCAudioItem> audios, IInterruptStateListener listener, boolean isPlaying) {
        isInterrupt = true;
        mInterruptStateListener = listener;
        mInterruptIndex = 0;
        mInterruptAudios = audios;
        mPlayerStateAtTheInterrupted = isPlaying;
        mPlayedAudios = new ArrayList<>();

        // 保険
        try {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mPlayerStateAtTheInterrupted = true;
            }
        } catch (IllegalStateException e) {
            FRODebug.logE(getClass(), e.getMessage(), DEBUG);
        }
        if (mInterruptPlayer != null)
            mInterruptPlayer.release();
        mInterruptPlayer = new MediaPlayer();
        float interruptVolume = getGainVolume(BASE_REPLAY_GAIN,
                MCUserInfoPreference.getInstance(mContext).getInterruptVolume());
        mInterruptPlayer.setVolume(interruptVolume, interruptVolume);
        MCAudioItem item = mInterruptAudios.get(mInterruptIndex++);
        mInterruptPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mInterruptIndex < mInterruptAudios.size()) {
                    mInterruptPlayer.reset();
                    if (mInterruptStateListener != null)
                        mInterruptStateListener.onNext(mInterruptIndex);
                    MCAudioItem item = mInterruptAudios.get(mInterruptIndex++);
                    prepareInterrupt(item);
                    mInterruptPlayer.start();
                } else {
                    FROUtils.deleteOldInterruptFile(mContext); // check unused
                    // files
                    isInterrupt = false;
                    if (mInterruptStateListener != null)
                        mInterruptStateListener.onFinish(mPlayedAudios, "yes", mPlayerStateAtTheInterrupted);

                    mInterruptPlayer.release();
                    mInterruptPlayer = null;
                    mInterruptAudios.clear();
                    mInterruptAudios = null;
                }
            }
        });
        prepareInterrupt(item);
        mInterruptPlayer.start();
    }

    private void prepareInterrupt(MCAudioItem item) {
        try {
            String path = FROUtils.getInterruptTrackName(item.getString(MCDefResult.MCR_KIND_AUDIO_ID));
            FROUtils.updateLastModified(mContext, path); // update last modify
            FileInputStream fis = mContext.openFileInput(path);
            mInterruptPlayer.setDataSource(fis.getFD());
            mInterruptPlayer.prepare();
            mPlayedAudios.add(item.getString(MCDefResult.MCR_KIND_AUDIO_ID));
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getInterruptIndex() {
        return mInterruptIndex;
    }

    public int getInterruptPosition() {
        return (mInterruptPlayer != null && mInterruptPlayer.isPlaying()) ? mInterruptPlayer.getCurrentPosition() : 0;
    }

    public List<MCAudioItem> getInterruptList() {
        return mInterruptAudios;
    }

    /**
     * 再生状態の確認
     *
     * @return true:再生中、false:それ以外
     */
    public boolean isPlaying() {
        Boolean ret = false;

        if (mMediaPlayer != null) {
            try {
                ret = mMediaPlayer.isPlaying();
            } catch (IllegalStateException e) {
                FRODebug.logE(getClass(), e.getMessage(), DEBUG);
            }
        }

        return ret;
    }

    public void getMusicData(MusicInfo info) {
        if (isPreparing)
            return;

        if (mMediaPlayer != null) {
            int length = mMediaPlayer.getDuration();
            int current = mMediaPlayer.getCurrentPosition();
            info.setLength(length);
            info.setCurrentPos(current);
        }
    }

    public void changeChannelVolume() {
        if (mMediaPlayer != null) {
            channelVolume = getChannelVolume(replayGain,
                    MCUserInfoPreference.getInstance(mContext).getChannelVolume(),
                    MCUserInfoPreference.getInstance(mContext).getAudioFocusedVolume(),
                    AudioFocusHelper.getFocusState() != AudioManager.AUDIOFOCUS_GAIN);
            mMediaPlayer.setVolume(channelVolume, channelVolume);
            FRODebug.logD(getClass(), "volume = " + channelVolume, true);
        }
    }

    public void changeInterruptVolume() {
        if (mInterruptPlayer != null && mInterruptPlayer.isPlaying()) {
            float interruptVolume = getGainVolume(BASE_REPLAY_GAIN,
                    MCUserInfoPreference.getInstance(mContext).getInterruptVolume());
            mInterruptPlayer.setVolume(interruptVolume, interruptVolume);
            FRODebug.logD(getClass(), "volume = " + interruptVolume, true);
        }
    }

    /**
     * 終了処理
     */
    public void term() {
        while (isPreparing) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (isPlaying()) {
            stop();
        }
        if (mPlayerType == PLAYER_TYPE_SIMUL) {
            resetPlayer();
            resetProxy(null);
            mHlsProxy = null;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        mMediaPlayer = null;
        AudioFocusHelper.abandonFocus(mContext);
        mContext = null;
        if (mediaServer.isRunning()) {
            mediaServer.stop();
        }
        mediaServer = null;
    }

    private void output(String msg) {
        FROUtils.outputLog(msg);
    }

    @Override
    public void onFocusChanged(int focusChange) {
        if (mMediaPlayer == null)
            return;

        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                changeChannelVolume();
                break;
        }
    }

}
