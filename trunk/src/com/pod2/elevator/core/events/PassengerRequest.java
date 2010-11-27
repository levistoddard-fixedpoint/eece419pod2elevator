package com.pod2.elevator.core.events;

import com.pod2.elevator.core.ActiveSimulation;

public class PassengerRequest extends Event {
	/**
	 *  OVERVIEW: An Event which inserts a passenger request into the ActiveSimulation.
	 */

	private final int onloadFloor;
	private final int offloadFloor;
	private final long timeConstraint;

	PassengerRequest(EventSource source, long quantum, int onloadFloor,
			int offloadFloor, long timeConstraint) {
		/**
		 *  REQUIRES: source != null && quantum >= 0 && onloadFloor >= 0 &&
		 *  	offloadFloor >= 0 && timeConstraint >= 0
		 *  MODIFIES: this
		 *  EFFECTS: Constructor for the class.  Initiate all variables used in this class.
		 */
		
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
		/**
		 * 	REQUIRES: simulation != null
		 * 	MODIFIES: this
		 * 	EFFECTS: Apply passenger request event to simulation.
		 */
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
		/**
		 * 	EFFECTS: Returns a string with the information of the passenger request.
		 */
		final String format = "%s: Passenger entered floor %d. Wants to go to floor %d within %d quantum.";
		return String.format(format, super.toString(), onloadFloor,
				offloadFloor, timeConstraint);
	}

}
