package jp.faraopro.play.act.newone;

/**
 * 各モード画面(ジャンルやアーティストなど)を表示するアクティビティが実装するクラス<br>
 * 
 * @author AIM Corporation
 * 
 */
public interface IModeActivity {
	/**
	 * 表示画面を切り替える
	 * 
	 * @param type
	 *            表示する画面
	 */
	public void showMode(int type);

	/**
	 * 画面下部に表示しているメニューの高さを取得する
	 * 
	 * @return メニューの高さ
	 */
	public int getMenuSize();

	/**
	 * ヘッダーに表示されている文言を変更する
	 * 
	 * @param title
	 *            表示する文言
	 */
	public void setHeaderTitle(String title);

	/**
	 * シェアダイアログを表示する
	 * 
	 * @param pos
	 *            シェアの種別
	 */
	public void showShareDialog(int pos);
}
