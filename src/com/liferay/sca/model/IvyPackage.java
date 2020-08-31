package com.liferay.sca.model;

import com.liferay.sca.exception.ParseException;
import com.liferay.sca.util.FileUtil;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class IvyPackage extends Package {

	public IvyPackage(File file) {
		_parse(file);
	}

	private String _findDependenciesSection(String content)
		throws ParseException {

		int x = content.indexOf(_DEPENDENCIES_START);

		if (x < 0) {
			return "";
		}

		int y = content.indexOf(">", x);

		y = y + 1;

		int z = content.indexOf(_DEPENDENCIES_END, y);

		if (z < 0) {
			throw new ParseException(content.substring(y, y+80));
		}

		return content.substring(y, z);
	}

	private List<String> _findDependencyXmls(String dependenciesSection)
		throws ParseException {

		List<String> dependencyXmls = new ArrayList<String>();

		int x = 0;

		while (true) {
			int y = dependenciesSection.indexOf(_DEPENDENCY_START, x);

			if (y < 0) {
				break;
			}

			int z = dependenciesSection.indexOf(_DEPENDENCY_END, y);

			if (z < 0) {
				throw new ParseException(
					dependenciesSection.substring(y, y+80));
			}

			z = z + _DEPENDENCY_END.length();

			dependencyXmls.add(dependenciesSection.substring(y, z));

			x = z;
		}

		return dependencyXmls;
	}

	private String _getAttribute(String dependencyXml, String attribute)
			throws ParseException {

			String attributeStart = attribute + "=\"";

			int x = dependencyXml.indexOf(attributeStart);

			if (x < 0) {
				throw new ParseException(
					"No " + attribute + " defined: " + dependencyXml);
			}

			x = x + attributeStart.length();

			int y = dependencyXml.indexOf("\"", x);

			if (y < 0) {
				throw new ParseException("Near: " + dependencyXml);
			}

			return dependencyXml.substring(x, y);
		}

	private void _parse(File file) {
		try {
			String content = FileUtil.read(file);

			String dependenciesSection = _findDependenciesSection(content);

			List<String> dependencyXmls = _findDependencyXmls(
				dependenciesSection);

			for (String dependencyXml : dependencyXmls) {
				_parseDependencyXml(dependencyXml);
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		catch (ParseException pe) {
			pe.printStackTrace();
		}
	}

	private void _parseDependencyXml(String dependencyXml) {
		try {
			String name = _getAttribute(dependencyXml, "name");
			String org = _getAttribute(dependencyXml, "org");
			String rev = _getAttribute(dependencyXml, "rev");

			addDependency(org, name, rev);
		}
		catch (ParseException pe) {
			pe.printStackTrace();
		}
	}

	private static final String _DEPENDENCIES_END = "</dependencies>";

	private static final String _DEPENDENCY_END = "/>";

	private static final String _DEPENDENCIES_START = "<dependencies";

	private static final String _DEPENDENCY_START = "<dependency";

}