package com.pod2.elevator.core.events;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.Elevator;

/**
 * An ElevatorEvent which puts an elevator back into service.
 * 
 */
public class PutInService extends ElevatorEvent {

	PutInService(EventSource source, long quantum, int elevatorNumber) {
		super(source, quantum, elevatorNumber);
	}

	@Override
	public void apply(ActiveSimulation simulation) {
		Elevator elevator = simulation.getElevators()[getElevatorNumber()];
		elevator.putInService();
	}

	@Override
	public String toString() {
		return String.format("%s put in service.", super.toString());
	}

}
