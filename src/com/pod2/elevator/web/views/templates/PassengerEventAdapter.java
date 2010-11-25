package com.pod2.elevator.web.views.templates;

import com.pod2.elevator.data.TemplatePassengerRequest;
import com.vaadin.ui.Button;

public class PassengerEventAdapter extends TemplatePassengerRequest {

	private Button deleteButton;

	public PassengerEventAdapter(TemplatePassengerRequest passengerEvent) {
		setQuantum(passengerEvent.getQuantum());
		setOnloadFloor(passengerEvent.getOnloadFloor());
		setOffloadFloor(passengerEvent.getOffloadFloor());
		setTimeConstraint(passengerEvent.getTimeConstraint());
	}

	public void setDeleteButton(Button deleteButton) {
		this.deleteButton = deleteButton;
	}

	public Button getDeleteButton() {
		return deleteButton;
	}

	
	public static String[] getEditableFields() {
		return new String[]{"quantum", "onloadFloor", "offloadFloor", "timeConstraint"};
	}
	
	public static Object[] getVisibleColumns() {
		return new Object[] { "quantum", "onloadFloor", "offloadFloor", "timeConstraint",
				"deleteButton" };
	}

	public static String[] getColumnHeaders() {
		return new String[] { "Quantum", "Onload Floor", "Offload Floor", "Time Constaint",
				"Delete" };
	}

}
