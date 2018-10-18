package jp.faraopro.play.frg.mode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.util.Utils;

/**
 * 
 * @author Aim タイトルバー
 */
public class AboutFragment extends Fragment implements IModeFragment {
	/** const **/
	private static String DIRECTORY_NAME = "license";

	/** bundle key **/

	/** view **/
	private LinearLayout lnrLicense;
	private TextView txtAbout;

	/** member **/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_frg_about, container, false);
		initViews(view);

		return view;
	}

	// Viewの初期化(参照取得、イベント設定など)
	@SuppressLint("SetJavaScriptEnabled")
	private void initViews(View view) {
		Bundle args = getArguments();
		if (args != null) {
		}

		txtAbout = (TextView) view.findViewById(R.id.about_txt_about);
		// String webSite;
		final Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		// String txt = String.format(getString(R.string.msg_about),
		// ADTUtilities.getVersionName(getActivity()),
		// Integer.toString(year));
		// 2013固定
		String txt = String.format(getString(R.string.msg_about), Utils.getVersionName(getActivity()), "2015");
		txtAbout.setText(txt);
		Linkify.addLinks(txtAbout, Linkify.EMAIL_ADDRESSES | Linkify.WEB_URLS);
		lnrLicense = (LinearLayout) view.findViewById(R.id.about_lnr_license);
		addLicense();
	}

	private void addLicense() {
		String[] files = getFileNames();
		InputStream is = null;
		BufferedReader br = null;
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				StringBuilder sb = new StringBuilder();
				try {
					is = getActivity().getAssets().open(DIRECTORY_NAME + "/" + files[i]);
					br = new BufferedReader(new InputStreamReader(is));
					String str;
					while ((str = br.readLine()) != null) {
						sb.append(str + "\n");
					}
				} catch (IOException e) {
					return;
				}
				TextView name = new TextView(getActivity());
				name.setGravity(Gravity.CENTER_HORIZONTAL);
				name.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
				TextView text = new TextView(getActivity());
				text.setGravity(Gravity.CENTER_HORIZONTAL);
				name.setText(files[i] + "\n");
				text.setText(sb.toString() + "\n\n\n");
				Linkify.addLinks(text, Linkify.EMAIL_ADDRESSES | Linkify.WEB_URLS);
				lnrLicense.addView(name);
				lnrLicense.addView(text);
			}
		}
	}

	private String[] getFileNames() {
		String[] subFiles = null;
		AssetManager assets = getActivity().getAssets();
		try {
			String[] list = assets.list(DIRECTORY_NAME);
			subFiles = list;
		} catch (IOException e) {
			subFiles = null;
		}

		return subFiles;
	}

	@Override
	public void onStart() {
		super.onStart();

		if (FROForm.getInstance().isTablet(getActivity())) {
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if (!hidden) {
		} else {
		}
	}

	@Override
	public void show() {
	}

	@Override
	public void updateViews() {
	}

	@Override
	public void onBackPress() {
		((IModeActivity) getActivity()).showMode(MainActivity.SETTING);
	}

	@Override
	public void getData() {
	}

	@Override
	public void onError(int when, int code) {
	}
}
