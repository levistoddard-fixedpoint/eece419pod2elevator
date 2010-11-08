package com.pod2.elevator.core;

public class ElevatorEvent extends Event {
	public ElevatorEvent(EventSource eventSource, long timeQuantum,
			int elevatorNumber) {
		super(eventSource, timeQuantum);
		this.elevatorNumber = elevatorNumber;
	}
	
	public void apply(ActiveSimulation activeSimulation) {
		
	}

	private int elevatorNumber;
	
}
