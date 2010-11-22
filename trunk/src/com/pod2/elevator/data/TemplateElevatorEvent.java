package com.pod2.elevator.data;

abstract class TemplateElevatorEvent extends TemplateEvent {

	private int elevatorNumber;

	public int getElevatorNumber() {
		return elevatorNumber;
	}

	public void setElevatorNumber(int elevatorNumber) {
		this.elevatorNumber = elevatorNumber;
	}

}
