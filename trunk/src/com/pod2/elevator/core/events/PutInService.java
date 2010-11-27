package com.pod2.elevator.core.events;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.Elevator;

public class PutInService extends ElevatorEvent {
	/**
	 *  OVERVIEW: An ElevatorEvent which puts an elevator back into service.
	 */
	
	PutInService(EventSource source, long quantum, int elevatorNumber) {
		/**
		 * 	REQUIRES: source != null && quantum >= 0 && elevatorNumber >= 0
		 * 	MODIFIES: this
		 * 	EFFECTS: Constructor of the class.  Initiate variable for use in this class.
		 */
		super(source, quantum, elevatorNumber);
	}

	@Override
	public void apply(ActiveSimulation simulation) {
		/**
		 * 	REQUIRES: simulation != null
		 * 	MODIFIES: simulation
		 * 	EFFECTS: Apply service event to elevator into service.
		 */
		Elevator elevator = simulation.getElevators()[getElevatorNumber()];
		elevator.putInService();
	}

	@Override
	public String toString() {
		/**
		 * 	EFFECTS: Return a string with service event information
		 */
		return String.format("%s put in service.", super.toString());
	}

}
