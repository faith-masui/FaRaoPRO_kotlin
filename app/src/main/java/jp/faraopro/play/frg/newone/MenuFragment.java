package jp.faraopro.play.frg.newone;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import jp.faraopro.play.R;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.common.Updater;
import jp.faraopro.play.model.UserDataHelper;

/**
 * タイトルバーを構成するフラグメント<br>
 * 
 * @author Aim
 */
public class MenuFragment extends Fragment {
	/** const **/

	/** bundle key **/

	/** view **/
	private LinearLayout lnrParent;
	private LinearLayout lnrHome;
	private LinearLayout lnrBtn1;
	private LinearLayout lnrBtn2;
	private LinearLayout lnrBtn3;
	private LinearLayout lnrOther;
	private ImageView img1;
	private ImageView img2;
	private ImageView img3;
	private ImageView mImageUpdate;
	private TextView txt1;
	private TextView txt2;
	private TextView txt3;
	private LinearLayout[] layouts = new LinearLayout[3];
	private ImageView[] images = new ImageView[3];
	private TextView[] texts = new TextView[3];

	/** member **/
	private OnBtnClickListener btnClickListener;
	private ArrayList<Integer> menus;

	public static MenuFragment newInstance() {
		MenuFragment instance = new MenuFragment();

		return instance;
	}

	public interface OnBtnClickListener {
		/**
		 * 各フラグメントのボタン押下時の動作を設定する
		 * 
		 * @param resId
		 *            押下されたViewのリソースID
		 */
		public void onBtnClicked(int resId);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnBtnClickListener == true)
			btnClickListener = ((OnBtnClickListener) activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_frg_menu1, container, false);
		menus = FROForm.getInstance().getMenuList(getActivity());
		initViews(view);

		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if (!hidden) {
			menus = FROForm.getInstance().getMenuList(getActivity());
			renewIcons();
		}
	}

	// Viewの初期化(参照取得、イベント設定など)
	private void initViews(View view) {
		Bundle args = getArguments();

		if (args != null) {
		}
		// 親View
		lnrParent = (LinearLayout) view.findViewById(R.id.menu_lnr_parent);
		// ボタン1
		lnrHome = (LinearLayout) view.findViewById(R.id.menu_lnr_home);
		// btnHome = (Button) view.findViewById(R.id.menu_btn_home);
		lnrHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (btnClickListener != null)
					btnClickListener.onBtnClicked(R.id.menu_lnr_home);
				setSelected(0);
			}
		});
		// ボタン1
		lnrBtn1 = (LinearLayout) view.findViewById(R.id.menu_lnr_btn_1);
		img1 = (ImageView) view.findViewById(R.id.menu_img_btn_1);
		txt1 = (TextView) view.findViewById(R.id.menu_txt_btn_1);
		lnrBtn1.setOnClickListener(getClickListener(1));
		// ボタン2
		lnrBtn2 = (LinearLayout) view.findViewById(R.id.menu_lnr_btn_2);
		img2 = (ImageView) view.findViewById(R.id.menu_img_btn_2);
		txt2 = (TextView) view.findViewById(R.id.menu_txt_btn_2);
		lnrBtn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (btnClickListener != null)
					btnClickListener.onBtnClicked(R.id.menu_lnr_btn_2);
				setSelected(2);
			}
		});
		// ボタン3
		lnrBtn3 = (LinearLayout) view.findViewById(R.id.menu_lnr_btn_3);
		img3 = (ImageView) view.findViewById(R.id.menu_img_btn_3);
		txt3 = (TextView) view.findViewById(R.id.menu_txt_btn_3);
		lnrBtn3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (btnClickListener != null)
					btnClickListener.onBtnClicked(R.id.menu_lnr_btn_3);
				setSelected(3);
			}
		});
		// ボタン1
		lnrOther = (LinearLayout) view.findViewById(R.id.menu_lnr_other);
		// btnOther = (Button) view.findViewById(R.id.menu_btn_other);
		lnrOther.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (btnClickListener != null)
					btnClickListener.onBtnClicked(R.id.menu_lnr_other);
				setSelected(4);
			}
		});
		mImageUpdate = (ImageView) view.findViewById(R.id.menu_img_update);
		layouts[0] = lnrBtn1;
		layouts[1] = lnrBtn2;
		layouts[2] = lnrBtn3;
		images[0] = img1;
		images[1] = img2;
		images[2] = img3;
		texts[0] = txt1;
		texts[1] = txt2;
		texts[2] = txt3;
		renewIcons();
	}

	private OnClickListener clickBtn1 = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (btnClickListener != null)
				btnClickListener.onBtnClicked(R.id.menu_lnr_btn_1);
			setSelected(1);
		}
	};

	private OnClickListener clickBtn2 = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (btnClickListener != null)
				btnClickListener.onBtnClicked(R.id.menu_lnr_btn_2);
			setSelected(2);
		}
	};

	private OnClickListener clickBtn3 = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (btnClickListener != null)
				btnClickListener.onBtnClicked(R.id.menu_lnr_btn_3);
			setSelected(3);
		}
	};

	private OnClickListener getClickListener(int pos) {
		switch (pos) {
		case 0:
			return clickBtn1;
		case 1:
			return clickBtn2;
		case 2:
			return clickBtn3;
		default:
			return null;
		}
	}

	public void renewIcons() {
		for (int i = 0; i < images.length; i++) {
			Integer name = menus.get(i);
			if (name.equals(Consts.TAG_MODE_ARTIST)) {
				boolean enableSearch = UserDataHelper.hasSearch();
				if (enableSearch) {
					layouts[i].setOnClickListener(getClickListener(i));
				} else {
					layouts[i].setOnClickListener(null);
				}
				images[i].setImageResource(R.drawable.new_res_btn_artist);
				texts[i].setText(R.string.page_title_artist);
				images[i].setEnabled(enableSearch);
				texts[i].setEnabled(enableSearch);
			} else if (name.equals(Consts.TAG_MODE_HISTORY)) {
				images[i].setImageResource(R.drawable.new_res_btn_history);
				texts[i].setText(R.string.page_title_goodlist_pro);
				images[i].setEnabled(true);
				texts[i].setEnabled(true);
				layouts[i].setOnClickListener(getClickListener(i));
			} else if (name.equals(Consts.TAG_MODE_MYCHANNEL)) {
				images[i].setImageResource(R.drawable.new_res_btn_mychannel);
				texts[i].setText(R.string.page_title_favorite);
				images[i].setEnabled(true);
				texts[i].setEnabled(true);
				layouts[i].setOnClickListener(getClickListener(i));
			} else if (name.equals(Consts.TAG_MODE_RELEASE)) {
				boolean enableSearch = UserDataHelper.hasSearch();
				if (enableSearch) {
					layouts[i].setOnClickListener(getClickListener(i));
				} else {
					layouts[i].setOnClickListener(null);
				}
				images[i].setImageResource(R.drawable.new_res_btn_release);
				texts[i].setText(R.string.page_title_released_year_pro);
				images[i].setEnabled(enableSearch);
				texts[i].setEnabled(enableSearch);
			} else if (name.equals(Consts.TAG_MODE_SPECIAL)) {
				images[i].setImageResource(R.drawable.new_res_btn_special);
				texts[i].setText(R.string.page_title_special_pro);
				images[i].setEnabled(true);
				texts[i].setEnabled(true);
				layouts[i].setOnClickListener(getClickListener(i));
			} else if (name.equals(Consts.TAG_MODE_STREAMING)) {
				images[i].setImageResource(R.drawable.new_res_btn_stream);
				texts[i].setText(R.string.page_title_simulcast_list);
				images[i].setEnabled(false);
				texts[i].setEnabled(false);
				layouts[i].setOnClickListener(null);
			}
		}

		renewOtherIcon();
	}

	public void renewOtherIcon() {
		if (Updater.getInstance().ismAvailableUpdate()) {
			mImageUpdate.setVisibility(View.VISIBLE);
		} else {
			mImageUpdate.setVisibility(View.GONE);
		}
	}

	public void setSelected(int index) {
		switch (index) {
		case 0:
			lnrHome.setSelected(true);
			lnrOther.setSelected(false);
			for (LinearLayout b : layouts) {
				b.setSelected(false);
			}
			break;
		case 1:
			lnrBtn1.setSelected(true);
			lnrOther.setSelected(false);
			lnrHome.setSelected(false);
			lnrBtn2.setSelected(false);
			lnrBtn3.setSelected(false);
			break;
		case 2:
			lnrBtn2.setSelected(true);
			lnrOther.setSelected(false);
			lnrHome.setSelected(false);
			lnrBtn1.setSelected(false);
			lnrBtn3.setSelected(false);
			break;
		case 3:
			lnrBtn3.setSelected(true);
			lnrOther.setSelected(false);
			lnrHome.setSelected(false);
			lnrBtn1.setSelected(false);
			lnrBtn2.setSelected(false);
			break;
		case 4:
			lnrOther.setSelected(true);
			lnrHome.setSelected(false);
			for (LinearLayout b : layouts) {
				b.setSelected(false);
			}
			break;
		}
	}

	public int getWidth() {
		if (lnrParent != null)
			return lnrParent.getWidth();
		else
			return -1;
	}

	public int getHeight() {
		if (lnrParent != null)
			return lnrParent.getHeight();
		else
			return -1;
	}

}
