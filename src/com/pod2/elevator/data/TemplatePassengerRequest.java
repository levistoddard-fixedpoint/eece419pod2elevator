package com.pod2.elevator.data;

public class TemplatePassengerRequest extends TemplateEvent {

	private int onloadFloor;
	private int offloadFloor;
	private long timeConstraint;

	public int getOnloadFloor() {
		return onloadFloor;
	}

	public void setOnloadFloor(int onloadFloor) {
		this.onloadFloor = onloadFloor;
	}

	public int getOffloadFloor() {
		return offloadFloor;
	}

	public void setOffloadFloor(int offloadFloor) {
		this.offloadFloor = offloadFloor;
	}

	public long getTimeConstraint() {
		return timeConstraint;
	}

	public void setTimeConstraint(long timeConstraint) {
		this.timeConstraint = timeConstraint;
	}

}
