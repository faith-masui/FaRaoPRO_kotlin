package jp.faraopro.play.frg.mode;

public interface IModeFragment {
	/**
	 * 呼び出すとFragment内で必要なデータをWebAPIやDBから取得する
	 */
	public void getData();

	/**
	 * 呼び出すと{@link IModeFragment#getData()}で得られたデータをFragment内で表示する
	 */
	public void show();

	/**
	 * 呼び出すと{@link IModeFragment#show()}で表示した内容を更新する
	 */
	public void updateViews();

	/**
	 * 呼び出すとバックキー押下時の動作を実行する
	 */
	public void onBackPress();

	/**
	 * エラー発生時に呼び出す
	 * 
	 * @param when
	 *            エラーケース
	 * @param code
	 *            エラーコード
	 */
	public void onError(int when, int code);
}
