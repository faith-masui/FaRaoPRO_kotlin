package jp.faraopro.play.domain;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.ArrayList;
import java.util.Date;

import jp.faraopro.play.R;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.model.UserDataHelper;
import jp.faraopro.play.util.FROUtils;
import jp.faraopro.play.util.Utils;
import jp.faraopro.play.view.CircleView;
import jp.faraopro.play.view.CustomListItem;

/**
 * ListView用アダプター
 *
 * @author AIM
 */
public class ListAdapterForBase extends ArrayAdapter<CustomListItem> {
    private ArrayList<CustomListItem> mItems;
    private LayoutInflater inflater;
    private int resItemId;
    private IListClickListener clickListener;
    private IImageLoadListener loadListener;
    private Context context;

    public interface IListClickListener {
        public void onListItemClicked(int resId, int position);
    }

    public interface IImageLoadListener {
        // public void loadImage(String id, int position);
        public void loadImage(CustomListItem item, int position);
    }

    /**
     * コンストラクタ
     *
     * @param context
     * @param resId   : リスト要素のレイアウト
     * @param items   : 表示する内容
     */
    public ListAdapterForBase(Context context, int resId, ArrayList<CustomListItem> items) {
        super(context, resId, items);
        this.mItems = items;
        this.resItemId = resId;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    public ListAdapterForBase(Context context, int resId, ArrayList<CustomListItem> items,
                              IListClickListener clickListener, IImageLoadListener loadListener) {
        this(context, resId, items);
        if (clickListener instanceof IListClickListener)
            this.clickListener = clickListener;
        if (loadListener instanceof IImageLoadListener)
            this.loadListener = loadListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int fPos = position;
        View v = convertView;
        int colorIndex;

        /** new **/
        LinearLayout lnrElement;
        final TextView text;
        TextView description;
        CircleView circle;
        TextView subText;
        ImageView icon;
        ImageView info;
        // ImageView rating;
        ImageView play;
        ImageView next;
        /** new **/

        if (v == null) {
            v = inflater.inflate(resItemId, null);
        }
        CustomListItem item = mItems.get(position);

        switch (resItemId) {

            // 基本レイアウト
            case R.layout.new_ele_simplelist_item:
                // タイトルの設定
                text = (TextView) v.findViewById(R.id.simplelist_txt_text);
                if (FROUtils.isPrimaryLanguage()) {
                    text.setText(item.getmName());
                } else {
                    text.setText(item.getmNameEn());
                }
                circle = (CircleView) v.findViewById(R.id.simplelist_circle);
                colorIndex = position;
                while (colorIndex > 7) {
                    colorIndex -= 8;
                }
                circle.setColor(Consts.COLOR_PALETTE_CIRCLE[colorIndex]);

                if (colorIndex == 0) {
                    v.setBackgroundResource(R.drawable.color_palette1);
                }
                if (colorIndex == 1) {
                    v.setBackgroundResource(R.drawable.color_palette2);
                }
                if (colorIndex == 2) {
                    v.setBackgroundResource(R.drawable.color_palette3);
                }
                if (colorIndex == 3) {
                    v.setBackgroundResource(R.drawable.color_palette4);
                }
                if (colorIndex == 4) {
                    v.setBackgroundResource(R.drawable.color_palette5);
                }
                if (colorIndex == 5) {
                    v.setBackgroundResource(R.drawable.color_palette6);
                }
                if (colorIndex == 6) {
                    v.setBackgroundResource(R.drawable.color_palette7);
                }
                if (colorIndex == 7) {
                    v.setBackgroundResource(R.drawable.color_palette8);
                }

                play = (ImageView) v.findViewById(R.id.simplelist_img_play);
                next = (ImageView) v.findViewById(R.id.simplelist_img_next);
                ImageView plus = (ImageView) v.findViewById(R.id.simplelist_img_import);
                // 子要素を持っている場合、ネクストアイコンを表示
                if (item.isNext()) {
                    next.setVisibility(View.VISIBLE);
                    play.setVisibility(View.GONE);
                    plus.setVisibility(View.GONE);
                }
                // テンプレートタイプが設定されている場合、インポートアイコンを表示
                else if (item.getTemplateType() > 0) {
                    next.setVisibility(View.GONE);
                    play.setVisibility(View.GONE);
                    plus.setVisibility(View.VISIBLE);
                }
                // そうでなければ再生アイコンを表示
                else {
                    next.setVisibility(View.GONE);
                    play.setVisibility(View.VISIBLE);
                    plus.setVisibility(View.GONE);
                }
                // ダミーアイテムの場合、すべてのアイコンを非表示
                if (item.isDummy()) {
                    next.setVisibility(View.GONE);
                    play.setVisibility(View.GONE);
                    plus.setVisibility(View.GONE);
                }
                if (item.getPermission() == 0 || UserDataHelper.hasPermission(item.getPermission())) {
                    v.setEnabled(true);
                    text.setTextColor(getContext().getResources().getColor(R.color.jp_farao_text_default));
                } else {
                    v.setEnabled(false);
                    text.setTextColor(getContext().getResources().getColor(R.color.jp_farao_text_disable));
                }
                break;

            case R.layout.new_ele_timerlist_item:
                // タイトルの設定
                text = (TextView) v.findViewById(R.id.timerlist_txt_text);
                if (FROUtils.isPrimaryLanguage()) {
                    text.setText(item.getmName());
                } else {
                    text.setText(item.getmNameEn());
                }
                ImageView onTimer = (ImageView) v.findViewById(R.id.timerlist_img_ontimer);
                if (item.getNodeType() == Consts.MUSIC_TYPE_STOP) {
                    onTimer.setVisibility(View.INVISIBLE);
                } else {
                    onTimer.setVisibility(View.VISIBLE);
                }
                TextView title = (TextView) v.findViewById(R.id.timerlist_txt_name);
                title.setText(item.getText());
                byte week = Byte.parseByte(item.getmDescription());
                int hour = item.getParent() / 60;
                int minute = item.getParent() % 60;
                String hourStr = Integer.toString(hour);
                if (hour < 10)
                    hourStr = "0" + hourStr;
                String minuteStr = Integer.toString(minute);
                if (minute < 10)
                    minuteStr = "0" + minuteStr;
                String tmpTime = hourStr + " : " + minuteStr;
                String tmpWeek = "";
                for (int i = 0; i < Consts.WEEK_BYTE_ARRAY.length; i++) {
                    if ((week & Consts.WEEK_BYTE_ARRAY[i]) == Consts.WEEK_BYTE_ARRAY[i]) {
                        tmpWeek += Consts.getWeekStringArray()[i] + " ";
                    }
                }
                TextView timerDate = (TextView) v.findViewById(R.id.timerlist_txt_time);
                timerDate.setText(tmpTime);
                TextView timerWeek = (TextView) v.findViewById(R.id.timerlist_txt_week);
                timerWeek.setText(tmpWeek);

                LinearLayout lnrName = (LinearLayout) v.findViewById(R.id.timerlist_lnr_element);
                lnrName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickListener != null)
                            clickListener.onListItemClicked(R.id.timerlist_lnr_element, fPos);
                    }
                });

                break;

            case R.layout.new_ele_folderlist_item:
                // タイトルの設定
                text = (TextView) v.findViewById(R.id.folderlist_txt_text);
                if (FROUtils.isPrimaryLanguage()) {
                    text.setText(item.getmName());
                } else {
                    text.setText(item.getmNameEn());
                }
                next = (ImageView) v.findViewById(R.id.folderlist_img_next);
                if (item.isNext()) {
                    next.setVisibility(View.VISIBLE);
                } else {
                    next.setVisibility(View.GONE);
                }
                icon = (ImageView) v.findViewById(R.id.folderlist_img_icon);
                if (item.isNext()) {
                    icon.setImageResource(android.R.drawable.ic_menu_add);
                } else {
                    icon.setImageResource(android.R.drawable.ic_media_play);
                }
                break;

            // ホットチャート、おすすめアーティスト用レイアウト
            case R.layout.new_ele_imagelist_item:
                // タイトルの設定
                text = (TextView) v.findViewById(R.id.imagelist_txt_text);
                if (FROUtils.isPrimaryLanguage()) {
                    text.setText(item.getmName());
                } else {
                    text.setText(item.getmNameEn());
                }

                // 説明文の設定
                description = (TextView) v.findViewById(R.id.imagelist_txt_description);
                if (TextUtils.isEmpty(item.getmDescription())) {
                    description.setVisibility(View.GONE);
                } else {
                    description.setVisibility(View.VISIBLE);
                    description.setText(item.getmDescription());
                }

                // 有効期限
                TextView limit = (TextView) v.findViewById(R.id.imagelist_txt_limit);
                String deliveryEnd = item.getmDeliveryEnd();
                if (!TextUtils.isEmpty(deliveryEnd)) {
                    limit.setVisibility(View.VISIBLE);
                    long tmp = Long.parseLong(item.getmDeliveryEnd());

                    String date = DateFormatUtils.format(new Date(tmp), "yyyy/MM/dd");
                    limit.setText(context.getString(R.string.msg_benefit_due) + " : " + date);

                    // String date = DateUtils.formatDateTime(context, tmp,
                    // DateUtils.FORMAT_NUMERIC_DATE);
                    // String time = DateUtils.formatDateTime(context, tmp,
                    // DateUtils.FORMAT_SHOW_TIME);
                    // limit.setText(context.getString(R.string.cap_expiration_date)
                    // + date + " " + time);
                } else {
                    limit.setVisibility(View.GONE);
                }

                // サムネイルの設定
                icon = (ImageView) v.findViewById(R.id.imagelist_img_thumb);
                if (FROImageCache.getImage(item.getmThumbIcon()) != null) {
                    icon.setImageBitmap(FROImageCache.getImage(item.getmThumbIcon()));
                } else if (item.getmIcon() != null) {
                    Bitmap bitmap = Utils.loadBitmap(item.getmIcon(), 1, context);
                    FROImageCache.setImage(item.getmThumbIcon(), bitmap);
                    icon.setImageBitmap(bitmap);
                } else {
                    if (!TextUtils.isEmpty(item.getmThumbIcon()) && loadListener != null) {
                        icon.setImageBitmap(null);
                        loadListener.loadImage(item, position);
                    } else {
                        icon.setImageResource(R.drawable.nothumb);
                    }
                }
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickListener != null)
                            clickListener.onListItemClicked(R.id.imagelist_img_thumb, fPos);
                    }
                });

                // infoマークの設定
                info = (ImageView) v.findViewById(R.id.imagelist_img_info);
                // 楽曲情報が存際する場合
                if (!TextUtils.isEmpty(item.getmContentText())) {
                    info.setVisibility(View.VISIBLE);
                }
                // 楽曲情報が存際しない場合
                else {
                    info.setVisibility(View.GONE);
                }
                break;

            // 履歴、グッドリスト用レイアウト
            case R.layout.new_ele_historylist_item:
                // タイトルの設定
                text = (TextView) v.findViewById(R.id.historylist_text_title);
                //text.setText(item.getText() + " / " + item.getmThumbIcon());
                text.setText(item.getText());
                // アルバム名の設定
                subText = (TextView) v.findViewById(R.id.historylist_text_artist_name);
                //subText.setText(item.getmName());
                subText.setText(item.getmName() + "  —  " + item.getmThumbIcon());
                // チャンネルの設定
                TextView channelName = (TextView) v.findViewById(R.id.historylist_text_channel_name);
                channelName.setText(item.getStationName());

                // サムネイルの設定
                icon = (ImageView) v.findViewById(R.id.historylist_img_thumb);
                String path = Consts.PRIVATE_PATH_THUMB_HISTORY + item.getId();
                Bitmap bitmap;
                if ((bitmap = FROHistoryImageCache.getImage(path)) != null) {
                    icon.setImageBitmap(bitmap);
                } else {
                    bitmap = Utils.loadBitmap(path, 4, context);
                    if (bitmap != null) {
                        icon.setImageBitmap(bitmap);
                        FROHistoryImageCache.setImage(path, bitmap);
                    } else {
                        icon.setImageResource(R.drawable.nothumb);
                    }
                }

                lnrElement = (LinearLayout) v.findViewById(R.id.historylist_lnr_parent);
                // サムネイル押下時の動作
                lnrElement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickListener != null)
                            clickListener.onListItemClicked(R.id.historylist_lnr_parent, fPos);
                    }
                });
                break;

            // その他用レイアウト
            case R.layout.new_ele_otherlist_item:
                // タイトルの設定
                text = (TextView) v.findViewById(R.id.otherlist_txt_text);
                text.setText(item.getText());
                icon = (ImageView) v.findViewById(R.id.otherlist_img_icon);
                setIcon(icon, item.getId());
                next = (ImageView) v.findViewById(R.id.otherlist_img_next);
                next.setVisibility(View.VISIBLE);
                text.setTextColor(item.isUpdate() ? 0xff4192D9 : Color.BLACK);
                break;
            case R.layout.new_ele_setting_item:
                // タイトルの設定
                text = (TextView) v.findViewById(R.id.settinglist_txt_text);
                text.setText(item.getmName());
                text.setTextColor(item.isUpdate() ? 0xff4192D9 : Color.BLACK);
                next = (ImageView) v.findViewById(R.id.settinglist_img_next);
                Switch enableInterrupt = (Switch) v.findViewById(R.id.settinglist_switch_enable_interrupt);
                next.setVisibility(View.VISIBLE);
                enableInterrupt.setVisibility(View.GONE);
                break;
            case R.layout.new_ele_checklist_item:
                text = (TextView) v.findViewById(R.id.checklist_txt_name);
                text.setText(item.getmName());
                CheckBox selected = (CheckBox) v.findViewById(R.id.checklist_chk_select);
                selected.setChecked(item.isNext());
                break;
            case R.layout.new_ele_deletelist_timer_item:
                text = (TextView) v.findViewById(R.id.deletelist_timer_txt_text);
                text.setText(item.getmName());
                // LinearLayout lnrName = (LinearLayout)
                // v.findViewById(R.id.deletelist_timer_lnr_name);
                // lnrName.setOnClickListener(new View.OnClickListener() {
                // @Override
                // public void onClick(View v) {
                // if (clickListener != null)
                // clickListener.onListItemClicked(R.id.deletelist_timer_lnr_name,
                // fPos);
                // }
                // });
                ImageButton deleteTimer = (ImageButton) v.findViewById(R.id.deletelist_timer_ibtn_delete);
                deleteTimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickListener != null)
                            clickListener.onListItemClicked(R.id.deletelist_timer_ibtn_delete, fPos);
                    }
                });
                TextView title2 = (TextView) v.findViewById(R.id.deletelist_timer_txt_name);
                title2.setText(item.getText());
                byte week2 = Byte.parseByte(item.getmDescription());
                int hour2 = item.getParent() / 60;
                int minute2 = item.getParent() % 60;
                String hourStr2 = Integer.toString(hour2);
                if (hour2 < 10)
                    hourStr2 = "0" + hourStr2;
                String minuteStr2 = Integer.toString(minute2);
                if (minute2 < 10)
                    minuteStr2 = "0" + minuteStr2;
                String tmpTime2 = hourStr2 + " : " + minuteStr2;
                String tmpWeek2 = "";
                for (int i = 0; i < Consts.WEEK_BYTE_ARRAY.length; i++) {
                    if ((week2 & Consts.WEEK_BYTE_ARRAY[i]) == Consts.WEEK_BYTE_ARRAY[i]) {
                        tmpWeek2 += Consts.getWeekStringArray()[i] + " ";
                    }
                }
                TextView timerDate2 = (TextView) v.findViewById(R.id.deletelist_timer_txt_time);
                timerDate2.setText(tmpTime2);
                TextView timerWeek2 = (TextView) v.findViewById(R.id.deletelist_timer_txt_week);
                timerWeek2.setText(tmpWeek2);
                break;

            case R.layout.new_ele_deletelist_item:
                text = (TextView) v.findViewById(R.id.deletelist_txt_text);
                text.setText(item.getmName());
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickListener != null)
                            clickListener.onListItemClicked(R.id.deletelist_txt_text, fPos);
                    }
                });
                ImageButton delete = (ImageButton) v.findViewById(R.id.deletelist_ibtn_delete);
                if (item.getId() != -1) {
                    delete.setVisibility(View.VISIBLE);
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (clickListener != null)
                                clickListener.onListItemClicked(R.id.deletelist_ibtn_delete, fPos);
                        }
                    });
                } else {
                    delete.setVisibility(View.INVISIBLE);
                }
                break;
            case R.layout.layout_pattern_list_element:
                LinearLayout layoutSection = (LinearLayout) v.findViewById(R.id.pattern_list_element_section);
                TextView layoutSectionName = (TextView) v.findViewById(R.id.pattern_list_element_section_name);
                LinearLayout layoutDataSingle = (LinearLayout) v.findViewById(R.id.pattern_list_element_data1);
                TextView layoutDataSingleKey = (TextView) v.findViewById(R.id.pattern_list_element_data1_key);
                TextView layoutDataSingleValue = (TextView) v.findViewById(R.id.pattern_list_element_data1_value);
                LinearLayout layoutDataDouble = (LinearLayout) v.findViewById(R.id.pattern_list_element_data2);
                TextView layoutDataDoubleLeftKey = (TextView) v.findViewById(R.id.pattern_list_element_data2_left_key);
                TextView layoutDataDoubleLeftValue = (TextView) v.findViewById(R.id.pattern_list_element_data2_left_value);
                TextView layoutDataDoubleRightKey = (TextView) v.findViewById(R.id.pattern_list_element_data2_right_key);
                TextView layoutDataDoubleRightValue = (TextView) v
                        .findViewById(R.id.pattern_list_element_data2_right_value);
                LinearLayout layoutData2Line = (LinearLayout) v.findViewById(R.id.pattern_list_element_data3);
                TextView layoutData2LineTop = (TextView) v.findViewById(R.id.pattern_list_element_data3_value1);
                TextView layoutData2LineBottom = (TextView) v.findViewById(R.id.pattern_list_element_data3_value2);
                LinearLayout layoutDataSinglePlane = (LinearLayout) v.findViewById(R.id.pattern_list_element_data4);
                TextView layoutDataSinglePlaneValue = (TextView) v.findViewById(R.id.pattern_list_element_data4_value);
                int color;
                switch (item.getLock()) {
                    case 0:
                        layoutSection.setVisibility(View.VISIBLE);
                        layoutDataSingle.setVisibility(View.GONE);
                        layoutDataDouble.setVisibility(View.GONE);
                        layoutData2Line.setVisibility(View.GONE);
                        layoutDataSinglePlane.setVisibility(View.GONE);
                        layoutSectionName.setText(item.getText());
                        break;
                    case 1:
                        layoutSection.setVisibility(View.GONE);
                        layoutDataSingle.setVisibility(View.VISIBLE);
                        layoutDataDouble.setVisibility(View.GONE);
                        layoutData2Line.setVisibility(View.GONE);
                        layoutDataSinglePlane.setVisibility(View.GONE);
                        color = (item.isNext()) ? context.getResources().getColor(R.color.jp_farao_red)
                                : context.getResources().getColor(R.color.jp_farao_text_default);
                        layoutDataSingleKey.setText(item.getmContentTitle());
                        layoutDataSingleValue.setText(item.getmContentTitleEn());
                        layoutDataSingleValue.setTextColor(color);
                        break;
                    case 2:
                        layoutSection.setVisibility(View.GONE);
                        layoutDataSingle.setVisibility(View.GONE);
                        layoutDataDouble.setVisibility(View.VISIBLE);
                        layoutData2Line.setVisibility(View.GONE);
                        layoutDataSinglePlane.setVisibility(View.GONE);
                        layoutDataDoubleLeftKey.setText(item.getmContentTitle());
                        layoutDataDoubleLeftValue.setText(item.getmContentTitleEn());
                        layoutDataDoubleRightKey.setText(item.getmContentText());
                        layoutDataDoubleRightValue.setText(item.getmContentTextEn());
                        color = (item.isNext()) ? context.getResources().getColor(R.color.jp_farao_red)
                                : context.getResources().getColor(R.color.jp_farao_text_default);
                        layoutDataDoubleLeftKey.setTextColor(color);
                        layoutDataDoubleLeftValue.setTextColor(color);
                        layoutDataDoubleRightKey.setTextColor(color);
                        layoutDataDoubleRightValue.setTextColor(color);
                        break;
                    case 3:
                        layoutSection.setVisibility(View.GONE);
                        layoutDataSingle.setVisibility(View.GONE);
                        layoutDataDouble.setVisibility(View.GONE);
                        layoutData2Line.setVisibility(View.VISIBLE);
                        layoutDataSinglePlane.setVisibility(View.GONE);
                        layoutData2LineTop.setText(item.getmContentTitle());
                        layoutData2LineBottom.setText(item.getmContentTitleEn());
                        break;
                    case 4:
                        layoutSection.setVisibility(View.GONE);
                        layoutDataSingle.setVisibility(View.GONE);
                        layoutDataDouble.setVisibility(View.GONE);
                        layoutData2Line.setVisibility(View.GONE);
                        layoutDataSinglePlane.setVisibility(View.VISIBLE);
                        layoutDataSinglePlaneValue.setText(item.getText());
                        break;
                }
                break;
        }

        // // XMLで定義したアニメーションを読み込む
        // Animation anim = AnimationUtils.loadAnimation(getContext(),
        // R.anim.slide_left_to_right);
        // // リストアイテムのアニメーションを開始
        // v.startAnimation(anim);

        return v;
    }

    private void setIcon(ImageView view, int tag) {
        switch (tag) {
            case Consts.TAG_MODE_ARTIST:
                view.setImageResource(R.drawable.new_btn_menu_artist_on);
                break;
            case Consts.TAG_MODE_GENRE:
                view.setImageResource(R.drawable.new_btn_menu_genres_on);
                break;
            case Consts.TAG_MODE_HISTORY:
                view.setImageResource(R.drawable.new_btn_menu_goodlist_on);
                break;
            case Consts.TAG_MODE_MYCHANNEL:
                view.setImageResource(R.drawable.new_btn_menu_mychannels_on);
                break;
            case Consts.TAG_MODE_RELEASE:
                view.setImageResource(R.drawable.new_btn_menu_release_on);
                break;
            case Consts.TAG_MODE_SPECIAL:
                view.setImageResource(R.drawable.new_btn_menu_special_on);
                break;
            case Consts.TAG_MODE_SETTING:
                view.setImageResource(R.drawable.new_btn_menu_setting);
                break;
            case Consts.TAG_MODE_STREAMING:
                view.setImageResource(R.drawable.new_btn_menu_stream_on);
                break;
        }
    }
}
