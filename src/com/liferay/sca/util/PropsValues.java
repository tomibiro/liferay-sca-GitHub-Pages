package com.liferay.sca.util;

public interface PropsValues {

	public static final String GRADLE_PROJECT_TEMPLATE_TOKEN_FILE =
		PropsUtil.get(PropsKeys.GRADLE_PROJECT_TEMPLATE_TOKEN_FILE);

	public static final String[] GRADLE_VARIABLES = PropsUtil.getArray(
		PropsKeys.GRADLE_VARIABLES);

	public static final String[] IGNORED_FOLDERS = PropsUtil.getArray(
		PropsKeys.IGNORED_FOLDERS);

	public static final String[] PACKAGE_FILENAMES = PropsUtil.getArray(
		PropsKeys.PACKAGE_FILENAMES);

	public static final String[] PROJECTS = PropsUtil.getArray(
		PropsKeys.PROJECTS);

	public static final String SONATYPE_OSS_INDEX_API_TOKEN = PropsUtil.get(
		PropsKeys.SONATYPE_OSS_INDEX_API_TOKEN);

	public static final String SONATYPE_OSS_INDEX_USERNAME = PropsUtil.get(
		PropsKeys.SONATYPE_OSS_INDEX_USERNAME);

}