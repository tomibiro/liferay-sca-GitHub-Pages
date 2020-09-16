package com.liferay.sca.model.npm;

import com.liferay.sca.util.FileUtil;

import java.io.File;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PackageJsonPackage extends NpmPackage {

	public PackageJsonPackage(String project, File file) throws IOException {
		String content = FileUtil.read(file);

		JSONParser parser = new JSONParser();

		try {
			JSONObject packageJson = (JSONObject)parser.parse(content);

			JSONObject dependenciesJson = (JSONObject)packageJson.get(
				"dependencies");

			if (dependenciesJson == null) {
				return;
			}

			for (Object key : dependenciesJson.keySet()) {
				Object value = dependenciesJson.get(key);

				String pkg = (String)key;
				String version = (String)value;

				addDependency(pkg, version);
			}
		}
		catch (ParseException pe) {
			StringBuilder sb = new StringBuilder();

			sb.append(pe.getClass().getName());
			sb.append(": ");
			sb.append(pe.toString());
			sb.append(" ");
			sb.append(file.getPath());

			System.err.println(sb.toString());
		}
	}

}