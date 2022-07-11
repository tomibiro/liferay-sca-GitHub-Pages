package com.liferay.sca.util;

public class ProjectPropsUtil {

	public static String get(String project, String key) {
		return PropsUtil.get(project + "." + key);
	}

	public static String[] getArray(String project, String key) {
		return PropsUtil.getArray(project + "." + key);
	}

}