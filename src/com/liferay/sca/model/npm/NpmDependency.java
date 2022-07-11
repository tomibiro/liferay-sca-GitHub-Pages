package com.liferay.sca.model.npm;

import com.liferay.sca.model.Dependency;

public class NpmDependency extends Dependency {

	public NpmDependency(String pkg, String version) {
		_package = pkg;
		_version = version;
	}

	public boolean equals(NpmDependency d2) {
		if (!_package.equals(d2.getPackage())) {
			return false;
		}

		if (!_version.equals(d2.getVersion())) {
			return false;
		}

		return true;
	}

	public String getPackage() {
		return _package;
	}

	public String getVersion() {
		return _version;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(getPackage());
		sb.append(":");
		sb.append(getVersion());

		return sb.toString();
	}

	private String _package;
	private String _version;
}