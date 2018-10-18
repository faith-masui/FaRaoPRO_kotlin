package jp.faraopro.play.mclient;

/**
 * MusicClient(Web API)アクセス用パラメータインスタンスのファクトリークラス
 * 
 * @author AIM Corporation
 * 
 */
public class MCPostActionFactory {
	private static IMCPostActionParam mMCPostActionParam = null;

	/**
	 * インスタンスの取得
	 * 
	 * @return
	 */
	public synchronized static IMCPostActionParam getInstance() {
		mMCPostActionParam = new MCPostActionParam();
		return mMCPostActionParam;
	}
}
