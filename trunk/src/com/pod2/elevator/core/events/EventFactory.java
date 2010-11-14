package com.pod2.elevator.core.events;

import com.pod2.elevator.core.component.ComponentDetails;

/**
 * Provides a set of procedures to create Events which can be applied to an
 * ActiveSimulation.
 * 
 */
public class EventFactory {

	public static Event createComponentFailureEvent(EventSource source,
			long quantum, int elevatorNumber, ComponentDetails details) {
		return new ComponentFailure(source, quantum, elevatorNumber, details);
	}

	public static Event createPassengerRequest(EventSource source,
			long quantum, int onloadFloor, int offloadFloor, long timeConstraint) {
		return new PassengerRequest(source, quantum, onloadFloor, offloadFloor,
				timeConstraint);
	}

	public static Event createServiceEvent(EventSource source, long quantum,
			int elevatorNumber, boolean putInService, String reason) {
		if (putInService) {
			return new PutInService(source, quantum, elevatorNumber);
		} else {
			return new PutOutOfService(source, quantum, elevatorNumber, reason);
		}
	}

	public static Event createServiceEvent(EventSource source, long quantum,
			int elevatorNumber, boolean putInService) {
		return createServiceEvent(source, quantum, elevatorNumber,
				putInService, "");
	}

}
