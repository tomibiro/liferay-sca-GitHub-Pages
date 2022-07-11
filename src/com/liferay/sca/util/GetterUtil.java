package com.liferay.sca.util;

public class GetterUtil {

	public static boolean getBoolean(String str) {
		str = str.toLowerCase();

		if (str.equals("true") || str.equals("1")) {
			return true;
		}

		return false;
	}

}