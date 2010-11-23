package com.pod2.elevator.data;

public abstract class TemplateElevatorEvent extends TemplateEvent {

	private int elevatorNumber;

	public int getElevatorNumber() {
		return elevatorNumber;
	}

	public void setElevatorNumber(int elevatorNumber) {
		this.elevatorNumber = elevatorNumber;
	}

}
