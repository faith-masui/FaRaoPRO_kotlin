package jp.faraopro.play.app;

import jp.faraopro.play.app.Service_Callback_IF;

interface IBindService {

	void addListener( Service_Callback_IF listener );

	void removeListener( Service_Callback_IF listener );

	void callAPI();

	void sSignin( String mail, String pass );

	void sActivation();

	void sLogin( String mail, String pass, String force);

	void sLogout();

	void sGetStatus();

	void sGetGenreList();

	void sGetArtistList( String word );

	void sGetHot100List();

	void sGetMychannelList();

	void sGetMsg( String type );

	void sGetPlayLists(int mode, int channel, String range, int size, int permisson);

	void sRequestNext( String decison );

	void sPauseMusic();

	boolean sIsPlaying();

	void sTerm();

	void sSendDecision( String decision );

	void sTermMusic( int type );

	void sSetMyChannel( int id, String name, int lock );

	void sendPurchaseInfomation( String key, int type, int market, String receipt );

	void purchaseLock( String key );

	void purchaseCommit( String key );

	void purchaseCancel( String key );

	void oneHourNotify();

	void updateMusicInfo();

	int sCheckPlayerInstance();

	String sGetModeType();
	
	void sChannelShare(String id);
	
	void sChannelExpand(String shareKey);
	
	void sCancelBoot();
	
	void sInterruptCancel();
	
	String sGetSession();
	
	void sFacebookLookup(String email);
	
	void sFacebookLogin(String token, String email, String force);
	
	void sFacebookAccount(String mail, String pass, int gender, int year, String province, int region, String country);
	
	void sGetFeaturedArtistList();
	
	void sGetLocationList();
	
	void sAdRating(String rating);
	
	void sCheckTicket(String domain, String serial);
	
	void sAddTicket();
	
	void sSetSleepTimer(int time);
	
	int sGetCurrentPos();
	
	void sSearchShop(String latitude, String longitude, String distance, String industry);
	
	void sGetBusinessList(String node);
	
	void sPlayLocal(String path);

	void sRequestNextLocal(int direction);
	
	void sGetTemplateList(int type);
	
	void sDownloadTemplateList(int type, int id);
	
	void sGetStreamList(int type);
	
	void sPlayStream(int streamId);
	
	void sSendTemplateState();
	
	boolean sIsInterrupt();
	
	void sCheckInterruptSchedule();
	
	void sSetPreferenceBoolean(int type, boolean value);
	
	void sSetPreferenceInteger(int type, int value);
	
	void sLicenseStatus();
	
	int sGetPlayerType();
	
	void sUpdateInterruptData();

	void sChangeChannelVolume(float volume);
	void sChangeInterruptVolume(float volume);
	float sGetChannelVolume();
	float sGetInterruptVolume();
	void sChangeAudioFocusedVolume(float volume);
    float sGetAudioFocusedVolume();

	boolean sIsEmergency();
	void sPlayEmergency();
	void sPauseEmergency();
	void sCancelEmergency();
	int sGetEmergencyLength();
	int sGetEmergencyPosition();
	boolean sIsPlayingEmergency();
	boolean sIsRecoveredEmergency();

	String sGetPreferenceString(int type);
	int sGetInterruptIndex();
	int sGetInterruptPosition();
}