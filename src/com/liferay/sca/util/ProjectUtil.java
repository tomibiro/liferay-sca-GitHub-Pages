package com.liferay.sca.util;

import com.liferay.sca.exception.ProjectNotConfiguredException;

public class ProjectUtil {

	public static void validate(String project)
		throws ProjectNotConfiguredException {

		if (!ArrayUtil.contains(PropsValues.PROJECTS, project)) {
			throw new ProjectNotConfiguredException(project);
		}
	}

}