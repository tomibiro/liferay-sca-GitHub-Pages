package com.liferay.sca.model;

import com.liferay.sca.comparator.MavenDependencyComparator;
import com.liferay.sca.model.maven.MavenDependency;

import java.util.Set;
import java.util.TreeSet;

public class DependencySet {

	public  boolean add(MavenDependency dependency) {
		return _mavenDependencies.add((MavenDependency)dependency);
	}

	public  boolean addAll(Set<MavenDependency> dependencies) {
		return _mavenDependencies.addAll(dependencies);
	}

	public Set<MavenDependency> mavenDependencies() {
		return _mavenDependencies;
	}

	private Set<MavenDependency> _mavenDependencies =
		new TreeSet<MavenDependency>(new MavenDependencyComparator());

}