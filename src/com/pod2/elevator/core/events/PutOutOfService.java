package com.pod2.elevator.core.events;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.Elevator;
import com.pod2.elevator.core.MotionStatus;

/**
 * An ElevatorEvent which takes an Elevator out of service.
 * 
 */
public class PutOutOfService extends ElevatorEvent {

	PutOutOfService(EventSource source, long quantum, int elevatorNumber) {
		super(source, quantum, elevatorNumber);
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
