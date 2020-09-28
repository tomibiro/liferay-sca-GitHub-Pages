package com.liferay.sca;

import com.liferay.sca.util.FileUtil;
import com.liferay.sca.util.ProjectPropsUtil;
import com.liferay.sca.util.PropsKeys;

import java.io.File;
import java.io.IOException;

public class GitHash {

	public static void generate(String project) throws IOException {
		String packRef = ProjectPropsUtil.get(project, PropsKeys.GIT_PACK_REF);

		String srcCodeFolder = ProjectPropsUtil.get(
			project, PropsKeys.SRC_CODE);

		File packedRefsFile = new File(srcCodeFolder + "/.git/packed-refs");

		String content = FileUtil.read(packedRefsFile);

		String[] lines = content.split("\\n");

		for (String line : lines) {
			line = line.trim();

			if (!line.endsWith(packRef)) {
				continue;
			}

			int x = line.indexOf(" ");

			String gitHash = line.substring(0, x);

			save(project, gitHash);

			break;
		}
	}

	public static void save(String project, String gitHash) throws IOException {
		String folder = ProjectPropsUtil.get(
			project, PropsKeys.DEPENDABOT_PACKAGE_DIR);

		String filename = folder + "/git-hash.txt";

		FileUtil.write(gitHash, filename);
	}

}