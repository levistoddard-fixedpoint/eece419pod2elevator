package com.pod2.elevator.core.events;

public abstract class ElevatorEvent extends Event {

	private int elevatorNumber;

	public ElevatorEvent(EventSource eventSource, long timeQuantum,
			int elevatorNumber) {
		super(eventSource, timeQuantum);
		this.elevatorNumber = elevatorNumber;
	}

	public int getElevatorNumber() {
		return elevatorNumber;
	}

}
