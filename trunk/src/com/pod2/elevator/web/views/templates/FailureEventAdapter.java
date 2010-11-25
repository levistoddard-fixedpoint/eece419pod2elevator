package com.pod2.elevator.web.views.templates;

import com.pod2.elevator.data.TemplateFailureEvent;
import com.vaadin.ui.Button;

public class FailureEventAdapter extends TemplateFailureEvent {

	private Button deleteButton;

	public FailureEventAdapter(TemplateFailureEvent failureEvent) {
		setQuantum(failureEvent.getQuantum());
		setElevatorNumber(failureEvent.getElevatorNumber());
		setComponent(failureEvent.getComponent());
	}

	public void setDeleteButton(Button deleteButton) {
		this.deleteButton = deleteButton;
	}

	public Button getDeleteButton() {
		return deleteButton;
	}

	public static String[] getEditableFields() {
		return new String[] { "quantum", "elevatorNumber", "component" };
	}

	public static Object[] getVisibleColumns() {
		return new Object[] { "quantum", "elevatorNumber", "component", "deleteButton" };
	}

	public static String[] getColumnHeaders() {
		return new String[] { "Quantum", "Elevator Number", "Component", "Delete" };
	}

}
