package jp.faraopro.play.app;

import jp.faraopro.play.common.Consts;
import jp.faraopro.play.mclient.IMCPostActionParam;
import jp.faraopro.play.mclient.MCDefAction;

/**
 * 1つのWebAPI呼び出し処理を格納するクラス
 * 
 * @author AIM Corporation
 * 
 */
public class ApiTask {
	private double key;
	private int action;
	private IMCPostActionParam params;
	private Runnable processing;

	/**
	 * コンストラクタ<br>
	 * 
	 * @param action
	 *            API名
	 * @param param
	 *            パラメータ
	 * @param processing
	 *            呼び出し処理
	 */
	public ApiTask(int action, IMCPostActionParam param, Runnable processing) {
		this.action = action;
		this.params = param;
		this.processing = processing;
	}

	/**
	 * 呼び出しAPIの優先度を比較するクラス<br>
	 * 与えられたパラメータに対して、自身の方が優先度が高い場合、 true を返却する
	 * 
	 * @param action
	 *            比較したいAPI名
	 * @return true:与えられたパラメータより自身の方が優先度が高い場合, false:それ以外
	 */
	public boolean compare(int action) {
		boolean higher = false;
		switch (this.action) {
		case Consts.NOTIFY_COMPLETE_CANCEL:
			switch (action) {
			case MCDefAction.MCA_KIND_LISTEN:
			case MCDefAction.MCA_KIND_DOWNLOAD_CDN:
			case MCDefAction.MCA_KIND_TRACK_DL:
			case MCDefAction.MCA_KIND_ARTWORK_DL:
				higher = true;
				break;
			}
			break;
		}

		return higher;
	}

	public double getKey() {
		return key;
	}

	public void setKey(double key) {
		this.key = key;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public IMCPostActionParam getParams() {
		return params;
	}

	public void setParams(IMCPostActionParam params) {
		this.params = params;
	}

	public Runnable getProcessing() {
		return processing;
	}

	public void setProcessing(Runnable callback) {
		this.processing = callback;
	}

	public void clear() {
		params = null;
		processing = null;
	}
}
