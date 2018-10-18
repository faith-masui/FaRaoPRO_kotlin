package jp.faraopro.play.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文字列チェッククラス
 * 
 * @author ksu
 * 
 */
public class TextChecker {
	final static int numeric = 1;
	final static int lower = 2;
	final static int upper = 4;
	final static String strNum = "0-9";
	final static String strLower = "a-z";
	final static String strUpper = "A-Z";
	static String regex;

	// FOR TEST --------------------------
	public static String getErrMsg(int ret) {
		String s0 = "no error";
		String s1 = "must be only num";
		String s2 = "must be only low";
		String s3 = "must be num or low";
		String s4 = "must be only upp";
		String s5 = "must be num or upp";
		String s6 = "must be low or upp";
		String s7 = "must be num or low or upp";
		switch (ret) {
		case 0:
			return s0;
		case 1:
			return s1;
		case 2:
			return s2;
		case 3:
			return s3;
		case 4:
			return s4;
		case 5:
			return s5;
		case 6:
			return s6;
		case 7:
			return s7;
		default:
			return "unknown error";
		}
	}

	// -----------------------------------

	/**
	 * 文字列の構成チェック
	 * 
	 * @param in
	 *            : チェックする文字列
	 * @param value
	 *            : チェックしたい構成
	 */
	public static int CheckType(String in, int value) {
		// flag = 1, word must be appear at least once;

		if (value == numeric) {
			regex = "[^" + strNum + "]";
		} else if (value == lower) {
			regex = "[^" + strLower + "]";
		} else if (value == upper) {
			regex = "[^" + strUpper + "]";
		} else if (value == (numeric | lower)) {
			regex = "[^" + strNum + strLower + "]";
		} else if (value == (numeric | upper)) {
			regex = "[^" + strNum + strUpper + "]";
		} else if (value == (lower | upper)) {
			regex = "[^" + strLower + strUpper + "]";
		} else if (value == (numeric | lower | upper)) {
			regex = "[^" + strNum + strLower + strUpper + "]";
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(in);
		if (matcher.find()) {
			return value;
		}

		return 0;
	}

	/**
	 * 英数混じりかどうかを判定
	 * 
	 * @param in
	 *            : 判定したい文字列
	 * @return : 含まれているか否か
	 */
	public static boolean CheckAppearance(String in) {
		// 後で作り直すこと

		Pattern pattern;
		Matcher matcher;
		String[] reg = new String[2];

		reg[0] = "[" + strNum + "]++";
		reg[1] = "[" + strUpper + strLower + "]++";
		for (int i = 0; i < 2; i++) {
			pattern = Pattern.compile(reg[i]);
			matcher = pattern.matcher(in);
			if (!matcher.find()) {
				return false;
			}
		}

		return true;
	}

	public static boolean CheckByte(String str, int size) {
		int len;

		len = str.length();
		for (int i = 0; i < len; i++) {
			if ((0x3040 <= str.charAt(i)) && (str.charAt(i) <= 0x30FF)) {
				size -= 2;
			} else if ((0x4E00 <= str.charAt(i)) && (str.charAt(i) <= 0x9FFF)) {
				size -= 2;
			} else if ((0xFF00 <= str.charAt(i)) && (str.charAt(i) <= 0xFF65)) {
				size -= 2;
			} else {
				size--;
			}
		}
		if (size >= 0) {
			return true;
		}

		return false;
	}

	public static boolean CheckLength(String in, int min, int max) {
		if (in.length() < min || max < in.length()) {
			return false;
		}
		return true;
	}

}
