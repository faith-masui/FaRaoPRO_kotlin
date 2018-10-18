package jp.faraopro.play.frg.newone;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.todddavies.components.progressbar.ProgressWheel;

import java.util.ArrayList;

import jp.faraopro.play.R;
import jp.faraopro.play.mclient.MCAudioItem;
import jp.faraopro.play.mclient.MCDefResult;

/**
 * メッセージとボタン(最大で2つ)からなるダイアログを表示するFragment
 * 
 * @author AIM
 * 
 */
public class InterruptDialogFragment extends DialogFragment {
	/** const **/
	private static final boolean DEBUG = true;

	/** bundle key **/
	public static final String TITLE = "TITLE";
    public static final String AUDIOS = "AUDIOS";

    /**
     * member
     **/
    private int mCurrentIndex;
    private int titleResourceId = R.string.msg_interrupt_playing_audio;
    private ArrayList<MCAudioItem> audios;
    private TextView mTextCount;
    private TextView mTextIndex;
    private TextView mTextMessage;
    private ProgressWheel mProgress;
    private Handler mHandler;
    private CountDownTimer progressCounter;
    private CountDownTimer timeCounter;

    public interface InterruptDialogListener {
        void onViewCreated();
    }

    private InterruptDialogListener listener;

    public static InterruptDialogFragment newInstance(int titleResourceId, ArrayList<MCAudioItem> audios) {
        InterruptDialogFragment fragment = new InterruptDialogFragment();
        Bundle args = new Bundle();
        args.putInt(InterruptDialogFragment.TITLE, titleResourceId);
        args.putParcelableArrayList(InterruptDialogFragment.AUDIOS, audios);
        fragment.setArguments(args);
        return fragment;
    }

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        if (activity instanceof InterruptDialogListener)
            listener = (InterruptDialogListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHandler = new Handler(Looper.getMainLooper());
        return LayoutInflater.from(getActivity()).inflate(R.layout.frg_interrupt_dialog, container, true);
	}

	@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgress = (ProgressWheel) view.findViewById(R.id.interruptdialog_progresswheel_progress);
        ((TextView) view.findViewById(R.id.interruptdialog_text_title)).setText(titleResourceId);
        mTextCount = (TextView) view.findViewById(R.id.interruptdialog_text_couter);
        mTextIndex = (TextView) view.findViewById(R.id.interruptdialog_text_index);
        mTextMessage = (TextView) view.findViewById(R.id.interruptdialog_text_message);
        if (listener != null)
            listener.onViewCreated();
    }

    @Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle args = getArguments();
		if (args != null) {
            titleResourceId = args.getInt(TITLE);
            audios = args.getParcelableArrayList(AUDIOS);
		}

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCanceledOnTouchOutside(false);
        // Dialog#setCancelable ではなく DialogFragment#setCancelable でセットしないと反映されない
        this.setCancelable(false);
        return dialog;
	}

    @Override
    public void onDetach() {
        super.onDetach();
        release();
	}

	public void start(int index, long position) {
		mCurrentIndex = index;
        if (audios == null || audios.size() < 1) {
            return;
        }
        MCAudioItem audio = audios.get(index);
        long length = Long.parseLong(audio.getString(MCDefResult.MCR_KIND_AUDIO_TIME)) * 1000l;
        mTextIndex.setText((mCurrentIndex + 1) + " / " + audios.size());
        mTextMessage.setText("ID." + audio.getString(MCDefResult.MCR_KIND_AUDIO_ID));
		startProgress(length, position);
	}

	public void startProgress(long length, long position) {
		mProgress.resetCount();
		mProgress.setText("");
        mProgress.setProgress((int) (position * 360 / length));
		final long interval = length / 360l;
        if (progressCounter != null)
            progressCounter.cancel();
        if (timeCounter != null)
            timeCounter.cancel();
        // 円形のプログレスバーを描画するためのタイマー
        progressCounter = new ProgressCounter(length - position + interval, interval);
        progressCounter.start();

        // 経過時間を描画するためのタイマー
        timeCounter = new TimeCounter(length - position + 2000, 1000, (int) position / 1000);
        timeCounter.start();
    }

    public void release() {
        if (audios != null)
            audios.clear();
        audios = null;
        mHandler = null;
        if (progressCounter != null)
            progressCounter.cancel();
        progressCounter = null;
        if (timeCounter != null)
            timeCounter.cancel();
        timeCounter = null;
        listener = null;
    }

    public final class ProgressCounter extends CountDownTimer {
        public ProgressCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

			@Override
			public void onTick(long millisUntilFinished) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mProgress.incrementProgress();
					}
				});
			}

			// 更新間隔は小数点切り捨てであるため、トラック長によってはプログレスバーが描ききれない、その帳尻をここで合わせる
			@Override
			public void onFinish() {
				mProgress.setProgress(360);
			}
    }

    public final class TimeCounter extends CountDownTimer {
        int timerCount;

        public TimeCounter(long millisInFuture, long countDownInterval, int position) {
            super(millisInFuture, countDownInterval);
            timerCount = position;
        }

			// 更新間隔は1000ms
			@Override
			public void onTick(long millisUntilFinished) {
            final String time = intToString(timerCount);
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mTextCount.setText(time);
					}
				});
            timerCount++;
			}

			// ラグが1000ms近くあるのでonFinishは使用しない、代わりにmillisInFutureを1000ms増やしてonTickで描画する
			@Override
			public void onFinish() {
			}
    }

    private static final String intToString(int time) {
		String result = "";
		int min = time / 60;
		int sec = time % 60;
		result = "" + ((min < 10) ? "0" : "") + min + ":" + ((sec < 10) ? "0" : "") + sec;

		return result;
	}
}
