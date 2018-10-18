package jp.faraopro.play.app;

import jp.faraopro.play.domain.ListDataInfo;
import jp.faraopro.play.domain.MusicInfo;
import jp.faraopro.play.domain.MCListDataInfo;
import jp.faraopro.play.model.FROUserData;

interface Service_Callback_IF {
	void onNotifyResult(int when, int statusCode);
	void onNotifyChannelList(int when, int statusCode, inout ListDataInfo list);
	void onNotifyMusicData(int when, int statusCode, inout MusicInfo info);
	void onNotifyWithString(int when, int statusCode, String data);
	void onNotifyMCItemList(int when, int statusCode, inout MCListDataInfo list);
	void onNotifyUserData(int when, int statusCode, in FROUserData data);
}