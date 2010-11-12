package com.pod2.elevator.core.events;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.Elevator;

public class PutInService extends ElevatorEvent {

	public PutInService(EventSource eventSource, long timeQuantum,
			int elevatorNumber) {
		super(eventSource, timeQuantum, elevatorNumber);
	}

	@Override
	public void apply(ActiveSimulation simulation) {
		Elevator elevator = simulation.getElevators()[getElevatorNumber()];
		elevator.putInService();
	}

	@Override
	public boolean canApplyNow(ActiveSimulation simulation) {
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("%s: Put in service.", super.toString());
	}

}
