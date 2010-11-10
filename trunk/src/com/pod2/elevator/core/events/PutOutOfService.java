package com.pod2.elevator.core.events;

import com.pod2.elevator.core.ActiveSimulation;

public class PutOutOfService extends ElevatorEvent {

	public PutOutOfService(EventSource eventSource, long timeQuantum,
			int elevatorNumber) {
		super(eventSource, timeQuantum, elevatorNumber);
	}

	@Override
	public void apply(ActiveSimulation activeSimulation) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canApplyNow(ActiveSimulation activeSimulation) {
		return true;
	}

}
