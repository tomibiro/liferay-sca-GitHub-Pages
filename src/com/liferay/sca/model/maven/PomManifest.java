package com.liferay.sca.model.maven;

import com.liferay.sca.exception.BlankXmlException;
import com.liferay.sca.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PomManifest extends MavenManifest {

	public PomManifest(String project, File file) throws Exception {
		if (_isBlank(file)) {
			throw new BlankXmlException(file.getPath() + " is blank");
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = factory.newDocumentBuilder();

		Document document = builder.parse(file);

		NodeList dependencyNodeList = document.getElementsByTagName(
			"dependency");

		for (int i = 0; i < dependencyNodeList.getLength(); i++) {
			Element dependencyElement = (Element)dependencyNodeList.item(i);

			NodeList groupIdNodeList = dependencyElement.getElementsByTagName(
				"groupId");
			NodeList artifactIdNodeList =
				dependencyElement.getElementsByTagName("artifactId");
			NodeList versionNodeList = dependencyElement.getElementsByTagName(
				"version");

			Element groupIdElement = (Element)groupIdNodeList.item(0);
			Element artifactIdElement = (Element)artifactIdNodeList.item(0);
			Element versionElement = (Element)versionNodeList.item(0);

			if ((groupIdElement != null) && (artifactIdElement != null) &&
				(versionElement != null)) {

				String groupId = _replaceVariables(
					project, groupIdElement.getTextContent());
				String artifactId = _replaceVariables(
					project, artifactIdElement.getTextContent());
				String version = _replaceVariables(
					project, versionElement.getTextContent());

				addDependency(groupId, artifactId, version);
			}
		}
	}

	private boolean _isBlank(File file) throws IOException {
		String content = FileUtil.read(file);

		if (content.trim().equals("")) {
			return true;
		}

		return false;
	}

	private String _replaceVariables(String project, String str)
		throws IOException {

		Map<String,String> projectTemplateTokens = getProjectTemplateTokens(
			project);

		for (String key : projectTemplateTokens.keySet()) {
			str = str.replaceAll(
				"@" + key + "@", projectTemplateTokens.get(key));
		}

		return str;
	}
}
