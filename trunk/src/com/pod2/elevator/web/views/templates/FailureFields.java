package com.pod2.elevator.web.views.templates;

public enum FailureFields {

	Quantum("Time"), Elevator("Elevator"), Component("Component"), Delete(
			"Delete");

	private String title;

	private FailureFields(String title) {
		this.title = title;
	}

	public String toString() {
		return title;
	}

}
