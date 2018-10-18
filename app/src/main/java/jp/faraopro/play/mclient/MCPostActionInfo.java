package jp.faraopro.play.mclient;

import android.text.TextUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import jp.faraopro.play.common.Flavor;

/**
 * Web API用パラメータ確定&動作決定クラス
 *
 * @author AIM Corporation
 */
public class MCPostActionInfo {
    private String mUrl;
    private List<NameValuePair> mParams = null;
    private int mActionKind;
    private boolean mIncludeXMLResultMSG;
    private boolean mAnalyzeXML;
    private IMCPostActionParam mBaseParam = null;

    // public static final String MC_OUTPUT_DIR = "/FaraoPRO";

    /**
     * コンストラクタ
     *
     * @param kind
     * @param param
     */
    public MCPostActionInfo(int kind, IMCPostActionParam param) {
        lclear();
        mBaseParam = param;
        mActionKind = kind;
        setPrepare();
    }

    /**
     * 解放処理
     */
    public void clear() {
        lclear();
        if (mBaseParam != null)
            mBaseParam.clear();
        mBaseParam = null;
    }

    private void lclear() {
        mActionKind = (-1);
        mIncludeXMLResultMSG = false;
        mUrl = null;
        if (mParams != null)
            mParams.clear();
        mParams = null;
        mAnalyzeXML = false;
    }

    private void setPrepare() {
        int kind = mActionKind;
        // Action URL作成
        if (MCDefAction.getApi(kind) != null)
            mUrl = Flavor.MC_URL_BASE + MCDefAction.getApi(kind);
        // HTTP用パラメータリスト作成
        mParams = new ArrayList<>();
        makePramList(kind);

        // レスポンスのXMLからメッセージを抽出し、エラー判断する必要があるか否か？
        mIncludeXMLResultMSG = MCDefAction.isIncXMLResMSG(kind);

        // XML解析必要？
        mAnalyzeXML = MCDefAction.isAnalyzeXml(kind);
    }

    /**
     * Url取得
     *
     * @return
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * パラメータリスト取得
     *
     * @return
     */
    public List<NameValuePair> getParams() {
        return mParams;
    }

    /**
     * Action種別
     *
     * @return
     */
    public int getActionKind() {
        return mActionKind;
    }

    /**
     * ステータスコードは200だが、XMLにメッセージが別途はいっているか否か？
     *
     * @return
     */
    public boolean isXmlResultMSG() {
        return mIncludeXMLResultMSG;
    }

    /**
     * XMLデータの返却があるか否か？
     *
     * @return
     */
    public boolean isAnalyzeXML() {
        return mAnalyzeXML;
    }

    /**
     * パラメータ一覧取得
     *
     * @return
     */
    public IMCPostActionParam getPostActionParam() {
        return mBaseParam;
    }

    private void makePramList(int kind) {
        switch (kind) {
            case MCDefAction.MCA_KIND_SIGNUP:
                addParam(MCDefParam.MCP_TAG_EMAIL);
                addParam(MCDefParam.MCP_TAG_PASSWORD);
                addParam(MCDefParam.MCP_TAG_LANGUAGE);
                break;
            case MCDefAction.MCA_KIND_ACTIVATION:
                addParam(MCDefParam.MCP_TAG_ACTIVATIONKEY);
                break;
            case MCDefAction.MCA_KIND_LOGIN:
                addParam(MCDefParam.MCP_TAG_EMAIL);
                addParam(MCDefParam.MCP_TAG_PASSWORD);
                addParam(MCDefParam.MCP_TAG_DEVICE_TOKEN);
                addParam(MCDefParam.MCP_TAG_FORCE);
                addParam(MCDefParam.MCP_TAG_STATUS);
                break;
            case MCDefAction.MCA_KIND_LOGOUT:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                break;
            case MCDefAction.MCA_KIND_STATUS:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                break;
            case MCDefAction.MCA_KIND_LISTEN:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_MODE_NO);
                addParam(MCDefParam.MCP_TAG_MYCHANNELID);
                addParam(MCDefParam.MCP_TAG_RANGE);
                break;
            case MCDefAction.MCA_KIND_RATING:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_MODE_NO);
                addParam(MCDefParam.MCP_TAG_MYCHANNELID);
                addParam(MCDefParam.MCP_TAG_RANGE);
                addParam(MCDefParam.MCP_TAG_RATING_TRACKID);
                addParam(MCDefParam.MCP_TAG_RATE_ACTION);
                addParam(MCDefParam.MCP_TAG_PLAY_COMPLETE);
                addParam(MCDefParam.MCP_TAG_PLAY_DURATIN);
                if (!TextUtils.isEmpty(mBaseParam.getStringValue(MCDefParam.MCP_KIND_ERROR_REASON)))
                    addParam(MCDefParam.MCP_TAG_ERROR_REASON);
                break;
            case MCDefAction.MCA_KIND_LIST:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_MODE_NO);
                addParam(MCDefParam.MCP_TAG_PARENT_NODE);
                break;
            case MCDefAction.MCA_KIND_SEARCH:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_MODE_NO);
                addParam(MCDefParam.MCP_TAG_KEYWORD);
                break;
            case MCDefAction.MCA_KIND_SET:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_MYCHANNEL_ID);
                addParam(MCDefParam.MCP_TAG_MYCHANNEL_NAME);
                addParam(MCDefParam.MCP_TAG_MYCHANNEL_LOCK);
                break;
            case MCDefAction.MCA_KIND_TRACK_DL:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_TRACKID);
                addParam(MCDefParam.MCP_TAG_QUARITY);
                // addParam(MCDefParam.MCP_TAG_FORMAT);
                break;
            case MCDefAction.MCA_KIND_ARTWORK_DL:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_JACKETID);
                break;
            case MCDefAction.MCA_KIND_PING:
                break;
            case MCDefAction.MCA_KIND_SAVE:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_MYCHANNEL_ID);
                break;
            case MCDefAction.MCA_KIND_AD_DL:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_ADID);
                addParam(MCDefParam.MCP_TAG_TYPE_DL);
                break;
            case MCDefAction.MCA_KIND_MSG_DL:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_TYPE_DL);
                break;
            case MCDefAction.MCA_KIND_AD_LIST:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                break;
            case MCDefAction.MCA_KIND_AD_RATING:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_RATE_ADID);
                addParam(MCDefParam.MCP_TAG_RATE_ACTION);
                break;
            case MCDefAction.MCA_KIND_PAYMENT_SUB:
                addParam(MCDefParam.MCP_TAG_TRACKING_KEY);
                addParam(MCDefParam.MCP_TAG_TYPE_DL);
                addParam(MCDefParam.MCP_TAG_MYCHANNEL_ID);
                addParam(MCDefParam.MCP_TAG_RECEIPT);
                addParam(MCDefParam.MCP_TAG_PAID);
                break;
            case MCDefAction.MCA_KIND_PAYMENT_ITEM:
                addParam(MCDefParam.MCP_TAG_TRACKING_KEY);
                addParam(MCDefParam.MCP_TAG_TYPE_DL);
                addParam(MCDefParam.MCP_TAG_MYCHANNEL_ID);
                addParam(MCDefParam.MCP_TAG_RECEIPT);
                addParam(MCDefParam.MCP_TAG_PAID);
                break;
            case MCDefAction.MCA_KIND_PAYMENT_LOCK:
                addParam(MCDefParam.MCP_TAG_TRACKING_KEY);
                break;
            case MCDefAction.MCA_KIND_PAYMENT_COMMIT:
                addParam(MCDefParam.MCP_TAG_TRACKING_KEY);
                addParam(MCDefParam.MCP_TAG_PAYMENT_TYPE);
                addParam(MCDefParam.MCP_TAG_PAYMENT_ID);
                break;
            case MCDefAction.MCA_KIND_PAYMENT_CANCEL:
                addParam(MCDefParam.MCP_TAG_TRACKING_KEY);
                break;
            case MCDefAction.MCA_KIND_RATING_OFFLINE:
                addParam(MCDefParam.MCP_TAG_TRACKING_KEY);
                addParam(MCDefParam.MCP_TAG_MODE_NO);
                addParam(MCDefParam.MCP_TAG_MYCHANNELID);
                addParam(MCDefParam.MCP_TAG_RANGE);
                addParam(MCDefParam.MCP_TAG_RATING_TRACKID);
                addParam(MCDefParam.MCP_TAG_RATE_ACTION);
                addParam(MCDefParam.MCP_TAG_PLAY_COMPLETE);
                addParam(MCDefParam.MCP_TAG_PLAY_DURATIN);
                break;
            case MCDefAction.MCA_KIND_CHANNEL_SHARE:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_MYCHANNEL_ID);
                break;
            case MCDefAction.MCA_KIND_CHANNEL_EXPAND:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_SHARE_SHAREKEY);
                break;
            case MCDefAction.MCA_KIND_FACEBOOK_LOOKUP:
                addParam(MCDefParam.MCP_TAG_EMAIL);
                break;
            case MCDefAction.MCA_KIND_FACEBOOK_LOGIN:
                addParam(MCDefParam.MCP_TAG_ACCESS_TOKEN);
                addParam(MCDefParam.MCP_TAG_EMAIL);
                addParam(MCDefParam.MCP_TAG_FORCE);
                break;
            case MCDefAction.MCA_KIND_FACEBOOK_ACCOUNT:
                addParam(MCDefParam.MCP_TAG_EMAIL);
                addParam(MCDefParam.MCP_TAG_PASSWORD);
                addParam(MCDefParam.MCP_TAG_GENDER);
                addParam(MCDefParam.MCP_TAG_BIRTH_YEAR);
                addParam(MCDefParam.MCP_TAG_PROVINCE);
                addParam(MCDefParam.MCP_TAG_REGION);
                addParam(MCDefParam.MCP_TAG_COUNTRY);
                break;
            case MCDefAction.MCA_KIND_FEATURED:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_MODE_SEARCH);
                break;
            case MCDefAction.MCA_KIND_THUMB_DL:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_THUMB_ID);
                break;
            case MCDefAction.MCA_KIND_TICKET_CHECK:
                addParam(MCDefParam.MCP_TAG_TRACKING_KEY);
                addParam(MCDefParam.MCP_TAG_TICKET_DOMAIN);
                addParam(MCDefParam.MCP_TAG_TICKET_SERIAL);
                break;
            case MCDefAction.MCA_KIND_TICKET_ADD:
                addParam(MCDefParam.MCP_TAG_TRACKING_KEY);
                addParam(MCDefParam.MCP_TAG_TICKET_DOMAIN);
                addParam(MCDefParam.MCP_TAG_TICKET_SERIAL);
                addParam(MCDefParam.MCP_TAG_CAMPAIGN_ID);
                break;
            case MCDefAction.MCA_KIND_SEARCH_SHOP:
                addParam(MCDefParam.MCP_TAG_LATITUDE);
                addParam(MCDefParam.MCP_TAG_LONGITUDE);
                addParam(MCDefParam.MCP_TAG_DISTANCE);
                addParam(MCDefParam.MCP_TAG_INDUSTRY);
                break;

            case MCDefAction.MCA_KIND_DOWNLOAD_CDN:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                if (!TextUtils.isEmpty(mBaseParam.getStringValue(MCDefParam.MCP_KIND_JACKETID)))
                    addParam(MCDefParam.MCP_TAG_JACKETID);
                if (!TextUtils.isEmpty(mBaseParam.getStringValue(MCDefParam.MCP_KIND_TRACKID)))
                    addParam(MCDefParam.MCP_TAG_TRACKID);
                addParam(MCDefParam.MCP_TAG_QUARITY);
                break;

            case MCDefAction.MCA_KIND_LICENSE_STATUS:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                break;
            case MCDefAction.MCA_KIND_LICENSE_INSTALL:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_LATITUDE);
                addParam(MCDefParam.MCP_TAG_LONGITUDE);
                break;
            case MCDefAction.MCA_KIND_LICENSE_TRACKING:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_LATITUDE);
                addParam(MCDefParam.MCP_TAG_LONGITUDE);
                break;
            case MCDefAction.MCA_KIND_TEMPLATE_LIST:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_TEMPLATE_TYPE);
                if (!TextUtils.isEmpty(mBaseParam.getStringValue(MCDefParam.MCP_KIND_TEMPLATE_ID)))
                    addParam(MCDefParam.MCP_TAG_TEMPLATE_ID);
                break;
            case MCDefAction.MCA_KIND_TEMPLATE_DOWNLOAD:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_TEMPLATE_TYPE);
                addParam(MCDefParam.MCP_TAG_TEMPLATE_ID);
                break;

            case MCDefAction.MCA_KIND_STREAM_LIST:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_SOURCE_TYPE);
                break;
            case MCDefAction.MCA_KIND_STREAM_PLAY:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_STREAM_ID);
                break;
            case MCDefAction.MCA_KIND_STREAM_LOGGING:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_STREAM_ID);
                addParam(MCDefParam.MCP_TAG_PLAY_DURATIN);
                addParam(MCDefParam.MCP_TAG_PLAY_COMPLETE);
                break;
            case MCDefAction.MCA_KIND_STREAM_LOGGING_OFFLINE:
                addParam(MCDefParam.MCP_TAG_TRACKING_KEY);
                addParam(MCDefParam.MCP_TAG_STREAM_ID);
                addParam(MCDefParam.MCP_TAG_PLAY_DURATIN);
                addParam(MCDefParam.MCP_TAG_PLAY_COMPLETE);
                break;

            case MCDefAction.MCA_KIND_NETWORK_RECOVERY:
                addParam(MCDefParam.MCP_TAG_TRACKING_KEY);
                addParam(MCDefParam.MCP_TAG_DOWNTIME_BEGIN);
                addParam(MCDefParam.MCP_TAG_DOWNTIME_END);
                addParam(MCDefParam.MCP_TAG_OFFLINE_CAUSE);
                break;

            case MCDefAction.MCA_KIND_TEMPLATE_UPDATE:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_TEMPLATE_ID);
                break;

            case MCDefAction.MCA_KIND_PATTERN_SCHEDULE:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_TARGET_DATE);
                break;
            case MCDefAction.MCA_KIND_PATTERN_DOWNLOAD:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_PATTERN_ID);
                break;
            case MCDefAction.MCA_KIND_PATTERN_UPDATE:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_TARGET_DATE);
                addParam(MCDefParam.MCP_TAG_PATTERN_ID);
                break;
            case MCDefAction.MCA_KIND_PATTERN_ONAIR:
                addParam(MCDefParam.MCP_TAG_SESSIONKEY);
                addParam(MCDefParam.MCP_TAG_AUDIO_ID);
                addParam(MCDefParam.MCP_TAG_FRAME_ID);
                addParam(MCDefParam.MCP_TAG_COMPLETE);
                break;
            case MCDefAction.MCA_KIND_PATTERN_ONAIR_OFFLINE:
                addParam(MCDefParam.MCP_TAG_TRACKING_KEY);
                addParam(MCDefParam.MCP_TAG_AUDIO_ID);
                addParam(MCDefParam.MCP_TAG_FRAME_ID);
                addParam(MCDefParam.MCP_TAG_COMPLETE);
                break;

            default:
                break;
        }
    }

    private void addParam(String tag) {
        int kind = MCDefParam.getKind(tag);
        if (kind > MCDefParam.MCP_KIND_UNKNOWN) {
            if (kind != MCDefParam.MCP_KIND_AUDIO_ID && kind != MCDefParam.MCP_KIND_JACKETID) {
                NameValuePair param = new BasicNameValuePair(tag, mBaseParam.getStringValue(kind));
                if (param != null)
                    mParams.add(param);
            } else {
                String linkedValue = mBaseParam.getStringValue(kind);
                String[] splited = null;
                switch (kind) {
                    case MCDefParam.MCP_KIND_AUDIO_ID:
                        splited = (linkedValue != null) ? linkedValue.split("&") : null;
                        break;
                    case MCDefParam.MCP_KIND_JACKETID:
                        splited = (linkedValue != null) ? linkedValue.split(",") : null;
                        break;
                }
                if (splited != null && splited.length > 0) {
                    for (String value : splited) {
                        NameValuePair param = new BasicNameValuePair(tag, value);
                        if (param != null)
                            mParams.add(param);
                    }
                }
            }

        }
    }
}
