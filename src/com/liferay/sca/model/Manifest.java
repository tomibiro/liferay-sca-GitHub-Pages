package com.liferay.sca.model;

import com.liferay.sca.exception.UnknownManifestTypeException;
import com.liferay.sca.model.maven.GradleManifest;
import com.liferay.sca.model.maven.IvyManifest;
import com.liferay.sca.model.maven.PomManifest;
import com.liferay.sca.model.maven.PropertiesManifest;
import com.liferay.sca.model.npm.PackageJsonManifest;

import java.io.File;

import java.util.Set;

public abstract class Manifest {

	public static Manifest load(String project, File file)
		throws Exception {

		String path = file.getPath();

		if (path.endsWith(".gradle")) {
			return new GradleManifest(project, file);
		}

		if (path.endsWith("dependencies.properties")) {
			return new PropertiesManifest(file);
		}

		if (path.endsWith("ivy.xml")) {
			return new IvyManifest(file);
		}

		if (path.endsWith("package.json")) {
			return new PackageJsonManifest(project, file);
		}

		if (path.endsWith("pom.xml")) {
			return new PomManifest(project, file);
		}

		throw new UnknownManifestTypeException(path);
	}

	public abstract Set<Dependency> getDependencies();

}