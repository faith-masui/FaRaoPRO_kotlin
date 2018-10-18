package jp.faraopro.play.frg.newone;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.todddavies.components.progressbar.ProgressWheel;

import jp.faraopro.play.R;


/**
 * エマージェンシーモードで表示するサークルプログレス付きのダイアログ
 *
 * @author AIM
 */
public class EmergencyDialogFragment extends DialogFragment {
    private static final boolean DEBUG = true;

    private static final long DEGREES_360 = 360l;
    public static final int NO_VIEW_ID = -1;
    public static final int ACTION_NONE = 0;
    public static final int ACTION_PLAY = 1;
    public static final int ACTION_PAUSE = 2;
    public static final int ACTION_BACK = 3;

    private ProgressWheel progressWheel;
    private TextView messageInProgress;
    private TextView description;
    private ImageButton play;
    private ImageButton openTips;
    private RelativeLayout tips;
//    private Button back;

    private long trackLength;
    private long currentPosition;
    private long interval;
    private CountDownTimer progressTimer;
    private Handler handler;

    public interface EmergencyDialogListener {
        void onViewCreated();

        void onClick(int viewId, int action);
    }

    private EmergencyDialogListener listener;

    /**
     * ファクトリメソッド
     *
     * @param trackLength 最初に再生する楽曲の長さ
     * @return このクラスのインスタンス
     */
    public static EmergencyDialogFragment newInstance(long trackLength) {
        EmergencyDialogFragment fragment = new EmergencyDialogFragment();
        // Bundle bundle = new Bundle();
        // bundle.putLong(BUNDLE_TRACK_LENGTH, trackLength);
        // fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof EmergencyDialogListener)
            listener = (EmergencyDialogListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        handler = new Handler(Looper.getMainLooper());
        return LayoutInflater.from(getActivity()).inflate(R.layout.frg_emergency_dialog, container, true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Animation inAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.tooltip_in_anim);
        final Animation outAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.tooltip_out_anim);
        progressWheel = (ProgressWheel) view.findViewById(R.id.interruptdialog_progresswheel_progress);
        messageInProgress = (TextView) view.findViewById(R.id.interruptdialog_text_couter);
        description = (TextView) view.findViewById(R.id.text_description);
        play = (ImageButton) view.findViewById(R.id.image_button_play);
        // play.setSelected(!getArguments().getBoolean(BUNDLE_IS_PLAYING));
        play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressTimer == null) {
                    play();
                    if (listener != null)
                        listener.onClick(R.id.image_button_play, ACTION_PLAY);
                } else {
                    pause();
                    if (listener != null)
                        listener.onClick(R.id.image_button_play, ACTION_PAUSE);
                }
            }
        });
        tips = (RelativeLayout) view.findViewById(R.id.layout_tips);
        tips.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(outAnimation);
                v.setVisibility(View.GONE);
            }
        });
        tips.setVisibility(View.GONE);
        openTips = (ImageButton) view.findViewById(R.id.image_button_tips);
        openTips.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tips.getVisibility() == View.GONE) {
                    tips.startAnimation(inAnimation);
                    tips.setVisibility(View.VISIBLE);
                } else {
                    tips.startAnimation(outAnimation);
                    tips.setVisibility(View.GONE);
                }
            }
        });
//        back = (Button) view.findViewById(R.id.button_back);
//        back.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                release();
//                dismissDialog();
//                if (listener != null)
//                    listener.onClick(R.id.button_back, ACTION_NONE);
//            }
//        });
        if (listener != null)
            listener.onViewCreated();
    }

    @Override
    public void onResume() {
        super.onResume();
        description.requestFocus();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        release();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCanceledOnTouchOutside(false);
        // Dialog#setCancelable ではなく DialogFragment#setCancelable でセットしないと反映されない
        this.setCancelable(false);
        // バックキーの動作をフックする
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event != null && event.getAction() == KeyEvent.ACTION_UP) {
                    if (listener != null)
                        listener.onClick(-1, ACTION_BACK);
                    return true;
                }
                return false;
            }
        });
        return dialog;
    }

    public void setTrack(long trackLength, long position) {
        destroyTimer();
        this.trackLength = trackLength;
        currentPosition = position;
        progressWheel.resetCount();
        int progressValue = (int) (DEGREES_360 * position / trackLength);
        progressWheel.setProgress(progressValue);
        interval = trackLength / DEGREES_360;
    }

    public void play() {
        play.setSelected(false);
        createTimer(currentPosition);
        progressTimer.start();
    }

    public void pause() {
        play.setSelected(true);
        destroyTimer();
    }

    public void setMessages(int messageInProgress, int description) {
        this.messageInProgress.setText(messageInProgress);
        this.description.setText(description);
    }

    private void createTimer(long position) {
        progressTimer = new CountDownTimer(trackLength - position, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentPosition += interval;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressWheel.incrementProgress();
                    }
                });
            }

            @Override
            public void onFinish() {
                progressWheel.setProgress(360);
            }
        };
    }

    private void destroyTimer() {
        if (progressTimer != null) {
            progressTimer.cancel();
            progressTimer = null;
        }
    }

    public void release() {
        destroyTimer();
        handler = null;
    }
}
