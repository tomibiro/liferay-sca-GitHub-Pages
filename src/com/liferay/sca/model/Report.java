package com.liferay.sca.model;

import com.liferay.sca.comparator.DependencyComparator;
import com.liferay.sca.exception.ParseException;
import com.liferay.sca.exception.ProjectNotConfiguredException;
import com.liferay.sca.util.FileUtil;
import com.liferay.sca.util.ProjectUtil;
import com.liferay.sca.util.PropsKeys;
import com.liferay.sca.util.PropsUtil;

import java.io.IOException;
import java.io.File;

import java.util.Set;
import java.util.TreeSet;

public class Report {

	public static Report load(File file) throws Exception {
		Report report = new Report();

		String content = FileUtil.read(file);

		String[] lines = content.split("\\n");

		for (String line : lines) {
			line = line.trim();

			String[] parts = line.split(":");

			if (parts.length != 3) {
				throw new ParseException(line);
			}

			Dependency dependency = new Dependency(
				parts[0], parts[1], parts[2]);

			report.add(dependency);
		}

		return report;
	}

	public static Report load(String project) throws Exception {
		ProjectUtil.validate(project);

		File file = new File(
			PropsUtil.get(project + "." + PropsKeys.DEPENDENCIES_REPORT));

		Report report = load(file);

		report.setProject(project);

		return report;
	}

	public void add(Dependency dependency) {
		_dependencies.add(dependency);
	}

	public void add(Set<Dependency> dependencies) {
		_dependencies.addAll(dependencies);
	}

	public Set<Dependency> getDependencies() {
		return _dependencies;
	}

	public void save() throws IOException, ProjectNotConfiguredException {
		ProjectUtil.validate(_project);

		StringBuilder sb = new StringBuilder();

		for (Dependency dependency : _dependencies) {
			sb.append(dependency.toString());
			sb.append("\n");
		}

		FileUtil.write(
			sb.toString(),
			PropsUtil.get(_project + "." + PropsKeys.DEPENDENCIES_REPORT));
	}

	public void setProject(String project) {
		_project = project;
	}

	Set<Dependency> _dependencies = new TreeSet<Dependency>(
		new DependencyComparator());

	String _project;

}