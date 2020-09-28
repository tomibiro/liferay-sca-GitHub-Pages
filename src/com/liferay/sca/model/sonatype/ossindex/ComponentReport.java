package com.liferay.sca.model.sonatype.ossindex;

public class ComponentReport {

	public ComponentReport(String coordinates, String reference, int vulnerabilityCount) {
		_coordinates = coordinates;
		_reference = reference;
		_vulnerabilityCount = vulnerabilityCount;
	}

	public String getCoordinates() {
		return _coordinates;
	}

	public String getReference() {
		return _reference;
	}

	public int getVulnerabilityCount() {
		return _vulnerabilityCount;
	}

	private String _coordinates;
	private String _reference;
	private int _vulnerabilityCount = 0;

}