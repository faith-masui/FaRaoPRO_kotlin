package jp.faraopro.play.mclient;

/**
 * Musicサーバーからの受信結果管理クラス(HttpHeader)
 * 
 * @author AIM Corporation
 * 
 */
public class MCAnalyzeHeaderInfo implements IMCResultInfo {

	private int mStatusCode;
	private String mEncMethod;
	private String mEncKey;
	private String mEncIv;
	private String mTrackId; // 特別：ダウンロードした楽曲のID
	private String mOutputPath; // 特別：ダウンロードしたコンテンツのパス

	private String mApiSig; // テスト

	/**
	 * コンストラクタ
	 */
	public MCAnalyzeHeaderInfo() {
		lclear();
	}

	@Override
	public void clear() {
		lclear();
	}

	private void lclear() {
		mStatusCode = 0;
		mEncMethod = null;
		mEncKey = null;
		mEncIv = null;
		mTrackId = null;
		mOutputPath = null;

		mApiSig = null;
	}

	@Override
	public void setString(int kind, String value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_ENCMETHOD:
			mEncMethod = value;
			break;
		case MCDefResult.MCR_KIND_ENCKEY:
			mEncKey = value;
			break;
		case MCDefResult.MCR_KIND_ENCIV:
			mEncIv = value;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_ID:
			mTrackId = value;
			break;
		case MCDefResult.MCR_KIND_DL_PATH:
			mOutputPath = value;
			break;

		case MCDefResult.MCR_KIND_API_SIG:
			mApiSig = value;
			break;
		}
	}

	@Override
	public String getString(int kind) {
		String value = null;
		switch (kind) {
		case MCDefResult.MCR_KIND_ENCMETHOD:
			value = mEncMethod;
			break;
		case MCDefResult.MCR_KIND_ENCKEY:
			value = mEncKey;
			break;
		case MCDefResult.MCR_KIND_ENCIV:
			value = mEncIv;
			break;
		case MCDefResult.MCR_KIND_TRACKITEM_ID:
			value = mTrackId;
			break;
		case MCDefResult.MCR_KIND_DL_PATH:
			value = mOutputPath;
			break;

		case MCDefResult.MCR_KIND_API_SIG:
			value = mApiSig;
			break;
		}
		return value;
	}

	@Override
	public void setList(int kind, IMCItemList list) {
	}

	@Override
	public IMCItemList getList(int kind) {
		return null;
	}

	@Override
	public void setInt(int kind, int value) {
		switch (kind) {
		case MCDefResult.MCR_KIND_STATUS_CODE:
			mStatusCode = value;
			break;
		default:
			break;
		}
	}

	@Override
	public int getInt(int kind) {
		int value = 0;
		switch (kind) {
		case MCDefResult.MCR_KIND_STATUS_CODE:
			value = mStatusCode;
			break;
		default:
			break;
		}
		return value;
	}

	@Override
	public void setItem(int kind, IMCItem item) {
		// TODO Auto-generated method stub

	}

	@Override
	public IMCItem getItem(int kind) {
		// TODO Auto-generated method stub
		return null;
	}
}
