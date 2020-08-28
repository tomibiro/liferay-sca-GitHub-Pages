package com.liferay.sca.comparator;

import com.liferay.sca.model.Dependency;

import java.util.Comparator;

public class DependencyComparator implements Comparator<Dependency>{

	public int compare(Dependency dependency1, Dependency dependency2) {
		String group1 = dependency1.getGroup();
		String group2 = dependency2.getGroup();

		String artifact1 = dependency1.getArtifact();
		String artifact2 = dependency2.getArtifact();

		String version1 = dependency1.getVersion();
		String version2 = dependency2.getVersion();

		int x = group1.compareTo(group2);

		if (x != 0) {
			return x;
		}

		int y = artifact1.compareTo(artifact2);

		if (y != 0) {
			return y;
		}

		return version1.compareTo(version2);
	}

}