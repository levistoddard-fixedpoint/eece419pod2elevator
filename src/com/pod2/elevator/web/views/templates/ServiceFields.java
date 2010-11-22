package com.pod2.elevator.web.views.templates;

public enum ServiceFields {

	Quantum("Time"), Elevator("Elevator"), Request("Request"), Delete("Delete");

	private String title;

	private ServiceFields(String title) {
		this.title = title;
	}

	public String toString() {
		return title;
	}

}
