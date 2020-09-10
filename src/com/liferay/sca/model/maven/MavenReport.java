package com.liferay.sca.model.maven;

import com.liferay.sca.comparator.MavenDependencyComparator;
import com.liferay.sca.exception.ParseException;
import com.liferay.sca.exception.ProjectNotConfiguredException;
import com.liferay.sca.util.FileUtil;
import com.liferay.sca.util.ProjectPropsUtil;
import com.liferay.sca.util.ProjectUtil;
import com.liferay.sca.util.PropsKeys;

import java.io.IOException;
import java.io.File;

import java.util.Set;
import java.util.TreeSet;

public class MavenReport {

	public static MavenReport load(File file) throws Exception {
		MavenReport report = new MavenReport();

		String content = FileUtil.read(file);

		String[] lines = content.split("\\n");

		for (String line : lines) {
			line = line.trim();

			String[] parts = line.split(":");

			if (parts.length != 3) {
				throw new ParseException(line, file);
			}

			MavenDependency dependency = new MavenDependency(
				parts[0], parts[1], parts[2]);

			report.add(dependency);
		}

		return report;
	}

	public static MavenReport load(String project) throws Exception {
		ProjectUtil.validate(project);

		File file = new File(
			ProjectPropsUtil.get(project, PropsKeys.DEPENDENCIES_REPORT));

		MavenReport report = load(file);

		report.setProject(project);

		return report;
	}

	public void add(MavenDependency dependency) {
		_dependencies.add(dependency);
	}

	public void add(Set<MavenDependency> dependencies) {
		_dependencies.addAll(dependencies);
	}

	public Set<MavenDependency> getDependencies() {
		return _dependencies;
	}

	public void save() throws IOException, ProjectNotConfiguredException {
		ProjectUtil.validate(_project);

		StringBuilder sb = new StringBuilder();

		for (MavenDependency dependency : _dependencies) {
			sb.append(dependency.toString());
			sb.append("\n");
		}

		FileUtil.write(
			sb.toString(),
			ProjectPropsUtil.get(_project, PropsKeys.DEPENDENCIES_REPORT));
	}

	public void setProject(String project) {
		_project = project;
	}

	Set<MavenDependency> _dependencies = new TreeSet<MavenDependency>(
		new MavenDependencyComparator());

	String _project;

}