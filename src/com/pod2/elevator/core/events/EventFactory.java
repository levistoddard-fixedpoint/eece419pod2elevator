package com.pod2.elevator.core.events;

import com.pod2.elevator.core.component.ElevatorComponent;

public class EventFactory {

	public static Event createPassengerRequest(EventSource eventSource,
			long timeQuantum, int onloadFloor, int offloadFloor,
			long timeConstraint) {
		return new PassengerRequest(eventSource, timeQuantum, onloadFloor,
				offloadFloor, timeConstraint);
	}

	public static Event createComponentFailureEvent(EventSource eventSource,
			long timeQuantum, int elevatorNumber,
			Class<? extends ElevatorComponent> component) {
		return new ComponentFailure(eventSource, timeQuantum, elevatorNumber,
				component);
	}

	public static Event createServiceEvent(EventSource eventSource,
			long timeQuantum, int elevatorNumber, boolean shouldPutOutOfService) {
		if (shouldPutOutOfService) {
			return new PutOutOfService(eventSource, timeQuantum, elevatorNumber);
		} else {
			return new PutInService(eventSource, timeQuantum, elevatorNumber);
		}
	}

}
