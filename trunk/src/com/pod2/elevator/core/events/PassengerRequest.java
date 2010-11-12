package com.pod2.elevator.core.events;

import com.pod2.elevator.core.ActiveSimulation;

public class PassengerRequest extends Event {

	private int onloadFloor;
	private int offloadFloor;
	private long timeConstraint;

	public PassengerRequest(EventSource eventSource, long timeQuantum,
			int onloadFloor, int offloadFloor, long timeConstraint) {
		super(eventSource, timeQuantum);
		this.onloadFloor = onloadFloor;
		this.offloadFloor = offloadFloor;
		this.timeConstraint = timeConstraint;
	}

	public PassengerRequest(PassengerRequest otherRequest) {
		this(otherRequest.getEventSource(), otherRequest.getTimeQuantum(),
				otherRequest.onloadFloor, otherRequest.offloadFloor,
				otherRequest.timeConstraint);
	}

	@Override
	public void apply(ActiveSimulation simulation) {
		simulation.enqueuePassenger(this);
	}

	@Override
	public boolean canApplyNow(ActiveSimulation simulation) {
		return true;
	}
	
	@Override
	public boolean isLoggable() {
		return false;
	}

	public int getOnloadFloor() {
		return onloadFloor;
	}

	public int getOffloadFloor() {
		return offloadFloor;
	}

	public long getTimeConstraint() {
		return timeConstraint;
	}

}
