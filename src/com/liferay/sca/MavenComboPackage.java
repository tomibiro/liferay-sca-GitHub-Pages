package com.liferay.sca;

import com.liferay.sca.model.DependencySet;
import com.liferay.sca.model.maven.MavenDependency;
import com.liferay.sca.model.maven.MavenReport;
import com.liferay.sca.util.FileUtil;
import com.liferay.sca.util.ProjectPropsUtil;
import com.liferay.sca.util.ProjectUtil;
import com.liferay.sca.util.PropsKeys;
import com.liferay.sca.util.PropsValues;

import java.io.IOException;

import java.util.Set;

public class MavenComboPackage {

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			generate();
		}
		else {
			generate(args[0]);
		}
	}

	public static void generate() throws Exception {
		for (String project : PropsValues.PROJECTS) {
			generate(project);
		}
	}

	public static void generate(String project) throws Exception {
		ProjectUtil.validate(project);

		StringBuilder sb = new StringBuilder();

		_addHeader(sb, project);

		MavenReport report = MavenReport.load(project);

		Set<MavenDependency> dependencies = report.getDependencies();

		for (MavenDependency dependency : dependencies) {
			if (ignore(dependency)) {
				continue;
			}

			_addDependency(sb, dependency);
		}

		_addFooter(sb);

		save(project, sb.toString());
	}

	public static void generate(String project, DependencySet dependencySet) throws Exception {
		ProjectUtil.validate(project);

		StringBuilder sb = new StringBuilder();

		_addHeader(sb, project);

		for (MavenDependency dependency : dependencySet.mavenDependencies()) {
			if (ignore(dependency)) {
				continue;
			}

			_addDependency(sb, dependency);
		}

		_addFooter(sb);

		save(project, sb.toString());
	}

	protected static boolean ignore(MavenDependency dependency) {
		String version = dependency.getVersion();

		if (version.equals("default")) {
			return true;
		}

		return false;
	}

	protected static void save(String project, String content) throws IOException {
		String folder = ProjectPropsUtil.get(
			project, PropsKeys.DEPENDABOT_PACKAGE_DIR);

		String path = folder + "/pom.xml";

		FileUtil.write(content, path);
	}

	private static void _addDependency(
		StringBuilder sb, MavenDependency dependency) {

		sb.append("\t\t<dependency>\n");
		sb.append("\t\t\t<groupId>");
		sb.append(dependency.getGroup());
		sb.append("</groupId>\n");
		sb.append("\t\t\t<artifactId>");
		sb.append(dependency.getArtifact());
		sb.append("</artifactId>\n");
		sb.append("\t\t\t<version>");
		sb.append(dependency.getVersion());
		sb.append("</version>\n");
		sb.append("\t\t</dependency>\n");
	}

	private static void _addFooter(StringBuilder sb) {
		sb.append("\t</dependencies>\n");
		sb.append("</project>");
	}

	private static void _addHeader(StringBuilder sb, String project) {
		sb.append("<?xml version=\"1.0\"?>\n\n");
		sb.append("<project\n");
		sb.append("\txmlns=\"http://maven.apache.org/POM/4.0.0\"\n");
		sb.append(
			"\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		sb.append(
			"\txsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 " +
				"http://maven.apache.org/xsd/maven-4.0.0.xsd\"\n");
		sb.append(">\n");
		sb.append("\t<groupId>com.liferay</groupId>\n");
		sb.append("\t<artifactId>");
		sb.append(project);
		sb.append("</artifactId>\n");
		sb.append("\t<version>0.0.1</version>\n");
		sb.append("\t<dependencies>\n");
	}

}