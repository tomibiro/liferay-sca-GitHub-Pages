package com.liferay.sca;

import com.liferay.sca.exception.ProjectNotConfiguredException;
import com.liferay.sca.model.DependencySet;
import com.liferay.sca.util.ArrayUtil;
import com.liferay.sca.util.PropsValues;

public class SCA {

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			run();
		}
		else {
			run(args[0]);
		}
	}

	public static void run() throws Exception {
		for (String project : PropsValues.PROJECTS) {
			run(project);
		}
	}

	public static void run(String project) throws Exception {
		validateProject(project);

		DependencySet dependencySet = DependencyFinder.find(project);

		MavenComboPackage.generate(dependencySet);
		NpmComboPackage.generate(dependencySet);
	}

	protected static void validateProject(String project)
		throws ProjectNotConfiguredException {

		if (!ArrayUtil.contains(PropsValues.PROJECTS, project)) {
			throw new ProjectNotConfiguredException(project);
		}
	}

}