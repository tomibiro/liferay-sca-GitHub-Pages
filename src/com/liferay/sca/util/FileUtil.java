package com.liferay.sca.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

public class FileUtil {

	public static String read(File file) throws IOException {
		return read(file.getAbsolutePath());
	}

	public static String read(String filename) throws IOException {
		StringBuilder sb = new StringBuilder();

		Reader reader = null;

		try {
			reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(filename), UTF8));

			int length = 0;
			char[] buffer = new char[4096];

			while ((length = reader.read(buffer)) != -1) {
				sb.append(buffer, 0, length);
			}
		}
		finally {
			if (reader != null) {
				reader.close();
			}
		}

		return sb.toString();
	}

	public static void write(String content, File file)
		throws IOException {

		File parent = file.getParentFile();

		if (!parent.exists()) {
			parent.mkdirs();
		}

		Writer out = new PrintWriter(file, UTF8);

		try {
			out.write(content);
		}
		finally {
			out.close();
		}
	}

	public static void write(String content, String filename)
		throws IOException {

		write(content, new File(filename));
	}

	protected static String UTF8 = "UTF-8";

}