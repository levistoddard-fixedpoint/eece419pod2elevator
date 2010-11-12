package com.pod2.elevator.core.events;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.Elevator;
import com.pod2.elevator.core.MotionStatus;

public class PutOutOfService extends ElevatorEvent {

	public PutOutOfService(EventSource eventSource, long timeQuantum,
			int elevatorNumber) {
		super(eventSource, timeQuantum, elevatorNumber);
	}

	@Override
	public void apply(ActiveSimulation simulation) {
		Elevator elevator = simulation.getElevators()[getElevatorNumber()];
		elevator.putOutOfService();
	}

	@Override
	public boolean canApplyNow(ActiveSimulation simulation) {
		Elevator elevator = simulation.getElevators()[getElevatorNumber()];
		return MotionStatus.DoorsOpen.equals(elevator.getMotionStatus())
				&& !elevator.getRequestPanel().areRequests();
	}

	@Override
	public String toString() {
		return String.format("%s: Put out of service.", super.toString());
	}

}
