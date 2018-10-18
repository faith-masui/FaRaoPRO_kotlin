package jp.faraopro.play.frg.newone;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import jp.faraopro.play.R;

/**
 * タイトルバーを構成するフラグメント<br>
 * 
 * @author Aim
 */
public class HeaderFragment extends Fragment {
	/** const **/

	/** bundle key **/
	public static final String CAPTION = "CAPTION";
	public static final String LEFT_VISIBLE = "LEFT_VISIBLE";
	public static final String RIGHT_VISIBLE = "RIGHT_VISIBLE";

	/** view **/
	private LinearLayout lnrBackground;
	private Button btnLeft;
	private Button btnRight;
	private ImageButton ibtnLeft;
	private ImageButton ibtnRight;
	private ImageView imgCaption;
	private TextView txtCaption;
	private LinearLayout lnrParent;

	/** member **/
	private OnBtnClickListener btnClickListener;

	public static HeaderFragment newInstance() {
		HeaderFragment instance = new HeaderFragment();

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
		View view = inflater.inflate(R.layout.new_frg_header, container, false);
		initViews(view);

		return view;
	}

	// Viewの初期化(参照取得、イベント設定など)
	private void initViews(View view) {
		int caption = -1;
		boolean left = false;
		boolean right = false;
		Bundle args = getArguments();

		if (args != null) {
			caption = args.getInt(CAPTION, -1);
			left = args.getBoolean(LEFT_VISIBLE, false);
			right = args.getBoolean(RIGHT_VISIBLE, false);
		}
		// 親View
		lnrParent = (LinearLayout) view.findViewById(R.id.header_lnr_parent);
		lnrBackground = (LinearLayout) view.findViewById(R.id.header_lnr_background);
		// キャプション
		imgCaption = (ImageView) view.findViewById(R.id.header_img_logo);
		txtCaption = (TextView) view.findViewById(R.id.header_txt_caption);
		if (caption != -1 && caption != R.drawable.logo_white) {
			txtCaption.setVisibility(View.VISIBLE);
			imgCaption.setVisibility(View.GONE);
			txtCaption.setText(caption);
		} else {
			txtCaption.setVisibility(View.GONE);
			imgCaption.setVisibility(View.VISIBLE);
		}

		// 左ボタン
		btnLeft = (Button) view.findViewById(R.id.header_btn_left);
		btnLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (btnClickListener != null)
					btnClickListener.onBtnClicked(R.id.header_btn_left);
			}
		});
		if (left) {
			btnLeft.setVisibility(View.VISIBLE);
		} else {
			btnLeft.setVisibility(View.GONE);
		}
		ibtnLeft = (ImageButton) view.findViewById(R.id.header_ibtn_left);
		ibtnLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (btnClickListener != null)
					btnClickListener.onBtnClicked(R.id.header_btn_left);
			}
		});

		// 右ボタン
		btnRight = (Button) view.findViewById(R.id.header_btn_right);
		btnRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (btnClickListener != null)
					btnClickListener.onBtnClicked(R.id.header_btn_right);
			}
		});
		if (right) {
			btnRight.setVisibility(View.VISIBLE);
		} else {
			btnRight.setVisibility(View.GONE);
		}
		ibtnRight = (ImageButton) view.findViewById(R.id.header_ibtn_right);
		ibtnRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (btnClickListener != null)
					btnClickListener.onBtnClicked(R.id.header_btn_right);
			}
		});

	}

	/**
	 * このフラグメントの横幅を返す
	 * 
	 * @return 横幅
	 */
	public int getWidth() {
		if (lnrParent != null)
			return lnrParent.getWidth();
		else
			return -1;
	}

	/**
	 * このフラグメントの高さを返す
	 * 
	 * @return 高さ
	 */
	public int getHeight() {
		if (lnrParent != null)
			return lnrParent.getHeight();
		else
			return -1;
	}

	/**
	 * 中央のキャプション(画像)のリソースを指定する
	 * 
	 * @param resId
	 *            リソースID
	 */
	public void setImageCaption(int resId) {
		imgCaption.setImageResource(resId);
		imgCaption.setScaleType(ScaleType.FIT_CENTER);
		imgCaption.setVisibility(View.VISIBLE);
		txtCaption.setVisibility(View.GONE);
	}

	/**
	 * 中央のキャプション(文字)のリソースを指定する
	 * 
	 * @param resId
	 *            リソースID
	 */
	public void setTextCaption(int resId) {
		txtCaption.setText(resId);
		txtCaption.setVisibility(View.VISIBLE);
		imgCaption.setVisibility(View.GONE);
	}

	/**
	 * 中央のキャプション(文字)の文言を指定する
	 * 
	 * @param resId
	 *            表示文言
	 */
	public void setTextCaption(String txt) {
		txtCaption.setText(txt);
		txtCaption.setVisibility(View.VISIBLE);
		imgCaption.setVisibility(View.GONE);
	}

	/**
	 * 左ボタンの表示設定
	 * 
	 * @param visible
	 *            true:表示, false:非表示
	 */
	public void setLeftVisibility(boolean visible) {
		if (visible)
			this.btnLeft.setVisibility(View.VISIBLE);
		else
			this.btnLeft.setVisibility(View.GONE);
	}

	/**
	 * 左ボタンの表示設定
	 * 
	 * @param visible
	 *            true:表示, false:非表示
	 */
	public void setLeftImageVisibility(boolean visible) {
		if (visible)
			this.ibtnLeft.setVisibility(View.VISIBLE);
		else
			this.ibtnLeft.setVisibility(View.GONE);
	}

	public void setLeftText(String txt) {
		if (txt.equalsIgnoreCase(getString(R.string.page_title_home))) {
			this.btnLeft.setVisibility(View.GONE);
			this.ibtnLeft.setVisibility(View.VISIBLE);
			this.ibtnLeft.setImageResource(R.drawable.new_res_btn_header_home);
		} else {
			this.btnLeft.setVisibility(View.VISIBLE);
			this.ibtnLeft.setVisibility(View.GONE);
			this.btnLeft.setText(txt);
		}
	}

	/**
	 * 右ボタンの表示設定
	 * 
	 * @param visible
	 *            true:表示, false:非表示
	 */
	public void setRightVisibility(boolean visible) {
		if (visible)
			this.btnRight.setVisibility(View.VISIBLE);
		else
			this.btnRight.setVisibility(View.GONE);
	}

	/**
	 * 右ボタンの表示設定
	 * 
	 * @param visible
	 *            true:表示, false:非表示
	 */
	public void setRightImageVisibility(boolean visible) {
		if (visible)
			this.ibtnRight.setVisibility(View.VISIBLE);
		else
			this.ibtnRight.setVisibility(View.GONE);
	}

	public void setRightText(String txt) {
		if (txt.equalsIgnoreCase("Play")) {
			this.btnRight.setVisibility(View.GONE);
			this.ibtnRight.setVisibility(View.VISIBLE);
			this.ibtnRight.setImageResource(R.drawable.new_res_btn_header_play);
		} else if (txt.equalsIgnoreCase("Cart")) {
			this.btnRight.setVisibility(View.GONE);
			this.ibtnRight.setVisibility(View.GONE);
		} else if (txt.equalsIgnoreCase("Update")) {
			this.btnRight.setVisibility(View.GONE);
			this.ibtnRight.setVisibility(View.VISIBLE);
			this.ibtnRight.setImageResource(R.drawable.icon_update);
		} else if (txt.equalsIgnoreCase("Info")) {
			this.btnRight.setVisibility(View.GONE);
			this.ibtnRight.setVisibility(View.VISIBLE);
			this.ibtnRight.setImageResource(R.drawable.new_res_btn_header_info);
		} else {
			this.btnRight.setVisibility(View.VISIBLE);
			this.ibtnRight.setVisibility(View.GONE);
			this.btnRight.setText(txt);
		}
	}

	public void changeColor(int color) {
		lnrBackground.setBackgroundResource(color);
	}

}
