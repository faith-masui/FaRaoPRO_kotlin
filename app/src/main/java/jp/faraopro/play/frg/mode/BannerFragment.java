package jp.faraopro.play.frg.mode;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.common.Flavor;
import jp.faraopro.play.model.ChannelMode;
import jp.faraopro.play.model.UserDataHelper;

/**
 * タイトルバーを構成するフラグメント<br>
 * 
 * @author Aim
 */
public class BannerFragment extends Fragment implements IModeFragment {
	/** const **/

	/** bundle key **/

	/** view **/
	private WebView webMain;
	private ViewSwitcher vswLoading;
	private LinearLayout lnrTop;
	private LinearLayout lnrBottom;

	// private PullToRefreshScrollView mPullRefreshScrollView;

	/** member **/

	public static BannerFragment newInstance() {
		BannerFragment instance = new BannerFragment();

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
		View view = inflater.inflate(R.layout.new_frg_browser_pulltorefresh, container, false);
		initViews(view);

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();

		loadUrl();
	}

	// Viewの初期化(参照取得、イベント設定など)
	@SuppressLint("SetJavaScriptEnabled")
	private void initViews(View view) {
		webMain = (WebView) view.findViewById(R.id.browser_web_main);
		webMain.clearCache(true);
		WebSettings ws = webMain.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setBuiltInZoomControls(false);
		ws.setSupportZoom(false);
		ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
		if (FROForm.getInstance().isTablet(getActivity())) {
			String ua = ws.getUserAgentString();
			ws.setUserAgentString(ua + " FaRaoLayout/landscape");
		}
		vswLoading = (ViewSwitcher) view.findViewById(R.id.browser_swt_banner);
		lnrTop = (LinearLayout) view.findViewById(R.id.browser_lnr_top);
		lnrBottom = (LinearLayout) view.findViewById(R.id.browser_lnr_bottom);

	}

	public void setBottomPadding(int height) {
		lnrBottom.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, height));
	}

	public void setTopPadding(int height) {
		lnrTop.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, height));
	}

	public void loadUrl() {
		webMain.setWebViewClient(new CustomWebViewClient());
		if (vswLoading.getCurrentView() instanceof ScrollView) {
			vswLoading.showPrevious();
		}
		String device = "?device=android";
        String appVer = "&appver=" + Flavor.APP_VERSION;
		String portrait;
		if (FROForm.getInstance().isTablet(getActivity())) {
			portrait = "&orientation=landscape";
		} else {
			portrait = "&orientation=portrait";
		}
        String serviceLevel = TextUtils.isEmpty(UserDataHelper.getServiceLevel()) ? "" : "_" + UserDataHelper.getServiceLevel();
        loadUrl = String.format(Flavor.URL_TOP, serviceLevel) + device + appVer + portrait;
        FRODebug.logD(getClass(), "loadUrl " + loadUrl, true);
        webMain.loadUrl(loadUrl);
	}

    private String loadUrl;

	// hooking load action of webview
	private class CustomWebViewClient extends WebViewClient {

		@Override
		public void onPageFinished(WebView view, String url) {
            FRODebug.logD(getClass(), "onPageFinished " + loadUrl, true);
            if (url != null && url.equals(loadUrl)) {
				if (!(vswLoading.getCurrentView() instanceof ScrollView)) {
					vswLoading.showNext();
				}
			}
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.stopLoading();
            if (!url.contains(Flavor.PLAY_LINK)) {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
				return false;
			} else {
					ArrayList<String> params = bannerUrlParser(url);
					String mode = null;
					String channel = "-1";
					String range = null;

					if (params.size() > 0)
						mode = params.get(0);
					if (params.size() > 1)
						channel = params.get(1);
					if (params.size() > 2)
						range = params.get(2);

					// ジャンル除外対応
                if (!TextUtils.isEmpty(mode) && mode.equals(ChannelMode.GENRE.text)) {
                    ((BaseActivity) getActivity()).showToast(getString(R.string.msg_can_not_play_channel));
                    return false;
                }
                ((MainActivity) getActivity()).bootPlayer(ChannelMode.valueOfString(mode), Integer.parseInt(channel),
                        range);

				return false;
			}
		}
	}

	// 与えられたurlをパースし、WebAPI(radio/listen)用のパラメータを作成する
	private ArrayList<String> bannerUrlParser(String url) {
		ArrayList<String> params = null;

		if (url != null) {
			params = new ArrayList<String>();
			String[] tmp = new String[3];

			tmp = url.split("\\?");
			tmp = tmp[1].split("&");
			for (String str : tmp) {
				String param = null;
				param = str.split("=")[1];
				params.add(param);
			}
		}

		return params;
	}

	@Override
	public void show() {
	}

	@Override
	public void updateViews() {
	}

	@Override
	public void onBackPress() {
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
