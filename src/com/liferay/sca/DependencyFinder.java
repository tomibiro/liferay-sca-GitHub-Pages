package com.liferay.sca;

import com.liferay.sca.exception.BlankXmlException;
import com.liferay.sca.model.maven.Package;
import com.liferay.sca.model.maven.Report;
import com.liferay.sca.util.ArrayUtil;
import com.liferay.sca.util.ProjectPropsUtil;
import com.liferay.sca.util.ProjectUtil;
import com.liferay.sca.util.PropsKeys;
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

	public static void find() throws Exception {
		for (String project : PropsValues.PROJECTS) {
			find(project);
		}
	}

	public static void find(String project) throws Exception {
		ProjectUtil.validate(project);

		Report report = new Report();

		report.setProject(project);

		File srcCodeFile = new File(
			ProjectPropsUtil.get(project, PropsKeys.SRC_CODE));

		Set<File> packageFiles = findPackageFiles(srcCodeFile);

		for (File file : packageFiles) {
			try {
				Package packageObj = Package.load(project, file);

				report.add(packageObj.getDependencies());
			}
			catch (BlankXmlException bxe) {
			}
		}

		report.save();
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
				boolean isIgnoredFolder = ArrayUtil.contains(
					PropsValues.IGNORED_FOLDERS, file.getName());

				if (!isIgnoredFolder) {
					_findPackageFiles(file, packageFiles);
				}

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