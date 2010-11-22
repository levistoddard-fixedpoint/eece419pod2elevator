package com.pod2.elevator.web.views.templates;

public enum PassengerFields {
	Quantum("Time"), OnloadFloor("Onload Floor"), OffloadFloor("Offload Floor"), TimeConstraint(
			"Time Constraint"), Delete("Delete");

	private String title;

	private PassengerFields(String title) {
		this.title = title;
	}

	public String toString() {
		return title;
	}
}
