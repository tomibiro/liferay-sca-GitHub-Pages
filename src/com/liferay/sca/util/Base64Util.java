package com.liferay.sca.util;

import java.util.Base64;

public class Base64Util {

	public static String encode(byte[] src) {
		Base64.Encoder encoder = Base64.getEncoder();

		return encoder.encodeToString(src);
	}

	public static String encode(String src) {
		return encode(src.getBytes());
	}

}