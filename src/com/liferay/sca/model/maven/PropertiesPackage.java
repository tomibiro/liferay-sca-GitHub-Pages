package com.liferay.sca.model.maven;

import com.liferay.sca.exception.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;

public class PropertiesPackage extends Package {

	public PropertiesPackage(File file) {
		try {
			Properties properties = new Properties();

			properties.load(new FileInputStream(file));

			for (Object valueyObj : properties.values()) {
				String value = (String)valueyObj;

				String[] parts = value.split(":");

				if (parts.length < 3) {
					ParseException pe = new ParseException(value, file);

					pe.printStackTrace();

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