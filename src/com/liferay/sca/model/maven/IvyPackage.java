package com.liferay.sca.model.maven;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IvyPackage extends Package {

	public IvyPackage(File file) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = factory.newDocumentBuilder();

		Document document = builder.parse(file);

		NodeList nodeList = document.getElementsByTagName("dependency");

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element element = (Element)node;

			if (element.hasAttribute("name") &&
				element.hasAttribute("org") &&
				element.hasAttribute("rev")) {

				addDependency(
					element.getAttribute("org"), element.getAttribute("name"),
					element.getAttribute("rev"));
			}
		}
	}

}