package jp.faraopro.play.frg.mode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.BootActivity;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Flavor;
import jp.faraopro.play.domain.MainPreference;
import jp.faraopro.play.mclient.MCUserInfoPreference;
import jp.faraopro.play.util.Utils;
import jp.faraopro.play.view.CustomListItem;

public class SettingFragment extends ListBaseFragment {
    /**
     * conts
     **/
    public enum Menu {
        TIMER(R.string.page_title_setting_time_shedule, ACTION_TYPE_TRANSITION, MainActivity.TIMER, null),
        PATTERN(R.string.msg_interrupt_playing_switch, ACTION_TYPE_TRANSITION, MainActivity.PATTERN_PARENT, null),
        VOLUME(R.string.cap_volume_setting, ACTION_TYPE_TRANSITION, MainActivity.VOLUME_CONTROL, null),
        OTHER_SETTING(R.string.page_title_setting_enable_autoboot, ACTION_TYPE_TRANSITION, MainActivity.OTHER_SETTING, null),
        //CONTACT(R.string.page_title_setting_inquiry, ACTION_TYPE_BROWSER, -1, Flavor.CONTACT_US_URL),
        HELP(R.string.page_title_setting_help, ACTION_TYPE_BROWSER, -1, Flavor.FAQ_URL),
        ABOUT(R.string.menu_about, ACTION_TYPE_TRANSITION, MainActivity.ABOUT, null);

        public final int textResId;
        public final int actionType;
        public final int modeId;
        public final String url;

        Menu(int textResId, int actionType, int modeId, String url) {
            this.textResId = textResId;
            this.actionType = actionType;
            this.modeId = modeId;
            this.url = url;
        }
    }

    public static final int ACTION_TYPE_TRANSITION = 1;
    public static final int ACTION_TYPE_BROWSER = 2;

    /**
     * view
     **/
    private Button btnLogout;
    private TextView txtMail;
    private TextView txtUserType;
    private TextView txtVersion;

    /**
     * member
     **/
    private ArrayList<CustomListItem> lists;

    @Override
    protected void initViews(View view) {
        hasToolbar = false;
        super.initViews(view);

        if (!FROForm.getInstance().isTablet(getActivity())) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.new_res_user_info, null);
            v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            setOptionalView(1, v);

            MainPreference pref = MainPreference.getInstance(getActivity());
            txtMail = (TextView) v.findViewById(R.id.setting_txt_mail);
            try {
                txtMail.setText(FROForm.getInstance().getPreferenceString(MCUserInfoPreference.STRING_DATA_MAIL));
            } catch (Exception e) {
                ((BaseActivity) getActivity()).startActivity(BootActivity.class, true);
                return;
            }
            pref.term();
            txtUserType = (TextView) v.findViewById(R.id.setting_txt_user_type);
            txtUserType.setVisibility(View.GONE);
            txtVersion = (TextView) v.findViewById(R.id.setting_txt_version);
            txtVersion.setText(" : " + Utils.getVersionName(getActivity()));
            btnLogout = (Button) v.findViewById(R.id.setting_btn_logout);
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BaseActivity) getActivity()).showNegativeDialog(R.string.msg_confirm_logout,
                            MainActivity.DIALOG_TAG_LOGOUT_POS, MainActivity.DIALOG_TAG_LOGOUT_NEG, true);
                }
            });
        }
    }

    @Override
    public void getData() {
        if (lists != null)
            lists.clear();
        lists = new ArrayList<>();
        for (Menu menu : Menu.values()) {
            CustomListItem item = new CustomListItem();
            item.setmName(getString(menu.textResId));
            item.setmNameEn(getString(menu.textResId));
            item.setNext(true);
            lists.add(item);
        }
        show();
    }

    @Override
    public void show() {
        setContents(lists);
        setClickEvent(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Menu menu = Menu.values()[arg2];
                switch (menu.actionType) {
                    case ACTION_TYPE_TRANSITION:
                        ((IModeActivity) getActivity()).showMode(menu.modeId);
                        break;
                    case ACTION_TYPE_BROWSER:
                        ((BaseActivity) getActivity()).startBrowser(menu.url);
                        break;
                }
            }
        });
        refresh();
    }

    @Override
    public void onListItemClicked(int resId, int position) {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            ((BaseActivity) getActivity()).showProgress();
            FROForm.getInstance().getStatus();
        }
    }

    @Override
    public void updateViews() {
    }

    @Override
    public void onBackPress() {
        if (!FROForm.getInstance().isTablet(getActivity())) {
            ((IModeActivity) getActivity()).showMode(MainActivity.OTHER);
        } else {
            ((IModeActivity) getActivity()).showMode(MainActivity.TOP);
        }
    }
}
