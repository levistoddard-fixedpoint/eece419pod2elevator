package com.pod2.elevator.data;

public class TemplateServiceEvent extends TemplateElevatorEvent {

	private boolean putInService;

	public boolean isPutInService() {
		return putInService;
	}

	public void setPutInService(boolean putInService) {
		this.putInService = putInService;
	}

	@Override
	public Object[] getFields() {
		return new Object[] { "quantum", "elevatorNumber", "putInService" };
	}

	@Override
	public Object[] getFieldValues() {
		return new Object[] { getQuantum(), getElevatorNumber(), isPutInService() };
	}

}
