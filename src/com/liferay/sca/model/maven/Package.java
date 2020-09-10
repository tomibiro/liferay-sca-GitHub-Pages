package com.liferay.sca.model.maven;

import com.liferay.sca.exception.ParseException;
import com.liferay.sca.exception.UnknownPackageTypeException;
import com.liferay.sca.util.FileUtil;
import com.liferay.sca.util.ProjectPropsUtil;
import com.liferay.sca.util.PropsKeys;
import com.liferay.sca.util.PropsValues;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Package {

	public static Package load(String project, File file) throws Exception {
		getProjectTemplateTokens(project);

		String path = file.getPath();

		if (path.endsWith(".gradle")) {
			return new GradlePackage(project, file);
		}

		if (path.endsWith("dependencies.properties")) {
			return new PropertiesPackage(file);
		}

		if (path.endsWith("ivy.xml")) {
			return new IvyPackage(file);
		}

		/*if (path.endsWith("package.json") ||
			path.endsWith("package-lock.json")) {

			return new NpmPackage(project, file);
		}*/

		if (path.endsWith("pom.xml")) {
			return new PomPackage(project, file);
		}

		throw new UnknownPackageTypeException(path);
	}

	public void addDependency(Dependency dependency) {
		_dependencies.add(dependency);
	}

	public void addDependency(String group, String artifact, String version) {
		addDependency(new Dependency(group, artifact, version));
	}

	public Set<Dependency> getDependencies() {
		return _dependencies;
	}

	public static Map<String,String> getProjectTemplateTokens(String project)
		throws IOException {

		if (_PROJECT_TEMPLATE_TOKENS != null) {
			return _PROJECT_TEMPLATE_TOKENS;
		}

		_PROJECT_TEMPLATE_TOKENS = new HashMap<String, String>();

		File projectTemplateTokensFile = new File(
			ProjectPropsUtil.get(project, PropsKeys.SRC_CODE),
			PropsValues.GRADLE_PROJECT_TEMPLATE_TOKEN_FILE);

		String content = FileUtil.read(projectTemplateTokensFile);

		int x = content.indexOf(_PROJECT_TEMPLATE_TOKEN_START);

		if (x < 0) {
			return _PROJECT_TEMPLATE_TOKENS;
		}

		x = x + _PROJECT_TEMPLATE_TOKEN_START.length();

		int y = content.indexOf(_PROJECT_TEMPLATE_TOKEN_END, x);

		if (y < 0) {
			ParseException pe = new ParseException(
				content.substring(x, x+80), projectTemplateTokensFile);

			pe.printStackTrace();

			return _PROJECT_TEMPLATE_TOKENS;
		}

		String[] tokenLineList = content.substring(x, y).split(",");

		for (String tokenLine : tokenLineList) {
			String[] parts = tokenLine.split(":");

			parts[0] = _removeQuotes(parts[0]);
			parts[1] = _removeQuotes(parts[1]);

			_PROJECT_TEMPLATE_TOKENS.put(parts[0], parts[1]);
		}

		return _PROJECT_TEMPLATE_TOKENS;
	}

	private static String _removeQuotes(String str) {
		int x = str.indexOf("\"");

		if (x < 0) {
			return str;
		}

		int y = str.lastIndexOf("\"");

		if (y < 0) {
			return str;
		}

		return str.substring(x+1, y);
	}

	private static final String _PROJECT_TEMPLATE_TOKEN_END = "]";

	private static final String _PROJECT_TEMPLATE_TOKEN_START = 
		"projectTemplateTokens = [";

	private static Map<String, String> _PROJECT_TEMPLATE_TOKENS = null;

	private Set<Dependency> _dependencies = new HashSet<Dependency>();

}