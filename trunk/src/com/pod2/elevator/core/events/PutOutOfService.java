package com.pod2.elevator.core.events;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.Elevator;
import com.pod2.elevator.core.MotionStatus;

public class PutOutOfService extends ElevatorEvent {
	/**
	 * OVERVIEW: An ElevatorEvent which takes an Elevator out of service.
	 */
	
	private final String reason;

	PutOutOfService(EventSource source, long quantum, int elevatorNumber,
			String reason) {
		/**
		 * 	REQUIRES: source != null && quantum >= 0 && elevatorNumber >= 0 &&
		 * 		reason != null
		 * 	MODIFIES: this
		 * 	EFFECTS: Constructor for the class.  Initialize variables for use in
		 * 		this class.
		 */
		super(source, quantum, elevatorNumber);
		assert (reason != null);
		this.reason = reason;
	}

	@Override
	public void apply(ActiveSimulation simulation) {
		/**
		 * 	REQUIRES: simulation != null
		 * 	MODIFIES: simulation
		 * 	EFFECTS: Apply service event to elevator out of service.
		 */
		Elevator elevator = simulation.getElevators()[getElevatorNumber()];
		elevator.putOutOfService();
	}

	@Override
	public boolean canApplyNow(ActiveSimulation simulation) {
		/**
		 * 	EFFECTS: Return true for ability to apply events.  Returns false otherwise.
		 */
		Elevator elevator = simulation.getElevators()[getElevatorNumber()];
		boolean isOpen = MotionStatus.DoorsOpen == elevator.getMotionStatus();
		boolean isEmpty = elevator.getRequestPanel().getRequestedFloors()
				.isEmpty();
		return isOpen && isEmpty;
	}

	public String getReason() {
		/**
		 * 	EFFECTS: Return the reason for the out of service event.
		 */
		return reason;
	}

	@Override
	public String toString() {
		/**
		 * 	Return a string with service event information
		 */
		return String.format("%s put out of service. %s", super.toString(),
				reason);
	}

}
