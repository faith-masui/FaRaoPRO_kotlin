package jp.faraopro.play.act;

/**
 * Fragmentが描画されたタイミングで通知を受ける必要があるクラスが実装するIF
 * 
 * @author AIM Corporation
 * 
 */
public interface IFragmentBaseActivity {
	/**
	 * Fragmentの描画完了を通知するメソッド
	 * 
	 * @param cls
	 *            描画したFragment
	 */
	public void onCommit(Class<?> cls);
}
