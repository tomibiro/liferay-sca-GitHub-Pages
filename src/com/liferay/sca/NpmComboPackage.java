package com.liferay.sca;

import com.liferay.sca.model.DependencySet;
import com.liferay.sca.model.npm.NpmDependency;
import com.liferay.sca.util.FileUtil;
import com.liferay.sca.util.ProjectPropsUtil;
import com.liferay.sca.util.PropsKeys;

import java.io.IOException;

public class NpmComboPackage {

	public static void generate(DependencySet dependencySet)
		throws IOException {

		StringBuilder sb = new StringBuilder();

		_addHeader(sb);

		for (NpmDependency dependency : dependencySet.getNpmDependencies()) {
			_addDependency(sb, dependency);
		}

		sb.delete(sb.length() - 2, sb.length());

		sb.append("\n");

		_addFooter(sb, dependencySet.getProject());

		save(dependencySet.getProject(), sb.toString());
	}

	protected static void save(String project, String content)
		throws IOException {

		String folder = ProjectPropsUtil.get(
			project, PropsKeys.DEPENDABOT_PACKAGE_DIR);

		String path = folder + "/package.json";

		FileUtil.write(content, path);
	}

	private static void _addDependency(
		StringBuilder sb, NpmDependency dependency) {

		sb.append("\t\t\"");
		sb.append(dependency.getPackage());
		sb.append("\": \"");
		sb.append(dependency.getVersion());
		sb.append("\",\n");
	}

	private static void _addFooter(StringBuilder sb, String project) {
		sb.append("\t},\n");
		sb.append("\t\"name\": \"");
		sb.append(project);
		sb.append("\",\n");
		sb.append("\t\"version\": \"0.0.1\"\n");
		sb.append("}");
	}

	private static void _addHeader(StringBuilder sb) {
		sb.append("{\n");
		sb.append("\t\"dependencies\": {\n");
	}

}