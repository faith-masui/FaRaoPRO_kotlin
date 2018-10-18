package jp.faraopro.play.mclient;

import android.os.Build;
import android.text.TextUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.common.Flavor;
import jp.faraopro.play.util.NetworkUtil;

// commons-codec-1.2.jar

/**
 * HTTPアクセス管理クラス
 *
 * @author AIM Corporation
 */
public class MCHttpClient {
    // put out logs
    protected boolean DEBUG = true;

    private static final String MC_ENCODE_ALGORITHM = "HMACSHA1";
    private static final String MC_AUTHTAG_TITLE = "Authorization";
    private static final String MC_AUTH_API_PK = "f3181ef0-937f-4978-91f3-aa9e66a85723"; // 公開鍵 biz GP
    private static final String MC_AUTH_API_SK = "N3hoajdia0hnODZSRmlYVGt5U24wVQ=="; // 秘密鍵 biz
    private static final String MC_DEFAULT_CHARSET_STR = "UTF-8";
    private static final int MC_DEFAULT_TIMEOUT = 10 * 1000;
    private static final String appVer = Flavor.APP_VERSION;
    private static final String httpVer = "4.0";
    private static final String deviceVer = Build.VERSION.RELEASE;

    private boolean mCanceled;
    protected InputStream mInputStream = null;
    private MCPostActionInfo mActionInfo = null;

    /**
     * インスタンスの取得
     *
     * @return
     */
    public static MCHttpClient getInstance() {
        return new MCHttpClient();
    }

    private MCHttpClient() {
        lclear();
    }

    private void lclear() {
        clearCancel();

        if (mActionInfo != null)
            mActionInfo.clear();
        mActionInfo = null;

        if (mInputStream != null) {
            try {
                mInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mInputStream = null;
    }

    /**
     * 事前情報設定(送信処理前必須)
     *
     * @param info
     */
    public void setPrepare(MCPostActionInfo info) {
        lclear();
        mActionInfo = info;
    }

    /**
     * ストリームの取得
     *
     * @return
     */
    public InputStream getStream() {
        return mInputStream;
    }

    private void setStream(InputStream stream) {
        mInputStream = stream;
    }

    /**
     * 送信&Header解析
     *
     * @return
     */
    public IMCResultInfo action() {
        InputStream input;
        IMCResultInfo result = new MCAnalyzeHeaderInfo();

        DefaultHttpClient httpclient = new DefaultHttpClient();
        // NonSecureHttpClient httpclient = new NonSecureHttpClient();
        HttpPost httppost = new HttpPost(mActionInfo.getUrl());
        HttpResponse response = null;
        HttpParams params = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, MC_DEFAULT_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, MC_DEFAULT_TIMEOUT);
        params.setParameter(CoreProtocolPNames.USER_AGENT,
                "FaRaoPRO_GP/" + appVer + " HttpClient/" + httpVer + " Android/" + deviceVer);

        if (mActionInfo.getActionKind() == MCDefAction.MCA_KIND_TRACK_DL
                || mActionInfo.getActionKind() == MCDefAction.MCA_KIND_ARTWORK_DL) {
            return actionGet();
        }

        FRODebug.logI(getClass(), "Http request params IN -------------------------------------", DEBUG);
        FRODebug.logI(getClass(), "USER=AGENT = " + params.getParameter(CoreProtocolPNames.USER_AGENT), DEBUG);
        FRODebug.logI(getClass(), "API NAME = " + mActionInfo.getUrl(), DEBUG);
        try {
            if (mActionInfo.getParams() != null)
                httppost.setEntity(new UrlEncodedFormEntity(mActionInfo.getParams(), MC_DEFAULT_CHARSET_STR));
            List<NameValuePair> param = mActionInfo.getParams();
            for (int i = 0; i < param.size(); i++) {
                String name = param.get(i).getName();
                String value = param.get(i).getValue();
                FRODebug.logI(getClass(), "<" + name + ">  " + value, DEBUG);
            }
            FRODebug.logI(getClass(), "Http request params OUT -------------------------------------\n", DEBUG);
            FRODebug.logI(getClass(), " ", DEBUG);
            httppost.setHeader("MIME-version", "1.0");
            httppost.addHeader(MC_AUTHTAG_TITLE, getAuthValue());

            response = httpclient.execute(httppost);
            // response = httpclient.execute2(httppost);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            HttpEntity tmp = response.getEntity();
            FRODebug.logI(getClass(), "response size = " + tmp.getContentLength(), DEBUG);
            tmp.writeTo(byteArrayOutputStream);
            // FRODebug.logD(getClass(), "Http response header IN
            // -------------------------------------", DEBUG);
            // for (Header h : allHeader) {
            // FRODebug.logD(getClass(), "<" + h.getName() + "> " +
            // h.getValue(), DEBUG);
            // }
            // FRODebug.logD(getClass(), "[FRO] status code = " +
            // response.getStatusLine().getStatusCode(), DEBUG);
            // FRODebug.logD(getClass(), "Http response header OUT
            // -------------------------------------\n", DEBUG);

            if (response.containsHeader("X-ENC")) {
                Header heds = response.getFirstHeader("X-ENC");
                String value = heds.getValue();
                String[] strArray = value.split(",");
                String[] encMethodArray = strArray[0].split("\"");

                String[] encKeyArray = strArray[1].split("\"");

                String[] encIvArray = strArray[2].split("\"");

                result.setString(MCDefResult.MCR_KIND_ENCMETHOD, encMethodArray[1]);
                result.setString(MCDefResult.MCR_KIND_ENCKEY, encKeyArray[1]);
                result.setString(MCDefResult.MCR_KIND_ENCIV, encIvArray[1]);
                result.setString(MCDefResult.MCR_KIND_API_SIG, this.api_sig);
            }

            result.setInt(MCDefResult.MCR_KIND_STATUS_CODE, response.getStatusLine().getStatusCode());
            input = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            byteArrayOutputStream.close();
            setStream(input);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            result.setInt(MCDefResult.MCR_KIND_STATUS_CODE, MCError.MC_APPERR_UNSUPPORTED_ENC);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result.setInt(MCDefResult.MCR_KIND_STATUS_CODE, MCError.MC_APPERR_CLIENT_PROTOCOL);
        } catch (IOException e) {
            e.printStackTrace();
            result.setInt(MCDefResult.MCR_KIND_STATUS_CODE, MCError.MC_APPERR_IO_HTTP);
        }
        clearCancel();
        return result;
    }

    public IMCResultInfo actionGet() {
        InputStream input = null;
        IMCResultInfo result = new MCAnalyzeHeaderInfo();

        DefaultHttpClient httpclient = new DefaultHttpClient();
        // NonSecureHttpClient httpclient = new NonSecureHttpClient();
        HttpParams params = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, MC_DEFAULT_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, MC_DEFAULT_TIMEOUT);
        MCPostActionParam postParam = (MCPostActionParam) mActionInfo.getPostActionParam();
        MCCdnMusicItem param = (MCCdnMusicItem) postParam.getGetParam();
        HttpGet httpget;
        HttpResponse response;
        int kind;

        if (mActionInfo.getActionKind() == MCDefAction.MCA_KIND_TRACK_DL) {
            String url = param.getString(MCDefResult.MCR_KIND_ITEM_CDN_TRACK_URL);
            kind = MCDefAction.MCA_KIND_TRACK_DL;
            httpget = new HttpGet(url);
            FRODebug.logI(getClass(), "API NAME = " + url, DEBUG);
        } else if (mActionInfo.getActionKind() == MCDefAction.MCA_KIND_THUMB_DL) {
            String url = postParam.getStringValue(MCDefParam.MCP_KIND_TRACKID);
            String lastModified = postParam.getStringValue(MCDefParam.MCP_KIND_RANGE);
            param = new MCCdnMusicItem();
            param.setString(MCDefResult.MCR_KIND_TRACKITEM_ID, postParam.getStringValue(MCDefParam.MCP_KIND_THUMB_ID));
            kind = MCDefAction.MCA_KIND_THUMB_DL;
            httpget = new HttpGet(url);
            FRODebug.logI(getClass(), "lastModified = " + lastModified, true);
            if (!TextUtils.isEmpty(lastModified))
                httpget.addHeader("If-Modified-Since", lastModified);
            FRODebug.logI(getClass(), "API NAME = " + url, DEBUG);
        } else {
            String url = param.getString(MCDefResult.MCR_KIND_ITEM_CDN_JACKET_URL);
            kind = MCDefAction.MCA_KIND_ARTWORK_DL;
            httpget = new HttpGet(url);
            FRODebug.logI(getClass(), "API NAME = " + url, DEBUG);
        }

        FRODebug.logI(getClass(), "------ Http request params IN ------", DEBUG);
        FRODebug.logI(getClass(), "API NAME = " + mActionInfo.getUrl(), DEBUG);
        FRODebug.logI(getClass(), "------ Http request params OUT ------\n", DEBUG);
        try {
            response = httpclient.execute(httpget);
            // response = httpclient.execute2(httpget);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            HttpEntity tmp = response.getEntity();


            Header[] allHeader = response.getAllHeaders();
            FRODebug.logD(getClass(), "------ Http response header IN ------", DEBUG);
            for (Header h : allHeader) {
                FRODebug.logD(getClass(), "<" + h.getName() + "> " + h.getValue(), DEBUG);
            }
            FRODebug.logD(getClass(), "status code = " + response.getStatusLine().getStatusCode(), DEBUG);
            FRODebug.logD(getClass(), "------ Http response header OUT ------\n", DEBUG);

            result.setString(MCDefResult.MCR_KIND_ENCMETHOD, param.getString(MCDefResult.MCR_KIND_ITEM_METHOD));
            result.setString(MCDefResult.MCR_KIND_ENCKEY, param.getString(MCDefResult.MCR_KIND_ITEM_KEY));
            result.setString(MCDefResult.MCR_KIND_ENCIV, param.getString(MCDefResult.MCR_KIND_ITEM_IV));
            result.setString(MCDefResult.MCR_KIND_API_SIG, param.getString(MCDefResult.MCR_KIND_ITEM_SIGNATURE));

            long contentLength = 0;
            if (response.containsHeader("Content-Length")) {
                Header header = response.getFirstHeader("Content-Length");
                String value = header.getValue();
                if (!TextUtils.isEmpty(value))
                    contentLength = Long.valueOf(value);
            }
            if (listener != null)
                listener.onStartDownload((int) contentLength);

            result.setInt(MCDefResult.MCR_KIND_STATUS_CODE, response.getStatusLine().getStatusCode());

            if (result.getInt(MCDefResult.MCR_KIND_STATUS_CODE) == HttpStatus.SC_OK && tmp != null) {
                input = tmp.getContent();
                int res = MCError.MC_NO_ERROR;
                if (!TextUtils.isEmpty(param.getOutputFileName(kind))) {
                    res = saveContent(param.getOutputFileName(kind), input, contentLength);
                }
                result.setInt(MCDefResult.MCR_KIND_STATUS_CODE, res);
                result.setString(MCDefResult.MCR_KIND_DL_PATH, param.getOutputFileName(kind));
                FRODebug.logI(getClass(), "path = " + param.getOutputFileName(kind), true);
                result.setString(MCDefResult.MCR_KIND_TRACKITEM_ID, param.getString(MCDefResult.MCR_KIND_TRACKITEM_ID));
            }

            byteArrayOutputStream.close();
            setStream(input);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            result.setInt(MCDefResult.MCR_KIND_STATUS_CODE, MCError.MC_APPERR_UNSUPPORTED_ENC);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result.setInt(MCDefResult.MCR_KIND_STATUS_CODE, MCError.MC_APPERR_CLIENT_PROTOCOL);
        } catch (IOException e) {
            e.printStackTrace();
            result.setInt(MCDefResult.MCR_KIND_STATUS_CODE, MCError.MC_APPERR_IO_HTTP);
        }
        clearCancel();
        return result;
    }

    private String api_sig;

    /**
     * 中断
     *
     * @return
     */
    public void cancel() {
        mCanceled = true;
    }

    private void clearCancel() {
        mCanceled = false;
    }

    /**
     * タイムスタンプ(UTC・秒)
     *
     * @return
     */
    private String getTimeStamp() {
        String value = null;
        long utc = System.currentTimeMillis() / 1000;
        value = String.valueOf(utc);
        return value;
    }

    /**
     * Nonce値作成
     *
     * @return
     */
    private String getNonce() {
        String value = "";
        int i;
        String[] str = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

        List<String> list = Arrays.asList(str);

        for (i = 0; i < 16; i++) {
            Collections.shuffle(list);
            value += list.get(0).toString();
        }
        list = null;
        return value;
    }

    private String getAuthValue() {
        String value;
        String timestamp = getTimeStamp();
        String nonce = getNonce();
        String api_key = MC_AUTH_API_PK;
        String sig_items = nonce + timestamp + api_key;
        String api_sig = getApiSignature(sig_items);

        // for debug
        this.api_sig = api_sig;
        // for debug

        value = "X-FSSE api-key=\"" + api_key + "\",nonce=\"" + nonce + "\",timestamp=\"" + timestamp
                + "\",api-signature=\"" + api_sig + "\"";

        return value;
    }

    private String getApiSignature(String items) {
        String value = null;

        // 秘密Key暗号化解除
        String api_secret = null;
        try {
            byte[] tmp = Base64.decodeBase64(MC_AUTH_API_SK.getBytes(MC_DEFAULT_CHARSET_STR));
            xor(tmp);
            api_secret = new String(tmp, MC_DEFAULT_CHARSET_STR);

            // Log.d(this.getClass().getSimpleName(), "pkey = " +
            // MC_AUTH_API_PK);
            // Log.d(this.getClass().getSimpleName(), "skey = " + api_secret);

            // String a = "";
            // byte[] b = a.getBytes(MC_DEFAULT_CHARSET);
            // xor(b);
            // byte[] c = Base64.encodeBase64(b);
            // String d = new String(c, MC_DEFAULT_CHARSET_STR);
            // Log.d(this.getClass().getSimpleName(), "***pkey = " + d);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return value;
        }
        byte[] key = api_secret.getBytes();
        byte[] input = items.getBytes();

        value = makeApiSignature(key, input);

        return value;
    }

    private String makeApiSignature(byte[] key, byte[] input) {
        Mac hmacSha1 = null;
        String strBase64 = null;
        try {
            hmacSha1 = Mac.getInstance(MC_ENCODE_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        try {
            hmacSha1.init(new SecretKeySpec(key, MC_ENCODE_ALGORITHM));
        } catch (InvalidKeyException e) {
            return null;
        }
        byte[] byteArray = hmacSha1.doFinal(input);
        if (byteArray != null) {
            strBase64 = new String(Base64.encodeBase64(byteArray));
        }
        return strBase64;
    }

    public static void xor(byte[] src) {
        for (int i = 0; i < src.length; i++) {
            src[i] = (byte) (src[i] ^ 1);
        }
    }

    private int saveContent(String filePath, InputStream content, long contentLength) {
        int res = MCError.MC_NO_ERROR;
        try {
            NetworkUtil.ProgressCallback progressListener = null;
            if (listener != null) {
                progressListener = new NetworkUtil.ProgressCallback() {
                    @Override
                    public void onProgress(long contentLength, long downloadedSize) {
                        if (listener != null)
                            listener.onProgress((int) downloadedSize);
                    }
                };
            }
            NetworkUtil.writeResponseBodyToDisk(filePath, content, contentLength, progressListener);
        } catch (IOException e) {
            e.printStackTrace();
            res = MCError.MC_APPERR_FILE;
        }
        return res;
    }

    IMCHttpClientListener listener;

    public void setProgressListener(IMCHttpClientListener listener) {
        this.listener = listener;
    }

    public interface IMCHttpClientListener {
        void onStartDownload(int size);

        void onProgress(int progress);
    }
}
