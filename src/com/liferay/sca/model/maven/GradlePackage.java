package com.liferay.sca.model.maven;

import com.liferay.sca.exception.ParseException;
import com.liferay.sca.util.FileUtil;
import com.liferay.sca.util.PropsValues;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GradlePackage extends Package {

	public GradlePackage(String project, File file) {
		_parse(project, file);
	}

	private List<String> _findDependencySections(String content, File file) {
		List<String> dependencySections = new ArrayList<String>();

		int x = 0;

		while (true) {
			int y = content.indexOf(_DEPENDENCY_START, x);

			if (y < 0) {
				break;
			}

			y = y + _DEPENDENCY_START.length();

			int z = content.indexOf(_DEPENDENCY_END, y);

			if (z < 0) {
				ParseException pe = new ParseException(
					content.substring(y, y+80), file);

				pe.printStackTrace();

				break;
			}

			dependencySections.add(content.substring(y, z).trim());

			x = z;
		}

		return dependencySections;
	}

	private String _getProperty(String dependencyLine, String property, File file)
		throws ParseException {

		int x = dependencyLine.indexOf(property + ":");

		if (x < 0) {
			return null;
		}

		x = dependencyLine.indexOf("\"", x) + 1;

		int y = dependencyLine.indexOf("\"", x);

		if (y < 0) {
			throw new ParseException(dependencyLine, file);
		}

		return dependencyLine.substring(x, y);
	}

	private void _parse(String project, File file) {
		try {
			String content = FileUtil.read(file);

			content = _replaceVariables(project, content);

			List<String> dependencySections = _findDependencySections(
				content, file);

			for (String dependencySection : dependencySections) {
				_parseDependencySection(dependencySection, file);
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private void _parseDependencyLine(String line, File file) {
		line = line.replace('\'', '"');

		try {
			String group = _getProperty(line, "group", file);
			String artifact = _getProperty(line, "name", file);
			String version = _getProperty(line, "version", file);

			if ((group != null) && (artifact != null) && (version != null)) {
				addDependency(group, artifact, version);
			}
		}
		catch (ParseException pe) {
			pe.printStackTrace();
		}
	}

	private void _parseDependencySection(String section, File file) {
		String[] lines = section.split("\\n");

		for (String line : lines) {
			line = line.trim();

			if (line.equals("")) {
				continue;
			}

			_parseDependencyLine(line, file);
		}
	}

	private String _replaceVariables(String project, String content)
		throws IOException {

		// In file variables

		for (String variable : PropsValues.GRADLE_VARIABLES) {
			content = _replaceVariable(content, variable);
		}

		// ${foo} style variables

		while (true) {
			int x = content.indexOf("${");

			if (x < 0) {
				break;
			}

			int y = content.indexOf("}", x);

			if (y < 0) {
				break;
			}

			content = content.substring(0, x) + content.substring(x+2, y) + 
				content.substring(y+1);
		}

		// @foo@ style variables

		Map<String,String> projectTemplateTokens = getProjectTemplateTokens(
			project);

		for (String key : projectTemplateTokens.keySet()) {
			content = content.replaceAll(
				"@" + key + "@", projectTemplateTokens.get(key));
		}

		return content;
	}

	private String _replaceVariable(String content, String variable) {
		int x = content.indexOf("String " + variable + " =");

		if (x < 0) {
			return content;
		}

		int y = content.indexOf("\"", x);

		int z = content.indexOf("\"", y+1);

		String value = content.substring(y, z+1);

		return content.replaceAll(": " + variable, ": " + value);
	}

	private static final String _DEPENDENCY_END = "}";

	private static final String _DEPENDENCY_START = "dependencies {";

}