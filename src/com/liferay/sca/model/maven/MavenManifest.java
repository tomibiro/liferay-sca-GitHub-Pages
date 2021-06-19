package com.liferay.sca.model.maven;

import com.liferay.sca.exception.ParseException;
import com.liferay.sca.model.Dependency;
import com.liferay.sca.model.Manifest;
import com.liferay.sca.util.FileUtil;
import com.liferay.sca.util.ProjectPropsUtil;
import com.liferay.sca.util.PropsKeys;
import com.liferay.sca.util.PropsValues;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class MavenManifest extends Manifest {

	public void addDependency(MavenDependency dependency) {
		_dependencies.add(dependency);
	}

	public void addDependency(String group, String artifact, String version) {
		addDependency(new MavenDependency(group, artifact, version));
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

		try {
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
		catch (FileNotFoundException fnfe) {
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