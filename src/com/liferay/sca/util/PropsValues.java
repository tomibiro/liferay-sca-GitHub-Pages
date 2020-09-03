package com.liferay.sca.util;

public interface PropsValues {

	public static final String GRADLE_PROJECT_TEMPLATE_TOKEN_FILE =
		PropsUtil.get(PropsKeys.GRADLE_PROJECT_TEMPLATE_TOKEN_FILE);

	public static final String[] GRADLE_VARIABLES = PropsUtil.getArray(
		PropsKeys.GRADLE_VARIABLES);

	public static final String[] PACKAGE_FILENAMES = PropsUtil.getArray(
		PropsKeys.PACKAGE_FILENAMES);

	public static final String[] PROJECTS = PropsUtil.getArray(
		PropsKeys.PROJECTS);

}