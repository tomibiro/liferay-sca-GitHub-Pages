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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DependencyFinder {

	public static DependencySet find(String project) throws Exception {
		DependencySet dependencySet = new DependencySet(project);

		ManifestLog log = new ManifestLog(project);

		File srcCodeFile = new File(
			ProjectPropsUtil.get(project, PropsKeys.SRC_CODE));

		Set<File> manifestFiles = findManifestFiles(project, srcCodeFile);

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

	protected static Set<File> findManifestFiles(
		String project, File srcCodeFile) {

		Set<File> manifestFiles = new HashSet<File>();

		_findManifestFiles(project, srcCodeFile, manifestFiles);

		return manifestFiles;
	}

	private static void _findManifestFiles(
		String project, File folder, Set<File> manifestFiles) {

		File[] files = folder.listFiles();

		for (File file : files) {
			if (file.isDirectory()) {
				List<String> ignoredFolders = _getIgnoredFolders(project);

				boolean isIgnoredFolder = ignoredFolders.contains(
					file.getName());

				if (!isIgnoredFolder) {
					_findManifestFiles(project, file, manifestFiles);
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

	private static List<String> _getIgnoredFolders(String project) {
		if (_ignoredFolders.containsKey(project)) {
			return _ignoredFolders.get(project);
		}

		List<String> ignoredFolders = new ArrayList<String>();

		for (String folder : PropsValues.IGNORED_FOLDERS) {
			ignoredFolders.add(folder);
		}

		String[] projectIgnoredFolders = ProjectPropsUtil.getArray(
			project, PropsKeys.IGNORED_FOLDERS);

		for (String folder : projectIgnoredFolders) {
			ignoredFolders.add(folder);
		}

		_ignoredFolders.put(project, ignoredFolders);

		return ignoredFolders;
	}

	private static Map<String,List<String>>_ignoredFolders =
		new HashMap<String,List<String>>();

}