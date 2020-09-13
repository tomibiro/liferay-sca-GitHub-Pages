package com.liferay.sca.comparator;

import com.liferay.sca.model.npm.NpmDependency;

import java.util.Comparator;

public class NpmDependencyComparator implements Comparator<NpmDependency>{

	public int compare(
		NpmDependency dependency1, NpmDependency dependency2) {

		String package1 = dependency1.getPackage();
		String package2 = dependency2.getPackage();

		String version1 = dependency1.getVersion();
		String version2 = dependency2.getVersion();

		int x = package1.compareTo(package2);

		if (x != 0) {
			return x;
		}

		return version1.compareTo(version2);
	}

}