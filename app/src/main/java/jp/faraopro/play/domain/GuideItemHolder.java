package jp.faraopro.play.domain;

import java.util.ArrayList;
import java.util.List;

import jp.faraopro.play.mclient.IMCItem;
import jp.faraopro.play.mclient.MCBusinessItem;
import jp.faraopro.play.view.GuidesItem;

/**
 * 番組チャンネルの内容を保持するクラス
 *
 * @author AIM
 *
 */
public class GuideItemHolder {
	private static GuideItemHolder sInstance;
	private GuidesItem mSelectedItem;
	private GuidesItem mTempSelectedItem;

	private GuideItemHolder() {
	}

	/**
	 * このクラスのインスタンスを返す<br>
	 * シングルトンであるため、どのクラスから呼び出しても同じインスタンスが返却される<br>
	 * スレッドセーフではないため、排他処理は呼び出し元で行うこと
	 *
	 * @return このクラスのインスタンス
	 */
	public synchronized static GuideItemHolder getInstance() {
		if (sInstance == null)
			sInstance = new GuideItemHolder();

		return sInstance;
	}

	/**
	 *
	 * @return 現在選択中のチャンネル、または null
	 */
	public GuidesItem getSelected() {
		return mSelectedItem;
	}

	/**
	 * {@link GuideItemHolder#changeSelected(int)} で選択された子要素を現在選択中のチャンネルとし、
	 * 引数で与えられた子要素のリストをこれに追加する<br>
	 * 選択中のチャンネルがない場合はルートチャンネルを作成する(id = 0)
	 *
	 * @param guides
	 */
	public void addChild(List<IMCItem> guides) {
		if (mSelectedItem == null) {
			GuidesItem guidesItem = new GuidesItem();
			guidesItem.setId(0);
			guidesItem.setNext(true);
			mSelectedItem = guidesItem;
		} else if (mTempSelectedItem != null) {
			mTempSelectedItem.setParentItem(mSelectedItem);
			mSelectedItem = mTempSelectedItem;
			mTempSelectedItem = null;
		}
		ArrayList<GuidesItem> tmpGuides = new ArrayList<GuidesItem>();
		if (guides != null) {
			for (IMCItem mcItem : guides) {
				tmpGuides.add(new GuidesItem((MCBusinessItem) mcItem));
			}
		}
		mSelectedItem.setChildren(tmpGuides);
	}

	/**
	 * 現在選択中のチャンネルの子要素を全て破棄し、現在選択中のチャンネルを1つ上の親要素に変更する
	 */
	public void removeChild() {
		if (mSelectedItem == null)
			return;

		mSelectedItem.removeChildren();
		mSelectedItem = mSelectedItem.getParentItem();
	}

	/**
	 * 現在選択中のチャンネルの引数番の子要素を現在選択中のチャンネルに予定する<br>
	 * この関数のみで処理は完結せず、{@link GuideItemHolder#addChild(List)} が呼びだされて反映がされる
	 *
	 * @param index
	 *            次に選択中のチャンネルになる予定の子要素の番号
	 * @return 次に選択中のチャンネルになる予定の子要素
	 */
	public GuidesItem changeSelected(int index) {
		mTempSelectedItem = mSelectedItem.getChild(index);
		return mTempSelectedItem;
	}

	/**
	 * インスタンス内のすべてのメンバを破棄する
	 */
	public void clear() {
		if (mSelectedItem != null) {
			while (!mSelectedItem.isRoot()) {
				removeChild();
			}
			mSelectedItem = null;
		}
		mTempSelectedItem = null;
		sInstance = null;
	}
}
