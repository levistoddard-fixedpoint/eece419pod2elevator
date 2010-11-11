package com.pod2.elevator.scheduling;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.Elevator;
import com.pod2.elevator.core.FloorRequestButton;
import com.pod2.elevator.core.MotionStatus;
import com.pod2.elevator.core.ServiceStatus;

public class SimpleScheduler implements ElevatorScheduler {

	int nextFloor = 1;

	@Override
	public String getKey() {
		return SimpleScheduler.class.getName();
	}

	@Override
	public String getName() {
		return "A VERY simple scheduler";
	}

	@Override
	public String getDescription() {
		return "To be used for initial testing";
	}

	private Elevator getNextElevator(ActiveSimulation simulation) {
		for (Elevator elevator : simulation.getElevators()) {
			if (elevator.getServiceStatus().equals(ServiceStatus.InService)
					&& elevator.getMotionStatus()
							.equals(MotionStatus.DoorsOpen)) {
				return elevator;
			}
		}
		return null;
	}

	private SimpleSchedulerData getData(Elevator elevator) {
		return (SimpleSchedulerData) elevator.getSchedulerData();
	}

	private static class SimpleSchedulerData extends SchedulerData {
		public int toFloor;
	}

	@Override
	public void schedule(ActiveSimulation simulation) {

		FloorRequestButton[] buttons = simulation.getRequestButtons();
		for (int n = 0; n < buttons.length; n++) {
			FloorRequestButton button = buttons[n];
			if (button.isUpSelected() || button.isDownSelected()) {
				Elevator elevator = getNextElevator(simulation);
				if (elevator == null)
					continue;
				SimpleSchedulerData data = getData(elevator);
				if (data == null) {
					data = new SimpleSchedulerData();
					elevator.setSchedulerData(data);
				}
				data.toFloor = n;
				elevator.closeDoors();
			}
		}

		for (Elevator elevator : simulation.getElevators()) {
			if (!elevator.getServiceStatus().equals(ServiceStatus.InService))
				continue;
			if (elevator.getMotionStatus().equals(MotionStatus.DoorsClosed)) {
				if (!elevator.getRequestPanel().areRequests()) {
					elevator.moveToFloor(getData(elevator).toFloor);
				} else {
					int toFloor = elevator.getRequestPanel()
							.getRequestedFloors().iterator().next();
					elevator.moveToFloor(toFloor);
				}
			} else if (elevator.getMotionStatus().equals(
					MotionStatus.ReachedDestinationFloor)) {
				elevator.openDoors();
			}
		}

	}

}
