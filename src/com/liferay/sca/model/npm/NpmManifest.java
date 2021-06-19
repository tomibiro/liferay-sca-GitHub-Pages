package com.liferay.sca.model.npm;

import com.liferay.sca.model.Dependency;
import com.liferay.sca.model.Manifest;

import java.util.HashSet;
import java.util.Set;

public abstract class NpmManifest extends Manifest {

	public void addDependency(NpmDependency dependency) {
		_dependencies.add(dependency);
	}

	public void addDependency(String pkg, String version) {
		addDependency(new NpmDependency(pkg, version));
	}

	public Set<Dependency> getDependencies() {
		return _dependencies;
	}

	private Set<Dependency> _dependencies = new HashSet<Dependency>();

}