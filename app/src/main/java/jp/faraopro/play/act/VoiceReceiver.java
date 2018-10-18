package jp.faraopro.play.act;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by masui on 2018/04/23.
 */

public class VoiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent receive_voice = new Intent();
        //FaRaoVoiceからのStrings受け取り
        String status = receive_voice.getStringExtra("voice");
        //FaRaoVoiceからのStrings値チェック
        if(status =="start") {
            Log.d("MSSUILOG",status);
            //受け取り値が正しい場合、prefarenceにFaRaoVoice再生中を書き込み
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            sp.edit().putBoolean("voice", true).commit();
        }
    }
}
