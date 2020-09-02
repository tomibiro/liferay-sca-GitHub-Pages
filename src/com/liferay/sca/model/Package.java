package com.liferay.sca.model;

import com.liferay.sca.exception.UnknownPackageTypeException;

import java.io.File;

import java.util.HashSet;
import java.util.Set;

public abstract class Package {

	public static Package load(File file) throws Exception {
		String path = file.getPath();

		if (path.endsWith(".gradle")) {
			return new GradlePackage(file);
		}

		if (path.endsWith("dependencies.properties")) {
			return new PropertiesPackage(file);
		}

		if (path.endsWith("ivy.xml")) {
			return new IvyPackage(file);
		}

		throw new UnknownPackageTypeException(path);
	}

	public void addDependency(Dependency dependency) {
		_dependencies.add(dependency);
	}

	public void addDependency(String group, String artifact, String version) {
		addDependency(new Dependency(group, artifact, version));
	}

	public Set<Dependency> getDependencies() {
		return _dependencies;
	}

	private Set<Dependency> _dependencies = new HashSet<Dependency>();

}