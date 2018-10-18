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
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.HashMap;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.domain.GuideItemHolder;
import jp.faraopro.play.util.FROUtils;
import jp.faraopro.play.view.GuidesItem;

public class SpecialFragment extends Fragment implements IModeFragment {
    /**
     * const
     **/
    @SuppressWarnings("unused")
    private static final boolean DEBUG = true;
    private static final int BENIFIT_CHANNEL = 0;
    private static final int GUIDES_CHANNEL = 1;

    /** bundle key **/

    /**
     * view
     **/
    private LinearLayout lnrHeader;
    private Button btnTabGuide;
    private Button btnTabBenifit;
    private BenifitFragment frgBenifit;

    /**
     * member
     **/
    private int type = GUIDES_CHANNEL;
    private HashMap<Integer, GuidesFragment> fragmentMap; // 番組チャンネル格納用マップ

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // マップクラスの初期化
        fragmentMap = new HashMap<>();
        View view = inflater.inflate(R.layout.layout_simple_2block, container, false);
        initViews(view);

        return view;
    }

    protected void initViews(View view) {
        // タブを追加する
        lnrHeader = (LinearLayout) view.findViewById(R.id.simple2_lnr_header);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.new_res_2tab, null);
        v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        lnrHeader.addView(v);
        // 番組チャンネルタブ
        btnTabGuide = (Button) v.findViewById(R.id.tab_btn_left);
        btnTabGuide.setText(R.string.btn_recommended_bgm);
        btnTabGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTabGuide.setSelected(true);
                btnTabBenifit.setSelected(false);
                changeFragment(GUIDES_CHANNEL, false);
            }
        });
        // マイ特集チャンネルタブ
        btnTabBenifit = (Button) v.findViewById(R.id.tab_btn_right);
        btnTabBenifit.setText(R.string.btn_play_benefit_pro);
        btnTabBenifit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTabGuide.setSelected(false);
                btnTabBenifit.setSelected(true);
                changeFragment(BENIFIT_CHANNEL, false);
                if (FROForm.getInstance().benifitList == null) {
                    ((BaseActivity) getActivity()).showProgress();
                    type = BENIFIT_CHANNEL;
                    FROForm.getInstance().getHot100List();
                }
            }
        });
    }

    private void changeFragment(int type, boolean isBackStack) {
        // 現在表示中のタブと一致していた場合は何もしない
        if (type == this.type)
            return;

        this.type = type;
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (type == GUIDES_CHANNEL) {
            Fragment fragment;
            if (fragmentMap != null && fragmentMap.size() > 0) {
                fragment = fragmentMap.get(new Integer(fragmentMap.size() - 1));
            } else {
                GuidesFragment frgGuides = new GuidesFragment();
                Bundle args = new Bundle();
                args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_NORMAL);
                frgGuides.setArguments(args);

                // 作成したフラグメントをHashMapに追加して管理する
                fragmentMap.put(fragmentMap.size(), frgGuides);
                fragment = frgGuides;
            }
            transaction.replace(R.id.simple2_lnr_body, fragment);

            GuidesItem selected = GuideItemHolder.getInstance().getSelected();
            if (selected != null) {
                GuidesItem parent = selected.getParentItem();
                String title;
                String parentName = null;
                // タイトルの名称取得
                if (FROUtils.isPrimaryLanguage()) {
                    title = selected.getmName();
                } else {
                    title = selected.getmNameEn();
                }
                if (TextUtils.isEmpty(title))
                    title = getString(R.string.page_title_special_pro);
                // 左ボタンの名称取得
                if (parent != null) {
                    if (!TextUtils.isEmpty(parent.getmName()))
                        parentName = "< " + getString(R.string.btn_back);
                    else
                        parentName = "< " + getString(R.string.page_title_special_pro);
                }
                if (isAdded() && isVisible()) {
                    ((MainActivity) getActivity()).setHeaderTitle(title);
                    ((MainActivity) getActivity()).setHeaderLeftButton(parentName);
                }
            }
        } else {
            if (frgBenifit == null) {
                frgBenifit = new BenifitFragment();
                Bundle args = new Bundle();
                args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_IMAGE);
                frgBenifit.setArguments(args);
            }
            if (isAdded()) {
                ((MainActivity) getActivity()).setHeaderLeftButton(null);
            }
            transaction.replace(R.id.simple2_lnr_body, frgBenifit);

            if (isAdded()) {
                ((MainActivity) getActivity()).setHeaderTitle(getString(R.string.page_title_special_pro));
            }

        }
        if (isBackStack)
            transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public void updateHeaderIfNeeded() {
        if (type == GUIDES_CHANNEL) {
            updateHeader();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            // 2回目以降の表示の場合、先に表示していたデータをすべて初期化する
            GuideItemHolder.getInstance().clear();
            if (fragmentMap != null) {
                // 親のFragmentにattachされているすべてのFragmentを削除
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                while (fragmentMap.size() > 0) {
                    Integer key = new Integer(fragmentMap.size() - 1);
                    GuidesFragment frg = fragmentMap.get(key);
                    transaction.remove(frg);
                    fragmentMap.remove(key);
                }
                transaction.commitAllowingStateLoss();
                getChildFragmentManager().executePendingTransactions();
                // HashMapの初期化
                fragmentMap.clear();
                fragmentMap = new HashMap<>();
            }
            if (frgBenifit != null) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.remove(frgBenifit);
                transaction.commitAllowingStateLoss();
                getChildFragmentManager().executePendingTransactions();
                if (FROForm.getInstance().benifitList != null) {
                    FROForm.getInstance().benifitList.clear();
                    FROForm.getInstance().benifitList = null;
                }
                frgBenifit = null;
            }

            // データを全て解放し終えたら番組チャンネルリストを取得する
            ((BaseActivity) getActivity()).showProgress();
            btnTabGuide.setSelected(true);
            btnTabBenifit.setSelected(false);
            type = GUIDES_CHANNEL;
            FROForm.getInstance().getGuidesList(0);
        }
    }

    @Override
    public void show() {
        // if (FROForm.getInstance().benifitList == null) {
        // ((BaseActivity) getActivity()).showProgress();
        // FROForm.getInstance().getHot100List();
        // return;
        // }

        ((BaseActivity) getActivity()).dismissProgress();
        Fragment frg;
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        // 番組チャンネルを表示
        if (type == GUIDES_CHANNEL) {
            // 番組チャンネル1ページ分を表示するFragmentを作成
            GuidesFragment frgGuides = new GuidesFragment();
            Bundle args = new Bundle();
            args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_NORMAL);
            frgGuides.setArguments(args);
            // 作成したフラグメントをHashMapに追加して管理する
            fragmentMap.put(fragmentMap.size(), frgGuides);
            frg = frgGuides;

            updateHeader();
            // GuidesItem parent =
            // FROForm.getInstance().selected.getParentItem();
            // String title;
            // String parentName = null;
            // // タイトルの名称取得
            // if (Utils.isJapanese()) {
            // title = FROForm.getInstance().selected.getmName();
            // } else {
            // title = FROForm.getInstance().selected.getmNameEn();
            // }
            // if (TextUtils.isEmpty(title))
            // title = getString(R.string.page_title_special_pro);
            // // 左ボタンの名称取得
            // if (parent != null) {
            // if (!TextUtils.isEmpty(parent.getmName()))
            // parentName = "< " + getString(R.string.btn_back);
            // else
            // parentName = "< " + getString(R.string.page_title_special_pro);
            // }
            // if (isAdded() && isVisible()) {
            // ((MainActivity) getActivity()).setHeaderTitle(title);
            // ((MainActivity) getActivity()).setHeaderLeftButton(parentName);
            // }
        }
        // マイ特集を表示
        else {
            if (frgBenifit == null) {
                frgBenifit = new BenifitFragment();
                Bundle args = new Bundle();
                args.putInt(ListBaseFragment.LIST_TYPE, ListBaseFragment.LIST_TYPE_IMAGE);
                frgBenifit.setArguments(args);
            } else {
                frgBenifit.show();
            }
            frg = frgBenifit;

            if (isAdded()) {
                ((MainActivity) getActivity()).setHeaderLeftButton(null);
            }
            if (isAdded()) {
                ((MainActivity) getActivity()).setHeaderTitle(getString(R.string.page_title_special_pro));
            }
        }
        transaction.replace(R.id.simple2_lnr_body, frg);
        transaction.commitAllowingStateLoss();
    }

    private void updateHeader() {
        GuidesItem selected = GuideItemHolder.getInstance().getSelected();
        if (selected == null)
            return;

        GuidesItem parent = selected.getParentItem();
        String title;
        String parentName = null;
        // タイトルの名称取得
        if (FROUtils.isPrimaryLanguage()) {
            title = selected.getmName();
        } else {
            title = selected.getmNameEn();
        }
        if (TextUtils.isEmpty(title))
            title = getString(R.string.page_title_special_pro);
        // 左ボタンの名称取得
        if (parent != null) {
            if (FROUtils.isPrimaryLanguage()) {
                parentName = parent.getmName();
            } else {
                parentName = parent.getmNameEn();
            }
            if (!TextUtils.isEmpty(parentName))
                parentName = "< " + parentName;
            else
                parentName = "< " + getString(R.string.page_title_special_pro);
        }
        if (isAdded() && isVisible()) {
            ((MainActivity) getActivity()).setHeaderTitle(title);
            ((MainActivity) getActivity()).setHeaderLeftButton(parentName);
        }
    }

    @Override
    public void updateViews() {
    }

    @Override
    public void onBackPress() {
        // マイ特集画面で戻るボタン押下
        if (type == BENIFIT_CHANNEL) {
            // 特集チャンネルがメニューバーに表示されている場合
            if (FROForm.getInstance().isMenu(Consts.TAG_MODE_SPECIAL, getActivity())) {
                // トップに戻る
                ((MainActivity) getActivity()).showMode(MainActivity.TOP);
            }
            // 特集チャンネルがメニューバーに表示されていない場合
            else {
                // その他に戻る
                ((MainActivity) getActivity()).showMode(MainActivity.OTHER);
            }
        }
        // 番組チャンネルで戻るボタン押下
        else {
            // HashMapから一番新しく追加されたFragment(表示されているFragment)を削除
            fragmentMap.remove(new Integer(fragmentMap.size() - 1));
            GuideItemHolder.getInstance().removeChild();
            // 表示するフラグメントがまだ残っている場合
            if (fragmentMap.size() > 0) {
                updateHeader();
                // HashMapからFragmentを取得し
                GuidesFragment frg = fragmentMap.get(new Integer(fragmentMap.size() - 1));
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                // 表示する
                transaction.replace(R.id.simple2_lnr_body, frg);
                transaction.commitAllowingStateLoss();
            }
            // 表示するフラグメントがなくなった場合
            else {
                // 特集チャンネルがメニューバーに表示されている場合
                if (FROForm.getInstance().isMenu(Consts.TAG_MODE_SPECIAL, getActivity())) {
                    // トップに戻る
                    ((MainActivity) getActivity()).showMode(MainActivity.TOP);
                }
                // 特集チャンネルがメニューバーに表示されていない場合
                else {
                    // その他に戻る
                    ((MainActivity) getActivity()).showMode(MainActivity.OTHER);
                }
            }
        }
    }

    @Override
    public void getData() {
    }

    @Override
    public void onError(int when, int code) {
        // TODO Auto-generated method stub

    }
}
