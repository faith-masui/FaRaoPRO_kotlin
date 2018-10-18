package jp.faraopro.play.mclient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

public class NonSecureHttpClient extends DefaultHttpClient {
	public NonSecureHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new NonSecureSSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Scheme https = new Scheme("https", sf, 443);
			this.getConnectionManager().getSchemeRegistry().register(https);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HttpResponse execute2(HttpUriRequest request) throws ClientProtocolException, IOException {
		HttpContext context = new BasicHttpContext();
		context.setAttribute(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		context.setAttribute(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
		context.setAttribute(CoreProtocolPNames.HTTP_CONTENT_CHARSET, HTTP.UTF_8);

		return super.execute(request, context);
	}

	public class NonSecureSSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public NonSecureSSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			// 自己署名証明書を受け付けるカスタムSSLContextの準備
			TrustManager tm = new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
				throws IOException, UnknownHostException {
			// カスタムSSLContext経由で生成したSSLソケットを返す。
			return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			// カスタムSSLContext経由で生成したSSLソケットを返す。
			return sslContext.getSocketFactory().createSocket();
		}
	}
}
