package com.liferay.sca.model.maven;

import com.liferay.sca.model.Dependency;

public class MavenDependency extends Dependency {

	public MavenDependency(String group, String artifact, String version) {
		_group = group;
		_artifact = artifact;
		_version = version;
	}

	public boolean equals(MavenDependency d2) {
		if (!_group.equals(d2.getGroup())) {
			return false;
		}

		if (!_artifact.equals(d2.getArtifact())) {
			return false;
		}

		if (!_version.equals(d2.getVersion())) {
			return false;
		}

		return true;
	}

	public String getArtifact() {
		return _artifact;
	}

	public String getGroup() {
		return _group;
	}

	public String getVersion() {
		return _version;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(getGroup());
		sb.append(":");
		sb.append(getArtifact());
		sb.append(":");
		sb.append(getVersion());

		return sb.toString();
	}

	private String _artifact;
	private String _group;
	private String _version;
}