package com.pod2.elevator.core.events;

public abstract class ElevatorEvent extends Event {
	/**
	 * OVERVIEW: An Event which changes the state of a particular Elevator within an
	 * 		ActiveSimulation.
	 */
	
	private final int elevatorNumber;

	ElevatorEvent(EventSource source, long quantum, int elevatorNumber) {
		/**
		 * 	REQUIRES: source != null && quantum >= 0 && elevatorNumber >= 0
		 * 	MODIFIES: this
		 * 	EFFECTS: Constructor for the class.  Set the elevator number.
		 */
		super(source, quantum);
		assert (elevatorNumber >= 0);
		this.elevatorNumber = elevatorNumber;
	}

	public int getElevatorNumber() {
		/**
		 * 	EFFECTS: Return integer representation of the elevator number.
		 */
		return elevatorNumber;
	}

	@Override
	public String toString() {
		/**
		 * 	EFFECTS: Return a string with the elevator Number.
		 */
		return String.format("%s: Elevator %d", super.toString(), elevatorNumber);
	}

}
