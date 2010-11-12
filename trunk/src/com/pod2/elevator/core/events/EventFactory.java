package com.pod2.elevator.core.events;

public class EventFactory {

	public static Event createPassengerRequest(EventSource eventSource,
			long timeQuantum, int onloadFloor, int offloadFloor,
			long timeConstraint) {
		return new PassengerRequest(eventSource, timeQuantum, onloadFloor,
				offloadFloor, timeConstraint);
	}

	public static Event createComponentFailureEvent(EventSource eventSource,
			long timeQuantum, int elevatorNumber, String componentKey) {
		return new ComponentFailure(eventSource, timeQuantum, elevatorNumber,
				componentKey);
	}

	public static Event createServiceEvent(EventSource eventSource,
			long timeQuantum, int elevatorNumber, boolean putInService) {
		if (putInService) {
			return new PutInService(eventSource, timeQuantum, elevatorNumber);
		} else {
			return new PutOutOfService(eventSource, timeQuantum, elevatorNumber);
		}
	}

}
