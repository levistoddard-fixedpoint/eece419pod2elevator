package com.pod2.elevator.core;

public class PassengerRequest extends Event {
	public PassengerRequest(EventSource eventSource, long timeQuantum, int onloadFloor,
			int offloadFloor, long timeConstraint) {
		super(eventSource, timeQuantum);
		this.onloadFloor = onloadFloor;
		this.offloadFloor = offloadFloor;
		this.timeConstraint = timeConstraint;
	}
	private int onloadFloor;
	private int offloadFloor;
	private long timeConstraint;
	
	
}
