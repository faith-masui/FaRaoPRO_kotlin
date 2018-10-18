package jp.faraopro.play.frg.mode;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.BootActivity;
import jp.faraopro.play.act.newone.LoginActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Flavor;
import jp.faraopro.play.common.LocationHelper;
import jp.faraopro.play.common.TextChecker;
import jp.faraopro.play.mclient.MCDefParam;
import jp.faraopro.play.mclient.MCUserInfoPreference;

/**
 * 
 * @author Aim タイトルバー
 */
public class LoginSelectFragment extends Fragment implements TextWatcher {
	/** dialog tags **/
	private static final int DIALOG_INVALID_PASS = 100;

	/** view **/
	private Button btnLogin;
	private EditText edtEmail;
	private EditText edtPassword;

	/** member **/
	String email;
	String password;

	public static LoginSelectFragment newInstance() {
		LoginSelectFragment instance = new LoginSelectFragment();

		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_frg_login_select, container, false);
		initViews(view);

		return view;
	}

	// Viewの初期化(参照取得、イベント設定など)
	@SuppressLint("SetJavaScriptEnabled")
	private void initViews(View view) {
		Bundle args = getArguments();
		if (args != null) {
		}

		btnLogin = (Button) view.findViewById(R.id.login_btn_login);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!LocationHelper.isEnableProvider(getActivity())) {
					((BaseActivity) getActivity()).showNegativeDialog(R.string.msg_logout_failed_location,
							LoginActivity.DIALOG_TAG_GOTO_SETTING_FOR_LOCATION, -1, true);
					return;
				}

				email = edtEmail.getText().toString();
				password = edtPassword.getText().toString();
				String errorMsg = null;

				if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
					errorMsg = getString(R.string.msg_input_all_fields);
				} else if (TextChecker.CheckType(password, 7) != 0 || !TextChecker.CheckAppearance(password)
						|| !TextChecker.CheckLength(password, 6, 16)) {
					errorMsg = getString(R.string.msg_invalid_password);
				}
				if (errorMsg == null) {
					((BaseActivity) getActivity()).showProgress();
                    FROForm.getInstance().login(email, password, MCDefParam.MCP_PARAM_STR_NO);
				} else {
					((BaseActivity) getActivity()).showPositiveDialog(errorMsg, DIALOG_INVALID_PASS, true);
				}
			}
		});
		edtEmail = (EditText) view.findViewById(R.id.login_edt_email);
		edtPassword = (EditText) view.findViewById(R.id.login_edt_password);

        if (Flavor.DEVELOPER_MODE) {
            edtEmail.setText("f1000.302@biz.faraoradio.jp");
            edtPassword.setText("qwe123");
        }

        String email;
        String password;
        try {
            email = FROForm.getInstance().getPreferenceString(MCUserInfoPreference.STRING_DATA_MAIL);
            password = FROForm.getInstance().getPreferenceString(MCUserInfoPreference.STRING_DATA_PASS);
        } catch (Exception e) {
            ((BaseActivity) getActivity()).startActivity(BootActivity.class, true);
            return;
        }
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            edtEmail.setText(email);
            edtPassword.setText(password);
		}
	}

	public String getEmail() {
		return email;
	}

	public String getPass() {
		return password;
	}

	private boolean checkEmpty() {
		boolean isEmpty = true;
		String mail = edtEmail.getText().toString();
		String pass = edtPassword.getText().toString();
		if (!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pass))
			isEmpty = false;

		return isEmpty;
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		btnLogin.setEnabled(!checkEmpty());
	}

	@Override
	public void afterTextChanged(Editable arg0) {
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}
}
