package com.liferay.sca.model;

import com.liferay.sca.exception.ParseException;
import com.liferay.sca.util.FileUtil;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class GradlePackage extends Package {

	public GradlePackage(File file) {
		_parse(file);
	}

	private List<String> _findDependencySections(String content) {
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
				System.err.println(
					"Parse error near: " + content.substring(y, y+80));

				break;
			}

			dependencySections.add(content.substring(y, z).trim());

			x = z;
		}

		return dependencySections;
	}

	private String _getProperty(String dependencyLine, String property)
		throws ParseException {

		int x = dependencyLine.indexOf(property + ":");

		if (x < 0) {
			throw new ParseException(
				"No " + property + " defined: " + dependencyLine);
		}

		x = dependencyLine.indexOf("\"", x) + 1;

		int y = dependencyLine.indexOf("\"", x);

		if (y < 0) {
			throw new ParseException("Near: " + dependencyLine);
		}

		return dependencyLine.substring(x, y);
	}

	private boolean _ignore(String line) {
		if (line.indexOf("fileTree(") >= 0) {
			return true;
		}

		if (line.indexOf("if (") >= 0) {
			return true;
		}

		if (line.indexOf("project (") >= 0) {
			return true;
		}

		if (line.indexOf("project(") >= 0) {
			return true;
		}

		if (line.indexOf("rootProject.files(") >= 0) {
			return true;
		}

		return false;
	}

	private void _parse(File file) {
		try {
			String content = FileUtil.read(file);

			List<String> dependencySections = _findDependencySections(content);

			for (String dependencySection : dependencySections) {
				_parseDependencySection(dependencySection);
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private void _parseDependencyLine(String line) {
		if (_ignore(line)) {
			return;
		}

		try {
			String group = _getProperty(line, "group");
			String artifact = _getProperty(line, "name");
			String version = _getProperty(line, "version");

			addDependency(group, artifact, version);
		}
		catch (ParseException pe) {
			System.err.println(pe.getMessage());
		}
	}

	private void _parseDependencySection(String section) {
		String[] lines = section.split("\\n");

		for (String line : lines) {
			line = line.trim();

			if (line.equals("")) {
				continue;
			}

			_parseDependencyLine(line);
		}
	}

	private static final String _DEPENDENCY_END = "}";

	private static final String _DEPENDENCY_START = "dependencies {";

}