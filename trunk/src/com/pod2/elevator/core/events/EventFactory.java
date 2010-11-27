package com.pod2.elevator.core.events;

import com.pod2.elevator.core.component.ComponentDetails;

public class EventFactory {
	/**
	 * OVERVIEW: Provides a set of procedures to create Events which can be applied to an
	 * ActiveSimulation.
	 */
	
	public static Event createComponentFailureEvent(EventSource source,
			long quantum, int elevatorNumber, ComponentDetails details) {
		/**
		 * 	REQUIRES: source != null && quantum >= 0 && elevatorNumber >= 0 &&
		 * 		details != null
		 * 	EFFECTS: Return an event which causes a component to fail.
		 */
		return new ComponentFailure(source, quantum, elevatorNumber, details);
	}

	public static Event createPassengerRequest(EventSource source,
			long quantum, int onloadFloor, int offloadFloor, long timeConstraint) {
		/**
		 * 	REQUIRES: source != null && quantum >= 0 && onloadFloor >= 0 &&
		 * 		offloadFloor >= 0 && timeConstraint >= 0
		 * 	EFFECTS: Return an event which creates a passenger request.
		 */
		return new PassengerRequest(source, quantum, onloadFloor, offloadFloor,
				timeConstraint);
	}

	public static Event createServiceEvent(EventSource source, long quantum,
			int elevatorNumber, boolean putInService, String reason) {
		/**
		 * 	REQUIRES: source != null && quantum >= 0 && elevatorNumber >= 0 &&
		 * 		putInService != null && reason != null
		 * 	EFFECTS: Return an event which changes the service status of the elevator.
		 */
		if (putInService) {
			return new PutInService(source, quantum, elevatorNumber);
		} else {
			return new PutOutOfService(source, quantum, elevatorNumber, reason);
		}
	}

	public static Event createServiceEvent(EventSource source, long quantum,
			int elevatorNumber, boolean putInService) {
		/**
		 * 	REQUIRES: source != null && quantum >= 0 && elevatorNumber >= 0 &&
		 * 		putInService != null
		 * 	EFFECTS: Return an event which changes the service status of the elevator.
		 */
		return createServiceEvent(source, quantum, elevatorNumber,
				putInService, "");
	}

}
