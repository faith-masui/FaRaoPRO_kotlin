package jp.faraopro.play.frg.mode;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.domain.ChannelInfo;
import jp.faraopro.play.domain.FROFavoriteChannelDB;
import jp.faraopro.play.domain.FROFavoriteChannelDBFactory;
import jp.faraopro.play.domain.MusicInfo;
import jp.faraopro.play.mclient.MCDefParam;
import jp.faraopro.play.model.ChannelParams;
import jp.faraopro.play.util.FROUtils;
import jp.faraopro.play.util.SimpleTimer2;
import jp.faraopro.play.util.Utils;

/**
 * タイトルバーを構成するフラグメント<br>
 *
 * @author Aim
 */
public class PlayerFragment extends Fragment implements IModeFragment, SimpleTimer2.TimerListener {
    private static final int TIMER_TYPE_PROGRESS = 0;

    /** bundle key **/

    /**
     * view
     **/
    private Button btnShare;
    //private Button btnBad;
    private Button btnPause;
    //private Button btnGood;
    private Button btnSkip;
    private ImageView imgJacket;
    private TextView txtArtist;
    private TextView txtTitle;
    private ProgressBar prgDuration;

    /**
     * member
     **/
    private Drawable jacketResource;
    private SimpleTimer2 progressTimer;
    // private ArrayList<String> cartItem;
    // private String[] affiliateUrls;
    private ArrayList<String> mCartItem;
    private ArrayList<String> mAffiliateUrl;
    private boolean isFirst = true;
    private ChannelInfo favorite;

    public static PlayerFragment newInstance() {
        PlayerFragment instance = new PlayerFragment();

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
        View view = inflater.inflate(R.layout.new_frg_player_pro, container, false);
        initViews(view);
        return view;
    }

    // Viewの初期化(参照取得、イベント設定など)
    @SuppressLint("SetJavaScriptEnabled")
    private void initViews(View view) {
        // BAD
        //btnBad = (Button)view.findViewById(R.id.player_btn_bad);
        //btnBad.setVisibility(View.INVISIBLE);
        //btnBad = (Button) view.findViewById(R.id.player_btn_bad);
        //btnBad.setOnClickListener(clickBad);
        // GOOD
        //btnGood = (Button) view.findViewById(R.id.player_btn_good);
        //btnGood.setVisibility(View.INVISIBLE);
        //btnGood = (Button) view.findViewById(R.id.player_btn_good);
        //btnGood.setOnClickListener(clickGood);
        // PAUSE
        btnPause = (Button) view.findViewById(R.id.player_btn_pause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FROForm.getInstance().pauseMusic();
                if (btnPause.isSelected()) {
                    btnPause.setSelected(false);
                } else {
                    btnPause.setSelected(true);
                }
            }
        });
        // SHARE
        btnShare = (Button) view.findViewById(R.id.player_btn_share);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FROFavoriteChannelDB db = FROFavoriteChannelDBFactory.getInstance(getActivity());
                if (db.getSize() < 99) {
                    favorite = new ChannelInfo(ChannelParams.getMode().text, ChannelParams.getChannelIdString(),
                            ChannelParams.getRange(), ChannelParams.getPermission(),
                            FROForm.getInstance().getNowPlaying().getChannelName(), null, Consts.MUSIC_TYPE_NORMAL,
                            FROForm.getInstance().isSkipControl());
                    ((BaseActivity) getActivity()).showTextBoxDialog(null, favorite.getChannelName(),
                            MainActivity.DIALOG_TAG_ADD_TO_FAVORITE_POS, MainActivity.DIALOG_TAG_ADD_TO_FAVORITE_NEG,
                            R.string.btn_favorite_regist, -1);
                } else {
                    ((BaseActivity) getActivity()).showToast(getString(R.string.msg_favorite_regist_error_maxsize));
                }
            }
        });
        // SKIP
        btnSkip = (Button) view.findViewById(R.id.player_btn_skip);
        btnSkip.setOnClickListener(clickSkip);
        // 楽曲情報
        txtArtist = (TextView) view.findViewById(R.id.player_txt_artist);
        txtTitle = (TextView) view.findViewById(R.id.player_txt_title);
        prgDuration = (ProgressBar) view.findViewById(R.id.player_prg_duration);
        imgJacket = (ImageView) view.findViewById(R.id.player_img_jacket);
    }

    //public View.OnClickListener clickBad = new View.OnClickListener() {
    //    @Override
    //    public void onClick(View v) {
    //        if (btnBad.isSelected()) {
    //            btnBad.setSelected(false);
    //            FROForm.getInstance().setRating(MCDefParam.MCP_PARAM_STR_RATING_NOP);
    //        } else {
    //            btnBad.setSelected(true);
    //            if (btnGood.isSelected())
    //                btnGood.setSelected(false);
    //            FROForm.getInstance().setRating(MCDefParam.MCP_PARAM_STR_RATING_BAD);
    //        }
    //        // FROForm.getInstance().callAPI();
    //    }
    //};

    //public View.OnClickListener clickGood = new View.OnClickListener() {
    //    @Override
    //    public void onClick(View v) {
    //        if (btnGood.isSelected()) {
    //            btnGood.setSelected(false);
    //            FROForm.getInstance().setRating(MCDefParam.MCP_PARAM_STR_RATING_NOP);
    //        } else {
    //            btnGood.setSelected(true);
    //            if (btnBad.isSelected())
    //                btnBad.setSelected(false);
    //            FROForm.getInstance().setRating(MCDefParam.MCP_PARAM_STR_RATING_GOOD);
    //        }
    //    }
    //};

    public View.OnClickListener clickDisableRating = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((BaseActivity) getActivity()).showToast(getString(R.string.msg_skip_button_disabled_for_benefit));
        }
    };

    public View.OnClickListener clickSkip = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((BaseActivity) getActivity()).showProgress(0);
            FROForm.getInstance().requestNext();
        }
    };

    public View.OnClickListener clickDisableSkip15Sec = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((BaseActivity) getActivity()).showToast(getString(R.string.msg_skip_button_limit_time_info));
        }
    };

    // public View.OnClickListener clickDisableSkipAd = new
    // View.OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // ((BaseActivity)
    // getActivity()).showToast(getString(R.string.msg_skip_button_disabled_for_ad));
    // }
    // };

    @Override
    public void updateViews() {
        if (isFirst) {
            isFirst = false;
        }
        FROForm form = FROForm.getInstance();
        String imagePath = null;

        // 画像パス
        if (form.getNowPlaying() != null && form.getNowPlaying().getArtwork() != null)
            imagePath = FROUtils.getJacketPath() + form.getNowPlaying().getArtwork().replace(".jpg", "");

        // イレギュラーケース、5秒後にリトライ
        if (form.getNowPlaying() == null || getActivity() == null) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateViews();
                }
            }, 1000 * 5);
            return;
        }

        MusicInfo nowplaying = form.getNowPlaying();
        if (nowplaying != null) {
            String artist = form.getNowPlaying().getArtist();
            if (!TextUtils.isEmpty(form.getNowPlaying().getThumb()))
                artist += "  —  " + form.getNowPlaying().getThumb();
            txtArtist.setText(artist);
            //txtArtist.setText(form.getNowPlaying().getArtist()); // artist name
            String title = form.getNowPlaying().getTitle();
            txtTitle.setText(title);
        }
        txtArtist.requestFocus();
        //txtTitle.requestFocus();
        // スキップの可否
        if (form.isSkippable()) {
            btnSkip.setBackgroundResource(R.drawable.new_res_btn_toolbar_skip);
            btnSkip.setOnClickListener(clickSkip);
        } else {
            btnSkip.setBackgroundResource(R.drawable.new_btn_toolbar_skip_off);
            if (!form.isSkipControl()) {
                btnSkip.setOnClickListener(clickDisableSkip15Sec);
            } else {
                btnSkip.setOnClickListener(clickDisableRating);
            }
        }
        ((IModeActivity) getActivity()).setHeaderTitle(form.getNowPlaying().getChannelName());
        switch (FROForm.getInstance().getIntRating()) {
            //case Consts.RATING_GOOD:
            //    btnBad.setSelected(false);
            //    btnGood.setSelected(true);
            //    break;
            //case Consts.RATING_BAD:
            //    btnBad.setSelected(true);
            //    btnGood.setSelected(false);
            //    break;
            //default:
            //    btnBad.setSelected(false);
            //    btnGood.setSelected(false);
            //    break;
        }
        //btnBad.setEnabled(true);
        //btnGood.setEnabled(true);
        //btnShare.setEnabled(true);
        //btnBad.setOnClickListener(clickBad);
        //btnGood.setOnClickListener(clickGood);
        //btnBad.setBackgroundResource(R.drawable.new_res_btn_toolbar_bad);
        //btnGood.setBackgroundResource(R.drawable.new_res_btn_toolbar_good);

        setJacket(imagePath);
        if (FROForm.getInstance().isPlaying()) {
            btnPause.setSelected(false);
        } else {
            btnPause.setSelected(true);
        }
    }

    public void onPast15Sec() {
        // if (!FROForm.getInstance().isPlayingAds()) {
        if (FROForm.getInstance().isSkippable()) {
            btnSkip.setBackgroundResource(R.drawable.new_res_btn_toolbar_skip);
            btnSkip.setOnClickListener(clickSkip);
        } else {
            btnSkip.setBackgroundResource(R.drawable.new_btn_toolbar_skip_off);
            if (!FROForm.getInstance().isSkipControl()) {
                btnSkip.setOnClickListener(clickDisableSkip15Sec);
            } else {
                btnSkip.setOnClickListener(clickDisableRating);
            }
        }
        // } else {
        // btnSkip.setBackgroundResource(R.drawable.new_btn_toolbar_skip_off);
        // btnSkip.setOnClickListener(clickDisableSkipAd);
        // }
    }

    int currentPos = -1;

    public void initProgress() {
        prgDuration.setProgress(0);
        if (!FROForm.getInstance().isPlayingAds()) {
            MusicInfo info = FROForm.getInstance().getNowPlaying();
            int length = info.getLength() / 1000;
            currentPos = FROForm.getInstance().getCurrentPlayerPos() / 1000;
            prgDuration.setMax(length);
            prgDuration.setProgress(currentPos);
            if (progressTimer == null) {
                progressTimer = new SimpleTimer2(this, true, TIMER_TYPE_PROGRESS);
            }
            progressTimer.start(1000);
        }
    }

    @Override
    public void onTimerEvent(int type) {
        switch (type) {
            case TIMER_TYPE_PROGRESS:
                if (getActivity() != null)
                    ((BaseActivity) getActivity()).getUiHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            if (FROForm.getInstance().isPlaying())
                                prgDuration.setProgress(++currentPos);
                        }
                    });
        }
    }

    public void stopTimer() {
        if (progressTimer != null) {
            progressTimer.stop();
            progressTimer = null;
        }
    }

    private void setJacket(String path) {
        // ジャケットリソースの解放
        if (jacketResource != null) {
            jacketResource.setCallback(null);
            jacketResource = null;
        }
        // ジャケットファイルの確認
        File file = null;
        if (path != null)
            file = new File(path);
        if (file != null && file.isFile()) {
            // リソースが有ればDrawableとして読み込み
            Bitmap jacket = Utils.loadBitmap(path);
            // 低スペック端末でデコードに失敗する可能性があるためチェックする
            if (jacket != null) {
                jacketResource = new BitmapDrawable(getResources(), jacket);
            } else {
                jacketResource = getActivity().getResources().getDrawable(R.drawable.noimage);
            }
        } else {
            // 無ければ共通画像をDrawableとして読み込み
            jacketResource = getActivity().getResources().getDrawable(R.drawable.noimage);
        }
        imgJacket.setImageDrawable(jacketResource);
        // XMLで定義したアニメーションを読み込む
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_player_jacket_in);
        // リストアイテムのアニメーションを開始
        // imgJacket.startAnimation(anim);
    }

    public ChannelInfo getFavorite() {
        return this.favorite;
    }

    @Override
    public void show() {
    }

    @Override
    public void onBackPress() {
        ((IModeActivity) getActivity()).showMode(MainActivity.TOP);
    }

    public void onBtnClicked(int resId) {
        switch (resId) {
            case R.id.header_btn_right:
                if (FROForm.getInstance().isPlayingAds())
                    return;

                if (mAffiliateUrl != null)
                    mAffiliateUrl.clear();
                mAffiliateUrl = new ArrayList<String>();
                if (mCartItem != null)
                    mCartItem.clear();
                mCartItem = new ArrayList<String>();
                ArrayList<Pair<String, String>> urlList = FROForm.getInstance().getNowPlaying().getUrlPairList();
                if (urlList != null && urlList.size() > 0) {
                    for (Pair<String, String> pair : urlList) {
                        if (pair.first.equals(Consts.AFFILIATE_BUY_CD)) {
                            mAffiliateUrl.add(pair.second);
                            mCartItem.add(getString(R.string.btn_buy_cd));
                        } else if (pair.first.equals(Consts.AFFILIATE_DOWNLOAD_ANDROID)) {
                            // mAffiliateUrl.add(tmp[1]);
                            // mCartItem.add(getString(R.string.btn_buy_music));
                        }
                    }
                }
                mCartItem.add(getString(R.string.btn_cancel));
                ((BaseActivity) getActivity()).showListDialog(MainActivity.DIALOG_TAG_CART, null,
                        mCartItem.toArray(new String[0]));

                break;
            // case 12345:
            // case R.id.header_btn_right:
            // ((BaseActivity) getActivity()).showListDialog(12345, "Change Base
            // Volume", VOLUME_SET);
            // break;
        }
    }

    // public static final String[] VOLUME_SET = { "1.0", "0.9", "0.8", "0.7",
    // "0.6", "0.5", "0.4", "0.3", "0.2", "0.1",
    // "Cancel" };

    public void onItemClicked(int tag, int pos) {
        switch (tag) {
            case MainActivity.DIALOG_TAG_CART:
                String item = mCartItem.get(pos);
                // if (item.equalsIgnoreCase(getString(R.string.btn_artist_search)))
                // {
                // String encodeKey =
                // Uri.encode(FROForm.getInstance().getNowPlaying().getArtist() + "
                // "
                // + FROForm.getInstance().getNowPlaying().getTitle());
                // String query;
                // if (Utils.isJapanese()) {
                // query = QUERY_URL + URL_JA_IMG + encodeKey + "&lr";
                // } else {
                // query = QUERY_URL + URL_EN_IMG + encodeKey + "&lr";
                // }
                // ((BaseActivity) getActivity()).startBrowser(query);
                // } else
                if (item.equalsIgnoreCase(getString(R.string.btn_cancel))) {
                } else {
                    ((BaseActivity) getActivity()).startBrowser(mAffiliateUrl.get(pos));
                }
                break;
        }
    }

    @Override
    public void getData() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onError(int when, int code) {
        // TODO Auto-generated method stub

    }
}
