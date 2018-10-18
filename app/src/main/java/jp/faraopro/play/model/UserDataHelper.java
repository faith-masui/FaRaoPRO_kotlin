package jp.faraopro.play.model;

/**
 * ユーザデータに関する判定などを行うヘルパークラス<br>
 * 有効期限などのデータは直接数値で扱うとコードが冗長になるため、このクラス内で期限内 or 期限外などの2値に変換する
 *
 * @author AIM
 *
 */
public class UserDataHelper {
	private static FROUserData userData;

	// このクラス自身のインスタンス化を禁止
	private UserDataHelper() {
	}

	/**
	 * 対象となるユーザデータをセットする<br>
	 * 基本的に auth/status のコールバックでこのメソッドを呼び出す
	 *
	 * @param userData
	 *            ユーザデータ
	 */
	public static void setData(FROUserData userData) {
		UserDataHelper.userData = userData;
	}

	/**
	 * 課金期限内のユーザであるか確認
	 *
	 * @return true:有効期限内, false:有効期限外
	 */
	public static boolean isPremium() {
		return (userData != null && userData.getRemainingTime() > 0);
	}

	/**
	 * HLS 機能が有効なユーザであるか確認
	 *
	 * @return true:有効, false:無効
	 */
	public static boolean hasHls() {
		return (userData != null && ServiceLevel.HLS.isContained(userData.getFeatures()));
	}

	/**
	 * 検索機能(アーティスト、年代)が有効なユーザであるか確認
	 *
	 * @return true:有効, false:無効
	 */
	public static boolean hasSearch() {
		return (userData != null && ServiceLevel.SEARCH.isContained(userData.getFeatures()));
	}

	/**
	 * 引数で与えられた権限を所有するユーザか確認
	 *
	 * @param targetPermission
	 *            確認したい権限(ビット)
	 * @return true:所有, false:未所有
	 */
	public static boolean hasPermission(int targetPermission) {
		return (userData != null && (userData.getPermissions() & targetPermission) > 0);
	}

	public static String getServiceLevel() {
		return (userData != null) ? String.valueOf(userData.getServiceLevel()) : "";
	}
}
