package com.pod2.elevator.scheduling;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.Elevator;
import com.pod2.elevator.core.MotionStatus;
import com.pod2.elevator.core.ServiceStatus;

/**
 * An ElevatorScheduler which makes each elevator continuously travel from the
 * bottom to the top floor, then back to the bottom floor again.
 * 
 */
public class SabathScheduler implements ElevatorScheduler {

	int nextFloor = 1;

	@Override
	public String getKey() {
		return SabathScheduler.class.getName();
	}

	@Override
	public String getName() {
		return "Sabath Scheduler";
	}

	@Override
	public String getDescription() {
		return "Each elevator moves continuously from the bottom "
				+ "floor to the top floor, then back to the bottom floor.";
	}

	private SabathData getData(Elevator elevator) {
		return (SabathData) elevator.getSchedulerData();
	}

	private static class SabathData extends SchedulerData {
		public boolean goingUp;
	}

	@Override
	public void schedule(ActiveSimulation simulation) {

		for (Elevator elevator : simulation.getElevators()) {
			if (!elevator.getServiceStatus().equals(ServiceStatus.InService))
				continue;
			SabathData data = getData(elevator);
			if (!(data instanceof SabathData)) {
				data = new SabathData();
				data.goingUp = true;
			}
			elevator.setSchedulerData(data);

			if (MotionStatus.DoorsOpen.equals(elevator.getMotionStatus())) {
				elevator.closeDoors();
			} else if (MotionStatus.ReachedDestinationFloor.equals(elevator
					.getMotionStatus())) {
				elevator.openDoors();
			} else if (MotionStatus.DoorsClosed.equals(elevator
					.getMotionStatus())) {
				if (data.goingUp) {
					int nextFloor = (int) elevator.getPosition() + 1;
					if (simulation.getNumberFloors() == nextFloor) {
						data.goingUp = false;
						nextFloor -= 2;
					}
					elevator.moveToFloor(nextFloor);
				} else {
					int nextFloor = (int) elevator.getPosition() - 1;
					if (nextFloor < 0) {
						data.goingUp = true;
						nextFloor += 2;
					}
					elevator.moveToFloor(nextFloor);
				}
			}

		}

	}
	
	public String toString() {
		return getName();
	}

}
