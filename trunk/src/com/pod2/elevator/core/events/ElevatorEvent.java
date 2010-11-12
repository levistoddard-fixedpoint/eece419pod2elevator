package com.pod2.elevator.core.events;

/**
 * An Event which changes the state of a particular Elevator within an
 * ActiveSimulation.
 * 
 */
public abstract class ElevatorEvent extends Event {

	private final int elevatorNumber;

	ElevatorEvent(EventSource source, long quantum, int elevatorNumber) {
		super(source, quantum);
		assert (elevatorNumber >= 0);
		this.elevatorNumber = elevatorNumber;
	}

	public int getElevatorNumber() {
		return elevatorNumber;
	}

	@Override
	public String toString() {
		return String.format("%s: Elevator %d", super.toString(), elevatorNumber);
	}

}
