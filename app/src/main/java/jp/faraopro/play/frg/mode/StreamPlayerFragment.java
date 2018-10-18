package jp.faraopro.play.frg.mode;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.domain.ChannelInfo;
import jp.faraopro.play.domain.MusicInfo;
import jp.faraopro.play.util.FROUtils;
import jp.faraopro.play.util.Utils;

/**
 * タイトルバーを構成するフラグメント<br>
 *
 * @author Aim
 */
public class StreamPlayerFragment extends Fragment implements IModeFragment {
    private static boolean rollBlock = false;
    private Button btnFavorite;
    private Button btnPause;
    private TextView txtFirstLine;
    private ImageView jacketCurrent;
    private ImageView jacketNext;
    private ChannelInfo favorite;
    private CountDownTimer slideShowTimer;
    private List<String> slideImageList;
    private int slidePosition;
    private long slideInterval;

    public static StreamPlayerFragment newInstance() {
        StreamPlayerFragment instance = new StreamPlayerFragment();
        return instance;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

        /*** スマホ用アニメーション ***/
        if (transit == MainActivity.ANIM_HIDE_FLICK) {
            if (enter) {
                return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_flip_horizontal_exit);
            } else {
                return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_flip_horizontal_exit);
            }
        }
        if (transit == MainActivity.ANIM_SHOW_FLICK) {
            if (enter) {
                return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_flip_horizontal_enter);
            } else {
                return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_flip_horizontal_enter);
            }
        }

        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_frg_stream_player, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    // Viewの初期化(参照取得、イベント設定など)
    private void initViews(View view) {
        btnFavorite = (Button) view.findViewById(R.id.stream_player_btn_favorite);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicInfo nowplaying = FROForm.getInstance().getNowPlaying();
                favorite = new ChannelInfo(null, nowplaying.getId(), null, nowplaying.getTitle(), null,
                        Consts.MUSIC_TYPE_SIMUL);
                ((BaseActivity) getActivity()).showTextBoxDialog(null, favorite.getChannelName(),
                        MainActivity.DIALOG_TAG_ADD_TO_FAVORITE_POS, MainActivity.DIALOG_TAG_ADD_TO_FAVORITE_NEG,
                        R.string.btn_favorite_regist, -1);
            }
        });

        btnPause = (Button) view.findViewById(R.id.stream_player_btn_pause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 連打抑止用フラグ
                if (!rollBlock) {
                    // ロックする
                    rollBlock = true;

                    if (!FROForm.getInstance().isPlaying())
                        ((BaseActivity) getActivity()).showProgress();
                    FROForm.getInstance().pauseMusicAsync();
                    if (btnPause.isSelected()) {
                        btnPause.setSelected(false);
                    } else {
                        btnPause.setSelected(true);
                    }

                    // 2s 後にロックを解除する
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rollBlock = false;
                        }
                    }, 500);
                }
            }
        });

        txtFirstLine = (TextView) view.findViewById(R.id.stream_player_txt_first_line);
        jacketCurrent = (ImageView) view.findViewById(R.id.stream_player_img_jacket);
        jacketNext = (ImageView) view.findViewById(R.id.stream_player_img_jacket_slidein);
        // 画像表示エリアの viewgroup の横幅を端末の2倍にする
        LinearLayout slideImageHolder = (LinearLayout) view.findViewById(R.id.linear_image_holder);
        slideImageHolder.getLayoutParams().width = FROForm.getInstance().width * 2;
        // imageview のサイズを1辺が端末の横幅となるような正方形にする
        jacketCurrent.getLayoutParams().width = FROForm.getInstance().width;
        jacketCurrent.getLayoutParams().height = FROForm.getInstance().width;
        jacketNext.getLayoutParams().width = FROForm.getInstance().width;
        jacketNext.getLayoutParams().height = FROForm.getInstance().width;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (slideShowTimer != null) {
                slideShowTimer.cancel();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (slideShowTimer != null) {
            slideShowTimer.cancel();
            slideShowTimer = null;
        }
        if (slideImageList != null) {
            slideImageList.clear();
            slideImageList = null;
        }
    }

    /**
     * 楽曲再生時に行う処理<br>
     * {@link #updateViews()}と異なり、再生開始時のみに行いたい処理を記述する
     */
    public void onStartMusic() {
        deleteSlideShowTimer();
        MusicInfo nowplaying = FROForm.getInstance().getNowPlaying();
        slideInterval = (TextUtils.isDigitsOnly(nowplaying.getShowDuration()))
                ? Long.parseLong(nowplaying.getShowDuration()) * 1000 * 60 : 0;
        String path = null;
        // スライドショー有り
        if (slideInterval > 0) {
            // スライドショーの更新
            List<String> optionals = nowplaying.getShowJackets();
            if (optionals != null && optionals.size() > 0) {
                slideImageList = new ArrayList<String>();
                slideImageList.add(path);
                for (String optionalPath : optionals) {
                    slideImageList.add(FROUtils.getJacketPath() + optionalPath.replace(".jpg", ""));
                }
                slideImageList = removeUnexistFile(slideImageList);
                if (slideImageList.size() > 0) {
                    setImage(slideImageList.get(0));
                    if (slideImageList.size() > 1) {
                        slidePosition = 1;
                        createSlideShowTimer().start();
                    }
                } else {
                    setImage(null);
                }
            }
        }
        // スライドショー無し
        else {
            if (!TextUtils.isEmpty(nowplaying.getArtwork())) {
                path = FROUtils.getJacketPath() + nowplaying.getArtwork().replace(".jpg", "");
            }
            setImage(path);
        }
    }

    private void setImage(String path) {
        File file = (path != null) ? new File(path) : null;
        Bitmap jacket = null;
        if (file != null && file.isFile()) {
            jacket = Utils.loadBitmap(path);
        }
        Drawable drawable = (jacket != null) ? new BitmapDrawable(getResources(), jacket)
                : getActivity().getResources().getDrawable(R.drawable.noimage);
        jacketCurrent.setImageDrawable(drawable);
    }

    private static List<String> removeUnexistFile(List<String> list) {
        List<String> existList = new ArrayList<String>();
        if (list != null) {
            for (String path : list) {
                if (path != null) {
                    File file = new File(path);
                    if (file != null && file.exists())
                        existList.add(path);
                }
            }
        }
        return existList;
    }

    @Override
    public void updateViews() {
        MusicInfo nowplaying = FROForm.getInstance().getNowPlaying();
        // テキストの更新
        txtFirstLine.setText(nowplaying.getTitle());
        txtFirstLine.requestFocus();
        ((MainActivity) getActivity()).setHeaderTitle(nowplaying.getName());
        // ボタンの更新
        if (FROForm.getInstance().isPlaying()) {
            btnPause.setSelected(false);
        } else {
            btnPause.setSelected(true);
        }
        if (slideShowTimer != null) {
            slideShowTimer.cancel();
            slideShowTimer.start();
        }
    }

    private void slideShow() {
        String path = slideImageList.get(slidePosition);
        File file = (path != null) ? new File(path) : null;
        Bitmap jacket = null;
        if (file != null && file.isFile()) {
            jacket = Utils.loadBitmap(path);
        }
        Drawable drawable = (jacket != null) ? new BitmapDrawable(getResources(), jacket)
                : getActivity().getResources().getDrawable(R.drawable.noimage);
        jacketNext.setImageDrawable(drawable);
        slidePosition = (slideImageList.size() - 1 <= slidePosition) ? 0 : slidePosition + 1;
        Animation outAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_player_jacket_out);
        outAnimation.setFillAfter(true);
        Animation inAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_player_jacket_out);
        inAnimation.setFillAfter(true);
        inAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                jacketCurrent.clearAnimation();
                jacketNext.clearAnimation();
                jacketCurrent.setImageDrawable(jacketNext.getDrawable());
                // 何故か jacketCurrent にセットした drawable に影響がでるのでコメントアウト
                // jacketNext.setImageDrawable(null);
            }
        });
        jacketCurrent.startAnimation(outAnimation);
        jacketNext.startAnimation(inAnimation);
    }

    private CountDownTimer createSlideShowTimer() {
        deleteSlideShowTimer();
        slideShowTimer = new CountDownTimer(slideInterval, slideInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                FRODebug.logD(getClass(), "onTick", true);
            }

            @Override
            public void onFinish() {
                FRODebug.logD(getClass(), "onFinish", true);
                slideShow();
                slideShowTimer.cancel();
                createSlideShowTimer().start();
            }
        };
        return slideShowTimer;
    }

    private void deleteSlideShowTimer() {
        if (slideShowTimer != null)
            slideShowTimer.cancel();
        slideShowTimer = null;
    }

    public ChannelInfo getFavorite() {
        return this.favorite;
    }

    @Override
    public void onBackPress() {
        ((IModeActivity) getActivity()).showMode(MainActivity.TOP);
    }

    public void onBtnClicked(int resId) {
        switch (resId) {
            case R.id.header_btn_right:
                ((BaseActivity) getActivity()).startBrowser(FROForm.getInstance().getNowPlaying().getExternalLink1());
                break;
        }
    }

    public boolean hasLink() {
        MusicInfo info = FROForm.getInstance().getNowPlaying();
        if (info == null || TextUtils.isEmpty(info.getExternalLink1()))
            return false;
        else
            return true;
    }

    @Override
    public void getData() {
    }

    @Override
    public void show() {
    }

    @Override
    public void onError(int when, int code) {
    }
}
