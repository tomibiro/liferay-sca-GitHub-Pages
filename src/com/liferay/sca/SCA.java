package com.liferay.sca;

import com.liferay.sca.model.DependencySet;
import com.liferay.sca.util.ProjectUtil;
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
		ProjectUtil.validate(project);

		DependencySet dependencySet = DependencyFinder.find(project);

		MavenComboManifest.generate(dependencySet);
		NpmComboManifest.generate(dependencySet);

		if (PropsValues.SONATYPE_OSS_INDEX_ENABLED) {
			SonatypeOssIndex.generate(dependencySet);
		}

		GitHash.generate(project);
	}

}