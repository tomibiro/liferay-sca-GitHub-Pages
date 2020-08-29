package com.liferay.sca.model;

import com.liferay.sca.comparator.DependencyComparator;
import com.liferay.sca.exception.ProjectNotConfiguredException;
import com.liferay.sca.util.FileUtil;
import com.liferay.sca.util.ProjectUtil;
import com.liferay.sca.util.PropsKeys;
import com.liferay.sca.util.PropsUtil;

import java.io.IOException;

import java.util.Set;
import java.util.TreeSet;

public class Report {

	public void add(Set<Dependency> dependencies) {
		_dependencies.addAll(dependencies);
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