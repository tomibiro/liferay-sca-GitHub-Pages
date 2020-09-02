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

	private String _findDependenciesSection(String content, File file)
		throws ParseException {

		int x = content.indexOf(_DEPENDENCIES_START);

		if (x < 0) {
			return "";
		}

		int y = content.indexOf(">", x);

		y = y + 1;

		int z = content.indexOf(_DEPENDENCIES_END, y);

		if (z < 0) {
			throw new ParseException(content.substring(y, y+80), file);
		}

		return content.substring(y, z);
	}

	private List<String> _findDependencyXmls(
			String dependenciesSection, File file)
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
					dependenciesSection.substring(y, y+80), file);
			}

			z = z + _DEPENDENCY_END.length();

			dependencyXmls.add(dependenciesSection.substring(y, z));

			x = z;
		}

		return dependencyXmls;
	}

	private String _getAttribute(
			String dependencyXml, String attribute, File file)
		throws ParseException {

		String attributeStart = attribute + "=\"";

		int x = dependencyXml.indexOf(attributeStart);

		if (x < 0) {
			throw new ParseException(dependencyXml, file);
		}

		x = x + attributeStart.length();

		int y = dependencyXml.indexOf("\"", x);

		if (y < 0) {
			throw new ParseException(dependencyXml, file);
		}

		return dependencyXml.substring(x, y);
	}

	private void _parse(File file) {
		try {
			String content = FileUtil.read(file);

			String dependenciesSection = _findDependenciesSection(content, file);

			List<String> dependencyXmls = _findDependencyXmls(
				dependenciesSection, file);

			for (String dependencyXml : dependencyXmls) {
				_parseDependencyXml(dependencyXml, file);
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		catch (ParseException pe) {
			pe.printStackTrace();
		}
	}

	private void _parseDependencyXml(String dependencyXml, File file) {
		try {
			String name = _getAttribute(dependencyXml, "name", file);
			String org = _getAttribute(dependencyXml, "org", file);
			String rev = _getAttribute(dependencyXml, "rev", file);

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