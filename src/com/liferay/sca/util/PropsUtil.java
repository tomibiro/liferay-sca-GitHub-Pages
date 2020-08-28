package com.liferay.sca.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Enumeration;
import java.util.Properties;

public class PropsUtil {

	public static String get(String key) {
		return _instance._get(key);
	}

	public static String get(String key, String defaultValue) {
		return _instance._get(key, defaultValue);
	}

	public static String[] getArray(String key) {
		return _instance._getArray(key);
	}

	public static Properties getProperties() {
		return _instance._getProperties();
	}

	public static Properties getProperties(
		String prefix, boolean removePrefix) {

		return _instance._getProperties(prefix, removePrefix);
	}

	private PropsUtil() {
		_properties = new Properties();

		try {

			// config.properties

			_properties.load(new FileInputStream(new File(CONFIG_FILENAME)));

			// config-ext.properties

			File configExtFile = new File(CONFIG_EXT_FILENAME);

			if (configExtFile.exists()) {
				_properties.load(new FileInputStream(configExtFile));
			}

			// Additional properties

			String[] additionalProperties = _getArray("include-and-override");

			if (additionalProperties != null) {
				for (String additionalProperty : additionalProperties) {
					File additionalPropertyFile = new File(additionalProperty);

					if (additionalPropertyFile.exists()) {
						_properties.load(
							new FileInputStream(additionalPropertyFile));
					}
				}
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		_replaceVariables();
	}

	private String _get(String key) {
		return _properties.getProperty(key);
	}

	private String _get(String key, String defaultValue) {
		return _properties.getProperty(key, defaultValue);
	}

	private String[] _getArray(String key) {
		String value = _properties.getProperty(key);

		if (value == null) {
			return null;
		}

		String[] values =  value.split(",");

		for (int i = 0; i < values.length; i++) {
			values[i] = values[i].trim();
		}

		return values;
	}

	private Properties _getProperties() {
		return _properties;
	}

	private Properties _getProperties(String prefix, boolean removePrefix) {
		Properties properties = new Properties();

		Enumeration<?> e = _properties.propertyNames();

		while (e.hasMoreElements()) {
			String key = (String)e.nextElement();

			if (key.startsWith(prefix)) {
				String newKey = key;

				if (removePrefix) {
					newKey = key.substring(prefix.length());

					if (newKey.startsWith(".")) {
						newKey = newKey.substring(1);
					}
				}

				properties.put(newKey, _properties.getProperty(key));
			}
		}

		return properties;
	}

	private void _replaceVariables() {
		boolean foundVariable = false;

		Enumeration<?> e = _properties.propertyNames();

		while (e.hasMoreElements()) {
			String key = (String)e.nextElement();
			String value = _properties.getProperty(key);

			int x = value.indexOf("${");
			int y = value.indexOf("}");

			if (x >= 0) {
				foundVariable = true;

				String variable = value.substring(x+2, y);

				String newValue = value.substring(0, x) + 
					_properties.getProperty(variable) + value.substring(y+1);

				_properties.setProperty(key, newValue);
			}
		}

		if (foundVariable) {
			_replaceVariables();
		}
	}

	private static final String CONFIG_FILENAME = "config.properties";
	private static final String CONFIG_EXT_FILENAME = "config-ext.properties";

	private static PropsUtil _instance = new PropsUtil();

	private Properties _properties;

}