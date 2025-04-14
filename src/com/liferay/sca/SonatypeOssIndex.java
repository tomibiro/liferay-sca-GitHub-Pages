package com.liferay.sca;

import com.liferay.sca.model.Dependency;
import com.liferay.sca.model.DependencySet;
import com.liferay.sca.model.maven.MavenDependency;
import com.liferay.sca.model.npm.NpmDependency;
import com.liferay.sca.model.sonatype.ossindex.ComponentReports;
import com.liferay.sca.util.Base64Util;
import com.liferay.sca.util.ProjectPropsUtil;
import com.liferay.sca.util.ProjectUtil;
import com.liferay.sca.util.PropsKeys;
import com.liferay.sca.util.PropsValues;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class SonatypeOssIndex {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("No project specified");

			System.exit(1);
		}

		String project = args[0];

		ProjectUtil.validate(project);

		DependencySet dependencySet = DependencyFinder.find(project);

		ComponentReports componentReports = query(dependencySet);

		String folder = ProjectPropsUtil.get(
			project, PropsKeys.DEPENDABOT_PACKAGE_DIR);

		componentReports.save(folder);
	}

	public static void generate(DependencySet dependencySet) throws Exception {
		ComponentReports componentReports = query(dependencySet);

		String folder = ProjectPropsUtil.get(
			dependencySet.getProject(), PropsKeys.DEPENDABOT_PACKAGE_DIR);

		componentReports.save(folder);
	}

	public static ComponentReports query(DependencySet dependencySet) throws Exception {
		ComponentReports componentReports = new ComponentReports();

		query(
			dependencySet.getMavenDependencies(),
			dependencySet.getNpmDependencies(), componentReports);

		return componentReports;
	}

	protected static void query(
			List<Dependency> dependencies,
			ComponentReports componentReports)
		throws Exception {

		StringBuilder sb = new StringBuilder();

		sb.append("{\"coordinates\": [");

		for (Dependency dependency : dependencies) {
			if (dependency instanceof MavenDependency) {
				MavenDependency mavenDependency = (MavenDependency)dependency;

				sb.append("\"pkg:maven/");
				sb.append(mavenDependency.getGroup());
				sb.append("/");
				sb.append(mavenDependency.getArtifact());
				sb.append("@");
				sb.append(mavenDependency.getVersion());
				sb.append("\",");
			}
			else if(dependency instanceof NpmDependency) {
				NpmDependency npmDependency = (NpmDependency)dependency;

				sb.append("\"pkg:npm/");
				sb.append(_encodeNpmPackage(npmDependency.getPackage()));
				sb.append("@");
				sb.append(_encodeNpmVersion(npmDependency.getVersion()));
				sb.append("\",");
			}
		}

		sb.delete(sb.length() - 1, sb.length());

		sb.append("]}");

		query(sb.toString(), componentReports);
	}

	protected static void query(
			Set<MavenDependency> mavenDependencies,
			Set<NpmDependency> npmDependencies,
			ComponentReports componentReports)
		throws Exception {

		List<Dependency> dependencyList = new ArrayList<Dependency>();

		dependencyList.addAll(mavenDependencies);
		dependencyList.addAll(npmDependencies);

		for (int i = 0; i < dependencyList.size(); i = i + _API_MAX_COMPONENTS) {
			int toIndex = Math.min(
				(i + _API_MAX_COMPONENTS), dependencyList.size());

			query(dependencyList.subList(i, toIndex), componentReports);
		}
	}

	protected static void query(
			String requestBody, ComponentReports componentReports)
		throws Exception {

		HttpPost httpPost = _getHttpPost(requestBody);

		CloseableHttpClient httpClient = _getHttpClient();

		CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

		StatusLine statusLine = httpResponse.getStatusLine();

		if (statusLine.getStatusCode() != 200) {
			System.err.println(statusLine.toString() + " : " + requestBody);
		}

		try {
			HttpEntity httpEntity = httpResponse.getEntity();

			String responseBody = EntityUtils.toString(httpEntity);

			componentReports.add(responseBody);

			EntityUtils.consume(httpEntity);
		}
		finally {
			httpResponse.close();
		}
	}

	private static String _encodeNpmPackage(String pkg) {
		return pkg.replaceAll("@", "%40");
	}

	private static String _encodeNpmVersion(String version) {
		if (version.startsWith("~") || version.startsWith("^")) {
			return version.substring(1);
		}

		return version;
	}

	private static String _formatAuth(String username, String apiToken) {
		StringBuilder sb = new StringBuilder();

		sb.append(username);
		sb.append(":");
		sb.append(apiToken);

		return "Basic " + Base64Util.encode(sb.toString());
	}

	private static CloseableHttpClient _getHttpClient() {
		RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();

		requestConfigBuilder.setCookieSpec(CookieSpecs.STANDARD);

		HttpClientBuilder httpClientBuilder = HttpClients.custom();

		httpClientBuilder.setDefaultRequestConfig(requestConfigBuilder.build());

		return httpClientBuilder.build();
	}

	private static HttpPost _getHttpPost(String requestBody) {
		HttpPost httpPost = new HttpPost(_API_ENDPOINT);

		httpPost.addHeader("Accept", "application/json");
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.addHeader(
			"Authorization",
			_formatAuth(
				PropsValues.SONATYPE_OSS_INDEX_USERNAME,
				PropsValues.SONATYPE_OSS_INDEX_API_TOKEN));

		httpPost.setEntity(
			new StringEntity(requestBody, StandardCharsets.UTF_8));

		return httpPost;
	}

	private static final String _API_ENDPOINT =
		"https://ossindex.sonatype.org/api/v3/component-report";

	private static final int _API_MAX_COMPONENTS = 128;

}