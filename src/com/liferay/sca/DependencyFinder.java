package com.liferay.sca;

import com.liferay.sca.exception.ProjectNotConfiguredException;
import com.liferay.sca.util.ArrayUtil;
import com.liferay.sca.util.PropsKeys;
import com.liferay.sca.util.PropsUtil;
import com.liferay.sca.util.PropsValues;

import java.io.File;

import java.util.HashSet;
import java.util.Set;

public class DependencyFinder {

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			find();
		}
		else {
			find(args[0]);
		}
	}

	public static void find() throws ProjectNotConfiguredException {
		for (String project : PropsValues.PROJECTS) {
			find(project);
		}
	}

	public static void find(String project)
		throws ProjectNotConfiguredException {

		if (!ArrayUtil.contains(PropsValues.PROJECTS, project)) {
			throw new ProjectNotConfiguredException(project);
		}

		File srcCodeFile = new File(
			PropsUtil.get(project + "." + PropsKeys.SRC_CODE));

		Set<File> packageFiles = findPackageFiles(srcCodeFile);

		for (File file : packageFiles) {
			System.out.println(file.getPath());
		}
	}

	protected static Set<File> findPackageFiles(File srcCodeFile) {
		Set<File> packageFiles = new HashSet<File>();

		_findPackageFiles(srcCodeFile, packageFiles);

		return packageFiles;
	}

	private static void _findPackageFiles(File folder, Set<File> packageFiles) {
		File[] files = folder.listFiles();

		for (File file : files) {
			if (file.isDirectory()) {
				_findPackageFiles(file, packageFiles);

				continue;
			}

			String absolutePath = file.getAbsolutePath();

			absolutePath = absolutePath.replace('\\', '/');

			for (String packageFilename : PropsValues.PACKAGE_FILENAMES) {
				if (absolutePath.endsWith(packageFilename)) {
					packageFiles.add(file);
				}
			}
		}
	}

}