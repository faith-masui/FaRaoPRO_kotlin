package jp.faraopro.play.app;

import jp.faraopro.play.mclient.IMCPostActionParam;

public class ApiRunnable implements Runnable {
	IMCPostActionParam params;

	public ApiRunnable(IMCPostActionParam params) {
		this.params = params;
	}

	@Override
	public void run() {

	}

}
