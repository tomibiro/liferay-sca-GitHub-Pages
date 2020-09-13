package com.liferay.sca.model;

import com.liferay.sca.comparator.MavenDependencyComparator;
import com.liferay.sca.model.maven.MavenDependency;

import java.util.Set;
import java.util.TreeSet;

public class DependencySet {

	public DependencySet() {
	}

	public DependencySet(String project) {
		setProject(project);
	}

	public  boolean add(Dependency dependency) {
		if (dependency instanceof MavenDependency) {
			return _mavenDependencies.add((MavenDependency)dependency);
		}

		return false;
	}

	public  boolean addAll(Set<Dependency> dependencies) {
		boolean isChanged = false;

		for (Dependency dependency : dependencies) {
			boolean result = add(dependency);

			isChanged = isChanged || result;
		}

		return isChanged;
	}

	public Set<MavenDependency> getMavenDependencies() {
		return _mavenDependencies;
	}

	public String getProject() {
		return _project;
	}

	public void setProject(String project) {
		_project = project;
	}

	private Set<MavenDependency> _mavenDependencies =
		new TreeSet<MavenDependency>(new MavenDependencyComparator());

	private String _project = null;

}