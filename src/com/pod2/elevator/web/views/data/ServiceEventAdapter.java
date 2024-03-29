package com.pod2.elevator.web.views.data;

import com.pod2.elevator.data.TemplateServiceEvent;
import com.vaadin.ui.Button;

/**
 * OVERVIEW: A TemplateServiceEvent which is augmented with information for
 * displaying the event in a table or form.
 * 
 */
public class ServiceEventAdapter extends TemplateServiceEvent {

	private Button deleteButton;

	public ServiceEventAdapter(TemplateServiceEvent serviceEvent) {
		setQuantum(serviceEvent.getQuantum());
		setElevatorNumber(serviceEvent.getElevatorNumber());
		setPutInService(serviceEvent.isPutInService());
	}

	public void setDeleteButton(Button deleteButton) {
		this.deleteButton = deleteButton;
	}

	public Button getDeleteButton() {
		return deleteButton;
	}

	public static String[] getEditableFields() {
		return new String[] { "quantum", "elevatorNumber", "putInService" };
	}

	public static Object[] getVisibleColumns() {
		return new Object[] { "quantum", "elevatorNumber", "putInService", "deleteButton" };
	}

	public static String[] getColumnHeaders() {
		return new String[] { "Quantum", "Elevator Number", "Request", "Delete" };
	}

}
