package com.liferay.sca.util;

public class ArrayUtil {

	public static boolean contains(String[] array, String value) {
		for (String s: array) {
			if (s.equals(value)) {
				return true;
			}
		}

		return false;
	}

}