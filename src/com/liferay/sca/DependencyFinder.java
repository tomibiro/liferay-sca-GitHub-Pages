package com.liferay.sca;

import com.liferay.sca.exception.BlankXmlException;
import com.liferay.sca.model.DependencySet;
import com.liferay.sca.model.Manifest;
import com.liferay.sca.util.ArrayUtil;
import com.liferay.sca.util.ManifestLog;
import com.liferay.sca.util.ProjectPropsUtil;
import com.liferay.sca.util.PropsKeys;
import com.liferay.sca.util.PropsValues;

import java.io.File;

import java.util.HashSet;
import java.util.Set;

public class DependencyFinder {

	public static DependencySet find(String project) throws Exception {
		DependencySet dependencySet = new DependencySet(project);

		ManifestLog log = new ManifestLog(project);

		File srcCodeFile = new File(
			ProjectPropsUtil.get(project, PropsKeys.SRC_CODE));

		Set<File> manifestFiles = findManifestFiles(srcCodeFile);

		for (File file : manifestFiles) {
			log.log(file);

			try {
				Manifest manifestObj = Manifest.load(project, file);

				log.log(manifestObj.getDependencies());

				dependencySet.addAll(manifestObj.getDependencies());
			}
			catch (BlankXmlException bxe) {
			}
		}

		log.save();

		return dependencySet;
	}

	protected static Set<File> findManifestFiles(File srcCodeFile) {
		Set<File> manifestFiles = new HashSet<File>();

		_findManifestFiles(srcCodeFile, manifestFiles);

		return manifestFiles;
	}

	private static void _findManifestFiles(File folder, Set<File> manifestFiles) {
		File[] files = folder.listFiles();

		for (File file : files) {
			if (file.isDirectory()) {
				boolean isIgnoredFolder = ArrayUtil.contains(
					PropsValues.IGNORED_FOLDERS, file.getName());

				if (!isIgnoredFolder) {
					_findManifestFiles(file, manifestFiles);
				}

				continue;
			}

			String absolutePath = file.getAbsolutePath();

			absolutePath = absolutePath.replace('\\', '/');

			for (String packageFilename : PropsValues.MANIFEST_FILENAMES) {
				if (absolutePath.endsWith(packageFilename)) {
					manifestFiles.add(file);
				}
			}
		}
	}

}