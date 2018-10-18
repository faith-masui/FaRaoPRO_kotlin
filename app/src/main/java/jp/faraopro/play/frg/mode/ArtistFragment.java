package jp.faraopro.play.frg.mode;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;

public class ArtistFragment extends Fragment implements IModeFragment {
	/** const **/
	private static final int RESULT = 0;
	private static final int FEATURED = 1;

	/** fragment tag **/
	private static final String TAG_FRG_FEATURED = "FRG_FEATURED";
	private static final String TAG_FRG_RESULT = "FRG_RESULT";

	/** bundle key **/

	/** view **/
	private LinearLayout lnrHeader;
	private EditText edtInput;
	private ImageButton ibtnSearch;
	private TextView txtTitle;
	private ArtistResultFragment frgResult;
	private ArtistFeaturedFragment frgFeatured;

	/** member **/
	private int type = FEATURED;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_simple_2block, container, false);
		initViews(view);

		return view;
	}

	protected void initViews(View view) {
		lnrHeader = (LinearLayout) view.findViewById(R.id.simple2_lnr_header);
		// 検索エリアの読み込み
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.new_res_search, null);
		v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		lnrHeader.addView(v);

		// 文字入力エリアのフォーカス設定
		edtInput = (EditText) v.findViewById(R.id.search_edt_input);

		// 検索ボタンの設定
		ibtnSearch = (ImageButton) v.findViewById(R.id.search_ibtn_search);
		ibtnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideKeyboard();
				String input = edtInput.getText().toString();
				// 入力が空でなければ検索する
				if (!TextUtils.isEmpty(input)) {
					((BaseActivity) getActivity()).showProgress();
					type = RESULT;
					FROForm.getInstance().getArtistList(input);
				}
			}
		});
		txtTitle = (TextView) v.findViewById(R.id.search_txt_title);

		FragmentManager manager = getChildFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		// FEATURED
		frgFeatured = (ArtistFeaturedFragment) manager.findFragmentByTag(TAG_FRG_FEATURED);
		if (frgFeatured == null) {
			frgFeatured = new ArtistFeaturedFragment();
			Bundle args = new Bundle();
			args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_IMAGE);
			frgFeatured.setArguments(args);
			transaction.add(R.id.simple2_lnr_body, frgFeatured, TAG_FRG_FEATURED);
		}
		// FEATURED
		frgResult = (ArtistResultFragment) manager.findFragmentByTag(TAG_FRG_RESULT);
		if (frgResult == null) {
			frgResult = new ArtistResultFragment();
			Bundle args = new Bundle();
			args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_NORMAL);
			frgResult.setArguments(args);
			transaction.add(R.id.simple2_lnr_body, frgResult, TAG_FRG_RESULT);
			transaction.hide(frgResult);
		}
		transaction.commitAllowingStateLoss();
	}

	private void changeFragment(int type, boolean isBackStack) {
		FragmentManager manager = getChildFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		if (type == FEATURED) {
			transaction.hide(frgResult);
			transaction.show(frgFeatured);

			edtInput.setText("");
			txtTitle.setText(R.string.header_artist_attention);
			// int animType = R.anim.slide_left_to_right;
			// // XMLで定義したアニメーションを読み込む
			// Animation anim = AnimationUtils.loadAnimation(getActivity(),
			// animType);
			// // リストアイテムのアニメーションを開始
			// txtTitle.startAnimation(anim);
		} else {
			transaction.show(frgResult);
			transaction.hide(frgFeatured);
			txtTitle.setText(R.string.page_title_shopsearch_result);
			// int animType = R.anim.slide_right_to_left;
			// // XMLで定義したアニメーションを読み込む
			// Animation anim = AnimationUtils.loadAnimation(getActivity(),
			// animType);
			// // リストアイテムのアニメーションを開始
			// txtTitle.startAnimation(anim);
		}
		if (isBackStack)
			transaction.addToBackStack(null);
		transaction.commitAllowingStateLoss();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if (!hidden && frgFeatured != null) {
			changeFragment(FEATURED, false);
			type = FEATURED;
			frgFeatured.getData();
		}
		if (hidden)
			hideKeyboard();
	}

	// ソフトウェアキーボードを隠す
	private void hideKeyboard() {
		if (edtInput == null)
			return;

		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edtInput.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public void show() {
		if (type == FEATURED) {
			changeFragment(FEATURED, false);
			frgFeatured.show();
		} else {
			changeFragment(RESULT, false);
			frgResult.show();
		}
	}

	@Override
	public void updateViews() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onBackPress() {
		if (type == RESULT) {
			type = FEATURED;
			changeFragment(FEATURED, false);
		} else {
			if (FROForm.getInstance().isMenu(Consts.TAG_MODE_ARTIST, getActivity())) {
				hideKeyboard();
				((MainActivity) getActivity()).showMode(MainActivity.TOP);
			} else {
				hideKeyboard();
				((MainActivity) getActivity()).showMode(MainActivity.OTHER);
			}
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
