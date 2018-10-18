package jp.faraopro.play.common;


import jp.faraopro.play.BuildConfig;

/**
 * Constant for "staging flavor"
 */
public class Flavor {
    // flag
    public static final boolean DEVELOPER_MODE = true;

    // URL
    public static final String FARAO_DOMAIN = "http://pro-stg.faraoradio.jp";
    public static final String APP_VERSION = BuildConfig.VERSION_NAME;
    public static final String URL_TOP = "http://pro-stg.faraoradio.jp/mobile_pro_app%s/index.html";
    public static final String PLAY_LINK = "http://www.faraopro.jp/banner";
    public static final String MC_URL_BASE = "https://pro-stg.faraoradio.jp/v1/rest";
    public static final String CONTACT_US_URL = "http://pro-stg.faraoradio.jp/other_app/call.html";
    //public static final String FAQ_URL = "http://pro-stg.faraoradio.jp/other_app/faq.html";
    public static final String FAQ_URL = "http://pro-stg.faraoradio.jp/other_app/help.html";
}
