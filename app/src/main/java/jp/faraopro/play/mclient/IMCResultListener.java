package jp.faraopro.play.mclient;

/**
 * MusicClient(Musicサーバーアクセス)専用リザルトリスナー
 * 
 * @author AIM Corporation
 * 
 */
public interface IMCResultListener {
	/**
	 * Musicサーバーアクセス時の結果通知イベント
	 * 
	 * @param when
	 *            （MCDefAction参照）
	 * @param result
	 */
	public void onNotifyMCResult(int when, IMCResultInfo result);

	/**
	 * Musicサーバーアクセス時のエラー通知イベント
	 * 
	 * @param when
	 *            （MCDefAction参照）
	 * @param errorCode
	 *            （MCError参照）
	 */
	public void onNotifyMCError(int when, int errorCode);
}
