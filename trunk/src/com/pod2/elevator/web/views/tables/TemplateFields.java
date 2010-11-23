package com.pod2.elevator.web.views.tables;

public enum TemplateFields {

	Name("Name"), CreatedDate("Created Date"), EditDate("Last Edit Date"), Edit(
			"Edit"), Delete("Delete");

	private String title;

	private TemplateFields(String title) {
		this.title = title;
	}

	public String toString() {
		return title;
	}

}
