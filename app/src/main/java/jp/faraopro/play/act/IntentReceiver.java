package jp.faraopro.play.act;

import android.content.Intent;
import android.os.Bundle;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.BootActivity;

/**
 * 外部からのIntentを受け取るクラス<br>
 * 外部アプリのタスク上にFaRaoが展開されることを避けるため、<br>
 * フラグに{@link Intent#FLAG_ACTIVITY_NEW_TASK} を追加して{@link BootActivity}を起動する
 * 
 * @author AIM Corporation
 * 
 */
public class IntentReceiver extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		// String action = intent.getAction();
		// if (Intent.ACTION_VIEW.equals(action)) {
		// Uri uri = intent.getData();
		// }
		intent.setClass(getApplicationContext(), BootActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);

		finish();
	}
}
