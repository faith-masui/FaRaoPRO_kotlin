package jp.faraopro.play.domain;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import jp.faraopro.play.R;
import jp.faraopro.play.common.Consts;

public class ListAdapterForDrag extends BaseAdapter {
	// private static final String[] items = { "Genres", "Special", "Good List",
	// "Artists", "Released Year", "My Channel", "Setting" };
	private ArrayList<Integer> items;

	private Context context;
	private int currentPosition = -1;

	public ListAdapterForDrag(Context context, ArrayList<Integer> items) {
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		if (items != null)
			return items.size();
		else
			return -1;
	}

	@Override
	public Object getItem(int position) {
		if (items != null)
			return items.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * リスト項目のViewを取得する
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// View作成
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.new_ele_draglist_item, null);
		}
		TextView txtTitle = (TextView) convertView.findViewById(R.id.draglist_txt_text);
		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.draglist_img_icon);
		TextView txtVisible = (TextView) convertView.findViewById(R.id.draglist_txt_visible);
		if (position < 3) {
			txtTitle.setTextColor(context.getResources().getColor(R.color.jp_farao_theme));
			txtVisible.setVisibility(View.VISIBLE);
		} else {
			txtTitle.setTextColor(context.getResources().getColor(R.color.jp_farao_text_disable));
			txtVisible.setVisibility(View.GONE);
		}

		// データ設定
		Integer item = (Integer) getItem(position);
		txtTitle.setText(Consts.getMenuString(context, item.intValue()));
		setIcon(imgIcon, item.intValue());

		// ドラッグ対象項目は、ListView側で別途描画するため、非表示にする。
		if (position == currentPosition) {
			convertView.setVisibility(View.INVISIBLE);
		} else {
			convertView.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	public ArrayList<Integer> getContents() {
		return items;
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
		case Consts.TAG_MODE_STREAMING:
			view.setImageResource(R.drawable.new_btn_menu_stream_on);
			break;
		}
	}

	/**
	 * ドラッグ開始
	 * 
	 * @param position
	 */
	public void startDrag(int position) {
		this.currentPosition = position;
	}

	/**
	 * ドラッグに従ってデータを並び替える
	 * 
	 * @param newPosition
	 */
	public void doDrag(int newPosition) {
		Integer item = items.get(currentPosition);
		if (currentPosition < newPosition) {
			// リスト項目を下に移動している場合
			for (int i = currentPosition; i < newPosition; i++) {
				// if (i != 0 && i != 4)
				items.set(i, items.get(i + 1));
			}
		} else if (currentPosition > newPosition) {
			// リスト項目を上に移動している場合
			for (int i = currentPosition; i > newPosition; i--) {
				// if (i != 0 && i != 4)
				items.set(i, items.get(i - 1));
			}
		}
		items.set(newPosition, item);

		currentPosition = newPosition;
	}

	/**
	 * ドラッグ終了
	 */
	public void stopDrag() {
		this.currentPosition = -1;
	}
}