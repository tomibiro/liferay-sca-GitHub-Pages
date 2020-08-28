package com.liferay.sca.model;

import com.liferay.sca.util.FileUtil;

import java.io.File;
import java.io.IOException;

public class PropertiesPackage extends Package {

	public PropertiesPackage(File file) {
		_parse(file);
	}

	private void _parse(File file) {
		try {
			String content = FileUtil.read(file);

			String[] lines = content.split("\\n");

			for (String line : lines) {
				line = line.trim();

				int x = line.indexOf("=");

				if (x < 0) {
					continue;
				}

				String dependencyString = line.substring(x+1);

				String[] parts = dependencyString.split(":");

				if (parts.length <3) {
					System.err.println("Parse error near: " + line);

					continue;
				}

				addDependency(parts[0], parts[1], parts[2]);
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}