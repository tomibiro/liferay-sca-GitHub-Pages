package com.liferay.sca.exception;

public class ProjectNotConfiguredException extends Exception {

	public ProjectNotConfiguredException(String projectName) {
		super("Project " + projectName + " has not been configured");
	}

}