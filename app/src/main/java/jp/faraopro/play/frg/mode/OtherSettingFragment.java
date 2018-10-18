package jp.faraopro.play.frg.mode;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.domain.MainPreference;

public class OtherSettingFragment extends Fragment implements IModeFragment {

    public static OtherSettingFragment newInstance() {
        return new OtherSettingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_other_setting, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Switch enableAutoBoot = (Switch) view.findViewById(R.id.switch_enable_auto_boot);
        enableAutoBoot.setChecked(MainPreference.getInstance(getActivity()).isEnableAutoBoot());
        enableAutoBoot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainPreference.getInstance(getActivity()).setEnableAutoBoot(isChecked);
            }
        });
    }

    @Override
    public void getData() {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public void show() {
        // TODO 自動生成されたメソッド・スタブ

    }

    @Override
    public void updateViews() {
    }

    @Override
    public void onBackPress() {
        ((MainActivity) getActivity()).showMode(MainActivity.SETTING);
    }

    @Override
    public void onError(int when, int code) {
    }

}
