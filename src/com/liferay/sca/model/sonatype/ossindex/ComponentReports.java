package com.liferay.sca.model.sonatype.ossindex;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.liferay.sca.util.FileUtil;

public class ComponentReports {

	public void add(String reportsJsonStr) throws Exception {
		JSONParser parser = new JSONParser();

		JSONArray reportsJsonArray = (JSONArray)parser.parse(
			reportsJsonStr);

		for (Object curObject : reportsJsonArray) {
			JSONObject reportJson = (JSONObject)curObject;

			String coordinates = (String)reportJson.get("coordinates");
			String reference = (String)reportJson.get("reference");
			JSONArray vulnerabilityJsonArray = (JSONArray)reportJson.get(
				"vulnerabilities");

			ComponentReport componentReport = new ComponentReport(
				coordinates, reference, vulnerabilityJsonArray.size());

			_reports.add(componentReport);
		}
	}

	public List<ComponentReport> getReports() {
		return _reports;
	}

	public void save(String path) throws IOException {
		StringBuilder sb = new StringBuilder();

		sb.append("<!DOCTYPE html>\n");
		sb.append("<html>\n");
		sb.append("<head>\n");
		sb.append("<title>Sonatype OSS Index Report</title>\n");
		sb.append("<style type=\"text/css\">\n");
		sb.append("tr:nth-child(even) {background-color: #DDD;}\n");
		sb.append("</style>");
		sb.append("</head>\n");
		sb.append("<body>\n");
		sb.append("<table>\n");
		sb.append("<tr>\n");
		sb.append("\t<th>Coordinates</th>\n");
		sb.append("\t<th>Vulns</th>\n");
		sb.append("\t<th>Ref</th>\n");
		sb.append("</tr>\n");
		

		for (ComponentReport report : getReports()) {
			if (report.getVulnerabilityCount() == 0) {
				continue;
			}

			sb.append("");
			sb.append("<tr>\n");
			sb.append("\t<td>");
			sb.append(report.getCoordinates());
			sb.append("</td>\n");
			sb.append("\t<td>");
			sb.append(report.getVulnerabilityCount());
			sb.append("</td>\n");
			sb.append("\t<td><a href=\"");
			sb.append(report.getReference());
			sb.append("\">URL</a></td>\n");
			sb.append("</tr>\n");
		}

		sb.append("</table>\n");
		sb.append("</body>\n");
		sb.append("</html>\n");

		sb.toString();

		String filename = path + "/sonatype-oss-index-report.html";

		FileUtil.write(sb.toString(), filename);
	}

	List<ComponentReport> _reports = new ArrayList<ComponentReport>();

}