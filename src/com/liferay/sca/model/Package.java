package com.liferay.sca.model;

import com.liferay.sca.exception.UnknownPackageTypeException;
import com.liferay.sca.model.maven.GradlePackage;
import com.liferay.sca.model.maven.IvyPackage;
import com.liferay.sca.model.maven.PomPackage;
import com.liferay.sca.model.maven.PropertiesPackage;

import java.io.File;

import java.util.Set;

public abstract class Package {

	public static Package load(String project, File file)
		throws Exception {

		String path = file.getPath();

		if (path.endsWith(".gradle")) {
			return new GradlePackage(project, file);
		}

		if (path.endsWith("dependencies.properties")) {
			return new PropertiesPackage(file);
		}

		if (path.endsWith("ivy.xml")) {
			return new IvyPackage(file);
		}

		if (path.endsWith("pom.xml")) {
			return new PomPackage(project, file);
		}

		throw new UnknownPackageTypeException(path);
	}

	public abstract Set<Dependency> getDependencies();

}