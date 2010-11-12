package com.pod2.elevator.core.events;

import com.pod2.elevator.core.ActiveSimulation;

/**
 * An Event which inserts a passenger request into the ActiveSimulation.
 * 
 */
public class PassengerRequest extends Event {

	private final int onloadFloor;
	private final int offloadFloor;
	private final long timeConstraint;

	PassengerRequest(EventSource source, long quantum, int onloadFloor,
			int offloadFloor, long timeConstraint) {
		super(source, quantum);
		assert (onloadFloor >= 0);
		assert (offloadFloor >= 0);
		assert (timeConstraint >= 0);
		this.onloadFloor = onloadFloor;
		this.offloadFloor = offloadFloor;
		this.timeConstraint = timeConstraint;
	}

	@Override
	public void apply(ActiveSimulation simulation) {
		simulation.enqueuePassenger(this);
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

	@Override
	public String toString() {
		final String format = "%s: Passenger entered floor %d. Wants to go to floor %d within %d quantum.";
		return String.format(format, super.toString(), onloadFloor,
				offloadFloor, timeConstraint);
	}

}
