// Copyright 2010 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package jp.faraopro.play.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import android.content.Context;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.common.Digest;
import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.mclient.MCHttpClient;

/**
 * ローカルサーバクラス
 * 
 * @author ksu
 * 
 */
public class HttpRawResourceServer extends HttpServer {
	private final static boolean DEBUG = true;

	public String key;
	public String iv;
	public String sig;
	private static final String MC_AUTH_API_SK = "N3hoajdia0hnODZSRmlYVGt5U24wVQ==";
	private String defaultCharaset = "UTF-8";

	public void setEncParam(String key, String iv, String sig, String path) {
		this.key = key;
		this.iv = iv;
		this.sig = sig;
		super.internalPath = path;
	}

	/**
	 * Creates a new HttpRawResourceServer instance. This server accepts HTTP
	 * request and returns files in the res/raw folder of the the application.
	 * 
	 * When using with Android unit tests, note that AndroidTestCase.getContext
	 * returns the context of the <i>application under test</a>, not the test
	 * test itself, so this would look in the application's res/raw/ folder.
	 * 
	 * To get access to the test package's res/raw/ folder files, you need to
	 * derive your test from InstrumentationTestCase and then use
	 * getInstrumentation().getContext().
	 * 
	 * @param context
	 *            The Android application context. Used to access resources.
	 * @param streaming
	 *            Whether the server will continuously stream the content.
	 */
	public HttpRawResourceServer(Context context, boolean streaming) {
		if (context == null) {
			throw new IllegalArgumentException("Context for accessing resources cannot be null");
		}
		setSimulateStream(streaming);
		// resourceContext = context;
	}

	/**
	 * Creates a RawResourceDataSource object for the request.
	 * 
	 * @return A <code>DataSource</code> subclass for the raw resource request.
	 */
	@Override
	protected DataSource getData(String request) {
		// Remove initial '/' in path
		String path = request.substring(1);
		return new RawResourceDataSource(path);
	}

	protected class RawResourceDataSource extends DataSource {
		// private AssetFileDescriptor fd; //for asset data
		// private final int resourceId; //for asset data
		private final String resourcePath; // ksu
		private FileInputStream fis;

		public RawResourceDataSource(String resourceName) {
			resourcePath = resourceName;
		}

		public String hex(byte[] binary) {
			return new String(Hex.encodeHex(binary));
		}

		public byte[] bin(String hex) throws DecoderException {
			return Hex.decodeHex(hex.toCharArray());
		}

		@Override
		public InputStream createInputStream() throws IOException {
			// NB: Because createInputStream can only be called once per asset
			// we always create a new file descriptor here.
			// getFileDescriptor();
			getFileInputStream();

			byte[] apiSecretKey = Base64.decodeBase64(MC_AUTH_API_SK.getBytes(defaultCharaset));
			MCHttpClient.xor(apiSecretKey);

			byte[] apiKey = Base64.decodeBase64(key.getBytes(defaultCharaset));
			byte[] apiIv = null;
			try {
				apiIv = bin(iv);
			} catch (DecoderException e1) {
				e1.printStackTrace();
			}
			byte[] apiSig = Base64.decodeBase64(sig.getBytes(defaultCharaset));
			byte[] encKey = null;

			// 解除キーの複合化
			try {
				int pbkdf2_iteration = 1000;
				int pbkdf2_keylen = 32; // バイトサイズ
				String pbkdf2_hex = hex(Digest.pbkdf2_hmac_sha1(apiSecretKey, apiSig, pbkdf2_iteration, pbkdf2_keylen));
				// logV("pbkdf2(hex) = " + pbkdf2_hex);

				String pbkey0 = pbkdf2_hex.substring(0, 32);
				String pbkey16 = pbkdf2_hex.substring(32, 64);
				// logV( "pbkey0(hex) = " + pbkey0 );
				// logV( "pbkey16(hex) = " + pbkey16 );

				Cipher c1 = null;
				c1 = Cipher.getInstance("AES/CBC/PKCS7Padding");
				c1.init(Cipher.DECRYPT_MODE, new SecretKeySpec(bin(pbkey0), "AES"), new IvParameterSpec(bin(pbkey16)));
				encKey = c1.doFinal(apiKey);
			} catch (DecoderException e) {
				e.printStackTrace();
				errorCode = Consts.DECODER_EXCEPTION;

				FRODebug.logE(getClass(), e.getMessage(), DEBUG);

			} catch (GeneralSecurityException e) {
				e.printStackTrace();

				FRODebug.logE(getClass(), e.getMessage(), DEBUG);

			}

			// 楽曲の複合化
			Cipher c2 = null;
			try {
				c2 = Cipher.getInstance("AES/CTR/NoPadding");
				c2.init(Cipher.DECRYPT_MODE, new SecretKeySpec(encKey, "AES"), new IvParameterSpec(apiIv));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();

				FRODebug.logE(getClass(), e.getMessage(), DEBUG);

			} catch (NoSuchPaddingException e) {
				e.printStackTrace();

				FRODebug.logE(getClass(), e.getMessage(), DEBUG);

			} catch (InvalidKeyException e) {
				e.printStackTrace();

				FRODebug.logE(getClass(), e.getMessage(), DEBUG);

			} catch (InvalidAlgorithmParameterException e) {
				e.printStackTrace();

				FRODebug.logE(getClass(), e.getMessage(), DEBUG);

			}

			return new CipherInputStream(fis, c2);
		}

		@Override
		public String getContentType() {
			// TODO: Support other media if we need to
			return "audio/mp3";
		}

		@Override
		public long getContentLength() {
			if (isSimulatingStream()) {
				super.getContentLength();
			}
			if (fis == null) {
				getFileInputStream();
			}
			return new File(resourcePath).length();
		}

		private void getFileInputStream() {
			try {
				fis = new FileInputStream(new File(resourcePath));
			} catch (FileNotFoundException e) {
				e.printStackTrace();

				FRODebug.logE(getClass(), e.getMessage(), DEBUG);

			}
		}
	}

}