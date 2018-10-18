package jp.faraopro.play.common;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Digest - SHA1/HMAC-SHA1/PBKDF-HMAC-SHA1
 */
public class Digest {
	private Digest() {
	}

	/**
	 * SHA1 - SHA1 ハッシュ値を出力します
	 * 
	 * @param input
	 *            入力バイト配列
	 * @return 160bit=20byte のハッシュ値を出力します
	 * @throws GeneralSecurityException
	 */
	public static byte[] sha1(byte[] input) throws GeneralSecurityException {
		MessageDigest sha1 = MessageDigest.getInstance("SHA1");
		return sha1.digest(input);
	}

	/**
	 * SHA1 - SHA1 ハッシュ値を出力します
	 * 
	 * @param in
	 *            入力ストリーム
	 * @return 160bit=20byte のハッシュ値を出力します
	 * @throws GeneralSecurityException
	 */
	public static byte[] sha1(InputStream in) throws GeneralSecurityException, IOException {
		MessageDigest sha1 = MessageDigest.getInstance("SHA1");
		int len = 0;
		byte[] temp = new byte[1024 * 8];
		while ((len = in.read(temp)) > 0) {
			sha1.update(temp, 0, len);
		}
		return sha1.digest();
	}

	/**
	 * HMAC-SHA1 - SHA1 ハッシュ関数を使用して、ハッシュメッセージ認証コードを出力します
	 * 
	 * @param key
	 *            キー
	 * @param input
	 *            入力バイト配列
	 * @return 160bit=20byte のハッシュメッセージ認証コードを出力します
	 * @throws GeneralSecurityException
	 */
	public static byte[] hmac_sha1(byte[] key, byte[] input) throws GeneralSecurityException {
		Mac hmacsha1 = Mac.getInstance("HMACSHA1");
		hmacsha1.init(new SecretKeySpec(key, "HMACSHA1"));
		return hmacsha1.doFinal(input);
	}

	/**
	 * HMAC-SHA1 - SHA1 ハッシュ関数を使用して、ハッシュメッセージ認証コードを出力します
	 * 
	 * @param key
	 *            キー
	 * @param in
	 *            入力ストリーム
	 * @return 160bit=20byte のハッシュメッセージ認証コードを出力します
	 * @throws GeneralSecurityException
	 */
	public static byte[] hmac_sha1(byte[] key, InputStream in) throws GeneralSecurityException, IOException {
		Mac hmacsha1 = Mac.getInstance("HMACSHA1");
		hmacsha1.init(new SecretKeySpec(key, "HMACSHA1"));
		int len = 0;
		byte[] temp = new byte[1024 * 8];
		while ((len = in.read(temp)) > 0) {
			hmacsha1.update(temp, 0, len);
		}
		return hmacsha1.doFinal();
	}

	/**
	 * PBKDF2-HMAC-SHA1 - パスワードベースの鍵導出関数
	 * 
	 * @param password
	 *            パスワード
	 * @param salt
	 *            ソルト
	 * @param iteration
	 *            イテレーション関数
	 * @param keylen
	 *            出力する鍵のバイトサイズ
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static byte[] pbkdf2_hmac_sha1(byte[] password, byte[] salt, int iteration, int keylen)
			throws GeneralSecurityException {

		// // Android SDK では PBKDF2WithHMACSHA1 がサポートされていない
		// SecretKeyFactory kf =
		// SecretKeyFactory.getInstance("PBKDF2WithHMACSHA1");
		// KeySpec ks = new PBEKeySpec(new String(password).toCharArray(), salt,
		// iteration, keylen*8);
		// SecretKey sk = kf.generateSecret(ks);
		// return sk.getEncoded();

		// Android では PBKDF2 を別途実装する。
		Mac mac = Mac.getInstance("HMACSHA1");
		Key key = new SecretKeySpec(password, mac.getAlgorithm());
		return new PBKDF2().generateKey(mac, key, salt, iteration, keylen);
	}

	public static class PBKDF2 {
		public byte[] generateKey(Mac mac, Key key, byte[] salt, int iterationCount, int keyBytes)
				throws InvalidKeyException, NoSuchAlgorithmException {
			int hlen = mac.getMacLength();
			int l = (keyBytes + hlen - 1) / hlen;
			byte[] in = new byte[4];
			byte[] out = new byte[l * hlen];
			for (int i = 1; i <= l; i++) {
				in[0] = (byte) ((i >> 24) & 0xff);
				in[1] = (byte) ((i >> 16) & 0xff);
				in[2] = (byte) ((i >> 8) & 0xff);
				in[3] = (byte) ((i >> 0) & 0xff);
				func(mac.getAlgorithm(), key, salt, iterationCount, in, out, (i - 1) * hlen);
			}
			byte[] output = new byte[keyBytes];
			System.arraycopy(out, 0, output, 0, output.length);
			return output;
		}

		private void func(String algorithm, Key key, byte[] s, int c, byte[] in, byte[] out, int offset)
				throws InvalidKeyException, NoSuchAlgorithmException {
			Mac mac = Mac.getInstance(algorithm);
			mac.init(key);
			if (s != null)
				mac.update(s, 0, s.length);
			mac.update(in, 0, in.length);
			byte[] state = mac.doFinal();
			System.arraycopy(state, 0, out, offset, state.length);
			for (int count = 1; count != c; count++) {
				mac = Mac.getInstance(algorithm);
				mac.init(key);
				mac.update(state);
				state = mac.doFinal();
				for (int j = 0; j != state.length; j++) {
					out[offset + j] ^= state[j];
				}
			}
		}
	}
}