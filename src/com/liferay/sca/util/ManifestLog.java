package com.liferay.sca.util;

import com.liferay.sca.model.Dependency;

import java.io.File;
import java.io.IOException;

import java.util.Set;

public class ManifestLog {

	public ManifestLog(String project) {
		_project = project;
		_sb = new StringBuilder();
	}

	public void log(Dependency dependency) {
		_sb.append("    ");
		_sb.append(dependency.toString());
		_sb.append("\n");
	}

	public void log(File manifestFile) {
		_sb.append(manifestFile.getAbsolutePath());
		_sb.append("\n");
	}

	public void log(Set<Dependency> dependencies) {
		for (Dependency dependency : dependencies) {
			log(dependency);
		}
	}

	public void save() throws IOException {
		String folder = ProjectPropsUtil.get(
			_project, PropsKeys.DEPENDABOT_PACKAGE_DIR);

		String path = folder + "/manifest.log";

		FileUtil.write(_sb.toString(), path);

	}

	String _project;
	StringBuilder _sb;

}